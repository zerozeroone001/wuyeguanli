<template>
  <view class="detail-container">
    <!-- 制度基本信息 -->
    <view class="regulation-info">
      <view class="info-header">
        <text class="regulation-name">{{ regulationInfo.regulationName }}</text>
        <view class="regulation-status" :class="[regulationInfo.status === 'draft' ? 'status-draft' : regulationInfo.status === 'published' ? 'status-published' : regulationInfo.status === 'archived' ? 'status-archived' : 'status-default']">
          {{ getStatusText(regulationInfo.status) }}
        </view>
      </view>
      
      <view class="regulation-meta">
        <view class="meta-item">
          <uni-icons type="list" size="14" color="#8C8C8C" />
          <text>{{ getCategoryText(regulationInfo.category) }}</text>
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
      
      <!-- 重要标识 -->
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
        <view class="content-tools">
          <view class="tool-item" @click="toggleFontSize">
            <uni-icons type="font" size="16" color="#8C8C8C" />
          </view>
          <view class="tool-item" @click="toggleNightMode">
            <uni-icons type="eye" size="16" color="#8C8C8C" />
          </view>
        </view>
      </view>
      
      <view class="content-text" :class="{ 'large-font': isLargeFont, 'night-mode': isNightMode }">
        <!-- 章节导航 -->
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
        <view class="regulation-content">
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
              
              <!-- 条款备注 -->
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
          <text class="stat-number">{{ regulationInfo.viewCount }}</text>
          <text class="stat-label">浏览次数</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ regulationInfo.downloadCount }}</text>
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

<script>
export default {
  data() {
    return {
      regulationId: null,
      regulationInfo: {},
      relatedRegulations: [],
      isLargeFont: false,
      isNightMode: false,
      isFavorited: false
    }
  },
  onLoad(options) {
    this.regulationId = options.id
    this.loadRegulationDetail()
    this.loadRelatedRegulations()
  },
  methods: {
    loadRegulationDetail() {
      // 模拟制度详情数据
      this.regulationInfo = {
        regulationId: 1,
        regulationName: '智慧花园小区物业管理规约',
        category: 'management',
        version: '2.1',
        status: 'active',
        effectiveDate: '2024-01-01',
        updateTime: '2024-01-15 10:30',
        summary: '本规约根据《中华人民共和国物权法》《物业管理条例》等法律法规制定，旨在规范小区物业管理服务标准，明确业主权利义务，建立和谐社区管理秩序，保障业主合法权益。',
        viewCount: 1256,
        downloadCount: 89,
        favoriteCount: 45,
        isImportant: true,
        fileSize: '2.5MB',
        fileType: 'PDF',
        chapters: [
          {
            title: '总则',
            articles: [
              {
                title: '制定依据',
                content: '为了规范物业管理活动，维护业主和物业服务企业的合法权益，改善人民群众的生活和工作环境，制定本规约。',
                note: '本条款为制定依据说明'
              },
              {
                title: '适用范围',
                content: '本规约适用于智慧花园小区全体业主、物业使用人、物业服务企业及相关管理人员。',
                note: null
              }
            ]
          },
          {
            title: '业主权利义务',
            articles: [
              {
                title: '业主权利',
                content: '业主在物业管理活动中，享有下列权利：\n（一）按照物业服务合同的约定，接受物业服务企业提供的服务；\n（二）提议召开业主大会会议，并就物业管理的有关事项提出建议；\n（三）提出制定和修改管理规约、业主大会议事规则的建议；\n（四）参加业主大会会议，行使投票权；\n（五）选举业主委员会成员，并享有被选举权。',
                note: '业主权利受法律保护'
              },
              {
                title: '业主义务',
                content: '业主在物业管理活动中，应当履行下列义务：\n（一）遵守管理规约、业主大会议事规则；\n（二）遵守物业管理区域内物业共用部位和共用设施设备的使用、公共秩序和环境卫生的维护等方面的规章制度；\n（三）执行业主大会的决定和业主大会授权业主委员会作出的决定；\n（四）按照国家有关规定交纳专项维修资金；\n（五）按时交纳物业服务费用。',
                note: '业主应严格履行义务'
              }
            ]
          },
          {
            title: '物业服务',
            articles: [
              {
                title: '服务内容',
                content: '物业服务企业应当按照物业服务合同的约定，提供相应的服务，包括但不限于：\n（一）物业共用部位的维护、保养和管理；\n（二）物业共用设施设备的运行、维护、保养和管理；\n（三）物业管理区域内的清洁卫生；\n（四）物业管理区域内的秩序维护；\n（五）物业管理区域内的绿化养护。',
                note: '服务内容应符合合同约定'
              }
            ]
          }
        ],
        revisionHistory: [
          {
            version: '2.1',
            date: '2024-01-15',
            description: '根据最新法律法规，完善业主权利义务条款，增加物业服务标准细则。',
            author: '业主委员会'
          },
          {
            version: '2.0',
            date: '2023-06-01',
            description: '全面修订管理规约，增加数字化管理相关条款。',
            author: '业主委员会'
          },
          {
            version: '1.0',
            date: '2022-01-01',
            description: '初始版本制定，确立基本管理框架。',
            author: '开发商'
          }
        ]
      }
    },
    
    loadRelatedRegulations() {
      // 模拟相关制度数据
      this.relatedRegulations = [
        {
          regulationId: 2,
          regulationName: '业主大会议事规则',
          version: '1.3'
        },
        {
          regulationId: 3,
          regulationName: '物业服务质量标准',
          version: '3.0'
        },
        {
          regulationId: 6,
          regulationName: '停车管理规定',
          version: '1.2'
        }
      ]
    },
    
    getCategoryText(category) {
      const categoryMap = {
        'management': '管理制度',
        'service': '服务标准',
        'safety': '安全制度',
        'finance': '财务制度'
      }
      return categoryMap[category] || '未知分类'
    },
    
    getStatusClass(status) {
      const classMap = {
        'active': 'status-active',
        'draft': 'status-draft',
        'expired': 'status-expired'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        'active': '生效中',
        'draft': '草案',
        'expired': '已失效'
      }
      return textMap[status] || '未知状态'
    },
    
    getFileSize() {
      return this.regulationInfo.fileSize || '未知'
    },
    
    toggleFontSize() {
      this.isLargeFont = !this.isLargeFont
      uni.showToast({
        title: this.isLargeFont ? '大字体模式' : '标准字体模式',
        icon: 'none'
      })
    },
    
    toggleNightMode() {
      this.isNightMode = !this.isNightMode
      uni.showToast({
        title: this.isNightMode ? '夜间模式' : '日间模式',
        icon: 'none'
      })
    },
    
    scrollToChapter(index) {
      const elementId = `chapter-${index}`
      uni.pageScrollTo({
        selector: `#${elementId}`,
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
      this.isFavorited = !this.isFavorited
      uni.showToast({
        title: this.isFavorited ? '已收藏' : '已取消收藏',
        icon: 'success'
      })
    },
    
    shareRegulation() {
      uni.showToast({
        title: '分享功能开发中',
        icon: 'none'
      })
    },
    
    downloadRegulation() {
      // 增加下载量
      this.regulationInfo.downloadCount++
      
      uni.showLoading({
        title: '下载中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '下载完成',
          icon: 'success'
        })
      }, 2000)
    },
    
    viewRelated(related) {
      uni.navigateTo({
        url: `/pages/work/regulation-detail?id=${related.regulationId}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.detail-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.regulation-info, .summary-section, .content-section, .history-section, .related-section, .stats-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24rpx;
    
    .section-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .content-tools {
      display: flex;
      gap: 16rpx;
      
      .tool-item {
        width: 48rpx;
        height: 48rpx;
        background: #F8F9FA;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        
        &:active {
          background: #F0F0F0;
        }
      }
    }
  }
}

.regulation-info {
  .info-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
    
    .regulation-name {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
    }
    
    .regulation-status {
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      font-size: 22rpx;
      font-weight: 500;
      white-space: nowrap;
      
      &.status-active {
        background: #F6FFED;
        color: #52C41A;
      }
      
      &.status-draft {
        background: #FFF2E8;
        color: #FA8C16;
      }
      
      &.status-expired {
        background: #F5F5F5;
        color: #8C8C8C;
      }
    }
  }
  
  .regulation-meta {
    display: flex;
    align-items: center;
    gap: 24rpx;
    flex-wrap: wrap;
    margin-bottom: 20rpx;
    
    .meta-item {
      display: flex;
      align-items: center;
      
      text {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #8C8C8C;
      }
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
      font-weight: 500;
    }
  }
}

.summary-content {
  text {
    font-size: 26rpx;
    color: #595959;
    line-height: 1.6;
  }
}

.content-text {
  &.large-font {
    .chapter-title {
      font-size: 36rpx !important;
    }
    
    .article-title {
      font-size: 32rpx !important;
    }
    
    .article-content {
      font-size: 30rpx !important;
    }
  }
  
  &.night-mode {
    background: #1F1F1F;
    border-radius: 16rpx;
    padding: 30rpx;
    
    .chapter-title, .article-title, .article-content {
      color: #E6E6E6 !important;
    }
    
    .nav-title, .nav-item text {
      color: #CCCCCC !important;
    }
  }
  
  .chapter-nav {
    background: #F8F9FA;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 40rpx;
    border: 1rpx solid #F0F0F0;
    
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
      
      &:last-child {
        border-bottom: none;
      }
      
      &:active {
        background: #F0F0F0;
      }
      
      text {
        font-size: 24rpx;
        color: #1890FF;
      }
    }
  }
  
  .regulation-content {
    .chapter-section {
      margin-bottom: 60rpx;
      
      &:last-child {
        margin-bottom: 0;
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
        
        &:last-child {
          margin-bottom: 0;
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
          margin-bottom: 16rpx;
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
      }
    }
  }
}

.history-timeline {
  .timeline-item {
    position: relative;
    padding-left: 60rpx;
    padding-bottom: 40rpx;
    
    &:last-child {
      padding-bottom: 0;
    }
    
    &:not(:last-child)::before {
      content: '';
      position: absolute;
      left: 19rpx;
      top: 40rpx;
      bottom: 0;
      width: 2rpx;
      background: #F0F0F0;
    }
    
    .timeline-dot {
      position: absolute;
      left: 0;
      top: 0;
      width: 40rpx;
      height: 40rpx;
      background: #F0F0F0;
      border-radius: 50%;
      
      &.current {
        background: #1890FF;
      }
    }
    
    .timeline-content {
      .revision-header {
        display: flex;
        align-items: center;
        gap: 16rpx;
        margin-bottom: 12rpx;
        
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
    }
  }
}

.related-list {
  .related-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #F0F0F0;
    
    &:last-child {
      border-bottom: none;
    }
    
    &:active {
      background: #F8F9FA;
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
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30rpx;
  
  .stat-item {
    text-align: center;
    
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
  }
}

.action-buttons {
  display: flex;
  gap: 16rpx;
  padding: 0 20rpx;
  
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
    
    text {
      margin-left: 8rpx;
    }
    
    &.secondary {
      background: #F8F9FA;
      color: #8C8C8C;
      border: 1rpx solid #F0F0F0;
    }
    
    &.primary {
      background: #1890FF;
      color: #FFFFFF;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}

.floating-catalog {
  position: fixed;
  bottom: 160rpx;
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
  
  &:active {
    background: #096DD9;
    transform: scale(0.95);
  }
}

.catalog-panel {
  width: 500rpx;
  height: 100vh;
  background: #FFFFFF;
  
  .catalog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #F0F0F0;
    
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
  }
  
  .catalog-content {
    padding: 20rpx;
    
    .catalog-item {
      padding: 20rpx;
      border-radius: 16rpx;
      margin-bottom: 8rpx;
      
      &:active {
        background: #F0F8FF;
      }
      
      text {
        font-size: 26rpx;
        color: #262626;
      }
    }
  }
}
</style>
  <view class="detail-container">
    <!-- 制度基本信息 -->
    <view class="regulation-info">
      <view class="info-header">
        <text class="regulation-name">{{ regulationInfo.regulationName }}</text>
        <view class="regulation-status" :class="[regulationInfo.status === 'draft' ? 'status-draft' : regulationInfo.status === 'published' ? 'status-published' : regulationInfo.status === 'archived' ? 'status-archived' : 'status-default']">
          {{ getStatusText(regulationInfo.status) }}
        </view>
      </view>
      
      <view class="regulation-meta">
        <view class="meta-item">
          <uni-icons type="list" size="14" color="#8C8C8C" />
          <text>{{ getCategoryText(regulationInfo.category) }}</text>
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
      
      <!-- 重要标识 -->
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
        <view class="content-tools">
          <view class="tool-item" @click="toggleFontSize">
            <uni-icons type="font" size="16" color="#8C8C8C" />
          </view>
          <view class="tool-item" @click="toggleNightMode">
            <uni-icons type="eye" size="16" color="#8C8C8C" />
          </view>
        </view>
      </view>
      
      <view class="content-text" :class="{ 'large-font': isLargeFont, 'night-mode': isNightMode }">
        <!-- 章节导航 -->
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
        <view class="regulation-content">
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
              
              <!-- 条款备注 -->
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
          <text class="stat-number">{{ regulationInfo.viewCount }}</text>
          <text class="stat-label">浏览次数</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ regulationInfo.downloadCount }}</text>
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

<script>
export default {
  data() {
    return {
      regulationId: null,
      regulationInfo: {},
      relatedRegulations: [],
      isLargeFont: false,
      isNightMode: false,
      isFavorited: false
    }
  },
  onLoad(options) {
    this.regulationId = options.id
    this.loadRegulationDetail()
    this.loadRelatedRegulations()
  },
  methods: {
    loadRegulationDetail() {
      // 模拟制度详情数据
      this.regulationInfo = {
        regulationId: 1,
        regulationName: '智慧花园小区物业管理规约',
        category: 'management',
        version: '2.1',
        status: 'active',
        effectiveDate: '2024-01-01',
        updateTime: '2024-01-15 10:30',
        summary: '本规约根据《中华人民共和国物权法》《物业管理条例》等法律法规制定，旨在规范小区物业管理服务标准，明确业主权利义务，建立和谐社区管理秩序，保障业主合法权益。',
        viewCount: 1256,
        downloadCount: 89,
        favoriteCount: 45,
        isImportant: true,
        fileSize: '2.5MB',
        fileType: 'PDF',
        chapters: [
          {
            title: '总则',
            articles: [
              {
                title: '制定依据',
                content: '为了规范物业管理活动，维护业主和物业服务企业的合法权益，改善人民群众的生活和工作环境，制定本规约。',
                note: '本条款为制定依据说明'
              },
              {
                title: '适用范围',
                content: '本规约适用于智慧花园小区全体业主、物业使用人、物业服务企业及相关管理人员。',
                note: null
              }
            ]
          },
          {
            title: '业主权利义务',
            articles: [
              {
                title: '业主权利',
                content: '业主在物业管理活动中，享有下列权利：\n（一）按照物业服务合同的约定，接受物业服务企业提供的服务；\n（二）提议召开业主大会会议，并就物业管理的有关事项提出建议；\n（三）提出制定和修改管理规约、业主大会议事规则的建议；\n（四）参加业主大会会议，行使投票权；\n（五）选举业主委员会成员，并享有被选举权。',
                note: '业主权利受法律保护'
              },
              {
                title: '业主义务',
                content: '业主在物业管理活动中，应当履行下列义务：\n（一）遵守管理规约、业主大会议事规则；\n（二）遵守物业管理区域内物业共用部位和共用设施设备的使用、公共秩序和环境卫生的维护等方面的规章制度；\n（三）执行业主大会的决定和业主大会授权业主委员会作出的决定；\n（四）按照国家有关规定交纳专项维修资金；\n（五）按时交纳物业服务费用。',
                note: '业主应严格履行义务'
              }
            ]
          },
          {
            title: '物业服务',
            articles: [
              {
                title: '服务内容',
                content: '物业服务企业应当按照物业服务合同的约定，提供相应的服务，包括但不限于：\n（一）物业共用部位的维护、保养和管理；\n（二）物业共用设施设备的运行、维护、保养和管理；\n（三）物业管理区域内的清洁卫生；\n（四）物业管理区域内的秩序维护；\n（五）物业管理区域内的绿化养护。',
                note: '服务内容应符合合同约定'
              }
            ]
          }
        ],
        revisionHistory: [
          {
            version: '2.1',
            date: '2024-01-15',
            description: '根据最新法律法规，完善业主权利义务条款，增加物业服务标准细则。',
            author: '业主委员会'
          },
          {
            version: '2.0',
            date: '2023-06-01',
            description: '全面修订管理规约，增加数字化管理相关条款。',
            author: '业主委员会'
          },
          {
            version: '1.0',
            date: '2022-01-01',
            description: '初始版本制定，确立基本管理框架。',
            author: '开发商'
          }
        ]
      }
    },
    
    loadRelatedRegulations() {
      // 模拟相关制度数据
      this.relatedRegulations = [
        {
          regulationId: 2,
          regulationName: '业主大会议事规则',
          version: '1.3'
        },
        {
          regulationId: 3,
          regulationName: '物业服务质量标准',
          version: '3.0'
        },
        {
          regulationId: 6,
          regulationName: '停车管理规定',
          version: '1.2'
        }
      ]
    },
    
    getCategoryText(category) {
      const categoryMap = {
        'management': '管理制度',
        'service': '服务标准',
        'safety': '安全制度',
        'finance': '财务制度'
      }
      return categoryMap[category] || '未知分类'
    },
    
    getStatusClass(status) {
      const classMap = {
        'active': 'status-active',
        'draft': 'status-draft',
        'expired': 'status-expired'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        'active': '生效中',
        'draft': '草案',
        'expired': '已失效'
      }
      return textMap[status] || '未知状态'
    },
    
    getFileSize() {
      return this.regulationInfo.fileSize || '未知'
    },
    
    toggleFontSize() {
      this.isLargeFont = !this.isLargeFont
      uni.showToast({
        title: this.isLargeFont ? '大字体模式' : '标准字体模式',
        icon: 'none'
      })
    },
    
    toggleNightMode() {
      this.isNightMode = !this.isNightMode
      uni.showToast({
        title: this.isNightMode ? '夜间模式' : '日间模式',
        icon: 'none'
      })
    },
    
    scrollToChapter(index) {
      const elementId = `chapter-${index}`
      uni.pageScrollTo({
        selector: `#${elementId}`,
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
      this.isFavorited = !this.isFavorited
      uni.showToast({
        title: this.isFavorited ? '已收藏' : '已取消收藏',
        icon: 'success'
      })
    },
    
    shareRegulation() {
      uni.showToast({
        title: '分享功能开发中',
        icon: 'none'
      })
    },
    
    downloadRegulation() {
      // 增加下载量
      this.regulationInfo.downloadCount++
      
      uni.showLoading({
        title: '下载中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '下载完成',
          icon: 'success'
        })
      }, 2000)
    },
    
    viewRelated(related) {
      uni.navigateTo({
        url: `/pages/work/regulation-detail?id=${related.regulationId}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.detail-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.regulation-info, .summary-section, .content-section, .history-section, .related-section, .stats-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24rpx;
    
    .section-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .content-tools {
      display: flex;
      gap: 16rpx;
      
      .tool-item {
        width: 48rpx;
        height: 48rpx;
        background: #F8F9FA;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        
        &:active {
          background: #F0F0F0;
        }
      }
    }
  }
}

.regulation-info {
  .info-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
    
    .regulation-name {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
    }
    
    .regulation-status {
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      font-size: 22rpx;
      font-weight: 500;
      white-space: nowrap;
      
      &.status-active {
        background: #F6FFED;
        color: #52C41A;
      }
      
      &.status-draft {
        background: #FFF2E8;
        color: #FA8C16;
      }
      
      &.status-expired {
        background: #F5F5F5;
        color: #8C8C8C;
      }
    }
  }
  
  .regulation-meta {
    display: flex;
    align-items: center;
    gap: 24rpx;
    flex-wrap: wrap;
    margin-bottom: 20rpx;
    
    .meta-item {
      display: flex;
      align-items: center;
      
      text {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #8C8C8C;
      }
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
      font-weight: 500;
    }
  }
}

.summary-content {
  text {
    font-size: 26rpx;
    color: #595959;
    line-height: 1.6;
  }
}

.content-text {
  &.large-font {
    .chapter-title {
      font-size: 36rpx !important;
    }
    
    .article-title {
      font-size: 32rpx !important;
    }
    
    .article-content {
      font-size: 30rpx !important;
    }
  }
  
  &.night-mode {
    background: #1F1F1F;
    border-radius: 16rpx;
    padding: 30rpx;
    
    .chapter-title, .article-title, .article-content {
      color: #E6E6E6 !important;
    }
    
    .nav-title, .nav-item text {
      color: #CCCCCC !important;
    }
  }
  
  .chapter-nav {
    background: #F8F9FA;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 40rpx;
    border: 1rpx solid #F0F0F0;
    
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
      
      &:last-child {
        border-bottom: none;
      }
      
      &:active {
        background: #F0F0F0;
      }
      
      text {
        font-size: 24rpx;
        color: #1890FF;
      }
    }
  }
  
  .regulation-content {
    .chapter-section {
      margin-bottom: 60rpx;
      
      &:last-child {
        margin-bottom: 0;
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
        
        &:last-child {
          margin-bottom: 0;
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
          margin-bottom: 16rpx;
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
      }
    }
  }
}

.history-timeline {
  .timeline-item {
    position: relative;
    padding-left: 60rpx;
    padding-bottom: 40rpx;
    
    &:last-child {
      padding-bottom: 0;
    }
    
    &:not(:last-child)::before {
      content: '';
      position: absolute;
      left: 19rpx;
      top: 40rpx;
      bottom: 0;
      width: 2rpx;
      background: #F0F0F0;
    }
    
    .timeline-dot {
      position: absolute;
      left: 0;
      top: 0;
      width: 40rpx;
      height: 40rpx;
      background: #F0F0F0;
      border-radius: 50%;
      
      &.current {
        background: #1890FF;
      }
    }
    
    .timeline-content {
      .revision-header {
        display: flex;
        align-items: center;
        gap: 16rpx;
        margin-bottom: 12rpx;
        
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
    }
  }
}

.related-list {
  .related-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #F0F0F0;
    
    &:last-child {
      border-bottom: none;
    }
    
    &:active {
      background: #F8F9FA;
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
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30rpx;
  
  .stat-item {
    text-align: center;
    
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
  }
}

.action-buttons {
  display: flex;
  gap: 16rpx;
  padding: 0 20rpx;
  
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
    
    text {
      margin-left: 8rpx;
    }
    
    &.secondary {
      background: #F8F9FA;
      color: #8C8C8C;
      border: 1rpx solid #F0F0F0;
    }
    
    &.primary {
      background: #1890FF;
      color: #FFFFFF;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}

.floating-catalog {
  position: fixed;
  bottom: 160rpx;
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
  
  &:active {
    background: #096DD9;
    transform: scale(0.95);
  }
}

.catalog-panel {
  width: 500rpx;
  height: 100vh;
  background: #FFFFFF;
  
  .catalog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #F0F0F0;
    
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
  }
  
  .catalog-content {
    padding: 20rpx;
    
    .catalog-item {
      padding: 20rpx;
      border-radius: 16rpx;
      margin-bottom: 8rpx;
      
      &:active {
        background: #F0F8FF;
      }
      
      text {
        font-size: 26rpx;
        color: #262626;
      }
    }
  }
}
</style>
 