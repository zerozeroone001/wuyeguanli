<template>
  <view class="handover-container">
    <!-- 服务介绍 -->
    <view class="service-intro">
      <view class="intro-content">
        <view class="intro-header">
          <uni-icons type="checkmarkempty" size="32" color="#FFFFFF" />
          <view class="intro-text">
            <text class="intro-title">承接查验服务</text>
            <text class="intro-subtitle">专业查验 · 规范承接 · 质量保障</text>
          </view>
        </view>
        <text class="intro-description">
          为物业项目承接提供专业的查验服务，确保物业设施设备完好，承接手续规范，维护业主权益。
        </text>
      </view>
    </view>

    <!-- 服务统计 -->
    <view class="service-stats">
      <view class="stats-grid">
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.totalHandover }}</text>
          <text class="stat-label">累计查验</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.activeHandover }}</text>
          <text class="stat-label">进行中</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.completedHandover }}</text>
          <text class="stat-label">已完成</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ statsInfo.qualityRate }}%</text>
          <text class="stat-label">合格率</text>
        </view>
      </view>
    </view>

    <!-- 查验类型 -->
    <view class="handover-types">
      <view class="types-header">
        <uni-icons type="list" size="20" color="#262626" />
        <text class="types-title">查验类型</text>
      </view>
      
      <view class="types-grid">
        <view 
          class="type-item" 
          v-for="type in handoverTypes" 
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

    <!-- 查验列表 -->
    <view class="handover-list">
      <view 
        class="handover-item" 
        v-for="item in handoverList" 
        :key="item.handoverId"
        @click="viewDetail(item)"
      >
        <view class="handover-header">
          <view class="handover-info">
            <text class="handover-title">{{ item.title }}</text>
            <view class="handover-meta">
              <text class="handover-no">编号：{{ item.handoverNo }}</text>
              <view class="handover-status" :class="['status-' + item.status]">
                {{ getStatusText(item.status) }}
              </view>
            </view>
          </view>
          <view class="handover-type" :style="{ backgroundColor: getTypeColor(item.type) }">
            {{ getTypeText(item.type) }}
          </view>
        </view>
        
        <view class="handover-content">
          <view class="content-item">
            <uni-icons type="person" size="14" color="#8C8C8C" />
            <text>查验员：{{ item.inspector }}</text>
          </view>
          <view class="content-item">
            <uni-icons type="calendar" size="14" color="#8C8C8C" />
            <text>查验时间：{{ item.inspectionTime }}</text>
          </view>
          <view class="content-item" v-if="item.handoverTime">
            <uni-icons type="clock" size="14" color="#8C8C8C" />
            <text>承接时间：{{ item.handoverTime }}</text>
          </view>
          <view class="content-item">
            <uni-icons type="location" size="14" color="#8C8C8C" />
            <text>项目地址：{{ item.projectAddress }}</text>
          </view>
        </view>
        
        <!-- 查验结果统计 -->
        <view class="inspection-stats" v-if="item.inspectionStats">
          <view class="stats-row">
            <view class="stat-item">
              <text class="stat-label">检查项目</text>
              <text class="stat-value">{{ item.inspectionStats.total }}</text>
            </view>
            <view class="stat-item">
              <text class="stat-label">合格</text>
              <text class="stat-value success">{{ item.inspectionStats.passed }}</text>
            </view>
            <view class="stat-item">
              <text class="stat-label">不合格</text>
              <text class="stat-value danger">{{ item.inspectionStats.failed }}</text>
            </view>
          </view>
          
          <!-- 合格率 -->
          <view class="quality-section" v-if="item.inspectionStats.total > 0">
            <view class="quality-header">
              <text class="quality-text">合格率</text>
              <text class="quality-percent">{{ getQualityPercent(item.inspectionStats) }}%</text>
            </view>
            <view class="quality-bar">
              <view 
                class="quality-fill" 
                :style="{ width: getQualityPercent(item.inspectionStats) + '%' }"
              ></view>
            </view>
          </view>
        </view>
        
        <view class="handover-actions">
          <button 
            class="action-btn secondary" 
            @click.stop="viewReport(item)"
          >
            查验报告
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
      <view class="empty-state" v-if="handoverList.length === 0">
        <uni-icons type="checkmarkempty" size="64" color="#D9D9D9" />
        <text class="empty-text">暂无查验记录</text>
        <button class="empty-btn" @click="applyHandover">申请查验</button>
      </view>
    </view>

    <!-- 浮动申请按钮 -->
    <view class="floating-apply" @click="applyHandover">
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
        totalHandover: 28,
        activeHandover: 3,
        completedHandover: 23,
        qualityRate: 95
      },
      handoverTypes: [
        {
          value: 'new_project',
          name: '新建项目',
          icon: 'home',
          bgColor: '#1890FF',
          count: 12
        },
        {
          value: 'renovation',
          name: '改造项目',
          icon: 'settings',
          bgColor: '#52C41A',
          count: 8
        },
        {
          value: 'equipment',
          name: '设备更新',
          icon: 'gear',
          bgColor: '#FA8C16',
          count: 6
        },
        {
          value: 'maintenance',
          name: '维修项目',
          icon: 'compose',
          bgColor: '#13C2C2',
          count: 2
        }
      ],
      statusTabs: [
        { label: '全部', value: 'all', count: 0 },
        { label: '计划中', value: 'planned', count: 2 },
        { label: '查验中', value: 'in_progress', count: 1 },
        { label: '已完成', value: 'completed', count: 23 },
        { label: '需整改', value: 'need_rectification', count: 2 }
      ],
      handoverList: []
    }
  },
  onLoad() {
    this.loadHandoverList()
  },
  onPullDownRefresh() {
    this.loadHandoverList().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    loadHandoverList() {
      // 模拟查验列表数据
      const mockData = [
        {
          handoverId: 1,
          handoverNo: 'CJ202412001',
          title: '智慧花园二期承接查验',
          type: 'new_project',
          status: 'completed',
          inspector: '张查验员',
          inspectionTime: '2024-12-01 09:00',
          handoverTime: '2024-12-05 17:00',
          projectAddress: '北京市朝阳区建国路88号',
          inspectionStats: {
            total: 156,
            passed: 148,
            failed: 8
          },
          priority: 'high',
          description: '智慧花园二期项目承接查验，包括建筑主体、设备设施、配套工程等全面检查'
        },
        {
          handoverId: 2,
          handoverNo: 'CJ202412002',
          title: '电梯系统更新查验',
          type: 'equipment',
          status: 'in_progress',
          inspector: '李查验员',
          inspectionTime: '2024-12-10 14:00',
          handoverTime: '',
          projectAddress: '北京市朝阳区建国路88号',
          inspectionStats: {
            total: 24,
            passed: 20,
            failed: 4
          },
          priority: 'medium',
          description: '小区电梯系统更新改造后的承接查验工作'
        },
        {
          handoverId: 3,
          handoverNo: 'CJ202412003',
          title: '地下车库改造查验',
          type: 'renovation',
          status: 'need_rectification',
          inspector: '王查验员',
          inspectionTime: '2024-12-08 10:00',
          handoverTime: '',
          projectAddress: '北京市朝阳区建国路88号',
          inspectionStats: {
            total: 45,
            passed: 38,
            failed: 7
          },
          priority: 'high',
          description: '地下车库防水改造工程承接查验'
        },
        {
          handoverId: 4,
          handoverNo: 'CJ202412004',
          title: '消防系统维修查验',
          type: 'maintenance',
          status: 'planned',
          inspector: '陈查验员',
          inspectionTime: '2024-12-15 09:00',
          handoverTime: '',
          projectAddress: '北京市朝阳区建国路88号',
          inspectionStats: {
            total: 0,
            passed: 0,
            failed: 0
          },
          priority: 'high',
          description: '消防系统维修工程完工后的承接查验'
        },
        {
          handoverId: 5,
          handoverNo: 'CJ202412005',
          title: '绿化景观改造查验',
          type: 'renovation',
          status: 'completed',
          inspector: '刘查验员',
          inspectionTime: '2024-11-20 08:30',
          handoverTime: '2024-11-22 18:00',
          projectAddress: '北京市朝阳区建国路88号',
          inspectionStats: {
            total: 32,
            passed: 32,
            failed: 0
          },
          priority: 'medium',
          description: '小区绿化景观改造工程承接查验'
        }
      ]
      
      // 根据状态筛选
      if (this.activeStatus === 'all') {
        this.handoverList = mockData
      } else {
        this.handoverList = mockData.filter(item => item.status === this.activeStatus)
      }
      
      return Promise.resolve()
    },
    
    switchStatus(status) {
      this.activeStatus = status
      this.loadHandoverList()
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
        'need_rectification': 'status-rectification'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        'planned': '计划中',
        'in_progress': '查验中',
        'completed': '已完成',
        'need_rectification': '需整改'
      }
      return textMap[status] || '未知状态'
    },
    
    getTypeText(type) {
      const textMap = {
        'new_project': '新建项目',
        'renovation': '改造项目',
        'equipment': '设备更新',
        'maintenance': '维修项目'
      }
      return textMap[type] || '其他'
    },
    
    getTypeColor(type) {
      const colorMap = {
        'new_project': '#1890FF',
        'renovation': '#52C41A',
        'equipment': '#FA8C16',
        'maintenance': '#13C2C2'
      }
      return colorMap[type] || '#8C8C8C'
    },
    
    getQualityPercent(inspectionStats) {
      if (inspectionStats.total === 0) return 0
      return Math.round((inspectionStats.passed / inspectionStats.total) * 100)
    },
    
    viewDetail(item) {
      uni.navigateTo({
        url: `/pages/handover/detail?id=${item.handoverId}`
      })
    },
    
    viewReport(item) {
      uni.navigateTo({
        url: `/pages/handover/report?id=${item.handoverId}`
      })
    },
    
    addRecord(item) {
      uni.navigateTo({
        url: `/pages/handover/record?id=${item.handoverId}`
      })
    },
    
    applyHandover() {
      uni.navigateTo({
        url: '/pages/handover/apply'
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

.handover-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.service-intro {
  background: linear-gradient(135deg, #13C2C2 0%, #08979C 100%);
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
        color: #13C2C2;
        margin-bottom: 8rpx;
      }
      
      .stat-label {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
}

.handover-types {
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
        background: #E6FFFB;
        
        text {
          color: #13C2C2;
          font-weight: 600;
        }
      }
    }
  }
}

.handover-list {
  .handover-item {
    background: #FFFFFF;
    margin: 0 20rpx 20rpx;
    border-radius: 24rpx;
    padding: 30rpx;
    border: 1rpx solid #F0F0F0;
    
    &:active {
      background: #F8F9FA;
    }
    
    .handover-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 20rpx;
      
      .handover-info {
        flex: 1;
        margin-right: 20rpx;
        
        .handover-title {
          display: block;
          font-size: 28rpx;
          font-weight: 600;
          color: #262626;
          margin-bottom: 12rpx;
        }
        
        .handover-meta {
          display: flex;
          align-items: center;
          gap: 16rpx;
          
          .handover-no {
            font-size: 22rpx;
            color: #8C8C8C;
          }
          
          .handover-status {
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
            
            &.status-rectification {
              background: #FFF2F0;
              color: #FF4D4F;
            }
          }
        }
      }
      
      .handover-type {
        padding: 8rpx 16rpx;
        border-radius: 16rpx;
        font-size: 22rpx;
        font-weight: 500;
        color: #FFFFFF;
        white-space: nowrap;
      }
    }
    
    .handover-content {
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
    
    .inspection-stats {
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
            color: #262626;
            
            &.success {
              color: #52C41A;
            }
            
            &.danger {
              color: #FF4D4F;
            }
          }
        }
      }
      
      .quality-section {
        .quality-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 12rpx;
          
          .quality-text {
            font-size: 24rpx;
            color: #595959;
          }
          
          .quality-percent {
            font-size: 24rpx;
            color: #13C2C2;
            font-weight: 600;
          }
        }
        
        .quality-bar {
          height: 8rpx;
          background: #F0F0F0;
          border-radius: 4rpx;
          overflow: hidden;
          
          .quality-fill {
            height: 100%;
            background: #13C2C2;
            border-radius: 4rpx;
            transition: width 0.3s ease;
          }
        }
      }
    }
    
    .handover-actions {
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
          background: #13C2C2;
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
    background: #13C2C2;
    color: #FFFFFF;
    height: 80rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    padding: 0 40rpx;
    
    &:active {
      background: #08979C;
    }
  }
}

.floating-apply {
  position: fixed;
  bottom: 160rpx;
  right: 40rpx;
  width: 96rpx;
  height: 96rpx;
  background: #13C2C2;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(19, 194, 194, 0.3);
  z-index: 999;
  
  &:active {
    background: #08979C;
    transform: scale(0.95);
  }
}
</style>