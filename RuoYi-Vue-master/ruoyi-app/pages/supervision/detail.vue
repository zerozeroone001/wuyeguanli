<template>
  <view class="detail-container">
    <!-- 监督基本信息 -->
    <view class="supervision-info">
      <view class="info-header">
        <text class="supervision-title">{{ supervisionInfo.title }}</text>
        <view class="supervision-status" :class="['status-' + supervisionInfo.status]">
          {{ getStatusText(supervisionInfo.status) }}
        </view>
      </view>
      
      <view class="supervision-meta">
        <view class="meta-item">
          <uni-icons type="list" size="14" color="#8C8C8C" />
          <text>编号：{{ supervisionInfo.supervisionNo }}</text>
        </view>
        <view class="meta-item">
          <uni-icons type="calendar" size="14" color="#8C8C8C" />
          <text>开始时间：{{ supervisionInfo.startTime }}</text>
        </view>
        <view class="meta-item" v-if="supervisionInfo.endTime">
          <uni-icons type="clock" size="14" color="#8C8C8C" />
          <text>结束时间：{{ supervisionInfo.endTime }}</text>
        </view>
        <view class="meta-item">
          <uni-icons type="person" size="14" color="#8C8C8C" />
          <text>监督员：{{ supervisionInfo.supervisor }}</text>
        </view>
      </view>
      
      <view class="supervision-type-badge" :style="{ backgroundColor: getTypeColor(supervisionInfo.type) }">
        {{ getTypeText(supervisionInfo.type) }}
      </view>
      
      <!-- 优先级标识 -->
      <view class="priority-badge" :class="['priority-' + supervisionInfo.priority]">
        <uni-icons :type="getPriorityIcon(supervisionInfo.priority)" size="12" />
        <text>{{ getPriorityText(supervisionInfo.priority) }}</text>
      </view>
    </view>

    <!-- 监督对象信息 -->
    <view class="target-info">
      <view class="section-header">
        <uni-icons type="home" size="20" color="#262626" />
        <text class="section-title">监督对象</text>
      </view>
      
      <view class="target-content">
        <view class="target-item">
          <text class="target-label">监督对象：</text>
          <text class="target-value">{{ supervisionInfo.target }}</text>
        </view>
        <view class="target-item">
          <text class="target-label">负责人：</text>
          <text class="target-value">{{ supervisionInfo.targetContact }}</text>
        </view>
        <view class="target-item">
          <text class="target-label">联系电话：</text>
          <text class="target-value">{{ supervisionInfo.targetPhone }}</text>
        </view>
        <view class="target-item">
          <text class="target-label">监督范围：</text>
          <text class="target-value">{{ supervisionInfo.scope }}</text>
        </view>
      </view>
    </view>

    <!-- 监督说明 -->
    <view class="description-section" v-if="supervisionInfo.description">
      <view class="section-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="section-title">监督说明</text>
      </view>
      
      <view class="description-content">
        <text>{{ supervisionInfo.description }}</text>
      </view>
    </view>

    <!-- 问题统计 -->
    <view class="issue-overview" v-if="supervisionInfo.issueStats">
      <view class="section-header">
        <uni-icons type="bar-chart" size="20" color="#262626" />
        <text class="section-title">问题统计</text>
      </view>
      
      <view class="stats-grid">
        <view class="stat-card total">
          <view class="stat-icon">
            <uni-icons type="list" size="24" color="#1890FF" />
          </view>
          <view class="stat-info">
            <text class="stat-number">{{ supervisionInfo.issueStats.total }}</text>
            <text class="stat-label">发现问题</text>
          </view>
        </view>
        <view class="stat-card resolved">
          <view class="stat-icon">
            <uni-icons type="checkmarkempty" size="24" color="#52C41A" />
          </view>
          <view class="stat-info">
            <text class="stat-number">{{ supervisionInfo.issueStats.resolved }}</text>
            <text class="stat-label">已整改</text>
          </view>
        </view>
        <view class="stat-card pending">
          <view class="stat-icon">
            <uni-icons type="clock" size="24" color="#FA8C16" />
          </view>
          <view class="stat-info">
            <text class="stat-number">{{ supervisionInfo.issueStats.pending }}</text>
            <text class="stat-label">整改中</text>
          </view>
        </view>
        <view class="stat-card overdue">
          <view class="stat-icon">
            <uni-icons type="close" size="24" color="#FF4D4F" />
          </view>
          <view class="stat-info">
            <text class="stat-number">{{ supervisionInfo.issueStats.overdue || 0 }}</text>
            <text class="stat-label">已逾期</text>
          </view>
        </view>
      </view>
      
      <!-- 整改进度 -->
      <view class="progress-section" v-if="supervisionInfo.issueStats.total > 0">
        <view class="progress-header">
          <text class="progress-text">整改进度</text>
          <text class="progress-percent">{{ getProgressPercent() }}%</text>
        </view>
        <view class="progress-bar">
          <view class="progress-fill" :style="{ width: getProgressPercent() + '%' }"></view>
        </view>
      </view>
    </view>

    <!-- 监督记录 -->
    <view class="supervision-records">
      <view class="section-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="section-title">监督记录</text>
        <text class="record-count">{{ recordList.length }}条</text>
      </view>
      
      <view class="record-timeline">
        <view 
          class="timeline-item" 
          v-for="(record, index) in recordList" 
          :key="record.id"
        >
          <view class="timeline-dot" :class="['dot-' + record.type]"></view>
          <view class="timeline-content">
            <view class="record-header">
              <text class="record-title">{{ record.title }}</text>
              <text class="record-time">{{ record.createTime }}</text>
            </view>
            <text class="record-content">{{ record.content }}</text>
            
            <!-- 问题列表 -->
            <view class="issue-list" v-if="record.issues && record.issues.length > 0">
              <view 
                class="issue-item" 
                v-for="issue in record.issues" 
                :key="issue.id"
                @click="viewIssue(issue)"
              >
                <view class="issue-icon" :class="['issue-' + issue.status]">
                  <uni-icons :type="getIssueStatusIcon(issue.status)" size="16" />
                </view>
                <view class="issue-info">
                  <text class="issue-title">{{ issue.title }}</text>
                  <text class="issue-desc">{{ issue.description }}</text>
                  <view class="issue-meta">
                    <text class="issue-level" :class="'level-' + issue.level">
                      {{ getIssueLevelText(issue.level) }}
                    </text>
                    <text class="issue-deadline" v-if="issue.deadline">
                      整改期限：{{ issue.deadline }}
                    </text>
                  </view>
                </view>
              </view>
            </view>
            
            <!-- 附件 -->
            <view class="record-attachments" v-if="record.attachments && record.attachments.length > 0">
              <text class="attachment-title">相关附件：</text>
              <view class="attachment-list">
                <view 
                  class="attachment-item" 
                  v-for="attachment in record.attachments" 
                  :key="attachment.id"
                  @click="previewAttachment(attachment)"
                >
                  <uni-icons type="paperplane" size="16" color="#1890FF" />
                  <text class="attachment-name">{{ attachment.name }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 监督报告 -->
    <view class="supervision-report" v-if="supervisionInfo.status === 'completed'">
      <view class="section-header">
        <uni-icons type="medal" size="20" color="#262626" />
        <text class="section-title">监督报告</text>
      </view>
      
      <view class="report-card">
        <view class="report-header">
          <view class="report-icon">
            <uni-icons type="medal" size="32" color="#722ED1" />
          </view>
          <view class="report-info">
            <text class="report-title">{{ supervisionInfo.reportTitle }}</text>
            <text class="report-no">报告编号：{{ supervisionInfo.reportNo }}</text>
            <text class="report-date">出具日期：{{ supervisionInfo.reportDate }}</text>
          </view>
        </view>
        
        <view class="report-summary">
          <text class="summary-title">监督结论：</text>
          <text class="summary-content">{{ supervisionInfo.reportSummary }}</text>
        </view>
        
        <view class="report-actions">
          <button class="report-btn secondary" @click="previewReport">
            预览报告
          </button>
          <button class="report-btn primary" @click="downloadReport">
            下载报告
          </button>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button 
        class="action-btn secondary" 
        v-if="supervisionInfo.status === 'in_progress'"
        @click="addRecord"
      >
        添加记录
      </button>
      <button 
        class="action-btn secondary" 
        @click="shareSupervision"
      >
        分享
      </button>
      <button 
        class="action-btn primary" 
        v-if="supervisionInfo.status === 'completed'"
        @click="downloadReport"
      >
        下载报告
      </button>
      <button 
        class="action-btn secondary" 
        v-if="canSuspend"
        @click="suspendSupervision"
      >
        暂停监督
      </button>
    </view>
  </view>
</template>

<script>
import { previewAttachment } from '@/utils/filePreview.js';
import config from '@/config.js';

export default {
  data() {
    return {
      supervisionId: null,
      supervisionInfo: {},
      recordList: []
    }
  },
  computed: {
    canSuspend() {
      return this.supervisionInfo.status === 'in_progress'
    }
  },
  onLoad(options) {
    this.supervisionId = options.id
    this.loadSupervisionDetail()
  },
  methods: {
    loadSupervisionDetail() {
      // 模拟监督详情数据
      this.supervisionInfo = {
        supervisionId: 1,
        supervisionNo: 'JD202412001',
        title: '物业服务质量专项监督',
        type: 'service',
        status: 'completed',
        priority: 'high',
        supervisor: '李监督员',
        startTime: '2024-12-01 09:00',
        endTime: '2024-12-05 17:00',
        target: '物业服务企业',
        targetContact: '王经理',
        targetPhone: '138****5678',
        scope: '小区物业服务质量、服务态度、响应时效等',
        description: '针对小区物业服务质量进行全面监督检查，重点关注业主投诉较多的问题，确保物业服务符合合同约定和相关标准。',
        issueStats: {
          total: 8,
          resolved: 7,
          pending: 1,
          overdue: 0
        },
        reportTitle: '物业服务质量专项监督报告',
        reportNo: 'JD-BG-202412001',
        reportDate: '2024-12-05',
        reportSummary: '经过为期5天的专项监督，发现8个问题，目前已整改7个，整改率87.5%。总体而言，物业服务质量有明显改善，但仍需持续关注。'
      }
      
      // 模拟监督记录
      this.recordList = [
        {
          id: 1,
          title: '监督启动',
          type: 'start',
          content: '正式启动物业服务质量专项监督工作，已通知相关责任人配合监督检查。',
          createTime: '2024-12-01 09:00',
          issues: [],
          attachments: [
            {
              id: 1,
              name: '监督通知书.pdf',
              url: '/static/docs/notice.pdf'
            }
          ]
        },
        {
          id: 2,
          title: '现场检查',
          type: 'inspection',
          content: '对小区公共区域清洁、绿化养护、设施设备维护等进行现场检查，发现多项问题。',
          createTime: '2024-12-02 10:30',
          issues: [
            {
              id: 1,
              title: '垃圾清运不及时',
              description: '1号楼垃圾桶满溢，未及时清运',
              level: 'medium',
              status: 'resolved',
              deadline: '2024-12-03 18:00'
            },
            {
              id: 2,
              title: '绿化带杂草丛生',
              description: '东侧绿化带杂草较多，影响小区环境',
              level: 'low',
              status: 'resolved',
              deadline: '2024-12-05 18:00'
            },
            {
              id: 3,
              title: '电梯维护记录缺失',
              description: '2号楼电梯近期维护记录不完整',
              level: 'high',
              status: 'resolved',
              deadline: '2024-12-04 18:00'
            }
          ],
          attachments: [
            {
              id: 2,
              name: '现场检查照片.zip',
              url: '/static/images/inspection.zip'
            }
          ]
        },
        {
          id: 3,
          title: '问题整改跟进',
          type: 'follow_up',
          content: '跟进前期发现问题的整改情况，大部分问题已得到有效解决。',
          createTime: '2024-12-04 14:20',
          issues: [
            {
              id: 4,
              title: '门禁系统故障',
              description: '3号楼门禁系统偶发性故障，影响业主正常出入',
              level: 'high',
              status: 'pending',
              deadline: '2024-12-10 18:00'
            }
          ],
          attachments: []
        },
        {
          id: 4,
          title: '监督总结',
          type: 'summary',
          content: '完成本次专项监督工作，整体效果良好，物业服务质量有明显提升，形成监督报告。',
          createTime: '2024-12-05 17:00',
          issues: [],
          attachments: [
            {
              id: 3,
              name: '监督报告.pdf',
              url: '/static/docs/report.pdf'
            }
          ]
        }
      ]
    },
    
    getStatusClass(status) {
      const classMap = {
        'planned': 'status-planned',
        'in_progress': 'status-progress',
        'completed': 'status-completed',
        'suspended': 'status-suspended'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        'planned': '计划中',
        'in_progress': '进行中',
        'completed': '已完成',
        'suspended': '已暂停'
      }
      return textMap[status] || '未知状态'
    },
    
    getTypeText(type) {
      const textMap = {
        'service': '服务监督',
        'finance': '财务监督',
        'maintenance': '维护监督',
        'management': '管理监督'
      }
      return textMap[type] || '其他'
    },
    
    getTypeColor(type) {
      const colorMap = {
        'service': '#1890FF',
        'finance': '#52C41A',
        'maintenance': '#FA8C16',
        'management': '#722ED1'
      }
      return colorMap[type] || '#8C8C8C'
    },
    
    getPriorityClass(priority) {
      const classMap = {
        'high': 'priority-high',
        'medium': 'priority-medium',
        'low': 'priority-low'
      }
      return classMap[priority] || 'priority-default'
    },
    
    getPriorityText(priority) {
      const textMap = {
        'high': '高优先级',
        'medium': '中优先级',
        'low': '低优先级'
      }
      return textMap[priority] || '普通'
    },
    
    getPriorityIcon(priority) {
      const iconMap = {
        'high': 'fire',
        'medium': 'flag',
        'low': 'info'
      }
      return iconMap[priority] || 'info'
    },
    
    getRecordTypeClass(type) {
      const classMap = {
        'start': 'dot-start',
        'inspection': 'dot-inspection',
        'follow_up': 'dot-follow',
        'summary': 'dot-summary'
      }
      return classMap[type] || 'dot-default'
    },
    
    getIssueStatusClass(status) {
      const classMap = {
        'pending': 'issue-pending',
        'resolved': 'issue-resolved',
        'overdue': 'issue-overdue'
      }
      return classMap[status] || 'issue-default'
    },
    
    getIssueStatusIcon(status) {
      const iconMap = {
        'pending': 'clock',
        'resolved': 'checkmarkempty',
        'overdue': 'close'
      }
      return iconMap[status] || 'help'
    },
    
    getIssueLevelText(level) {
      const textMap = {
        'high': '严重',
        'medium': '一般',
        'low': '轻微'
      }
      return textMap[level] || '未知'
    },
    
    getProgressPercent() {
      const stats = this.supervisionInfo.issueStats
      if (!stats || stats.total === 0) return 0
      return Math.round((stats.resolved / stats.total) * 100)
    },
    
    viewIssue(issue) {
      uni.navigateTo({
        url: `/pages/supervision/issue?id=${issue.id}`
      })
    },
    
    previewAttachment(attachment) {
      previewAttachment(attachment, config.baseUrl);
    },
    
    addRecord() {
      uni.navigateTo({
        url: `/pages/supervision/record?id=${this.supervisionId}`
      })
    },
    
    previewReport() {
      uni.navigateTo({
        url: `/pages/supervision/report?id=${this.supervisionId}`
      })
    },
    
    downloadReport() {
      uni.showLoading({
        title: '下载中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '下载完成',
          icon: 'success'
        })
      }, 2000)
    },
    
    shareSupervision() {
      uni.showActionSheet({
        itemList: ['分享给微信好友', '分享到朋友圈', '复制链接'],
        success: (res) => {
          const actions = ['微信好友', '朋友圈', '复制链接']
          uni.showToast({
            title: `分享到${actions[res.tapIndex]}`,
            icon: 'success'
          })
        }
      })
    },
    
    suspendSupervision() {
      uni.showModal({
        title: '暂停监督',
        content: '确定要暂停此次监督吗？',
        confirmColor: '#FA8C16',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '处理中...'
            })
            
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({
                title: '监督已暂停',
                icon: 'success'
              })
              
              this.supervisionInfo.status = 'suspended'
            }, 1000)
          }
        }
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

.detail-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.supervision-info, .target-info, .description-section, .issue-overview, 
.supervision-records, .supervision-report {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24rpx;
    
    .section-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .record-count {
      font-size: 24rpx;
      color: #8C8C8C;
    }
  }
}

.supervision-info {
  position: relative;
  
  .info-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
    
    .supervision-title {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
    }
    
    .supervision-status {
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      font-size: 22rpx;
      font-weight: 500;
      white-space: nowrap;
      
      &.status-planned {
        background: #F0F8FF;
        color: #1890FF;
      }
      
      &.status-progress {
        background: #FFF2E8;
        color: #FA8C16;
      }
      
      &.status-completed {
        background: #F6FFED;
        color: #52C41A;
      }
      
      &.status-suspended {
        background: #F5F5F5;
        color: #8C8C8C;
      }
    }
  }
  
  .supervision-meta {
    display: flex;
    align-items: center;
    gap: 24rpx;
    flex-wrap: wrap;
    margin-bottom: 20rpx;
    
    .meta-item {
      display: flex;
      align-items: center;
      
      text {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .supervision-type-badge {
    position: absolute;
    top: 40rpx;
    right: 40rpx;
    padding: 8rpx 16rpx;
    border-radius: 16rpx;
    font-size: 22rpx;
    font-weight: 500;
    color: #FFFFFF;
  }
  
  .priority-badge {
    display: inline-flex;
    align-items: center;
    padding: 4rpx 12rpx;
    border-radius: 12rpx;
    
    text {
      margin-left: 4rpx;
      font-size: 20rpx;
      font-weight: 500;
    }
    
    &.priority-high {
      background: #FFF2F0;
      color: #FF4D4F;
    }
    
    &.priority-medium {
      background: #FFF2E8;
      color: #FA8C16;
    }
    
    &.priority-low {
      background: #F0F8FF;
      color: #1890FF;
    }
  }
}

.target-content {
  .target-item {
    display: flex;
    margin-bottom: 16rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .target-label {
      font-size: 24rpx;
      color: #8C8C8C;
      min-width: 160rpx;
    }
    
    .target-value {
      font-size: 24rpx;
      color: #262626;
    }
  }
}

.description-content {
  text {
    font-size: 26rpx;
    color: #595959;
    line-height: 1.6;
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  margin-bottom: 30rpx;
  
  .stat-card {
    display: flex;
    align-items: center;
    padding: 24rpx;
    background: #F8F9FA;
    border-radius: 16rpx;
    
    .stat-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
    }
    
    .stat-info {
      .stat-number {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 4rpx;
      }
      
      .stat-label {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
    
    &.total .stat-icon {
      background: #E6F7FF;
    }
    
    &.resolved .stat-icon {
      background: #F6FFED;
    }
    
    &.pending .stat-icon {
      background: #FFF2E8;
    }
    
    &.overdue .stat-icon {
      background: #FFF2F0;
    }
  }
}

.progress-section {
  .progress-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12rpx;
    
    .progress-text {
      font-size: 24rpx;
      color: #595959;
    }
    
    .progress-percent {
      font-size: 24rpx;
      color: #52C41A;
      font-weight: 600;
    }
  }
  
  .progress-bar {
    height: 8rpx;
    background: #F0F0F0;
    border-radius: 4rpx;
    overflow: hidden;
    
    .progress-fill {
      height: 100%;
      background: #52C41A;
      border-radius: 4rpx;
      transition: width 0.3s ease;
    }
  }
}

.record-timeline {
  .timeline-item {
    position: relative;
    padding-left: 60rpx;
    padding-bottom: 40rpx;
    
    &:last-child {
      padding-bottom: 0;
    }
    
    &:not(:last-child)::before {
      content: '';
      position: absolute;
      left: 19rpx;
      top: 40rpx;
      bottom: 0;
      width: 2rpx;
      background: #F0F0F0;
    }
    
    .timeline-dot {
      position: absolute;
      left: 0;
      top: 0;
      width: 40rpx;
      height: 40rpx;
      border-radius: 50%;
      
      &.dot-start {
        background: #52C41A;
      }
      
      &.dot-inspection {
        background: #1890FF;
      }
      
      &.dot-follow {
        background: #FA8C16;
      }
      
      &.dot-summary {
        background: #722ED1;
      }
    }
    
    .timeline-content {
      .record-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12rpx;
        
        .record-title {
          font-size: 26rpx;
          font-weight: 600;
          color: #262626;
        }
        
        .record-time {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
      
      .record-content {
        display: block;
        font-size: 24rpx;
        color: #595959;
        line-height: 1.6;
        margin-bottom: 20rpx;
      }
    }
  }
}

.issue-list {
  margin: 20rpx 0;
  
  .issue-item {
    display: flex;
    align-items: flex-start;
    padding: 20rpx;
    background: #F8F9FA;
    border-radius: 16rpx;
    margin-bottom: 16rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    &:active {
      background: #F0F0F0;
    }
    
    .issue-icon {
      width: 48rpx;
      height: 48rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16rpx;
      margin-top: 4rpx;
      
      &.issue-pending {
        background: #FFF2E8;
        color: #FA8C16;
      }
      
      &.issue-resolved {
        background: #F6FFED;
        color: #52C41A;
      }
      
      &.issue-overdue {
        background: #FFF2F0;
        color: #FF4D4F;
      }
    }
    
    .issue-info {
      flex: 1;
      
      .issue-title {
        display: block;
        font-size: 26rpx;
        font-weight: 500;
        color: #262626;
        margin-bottom: 8rpx;
      }
      
      .issue-desc {
        display: block;
        font-size: 24rpx;
        color: #595959;
        margin-bottom: 12rpx;
      }
      
      .issue-meta {
        display: flex;
        align-items: center;
        gap: 16rpx;
        
        .issue-level {
          padding: 2rpx 8rpx;
          border-radius: 8rpx;
          font-size: 20rpx;
          font-weight: 500;
          
          &.level-high {
            background: #FFF2F0;
            color: #FF4D4F;
          }
          
          &.level-medium {
            background: #FFF2E8;
            color: #FA8C16;
          }
          
          &.level-low {
            background: #F0F8FF;
            color: #1890FF;
          }
        }
        
        .issue-deadline {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
    }
  }
}

.record-attachments {
  margin-top: 20rpx;
  
  .attachment-title {
    display: block;
    font-size: 24rpx;
    color: #595959;
    margin-bottom: 12rpx;
  }
  
  .attachment-list {
    .attachment-item {
      display: flex;
      align-items: center;
      padding: 12rpx 16rpx;
      background: #E6F7FF;
      border-radius: 12rpx;
      margin-bottom: 8rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      &:active {
        background: #BAE7FF;
      }
      
      .attachment-name {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #1890FF;
      }
    }
  }
}

.report-card {
  .report-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .report-icon {
      width: 80rpx;
      height: 80rpx;
      background: #F3E8FF;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
    }
    
    .report-info {
      flex: 1;
      
      .report-title {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 8rpx;
      }
      
      .report-no {
        display: block;
        font-size: 24rpx;
        color: #595959;
        margin-bottom: 4rpx;
      }
      
      .report-date {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .report-summary {
    background: #F3E8FF;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    
    .summary-title {
      display: block;
      font-size: 24rpx;
      font-weight: 600;
      color: #722ED1;
      margin-bottom: 12rpx;
    }
    
    .summary-content {
      font-size: 24rpx;
      color: #595959;
      line-height: 1.6;
    }
  }
  
  .report-actions {
    display: flex;
    gap: 16rpx;
    
    .report-btn {
      flex: 1;
      height: 60rpx;
      border-radius: 16rpx;
      font-size: 24rpx;
      font-weight: 500;
      border: none;
      
      &.secondary {
        background: #F8F9FA;
        color: #595959;
        border: 1rpx solid #F0F0F0;
      }
      
      &.primary {
        background: #722ED1;
        color: #FFFFFF;
      }
      
      &:active {
        opacity: 0.8;
      }
    }
  }
}

.action-buttons {
  display: flex;
  gap: 16rpx;
  padding: 0 20rpx;
  flex-wrap: wrap;
  
  .action-btn {
    flex: 1;
    min-width: 160rpx;
    height: 80rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    
    &.secondary {
      background: #F8F9FA;
      color: #595959;
      border: 1rpx solid #F0F0F0;
    }
    
    &.primary {
      background: #722ED1;
      color: #FFFFFF;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}
</style>