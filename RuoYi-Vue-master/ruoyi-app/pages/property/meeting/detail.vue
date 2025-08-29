<template>
  <view class="detail-container">
    <!-- 会议基本信息 -->
    <view class="meeting-info">
      <view class="info-header">
        <text class="meeting-title">{{ meetingInfo.meetingTitle }}</text>
        <view class="meeting-status" :class="[meetingInfo.meetingStatus === 'preparing' ? 'status-preparing' : meetingInfo.meetingStatus === 'voting' ? 'status-voting' : meetingInfo.meetingStatus === 'completed' ? 'status-completed' : 'status-default']">
          {{ getStatusText(meetingInfo.meetingStatus) }}
        </view>
      </view>
      
      <view class="info-content">
        <view class="info-item">
          <uni-icons type="calendar" size="18" color="#1890FF" />
          <text class="info-label">会议时间</text>
          <text class="info-value">{{ meetingInfo.meetingTime }}</text>
        </view>
        <view class="info-item">
          <uni-icons type="location" size="18" color="#1890FF" />
          <text class="info-label">会议地点</text>
          <text class="info-value">{{ meetingInfo.meetingLocation }}</text>
        </view>
        <view class="info-item" v-if="meetingInfo.voteEndTime">
          <uni-icons type="clock" size="18" color="#1890FF" />
          <text class="info-label">截止时间</text>
          <text class="info-value">{{ meetingInfo.voteEndTime }}</text>
        </view>
        <view class="info-item">
          <uni-icons type="person" size="18" color="#1890FF" />
          <text class="info-label">参与情况</text>
          <text class="info-value">{{ meetingInfo.actualVoters }}/{{ meetingInfo.totalVoters }}人</text>
        </view>
      </view>
    </view>

    <!-- 会议内容 -->
    <view class="meeting-content">
      <view class="content-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="content-title">会议内容</text>
      </view>
      <view class="content-text">
        <text>{{ meetingInfo.meetingContent || '暂无详细内容' }}</text>
      </view>
    </view>

    <!-- 议题列表 -->
    <view class="topic-section" v-if="topicList.length > 0">
      <view class="section-header">
        <uni-icons type="list" size="20" color="#262626" />
        <text class="section-title">会议议题</text>
      </view>
      
      <view class="topic-list">
        <view class="topic-item" v-for="(topic, index) in topicList" :key="topic.topicId">
          <view class="topic-header">
            <text class="topic-number">{{ index + 1 }}</text>
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
          
          <!-- 投票结果 -->
          <view class="vote-result" v-if="meetingInfo.meetingStatus === '2'">
            <view class="result-header">
              <text>投票结果</text>
            </view>
            <view class="result-stats">
              <view class="stat-item agree">
                <text class="stat-label">同意</text>
                <text class="stat-count">{{ topic.agreeCount }}票</text>
                <text class="stat-percent">({{ getPercent(topic.agreeCount, getTotalVotes(topic)) }}%)</text>
              </view>
              <view class="stat-item oppose">
                <text class="stat-label">反对</text>
                <text class="stat-count">{{ topic.opposeCount }}票</text>
                <text class="stat-percent">({{ getPercent(topic.opposeCount, getTotalVotes(topic)) }}%)</text>
              </view>
              <view class="stat-item abstain">
                <text class="stat-label">弃权</text>
                <text class="stat-count">{{ topic.abstainCount }}票</text>
                <text class="stat-percent">({{ getPercent(topic.abstainCount, getTotalVotes(topic)) }}%)</text>
              </view>
            </view>
            
            <!-- 进度条 -->
            <view class="progress-bar">
              <view class="progress-item agree" :style="{ width: getPercent(topic.agreeCount, getTotalVotes(topic)) + '%' }"></view>
              <view class="progress-item oppose" :style="{ width: getPercent(topic.opposeCount, getTotalVotes(topic)) + '%' }"></view>
              <view class="progress-item abstain" :style="{ width: getPercent(topic.abstainCount, getTotalVotes(topic)) + '%' }"></view>
            </view>
            
            <!-- 结果状态 -->
            <view class="result-status">
              <text class="status-text" :class="[topic.supportCount > topic.opposeCount ? 'result-pass' : 'result-reject']">
                {{ getResultText(topic) }}
              </text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons" v-if="meetingInfo.meetingStatus === '1'">
      <button class="vote-btn" @click="goVote">
        参与投票
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
  onLoad(options) {
    this.meetingId = options.id
    this.loadMeetingDetail()
    this.loadTopics()
  },
  methods: {
    loadMeetingDetail() {
      // 模拟数据
      this.meetingInfo = {
        meetingId: 1,
        meetingTitle: '2024年度业主大会第一次会议',
        meetingType: '1',
        meetingStatus: '2',
        meetingTime: '2024-01-15 14:00',
        meetingLocation: '小区会议室',
        voteEndTime: '2024-01-20 18:00',
        totalVoters: 856,
        actualVoters: 567,
        meetingContent: '本次会议主要讨论以下几个重要议题：1. 物业管理费调整方案；2. 小区监控设备更新；3. 绿化改造项目。会议将采用投票方式进行决策，请各位业主积极参与。会议期间将详细介绍各议题的背景、必要性和实施方案，并回答业主关心的问题。'
      }
    },
    
    loadTopics() {
      // 模拟议题数据
      this.topicList = [
        {
          topicId: 1,
          topicTitle: '关于调整物业管理费的议案',
          topicContent: '鉴于近年来人工成本、材料成本持续上涨，现有物业费标准已不能满足小区日常管理和维护需要，建议将物业管理费从每平方米2.5元调整至3.0元。调整后的费用将主要用于：1. 提高保洁、保安、绿化人员工资待遇；2. 更新维护小区公共设施；3. 增加小区安全防范措施。',
          agreeCount: 312,
          opposeCount: 156,
          abstainCount: 99,
          files: [
            { fileName: '物业费调整详细方案.pdf', fileUrl: '/profile/meeting/topic1_1.pdf' }
          ]
        },
        {
          topicId: 2,
          topicTitle: '关于更换小区监控设备的议案',
          topicContent: '小区现有监控设备使用年限较长，部分设备老化严重，影响安全监控效果。建议投资80万元更换高清数字监控设备，包括：1. 更换所有老旧摄像头为4K高清设备；2. 升级监控存储系统；3. 增设人脸识别功能；4. 配备夜视功能。费用从公共维修基金中支出。',
          agreeCount: 398,
          opposeCount: 98,
          abstainCount: 71,
          files: [
            { fileName: '监控设备更换报价单-A.pdf', fileUrl: '/profile/meeting/topic2_1.pdf' },
            { fileName: '监控设备更换报价单-B.pdf', fileUrl: '/profile/meeting/topic2_2.pdf' },
            { fileName: '现有设备状况报告.docx', fileUrl: '/profile/meeting/topic2_3.docx' }
          ]
        },
        {
          topicId: 3,
          topicTitle: '关于小区绿化改造的议案',
          topicContent: '为改善小区居住环境，提升生活品质，建议对小区绿化进行升级改造。主要内容包括：1. 重新规划绿化布局；2. 种植四季花卉；3. 增加休闲座椅和健身器材；4. 建设儿童游乐区；5. 完善步道照明。预算费用50万元，从公共维修基金中支出。',
          agreeCount: 423,
          opposeCount: 89,
          abstainCount: 55,
          files: [
            { fileName: '绿化改造效果图-1.jpg', fileUrl: '/profile/meeting/topic3_1.jpg' },
            { fileName: '绿化改造效果图-2.jpg', fileUrl: '/profile/meeting/topic3_2.jpg' },
            { fileName: '绿化改造工程预算.xlsx', fileUrl: '/profile/meeting/topic3_3.xlsx' },
            { fileName: '设计公司资质.pdf', fileUrl: '/profile/meeting/topic3_4.pdf' },
            { fileName: '业主意见征集稿.docx', fileUrl: '/profile/meeting/topic3_5.docx' }
          ]
        }
      ]
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
    
    goVote() {
      uni.navigateTo({
        url: `/pages/property/meeting/vote?id=${this.meetingId}`
      })
    },

    getIconByFileName(fileName) {
      const ext = fileName.split('.').pop().toLowerCase();
      // 使用一组基础且通用的图标
      if (['png', 'jpg', 'jpeg', 'gif'].includes(ext)) {
        return 'image';
      }
      if (['zip', 'rar', '7z'].includes(ext)) {
        return 'folder';
      }
      if (['pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt'].includes(ext)) {
        return 'paperclip';
      }
      return 'document'; // 默认图标
    },
    
    previewFile(file) {
      if (!file || !file.fileUrl) {
        uni.showToast({
          title: '无效的文件信息',
          icon: 'none'
        });
        return;
      }
      
      uni.showLoading({
        title: '文件加载中...'
      });

      // 注意：这里的 this.config.baseUrl 来自项目根目录的 config.js 文件
      // 它已在 main.js 中挂载到 Vue.prototype.config
      const fileUrl = this.config.baseUrl + file.fileUrl;

      uni.downloadFile({
        url: fileUrl,
        success: (res) => {
          if (res.statusCode === 200) {
            uni.openDocument({
              filePath: res.tempFilePath,
              showMenu: true, // 允许用户转发、保存等操作
              fail: () => {
                uni.showToast({
                  title: '不支持预览该文件格式',
                  icon: 'none'
                });
              }
            });
          } else {
            uni.showToast({
              title: '文件下载失败',
              icon: 'none'
            });
          }
        },
        fail: () => {
          uni.showToast({
            title: '文件下载失败',
            icon: 'none'
          });
        },
        complete: () => {
          uni.hideLoading();
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.detail-container {
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
  
  .info-header {
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
  
  .info-content {
    .info-item {
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .info-label {
        margin-left: 12rpx;
        margin-right: 20rpx;
        font-size: 26rpx;
        color: #8C8C8C;
        min-width: 120rpx;
      }
      
      .info-value {
        flex: 1;
        font-size: 26rpx;
        color: #262626;
        font-weight: 500;
      }
    }
  }
}

.meeting-content {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .content-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .content-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .content-text {
    text {
      font-size: 26rpx;
      color: #595959;
      line-height: 1.6;
    }
  }
}

.topic-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .section-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
}

.topic-item {
  margin-bottom: 40rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .topic-header {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
    
    .topic-number {
      display: inline-block;
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
    }
    
    .topic-title {
      flex: 1;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
    }
  }
  
  .topic-content {
    margin-bottom: 24rpx;
    padding-left: 64rpx;
    
    text {
      font-size: 26rpx;
      color: #595959;
      line-height: 1.6;
    }
  }
}

.attachment-section {
  margin-top: 24rpx;
  padding-left: 64rpx;

  .attachment-scroll-view {
    width: 100%;
    height: 200rpx; /* 明确高度以确保渲染 */

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
  width: 220rpx; /* 增加宽度 */
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

.vote-result {
  padding-left: 64rpx;
  
  .result-header {
    margin-bottom: 20rpx;
    
    text {
      font-size: 26rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .result-stats {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20rpx;
    
    .stat-item {
      flex: 1;
      text-align: center;
      
      .stat-label {
        display: block;
        font-size: 22rpx;
        color: #8C8C8C;
        margin-bottom: 8rpx;
      }
      
      .stat-count {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 4rpx;
      }
      
      .stat-percent {
        font-size: 20rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .progress-bar {
    display: flex;
    height: 12rpx;
    background: #F0F0F0;
    border-radius: 6rpx;
    overflow: hidden;
    margin-bottom: 20rpx;
    
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
  
  .result-status {
    text-align: center;
    
    .status-text {
      display: inline-block;
      padding: 8rpx 24rpx;
      border-radius: 16rpx;
      font-size: 24rpx;
      font-weight: 600;
      
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

.action-buttons {
  padding: 40rpx;
  
  .vote-btn {
    width: 100%;
    height: 88rpx;
    background: #1890FF;
    color: #FFFFFF;
    font-size: 32rpx;
    font-weight: 600;
    border-radius: 24rpx;
    border: none;
    
    &:active {
      background: #096DD9;
    }
  }
}
</style>