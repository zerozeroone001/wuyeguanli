<template>
  <view class="detail-container">
    <!-- 查验基本信息 -->
    <view class="handover-info">
      <view class="info-header">
        <text class="handover-title">{{ handoverInfo.title }}</text>
        <view class="handover-status" :class="['status-' + handoverInfo.status]">
          {{ getStatusText(handoverInfo.status) }}
        </view>
      </view>
      
      <view class="handover-meta">
        <view class="meta-item">
          <uni-icons type="list" size="14" color="#8C8C8C" />
          <text>编号：{{ handoverInfo.handoverNo }}</text>
        </view>
        <view class="meta-item">
          <uni-icons type="calendar" size="14" color="#8C8C8C" />
          <text>查验时间：{{ handoverInfo.inspectionTime }}</text>
        </view>
        <view class="meta-item" v-if="handoverInfo.handoverTime">
          <uni-icons type="clock" size="14" color="#8C8C8C" />
          <text>承接时间：{{ handoverInfo.handoverTime }}</text>
        </view>
        <view class="meta-item">
          <uni-icons type="person" size="14" color="#8C8C8C" />
          <text>查验员：{{ handoverInfo.inspector }}</text>
        </view>
      </view>
      
      <view class="handover-type-badge" :style="{ backgroundColor: getTypeColor(handoverInfo.type) }">
        {{ getTypeText(handoverInfo.type) }}
      </view>
      
      <!-- 优先级标识 -->
      <view class="priority-badge" :class="['priority-' + handoverInfo.priority]">
        <uni-icons :type="getPriorityIcon(handoverInfo.priority)" size="12" />
        <text>{{ getPriorityText(handoverInfo.priority) }}</text>
      </view>
    </view>

    <!-- 项目信息 -->
    <view class="project-info">
      <view class="section-header">
        <uni-icons type="home" size="20" color="#262626" />
        <text class="section-title">项目信息</text>
      </view>
      
      <view class="project-content">
        <view class="project-item">
          <text class="project-label">项目名称：</text>
          <text class="project-value">{{ handoverInfo.projectName }}</text>
        </view>
        <view class="project-item">
          <text class="project-label">项目地址：</text>
          <text class="project-value">{{ handoverInfo.projectAddress }}</text>
        </view>
        <view class="project-item">
          <text class="project-label">建设单位：</text>
          <text class="project-value">{{ handoverInfo.developer }}</text>
        </view>
        <view class="project-item">
          <text class="project-label">施工单位：</text>
          <text class="project-value">{{ handoverInfo.contractor }}</text>
        </view>
        <view class="project-item">
          <text class="project-label">项目规模：</text>
          <text class="project-value">{{ handoverInfo.projectScale }}</text>
        </view>
        <view class="project-item">
          <text class="project-label">竣工时间：</text>
          <text class="project-value">{{ handoverInfo.completionTime }}</text>
        </view>
      </view>
    </view>

    <!-- 查验说明 -->
    <view class="description-section" v-if="handoverInfo.description">
      <view class="section-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="section-title">查验说明</text>
      </view>
      
      <view class="description-content">
        <text>{{ handoverInfo.description }}</text>
      </view>
    </view>

    <!-- 查验结果统计 -->
    <view class="inspection-overview" v-if="handoverInfo.inspectionStats">
      <view class="section-header">
        <uni-icons type="bar-chart" size="20" color="#262626" />
        <text class="section-title">查验结果统计</text>
      </view>
      
      <view class="stats-grid">
        <view class="stat-card total">
          <view class="stat-icon">
            <uni-icons type="list" size="24" color="#13C2C2" />
          </view>
          <view class="stat-info">
            <text class="stat-number">{{ handoverInfo.inspectionStats.total }}</text>
            <text class="stat-label">检查项目</text>
          </view>
        </view>
        <view class="stat-card passed">
          <view class="stat-icon">
            <uni-icons type="checkmarkempty" size="24" color="#52C41A" />
          </view>
          <view class="stat-info">
            <text class="stat-number">{{ handoverInfo.inspectionStats.passed }}</text>
            <text class="stat-label">合格</text>
          </view>
        </view>
        <view class="stat-card failed">
          <view class="stat-icon">
            <uni-icons type="close" size="24" color="#FF4D4F" />
          </view>
          <view class="stat-info">
            <text class="stat-number">{{ handoverInfo.inspectionStats.failed }}</text>
            <text class="stat-label">不合格</text>
          </view>
        </view>
        <view class="stat-card rectified">
          <view class="stat-icon">
            <uni-icons type="refresh" size="24" color="#FA8C16" />
          </view>
          <view class="stat-info">
            <text class="stat-number">{{ handoverInfo.inspectionStats.rectified || 0 }}</text>
            <text class="stat-label">已整改</text>
          </view>
        </view>
      </view>
      
      <!-- 合格率 -->
      <view class="quality-section" v-if="handoverInfo.inspectionStats.total > 0">
        <view class="quality-header">
          <text class="quality-text">合格率</text>
          <text class="quality-percent">{{ getQualityPercent() }}%</text>
        </view>
        <view class="quality-bar">
          <view class="quality-fill" :style="{ width: getQualityPercent() + '%' }"></view>
        </view>
      </view>
    </view>

    <!-- 查验项目详情 -->
    <view class="inspection-items">
      <view class="section-header">
        <uni-icons type="list" size="20" color="#262626" />
        <text class="section-title">查验项目详情</text>
        <text class="item-count">{{ inspectionItems.length }}项</text>
      </view>
      
      <!-- 分类筛选 -->
      <view class="category-filter">
        <view 
          class="filter-chip" 
          :class="{ active: activeCategory === category.value }"
          v-for="category in itemCategories" 
          :key="category.value"
          @click="filterByCategory(category.value)"
        >
          <text>{{ category.label }}</text>
          <view class="chip-count" v-if="category.count > 0">{{ category.count }}</view>
        </view>
      </view>
      
      <view class="item-list">
        <view 
          class="item-group" 
          v-for="group in groupedItems" 
          :key="group.category"
        >
          <view class="group-header">
            <uni-icons :type="getCategoryIcon(group.category)" size="18" color="#262626" />
            <text class="group-title">{{ getCategoryText(group.category) }}</text>
            <text class="group-count">{{ group.items.length }}项</text>
          </view>
          
          <view class="group-items">
            <view 
              class="inspection-item" 
              v-for="item in group.items" 
              :key="item.id"
              @click="viewItemDetail(item)"
            >
              <view class="item-icon" :class="['item-' + item.status]">
                <uni-icons :type="getItemStatusIcon(item.status)" size="16" />
              </view>
              <view class="item-info">
                <text class="item-name">{{ item.name }}</text>
                <text class="item-desc">{{ item.description }}</text>
                <view class="item-meta" v-if="item.issue">
                  <text class="issue-text">问题：{{ item.issue }}</text>
                  <text class="rectification-date" v-if="item.rectificationDate">
                    整改期限：{{ item.rectificationDate }}
                  </text>
                </view>
              </view>
              <view class="item-status" :class="['item-' + item.status]">
                <text>{{ getItemStatusText(item.status) }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 查验记录 -->
    <view class="inspection-records">
      <view class="section-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="section-title">查验记录</text>
        <text class="record-count">{{ recordList.length }}条</text>
      </view>
      
      <view class="record-timeline">
        <view 
          class="timeline-item" 
          v-for="(record, index) in recordList" 
          :key="record.id"
        >
          <view class="timeline-dot" :class="['dot-' + record.type]"></view>
          <view class="timeline-content">
            <view class="record-header">
              <text class="record-title">{{ record.title }}</text>
              <text class="record-time">{{ record.createTime }}</text>
            </view>
            <text class="record-content">{{ record.content }}</text>
            
            <!-- 附件 -->
            <view class="record-attachments" v-if="record.attachments && record.attachments.length > 0">
              <text class="attachment-title">相关附件：</text>
              <view class="attachment-list">
                <view 
                  class="attachment-item" 
                  v-for="attachment in record.attachments" 
                  :key="attachment.id"
                  @click="previewAttachment(attachment)"
                >
                  <uni-icons type="paperplane" size="16" color="#13C2C2" />
                  <text class="attachment-name">{{ attachment.name }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 查验报告 -->
    <view class="inspection-report" v-if="handoverInfo.status === 'completed'">
      <view class="section-header">
        <uni-icons type="medal" size="20" color="#262626" />
        <text class="section-title">查验报告</text>
      </view>
      
      <view class="report-card">
        <view class="report-header">
          <view class="report-icon">
            <uni-icons type="medal" size="32" color="#13C2C2" />
          </view>
          <view class="report-info">
            <text class="report-title">{{ handoverInfo.reportTitle }}</text>
            <text class="report-no">报告编号：{{ handoverInfo.reportNo }}</text>
            <text class="report-date">出具日期：{{ handoverInfo.reportDate }}</text>
          </view>
        </view>
        
        <view class="report-summary">
          <text class="summary-title">查验结论：</text>
          <text class="summary-content">{{ handoverInfo.reportSummary }}</text>
        </view>
        
        <view class="report-actions">
          <button class="report-btn secondary" @click="previewReport">
            预览报告
          </button>
          <button class="report-btn primary" @click="downloadReport">
            下载报告
          </button>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button 
        class="action-btn secondary" 
        v-if="handoverInfo.status === 'in_progress'"
        @click="addRecord"
      >
        添加记录
      </button>
      <button 
        class="action-btn secondary" 
        @click="shareHandover"
      >
        分享
      </button>
      <button 
        class="action-btn primary" 
        v-if="handoverInfo.status === 'completed'"
        @click="downloadReport"
      >
        下载报告
      </button>
      <button 
        class="action-btn secondary" 
        v-if="canRectify"
        @click="requestRectification"
      >
        要求整改
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      handoverId: null,
      handoverInfo: {},
      inspectionItems: [],
      recordList: [],
      activeCategory: 'all',
      itemCategories: [
        { label: '全部', value: 'all', count: 0 },
        { label: '建筑主体', value: 'structure', count: 0 },
        { label: '设备设施', value: 'equipment', count: 0 },
        { label: '配套工程', value: 'supporting', count: 0 },
        { label: '环境景观', value: 'landscape', count: 0 }
      ]
    }
  },
  computed: {
    canRectify() {
      return this.handoverInfo.status === 'in_progress' && 
             this.handoverInfo.inspectionStats && 
             this.handoverInfo.inspectionStats.failed > 0
    },
    
    groupedItems() {
      if (this.activeCategory === 'all') {
        const groups = {}
        this.inspectionItems.forEach(item => {
          if (!groups[item.category]) {
            groups[item.category] = {
              category: item.category,
              items: []
            }
          }
          groups[item.category].items.push(item)
        })
        return Object.values(groups)
      } else {
        const filteredItems = this.inspectionItems.filter(item => item.category === this.activeCategory)
        return [{
          category: this.activeCategory,
          items: filteredItems
        }]
      }
    }
  },
  onLoad(options) {
    this.handoverId = options.id
    this.loadHandoverDetail()
  },
  methods: {
    loadHandoverDetail() {
      // 模拟查验详情数据
      this.handoverInfo = {
        handoverId: 1,
        handoverNo: 'CJ202412001',
        title: '智慧花园二期承接查验',
        type: 'new_project',
        status: 'completed',
        priority: 'high',
        inspector: '张查验员',
        inspectionTime: '2024-12-01 09:00',
        handoverTime: '2024-12-05 17:00',
        projectName: '智慧花园二期住宅项目',
        projectAddress: '北京市朝阳区建国路88号',
        developer: '北京智慧地产开发有限公司',
        contractor: '北京建工集团有限责任公司',
        projectScale: '总建筑面积15万平方米，住宅楼12栋，商业配套2栋',
        completionTime: '2024-11-15',
        description: '智慧花园二期项目承接查验，包括建筑主体、设备设施、配套工程等全面检查，确保项目质量符合承接标准。',
        inspectionStats: {
          total: 156,
          passed: 148,
          failed: 8,
          rectified: 6
        },
        reportTitle: '智慧花园二期承接查验报告',
        reportNo: 'CJ-BG-202412001',
        reportDate: '2024-12-05',
        reportSummary: '经过全面查验，项目整体质量良好，发现的8个问题已整改6个，剩余2个问题已制定整改计划，建议在整改完成后正式承接。'
      }
      
      // 模拟查验项目
      this.inspectionItems = [
        {
          id: 1,
          name: '主体结构检查',
          category: 'structure',
          description: '检查建筑主体结构完整性和安全性',
          status: 'passed',
          issue: '',
          rectificationDate: ''
        },
        {
          id: 2,
          name: '外墙防水检查',
          category: 'structure',
          description: '检查外墙防水层完整性',
          status: 'failed',
          issue: '3号楼东侧外墙存在渗水现象',
          rectificationDate: '2024-12-10'
        },
        {
          id: 3,
          name: '电梯系统检查',
          category: 'equipment',
          description: '检查电梯运行状态和安全设施',
          status: 'passed',
          issue: '',
          rectificationDate: ''
        },
        {
          id: 4,
          name: '消防系统检查',
          category: 'equipment',
          description: '检查消防设备和报警系统',
          status: 'rectified',
          issue: '部分消防栓压力不足',
          rectificationDate: '2024-12-03'
        },
        {
          id: 5,
          name: '供水系统检查',
          category: 'equipment',
          description: '检查供水管道和水质',
          status: 'failed',
          issue: '二次供水设备需要调试',
          rectificationDate: '2024-12-08'
        },
        {
          id: 6,
          name: '道路工程检查',
          category: 'supporting',
          description: '检查小区道路质量和标识',
          status: 'passed',
          issue: '',
          rectificationDate: ''
        },
        {
          id: 7,
          name: '停车场检查',
          category: 'supporting',
          description: '检查地下停车场设施',
          status: 'passed',
          issue: '',
          rectificationDate: ''
        },
        {
          id: 8,
          name: '绿化工程检查',
          category: 'landscape',
          description: '检查绿化植被和景观设施',
          status: 'passed',
          issue: '',
          rectificationDate: ''
        }
      ]
      
      // 更新分类统计
      this.updateCategoryStats()
      
      // 模拟查验记录
      this.recordList = [
        {
          id: 1,
          title: '查验启动',
          type: 'start',
          content: '正式启动智慧花园二期项目承接查验工作，组建查验小组，制定查验计划。',
          createTime: '2024-12-01 09:00',
          attachments: [
            {
              id: 1,
              name: '查验计划.pdf',
              url: '/static/docs/plan.pdf'
            }
          ]
        },
        {
          id: 2,
          title: '主体结构查验',
          type: 'inspection',
          content: '对项目建筑主体结构进行全面检查，包括基础、墙体、楼板等结构安全检查。',
          createTime: '2024-12-02 10:30',
          attachments: [
            {
              id: 2,
              name: '结构检查报告.pdf',
              url: '/static/docs/structure.pdf'
            },
            {
              id: 3,
              name: '现场照片.zip',
              url: '/static/images/structure.zip'
            }
          ]
        },
        {
          id: 3,
          title: '设备设施查验',
          type: 'inspection',
          content: '对电梯、消防、供水等设备设施进行功能性检查，发现部分问题需要整改。',
          createTime: '2024-12-03 14:20',
          attachments: [
            {
              id: 4,
              name: '设备检查清单.xlsx',
              url: '/static/docs/equipment.xlsx'
            }
          ]
        },
        {
          id: 4,
          title: '整改跟进',
          type: 'follow_up',
          content: '跟进前期发现问题的整改情况，大部分问题已得到有效解决。',
          createTime: '2024-12-04 16:00',
          attachments: []
        },
        {
          id: 5,
          title: '查验总结',
          type: 'summary',
          content: '完成全面查验工作，形成查验报告，项目整体质量符合承接标准。',
          createTime: '2024-12-05 17:00',
          attachments: [
            {
              id: 5,
              name: '承接查验报告.pdf',
              url: '/static/docs/handover_report.pdf'
            }
          ]
        }
      ]
    },
    
    updateCategoryStats() {
      // 更新分类统计数据
      const stats = {
        all: this.inspectionItems.length,
        structure: this.inspectionItems.filter(item => item.category === 'structure').length,
        equipment: this.inspectionItems.filter(item => item.category === 'equipment').length,
        supporting: this.inspectionItems.filter(item => item.category === 'supporting').length,
        landscape: this.inspectionItems.filter(item => item.category === 'landscape').length
      }
      
      this.itemCategories.forEach(category => {
        category.count = stats[category.value] || 0
      })
    },
    
    filterByCategory(category) {
      this.activeCategory = category
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
    
    getPriorityClass(priority) {
      const classMap = {
        'high': 'priority-high',
        'medium': 'priority-medium',
        'low': 'priority-low'
      }
      return classMap[priority] || 'priority-default'
    },
    
    getPriorityText(priority) {
      const textMap = {
        'high': '高优先级',
        'medium': '中优先级',
        'low': '低优先级'
      }
      return textMap[priority] || '普通'
    },
    
    getPriorityIcon(priority) {
      const iconMap = {
        'high': 'fire',
        'medium': 'flag',
        'low': 'info'
      }
      return iconMap[priority] || 'info'
    },
    
    getCategoryIcon(category) {
      const iconMap = {
        'structure': 'home',
        'equipment': 'gear',
        'supporting': 'compose',
        'landscape': 'leaf'
      }
      return iconMap[category] || 'list'
    },
    
    getCategoryText(category) {
      const textMap = {
        'structure': '建筑主体',
        'equipment': '设备设施',
        'supporting': '配套工程',
        'landscape': '环境景观'
      }
      return textMap[category] || '其他'
    },
    
    getItemStatusClass(status) {
      const classMap = {
        'passed': 'item-passed',
        'failed': 'item-failed',
        'rectified': 'item-rectified'
      }
      return classMap[status] || 'item-default'
    },
    
    getItemStatusIcon(status) {
      const iconMap = {
        'passed': 'checkmarkempty',
        'failed': 'close',
        'rectified': 'refresh'
      }
      return iconMap[status] || 'help'
    },
    
    getItemStatusText(status) {
      const textMap = {
        'passed': '合格',
        'failed': '不合格',
        'rectified': '已整改'
      }
      return textMap[status] || '未知'
    },
    
    getRecordTypeClass(type) {
      const classMap = {
        'start': 'dot-start',
        'inspection': 'dot-inspection',
        'follow_up': 'dot-follow',
        'summary': 'dot-summary'
      }
      return classMap[type] || 'dot-default'
    },
    
    getQualityPercent() {
      const stats = this.handoverInfo.inspectionStats
      if (!stats || stats.total === 0) return 0
      return Math.round(((stats.passed + stats.rectified) / stats.total) * 100)
    },
    
    viewItemDetail(item) {
      uni.navigateTo({
        url: `/pages/handover/item?id=${item.id}`
      })
    },
    
    previewAttachment(attachment) {
      // 根据文件类型进行预览
      if (attachment.name.includes('.pdf')) {
        uni.navigateTo({
          url: `/pages/common/webview/webview?title=${attachment.name}&url=${attachment.url}`
        })
      } else {
        uni.previewImage({
          urls: [attachment.url]
        })
      }
    },
    
    addRecord() {
      uni.navigateTo({
        url: `/pages/handover/record?id=${this.handoverId}`
      })
    },
    
    previewReport() {
      uni.navigateTo({
        url: `/pages/handover/report?id=${this.handoverId}`
      })
    },
    
    downloadReport() {
      uni.showLoading({
        title: '下载中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '下载完成',
          icon: 'success'
        })
      }, 2000)
    },
    
    shareHandover() {
      uni.showActionSheet({
        itemList: ['分享给微信好友', '分享到朋友圈', '复制链接'],
        success: (res) => {
          const actions = ['微信好友', '朋友圈', '复制链接']
          uni.showToast({
            title: `分享到${actions[res.tapIndex]}`,
            icon: 'success'
          })
        }
      })
    },
    
    requestRectification() {
      uni.showModal({
        title: '要求整改',
        content: '确定要求对不合格项目进行整改吗？',
        confirmColor: '#FA8C16',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '处理中...'
            })
            
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({
                title: '整改要求已发送',
                icon: 'success'
              })
              
              this.handoverInfo.status = 'need_rectification'
            }, 1000)
          }
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
  padding-bottom: 120rpx;
}

.handover-info, .project-info, .description-section, .inspection-overview, 
.inspection-items, .inspection-records, .inspection-report {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24rpx;
    
    .section-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .item-count, .record-count {
      font-size: 24rpx;
      color: #8C8C8C;
    }
  }
}

.handover-info {
  position: relative;
  
  .info-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
    
    .handover-title {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
    }
    
    .handover-status {
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      font-size: 22rpx;
      font-weight: 500;
      white-space: nowrap;
      
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
  
  .handover-meta {
    display: flex;
    align-items: center;
    gap: 24rpx;
    flex-wrap: wrap;
    margin-bottom: 20rpx;
    
    .meta-item {
      display: flex;
      align-items: center;
      
      text {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .handover-type-badge {
    position: absolute;
    top: 40rpx;
    right: 40rpx;
    padding: 8rpx 16rpx;
    border-radius: 16rpx;
    font-size: 22rpx;
    font-weight: 500;
    color: #FFFFFF;
  }
  
  .priority-badge {
    display: inline-flex;
    align-items: center;
    padding: 4rpx 12rpx;
    border-radius: 12rpx;
    
    text {
      margin-left: 4rpx;
      font-size: 20rpx;
      font-weight: 500;
    }
    
    &.priority-high {
      background: #FFF2F0;
      color: #FF4D4F;
    }
    
    &.priority-medium {
      background: #FFF2E8;
      color: #FA8C16;
    }
    
    &.priority-low {
      background: #F0F8FF;
      color: #1890FF;
    }
  }
}

.project-content {
  .project-item {
    display: flex;
    margin-bottom: 16rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .project-label {
      font-size: 24rpx;
      color: #8C8C8C;
      min-width: 160rpx;
    }
    
    .project-value {
      font-size: 24rpx;
      color: #262626;
    }
  }
}

.description-content {
  text {
    font-size: 26rpx;
    color: #595959;
    line-height: 1.6;
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  margin-bottom: 30rpx;
  
  .stat-card {
    display: flex;
    align-items: center;
    padding: 24rpx;
    background: #F8F9FA;
    border-radius: 16rpx;
    
    .stat-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
    }
    
    .stat-info {
      .stat-number {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 4rpx;
      }
      
      .stat-label {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
    
    &.total .stat-icon {
      background: #E6FFFB;
    }
    
    &.passed .stat-icon {
      background: #F6FFED;
    }
    
    &.failed .stat-icon {
      background: #FFF2F0;
    }
    
    &.rectified .stat-icon {
      background: #FFF2E8;
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

.category-filter {
  display: flex;
  gap: 12rpx;
  margin-bottom: 24rpx;
  flex-wrap: wrap;
  
  .filter-chip {
    display: flex;
    align-items: center;
    padding: 8rpx 16rpx;
    background: #F8F9FA;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    position: relative;
    
    text {
      font-size: 24rpx;
      color: #8C8C8C;
      font-weight: 500;
    }
    
    .chip-count {
      margin-left: 8rpx;
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
      border-color: #13C2C2;
      
      text {
        color: #13C2C2;
        font-weight: 600;
      }
      
      .chip-count {
        background: #13C2C2;
      }
    }
  }
}

.item-list {
  .item-group {
    margin-bottom: 30rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .group-header {
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;
      padding-bottom: 16rpx;
      border-bottom: 1rpx solid #F0F0F0;
      
      .group-title {
        margin-left: 12rpx;
        font-size: 26rpx;
        font-weight: 600;
        color: #262626;
        flex: 1;
      }
      
      .group-count {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
    
    .group-items {
      .inspection-item {
        display: flex;
        align-items: flex-start;
        padding: 20rpx;
        background: #F8F9FA;
        border-radius: 16rpx;
        margin-bottom: 16rpx;
        
        &:last-child {
          margin-bottom: 0;
        }
        
        &:active {
          background: #F0F0F0;
        }
        
        .item-icon {
          width: 48rpx;
          height: 48rpx;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16rpx;
          margin-top: 4rpx;
          
          &.item-passed {
            background: #F6FFED;
            color: #52C41A;
          }
          
          &.item-failed {
            background: #FFF2F0;
            color: #FF4D4F;
          }
          
          &.item-rectified {
            background: #FFF2E8;
            color: #FA8C16;
          }
        }
        
        .item-info {
          flex: 1;
          
          .item-name {
            display: block;
            font-size: 26rpx;
            font-weight: 500;
            color: #262626;
            margin-bottom: 8rpx;
          }
          
          .item-desc {
            display: block;
            font-size: 24rpx;
            color: #595959;
            margin-bottom: 12rpx;
          }
          
          .item-meta {
            .issue-text {
              display: block;
              font-size: 22rpx;
              color: #FF4D4F;
              margin-bottom: 4rpx;
            }
            
            .rectification-date {
              font-size: 22rpx;
              color: #FA8C16;
            }
          }
        }
        
        .item-status {
          padding: 4rpx 12rpx;
          border-radius: 12rpx;
          font-size: 20rpx;
          font-weight: 500;
          margin-top: 4rpx;
          
          &.item-passed {
            background: #F6FFED;
            color: #52C41A;
          }
          
          &.item-failed {
            background: #FFF2F0;
            color: #FF4D4F;
          }
          
          &.item-rectified {
            background: #FFF2E8;
            color: #FA8C16;
          }
        }
      }
    }
  }
}

.record-timeline {
  .timeline-item {
    position: relative;
    padding-left: 60rpx;
    padding-bottom: 40rpx;
    
    &:last-child {
      padding-bottom: 0;
    }
    
    &:not(:last-child)::before {
      content: '';
      position: absolute;
      left: 19rpx;
      top: 40rpx;
      bottom: 0;
      width: 2rpx;
      background: #F0F0F0;
    }
    
    .timeline-dot {
      position: absolute;
      left: 0;
      top: 0;
      width: 40rpx;
      height: 40rpx;
      border-radius: 50%;
      
      &.dot-start {
        background: #52C41A;
      }
      
      &.dot-inspection {
        background: #13C2C2;
      }
      
      &.dot-follow {
        background: #FA8C16;
      }
      
      &.dot-summary {
        background: #1890FF;
      }
    }
    
    .timeline-content {
      .record-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12rpx;
        
        .record-title {
          font-size: 26rpx;
          font-weight: 600;
          color: #262626;
        }
        
        .record-time {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
      
      .record-content {
        display: block;
        font-size: 24rpx;
        color: #595959;
        line-height: 1.6;
        margin-bottom: 20rpx;
      }
    }
  }
}

.record-attachments {
  margin-top: 20rpx;
  
  .attachment-title {
    display: block;
    font-size: 24rpx;
    color: #595959;
    margin-bottom: 12rpx;
  }
  
  .attachment-list {
    .attachment-item {
      display: flex;
      align-items: center;
      padding: 12rpx 16rpx;
      background: #E6FFFB;
      border-radius: 12rpx;
      margin-bottom: 8rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      &:active {
        background: #B5F5EC;
      }
      
      .attachment-name {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #13C2C2;
      }
    }
  }
}

.report-card {
  .report-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .report-icon {
      width: 80rpx;
      height: 80rpx;
      background: #E6FFFB;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
    }
    
    .report-info {
      flex: 1;
      
      .report-title {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 8rpx;
      }
      
      .report-no {
        display: block;
        font-size: 24rpx;
        color: #595959;
        margin-bottom: 4rpx;
      }
      
      .report-date {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .report-summary {
    background: #E6FFFB;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    
    .summary-title {
      display: block;
      font-size: 24rpx;
      font-weight: 600;
      color: #13C2C2;
      margin-bottom: 12rpx;
    }
    
    .summary-content {
      font-size: 24rpx;
      color: #595959;
      line-height: 1.6;
    }
  }
  
  .report-actions {
    display: flex;
    gap: 16rpx;
    
    .report-btn {
      flex: 1;
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

.action-buttons {
  display: flex;
  gap: 16rpx;
  padding: 0 20rpx;
  flex-wrap: wrap;
  
  .action-btn {
    flex: 1;
    min-width: 160rpx;
    height: 80rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
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
</style>