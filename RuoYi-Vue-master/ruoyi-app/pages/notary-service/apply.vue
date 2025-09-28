<template>
  <view class="apply-container">
    <!-- 申请进度 -->
    <view class="apply-progress">
      <view class="progress-header">
        <text class="progress-title">申请公证</text>
        <text class="progress-subtitle">{{ getTypeText(formData.type) }}</text>
      </view>
      
      <view class="progress-steps">
        <view 
          class="step-item" 
          :class="{ active: currentStep >= index + 1, completed: currentStep > index + 1 }"
          v-for="(step, index) in steps" 
          :key="index"
        >
          <view class="step-circle">
            <uni-icons 
              v-if="currentStep > index + 1"
              type="checkmarkempty" 
              size="16" 
              color="#FFFFFF" 
            />
            <text v-else>{{ index + 1 }}</text>
          </view>
          <text class="step-label">{{ step.label }}</text>
        </view>
      </view>
    </view>

    <!-- 表单内容 -->
    <form @submit="submitApplication">
      <!-- 基本信息 -->
      <view class="form-section" v-if="currentStep === 1">
        <view class="section-header">
          <uni-icons type="compose" size="20" color="#262626" />
          <text class="section-title">基本信息</text>
        </view>
        
        <view class="form-item">
          <text class="form-label">公证标题</text>
          <input 
            v-model="formData.title"
            type="text"
            placeholder="请输入公证标题"
            class="form-input"
          />
        </view>
        
        <view class="form-item">
          <text class="form-label">公证类型</text>
          <view class="type-selector">
            <view 
              class="type-option" 
              :class="{ selected: formData.type === type.value }"
              v-for="type in notaryTypes" 
              :key="type.value"
              @click="selectType(type.value)"
            >
              <view class="type-icon" :style="{ backgroundColor: type.bgColor }">
                <uni-icons :type="type.icon" size="20" color="#FFFFFF" />
              </view>
              <text class="type-name">{{ type.name }}</text>
            </view>
          </view>
        </view>
        
        <view class="form-item">
          <text class="form-label">紧急程度</text>
          <radio-group @change="onUrgencyChange">
            <label class="urgency-option" v-for="urgency in urgencyOptions" :key="urgency.value">
              <radio :value="urgency.value" :checked="formData.urgent === urgency.value" />
              <view class="urgency-content">
                <text class="urgency-name">{{ urgency.name }}</text>
                <text class="urgency-desc">{{ urgency.desc }}</text>
                <text class="urgency-fee" v-if="urgency.extraFee">+{{ urgency.extraFee }}元</text>
              </view>
            </label>
          </radio-group>
        </view>
        
        <view class="form-item">
          <text class="form-label">申请说明</text>
          <textarea 
            v-model="formData.description"
            placeholder="请详细说明公证用途和具体要求..."
            maxlength="500"
            class="form-textarea"
          />
          <text class="char-count">还可输入{{ 500 - formData.description.length }}字</text>
        </view>
      </view>

      <!-- 材料上传 -->
      <view class="form-section" v-if="currentStep === 2">
        <view class="section-header">
          <uni-icons type="folder-add" size="20" color="#262626" />
          <text class="section-title">材料上传</text>
        </view>
        
        <!-- 必需材料 -->
        <view class="material-group">
          <text class="group-title">必需材料</text>
          <view class="material-list">
            <view 
              class="material-item required" 
              v-for="material in requiredMaterials" 
              :key="material.type"
            >
              <view class="material-info">
                <text class="material-name">{{ material.name }}</text>
                <text class="material-desc">{{ material.desc }}</text>
              </view>
              <view class="material-actions">
                <button 
                  class="upload-btn"
                  @click="uploadMaterial(material.type)"
                >
                  {{ getMaterialStatus(material.type) }}
                </button>
              </view>
            </view>
          </view>
        </view>
        
        <!-- 可选材料 -->
        <view class="material-group">
          <text class="group-title">可选材料</text>
          <view class="material-list">
            <view 
              class="material-item optional" 
              v-for="material in optionalMaterials" 
              :key="material.type"
            >
              <view class="material-info">
                <text class="material-name">{{ material.name }}</text>
                <text class="material-desc">{{ material.desc }}</text>
              </view>
              <view class="material-actions">
                <button 
                  class="upload-btn secondary"
                  @click="uploadMaterial(material.type)"
                >
                  {{ getMaterialStatus(material.type) }}
                </button>
              </view>
            </view>
          </view>
        </view>
        
        <!-- 已上传文件 -->
        <view class="uploaded-files" v-if="uploadedFiles.length > 0">
          <text class="files-title">已上传文件</text>
          <view class="file-list">
            <view 
              class="file-item" 
              v-for="(file, index) in uploadedFiles" 
              :key="index"
            >
              <view class="file-icon">
                <uni-icons :type="getFileIcon(file.type)" size="20" color="#1890FF" />
              </view>
              <view class="file-info">
                <text class="file-name">{{ file.name }}</text>
                <text class="file-size">{{ file.size }}</text>
              </view>
              <view class="file-actions">
                <view class="preview-btn" @click="previewFile(file)">
                  <uni-icons type="eye" size="16" color="#1890FF" />
                </view>
                <view class="delete-btn" @click="deleteFile(index)">
                  <uni-icons type="trash" size="16" color="#FF4D4F" />
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 联系信息 -->
      <view class="form-section" v-if="currentStep === 3">
        <view class="section-header">
          <uni-icons type="person" size="20" color="#262626" />
          <text class="section-title">联系信息</text>
        </view>
        
        <view class="form-item">
          <text class="form-label">联系人姓名</text>
          <input 
            v-model="formData.contactName"
            type="text"
            placeholder="请输入联系人姓名"
            class="form-input"
          />
        </view>
        
        <view class="form-item">
          <text class="form-label">联系电话</text>
          <input 
            v-model="formData.contactPhone"
            type="number"
            placeholder="请输入手机号"
            class="form-input"
          />
        </view>
        
        <view class="form-item">
          <text class="form-label">身份证号</text>
          <input 
            v-model="formData.idCard"
            type="text"
            placeholder="请输入身份证号"
            class="form-input"
          />
        </view>
        
        <view class="form-item">
          <text class="form-label">通讯地址</text>
          <textarea 
            v-model="formData.address"
            placeholder="请输入详细地址"
            class="form-textarea"
          />
        </view>
        
        <!-- 公证处选择 -->
        <view class="form-item">
          <text class="form-label">选择公证处</text>
          <view class="notary-office-selector">
            <view 
              class="office-option" 
              :class="{ selected: formData.notaryOffice === office.id }"
              v-for="office in notaryOffices" 
              :key="office.id"
              @click="selectOffice(office.id)"
            >
              <view class="office-info">
                <text class="office-name">{{ office.name }}</text>
                <text class="office-address">{{ office.address }}</text>
                <text class="office-phone">电话：{{ office.phone }}</text>
              </view>
              <view class="office-distance">
                <text>{{ office.distance }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 确认信息 -->
      <view class="form-section" v-if="currentStep === 4">
        <view class="section-header">
          <uni-icons type="checkmarkempty" size="20" color="#262626" />
          <text class="section-title">确认信息</text>
        </view>
        
        <!-- 申请摘要 -->
        <view class="summary-card">
          <view class="summary-header">
            <text class="summary-title">{{ formData.title }}</text>
            <view class="summary-type" :style="{ backgroundColor: getTypeColor(formData.type) }">
              {{ getTypeText(formData.type) }}
            </view>
          </view>
          
          <view class="summary-content">
            <view class="summary-item">
              <text class="summary-label">申请人：</text>
              <text class="summary-value">{{ formData.contactName }}</text>
            </view>
            <view class="summary-item">
              <text class="summary-label">联系电话：</text>
              <text class="summary-value">{{ formData.contactPhone }}</text>
            </view>
            <view class="summary-item">
              <text class="summary-label">公证处：</text>
              <text class="summary-value">{{ getOfficeName(formData.notaryOffice) }}</text>
            </view>
            <view class="summary-item">
              <text class="summary-label">紧急程度：</text>
              <text class="summary-value">{{ formData.urgent ? '加急办理' : '普通办理' }}</text>
            </view>
            <view class="summary-item">
              <text class="summary-label">预计完成：</text>
              <text class="summary-value">{{ expectedTime }}</text>
            </view>
          </view>
        </view>
        
        <!-- 费用明细 -->
        <view class="fee-card">
          <view class="fee-header">
            <uni-icons type="wallet" size="20" color="#262626" />
            <text class="fee-title">费用明细</text>
          </view>
          
          <view class="fee-content">
            <view class="fee-item">
              <text class="fee-label">基础费用</text>
              <text class="fee-value">¥{{ baseFee }}</text>
            </view>
            <view class="fee-item" v-if="formData.urgent">
              <text class="fee-label">加急费用</text>
              <text class="fee-value">¥{{ urgentFee }}</text>
            </view>
            <view class="fee-item total">
              <text class="fee-label">总计</text>
              <text class="fee-value">¥{{ totalFee }}</text>
            </view>
          </view>
        </view>
        
        <!-- 服务协议 -->
        <view class="agreement-section">
          <label class="agreement-checkbox">
            <checkbox 
              :checked="formData.agreeTerms" 
              @change="onAgreeChange"
            />
            <text class="agreement-text">
              我已阅读并同意
              <text class="agreement-link" @click="viewAgreement">《公证服务协议》</text>
            </text>
          </label>
        </view>
      </view>
    </form>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button 
        class="action-btn secondary" 
        v-if="currentStep > 1"
        @click="prevStep"
      >
        上一步
      </button>
      <button 
        class="action-btn secondary" 
        @click="saveDraft"
      >
        保存草稿
      </button>
      <button 
        class="action-btn primary" 
        :disabled="!canNextStep"
        @click="nextStep"
      >
        {{ currentStep === 4 ? '提交申请' : '下一步' }}
      </button>
    </view>
  </view>
</template>

<script>
import { getNotaryDetail, addNotary, updateNotary, getNotaryOfficeList } from '@/api/notary'
import { uploadFile } from '@/api/common' // Assuming a common upload API
import { previewFile, getFileIcon } from '@/utils/filePreview.js';
import config from '@/config.js';

export default {
  data() {
    return {
      currentStep: 1,
      steps: [
        { label: '基本信息' },
        { label: '材料上传' },
        { label: '联系信息' },
        { label: '确认提交' }
      ],
      formData: {
        notaryId: null,
        title: '',
        type: 'document',
        urgent: 0, // 0 for false, 1 for true
        description: '',
        contactName: '',
        contactPhone: '',
        idCard: '',
        address: '',
        notaryOfficeId: null, // Changed to notaryOfficeId to match backend
        agreeTerms: false,
        notaryAttachmentList: [] // For attachments to be sent to backend
      },
      notaryTypes: [
        {
          value: 'document',
          name: '文件公证',
          icon: 'paperplane',
          bgColor: '#1890FF'
        },
        {
          value: 'signature',
          name: '签名公证',
          icon: 'compose',
          bgColor: '#52C41A'
        },
        {
          value: 'identity',
          name: '身份公证',
          icon: 'person',
          bgColor: '#FA8C16'
        },
        {
          value: 'property',
          name: '财产公证',
          icon: 'wallet',
          bgColor: '#722ED1'
        },
        {
          value: 'inheritance',
          name: '继承公证',
          icon: 'home',
          bgColor: '#13C2C2'
        },
        {
          value: 'power',
          name: '委托公证',
          icon: 'hand-up',
          bgColor: '#EB2F96'
        }
      ],
      urgencyOptions: [
        {
          value: 0,
          name: '普通办理',
          desc: '7-10个工作日完成',
          extraFee: 0
        },
        {
          value: 1,
          name: '加急办理',
          desc: '3-5个工作日完成',
          extraFee: 100
        }
      ],
      requiredMaterials: [
        {
          type: 'id_card',
          name: '身份证',
          desc: '申请人身份证正反面'
        },
        {
          type: 'application',
          name: '公证申请书',
          desc: '公证申请书'
        }
      ],
      optionalMaterials: [
        {
          type: 'supporting_docs',
          name: '支撑材料',
          desc: '相关证明文件'
        },
        {
          type: 'photos',
          name: '现场照片',
          desc: '相关现场照片'
        }
      ],
      uploadedFiles: [], // Files selected by user, with temp paths or uploaded URLs
      notaryOffices: [],
      baseFee: 200,
      urgentFee: 100
    }
  },
  computed: {
    canNextStep() {
      switch (this.currentStep) {
        case 1:
          return this.formData.title && this.formData.type && this.formData.description
        case 2:
          // Check if all required materials are uploaded
          const allRequiredUploaded = this.requiredMaterials.every(material => 
            this.uploadedFiles.some(file => file.fileType === material.type && file.fileUrl)
          )
          return allRequiredUploaded
        case 3:
          return this.formData.contactName && this.formData.contactPhone && this.formData.idCard && this.formData.notaryOfficeId
        case 4:
          return this.formData.agreeTerms
        default:
          return false
      }
    },
    
    totalFee() {
      return this.baseFee + (this.formData.urgent === 1 ? this.urgentFee : 0)
    },
    
    expectedTime() {
      const days = this.formData.urgent === 1 ? 5 : 10
      const date = new Date()
      date.setDate(date.getDate() + days)
      return date.toLocaleDateString('zh-CN')
    },
    
    // Get the name of the selected notary office for display
    selectedOfficeName() {
      const office = this.notaryOffices.find(o => o.officeId === this.formData.notaryOfficeId)
      return office ? office.name : ''
    }
  },
  onLoad(options) {
    if (options.type) {
      this.formData.type = options.type
    }
    if (options.id) {
      this.formData.notaryId = options.id
      this.loadDraftData(options.id)
    }
    this.loadNotaryOffices()
  },
  methods: {
    loadDraftData(id) {
      uni.showLoading({
        title: '加载草稿...'
      })
      getNotaryDetail(id).then(res => {
        uni.hideLoading()
        if (res.code === 200) {
          const data = res.data
          this.formData.notaryId = data.notaryId
          this.formData.title = data.title
          this.formData.type = data.type
          this.formData.urgent = data.urgent
          this.formData.description = data.description
          this.formData.contactName = data.contactName
          this.formData.contactPhone = data.contactPhone
          this.formData.idCard = data.idCard
          this.formData.address = data.address
          this.formData.notaryOfficeId = data.notaryOfficeId
          // Populate uploadedFiles from existing attachments
          this.uploadedFiles = data.notaryAttachmentList.map(att => ({
            fileType: att.fileType,
            fileName: att.fileName,
            fileUrl: att.fileUrl, // This is the URL from backend
            status: att.status // For display if needed
          }))
        }
      }).catch(() => {
        uni.hideLoading()
        uni.showToast({
          title: '加载草稿失败',
          icon: 'none'
        })
      })
    },
    
    loadNotaryOffices() {
      getNotaryOfficeList().then(res => {
        if (res.code === 200) {
          this.notaryOffices = res.data
        }
      })
    },
    
    selectType(type) {
      this.formData.type = type
    },
    
    onUrgencyChange(event) {
      this.formData.urgent = parseInt(event.detail.value)
    },
    
    uploadMaterial(fileType) {
      uni.chooseImage({
        count: 1, // Only allow one file per material type for simplicity
        sizeType: ['compressed'],
        sourceType: ['camera', 'album'],
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0]
          uni.showLoading({
            title: '上传中...'
          })
          // Assuming a common upload API is available at /api/common.js
          // You might need to create this file if it doesn't exist
          uploadFile(tempFilePath).then(uploadRes => {
            uni.hideLoading()
            if (uploadRes.code === 200) {
              // Remove existing file of the same type if any
              this.uploadedFiles = this.uploadedFiles.filter(file => file.fileType !== fileType)
              this.uploadedFiles.push({
                fileType: fileType,
                fileName: uploadRes.fileName, // Assuming backend returns fileName
                fileUrl: uploadRes.url, // Assuming backend returns URL
                status: 'pending' // Initial status for new upload
              })
              uni.showToast({
                title: '上传成功',
                icon: 'success'
              })
            } else {
              uni.showToast({
                title: uploadRes.msg || '上传失败',
                icon: 'none'
              })
            }
          }).catch(() => {
            uni.hideLoading()
            uni.showToast({
              title: '网络错误，请重试',
              icon: 'none'
            })
          })
        }
      })
    },
    
    getMaterialStatus(fileType) {
      const uploaded = this.uploadedFiles.some(file => file.fileType === fileType && file.fileUrl)
      return uploaded ? '已上传' : '上传'
    },
    
    getFileIcon(fileType) {
      const iconMap = {
        'id_card': 'person',
        'application': 'paperplane',
        'supporting_docs': 'folder',
        'photos': 'image'
      }
      return iconMap[fileType] || 'paperplane'
    },
    
    previewFile(file) {
      previewFile(file, config.baseUrl);
    },
    
    deleteFile(index) {
      this.uploadedFiles.splice(index, 1)
    },
    
    selectOffice(officeId) {
      this.formData.notaryOfficeId = officeId
    },
    
    getTypeText(type) {
      const typeObj = this.notaryTypes.find(t => t.value === type)
      return typeObj ? typeObj.name : '其他'
    },
    
    getTypeColor(type) {
      const typeObj = this.notaryTypes.find(t => t.value === type)
      return typeObj ? typeObj.bgColor : '#8C8C8C'
    },
    
    getOfficeName(officeId) {
      const office = this.notaryOffices.find(o => o.officeId === officeId)
      return office ? office.name : ''
    },
    
    onAgreeChange(event) {
      this.formData.agreeTerms = event.detail.value.length > 0
    },
    
    viewAgreement() {
      uni.navigateTo({
        url: '/pages/common/webview/webview?title=公证服务协议&url=https://example.com/agreement'
      })
    },
    
    prevStep() {
      if (this.currentStep > 1) {
        this.currentStep--
      }
    },
    
    nextStep() {
      if (!this.canNextStep) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        })
        return
      }
      
      if (this.currentStep === 4) {
        this.submitApplication()
      } else {
        this.currentStep++
      }
    },
    
    saveDraft() {
      this.submitForm('draft')
    },
    
    submitApplication() {
      this.submitForm('reviewing')
    },

    submitForm(status) {
      uni.showLoading({
        title: '提交中...'
      })

      // Prepare data for backend
      const dataToSend = {
        ...this.formData,
        status: status,
        notaryAttachmentList: this.uploadedFiles.map(file => ({
          fileName: file.fileName,
          fileUrl: file.fileUrl,
          fileType: file.fileType,
          status: file.status || 'pending' // Default to pending if not set
        }))
      }

      let requestPromise
      if (dataToSend.notaryId) {
        requestPromise = updateNotary(dataToSend)
      } else {
        requestPromise = addNotary(dataToSend)
      }

      requestPromise.then(res => {
        uni.hideLoading()
        if (res.code === 200) {
          uni.showModal({
            title: '操作成功',
            content: status === 'draft' ? '草稿已保存' : '您的公证申请已提交，我们会在1个工作日内联系您确认相关信息。',
            showCancel: false,
            success: () => {
              uni.navigateBack() // Go back to index page
            }
          })
        } else {
          uni.showToast({
            title: res.msg || '操作失败',
            icon: 'none'
          })
        }
      }).catch(() => {
        uni.hideLoading()
        uni.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        })
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

.apply-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.apply-progress {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .progress-header {
    text-align: center;
    margin-bottom: 30rpx;
    
    .progress-title {
      display: block;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      margin-bottom: 8rpx;
    }
    
    .progress-subtitle {
      font-size: 24rpx;
      color: #8C8C8C;
    }
  }
  
  .progress-steps {
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    
    &::before {
      content: '';
      position: absolute;
      top: 24rpx;
      left: 48rpx;
      right: 48rpx;
      height: 2rpx;
      background: #F0F0F0;
      z-index: 1;
    }
    
    .step-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      position: relative;
      z-index: 2;
      
      .step-circle {
        width: 48rpx;
        height: 48rpx;
        border-radius: 50%;
        background: #F0F0F0;
        color: #8C8C8C;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24rpx;
        font-weight: 600;
        margin-bottom: 12rpx;
        
        text {
          font-size: 24rpx;
          font-weight: 600;
        }
      }
      
      .step-label {
        font-size: 22rpx;
        color: #8C8C8C;
      }
      
      &.active {
        .step-circle {
          background: #1890FF;
          color: #FFFFFF;
        }
        
        .step-label {
          color: #1890FF;
          font-weight: 500;
        }
      }
      
      &.completed {
        .step-circle {
          background: #52C41A;
          color: #FFFFFF;
        }
        
        .step-label {
          color: #52C41A;
        }
      }
    }
  }
}

.form-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .section-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .form-item {
    margin-bottom: 30rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .form-label {
      display: block;
      font-size: 26rpx;
      font-weight: 500;
      color: #262626;
      margin-bottom: 16rpx;
    }
    
    .form-input {
      width: 100%;
      height: 80rpx;
      background: #F8F9FA;
      border: 1rpx solid #F0F0F0;
      border-radius: 16rpx;
      padding: 0 24rpx;
      font-size: 26rpx;
      color: #262626;
      
      &:focus {
        border-color: #1890FF;
        background: #FFFFFF;
      }
    }
    
    .form-textarea {
      width: 100%;
      min-height: 160rpx;
      background: #F8F9FA;
      border: 1rpx solid #F0F0F0;
      border-radius: 16rpx;
      padding: 24rpx;
      font-size: 26rpx;
      color: #262626;
      
      &:focus {
        border-color: #1890FF;
        background: #FFFFFF;
      }
    }
    
    .char-count {
      display: block;
      text-align: right;
      font-size: 22rpx;
      color: #8C8C8C;
      margin-top: 8rpx;
    }
  }
}

.type-selector {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
  
  .type-option {
    text-align: center;
    padding: 20rpx;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    background: #FFFFFF;
    
    &.selected {
      border-color: #1890FF;
      background: #E6F7FF;
    }
    
    .type-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 12rpx;
    }
    
    .type-name {
      font-size: 24rpx;
      color: #262626;
    }
  }
}

.urgency-option {
  display: flex;
  align-items: flex-start;
  padding: 20rpx;
  border: 1rpx solid #F0F0F0;
  border-radius: 16rpx;
  margin-bottom: 16rpx;
  background: #FFFFFF;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  radio {
    margin-right: 16rpx;
    margin-top: 4rpx;
  }
  
  .urgency-content {
    flex: 1;
    
    .urgency-name {
      display: block;
      font-size: 26rpx;
      font-weight: 500;
      color: #262626;
      margin-bottom: 8rpx;
    }
    
    .urgency-desc {
      display: block;
      font-size: 24rpx;
      color: #8C8C8C;
      margin-bottom: 4rpx;
    }
    
    .urgency-fee {
      font-size: 22rpx;
      color: #FA8C16;
      font-weight: 500;
    }
  }
}

.material-group {
  margin-bottom: 40rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .group-title {
    display: block;
    font-size: 26rpx;
    font-weight: 600;
    color: #262626;
    margin-bottom: 20rpx;
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
      
      .material-info {
        flex: 1;
        
        .material-name {
          display: block;
          font-size: 26rpx;
          font-weight: 500;
          color: #262626;
          margin-bottom: 8rpx;
        }
        
        .material-desc {
          font-size: 24rpx;
          color: #8C8C8C;
        }
      }
      
      .material-actions {
        .upload-btn {
          height: 60rpx;
          padding: 0 24rpx;
          background: #1890FF;
          color: #FFFFFF;
          border-radius: 16rpx;
          font-size: 24rpx;
          font-weight: 500;
          border: none;
          
          &.secondary {
            background: #F8F9FA;
            color: #595959;
            border: 1rpx solid #F0F0F0;
          }
          
          &:active {
            opacity: 0.8;
          }
        }
      }
    }
  }
}

.uploaded-files {
  margin-top: 30rpx;
  
  .files-title {
    display: block;
    font-size: 26rpx;
    font-weight: 600;
    color: #262626;
    margin-bottom: 20rpx;
  }
  
  .file-list {
    .file-item {
      display: flex;
      align-items: center;
      padding: 20rpx;
      background: #F8F9FA;
      border-radius: 16rpx;
      margin-bottom: 16rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .file-icon {
        width: 60rpx;
        height: 60rpx;
        background: #E6F7FF;
        border-radius: 16rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20rpx;
      }
      
      .file-info {
        flex: 1;
        
        .file-name {
          display: block;
          font-size: 26rpx;
          color: #262626;
          margin-bottom: 8rpx;
        }
        
        .file-size {
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
      
      .file-actions {
        display: flex;
        gap: 16rpx;
        
        .preview-btn, .delete-btn {
          width: 48rpx;
          height: 48rpx;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
        }
        
        .preview-btn {
          background: #E6F7FF;
        }
        
        .delete-btn {
          background: #FFF2F0;
        }
      }
    }
  }
}

.notary-office-selector {
  .office-option {
    display: flex;
    align-items: center;
    padding: 24rpx;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    margin-bottom: 16rpx;
    background: #FFFFFF;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    &.selected {
      border-color: #1890FF;
      background: #E6F7FF;
    }
    
    .office-info {
      flex: 1;
      
      .office-name {
        display: block;
        font-size: 26rpx;
        font-weight: 500;
        color: #262626;
        margin-bottom: 8rpx;
      }
      
      .office-address {
        display: block;
        font-size: 24rpx;
        color: #8C8C8C;
        margin-bottom: 4rpx;
      }
      
      .office-phone {
        font-size: 22rpx;
        color: #1890FF;
      }
    }
    
    .office-distance {
      text {
        font-size: 24rpx;
        color: #52C41A;
        font-weight: 500;
      }
    }
  }
}

.summary-card, .fee-card {
  background: #F8F9FA;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.summary-card {
  .summary-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
    
    .summary-title {
      flex: 1;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
      margin-right: 20rpx;
    }
    
    .summary-type {
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      font-size: 22rpx;
      font-weight: 500;
      color: #FFFFFF;
      white-space: nowrap;
    }
  }
  
  .summary-content {
    .summary-item {
      display: flex;
      margin-bottom: 12rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .summary-label {
        font-size: 24rpx;
        color: #8C8C8C;
        min-width: 160rpx;
      }
      
      .summary-value {
        font-size: 24rpx;
        color: #262626;
      }
    }
  }
}

.fee-card {
  .fee-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .fee-title {
      margin-left: 12rpx;
      font-size: 26rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .fee-content {
    .fee-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12rpx;
      
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
        padding-top: 12rpx;
        border-top: 1rpx solid #F0F0F0;
        
        .fee-label, .fee-value {
          font-size: 28rpx;
          font-weight: 600;
          color: #1890FF;
        }
      }
    }
  }
}

.agreement-section {
  margin-top: 30rpx;
  
  .agreement-checkbox {
    display: flex;
    align-items: flex-start;
    
    checkbox {
      margin-right: 16rpx;
      margin-top: 4rpx;
    }
    
    .agreement-text {
      font-size: 24rpx;
      color: #595959;
      line-height: 1.5;
      
      .agreement-link {
        color: #1890FF;
        text-decoration: underline;
      }
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
      
      &:disabled {
        background: #F0F0F0;
        color: #BFBFBF;
      }
    }
    
    &:active:not(:disabled) {
      opacity: 0.8;
    }
  }
}
</style>