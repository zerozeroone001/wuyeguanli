<template>
  <view class="report-container">
    <!-- 报表类型选择 -->
    <view class="report-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeReport === 'monthly' }"
        @click="switchReport('monthly')"
      >
        月度报表
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeReport === 'quarterly' }"
        @click="switchReport('quarterly')"
      >
        季度报表
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeReport === 'annual' }"
        @click="switchReport('annual')"
      >
        年度报表
      </view>
    </view>

    <!-- 报表时间选择 -->
    <view class="time-selector">
      <view class="selector-item" @click="showTimePicker">
        <uni-icons type="calendar" size="18" color="#1890FF" />
        <text>{{ selectedTime }}</text>
        <uni-icons type="arrowdown" size="14" color="#8C8C8C" />
      </view>
      <button class="export-btn" @click="exportReport">
        <uni-icons type="download" size="16" color="#1890FF" />
        <text>导出</text>
      </button>
    </view>

    <!-- 报表概览 -->
    <view class="report-overview">
      <view class="overview-title">
        <text>{{ getReportTitle() }}</text>
      </view>
      
      <view class="overview-cards">
        <view class="overview-card income">
          <view class="card-icon">
            <uni-icons type="plus" size="24" color="#52C41A" />
          </view>
          <view class="card-content">
            <text class="card-label">总收入</text>
            <text class="card-amount">¥{{ formatAmount(reportData.totalIncome) }}</text>
          </view>
        </view>
        
        <view class="overview-card expense">
          <view class="card-icon">
            <uni-icons type="minus" size="24" color="#FF4D4F" />
          </view>
          <view class="card-content">
            <text class="card-label">总支出</text>
            <text class="card-amount">¥{{ formatAmount(reportData.totalExpense) }}</text>
          </view>
        </view>
        
        <view class="overview-card balance">
          <view class="card-icon">
            <uni-icons type="wallet" size="24" color="#1890FF" />
          </view>
          <view class="card-content">
            <text class="card-label">净收益</text>
            <text class="card-amount" :class="{ negative: reportData.netProfit < 0 }">
              ¥{{ formatAmount(Math.abs(reportData.netProfit)) }}
            </text>
          </view>
        </view>
      </view>
    </view>

    <!-- 收入分析 -->
    <view class="analysis-section">
      <view class="section-header">
        <uni-icons type="plus" size="20" color="#52C41A" />
        <text class="section-title">收入分析</text>
      </view>
      
      <view class="analysis-content">
        <!-- 收入构成图表 -->
        <view class="chart-container">
          <view class="chart-placeholder">
            <uni-icons type="pie-chart" size="60" color="#D9D9D9" />
            <text class="chart-text">收入构成图</text>
          </view>
        </view>
        
        <!-- 收入明细 -->
        <view class="income-details">
          <view 
            class="detail-item" 
            v-for="item in reportData.incomeBreakdown" 
            :key="item.category"
          >
            <view class="item-info">
              <view class="item-dot" :style="{ backgroundColor: item.color }"></view>
              <text class="item-label">{{ item.name }}</text>
            </view>
            <view class="item-values">
              <text class="item-amount">¥{{ formatAmount(item.amount) }}</text>
              <text class="item-percent">{{ item.percentage }}%</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 支出分析 -->
    <view class="analysis-section">
      <view class="section-header">
        <uni-icons type="minus" size="20" color="#FF4D4F" />
        <text class="section-title">支出分析</text>
      </view>
      
      <view class="analysis-content">
        <!-- 支出构成图表 -->
        <view class="chart-container">
          <view class="chart-placeholder">
            <uni-icons type="bar-chart" size="60" color="#D9D9D9" />
            <text class="chart-text">支出构成图</text>
          </view>
        </view>
        
        <!-- 支出明细 -->
        <view class="expense-details">
          <view 
            class="detail-item" 
            v-for="item in reportData.expenseBreakdown" 
            :key="item.category"
          >
            <view class="item-info">
              <view class="item-dot" :style="{ backgroundColor: item.color }"></view>
              <text class="item-label">{{ item.name }}</text>
            </view>
            <view class="item-values">
              <text class="item-amount">¥{{ formatAmount(item.amount) }}</text>
              <text class="item-percent">{{ item.percentage }}%</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 趋势分析 -->
    <view class="trend-section">
      <view class="section-header">
        <uni-icons type="trending-up" size="20" color="#1890FF" />
        <text class="section-title">趋势分析</text>
      </view>
      
      <view class="trend-content">
        <!-- 趋势图表 -->
        <view class="chart-container">
          <view class="chart-placeholder">
            <uni-icons type="trending-up" size="60" color="#D9D9D9" />
            <text class="chart-text">收支趋势图</text>
          </view>
        </view>
        
        <!-- 趋势说明 -->
        <view class="trend-summary">
          <view class="summary-item">
            <text class="summary-label">收入同比</text>
            <text class="summary-value positive">+{{ reportData.incomeGrowth }}%</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">支出同比</text>
            <text class="summary-value" :class="{ positive: reportData.expenseGrowth > 0, negative: reportData.expenseGrowth < 0 }">
              {{ reportData.expenseGrowth > 0 ? '+' : '' }}{{ reportData.expenseGrowth }}%
            </text>
          </view>
          <view class="summary-item">
            <text class="summary-label">结余同比</text>
            <text class="summary-value" :class="{ positive: reportData.profitGrowth > 0, negative: reportData.profitGrowth < 0 }">
              {{ reportData.profitGrowth > 0 ? '+' : '' }}{{ reportData.profitGrowth }}%
            </text>
          </view>
        </view>
      </view>
    </view>

    <!-- 审计信息 -->
    <view class="audit-section">
      <view class="section-header">
        <uni-icons type="checkmarkempty" size="20" color="#FA8C16" />
        <text class="section-title">审计信息</text>
      </view>
      
      <view class="audit-content">
        <view class="audit-item">
          <text class="audit-label">审计状态</text>
          <view class="audit-status approved">
            <uni-icons type="checkmarkempty" size="16" color="#52C41A" />
            <text>已审计</text>
          </view>
        </view>
        <view class="audit-item">
          <text class="audit-label">审计机构</text>
          <text class="audit-value">中诚会计师事务所</text>
        </view>
        <view class="audit-item">
          <text class="audit-label">审计时间</text>
          <text class="audit-value">2024-01-20</text>
        </view>
        <view class="audit-item">
          <text class="audit-label">审计意见</text>
          <text class="audit-value">财务报表在所有重大方面公允反映了财务状况</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button class="action-btn print" @click="printReport">
        <uni-icons type="printer" size="18" color="#1890FF" />
        <text>打印报表</text>
      </button>
      <button class="action-btn share" @click="shareReport">
        <uni-icons type="redo" size="18" color="#52C41A" />
        <text>分享报表</text>
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeReport: 'monthly',
      selectedTime: '2024年1月',
      reportData: {
        totalIncome: 0,
        totalExpense: 0,
        netProfit: 0,
        incomeGrowth: 0,
        expenseGrowth: 0,
        profitGrowth: 0,
        incomeBreakdown: [],
        expenseBreakdown: []
      }
    }
  },
  onLoad() {
    this.loadReportData()
  },
  methods: {
    loadReportData() {
      // 模拟报表数据
      this.reportData = {
        totalIncome: 125000.00,
        totalExpense: 89000.00,
        netProfit: 36000.00,
        incomeGrowth: 8.5,
        expenseGrowth: 5.2,
        profitGrowth: 15.3,
        incomeBreakdown: [
          {
            category: 'property',
            name: '物业服务费',
            amount: 85000.00,
            percentage: 68,
            color: '#1890FF'
          },
          {
            category: 'parking',
            name: '停车费收入',
            amount: 25000.00,
            percentage: 20,
            color: '#52C41A'
          },
          {
            category: 'advertising',
            name: '广告位收入',
            amount: 10000.00,
            percentage: 8,
            color: '#FA8C16'
          },
          {
            category: 'other',
            name: '其他收入',
            amount: 5000.00,
            percentage: 4,
            color: '#722ED1'
          }
        ],
        expenseBreakdown: [
          {
            category: 'staff',
            name: '人员工资',
            amount: 40000.00,
            percentage: 45,
            color: '#FF4D4F'
          },
          {
            category: 'maintenance',
            name: '维修保养',
            amount: 22000.00,
            percentage: 25,
            color: '#FA8C16'
          },
          {
            category: 'utilities',
            name: '公共事业',
            amount: 18000.00,
            percentage: 20,
            color: '#1890FF'
          },
          {
            category: 'other',
            name: '其他支出',
            amount: 9000.00,
            percentage: 10,
            color: '#8C8C8C'
          }
        ]
      }
    },
    
    switchReport(type) {
      this.activeReport = type
      this.updateSelectedTime()
      this.loadReportData()
    },
    
    updateSelectedTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = now.getMonth() + 1
      const quarter = Math.ceil(month / 3)
      
      switch (this.activeReport) {
        case 'monthly':
          this.selectedTime = `${year}年${month}月`
          break
        case 'quarterly':
          this.selectedTime = `${year}年第${quarter}季度`
          break
        case 'annual':
          this.selectedTime = `${year}年`
          break
      }
    },
    
    getReportTitle() {
      const titleMap = {
        'monthly': '月度财务报表',
        'quarterly': '季度财务报表',
        'annual': '年度财务报表'
      }
      return titleMap[this.activeReport] || '财务报表'
    },
    
    formatAmount(amount) {
      return new Intl.NumberFormat('zh-CN').format(amount)
    },
    
    showTimePicker() {
      uni.showToast({
        title: '时间选择功能开发中',
        icon: 'none'
      })
    },
    
    exportReport() {
      uni.showLoading({
        title: '导出中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '导出成功',
          icon: 'success'
        })
      }, 2000)
    },
    
    printReport() {
      uni.showToast({
        title: '打印功能开发中',
        icon: 'none'
      })
    },
    
    shareReport() {
      uni.showToast({
        title: '分享功能开发中',
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

.report-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 40rpx;
}

.report-tabs {
  display: flex;
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 10rpx;
  border: 1rpx solid #F0F0F0;
  
  .tab-item {
    flex: 1;
    text-align: center;
    padding: 20rpx 0;
    font-size: 26rpx;
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

.time-selector {
  display: flex;
  align-items: center;
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 30rpx;
  border: 1rpx solid #F0F0F0;
  
  .selector-item {
    flex: 1;
    display: flex;
    align-items: center;
    
    text {
      margin: 0 16rpx;
      font-size: 28rpx;
      color: #262626;
      font-weight: 500;
    }
  }
  
  .export-btn {
    display: flex;
    align-items: center;
    padding: 16rpx 24rpx;
    background: #E6F7FF;
    color: #1890FF;
    border-radius: 20rpx;
    font-size: 24rpx;
    border: none;
    
    text {
      margin-left: 8rpx;
    }
    
    &:active {
      background: #BAE7FF;
    }
  }
}

.report-overview {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .overview-title {
    text-align: center;
    margin-bottom: 30rpx;
    
    text {
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .overview-cards {
    display: flex;
    gap: 20rpx;
    
    .overview-card {
      flex: 1;
      display: flex;
      align-items: center;
      padding: 30rpx 20rpx;
      border-radius: 20rpx;
      
      &.income {
        background: #F6FFED;
      }
      
      &.expense {
        background: #FFF2F0;
      }
      
      &.balance {
        background: #E6F7FF;
      }
      
      .card-icon {
        width: 60rpx;
        height: 60rpx;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.8);
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20rpx;
      }
      
      .card-content {
        flex: 1;
        
        .card-label {
          display: block;
          font-size: 22rpx;
          color: #8C8C8C;
          margin-bottom: 8rpx;
        }
        
        .card-amount {
          font-size: 28rpx;
          font-weight: 600;
          color: #262626;
          
          &.negative {
            color: #FF4D4F;
          }
        }
      }
    }
  }
}

.analysis-section, .trend-section, .audit-section {
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

.analysis-content, .trend-content {
  .chart-container {
    margin-bottom: 30rpx;
    
    .chart-placeholder {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 300rpx;
      background: #F8F9FA;
      border-radius: 16rpx;
      
      .chart-text {
        margin-top: 16rpx;
        font-size: 24rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .income-details, .expense-details {
    .detail-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #F0F0F0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .item-info {
        display: flex;
        align-items: center;
        
        .item-dot {
          width: 16rpx;
          height: 16rpx;
          border-radius: 50%;
          margin-right: 16rpx;
        }
        
        .item-label {
          font-size: 26rpx;
          color: #262626;
        }
      }
      
      .item-values {
        display: flex;
        align-items: center;
        gap: 16rpx;
        
        .item-amount {
          font-size: 26rpx;
          font-weight: 600;
          color: #262626;
        }
        
        .item-percent {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
    }
  }
}

.trend-summary {
  display: flex;
  justify-content: space-around;
  
  .summary-item {
    text-align: center;
    
    .summary-label {
      display: block;
      font-size: 22rpx;
      color: #8C8C8C;
      margin-bottom: 12rpx;
    }
    
    .summary-value {
      font-size: 28rpx;
      font-weight: 600;
      
      &.positive {
        color: #52C41A;
      }
      
      &.negative {
        color: #FF4D4F;
      }
    }
  }
}

.audit-content {
  .audit-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #F0F0F0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .audit-label {
      font-size: 26rpx;
      color: #8C8C8C;
      min-width: 160rpx;
    }
    
    .audit-value {
      flex: 1;
      text-align: right;
      font-size: 26rpx;
      color: #262626;
      line-height: 1.4;
    }
    
    .audit-status {
      display: flex;
      align-items: center;
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      font-size: 22rpx;
      
      &.approved {
        background: #F6FFED;
        color: #52C41A;
      }
      
      text {
        margin-left: 8rpx;
      }
    }
  }
}

.action-buttons {
  display: flex;
  gap: 20rpx;
  padding: 0 20rpx;
  
  .action-btn {
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
    
    &.print {
      background: #E6F7FF;
      color: #1890FF;
    }
    
    &.share {
      background: #F6FFED;
      color: #52C41A;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}
</style>
 
 