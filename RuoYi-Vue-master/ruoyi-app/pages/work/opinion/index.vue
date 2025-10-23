<template>
  <view class="opinion-container">
    <!-- 页面头部 -->
    <view class="page-header">
      <view class="header-content">
        <text class="page-title">问卷调查</text>
        <text class="page-subtitle">参与社区意见征询</text>
      </view>
    </view>

    <!-- 问卷列表 -->
    <view class="poll-list" v-if="pollList.length > 0">
      <view 
        class="poll-item" 
        v-for="(poll, index) in pollList" 
        :key="poll.pollId"
        @click="handlePollClick(poll)"
      >
        <view class="poll-header">
          <view class="poll-title-section">
            <text class="poll-title">{{ poll.title }}</text>
            <view class="poll-status" :class="poll.statusClass">
              <text class="status-text">{{ poll.statusText }}</text>
            </view>
          </view>
          <view class="poll-time">
            <view class="time-item" v-if="poll.startTime">
              <uni-icons type="calendar" size="14" color="#8C8C8C" />
              <text class="time-text">开始：{{ formatDate(poll.startTime) }}</text>
            </view>
            <view class="time-item" v-if="poll.endTime">
              <uni-icons type="clock" size="14" color="#8C8C8C" />
              <text class="time-text">截止：{{ formatDate(poll.endTime) }}</text>
            </view>
          </view>
        </view>
        
        <view class="poll-content">
          <text class="poll-description">{{ poll.description }}</text>
        </view>
        
        <view class="poll-footer">
          <view class="poll-info">
            <text class="form-name">{{ poll.formName }}</text>
          </view>
          <view class="poll-action">
            <uni-icons type="right" size="16" color="#C0C4CC" />
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-else-if="!loading">
      <view class="empty-icon">
        <uni-icons type="compose" size="80" color="#D9D9D9" />
      </view>
      <text class="empty-text">暂无进行中的问卷</text>
      <text class="empty-desc">请耐心等待新的问卷发布</text>
    </view>

    <!-- 加载状态 -->
    <view class="loading-state" v-if="loading">
      <uni-load-more status="loading" />
    </view>

    <!-- 底部安全距离 -->
    <view class="safe-area-bottom"></view>
  </view>
</template>

<script>
import { listSuggestionPolls } from '@/api/poll.js'
import { formatDate } from '@/utils/common.js'

export default {
  components: {
    'uni-icons': () => import('@/uni_modules/uni-icons/components/uni-icons/uni-icons.vue'),
    'uni-load-more': () => import('@/uni_modules/uni-load-more/components/uni-load-more/uni-load-more.vue')
  },
  data() {
    return {
      pollList: [],
      loading: false
    }
  },
  
  onLoad() {
    this.loadPollList()
  },
  
  onShow() {
    // 每次显示页面时刷新列表
    this.loadPollList()
  },
  
  methods: {
    async loadPollList() {
      this.loading = true
      try {
        const response = await listSuggestionPolls()
        this.pollList = (response.data || []).map(poll => {
          const now = new Date()
          const startTime = poll.startTime ? new Date(poll.startTime) : null
          const endTime = poll.endTime ? new Date(poll.endTime) : null
          
          let statusClass = 'status-active'
          let statusText = '进行中'
          
          if (startTime && now < startTime) {
            statusClass = 'status-pending'
            statusText = '未开始'
          } else if (endTime && now > endTime) {
            statusClass = 'status-ended'
            statusText = '已结束'
          }
          
          return {
            ...poll,
            statusClass,
            statusText
          }
        })
      } catch (error) {
        console.error('加载问卷列表失败:', error)
        uni.showToast({
          title: '加载失败，请重试',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    handlePollClick(poll) {
      // 检查问卷是否在有效期内
      const now = new Date()
      const startTime = poll.startTime ? new Date(poll.startTime) : null
      const endTime = poll.endTime ? new Date(poll.endTime) : null
      
      if (startTime && now < startTime) {
        uni.showToast({
          title: '问卷尚未开始',
          icon: 'none'
        })
        return
      }
      
      if (endTime && now > endTime) {
        uni.showToast({
          title: '问卷已结束',
          icon: 'none'
        })
        return
      }
      
      // 跳转到问卷详情页
      uni.navigateTo({
        url: `/pages/work/opinion/detail?pollId=${poll.pollId}`,
        fail: (err) => {
          console.error('导航失败:', err)
          uni.showToast({
            title: '页面跳转失败',
            icon: 'none'
          })
        }
      })
    },
    
    
    formatDate(dateString) {
      if (!dateString) return ''
      return formatDate(dateString, 'MM-dd hh:mm')
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.opinion-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.page-header {
  background: linear-gradient(135deg, #1890FF 0%, #40A9FF 100%);
  padding: 60rpx 30rpx 40rpx;
  
  .header-content {
    text-align: center;
    
    .page-title {
      display: block;
      font-size: 36rpx;
      font-weight: bold;
      color: #FFFFFF;
      margin-bottom: 12rpx;
    }
    
    .page-subtitle {
      font-size: 24rpx;
      color: rgba(255, 255, 255, 0.8);
    }
  }
}

.poll-list {
  padding: 30rpx;
}

.poll-item {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 24rpx;
  border: 1rpx solid #F0F0F0;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
  
  &:active {
    background-color: #F8F9FA;
  }
}

.poll-header {
  margin-bottom: 24rpx;
  
  .poll-title-section {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 16rpx;
    
    .poll-title {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
    }
    
    .poll-status {
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      
      .status-text {
        font-size: 22rpx;
        font-weight: 500;
      }
      
      &.status-active {
        background: #F6FFED;
        .status-text {
          color: #52C41A;
        }
      }
      
      &.status-pending {
        background: #FFF2E8;
        .status-text {
          color: #FA8C16;
        }
      }
      
      &.status-ended {
        background: #FFF1F0;
        .status-text {
          color: #FF4D4F;
        }
      }
    }
  }
  
  .poll-time {
    .time-item {
      display: flex;
      align-items: center;
      margin-bottom: 8rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .time-text {
        margin-left: 8rpx;
        font-size: 24rpx;
        color: #8C8C8C;
      }
    }
  }
}

.poll-content {
  margin-bottom: 24rpx;
  
  .poll-description {
    font-size: 26rpx;
    color: #595959;
    line-height: 1.6;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
}

.poll-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .poll-info {
    .form-name {
      font-size: 22rpx;
      color: #8C8C8C;
      background: #F8F9FA;
      padding: 6rpx 12rpx;
      border-radius: 12rpx;
    }
  }
  
  .poll-action {
    display: flex;
    align-items: center;
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 60rpx;
  
  .empty-icon {
    margin-bottom: 32rpx;
  }
  
  .empty-text {
    font-size: 28rpx;
    color: #8C8C8C;
    margin-bottom: 12rpx;
  }
  
  .empty-desc {
    font-size: 24rpx;
    color: #BFBFBF;
  }
}

.loading-state {
  display: flex;
  justify-content: center;
  padding: 60rpx;
}

.safe-area-bottom {
  height: 120rpx;
}
</style>
