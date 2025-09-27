<template>
  <view class="subscribe-demo">
    <uni-section title="订阅消息演示" type="line">
      <view class="demo-section">
        <view class="demo-item" v-for="item in demoList" :key="item.id" @click="handleDemo(item)">
          <view class="demo-icon" :style="{ backgroundColor: item.bgColor }">
            <uni-icons :type="item.icon" size="20" color="white" />
          </view>
          <view class="demo-content">
            <text class="demo-title">{{ item.title }}</text>
            <text class="demo-desc">{{ item.desc }}</text>
          </view>
          <uni-icons type="right" size="14" color="#C0C4CC" />
        </view>
      </view>
    </uni-section>
    
    <view class="tips-section">
      <view class="tips-header">
        <uni-icons type="info" size="18" color="#1890FF" />
        <text class="tips-title">使用说明</text>
      </view>
      <view class="tips-content">
        <text>• 点击上方按钮可体验订阅消息授权</text>
        <text>• 授权后可在相应业务场景中收到通知</text>
        <text>• 可在个人中心-订阅消息中管理订阅状态</text>
        <text>• 需要先在微信公众平台配置订阅消息模板</text>
      </view>
    </view>
  </view>
</template>

<script>
import SubscribeMessageUtils from '@/utils/subscribeMessage'
import SubscribeConfig from '@/utils/subscribeConfig'

export default {
  name: 'SubscribeDemo',
  data() {
    return {
      demoList: [
        {
          id: 'complaint',
          title: '投诉处理通知',
          desc: '体验投诉处理进度通知订阅',
          icon: 'chat',
          bgColor: '#F5222D',
          templateId: 'complaint'
        },
        {
          id: 'meeting',
          title: '会议通知',
          desc: '体验业主会议通知订阅',
          icon: 'calendar',
          bgColor: '#1890FF',
          templateId: 'meeting'
        },
        {
          id: 'payment',
          title: '缴费提醒',
          desc: '体验物业费缴费提醒订阅',
          icon: 'wallet',
          bgColor: '#52C41A',
          templateId: 'payment'
        },
        {
          id: 'repair',
          title: '维修进度通知',
          desc: '体验设备维修进度通知订阅',
          icon: 'gear',
          bgColor: '#FA8C16',
          templateId: 'repair'
        }
      ]
    }
  },
  methods: {
    async handleDemo(item) {
      try {
        const templateId = SubscribeConfig.getTemplateId(item.templateId)
        
        // 检查模板ID是否已配置
        if (!SubscribeConfig.isTemplateConfigured(item.templateId)) {
          uni.showModal({
            title: '提示',
            content: `${item.title}的模板ID尚未配置，请在微信公众平台配置后使用`,
            showCancel: false,
            confirmText: '知道了'
          })
          return
        }
        
        // 请求订阅授权
        const result = await SubscribeMessageUtils.requestSubscribe(templateId, `demo_${item.id}`)
        
        if (result) {
          uni.showToast({
            title: `${item.title}订阅成功`,
            icon: 'success'
          })
        } else {
          uni.showToast({
            title: '订阅被拒绝',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('订阅失败:', error)
        uni.showToast({
          title: '订阅失败',
          icon: 'none'
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.subscribe-demo {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.demo-section {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.demo-item {
  display: flex;
  align-items: center;
  padding: 30rpx 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
  
  .demo-icon {
    width: 60rpx;
    height: 60rpx;
    border-radius: 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 20rpx;
  }
  
  .demo-content {
    flex: 1;
    
    .demo-title {
      display: block;
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
      margin-bottom: 8rpx;
    }
    
    .demo-desc {
      font-size: 26rpx;
      color: #666;
    }
  }
}

.tips-section {
  margin-top: 40rpx;
  background-color: #fff;
  border-radius: 10rpx;
  padding: 30rpx;
  
  .tips-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .tips-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #1890FF;
    }
  }
  
  .tips-content {
    text {
      display: block;
      font-size: 24rpx;
      color: #666;
      line-height: 1.6;
      margin-bottom: 8rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}
</style>