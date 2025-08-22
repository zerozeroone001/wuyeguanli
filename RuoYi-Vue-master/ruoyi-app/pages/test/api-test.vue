<template>
  <view class="container">
    <uni-nav-bar 
      title="接口测试" 
      left-icon="back" 
      @clickLeft="goBack"
      background-color="#1890FF"
      color="#fff"
    />
    
    <view class="test-content">
      <view class="test-section">
        <view class="section-title">用户认证接口</view>
        <view class="test-buttons">
          <button @click="testLogin" class="test-btn">测试登录</button>
          <button @click="testGetInfo" class="test-btn">获取用户信息</button>
          <button @click="testGetCaptcha" class="test-btn">获取验证码</button>
        </view>
      </view>
      
      <view class="test-section">
        <view class="section-title">首页数据接口</view>
        <view class="test-buttons">
          <button @click="testHomeData" class="test-btn">首页数据</button>
          <button @click="testNotices" class="test-btn">公告列表</button>
          <button @click="testWeather" class="test-btn">天气信息</button>
        </view>
      </view>
      
      <view class="test-section">
        <view class="section-title">投诉管理接口</view>
        <view class="test-buttons">
          <button @click="testComplaintList" class="test-btn">投诉列表</button>
          <button @click="testAddComplaint" class="test-btn">提交投诉</button>
        </view>
      </view>
      
      <view class="test-section">
        <view class="section-title">会议管理接口</view>
        <view class="test-buttons">
          <button @click="testMeetingList" class="test-btn">会议列表</button>
          <button @click="testMeetingDetail" class="test-btn">会议详情</button>
        </view>
      </view>
      
      <view class="test-section">
        <view class="section-title">资金管理接口</view>
        <view class="test-buttons">
          <button @click="testFundOverview" class="test-btn">资金概览</button>
          <button @click="testFundFlow" class="test-btn">资金流水</button>
        </view>
      </view>
      
      <view class="test-section">
        <view class="section-title">制度管理接口</view>
        <view class="test-buttons">
          <button @click="testRegulationList" class="test-btn">制度列表</button>
        </view>
      </view>
    </view>
    
    <!-- 结果显示区域 -->
    <view class="result-section" v-if="testResult">
      <view class="result-title">测试结果</view>
      <view class="result-content">{{ testResult }}</view>
    </view>
  </view>
</template>

<script>
import { login, getInfo, getCodeImg } from '@/api/login'
import { getHomeData, getNotices, getWeather } from '@/api/home'
import { listMyComplaint, addComplaint } from '@/api/property/complaint'
import { listMeeting, getMeeting } from '@/api/property/meeting'
import { getFundOverview, listFundFlow } from '@/api/property/fund'
import { listRegulation } from '@/api/property/regulation'

export default {
  data() {
    return {
      testResult: ''
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    
    // 用户认证接口测试
    async testLogin() {
      try {
        const result = await login('admin', 'admin123', '', '')
        this.showResult('登录接口', result)
      } catch (error) {
        this.showError('登录接口', error)
      }
    },
    
    async testGetInfo() {
      try {
        const result = await getInfo()
        this.showResult('获取用户信息', result)
      } catch (error) {
        this.showError('获取用户信息', error)
      }
    },
    
    async testGetCaptcha() {
      try {
        const result = await getCodeImg()
        this.showResult('获取验证码', result)
      } catch (error) {
        this.showError('获取验证码', error)
      }
    },
    
    // 首页数据接口测试
    async testHomeData() {
      try {
        const result = await getHomeData()
        this.showResult('首页数据', result)
      } catch (error) {
        this.showError('首页数据', error)
      }
    },
    
    async testNotices() {
      try {
        const result = await getNotices()
        this.showResult('公告列表', result)
      } catch (error) {
        this.showError('公告列表', error)
      }
    },
    
    async testWeather() {
      try {
        const result = await getWeather()
        this.showResult('天气信息', result)
      } catch (error) {
        this.showError('天气信息', error)
      }
    },
    
    // 投诉管理接口测试
    async testComplaintList() {
      try {
        const result = await listMyComplaint()
        this.showResult('投诉列表', result)
      } catch (error) {
        this.showError('投诉列表', error)
      }
    },
    
    async testAddComplaint() {
      try {
        const testData = {
          complaintTitle: '测试投诉',
          complaintContent: '这是一个测试投诉内容',
          complaintType: '1',
          urgencyLevel: '2'
        }
        const result = await addComplaint(testData)
        this.showResult('提交投诉', result)
      } catch (error) {
        this.showError('提交投诉', error)
      }
    },
    
    // 会议管理接口测试
    async testMeetingList() {
      try {
        const result = await listMeeting()
        this.showResult('会议列表', result)
      } catch (error) {
        this.showError('会议列表', error)
      }
    },
    
    async testMeetingDetail() {
      try {
        const result = await getMeeting(1)
        this.showResult('会议详情', result)
      } catch (error) {
        this.showError('会议详情', error)
      }
    },
    
    // 资金管理接口测试
    async testFundOverview() {
      try {
        const result = await getFundOverview()
        this.showResult('资金概览', result)
      } catch (error) {
        this.showError('资金概览', error)
      }
    },
    
    async testFundFlow() {
      try {
        const result = await listFundFlow()
        this.showResult('资金流水', result)
      } catch (error) {
        this.showError('资金流水', error)
      }
    },
    
    // 制度管理接口测试
    async testRegulationList() {
      try {
        const result = await listRegulation()
        this.showResult('制度列表', result)
      } catch (error) {
        this.showError('制度列表', error)
      }
    },
    
    // 显示成功结果
    showResult(apiName, result) {
      this.testResult = `${apiName} - 成功:\n${JSON.stringify(result, null, 2)}`
      console.log(apiName, result)
      uni.showToast({
        title: `${apiName}测试成功`,
        icon: 'success'
      })
    },
    
    // 显示错误结果
    showError(apiName, error) {
      this.testResult = `${apiName} - 失败:\n${JSON.stringify(error, null, 2)}`
      console.error(apiName, error)
      uni.showToast({
        title: `${apiName}测试失败`,
        icon: 'error'
      })
    }
  }
}
</script>

<style scoped>
.container {
  background: #f5f5f5;
  min-height: 100vh;
}

.test-content {
  padding: 20rpx;
}

.test-section {
  background: #fff;
  margin-bottom: 20rpx;
  border-radius: 12rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.1);
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  border-left: 6rpx solid #1890FF;
  padding-left: 15rpx;
}

.test-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 15rpx;
}

.test-btn {
  background: linear-gradient(135deg, #1890FF, #40a9ff);
  color: #fff;
  border: none;
  border-radius: 8rpx;
  padding: 15rpx 25rpx;
  font-size: 26rpx;
  min-width: 200rpx;
  transition: all 0.3s ease;
}

.test-btn:active {
  transform: scale(0.95);
  opacity: 0.8;
}

.result-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 12rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.1);
  max-height: 600rpx;
  overflow-y: scroll;
}

.result-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  border-left: 6rpx solid #52c41a;
  padding-left: 15rpx;
}

.result-content {
  font-size: 24rpx;
  color: #666;
  line-height: 1.6;
  white-space: pre-wrap;
  background: #f8f9fa;
  padding: 20rpx;
  border-radius: 8rpx;
  border: 1rpx solid #e9ecef;
}
</style>