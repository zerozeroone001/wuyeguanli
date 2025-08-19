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
        <uni-icons type="medal" size="48" color="#13C2C2" />
      </view>
    </view>

    <!-- 项目概况 -->
    <view class="project-overview">
      <view class="section-header">
        <uni-icons type="home" size="20" color="#262626" />
        <text class="section-title">项目概况</text>
      </view>
      
      <view class="overview-grid">
        <view class="overview-item">
          <text class="overview-label">项目名称</text>
          <text class="overview-value">{{ reportInfo.projectName }}</text>
        </view>
        <view class="overview-item">
          <text class="overview-label">项目地址</text>
          <text class="overview-value">{{ reportInfo.projectAddress }}</text>
        </view>
        <view class="overview-item">
          <text class="overview-label">建设单位</text>
          <text class="overview-value">{{ reportInfo.developer }}</text>
        </view>
        <view class="overview-item">
          <text class="overview-label">查验员</text>
          <text class="overview-value">{{ reportInfo.inspector }}</text>
        </view>
        <view class="overview-item">
          <text class="overview-label">查验时间</text>
          <text class="overview-value">{{ reportInfo.inspectionPeriod }}</text>
        </view>
        <view class="overview-item">
          <text class="overview-label">项目规模</text>
          <text class="overview-value">{{ reportInfo.projectScale }}</text>
        </view>
      </view>
    </view>

    <!-- 查验结果统计 -->
    <view class="inspection-statistics">
      <view class="section-header">
        <uni-icons type="pie-chart" size="20" color="#262626" />
        <text class="section-title">查验结果统计</text>
      </view>
      
      <view class="stats-overview">
        <view class="stats-chart">
          <!-- 这里可以放置图表组件 -->
          <view class="chart-placeholder">
            <view class="chart-circle">
              <text class="chart-total">{{ reportInfo.inspectionStats.total }}</text>
              <text class="chart-label">检查项目</text>
            </view>
          </view>
        </view>
        
        <view class="stats-details">
          <view class="detail-item passed">
            <view class="detail-dot"></view>
            <text class="detail-label">合格</text>
            <text class="detail-count">{{ reportInfo.inspectionStats.passed }}</text>
          </view>
          <view class="detail-item failed">
            <view class="detail-dot"></view>
            <text class="detail-label">不合格</text>
            <text class="detail-count">{{ reportInfo.inspectionStats.failed }}</text>
          </view>
          <view class="detail-item rectified">
            <view class="detail-dot"></view>
            <text class="detail-label">已整改</text>
            <text class="detail-count">{{ reportInfo.inspectionStats.rectified }}</text>
          </view>
        </view>
      </view>
      
      <!-- 合格率展示 -->
      <view class="quality-display">
        <view class="quality-header">
          <text class="quality-title">综合合格率</text>
          <text class="quality-percent">{{ qualityRate }}%</text>
        </view>
        <view class="quality-bar">
          <view class="quality-fill" :style="{ width: qualityRate + '%' }"></view>
        </view>
        <text class="quality-note">
          {{ qualityRate >= 95 ? '优秀' : qualityRate >= 85 ? '良好' : qualityRate >= 70 ? '合格' : '待改进' }}
        </text>
      </view>
    </view>

    <!-- 分类检查结果 -->
    <view class="category-results">
      <view class="section-header">
        <uni-icons type="list" size="20" color="#262626" />
        <text class="section-title">分类检查结果</text>
      </view>
      
      <view class="category-list">
        <view 
          class="category-item" 
          v-for="category in reportInfo.categoryResults" 
          :key="category.name"
        >
          <view class="category-header">
            <view class="category-icon">
              <uni-icons :type="getCategoryIcon(category.type)" size="20" color="#13C2C2" />
            </view>
            <text class="category-name">{{ category.name }}</text>
            <view class="category-stats">
              <text class="stats-text">{{ category.passed }}/{{ category.total }}</text>
              <text class="stats-rate">{{ getCategoryRate(category) }}%</text>
            </view>
          </view>
          
          <view class="category-progress">
            <view class="progress-bar">
              <view 
                class="progress-fill" 
                :style="{ width: getCategoryRate(category) + '%' }"
              ></view>
            </view>
          </view>
          
          <!-- 主要问题 -->
          <view class="category-issues" v-if="category.issues && category.issues.length > 0">
            <text class="issues-title">主要问题：</text>
            <view class="issue-tags">
              <view 
                class="issue-tag" 
                v-for="issue in category.issues" 
                :key="issue"
              >
                {{ issue }}
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 重要发现 -->
    <view class="important-findings">
      <view class="section-header">
        <uni-icons type="warning" size="20" color="#262626" />
        <text class="section-title">重要发现</text>
      </view>
      
      <view class="findings-list">
        <view 
          class="finding-item" 
          v-for="(finding, index) in reportInfo.importantFindings" 
          :key="index"
        >
          <view class="finding-header">
            <view class="finding-number">{{ index + 1 }}</view>
            <text class="finding-title">{{ finding.title }}</text>
            <view class="finding-severity" :class="'severity-' + finding.severity">
              {{ getSeverityText(finding.severity) }}
            </view>
          </view>
          <text class="finding-description">{{ finding.description }}</text>
          <view class="finding-location" v-if="finding.location">
            <uni-icons type="location" size="14" color="#8C8C8C" />
            <text>位置：{{ finding.location }}</text>
          </view>
          <view class="finding-status" v-if="finding.status">
            <text class="status-text" :class="'status-' + finding.status">
              {{ getStatusText(finding.status) }}
            </text>
          </view>
        </view>
      </view>
    </view>

    <!-- 整改要求 -->
    <view class="rectification-requirements">
      <view class="section-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="section-title">整改要求</text>
      </view>
      
      <view class="requirement-list">
        <view 
          class="requirement-item" 
          v-for="(req, index) in reportInfo.rectificationRequirements" 
          :key="index"
        >
          <view class="req-number">{{ index + 1 }}</view>
          <view class="req-content">
            <text class="req-title">{{ req.title }}</text>
            <text class="req-description">{{ req.description }}</text>
            <view class="req-meta">
              <text class="req-deadline">整改期限：{{ req.deadline }}</text>
              <text class="req-responsible">责任方：{{ req.responsible }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 查验结论 -->
    <view class="inspection-conclusion">
      <view class="section-header">
        <uni-icons type="checkmarkempty" size="20" color="#262626" />
        <text class="section-title">查验结论</text>
      </view>
      
      <view class="conclusion-content">
        <text class="conclusion-text">{{ reportInfo.conclusion }}</text>
      </view>
      
      <view class="conclusion-recommendation">
        <text class="recommendation-title">承接建议：</text>
        <view class="recommendation-badge" :class="'rec-' + reportInfo.recommendation">
          {{ getRecommendationText(reportInfo.recommendation) }}
        </view>
      </view>
    </view>

    <!-- 签名区域 -->
    <view class="signature-section">
      <view class="signature-grid">
        <view class="signature-item">
          <text class="signature-label">查验员：</text>
          <text class="signature-name">{{ reportInfo.inspector }}</text>
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
      handoverId: null,
      reportInfo: {}
    }
  },
  computed: {
    qualityRate() {
      const stats = this.reportInfo.inspectionStats
      if (!stats || stats.total === 0) return 0
      return Math.round(((stats.passed + stats.rectified) / stats.total) * 100)
    }
  },
  onLoad(options) {
    this.handoverId = options.id
    this.loadReportData()
  },
  methods: {
    loadReportData() {
      // 模拟查验报告数据
      this.reportInfo = {
        title: '智慧花园二期承接查验报告',
        reportNo: 'CJ-BG-202412001',
        reportDate: '2024-12-05',
        projectName: '智慧花园二期住宅项目',
        projectAddress: '北京市朝阳区建国路88号',
        developer: '北京智慧地产开发有限公司',
        inspector: '张查验员',
        inspectionPeriod: '2024-12-01 至 2024-12-05',
        projectScale: '总建筑面积15万平方米，住宅楼12栋，商业配套2栋',
        inspectionStats: {
          total: 156,
          passed: 148,
          failed: 8,
          rectified: 6
        },
        categoryResults: [
          {
            name: '建筑主体',
            type: 'structure',
            total: 45,
            passed: 43,
            issues: ['外墙渗水', '楼梯扶手松动']
          },
          {
            name: '设备设施',
            type: 'equipment',
            total: 68,
            passed: 62,
            issues: ['消防栓压力不足', '电梯噪音超标']
          },
          {
            name: '配套工程',
            type: 'supporting',
            total: 28,
            passed: 28,
            issues: []
          },
          {
            name: '环境景观',
            type: 'landscape',
            total: 15,
            passed: 15,
            issues: []
          }
        ],
        importantFindings: [
          {
            title: '3号楼东侧外墙渗水',
            description: '3号楼东侧外墙存在渗水现象，可能影响室内装修和居住舒适度。',
            location: '3号楼东侧2-6层',
            severity: 'high',
            status: 'pending'
          },
          {
            title: '部分消防栓压力不足',
            description: '1号楼和5号楼部分消防栓水压不达标，存在安全隐患。',
            location: '1号楼、5号楼各层',
            severity: 'high',
            status: 'rectified'
          },
          {
            title: '电梯运行噪音超标',
            description: '2号楼客梯运行时噪音超过标准值，影响居民休息。',
            location: '2号楼客梯',
            severity: 'medium',
            status: 'rectified'
          }
        ],
        rectificationRequirements: [
          {
            title: '外墙防水整改',
            description: '对3号楼东侧外墙进行防水处理，确保不再出现渗水现象。',
            deadline: '2024-12-15',
            responsible: '施工单位'
          },
          {
            title: '消防系统调试',
            description: '对所有消防栓进行压力测试和调试，确保水压达标。',
            deadline: '2024-12-10',
            responsible: '设备安装单位'
          },
          {
            title: '电梯噪音处理',
            description: '对电梯进行噪音治理，加装隔音设备，降低运行噪音。',
            deadline: '2024-12-12',
            responsible: '电梯安装单位'
          }
        ],
        conclusion: '经过全面查验，智慧花园二期项目整体质量良好，综合合格率达到98.1%。发现的主要问题集中在外墙防水和设备调试方面，均属于可整改范围。建议在完成相关整改工作后，可以正式承接该项目。',
        recommendation: 'conditional',
        reviewer: '李审核员',
        reviewDate: '2024-12-06'
      }
    },
    
    getCategoryIcon(type) {
      const iconMap = {
        'structure': 'home',
        'equipment': 'gear',
        'supporting': 'compose',
        'landscape': 'leaf'
      }
      return iconMap[type] || 'list'
    },
    
    getCategoryRate(category) {
      if (category.total === 0) return 0
      return Math.round((category.passed / category.total) * 100)
    },
    
    getSeverityText(severity) {
      const textMap = {
        'high': '严重',
        'medium': '一般',
        'low': '轻微'
      }
      return textMap[severity] || '未知'
    },
    
    getStatusText(status) {
      const textMap = {
        'pending': '待整改',
        'rectified': '已整改',
        'resolved': '已解决'
      }
      return textMap[status] || '未知'
    },
    
    getRecommendationText(recommendation) {
      const textMap = {
        'approved': '建议承接',
        'conditional': '条件承接',
        'rejected': '不建议承接'
      }
      return textMap[recommendation] || '待定'
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

.report-header, .project-overview, .inspection-statistics, .category-results,
.important-findings, .rectification-requirements, .inspection-conclusion, .signature-section {
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
    background: #E6FFFB;
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

.overview-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
  
  .overview-item {
    .overview-label {
      display: block;
      font-size: 22rpx;
      color: #8C8C8C;
      margin-bottom: 8rpx;
    }
    
    .overview-value {
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
  margin-bottom: 30rpx;
  
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
        border: 8rpx solid #13C2C2;
        border-radius: 50%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        
        .chart-total {
          font-size: 32rpx;
          font-weight: 600;
          color: #13C2C2;
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
      
      &.passed .detail-dot {
        background: #52C41A;
      }
      
      &.failed .detail-dot {
        background: #FF4D4F;
      }
      
      &.rectified .detail-dot {
        background: #FA8C16;
      }
    }
  }
}

.quality-display {
  .quality-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
    
    .quality-title {
      font-size: 26rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .quality-percent {
      font-size: 32rpx;
      font-weight: 600;
      color: #13C2C2;
    }
  }
  
  .quality-bar {
    height: 12rpx;
    background: #F0F0F0;
    border-radius: 6rpx;
    overflow: hidden;
    margin-bottom: 12rpx;
    
    .quality-fill {
      height: 100%;
      background: #13C2C2;
      border-radius: 6rpx;
      transition: width 0.3s ease;
    }
  }
  
  .quality-note {
    text-align: center;
    font-size: 24rpx;
    color: #13C2C2;
    font-weight: 500;
  }
}

.category-list {
  .category-item {
    padding: 24rpx;
    background: #F8F9FA;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .category-header {
      display: flex;
      align-items: center;
      margin-bottom: 16rpx;
      
      .category-icon {
        width: 48rpx;
        height: 48rpx;
        background: #E6FFFB;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16rpx;
      }
      
      .category-name {
        flex: 1;
        font-size: 26rpx;
        font-weight: 500;
        color: #262626;
      }
      
      .category-stats {
        text-align: right;
        
        .stats-text {
          display: block;
          font-size: 24rpx;
          color: #595959;
          margin-bottom: 4rpx;
        }
        
        .stats-rate {
          font-size: 28rpx;
          font-weight: 600;
          color: #13C2C2;
        }
      }
    }
    
    .category-progress {
      margin-bottom: 16rpx;
      
      .progress-bar {
        height: 8rpx;
        background: #F0F0F0;
        border-radius: 4rpx;
        overflow: hidden;
        
        .progress-fill {
          height: 100%;
          background: #13C2C2;
          border-radius: 4rpx;
          transition: width 0.3s ease;
        }
      }
    }
    
    .category-issues {
      .issues-title {
        display: block;
        font-size: 22rpx;
        color: #8C8C8C;
        margin-bottom: 12rpx;
      }
      
      .issue-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 8rpx;
        
        .issue-tag {
          padding: 4rpx 12rpx;
          background: #FFF2F0;
          color: #FF4D4F;
          border-radius: 12rpx;
          font-size: 20rpx;
        }
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
        background: #13C2C2;
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
      
      .finding-severity {
        padding: 4rpx 12rpx;
        border-radius: 12rpx;
        font-size: 20rpx;
        font-weight: 500;
        
        &.severity-high {
          background: #FFF2F0;
          color: #FF4D4F;
        }
        
        &.severity-medium {
          background: #FFF2E8;
          color: #FA8C16;
        }
        
        &.severity-low {
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
    
    .finding-location {
      display: flex;
      align-items: center;
      margin-bottom: 12rpx;
      
      text {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
    
    .finding-status {
      .status-text {
        padding: 4rpx 12rpx;
        border-radius: 12rpx;
        font-size: 20rpx;
        font-weight: 500;
        
        &.status-pending {
          background: #FFF2E8;
          color: #FA8C16;
        }
        
        &.status-rectified {
          background: #F6FFED;
          color: #52C41A;
        }
        
        &.status-resolved {
          background: #F6FFED;
          color: #52C41A;
        }
      }
    }
  }
}

.requirement-list {
  .requirement-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 24rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .req-number {
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
    
    .req-content {
      flex: 1;
      
      .req-title {
        display: block;
        font-size: 26rpx;
        font-weight: 500;
        color: #262626;
        margin-bottom: 8rpx;
      }
      
      .req-description {
        display: block;
        font-size: 24rpx;
        color: #595959;
        line-height: 1.6;
        margin-bottom: 12rpx;
      }
      
      .req-meta {
        display: flex;
        gap: 24rpx;
        
        .req-deadline {
          font-size: 22rpx;
          color: #FA8C16;
        }
        
        .req-responsible {
          font-size: 22rpx;
          color: #1890FF;
        }
      }
    }
  }
}

.conclusion-content {
  margin-bottom: 24rpx;
  
  .conclusion-text {
    font-size: 26rpx;
    color: #262626;
    line-height: 1.8;
  }
}

.conclusion-recommendation {
  display: flex;
  align-items: center;
  padding-top: 24rpx;
  border-top: 1rpx solid #F0F0F0;
  
  .recommendation-title {
    font-size: 26rpx;
    font-weight: 600;
    color: #262626;
    margin-right: 16rpx;
  }
  
  .recommendation-badge {
    padding: 8rpx 20rpx;
    border-radius: 16rpx;
    font-size: 24rpx;
    font-weight: 600;
    
    &.rec-approved {
      background: #F6FFED;
      color: #52C41A;
    }
    
    &.rec-conditional {
      background: #FFF2E8;
      color: #FA8C16;
    }
    
    &.rec-rejected {
      background: #FFF2F0;
      color: #FF4D4F;
    }
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
      background: #13C2C2;
      color: #FFFFFF;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}
</style>