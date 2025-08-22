<template>
  <view class="container">
    <uni-nav-bar 
      title="物业交接" 
      left-icon="back" 
      @clickLeft="goBack"
      background-color="#1890FF"
      color="#fff"
    />
    
    <view class="content">
      <!-- 搜索栏 -->
      <view class="search-bar">
        <uni-search-bar 
          v-model="searchKeyword" 
          placeholder="搜索交接事项"
          @search="handleSearch"
        />
      </view>
      
      <!-- 交接列表 -->
      <view class="handover-list">
        <view 
          v-for="item in handoverList" 
          :key="item.id"
          class="handover-item"
          @click="viewDetail(item)"
        >
          <view class="item-header">
            <text class="title">{{ item.title }}</text>
            <view class="status" :class="'status-' + item.status">
              {{ getStatusText(item.status) }}
            </view>
          </view>
          <view class="item-content">
            <text class="content-text">{{ item.content }}</text>
          </view>
          <view class="item-footer">
            <text class="time">{{ item.createTime }}</text>
            <view class="arrow">
              <uni-icons type="arrowright" size="16" color="#ccc" />
            </view>
          </view>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view v-if="handoverList.length === 0" class="empty-state">
        <uni-icons type="inbox" size="80" color="#ccc" />
        <text class="empty-text">暂无交接事项</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: '',
      handoverList: []
    }
  },
  onLoad() {
    this.getHandoverList()
  },
  onPullDownRefresh() {
    this.getHandoverList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    
    async getHandoverList() {
      try {
        // TODO: 调用交接列表接口
        // const result = await getHandoverList({ keyword: this.searchKeyword })
        // this.handoverList = result.data
        
        // 模拟数据
        this.handoverList = [
          {
            id: 1,
            title: '物业管理权交接',
            content: '原物业公司向新物业公司移交管理权相关事项',
            status: '1',
            createTime: '2025-01-25 10:00:00'
          },
          {
            id: 2,
            title: '设备设施交接',
            content: '小区内各类设备设施的交接清单及维护记录',
            status: '0',
            createTime: '2025-01-20 14:30:00'
          }
        ]
      } catch (error) {
        uni.showToast({
          title: '获取列表失败',
          icon: 'error'
        })
      }
    },
    
    handleSearch() {
      this.getHandoverList()
    },
    
    viewDetail(item) {
      uni.navigateTo({
        url: `/pages/handover/detail?id=${item.id}`
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

.search-bar {
  margin-bottom: 20rpx;
}

.handover-list {
  margin-bottom: 20rpx;
}

.handover-item {
  background: #fff;
  border-radius: 12rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.1);
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  flex: 1;
}

.status {
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

.item-content {
  margin-bottom: 15rpx;
}

.content-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.time {
  font-size: 24rpx;
  color: #999;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
  margin-top: 20rpx;
}
</style>