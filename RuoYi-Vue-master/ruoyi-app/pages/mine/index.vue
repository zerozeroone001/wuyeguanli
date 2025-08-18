<template>
  <view class="mine-container">
    <!-- 头部个人信息区 -->
    <view class="header-section">
      <view class="user-card">
        <view class="user-avatar-wrap">
          <image 
            class="user-avatar" 
            :src="mockUser.avatar || 'https://img.icons8.com/fluency/96/user-male-circle.png'" 
            mode="aspectFill"
            @click="handleToAvatar"
          />
          <view class="auth-badge" v-if="mockUser.authStatus">
            <uni-icons type="checkmarkempty" size="16" color="white" />
          </view>
        </view>
        <view class="user-info">
          <text class="user-name">{{ mockUser.nickName }}</text>
          <view class="user-status" @click="toggleAuthStatus">
            <text class="status-text" :class="{ verified: mockUser.authStatus }">
              {{ mockUser.authStatus ? '已认证业主' : '未认证业主' }}
            </text>
            <uni-icons type="right" size="14" color="#8C8C8C" />
          </view>
          <view class="property-info">
            <text class="property-text">{{ mockUser.building }} {{ mockUser.unit }} {{ mockUser.room }}</text>
          </view>
        </view>
        <view class="qr-code" @click="showQRCode">
          <uni-icons type="scan" size="24" color="white" />
        </view>
      </view>
    </view>

    <!-- 统计数据 -->
    <view class="stats-section">
      <view class="stats-card">
        <view class="stat-item" @click="goToOrders">
          <text class="stat-number">{{ stats.orders }}</text>
          <text class="stat-label">我的工单</text>
        </view>
        <view class="stat-item" @click="goToComplaints">
          <text class="stat-number">{{ stats.complaints }}</text>
          <text class="stat-label">投诉建议</text>
        </view>
        <view class="stat-item" @click="goToVotes">
          <text class="stat-number">{{ stats.votes }}</text>
          <text class="stat-label">参与投票</text>
        </view>
        <view class="stat-item" @click="goToMessages">
          <text class="stat-number">{{ stats.messages }}</text>
          <text class="stat-label">消息通知</text>
        </view>
      </view>
    </view>

    <!-- 快捷功能 -->
    <view class="quick-actions">
      <view class="section-title">
        <text>快捷功能</text>
      </view>
      <view class="action-grid">
        <view class="action-item" v-for="(action, index) in quickActions" :key="index" @click="handleActionClick(action)">
          <view class="action-icon" :style="{ backgroundColor: action.bgColor }">
            <uni-icons :type="action.icon" size="20" color="white" />
          </view>
          <text class="action-name">{{ action.name }}</text>
        </view>
      </view>
    </view>

    <!-- 服务菜单 -->
    <view class="service-menu">
      <view class="section-title">
        <text>服务中心</text>
      </view>
      <view class="menu-list">
        <view class="menu-item" v-for="(item, index) in serviceMenus" :key="index" @click="handleMenuClick(item)">
          <view class="menu-left">
            <view class="menu-icon" :style="{ backgroundColor: item.bgColor }">
              <uni-icons :type="item.icon" size="18" color="white" />
            </view>
            <text class="menu-name">{{ item.name }}</text>
          </view>
          <view class="menu-right">
            <text class="menu-badge" v-if="item.badge">{{ item.badge }}</text>
            <uni-icons type="right" size="14" color="#C0C4CC" />
          </view>
        </view>
      </view>
    </view>

    <!-- 设置菜单 -->
    <view class="settings-menu">
      <view class="section-title">
        <text>设置</text>
      </view>
      <view class="menu-list">
        <view class="menu-item" v-for="(item, index) in settingsMenus" :key="index" @click="handleMenuClick(item)">
          <view class="menu-left">
            <view class="menu-icon" :style="{ backgroundColor: item.bgColor }">
              <uni-icons :type="item.icon" size="18" color="white" />
            </view>
            <text class="menu-name">{{ item.name }}</text>
          </view>
          <view class="menu-right">
            <uni-icons type="right" size="14" color="#C0C4CC" />
          </view>
        </view>
      </view>
    </view>

    <!-- 底部安全距离 -->
    <view class="safe-area-bottom"></view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 模拟用户数据
      mockUser: {
        nickName: '张先生',
        avatar: 'https://img.icons8.com/fluency/96/user-male-circle.png',
        authStatus: false, // 可切换的认证状态
        phone: '138****8888',
        building: '3号楼',
        unit: '2单元',
        room: '1201'
      },
      // 统计数据
      stats: {
        orders: 3,
        complaints: 1,
        votes: 5,
        messages: 2
      },
      // 快捷功能
      quickActions: [
        {
          name: '我的工单',
          icon: 'list',
          bgColor: '#1890FF',
          path: '/pages/repair/my-orders'
        },
        {
          name: '缴费记录',
          icon: 'wallet',
          bgColor: '#52C41A',
          path: '/pages/payment/records'
        },
        {
          name: '投票记录',
          icon: 'compose',
          bgColor: '#FAAD14',
          path: '/pages/property/meeting/my-votes'
        },
        {
          name: '我的投诉',
          icon: 'chat',
          bgColor: '#F5222D',
          path: '/pages/complaints/my-complaints'
        }
      ],
      // 服务菜单
      serviceMenus: [
        {
          name: '房屋信息',
          icon: 'home',
          bgColor: '#1890FF',
          path: '/pages/mine/property-info'
        },
        {
          name: '家庭成员',
          icon: 'person-add',
          bgColor: '#52C41A',
          path: '/pages/mine/family-members'
        },
        {
          name: '车位管理',
          icon: 'car',
          bgColor: '#722ED1',
          path: '/pages/mine/parking'
        },
        {
          name: '门禁管理',
          icon: 'locked',
          bgColor: '#FAAD14',
          path: '/pages/mine/access-control'
        },
        {
          name: '邻里社交',
          icon: 'chat',
          bgColor: '#13C2C2',
          path: '/pages/mine/neighbors'
        },
        {
          name: '意见反馈',
          icon: 'sound',
          bgColor: '#F5222D',
          badge: 'NEW'
        }
      ],
      // 设置菜单
      settingsMenus: [
        {
          name: '个人资料',
          icon: 'person',
          bgColor: '#8C8C8C',
          path: '/pages/mine/info/index'
        },
        {
          name: '消息设置',
          icon: 'email',
          bgColor: '#096DD9',
          path: '/pages/mine/message-settings'
        },
        {
          name: '隐私设置',
          icon: 'eye-slash',
          bgColor: '#389E0D',
          path: '/pages/mine/privacy-settings'
        },
        {
          name: '关于我们',
          icon: 'info',
          bgColor: '#D48806',
          path: '/pages/mine/about/index'
        }
      ]
    }
  },
  methods: {
    toggleAuthStatus() {
      // 演示功能：切换认证状态
      this.mockUser.authStatus = !this.mockUser.authStatus
      uni.showToast({
        title: this.mockUser.authStatus ? '已切换为认证状态' : '已切换为未认证状态',
        icon: 'success',
        duration: 2000
      })
    },
    
    showQRCode() {
      uni.showToast({
        title: '二维码功能开发中',
        icon: 'none',
        duration: 2000
      })
    },
    
    // 统计数据点击事件
    goToOrders() {
      this.updateStats('orders')
      uni.showToast({ title: '我的工单功能开发中', icon: 'none' })
    },
    
    goToComplaints() {
      this.updateStats('complaints')
      uni.navigateTo({ url: '/pages/complaints/my-complaints' })
    },
    
    goToVotes() {
      this.updateStats('votes')
      uni.navigateTo({ url: '/pages/property/meeting/my-votes' })
    },
    
    goToMessages() {
      this.updateStats('messages')
      uni.showToast({ title: '消息中心功能开发中', icon: 'none' })
    },
    
    updateStats(type) {
      // 演示功能：点击后随机更新数据
      this.stats[type] = Math.floor(Math.random() * 10)
    },
    
    handleActionClick(action) {
      if (action.path) {
        uni.navigateTo({ url: action.path })
      } else {
        uni.showToast({
          title: `${action.name}功能开发中`,
          icon: 'none',
          duration: 2000
        })
      }
    },
    
    handleMenuClick(item) {
      if (item.path) {
        uni.navigateTo({ url: item.path })
      } else {
        uni.showToast({
          title: `${item.name}功能开发中`,
          icon: 'none',
          duration: 2000
        })
      }
    },
    
    handleToAvatar() {
      uni.navigateTo({ url: '/pages/mine/avatar/index' })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #F5F6FA;
}

.mine-container {
  min-height: 100vh;
  background-color: #F5F6FA;
}

.header-section {
  background: linear-gradient(135deg, #1890FF 0%, #40A9FF 100%);
  padding: 40rpx 30rpx 80rpx;
  
  .user-card {
    display: flex;
    align-items: center;
    
    .user-avatar-wrap {
      position: relative;
      margin-right: 30rpx;
      
      .user-avatar {
        width: 120rpx;
        height: 120rpx;
        border-radius: 60rpx;
        border: 4rpx solid rgba(255, 255, 255, 0.3);
      }
      
      .auth-badge {
        position: absolute;
        right: 0;
        bottom: 0;
        width: 32rpx;
        height: 32rpx;
        background-color: #52C41A;
        border-radius: 16rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 2rpx solid white;
      }
    }
    
    .user-info {
      flex: 1;
      
      .user-name {
        display: block;
        color: white;
        font-size: 36rpx;
        font-weight: bold;
        margin-bottom: 16rpx;
      }
      
      .user-status {
        display: flex;
        align-items: center;
        margin-bottom: 12rpx;
        
        .status-text {
          font-size: 26rpx;
          color: rgba(255, 255, 255, 0.8);
          margin-right: 8rpx;
          
          &.verified {
            color: #52C41A;
          }
        }
      }
      
      .property-info {
        .property-text {
          font-size: 24rpx;
          color: rgba(255, 255, 255, 0.7);
        }
      }
    }
    
    .qr-code {
      width: 80rpx;
      height: 80rpx;
      background-color: rgba(255, 255, 255, 0.2);
      border-radius: 40rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 1rpx solid rgba(255, 255, 255, 0.3);
    }
  }
}

.stats-section {
  margin: -50rpx 30rpx 40rpx;
  
  .stats-card {
    background-color: white;
    border-radius: 20rpx;
    padding: 40rpx 0;
    display: flex;
    box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
    
    .stat-item {
      flex: 1;
      text-align: center;
      
      .stat-number {
        display: block;
        font-size: 48rpx;
        font-weight: bold;
        color: #1890FF;
        margin-bottom: 8rpx;
      }
      
      .stat-label {
        font-size: 24rpx;
        color: #8C8C8C;
      }
    }
  }
}

.section-title {
  padding: 0 30rpx 20rpx;
  
  text {
    font-size: 32rpx;
    font-weight: bold;
    color: #262626;
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      left: 0;
      bottom: -8rpx;
      width: 60rpx;
      height: 4rpx;
      background-color: #1890FF;
      border-radius: 2rpx;
    }
  }
}

.quick-actions {
  margin-bottom: 40rpx;
  
  .action-grid {
    display: flex;
    padding: 0 30rpx;
    gap: 30rpx;
    
    .action-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 30rpx 20rpx;
      background-color: white;
      border-radius: 16rpx;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
      
      .action-icon {
        width: 80rpx;
        height: 80rpx;
        border-radius: 40rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 20rpx;
      }
      
      .action-name {
        font-size: 24rpx;
        color: #262626;
      }
    }
  }
}

.service-menu, .settings-menu {
  margin-bottom: 40rpx;
  
  .menu-list {
    background-color: white;
    margin: 0 30rpx;
    border-radius: 16rpx;
    overflow: hidden;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
    
    .menu-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx;
      border-bottom: 1rpx solid #F0F0F0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .menu-left {
        display: flex;
        align-items: center;
        flex: 1;
        
        .menu-icon {
          width: 60rpx;
          height: 60rpx;
          border-radius: 30rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 24rpx;
        }
        
        .menu-name {
          font-size: 28rpx;
          color: #262626;
          font-weight: 500;
        }
      }
      
      .menu-right {
        display: flex;
        align-items: center;
        
        .menu-badge {
          background-color: #FF4D4F;
          color: white;
          font-size: 20rpx;
          padding: 4rpx 12rpx;
          border-radius: 20rpx;
          margin-right: 16rpx;
        }
      }
    }
  }
}

.safe-area-bottom {
  height: 120rpx;
}
</style>
