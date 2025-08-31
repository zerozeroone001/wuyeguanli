<template>
  <view class="container">
    <!-- 加载状态 -->
    <view v-if="loading" class="loading-state">
      <uni-icons type="spinner-cycle" size="40" color="#D9D9D9" />
      <text class="loading-text">加载中...</text>
    </view>

    <!-- 加载成功后显示 -->
    <view v-else-if="contractInfo" class="detail-container">
      <!-- 合同基本信息 -->
      <view class="contract-info">
        <view class="info-header">
          <text class="contract-name">{{ contractInfo.contractName }}</text>
          <view class="contract-status" :class="[contractInfo.status === '0' ? 'status-active' : 'status-default']">
            {{ getStatusText(contractInfo.status) }}
          </view>
        </view>
        <view class="contract-no">
          <text>合同编号：{{ contractInfo.contractNo }}</text>
        </view>
      </view>

      <!-- 合同详情 -->
      <view class="detail-section">
        <view class="section-header">
          <uni-icons type="list" size="20" color="#262626" />
          <text class="section-title">合同详情</text>
        </view>
        <view class="detail-list">
          <view class="detail-item">
            <text class="detail-label">生效日期</text>
            <text class="detail-value">{{ contractInfo.effectiveDate | formatDate }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">到期日期</text>
            <text class="detail-value">{{ contractInfo.expiryDate | formatDate }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">合同期限</text>
            <text class="detail-value">{{ getContractDuration() }}</text>
          </view>
          <view class="detail-item" v-if="contractInfo.contractAmount">
            <text class="detail-label">合同金额</text>
            <text class="detail-value amount">¥{{ formatAmount(contractInfo.contractAmount) }}</text>
          </view>
        </view>
      </view>

      <!-- 合同双方 -->
      <view class="parties-section">
        <view class="section-header">
          <uni-icons type="person" size="20" color="#262626" />
          <text class="section-title">合同双方</text>
        </view>
        <view class="parties-content">
          <view class="party-item">
            <view class="party-header"><text class="party-role">甲方</text></view>
            <view class="party-info"><text class="party-name">{{ contractInfo.partyA }}</text></view>
          </view>
          <view class="party-divider"><uni-icons type="right" size="16" color="#D9D9D9" /></view>
          <view class="party-item">
            <view class="party-header"><text class="party-role">乙方</text></view>
            <view class="party-info"><text class="party-name">{{ contractInfo.partyB }}</text></view>
          </view>
        </view>
      </view>

      <!-- 负责人信息 -->
      <view class="manager-section" v-if="contractInfo.managerName">
        <view class="section-header">
          <uni-icons type="contact" size="20" color="#262626" />
          <text class="section-title">负责人信息</text>
        </view>
        <view class="detail-list">
          <view class="detail-item">
            <text class="detail-label">负责人姓名</text>
            <text class="detail-value">{{ contractInfo.managerName }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">联系电话</text>
            <text class="detail-value">{{ contractInfo.managerPhone }}</text>
          </view>
        </view>
      </view>

      <!-- 合同内容 -->
      <view class="content-section" v-if="contractInfo.contractContent">
        <view class="section-header">
          <uni-icons type="compose" size="20" color="#262626" />
          <text class="section-title">合同内容</text>
        </view>
        <view class="content-text"><text>{{ contractInfo.contractContent }}</text></view>
      </view>

      <!-- 重要条款 -->
      <view class="clauses-section" v-if="parsedClauses.length > 0">
        <view class="section-header">
          <uni-icons type="flag" size="20" color="#262626" />
          <text class="section-title">重要条款</text>
        </view>
        <view class="clauses-list">
          <view class="clause-item" v-for="(clause, index) in parsedClauses" :key="index">
            <view class="clause-number">{{ index + 1 }}</view>
            <view class="clause-content">
              <text class="clause-title">{{ clause.title }}</text>
              <text class="clause-text">{{ clause.content }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 操作按钮 -->
      <view class="action-buttons">
        <button class="action-btn contact" @click="contactManager" v-if="contractInfo.managerPhone">
          <uni-icons type="phone" size="18" color="#FA8C16" />
          <text>联系负责人</text>
        </button>
        <button class="action-btn share" open-type="share">
          <uni-icons type="redo" size="18" color="#52C41A" />
          <text>分享合同</text>
        </button>
      </view>
    </view>

    <!-- 加载失败或无数据状态 -->
    <view v-else class="empty-state">
      <uni-icons type="paperplane" size="80" color="#D9D9D9" />
      <text class="empty-text">无法加载合同信息</text>
    </view>
  </view>
</template>

<script>
import { getUserContract } from '@/api/contract.js';

export default {
  data() {
    return {
      contractId: null,
      contractInfo: null,
      loading: true
    }
  },
  filters: {
    formatDate(dateStr) {
      if (!dateStr) return '';
      return dateStr.split(' ')[0]; // 只取日期部分
    }
  },
  computed: {
    parsedClauses() {
      if (!this.contractInfo || !this.contractInfo.importantClauses) {
        return [];
      }
      try {
        const clauses = JSON.parse(this.contractInfo.importantClauses);
        return Array.isArray(clauses) ? clauses : [];
      } catch (e) {
        console.error("Parsing important clauses failed:", e);
        return [];
      }
    }
  },
  onLoad(options) {
    if (options.id) {
        this.contractId = options.id;
        this.loadContractDetail();
    } else {
        uni.showToast({
            title: '缺少合同ID',
            icon: 'error'
        });
        this.loading = false;
    }
  },
  onShareAppMessage() {
    return {
      title: `合同详情：${this.contractInfo.contractName}`,
      path: `/pages/contract-fulfillment/detail?id=${this.contractId}`
    };
  },
  methods: {
    loadContractDetail() {
      this.loading = true;
      getUserContract(this.contractId).then(response => {
        if (response.data) {
            this.contractInfo = response.data;
        } else {
            uni.showToast({
                title: '未找到合同信息',
                icon: 'none'
            });
        }
        this.loading = false;
      }).catch(error => {
        console.error("获取合同详情失败:", error);
        uni.showToast({
            title: '加载失败，请稍后重试',
            icon: 'none'
        });
        this.loading = false;
      });
    },
    
    getStatusText(status) {
      const textMap = {
        '0': '正常',
        '1': '已归档'
      }
      return textMap[status] || '未知'
    },
    
    formatAmount(amount) {
      if (amount === null || amount === undefined) return '0.00';
      return new Intl.NumberFormat('zh-CN').format(amount)
    },

    getContractDuration() {
      if (!this.contractInfo || !this.contractInfo.effectiveDate || !this.contractInfo.expiryDate) {
        return '未知'
      }
      
      const start = new Date(this.contractInfo.effectiveDate)
      const end = new Date(this.contractInfo.expiryDate)
      if (isNaN(start.getTime()) || isNaN(end.getTime())) {
          return '日期无效'
      }

      const diffTime = Math.abs(end - start)
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1 // 包含起止当天
      
      if (diffDays < 0) return '日期错误';

      const years = Math.floor(diffDays / 365)
      const months = Math.floor((diffDays % 365) / 30)
      
      let duration = ''
      if (years > 0) {
        duration += `${years}年`;
      }
      if (months > 0) {
        duration += `${months}个月`;
      }
      if (duration === '') {
          const days = Math.floor(diffDays % 30);
          if (days > 0) {
              duration = `${days}天`;
          }
      }

      return duration || '少于1个月';
    },

    contactManager() {
      if (!this.contractInfo.managerPhone) {
          uni.showToast({ title: '未提供联系电话', icon: 'none' });
          return;
      }
      uni.makePhoneCall({
        phoneNumber: this.contractInfo.managerPhone,
        fail: () => {
          uni.showToast({
            title: '拨打失败',
            icon: 'none'
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
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

.detail-container {
  padding-bottom: 160rpx; /* 为底部按钮留出空间 */
}

.contract-info, .detail-section, .parties-section, .manager-section, .content-section, .clauses-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
}

.contract-info {
  .info-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 16rpx;
    
    .contract-name {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
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
      
      &.status-default {
        background: #F0F0F0;
        color: #8C8C8C;
      }
    }
  }
  
  .contract-no {
    text {
      font-size: 24rpx;
      color: #8C8C8C;
    }
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

.detail-list {
  .detail-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #F0F0F0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .detail-label {
      font-size: 26rpx;
      color: #8C8C8C;
      min-width: 160rpx;
    }
    
    .detail-value {
      flex: 1;
      text-align: right;
      font-size: 26rpx;
      color: #262626;
      font-weight: 500;
      
      &.amount {
        color: #1890FF;
        font-weight: 600;
      }
    }
  }
}

.parties-content {
  display: flex;
  align-items: center;
  
  .party-item {
    flex: 1;
    
    .party-header {
      margin-bottom: 12rpx;
      
      .party-role {
        display: inline-block;
        padding: 6rpx 16rpx;
        background: #E6F7FF;
        color: #1890FF;
        font-size: 22rpx;
        font-weight: 500;
        border-radius: 12rpx;
      }
    }
    
    .party-info {
      .party-name {
        display: block;
        font-size: 26rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 8rpx;
      }
    }
  }
  
  .party-divider {
    margin: 0 30rpx;
    opacity: 0.5;
  }
}

.content-text {
  text {
    font-size: 26rpx;
    color: #595959;
    line-height: 1.6;
  }
}

.clauses-list {
  .clause-item {
    display: flex;
    margin-bottom: 30rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .clause-number {
      width: 48rpx;
      height: 48rpx;
      line-height: 48rpx;
      text-align: center;
      background: #1890FF;
      color: #FFFFFF;
      border-radius: 50%;
      font-size: 22rpx;
      font-weight: 600;
      margin-right: 20rpx;
      flex-shrink: 0;
    }
    
    .clause-content {
      flex: 1;
      
      .clause-title {
        display: block;
        font-size: 26rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 12rpx;
      }
      
      .clause-text {
        font-size: 24rpx;
        color: #595959;
        line-height: 1.6;
      }
    }
  }
}

.action-buttons {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  gap: 16rpx;
  padding: 20rpx;
  background-color: #FFFFFF;
  border-top: 1rpx solid #F0F0F0;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
  
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
    
    &.contact {
      background: #FFF2E8;
      color: #FA8C16;
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
