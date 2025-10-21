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
        <text>1. 对每个议题进行投票（同意、反对、弃权）</text>
        <text>2. 点击选项后将直接提交您的投票</text>
        <text>3. 在截止时间前，您可以随时修改您的投票</text>
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
        
        <!-- 附件列表 -->
        <view class="attachment-section" v-if="topic.files && topic.files.length > 0">
          <scroll-view class="attachment-scroll-view" scroll-x="true">
            <view class="attachment-list">
              <view 
                class="attachment-item" 
                v-for="(file, fileIndex) in topic.files" 
                :key="fileIndex"
                @click="previewFile(file)"
              >
                <view class="file-icon-wrapper">
                  <uni-icons :type="getIconByFileName(file.fileName)" size="32" color="#1890FF"></uni-icons>
                </view>
                <text class="attachment-name">{{ file.fileName }}</text>
              </view>
            </view>
          </scroll-view>
        </view>

        <!-- 投票统计 -->
        <view class="vote-stats" v-if="meetingInfo.meetingStatus === '2'">
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
        <view class="vote-options-btn" v-if="meetingInfo.meetingStatus == '1'">
          <button 
            size="mini"
            class="vote-btn agree"
            :class="{ selected: voteData[topic.topicId] === 0 }"
            @click="handleTopicVote(topic, 0)">
            <uni-icons type="checkmarkempty" size="16" :color="voteData[topic.topicId] === 0 ? '#FFFFFF' : '#52C41A'" />
            <text>同意</text>
          </button>
          <button 
            size="mini"
            class="vote-btn oppose"
            :class="{ selected: voteData[topic.topicId] === 1 }"
            @click="handleTopicVote(topic, 1)">
            <uni-icons type="clear" size="16" :color="voteData[topic.topicId] === 1 ? '#FFFFFF' : '#FF4D4F'" />
            <text>反对</text>
          </button>
          <button 
            size="mini"
            class="vote-btn abstain"
            :class="{ selected: voteData[topic.topicId] === 2 }"
            @click="handleTopicVote(topic, 2)">
            <uni-icons type="minus" size="16" :color="voteData[topic.topicId] === 2 ? '#FFFFFF' : '#8C8C8C'" />
            <text>弃权</text>
          </button>
        </view>
        <view class="consultation-btn-container" style="margin-top: 20rpx; text-align: right;">
          <button size="mini" type="primary" plain @click="handleConsultation(topic)">意见征询</button>
        </view>
      </view>
    </view>

    <!-- 意见征询弹窗 -->
    <uni-popup ref="opinionPopup" type="center" v-if="meetingInfo.meetingStatus == '1'">
      <view class="opinion-popup-content">
        <text class="popup-title">填写意见</text>
        <textarea class="opinion-textarea" v-model="opinionText" placeholder="请输入您的意见" />
        <view class="popup-actions">
          <button class="action-btn cancel" @click="closeOpinionPopup">取消</button>
          <button class="action-btn confirm" @click="submitOpinionForm">确定</button>
        </view>
      </view>
    </uni-popup>

  </view>
</template>

<script>
import { getMeeting, getMeetingTopics, getMyVotes, submitVote, submitOpinion } from '@/api/meeting.js';
import { formatDate } from '@/utils/common.js';
import { previewFile, getFileIcon } from '@/utils/filePreview.js';
import config from '@/config.js';

export default {
  data() {
    return {
      meetingId: null,
      meetingInfo: {},
      topicList: [],
      voteData: {}, // topicId -> choice
      countdown: 0,
      timer: null,
      currentTopicForOpinion: null,
      opinionText: ''
    }
  },
  onLoad(options) {
    this.meetingId = options.id
    if (this.meetingId) {
      this.loadPageData();
    } else {
      uni.showToast({
        title: '无效的会议ID',
        icon: 'none',
        complete: () => {
          setTimeout(() => uni.navigateBack(), 1500);
        }
      });
    }
  },
  onReady() {
    // 组件准备就绪
  },
  onUnload() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    async loadPageData() {
      uni.showLoading({
        title: '加载中...'
      });
      try {
        const [meetingRes, topicsRes, myVotesRes] = await Promise.all([
          getMeeting(this.meetingId),
          getMeetingTopics(this.meetingId),
          getMyVotes(this.meetingId) // 获取用户已有的投票记录
        ]);

        // 处理会议信息
        this.meetingInfo = {
          ...meetingRes.data,
          meetingTime: formatDate(meetingRes.data.meetingTime, 'yyyy-MM-dd hh:mm'),
          voteEndTime: formatDate(meetingRes.data.voteEndTime, 'yyyy-MM-dd hh:mm')
        };
        this.startCountdown();

        // 处理议题信息
        this.topicList = topicsRes.data.map(topic => ({
          ...topic,
          files: topic.files || [] 
        }));

        // 处理我的投票记录
        this.voteData = myVotesRes.data || {};

      } catch (error) {
        console.error('Failed to load meeting data:', error);
        uni.showToast({
          title: '数据加载失败',
          icon: 'none'
        });
      } finally {
        uni.hideLoading();
      }
    },
    
    
    async handleTopicVote(topic, choice) {
      
      const choiceText = {0: '同意', 1: '反对', 2: '弃权'};

      // 检查投票是否已截止
      const currentTime = new Date().getTime();
      const endTime = new Date(this.meetingInfo.voteEndTime).getTime();
      if (currentTime > endTime) {
        uni.showToast({
          title: '投票已截止，无法进行投票',
          icon: 'none',
          duration: 2000
        });
        return;
      }

      // 如果重复点击同一个选项，则不处理
      if (this.voteData[topic.topicId] === choice) {
        return;
      }

      const confirm = await uni.showModal({
        title: '确认投票',
        content: `您确定要为"${topic.topicTitle}"投【${choiceText[choice]}】票吗？`,
        confirmText: '确认',
        cancelText: '取消'
      });
      if (!confirm[1].confirm) return;

      uni.showLoading({ title: '提交中...' });

      try {
        const submissionData = {
          meetingId: this.meetingId,
          topicId: topic.topicId,
          voteOption: choice,
          voteType: 0 // 0小程序投票
        };

        await submitVote(submissionData);

        // 更新我的投票状态，立即反馈到UI
        this.$set(this.voteData, topic.topicId, choice);

        uni.hideLoading();
        uni.showToast({
          title: '投票成功',
          icon: 'success',
          duration: 1500
        });

      } catch (error) {
		  let { msg } = error
		  console.log(error)
        uni.hideLoading();
        uni.showToast({
          title: error.msg || '投票失败',
          icon: 'none'
        });
      }
    },

    handleConsultation(topic) {
      this.currentTopicForOpinion = topic;
      this.opinionText = ''; // Clear previous text
      // 确保ref存在后再打开弹窗
      this.$nextTick(() => {
        if (this.$refs.opinionPopup) {
          this.$refs.opinionPopup.open();
        } else {
          uni.showToast({
            title: '弹窗组件未准备好',
            icon: 'none'
          });
        }
      });
    },

    closeOpinionPopup() {
      if (this.$refs.opinionPopup) {
        this.$refs.opinionPopup.close();
      }
    },

    async submitOpinionForm() {

      

      if (!this.opinionText.trim()) {
        uni.showToast({ title: '意见不能为空', icon: 'none' });
        return;
      }

      uni.showLoading({ title: '提交中...' });

      try {
        const data = {
          topicId: this.currentTopicForOpinion.topicId,
          opinion: this.opinionText
        };
        await submitOpinion(data);
        uni.hideLoading();
        uni.showToast({ title: '意见提交成功', icon: 'success' });
        this.closeOpinionPopup();
      } catch (error) {
        uni.hideLoading();
        uni.showToast({ title: error.message || '提交失败', icon: 'none' });
      }
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
      if (time <= 0) return '已截止';
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

    getIconByFileName(fileName) {
      return getFileIcon(fileName);
    },
    
      previewFile(file) {
        previewFile(file, config.baseUrl);
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
  padding-bottom: 40rpx; // 移除底部按钮后减少padding
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

.attachment-section {
  margin-top: 24rpx;

  .attachment-scroll-view {
    width: 100%;
    height: 200rpx;

    .attachment-list {
      display: flex;
      align-items: center;
    }
  }
}

.attachment-item {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 220rpx;
  height: 200rpx;
  background-color: #F8F9FA;
  border: 1rpx solid #F0F0F0;
  border-radius: 24rpx;
  margin-right: 20rpx;
  padding: 20rpx;

  .file-icon-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 90rpx;
    height: 90rpx;
    background-color: #E6F7FF;
    border-radius: 50%;
    margin-bottom: 16rpx;
  }

  .attachment-name {
    font-size: 22rpx;
    color: #595959;
    text-align: center;
    width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }

  &:active {
    background-color: #F0F2F5;
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

.vote-options-btn {
  display: flex;
  justify-content: space-between;
  gap: 20rpx;

  .vote-btn {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 16rpx;
    border-radius: 16rpx;
    font-size: 28rpx;
    font-weight: 500;
    transition: all 0.3s ease;
    border: 2rpx solid #F0F0F0;
    background-color: #FFFFFF;

    text {
      margin-left: 10rpx;
    }

    &.agree {
      color: #52C41A;
      &.selected {
        background-color: #52C41A;
        color: #FFFFFF;
        border-color: #52C41A;
      }
    }
    &.oppose {
      color: #FF4D4F;
      &.selected {
        background-color: #FF4D4F;
        color: #FFFFFF;
        border-color: #FF4D4F;
      }
    }
    &.abstain {
      color: #8C8C8C;
      &.selected {
        background-color: #8C8C8C;
        color: #FFFFFF;
        border-color: #8C8C8C;
      }
    }
  }
}

.opinion-popup-content {
  width: 600rpx;
  background-color: #fff;
  border-radius: 24rpx;
  padding: 40rpx;

  .popup-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #262626;
    text-align: center;
    display: block;
    margin-bottom: 30rpx;
  }

  .opinion-textarea {
    width: 100%;
    height: 200rpx;
    background-color: #F8F9FA;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    padding: 20rpx;
    font-size: 28rpx;
    box-sizing: border-box;
    margin-bottom: 30rpx;
  }

  .popup-actions {
    display: flex;
    justify-content: space-between;
    gap: 20rpx;

    .action-btn {
      flex: 1;
      font-size: 28rpx;
      font-weight: 500;
      border-radius: 16rpx;
      padding: 16rpx;
      &.cancel {
        background-color: #F0F2F5;
        color: #595959;
      }
      &.confirm {
        background-color: $uni-color-primary;
        color: #fff;
      }
    }
  }
}

</style>