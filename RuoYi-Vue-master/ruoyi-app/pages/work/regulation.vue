<template>
  <view class="regulation-container">
    <!-- 搜索栏 -->
    <view class="search-section">
      <view class="search-box">
        <uni-icons type="search" size="18" color="#8C8C8C" />
        <input 
          class="search-input" 
          placeholder="搜索制度名称或关键词" 
          v-model="searchKeyword"
          @input="onSearch"
        />
        <text class="clear-btn" v-if="searchKeyword" @click="clearSearch">清除</text>
      </view>
    </view>

    <!-- 分类导航 -->
    <view class="category-nav">
      <view 
        class="nav-item" 
        :class="{ active: activeCategory === 'all' }"
        @click="switchCategory('all')"
      >
        全部制度
      </view>
      <view 
        class="nav-item" 
        :class="{ active: activeCategory === 'management' }"
        @click="switchCategory('management')"
      >
        管理制度
      </view>
      <view 
        class="nav-item" 
        :class="{ active: activeCategory === 'service' }"
        @click="switchCategory('service')"
      >
        服务标准
      </view>
      <view 
        class="nav-item" 
        :class="{ active: activeCategory === 'safety' }"
        @click="switchCategory('safety')"
      >
        安全制度
      </view>
      <view 
        class="nav-item" 
        :class="{ active: activeCategory === 'finance' }"
        @click="switchCategory('finance')"
      >
        财务制度
      </view>
    </view>

    <!-- 制度统计 -->
    <view class="stats-section">
      <view class="stat-item">
        <text class="stat-number">{{ stats.total }}</text>
        <text class="stat-label">制度总数</text>
      </view>
      <view class="stat-item">
        <text class="stat-number active">{{ stats.active }}</text>
        <text class="stat-label">生效中</text>
      </view>
      <view class="stat-item">
        <text class="stat-number updated">{{ stats.updated }}</text>
        <text class="stat-label">本月更新</text>
      </view>
      <view class="stat-item">
        <text class="stat-number views">{{ stats.totalViews }}</text>
        <text class="stat-label">总浏览量</text>
      </view>
    </view>

    <!-- 最新更新 -->
    <view class="recent-updates" v-if="recentUpdates.length > 0">
      <view class="section-header">
        <uni-icons type="refresh" size="18" color="#1890FF" />
        <text class="section-title">最新更新</text>
      </view>
      
      <view class="update-list">
        <view 
          class="update-item" 
          v-for="item in recentUpdates" 
          :key="item.regulationId"
          @click="viewRegulation(item)"
        >
          <view class="update-badge">新</view>
          <view class="update-content">
            <text class="update-title">{{ item.regulationName }}</text>
            <text class="update-time">{{ item.updateTime }}</text>
          </view>
          <uni-icons type="right" size="16" color="#D9D9D9" />
        </view>
      </view>
    </view>

    <!-- 制度列表 -->
    <view class="regulation-list">
      <view class="list-header">
        <text class="list-title">制度文件</text>
        <view class="sort-options">
          <text 
            class="sort-item" 
            :class="{ active: sortBy === 'time' }"
            @click="changeSortBy('time')"
          >
            按时间
          </text>
          <text 
            class="sort-item" 
            :class="{ active: sortBy === 'views' }"
            @click="changeSortBy('views')"
          >
            按浏览量
          </text>
        </view>
      </view>
      
      <view 
        class="regulation-item" 
        v-for="regulation in filteredRegulations" 
        :key="regulation.regulationId"
        @click="viewRegulation(regulation)"
      >
        <view class="regulation-header">
          <view class="regulation-info">
            <text class="regulation-name">{{ regulation.regulationName }}</text>
            <view class="regulation-meta">
              <text class="regulation-category">{{ getCategoryText(regulation.category) }}</text>
              <text class="regulation-version">v{{ regulation.version }}</text>
              <text class="regulation-date">{{ regulation.effectiveDate }}</text>
            </view>
          </view>
          <view class="regulation-status" :class="[regulation.status === 'draft' ? 'status-draft' : regulation.status === 'published' ? 'status-published' : regulation.status === 'archived' ? 'status-archived' : 'status-default']">
            {{ getStatusText(regulation.status) }}
          </view>
        </view>
        
        <view class="regulation-summary" v-if="regulation.summary">
          <text>{{ regulation.summary }}</text>
        </view>
        
        <view class="regulation-footer">
          <view class="regulation-stats">
            <view class="stat-item">
              <uni-icons type="eye" size="14" color="#8C8C8C" />
              <text>{{ regulation.viewCount }}次浏览</text>
            </view>
            <view class="stat-item">
              <uni-icons type="download" size="14" color="#8C8C8C" />
              <text>{{ regulation.downloadCount }}次下载</text>
            </view>
          </view>
          <view class="regulation-actions">
            <button class="action-btn view" @click.stop="quickView(regulation)">
              <uni-icons type="eye" size="16" color="#1890FF" />
              <text>预览</text>
            </button>
            <button class="action-btn download" @click.stop="downloadRegulation(regulation)">
              <uni-icons type="download" size="16" color="#52C41A" />
              <text>下载</text>
            </button>
          </view>
        </view>
        
        <!-- 重要标识 -->
        <view class="important-badge" v-if="regulation.isImportant">
          <uni-icons type="flag" size="12" color="#FF4D4F" />
          <text>重要</text>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="filteredRegulations.length === 0">
      <uni-icons type="paperplane" size="80" color="#D9D9D9" />
      <text class="empty-text">{{ getEmptyText() }}</text>
    </view>

    <!-- 制度申请入口 -->
    <view class="apply-section">
      <view class="apply-card">
        <view class="apply-icon">
          <uni-icons type="compose" size="24" color="#1890FF" />
        </view>
        <view class="apply-content">
          <text class="apply-title">制度建议</text>
          <text class="apply-desc">如有制度修订建议，欢迎提出</text>
        </view>
        <button class="apply-btn" @click="submitSuggestion">
          提出建议
        </button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: '',
      activeCategory: 'all',
      sortBy: 'time',
      regulationList: [],
      recentUpdates: [],
      stats: {
        total: 0,
        active: 0,
        updated: 0,
        totalViews: 0
      }
    }
  },
  computed: {
    filteredRegulations() {
      let regulations = this.regulationList
      
      // 按分类筛选
      if (this.activeCategory !== 'all') {
        regulations = regulations.filter(item => item.category === this.activeCategory)
      }
      
      // 按关键词搜索
      if (this.searchKeyword.trim()) {
        const keyword = this.searchKeyword.trim().toLowerCase()
        regulations = regulations.filter(item => 
          item.regulationName.toLowerCase().includes(keyword) ||
          (item.summary && item.summary.toLowerCase().includes(keyword))
        )
      }
      
      // 排序
      if (this.sortBy === 'time') {
        regulations.sort((a, b) => new Date(b.updateTime) - new Date(a.updateTime))
      } else if (this.sortBy === 'views') {
        regulations.sort((a, b) => b.viewCount - a.viewCount)
      }
      
      return regulations
    }
  },
  onLoad() {
    this.loadRegulations()
    this.loadRecentUpdates()
    this.loadStats()
  },
  onPullDownRefresh() {
    this.loadRegulations()
    this.loadRecentUpdates()
    this.loadStats()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    loadRegulations() {
      // 模拟制度数据
      this.regulationList = [
        {
          regulationId: 1,
          regulationName: '智慧花园小区物业管理规约',
          category: 'management',
          version: '2.1',
          status: 'active',
          effectiveDate: '2024-01-01',
          updateTime: '2024-01-15 10:30',
          summary: '规范小区物业管理服务标准，明确业主权利义务，建立和谐社区管理秩序。',
          viewCount: 1256,
          downloadCount: 89,
          isImportant: true,
          fileSize: '2.5MB',
          fileType: 'PDF'
        },
        {
          regulationId: 2,
          regulationName: '业主大会议事规则',
          category: 'management',
          version: '1.3',
          status: 'active',
          effectiveDate: '2024-01-01',
          updateTime: '2024-01-10 14:20',
          summary: '规范业主大会召开程序，保障业主民主决策权利，提高决策效率。',
          viewCount: 892,
          downloadCount: 67,
          isImportant: true,
          fileSize: '1.8MB',
          fileType: 'PDF'
        },
        {
          regulationId: 3,
          regulationName: '物业服务质量标准',
          category: 'service',
          version: '3.0',
          status: 'active',
          effectiveDate: '2024-01-01',
          updateTime: '2024-01-08 09:15',
          summary: '详细规定保洁、保安、绿化、设备维护等各项服务的具体标准和要求。',
          viewCount: 1089,
          downloadCount: 124,
          isImportant: false,
          fileSize: '3.2MB',
          fileType: 'PDF'
        },
        {
          regulationId: 4,
          regulationName: '消防安全管理制度',
          category: 'safety',
          version: '2.0',
          status: 'active',
          effectiveDate: '2024-01-01',
          updateTime: '2024-01-05 16:45',
          summary: '建立完善的消防安全管理体系，预防火灾事故，保障生命财产安全。',
          viewCount: 756,
          downloadCount: 45,
          isImportant: true,
          fileSize: '2.1MB',
          fileType: 'PDF'
        },
        {
          regulationId: 5,
          regulationName: '专项维修资金使用管理办法',
          category: 'finance',
          version: '1.5',
          status: 'active',
          effectiveDate: '2024-01-01',
          updateTime: '2024-01-03 11:30',
          summary: '规范专项维修资金的申请、审批、使用和监督管理流程。',
          viewCount: 634,
          downloadCount: 78,
          isImportant: false,
          fileSize: '1.6MB',
          fileType: 'PDF'
        },
        {
          regulationId: 6,
          regulationName: '停车管理规定',
          category: 'management',
          version: '1.2',
          status: 'active',
          effectiveDate: '2023-12-01',
          updateTime: '2023-12-15 13:20',
          summary: '规范小区内机动车停放秩序，合理分配停车资源，维护停车环境。',
          viewCount: 945,
          downloadCount: 56,
          isImportant: false,
          fileSize: '1.3MB',
          fileType: 'PDF'
        }
      ]
    },
    
    loadRecentUpdates() {
      // 获取最近更新的制度（最近30天）
      const thirtyDaysAgo = new Date()
      thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30)
      
      this.recentUpdates = this.regulationList
        .filter(item => new Date(item.updateTime) > thirtyDaysAgo)
        .sort((a, b) => new Date(b.updateTime) - new Date(a.updateTime))
        .slice(0, 3)
    },
    
    loadStats() {
      // 计算统计数据
      const total = this.regulationList.length
      const active = this.regulationList.filter(item => item.status === 'active').length
      const updated = this.recentUpdates.length
      const totalViews = this.regulationList.reduce((sum, item) => sum + item.viewCount, 0)
      
      this.stats = { total, active, updated, totalViews }
    },
    
    switchCategory(category) {
      this.activeCategory = category
    },
    
    changeSortBy(sortBy) {
      this.sortBy = sortBy
    },
    
    onSearch() {
      // 搜索防抖处理
      clearTimeout(this.searchTimer)
      this.searchTimer = setTimeout(() => {
        // 搜索逻辑在computed中处理
      }, 300)
    },
    
    clearSearch() {
      this.searchKeyword = ''
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
    
    getEmptyText() {
      if (this.searchKeyword.trim()) {
        return '未找到相关制度文件'
      }
      if (this.activeCategory !== 'all') {
        return '该分类下暂无制度文件'
      }
      return '暂无制度文件'
    },
    
    viewRegulation(regulation) {
      // 增加浏览量
      regulation.viewCount++
      
      uni.navigateTo({
        url: `/pages/work/regulation-detail?id=${regulation.regulationId}`
      })
    },
    
    quickView(regulation) {
      uni.showToast({
        title: '预览功能开发中',
        icon: 'none'
      })
    },
    
    downloadRegulation(regulation) {
      // 增加下载量
      regulation.downloadCount++
      
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
    
    submitSuggestion() {
      uni.showToast({
        title: '建议提交功能开发中',
        icon: 'none'
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

.regulation-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.search-section {
  background: #FFFFFF;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
  
  .search-box {
    display: flex;
    align-items: center;
    background: #F8F9FA;
    border-radius: 24rpx;
    padding: 20rpx 30rpx;
    border: 1rpx solid #F0F0F0;
    
    .search-input {
      flex: 1;
      margin-left: 16rpx;
      font-size: 28rpx;
      color: #262626;
    }
    
    .clear-btn {
      font-size: 24rpx;
      color: #8C8C8C;
      padding: 8rpx 16rpx;
    }
  }
}

.category-nav {
  display: flex;
  background: #FFFFFF;
  padding: 20rpx;
  overflow-x: auto;
  white-space: nowrap;
  border-bottom: 1rpx solid #F0F0F0;
  
  .nav-item {
    flex-shrink: 0;
    padding: 16rpx 32rpx;
    margin-right: 16rpx;
    background: #F8F9FA;
    color: #8C8C8C;
    border-radius: 20rpx;
    font-size: 24rpx;
    transition: all 0.3s ease;
    
    &.active {
      background: #1890FF;
      color: #FFFFFF;
      font-weight: 600;
    }
    
    &:last-child {
      margin-right: 0;
    }
  }
}

.stats-section {
  display: flex;
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 30rpx 20rpx;
  border: 1rpx solid #F0F0F0;
  
  .stat-item {
    flex: 1;
    text-align: center;
    
    .stat-number {
      display: block;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      margin-bottom: 8rpx;
      
      &.active {
        color: #52C41A;
      }
      
      &.updated {
        color: #1890FF;
      }
      
      &.views {
        color: #FA8C16;
      }
    }
    
    .stat-label {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
}

.recent-updates {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-header {
    display: flex;
    align-items: center;
    margin-bottom: 24rpx;
    
    .section-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .update-list {
    .update-item {
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
      
      .update-badge {
        width: 40rpx;
        height: 40rpx;
        background: #FF4D4F;
        color: #FFFFFF;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20rpx;
        font-weight: 600;
        margin-right: 20rpx;
        flex-shrink: 0;
      }
      
      .update-content {
        flex: 1;
        
        .update-title {
          display: block;
          font-size: 26rpx;
          color: #262626;
          font-weight: 500;
          margin-bottom: 8rpx;
        }
        
        .update-time {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
    }
  }
}

.regulation-list {
  padding: 0 20rpx;
  
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 30rpx 20rpx;
    
    .list-title {
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .sort-options {
      display: flex;
      gap: 24rpx;
      
      .sort-item {
        font-size: 24rpx;
        color: #8C8C8C;
        
        &.active {
          color: #1890FF;
          font-weight: 600;
        }
      }
    }
  }
  
  .regulation-item {
    position: relative;
    background: #FFFFFF;
    border-radius: 24rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    border: 1rpx solid #F0F0F0;
    
    &:active {
      background: #F8F9FA;
    }
    
    .regulation-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 20rpx;
      
      .regulation-info {
        flex: 1;
        margin-right: 20rpx;
        
        .regulation-name {
          display: block;
          font-size: 28rpx;
          font-weight: 600;
          color: #262626;
          line-height: 1.4;
          margin-bottom: 12rpx;
        }
        
        .regulation-meta {
          display: flex;
          align-items: center;
          gap: 16rpx;
          flex-wrap: wrap;
          
          .regulation-category {
            font-size: 22rpx;
            color: #1890FF;
            padding: 4rpx 12rpx;
            background: #E6F7FF;
            border-radius: 12rpx;
          }
          
          .regulation-version {
            font-size: 22rpx;
            color: #52C41A;
            padding: 4rpx 12rpx;
            background: #F6FFED;
            border-radius: 12rpx;
          }
          
          .regulation-date {
            font-size: 22rpx;
            color: #8C8C8C;
          }
        }
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
    
    .regulation-summary {
      margin-bottom: 20rpx;
      
      text {
        font-size: 24rpx;
        color: #595959;
        line-height: 1.6;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }
    }
    
    .regulation-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .regulation-stats {
        display: flex;
        gap: 24rpx;
        
        .stat-item {
          display: flex;
          align-items: center;
          
          text {
            margin-left: 8rpx;
            font-size: 22rpx;
            color: #8C8C8C;
          }
        }
      }
      
      .regulation-actions {
        display: flex;
        gap: 16rpx;
        
        .action-btn {
          display: flex;
          align-items: center;
          padding: 12rpx 20rpx;
          border-radius: 16rpx;
          font-size: 22rpx;
          border: none;
          
          text {
            margin-left: 8rpx;
          }
          
          &.view {
            background: #E6F7FF;
            color: #1890FF;
          }
          
          &.download {
            background: #F6FFED;
            color: #52C41A;
          }
          
          &:active {
            opacity: 0.8;
          }
        }
      }
    }
    
    .important-badge {
      position: absolute;
      top: 20rpx;
      right: 20rpx;
      display: flex;
      align-items: center;
      padding: 4rpx 12rpx;
      background: #FF4D4F;
      color: #FFFFFF;
      border-radius: 12rpx;
      font-size: 20rpx;
      
      text {
        margin-left: 4rpx;
      }
    }
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;
  
  .empty-text {
    margin-top: 30rpx;
    font-size: 28rpx;
    color: #8C8C8C;
  }
}

.apply-section {
  padding: 0 20rpx 40rpx;
  
  .apply-card {
    display: flex;
    align-items: center;
    background: #E6F7FF;
    border-radius: 24rpx;
    padding: 30rpx;
    border: 1rpx solid #91D5FF;
    
    .apply-icon {
      width: 60rpx;
      height: 60rpx;
      background: rgba(255, 255, 255, 0.8);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
    }
    
    .apply-content {
      flex: 1;
      
      .apply-title {
        display: block;
        font-size: 26rpx;
        font-weight: 600;
        color: #1890FF;
        margin-bottom: 8rpx;
      }
      
      .apply-desc {
        font-size: 22rpx;
        color: #1890FF;
        opacity: 0.8;
      }
    }
    
    .apply-btn {
      padding: 16rpx 32rpx;
      background: #1890FF;
      color: #FFFFFF;
      border-radius: 20rpx;
      font-size: 24rpx;
      font-weight: 500;
      border: none;
      
      &:active {
        background: #096DD9;
      }
    }
  }
}
</style>
 
 