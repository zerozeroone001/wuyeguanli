<template>
  <view class="vote-container">
    <!-- 会议信息 -->
    <view class="meeting-info">
      <view class="meeting-header">
        <text class="meeting-title">{{ meetingInfo.meetingTitle }}</text>
        <view class="countdown" v-if="countdown > 0">
          <uni-icons type="clock" size="16" color="#FA8C16" />
          <text class="countdown-text">{{ formatCountdown(countdown) }}</text>
        </view>
      </view>
      <view class="meeting-detail">
        <view class="detail-item">
          <uni-icons type="calendar" size="16" color="#8C8C8C" />
          <text>会议时间：{{ meetingInfo.meetingTime }}</text>
        </view>
        <view class="detail-item">
          <uni-icons type="location" size="16" color="#8C8C8C" />
          <text>会议地点：{{ meetingInfo.meetingLocation }}</text>
        </view>
        <view class="detail-item">
          <uni-icons type="clock" size="16" color="#8C8C8C" />
          <text>截止时间：{{ meetingInfo.voteEndTime }}</text>
        </view>
      </view>
    </view>

    <!-- 投票须知 -->
    <view class="vote-notice">
      <view class="notice-header">
        <uni-icons type="info" size="18" color="#1890FF" />
        <text class="notice-title">投票须知</text>
      </view>
      <view class="notice-content">
        <text>1. 每个议题必须选择一个选项（同意、反对、弃权）</text>
        <text>2. 投票一经提交不可修改，请慎重选择</text>
        <text>3. 投票截止时间前可随时提交</text>
        <text>4. 投票结果将在会议结束后公布</text>
      </view>
    </view>

    <!-- 投票议题 -->
    <view class="vote-topics">
      <view 
        class="topic-item" 
        v-for="(topic, index) in topicList" 
        :key="topic.topicId"
      >
        <view class="topic-header">
          <text class="topic-number">议题{{ index + 1 }}</text>
          <text class="topic-title">{{ topic.topicTitle }}</text>
        </view>
        <view class="topic-content">
          <text>{{ topic.topicContent }}</text>
        </view>
        
        <!-- 投票统计 -->
        <view class="vote-stats">
          <view class="stats-item">
            <text class="stats-label">同意</text>
            <text class="stats-count">{{ topic.agreeCount }}票</text>
          </view>
          <view class="stats-item">
            <text class="stats-label">反对</text>
            <text class="stats-count">{{ topic.opposeCount }}票</text>
          </view>
          <view class="stats-item">
            <text class="stats-label">弃权</text>
            <text class="stats-count">{{ topic.abstainCount }}票</text>
          </view>
        </view>
        
        <!-- 投票选项 -->
        <view class="vote-options">
          <radio-group @change="onVoteChange(topic.topicId, $event)">
            <label class="vote-option agree" :class="{ selected: voteData[topic.topicId] === '1' }">
              <radio value="1" :checked="voteData[topic.topicId] === '1'" />
              <view class="option-content">
                <uni-icons type="checkmarkempty" size="20" color="#52C41A" />
                <text>同意</text>
              </view>
            </label>
            <label class="vote-option oppose" :class="{ selected: voteData[topic.topicId] === '2' }">
              <radio value="2" :checked="voteData[topic.topicId] === '2'" />
              <view class="option-content">
                <uni-icons type="clear" size="20" color="#FF4D4F" />
                <text>反对</text>
              </view>
            </label>
            <label class="vote-option abstain" :class="{ selected: voteData[topic.topicId] === '3' }">
              <radio value="3" :checked="voteData[topic.topicId] === '3'" />
              <view class="option-content">
                <uni-icons type="minus" size="20" color="#8C8C8C" />
                <text>弃权</text>
              </view>
            </label>
          </radio-group>
        </view>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="vote-submit">
      <view class="submit-info">
        <text>已完成 {{ Object.keys(voteData).length }}/{{ topicList.length }} 个议题</text>
      </view>
      <button 
        class="submit-btn" 
        :class="{ disabled: !canSubmit }"
        :disabled="!canSubmit"
        @click="submitVote"
      >
        {{ canSubmit ? '提交投票' : `还需完成 ${topicList.length - Object.keys(voteData).length} 个议题` }}
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      meetingId: null,
      meetingInfo: {},
      topicList: [],
      voteData: {},
      countdown: 0,
      timer: null
    }
  },
  computed: {
    canSubmit() {
      return Object.keys(this.voteData).length === this.topicList.length
    }
  },
  onLoad(options) {
    this.meetingId = options.id
    this.loadMeetingInfo()
    this.loadTopics()
  },
  onUnload() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    loadMeetingInfo() {
      // 模拟数据
      this.meetingInfo = {
        meetingId: 1,
        meetingTitle: '2024年度业主大会第一次会议',
        meetingTime: '2024-01-15 14:00',
        meetingLocation: '小区会议室',
        voteEndTime: '2024-01-20 18:00',
        totalVoters: 856,
        actualVoters: 324
      }
      
      // 计算倒计时
      this.startCountdown()
    },
    
    loadTopics() {
      // 模拟议题数据
      this.topicList = [
        {
          topicId: 1,
          topicTitle: '关于调整物业管理费的议案',
          topicContent: '鉴于近年来人工成本、材料成本持续上涨，现有物业费标准已不能满足小区日常管理和维护需要，建议将物业管理费从每平方米2.5元调整至3.0元。',
          agreeCount: 156,
          opposeCount: 89,
          abstainCount: 79
        },
        {
          topicId: 2,
          topicTitle: '关于更换小区监控设备的议案',
          topicContent: '小区现有监控设备使用年限较长，部分设备老化严重，影响安全监控效果。建议投资80万元更换高清数字监控设备，提升小区安全防范水平。',
          agreeCount: 201,
          opposeCount: 67,
          abstainCount: 56
        },
        {
          topicId: 3,
          topicTitle: '关于小区绿化改造的议案',
          topicContent: '为改善小区居住环境，建议对小区绿化进行升级改造，增加休闲座椅、健身器材等设施，预算费用50万元，从公共维修基金中支出。',
          agreeCount: 178,
          opposeCount: 98,
          abstainCount: 48
        }
      ]
    },
    
    startCountdown() {
      const endTime = new Date(this.meetingInfo.voteEndTime).getTime()
      const now = new Date().getTime()
      this.countdown = Math.max(0, endTime - now)
      
      if (this.countdown > 0) {
        this.timer = setInterval(() => {
          this.countdown -= 1000
          if (this.countdown <= 0) {
            clearInterval(this.timer)
            uni.showToast({
              title: '投票已截止',
              icon: 'none'
            })
          }
        }, 1000)
      }
    },
    
    formatCountdown(time) {
      const days = Math.floor(time / (1000 * 60 * 60 * 24))
      const hours = Math.floor((time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
      const minutes = Math.floor((time % (1000 * 60 * 60)) / (1000 * 60))
      
      if (days > 0) {
        return `${days}天${hours}小时`
      } else if (hours > 0) {
        return `${hours}小时${minutes}分钟`
      } else {
        return `${minutes}分钟`
      }
    },
    
    onVoteChange(topicId, event) {
      this.voteData[topicId] = event.detail.value
      this.$forceUpdate()
    },
    
    async submitVote() {
      if (!this.canSubmit) return
      
      // 确认提交
      const confirm = await uni.showModal({
        title: '确认提交',
        content: '投票一经提交不可修改，确定要提交吗？',
        confirmText: '确认提交',
        cancelText: '再想想'
      })
      
      if (!confirm.confirm) return
      
      try {
        uni.showLoading({
          title: '提交中...'
        })
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1500))
        
        uni.hideLoading()
        
        uni.showToast({
          title: '投票提交成功',
          icon: 'success',
          duration: 2000
        })
        
        setTimeout(() => {
          uni.navigateBack()
        }, 2000)
        
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: error.message || '投票提交失败',
          icon: 'none'
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.vote-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 200rpx;
}

.meeting-info {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .meeting-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 30rpx;
    
    .meeting-title {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
    }
    
    .countdown {
      display: flex;
      align-items: center;
      background: #FFF2E8;
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      
      .countdown-text {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #FA8C16;
        font-weight: 500;
      }
    }
  }
  
  .meeting-detail {
    .detail-item {
      display: flex;
      align-items: center;
      margin-bottom: 16rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      text {
        margin-left: 12rpx;
        font-size: 26rpx;
        color: #595959;
      }
    }
  }
}

.vote-notice {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 30rpx;
  border: 1rpx solid #F0F0F0;
  
  .notice-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .notice-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .notice-content {
    text {
      display: block;
      font-size: 24rpx;
      color: #595959;
      line-height: 1.6;
      margin-bottom: 12rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

.vote-topics {
  padding: 0 20rpx;
}

.topic-item {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 20rpx;
  border: 1rpx solid #F0F0F0;
}

.topic-header {
  margin-bottom: 20rpx;
  
  .topic-number {
    display: inline-block;
    background: #E6F7FF;
    color: #1890FF;
    font-size: 22rpx;
    font-weight: 500;
    padding: 6rpx 12rpx;
    border-radius: 12rpx;
    margin-right: 16rpx;
  }
  
  .topic-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #262626;
    line-height: 1.4;
  }
}

.topic-content {
  margin-bottom: 30rpx;
  
  text {
    font-size: 26rpx;
    color: #595959;
    line-height: 1.6;
  }
}

.vote-stats {
  display: flex;
  justify-content: space-around;
  background: #F8F9FA;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 30rpx;
  
  .stats-item {
    text-align: center;
    
    .stats-label {
      display: block;
      font-size: 22rpx;
      color: #8C8C8C;
      margin-bottom: 8rpx;
    }
    
    .stats-count {
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
}

.vote-options {
  radio-group {
    display: flex;
    flex-direction: column;
  }
  
  .vote-option {
    display: flex;
    align-items: center;
    padding: 24rpx;
    border: 2rpx solid #F0F0F0;
    border-radius: 16rpx;
    margin-bottom: 16rpx;
    transition: all 0.3s ease;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    &.agree.selected {
      border-color: #52C41A;
      background: #F6FFED;
    }
    
    &.oppose.selected {
      border-color: #FF4D4F;
      background: #FFF2F0;
    }
    
    &.abstain.selected {
      border-color: #8C8C8C;
      background: #F5F5F5;
    }
    
    radio {
      margin-right: 16rpx;
    }
    
    .option-content {
      display: flex;
      align-items: center;
      flex: 1;
      
      text {
        margin-left: 12rpx;
        font-size: 28rpx;
        color: #262626;
        font-weight: 500;
      }
    }
  }
}

.vote-submit {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #FFFFFF;
  padding: 30rpx 40rpx;
  border-top: 1rpx solid #F0F0F0;
  
  .submit-info {
    text-align: center;
    margin-bottom: 20rpx;
    
    text {
      font-size: 24rpx;
      color: #8C8C8C;
    }
  }
  
  .submit-btn {
    width: 100%;
    height: 88rpx;
    background: #1890FF;
    color: #FFFFFF;
    font-size: 32rpx;
    font-weight: 600;
    border-radius: 24rpx;
    border: none;
    
    &.disabled {
      background: #F0F0F0;
      color: #8C8C8C;
    }
    
    &:active:not(.disabled) {
      background: #096DD9;
    }
  }
}
</style>
 
 