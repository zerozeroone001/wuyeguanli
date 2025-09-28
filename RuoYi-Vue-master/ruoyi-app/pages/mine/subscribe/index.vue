<template>
  <view class="subscribe-settings">
    <uni-section title="订阅消息设置" type="line">
      <view class="setting-item" v-for="item in subscribeList" :key="item.id">
        <view class="setting-content">
          <view class="setting-title">{{ item.title }}</view>
          <view class="setting-desc">{{ item.desc }}</view>
        </view>
        <switch 
          :checked="item.enabled" 
          :disabled="!isTemplateConfigured(item.id)"
          @change="onSwitchChange(item)"
          color="#1890FF"
        />
      </view>
    </uni-section>
    
    <view class="tips">
      <text class="tips-text">开启订阅消息后，您将及时收到相关通知</text>
    </view>
  </view>
</template>

<script>
import SubscribeMessageUtils from '@/utils/subscribeMessage'
import SubscribeConfig from '@/utils/subscribeConfig'

export default {
  name: 'SubscribeSettings',
  data() {
    return {
      subscribeList: [
        {
          id: 'complaint',
          title: '投诉处理通知',
          desc: '投诉处理进度和结果通知',
          templateId: SubscribeConfig.getTemplateId('complaint'),
          enabled: false
        },
        {
          id: 'meeting',
          title: '会议通知',
          desc: '业主大会、业委会会议通知',
          templateId: SubscribeConfig.getTemplateId('meeting'),
          enabled: false
        },
        {
          id: 'payment',
          title: '缴费提醒',
          desc: '物业费、水电费等缴费提醒',
          templateId: SubscribeConfig.getTemplateId('payment'),
          enabled: false
        },
        {
          id: 'repair',
          title: '维修进度通知',
          desc: '设备维修进度和结果通知',
          templateId: SubscribeConfig.getTemplateId('repair'),
          enabled: false
        }
      ]
    }
  },
  onLoad() {
    this.checkSubscribeStatus()
  },
  methods: {
    // 检查订阅状态
    async checkSubscribeStatus() {
      try {
        for (let item of this.subscribeList) {
          // 检查模板ID是否已配置
          if (!this.isTemplateConfigured(item.id)) {
            item.enabled = false
            continue
          }
          
          const status = await SubscribeMessageUtils.checkSubscribeStatus(item.templateId)
          // 微信小程序的订阅消息状态判断
          // 'granted' 表示已授权，'deny' 表示被拒绝，'undetermined' 表示未确定
          item.enabled = status.status === 'granted'
        }
      } catch (error) {
        console.error('检查订阅状态失败:', error)
        // 如果检查失败，将所有状态设为false
        this.subscribeList.forEach(item => {
          item.enabled = false
        })
      }
    },
    
    // 开关变化处理
    async onSwitchChange(item) {
      // 检查模板ID是否已配置
      if (!this.isTemplateConfigured(item.id)) {
        uni.showToast({
          title: '该功能暂未配置',
          icon: 'none'
        })
        return
      }
      
      if (item.enabled) {
        // 如果当前是开启状态，点击后关闭
        item.enabled = false
        uni.showToast({
          title: '已关闭订阅',
          icon: 'none'
        })
      } else {
        // 如果当前是关闭状态，点击后请求授权
        try {
          const result = await SubscribeMessageUtils.requestSubscribe(item.templateId)
          if (result) {
            item.enabled = true
            uni.showToast({
              title: '订阅成功',
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
    },
    
    // 检查模板ID是否已配置
    isTemplateConfigured(type) {
      return SubscribeConfig.isTemplateConfigured(type)
    }
  }
}
</script>

<style lang="scss" scoped>
.subscribe-settings {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 20rpx;
  background-color: #fff;
  margin-bottom: 20rpx;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.setting-content {
  flex: 1;
  margin-right: 20rpx;
}

.setting-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 10rpx;
}

.setting-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.4;
}

.tips {
  margin-top: 40rpx;
  padding: 20rpx;
  background-color: #fff;
  border-radius: 10rpx;
  text-align: center;
}

.tips-text {
  font-size: 28rpx;
  color: #999;
}
</style>