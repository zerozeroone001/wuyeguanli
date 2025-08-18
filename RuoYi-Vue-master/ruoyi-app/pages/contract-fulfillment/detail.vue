<template>
  <view class="detail-container">
    <!-- 合同基本信息 -->
    <view class="contract-info">
      <view class="info-header">
        <text class="contract-name">{{ contractInfo.contractName }}</text>
        <view class="contract-status" :class="[contractInfo.status === 'active' ? 'status-active' : contractInfo.status === 'expired' ? 'status-expired' : contractInfo.status === 'pending' ? 'status-pending' : 'status-default']">
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
          <text class="detail-label">合同类型</text>
          <text class="detail-value">{{ getCategoryText(contractInfo.category) }}</text>
        </view>
        <view class="detail-item">
          <text class="detail-label">签订日期</text>
          <text class="detail-value">{{ contractInfo.signDate }}</text>
        </view>
        <view class="detail-item">
          <text class="detail-label">生效日期</text>
          <text class="detail-value">{{ contractInfo.startDate }}</text>
        </view>
        <view class="detail-item">
          <text class="detail-label">到期日期</text>
          <text class="detail-value">{{ contractInfo.endDate }}</text>
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
          <view class="party-header">
            <text class="party-role">甲方</text>
          </view>
          <view class="party-info">
            <text class="party-name">{{ contractInfo.partyA }}</text>
            <text class="party-desc" v-if="contractInfo.partyAInfo">{{ contractInfo.partyAInfo }}</text>
          </view>
        </view>
        
        <view class="party-divider">
          <uni-icons type="right" size="16" color="#D9D9D9" />
        </view>
        
        <view class="party-item">
          <view class="party-header">
            <text class="party-role">乙方</text>
          </view>
          <view class="party-info">
            <text class="party-name">{{ contractInfo.partyB }}</text>
            <text class="party-desc" v-if="contractInfo.partyBInfo">{{ contractInfo.partyBInfo }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 合同内容 -->
    <view class="content-section">
      <view class="section-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="section-title">合同内容</text>
      </view>
      <view class="content-text">
        <text>{{ contractInfo.description }}</text>
      </view>
    </view>

    <!-- 重要条款 -->
    <view class="clauses-section" v-if="contractInfo.keyClauses && contractInfo.keyClauses.length > 0">
      <view class="section-header">
        <uni-icons type="flag" size="20" color="#262626" />
        <text class="section-title">重要条款</text>
      </view>
      
      <view class="clauses-list">
        <view class="clause-item" v-for="(clause, index) in contractInfo.keyClauses" :key="index">
          <view class="clause-number">{{ index + 1 }}</view>
          <view class="clause-content">
            <text class="clause-title">{{ clause.title }}</text>
            <text class="clause-text">{{ clause.content }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 履约情况 -->
    <view class="performance-section" v-if="contractInfo.status === 'active'">
      <view class="section-header">
        <uni-icons type="checkmarkempty" size="20" color="#262626" />
        <text class="section-title">履约情况</text>
      </view>
      
      <view class="performance-content">
        <view class="progress-info">
          <text class="progress-label">履约进度</text>
          <text class="progress-percent">{{ contractInfo.performanceRate || 0 }}%</text>
        </view>
        <view class="progress-bar">
          <view class="progress-fill" :style="{ width: (contractInfo.performanceRate || 0) + '%' }"></view>
        </view>
        
        <view class="performance-details">
          <view class="performance-item">
            <uni-icons type="calendar" size="16" color="#52C41A" />
            <text>最近更新：{{ contractInfo.lastUpdateTime || '暂无记录' }}</text>
          </view>
          <view class="performance-item">
            <uni-icons type="person" size="16" color="#52C41A" />
            <text>负责人：{{ contractInfo.manager || '待指定' }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 相关文档 -->
    <view class="documents-section" v-if="contractInfo.documents && contractInfo.documents.length > 0">
      <view class="section-header">
        <uni-icons type="paperplane" size="20" color="#262626" />
        <text class="section-title">相关文档</text>
      </view>
      
      <view class="documents-list">
        <view 
          class="document-item" 
          v-for="doc in contractInfo.documents" 
          :key="doc.id"
          @click="viewDocument(doc)"
        >
          <view class="doc-icon">
            <uni-icons :type="getDocIcon(doc.type)" size="24" :color="getDocColor(doc.type)" />
          </view>
          <view class="doc-info">
            <text class="doc-name">{{ doc.name }}</text>
            <text class="doc-meta">{{ doc.size }} • {{ doc.uploadTime }}</text>
          </view>
          <view class="doc-action">
            <uni-icons type="download" size="18" color="#1890FF" />
          </view>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button class="action-btn download" @click="downloadContract">
        <uni-icons type="download" size="18" color="#1890FF" />
        <text>下载合同</text>
      </button>
      <button class="action-btn share" @click="shareContract">
        <uni-icons type="redo" size="18" color="#52C41A" />
        <text>分享合同</text>
      </button>
      <button class="action-btn contact" @click="contactManager" v-if="contractInfo.managerPhone">
        <uni-icons type="phone" size="18" color="#FA8C16" />
        <text>联系负责人</text>
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      contractId: null,
      contractInfo: {}
    }
  },
  onLoad(options) {
    this.contractId = options.id
    this.loadContractDetail()
  },
  methods: {
    loadContractDetail() {
      // 模拟合同详情数据
      this.contractInfo = {
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
        partyAInfo: '代表全体业主利益的自治组织',
        partyBInfo: '专业物业管理服务企业，具有一级资质',
        contractAmount: 2400000.00,
        description: '本合同是智慧花园小区与北京优质物业服务有限公司签订的物业服务合同。合同约定乙方为甲方提供全方位的物业管理服务，包括但不限于：保洁服务、保安服务、绿化养护、设备维护、客户服务等。服务标准严格按照国家相关规定和行业标准执行，确保为业主提供优质、专业的物业服务。',
        performanceRate: 85,
        lastUpdateTime: '2024-01-15 10:30',
        manager: '李经理',
        managerPhone: '13800138001',
        keyClauses: [
          {
            title: '服务标准',
            content: '乙方应按照国家物业管理相关法规和本合同约定，提供专业、规范的物业服务，服务质量应达到优良标准。'
          },
          {
            title: '费用支付',
            content: '甲方应按月支付物业服务费，每月25日前支付下月费用。逾期支付的，按日加收0.5‰的滞纳金。'
          },
          {
            title: '违约责任',
            content: '任何一方违反合同约定的，应承担相应的违约责任，并赔偿对方因此造成的损失。'
          }
        ],
        documents: [
          {
            id: 1,
            name: '物业服务合同正本.pdf',
            type: 'pdf',
            size: '2.5MB',
            uploadTime: '2024-01-01'
          },
          {
            id: 2,
            name: '服务标准附件.doc',
            type: 'doc',
            size: '1.2MB',
            uploadTime: '2024-01-01'
          },
          {
            id: 3,
            name: '收费标准明细.xlsx',
            type: 'excel',
            size: '856KB',
            uploadTime: '2024-01-01'
          }
        ]
      }
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
    
    getCategoryText(category) {
      const categoryMap = {
        'property': '物业服务合同',
        'maintenance': '维修保养合同',
        'other': '其他合同'
      }
      return categoryMap[category] || '未知类型'
    },
    
    formatAmount(amount) {
      return new Intl.NumberFormat('zh-CN').format(amount)
    },
    
    getContractDuration() {
      if (!this.contractInfo.startDate || !this.contractInfo.endDate) {
        return '未知'
      }
      
      const start = new Date(this.contractInfo.startDate)
      const end = new Date(this.contractInfo.endDate)
      const diffTime = Math.abs(end - start)
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      const years = Math.floor(diffDays / 365)
      const months = Math.floor((diffDays % 365) / 30)
      
      if (years > 0) {
        return `${years}年${months > 0 ? months + '个月' : ''}`
      } else {
        return `${months}个月`
      }
    },
    
    getDocIcon(type) {
      const iconMap = {
        'pdf': 'paperplane',
        'doc': 'compose',
        'excel': 'list',
        'image': 'image'
      }
      return iconMap[type] || 'paperplane'
    },
    
    getDocColor(type) {
      const colorMap = {
        'pdf': '#FF4D4F',
        'doc': '#1890FF',
        'excel': '#52C41A',
        'image': '#FA8C16'
      }
      return colorMap[type] || '#8C8C8C'
    },
    
    viewDocument(doc) {
      uni.showToast({
        title: '文档预览功能开发中',
        icon: 'none'
      })
    },
    
    downloadContract() {
      uni.showToast({
        title: '下载功能开发中',
        icon: 'none'
      })
    },
    
    shareContract() {
      uni.showToast({
        title: '分享功能开发中',
        icon: 'none'
      })
    },
    
    contactManager() {
      uni.makePhoneCall({
        phoneNumber: this.contractInfo.managerPhone,
        success: () => {
          console.log('拨打电话成功')
        },
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
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.detail-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 40rpx;
}

.contract-info {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
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
  
  .contract-no {
    text {
      font-size: 24rpx;
      color: #8C8C8C;
    }
  }
}

.detail-section, .parties-section, .content-section, .clauses-section, .performance-section, .documents-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
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
      
      .party-desc {
        font-size: 22rpx;
        color: #8C8C8C;
        line-height: 1.4;
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

.performance-content {
  .progress-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
    
    .progress-label {
      font-size: 26rpx;
      color: #262626;
      font-weight: 500;
    }
    
    .progress-percent {
      font-size: 28rpx;
      font-weight: 600;
      color: #52C41A;
    }
  }
  
  .progress-bar {
    height: 16rpx;
    background: #F0F0F0;
    border-radius: 8rpx;
    overflow: hidden;
    margin-bottom: 24rpx;
    
    .progress-fill {
      height: 100%;
      background: linear-gradient(90deg, #52C41A, #73D13D);
      border-radius: 8rpx;
      transition: width 0.5s ease;
    }
  }
  
  .performance-details {
    .performance-item {
      display: flex;
      align-items: center;
      margin-bottom: 12rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      text {
        margin-left: 12rpx;
        font-size: 24rpx;
        color: #595959;
      }
    }
  }
}

.documents-list {
  .document-item {
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
    
    .doc-icon {
      width: 60rpx;
      height: 60rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #F8F9FA;
      border-radius: 12rpx;
      margin-right: 20rpx;
    }
    
    .doc-info {
      flex: 1;
      
      .doc-name {
        display: block;
        font-size: 26rpx;
        color: #262626;
        font-weight: 500;
        margin-bottom: 8rpx;
      }
      
      .doc-meta {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
    
    .doc-action {
      padding: 16rpx;
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
    
    &.download {
      background: #E6F7FF;
      color: #1890FF;
    }
    
    &.share {
      background: #F6FFED;
      color: #52C41A;
    }
    
    &.contact {
      background: #FFF2E8;
      color: #FA8C16;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}
</style>
 
 