<template>
  <view class="result-container">
    <!-- 会议基本信息 -->
    <view class="meeting-info">
      <text class="meeting-title">{{ meetingInfo.meetingTitle }}</text>
      <view class="meeting-meta">
        <view class="meta-item">
          <uni-icons type="calendar" size="16" color="#8C8C8C" />
          <text>{{ meetingInfo.meetingTime }}</text>
        </view>
        <view class="meta-item">
          <uni-icons type="location" size="16" color="#8C8C8C" />
          <text>{{ meetingInfo.meetingLocation }}</text>
        </view>
      </view>
    </view>

    <!-- 投票概况 -->
    <view class="vote-overview">
      <view class="overview-header">
        <uni-icons type="bar-chart" size="20" color="#262626" />
        <text class="overview-title">投票概况</text>
      </view>
      
      <view class="overview-stats">
        <view class="stat-card">
          <text class="stat-number">{{ meetingInfo.totalVoters }}</text>
          <text class="stat-label">应参与人数</text>
        </view>
        <view class="stat-card">
          <text class="stat-number">{{ meetingInfo.actualVoters }}</text>
          <text class="stat-label">实际参与人数</text>
        </view>
        <view class="stat-card">
          <text class="stat-number">{{ participationRate }}%</text>
          <text class="stat-label">参与率</text>
        </view>
      </view>
      
      <!-- 参与率进度条 -->
      <view class="participation-progress">
        <view class="progress-header">
          <text>参与率</text>
          <text class="progress-percent">{{ participationRate }}%</text>
        </view>
        <view class="progress-bar">
          <view class="progress-fill" :style="{ width: participationRate + '%' }"></view>
        </view>
      </view>
    </view>

    <!-- 议题结果 -->
    <view class="topics-result">
      <view class="result-header">
        <uni-icons type="list" size="20" color="#262626" />
        <text class="result-title">投票结果</text>
      </view>
      
      <view class="topic-list">
        <view class="topic-item" v-for="(topic, index) in topicList" :key="topic.topicId">
          <view class="topic-header">
            <view class="topic-number">{{ index + 1 }}</view>
            <view class="topic-info">
              <text class="topic-title">{{ topic.topicTitle }}</text>
              <view class="topic-result">
                <text class="result-status" :class="[topic.supportCount > topic.opposeCount ? 'result-pass' : 'result-reject']">
                  {{ getResultText(topic) }}
                </text>
              </view>
            </view>
          </view>
          
          <!-- 投票统计 -->
          <view class="vote-stats">
            <view class="stats-row">
              <view class="stat-item agree">
                <view class="stat-icon">
                  <uni-icons type="checkmarkempty" size="16" color="#52C41A" />
                </view>
                <text class="stat-label">同意</text>
                <text class="stat-count">{{ topic.agreeCount }}票</text>
                <text class="stat-percent">({{ getPercent(topic.agreeCount, getTotalVotes(topic)) }}%)</text>
              </view>
              <view class="stat-item oppose">
                <view class="stat-icon">
                  <uni-icons type="clear" size="16" color="#FF4D4F" />
                </view>
                <text class="stat-label">反对</text>
                <text class="stat-count">{{ topic.opposeCount }}票</text>
                <text class="stat-percent">({{ getPercent(topic.opposeCount, getTotalVotes(topic)) }}%)</text>
              </view>
              <view class="stat-item abstain">
                <view class="stat-icon">
                  <uni-icons type="minus" size="16" color="#8C8C8C" />
                </view>
                <text class="stat-label">弃权</text>
                <text class="stat-count">{{ topic.abstainCount }}票</text>
                <text class="stat-percent">({{ getPercent(topic.abstainCount, getTotalVotes(topic)) }}%)</text>
              </view>
            </view>
            
            <!-- 可视化图表 -->
            <view class="chart-container">
              <view class="pie-chart">
                <view class="chart-legend">
                  <view class="legend-item">
                    <view class="legend-color agree"></view>
                    <text>同意 {{ getPercent(topic.agreeCount, getTotalVotes(topic)) }}%</text>
                  </view>
                  <view class="legend-item">
                    <view class="legend-color oppose"></view>
                    <text>反对 {{ getPercent(topic.opposeCount, getTotalVotes(topic)) }}%</text>
                  </view>
                  <view class="legend-item">
                    <view class="legend-color abstain"></view>
                    <text>弃权 {{ getPercent(topic.abstainCount, getTotalVotes(topic)) }}%</text>
                  </view>
                </view>
              </view>
              
              <!-- 进度条图表 -->
              <view class="progress-chart">
                <view class="progress-bar-chart">
                  <view class="progress-item agree" :style="{ width: getPercent(topic.agreeCount, getTotalVotes(topic)) + '%' }"></view>
                  <view class="progress-item oppose" :style="{ width: getPercent(topic.opposeCount, getTotalVotes(topic)) + '%' }"></view>
                  <view class="progress-item abstain" :style="{ width: getPercent(topic.abstainCount, getTotalVotes(topic)) + '%' }"></view>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 结果总结 -->
    <view class="result-summary">
      <view class="summary-header">
        <uni-icons type="flag" size="20" color="#262626" />
        <text class="summary-title">会议总结</text>
      </view>
      <view class="summary-content">
        <view class="summary-stats">
          <text class="summary-text">本次会议共{{ topicList.length }}个议题，其中：</text>
          <text class="summary-text">• 通过议题：{{ passedTopics }}个</text>
          <text class="summary-text">• 未通过议题：{{ rejectedTopics }}个</text>
          <text class="summary-text">• 整体参与率：{{ participationRate }}%</text>
        </view>
        
        <view class="summary-note">
          <text>注：议题通过标准为同意票数超过参与投票总数的50%</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button class="share-btn" @click="shareResult">
        <uni-icons type="redo" size="18" color="#1890FF" />
        <text>分享结果</text>
      </button>
      <button class="download-btn" @click="downloadResult">
        <uni-icons type="download" size="18" color="#52C41A" />
        <text>下载报告</text>
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
      topicList: []
    }
  },
  computed: {
    participationRate() {
      if (this.meetingInfo.totalVoters === 0) return 0
      return Math.round((this.meetingInfo.actualVoters / this.meetingInfo.totalVoters) * 100)
    },
    
    passedTopics() {
      return this.topicList.filter(topic => {
        const total = this.getTotalVotes(topic)
        const agreePercent = this.getPercent(topic.agreeCount, total)
        return agreePercent > 50
      }).length
    },
    
    rejectedTopics() {
      return this.topicList.length - this.passedTopics
    }
  },
  onLoad(options) {
    this.meetingId = options.id
    this.loadMeetingInfo()
    this.loadTopicResults()
  },
  methods: {
    loadMeetingInfo() {
      // 模拟数据
      this.meetingInfo = {
        meetingId: 1,
        meetingTitle: '2024年度业主大会第一次会议',
        meetingTime: '2024-01-15 14:00',
        meetingLocation: '小区会议室',
        totalVoters: 856,
        actualVoters: 567
      }
    },
    
    loadTopicResults() {
      // 模拟议题结果数据
      this.topicList = [
        {
          topicId: 1,
          topicTitle: '关于调整物业管理费的议案',
          agreeCount: 312,
          opposeCount: 156,
          abstainCount: 99
        },
        {
          topicId: 2,
          topicTitle: '关于更换小区监控设备的议案',
          agreeCount: 398,
          opposeCount: 98,
          abstainCount: 71
        },
        {
          topicId: 3,
          topicTitle: '关于小区绿化改造的议案',
          agreeCount: 423,
          opposeCount: 89,
          abstainCount: 55
        }
      ]
    },
    
    getTotalVotes(topic) {
      return topic.agreeCount + topic.opposeCount + topic.abstainCount
    },
    
    getPercent(count, total) {
      return total > 0 ? Math.round((count / total) * 100) : 0
    },
    
    getResultClass(topic) {
      const total = this.getTotalVotes(topic)
      const agreePercent = this.getPercent(topic.agreeCount, total)
      
      if (agreePercent > 50) {
        return 'result-pass'
      } else {
        return 'result-reject'
      }
    },
    
    getResultText(topic) {
      const total = this.getTotalVotes(topic)
      const agreePercent = this.getPercent(topic.agreeCount, total)
      
      if (agreePercent > 50) {
        return '通过'
      } else {
        return '未通过'
      }
    },
    
    shareResult() {
      uni.showToast({
        title: '分享功能开发中',
        icon: 'none'
      })
    },
    
    downloadResult() {
      uni.showToast({
        title: '下载功能开发中',
        icon: 'none'
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

.result-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 40rpx;
}

.meeting-info {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  text-align: center;
  
  .meeting-title {
    display: block;
    font-size: 32rpx;
    font-weight: 600;
    color: #262626;
    margin-bottom: 20rpx;
    line-height: 1.4;
  }
  
  .meeting-meta {
    display: flex;
    justify-content: center;
    gap: 40rpx;
    
    .meta-item {
      display: flex;
      align-items: center;
      
      text {
        margin-left: 8rpx;
        font-size: 24rpx;
        color: #595959;
      }
    }
  }
}

.vote-overview {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .overview-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .overview-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .overview-stats {
    display: flex;
    justify-content: space-between;
    margin-bottom: 30rpx;
    
    .stat-card {
      flex: 1;
      text-align: center;
      
      .stat-number {
        display: block;
        font-size: 36rpx;
        font-weight: 600;
        color: #1890FF;
        margin-bottom: 8rpx;
      }
      
      .stat-label {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .participation-progress {
    .progress-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16rpx;
      
      text {
        font-size: 24rpx;
        color: #262626;
        font-weight: 500;
      }
      
      .progress-percent {
        color: #1890FF;
        font-weight: 600;
      }
    }
    
    .progress-bar {
      height: 16rpx;
      background: #F0F0F0;
      border-radius: 8rpx;
      overflow: hidden;
      
      .progress-fill {
        height: 100%;
        background: linear-gradient(90deg, #1890FF, #40A9FF);
        border-radius: 8rpx;
        transition: width 0.5s ease;
      }
    }
  }
}

.topics-result {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .result-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .result-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
}

.topic-item {
  margin-bottom: 50rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .topic-header {
    display: flex;
    align-items: flex-start;
    margin-bottom: 24rpx;
    
    .topic-number {
      width: 48rpx;
      height: 48rpx;
      line-height: 48rpx;
      text-align: center;
      background: #1890FF;
      color: #FFFFFF;
      border-radius: 50%;
      font-size: 22rpx;
      font-weight: 600;
      margin-right: 16rpx;
      flex-shrink: 0;
    }
    
    .topic-info {
      flex: 1;
      
      .topic-title {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #262626;
        line-height: 1.4;
        margin-bottom: 12rpx;
      }
      
      .topic-result {
        .result-status {
          display: inline-block;
          padding: 6rpx 16rpx;
          border-radius: 12rpx;
          font-size: 22rpx;
          font-weight: 500;
          
          &.result-pass {
            background: #F6FFED;
            color: #52C41A;
          }
          
          &.result-reject {
            background: #FFF2F0;
            color: #FF4D4F;
          }
        }
      }
    }
  }
}

.vote-stats {
  .stats-row {
    .stat-item {
      display: flex;
      align-items: center;
      padding: 16rpx 0;
      border-bottom: 1rpx solid #F0F0F0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .stat-icon {
        width: 32rpx;
        margin-right: 16rpx;
      }
      
      .stat-label {
        margin-right: 20rpx;
        font-size: 26rpx;
        color: #262626;
        font-weight: 500;
        min-width: 60rpx;
      }
      
      .stat-count {
        margin-right: 16rpx;
        font-size: 26rpx;
        color: #262626;
        font-weight: 600;
      }
      
      .stat-percent {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .chart-container {
    margin-top: 30rpx;
    
    .chart-legend {
      display: flex;
      justify-content: space-around;
      margin-bottom: 20rpx;
      
      .legend-item {
        display: flex;
        align-items: center;
        
        .legend-color {
          width: 16rpx;
          height: 16rpx;
          border-radius: 50%;
          margin-right: 8rpx;
          
          &.agree {
            background: #52C41A;
          }
          
          &.oppose {
            background: #FF4D4F;
          }
          
          &.abstain {
            background: #8C8C8C;
          }
        }
        
        text {
          font-size: 20rpx;
          color: #595959;
        }
      }
    }
    
    .progress-chart {
      .progress-bar-chart {
        display: flex;
        height: 20rpx;
        background: #F0F0F0;
        border-radius: 10rpx;
        overflow: hidden;
        
        .progress-item {
          height: 100%;
          
          &.agree {
            background: #52C41A;
          }
          
          &.oppose {
            background: #FF4D4F;
          }
          
          &.abstain {
            background: #8C8C8C;
          }
        }
      }
    }
  }
}

.result-summary {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .summary-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .summary-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .summary-content {
    .summary-stats {
      margin-bottom: 20rpx;
      
      .summary-text {
        display: block;
        font-size: 26rpx;
        color: #595959;
        line-height: 1.6;
        margin-bottom: 8rpx;
        
        &:last-child {
          margin-bottom: 0;
        }
      }
    }
    
    .summary-note {
      padding: 16rpx;
      background: #F8F9FA;
      border-radius: 12rpx;
      border-left: 4rpx solid #1890FF;
      
      text {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
}

.action-buttons {
  display: flex;
  gap: 20rpx;
  padding: 0 20rpx;
  
  button {
    flex: 1;
    height: 80rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    
    text {
      margin-left: 8rpx;
    }
    
    &.share-btn {
      background: #E6F7FF;
      color: #1890FF;
    }
    
    &.download-btn {
      background: #F6FFED;
      color: #52C41A;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}
</style>
 
 