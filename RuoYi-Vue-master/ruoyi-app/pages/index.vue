<template>
  <view class="home-container">
    <!-- 手机号绑定弹窗 -->
    <phone-bind-modal ref="phoneBindModal" @success="onPhoneBindSuccess" />

    <!-- 自定义导航栏 -->
    <view class="custom-navbar" :style="{ paddingTop: statusBarHeight + 'px' }">
      <view class="navbar-content">
        <picker
          class="location-picker"
          mode="selector"
          :range="communityOptions"
          range-key="name"
          :value="selectedCommunityIndex"
          :disabled="communityOptions.length === 0"
          @change="handleCommunityChange"
        >
          <view class="location-info">
            <uni-icons type="location" size="16" color="#fff" />
            <text class="location-text">{{ communityInfo.name }}</text>
            <uni-icons
              v-if="communityOptions.length > 0"
              type="arrowdown"
              size="12"
              color="#fff"
              class="location-arrow"
            />
          </view>
        </picker>
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
            :src="'/static/logo.png'"
            mode="aspectFill"
          />
        </view>
        <view class="user-detail">
          <!-- 已登录状态 -->
          <template v-if="isLoggedIn">
            <view class="greeting-wrap">
              <text class="greeting">{{ greeting }}，{{ user.name ||'游客' }}</text>
              <view class="weather-info" v-if="!authStatus">
                <uni-icons type="cloudy" size="14" color="#8C8C8C" />
                <text class="weather-text">{{ weather.text }} {{ weather.temp }}°C</text>
              </view>
            </view>
           <!-- <view class="property-info" v-if="authStatus && propertyAddress">
               <uni-icons type="home" size="14" color="#666" style="margin-right: 4px;" />
               <text class="property-text">{{ propertyAddress }}</text>
            </view> -->
            <view class="auth-status" v-if="!authStatus" @click="goAuth">
              <text class="status-text">{{ ownerStatusText }}</text>
              <text class="auth-link">去绑定 ></text>
            </view>
            <view class="auth-status verified" v-else>
              <uni-icons type="home-filled" size="16" color="#52C41A" />
              <text class="verified-text">{{ ownerStatusText || ownerStatusText }}</text>
            </view>
          </template>
          
          <!-- 未登录状态 -->
          <template v-else>
            <view class="greeting-wrap">
              <text class="greeting">{{ greeting }}，欢迎使用智慧物业</text>
              <!-- <view class="weather-info">
                <uni-icons type="cloudy" size="14" color="#8C8C8C" />
                <text class="weather-text">{{ weather.text }} {{ weather.temp }}°C</text>
              </view> -->
            </view>
            <view class="login-status" @click="goToLogin">
              <text class="login-text">请先登录以享受更多服务</text>
              <text class="login-link">去登录 ></text>
            </view>
			
          </template>
        </view>
      </view>
	  <view class="tishi" v-if="!isLoggedIn">
	  	账号仅限特定人群登录并进行登录账号鉴权
	  </view>
    </view>
	
	<!-- 轮播图 -->
	<view class="banner-section">
	  <swiper
	    class="banner-swiper"
	    indicator-dots
	    circular
	    autoplay
	    interval="3000"
	    indicator-color="rgba(255, 255, 255, 0.5)"
	    indicator-active-color="#FFFFFF"
	  >
	    <swiper-item v-for="(banner, index) in bannerList" :key="index">
	      <image
	        class="banner-image"
	        :src="banner.image"
	        mode="aspectFill"
	        @click="handleBannerClick(banner)"
	      />
	    </swiper-item>
	  </swiper>
	</view>

    <!-- 快捷入口魔方按钮 -->
    <view class="quick-entry-section">
      <view class="quick-entry-grid">
        <view class="quick-entry-item" @click="goToOwnerChange">
			<image src="https://zhuote.oss-cn-hangzhou.aliyuncs.com/2025/12/04/e7e14d10b072ccf23b053955991ac4a_20251204105926A012.png" mode="widthFix" style="width: 100%;border-radius: 24rpx"></image>
        </view>
        <view class="quick-entry-item" @click="goToPropertyAuth">
			<image src="https://zhuote.oss-cn-hangzhou.aliyuncs.com/2025/12/04/9624060951f091b2cda78ab0bc433f1_20251204105912A011.png" mode="widthFix" style="width: 100%;border-radius: 24rpx"></image>
        </view>
    </view>

    <!-- 主要服务 -->
    <view class="main-services">
      <uni-grid :column="3" :showBorder="false" :square="false">
        <uni-grid-item v-for="(service, index) in filteredMainServices" :key="index">
          <view class="service-item main-service" @click="handleServiceClick(service)">
            <view class="service-icon" :style="{ backgroundColor: service.bgColor }">
              <uni-icons :type="service.icon" size="28" color="white" />
            </view>
            <text class="service-name">{{ service.name }}</text>
            <text class="service-desc">{{ service.desc }}</text>
          </view>
        </uni-grid-item>
      </uni-grid>
    </view>


    <!-- 小区公告 -->
    <view class="community-notice-section">
      <view class="section-header">
        <text class="section-title">小区公告</text>
        <text class="more-link" @click="goToNoticeList">查看全部 ></text>
      </view>
      <view class="notice-list">
        <view
          class="notice-list-item"
          v-for="notice in communityNoticeList"
          :key="notice.noticeId"
          @click="viewNotice(notice.noticeId)"
        >
          <view class="notice-content">
            <text class="notice-item-title">{{ notice.noticeTitle }}</text>
            <text class="notice-item-date">{{ notice.formattedCreateTime }}</text>
          </view>
          <view class="notice-thumbnail">
            <image
              v-if="notice.coverUrl"
              :src="notice.coverUrl"
              mode="aspectFill"
              class="thumbnail-image"
            />
            <view v-else class="thumbnail-placeholder">
              <image
                :src="getNoticeIcon(notice)"
                mode="aspectFill"
                class="thumbnail-icon"
              />
            </view>
          </view>
        </view>
      </view>
      <view v-if="communityNoticeList.length === 0" class="empty-notice">
        <uni-icons type="sound" size="48" color="#C5D9FF" />
        <text class="empty-text">暂无公告</text>
      </view>
      <view v-if="communityNoticeList.length > 0" class="load-more-tip">
        继续上拉查看更多
      </view>
    </view>

    <!-- 底部安全距离 -->
    <view class="safe-area-bottom"></view>
  </view>
  </view>
</template>

<script>
import { mapGetters } from 'vuex'
import config from '@/config'
import { listNotice } from '@/api/notice.js'
import { listMyProperty, listCommunity } from '@/api/property.js'
import { listBanner } from '@/api/banner.js'
import { isAuthenticated, getAuthStatusText, getAuthStatusColor, getAuthStatusIcon } from '@/utils/authHelper'
import PhoneBindModal from '@/components/phone-bind-modal/phone-bind-modal.vue'

const COMMUNITY_STORAGE_KEY = 'app_current_community_id'
const COMMUNITY_INFO_STORAGE_KEY = 'app_current_community_info'

export default {
  components: {
    PhoneBindModal
  },
  data() {
    return {
      statusBarHeight: 0,
      defaultCommunityInfo: { ...config.property.communityInfo },
      communityInfo: { ...config.property.communityInfo },
      communityOptions: [],
      selectedCommunityIndex: 0,
      selectedCommunityId: null,
      unreadCount: 3,
      weather: {
        text: '晴',
        temp: 22
      },
      // 轮播图数据（从API获取）
      bannerList: [],
      // 公告数据改为空数组，将从API获取
      noticeList: [],
      // 小区公告数据（显示10条）
      communityNoticeList: [],
      // 主要服务数据
      mainServices: [
		  {
		    name: '产权认证',
		    desc: '产权认证',
		    icon: 'staff',
		    bgColor: '#FF69B4',
		    path: '/pageB/property/add'
		  },
		  {
		    name: '业主变更',
		    desc: '业主变更',
		    icon: 'loop',
		    bgColor: '#1890FF',
		    path: '/pageB/owner-change/submit'
		  },
        {
          name: '业委会会议',
          desc: '会议投票',
          icon: 'compose',
          bgColor: '#52C41A',
          path: '/pageB/property/committee-meeting/index'
        },
        {
          name: '合同查阅',
          desc: '合同管理',
          icon: 'paperplane',
          bgColor: '#FAAD14',
          path: '/pageB/contract-fulfillment/index'
        },
        {
          name: '资金公示',
          desc: '财务透明',
          icon: 'wallet',
          bgColor: '#722ED1',
          path: '/pageB/fund-management/index'
        },
        {
          name: '法律咨询',
          desc: '专业服务',
          icon: 'help',
          bgColor: '#F5222D',
          path: '/pageB/legal-consultation/index'
        },
        {
          name: '制度查阅',
          desc: '规章制度',
          icon: 'list',
          bgColor: '#13C2C2',
          path: '/pages/work/regulation'
        },
        {
          name: '公证存证',
          desc: '权威认证',
          icon: 'checkmarkempty',
          bgColor: '#EB2F96',
          path: '/pageB/notary-service/index'
        },
        {
          name: '指导监督',
          desc: '业务指导',
          icon: 'eye',
          bgColor: '#FA8C16',
          path: '/pages/supervision/index'
        },
        {
          name: '承接查验',
          desc: '物业交接',
          icon: 'flag',
          bgColor: '#389E0D',
          path: '/pages/handover/index'
        }
		
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
		'user',
        'userName',
		    'phonenumber',
        'ownerProfile', // 直接获取ownerProfile对象
        'token' // 添加token获取
    ]),
    // 检查是否已登录
    isLoggedIn() {
      return !!this.token
    },
    // 从isOwner字段派生出认证状态
    authStatus() {
      console.log(this.user)
      return isAuthenticated(this.user.isOwner)
    },
    // 业主状态文本
    ownerStatusText() {
      return getAuthStatusText(this.user.isOwner)
    },
    // 新增：从业主信息中拼接地址
    propertyAddress() {
      if (this.authStatus && this.ownerProfile) {
        return this.ownerProfile.mergedProperties || '';
      }
      return '';
    },
  
    greeting() {
      const hour = new Date().getHours()
      if (hour < 12) return '早上好'
      if (hour < 18) return '下午好'
      return '晚上好'
    },
    // 过滤后的主要服务列表（根据用户身份过滤）
    filteredMainServices() {
      return this.mainServices.filter(service => {
        // 如果是业委会会议，需要检查用户是否为业委会成员
        if (service.name === '业委会会议') {
          return this.ownerProfile.isOwner === 2 // 2表示业委会成员
        }
        return true // 其他服务都显示
      })
    }
  },
  onLoad() {
    this.initPage()
  },
  async onShow() {
    // 每次页面显示时，若已登录则刷新认证信息
    if (this.isLoggedIn) {
      try {
        await this.$store.dispatch('GetProfileInfo')
      } catch (error) {
        console.warn('获取认证信息失败', error)
      }
    }
    await this.loadCommunityOptions()
    // 重新加载数据，确保登录状态变化后数据正确显示
    this.loadData()
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
    async loadCommunityOptions() {
      if (!this.isLoggedIn) {
        this.communityOptions = []
        this.resetCommunityInfo()
        return
      }
      try {
        const propertyRes = await listMyProperty({ status: '1', pageNum: 1, pageSize: 100 })
        const propertyRows = propertyRes.rows || []
        const communityIds = Array.from(new Set(propertyRows.map(item => item.communityId).filter(Boolean)))
        if (communityIds.length === 0) {
          this.communityOptions = []
          this.resetCommunityInfo({ clearStorage: true })
          return
        }
        const communityRes = await listCommunity({ pageNum: 1, pageSize: 1000 })
        const communities = (communityRes.rows || []).filter(item => communityIds.includes(item.communityId))
        const options = communities.map(item => ({
          value: item.communityId,
          name: item.communityName,
          address: item.address,
          phone: item.contactPhone,
          raw: item
        }))
        this.communityOptions = options
        if (options.length > 0) {
          const storedId = uni.getStorageSync(COMMUNITY_STORAGE_KEY)
          let index = options.findIndex(option => option.value === storedId)
          if (index === -1) {
            index = 0
          }
          this.applyCommunity(options[index], index)
        } else {
          this.resetCommunityInfo({ clearStorage: true })
        }
      } catch (error) {
        console.error('加载小区信息失败', error)
        this.communityOptions = []
        this.resetCommunityInfo({ fallbackToStored: true })
      }
    },
    handleCommunityChange(event) {
      const index = Number(event.detail.value)
      const option = this.communityOptions[index]
      if (option) {
        this.applyCommunity(option, index)
      }
    },
    applyCommunity(option, index = 0) {
      if (option) {
        this.selectedCommunityIndex = index
        this.selectedCommunityId = option.value
        this.communityInfo = {
          ...this.defaultCommunityInfo,
          name: option.name || this.defaultCommunityInfo.name,
          address: option.address || this.defaultCommunityInfo.address,
          phone: option.phone || this.defaultCommunityInfo.phone
        }
        uni.setStorageSync(COMMUNITY_STORAGE_KEY, option.value)
        uni.setStorageSync(COMMUNITY_INFO_STORAGE_KEY, this.communityInfo)
      } else {
        this.resetCommunityInfo()
      }
    },
    resetCommunityInfo(options = {}) {
      const { fallbackToStored = false, clearStorage = false } = options
      let info = null
      if (fallbackToStored) {
        const storedInfo = uni.getStorageSync(COMMUNITY_INFO_STORAGE_KEY)
        if (storedInfo && storedInfo.name) {
          info = storedInfo
        }
      }
      this.communityInfo = info || { ...this.defaultCommunityInfo }
      this.selectedCommunityId = null
      this.selectedCommunityIndex = 0
      if (clearStorage) {
        uni.removeStorageSync(COMMUNITY_STORAGE_KEY)
        uni.removeStorageSync(COMMUNITY_INFO_STORAGE_KEY)
      }
    },
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
      // 只有在已登录时才调用需要认证的API，否则使用模拟数据
      // if (this.isLoggedIn) {
        this.getBannerList(); // 获取轮播图
        this.getNoticeList(); // 调用真实API
        this.getCommunityNoticeList(); // 获取小区公告
      // } else {
      //   this.getMockNoticeList(); // 使用模拟数据
      // }
      this.updateDynamicData()
    },

    // 获取轮播图列表
    getBannerList() {
      listBanner()
        .then(response => {
          const data = response.data || [];
          this.bannerList = data.map(item => ({
            bannerId: item.bannerId,
            image: item.bannerImage,
            title: item.bannerTitle,
            linkType: item.linkType,
            linkId: item.linkId,
            linkUrl: item.linkUrl
          }));
        })
        .catch(error => {
          console.error("获取轮播图失败", error);
          // 失败时使用默认轮播图
          this.bannerList = [
            {
              image: 'https://img.icons8.com/color/480/apartment.png',
              title: '智慧物业管理',
              linkType: null,
              linkId: null,
              linkUrl: null
            },
            {
              image: 'https://img.icons8.com/color/480/home.png',
              title: '温馨家园',
              linkType: null,
              linkId: null,
              linkUrl: null
            }
          ];
        });
    },

    // 获取公告列表
    getNoticeList() {
      // 只获取最新的几条，比如5条
      listNotice({ pageNum: 1, pageSize: 5 })
        .then(response => {
          const rows = Array.isArray(response?.rows) ? response.rows : [];
          this.noticeList = rows.map(item => this.normalizeNotice(item));
        })
        .catch(error => {
          console.error("获取公告失败", error);
          this.noticeList = [];
        });
    },

    // 获取小区公告列表（显示10条）
    getCommunityNoticeList() {
      listNotice({ pageNum: 1, pageSize: 10 })
        .then(response => {
          const rows = Array.isArray(response?.rows) ? response.rows : [];
          this.communityNoticeList = rows.map(item => this.normalizeNotice(item));
        })
        .catch(error => {
          console.error("获取小区公告失败", error);
          this.communityNoticeList = [];
        });
    },
    
    // 获取模拟公告列表（用于未登录状态）
    getMockNoticeList() {
      // 使用模拟公告数据
      const mockNotices = [
        {
          noticeId: 1,
          noticeTitle: '欢迎使用智慧物业管理系统',
          noticeContent: '本系统为业主提供便民服务，包括投诉建议、投票表决、资金公示等功能。',
          createTime: '2024-01-20',
          isNew: true
        },
        {
          noticeId: 2,
          noticeTitle: '物业服务时间调整通知',
          noticeContent: '因物业服务工作优化，服务时间调整为周一至周日9:00-18:00',
          createTime: '2024-01-18',
          isNew: false
        },
        {
          noticeId: 3,
          noticeTitle: '小区公共设施维护公告',
          noticeContent: '近日将对小区公共设施进行例行维护，如有不便敬请谅解',
          createTime: '2024-01-15',
          isNew: false
        }
      ];
      
      this.noticeList = mockNotices.map(notice => this.normalizeNotice(notice));
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

    handleServiceClick(service) {

      // 检查是否绑定手机号
      if (!this.checkPhoneBound()) {
        return
      }

      // 检查业委会会议权限
      if (service.name === '业委会会议' && this.ownerProfile.isOwner !== 2) {
        uni.showModal({
          title: '权限不足',
          content: '您不是业委会成员，无法访问业委会会议功能',
          showCancel: false,
          confirmText: '我知道了'
        })
        return
      }

      if (service.path) {
        uni.navigateTo({
          url: service.path,
          fail: (err) => {
            console.error('导航失败:', err)
            uni.showToast({
              title: '页面跳转失败',
              icon: 'none',
              duration: 2000
            })
          }
        })
      } else {
        uni.showToast({
          title: `${service.name}功能开发中`,
          icon: 'none',
          duration: 2000
        })
      }
    },
    
    goAuth() {
      // 如果未认证，先检查是否绑定手机号
      if (!this.authStatus) {
        // 检查是否绑定手机号
        if (!this.checkPhoneBound()) {
          return
        }

        // 已绑定手机号，跳转到业主认证页面
        uni.navigateTo({
          url: '/pageB/property/add',
          fail: (err) => {
            console.error('跳转业主认证页面失败:', err)
            uni.showToast({
              title: '页面跳转失败',
              icon: 'none'
            })
          }
        })
      }
    },

    // 检查是否绑定手机号（仅检查，不弹窗）
    isPhoneBound() {
      // 从store中获取用户手机号
      const phonenumber = this.user.phonenumber
      return phonenumber && phonenumber.trim() !== ''
    },

    // 检查是否绑定手机号（检查并弹窗）
    checkPhoneBound() {
      if (!this.isPhoneBound()) {
        // 未绑定手机号，弹出绑定弹窗
        this.$refs.phoneBindModal.open()
        return false
      }
      return true
    },

    // 手机号绑定成功回调
    onPhoneBindSuccess() {
      // 绑定成功后刷新用户信息
      this.$store.dispatch('GetInfo').then((res) => {
        uni.showToast({
          title: '产权绑定成功',
          icon: 'success'
        })
      }).catch(error => {
        console.error('刷新用户信息失败:', error)
      })
    },
    
    goToLogin() {
      // 跳转到登录页面
      uni.navigateTo({ url: '/pages/login' })
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
      uni.navigateTo({ url: '/pageB/property/regulation/index' })
    },
    
    goNews() {
      uni.navigateTo({ url: '/pages/common/news/index' })
    },
    
    goToNoticeList() {
      uni.navigateTo({ url: '/pages/common/notice/list' })
    },
    
    // 查看详情方法
    viewNotice(noticeId) {
      uni.navigateTo({
        url: `/pages/common/notice/detail?id=${noticeId}`,
        success: (res) => {
          if (res && res.eventChannel && typeof res.eventChannel.emit === 'function') {
            res.eventChannel.emit('noticeDetailParams', { noticeId: noticeId })
          }
        }
      })
    },

    normalizeNotice(notice = {}) {
      const formattedCreateTime = notice.createTime
        ? this.parseTime(notice.createTime, '{y}-{m}-{d}')
        : ''
      return {
        ...notice,
        summary: this.getNoticeSummary(
          notice.noticeContent || notice.noticeContentText || notice.remark || ''
        ),
        formattedCreateTime
      }
    },

    getNoticeSummary(content) {
      const text = this.stripHtml(content)
        .replace(/\s+/g, ' ')
        .trim()
      if (!text) {
        return '暂无公告内容'
      }
      return text.length > 60 ? `${text.slice(0, 60)}...` : text
    },

    stripHtml(content = '') {
      return content
        .replace(/<[^>]*>/g, '')
        .replace(/&nbsp;/gi, ' ')
        .replace(/&amp;/gi, '&')
        .replace(/&lt;/gi, '<')
        .replace(/&gt;/gi, '>')
        .replace(/&quot;/gi, '"')
        .replace(/&#39;/gi, "'")
    },



    
    viewNews(news) {
      uni.navigateTo({
        url: `/pages/common/news/detail?id=${news.id}`
      })
    },

    // 快捷入口点击事件
    goToOwnerChange() {
      // 检查是否绑定手机号
      if (!this.checkPhoneBound()) {
        return
      }

      uni.navigateTo({
        url: '/pageB/property/meeting/index',
        fail: (err) => {
          console.error('跳转业主变更页面失败:', err);
          uni.showToast({
            title: '页面跳转失败',
            icon: 'none'
          });
        }
      });
    },

    goToPropertyAuth() {
      // 检查是否绑定手机号
      if (!this.checkPhoneBound()) {
        return
      }

      uni.navigateTo({
        url: '/pages/work/opinion/index',
        fail: (err) => {
          console.error('跳转产权认证页面失败:', err);
          uni.showToast({
            title: '页面跳转失败',
            icon: 'none'
          });
        }
      });
    },

    // 轮播图点击事件
    handleBannerClick(banner) {
      console.log('点击轮播图:', banner);

      // 如果没有链接类型，则不跳转
      if (!banner.linkType) {
        console.log('轮播图没有设置链接');
        return;
      }

      // 根据链接类型进行不同的跳转
      switch (banner.linkType) {
        case 'meeting':
          // 跳转到业主大会详情
          if (banner.linkId) {
            uni.navigateTo({
              url: `/pageB/property/meeting/detail?id=${banner.linkId}`,
              fail: (err) => {
                console.error('跳转业主大会详情失败:', err);
                uni.showToast({
                  title: '页面跳转失败',
                  icon: 'none'
                });
              }
            });
          } else {
            uni.showToast({
              title: '业主大会ID无效',
              icon: 'none'
            });
          }
          break;

        case 'opinion':
          // 跳转到意见征询详情
          if (banner.linkId) {
            uni.navigateTo({
              url: `/pages/work/opinion/detail?id=${banner.linkId}`,
              fail: (err) => {
                console.error('跳转意见征询详情失败:', err);
                uni.showToast({
                  title: '页面跳转失败',
                  icon: 'none'
                });
              }
            });
          } else {
            uni.showToast({
              title: '意见征询ID无效',
              icon: 'none'
            });
          }
          break;

        case 'url':
          // 自定义链接跳转
          if (banner.linkUrl) {
            uni.navigateTo({
              url: banner.linkUrl,
              fail: (err) => {
                console.error('自定义链接跳转失败:', err);
                uni.showToast({
                  title: '页面跳转失败',
                  icon: 'none'
                });
              }
            });
          } else {
            uni.showToast({
              title: '链接地址无效',
              icon: 'none'
            });
          }
          break;

        default:
          console.log('未知的链接类型:', banner.linkType);
      }
    },

    // 根据公告获取对应的图标
    getNoticeIcon(notice) {
      // 根据公告标题关键词返回不同样式的图标背景
      const title = notice.noticeTitle || '';

      // 业主大会相关
      if (title.includes('业主大会') || title.includes('会议')) {
        return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgZmlsbD0iI0ZGNjY2NiIvPjx0ZXh0IHg9IjUwIiB5PSI1MCIgZm9udC1zaXplPSIyNCIgZmlsbD0iI0ZGRiIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZG9taW5hbnQtYmFzZWxpbmU9Im1pZGRsZSI+5Lia5Li75aSn5LygPC90ZXh0Pjwvc3ZnPg==';
      }

      // 投票相关
      if (title.includes('投票') || title.includes('表决')) {
        return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgZmlsbD0iI0VEMzkzOSIvPjx0ZXh0IHg9IjUwIiB5PSI1MCIgZm9udC1zaXplPSIyNCIgZmlsbD0iI0ZGRiIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZG9taW5hbnQtYmFzZWxpbmU9Im1pZGRsZSI+5oqV56Wo5rWB56iLPC90ZXh0Pjwvc3ZnPg==';
      }

      // 规则相关
      if (title.includes('规约') || title.includes('规则') || title.includes('制度')) {
        return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgZmlsbD0iI0VEMzkzOSIvPjx0ZXh0IHg9IjUwIiB5PSI1MCIgZm9udC1zaXplPSIyNCIgZmlsbD0iI0ZGRiIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZG9taW5hbnQtYmFzZWxpbmU9Im1pZGRsZSI+5Lia5Li75aSn5LygPC90ZXh0Pjwvc3ZnPg==';
      }

      // 管理规约
      if (title.includes('管理')) {
        return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgZmlsbD0iI0VEMzkzOSIvPjx0ZXh0IHg9IjUwIiB5PSI1MCIgZm9udC1zaXplPSIyNCIgZmlsbD0iI0ZGRiIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZG9taW5hbnQtYmFzZWxpbmU9Im1pZGRsZSI+5Lia5Li75aSn5LygPC90ZXh0Pjwvc3ZnPg==';
      }

      // 默认图标
      return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgZmlsbD0iI0VEMzkzOSIvPjx0ZXh0IHg9IjUwIiB5PSI1MCIgZm9udC1zaXplPSIyNCIgZmlsbD0iI0ZGRiIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZG9taW5hbnQtYmFzZWxpbmU9Im1pZGRsZSI+5Lia5Li75aSn5LygPC90ZXh0Pjwvc3ZnPg==';
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

/* 轮播图区域 */
.banner-section {
  margin: 0 30rpx 30rpx;

  .banner-swiper {
    height: 360rpx;
    border-radius: 24rpx;
    overflow: hidden;

    .banner-image {
      width: 100%;
      height: 100%;
    }
  }
}

/* 快捷入口魔方按钮 */
.quick-entry-section {
  margin: 0 30rpx 30rpx;

  .quick-entry-grid {
    display: flex;
    gap: 24rpx;
  }

  .quick-entry-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    // padding: 40rpx 20rpx;
    background: #FFFFFF;
    border-radius: 24rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
    border: 1rpx solid #F0F0F0;
    transition: all 0.3s;

    &:active {
      transform: scale(0.96);
      box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.04);
    }

    .quick-entry-icon {
      width: 100rpx;
      height: 100rpx;
      border-radius: 24rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 20rpx;
      box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.12);
    }

    .quick-entry-text {
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
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
    
    .location-picker {
      display: flex;
      align-items: center;
    }
    
    .location-info {
      display: flex;
      align-items: center;
      
      .location-text {
        margin-left: 12rpx;
        font-size: 32rpx;
        font-weight: 600;
        color: #262626;
      }
      
      .location-arrow {
        margin-left: 8rpx;
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
      
      .property-info {
        display: flex;
        align-items: center;
        margin-bottom: 16rpx;
        padding: 8rpx 0;
        
        .property-text {
          font-size: 26rpx;
          color: #666;
          font-weight: 500;
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
      
      .login-status {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 16rpx 24rpx;
        background: #F8F9FA;
        border-radius: 16rpx;
        border: 1rpx solid #E8E8E8;
        
        .login-text {
          font-size: 26rpx;
          color: #8C8C8C;
        }
        
        .login-link {
          font-size: 28rpx;
          color: #1890FF;
          font-weight: 500;
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

.regulations-section,
.latest-news {
  margin-top: 30rpx;
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

/* 主要服务模块 */
.main-services {
  margin-top: 20rpx; /* 应用 consistent horizontal margin */

  .service-item.main-service {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40rpx 20rpx;
    background: #FFFFFF;
    border-radius: 24rpx;
    border: 1rpx solid #F0F0F0;

    .service-icon {
      width: 88rpx;
      height: 88rpx;
      border-radius: 44rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 24rpx;
    }

    .service-name {
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
      margin-bottom: 8rpx;
    }

    .service-desc {
      font-size: 24rpx;
      color: #8C8C8C;
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

/* 小区公告模块 */
.community-notice-section {
  margin: 0 30rpx 40rpx;
  padding: 40rpx 30rpx;
  background: #FFFFFF;
  border-radius: 24rpx;

  .section-header {
    margin-bottom: 30rpx;
  }

  .notice-list {
    display: flex;
    flex-direction: column;
  }

  .notice-list-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 32rpx 0;
    border-bottom: 1rpx solid #F0F0F0;
    transition: all 0.3s;

    &:last-child {
      border-bottom: none;
    }

    &:active {
      opacity: 0.7;
    }

    .notice-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 12rpx;
      overflow: hidden;
      margin-right: 24rpx;

      .notice-item-title {
        font-size: 30rpx;
        font-weight: 500;
        color: #262626;
        line-height: 1.5;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
      }

      .notice-item-date {
        font-size: 26rpx;
        color: #8C8C8C;
      }
    }

    .notice-thumbnail {
      width: 132rpx;
      height: 132rpx;
      border-radius: 16rpx;
      overflow: hidden;
      flex-shrink: 0;
      background: #F5F5F5;

      .thumbnail-image {
        width: 100%;
        height: 100%;
      }

      .thumbnail-placeholder {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, #FFE7E7, #FFEDED);

        .thumbnail-icon {
          width: 80%;
          height: 80%;
        }
      }
    }
  }

  .empty-notice {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 80rpx 0;
    gap: 24rpx;

    .empty-text {
      font-size: 28rpx;
      color: #8C8C8C;
    }
  }

  .load-more-tip {
    text-align: center;
    font-size: 24rpx;
    color: #BFBFBF;
    padding: 30rpx 0 10rpx;
  }
}

/* 底部安全距离 */
.safe-area-bottom {
  height: env(safe-area-inset-bottom);
  background: #fff;
}

.tishi {
  text-align: center;
  font-size: 12px;
  color: red;
}
</style>
