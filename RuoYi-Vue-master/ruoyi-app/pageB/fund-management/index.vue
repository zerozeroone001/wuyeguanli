<template>
  <view class="fund-container">
    <!-- 资金概览 -->
    <view class="overview-section">
      <view class="overview-header">
        <text class="overview-title">资金概览</text>
        <text class="update-time">更新时间：{{ updateTime }}</text>
      </view>
      
      <view class="balance-cards">
        <view class="balance-card maintenance">
          <view class="card-header">
            <uni-icons type="wallet" size="24" color="#1890FF" />
            <text class="card-title">维修资金</text>
          </view>
          <text class="balance-amount">¥{{ formatAmount(balanceInfo.maintenanceFund) }}</text>
          <view class="balance-change">
            <uni-icons type="arrowup" size="14" color="#52C41A" />
            <text class="change-text">本月 +¥{{ formatAmount(balanceInfo.maintenanceIncrease) }}</text>
          </view>
        </view>
        
        <view class="balance-card operating">
          <view class="card-header">
            <uni-icons type="shop" size="24" color="#52C41A" />
            <text class="card-title">经营性收益</text>
          </view>
          <text class="balance-amount">¥{{ formatAmount(balanceInfo.operatingIncome) }}</text>
          <view class="balance-change">
            <uni-icons type="arrowup" size="14" color="#52C41A" />
            <text class="change-text">本月 +¥{{ formatAmount(balanceInfo.operatingIncrease) }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 功能导航 -->
    <view class="function-nav">
      <view class="nav-item" @click="goToDetail('income')">
        <view class="nav-icon income">
          <uni-icons type="plus" size="20" color="#52C41A" />
        </view>
        <text class="nav-text">收入明细</text>
      </view>
      <view class="nav-item" @click="goToDetail('expense')">
        <view class="nav-icon expense">
          <uni-icons type="minus" size="20" color="#FF4D4F" />
        </view>
        <text class="nav-text">支出明细</text>
      </view>
      <view class="nav-item" @click="goToReport()">
        <view class="nav-icon report">
          <uni-icons type="bar-chart" size="20" color="#1890FF" />
        </view>
        <text class="nav-text">财务报表</text>
      </view>
      <view class="nav-item" @click="goToAudit()">
        <view class="nav-icon audit">
          <uni-icons type="checkmarkempty" size="20" color="#FA8C16" />
        </view>
        <text class="nav-text">审计记录</text>
      </view>
      <view class="nav-item" v-if="isCommittee" @click="openExpenseForm">
        <view class="nav-icon expense">
          <uni-icons type="compose" size="20" color="#FF4D4F" />
        </view>
        <text class="nav-text">支出申请</text>
      </view>
    </view>

    <!-- 本月收支统计 -->
    <view class="monthly-stats">
      <view class="stats-header">
        <uni-icons type="calendar" size="20" color="#262626" />
        <text class="stats-title">{{ currentMonth }}月收支统计</text>
      </view>
      
      <view class="stats-content">
        <view class="stats-row">
          <view class="stats-item income">
            <text class="stats-label">本月收入</text>
            <text class="stats-value">¥{{ formatAmount(monthlyStats.income) }}</text>
          </view>
          <view class="stats-item expense">
            <text class="stats-label">本月支出</text>
            <text class="stats-value">¥{{ formatAmount(monthlyStats.expense) }}</text>
          </view>
        </view>
        <view class="stats-row">
          <view class="stats-item balance">
            <text class="stats-label">本月结余</text>
            <text class="stats-value" :class="{ negative: monthlyStats.balance < 0 }">
              ¥{{ formatAmount(Math.abs(monthlyStats.balance)) }}
            </text>
          </view>
          <view class="stats-item count">
            <text class="stats-label">交易笔数</text>
            <text class="stats-value">{{ monthlyStats.transactionCount }}笔</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 最近交易记录 -->
    <view class="recent-transactions">
      <view class="section-header">
        <text class="section-title">最近交易</text>
        <text class="more-link" @click="goToDetail('all')">查看全部 ></text>
      </view>
      
      <view class="transaction-list">
        <view 
          class="transaction-item" 
          v-for="transaction in recentTransactions" 
          :key="transaction.flowId"
          @click="viewTransaction(transaction)"
        >
          <view class="transaction-icon" :class="[(transaction.flowType === '1' || transaction.flowType === '3') ? 'income' : 'expense']">
            <uni-icons :type="getTransactionIcon(transaction.flowType)" size="18" color="#FFFFFF" />
          </view>
          <view class="transaction-info">
            <text class="transaction-title">{{ transaction.title }}</text>
            <text class="transaction-time">{{ transaction.flowDate }}</text>
          </view>
          <view class="transaction-amount" :class="[(transaction.flowType === '1' || transaction.flowType === '3') ? 'positive' : 'negative']">
            <text>{{ getAmountPrefix(transaction.flowType) }}¥{{ formatAmount(transaction.amount) }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 资金使用分析 -->
    <view class="usage-analysis">
      <view class="analysis-header">
        <uni-icons type="pie-chart" size="20" color="#262626" />
        <text class="analysis-title">资金使用分析</text>
      </view>
      
      <view class="analysis-content">
        <view class="chart-placeholder">
          <uni-icons type="bar-chart" size="60" color="#D9D9D9" />
          <text class="chart-text">图表功能开发中</text>
        </view>
        
        <view class="analysis-summary">
          <view class="summary-item">
            <text class="summary-label">物业服务费</text>
            <text class="summary-percent">45%</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">设备维护</text>
            <text class="summary-percent">25%</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">公共事业</text>
            <text class="summary-percent">20%</text>
          </view>
          <view class="summary-item">
            <text class="summary-label">其他支出</text>
            <text class="summary-percent">10%</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 公示说明 -->
    <view class="disclosure-notice">
      <view class="notice-header">
        <uni-icons type="info" size="18" color="#1890FF" />
        <text class="notice-title">公示说明</text>
      </view>
      <view class="notice-content">
        <text>• 资金数据每月定期公示，确保财务透明</text>
        <text>• 所有收支均经业委会审核确认</text>
        <text>• 如有疑问可联系财务负责人或业委会</text>
        <text>• 详细账目可申请查阅纸质版财务报表</text>
      </view>
    </view>

    <!-- 支出申请表单弹窗（仅业委会） -->
    <uni-popup ref="expensePopup" type="center">
      <view class="expense-form-card">
        <view class="expense-form-header">支出申请</view>
        <uni-forms ref="expenseFormRef" :modelValue="expenseForm" :rules="expenseRules" label-position="top">
          <uni-forms-item label="支出类型" name="flowType">
            <uni-data-select :localdata="expenseTypes" v-model="expenseForm.flowType" placeholder="请选择支出类型" />
          </uni-forms-item>
          <uni-forms-item label="金额(元)" name="amount">
            <uni-easyinput type="number" v-model="expenseForm.amount" placeholder="请输入金额" />
          </uni-forms-item>
          <uni-forms-item label="标题" name="title">
            <uni-easyinput v-model="expenseForm.title" placeholder="请输入标题" />
          </uni-forms-item>
          <uni-forms-item label="用途说明" name="description">
            <uni-easyinput type="textarea" v-model="expenseForm.description" placeholder="请填写用途说明" />
          </uni-forms-item>
          <uni-forms-item label="流水日期" name="flowDate">
            <uni-datetime-picker type="date" v-model="expenseForm.flowDate" />
          </uni-forms-item>
        </uni-forms>
        <view class="expense-form-actions">
          <button class="btn-cancel" @click="closeExpenseForm">取消</button>
          <button class="btn-submit" type="primary" @click="submitExpense">提交</button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import { getFundFlowList, getFundOverview, getMonthlyStats, createFundFlow } from '@/api/fund'

export default {
  data() {
    return {
      updateTime: '',
      currentMonth: '',
      balanceInfo: {
        maintenanceFund: 0,
        operatingIncome: 0,
        maintenanceIncrease: 0,
        operatingIncrease: 0
      },
      monthlyStats: {
        income: 0,
        expense: 0,
        balance: 0,
        transactionCount: 0
      },
      recentTransactions: [],
      expenseForm: {
        flowType: '',
        amount: '',
        title: '',
        description: '',
        flowDate: ''
      },
      expenseTypes: [
        { text: '经营性支出', value: '2' },
        { text: '维修资金支出', value: '4' }
      ],
      expenseRules: {
        flowType: { required: true, errorMessage: '请选择支出类型' },
        amount: {
          required: true,
          errorMessage: '请输入正确金额'
        },
        title: { required: true, errorMessage: '请输入标题' },
        flowDate: { required: true, errorMessage: '请选择日期' }
      }
    }
  },
  computed: {
    ownerProfile() {
      return this.$store.state.user.ownerProfile || {}
    },
    // 身份判断：业委会成员
    isCommittee() {
      // 需求：身份判断为 this.ownerProfile.isOwner
      // 业委会成员标识通常为2，这里按照约定：isOwner==2 视为业委会
      return this.ownerProfile && Number(this.ownerProfile.isOwner) === 2
    }
  },
  onLoad() {
    this.loadFundOverview()
    this.loadMonthlyStats()
    this.loadRecentTransactions()
    this.initDateTime()
    // 确保拉取身份信息
    if (!this.ownerProfile || !this.ownerProfile.isOwner) {
      this.$store.dispatch('GetProfileInfo').catch(() => {})
    }
  },
  onPullDownRefresh() {
    this.loadFundOverview()
    this.loadMonthlyStats()
    this.loadRecentTransactions()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    initDateTime() {
      const now = new Date()
      this.currentMonth = now.getMonth() + 1
      this.updateTime = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
    },
    
    loadFundOverview() {
      getFundOverview().then(res => {
        this.balanceInfo = res.data
      })
    },
    
    loadMonthlyStats() {
      getMonthlyStats().then(res => {
        this.monthlyStats = res.data
      })
    },
    
    loadRecentTransactions() {
      getFundFlowList({ pageNum: 1, pageSize: 5 }).then(res => {
        this.recentTransactions = res.rows
      })
    },
    
    formatAmount(amount) {
      return new Intl.NumberFormat('zh-CN').format(amount)
    },
    
    getTransactionClass(flowType) {
      const classMap = {
        '1': 'income',
        '2': 'expense',
        '3': 'income',
        '4': 'expense'
      }
      return classMap[flowType] || 'default'
    },
    
    getTransactionIcon(flowType) {
      const iconMap = {
        '1': 'plus',
        '2': 'minus',
        '3': 'plus',
        '4': 'minus'
      }
      return iconMap[flowType] || 'circle'
    },
    
    getAmountClass(flowType) {
      return flowType === '1' || flowType === '3' ? 'positive' : 'negative'
    },
    
    getAmountPrefix(flowType) {
      return flowType === '1' || flowType === '3' ? '+' : '-'
    },
    
    goToDetail(type) {
      uni.navigateTo({
        url: `/pageB/fund-management/detail?type=${type}`
      })
    },
    
    goToReport() {
      uni.navigateTo({
        url: '/pageB/fund-management/report'
      })
    },
    
    goToAudit() {
      uni.showToast({
        title: '审计记录功能开发中',
        icon: 'none'
      })
    },
    
    viewTransaction(transaction) {
      uni.navigateTo({
        url: `/pageB/fund-management/transaction?id=${transaction.flowId}`
      })
    },

    openExpenseForm() {
      // 默认日期为今天
      if (!this.expenseForm.flowDate) {
        const now = new Date()
        const yyyy = now.getFullYear()
        const mm = String(now.getMonth() + 1).padStart(2, '0')
        const dd = String(now.getDate()).padStart(2, '0')
        this.expenseForm.flowDate = `${yyyy}-${mm}-${dd}`
      }
      this.$refs.expensePopup.open()
    },
    closeExpenseForm() {
      this.$refs.expensePopup.close()
    },
    async submitExpense() {
      try {
        await this.$refs.expenseFormRef.validate()
        const payload = {
          flowType: this.expenseForm.flowType,
          amount: Number(this.expenseForm.amount),
          title: this.expenseForm.title,
          description: this.expenseForm.description,
          flowDate: this.expenseForm.flowDate
        }
        await createFundFlow(payload)
        uni.showToast({ title: '提交成功', icon: 'success' })
        this.closeExpenseForm()
        // 重置表单
        this.expenseForm = { flowType: '', amount: '', title: '', description: '', flowDate: '' }
        // 刷新数据
        this.loadFundOverview()
        this.loadMonthlyStats()
        this.loadRecentTransactions()
      } catch (e) {
        if (e && e.errMsg) return
        uni.showToast({ title: '提交失败', icon: 'none' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.fund-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.overview-section {
  background: linear-gradient(135deg, #1890FF, #40A9FF);
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  color: #FFFFFF;
  
  .overview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .overview-title {
      font-size: 32rpx;
      font-weight: 600;
    }
    
    .update-time {
      font-size: 22rpx;
      opacity: 0.8;
    }
  }
  
  .balance-cards {
    display: flex;
    gap: 20rpx;
    
    .balance-card {
      flex: 1;
      background: rgba(255, 255, 255, 0.15);
      backdrop-filter: blur(10rpx);
      border-radius: 20rpx;
      padding: 30rpx;
      
      .card-header {
        display: flex;
        align-items: center;
        margin-bottom: 20rpx;
        
        .card-title {
          margin-left: 12rpx;
          font-size: 24rpx;
          opacity: 0.9;
        }
      }
      
      .balance-amount {
        display: block;
        font-size: 32rpx;
        font-weight: 600;
        margin-bottom: 16rpx;
      }
      
      .balance-change {
        display: flex;
        align-items: center;
        
        .change-text {
          margin-left: 8rpx;
          font-size: 20rpx;
          opacity: 0.8;
        }
      }
    }
  }
}

.function-nav {
  display: flex;
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 30rpx 20rpx;
  border: 1rpx solid #F0F0F0;
  
  .nav-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    
    &:active {
      opacity: 0.7;
    }
    
    .nav-icon {
      width: 80rpx;
      height: 80rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16rpx;
      
      &.income {
        background: #52C41A;
      }
      
      &.expense {
        background: #FF4D4F;
      }
      
      &.report {
        background: #1890FF;
      }
      
      &.audit {
        background: #FA8C16;
      }
    }
    
    .nav-text {
      font-size: 24rpx;
      color: #262626;
      font-weight: 500;
    }
  }
}

.monthly-stats {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .stats-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .stats-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .stats-content {
    .stats-row {
      display: flex;
      margin-bottom: 30rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .stats-item {
        flex: 1;
        text-align: center;
        
        .stats-label {
          display: block;
          font-size: 22rpx;
          color: #8C8C8C;
          margin-bottom: 12rpx;
        }
        
        .stats-value {
          font-size: 28rpx;
          font-weight: 600;
          color: #262626;
          
          &.negative {
            color: #FF4D4F;
          }
        }
        
        &.income .stats-value {
          color: #52C41A;
        }
        
        &.expense .stats-value {
          color: #FF4D4F;
        }
        
        &.balance .stats-value {
          color: #1890FF;
        }
      }
    }
  }
}

.recent-transactions {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .section-title {
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .more-link {
      font-size: 24rpx;
      color: #1890FF;
    }
  }
  
  .transaction-list {
    .transaction-item {
      display: flex;
      align-items: center;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #F0F0F0;
      
      &:last-child {
        border-bottom: none;
      }
      
      &:active {
        background: #F8F9FA;
      }
      
      .transaction-icon {
        width: 60rpx;
        height: 60rpx;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20rpx;
        
        &.income {
          background: #52C41A;
        }
        
        &.expense {
          background: #FF4D4F;
        }
      }
      
      .transaction-info {
        flex: 1;
        
        .transaction-title {
          display: block;
          font-size: 26rpx;
          color: #262626;
          font-weight: 500;
          margin-bottom: 8rpx;
        }
        
        .transaction-time {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
      
      .transaction-amount {
        font-size: 26rpx;
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
}

.usage-analysis {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .analysis-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .analysis-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .analysis-content {
    .chart-placeholder {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 200rpx;
      background: #F8F9FA;
      border-radius: 16rpx;
      margin-bottom: 30rpx;
      
      .chart-text {
        margin-top: 16rpx;
        font-size: 24rpx;
        color: #8C8C8C;
      }
    }
    
    .analysis-summary {
      display: flex;
      flex-wrap: wrap;
      gap: 20rpx;
      
      .summary-item {
        flex: 1;
        min-width: 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16rpx 20rpx;
        background: #F8F9FA;
        border-radius: 12rpx;
        
        .summary-label {
          font-size: 22rpx;
          color: #595959;
        }
        
        .summary-percent {
          font-size: 24rpx;
          font-weight: 600;
          color: #1890FF;
        }
      }
    }
  }
}

.disclosure-notice {
  background: #E6F7FF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 30rpx;
  border: 1rpx solid #91D5FF;
  
  .notice-header {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
    
    .notice-title {
      margin-left: 12rpx;
      font-size: 26rpx;
      font-weight: 600;
      color: #1890FF;
    }
  }
  
  .notice-content {
    text {
      display: block;
      font-size: 22rpx;
      color: #1890FF;
      line-height: 1.6;
      margin-bottom: 8rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

.expense-form-card {
  width: 640rpx;
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
}
.expense-form-header {
  font-size: 30rpx;
  font-weight: 600;
  margin-bottom: 20rpx;
  color: #262626;
}
.expense-form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
  margin-top: 10rpx;
}
.btn-cancel {
  background: #F5F5F5;
  color: #595959;
}
.btn-submit {
  background: #FF4D4F;
  color: #FFFFFF;
}
</style>
 
 