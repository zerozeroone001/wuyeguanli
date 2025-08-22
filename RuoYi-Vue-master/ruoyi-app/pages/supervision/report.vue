<template>
  <view class="container">
    <uni-nav-bar 
      title="监督报告" 
      left-icon="back" 
      @clickLeft="goBack"
      background-color="#1890FF"
      color="#fff"
    />
    
    <view class="content">
      <!-- 报告统计 -->
      <view class="stats-section">
        <view class="section-title">监督统计</view>
        <view class="stats-cards">
          <view class="stats-card">
            <text class="card-number">{{ reportData.totalSupervisions || 0 }}</text>
            <text class="card-label">监督事项</text>
          </view>
          <view class="stats-card">
            <text class="card-number">{{ reportData.completedSupervisions || 0 }}</text>
            <text class="card-label">已完成</text>
          </view>
          <view class="stats-card">
            <text class="card-number">{{ reportData.pendingSupervisions || 0 }}</text>
            <text class="card-label">进行中</text>
          </view>
        </view>
      </view>
      
      <!-- 监督详情 -->
      <view class="detail-section">
        <view class="section-title">监督详情</view>
        <view class="detail-list">
          <view 
            v-for="item in reportData.supervisions" 
            :key="item.id"
            class="detail-item"
          >
            <view class="item-header">
              <text class="item-title">{{ item.title }}</text>
              <view class="item-status" :class="'status-' + item.status">
                {{ getStatusText(item.status) }}
              </view>
            </view>
            <text class="item-desc">{{ item.description }}</text>
            <view class="item-meta">
              <text class="item-time">{{ item.updateTime }}</text>
              <text class="item-type">{{ item.type }}</text>
            </view>
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
  onLoad() {
    this.getSupervisionReport()
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    
    async getSupervisionReport() {
      try {
        // TODO: 调用监督报告接口
        // const result = await getSupervisionReport()
        // this.reportData = result.data
        
        // 模拟数据
        this.reportData = {
          totalSupervisions: 12,
          completedSupervisions: 8,
          pendingSupervisions: 4,
          supervisions: [
            {
              id: 1,
              title: '物业服务质量监督',
              description: '对物业公司服务质量进行全面监督检查',
              status: '2',
              type: '服务监督',
              updateTime: '2025-01-25 10:00:00'
            },
            {
              id: 2,
              title: '公共设施维护监督',
              description: '监督公共设施的维护保养工作执行情况',
              status: '1',
              type: '设施监督',
              updateTime: '2025-01-24 16:30:00'
            },
            {
              id: 3,
              title: '财务收支监督',
              description: '对物业费收支情况进行监督审核',
              status: '1',
              type: '财务监督',
              updateTime: '2025-01-23 14:20:00'
            },
            {
              id: 4,
              title: '安全管理监督',
              description: '检查小区安全管理措施落实情况',
              status: '2',
              type: '安全监督',
              updateTime: '2025-01-22 09:15:00'
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
        '0': '待处理',
        '1': '监督中',
        '2': '已完成',
        '3': '已关闭'
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

.stats-section {
  margin-bottom: 30rpx;
}

.stats-cards {
  display: flex;
  gap: 20rpx;
}

.stats-card {
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

.item-header {
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

.status-3 {
  background: #999;
}

.item-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
  margin-bottom: 15rpx;
}

.item-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-time {
  font-size: 24rpx;
  color: #999;
}

.item-type {
  font-size: 24rpx;
  color: #1890FF;
  background: rgba(24, 144, 255, 0.1);
  padding: 4rpx 12rpx;
  border-radius: 12rpx;
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