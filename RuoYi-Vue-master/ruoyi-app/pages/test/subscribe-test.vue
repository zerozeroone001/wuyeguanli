<template>
  <view class="subscribe-test">
    <uni-section title="订阅消息测试" type="line">
      <view class="test-section">
        <view class="test-item" v-for="item in testList" :key="item.id" @click="handleTest(item)">
          <view class="test-icon" :style="{ backgroundColor: item.bgColor }">
            <uni-icons :type="item.icon" size="20" color="white" />
          </view>
          <view class="test-content">
            <text class="test-title">{{ item.title }}</text>
            <text class="test-desc">{{ item.desc }}</text>
          </view>
          <uni-icons type="right" size="14" color="#C0C4CC" />
        </view>
      </view>
    </uni-section>
    
    <view class="tips-section">
      <view class="tips-header">
        <uni-icons type="info" size="18" color="#1890FF" />
        <text class="tips-title">测试说明</text>
      </view>
      <view class="tips-content">
        <text>• 点击上方按钮可测试订阅消息发送</text>
        <text>• 需要先在小程序中获取用户的openId</text>
        <text>• 确保后端已配置正确的模板ID</text>
        <text>• 测试数据为预设的示例内容</text>
      </view>
    </view>
    
    <!-- 测试结果 -->
    <view class="result-section" v-if="testResult">
      <view class="result-header">
        <uni-icons type="checkmarkempty" size="18" :color="testResult.success ? '#52C41A' : '#F5222D'" />
        <text class="result-title" :class="{ success: testResult.success, error: !testResult.success }">
          {{ testResult.success ? '测试成功' : '测试失败' }}
        </text>
      </view>
      <view class="result-content">
        <text>{{ testResult.message }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { sendMeetingNotification, sendComplaintNotification, sendPaymentReminder, sendRepairNotification } from '@/api/subscribe'

export default {
  name: 'SubscribeTest',
  data() {
    return {
      testResult: null,
      testList: [
        {
          id: 'meeting',
          title: '会议通知测试',
          desc: '测试发送会议通知订阅消息',
          icon: 'calendar',
          bgColor: '#1890FF',
          api: sendMeetingNotification,
          data: {
            openId: 'test_openid_123',
            meetingTitle: '业主大会',
            meetingTime: '2024年01月15日 19:00',
            meetingLocation: '小区会议室'
          }
        },
        {
          id: 'complaint',
          title: '投诉通知测试',
          desc: '测试发送投诉处理通知订阅消息',
          icon: 'chat',
          bgColor: '#F5222D',
          api: sendComplaintNotification,
          data: {
            openId: 'test_openid_123',
            complaintTitle: '噪音投诉',
            status: '处理中',
            remark: '已安排工作人员现场查看'
          }
        },
        {
          id: 'payment',
          title: '缴费提醒测试',
          desc: '测试发送缴费提醒订阅消息',
          icon: 'wallet',
          bgColor: '#52C41A',
          api: sendPaymentReminder,
          data: {
            openId: 'test_openid_123',
            feeType: '物业费',
            amount: '1200元',
            dueDate: '2024年01月31日'
          }
        },
        {
          id: 'repair',
          title: '维修通知测试',
          desc: '测试发送维修进度通知订阅消息',
          icon: 'gear',
          bgColor: '#FA8C16',
          api: sendRepairNotification,
          data: {
            openId: 'test_openid_123',
            repairTitle: '电梯维修',
            status: '已完成',
            remark: '电梯已恢复正常运行'
          }
        }
      ]
    }
  },
  methods: {
    async handleTest(item) {
      try {
        uni.showLoading({
          title: '测试中...'
        })
        
        const response = await item.api(item.data)
        
        this.testResult = {
          success: response.code === 200,
          message: response.msg || (response.code === 200 ? '测试成功' : '测试失败')
        }
        
        uni.hideLoading()
        
        uni.showToast({
          title: this.testResult.success ? '测试成功' : '测试失败',
          icon: this.testResult.success ? 'success' : 'none'
        })
        
      } catch (error) {
        uni.hideLoading()
        
        this.testResult = {
          success: false,
          message: error.message || '测试失败'
        }
        
        uni.showToast({
          title: '测试失败',
          icon: 'none'
        })
        
        console.error('测试失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.subscribe-test {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.test-section {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.test-item {
  display: flex;
  align-items: center;
  padding: 30rpx 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
  
  .test-icon {
    width: 60rpx;
    height: 60rpx;
    border-radius: 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 20rpx;
  }
  
  .test-content {
    flex: 1;
    
    .test-title {
      display: block;
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
      margin-bottom: 8rpx;
    }
    
    .test-desc {
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

.result-section {
  margin-top: 40rpx;
  background-color: #fff;
  border-radius: 10rpx;
  padding: 30rpx;
  
  .result-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .result-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      
      &.success {
        color: #52C41A;
      }
      
      &.error {
        color: #F5222D;
      }
    }
  }
  
  .result-content {
    text {
      font-size: 26rpx;
      color: #666;
      line-height: 1.6;
    }
  }
}
</style>