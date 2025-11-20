<template>
  <view class="add-consultation-container">
    <!-- 咨询类型选择 -->
    <view class="type-section">
      <view class="section-title">
        <uni-icons type="list" size="18" color="#262626" />
        <text>咨询类型</text>
      </view>
      
      <view class="type-grid">
        <view 
          class="type-item" 
          :class="{ active: formData.consultationType === 'property' }"
          @click="selectType('property')"
        >
          <uni-icons type="home" size="24" :color="formData.consultationType === 'property' ? '#FFFFFF' : '#1890FF'" />
          <text>民事</text>
        </view>
        <view 
          class="type-item" 
          :class="{ active: formData.consultationType === 'contract' }"
          @click="selectType('contract')"
        >
          <uni-icons type="paperplane" size="24" :color="formData.consultationType === 'contract' ? '#FFFFFF' : '#52C41A'" />
          <text>行政</text>
        </view>
        <view 
          class="type-item" 
          :class="{ active: formData.consultationType === 'fee' }"
          @click="selectType('fee')"
        >
          <uni-icons type="wallet" size="24" :color="formData.consultationType === 'fee' ? '#FFFFFF' : '#FA8C16'" />
          <text>刑事</text>
        </view>
        <view 
          class="type-item" 
          :class="{ active: formData.consultationType === 'other' }"
          @click="selectType('other')"
        >
          <uni-icons type="help" size="24" :color="formData.consultationType === 'other' ? '#FFFFFF' : '#722ED1'" />
          <text>涉外</text>
        </view>
      </view>
    </view>

    <!-- 基本信息 -->
    <view class="form-section">
      <view class="section-title">
        <uni-icons type="compose" size="18" color="#262626" />
        <text>基本信息</text>
      </view>
      
      <view class="form-item">
        <text class="item-label">咨询标题</text>
        <input 
          class="item-input" 
          placeholder="请简要描述您的问题"
          v-model="formData.title"
          maxlength="50"
        />
        <text class="char-count">{{ formData.title.length }}/50</text>
      </view>
    </view>

    <!-- 问题描述 -->
    <view class="form-section">
      <view class="section-title">
        <uni-icons type="chat" size="18" color="#262626" />
        <text>问题描述</text>
      </view>
      
      <view class="form-item">
        <textarea 
          class="problem-textarea"
          placeholder="请详细描述您遇到的法律问题，包括事件经过、争议焦点、您的诉求等。信息越详细，律师越能给出准确的建议。"
          v-model="formData.content"
          maxlength="1000"
        />
        <text class="char-count">{{ formData.content.length }}/1000</text>
      </view>
    </view>

    <!-- 相关材料 -->
    <view class="form-section">
      <view class="section-title">
        <uni-icons type="image" size="18" color="#262626" />
        <text>相关材料</text>
        <text class="section-desc">（可上传合同、通知、照片等相关证据）</text>
      </view>
      
      <view class="upload-section">
        <view class="uploaded-files">
          <view 
            class="file-item" 
            v-for="(file, index) in uploadedFiles" 
            :key="index"
          >
            <view class="file-preview">
              <image v-if="file.type === 'image'" :src="file.url" class="file-image" />
              <view v-else class="file-doc">
                <uni-icons type="paperplane" size="32" color="#1890FF" />
              </view>
            </view>
            <text class="file-name">{{ file.name }}</text>
            <view class="file-delete" @click="deleteFile(index)">
              <uni-icons type="close" size="16" color="#FF4D4F" />
            </view>
          </view>
          
          <view class="upload-btn" @click="uploadFile" v-if="uploadedFiles.length < 5">
            <uni-icons type="plus" size="32" color="#8C8C8C" />
            <text>添加材料</text>
          </view>
        </view>
        
        <text class="upload-tip">最多上传5个文件，支持图片、PDF、Word等格式，单个文件不超过10MB</text>
      </view>
    </view>

    <!-- 联系方式 -->
    <view class="form-section">
      <view class="section-title">
        <uni-icons type="phone" size="18" color="#262626" />
        <text>联系方式</text>
      </view>
      
      <view class="form-item">
        <text class="item-label">联系电话</text>
        <input 
          class="item-input" 
          type="number"
          placeholder="请输入您的手机号码"
          v-model="formData.contactPhone"
          maxlength="11"
        />
      </view>
      
      <view class="form-item">
        <text class="item-label">微信号</text>
        <input 
          class="item-input" 
          placeholder="选填，便于律师联系（选填）"
          v-model="formData.wechatId"
          maxlength="50"
        />
      </view>
    </view>


    <!-- 提交按钮 -->
    <view class="submit-section">
      <button 
        class="submit-btn" 
        @click="submitConsultation"
      >
        提交咨询
      </button>
      <text class="submit-tip">提交后律师将尽快回复，请保持电话畅通</text>
    </view>
  </view>
</template>

<script>
import { addLegalConsultation } from '@/api/legal.js';

export default {
  data() {
    return {
      formData: {
        consultationType: '',
        title: '',
        content: '',
        urgency: '2',
        contactPhone: '',
        wechatId: '',
        preferredLawyer: 'auto'
      },
      uploadedFiles: [],
      availableLawyers: []
    }
  },
  onLoad(options) {
    if (options.type) {
      this.formData.consultationType = options.type
    }
    this.loadAvailableLawyers()
  },
  methods: {
    loadAvailableLawyers() {
      // 模拟可选律师数据
      this.availableLawyers = [
        {
          id: 1,
          name: '张建华',
          title: '高级合伙人律师',
          avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
          specialties: ['物业纠纷', '合同法'],
          available: true
        },
        {
          id: 2,
          name: '李美玲',
          title: '资深律师',
          avatar: 'https://randomuser.me/api/portraits/women/2.jpg',
          specialties: ['业主权益', '物业管理'],
          available: true
        },
        {
          id: 3,
          name: '王志强',
          title: '专业律师',
          avatar: 'https://randomuser.me/api/portraits/men/3.jpg',
          specialties: ['收费争议', '服务合同'],
          available: true
        }
      ]
    },
    
    selectType(type) {
      this.formData.consultationType = type
    },
    
    onUrgencyChange(e) {
      this.formData.urgency = e.detail.value
    },
    
    selectLawyer(lawyerId) {
      this.formData.preferredLawyer = lawyerId
    },
    
    async uploadFile() {
      try {
        const res = await uni.chooseFile({
          count: 5 - this.uploadedFiles.length,
          type: 'all',
          sourceType: ['camera', 'album', 'file']
        })
        
        // 模拟文件上传
        for (let tempFile of res.tempFiles) {
          const fileItem = {
            name: tempFile.name || `文件${this.uploadedFiles.length + 1}`,
            url: tempFile.path,
            type: tempFile.type || 'file',
            size: tempFile.size
          }
          this.uploadedFiles.push(fileItem)
        }
      } catch (error) {
        console.error('选择文件失败', error)
      }
    },
    
    deleteFile(index) {
      this.uploadedFiles.splice(index, 1)
    },
    
    async submitConsultation() {
      // Explicit validation with user feedback
      if (!this.formData.consultationType) {
        uni.showToast({ title: '请选择咨询类型', icon: 'none' });
        return;
      }
      if (!this.formData.title.trim()) {
        uni.showToast({ title: '请输入咨询标题', icon: 'none' });
        return;
      }
      if (!this.formData.content.trim()) {
        uni.showToast({ title: '请详细描述您的问题', icon: 'none' });
        return;
      }
      if (!this.formData.contactPhone.trim()) {
        uni.showToast({ title: '请输入您的联系电话', icon: 'none' });
        return;
      }
      if (!/^1[3-9]\d{9}$/.test(this.formData.contactPhone)) {
        uni.showToast({ title: '请输入正确的手机号码', icon: 'none' });
        return;
      }

      uni.showLoading({
        title: '提交中...'
      })
      
      // Prepare the data for submission, including the user ID
      const submissionData = {
        ...this.formData,
        userId: this.$store.getters.id
      };

      try {
        await addLegalConsultation(submissionData);
        
        uni.hideLoading()
        
        uni.showModal({
          title: '提交成功',
          content: '您的法律咨询已成功提交，律师将在承诺时间内回复。请保持电话畅通。',
          showCancel: false,
          success: () => {
            uni.navigateBack()
          }
        })
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '提交失败，请重试',
          icon: 'none'
        })
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

.add-consultation-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 40rpx;
}

.type-section, .form-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-title {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    text {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .section-desc {
      margin-left: 8rpx;
      font-size: 22rpx;
      color: #8C8C8C;
      font-weight: 400;
    }
  }
}

.type-grid {
  display: flex;
  grid-template-columns: 1fr;
  gap: 20rpx;
  
  .type-item {
	  width: 22%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40rpx 20rpx;
    border: 2rpx solid #F0F0F0;
    border-radius: 20rpx;
    transition: all 0.3s ease;
    
    &.active {
      background: #1890FF;
      border-color: #1890FF;
      
      text {
        color: #FFFFFF;
      }
    }
    
    &:not(.active):active {
      background: #F8F9FA;
    }
    
    text {
      margin-top: 16rpx;
      font-size: 24rpx;
      color: #262626;
      font-weight: 500;
    }
  }
}

.form-item {
  margin-bottom: 30rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .item-label {
    display: block;
    font-size: 26rpx;
    color: #262626;
    font-weight: 500;
    margin-bottom: 16rpx;
  }
  
  .item-input {
    width: 100%;
    height: 88rpx; /* Explicit height */
    padding: 0 20rpx; /* Horizontal padding only */
    background: #F8F9FA;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    font-size: 26rpx;
    color: #262626;
    box-sizing: border-box;
    line-height: 88rpx; /* Match height to vertically center text */
  }
  
  .char-count {
    display: block;
    text-align: right;
    font-size: 22rpx;
    color: #8C8C8C;
    margin-top: 8rpx;
  }
  
  .radio-group {
    .radio-item {
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      text {
        margin-left: 16rpx;
        font-size: 26rpx;
        color: #262626;
      }
    }
  }
  
  .problem-textarea {
    width: 100%;
    min-height: 200rpx;
    padding: 20rpx;
    background: #F8F9FA;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    font-size: 26rpx;
    color: #262626;
    line-height: 1.5; /* Adjusted for better alignment */
    display: block;
    box-sizing: border-box;
  }
}

.upload-section {
  .uploaded-files {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20rpx;
    margin-bottom: 20rpx;
    
    .file-item {
      position: relative;
      
      .file-preview {
        width: 100%;
        height: 160rpx;
        border-radius: 16rpx;
        overflow: hidden;
        background: #F8F9FA;
        border: 1rpx solid #F0F0F0;
        
        .file-image {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
        
        .file-doc {
          display: flex;
          align-items: center;
          justify-content: center;
          height: 100%;
        }
      }
      
      .file-name {
        display: block;
        font-size: 22rpx;
        color: #262626;
        text-align: center;
        margin-top: 8rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .file-delete {
        position: absolute;
        top: -8rpx;
        right: -8rpx;
        width: 32rpx;
        height: 32rpx;
        background: #FFFFFF;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
      }
    }
    
    .upload-btn {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 160rpx;
      border: 2rpx dashed #D9D9D9;
      border-radius: 16rpx;
      background: #FAFAFA;
      
      &:active {
        background: #F0F0F0;
      }
      
      text {
        margin-top: 8rpx;
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
  
  .upload-tip {
    font-size: 22rpx;
    color: #8C8C8C;
    line-height: 1.4;
  }
}

.lawyer-selection {
  .lawyer-item {
    display: flex;
    align-items: center;
    padding: 24rpx;
    border: 1rpx solid #F0F0F0;
    border-radius: 20rpx;
    margin-bottom: 16rpx;
    position: relative;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    &.active {
      border-color: #1890FF;
      background: #F0F8FF;
    }
    
    &:active {
      background: #F8F9FA;
    }
    
    .lawyer-avatar {
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      margin-right: 20rpx;
    }
    
    .auto-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      background: #F8F9FA;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
    }
    
    .lawyer-info {
      flex: 1;
      
      .lawyer-name {
        display: block;
        font-size: 26rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 8rpx;
      }
      
      .lawyer-title {
        display: block;
        font-size: 22rpx;
        color: #8C8C8C;
        margin-bottom: 12rpx;
      }
      
      .lawyer-specialties {
        display: flex;
        gap: 8rpx;
        flex-wrap: wrap;
        
        .specialty-tag {
          font-size: 20rpx;
          color: #1890FF;
          background: #E6F7FF;
          padding: 4rpx 8rpx;
          border-radius: 8rpx;
        }
      }
    }
    
    .selection-indicator {
      position: absolute;
      top: 16rpx;
      right: 16rpx;
    }
  }
}

.fee-notice {
  background: #E6F7FF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 30rpx;
  border: 1rpx solid #91D5FF;
  
  .notice-header {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
    
    .notice-title {
      margin-left: 12rpx;
      font-size: 26rpx;
      font-weight: 600;
      color: #1890FF;
    }
  }
  
  .notice-content {
    text {
      display: block;
      font-size: 22rpx;
      color: #1890FF;
      line-height: 1.6;
      margin-bottom: 8rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

.submit-section {
  padding: 0 20rpx;
  text-align: center;
  
  .submit-btn {
    width: 100%;
    height: 80rpx;
    background: #1890FF;
    color: #FFFFFF;
    border-radius: 20rpx;
    font-size: 28rpx;
    font-weight: 600;
    border: none;
    margin-bottom: 16rpx;
    
    &:disabled {
      background: #D9D9D9;
      color: #FFFFFF;
    }
    
    &:not(:disabled):active {
      background: #096DD9;
    }
  }
  
  .submit-tip {
    font-size: 22rpx;
    color: #8C8C8C;
  }
}
</style>
 
 