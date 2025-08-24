<template>
  <view class="meeting-container">
    <!-- 会议日历 -->
  <!--  <view class="calendar-section">
      <uni-calendar 
        :selected="selectedDate" 
        @change="onCalendarChange"
        :marks="meetingMarks"
      />
    </view> -->

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
          <view class="meeting-status" :class="[meeting.meetingStatus === 'preparing' ? 'status-preparing' : meeting.meetingStatus === 'voting' ? 'status-voting' : meeting.meetingStatus === 'completed' ? 'status-completed' : 'status-default']">
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
            <text class="meeting-participants">{{ meeting.actualVoters }}/{{ meeting.totalVoters }}人参与</text>
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
            v-else-if="meeting.meetingStatus === '2'" 
            class="result-btn"
            @click.stop="viewResult(meeting)"
          >
            查看结果
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

    <!-- 空状态 -->
    <view class="empty-state" v-if="meetingList.length === 0">
      <uni-icons type="calendar" size="80" color="#D9D9D9" />
      <text class="empty-text">暂无会议信息</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'ongoing',
      selectedDate: [],
      meetingList: [],
      meetingMarks: []
    }
  },
  onLoad() {
    this.loadMeetings()
  },
  onPullDownRefresh() {
    this.loadMeetings()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    loadMeetings() {
      // 模拟数据
      const mockData = {
        ongoing: [
          {
            meetingId: 1,
            meetingTitle: '2024年度业主大会第一次会议',
            meetingType: '1',
            meetingStatus: '1',
            meetingTime: '2024-01-15 14:00',
            meetingLocation: '小区会议室',
            totalVoters: 856,
            actualVoters: 324,
            voteEndTime: '2024-01-20 18:00',
            description: '讨论小区物业管理费调整方案'
          }
        ],
        upcoming: [
          {
            meetingId: 2,
            meetingTitle: '业委会换届选举大会',
            meetingType: '1',
            meetingStatus: '0',
            meetingTime: '2024-02-01 09:00',
            meetingLocation: '小区广场',
            totalVoters: 856,
            actualVoters: 0,
            description: '进行新一届业委会成员选举'
          },
          {
            meetingId: 3,
            meetingTitle: '小区绿化改造方案讨论',
            meetingType: '2',
            meetingStatus: '0',
            meetingTime: '2024-02-10 15:00',
            meetingLocation: '小区会议室',
            totalVoters: 856,
            actualVoters: 0,
            description: '讨论小区绿化改造具体方案'
          }
        ],
        finished: [
          {
            meetingId: 4,
            meetingTitle: '2023年度财务报告审议',
            meetingType: '1',
            meetingStatus: '2',
            meetingTime: '2023-12-25 14:00',
            meetingLocation: '小区会议室',
            totalVoters: 856,
            actualVoters: 567,
            description: '审议2023年度小区财务收支情况'
          },
          {
            meetingId: 5,
            meetingTitle: '停车位管理规定修订',
            meetingType: '2',
            meetingStatus: '2',
            meetingTime: '2023-12-10 16:00',
            meetingLocation: '小区会议室',
            totalVoters: 856,
            actualVoters: 423,
            description: '修订小区停车位使用管理规定'
          }
        ]
      }
      
      this.meetingList = mockData[this.activeTab] || []
      this.generateCalendarMarks()
    },
    
    generateCalendarMarks() {
      this.meetingMarks = this.meetingList.map(meeting => ({
        date: meeting.meetingTime.split(' ')[0],
        info: meeting.meetingTitle.substring(0, 6) + '...',
        data: meeting
      }))
    },
    
    switchTab(tab) {
      this.activeTab = tab
      this.loadMeetings()
    },
    
    getStatusByTab(tab) {
      const statusMap = {
        ongoing: '1',
        upcoming: '0', 
        finished: '2'
      }
      return statusMap[tab]
    },
    
    getStatusClass(status) {
      const classMap = {
        '0': 'status-upcoming',
        '1': 'status-ongoing',
        '2': 'status-finished'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        '0': '筹备中',
        '1': '投票中',
        '2': '已结束'
      }
      return textMap[status] || '未知'
    },
    
    onCalendarChange(event) {
      this.selectedDate = event.fulldate
      // 可以根据选中日期过滤会议
    },
    
    viewMeeting(meeting) {
      uni.navigateTo({
        url: `/pages/property/meeting/detail?id=${meeting.meetingId}`
      })
    },
    
    goVote(meeting) {
      uni.navigateTo({
        url: `/pages/property/meeting/vote?id=${meeting.meetingId}`
      })
    },
    
    viewResult(meeting) {
      uni.navigateTo({
        url: `/pages/property/meeting/result?id=${meeting.meetingId}`
      })
    },
    
    viewDetail(meeting) {
      uni.navigateTo({
        url: `/pages/property/meeting/detail?id=${meeting.meetingId}`
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

.calendar-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 30rpx;
  border: 1rpx solid #F0F0F0;
}

.meeting-tabs {
  display: flex;
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 10rpx;
  border: 1rpx solid #F0F0F0;
  
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
    padding: 16rpx 32rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    
    &.vote-btn {
      background: #1890FF;
      color: #FFFFFF;
    }
    
    &.result-btn {
      background: #52C41A;
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
 
 