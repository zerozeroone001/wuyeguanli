<template>
  <view class="container">
    <uni-nav-bar 
      title="交接报告" 
      left-icon="back" 
      @clickLeft="goBack"
      background-color="#1890FF"
      color="#fff"
    />
    
    <view class="content">
      <!-- 报告概览 -->
      <view class="overview-section">
        <view class="section-title">交接概览</view>
        <view class="overview-cards">
          <view class="overview-card">
            <text class="card-number">{{ reportData.totalItems || 0 }}</text>
            <text class="card-label">交接事项</text>
          </view>
          <view class="overview-card">
            <text class="card-number">{{ reportData.completedItems || 0 }}</text>
            <text class="card-label">已完成</text>
          </view>
          <view class="overview-card">
            <text class="card-number">{{ reportData.pendingItems || 0 }}</text>
            <text class="card-label">进行中</text>
          </view>
        </view>
      </view>
      
      <!-- 交接详情 -->
      <view class="detail-section">
        <view class="section-title">交接详情</view>
        <view class="detail-list">
          <view 
            v-for="item in reportData.items" 
            :key="item.id"
            class="detail-item"
          >
            <view class="item-info">
              <text class="item-title">{{ item.title }}</text>
              <view class="item-status" :class="'status-' + item.status">
                {{ getStatusText(item.status) }}
              </view>
            </view>
            <text class="item-desc">{{ item.description }}</text>
            <text class="item-time">{{ item.updateTime }}</text>
          </view>
        </view>
      </view>
      
      <!-- 操作按钮 -->
      <view class="action-section">
        <button class="action-btn primary" @click="exportReport">
          <uni-icons type="download" size="18" color="#fff" />
          <text>导出报告</text>
        </button>
        <button class="action-btn secondary" @click="shareReport">
          <uni-icons type="paperplane" size="18" color="#1890FF" />
          <text>分享报告</text>
        </button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      reportData: {}
    }
  },
  onLoad(options) {
    this.getReportData()
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    
    async getReportData() {
      try {
        // TODO: 调用交接报告接口
        // const result = await getHandoverReport()
        // this.reportData = result.data
        
        // 模拟数据
        this.reportData = {
          totalItems: 8,
          completedItems: 5,
          pendingItems: 3,
          items: [
            {
              id: 1,
              title: '物业管理权交接',
              description: '已完成管理权移交手续，相关证照已移交',
              status: '2',
              updateTime: '2025-01-25 10:00:00'
            },
            {
              id: 2,
              title: '设备设施交接',
              description: '正在进行设备清点和移交工作',
              status: '1',
              updateTime: '2025-01-24 16:30:00'
            },
            {
              id: 3,
              title: '财务账目交接',
              description: '财务账目审核中，预计本周完成',
              status: '1',
              updateTime: '2025-01-23 14:20:00'
            }
          ]
        }
      } catch (error) {
        uni.showToast({
          title: '获取报告失败',
          icon: 'error'
        })
      }
    },
    
    exportReport() {
      uni.showToast({
        title: '导出功能开发中',
        icon: 'none'
      })
    },
    
    shareReport() {
      uni.showToast({
        title: '分享功能开发中',
        icon: 'none'
      })
    },
    
    getStatusText(status) {
      const statusMap = {
        '0': '待交接',
        '1': '交接中',
        '2': '已完成'
      }
      return statusMap[status] || '未知状态'
    }
  }
}
</script>

<style scoped>
.container {
  background: #f5f5f5;
  min-height: 100vh;
}

.content {
  padding: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  border-left: 6rpx solid #1890FF;
  padding-left: 15rpx;
}

.overview-section {
  margin-bottom: 30rpx;
}

.overview-cards {
  display: flex;
  gap: 20rpx;
}

.overview-card {
  flex: 1;
  background: #fff;
  border-radius: 12rpx;
  padding: 30rpx 20rpx;
  text-align: center;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.1);
}

.card-number {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #1890FF;
  margin-bottom: 10rpx;
}

.card-label {
  font-size: 24rpx;
  color: #666;
}

.detail-section {
  margin-bottom: 30rpx;
}

.detail-list {
  background: #fff;
  border-radius: 12rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.1);
}

.detail-item {
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.detail-item:last-child {
  border-bottom: none;
}

.item-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.item-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  flex: 1;
}

.item-status {
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  color: #fff;
}

.status-0 {
  background: #faad14;
}

.status-1 {
  background: #1890ff;
}

.status-2 {
  background: #52c41a;
}

.item-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
  margin-bottom: 10rpx;
}

.item-time {
  font-size: 24rpx;
  color: #999;
}

.action-section {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10rpx;
  padding: 25rpx;
  border-radius: 12rpx;
  font-size: 28rpx;
  border: none;
}

.action-btn.primary {
  background: #1890FF;
  color: #fff;
}

.action-btn.secondary {
  background: #fff;
  color: #1890FF;
  border: 2rpx solid #1890FF;
}
</style>