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
        v-for="regulation in regulationList" 
        :key="regulation.regulationId"
        @click="viewRegulation(regulation)"
      >
        <view class="regulation-header">
          <view class="regulation-info">
            <text class="regulation-name">{{ regulation.regulationName }}</text>
            <view class="regulation-meta">
              <text class="regulation-category">{{ getCategoryText(regulation.categoryId) }}</text>
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
              <text>{{ regulation.viewCount || 0 }}次浏览</text>
            </view>
            <view class="stat-item">
              <uni-icons type="download" size="14" color="#8C8C8C" />
              <text>{{ regulation.downloadCount || 0 }}次下载</text>
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
        <!-- <view class="important-badge" v-if="regulation.isImportant">
          <uni-icons type="flag" size="12" color="#FF4D4F" />
          <text>重要</text>
        </view> -->
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="regulationList.length === 0">
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
import { listRegulation, listCategory, incrementViewCount } from '@/api/regulation.js'

export default {
  data() {
    return {
      searchKeyword: '',
      activeCategoryId: 'all',
      sortBy: 'updateTime',
      // 分类列表
      categoryList: [],
      // 制度列表
      regulationList: [],
      // 统计数据（后续可由专门接口提供）
      stats: {
        total: 0,
        active: 0,
        updated: 0,
        totalViews: 0
      },
      // 最近更新（后续可由专门接口提供）
      recentUpdates: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        regulationName: undefined,
        categoryId: undefined,
        orderByColumn: 'update_time',
        isAsc: 'desc'
      },
      // 列表总数
      total: 0,
      // 加载状态
      loading: false,
      // 定时器
      searchTimer: null
    }
  },
  onLoad() {
    this.getCategoryList()
    this.getList()
  },
  onPullDownRefresh() {
    this.queryParams.pageNum = 1
    this.getList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    /** 获取制度分类列表 */
    getCategoryList() {
      listCategory().then(response => {
        this.categoryList = [{ categoryId: 'all', categoryName: '全部制度' }]
        this.categoryList = this.categoryList.concat(response.data)
      })
    },
    /** 获取制度列表 */
    getList() {
      this.loading = true
      // 同步搜索关键词到查询参数
      this.queryParams.regulationName = this.searchKeyword
      listRegulation(this.queryParams).then(response => {
        this.regulationList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 切换分类 */
    switchCategory(categoryId) {
      this.activeCategoryId = categoryId
      this.queryParams.categoryId = categoryId === 'all' ? undefined : categoryId
      this.handleQuery()
    },
    /** 切换排序 */
    changeSortBy(sortBy) {
      this.sortBy = sortBy
      if (sortBy === 'time') {
        this.queryParams.orderByColumn = 'update_time'
      } else if (sortBy === 'views') {
        this.queryParams.orderByColumn = 'view_count'
      }
      this.handleQuery()
    },
    /** 搜索事件 */
    onSearch() {
      clearTimeout(this.searchTimer)
      this.searchTimer = setTimeout(() => {
        this.handleQuery()
      }, 300)
    },
    /** 清除搜索 */
    clearSearch() {
      this.searchKeyword = ''
      this.handleQuery()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 跳转到详情页 */
    viewRegulation(regulation) {
      if (!regulation || !regulation.regulationId) {
        uni.showToast({
          title: '无效的制度ID',
          icon: 'error'
        });
        return;
      }
      // 建议：调用接口增加浏览次数
      // incrementViewCount(regulation.regulationId)
      uni.navigateTo({
        url: `/pages/work/regulation-detail?id=${regulation.regulationId}`
      })
    },
    // 工具方法，保留UI模板中的调用
    getCategoryText(categoryId) {
      const category = this.categoryList.find(item => item.categoryId === categoryId)
      return category ? category.categoryName : '未知分类'
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
      if (this.activeCategoryId !== 'all') {
        return '该分类下暂无制度文件'
      }
      return '暂无制度文件'
    },
    // 模拟/占位方法
    quickView(regulation) {
      uni.showToast({
        title: '预览功能开发中',
        icon: 'none'
      })
    },
    downloadRegulation(regulation) {
      uni.showToast({
        title: '下载功能开发中',
        icon: 'none'
      })
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
 
 