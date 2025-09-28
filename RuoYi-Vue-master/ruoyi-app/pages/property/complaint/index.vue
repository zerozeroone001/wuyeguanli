<template>
  <view class="complaint-container">
    <!-- 顶部统计 -->
    <view class="stats-section">
      <view class="stat-item">
        <text class="stat-number">{{ stats.total }}</text>
        <text class="stat-label">累计投诉</text>
      </view>
      <view class="stat-item">
        <text class="stat-number pending">{{ stats.pending }}</text>
        <text class="stat-label">待处理</text>
      </view>
      <view class="stat-item">
        <text class="stat-number processing">{{ stats.processing }}</text>
        <text class="stat-label">处理中</text>
      </view>
      <view class="stat-item">
        <text class="stat-number completed">{{ stats.completed }}</text>
        <text class="stat-label">已完成</text>
      </view>
    </view>

    <!-- 筛选标签 -->
    <view class="filter-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'all' }"
        @click="switchTab('all')"
      >
        全部
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'pending' }"
        @click="switchTab('pending')"
      >
        待处理
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'processing' }"
        @click="switchTab('processing')"
      >
        处理中
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'completed' }"
        @click="switchTab('completed')"
      >
        已完成
      </view>
    </view>

    <!-- 投诉列表 -->
    <view class="complaint-list">
      <view 
        class="complaint-item" 
        v-for="complaint in complaintList" 
        :key="complaint.complaintId"
        @click="viewComplaint(complaint)"
      >
        <view class="complaint-header">
          <view class="complaint-info">
            <text class="complaint-title">{{ complaint.complaintTitle }}</text>
            <view class="complaint-meta">
              <text class="complaint-no">编号：{{ complaint.complaintNo || '未生成' }}</text>
              <text class="complaint-time">{{ complaint.createTime }}</text>
            </view>
          </view>
          <view class="complaint-status" :class="getStatusClass(complaint.status)">
            {{ getStatusText(complaint.status) }}
          </view>
        </view>
        
        <view class="complaint-content">
          <text class="content-text">{{ complaint.complaintContent }}</text>
        </view>
        
        <view class="complaint-details">
          <view class="detail-item">
            <uni-icons type="list" size="14" color="#8C8C8C" />
            <text>{{ getTypeText(complaint.complaintType) }}</text>
          </view>
          <view class="detail-item">
            <uni-icons type="flag" size="14" color="#8C8C8C" />
            <text>{{ getUrgencyText(complaint.urgency) }}</text>
          </view>
          <view class="detail-item" v-if="complaint.handlerName">
            <uni-icons type="person" size="14" color="#8C8C8C" />
            <text>处理人：{{ complaint.handlerName }}</text>
          </view>
        </view>
        
        <!-- 操作按钮 -->
        <view class="complaint-actions" v-if="complaint.status === '2'">
          <button class="evaluate-btn" @click.stop="evaluateComplaint(complaint)">
            评价
          </button>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="complaintList.length === 0">
      <uni-icons type="chat" size="80" color="#D9D9D9" />
      <text class="empty-text">暂无投诉记录</text>
      <button class="add-btn" @click="addComplaint">提交投诉</button>
    </view>

    <!-- 悬浮按钮 -->
    <view class="fab-button" @click="addComplaint">
      <uni-icons type="plus" size="24" color="#FFFFFF" />
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'all',
      complaintList: [],
      stats: {
        total: 0,
        pending: 0,
        processing: 0,
        completed: 0
      }
    }
  },
  onLoad() {
    this.loadComplaints()
    this.loadStats()
  },
  onPullDownRefresh() {
    this.loadComplaints()
    this.loadStats()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  onShow() {
    // 页面显示时刷新数据
    this.loadComplaints()
    this.loadStats()
  },
  methods: {
    loadComplaints() {
      // 模拟数据
      const mockData = {
        all: [
          {
            complaintId: 1,
            complaintNo: 'TS202401001',
            complaintTitle: '电梯故障频繁',
            complaintContent: '2号楼电梯经常出现故障，按钮失灵，给业主出行造成很大不便，希望能尽快维修。',
            complaintType: '2',
            urgency: '1',
            status: '1',
            createTime: '2024-01-10 09:30',
            handlerName: '张维修',
            expectedTime: '2'
          },
          {
            complaintId: 2,
            complaintNo: 'TS202401002',
            complaintTitle: '小区噪音扰民',
            complaintContent: '楼上住户深夜装修，噪音很大，影响休息，多次沟通无效。',
            complaintType: '3',
            urgency: '2',
            status: '2',
            createTime: '2024-01-08 22:15',
            handlerName: '李管家',
            expectedTime: '3',
            satisfaction: '4'
          },
          {
            complaintId: 3,
            complaintNo: 'TS202401003',
            complaintTitle: '停车位被占用',
            complaintContent: '我的固定停车位经常被其他车辆占用，物业管理不到位。',
            complaintType: '1',
            urgency: '2',
            status: '0',
            createTime: '2024-01-12 16:45',
            expectedTime: '2'
          },
          {
            complaintId: 4,
            complaintNo: 'TS202401004',
            complaintTitle: '垃圾清理不及时',
            complaintContent: '小区垃圾桶经常满溢，清理不及时，影响环境卫生。',
            complaintType: '3',
            urgency: '3',
            status: '2',
            createTime: '2024-01-05 14:20',
            handlerName: '王清洁',
            expectedTime: '4',
            satisfaction: '5'
          }
        ],
        pending: [],
        processing: [],
        completed: []
      }
      
      // 根据状态筛选数据
      if (this.activeTab === 'all') {
        this.complaintList = mockData.all
      } else {
        const statusMap = {
          pending: '0',
          processing: '1',
          completed: '2'
        }
        this.complaintList = mockData.all.filter(item => item.status === statusMap[this.activeTab])
      }
    },
    
    loadStats() {
      // 模拟统计数据
      this.stats = {
        total: 24,
        pending: 3,
        processing: 5,
        completed: 16
      }
    },
    
    switchTab(tab) {
      this.activeTab = tab
      this.loadComplaints()
    },
    
    getStatusClass(status) {
      const classMap = {
        '0': 'status-pending',
        '1': 'status-processing',
        '2': 'status-completed',
        '3': 'status-closed'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        '0': '待处理',
        '1': '处理中',
        '2': '已完成',
        '3': '已关闭'
      }
      return textMap[status] || '未知'
    },
    
    getTypeText(type) {
      const typeMap = {
        '1': '物业服务问题',
        '2': '设施设备故障',
        '3': '环境卫生问题',
        '4': '安全管理问题',
        '5': '收费争议',
        '6': '其他问题'
      }
      return typeMap[type] || '未知类型'
    },
    
    getUrgencyText(urgency) {
      const urgencyMap = {
        '1': '紧急',
        '2': '普通',
        '3': '一般'
      }
      return urgencyMap[urgency] || '普通'
    },
    
    viewComplaint(complaint) {
      uni.navigateTo({
        url: `/pages/property/complaint/detail?id=${complaint.complaintId}`
      })
    },
    
    addComplaint() {
      uni.navigateTo({
        url: '/pages/property/complaint/add'
      })
    },
    
    evaluateComplaint(complaint) {
      uni.navigateTo({
        url: `/pages/property/complaint/evaluate?id=${complaint.complaintId}`
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

.complaint-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.stats-section {
  display: flex;
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx 20rpx;
  border: 1rpx solid #F0F0F0;
  
  .stat-item {
    flex: 1;
    text-align: center;
    
    .stat-number {
      display: block;
      font-size: 32rpx;
      font-weight: 600;
      color: #1890FF;
      margin-bottom: 8rpx;
      
      &.pending {
        color: #FA8C16;
      }
      
      &.processing {
        color: #1890FF;
      }
      
      &.completed {
        color: #52C41A;
      }
    }
    
    .stat-label {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
}

.filter-tabs {
  display: flex;
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 10rpx;
  border: 1rpx solid #F0F0F0;
  
  .tab-item {
    flex: 1;
    text-align: center;
    padding: 16rpx 0;
    font-size: 26rpx;
    color: #8C8C8C;
    border-radius: 20rpx;
    transition: all 0.3s ease;
    
    &.active {
      background: #1890FF;
      color: #FFFFFF;
      font-weight: 600;
    }
  }
}

.complaint-list {
  padding: 0 20rpx;
}

.complaint-item {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  border: 1rpx solid #F0F0F0;
  
  &:active {
    background: #F8F9FA;
  }
}

.complaint-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
  
  .complaint-info {
    flex: 1;
    margin-right: 20rpx;
    
    .complaint-title {
      display: block;
      font-size: 30rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-bottom: 12rpx;
    }
    
    .complaint-meta {
      display: flex;
      flex-direction: column;
      gap: 8rpx;
      
      text {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .complaint-status {
    padding: 8rpx 16rpx;
    border-radius: 16rpx;
    font-size: 22rpx;
    font-weight: 500;
    white-space: nowrap;
    
    &.status-pending {
      background: #FFF2E8;
      color: #FA8C16;
    }
    
    &.status-processing {
      background: #E6F7FF;
      color: #1890FF;
    }
    
    &.status-completed {
      background: #F6FFED;
      color: #52C41A;
    }
    
    &.status-closed {
      background: #F5F5F5;
      color: #8C8C8C;
    }
  }
}

.complaint-content {
  margin-bottom: 20rpx;
  
  .content-text {
    font-size: 26rpx;
    color: #595959;
    line-height: 1.6;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
  }
}

.complaint-details {
  display: flex;
  flex-wrap: wrap;
  gap: 24rpx;
  margin-bottom: 20rpx;
  
  .detail-item {
    display: flex;
    align-items: center;
    
    text {
      margin-left: 8rpx;
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
}

.complaint-actions {
  display: flex;
  justify-content: flex-end;
  
  .evaluate-btn {
    padding: 12rpx 24rpx;
    background: #1890FF;
    color: #FFFFFF;
    font-size: 24rpx;
    border-radius: 16rpx;
    border: none;
    
    &:active {
      background: #096DD9;
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
    margin: 30rpx 0;
    font-size: 28rpx;
    color: #8C8C8C;
  }
  
  .add-btn {
    padding: 20rpx 40rpx;
    background: #1890FF;
    color: #FFFFFF;
    font-size: 28rpx;
    border-radius: 24rpx;
    border: none;
    
    &:active {
      background: #096DD9;
    }
  }
}

.fab-button {
  position: fixed;
  bottom: 120rpx;
  right: 40rpx;
  width: 112rpx;
  height: 112rpx;
  background: #1890FF;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(24, 144, 255, 0.3);
  z-index: 999;
  
  &:active {
    background: #096DD9;
    transform: scale(0.95);
  }
}
</style>
 
 