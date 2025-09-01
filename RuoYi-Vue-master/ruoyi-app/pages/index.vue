<template>
  <view class="home-container">
    <!-- 自定义导航栏 -->
    <view class="custom-navbar" :style="{ paddingTop: statusBarHeight + 'px' }">
      <view class="navbar-content">
        <view class="location-info">
          <uni-icons type="location" size="16" color="#fff" />
          <text class="location-text">{{ communityInfo.name }}</text>
        </view>
        <view class="navbar-right">
          <view class="message-icon" @click="goMessage">
            <uni-icons type="email" size="20" color="#fff" />
            <uni-badge :text="unreadCount" type="error" v-if="unreadCount > 0" />
          </view>
        </view>
      </view>
    </view>

    <!-- 用户信息区 -->
    <view class="user-info-section">
      <view class="user-card">
        <view class="user-avatar-wrap">
          <image 
            class="user-avatar" 
            :src="avatar || 'https://img.icons8.com/fluency/96/user-male-circle.png'" 
            mode="aspectFill"
          />
        </view>
        <view class="user-detail">
          <view class="greeting-wrap">
            <text class="greeting">{{ greeting }}，{{ nickName }}</text>
            <view class="weather-info">
              <uni-icons type="cloudy" size="14" color="#8C8C8C" />
              <text class="weather-text">{{ weather.text }} {{ weather.temp }}°C</text>
            </view>
          </view>
          <view class="auth-status" v-if="!authStatus" @click="goAuth">
            <text class="status-text">未认证业主</text>
            <text class="auth-link">去认证 ></text>
          </view>
          <view class="auth-status verified" v-else>
            <uni-icons type="home-filled" size="16" color="#52C41A" />
            <text class="verified-text">{{ propertyAddress || '已认证业主' }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 公告轮播 -->
    <view class="notice-section" v-if="noticeList.length > 0">
      <view class="notice-header">
        <view class="notice-title-wrap">
          <uni-icons type="sound" size="18" color="#1890FF" />
          <text class="notice-main-title">最新公告</text>
        </view>
        <text class="notice-more" @click="goToNoticeList">全部 ></text>
      </view>
      
      <swiper 
        class="notice-swiper" 
        indicator-dots 
        circular 
        autoplay 
        interval="5000"
        indicator-color="rgba(24, 144, 255, 0.3)"
        indicator-active-color="#1890FF"
        indicator-style="bottom: 16rpx;"
      >
        <swiper-item v-for="notice in noticeList" :key="notice.noticeId">
          <view class="notice-item" @click="viewNotice(notice)">
            <view class="notice-left">
            <!--  <view class="notice-badge" :class="notice.noticeType === '1' ? 'urgent' : 'normal'">
                <text class="notice-type">{{ notice.noticeType === '1' ? '重要' : '通知' }}</text>
              </view> -->
              <view class="notice-content">
                <text class="notice-title">{{ notice.noticeTitle }}</text>
                <text class="notice-summary">{{ notice.noticeContent }}</text>
                <view class="notice-meta">
                  <text class="notice-time">{{ notice.createTime }}</text>
                  <view class="notice-status" v-if="notice.isNew">
                    <text class="status-text">NEW</text>
                  </view>
                </view>
              </view>
            </view>
            <view class="notice-right">
              <uni-icons type="right" size="16" color="#8C8C8C" />
            </view>
          </view>
        </swiper-item>
      </swiper>
    </view>

    <!-- 快捷功能网格 -->
    <view class="quick-functions">
     <!-- <view class="section-header">
        <text class="section-title">便民服务</text>
        <text class="more-link" @click="uni.switchTab({ url: '/pages/work/index' })">更多 ></text>
      </view> -->
      <view class="function-grid">
        <view 
          class="grid-item" 
          v-for="item in quickFunctions" 
          :key="item.id"
          @click="navigateTo(item.path)"
        >
          <view class="grid-icon" :style="{ backgroundColor: item.bgColor }">
            <uni-icons :type="item.iconType" size="20" color="white" />
          </view>
          <text class="grid-text">{{ item.name }}</text>
          <text class="grid-desc">{{ item.desc }}</text>
          <view class="grid-badge" v-if="item.badge">
            <text class="badge-text">{{ item.badge }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 制度文件查阅 -->
   <!-- <view class="regulations-section">
      <view class="section-header">
        <text class="section-title">制度文件</text>
        <text class="more-link" @click="goRegulations">更多 ></text>
      </view>
      <view class="regulation-list">
        <view 
          class="regulation-item" 
          v-for="regulation in regulationList" 
          :key="regulation.regulationId"
          @click="viewRegulation(regulation)"
        >
          <view class="regulation-icon">
            <uni-icons type="paperplane-filled" size="20" color="#1890FF" />
          </view>
          <view class="regulation-info">
            <text class="regulation-name">{{ regulation.regulationName }}</text>
            <text class="regulation-date">发布时间：{{ regulation.createTime }}</text>
          </view>
          <uni-icons type="right" size="14" color="#8C8C8C" />
        </view>
      </view>
    </view> -->

    <!-- 最新动态 -->
    <view class="latest-news">
      <view class="section-header">
        <text class="section-title">最新动态</text>
        <text class="more-link" @click="goNews">更多 ></text>
      </view>
      <view class="news-list">
        <view 
          class="news-item" 
          v-for="news in newsList" 
          :key="news.id"
          @click="viewNews(news)"
        >
          <view class="news-dot"></view>
          <view class="news-content">
            <text class="news-title">{{ news.title }}</text>
            <text class="news-date">{{ news.date }}</text>
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
import config from '@/config'
import { listNotice } from '@/api/notice.js'

export default {
  data() {
    return {
      statusBarHeight: 0,
      communityInfo: config.property.communityInfo,
      unreadCount: 3,
      weather: {
        text: '晴',
        temp: 22
      },
      // 公告数据改为空数组，将从API获取
      noticeList: [],
      // 快捷功能数据
      quickFunctions: [
		  {
		    id: 1,
		    name: '来访登记',
		    desc: '',
		    iconType: 'wallet',
		              path: '/pages/visitor/add',
		    bgColor: '#F5222D'
		  },
        
        {
          id: 2,
          name: '意见投诉',
          desc: '',
          iconType: 'chat',
          path: '/pages/property/complaint/add',
          bgColor: '#52C41A'
        },
        {
          id: 3,
          name: '设备报修',
          desc: '',
          iconType: 'gear',
          path: '/pages/repair/index',
          bgColor: '#FAAD14'
        },
        {
          id: 4,
          name: '会议投票',
          desc: '',
          iconType: 'compose',
          path: '/pages/property/meeting/index',
          bgColor: '#1890FF',
          badge: '2'
        },
      ],
      // 制度文件数据
      regulationList: [
        {
          regulationId: 1,
          regulationName: '业主管理规约',
          regulationType: '规约',
          createTime: '2024-01-15'
        },
        {
          regulationId: 2,
          regulationName: '会议制度',
          regulationType: '制度',
          createTime: '2024-01-10'
        },
        {
          regulationId: 3,
          regulationName: '财务管理制度',
          regulationType: '制度',
          createTime: '2024-01-08'
        },
        {
          regulationId: 4,
          regulationName: '物业服务标准',
          regulationType: '标准',
          createTime: '2024-01-05'
        }
      ],
      // 最新动态数据
      newsList: [
        {
          id: 1,
          title: '2024年度业主大会投票结果公示',
          date: '2024-01-15'
        },
        {
          id: 2,
          title: '小区维修资金使用情况公示',
          date: '2024-01-10'
        },
        {
          id: 3,
          title: '物业服务满意度调查结果',
          date: '2024-01-05'
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
    // 从ownerProfile中派生出认证状态
    authStatus() {
      return this.ownerProfile && this.ownerProfile.authStatus === '2'; // 2-已认证
    },
    // 新增：从业主信息中拼接地址
    propertyAddress() {
      if (this.authStatus && this.ownerProfile) {
        const { buildingNo, unitNo, roomNo } = this.ownerProfile;
        if (buildingNo || unitNo || roomNo) {
          return `${buildingNo || ''} ${unitNo || ''} ${roomNo || ''}`.trim();
        }
      }
      return '';
    },
    greeting() {
      const hour = new Date().getHours()
      if (hour < 12) return '早上好'
      if (hour < 18) return '下午好'
      return '晚上好'
    }
  },
  onLoad() {
    this.initPage()
  },
  onShow() {
    // 每次页面显示时，都主动刷新一次认证信息
    this.$store.dispatch('GetProfileInfo');
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.loadData()
    // 模拟加载时间
    setTimeout(() => {
      uni.stopPullDownRefresh()
      uni.showToast({
        title: '刷新成功',
        icon: 'success',
        duration: 1500
      })
    }, 1000)
  },
  methods: {
    initPage() {
      // 获取状态栏高度
      uni.getSystemInfo({
        success: (systemInfo) => {
          this.statusBarHeight = systemInfo.statusBarHeight
        }
      })
      
      // 加载页面数据
      this.loadData()
    },
    
    loadData() {
      this.getNoticeList(); // 调用真实API
      this.updateDynamicData()
    },

    // 获取公告列表
    getNoticeList() {
      // 只获取最新的几条，比如5条
      listNotice({ pageNum: 1, pageSize: 5 }).then(response => {
        console.log("获取到公告数据", response);
        this.noticeList = response.rows.map(notice => {
          // 后端返回的 noticeContent 可能包含HTML标签，这里直接用
          // createTime 格式化
          notice.createTime = this.parseTime(notice.createTime, '{y}-{m}-{d}');
          return notice;
        });
      }).catch(error => {
        console.error("获取公告失败", error);
      });
    },
    
    updateDynamicData() {
      // 模拟动态更新数据
      // 更新天气信息
      const weathers = [
        { text: '晴', temp: 22 },
        { text: '多云', temp: 18 },
        { text: '小雨', temp: 15 },
        { text: '阴', temp: 16 },
        { text: '雾', temp: 12 }
      ]
      this.weather = weathers[Math.floor(Math.random() * weathers.length)]
      
      // 随机更新未读消息数量
      this.unreadCount = Math.floor(Math.random() * 6)
      
      // 更新最新动态的日期（模拟有新动态）
      const today = new Date()
      const dateStr = today.getFullYear() + '-' + 
                     String(today.getMonth() + 1).padStart(2, '0') + '-' + 
                     String(today.getDate()).padStart(2, '0')
      
      if (this.newsList.length > 0) {
        // 随机更新一条动态的日期
        const randomIndex = Math.floor(Math.random() * this.newsList.length)
        this.newsList[randomIndex].date = dateStr
      }
    },
    
    // 导航方法
    navigateTo(path) {
      if (path) {
        uni.navigateTo({ url: path })
      } else {
        uni.showToast({
          title: '功能开发中',
          icon: 'none'
        })
      }
    },
    
    goAuth() {
      // 如果未认证，则跳转到认证页面
      if (!this.authStatus) {
        uni.navigateTo({ url: '/pages/mine/auth' })
      }
    },
    
    goMessage() {
      // 演示功能：点击清空未读消息
      if (this.unreadCount > 0) {
        this.unreadCount = 0
        uni.showToast({
          title: '消息已读',
          icon: 'success'
        })
      } else {
        // 演示：重新设置未读消息
        this.unreadCount = Math.floor(Math.random() * 5) + 1
        uni.showToast({
          title: `有${this.unreadCount}条新消息`,
          icon: 'none'
        })
      }
      // 实际项目中应该跳转到消息页面
      // uni.navigateTo({ url: '/pages/common/message/index' })
    },
    
    goRegulations() {
      uni.navigateTo({ url: '/pages/property/regulation/index' })
    },
    
    goNews() {
      uni.navigateTo({ url: '/pages/common/news/index' })
    },
    
    goToNoticeList() {
      uni.navigateTo({ url: '/pages/common/notice/list' })
    },
    
    // 查看详情方法
    viewNotice(notice) {
      uni.navigateTo({
        url: `/pages/common/notice/detail?id=${notice.noticeId}`
      })
    },
    
    viewRegulation(regulation) {
      uni.navigateTo({
        url: `/pages/property/regulation/detail?id=${regulation.regulationId}`
      })
    },
    
    viewNews(news) {
      uni.navigateTo({
        url: `/pages/common/news/detail?id=${news.id}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

.home-container {
  min-height: 100vh;
  background: #FFFFFF;
}

/* 自定义导航栏 */
.custom-navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #FFFFFF;
  
  .navbar-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 44px;
    padding: 0 30rpx;
    
    .location-info {
      display: flex;
      align-items: center;
      
      .location-text {
        margin-left: 12rpx;
        font-size: 32rpx;
        font-weight: 600;
        color: #262626;
      }
    }
    
    .navbar-right {
      .message-icon {
        position: relative;
        padding: 12rpx;
        border-radius: 50%;
        background: #F8F9FA;
      }
    }
  }
}

/* 用户信息区 */
.user-info-section {
  padding: 30rpx;
  
  .user-card {
    display: flex;
    align-items: center;
    padding: 40rpx;
    background: #FFFFFF;
    border-radius: 24rpx;
    box-shadow: 0 2rpx 16rpx rgba(0, 0, 0, 0.04);
    border: 1rpx solid #F0F0F0;
    
    .user-avatar-wrap {
      margin-right: 32rpx;
      
      .user-avatar {
        width: 120rpx;
        height: 120rpx;
        border-radius: 60rpx;
        border: 3rpx solid #F0F0F0;
      }
    }
    
    .user-detail {
      flex: 1;
      
      .greeting-wrap {
        display: flex;
        align-items: center;
        margin-bottom: 16rpx;
        
        .greeting {
          font-size: 40rpx;
          font-weight: 600;
          color: #262626;
          margin-right: 24rpx;
        }
        
        .weather-info {
          display: flex;
          align-items: center;
          padding: 8rpx 16rpx;
          background: #F8F9FA;
          border-radius: 20rpx;
          
          .weather-text {
            margin-left: 8rpx;
            font-size: 24rpx;
            color: #8C8C8C;
          }
        }
      }
      
      .auth-status {
        display: flex;
        align-items: center;
        
        .status-text {
          font-size: 28rpx;
          color: #8C8C8C;
          margin-right: 12rpx;
        }
        
        .auth-link {
          font-size: 28rpx;
          color: #1890FF;
          font-weight: 500;
        }
        
        &.verified {
          .verified-text {
            margin-left: 8rpx;
            font-size: 28rpx;
            color: #52C41A;
            font-weight: 500;
          }
        }
      }
    }
  }
}

/* 公告轮播 */
.notice-section {
  margin: 0 30rpx 40rpx;
  
  .notice-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20rpx;
    
    .notice-title-wrap {
      display: flex;
      align-items: center;
      
      .notice-main-title {
        margin-left: 12rpx;
        font-size: 32rpx;
        font-weight: 600;
        color: #262626;
      }
    }
    
    .notice-more {
      font-size: 26rpx;
      color: #1890FF;
      font-weight: 500;
    }
  }
  
  .notice-swiper {
    height: 180rpx;
    border-radius: 24rpx;
    overflow: hidden;
    position: relative;
    
    .notice-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 100%;
      padding: 32rpx;
      background: linear-gradient(135deg, #F8F9FA 0%, #FFFFFF 100%);
      border: 1rpx solid #F0F0F0;
      position: relative;
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        width: 6rpx;
        background: linear-gradient(to bottom, #1890FF, #40A9FF);
        border-radius: 0 6rpx 6rpx 0;
      }
      
      .notice-left {
        display: flex;
        align-items: flex-start;
        flex: 1;
        
        .notice-badge {
          margin-right: 20rpx;
          margin-top: 4rpx;
          
          .notice-type {
            padding: 6rpx 12rpx;
            border-radius: 12rpx;
            font-size: 22rpx;
            font-weight: 600;
            line-height: 1;
          }
          
          &.urgent {
            .notice-type {
              background: linear-gradient(135deg, #FF4D4F, #FF7875);
              color: #fff;
            }
          }
          
          &.normal {
            .notice-type {
              background: #E6F7FF;
              color: #1890FF;
            }
          }
        }
        
        .notice-content {
          flex: 1;
          
          .notice-title {
            display: block;
            font-size: 30rpx;
            font-weight: 600;
            color: #262626;
            margin-bottom: 12rpx;
            line-height: 1.4;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          
          .notice-summary {
            font-size: 24rpx;
            color: #8C8C8C;
            line-height: 1.5;
            margin-bottom: 12rpx;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          
          .notice-meta {
            display: flex;
            align-items: center;
            justify-content: space-between;
            
            .notice-time {
              font-size: 22rpx;
              color: #BFBFBF;
            }
            
            .notice-status {
              .status-text {
                padding: 4rpx 8rpx;
                background: linear-gradient(135deg, #52C41A, #73D13D);
                color: #fff;
                font-size: 20rpx;
                font-weight: 600;
                border-radius: 8rpx;
                line-height: 1;
              }
            }
          }
        }
      }
      
      .notice-right {
        display: flex;
        align-items: center;
        margin-left: 16rpx;
      }
    }
  }
}

/* 主内容区 */
.quick-functions,
.regulations-section,
.latest-news {
  margin: 0 30rpx 40rpx;
  padding: 40rpx;
  background: #FFFFFF;
  border-radius: 24rpx;
  border: 1rpx solid #F0F0F0;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30rpx;
  
  .section-title {
    font-size: 36rpx;
    font-weight: 600;
    color: #262626;
  }
  
  .more-link {
    font-size: 28rpx;
    color: #1890FF;
  }
}

/* 快捷功能网格 */
.function-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24rpx;
  
  .grid-item {
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 24rpx 15rpx;
    border-radius: 24rpx;
    background: #FAFBFC;
    border: 1rpx solid #F0F0F0;
    transition: all 0.2s ease;
    
    &:active {
      transform: translateY(2rpx);
      background: #F5F6F7;
      border-color: #E0E0E0;
    }
    
    .grid-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 44rpx;
      display: flex;
    align-items: center;
    justify-content: center;
      margin-bottom: 24rpx;
    }
    
    .grid-text {
      font-size: 20rpx;
      color: #262626;
      font-weight: 600;
      margin-bottom: 8rpx;
    }
    
    .grid-desc {
      font-size: 24rpx;
      color: #8C8C8C;
    }
    
    .grid-badge {
      position: absolute;
      top: 20rpx;
      right: 20rpx;
      
      .badge-text {
        padding: 4rpx 12rpx;
        background: #FF4D4F;
        color: #fff;
        font-size: 20rpx;
        border-radius: 20rpx;
        min-width: 32rpx;
        text-align: center;
      }
    }
  }
}

/* 制度文件列表 */
.regulation-list {
  .regulation-item {
    display: flex;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 2rpx solid #F5F5F5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .regulation-icon {
      margin-right: 24rpx;
      width: 60rpx;
      height: 60rpx;
      border-radius: 12rpx;
      background: #E6F7FF;
      display: flex;
      align-items: center;
    justify-content: center;
  }

    .regulation-info {
      flex: 1;
      
      .regulation-name {
        display: block;
        font-size: 30rpx;
        color: #262626;
        font-weight: 500;
        margin-bottom: 8rpx;
      }
      
      .regulation-date {
        font-size: 24rpx;
        color: #8C8C8C;
      }
    }
  }
}

/* 最新动态列表 */
.news-list {
  .news-item {
    display: flex;
    align-items: flex-start;
    padding: 20rpx 0;
    
    .news-dot {
      width: 12rpx;
      height: 12rpx;
      border-radius: 50%;
      background: #1890FF;
      margin-top: 16rpx;
      margin-right: 20rpx;
      flex-shrink: 0;
    }
    
    .news-content {
      flex: 1;
      
      .news-title {
        display: block;
        font-size: 30rpx;
        color: #262626;
        line-height: 1.4;
        margin-bottom: 8rpx;
      }
      
      .news-date {
        font-size: 24rpx;
        color: #8C8C8C;
      }
    }
  }
}

/* 底部安全距离 */
.safe-area-bottom {
  height: env(safe-area-inset-bottom);
  background: #fff;
  }
</style>
