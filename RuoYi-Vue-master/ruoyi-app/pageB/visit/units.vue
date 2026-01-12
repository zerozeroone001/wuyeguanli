<template>
  <view class="container">
    <view class="header">
      <text class="title">选择单元</text>
      <text class="subtitle">{{ communityName }} - {{ buildingName }}</text>
    </view>

    <view class="unit-list">
      <view 
        v-for="(unit, index) in unitList" 
        :key="index"
        class="unit-item"
        @click="selectUnit(unit)"
      >
        <view class="unit-info">
          <text class="unit-name">{{ unit || '无单元' }}</text>
        </view>
        <uni-icons type="right" size="20" color="#C0C4CC" />
      </view>
    </view>

    <view v-if="unitList.length === 0 && !loading" class="empty">
      <text>暂无单元数据</text>
    </view>

    <view v-if="loading" class="loading">
      <uni-icons type="spinner-cycle" size="30" color="#1890FF" />
      <text>加载中...</text>
    </view>
  </view>
</template>

<script>
import { getUnitList } from '@/api/visit'

export default {
  data() {
    return {
      communityId: null,
      communityName: '',
      buildingName: '',
      unitList: [],
      loading: false
    }
  },
  onLoad(options) {
    this.communityId = options.communityId
    this.communityName = decodeURIComponent(options.communityName || '')
    this.buildingName = decodeURIComponent(options.buildingName || '')
    this.loadUnitList()
  },
  methods: {
    async loadUnitList() {
      if (!this.communityId || !this.buildingName) {
        uni.showToast({
          title: '参数缺失',
          icon: 'none'
        })
        return
      }

      this.loading = true
      try {
        const response = await getUnitList(this.communityId, this.buildingName)
        if (response.code === 200) {
          this.unitList = response.data || []
        } else {
          uni.showToast({
            title: response.msg || '加载失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('加载单元列表失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    selectUnit(unit) {
      // 跳转到房间列表页面
      uni.navigateTo({
        url: `/pageB/visit/rooms?communityId=${this.communityId}&communityName=${encodeURIComponent(this.communityName)}&buildingName=${encodeURIComponent(this.buildingName)}&unitName=${encodeURIComponent(unit || '')}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  background-color: #F5F7FA;
}

.header {
  background: linear-gradient(135deg, #1890FF 0%, #096DD9 100%);
  padding: 40rpx 30rpx;
  color: white;
  
  .title {
    display: block;
    font-size: 36rpx;
    font-weight: bold;
    margin-bottom: 12rpx;
  }
  
  .subtitle {
    font-size: 26rpx;
    opacity: 0.9;
  }
}

.unit-list {
  padding: 20rpx 30rpx;
  
  .unit-item {
    background: white;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
    
    .unit-info {
      flex: 1;
      
      .unit-name {
        font-size: 32rpx;
        font-weight: 500;
        color: #262626;
      }
    }
  }
}

.empty {
  text-align: center;
  padding: 100rpx 0;
  color: #8C8C8C;
  font-size: 28rpx;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
  
  text {
    margin-top: 20rpx;
    color: #8C8C8C;
    font-size: 28rpx;
  }
}
</style>
