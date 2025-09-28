<template>
  <view class="mine-container">
    <!-- 头部个人信息区 -->
    <view class="header-section">
      <view class="user-card">
        <view class="user-avatar-wrap">
          <image 
            class="user-avatar" 
            :src="avatar || 'https://img.icons8.com/fluency/96/user-male-circle.png'" 
            mode="aspectFill"
            @click="handleToAvatar"
          />
          <view class="auth-badge" v-if="authStatus">
            <uni-icons type="checkmarkempty" size="16" color="white" />
          </view>
        </view>
        <view class="user-info">
          <text class="user-name">{{ nickName }}</text>
          <view class="user-status" @click="handleAuthStatusClick">
            <text class="status-text" :class="{ verified: authStatus }">
              {{ ownerStatusText }}
            </text>
            <uni-icons type="right" size="14" color="#8C8C8C" />
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
          <text class="stat-label">我的合同</text>
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
  <!--  <view class="quick-actions">
      <view class="action-grid">
        <view class="action-item" v-for="(action, index) in quickActions" :key="index" @click="handleActionClick(action)">
          <view class="action-icon" :style="{ backgroundColor: action.bgColor }">
            <uni-icons :type="action.icon" size="20" color="white" />
          </view>
          <text class="action-name">{{ action.name }}</text>
        </view>
      </view>
    </view> -->

    <!-- 设置菜单 -->
    <view class="settings-menu">
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
import { mapGetters } from 'vuex'
import { isAuthenticated, getAuthStatusText, getAuthStatusColor, getAuthStatusIcon } from '@/utils/authHelper'
import { getMyVoteRecords } from '@/api/property/meeting'
import { listMyComplaint } from '@/api/property/complaint'

export default {
  data() {
    return {
      // 统计数据
      stats: {
        orders: 0,
        complaints: 0,
        votes: 0,
        messages: 0
      },
      // 快捷功能
      quickActions: [
        {
          name: '我的合同',
          icon: 'list',
          bgColor: '#1890FF',
          path: '/pages/property/contract/index'
        },
        {
          name: '缴费记录',
          icon: 'wallet',
          bgColor: '#52C41A',
          path: '/pages/fund-management/index'
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
      // 设置菜单
      settingsMenus: [
        {
          name: '个人资料',
          icon: 'person',
          bgColor: '#8C8C8C',
          path: '/pages/mine/info/index'
        },
        {
          name: '实名认证',
          icon: 'shield-filled',
          bgColor: '#1890FF',
          path: '/pages/mine/auth'
        },
        {
          name: '我的房产',
          icon: 'home-filled',
          bgColor: '#FA8C16',
          path: '/pages/property/index'
        },
        {
          name: '消息设置',
          icon: 'email',
          bgColor: '#096DD9',
          action: 'subscribe'
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
  computed: {
    ...mapGetters([
        'nickName',
        'avatar',
        'ownerProfile' // 直接获取ownerProfile对象
    ]),
	
	// 从isOwner字段派生出认证状态
	authStatus() {
	  return isAuthenticated(this.ownerProfile.isOwner)
	},
	
    // 从ownerProfile中派生出认证状态
    ownerStatusText() {
      return getAuthStatusText(this.ownerProfile.isOwner)
    },
    propertyAddress() {
      if (this.ownerProfile && (this.ownerProfile.buildingNo || this.ownerProfile.unitNo || this.ownerProfile.roomNo)) {
        const building = this.ownerProfile.buildingNo || '';
        const unit = this.ownerProfile.unitNo || '';
        const room = this.ownerProfile.roomNo || '';
        return `${building} ${unit} ${room}`.trim();
      }
      return '暂无房产信息';
    }
  },
  onShow() {
    // 每次页面显示时，都主动刷新一次认证信息
    this.$store.dispatch('GetProfileInfo');
    // 加载统计数据
    this.loadStats();
  },
  methods: {
    handleAuthStatusClick() {
      if (!this.authStatus) {
        uni.navigateTo({
          url: '/pages/mine/auth'
        });
      } else {
        uni.showToast({
          title: '您已完成实名认证',
          icon: 'none'
        });
      }
    },
    
    showQRCode() {
      uni.showToast({
        title: '二维码功能开发中',
        icon: 'none',
        duration: 2000
      })
    },
    
    // 加载统计数据
    async loadStats() {
      try {
        // 并行加载投票记录和投诉记录数量
        const [voteResponse, complaintResponse] = await Promise.all([
          getMyVoteRecords({ pageNum: 1, pageSize: 1 }),
          listMyComplaint({ pageNum: 1, pageSize: 1 })
        ]);
        
        // 更新投票记录数量
        if (voteResponse.code === 200) {
          this.stats.votes = voteResponse.total || 0;
        }
        
        // 更新投诉记录数量
        if (complaintResponse.code === 200) {
          this.stats.complaints = complaintResponse.total || 0;
        }
        
        // 这里可以添加其他统计数据的加载
        // 例如合同数量、消息数量等
        
      } catch (error) {
        console.error('加载统计数据失败:', error);
        // 失败时使用默认值，不影响页面显示
      }
    },
    
    // 统计数据点击事件
    goToOrders() {
      uni.showToast({ title: '我的合同功能开发中', icon: 'none' })
    },
    
    goToComplaints() {
      uni.navigateTo({ url: '/pages/complaints/my-complaints' })
    },
    
    goToVotes() {
      uni.navigateTo({ url: '/pages/property/meeting/my-votes' })
    },
    
    goToMessages() {
      uni.showToast({ title: '消息中心功能开发中', icon: 'none' })
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
        uni.navigateTo({ url: item.path });
      } else if (item.action === 'subscribe') {
        this.handleMessageSettings();
      } else {
        uni.showToast({
          title: `${item.name}功能开发中`,
          icon: 'none',
          duration: 2000
        });
      }
    },
    
    handleToAvatar() {
      uni.navigateTo({ url: '/pages/mine/avatar/index' })
    },
    handleMessageSettings() {
      // 跳转到订阅消息设置页面
      uni.navigateTo({
        url: '/pages/mine/subscribe/index'
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.mine-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.header-section {
  background: #FFFFFF;
  padding: 40rpx 30rpx 60rpx;
  border-bottom: 1rpx solid #F0F0F0;
  
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
        color: #262626;
        font-size: 36rpx;
        font-weight: 600;
        margin-bottom: 16rpx;
      }
      
      .user-status {
        display: flex;
        align-items: center;
        margin-bottom: 12rpx;
        
        .status-text {
          font-size: 26rpx;
          color: #8C8C8C;
          margin-right: 8rpx;
          
          &.verified {
            color: #52C41A;
          }
        }
      }
      
      .property-info {
        .property-text {
          font-size: 24rpx;
          color: #8C8C8C;
        }
      }
    }
    
    .qr-code {
      width: 80rpx;
      height: 80rpx;
      background-color: #F8F9FA;
      border-radius: 40rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 1rpx solid #E0E0E0;
    }
  }
}

.stats-section {
  margin: -30rpx 30rpx 40rpx;
  
  .stats-card {
    background-color: #FFFFFF;
    border-radius: 24rpx;
    padding: 40rpx 0;
    display: flex;
    border: 1rpx solid #F0F0F0;
    
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
    gap: 24rpx;
    
    .action-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 32rpx 20rpx;
      background-color: #FFFFFF;
      border-radius: 20rpx;
      border: 1rpx solid #F0F0F0;
      
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
    background-color: #FFFFFF;
    margin: 0 30rpx;
    border-radius: 24rpx;
    overflow: hidden;
    border: 1rpx solid #F0F0F0;
    
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