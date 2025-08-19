<template>
  <view class="detail-container">
    <!-- 公证基本信息 -->
    <view class="notary-info">
      <view class="info-header">
        <text class="notary-title">{{ notaryInfo.title }}</text>
        <view class="notary-status" :class="['status-' + notaryInfo.status]">
          {{ getStatusText(notaryInfo.status) }}
        </view>
      </view>
      
      <view class="notary-meta">
        <view class="meta-item">
          <uni-icons type="list" size="14" color="#8C8C8C" />
          <text>编号：{{ notaryInfo.notaryNo }}</text>
        </view>
        <view class="meta-item">
          <uni-icons type="calendar" size="14" color="#8C8C8C" />
          <text>申请时间：{{ notaryInfo.applyTime }}</text>
        </view>
        <view class="meta-item" v-if="notaryInfo.expectedTime">
          <uni-icons type="clock" size="14" color="#8C8C8C" />
          <text>预计完成：{{ notaryInfo.expectedTime }}</text>
        </view>
        <view class="meta-item" v-if="notaryInfo.completedTime">
          <uni-icons type="checkmarkempty" size="14" color="#8C8C8C" />
          <text>完成时间：{{ notaryInfo.completedTime }}</text>
        </view>
      </view>
      
      <view class="notary-type-badge" :style="{ backgroundColor: getTypeColor(notaryInfo.type) }">
        {{ getTypeText(notaryInfo.type) }}
      </view>
      
      <!-- 紧急标识 -->
      <view class="urgent-badge" v-if="notaryInfo.urgent">
        <uni-icons type="fire" size="12" color="#FF4D4F" />
        <text>加急</text>
      </view>
    </view>

    <!-- 办理进度 -->
    <view class="progress-section" v-if="notaryInfo.status !== 'draft'">
      <view class="section-header">
        <uni-icons type="bars" size="20" color="#262626" />
        <text class="section-title">办理进度</text>
        <text class="progress-percent">{{ notaryInfo.progress }}%</text>
      </view>
      
      <view class="progress-timeline">
        <view 
          class="timeline-item" 
          :class="{ completed: step.completed, current: step.current }"
          v-for="step in progressSteps" 
          :key="step.id"
        >
          <view class="timeline-dot">
            <uni-icons 
              v-if="step.completed"
              type="checkmarkempty" 
              size="12" 
              color="#FFFFFF" 
            />
          </view>
          <view class="timeline-content">
            <view class="step-header">
              <text class="step-title">{{ step.title }}</text>
              <text class="step-time" v-if="step.time">{{ step.time }}</text>
            </view>
            <text class="step-desc" v-if="step.desc">{{ step.desc }}</text>
            <text class="step-note" v-if="step.note">{{ step.note }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 申请信息 -->
    <view class="application-info">
      <view class="section-header">
        <uni-icons type="person" size="20" color="#262626" />
        <text class="section-title">申请信息</text>
      </view>
      
      <view class="info-content">
        <view class="info-item">
          <text class="info-label">申请人：</text>
          <text class="info-value">{{ notaryInfo.contactName }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">联系电话：</text>
          <text class="info-value">{{ notaryInfo.contactPhone }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">身份证号：</text>
          <text class="info-value">{{ maskIdCard(notaryInfo.idCard) }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">通讯地址：</text>
          <text class="info-value">{{ notaryInfo.address }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">公证处：</text>
          <text class="info-value">{{ notaryInfo.notaryOffice }}</text>
        </view>
      </view>
    </view>

    <!-- 申请说明 -->
    <view class="description-section" v-if="notaryInfo.description">
      <view class="section-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="section-title">申请说明</text>
      </view>
      
      <view class="description-content">
        <text>{{ notaryInfo.description }}</text>
      </view>
    </view>

    <!-- 申请材料 -->
    <view class="materials-section">
      <view class="section-header">
        <uni-icons type="folder" size="20" color="#262626" />
        <text class="section-title">申请材料</text>
        <text class="material-count">{{ materialList.length }}份</text>
      </view>
      
      <view class="material-list">
        <view 
          class="material-item" 
          v-for="material in materialList" 
          :key="material.id"
          @click="previewMaterial(material)"
        >
          <view class="material-icon">
            <uni-icons :type="getMaterialIcon(material.type)" size="20" color="#1890FF" />
          </view>
          <view class="material-info">
            <text class="material-name">{{ material.name }}</text>
            <text class="material-size">{{ material.size }}</text>
          </view>
          <view class="material-status" :class="['material-' + material.status]">
            {{ getMaterialStatusText(material.status) }}
          </view>
        </view>
      </view>
    </view>

    <!-- 费用信息 -->
    <view class="fee-section">
      <view class="section-header">
        <uni-icons type="wallet" size="20" color="#262626" />
        <text class="section-title">费用信息</text>
      </view>
      
      <view class="fee-content">
        <view class="fee-item">
          <text class="fee-label">基础费用</text>
          <text class="fee-value">¥{{ notaryInfo.baseFee }}</text>
        </view>
        <view class="fee-item" v-if="notaryInfo.urgent">
          <text class="fee-label">加急费用</text>
          <text class="fee-value">¥{{ notaryInfo.urgentFee }}</text>
        </view>
        <view class="fee-item" v-if="notaryInfo.extraFee > 0">
          <text class="fee-label">其他费用</text>
          <text class="fee-value">¥{{ notaryInfo.extraFee }}</text>
        </view>
        <view class="fee-item total">
          <text class="fee-label">总计</text>
          <text class="fee-value">¥{{ notaryInfo.totalFee }}</text>
        </view>
        
        <view class="payment-status" v-if="notaryInfo.paymentStatus">
          <view class="payment-info" :class="['payment-' + notaryInfo.paymentStatus]">
            <uni-icons :type="getPaymentIcon(notaryInfo.paymentStatus)" size="16" />
            <text>{{ getPaymentStatusText(notaryInfo.paymentStatus) }}</text>
          </view>
          <text class="payment-time" v-if="notaryInfo.paymentTime">
            支付时间：{{ notaryInfo.paymentTime }}
          </text>
        </view>
      </view>
    </view>

    <!-- 公证书信息 -->
    <view class="certificate-section" v-if="notaryInfo.status === 'completed'">
      <view class="section-header">
        <uni-icons type="medal" size="20" color="#262626" />
        <text class="section-title">公证书信息</text>
      </view>
      
      <view class="certificate-card">
        <view class="certificate-header">
          <view class="certificate-icon">
            <uni-icons type="medal" size="32" color="#FA8C16" />
          </view>
          <view class="certificate-info">
            <text class="certificate-title">{{ notaryInfo.certificateTitle }}</text>
            <text class="certificate-no">证书编号：{{ notaryInfo.certificateNo }}</text>
            <text class="certificate-date">出具日期：{{ notaryInfo.certificateDate }}</text>
          </view>
        </view>
        
        <view class="certificate-validity">
          <uni-icons type="checkmarkempty" size="16" color="#52C41A" />
          <text>该公证书具有法律效力</text>
        </view>
      </view>
    </view>

    <!-- 联系信息 -->
    <view class="contact-section" v-if="notaryInfo.handlerInfo">
      <view class="section-header">
        <uni-icons type="phone" size="20" color="#262626" />
        <text class="section-title">联系信息</text>
      </view>
      
      <view class="contact-card">
        <view class="contact-info">
          <text class="contact-name">{{ notaryInfo.handlerInfo.name }}</text>
          <text class="contact-title">{{ notaryInfo.handlerInfo.title }}</text>
          <text class="contact-phone">{{ notaryInfo.handlerInfo.phone }}</text>
          <text class="contact-office">{{ notaryInfo.notaryOffice }}</text>
        </view>
        <view class="contact-actions">
          <view class="contact-btn" @click="callHandler">
            <uni-icons type="phone" size="16" color="#1890FF" />
            <text>拨打电话</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button 
        class="action-btn secondary" 
        v-if="notaryInfo.status === 'draft'"
        @click="editNotary"
      >
        编辑申请
      </button>
      <button 
        class="action-btn secondary" 
        v-if="canPay"
        @click="payFee"
      >
        支付费用
      </button>
      <button 
        class="action-btn secondary" 
        @click="shareNotary"
      >
        分享
      </button>
      <button 
        class="action-btn primary" 
        v-if="notaryInfo.status === 'completed'"
        @click="downloadCertificate"
      >
        下载公证书
      </button>
      <button 
        class="action-btn secondary" 
        v-if="canCancel"
        @click="cancelNotary"
      >
        取消申请
      </button>
    </view>

    <!-- 评价弹窗 -->
    <uni-popup ref="ratePopup" type="center">
      <view class="rate-popup">
        <view class="rate-header">
          <text class="rate-title">服务评价</text>
          <view class="close-btn" @click="closeRatePopup">
            <uni-icons type="close" size="18" color="#8C8C8C" />
          </view>
        </view>
        <view class="rate-content">
          <view class="rate-stars">
            <view 
              class="star-item" 
              :class="{ active: index < rating }"
              v-for="(star, index) in 5" 
              :key="index"
              @click="setRating(index + 1)"
            >
              <uni-icons type="star-filled" size="32" :color="index < rating ? '#FA8C16' : '#F0F0F0'" />
            </view>
          </view>
          <textarea 
            v-model="rateComment"
            placeholder="请分享您的使用感受..."
            class="rate-textarea"
          />
          <view class="rate-actions">
            <button class="rate-btn secondary" @click="closeRatePopup">取消</button>
            <button class="rate-btn primary" @click="submitRating">提交评价</button>
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
      notaryId: null,
      notaryInfo: {},
      materialList: [],
      progressSteps: [],
      rating: 0,
      rateComment: ''
    }
  },
  computed: {
    canPay() {
      return this.notaryInfo.status === 'reviewing' && 
             this.notaryInfo.paymentStatus === 'unpaid'
    },
    
    canCancel() {
      return ['draft', 'reviewing'].includes(this.notaryInfo.status)
    }
  },
  onLoad(options) {
    this.notaryId = options.id
    this.loadNotaryDetail()
  },
  methods: {
    loadNotaryDetail() {
      // 模拟公证详情数据
      this.notaryInfo = {
        notaryId: 1,
        notaryNo: 'GZ202412001',
        title: '房屋买卖合同公证',
        type: 'document',
        status: 'completed',
        urgent: false,
        applyTime: '2024-12-01 10:30',
        expectedTime: '2024-12-05 17:00',
        completedTime: '2024-12-04 15:30',
        progress: 100,
        contactName: '张三',
        contactPhone: '138****5678',
        idCard: '110101199001011234',
        address: '北京市朝阳区建国路88号',
        notaryOffice: '北京市朝阳区公证处',
        description: '需要对房屋买卖合同进行公证，确保合同的真实性和有效性。该合同涉及北京市朝阳区一套住宅的买卖，合同金额较大，希望通过公证保障双方权益。',
        baseFee: 200,
        urgentFee: 0,
        extraFee: 0,
        totalFee: 200,
        paymentStatus: 'paid',
        paymentTime: '2024-12-01 11:00',
        certificateTitle: '房屋买卖合同公证书',
        certificateNo: 'GZ(2024)朝阳字第001号',
        certificateDate: '2024-12-04',
        handlerInfo: {
          name: '李公证员',
          title: '公证员',
          phone: '010-85123456',
          office: '北京市朝阳区公证处'
        }
      }
      
      // 模拟材料列表
      this.materialList = [
        {
          id: 1,
          name: '身份证正面',
          type: 'id_card',
          size: '2.5MB',
          status: 'approved',
          url: '/static/images/sample.jpg'
        },
        {
          id: 2,
          name: '身份证反面',
          type: 'id_card',
          size: '2.3MB',
          status: 'approved',
          url: '/static/images/sample.jpg'
        },
        {
          id: 3,
          name: '房屋买卖合同',
          type: 'contract',
          size: '5.8MB',
          status: 'approved',
          url: '/static/images/sample.jpg'
        },
        {
          id: 4,
          name: '房产证',
          type: 'property_cert',
          size: '3.2MB',
          status: 'approved',
          url: '/static/images/sample.jpg'
        }
      ]
      
      // 模拟进度步骤
      this.progressSteps = [
        {
          id: 1,
          title: '申请提交',
          desc: '您的申请已成功提交',
          time: '2024-12-01 10:30',
          completed: true,
          current: false
        },
        {
          id: 2,
          title: '材料审核',
          desc: '公证处正在审核您提交的材料',
          time: '2024-12-01 14:20',
          completed: true,
          current: false,
          note: '材料齐全，审核通过'
        },
        {
          id: 3,
          title: '费用支付',
          desc: '请支付公证费用',
          time: '2024-12-01 15:00',
          completed: true,
          current: false
        },
        {
          id: 4,
          title: '公证办理',
          desc: '公证员正在办理您的公证',
          time: '2024-12-02 09:00',
          completed: true,
          current: false
        },
        {
          id: 5,
          title: '公证完成',
          desc: '公证书已出具，可下载查看',
          time: '2024-12-04 15:30',
          completed: true,
          current: true
        }
      ]
    },
    
    getStatusClass(status) {
      const classMap = {
        'draft': 'status-draft',
        'reviewing': 'status-reviewing',
        'processing': 'status-processing',
        'completed': 'status-completed',
        'rejected': 'status-rejected'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        'draft': '草稿',
        'reviewing': '审核中',
        'processing': '办理中',
        'completed': '已完成',
        'rejected': '已拒绝'
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
    
    getMaterialIcon(type) {
      const iconMap = {
        'id_card': 'person',
        'contract': 'paperplane',
        'property_cert': 'home',
        'supporting_docs': 'folder',
        'photos': 'image'
      }
      return iconMap[type] || 'paperplane'
    },
    
    getMaterialStatusClass(status) {
      const classMap = {
        'pending': 'material-pending',
        'approved': 'material-approved',
        'rejected': 'material-rejected'
      }
      return classMap[status] || 'material-default'
    },
    
    getMaterialStatusText(status) {
      const textMap = {
        'pending': '审核中',
        'approved': '已通过',
        'rejected': '已拒绝'
      }
      return textMap[status] || '未知'
    },
    
    getPaymentStatusClass(status) {
      const classMap = {
        'unpaid': 'payment-unpaid',
        'paid': 'payment-paid',
        'refunded': 'payment-refunded'
      }
      return classMap[status] || 'payment-default'
    },
    
    getPaymentStatusText(status) {
      const textMap = {
        'unpaid': '待支付',
        'paid': '已支付',
        'refunded': '已退款'
      }
      return textMap[status] || '未知'
    },
    
    getPaymentIcon(status) {
      const iconMap = {
        'unpaid': 'clock',
        'paid': 'checkmarkempty',
        'refunded': 'undo'
      }
      return iconMap[status] || 'help'
    },
    
    maskIdCard(idCard) {
      if (!idCard) return ''
      return idCard.replace(/(\d{6})\d{8}(\d{4})/, '$1********$2')
    },
    
    previewMaterial(material) {
      uni.previewImage({
        urls: [material.url]
      })
    },
    
    editNotary() {
      uni.navigateTo({
        url: `/pages/notary-service/apply?id=${this.notaryId}&type=${this.notaryInfo.type}`
      })
    },
    
    payFee() {
      uni.showModal({
        title: '支付费用',
        content: `需要支付公证费用 ¥${this.notaryInfo.totalFee}`,
        confirmText: '立即支付',
        success: (res) => {
          if (res.confirm) {
            this.processPayment()
          }
        }
      })
    },
    
    processPayment() {
      uni.showLoading({
        title: '支付中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '支付成功',
          icon: 'success'
        })
        
        // 更新支付状态
        this.notaryInfo.paymentStatus = 'paid'
        this.notaryInfo.paymentTime = new Date().toLocaleString('zh-CN')
      }, 2000)
    },
    
    shareNotary() {
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
    
    downloadCertificate() {
      uni.showLoading({
        title: '下载中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '下载完成',
          icon: 'success'
        })
        
        // 弹出评价
        this.showRatePopup()
      }, 2000)
    },
    
    cancelNotary() {
      uni.showModal({
        title: '取消申请',
        content: '确定要取消此公证申请吗？',
        confirmColor: '#FF4D4F',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '取消中...'
            })
            
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({
                title: '申请已取消',
                icon: 'success'
              })
              
              setTimeout(() => {
                uni.navigateBack()
              }, 1500)
            }, 1000)
          }
        }
      })
    },
    
    callHandler() {
      uni.makePhoneCall({
        phoneNumber: this.notaryInfo.handlerInfo.phone
      })
    },
    
    showRatePopup() {
      this.$refs.ratePopup.open()
    },
    
    closeRatePopup() {
      this.$refs.ratePopup.close()
    },
    
    setRating(rating) {
      this.rating = rating
    },
    
    submitRating() {
      if (this.rating === 0) {
        uni.showToast({
          title: '请选择评分',
          icon: 'none'
        })
        return
      }
      
      uni.showLoading({
        title: '提交中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '评价成功',
          icon: 'success'
        })
        this.closeRatePopup()
      }, 1000)
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

.notary-info, .progress-section, .application-info, .description-section, 
.materials-section, .fee-section, .certificate-section, .contact-section {
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
    
    .progress-percent {
      font-size: 24rpx;
      color: #1890FF;
      font-weight: 600;
    }
    
    .material-count {
      font-size: 24rpx;
      color: #8C8C8C;
    }
  }
}

.notary-info {
  position: relative;
  
  .info-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
    
    .notary-title {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
    }
    
    .notary-status {
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      font-size: 22rpx;
      font-weight: 500;
      white-space: nowrap;
      
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
  
  .notary-meta {
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
  
  .notary-type-badge {
    position: absolute;
    top: 40rpx;
    right: 40rpx;
    padding: 8rpx 16rpx;
    border-radius: 16rpx;
    font-size: 22rpx;
    font-weight: 500;
    color: #FFFFFF;
  }
  
  .urgent-badge {
    display: inline-flex;
    align-items: center;
    padding: 4rpx 12rpx;
    background: #FFF2F0;
    border-radius: 12rpx;
    
    text {
      margin-left: 4rpx;
      font-size: 20rpx;
      color: #FF4D4F;
      font-weight: 500;
    }
  }
}

.progress-timeline {
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
      background: #F0F0F0;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    
    .timeline-content {
      .step-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8rpx;
        
        .step-title {
          font-size: 26rpx;
          font-weight: 500;
          color: #262626;
        }
        
        .step-time {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
      
      .step-desc {
        display: block;
        font-size: 24rpx;
        color: #595959;
        margin-bottom: 4rpx;
      }
      
      .step-note {
        font-size: 22rpx;
        color: #52C41A;
      }
    }
    
    &.completed {
      .timeline-dot {
        background: #52C41A;
      }
      
      &:not(:last-child)::before {
        background: #52C41A;
      }
    }
    
    &.current {
      .timeline-dot {
        background: #1890FF;
      }
      
      .step-title {
        color: #1890FF;
        font-weight: 600;
      }
    }
  }
}

.info-content {
  .info-item {
    display: flex;
    margin-bottom: 16rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .info-label {
      font-size: 24rpx;
      color: #8C8C8C;
      min-width: 160rpx;
    }
    
    .info-value {
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

.material-list {
  .material-item {
    display: flex;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #F0F0F0;
    
    &:last-child {
      border-bottom: none;
    }
    
    &:active {
      background: #F8F9FA;
    }
    
    .material-icon {
      width: 60rpx;
      height: 60rpx;
      background: #E6F7FF;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
    }
    
    .material-info {
      flex: 1;
      
      .material-name {
        display: block;
        font-size: 26rpx;
        color: #262626;
        margin-bottom: 8rpx;
      }
      
      .material-size {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
    
    .material-status {
      padding: 4rpx 12rpx;
      border-radius: 12rpx;
      font-size: 20rpx;
      font-weight: 500;
      
      &.material-pending {
        background: #FFF2E8;
        color: #FA8C16;
      }
      
      &.material-approved {
        background: #F6FFED;
        color: #52C41A;
      }
      
      &.material-rejected {
        background: #FFF2F0;
        color: #FF4D4F;
      }
    }
  }
}

.fee-content {
  .fee-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .fee-label {
      font-size: 24rpx;
      color: #595959;
    }
    
    .fee-value {
      font-size: 24rpx;
      color: #262626;
      font-weight: 500;
    }
    
    &.total {
      padding-top: 16rpx;
      border-top: 1rpx solid #F0F0F0;
      
      .fee-label, .fee-value {
        font-size: 28rpx;
        font-weight: 600;
        color: #1890FF;
      }
    }
  }
  
  .payment-status {
    margin-top: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid #F0F0F0;
    
    .payment-info {
      display: flex;
      align-items: center;
      margin-bottom: 8rpx;
      
      text {
        margin-left: 8rpx;
        font-size: 24rpx;
        font-weight: 500;
      }
      
      &.payment-unpaid {
        color: #FA8C16;
      }
      
      &.payment-paid {
        color: #52C41A;
      }
      
      &.payment-refunded {
        color: #8C8C8C;
      }
    }
    
    .payment-time {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
}

.certificate-card {
  .certificate-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .certificate-icon {
      width: 80rpx;
      height: 80rpx;
      background: #FFF2E8;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
    }
    
    .certificate-info {
      flex: 1;
      
      .certificate-title {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 8rpx;
      }
      
      .certificate-no {
        display: block;
        font-size: 24rpx;
        color: #595959;
        margin-bottom: 4rpx;
      }
      
      .certificate-date {
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .certificate-validity {
    display: flex;
    align-items: center;
    padding: 16rpx 20rpx;
    background: #F6FFED;
    border-radius: 16rpx;
    border-left: 4rpx solid #52C41A;
    
    text {
      margin-left: 12rpx;
      font-size: 24rpx;
      color: #52C41A;
      font-weight: 500;
    }
  }
}

.contact-card {
  display: flex;
  align-items: center;
  
  .contact-info {
    flex: 1;
    
    .contact-name {
      display: block;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
      margin-bottom: 8rpx;
    }
    
    .contact-title {
      display: block;
      font-size: 24rpx;
      color: #595959;
      margin-bottom: 8rpx;
    }
    
    .contact-phone {
      display: block;
      font-size: 24rpx;
      color: #1890FF;
      margin-bottom: 4rpx;
    }
    
    .contact-office {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
  
  .contact-actions {
    .contact-btn {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 16rpx;
      background: #E6F7FF;
      border-radius: 16rpx;
      
      text {
        margin-top: 8rpx;
        font-size: 22rpx;
        color: #1890FF;
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
      background: #1890FF;
      color: #FFFFFF;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}

.rate-popup {
  width: 600rpx;
  background: #FFFFFF;
  border-radius: 24rpx;
  
  .rate-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #F0F0F0;
    
    .rate-title {
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
  
  .rate-content {
    padding: 40rpx 30rpx;
    
    .rate-stars {
      display: flex;
      justify-content: center;
      gap: 16rpx;
      margin-bottom: 30rpx;
      
      .star-item {
        &:active {
          transform: scale(1.1);
        }
      }
    }
    
    .rate-textarea {
      width: 100%;
      min-height: 160rpx;
      background: #F8F9FA;
      border: 1rpx solid #F0F0F0;
      border-radius: 16rpx;
      padding: 24rpx;
      font-size: 26rpx;
      color: #262626;
      margin-bottom: 30rpx;
    }
    
    .rate-actions {
      display: flex;
      gap: 16rpx;
      
      .rate-btn {
        flex: 1;
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
          background: #1890FF;
          color: #FFFFFF;
        }
      }
    }
  }
}
</style>