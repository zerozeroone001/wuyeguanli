<template>
  <view class="meeting-container">
    <!-- 会议分类 -->
    <view class="meeting-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'ongoing' }"
        @click="switchTab('ongoing')"
      >
        进行中
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'upcoming' }"
        @click="switchTab('upcoming')"
      >
        即将开始
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'finished' }"
        @click="switchTab('finished')"
      >
        已结束
      </view>
    </view>

    <!-- 会议列表 -->
    <view class="meeting-list">
      <view 
        class="meeting-item" 
        v-for="meeting in meetingList" 
        :key="meeting.meetingId"
        @click="viewMeeting(meeting)"
      >
        <view class="meeting-header">
          <text class="meeting-title">{{ meeting.meetingTitle }}</text>
          <view class="meeting-status" :class="{ 'status-upcoming': meeting.meetingStatus == '0', 'status-ongoing': meeting.meetingStatus == '1', 'status-finished': meeting.meetingStatus == '2' }">
            {{ getStatusText(meeting.meetingStatus) }}
          </view>
        </view>
        <view class="meeting-info">
          <view class="info-row">
            <uni-icons type="calendar" size="16" color="#8C8C8C" />
            <text class="meeting-time">{{ meeting.meetingTime }}</text>
          </view>
          <view class="info-row">
            <uni-icons type="location" size="16" color="#8C8C8C" />
            <text class="meeting-location">{{ meeting.meetingLocation }}</text>
          </view>
          <view class="info-row">
            <uni-icons type="person" size="16" color="#8C8C8C" />
            <text class="meeting-participants">{{ meeting.actualVoters || 0 }}/{{ meeting.totalVoters || 0 }}人参与</text>
          </view>
        </view>
        <view class="meeting-action">
          <button 
            v-if="meeting.meetingStatus === '1'" 
            class="vote-btn"
            @click.stop="goVote(meeting)"
          >
            参与投票
          </button>
          <button 
            v-else 
            class="detail-btn"
            @click.stop="viewDetail(meeting)"
          >
            查看详情
          </button>
        </view>
      </view>
    </view>

    <!-- 列表为空或加载状态 -->
    <view class="status-view">
      <view class="empty-state" v-if="!loading && meetingList.length === 0">
        <uni-icons type="calendar" size="80" color="#D9D9D9" />
        <text class="empty-text">暂无业委会会议信息</text>
      </view>
      <uni-load-more v-if="loading || meetingList.length > 0" :status="loading ? 'loading' : (finished ? 'noMore' : 'more')"></uni-load-more>
    </view>

  </view>
</template>

<script>
import { listMeeting } from "@/api/property/meeting";

export default {
  data() {
    return {
      activeTab: 'ongoing',
      meetingList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        meetingStatus: '1', // 默认加载进行中
        meetingType: '2' // 业委会会议类型
      },
      // 加载状态
      loading: false,
      // 是否已加载全部数据
      finished: false,
      total: 0
    }
  },
  onLoad() {
    this.loadMeetings();
  },
  // 下拉刷新
  onPullDownRefresh() {
    this.resetAndLoad();
    setTimeout(() => {
      uni.stopPullDownRefresh();
    }, 500);
  },
  // 触底加载更多
  onReachBottom() {
    if (!this.finished && !this.loading) {
      this.queryParams.pageNum++;
      this.loadMeetings();
    }
  },
  methods: {
    // 重置并加载
    resetAndLoad() {
      this.queryParams.pageNum = 1;
      this.meetingList = [];
      this.finished = false;
      this.total = 0;
      this.loadMeetings();
    },
    // 加载会议列表
    loadMeetings() {
      if (this.loading || (this.queryParams.pageNum > 1 && this.finished)) {
        return;
      }
      this.loading = true;
      this.queryParams.meetingStatus = this.getStatusByTab(this.activeTab);

      listMeeting(this.queryParams).then(response => {
        const { rows, total } = response;
        if (rows && rows.length > 0) {
          this.meetingList = this.queryParams.pageNum === 1 ? rows : this.meetingList.concat(rows);
        }
        this.total = total;
        this.finished = this.meetingList.length >= this.total;
        this.loading = false;
      }).catch(err => {
        this.loading = false;
      });
    },
    // 切换Tab
    switchTab(tab) {
      if (this.activeTab === tab) {
        return;
      }
      this.activeTab = tab;
      this.resetAndLoad();
    },
    
    getStatusByTab(tab) {
      const statusMap = {
        ongoing: '1',
        upcoming: '0', 
        finished: '2'
      }
      return statusMap[tab]
    },
    
    
    
    getStatusText(status) {
      const textMap = {
        '0': '筹备中',
        '1': '进行中',
        '2': '已结束'
      }
      return textMap[status] || '未知'
    },
    
    viewMeeting(meeting) {
      // 统一跳转到投票页，因为那里信息最全
      this.goVote(meeting);
    },
    
    goVote(meeting) {
      uni.navigateTo({
        url: `/pageB/property/committee-meeting/vote?id=${meeting.meetingId}`
      })
    },
    
    viewDetail(meeting) {
      // 详情页也可以与投票页合并
      uni.navigateTo({
        url: `/pageB/property/committee-meeting/vote?id=${meeting.meetingId}`
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

.meeting-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.meeting-tabs {
  display: flex;
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 10rpx;
  border: 1rpx solid #F0F0F0;
  position: sticky;
  top: 0;
  z-index: 99;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx 0;
  font-size: 28rpx;
  color: #8C8C8C;
  border-radius: 20rpx;
  transition: all 0.3s ease;
  
  &.active {
    background: #1890FF;
    color: #FFFFFF;
    font-weight: 600;
  }
}

.meeting-list {
  padding: 0 20rpx;
}

.meeting-item {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 20rpx;
  border: 1rpx solid #F0F0F0;
  
  &:active {
    background: #F8F9FA;
  }
}

.meeting-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24rpx;
  
  .meeting-title {
    flex: 1;
    font-size: 32rpx;
    font-weight: 600;
    color: #262626;
    line-height: 1.4;
    margin-right: 20rpx;
  }
  
  .meeting-status {
    padding: 8rpx 16rpx;
    border-radius: 16rpx;
    font-size: 22rpx;
    font-weight: 500;
    white-space: nowrap;
    
    &.status-upcoming {
      background: #E6F7FF;
      color: #1890FF;
    }
    
    &.status-ongoing {
      background: #FFF2E8;
      color: #FA8C16;
    }
    
    &.status-finished {
      background: #F6FFED;
      color: #52C41A;
    }
  }
}

.meeting-info {
  margin-bottom: 30rpx;
  
  .info-row {
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

.meeting-action {
  display: flex;
  justify-content: flex-end;
  
  button {
    margin: 0;
    padding: 16rpx 32rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    line-height: 1.5;
    
    &::after {
      border: none;
    }

    &.vote-btn {
      background: #1890FF;
      color: #FFFFFF;
    }
    
    &.detail-btn {
      background: #F0F0F0;
      color: #595959;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}

.status-view {
  padding-bottom: 40rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;
  
  .empty-text {
    margin-top: 30rpx;
    font-size: 28rpx;
    color: #8C8C8C;
  }
}
</style>