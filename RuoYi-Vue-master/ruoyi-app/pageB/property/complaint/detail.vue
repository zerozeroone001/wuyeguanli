<template>
  <view class="detail-container">
    <!-- 投诉基本信息 -->
    <view class="complaint-info">
      <view class="info-header">
        <text class="complaint-title">{{ complaintInfo.complaintTitle }}</text>
        <view class="complaint-status" :class="[complaintInfo.status === 'pending' ? 'status-pending' : complaintInfo.status === 'processing' ? 'status-processing' : complaintInfo.status === 'completed' ? 'status-completed' : complaintInfo.status === 'closed' ? 'status-closed' : 'status-default']">
          {{ getStatusText(complaintInfo.status) }}
        </view>
      </view>
      
      <view class="info-meta">
        <view class="meta-item">
          <text class="meta-label">投诉编号</text>
          <text class="meta-value">{{ complaintInfo.complaintNo }}</text>
        </view>
        <view class="meta-item">
          <text class="meta-label">提交时间</text>
          <text class="meta-value">{{ complaintInfo.createTime }}</text>
        </view>
        <view class="meta-item">
          <text class="meta-label">投诉类型</text>
          <text class="meta-value">{{ getTypeText(complaintInfo.complaintType) }}</text>
        </view>
        <view class="meta-item">
          <text class="meta-label">紧急程度</text>
          <text class="meta-value urgency" :class="[complaintInfo.urgency === 'high' ? 'urgent' : complaintInfo.urgency === 'medium' ? 'normal' : 'low']">
            {{ getUrgencyText(complaintInfo.urgency) }}
          </text>
        </view>
      </view>
    </view>

    <!-- 投诉内容 -->
    <view class="complaint-content">
      <view class="content-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="content-title">投诉内容</text>
      </view>
      <view class="content-text">
        <text>{{ complaintInfo.complaintContent }}</text>
      </view>
      
      <!-- 图片展示 -->
      <view class="content-images" v-if="complaintInfo.images && complaintInfo.images.length > 0">
        <view class="images-title">相关图片</view>
        <view class="image-list">
          <image 
            v-for="(image, index) in complaintInfo.images" 
            :key="index"
            :src="image" 
            class="content-image"
            @click="previewImage(index)"
          />
        </view>
      </view>
    </view>

    <!-- 处理进度 -->
    <view class="progress-section">
      <view class="progress-header">
        <uni-icons type="clock" size="20" color="#262626" />
        <text class="progress-title">处理进度</text>
      </view>
      
      <view class="timeline">
        <view 
          class="timeline-item" 
          :class="{ active: index <= currentStep }"
          v-for="(record, index) in handleRecords" 
          :key="index"
        >
          <view class="timeline-dot">
            <uni-icons 
              :type="getStepIcon(record.handleType)" 
              size="16" 
              :color="index <= currentStep ? '#1890FF' : '#D9D9D9'"
            />
          </view>
          <view class="timeline-content">
            <view class="timeline-header">
              <text class="timeline-title">{{ getHandleTypeText(record.handleType) }}</text>
              <text class="timeline-time">{{ record.handleTime }}</text>
            </view>
            <view class="timeline-desc">
              <text>{{ record.handleContent }}</text>
            </view>
            <view class="timeline-handler" v-if="record.handlerName">
              <text>处理人：{{ record.handlerName }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 满意度评价 -->
    <view class="satisfaction-section" v-if="complaintInfo.status === '2' && complaintInfo.satisfaction">
      <view class="satisfaction-header">
        <uni-icons type="star" size="20" color="#262626" />
        <text class="satisfaction-title">满意度评价</text>
      </view>
      <view class="satisfaction-content">
        <view class="star-rating">
          <uni-icons 
            v-for="star in 5" 
            :key="star"
            type="star-filled" 
            size="24" 
            :color="star <= complaintInfo.satisfaction ? '#FAAD14' : '#D9D9D9'"
          />
          <text class="rating-text">{{ complaintInfo.satisfaction }}分</text>
        </view>
        <view class="evaluation-text" v-if="complaintInfo.evaluationNote">
          <text>{{ complaintInfo.evaluationNote }}</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <!-- 联系处理人 -->
      <button 
        class="contact-btn" 
        v-if="complaintInfo.handlerPhone"
        @click="contactHandler"
      >
        <uni-icons type="phone" size="18" color="#1890FF" />
        <text>联系处理人</text>
      </button>
      
      <!-- 评价按钮 -->
      <button 
        class="evaluate-btn" 
        v-if="complaintInfo.status === '2' && !complaintInfo.satisfaction"
        @click="evaluateComplaint"
      >
        <uni-icons type="star" size="18" color="#FFFFFF" />
        <text>评价服务</text>
      </button>
      
      <!-- 催办按钮 -->
      <button 
        class="urge-btn" 
        v-if="complaintInfo.status === '0' || complaintInfo.status === '1'"
        @click="urgeProcess"
      >
        <uni-icons type="sound" size="18" color="#FA8C16" />
        <text>催办处理</text>
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      complaintId: null,
      complaintInfo: {},
      handleRecords: [],
      currentStep: 0
    }
  },
  onLoad(options) {
    this.complaintId = options.id
    this.loadComplaintDetail()
    this.loadHandleRecords()
  },
  methods: {
    loadComplaintDetail() {
      // 模拟数据
      this.complaintInfo = {
        complaintId: 1,
        complaintNo: 'TS202401001',
        complaintTitle: '电梯故障频繁',
        complaintContent: '2号楼电梯经常出现故障，按钮失灵，楼层显示不准确，有时还会突然停止运行。这种情况已经持续了一周多，严重影响了业主的正常出行，特别是老人和小孩使用时存在安全隐患。希望物业能够尽快安排专业维修人员进行检修，确保电梯的安全运行。',
        complaintType: '2',
        urgency: '1',
        status: '1',
        createTime: '2024-01-10 09:30',
        handlerName: '张维修',
        handlerPhone: '13800138001',
        expectedTime: '2',
        images: [
          'https://via.placeholder.com/300x200/1890FF/FFFFFF?text=电梯按钮',
          'https://via.placeholder.com/300x200/52C41A/FFFFFF?text=楼层显示'
        ]
      }
    },
    
    loadHandleRecords() {
      // 模拟处理记录
      this.handleRecords = [
        {
          handleType: '1',
          handleContent: '投诉已收到，正在安排相关人员处理',
          handleTime: '2024-01-10 10:15',
          handlerName: '客服小王'
        },
        {
          handleType: '2',
          handleContent: '已转派给维修部门张师傅处理',
          handleTime: '2024-01-10 11:30',
          handlerName: '主管李经理'
        },
        {
          handleType: '3',
          handleContent: '现场查看电梯故障，发现控制面板老化，已联系厂家更换配件',
          handleTime: '2024-01-10 14:20',
          handlerName: '张维修'
        }
      ]
      
      // 计算当前步骤
      this.currentStep = this.handleRecords.length - 1
    },
    
    getStatusClass(status) {
      const classMap = {
        '0': 'status-pending',
        '1': 'status-processing',
        '2': 'status-completed',
        '3': 'status-closed'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        '0': '待处理',
        '1': '处理中',
        '2': '已完成',
        '3': '已关闭'
      }
      return textMap[status] || '未知'
    },
    
    getTypeText(type) {
      const typeMap = {
        '1': '物业服务问题',
        '2': '设施设备故障',
        '3': '环境卫生问题',
        '4': '安全管理问题',
        '5': '收费争议',
        '6': '其他问题'
      }
      return typeMap[type] || '未知类型'
    },
    
    getUrgencyClass(urgency) {
      const classMap = {
        '1': 'urgent',
        '2': 'normal',
        '3': 'low'
      }
      return classMap[urgency] || 'normal'
    },
    
    getUrgencyText(urgency) {
      const urgencyMap = {
        '1': '紧急',
        '2': '普通',
        '3': '一般'
      }
      return urgencyMap[urgency] || '普通'
    },
    
    getHandleTypeText(type) {
      const typeMap = {
        '1': '接收投诉',
        '2': '分派处理',
        '3': '现场处理',
        '4': '处理完成'
      }
      return typeMap[type] || '处理中'
    },
    
    getStepIcon(type) {
      const iconMap = {
        '1': 'checkmarkempty',
        '2': 'redo',
        '3': 'gear',
        '4': 'flag'
      }
      return iconMap[type] || 'circle'
    },
    
    previewImage(index) {
      uni.previewImage({
        current: index,
        urls: this.complaintInfo.images
      })
    },
    
    contactHandler() {
      uni.makePhoneCall({
        phoneNumber: this.complaintInfo.handlerPhone,
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
    },
    
    evaluateComplaint() {
      uni.navigateTo({
        url: `/pageB/property/complaint/evaluate?id=${this.complaintId}`
      })
    },
    
    async urgeProcess() {
      const confirm = await uni.showModal({
        title: '催办处理',
        content: '确定要催办此投诉的处理进度吗？',
        confirmText: '确认催办',
        cancelText: '取消'
      })
      
      if (confirm.confirm) {
        try {
          uni.showLoading({
            title: '催办中...'
          })
          
          // 模拟API调用
          await new Promise(resolve => setTimeout(resolve, 1500))
          
          uni.hideLoading()
          
          uni.showToast({
            title: '催办成功，我们会尽快处理',
            icon: 'success',
            duration: 2000
          })
          
        } catch (error) {
          uni.hideLoading()
          uni.showToast({
            title: '催办失败，请稍后重试',
            icon: 'none'
          })
        }
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

.detail-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 40rpx;
}

.complaint-info {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .info-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 30rpx;
    
    .complaint-title {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
    }
    
    .complaint-status {
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      font-size: 22rpx;
      font-weight: 500;
      white-space: nowrap;
      
      &.status-pending {
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
      
      &.status-closed {
        background: #F5F5F5;
        color: #8C8C8C;
      }
    }
  }
  
  .info-meta {
    .meta-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16rpx 0;
      border-bottom: 1rpx solid #F0F0F0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .meta-label {
        font-size: 26rpx;
        color: #8C8C8C;
      }
      
      .meta-value {
        font-size: 26rpx;
        color: #262626;
        font-weight: 500;
        
        &.urgency {
          &.urgent {
            color: #FF4D4F;
          }
          
          &.normal {
            color: #FA8C16;
          }
          
          &.low {
            color: #52C41A;
          }
        }
      }
    }
  }
}

.complaint-content {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .content-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .content-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .content-text {
    margin-bottom: 30rpx;
    
    text {
      font-size: 26rpx;
      color: #595959;
      line-height: 1.6;
    }
  }
  
  .content-images {
    .images-title {
      font-size: 26rpx;
      color: #262626;
      font-weight: 500;
      margin-bottom: 16rpx;
    }
    
    .image-list {
      display: flex;
      flex-wrap: wrap;
      gap: 16rpx;
      
      .content-image {
        width: 200rpx;
        height: 150rpx;
        border-radius: 12rpx;
        object-fit: cover;
      }
    }
  }
}

.progress-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .progress-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .progress-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
}

.timeline {
  .timeline-item {
    display: flex;
    margin-bottom: 40rpx;
    
    &:last-child {
      margin-bottom: 0;
      
      .timeline-dot::after {
        display: none;
      }
    }
    
    .timeline-dot {
      position: relative;
      width: 40rpx;
      height: 40rpx;
      border-radius: 50%;
      background: #F0F0F0;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
      flex-shrink: 0;
      
      &::after {
        content: '';
        position: absolute;
        top: 40rpx;
        left: 50%;
        transform: translateX(-50%);
        width: 2rpx;
        height: 60rpx;
        background: #F0F0F0;
      }
    }
    
    &.active {
      .timeline-dot {
        background: #E6F7FF;
        
        &::after {
          background: #1890FF;
        }
      }
    }
    
    .timeline-content {
      flex: 1;
      
      .timeline-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12rpx;
        
        .timeline-title {
          font-size: 26rpx;
          font-weight: 600;
          color: #262626;
        }
        
        .timeline-time {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
      
      .timeline-desc {
        margin-bottom: 8rpx;
        
        text {
          font-size: 24rpx;
          color: #595959;
          line-height: 1.5;
        }
      }
      
      .timeline-handler {
        text {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
    }
  }
}

.satisfaction-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .satisfaction-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .satisfaction-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .satisfaction-content {
    .star-rating {
      display: flex;
      align-items: center;
      margin-bottom: 16rpx;
      
      .rating-text {
        margin-left: 16rpx;
        font-size: 26rpx;
        color: #262626;
        font-weight: 500;
      }
    }
    
    .evaluation-text {
      text {
        font-size: 24rpx;
        color: #595959;
        line-height: 1.6;
      }
    }
  }
}

.action-buttons {
  display: flex;
  gap: 20rpx;
  padding: 0 20rpx;
  
  button {
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
    
    &.contact-btn {
      background: #E6F7FF;
      color: #1890FF;
    }
    
    &.evaluate-btn {
      background: #1890FF;
      color: #FFFFFF;
    }
    
    &.urge-btn {
      background: #FFF2E8;
      color: #FA8C16;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}
</style>
 
 