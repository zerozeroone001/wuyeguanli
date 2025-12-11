<template>
  <view class="vote-container">
    <!-- 会议信息 -->
    <view class="meeting-info">
      <view class="meeting-header">
        <text class="meeting-title">{{ meetingInfo.meetingTitle }}</text>
        <view class="meeting-tag">选举会议</view>
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
        <text>1. 请对每位候选人进行投票（同意、反对、弃权）。</text>
        <text>2. 选择完所有候选人后，点击底部"统一提交投票"按钮。</text>
        <text>3. 在截止时间前，您可以随时修改并重新提交。</text>
      </view>
    </view>

    <!-- 候选人列表 -->
    <view class="vote-topics">
      <view 
        class="topic-item" 
        v-for="(topic, index) in topicList" 
        :key="topic.topicId"
      >
        <view class="topic-header candidate-header">
          <view class="candidate-avatar-wrapper" v-if="topic.avatar">
            <image :src="topic.avatar" class="candidate-avatar" mode="aspectFill"></image>
          </view>
          <!-- 默认头像 -->
          <view class="candidate-avatar-wrapper default" v-else>
             <uni-icons type="person-filled" size="40" color="#BDC3C7"></uni-icons>
          </view>
          
          <view class="header-info">
             <view class="title-row">
               <text class="topic-number">候选人{{ index + 1 }}</text>
               <text class="topic-title">{{ topic.topicTitle }}</text>
             </view>
          </view>
        </view>
        
        <view class="topic-content">
          <rich-text :nodes="topic.topicContent"></rich-text>
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
      </view>
    </view>

    <!-- 底部统一提交按钮 -->
    <view class="submit-container" v-if="meetingInfo.meetingStatus == '1'">
      <view class="submit-info">
        <text class="voted-count">已选择 {{ Object.keys(voteData).length }}/{{ topicList.length }} 项</text>
      </view>      
    
      <button class="submit-btn" @click="handleSubmitAll" :disabled="isSubmitDisabled && !isVoteExpired">
        {{ submitButtonText }}
      </button>
    </view>

  </view>
</template>

<script>
import { getMeeting, getMeetingTopics, getMyVotes, submitVote } from '@/api/meeting.js';
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
      timer: null
    }
  },
  computed: {
    isVoteExpired() {
      if (!this.meetingInfo.voteEndTime) return false;
      const currentTime = new Date().getTime();
      const endTime = new Date(this.meetingInfo.voteEndTime).getTime();
      return currentTime > endTime;
    },
    isSubmitDisabled() {
      if (Object.keys(this.voteData).length === 0) return true;
      if (this.isVoteExpired) return true;
      return false;
    },
    submitButtonText() {
      if (this.isVoteExpired) return '投票已截止';
      if (Object.keys(this.voteData).length === 0) return '请先选择投票项';
      return '统一提交投票';
    }
  },
  onLoad(options) {
    this.meetingId = options.id
    if (this.meetingId) {
      this.loadPageData();
    } else {
      uni.showToast({ title: '无效的会议ID', icon: 'none' });
      setTimeout(() => uni.navigateBack(), 1500);
    }
  },
  onUnload() {
    if (this.timer) clearInterval(this.timer);
  },
  methods: {
    async loadPageData() {
      uni.showLoading({ title: '加载中...' });
      try {
        const [meetingRes, topicsRes, myVotesRes] = await Promise.all([
          getMeeting(this.meetingId),
          getMeetingTopics(this.meetingId),
          getMyVotes(this.meetingId)
        ]);

        this.meetingInfo = {
          ...meetingRes.data,
          meetingTime: formatDate(meetingRes.data.meetingTime, 'yyyy-MM-dd hh:mm'),
          voteEndTime: formatDate(meetingRes.data.voteEndTime, 'yyyy-MM-dd hh:mm')
        };
        this.startCountdown();

        this.topicList = topicsRes.data.map(topic => ({
          ...topic,
          files: topic.files || [] 
        }));

        this.voteData = myVotesRes.data || {};

      } catch (error) {
        console.error('Failed to load data:', error);
        uni.showToast({ title: '数据加载失败', icon: 'none' });
      } finally {
        uni.hideLoading();
      }
    },
    
    handleTopicVote(topic, choice) {
      if (this.voteData[topic.topicId] === choice) {
        this.$delete(this.voteData, topic.topicId);
      } else {
        this.$set(this.voteData, topic.topicId, choice);
      }
    },

    async handleSubmitAll() {
      if (Object.keys(this.voteData).length === 0) {
        uni.showToast({ title: '请先选择投票项', icon: 'none' });
        return;
      }

      if (this.isVoteExpired) {
        uni.showToast({ title: '投票已截止', icon: 'none' });
        return;
      }

      if (Object.keys(this.voteData).length < this.topicList.length) {
        const confirm = await uni.showModal({
          title: '提示',
          content: `还有 ${this.topicList.length - Object.keys(this.voteData).length} 位候选人未投票，确认提交？`,
          confirmText: '继续提交',
          cancelText: '返回'
        });
        if (!confirm[1].confirm) return;
      }

      const confirmSubmit = await uni.showModal({
        title: '确认提交',
        content: `确定要提交 ${Object.keys(this.voteData).length} 项投票吗？`,
        confirmText: '确认',
        cancelText: '取消'
      });
      if (!confirmSubmit[1].confirm) return;

      uni.showLoading({ title: '提交中...' });

      try {
        const submitPromises = Object.keys(this.voteData).map(topicId => {
          return submitVote({
            meetingId: this.meetingId,
            topicId: parseInt(topicId),
            voteOption: this.voteData[topicId],
            voteType: 0
          });
        });

        await Promise.all(submitPromises);

        uni.hideLoading();
        uni.showToast({ title: '投票提交成功', icon: 'success' });
        setTimeout(() => uni.navigateBack(), 2000);

      } catch (error) {
        uni.hideLoading();
        uni.showToast({ title: error.msg || '提交失败', icon: 'none' });
      }
    },

    startCountdown() {
      const endTime = new Date(this.meetingInfo.voteEndTime).getTime();
      const now = new Date().getTime();
      this.countdown = Math.max(0, endTime - now);
      if (this.countdown > 0) {
        this.timer = setInterval(() => {
          this.countdown -= 1000;
          if (this.countdown <= 0) clearInterval(this.timer);
        }, 1000);
      }
    },
    
    formatCountdown(time) {
      if (time <= 0) return '已截止';
      const days = Math.floor(time / (1000 * 60 * 60 * 24));
      const hours = Math.floor((time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      return days > 0 ? `${days}天${hours}小时` : `${hours}小时`;
    },

    getIconByFileName(fileName) { return getFileIcon(fileName); },
    previewFile(file) { previewFile(file, config.baseUrl); }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';
page { background-color: #FAFBFC; }
.vote-container { min-height: 100vh; background-color: #FAFBFC; padding-bottom: 220rpx; }

.meeting-info {
  background: #FFFFFF; margin: 20rpx; border-radius: 24rpx; padding: 40rpx; border: 1rpx solid #F0F0F0;
  .meeting-header {
    display: flex; align-items: flex-start; margin-bottom: 30rpx; flex-wrap: wrap;
    .meeting-title { flex: 1; font-size: 32rpx; font-weight: 600; color: #262626; line-height: 1.4; margin-right: 20rpx; }
    .meeting-tag { background: #FFF1F0; color: #FF4D4F; font-size: 22rpx; padding: 4rpx 12rpx; border-radius: 8rpx; margin-right: 10rpx; }
    .countdown { display: flex; align-items: center; background: #FFF2E8; padding: 8rpx 16rpx; border-radius: 16rpx; .countdown-text { margin-left: 8rpx; font-size: 22rpx; color: #FA8C16; } }
  }
  .meeting-detail .detail-item { display: flex; align-items: center; margin-bottom: 16rpx; text { margin-left: 12rpx; font-size: 26rpx; color: #595959; } }
}

.vote-notice {
  background: #FFFFFF; margin: 0 20rpx 20rpx; border-radius: 24rpx; padding: 30rpx; border: 1rpx solid #F0F0F0;
  .notice-header { display: flex; align-items: center; margin-bottom: 20rpx; .notice-title { margin-left: 12rpx; font-size: 28rpx; font-weight: 600; } }
  .notice-content text { display: block; font-size: 24rpx; color: #595959; line-height: 1.6; margin-bottom: 12rpx; }
}

.topic-item {
  background: #FFFFFF; border-radius: 24rpx; padding: 40rpx; margin-bottom: 20rpx; border: 1rpx solid #F0F0F0;
  .candidate-header {
    display: flex; align-items: center; margin-bottom: 20rpx;
    .candidate-avatar-wrapper { width: 100rpx; height: 100rpx; border-radius: 50%; overflow: hidden; margin-right: 20rpx; background: #f5f5f5; border: 2rpx solid #fff; box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1); 
       &.default { display: flex; align-items: center; justify-content: center; }
       .candidate-avatar { width: 100%; height: 100%; }
    }
    .header-info { flex: 1; }
    .title-row { display: flex; align-items: center; }
    .topic-number { background: #FFF1F0; color: #FF4D4F; font-size: 22rpx; padding: 6rpx 12rpx; border-radius: 12rpx; margin-right: 16rpx; }
    .topic-title { font-size: 32rpx; font-weight: 600; color: #262626; }
  }
  .topic-content { margin-bottom: 30rpx; font-size: 26rpx; color: #595959; line-height: 1.6; }
}

.attachment-section {
  margin-top: 24rpx;
  .attachment-scroll-view { width: 100%; height: 200rpx; .attachment-list { display: flex; } }
}
.attachment-item {
  flex-shrink: 0; display: flex; flex-direction: column; align-items: center; justify-content: center;
  width: 220rpx; height: 200rpx; background: #F8F9FA; border: 1rpx solid #F0F0F0; border-radius: 24rpx; margin-right: 20rpx; padding: 20rpx;
  .file-icon-wrapper { width: 90rpx; height: 90rpx; background: #E6F7FF; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-bottom: 16rpx; }
  .attachment-name { font-size: 22rpx; color: #595959; text-align: center; width: 100%; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
}

.vote-options-btn {
  display: flex; justify-content: space-between; gap: 20rpx;
  .vote-btn {
    flex: 1; display: flex; align-items: center; justify-content: center; padding: 16rpx; border-radius: 16rpx; font-size: 28rpx; font-weight: 500; border: 2rpx solid #F0F0F0; background: #FFFFFF;
    text { margin-left: 10rpx; }
    &.agree { color: #52C41A; &.selected { background: #52C41A; color: #FFFFFF; border-color: #52C41A; } }
    &.oppose { color: #FF4D4F; &.selected { background: #FF4D4F; color: #FFFFFF; border-color: #FF4D4F; } }
    &.abstain { color: #8C8C8C; &.selected { background: #8C8C8C; color: #FFFFFF; border-color: #8C8C8C; } }
  }
}

.submit-container {
  position: fixed; bottom: 0; left: 0; right: 0; background: #FFFFFF; padding: 20rpx 30rpx calc(20rpx + env(safe-area-inset-bottom)); box-shadow: 0 -4rpx 16rpx rgba(0,0,0,0.08); z-index: 100;
  .submit-info { text-align: center; margin-bottom: 16rpx; .voted-count { font-size: 26rpx; color: #8C8C8C; } }
  .submit-btn { width: 100%; background: linear-gradient(135deg, #1890FF, #40A9FF); color: #FFFFFF; font-size: 32rpx; font-weight: 600; border-radius: 16rpx; padding: 24rpx; border: none; &:disabled { background: #F0F0F0; color: #BFBFBF; } }
}
</style>