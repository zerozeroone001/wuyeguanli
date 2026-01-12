<template>
  <view class="container">
    <view class="header">
      <text class="title">房间列表</text>
      <text class="subtitle">{{ communityName }} - {{ buildingName }} {{ unitName }}</text>
    </view>

    <!-- 单元选择 -->
    <view class="unit-selector" v-if="unitList.length > 0">
      <text class="label">选择单元:</text>
      <view class="unit-tags">
        <view 
          v-for="(unit, index) in unitList" 
          :key="index"
          class="unit-tag"
          :class="{ 'active': selectedUnit === unit }"
          @click="selectUnit(unit)"
        >
          <text>{{ unit }}</text>
        </view>
      </view>
    </view>

    <!-- 会议选择 -->
    <view class="meeting-selector">
      <text class="label">选择会议:</text>
      <picker mode="selector" :range="meetingList" range-key="meetingTitle" @change="onMeetingChange">
        <view class="picker">
          <text>{{ selectedMeeting ? selectedMeeting.meetingTitle : '请选择会议' }}</text>
          <uni-icons type="down" size="16" color="#8C8C8C" />
        </view>
      </picker>
    </view>

    <view class="room-list">
      <view 
        v-for="(room, index) in roomList" 
        :key="index"
        class="room-item"
      >
        <view class="room-header">
          <text class="room-number">{{ room.ownerName }}</text>
          <view class="status-badge" :class="{
            'status-success': room.hasVoted,
            'status-default': !room.hasVoted
          }">
            <text v-if="room.hasVoted">已投票</text>
            <text v-else>未投票</text>
          </view>
        </view>
        
        <view class="owner-info">
          <view class="info-row">
            <text class="label">房号:</text>
            <text class="value">{{ room.roomNumber || '暂无' }}</text>
          </view>
          <view class="info-row">
            <text class="label">电话:</text>
            <text class="value">{{ room.phone || '暂无' }}</text>
          </view>
        </view>

        <view class="action-buttons" v-if="selectedMeeting">
          <button 
            class="btn btn-status" 
            :class="{
              'status-default': !room.offlineStatus || room.offlineStatus === 0,
              'status-success': room.offlineStatus === 7,
              'status-warning': room.offlineStatus === 3 || room.offlineStatus === 4,
              'status-info': room.offlineStatus === 5 || room.offlineStatus === 6
            }"
            size="mini"
            @click="showStatusPicker(room)"
            :disabled="!room.userId"
          >
            {{ getStatusText(room.offlineStatus) }}
          </button>
        </view>
      </view>
    </view>

    <view v-if="roomList.length === 0 && !loading" class="empty">
      <text>暂无房间数据</text>
    </view>

    <view v-if="loading" class="loading">
      <uni-icons type="spinner-cycle" size="30" color="#1890FF" />
      <text>加载中...</text>
    </view>
    
    <!-- 状态选择弹窗 -->
    <view v-if="showPicker" class="picker-mask" @click="closePicker">
      <view class="picker-content" @click.stop>
        <view class="picker-header">
          <text class="picker-title">选择线下拜访状态</text>
          <view class="picker-close" @click="closePicker">
            <uni-icons type="close" size="20" color="#8C8C8C" />
          </view>
        </view>
        <view class="picker-options">
          <view 
            v-for="(option, index) in statusOptions" 
            :key="index"
            class="picker-option"
            :class="{ 'active': currentRoom && currentRoom.offlineStatus === option.value }"
            @click="selectStatus(option.value)"
          >
            <text>{{ option.label }}</text>
            <uni-icons v-if="currentRoom && currentRoom.offlineStatus === option.value" type="checkmarkempty" size="20" color="#1890FF" />
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getRoomList, updateOfflineStatus, getUnitList } from '@/api/visit'
import { listMeeting } from '@/api/property/meeting'

export default {
  data() {
    return {
      communityId: null,
      communityName: '',
      buildingName: '',
      unitName: '',
      unitList: [],
      selectedUnit: '',
      roomList: [],
      meetingList: [],
      selectedMeeting: null,
      loading: false,
      showPicker: false,
      currentRoom: null,
      statusOptions: [
        { value: 0, label: '未送' },
        { value: 3, label: '已送无人' },
        { value: 4, label: '已送未收' },
        { value: 5, label: '已收未投' },
        { value: 6, label: '已投待唱' },
        { value: 7, label: '已唱' }
      ]
    }
  },
  onLoad(options) {
    this.communityId = options.communityId
    this.communityName = decodeURIComponent(options.communityName || '')
    this.buildingName = decodeURIComponent(options.buildingName || '')
    this.unitName = decodeURIComponent(options.unitName || '')
    
    // 先加载单元列表,再加载会议列表
    this.loadUnitList()
  },
  methods: {
    async loadUnitList() {
      if (!this.communityId || !this.buildingName) {
        this.loadMeetingList()
        return
      }

      try {
        const response = await getUnitList(this.communityId, this.buildingName)
        if (response.code === 200) {
          this.unitList = response.data || []
          if (this.unitList.length > 0) {
            // 如果有传入的单元名,使用传入的,否则使用第一个
            this.selectedUnit = this.unitName || this.unitList[0]
          }
        }
      } catch (error) {
        console.error('加载单元列表失败:', error)
      } finally {
        // 无论是否有单元,都继续加载会议列表
        this.loadMeetingList()
      }
    },
    
    selectUnit(unit) {
      this.selectedUnit = unit
      this.loadRoomList()
    },
    
    async loadMeetingList() {
      this.loading = true
      try {
        const response = await listMeeting({
          pageNum: 1,
          pageSize: 100,
          status: 1 // 只显示进行中的会议
        })
        if (response.code === 200) {
          this.meetingList = response.rows || []
          if (this.meetingList.length > 0) {
            this.selectedMeeting = this.meetingList[0]
            this.loading = false
            this.loadRoomList()
          }
        }
      } catch (error) {
        this.loading = false
        console.error('加载会议列表失败:', error)
      }
    },
    
    onMeetingChange(e) {
      const index = e.detail.value
      this.selectedMeeting = this.meetingList[index]
      this.loadRoomList()
    },
    
    async loadRoomList() {
      if (!this.communityId || !this.buildingName || !this.selectedMeeting) {
        return
      }

      this.loading = true
      try {
        // 使用选中的单元,如果没有单元列表则使用空字符串
        const unitName = this.unitList.length > 0 ? this.selectedUnit : ''
        const response = await getRoomList(
          this.communityId,
          this.buildingName,
          unitName,
          this.selectedMeeting.meetingId
        )
        console.log('接口返回数据:', response)
        if (response.code === 200) {
          console.log('response.data:', response.data)
          console.log('response.rows:', response.rows)

          // 兼容两种数据格式
          const dataList = response.rows || response.data || []
          console.log('获取到的数据列表:', dataList)
          console.log('数据列表类型:', typeof dataList)
          console.log('是否为数组:', Array.isArray(dataList))

          this.roomList = dataList
          console.log('赋值后 this.roomList:', this.roomList)
          console.log('赋值后 this.roomList.length:', this.roomList.length)

          // 强制更新视图
          this.$forceUpdate()

          if (this.roomList.length > 0) {
            console.log('第一个房间对象:', JSON.stringify(this.roomList[0]))
          }
        } else {
          uni.showToast({
            title: response.msg || '加载失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('加载房间列表失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    async updateStatus(room, status) {
      if (!room.userId) {
        uni.showToast({
          title: '该房间暂无业主信息',
          icon: 'none'
        })
        return
      }

      try {

        const response = await updateOfflineStatus({
          meetingId: this.selectedMeeting.meetingId,
          userId: room.userId,
          offlineStatus: status,
        })
        
        if (response.code === 200) {
          uni.showToast({
            title: '更新成功',
            icon: 'success'
          })
          // 重新加载列表
          this.loadRoomList()
        } else {
          uni.showToast({
            title: response.msg || '更新失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('更新状态失败:', error)
        uni.showToast({
          title: '更新失败',
          icon: 'none'
        })
      }
    },
    
    getStatusText(status) {
      const option = this.statusOptions.find(opt => opt.value === status)
      return option ? option.label : '未送'
    },
    
    getStatusClass(status) {
      if (status === 0 || !status) return 'status-default'
      if (status === 7) return 'status-success' // 已唱
      if (status === 3 || status === 4) return 'status-warning' // 已送无人、已送未收
      return 'status-info' // 其他状态
    },
    
    showStatusPicker(room) {
      this.currentRoom = room
      this.showPicker = true
    },
    
    closePicker() {
      this.showPicker = false
      this.currentRoom = null
    },
    
    async selectStatus(status) {
      if (!this.currentRoom) return
      
      // 先保存当前房间对象，避免closePicker后变为null
      const room = this.currentRoom
      this.closePicker()
      await this.updateStatus(room, status)
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  background-color: #F5F7FA;
  padding-bottom: 40rpx;
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

.unit-selector {
  background: white;
  margin: 20rpx 30rpx 0;
  padding: 24rpx;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  
  .label {
    font-size: 28rpx;
    color: #262626;
    margin-bottom: 16rpx;
    display: block;
  }
  
  .unit-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 16rpx;
    
    .unit-tag {
      padding: 12rpx 24rpx;
      border-radius: 8rpx;
      background: #F5F7FA;
      color: #595959;
      font-size: 26rpx;
      transition: all 0.3s;
      
      &.active {
        background: #1890FF;
        color: white;
      }
    }
  }
}

.meeting-selector {
  background: white;
  margin: 20rpx 30rpx;
  padding: 24rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  
  .label {
    font-size: 28rpx;
    color: #262626;
    margin-right: 20rpx;
  }
  
  .picker {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12rpx 20rpx;
    background: #F5F7FA;
    border-radius: 8rpx;
    
    text {
      font-size: 28rpx;
      color: #262626;
    }
  }
}

.room-list {
  padding: 0 30rpx;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  
  .room-item {
    background: white;
    border-radius: 16rpx;
    padding: 20rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
    width: calc(50% - 10rpx);
    
    .room-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 16rpx;
      padding-bottom: 12rpx;
      border-bottom: 1rpx solid #F0F0F0;
      
      .room-number {
        font-size: 28rpx;
        font-weight: bold;
        color: #262626;
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-right: 8rpx;
      }
      
      .status-badge {
        padding: 6rpx 16rpx;
        border-radius: 16rpx;
        font-size: 22rpx;
        flex-shrink: 0;
        
        &.status-default {
          background: #F0F0F0;
          color: #8C8C8C;
        }
        
        &.status-success {
          background: #F6FFED;
          color: #52C41A;
        }
        
        &.status-fail {
          background: #FFF1F0;
          color: #FF4D4F;
        }
      }
    }
    
    .owner-info {
      margin-bottom: 16rpx;
      
      .info-row {
        display: flex;
        align-items: center;
        margin-bottom: 10rpx;
        
        &:last-child {
          margin-bottom: 0;
        }
        
        .label {
          font-size: 24rpx;
          color: #8C8C8C;
          width: 80rpx;
          flex-shrink: 0;
        }
        
        .value {
          flex: 1;
          font-size: 26rpx;
          color: #262626;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
    
    .action-buttons {
      display: flex;
      gap: 12rpx;
      
      .btn {
        flex: 1;
        font-size: 22rpx;
        border-radius: 8rpx;
        padding: 8rpx 0;
        
        &.btn-status {
          border: none;
        }
        
        &.status-default {
          background: #F0F0F0;
          color: #8C8C8C;
        }
        
        &.status-success {
          background: #F6FFED;
          color: #52C41A;
          border: 1rpx solid #B7EB8F;
        }
        
        &.status-warning {
          background: #FFF7E6;
          color: #FA8C16;
          border: 1rpx solid #FFD591;
        }
        
        &.status-info {
          background: #E6F7FF;
          color: #1890FF;
          border: 1rpx solid #91D5FF;
        }
      }
    }
  }
}

// 状态选择弹窗样式
.picker-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 999;
  display: flex;
  align-items: flex-end;
}

.picker-content {
  width: 100%;
  background: white;
  border-radius: 24rpx 24rpx 0 0;
  padding-bottom: env(safe-area-inset-bottom);
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

.picker-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
  
  .picker-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #262626;
  }
  
  .picker-close {
    padding: 8rpx;
  }
}

.picker-options {
  padding: 20rpx 0;
  max-height: 600rpx;
  overflow-y: auto;
}

.picker-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 30rpx;
  font-size: 28rpx;
  color: #262626;
  transition: background 0.2s;
  
  &:active {
    background: #F5F5F5;
  }
  
  &.active {
    color: #1890FF;
    background: #E6F7FF;
  }
  
  text {
    flex: 1;
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
