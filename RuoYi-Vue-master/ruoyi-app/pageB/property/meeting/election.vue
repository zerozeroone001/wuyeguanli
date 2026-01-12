<template>
  <view class="vote-container">
    <!-- 顶部背景装饰 -->
    <view class="header-bg"></view>

    <!-- 会议信息卡片 -->
    <view class="meeting-card">
      <view class="meeting-header">
        <view class="title-wrapper">
          <text class="meeting-title">{{ meetingInfo.meetingTitle }}</text>
          <view class="status-badge">选举会议</view>
        </view>
        <view class="countdown-tag" v-if="countdown > 0">
          <uni-icons type="clock-filled" size="14" color="#FF8800" />
          <text class="time">{{ formatCountdown(countdown) }}</text>
        </view>
      </view>
      
      <view class="info-grid">
        <view class="info-item">
          <view class="icon-box"><uni-icons type="calendar-filled" size="18" color="#1890FF" /></view>
          <view class="info-text">
            <text class="label">会议时间</text>
            <text class="value">{{ meetingInfo.meetingTime }}</text>
          </view>
        </view>
        <view class="info-item">
          <view class="icon-box"><uni-icons type="location-filled" size="18" color="#1890FF" /></view>
          <view class="info-text">
            <text class="label">会议地点</text>
            <text class="value">{{ meetingInfo.meetingLocation }}</text>
          </view>
        </view>
      </view>

      <view class="notice-box">
        <view class="notice-title">
          <uni-icons type="info-filled" size="16" color="#FAAD14" />
          <text>投票须知</text>
        </view>
        <text class="notice-text">请仔细阅读候选人资料，对每位候选人投出神圣的一票。截止时间前可随时修改。</text>
      </view>
    </view>

    <!-- 候选人列表 -->
    <view class="candidates-list">
      <view class="section-title">
        <text class="text">候选人名单</text>
        <text class="count">共 {{ topicList.length }} 人</text>
      </view>

      <view 
        class="candidate-card" 
        v-for="(topic, index) in topicList" 
        :key="topic.topicId"
      >
        <!-- 候选人头部 -->
        <view class="candidate-header">
          <view class="avatar-box">
            <image 
              v-if="topic.avatar" 
              :src="topic.avatar" 
              class="avatar-img" 
              mode="aspectFill"
            ></image>
            <view v-else class="avatar-placeholder">
              <uni-icons type="person-filled" size="32" color="#BDC3C7"></uni-icons>
            </view>
            <view class="number-badge">{{ index + 1 }}号</view>
          </view>
          <view class="candidate-info">
            <text class="name">{{ topic.topicTitle }}</text>
            <text class="desc-preview">请查看下方详细介绍</text>
          </view>
          <!-- 投票状态标记 (已选显示) -->
          <view class="voted-mark" v-if="voteData[topic.topicId] !== undefined">
             <text v-if="voteData[topic.topicId] === 0" class="mark-agree">已同意</text>
             <text v-if="voteData[topic.topicId] === 1" class="mark-oppose">已反对</text>
             <text v-if="voteData[topic.topicId] === 2" class="mark-abstain">已弃权</text>
          </view>
        </view>
        
        <!-- 详细介绍 -->
        <view class="candidate-content">
          <rich-text :nodes="topic.topicContent"></rich-text>
        </view>
        
        <!-- 附件列表 -->
        <view class="attachment-area" v-if="topic.files && topic.files.length > 0">
          <scroll-view class="attachment-scroll" scroll-x="true">
            <view class="file-list">
              <view 
                class="file-chip" 
                v-for="(file, fileIndex) in topic.files" 
                :key="fileIndex"
                @click="previewFile(file)"
              >
                <uni-icons :type="getIconByFileName(file.fileName)" size="16" color="#666"></uni-icons>
                <text class="filename">{{ file.fileName }}</text>
              </view>
            </view>
          </scroll-view>
        </view>

        <!-- 投票操作区 -->
        <view class="vote-actions-area" v-if="meetingInfo.meetingStatus == '1'">
          <view class="divider"></view>
          <view class="actions-grid">
            <button 
              class="action-btn agree"
              :class="{ active: voteData[topic.topicId] === 0 }"
              @click="handleTopicVote(topic, 0)"
            >
              <view class="icon-circle">
                <uni-icons type="checkmarkempty" size="20" :color="voteData[topic.topicId] === 0 ? '#fff' : '#52C41A'" />
              </view>
              <text>同意</text>
            </button>
            
            <button 
              class="action-btn oppose"
              :class="{ active: voteData[topic.topicId] === 1 }"
              @click="handleTopicVote(topic, 1)"
            >
              <view class="icon-circle">
                <uni-icons type="closeempty" size="20" :color="voteData[topic.topicId] === 1 ? '#fff' : '#FF4D4F'" />
              </view>
              <text>反对</text>
            </button>
            
            <button 
              class="action-btn abstain"
              :class="{ active: voteData[topic.topicId] === 2 }"
              @click="handleTopicVote(topic, 2)"
            >
              <view class="icon-circle">
                <uni-icons type="minus" size="20" :color="voteData[topic.topicId] === 2 ? '#fff' : '#8C8C8C'" />
              </view>
              <text>弃权</text>
            </button>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部悬浮提交栏 -->
    <view class="footer-placeholder"></view>
    <view class="footer-bar" v-if="meetingInfo.meetingStatus == '1'">
      <view class="progress-info">
        <text class="label">已选进度</text>
        <view class="progress-track">
          <view class="progress-fill" :style="{ width: (Object.keys(voteData).length / topicList.length * 100) + '%' }"></view>
        </view>
        <text class="value">{{ Object.keys(voteData).length }}/{{ topicList.length }}</text>
      </view>
      <button 
        class="submit-btn" 
        :class="{ disabled: isSubmitDisabled && !isVoteExpired }"
        @click="handleSubmitAll" 
        :disabled="isSubmitDisabled && !isVoteExpired"
      >
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
      initialVoteData: {}, // 记录初始投票状态，用于比对变更
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
      if (Object.keys(this.voteData).length === 0) return '请选择';
      return '提交投票';
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

        const newVoteData = {};
        if (myVotesRes.data) {
           const votes = myVotesRes.data;
           Object.keys(votes).forEach(key => {
             // 确保 value 是数字类型
             newVoteData[Number(key)] = Number(votes[key]);
           });
        }
        this.voteData = newVoteData;
        this.initialVoteData = JSON.parse(JSON.stringify(newVoteData));

      } catch (error) {
        console.error('Failed to load data:', error);
        uni.showToast({ title: '数据加载失败', icon: 'none' });
      } finally {
        uni.hideLoading();
      }
    },
    
    handleTopicVote(topic, choice) {
      console.log('User clicked vote:', topic.topicId, choice);
      if (this.voteData[topic.topicId] === choice) {
        this.$delete(this.voteData, topic.topicId);
      } else {
        this.$set(this.voteData, topic.topicId, choice);
      }
      console.log('Updated voteData:', JSON.parse(JSON.stringify(this.voteData)));
    },

    async handleSubmitAll() {
      console.log('handleSubmitAll triggered');
      console.log('Current voteData:', this.voteData);
      console.log('Initial initialVoteData:', this.initialVoteData);

      // 1. 检查是否有未选择的项 (基本的非空检查)
      if (Object.keys(this.voteData).length === 0) {
        console.log('Validation failed: No selection');
        uni.showToast({ title: '请先选择投票项', icon: 'none' });
        return;
      }

      // 2. 检查是否过期
      if (this.isVoteExpired) {
        console.log('Validation failed: Expired');
        uni.showToast({ title: '投票已截止', icon: 'none' });
        return;
      }

      // 3. 检查是否有被移除的投票 (在初始中有，但在当前中没有)
      // 用户不能通过"反选"来撤销投票，必须改选其他项(如弃权)
      const removedTopicIds = Object.keys(this.initialVoteData).filter(topicId => {
         return this.voteData[topicId] === undefined;
      });

      console.log('Removed topics:', removedTopicIds);

      if (removedTopicIds.length > 0) {
        console.log('Validation failed: Removed topics detected');
        uni.showModal({
          title: '操作提示',
          content: '您取消了部分已投票项。系统不支持直接取消，请选择"弃权"或"反对"代替。',
          showCancel: false,
          confirmText: '我知道了'
        });
        return;
      }

      // 4. 进度提示 (如果未全部投完)
      if (Object.keys(this.voteData).length < this.topicList.length) {
        console.log('Showing progress confirmation modal');
        const confirm = await uni.showModal({
          title: '温馨提示',
          content: `还有 ${this.topicList.length - Object.keys(this.voteData).length} 位候选人未投票，是否仅提交已选部分？`,
          confirmText: '确认提交',
          cancelText: '我再看看'
        });
        if (!confirm.confirm) {
            console.log('User cancelled at progress modal');
            return;
        }
      }

      // 5. 最终确认
      console.log('Showing final confirmation modal');
      // 这里的数量显示为本次提交的总数
      const confirmResult = await new Promise((resolve) => {
        uni.showModal({
            title: '确认提交',
            content: `您将提交 ${Object.keys(this.voteData).length} 张选票，提交后不可修改。`,
            confirmText: '确认提交',
            cancelText: '取消',
            success: (res) => {
                console.log('Modal success res:', res);
                resolve(res);
            },
            fail: (err) => {
                console.error('Modal fail err:', err);
                resolve({ confirm: false });
            }
        });
      });

      console.log('Modal confirmed result:', confirmResult);

      if (!confirmResult.confirm) {
          console.log('User cancelled at final modal');
          return;
      }

      uni.showLoading({ title: '提交中...' });

      try {
        console.log('Preparing to submit votes...');
        // 6. 提交所有当前选中的投票
        // 后端已做防重处理，重复提交相同选项不会计入统计
        const submitPromises = Object.keys(this.voteData).map(topicId => {
          const payload = {
            meetingId: this.meetingId,
            topicId: parseInt(topicId),
            voteOption: this.voteData[topicId],
            voteType: 0
          };
          console.log('Submitting payload:', payload);
          return submitVote(payload);
        });

        await Promise.all(submitPromises);
        console.log('All votes submitted successfully');

        // 更新初始状态
        this.initialVoteData = JSON.parse(JSON.stringify(this.voteData));

        uni.hideLoading();
        uni.showToast({ title: '投票成功', icon: 'success' });
        setTimeout(() => uni.navigateBack(), 2000);

      } catch (error) {
        console.error('Submission error:', error);
        uni.hideLoading();
        uni.showToast({ title: error.msg || '提交失败', icon: 'none' });
      }
    },

    startCountdown() {
      if(!this.meetingInfo.voteEndTime) return;
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
      return days > 0 ? `${days}天${hours}时` : `${hours}小时`;
    },

    getIconByFileName(fileName) { return getFileIcon(fileName); },
    previewFile(file) { previewFile(file, config.baseUrl); }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

// 变量定义
$primary-color: #1890FF;
$success-color: #52C41A;
$error-color: #FF4D4F;
$warning-color: #FAAD14;
$text-main: #333333;
$text-sub: #666666;
$text-light: #999999;
$bg-color: #F5F7FA;
$card-radius: 20rpx;

page { background-color: $bg-color; }

.vote-container {
  min-height: 100vh;
  position: relative;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}

// 顶部蓝色背景区
.header-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 380rpx;
  background: linear-gradient(180deg, $primary-color 0%, rgba(24, 144, 255, 0) 100%);
  z-index: 0;
}

// 会议信息卡片
.meeting-card {
  position: relative;
  z-index: 1;
  margin: 30rpx 30rpx 0;
  padding: 40rpx;
  background: #FFFFFF;
  border-radius: $card-radius;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.08);

  .meeting-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 30rpx;

    .title-wrapper {
      flex: 1;
      margin-right: 20rpx;
      .meeting-title {
        font-size: 34rpx;
        font-weight: bold;
        color: $text-main;
        line-height: 1.4;
        display: block;
        margin-bottom: 12rpx;
      }
      .status-badge {
        display: inline-block;
        font-size: 20rpx;
        color: $primary-color;
        background: rgba(24, 144, 255, 0.1);
        padding: 4rpx 12rpx;
        border-radius: 8rpx;
      }
    }

    .countdown-tag {
      display: flex;
      align-items: center;
      background: #FFF7E6;
      padding: 8rpx 16rpx;
      border-radius: 30rpx;
      flex-shrink: 0;
      .time {
        font-size: 24rpx;
        color: #FF8800;
        margin-left: 8rpx;
        font-weight: 500;
      }
    }
  }

  .info-grid {
    display: flex;
    margin-bottom: 30rpx;
    background: #F9FAFB;
    padding: 20rpx;
    border-radius: 12rpx;
    
    .info-item {
      flex: 1;
      display: flex;
      align-items: center;
      
      .icon-box {
        width: 60rpx;
        height: 60rpx;
        background: #E6F7FF;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16rpx;
      }
      
      .info-text {
        display: flex;
        flex-direction: column;
        .label { font-size: 22rpx; color: $text-light; margin-bottom: 4rpx; }
        .value { font-size: 26rpx; color: $text-main; font-weight: 500; }
      }
    }
  }

  .notice-box {
    border-top: 1rpx dashed #EEEEEE;
    padding-top: 24rpx;
    .notice-title {
      display: flex;
      align-items: center;
      margin-bottom: 8rpx;
      text { font-size: 26rpx; font-weight: 600; margin-left: 8rpx; color: $text-main; }
    }
    .notice-text {
      font-size: 24rpx;
      color: $text-sub;
      line-height: 1.5;
    }
  }
}

// 候选人列表区域
.candidates-list {
  position: relative;
  z-index: 1;
  padding: 40rpx 30rpx;

  .section-title {
    display: flex;
    align-items: baseline;
    margin-bottom: 24rpx;
    .text { font-size: 32rpx; font-weight: bold; color: $text-main; margin-right: 12rpx; }
    .count { font-size: 24rpx; color: $text-light; }
  }
}

// 候选人卡片
.candidate-card {
  background: #FFFFFF;
  border-radius: $card-radius;
  padding: 0;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
  overflow: hidden;

  .candidate-header {
    padding: 30rpx;
    display: flex;
    align-items: center;
    border-bottom: 1rpx solid #F5F5F5;
    position: relative;

    .avatar-box {
      position: relative;
      margin-right: 24rpx;
      .avatar-img, .avatar-placeholder {
        width: 100rpx;
        height: 100rpx;
        border-radius: 50%;
        border: 4rpx solid #F0F0F0;
      }
      .avatar-placeholder {
        background: #F5F5F5;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .number-badge {
        position: absolute;
        bottom: -6rpx;
        left: 50%;
        transform: translateX(-50%);
        background: $primary-color;
        color: #fff;
        font-size: 18rpx;
        padding: 2rpx 10rpx;
        border-radius: 10rpx;
        white-space: nowrap;
      }
    }

    .candidate-info {
      flex: 1;
      .name { font-size: 32rpx; font-weight: bold; color: $text-main; margin-bottom: 8rpx; display: block; }
      .desc-preview { font-size: 24rpx; color: $text-light; }
    }

    .voted-mark {
      text {
        font-size: 24rpx;
        padding: 4rpx 12rpx;
        border-radius: 8rpx;
        font-weight: 500;
        &.mark-agree { color: $success-color; background: rgba(82, 196, 26, 0.1); }
        &.mark-oppose { color: $error-color; background: rgba(255, 77, 79, 0.1); }
        &.mark-abstain { color: $text-sub; background: rgba(0, 0, 0, 0.05); }
      }
    }
  }

  .candidate-content {
    padding: 30rpx;
    font-size: 28rpx;
    color: $text-sub;
    line-height: 1.6;
  }

  .attachment-area {
    padding: 0 30rpx 30rpx;
    .attachment-scroll {
      width: 100%;
      white-space: nowrap;
    }
    .file-list {
      display: flex;
    }
    .file-chip {
      display: flex;
      align-items: center;
      background: #F5F7FA;
      padding: 12rpx 20rpx;
      border-radius: 30rpx;
      margin-right: 16rpx;
      border: 1rpx solid #E8E8E8;
      .filename { font-size: 24rpx; color: $text-main; margin-left: 8rpx; }
      &:active { background: #E6F7FF; border-color: $primary-color; }
    }
  }

  .vote-actions-area {
    padding: 0 30rpx 30rpx;
    .divider { height: 1rpx; background: #F5F5F5; margin-bottom: 24rpx; }
    
    .actions-grid {
      display: flex;
      justify-content: space-between;
      gap: 20rpx;

      .action-btn {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 80rpx;
        background: #FFFFFF;
        border: 2rpx solid #E8E8E8;
        border-radius: 12rpx;
        padding: 0;
        margin: 0;
        transition: all 0.2s;
        
        .icon-circle {
          width: 36rpx;
          height: 36rpx;
          border-radius: 50%;
          border: 2rpx solid #D9D9D9;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12rpx;
          transition: all 0.2s;
        }

        text { font-size: 28rpx; color: $text-sub; font-weight: 500; }

        &.agree.active {
          background: rgba(82, 196, 26, 0.05);
          border-color: $success-color;
          .icon-circle { background: $success-color; border-color: $success-color; }
          text { color: $success-color; }
        }

        &.oppose.active {
          background: rgba(255, 77, 79, 0.05);
          border-color: $error-color;
          .icon-circle { background: $error-color; border-color: $error-color; }
          text { color: $error-color; }
        }

        &.abstain.active {
          background: rgba(0, 0, 0, 0.02);
          border-color: #8C8C8C;
          .icon-circle { background: #8C8C8C; border-color: #8C8C8C; }
          text { color: #8C8C8C; }
        }
        
        &::after { border: none; }
      }
    }
  }
}

// 底部悬浮栏
.footer-placeholder { height: 160rpx; }
.footer-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background: #FFFFFF;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.08);
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  z-index: 99;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .progress-info {
    flex: 1;
    margin-right: 30rpx;
    .label { font-size: 22rpx; color: $text-light; margin-bottom: 8rpx; display: block; }
    .progress-track {
      height: 8rpx;
      background: #F0F0F0;
      border-radius: 4rpx;
      margin-bottom: 8rpx;
      overflow: hidden;
      .progress-fill { height: 100%; background: $primary-color; transition: width 0.3s; }
    }
    .value { font-size: 20rpx; color: $text-sub; }
  }

  .submit-btn {
    width: 240rpx;
    height: 80rpx;
    line-height: 80rpx;
    background: linear-gradient(135deg, $primary-color 0%, #36CFC9 100%);
    color: #FFFFFF;
    font-size: 30rpx;
    font-weight: bold;
    border-radius: 40rpx;
    margin: 0;
    &.disabled {
      background: #E8E8E8;
      color: #999999;
    }
    &:active { opacity: 0.9; }
  }
}
</style>