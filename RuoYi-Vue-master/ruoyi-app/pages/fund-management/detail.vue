<template>
  <view class="detail-container">
    <!-- 筛选条件 -->
    <view class="filter-section">
      <view class="filter-tabs">
        <view 
          class="tab-item" 
          :class="{ active: activeType === 'all' }"
          @click="switchType('all')"
        >
          全部
        </view>
        <view 
          class="tab-item" 
          :class="{ active: activeType === 'income' }"
          @click="switchType('income')"
        >
          收入
        </view>
        <view 
          class="tab-item" 
          :class="{ active: activeType === 'expense' }"
          @click="switchType('expense')"
        >
          支出
        </view>
      </view>
      
      <view class="filter-options">
        <view class="date-filter" @click="showDatePicker">
          <uni-icons type="calendar" size="16" color="#1890FF" />
          <text>{{ selectedDate }}</text>
          <uni-icons type="arrowdown" size="14" color="#8C8C8C" />
        </view>
        <view class="category-filter" @click="showCategoryPicker">
          <uni-icons type="list" size="16" color="#1890FF" />
          <text>{{ selectedCategory }}</text>
          <uni-icons type="arrowdown" size="14" color="#8C8C8C" />
        </view>
      </view>
    </view>

    <!-- 统计汇总 -->
    <view class="summary-section">
      <view class="summary-item income">
        <text class="summary-label">收入总计</text>
        <text class="summary-amount">¥{{ formatAmount(summaryData.totalIncome) }}</text>
      </view>
      <view class="summary-item expense">
        <text class="summary-label">支出总计</text>
        <text class="summary-amount">¥{{ formatAmount(summaryData.totalExpense) }}</text>
      </view>
      <view class="summary-item balance">
        <text class="summary-label">结余</text>
        <text class="summary-amount" :class="{ negative: summaryData.balance < 0 }">
          ¥{{ formatAmount(Math.abs(summaryData.balance)) }}
        </text>
      </view>
    </view>

    <!-- 交易列表 -->
    <view class="transaction-list">
      <!-- 日期分组 -->
      <view 
        class="date-group" 
        v-for="group in groupedTransactions" 
        :key="group.date"
      >
        <view class="date-header">
          <text class="date-text">{{ group.date }}</text>
          <text class="date-summary">{{ group.count }}笔 • {{ group.netAmount >= 0 ? '+' : '-' }}¥{{ formatAmount(Math.abs(group.netAmount)) }}</text>
        </view>
        
        <view class="transaction-items">
          <view 
            class="transaction-item" 
            v-for="transaction in group.transactions" 
            :key="transaction.flowId"
            @click="viewTransaction(transaction)"
          >
            <view class="transaction-icon" :class="[(transaction.flowType === '1' || transaction.flowType === '3') ? 'income' : 'expense']">
              <uni-icons :type="getTransactionIcon(transaction.flowType)" size="18" color="#FFFFFF" />
            </view>
            
            <view class="transaction-info">
              <text class="transaction-title">{{ transaction.title }}</text>
              <view class="transaction-meta">
                <text class="transaction-category">{{ getCategoryText(transaction.category) }}</text>
                <text class="transaction-time">{{ transaction.createTime }}</text>
              </view>
              <text class="transaction-desc" v-if="transaction.description">{{ transaction.description }}</text>
            </view>
            
            <view class="transaction-right">
              <text class="transaction-amount" :class="[(transaction.flowType === '1' || transaction.flowType === '3') ? 'positive' : 'negative']">
                {{ getAmountPrefix(transaction.flowType) }}¥{{ formatAmount(transaction.amount) }}
              </text>
              <view class="transaction-status" :class="[transaction.approvalStatus === '0' ? 'status-pending' : transaction.approvalStatus === '1' ? 'status-approved' : transaction.approvalStatus === '2' ? 'status-rejected' : 'status-default']">
                {{ getStatusText(transaction.approvalStatus) }}
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="groupedTransactions.length === 0">
      <uni-icons type="wallet" size="80" color="#D9D9D9" />
      <text class="empty-text">暂无交易记录</text>
    </view>

    <!-- 日期选择器 -->
    <uni-popup ref="datePicker" type="bottom">
      <view class="picker-container">
        <view class="picker-header">
          <text class="picker-cancel" @click="closeDatePicker">取消</text>
          <text class="picker-title">选择日期</text>
          <text class="picker-confirm" @click="confirmDatePicker">确定</text>
        </view>
        <picker-view class="picker-view" :value="datePickerValue" @change="onDatePickerChange">
          <picker-view-column>
            <view v-for="(year, index) in years" :key="index" class="picker-item">{{ year }}年</view>
          </picker-view-column>
          <picker-view-column>
            <view v-for="(month, index) in months" :key="index" class="picker-item">{{ month }}月</view>
          </picker-view-column>
        </picker-view>
      </view>
    </uni-popup>

    <!-- 分类选择器 -->
    <uni-popup ref="categoryPicker" type="bottom">
      <view class="picker-container">
        <view class="picker-header">
          <text class="picker-cancel" @click="closeCategoryPicker">取消</text>
          <text class="picker-title">选择分类</text>
          <text class="picker-confirm" @click="confirmCategoryPicker">确定</text>
        </view>
        <view class="category-list">
          <view 
            class="category-item" 
            :class="{ active: tempCategory === category.value }"
            v-for="category in categories" 
            :key="category.value"
            @click="selectCategory(category.value)"
          >
            <text>{{ category.label }}</text>
            <uni-icons v-if="tempCategory === category.value" type="checkmarkempty" size="18" color="#1890FF" />
          </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeType: 'all',
      selectedDate: '2024年1月',
      selectedCategory: '全部分类',
      tempCategory: 'all',
      datePickerValue: [0, 0],
      years: [],
      months: [],
      categories: [
        { value: 'all', label: '全部分类' },
        { value: 'property', label: '物业服务费' },
        { value: 'maintenance', label: '维修保养' },
        { value: 'utilities', label: '公共事业' },
        { value: 'security', label: '安保服务' },
        { value: 'cleaning', label: '保洁服务' },
        { value: 'greening', label: '绿化养护' },
        { value: 'parking', label: '停车管理' },
        { value: 'other', label: '其他' }
      ],
      transactionList: [],
      summaryData: {
        totalIncome: 0,
        totalExpense: 0,
        balance: 0
      }
    }
  },
  computed: {
    groupedTransactions() {
      // 按日期分组交易记录
      const groups = {}
      
      this.transactionList.forEach(transaction => {
        const date = transaction.flowDate
        if (!groups[date]) {
          groups[date] = {
            date,
            transactions: [],
            count: 0,
            netAmount: 0
          }
        }
        groups[date].transactions.push(transaction)
        groups[date].count++
        
        const amount = transaction.flowType === '1' || transaction.flowType === '3' 
          ? transaction.amount 
          : -transaction.amount
        groups[date].netAmount += amount
      })
      
      // 转换为数组并按日期排序
      return Object.values(groups).sort((a, b) => new Date(b.date) - new Date(a.date))
    }
  },
  onLoad(options) {
    if (options.type) {
      this.activeType = options.type
    }
    this.initDatePicker()
    this.loadTransactions()
    this.calculateSummary()
  },
  onPullDownRefresh() {
    this.loadTransactions()
    this.calculateSummary()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    initDatePicker() {
      const currentYear = new Date().getFullYear()
      this.years = Array.from({ length: 5 }, (_, i) => currentYear - i)
      this.months = Array.from({ length: 12 }, (_, i) => i + 1)
    },
    
    loadTransactions() {
      // 模拟交易数据
      const mockData = [
        {
          flowId: 1,
          flowNo: 'FL202401001',
          flowType: '1',
          title: '物业服务费收入',
          amount: 35000.00,
          category: 'property',
          description: '1月份物业服务费收缴',
          flowDate: '2024-01-15',
          createTime: '09:30',
          approvalStatus: '1',
          handler: '财务部'
        },
        {
          flowId: 2,
          flowNo: 'FL202401002',
          flowType: '2',
          title: '电梯维保费用',
          amount: 8000.00,
          category: 'maintenance',
          description: '电梯月度维保费用支出',
          flowDate: '2024-01-15',
          createTime: '14:20',
          approvalStatus: '1',
          handler: '维修部'
        },
        {
          flowId: 3,
          flowNo: 'FL202401003',
          flowType: '1',
          title: '停车位租赁收入',
          amount: 12000.00,
          category: 'parking',
          description: '地下停车位月租收入',
          flowDate: '2024-01-14',
          createTime: '16:45',
          approvalStatus: '1',
          handler: '管理处'
        },
        {
          flowId: 4,
          flowNo: 'FL202401004',
          flowType: '2',
          title: '绿化养护费用',
          amount: 5000.00,
          category: 'greening',
          description: '小区绿化维护费用',
          flowDate: '2024-01-14',
          createTime: '10:15',
          approvalStatus: '1',
          handler: '绿化部'
        },
        {
          flowId: 5,
          flowNo: 'FL202401005',
          flowType: '3',
          title: '维修资金利息',
          amount: 3500.00,
          category: 'other',
          description: '维修资金银行存款利息',
          flowDate: '2024-01-13',
          createTime: '11:30',
          approvalStatus: '1',
          handler: '财务部'
        },
        {
          flowId: 6,
          flowNo: 'FL202401006',
          flowType: '2',
          title: '公共区域电费',
          amount: 6800.00,
          category: 'utilities',
          description: '小区公共区域用电费用',
          flowDate: '2024-01-13',
          createTime: '15:20',
          approvalStatus: '1',
          handler: '管理处'
        }
      ]
      
      // 根据类型筛选
      if (this.activeType === 'income') {
        this.transactionList = mockData.filter(t => t.flowType === '1' || t.flowType === '3')
      } else if (this.activeType === 'expense') {
        this.transactionList = mockData.filter(t => t.flowType === '2' || t.flowType === '4')
      } else {
        this.transactionList = mockData
      }
    },
    
    calculateSummary() {
      let totalIncome = 0
      let totalExpense = 0
      
      this.transactionList.forEach(transaction => {
        if (transaction.flowType === '1' || transaction.flowType === '3') {
          totalIncome += transaction.amount
        } else {
          totalExpense += transaction.amount
        }
      })
      
      this.summaryData = {
        totalIncome,
        totalExpense,
        balance: totalIncome - totalExpense
      }
    },
    
    switchType(type) {
      this.activeType = type
      this.loadTransactions()
      this.calculateSummary()
    },
    
    formatAmount(amount) {
      return new Intl.NumberFormat('zh-CN').format(amount)
    },
    
    getTransactionClass(flowType) {
      return (flowType === '1' || flowType === '3') ? 'income' : 'expense'
    },
    
    getTransactionIcon(flowType) {
      return (flowType === '1' || flowType === '3') ? 'plus' : 'minus'
    },
    
    getAmountClass(flowType) {
      return (flowType === '1' || flowType === '3') ? 'positive' : 'negative'
    },
    
    getAmountPrefix(flowType) {
      return (flowType === '1' || flowType === '3') ? '+' : '-'
    },
    
    getCategoryText(category) {
      const categoryItem = this.categories.find(c => c.value === category)
      return categoryItem ? categoryItem.label : '未知分类'
    },
    
    getStatusClass(status) {
      const classMap = {
        '0': 'status-pending',
        '1': 'status-approved',
        '2': 'status-rejected'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        '0': '待审批',
        '1': '已审批',
        '2': '已拒绝'
      }
      return textMap[status] || '未知'
    },
    
    showDatePicker() {
      this.$refs.datePicker.open()
    },
    
    closeDatePicker() {
      this.$refs.datePicker.close()
    },
    
    confirmDatePicker() {
      const year = this.years[this.datePickerValue[0]]
      const month = this.months[this.datePickerValue[1]]
      this.selectedDate = `${year}年${month}月`
      this.closeDatePicker()
      this.loadTransactions()
    },
    
    onDatePickerChange(e) {
      this.datePickerValue = e.detail.value
    },
    
    showCategoryPicker() {
      this.tempCategory = this.categories.find(c => c.label === this.selectedCategory)?.value || 'all'
      this.$refs.categoryPicker.open()
    },
    
    closeCategoryPicker() {
      this.$refs.categoryPicker.close()
    },
    
    confirmCategoryPicker() {
      const category = this.categories.find(c => c.value === this.tempCategory)
      if (category) {
        this.selectedCategory = category.label
      }
      this.closeCategoryPicker()
      this.loadTransactions()
    },
    
    selectCategory(value) {
      this.tempCategory = value
    },
    
    viewTransaction(transaction) {
      uni.navigateTo({
        url: `/pages/fund-management/transaction?id=${transaction.flowId}`
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
}

.filter-section {
  background: #FFFFFF;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
  
  .filter-tabs {
    display: flex;
    background: #F8F9FA;
    border-radius: 20rpx;
    padding: 8rpx;
    margin-bottom: 30rpx;
    
    .tab-item {
      flex: 1;
      text-align: center;
      padding: 16rpx 0;
      font-size: 26rpx;
      color: #8C8C8C;
      border-radius: 16rpx;
      transition: all 0.3s ease;
      
      &.active {
        background: #1890FF;
        color: #FFFFFF;
        font-weight: 600;
      }
    }
  }
  
  .filter-options {
    display: flex;
    gap: 20rpx;
    
    .date-filter, .category-filter {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 20rpx;
      background: #F8F9FA;
      border-radius: 16rpx;
      border: 1rpx solid #F0F0F0;
      
      text {
        margin: 0 12rpx;
        font-size: 24rpx;
        color: #262626;
      }
    }
  }
}

.summary-section {
  display: flex;
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 30rpx 20rpx;
  border: 1rpx solid #F0F0F0;
  
  .summary-item {
    flex: 1;
    text-align: center;
    
    .summary-label {
      display: block;
      font-size: 22rpx;
      color: #8C8C8C;
      margin-bottom: 12rpx;
    }
    
    .summary-amount {
      font-size: 28rpx;
      font-weight: 600;
      
      &.negative {
        color: #FF4D4F;
      }
    }
    
    &.income .summary-amount {
      color: #52C41A;
    }
    
    &.expense .summary-amount {
      color: #FF4D4F;
    }
    
    &.balance .summary-amount {
      color: #1890FF;
    }
  }
}

.transaction-list {
  padding: 0 20rpx;
}

.date-group {
  margin-bottom: 30rpx;
  
  .date-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 30rpx;
    background: #F8F9FA;
    border-radius: 16rpx 16rpx 0 0;
    
    .date-text {
      font-size: 26rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .date-summary {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
  
  .transaction-items {
    background: #FFFFFF;
    border-radius: 0 0 16rpx 16rpx;
    border: 1rpx solid #F0F0F0;
    border-top: none;
    
    .transaction-item {
      display: flex;
      align-items: flex-start;
      padding: 30rpx;
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
        flex-shrink: 0;
        
        &.income {
          background: #52C41A;
        }
        
        &.expense {
          background: #FF4D4F;
        }
      }
      
      .transaction-info {
        flex: 1;
        margin-right: 20rpx;
        
        .transaction-title {
          display: block;
          font-size: 28rpx;
          font-weight: 600;
          color: #262626;
          margin-bottom: 12rpx;
        }
        
        .transaction-meta {
          display: flex;
          align-items: center;
          margin-bottom: 8rpx;
          
          .transaction-category {
            font-size: 22rpx;
            color: #1890FF;
            margin-right: 16rpx;
          }
          
          .transaction-time {
            font-size: 22rpx;
            color: #8C8C8C;
          }
        }
        
        .transaction-desc {
          font-size: 24rpx;
          color: #595959;
          line-height: 1.4;
        }
      }
      
      .transaction-right {
        display: flex;
        flex-direction: column;
        align-items: flex-end;
        
        .transaction-amount {
          font-size: 26rpx;
          font-weight: 600;
          margin-bottom: 8rpx;
          
          &.positive {
            color: #52C41A;
          }
          
          &.negative {
            color: #FF4D4F;
          }
        }
        
        .transaction-status {
          padding: 4rpx 12rpx;
          border-radius: 12rpx;
          font-size: 20rpx;
          
          &.status-pending {
            background: #FFF2E8;
            color: #FA8C16;
          }
          
          &.status-approved {
            background: #F6FFED;
            color: #52C41A;
          }
          
          &.status-rejected {
            background: #FFF2F0;
            color: #FF4D4F;
          }
        }
      }
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

.picker-container {
  background: #FFFFFF;
  border-radius: 24rpx 24rpx 0 0;
  
  .picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #F0F0F0;
    
    .picker-cancel, .picker-confirm {
      font-size: 28rpx;
      color: #1890FF;
    }
    
    .picker-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .picker-view {
    height: 400rpx;
    
    .picker-item {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 80rpx;
      font-size: 28rpx;
      color: #262626;
    }
  }
  
  .category-list {
    max-height: 600rpx;
    
    .category-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 30rpx;
      border-bottom: 1rpx solid #F0F0F0;
      
      &:last-child {
        border-bottom: none;
      }
      
      &.active {
        background: #F0F8FF;
        color: #1890FF;
      }
      
      text {
        font-size: 28rpx;
      }
    }
  }
}
</style>
 
 