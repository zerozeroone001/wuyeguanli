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

    <!-- 合同列表 -->
    <view class="contract-list" v-if="!loading">
      <view 
        class="contract-item" 
        v-for="contract in contractList" 
        :key="contract.contractId"
        @click="viewContract(contract)"
      >
        <view class="contract-header">
          <view class="contract-info">
            <text class="contract-name">{{ contract.contractName }}</text>
            <text class="contract-no">编号：{{ contract.contractNo }}</text>
          </view>
          <view class="contract-status" :class="getContractDynamicStatus(contract).className">
            {{ getContractDynamicStatus(contract).text }}
          </view>
        </view>
        
        <view class="contract-details">
          <view class="detail-row">
            <view class="detail-item">
              <uni-icons type="calendar" size="14" color="#8C8C8C" />
              <text>签订：{{ contract.effectiveDate | formatDate }}</text>
            </view>
            <view class="detail-item">
              <uni-icons type="clock" size="14" color="#8C8C8C" />
              <text>到期：{{ contract.expiryDate | formatDate }}</text>
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
      </view>
    </view>

    <!-- 加载状态 -->
    <view class="loading-state" v-if="loading">
        <uni-icons type="spinner-cycle" size="40" color="#D9D9D9" />
        <text class="loading-text">加载中...</text>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="!loading && contractList.length === 0">
      <uni-icons type="paperplane" size="80" color="#D9D9D9" />
      <text class="empty-text">暂无合同数据</text>
    </view>
  </view>
</template>

<script>
import { listUserContract } from '@/api/contract.js';
import { getContractDynamicStatus } from '@/utils/contract.js';

export default {
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractName: undefined,
        category: undefined,
      },
      searchKeyword: '',
      activeCategory: 'all',
      contractList: [],
      total: 0,
      loading: true,
    }
  },
  filters: {
    formatDate(dateStr) {
      if (!dateStr) return '';
      return dateStr.split(' ')[0];
    }
  },
  onLoad() {
    this.loadContracts();
  },
  onReachBottom() {
    if (this.contractList.length < this.total) {
      this.queryParams.pageNum++;
      this.loadContracts(true);
    }
  },
  onPullDownRefresh() {
    this.queryParams.pageNum = 1;
    this.loadContracts();
    uni.stopPullDownRefresh();
  },
  methods: {
    loadContracts(loadMore = false) {
      this.loading = true;
      this.queryParams.contractName = this.searchKeyword;

      listUserContract(this.queryParams).then(response => {
        const newList = response.rows || [];
        if (loadMore) {
          this.contractList = this.contractList.concat(newList);
        } else {
          this.contractList = newList;
        }
        this.total = response.total || 0;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    switchCategory(category) {
      this.activeCategory = category;
      this.queryParams.category = category === 'all' ? undefined : category;
      this.queryParams.pageNum = 1;
      this.loadContracts();
    },
    onSearch() {
      clearTimeout(this.searchTimer);
      this.searchTimer = setTimeout(() => {
        this.queryParams.pageNum = 1;
        this.loadContracts();
      }, 300);
    },
    clearSearch() {
      this.searchKeyword = '';
      this.queryParams.contractName = undefined;
      this.queryParams.pageNum = 1;
      this.loadContracts();
    },
    getContractDynamicStatus(contract) {
      return getContractDynamicStatus(contract);
    },
    viewContract(contract) {
      uni.navigateTo({
        url: `/pages/contract-fulfillment/detail?id=${contract.contractId}`
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.loading-state, .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding-top: 300rpx;
    .loading-text, .empty-text {
        margin-top: 20rpx;
        color: #8C8C8C;
    }
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
  margin: 20rpx;
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
      background: #F5F5F5;
      color: #8C8C8C;
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
</style>