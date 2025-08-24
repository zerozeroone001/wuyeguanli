<template>
  <view class="work-container">
    <!-- 头部搜索区 -->
    <view class="search-header">
     <!-- <view class="search-box">
        <uni-icons type="search" size="18" color="#8C8C8C" />
        <input class="search-input" placeholder="搜索物业服务" placeholder-style="color: #8C8C8C" />
      </view> -->
      <view class="location-info">
        <uni-icons type="location" size="16" color="#1890FF" />
        <text class="location-text">{{ communityInfo.name }}</text>
      </view>
    </view>

    <!-- 服务分类 -->
    <view class="service-categories">
      <!-- <view class="category-title">
        <text class="title-text">物业服务</text>
        <text class="subtitle-text">Property Services</text>
      </view> -->
      
      <!-- 主要服务 -->
      <view class="main-services">
        <uni-grid :column="3" :showBorder="false" :square="false">
          <uni-grid-item v-for="(service, index) in mainServices" :key="index">
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

      <!-- 便民服务 -->
      <view class="convenience-services">
        <view class="section-header">
          <text class="section-title">便民服务</text>
        </view>
        <view class="service-list">
          <view 
            class="service-row" 
            v-for="(service, index) in convenienceServices" 
            :key="index"
            @click="handleServiceClick(service)"
          >
            <view class="service-left">
              <view class="service-icon tiny" :style="{ backgroundColor: service.bgColor }">
                <uni-icons :type="service.icon" size="16" color="white" />
              </view>
              <view class="service-info">
                <text class="service-name">{{ service.name }}</text>
                <text class="service-desc">{{ service.desc }}</text>
              </view>
            </view>
            <view class="service-right">
              <text class="service-status" :class="service.statusClass">{{ service.status }}</text>
              <uni-icons type="right" size="14" color="#C0C4CC" />
            </view>
          </view>
        </view>
      </view>

      <!-- 联系我们 -->
      <view class="contact-section">
        <view class="contact-card">
          <view class="contact-header">
            <uni-icons type="phone" size="20" color="#1890FF" />
            <text class="contact-title">联系物业</text>
          </view>
          <view class="contact-info">
            <text class="contact-phone">{{ communityInfo.phone }}</text>
            <text class="contact-time">服务时间：8:00-18:00</text>
          </view>
          <view class="contact-actions">
            <view class="contact-btn primary" @click="makePhoneCall">
              <uni-icons type="phone" size="16" color="white" />
              <text>拨打电话</text>
            </view>
            <view class="contact-btn secondary" @click="openChat">
              <uni-icons type="chat" size="16" color="#1890FF" />
              <text>在线客服</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部安全距离 -->
    <view class="safe-area-bottom"></view>
  </view>
</template>

<script>
import config from '@/config'

export default {
  data() {
    return {
      communityInfo: config.property.communityInfo,
      // 主要服务
      mainServices: [
        {
          name: '业主大会',
          desc: '会议投票',
          icon: 'compose',
          bgColor: '#1890FF',
          path: '/pages/property/meeting/index'
        },
        {
          name: '意见征询',
          desc: '问题反馈',
          icon: 'chat',
          bgColor: '#52C41A',
          path: '/pages/property/complaint/index'
        },
        {
          name: '合同查阅',
          desc: '合同管理',
          icon: 'paperplane',
          bgColor: '#FAAD14',
          path: '/pages/contract-fulfillment/index'
        },
        {
          name: '资金公示',
          desc: '财务透明',
          icon: 'wallet',
          bgColor: '#722ED1',
          path: '/pages/fund-management/index'
        },
        {
          name: '法律咨询',
          desc: '专业服务',
          icon: 'help',
          bgColor: '#F5222D',
          path: '/pages/legal-consultation/index'
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
          path: '/pages/notary-service/index'
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
      // 生活服务
      lifeServices: [
        {
          name: '快递代收',
          icon: 'gift',
          bgColor: '#6C5CE7'
        },
        {
          name: '家政服务',
          icon: 'home',
          bgColor: '#A29BFE'
        },
        {
          name: '洗车服务',
          icon: 'car',
          bgColor: '#FD79A8'
        },
        {
          name: '宠物服务',
          icon: 'heart',
          bgColor: '#FDCB6E'
        },
        {
          name: '健身预约',
          icon: 'fire',
          bgColor: '#E17055'
        },
        {
          name: '场地预约',
          icon: 'location',
          bgColor: '#00B894'
        },
        {
          name: '二手交易',
          icon: 'shop',
          bgColor: '#0984E3'
        },
        {
          name: '邻里互助',
          icon: 'help',
          bgColor: '#B2BEC3'
        }
      ],
      // 便民服务
      convenienceServices: [
        {
          name: '物业费查询',
          desc: '查看缴费记录和余额',
          icon: 'wallet',
          bgColor: '#1890FF',
          status: '可用',
          statusClass: 'available'
        },
        {
          name: '停车位管理',
          desc: '车位信息和临时停车',
          icon: 'car',
          bgColor: '#52C41A',
          status: '可用',
          statusClass: 'available'
        },
        {
          name: '门禁管理',
          desc: '门禁卡申请和管理',
          icon: 'locked',
          bgColor: '#FAAD14',
          status: '可用',
          statusClass: 'available'
        },
        {
          name: '装修申请',
          desc: '装修许可和押金管理',
          icon: 'gear',
          bgColor: '#F5222D',
          status: '暂停',
          statusClass: 'unavailable'
        }
      ]
    }
  },
  methods: {
    handleServiceClick(service) {
      console.log('点击了服务:', service.name, service.path)
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
    
    makePhoneCall() {
      uni.makePhoneCall({
        phoneNumber: this.communityInfo.phone,
        success: function () {
          console.log('拨打电话成功')
        },
        fail: function () {
          uni.showToast({
            title: '拨打失败',
            icon: 'none'
          })
        }
      })
    },
    
    openChat() {
      uni.showToast({
        title: '在线客服功能开发中',
        icon: 'none',
        duration: 2000
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

.work-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.search-header {
  background: #FFFFFF;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
  
  .search-box {
    display: flex;
    align-items: center;
    background-color: #F8F9FA;
    border-radius: 24rpx;
    padding: 24rpx 30rpx;
    margin-bottom: 24rpx;
    border: 1rpx solid #F0F0F0;
    
    .search-input {
      flex: 1;
      margin-left: 20rpx;
      font-size: 28rpx;
      color: #262626;
    }
  }
  
  .location-info {
    display: flex;
    align-items: center;
    
    .location-text {
      margin-left: 12rpx;
      color: #262626;
      font-size: 28rpx;
      font-weight: 600;
    }
  }
}

.service-categories {
  padding: 30rpx;
}

.category-title {
  text-align: center;
  margin-bottom: 40rpx;
  
  .title-text {
    display: block;
    font-size: 36rpx;
    font-weight: bold;
    color: #262626;
    margin-bottom: 10rpx;
  }
  
  .subtitle-text {
    font-size: 24rpx;
    color: #8C8C8C;
  }
}

.main-services {
  margin-bottom: 50rpx;
  
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

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
  
  .section-title {
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

.life-services {
  margin-bottom: 50rpx;
  
  .service-item.life-service {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 32rpx 16rpx;
    background: #FFFFFF;
    border-radius: 20rpx;
    border: 1rpx solid #F0F0F0;
    
    .service-icon.small {
      width: 72rpx;
      height: 72rpx;
      border-radius: 36rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16rpx;
    }
    
    .service-name.small {
      font-size: 24rpx;
      color: #262626;
      font-weight: 500;
    }
  }
}

.convenience-services {
  margin-bottom: 50rpx;
  
  .service-list {
    background-color: #FFFFFF;
    border-radius: 24rpx;
    overflow: hidden;
    border: 1rpx solid #F0F0F0;
  }
  
  .service-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 30rpx;
    border-bottom: 1rpx solid #F0F0F0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .service-left {
      display: flex;
      align-items: center;
      flex: 1;
      
      .service-icon.tiny {
        width: 60rpx;
        height: 60rpx;
        border-radius: 30rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 24rpx;
      }
      
      .service-info {
        .service-name {
          display: block;
          font-size: 28rpx;
          color: #262626;
          font-weight: 500;
          margin-bottom: 8rpx;
        }
        
        .service-desc {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
    }
    
    .service-right {
      display: flex;
      align-items: center;
      
      .service-status {
        font-size: 22rpx;
        margin-right: 16rpx;
        
        &.available {
          color: #52C41A;
        }
        
        &.unavailable {
          color: #FF4D4F;
        }
      }
    }
  }
}

.contact-section {
  .contact-card {
    background: #FFFFFF;
    border-radius: 24rpx;
    padding: 40rpx;
    color: #262626;
    border: 1rpx solid #F0F0F0;
    
    .contact-header {
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;
      
      .contact-title {
        font-size: 32rpx;
        font-weight: bold;
        margin-left: 16rpx;
      }
    }
    
    .contact-info {
      margin-bottom: 30rpx;
      
      .contact-phone {
        display: block;
        font-size: 36rpx;
        font-weight: bold;
        margin-bottom: 8rpx;
      }
      
      .contact-time {
        font-size: 24rpx;
        opacity: 0.8;
      }
    }
    
    .contact-actions {
      display: flex;
      gap: 20rpx;
      
      .contact-btn {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 20rpx;
        border-radius: 12rpx;
        font-size: 26rpx;
        
        text {
          margin-left: 12rpx;
        }
        
        &.primary {
          background-color: #1890FF;
          color: white;
        }
        
        &.secondary {
          background-color: #F8F9FA;
          color: #1890FF;
          border: 1rpx solid #E0E0E0;
        }
      }
    }
  }
}

.safe-area-bottom {
  height: 120rpx;
}
</style>
