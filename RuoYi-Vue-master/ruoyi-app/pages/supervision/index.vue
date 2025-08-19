<template>
  <view class="supervision-container">
    <!-- 服务介绍 -->
    <view class="service-intro">
      <view class="intro-content">
        <view class="intro-header">
          <uni-icons type="eye" size="32" color="#FFFFFF" />
          <view class="intro-text">
            <text class="intro-title">指导监督服务</text>
            <text class="intro-subtitle">专业指导 · 有效监督 · 规范管理</text>
          </view>
        </view>
        <text class="intro-description">
          为业主委员会和物业服务企业提供专业的指导监督服务，确保物业管理规范运行，维护业主合法权益。
        </text>
      </view>
    </view>

    <!-- 服务统计 -->
    <view class="service-stats">
      <view class="stats-grid">
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.totalSupervision }}</text>
          <text class="stat-label">累计监督</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.activeSupervision }}</text>
          <text class="stat-label">进行中</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.completedSupervision }}</text>
          <text class="stat-label">已完成</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.improvementRate }}%</text>
          <text class="stat-label">改进率</text>
        </view>
      </view>
    </view>

    <!-- 监督类型 -->
    <view class="supervision-types">
      <view class="types-header">
        <uni-icons type="list" size="20" color="#262626" />
        <text class="types-title">监督类型</text>
      </view>
      
      <view class="types-grid">
        <view 
          class="type-item" 
          v-for="type in supervisionTypes" 
          :key="type.value"
          @click="filterByType(type.value)"
        >
          <view class="type-icon" :style="{ backgroundColor: type.bgColor }">
            <uni-icons :type="type.icon" size="24" color="#FFFFFF" />
          </view>
          <text class="type-name">{{ type.name }}</text>
          <text class="type-count">{{ type.count }}项</text>
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

    <!-- 监督列表 -->
    <view class="supervision-list">
      <view 
        class="supervision-item" 
        v-for="item in supervisionList" 
        :key="item.supervisionId"
        @click="viewDetail(item)"
      >
        <view class="supervision-header">
          <view class="supervision-info">
            <text class="supervision-title">{{ item.title }}</text>
            <view class="supervision-meta">
              <text class="supervision-no">编号：{{ item.supervisionNo }}</text>
              <view class="supervision-status" :class="['status-' + item.status]">
                {{ getStatusText(item.status) }}
              </view>
            </view>
          </view>
          <view class="supervision-type" :style="{ backgroundColor: getTypeColor(item.type) }">
            {{ getTypeText(item.type) }}
          </view>
        </view>
        
        <view class="supervision-content">
          <view class="content-item">
            <uni-icons type="person" size="14" color="#8C8C8C" />
            <text>监督员：{{ item.supervisor }}</text>
          </view>
          <view class="content-item">
            <uni-icons type="calendar" size="14" color="#8C8C8C" />
            <text>开始时间：{{ item.startTime }}</text>
          </view>
          <view class="content-item" v-if="item.endTime">
            <uni-icons type="clock" size="14" color="#8C8C8C" />
            <text>结束时间：{{ item.endTime }}</text>
          </view>
          <view class="content-item">
            <uni-icons type="location" size="14" color="#8C8C8C" />
            <text>监督对象：{{ item.target }}</text>
          </view>
        </view>
        
        <!-- 问题统计 -->
        <view class="issue-stats" v-if="item.issueStats">
          <view class="stats-row">
            <view class="stat-item">
              <text class="stat-label">发现问题</text>
              <text class="stat-value danger">{{ item.issueStats.total }}</text>
            </view>
            <view class="stat-item">
              <text class="stat-label">已整改</text>
              <text class="stat-value success">{{ item.issueStats.resolved }}</text>
            </view>
            <view class="stat-item">
              <text class="stat-label">整改中</text>
              <text class="stat-value warning">{{ item.issueStats.pending }}</text>
            </view>
          </view>
          
          <!-- 整改进度 -->
          <view class="progress-section" v-if="item.issueStats.total > 0">
            <view class="progress-header">
              <text class="progress-text">整改进度</text>
              <text class="progress-percent">{{ getProgressPercent(item.issueStats) }}%</text>
            </view>
            <view class="progress-bar">
              <view 
                class="progress-fill" 
                :style="{ width: getProgressPercent(item.issueStats) + '%' }"
              ></view>
            </view>
          </view>
        </view>
        
        <view class="supervision-actions">
          <button 
            class="action-btn secondary" 
            @click.stop="viewReport(item)"
          >
            查看报告
          </button>
          <button 
            class="action-btn secondary"
            @click.stop="viewDetail(item)"
          >
            查看详情
          </button>
          <button 
            class="action-btn primary" 
            v-if="item.status === 'in_progress'"
            @click.stop="addRecord(item)"
          >
            添加记录
          </button>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-if="supervisionList.length === 0">
        <uni-icons type="eye" size="64" color="#D9D9D9" />
        <text class="empty-text">暂无监督记录</text>
        <button class="empty-btn" @click="applySupervision">申请监督</button>
      </view>
    </view>

    <!-- 浮动申请按钮 -->
    <view class="floating-apply" @click="applySupervision">
      <uni-icons type="plus" size="24" color="#FFFFFF" />
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeStatus: 'all',
      statsInfo: {
        totalSupervision: 45,
        activeSupervision: 8,
        completedSupervision: 35,
        improvementRate: 92
      },
      supervisionTypes: [
        {
          value: 'service',
          name: '服务监督',
          icon: 'star',
          bgColor: '#1890FF',
          count: 15
        },
        {
          value: 'finance',
          name: '财务监督',
          icon: 'wallet',
          bgColor: '#52C41A',
          count: 8
        },
        {
          value: 'maintenance',
          name: '维护监督',
          icon: 'settings',
          bgColor: '#FA8C16',
          count: 12
        },
        {
          value: 'management',
          name: '管理监督',
          icon: 'person',
          bgColor: '#722ED1',
          count: 10
        }
      ],
      statusTabs: [
        { label: '全部', value: 'all', count: 0 },
        { label: '计划中', value: 'planned', count: 3 },
        { label: '进行中', value: 'in_progress', count: 5 },
        { label: '已完成', value: 'completed', count: 35 },
        { label: '已暂停', value: 'suspended', count: 2 }
      ],
      supervisionList: []
    }
  },
  onLoad() {
    this.loadSupervisionList()
  },
  onPullDownRefresh() {
    this.loadSupervisionList().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    loadSupervisionList() {
      // 模拟监督列表数据
      const mockData = [
        {
          supervisionId: 1,
          supervisionNo: 'JD202412001',
          title: '物业服务质量专项监督',
          type: 'service',
          status: 'completed',
          supervisor: '李监督员',
          startTime: '2024-12-01 09:00',
          endTime: '2024-12-05 17:00',
          target: '物业服务企业',
          issueStats: {
            total: 8,
            resolved: 7,
            pending: 1
          },
          priority: 'high',
          description: '针对小区物业服务质量进行全面监督检查'
        },
        {
          supervisionId: 2,
          supervisionNo: 'JD202412002',
          title: '公共维修资金使用监督',
          type: 'finance',
          status: 'in_progress',
          supervisor: '王监督员',
          startTime: '2024-12-10 14:00',
          endTime: '',
          target: '业主委员会',
          issueStats: {
            total: 3,
            resolved: 1,
            pending: 2
          },
          priority: 'medium',
          description: '监督公共维修资金的申请、审批和使用情况'
        },
        {
          supervisionId: 3,
          supervisionNo: 'JD202412003',
          title: '设施设备维护监督',
          type: 'maintenance',
          status: 'in_progress',
          supervisor: '张监督员',
          startTime: '2024-12-08 10:00',
          endTime: '',
          target: '物业服务企业',
          issueStats: {
            total: 5,
            resolved: 3,
            pending: 2
          },
          priority: 'high',
          description: '监督小区公共设施设备的维护保养工作'
        },
        {
          supervisionId: 4,
          supervisionNo: 'JD202412004',
          title: '业主大会决议执行监督',
          type: 'management',
          status: 'planned',
          supervisor: '陈监督员',
          startTime: '2024-12-15 09:00',
          endTime: '',
          target: '业主委员会',
          issueStats: {
            total: 0,
            resolved: 0,
            pending: 0
          },
          priority: 'medium',
          description: '监督业主大会决议的执行落实情况'
        },
        {
          supervisionId: 5,
          supervisionNo: 'JD202412005',
          title: '安全管理专项监督',
          type: 'management',
          status: 'completed',
          supervisor: '刘监督员',
          startTime: '2024-11-20 08:30',
          endTime: '2024-11-25 18:00',
          target: '物业服务企业',
          issueStats: {
            total: 6,
            resolved: 6,
            pending: 0
          },
          priority: 'high',
          description: '针对小区安全管理制度执行情况进行监督'
        }
      ]
      
      // 根据状态筛选
      if (this.activeStatus === 'all') {
        this.supervisionList = mockData
      } else {
        this.supervisionList = mockData.filter(item => item.status === this.activeStatus)
      }
      
      return Promise.resolve()
    },
    
    switchStatus(status) {
      this.activeStatus = status
      this.loadSupervisionList()
    },
    
    filterByType(type) {
      // 可以添加按类型筛选的逻辑
      uni.showToast({
        title: `筛选${this.getTypeText(type)}`,
        icon: 'none'
      })
    },
    
    getStatusClass(status) {
      const classMap = {
        'planned': 'status-planned',
        'in_progress': 'status-progress',
        'completed': 'status-completed',
        'suspended': 'status-suspended'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        'planned': '计划中',
        'in_progress': '进行中',
        'completed': '已完成',
        'suspended': '已暂停'
      }
      return textMap[status] || '未知状态'
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
    
    getTypeColor(type) {
      const colorMap = {
        'service': '#1890FF',
        'finance': '#52C41A',
        'maintenance': '#FA8C16',
        'management': '#722ED1'
      }
      return colorMap[type] || '#8C8C8C'
    },
    
    getProgressPercent(issueStats) {
      if (issueStats.total === 0) return 0
      return Math.round((issueStats.resolved / issueStats.total) * 100)
    },
    
    viewDetail(item) {
      uni.navigateTo({
        url: `/pages/supervision/detail?id=${item.supervisionId}`
      })
    },
    
    viewReport(item) {
      uni.navigateTo({
        url: `/pages/supervision/report?id=${item.supervisionId}`
      })
    },
    
    addRecord(item) {
      uni.navigateTo({
        url: `/pages/supervision/record?id=${item.supervisionId}`
      })
    },
    
    applySupervision() {
      uni.navigateTo({
        url: '/pages/supervision/apply'
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

.supervision-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.service-intro {
  background: linear-gradient(135deg, #722ED1 0%, #531DAB 100%);
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
        color: #722ED1;
        margin-bottom: 8rpx;
      }
      
      .stat-label {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
}

.supervision-types {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .types-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .types-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .types-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 24rpx;
    
    .type-item {
      text-align: center;
      
      &:active {
        transform: scale(0.95);
      }
      
      .type-icon {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 16rpx;
      }
      
      .type-name {
        display: block;
        font-size: 24rpx;
        font-weight: 500;
        color: #262626;
        margin-bottom: 4rpx;
      }
      
      .type-count {
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
        background: #F3E8FF;
        
        text {
          color: #722ED1;
          font-weight: 600;
        }
      }
    }
  }
}

.supervision-list {
  .supervision-item {
    background: #FFFFFF;
    margin: 0 20rpx 20rpx;
    border-radius: 24rpx;
    padding: 30rpx;
    border: 1rpx solid #F0F0F0;
    
    &:active {
      background: #F8F9FA;
    }
    
    .supervision-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 20rpx;
      
      .supervision-info {
        flex: 1;
        margin-right: 20rpx;
        
        .supervision-title {
          display: block;
          font-size: 28rpx;
          font-weight: 600;
          color: #262626;
          margin-bottom: 12rpx;
        }
        
        .supervision-meta {
          display: flex;
          align-items: center;
          gap: 16rpx;
          
          .supervision-no {
            font-size: 22rpx;
            color: #8C8C8C;
          }
          
          .supervision-status {
            padding: 4rpx 12rpx;
            border-radius: 12rpx;
            font-size: 20rpx;
            font-weight: 500;
            
            &.status-planned {
              background: #F0F8FF;
              color: #1890FF;
            }
            
            &.status-progress {
              background: #FFF2E8;
              color: #FA8C16;
            }
            
            &.status-completed {
              background: #F6FFED;
              color: #52C41A;
            }
            
            &.status-suspended {
              background: #F5F5F5;
              color: #8C8C8C;
            }
          }
        }
      }
      
      .supervision-type {
        padding: 8rpx 16rpx;
        border-radius: 16rpx;
        font-size: 22rpx;
        font-weight: 500;
        color: #FFFFFF;
        white-space: nowrap;
      }
    }
    
    .supervision-content {
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
    
    .issue-stats {
      background: #F8F9FA;
      border-radius: 16rpx;
      padding: 24rpx;
      margin-bottom: 20rpx;
      
      .stats-row {
        display: flex;
        justify-content: space-around;
        margin-bottom: 20rpx;
        
        .stat-item {
          text-align: center;
          
          .stat-label {
            display: block;
            font-size: 22rpx;
            color: #8C8C8C;
            margin-bottom: 8rpx;
          }
          
          .stat-value {
            font-size: 28rpx;
            font-weight: 600;
            
            &.danger {
              color: #FF4D4F;
            }
            
            &.success {
              color: #52C41A;
            }
            
            &.warning {
              color: #FA8C16;
            }
          }
        }
      }
      
      .progress-section {
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
            color: #52C41A;
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
            background: #52C41A;
            border-radius: 4rpx;
            transition: width 0.3s ease;
          }
        }
      }
    }
    
    .supervision-actions {
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
          background: #722ED1;
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
    background: #722ED1;
    color: #FFFFFF;
    height: 80rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    padding: 0 40rpx;
    
    &:active {
      background: #531DAB;
    }
  }
}

.floating-apply {
  position: fixed;
  bottom: 160rpx;
  right: 40rpx;
  width: 96rpx;
  height: 96rpx;
  background: #722ED1;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(114, 46, 209, 0.3);
  z-index: 999;
  
  &:active {
    background: #531DAB;
    transform: scale(0.95);
  }
}
</style>