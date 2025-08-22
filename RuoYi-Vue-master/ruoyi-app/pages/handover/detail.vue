<template>
  <view class="container">
    <uni-nav-bar 
      title="交接详情" 
      left-icon="back" 
      @clickLeft="goBack"
      background-color="#1890FF"
      color="#fff"
    />
    
    <view class="detail-content">
      <view class="detail-card">
        <view class="card-header">
          <text class="title">交接信息</text>
        </view>
        <view class="card-body">
          <view class="info-item">
            <text class="label">交接标题：</text>
            <text class="value">{{ handoverInfo.title || '暂无数据' }}</text>
          </view>
          <view class="info-item">
            <text class="label">交接状态：</text>
            <text class="value">{{ getStatusText(handoverInfo.status) }}</text>
          </view>
          <view class="info-item">
            <text class="label">交接时间：</text>
            <text class="value">{{ handoverInfo.handoverTime || '暂无数据' }}</text>
          </view>
          <view class="info-item">
            <text class="label">交接内容：</text>
            <text class="value">{{ handoverInfo.content || '暂无数据' }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      handoverId: null,
      handoverInfo: {}
    }
  },
  onLoad(options) {
    if (options.id) {
      this.handoverId = options.id
      this.getHandoverDetail()
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    
    async getHandoverDetail() {
      try {
        // TODO: 调用交接详情接口
        // const result = await getHandoverDetail(this.handoverId)
        // this.handoverInfo = result.data
        
        // 模拟数据
        this.handoverInfo = {
          title: '物业交接事项',
          status: '1',
          handoverTime: '2025-01-25 10:00:00',
          content: '这里是交接详情内容...'
        }
      } catch (error) {
        uni.showToast({
          title: '获取详情失败',
          icon: 'error'
        })
      }
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

.detail-content {
  padding: 20rpx;
}

.detail-card {
  background: #fff;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.1);
  overflow: hidden;
}

.card-header {
  padding: 30rpx;
  background: linear-gradient(135deg, #1890FF, #40a9ff);
}

.title {
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
}

.card-body {
  padding: 30rpx;
}

.info-item {
  display: flex;
  margin-bottom: 20rpx;
  align-items: flex-start;
}

.label {
  color: #666;
  font-size: 28rpx;
  min-width: 140rpx;
  flex-shrink: 0;
}

.value {
  color: #333;
  font-size: 28rpx;
  flex: 1;
  line-height: 1.6;
}
</style>