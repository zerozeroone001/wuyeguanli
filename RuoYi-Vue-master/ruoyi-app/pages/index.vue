<template>
  <view class="home-container">
    <!-- Banner -->
    <swiper class="swiper" :indicator-dots="true" :autoplay="true" :interval="3000" :duration="500">
      <swiper-item>
        <image class="swiper-image" src="https://images.pexels.com/photos/269077/pexels-photo-269077.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" mode="aspectFill"></image>
      </swiper-item>
      <swiper-item>
        <image class="swiper-image" src="https://images.pexels.com/photos/1571460/pexels-photo-1571460.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" mode="aspectFill"></image>
      </swiper-item>
      <swiper-item>
        <image class="swiper-image" src="https://images.pexels.com/photos/276724/pexels-photo-276724.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" mode="aspectFill"></image>
      </swiper-item>
    </swiper>

    <!-- Announcements -->
    <view class="notice-bar">
        <uni-notice-bar scrollable single text="[重要] 关于小区电费调整的通知，请各位业主周知。"></uni-notice-bar>
    </view>

    <!-- Grid Menu -->
    <uni-grid :column="3" :show-border="false" :square="true">
      <uni-grid-item v-for="(item, index) in gridItems" :key="index" @click.native="goToPage(item.path)">
        <view class="grid-item-box">
          <image class="grid-item-image" :src="item.icon" mode="aspectFill"></image>
          <text class="grid-item-text">{{ item.text }}</text>
          <view v-if="item.badge" class="badge">{{ item.badge }}</view>
        </view>
      </uni-grid-item>
    </uni-grid>

    <!-- Recent Hotspots -->
    <view class="hotspots-section">
        <h3 class="section-title">近期热点</h3>
        <uni-list>
            <uni-list-item v-for="(item, index) in hotSpots" :key="index" :title="item.title" :note="item.note" showArrow :to="item.path">
                <template v-slot:header>
                    <view class="hotspot-tag" :style="{backgroundColor: item.tagColor}">{{ item.tag }}</view>
                </template>
            </uni-list-item>
        </uni-list>
    </view>

    <!-- Service Guide -->
    <view class="guide-section">
        <h3 class="section-title">办事指南</h3>
        <uni-grid :column="4" :show-border="false" :square="true">
          <uni-grid-item v-for="(item, index) in guideItems" :key="index" @click.native="goToPage(item.path)">
            <view class="grid-item-box">
              <image class="guide-item-image" :src="item.icon" mode="aspectFill"></image>
              <text class="grid-item-text">{{ item.text }}</text>
            </view>
          </uni-grid-item>
        </uni-grid>
    </view>

  </view>
</template>

<script>
import uniNoticeBar from '@/uni_modules/uni-notice-bar/components/uni-notice-bar/uni-notice-bar.vue'
export default {
  components: {
    uniNoticeBar
  },
  data() {
    return {
      gridItems: [
        {
          text: '日常管理',
          icon: 'https://cdn-icons-png.flaticon.com/512/149/149294.png',
          path: '/pages/daily-management/index',
          badge: 3
        },
        {
          text: '合同履行',
          icon: 'https://cdn-icons-png.flaticon.com/512/1534/1534334.png',
          path: '/pages/contract-fulfillment/index'
        },
        {
          text: '资金管理',
          icon: 'https://cdn-icons-png.flaticon.com/512/181/181314.png',
          path: '/pages/fund-management/index'
        },
        {
          text: '法律咨询',
          icon: 'https://cdn-icons-png.flaticon.com/512/299/299110.png',
          path: '/pages/legal-consultation/index'
        },
        {
          text: '公证服务',
          icon: 'https://cdn-icons-png.flaticon.com/512/3437/3437433.png',
          path: '/pages/notary-service/index'
        },
        {
          text: '投诉',
          icon: 'https://cdn-icons-png.flaticon.com/512/2959/2959351.png',
          path: '/pages/complaints/index'
        }
      ],
      hotSpots: [
          {
              tag: '投票',
              tagColor: '#F56C6C',
              title: '关于聘请新物业公司的投票',
              note: '截止日期: 2025-09-20',
              path: '/pages/daily-management/voting-detail?id=1'
          },
          {
              tag: '公告',
              tagColor: '#409EFF',
              title: '关于国庆期间小区停车安排的通知',
              note: '发布日期: 2025-09-18',
              path: '/pages/common/textview/index?title=关于国庆期间小区停车安排的通知'
          },
          {
              tag: '征询',
              tagColor: '#67C23A',
              title: '关于小区绿化升级改造的意见征询',
              note: '截止日期: 2025-09-15',
              path: '/pages/daily-management/solicitation-detail?id=1'
          }
      ],
      guideItems: [
          {
              text: '在线报修',
              icon: 'https://cdn-icons-png.flaticon.com/512/2918/2918511.png',
              path: '/pages/repair/index'
          },
          {
              text: '费用缴纳',
              icon: 'https://cdn-icons-png.flaticon.com/512/1086/1086741.png',
              path: '/pages/payment/index'
          },
          {
              text: '访客邀请',
              icon: 'https://cdn-icons-png.flaticon.com/512/3048/3048348.png',
              path: '/pages/visitor/index'
          },
          {
              text: '投诉建议',
              icon: 'https://cdn-icons-png.flaticon.com/512/1945/1945947.png',
              path: '/pages/complaints/index'
          }
      ]
    };
  },
  methods: {
    goToPage(path) {
      uni.navigateTo({ url: path });
    }
  }
};
</script>

<style scoped>
.home-container {
  background-color: #f8f8f8;
}
.swiper {
  height: 300rpx;
}
.swiper-image {
  width: 100%;
  height: 100%;
}
.notice-bar {
    margin: 10rpx 0;
}
.grid-item-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  position: relative;
}
.grid-item-image {
  width: 50rpx;
  height: 50rpx;
  margin-bottom: 8rpx;
}
.guide-item-image {
    width: 50rpx;
    height: 50rpx;
    margin-bottom: 10rpx;
}
.grid-item-text {
  font-size: 24rpx;
}
.badge {
  position: absolute;
  top: 10rpx;
  right: 10rpx;
  background-color: #F56C6C;
  color: #fff;
  border-radius: 50%;
  padding: 4rpx 8rpx;
  font-size: 20rpx;
  line-height: 1;
}
.section-title {
    font-size: 32rpx;
    font-weight: bold;
    padding: 20rpx;
}
.hotspot-tag {
    color: #fff;
    border-radius: 4rpx;
    padding: 2rpx 8rpx;
    font-size: 20rpx;
    margin-right: 10rpx;
}
</style>
