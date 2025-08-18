<template>
  <view class="contract-container">
    <!-- 搜索栏 -->
    <view class="search-section">
      <view class="search-box">
        <uni-icons type="search" size="18" color="#8C8C8C" />
        <input 
          class="search-input" 
          placeholder="搜索合同名称或编号" 
          v-model="searchKeyword"
          @input="onSearch"
        />
        <text class="clear-btn" v-if="searchKeyword" @click="clearSearch">清除</text>
      </view>
    </view>

    <!-- 合同分类 -->
    <view class="category-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeCategory === 'all' }"
        @click="switchCategory('all')"
      >
        全部合同
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeCategory === 'property' }"
        @click="switchCategory('property')"
      >
        物业服务
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeCategory === 'maintenance' }"
        @click="switchCategory('maintenance')"
      >
        维修保养
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeCategory === 'other' }"
        @click="switchCategory('other')"
      >
        其他合同
      </view>
    </view>

    <!-- 合同统计 -->
    <view class="stats-section">
      <view class="stat-item">
        <text class="stat-number">{{ stats.total }}</text>
        <text class="stat-label">总合同数</text>
      </view>
      <view class="stat-item">
        <text class="stat-number active">{{ stats.active }}</text>
        <text class="stat-label">生效中</text>
      </view>
      <view class="stat-item">
        <text class="stat-number expired">{{ stats.expired }}</text>
        <text class="stat-label">已到期</text>
      </view>
      <view class="stat-item">
        <text class="stat-number pending">{{ stats.pending }}</text>
        <text class="stat-label">待生效</text>
      </view>
    </view>

    <!-- 合同列表 -->
    <view class="contract-list">
      <view 
        class="contract-item" 
        v-for="contract in filteredContracts" 
        :key="contract.contractId"
        @click="viewContract(contract)"
      >
        <view class="contract-header">
          <view class="contract-info">
            <text class="contract-name">{{ contract.contractName }}</text>
            <text class="contract-no">编号：{{ contract.contractNo }}</text>
          </view>
          <view class="contract-status" :class="[contract.status === 'pending' ? 'status-pending' : contract.status === 'active' ? 'status-active' : contract.status === 'completed' ? 'status-completed' : contract.status === 'terminated' ? 'status-terminated' : 'status-default']">
            {{ getStatusText(contract.status) }}
          </view>
        </view>
        
        <view class="contract-details">
          <view class="detail-row">
            <view class="detail-item">
              <uni-icons type="calendar" size="14" color="#8C8C8C" />
              <text>签订：{{ contract.signDate }}</text>
            </view>
            <view class="detail-item">
              <uni-icons type="clock" size="14" color="#8C8C8C" />
              <text>到期：{{ contract.endDate }}</text>
            </view>
          </view>
          <view class="detail-row">
            <view class="detail-item">
              <uni-icons type="person" size="14" color="#8C8C8C" />
              <text>甲方：{{ contract.partyA }}</text>
            </view>
            <view class="detail-item">
              <uni-icons type="business" size="14" color="#8C8C8C" />
              <text>乙方：{{ contract.partyB }}</text>
            </view>
          </view>
        </view>
        
        <view class="contract-amount" v-if="contract.contractAmount">
          <text class="amount-label">合同金额：</text>
          <text class="amount-value">¥{{ formatAmount(contract.contractAmount) }}</text>
        </view>
        
        <!-- 快速操作 -->
        <view class="contract-actions">
          <button class="action-btn download" @click.stop="downloadContract(contract)">
            <uni-icons type="download" size="16" color="#1890FF" />
            <text>下载</text>
          </button>
          <button class="action-btn share" @click.stop="shareContract(contract)">
            <uni-icons type="redo" size="16" color="#52C41A" />
            <text>分享</text>
          </button>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="filteredContracts.length === 0">
      <uni-icons type="paperplane" size="80" color="#D9D9D9" />
      <text class="empty-text">{{ getEmptyText() }}</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: '',
      activeCategory: 'all',
      contractList: [],
      stats: {
        total: 0,
        active: 0,
        expired: 0,
        pending: 0
      }
    }
  },
  computed: {
    filteredContracts() {
      let contracts = this.contractList
      
      // 按分类筛选
      if (this.activeCategory !== 'all') {
        contracts = contracts.filter(contract => contract.category === this.activeCategory)
      }
      
      // 按关键词搜索
      if (this.searchKeyword.trim()) {
        const keyword = this.searchKeyword.trim().toLowerCase()
        contracts = contracts.filter(contract => 
          contract.contractName.toLowerCase().includes(keyword) ||
          contract.contractNo.toLowerCase().includes(keyword)
        )
      }
      
      return contracts
    }
  },
  onLoad() {
    this.loadContracts()
    this.loadStats()
  },
  onPullDownRefresh() {
    this.loadContracts()
    this.loadStats()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    loadContracts() {
      // 模拟合同数据
      this.contractList = [
        {
          contractId: 1,
          contractNo: 'HT202401001',
          contractName: '智慧花园小区物业服务合同',
          category: 'property',
          status: 'active',
          signDate: '2024-01-01',
          startDate: '2024-01-01',
          endDate: '2026-12-31',
          partyA: '智慧花园业主委员会',
          partyB: '北京优质物业服务有限公司',
          contractAmount: 2400000.00,
          description: '为智慧花园小区提供全方位物业管理服务，包括保洁、保安、绿化、设备维护等服务内容。'
        },
        {
          contractId: 2,
          contractNo: 'HT202401002',
          contractName: '电梯维保服务合同',
          category: 'maintenance',
          status: 'active',
          signDate: '2024-02-15',
          startDate: '2024-03-01',
          endDate: '2025-02-28',
          partyA: '智慧花园业主委员会',
          partyB: '奥的斯电梯服务公司',
          contractAmount: 180000.00,
          description: '为小区12部电梯提供定期维护保养服务，确保电梯安全运行。'
        },
        {
          contractId: 3,
          contractNo: 'HT202401003',
          contractName: '消防设备维护合同',
          category: 'maintenance',
          status: 'pending',
          signDate: '2024-03-10',
          startDate: '2024-04-01',
          endDate: '2025-03-31',
          partyA: '智慧花园业主委员会',
          partyB: '安全消防技术有限公司',
          contractAmount: 120000.00,
          description: '负责小区消防设备的定期检测、维护和更新工作。'
        },
        {
          contractId: 4,
          contractNo: 'HT202401004',
          contractName: '绿化养护服务合同',
          category: 'other',
          status: 'active',
          signDate: '2024-01-20',
          startDate: '2024-02-01',
          endDate: '2024-12-31',
          partyA: '智慧花园业主委员会',
          partyB: '城市绿化工程有限公司',
          contractAmount: 200000.00,
          description: '负责小区绿化植物的日常养护、修剪、病虫害防治等工作。'
        },
        {
          contractId: 5,
          contractNo: 'HT202301005',
          contractName: '停车场管理合同',
          category: 'property',
          status: 'expired',
          signDate: '2023-01-01',
          startDate: '2023-01-01',
          endDate: '2023-12-31',
          partyA: '智慧花园业主委员会',
          partyB: '智能停车管理公司',
          contractAmount: 150000.00,
          description: '负责小区地下停车场的日常管理和设备维护工作。'
        }
      ]
    },
    
    loadStats() {
      // 计算统计数据
      const total = this.contractList.length
      const active = this.contractList.filter(c => c.status === 'active').length
      const expired = this.contractList.filter(c => c.status === 'expired').length
      const pending = this.contractList.filter(c => c.status === 'pending').length
      
      this.stats = { total, active, expired, pending }
    },
    
    switchCategory(category) {
      this.activeCategory = category
    },
    
    onSearch() {
      // 搜索防抖处理
      clearTimeout(this.searchTimer)
      this.searchTimer = setTimeout(() => {
        // 搜索逻辑在computed中处理
      }, 300)
    },
    
    clearSearch() {
      this.searchKeyword = ''
    },
    
    getStatusClass(status) {
      const classMap = {
        'active': 'status-active',
        'expired': 'status-expired',
        'pending': 'status-pending'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        'active': '生效中',
        'expired': '已到期',
        'pending': '待生效'
      }
      return textMap[status] || '未知'
    },
    
    formatAmount(amount) {
      return new Intl.NumberFormat('zh-CN').format(amount)
    },
    
    getEmptyText() {
      if (this.searchKeyword.trim()) {
        return '未找到相关合同'
      }
      if (this.activeCategory !== 'all') {
        return '该分类下暂无合同'
      }
      return '暂无合同数据'
    },
    
    viewContract(contract) {
      uni.navigateTo({
        url: `/pages/contract-fulfillment/detail?id=${contract.contractId}`
      })
    },
    
    downloadContract(contract) {
      uni.showToast({
        title: '下载功能开发中',
        icon: 'none'
      })
    },
    
    shareContract(contract) {
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

.contract-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.search-section {
  background: #FFFFFF;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
  
  .search-box {
    display: flex;
    align-items: center;
    background: #F8F9FA;
    border-radius: 24rpx;
    padding: 20rpx 30rpx;
    border: 1rpx solid #F0F0F0;
    
    .search-input {
      flex: 1;
      margin-left: 16rpx;
      font-size: 28rpx;
      color: #262626;
    }
    
    .clear-btn {
      font-size: 24rpx;
      color: #8C8C8C;
      padding: 8rpx 16rpx;
    }
  }
}

.category-tabs {
  display: flex;
  background: #FFFFFF;
  margin: 0 20rpx;
  border-radius: 24rpx;
  padding: 10rpx;
  border: 1rpx solid #F0F0F0;
  
  .tab-item {
    flex: 1;
    text-align: center;
    padding: 16rpx 0;
    font-size: 24rpx;
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

.stats-section {
  display: flex;
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 30rpx 20rpx;
  border: 1rpx solid #F0F0F0;
  
  .stat-item {
    flex: 1;
    text-align: center;
    
    .stat-number {
      display: block;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      margin-bottom: 8rpx;
      
      &.active {
        color: #52C41A;
      }
      
      &.expired {
        color: #FF4D4F;
      }
      
      &.pending {
        color: #FA8C16;
      }
    }
    
    .stat-label {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
}

.contract-list {
  padding: 0 20rpx;
}

.contract-item {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  border: 1rpx solid #F0F0F0;
  
  &:active {
    background: #F8F9FA;
  }
}

.contract-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
  
  .contract-info {
    flex: 1;
    margin-right: 20rpx;
    
    .contract-name {
      display: block;
      font-size: 30rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-bottom: 8rpx;
    }
    
    .contract-no {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
  
  .contract-status {
    padding: 8rpx 16rpx;
    border-radius: 16rpx;
    font-size: 22rpx;
    font-weight: 500;
    white-space: nowrap;
    
    &.status-active {
      background: #F6FFED;
      color: #52C41A;
    }
    
    &.status-expired {
      background: #FFF2F0;
      color: #FF4D4F;
    }
    
    &.status-pending {
      background: #FFF2E8;
      color: #FA8C16;
    }
  }
}

.contract-details {
  margin-bottom: 20rpx;
  
  .detail-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 12rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .detail-item {
      display: flex;
      align-items: center;
      flex: 1;
      
      text {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #595959;
      }
    }
  }
}

.contract-amount {
  display: flex;
  align-items: center;
  padding: 16rpx;
  background: #F0F8FF;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
  
  .amount-label {
    font-size: 24rpx;
    color: #595959;
  }
  
  .amount-value {
    font-size: 28rpx;
    font-weight: 600;
    color: #1890FF;
    margin-left: 8rpx;
  }
}

.contract-actions {
  display: flex;
  gap: 16rpx;
  
  .action-btn {
    flex: 1;
    height: 64rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 16rpx;
    font-size: 24rpx;
    border: none;
    
    text {
      margin-left: 8rpx;
    }
    
    &.download {
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
 
 