<template>
  <view class="my-complaints-container">
    <!-- 页面标题 -->
<!--    <view class="page-header">-->
<!--      <view class="header-content">-->
<!--        <view class="header-left" @click="goBack">-->
<!--          <uni-icons type="left" size="20" color="#333" />-->
<!--        </view>-->
<!--        <text class="header-title">我的投诉</text>-->
<!--        <view class="header-right" @click="addComplaint">-->
<!--          <uni-icons type="plus" size="20" color="#1890FF" />-->
<!--        </view>-->
<!--      </view>-->
<!--    </view>-->

    <!-- 筛选条件 -->
    <view class="filter-section">
      <view class="filter-tabs">
        <view 
          class="filter-tab" 
          :class="{ active: activeTab === 'all' }"
          @click="switchTab('all')"
        >
          <text>全部</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: activeTab === 'pending' }"
          @click="switchTab('pending')"
        >
          <text>待处理</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: activeTab === 'processing' }"
          @click="switchTab('processing')"
        >
          <text>处理中</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: activeTab === 'completed' }"
          @click="switchTab('completed')"
        >
          <text>已完成</text>
        </view>
      </view>
    </view>

    <!-- 投诉记录列表 -->
    <view class="complaints-list">
      <view v-if="loading" class="loading-container">
        <uni-load-more status="loading" />
      </view>
      
      <view v-else-if="complaintList.length === 0" class="empty-container">
        <image class="empty-icon" src="/static/images/empty-complaint.png" mode="aspectFit" />
        <text class="empty-text">暂无投诉记录</text>
        <text class="empty-desc">您还没有提交任何投诉</text>
        <button class="add-btn" @click="addComplaint">提交投诉</button>
      </view>
      
      <view v-else class="complaint-items">
        <view 
          class="complaint-item" 
          v-for="(item, index) in complaintList" 
          :key="index"
          @click="viewComplaintDetail(item)"
        >
          <view class="complaint-header">
            <view class="complaint-title-section">
              <text class="complaint-title">{{ item.complaintTitle || item.title || '未知' }}</text>
              <view class="complaint-status" :class="'status-' + item.status">
                <text class="status-text">{{ getStatusText(item.status) }}</text>
              </view>
            </view>
            <view class="complaint-time">
              <text class="time-text">{{ formatTime(item.createTime) }}</text>
            </view>
          </view>
          
          <view class="complaint-content">
            <text class="complaint-desc">{{ item.complaintContent || item.description || '暂无描述' }}</text>
          </view>
          
          <view class="complaint-footer">
            <view class="complaint-info">
              <text class="info-text">投诉编号：{{ item.complaintNo || '未生成' }}</text>
            </view>
            <view class="complaint-actions">
              <text class="action-text">查看详情</text>
              <uni-icons type="right" size="14" color="#C0C4CC" />
            </view>
          </view>
          
          <!-- 处理进度 -->
          <view v-if="item.status !== 'pending'" class="progress-section">
            <view class="progress-bar">
              <view class="progress-fill" :style="{ width: getProgressWidth(item.status) }"></view>
            </view>
            <text class="progress-text">{{ getProgressText(item.status) }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 加载更多 -->
    <view v-if="hasMore && !loading" class="load-more">
      <uni-load-more status="more" @clickLoadMore="loadMore" />
    </view>
  </view>
</template>

<script>
import { listMyComplaint } from '@/api/property/complaint'

export default {
  data() {
    return {
      activeTab: 'all',
      complaintList: [],
      loading: false,
      hasMore: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: null
      }
    }
  },
  
  onLoad() {
    this.loadComplaints()
  },
  
  onPullDownRefresh() {
    this.refreshData()
  },
  
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.loadMore()
    }
  },
  
  methods: {
    goBack() {
      uni.navigateBack()
    },
    
    switchTab(tab) {
      this.activeTab = tab
      this.queryParams.status = tab === 'all' ? null : tab
      this.refreshData()
    },
    
    async loadComplaints() {
      if (this.loading) return
      
      this.loading = true
      try {
        const response = await listMyComplaint(this.queryParams)
        if (response.code === 200) {
          const newData = response.rows || []
          
          if (this.queryParams.pageNum === 1) {
            this.complaintList = newData
          } else {
            this.complaintList = [...this.complaintList, ...newData]
          }
          
          this.hasMore = newData.length === this.queryParams.pageSize
        }
      } catch (error) {
        console.error('加载投诉记录失败:', error)
        uni.showToast({
          title: '加载失败，请重试',
          icon: 'none'
        })
      } finally {
        this.loading = false
        uni.stopPullDownRefresh()
      }
    },
    
    refreshData() {
      this.queryParams.pageNum = 1
      this.hasMore = true
      this.loadComplaints()
    },
    
    loadMore() {
      this.queryParams.pageNum++
      this.loadComplaints()
    },
    
    addComplaint() {
      uni.navigateTo({
        url: '/pages/property/complaint/add'
      })
    },
    
    viewComplaintDetail(item) {
      uni.navigateTo({
        url: `/pages/property/complaint/detail?id=${item.id}`
      })
    },
    
    getStatusText(status) {
      const statusMap = {
        '0': '待处理',
        '1': '处理中', 
        '2': '已完成',
        '3': '已关闭',
        'pending': '待处理',
        'processing': '处理中',
        'completed': '已完成',
        'rejected': '已驳回'
      }
      return statusMap[status] || '未知'
    },
    
    
    getProgressWidth(status) {
      const progressMap = {
        '0': '0%',
        '1': '50%',
        '2': '100%',
        '3': '100%',
        'pending': '0%',
        'processing': '50%',
        'completed': '100%',
        'rejected': '100%'
      }
      return progressMap[status] || '0%'
    },
    
    getProgressText(status) {
      const textMap = {
        '0': '等待处理',
        '1': '正在处理中',
        '2': '处理完成',
        '3': '已关闭',
        'pending': '等待处理',
        'processing': '正在处理中',
        'completed': '处理完成',
        'rejected': '已驳回'
      }
      return textMap[status] || '等待处理'
    },
    
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) { // 1分钟内
        return '刚刚'
      } else if (diff < 3600000) { // 1小时内
        return Math.floor(diff / 60000) + '分钟前'
      } else if (diff < 86400000) { // 24小时内
        return Math.floor(diff / 3600000) + '小时前'
      } else {
        return date.toLocaleDateString()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

.my-complaints-container {
  min-height: 100vh;
  background-color: #F8F9FA;
}

.page-header {
  background-color: #FFFFFF;
  border-bottom: 1rpx solid #F0F0F0;
  
  .header-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20rpx 30rpx;
    
    .header-left {
      width: 60rpx;
      height: 60rpx;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    
    .header-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .header-right {
      width: 60rpx;
      height: 60rpx;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}

.filter-section {
  background-color: #FFFFFF;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
  
  .filter-tabs {
    display: flex;
    background-color: #F8F9FA;
    border-radius: 12rpx;
    padding: 8rpx;
    
    .filter-tab {
      flex: 1;
      text-align: center;
      padding: 16rpx 0;
      border-radius: 8rpx;
      transition: all 0.3s;
      
      text {
        font-size: 24rpx;
        color: #8C8C8C;
        transition: color 0.3s;
      }
      
      &.active {
        background-color: #FFFFFF;
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
        
        text {
          color: #1890FF;
          font-weight: 500;
        }
      }
    }
  }
}

.complaints-list {
  padding: 30rpx;
  
  .loading-container {
    display: flex;
    justify-content: center;
    padding: 60rpx 0;
  }
  
  .empty-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 120rpx 0;
    
    .empty-icon {
      width: 200rpx;
      height: 200rpx;
      margin-bottom: 40rpx;
      opacity: 0.6;
    }
    
    .empty-text {
      font-size: 32rpx;
      color: #8C8C8C;
      margin-bottom: 16rpx;
    }
    
    .empty-desc {
      font-size: 24rpx;
      color: #C0C4CC;
      margin-bottom: 40rpx;
    }
    
    .add-btn {
      background-color: #1890FF;
      color: #FFFFFF;
      border: none;
      border-radius: 12rpx;
      padding: 24rpx 48rpx;
      font-size: 28rpx;
      font-weight: 500;
    }
  }
  
  .complaint-items {
    .complaint-item {
      background-color: #FFFFFF;
      border-radius: 20rpx;
      padding: 30rpx;
      margin-bottom: 24rpx;
      border: 1rpx solid #F0F0F0;
      transition: all 0.3s;
      
      &:active {
        transform: scale(0.98);
        background-color: #F8F9FA;
      }
      
      .complaint-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 24rpx;
        
        .complaint-title-section {
          flex: 1;
          
          .complaint-title {
            font-size: 30rpx;
            font-weight: 600;
            color: #262626;
            margin-bottom: 12rpx;
            display: block;
          }
          
          .complaint-status {
            display: inline-block;
            padding: 6rpx 16rpx;
            border-radius: 20rpx;
            
            .status-text {
              font-size: 22rpx;
              font-weight: 500;
            }
            
            &.status-pending {
              background-color: #FFF7E6;
              
              .status-text {
                color: #FA8C16;
              }
            }
            
            &.status-processing {
              background-color: #E6F7FF;
              
              .status-text {
                color: #1890FF;
              }
            }
            
            &.status-completed {
              background-color: #F6FFED;
              
              .status-text {
                color: #52C41A;
              }
            }
            
            &.status-rejected {
              background-color: #FFF2F0;
              
              .status-text {
                color: #FF4D4F;
              }
            }
          }
        }
        
        .complaint-time {
          .time-text {
            font-size: 22rpx;
            color: #8C8C8C;
          }
        }
      }
      
      .complaint-content {
        margin-bottom: 24rpx;
        
        .complaint-desc {
          font-size: 26rpx;
          color: #595959;
          line-height: 1.6;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
      }
      
      .complaint-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
        
        .complaint-info {
          .info-text {
            font-size: 22rpx;
            color: #8C8C8C;
          }
        }
        
        .complaint-actions {
          display: flex;
          align-items: center;
          
          .action-text {
            font-size: 24rpx;
            color: #1890FF;
            margin-right: 8rpx;
          }
        }
      }
      
      .progress-section {
        .progress-bar {
          width: 100%;
          height: 8rpx;
          background-color: #F0F0F0;
          border-radius: 4rpx;
          overflow: hidden;
          margin-bottom: 12rpx;
          
          .progress-fill {
            height: 100%;
            background-color: #1890FF;
            border-radius: 4rpx;
            transition: width 0.3s;
          }
        }
        
        .progress-text {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
    }
  }
}

.load-more {
  padding: 30rpx;
}
</style>