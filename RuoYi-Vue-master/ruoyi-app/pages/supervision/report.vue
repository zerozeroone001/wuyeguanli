<template>
  <view class="report-container">
    <!-- 报告头部 -->
    <view class="report-header">
      <view class="header-content">
        <text class="report-title">{{ reportInfo.title }}</text>
        <view class="report-meta">
          <text class="report-no">报告编号：{{ reportInfo.reportNo }}</text>
          <text class="report-date">出具日期：{{ reportInfo.reportDate }}</text>
        </view>
      </view>
      <view class="report-logo">
        <uni-icons type="medal" size="48" color="#722ED1" />
      </view>
    </view>

    <!-- 监督概况 -->
    <view class="supervision-summary">
      <view class="section-header">
        <uni-icons type="bar-chart" size="20" color="#262626" />
        <text class="section-title">监督概况</text>
      </view>
      
      <view class="summary-grid">
        <view class="summary-item">
          <text class="summary-label">监督对象</text>
          <text class="summary-value">{{ reportInfo.target }}</text>
        </view>
        <view class="summary-item">
          <text class="summary-label">监督员</text>
          <text class="summary-value">{{ reportInfo.supervisor }}</text>
        </view>
        <view class="summary-item">
          <text class="summary-label">监督时间</text>
          <text class="summary-value">{{ reportInfo.duration }}</text>
        </view>
        <view class="summary-item">
          <text class="summary-label">监督类型</text>
          <text class="summary-value">{{ getTypeText(reportInfo.type) }}</text>
        </view>
      </view>
    </view>

    <!-- 问题统计 -->
    <view class="issue-statistics">
      <view class="section-header">
        <uni-icons type="pie-chart" size="20" color="#262626" />
        <text class="section-title">问题统计</text>
      </view>
      
      <view class="stats-overview">
        <view class="stats-chart">
          <!-- 这里可以放置图表组件 -->
          <view class="chart-placeholder">
            <view class="chart-circle">
              <text class="chart-total">{{ reportInfo.issueStats.total }}</text>
              <text class="chart-label">发现问题</text>
            </view>
          </view>
        </view>
        
        <view class="stats-details">
          <view class="detail-item resolved">
            <view class="detail-dot"></view>
            <text class="detail-label">已整改</text>
            <text class="detail-count">{{ reportInfo.issueStats.resolved }}</text>
          </view>
          <view class="detail-item pending">
            <view class="detail-dot"></view>
            <text class="detail-label">整改中</text>
            <text class="detail-count">{{ reportInfo.issueStats.pending }}</text>
          </view>
          <view class="detail-item overdue">
            <view class="detail-dot"></view>
            <text class="detail-label">已逾期</text>
            <text class="detail-count">{{ reportInfo.issueStats.overdue || 0 }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 主要发现 -->
    <view class="main-findings">
      <view class="section-header">
        <uni-icons type="search" size="20" color="#262626" />
        <text class="section-title">主要发现</text>
      </view>
      
      <view class="findings-list">
        <view 
          class="finding-item" 
          v-for="(finding, index) in reportInfo.findings" 
          :key="index"
        >
          <view class="finding-header">
            <view class="finding-number">{{ index + 1 }}</view>
            <text class="finding-title">{{ finding.title }}</text>
            <view class="finding-level" :class="'level-' + finding.level">
              {{ getLevelText(finding.level) }}
            </view>
          </view>
          <text class="finding-description">{{ finding.description }}</text>
          <view class="finding-status" v-if="finding.status">
            <text class="status-text" :class="'status-' + finding.status">
              {{ getStatusText(finding.status) }}
            </text>
          </view>
        </view>
      </view>
    </view>

    <!-- 整改建议 -->
    <view class="recommendations">
      <view class="section-header">
        <uni-icons type="lightbulb" size="20" color="#262626" />
        <text class="section-title">整改建议</text>
      </view>
      
      <view class="recommendation-list">
        <view 
          class="recommendation-item" 
          v-for="(rec, index) in reportInfo.recommendations" 
          :key="index"
        >
          <view class="rec-number">{{ index + 1 }}</view>
          <view class="rec-content">
            <text class="rec-text">{{ rec.content }}</text>
            <text class="rec-deadline" v-if="rec.deadline">
              建议完成时间：{{ rec.deadline }}
            </text>
          </view>
        </view>
      </view>
    </view>

    <!-- 监督结论 -->
    <view class="conclusion">
      <view class="section-header">
        <uni-icons type="checkmarkempty" size="20" color="#262626" />
        <text class="section-title">监督结论</text>
      </view>
      
      <view class="conclusion-content">
        <text class="conclusion-text">{{ reportInfo.conclusion }}</text>
      </view>
      
      <view class="conclusion-rating">
        <text class="rating-label">总体评价：</text>
        <view class="rating-stars">
          <view 
            class="star-item" 
            v-for="(star, index) in 5" 
            :key="index"
            :class="{ active: index < reportInfo.rating }"
          >
            <uni-icons type="star-filled" size="20" :color="index < reportInfo.rating ? '#FA8C16' : '#F0F0F0'" />
          </view>
        </view>
        <text class="rating-text">{{ getRatingText(reportInfo.rating) }}</text>
      </view>
    </view>

    <!-- 签名区域 -->
    <view class="signature-section">
      <view class="signature-grid">
        <view class="signature-item">
          <text class="signature-label">监督员：</text>
          <text class="signature-name">{{ reportInfo.supervisor }}</text>
          <text class="signature-date">{{ reportInfo.reportDate }}</text>
        </view>
        <view class="signature-item">
          <text class="signature-label">审核人：</text>
          <text class="signature-name">{{ reportInfo.reviewer || '待审核' }}</text>
          <text class="signature-date" v-if="reportInfo.reviewDate">{{ reportInfo.reviewDate }}</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button class="action-btn secondary" @click="shareReport">
        <uni-icons type="share" size="18" color="#595959" />
        <text>分享</text>
      </button>
      <button class="action-btn secondary" @click="printReport">
        <uni-icons type="printer" size="18" color="#595959" />
        <text>打印</text>
      </button>
      <button class="action-btn primary" @click="downloadReport">
        <uni-icons type="download" size="18" color="#FFFFFF" />
        <text>下载PDF</text>
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      supervisionId: null,
      reportInfo: {}
    }
  },
  onLoad(options) {
    this.supervisionId = options.id
    this.loadReportData()
  },
  methods: {
    loadReportData() {
      // 模拟监督报告数据
      this.reportInfo = {
        title: '物业服务质量专项监督报告',
        reportNo: 'JD-BG-202412001',
        reportDate: '2024-12-05',
        target: '物业服务企业',
        supervisor: '李监督员',
        duration: '2024-12-01 至 2024-12-05',
        type: 'service',
        issueStats: {
          total: 8,
          resolved: 7,
          pending: 1,
          overdue: 0
        },
        findings: [
          {
            title: '垃圾清运不及时',
            description: '1号楼垃圾桶满溢，未及时清运，影响小区环境卫生。',
            level: 'medium',
            status: 'resolved'
          },
          {
            title: '绿化带杂草丛生',
            description: '东侧绿化带杂草较多，影响小区整体美观度。',
            level: 'low',
            status: 'resolved'
          },
          {
            title: '电梯维护记录缺失',
            description: '2号楼电梯近期维护记录不完整，存在安全隐患。',
            level: 'high',
            status: 'resolved'
          },
          {
            title: '门禁系统故障',
            description: '3号楼门禁系统偶发性故障，影响业主正常出入。',
            level: 'high',
            status: 'pending'
          }
        ],
        recommendations: [
          {
            content: '建立垃圾清运定时制度，确保每日定时清运，保持小区环境整洁。',
            deadline: '2024-12-10'
          },
          {
            content: '加强绿化养护工作，定期除草修剪，提升小区环境品质。',
            deadline: '2024-12-15'
          },
          {
            content: '完善设备维护档案，建立电子化记录系统，确保维护记录完整。',
            deadline: '2024-12-08'
          },
          {
            content: '尽快修复门禁系统故障，建立设备巡检制度，预防类似问题。',
            deadline: '2024-12-12'
          }
        ],
        conclusion: '经过为期5天的专项监督，发现8个问题，目前已整改7个，整改率87.5%。总体而言，物业服务质量有明显改善，但仍需持续关注门禁系统等关键设施的维护工作。建议物业公司建立更完善的管理制度，提升服务质量和响应速度。',
        rating: 4,
        reviewer: '王审核员',
        reviewDate: '2024-12-06'
      }
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
    
    getLevelText(level) {
      const textMap = {
        'high': '严重',
        'medium': '一般',
        'low': '轻微'
      }
      return textMap[level] || '未知'
    },
    
    getStatusText(status) {
      const textMap = {
        'resolved': '已整改',
        'pending': '整改中',
        'overdue': '已逾期'
      }
      return textMap[status] || '未知'
    },
    
    getRatingText(rating) {
      const textMap = {
        5: '优秀',
        4: '良好',
        3: '合格',
        2: '待改进',
        1: '不合格'
      }
      return textMap[rating] || '未评级'
    },
    
    shareReport() {
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
    
    printReport() {
      uni.showToast({
        title: '打印功能开发中',
        icon: 'none'
      })
    },
    
    downloadReport() {
      uni.showLoading({
        title: '生成PDF中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '下载完成',
          icon: 'success'
        })
      }, 2000)
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.report-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.report-header, .supervision-summary, .issue-statistics, .main-findings,
.recommendations, .conclusion, .signature-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
}

.report-header {
  display: flex;
  align-items: center;
  
  .header-content {
    flex: 1;
    
    .report-title {
      display: block;
      font-size: 36rpx;
      font-weight: 600;
      color: #262626;
      margin-bottom: 16rpx;
    }
    
    .report-meta {
      .report-no, .report-date {
        display: block;
        font-size: 24rpx;
        color: #8C8C8C;
        margin-bottom: 8rpx;
      }
    }
  }
  
  .report-logo {
    width: 96rpx;
    height: 96rpx;
    background: #F3E8FF;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
  
  .section-title {
    margin-left: 12rpx;
    font-size: 28rpx;
    font-weight: 600;
    color: #262626;
  }
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
  
  .summary-item {
    .summary-label {
      display: block;
      font-size: 22rpx;
      color: #8C8C8C;
      margin-bottom: 8rpx;
    }
    
    .summary-value {
      font-size: 26rpx;
      font-weight: 500;
      color: #262626;
    }
  }
}

.stats-overview {
  display: flex;
  align-items: center;
  gap: 40rpx;
  
  .stats-chart {
    .chart-placeholder {
      width: 160rpx;
      height: 160rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      
      .chart-circle {
        width: 120rpx;
        height: 120rpx;
        border: 8rpx solid #722ED1;
        border-radius: 50%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        
        .chart-total {
          font-size: 32rpx;
          font-weight: 600;
          color: #722ED1;
          margin-bottom: 4rpx;
        }
        
        .chart-label {
          font-size: 20rpx;
          color: #8C8C8C;
        }
      }
    }
  }
  
  .stats-details {
    flex: 1;
    
    .detail-item {
      display: flex;
      align-items: center;
      margin-bottom: 16rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .detail-dot {
        width: 12rpx;
        height: 12rpx;
        border-radius: 50%;
        margin-right: 12rpx;
      }
      
      .detail-label {
        flex: 1;
        font-size: 24rpx;
        color: #595959;
      }
      
      .detail-count {
        font-size: 24rpx;
        font-weight: 600;
        color: #262626;
      }
      
      &.resolved .detail-dot {
        background: #52C41A;
      }
      
      &.pending .detail-dot {
        background: #FA8C16;
      }
      
      &.overdue .detail-dot {
        background: #FF4D4F;
      }
    }
  }
}

.findings-list {
  .finding-item {
    padding: 24rpx;
    background: #F8F9FA;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .finding-header {
      display: flex;
      align-items: center;
      margin-bottom: 16rpx;
      
      .finding-number {
        width: 36rpx;
        height: 36rpx;
        background: #722ED1;
        color: #FFFFFF;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 22rpx;
        font-weight: 600;
        margin-right: 16rpx;
      }
      
      .finding-title {
        flex: 1;
        font-size: 26rpx;
        font-weight: 500;
        color: #262626;
      }
      
      .finding-level {
        padding: 4rpx 12rpx;
        border-radius: 12rpx;
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
    }
    
    .finding-description {
      font-size: 24rpx;
      color: #595959;
      line-height: 1.6;
      margin-bottom: 12rpx;
    }
    
    .finding-status {
      .status-text {
        padding: 4rpx 12rpx;
        border-radius: 12rpx;
        font-size: 20rpx;
        font-weight: 500;
        
        &.status-resolved {
          background: #F6FFED;
          color: #52C41A;
        }
        
        &.status-pending {
          background: #FFF2E8;
          color: #FA8C16;
        }
        
        &.status-overdue {
          background: #FFF2F0;
          color: #FF4D4F;
        }
      }
    }
  }
}

.recommendation-list {
  .recommendation-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 24rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .rec-number {
      width: 36rpx;
      height: 36rpx;
      background: #E6F7FF;
      color: #1890FF;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 22rpx;
      font-weight: 600;
      margin-right: 16rpx;
      margin-top: 4rpx;
    }
    
    .rec-content {
      flex: 1;
      
      .rec-text {
        display: block;
        font-size: 26rpx;
        color: #262626;
        line-height: 1.6;
        margin-bottom: 8rpx;
      }
      
      .rec-deadline {
        font-size: 22rpx;
        color: #FA8C16;
      }
    }
  }
}

.conclusion-content {
  .conclusion-text {
    font-size: 26rpx;
    color: #262626;
    line-height: 1.8;
  }
}

.conclusion-rating {
  display: flex;
  align-items: center;
  margin-top: 24rpx;
  padding-top: 24rpx;
  border-top: 1rpx solid #F0F0F0;
  
  .rating-label {
    font-size: 24rpx;
    color: #595959;
    margin-right: 16rpx;
  }
  
  .rating-stars {
    display: flex;
    gap: 8rpx;
    margin-right: 16rpx;
  }
  
  .rating-text {
    font-size: 24rpx;
    font-weight: 600;
    color: #FA8C16;
  }
}

.signature-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 40rpx;
  
  .signature-item {
    text-align: center;
    
    .signature-label {
      display: block;
      font-size: 24rpx;
      color: #8C8C8C;
      margin-bottom: 16rpx;
    }
    
    .signature-name {
      display: block;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
      margin-bottom: 8rpx;
    }
    
    .signature-date {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
}

.action-buttons {
  display: flex;
  gap: 16rpx;
  padding: 0 20rpx;
  
  .action-btn {
    flex: 1;
    height: 80rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8rpx;
    
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