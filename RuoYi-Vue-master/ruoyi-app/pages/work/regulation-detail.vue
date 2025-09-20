<template>
  <view class="detail-container">
    <!-- 数据加载完成前显示骨架屏或加载动画 -->
    <view v-if="!regulationInfo.regulationId" class="loading-state">
      <uni-load-more status="loading"></uni-load-more>
    </view>

    <!-- 数据加载完成后显示内容 -->
    <template v-if="regulationInfo.regulationId">
      <view>
        <!-- 制度基本信息 -->
        <view class="regulation-info">
          <view class="info-header">
            <text class="regulation-name">{{ regulationInfo.regulationName }}</text>
            <view class="regulation-status" :class="[regulationInfo.status === 'draft' ? 'status-draft' : regulationInfo.status === 'active' ? 'status-active' : 'status-expired']">
              {{ getStatusText(regulationInfo.status) }}
            </view>
          </view>
          
          <view class="regulation-meta">
            <view class="meta-item">
              <uni-icons type="list" size="14" color="#8C8C8C" />
              <text>{{ regulationInfo.categoryName || '未知分类' }}</text>
            </view>
            <view class="meta-item">
              <uni-icons type="calendar" size="14" color="#8C8C8C" />
              <text>生效日期：{{ regulationInfo.effectiveDate }}</text>
            </view>
            <view class="meta-item">
              <uni-icons type="refresh" size="14" color="#8C8C8C" />
              <text>更新时间：{{ regulationInfo.updateTime }}</text>
            </view>
            <view class="meta-item">
              <uni-icons type="flag" size="14" color="#8C8C8C" />
              <text>版本：v{{ regulationInfo.version }}</text>
            </view>
          </view>
          
          <view class="important-notice" v-if="regulationInfo.isImportant">
            <uni-icons type="info" size="16" color="#FF4D4F" />
            <text>重要制度文件，请认真阅读并严格执行</text>
          </view>
        </view>

        <!-- 制度摘要 -->
        <view class="summary-section" v-if="regulationInfo.summary">
          <view class="section-header">
            <uni-icons type="compose" size="20" color="#262626" />
            <text class="section-title">制度摘要</text>
          </view>
          <view class="summary-content">
            <text>{{ regulationInfo.summary }}</text>
          </view>
        </view>

        <!-- 制度内容 -->
        <view class="content-section">
          <view class="section-header">
            <uni-icons type="paperplane" size="20" color="#262626" />
            <text class="section-title">制度内容</text>
          </view>
          <view class="content-text">
            <!-- 章节导航 (TOC) -->
            <view class="chapter-nav" v-if="regulationInfo.chapters && regulationInfo.chapters.length > 0">
              <text class="nav-title">目录</text>
              <view 
                class="nav-item" 
                v-for="(chapter, index) in regulationInfo.chapters" 
                :key="index"
                @click="scrollToChapter(index)"
              >
                <text>第{{ index + 1 }}章 {{ chapter.title }}</text>
              </view>
            </view>

            <!-- 制度正文 -->
            <view class="regulation-content" v-if="regulationInfo.chapters">
              <view 
                class="chapter-section" 
                v-for="(chapter, index) in regulationInfo.chapters" 
                :key="index"
                :id="'chapter-' + index"
              >
                <text class="chapter-title">第{{ index + 1 }}章 {{ chapter.title }}</text>
                <view 
                  class="article-section" 
                  v-for="(article, articleIndex) in chapter.articles" 
                  :key="articleIndex"
                >
                  <text class="article-title">第{{ articleIndex + 1 }}条 {{ article.title }}</text>
                  <text class="article-content">{{ article.content }}</text>
                  <view class="article-note" v-if="article.note">
                    <uni-icons type="info" size="14" color="#FA8C16" />
                    <text>{{ article.note }}</text>
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>

        <!-- 修订历史 -->
        <view class="history-section" v-if="regulationInfo.revisionHistory && regulationInfo.revisionHistory.length > 0">
          <view class="section-header">
            <uni-icons type="clock" size="20" color="#262626" />
            <text class="section-title">修订历史</text>
          </view>
          <view class="history-timeline">
            <view 
              class="timeline-item" 
              v-for="(revision, index) in regulationInfo.revisionHistory" 
              :key="index"
            >
              <view class="timeline-dot" :class="{ current: index === 0 }"></view>
              <view class="timeline-content">
                <view class="revision-header">
                  <text class="revision-version">v{{ revision.version }}</text>
                  <text class="revision-date">{{ revision.date }}</text>
                </view>
                <text class="revision-description">{{ revision.description }}</text>
                <text class="revision-author">修订人：{{ revision.author }}</text>
              </view>
            </view>
          </view>
        </view>

        <!-- 相关制度 -->
        <view class="related-section" v-if="relatedRegulations.length > 0">
          <view class="section-header">
            <uni-icons type="link" size="20" color="#262626" />
            <text class="section-title">相关制度</text>
          </view>
          <view class="related-list">
            <view 
              class="related-item" 
              v-for="related in relatedRegulations" 
              :key="related.regulationId"
              @click="viewRelated(related)"
            >
              <uni-icons type="paperplane" size="16" color="#1890FF" />
              <text class="related-name">{{ related.regulationName }}</text>
              <text class="related-version">v{{ related.version }}</text>
            </view>
          </view>
        </view>

        <!-- 统计信息 -->
        <view class="stats-section">
          <view class="section-header">
            <uni-icons type="bar-chart" size="20" color="#262626" />
            <text class="section-title">统计信息</text>
          </view>
          <view class="stats-grid">
            <view class="stat-item">
              <text class="stat-number">{{ regulationInfo.viewCount || 0 }}</text>
              <text class="stat-label">浏览次数</text>
            </view>
            <view class="stat-item">
              <text class="stat-number">{{ regulationInfo.downloadCount || 0 }}</text>
              <text class="stat-label">下载次数</text>
            </view>
            <view class="stat-item">
              <text class="stat-number">{{ regulationInfo.favoriteCount || 0 }}</text>
              <text class="stat-label">收藏次数</text>
            </view>
            <view class="stat-item">
              <text class="stat-number">{{ getFileSize() }}</text>
              <text class="stat-label">文件大小</text>
            </view>
          </view>
        </view>

        <!-- 操作按钮 -->
        <view class="action-buttons">
          <button class="action-btn secondary" @click="favoriteRegulation">
            <uni-icons type="star" size="18" :color="isFavorited ? '#FA8C16' : '#8C8C8C'" />
            <text>{{ isFavorited ? '已收藏' : '收藏' }}</text>
          </button>
          <button class="action-btn secondary" @click="shareRegulation">
            <uni-icons type="redo" size="18" color="#52C41A" />
            <text>分享</text>
          </button>
          <button class="action-btn primary" @click="downloadRegulation">
            <uni-icons type="download" size="18" color="#FFFFFF" />
            <text>下载PDF</text>
          </button>
        </view>

        <!-- 浮动目录按钮 -->
        <view class="floating-catalog" @click="showCatalog" v-if="regulationInfo.chapters && regulationInfo.chapters.length > 1">
          <uni-icons type="list" size="20" color="#FFFFFF" />
        </view>

        <!-- 目录弹窗 -->
        <uni-popup ref="catalogPopup" type="right">
          <view class="catalog-panel">
            <view class="catalog-header">
              <text class="catalog-title">目录</text>
              <view class="close-btn" @click="closeCatalog">
                <uni-icons type="close" size="18" color="#8C8C8C" />
              </view>
            </view>
            <view class="catalog-content">
              <view 
                class="catalog-item" 
                v-for="(chapter, index) in regulationInfo.chapters" 
                :key="index"
                @click="scrollToChapter(index)"
              >
                <text>第{{ index + 1 }}章 {{ chapter.title }}</text>
              </view>
            </view>
          </view>
        </uni-popup>
      </view>
    </template>
  </view>
</template>

<script>
import { getRegulation } from '@/api/regulation.js'

export default {
  data() {
    return {
      regulationId: null,
      regulationInfo: {},
      relatedRegulations: [], // 相关制度暂时保留模拟数据
      isFavorited: false // 收藏状态后续可由接口提供
    }
  },
  onLoad(options) {
    this.regulationId = options.id
    if (this.regulationId) {
      this.loadRegulationDetail()
    } else {
      uni.showToast({ title: '缺少制度ID', icon: 'error' })
    }
    this.loadRelatedRegulations() // 保留模拟数据
  },
  methods: {
    loadRegulationDetail() {
      uni.showLoading({ title: '加载中...' })
      getRegulation(this.regulationId).then(response => {
        let data = response.data;
        // JSON字符串转换为对象
        if (data.chapters && typeof data.chapters === 'string') {
          try { data.chapters = JSON.parse(data.chapters) } catch(e) { data.chapters = [] }
        }
        if (data.revisionHistory && typeof data.revisionHistory === 'string') {
          try { data.revisionHistory = JSON.parse(data.revisionHistory) } catch(e) { data.revisionHistory = [] }
        }
        this.regulationInfo = data
        uni.hideLoading()
      }).catch(() => {
        uni.hideLoading()
        uni.showToast({ title: '数据加载失败', icon: 'error' })
      })
    },
    loadRelatedRegulations() {
      // 模拟相关制度数据
      this.relatedRegulations = [
        { regulationId: 2, regulationName: '业主大会议事规则', version: '1.3' },
        { regulationId: 3, regulationName: '物业服务质量标准', version: '3.0' }
      ]
    },
    getStatusText(status) {
      const textMap = { 'active': '生效中', 'draft': '草案', 'expired': '已失效' };
      return textMap[status] || '未知状态';
    },
    getFileSize() {
        if (!this.regulationInfo.fileSize) return '未知';
        const size = parseFloat(this.regulationInfo.fileSize);
        if (size < 1024) return size + ' B';
        if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' KB';
        return (size / (1024 * 1024)).toFixed(1) + ' MB';
    },
    // UI交互方法
    scrollToChapter(index) {
      const selector = `#chapter-${index}`
      uni.pageScrollTo({
        selector: selector,
        duration: 300
      })
      this.closeCatalog()
    },
    showCatalog() {
      this.$refs.catalogPopup.open()
    },
    closeCatalog() {
      this.$refs.catalogPopup.close()
    },
    favoriteRegulation() {
      this.isFavorited = !this.isFavorited;
      uni.showToast({ title: this.isFavorited ? '已收藏' : '已取消收藏', icon: 'success' });
    },
    shareRegulation() {
      uni.showToast({ title: '分享功能开发中', icon: 'none' });
    },
    downloadRegulation() {
      uni.showToast({ title: '下载功能开发中', icon: 'none' });
    },
    viewRelated(related) {
      uni.navigateTo({ url: `/pages/work/regulation-detail?id=${related.regulationId}` });
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

.detail-container {
  padding-bottom: 160rpx; /* 增加底部内边距以防按钮遮挡 */
}
.loading-state {
  padding-top: 200rpx;
}
.regulation-info, .summary-section, .content-section, .history-section, .related-section, .stats-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
}
.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
}
.section-title {
  margin-left: 12rpx;
  font-size: 28rpx;
  font-weight: 600;
  color: #262626;
}
.info-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
}
.regulation-name {
  flex: 1;
  font-size: 32rpx;
  font-weight: 600;
  color: #262626;
}
.regulation-status {
  padding: 8rpx 16rpx;
  border-radius: 16rpx;
  font-size: 22rpx;
  font-weight: 500;
  white-space: nowrap;
  &.status-active { background: #F6FFED; color: #52C41A; }
  &.status-draft { background: #FFF2E8; color: #FA8C16; }
  &.status-expired { background: #F5F5F5; color: #8C8C8C; }
}
.regulation-meta {
  display: flex;
  align-items: center;
  gap: 24rpx;
  flex-wrap: wrap;
  margin-bottom: 20rpx;
}
.meta-item {
  display: flex;
  align-items: center;
  text {
    margin-left: 8rpx;
    font-size: 22rpx;
    color: #8C8C8C;
  }
}
.important-notice {
  display: flex;
  align-items: center;
  padding: 16rpx 20rpx;
  background: #FFF2F0;
  border-radius: 16rpx;
  border-left: 4rpx solid #FF4D4F;
  text {
    margin-left: 12rpx;
    font-size: 24rpx;
    color: #FF4D4F;
  }
}
.summary-content {
  font-size: 26rpx;
  color: #595959;
  line-height: 1.6;
}
.chapter-nav {
  background: #F8F9FA;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 40rpx;
  border: 1rpx solid #F0F0F0;
}
.nav-title {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #262626;
  margin-bottom: 20rpx;
}
.nav-item {
  padding: 12rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
  &:last-child { border-bottom: none; }
  text { font-size: 24rpx; color: #1890FF; }
}
.regulation-content .chapter-section {
  margin-bottom: 40rpx;
}
.chapter-title {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #262626;
  margin-bottom: 30rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid #1890FF;
}
.article-section {
  margin-bottom: 40rpx;
}
.article-title {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #262626;
  margin-bottom: 16rpx;
}
.article-content {
  font-size: 26rpx;
  color: #595959;
  line-height: 1.8;
  white-space: pre-line;
}
.article-note {
  display: flex;
  align-items: center;
  padding: 12rpx 16rpx;
  background: #FFF2E8;
  border-radius: 12rpx;
  text {
    margin-left: 8rpx;
    font-size: 22rpx;
    color: #FA8C16;
  }
}
.history-timeline .timeline-item {
  position: relative;
  padding-left: 60rpx;
  padding-bottom: 40rpx;
  &:last-child { padding-bottom: 0; }
  &:not(:last-child)::before {
    content: '';
    position: absolute;
    left: 19rpx;
    top: 40rpx;
    bottom: 0;
    width: 2rpx;
    background: #F0F0F0;
  }
}
.timeline-dot {
  position: absolute;
  left: 0;
  top: 0;
  width: 40rpx;
  height: 40rpx;
  background: #F0F0F0;
  border-radius: 50%;
  &.current { background: #1890FF; }
}
.revision-header {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 12rpx;
}
.revision-version {
  font-size: 24rpx;
  font-weight: 600;
  color: #1890FF;
  padding: 4rpx 12rpx;
  background: #E6F7FF;
  border-radius: 12rpx;
}
.revision-date {
  font-size: 22rpx;
  color: #8C8C8C;
}
.revision-description {
  display: block;
  font-size: 26rpx;
  color: #262626;
  line-height: 1.6;
  margin-bottom: 8rpx;
}
.revision-author {
  font-size: 22rpx;
  color: #8C8C8C;
}
.related-list .related-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
  &:last-child { border-bottom: none; }
}
.related-name {
  flex: 1;
  margin-left: 12rpx;
  font-size: 26rpx;
  color: #262626;
}
.related-version {
  font-size: 22rpx;
  color: #52C41A;
  padding: 4rpx 12rpx;
  background: #F6FFED;
  border-radius: 12rpx;
}
.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30rpx;
}
.stat-item {
  text-align: center;
}
.stat-number {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #1890FF;
  margin-bottom: 8rpx;
}
.stat-label {
  font-size: 22rpx;
  color: #8C8C8C;
}
.action-buttons {
  display: flex;
  gap: 16rpx;
  padding: 20rpx;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #FFFFFF;
  padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  border-top: 1rpx solid #F0F0F0;
}
.action-btn {
  flex: 1;
  height: 80rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 20rpx;
  font-size: 26rpx;
  font-weight: 500;
  border: none;
  text { margin-left: 8rpx; }
  &.secondary {
    background: #F8F9FA;
    color: #8C8C8C;
    border: 1rpx solid #F0F0F0;
  }
  &.primary {
    background: #1890FF;
    color: #FFFFFF;
  }
}
.floating-catalog {
  position: fixed;
  bottom: 180rpx; /* 调整位置，避免与操作按钮重叠 */
  right: 40rpx;
  width: 80rpx;
  height: 80rpx;
  background: #1890FF;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(24, 144, 255, 0.3);
  z-index: 999;
}
.catalog-panel {
  width: 500rpx;
  height: 100vh;
  background: #FFFFFF;
}
.catalog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
}
.catalog-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #262626;
}
.close-btn {
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.catalog-content {
  padding: 20rpx;
}
.catalog-item {
  padding: 20rpx;
  border-radius: 16rpx;
  margin-bottom: 8rpx;
  text { font-size: 26rpx; color: #262626; }
}
</style>
