<template>
  <view class="notary-container">
    <!-- 服务介绍 -->
    <view class="service-intro">
      <view class="intro-content">
        <view class="intro-header">
          <uni-icons type="checkmarkempty" size="32" color="#FFFFFF" />
          <view class="intro-text">
            <text class="intro-title">公证存证服务</text>
            <text class="intro-subtitle">权威认证 · 法律保障 · 数字存证</text>
          </view>
        </view>
        <text class="intro-description">
          提供专业的公证存证服务，为您的重要文件和数据提供法律保障，确保证据的真实性和完整性。
        </text>
      </view>
    </view>

    <!-- 服务统计 -->
    <view class="service-stats">
      <view class="stats-grid">
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.totalNotary }}</text>
          <text class="stat-label">累计公证</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.pendingNotary }}</text>
          <text class="stat-label">办理中</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.completedNotary }}</text>
          <text class="stat-label">已完成</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.successRate }}%</text>
          <text class="stat-label">成功率</text>
        </view>
      </view>
    </view>

    <!-- 快速申请 -->
    <view class="quick-apply">
      <view class="apply-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="apply-title">快速申请</text>
      </view>
      
      <view class="apply-grid">
        <view 
          class="apply-item" 
          v-for="service in quickServices" 
          :key="service.type"
          @click="quickApply(service.type)"
        >
          <view class="apply-icon" :style="{ backgroundColor: service.bgColor }">
            <uni-icons :type="service.icon" size="24" color="#FFFFFF" />
          </view>
          <text class="apply-name">{{ service.name }}</text>
          <text class="apply-desc">{{ service.desc }}</text>
        </view>
      </view>
    </view>

    <!-- 状态筛选 -->
    <view class="status-filter">
      <view class="filter-tabs">
        <view 
          class="tab-item" 
          :class="{ active: activeStatus === status.value }"
          v-for="status in statusTabs" 
          :key="status.value"
          @click="switchStatus(status.value)"
        >
          <text>{{ status.label }}</text>
          <view class="tab-badge" v-if="status.count > 0">{{ status.count }}</view>
        </view>
      </view>
    </view>

    <!-- 公证列表 -->
    <view class="notary-list">
      <view 
        class="notary-item" 
        v-for="notary in notaryList" 
        :key="notary.notaryId"
        @click="viewDetail(notary)"
      >
        <view class="notary-header">
          <view class="notary-info">
            <text class="notary-title">{{ notary.title }}</text>
            <view class="notary-meta">
              <text class="notary-no">编号：{{ notary.notaryNo }}</text>
              <view class="notary-status" :class="['status-' + notary.status]">
                {{ getStatusText(notary.status) }}
              </view>
            </view>
          </view>
          <view class="notary-type" :style="{ backgroundColor: getTypeColor(notary.type) }">
            {{ getTypeText(notary.type) }}
          </view>
        </view>
        
        <view class="notary-content">
          <view class="content-item">
            <uni-icons type="list" size="14" color="#8C8C8C" />
            <text>类型：{{ getTypeText(notary.type) }}</text>
          </view>
          <view class="content-item">
            <uni-icons type="calendar" size="14" color="#8C8C8C" />
            <text>申请时间：{{ notary.applyTime }}</text>
          </view>
          <view class="content-item" v-if="notary.expectedTime">
            <uni-icons type="clock" size="14" color="#8C8C8C" />
            <text>预计完成：{{ notary.expectedTime }}</text>
          </view>
          <view class="content-item" v-if="notary.notaryOffice">
            <uni-icons type="home" size="14" color="#8C8C8C" />
            <text>公证处：{{ notary.notaryOffice }}</text>
          </view>
        </view>
        
        <view class="notary-progress" v-if="notary.status === 'processing'">
          <view class="progress-header">
            <text class="progress-text">办理进度</text>
            <text class="progress-percent">{{ notary.progress }}%</text>
          </view>
          <view class="progress-bar">
            <view class="progress-fill" :style="{ width: notary.progress + '%' }"></view>
          </view>
        </view>
        
        <view class="notary-actions">
          <button 
            class="action-btn secondary" 
            v-if="notary.status === 'draft'"
            @click.stop="editNotary(notary)"
          >
            编辑
          </button>
          <button 
            class="action-btn secondary"
            @click.stop="viewDetail(notary)"
          >
            查看详情
          </button>
          <button 
            class="action-btn primary" 
            v-if="notary.status === 'completed'"
            @click.stop="downloadCertificate(notary)"
          >
            下载证书
          </button>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-if="notaryList.length === 0">
        <uni-icons type="paperplane" size="64" color="#D9D9D9" />
        <text class="empty-text">暂无公证记录</text>
        <button class="empty-btn" @click="quickApply('document')">立即申请</button>
      </view>
    </view>

    <!-- 浮动申请按钮 -->
    <view class="floating-apply" @click="showApplyOptions">
      <uni-icons type="plus" size="24" color="#FFFFFF" />
    </view>

    <!-- 申请选项弹窗 -->
    <uni-popup ref="applyPopup" type="bottom">
      <view class="apply-popup">
        <view class="popup-header">
          <text class="popup-title">选择公证类型</text>
          <view class="close-btn" @click="closeApplyPopup">
            <uni-icons type="close" size="18" color="#8C8C8C" />
          </view>
        </view>
        <view class="popup-content">
          <view 
            class="popup-item" 
            v-for="service in allServices" 
            :key="service.type"
            @click="applyNotary(service.type)"
          >
            <view class="popup-icon" :style="{ backgroundColor: service.bgColor }">
              <uni-icons :type="service.icon" size="20" color="#FFFFFF" />
            </view>
            <view class="popup-info">
              <text class="popup-name">{{ service.name }}</text>
              <text class="popup-desc">{{ service.desc }}</text>
            </view>
            <uni-icons type="arrowright" size="16" color="#BFBFBF" />
          </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import { listNotary, getNotaryStats } from '@/api/notary'

export default {
  data() {
    return {
      activeStatus: 'all',
      statsInfo: {
        totalNotary: 0,
        pendingNotary: 0,
        completedNotary: 0,
        successRate: 0
      },
      quickServices: [
        {
          type: 'document',
          name: '文件公证',
          desc: '合同协议等',
          icon: 'paperplane',
          bgColor: '#1890FF'
        },
        {
          type: 'signature',
          name: '签名公证',
          desc: '签字真实性',
          icon: 'compose',
          bgColor: '#52C41A'
        },
        {
          type: 'identity',
          name: '身份公证',
          desc: '身份证明',
          icon: 'person',
          bgColor: '#FA8C16'
        },
        {
          type: 'property',
          name: '财产公证',
          desc: '财产证明',
          icon: 'wallet',
          bgColor: '#722ED1'
        }
      ],
      allServices: [
        {
          type: 'document',
          name: '文件公证',
          desc: '合同、协议、声明等文件的真实性公证',
          icon: 'paperplane',
          bgColor: '#1890FF'
        },
        {
          type: 'signature',
          name: '签名公证',
          desc: '签字、盖章行为的真实性公证',
          icon: 'compose',
          bgColor: '#52C41A'
        },
        {
          type: 'identity',
          name: '身份公证',
          desc: '身份证明、学历证明等身份信息公证',
          icon: 'person',
          bgColor: '#FA8C16'
        },
        {
          type: 'property',
          name: '财产公证',
          desc: '房产、车辆等财产权属证明公证',
          icon: 'wallet',
          bgColor: '#722ED1'
        },
        {
          type: 'inheritance',
          name: '继承公证',
          desc: '遗产继承相关的公证服务',
          icon: 'home',
          bgColor: '#13C2C2'
        },
        {
          type: 'power',
          name: '委托公证',
          desc: '授权委托书等委托关系公证',
          icon: 'hand-up',
          bgColor: '#EB2F96'
        }
      ],
      statusTabs: [
        { label: '全部', value: 'all', count: 0 },
        { label: '草稿', value: 'draft', count: 0 },
        { label: '审核中', value: 'reviewing', count: 0 },
        { label: '办理中', value: 'processing', count: 0 },
        { label: '已完成', value: 'completed', count: 0 },
        { label: '已拒绝', value: 'rejected', count: 0 }
      ],
      notaryList: [],
      pageNum: 1,
      pageSize: 10,
      total: 0
    }
  },
  onLoad() {
    this.getStatsData()
    this.loadNotaryList()
  },
  onPullDownRefresh() {
    this.pageNum = 1
    this.notaryList = []
    this.getStatsData()
    this.loadNotaryList().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  onReachBottom() {
    if (this.notaryList.length < this.total) {
      this.pageNum++
      this.loadNotaryList()
    }
  },
  methods: {
    getStatsData() {
      getNotaryStats().then(res => {
        if (res.code === 200) {
          this.statsInfo = res.data
          // Update statusTabs counts based on statsInfo
          this.statusTabs.find(tab => tab.value === 'all').count = res.data.totalNotary
          this.statusTabs.find(tab => tab.value === 'processing').count = res.data.pendingNotary
          this.statusTabs.find(tab => tab.value === 'completed').count = res.data.completedNotary
          // For draft, reviewing, rejected, we might need separate calls or backend enhancement
          // For now, they remain 0 or their mock values if not updated by API
        }
      })
    },
    loadNotaryList() {
      uni.showLoading({
        title: '加载中...'
      })
      const query = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        status: this.activeStatus === 'all' ? '' : this.activeStatus
      }
      listNotary(query).then(res => {
        uni.hideLoading()
        if (res.code === 200) {
          this.notaryList = this.notaryList.concat(res.rows)
          this.total = res.total
        }
      }).catch(() => {
        uni.hideLoading()
      })
      return Promise.resolve()
    },
    
    switchStatus(status) {
      this.activeStatus = status
      this.pageNum = 1
      this.notaryList = []
      this.loadNotaryList()
    },
    
    getStatusClass(status) {
      const classMap = {
        'draft': 'status-draft',
        'reviewing': 'status-reviewing',
        'processing': 'status-processing',
        'completed': 'status-completed',
        'rejected': 'status-rejected',
        'cancelled': 'status-rejected' // Assuming cancelled also uses rejected style
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        'draft': '草稿',
        'reviewing': '审核中',
        'processing': '办理中',
        'completed': '已完成',
        'rejected': '已拒绝',
        'cancelled': '已取消'
      }
      return textMap[status] || '未知状态'
    },
    
    getTypeText(type) {
      const textMap = {
        'document': '文件公证',
        'signature': '签名公证',
        'identity': '身份公证',
        'property': '财产公证',
        'inheritance': '继承公证',
        'power': '委托公证'
      }
      return textMap[type] || '其他'
    },
    
    getTypeColor(type) {
      const colorMap = {
        'document': '#1890FF',
        'signature': '#52C41A',
        'identity': '#FA8C16',
        'property': '#722ED1',
        'inheritance': '#13C2C2',
        'power': '#EB2F96'
      }
      return colorMap[type] || '#8C8C8C'
    },
    
    quickApply(type) {
      this.applyNotary(type)
    },
    
    applyNotary(type) {
      uni.navigateTo({
        url: `/pageB/notary-service/apply?type=${type}`
      })
      this.closeApplyPopup()
    },
    
    editNotary(notary) {
      uni.navigateTo({
        url: `/pageB/notary-service/apply?id=${notary.notaryId}&type=${notary.type}`
      })
    },
    
    viewDetail(notary) {
      uni.navigateTo({
        url: `/pageB/notary-service/detail?id=${notary.notaryId}`
      })
    },
    
    downloadCertificate(notary) {
      if (!notary.certificateUrl) {
        uni.showToast({
          title: '暂无证书可下载',
          icon: 'none'
        })
        return
      }
      uni.showLoading({
        title: '下载中...'
      })
      uni.downloadFile({
        url: notary.certificateUrl,
        success: (res) => {
          if (res.statusCode === 200) {
            uni.openDocument({
              filePath: res.tempFilePath,
              showMenu: true,
              success: () => {
                uni.hideLoading()
                uni.showToast({
                  title: '下载完成',
                  icon: 'success'
                })
              },
              fail: (err) => {
                uni.hideLoading()
                uni.showToast({
                  title: '打开文件失败',
                  icon: 'none'
                })
                console.error('打开文件失败', err)
              }
            })
          } else {
            uni.hideLoading()
            uni.showToast({
              title: '下载失败',
              icon: 'none'
            })
          }
        },
        fail: (err) => {
          uni.hideLoading()
          uni.showToast({
            title: '下载失败',
            icon: 'none'
          })
          console.error('下载失败', err)
        }
      })
    },
    
    showApplyOptions() {
      this.$refs.applyPopup.open()
    },
    
    closeApplyPopup() {
      this.$refs.applyPopup.close()
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.notary-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.service-intro {
  background: linear-gradient(135deg, #1890FF 0%, #096DD9 100%);
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  
  .intro-content {
    .intro-header {
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;
      
      .intro-text {
        margin-left: 20rpx;
        
        .intro-title {
          display: block;
          font-size: 32rpx;
          font-weight: 600;
          color: #FFFFFF;
          margin-bottom: 8rpx;
        }
        
        .intro-subtitle {
          font-size: 24rpx;
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }
    
    .intro-description {
      font-size: 26rpx;
      color: rgba(255, 255, 255, 0.9);
      line-height: 1.6;
    }
  }
}

.service-stats {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 30rpx;
    
    .stat-item {
      text-align: center;
      
      .stat-number {
        display: block;
        font-size: 36rpx;
        font-weight: 600;
        color: #1890FF;
        margin-bottom: 8rpx;
      }
      
      .stat-label {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
}

.quick-apply {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .apply-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .apply-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .apply-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 24rpx;
    
    .apply-item {
      text-align: center;
      
      .apply-icon {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 16rpx;
      }
      
      .apply-name {
        display: block;
        font-size: 24rpx;
        font-weight: 500;
        color: #262626;
        margin-bottom: 4rpx;
      }
      
      .apply-desc {
        font-size: 20rpx;
        color: #8C8C8C;
      }
    }
  }
}

.status-filter {
  margin: 0 20rpx 20rpx;
  
  .filter-tabs {
    display: flex;
    background: #FFFFFF;
    border-radius: 24rpx;
    padding: 8rpx;
    border: 1rpx solid #F0F0F0;
    
    .tab-item {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 60rpx;
      border-radius: 16rpx;
      position: relative;
      
      text {
        font-size: 24rpx;
        color: #8C8C8C;
        font-weight: 500;
      }
      
      .tab-badge {
        position: absolute;
        top: -8rpx;
        right: -8rpx;
        background: #FF4D4F;
        color: #FFFFFF;
        font-size: 18rpx;
        padding: 2rpx 8rpx;
        border-radius: 12rpx;
        min-width: 24rpx;
        text-align: center;
      }
      
      &.active {
        background: #E6F7FF;
        
        text {
          color: #1890FF;
          font-weight: 600;
        }
      }
    }
  }
}

.notary-list {
  .notary-item {
    background: #FFFFFF;
    margin: 0 20rpx 20rpx;
    border-radius: 24rpx;
    padding: 30rpx;
    border: 1rpx solid #F0F0F0;
    
    &:active {
      background: #F8F9FA;
    }
    
    .notary-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 20rpx;
      
      .notary-info {
        flex: 1;
        margin-right: 20rpx;
        
        .notary-title {
          display: block;
          font-size: 28rpx;
          font-weight: 600;
          color: #262626;
          margin-bottom: 12rpx;
        }
        
        .notary-meta {
          display: flex;
          align-items: center;
          gap: 16rpx;
          
          .notary-no {
            font-size: 22rpx;
            color: #8C8C8C;
          }
          
          .notary-status {
            padding: 4rpx 12rpx;
            border-radius: 12rpx;
            font-size: 20rpx;
            font-weight: 500;
            
            &.status-draft {
              background: #F5F5F5;
              color: #8C8C8C;
            }
            
            &.status-reviewing {
              background: #FFF2E8;
              color: #FA8C16;
            }
            
            &.status-processing {
              background: #E6F7FF;
              color: #1890FF;
            }
            
            &.status-completed {
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
      
      .notary-type {
        padding: 8rpx 16rpx;
        border-radius: 16rpx;
        font-size: 22rpx;
        font-weight: 500;
        color: #FFFFFF;
        white-space: nowrap;
      }
    }
    
    .notary-content {
      margin-bottom: 20rpx;
      
      .content-item {
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
    
    .notary-progress {
      margin-bottom: 20rpx;
      
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
          color: #1890FF;
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
          background: #1890FF;
          border-radius: 4rpx;
          transition: width 0.3s ease;
        }
      }
    }
    
    .notary-actions {
      display: flex;
      gap: 16rpx;
      
      .action-btn {
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
          background: #1890FF;
          color: #FFFFFF;
        }
        
        &:active {
          opacity: 0.8;
        }
      }
    }
  }
}

.empty-state {
  text-align: center;
  padding: 80rpx 40rpx;
  
  .empty-text {
    display: block;
    font-size: 28rpx;
    color: #8C8C8C;
    margin: 30rpx 0;
  }
  
  .empty-btn {
    background: #1890FF;
    color: #FFFFFF;
    height: 80rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    padding: 0 40rpx;
    
    &:active {
      background: #096DD9;
    }
  }
}

.floating-apply {
  position: fixed;
  bottom: 160rpx;
  right: 40rpx;
  width: 96rpx;
  height: 96rpx;
  background: #1890FF;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(24, 144, 255, 0.3);
  z-index: 999;
  
  &:active {
    background: #096DD9;
    transform: scale(0.95);
  }
}

.apply-popup {
  background: #FFFFFF;
  border-radius: 24rpx 24rpx 0 0;
  
  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #F0F0F0;
    
    .popup-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .close-btn {
      width: 48rpx;
      height: 48rpx;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  
  .popup-content {
    padding: 20rpx 0;
    
    .popup-item {
      display: flex;
      align-items: center;
      padding: 24rpx 30rpx;
      
      &:active {
        background: #F8F9FA;
      }
      
      .popup-icon {
        width: 60rpx;
        height: 60rpx;
        border-radius: 16rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20rpx;
      }
      
      .popup-info {
        flex: 1;
        
        .popup-name {
          display: block;
          font-size: 28rpx;
          font-weight: 500;
          color: #262626;
          margin-bottom: 8rpx;
        }
        
        .popup-desc {
          font-size: 24rpx;
          color: #8C8C8C;
        }
      }
    }
  }
}
</style>