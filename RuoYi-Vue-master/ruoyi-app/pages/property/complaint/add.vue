<template>
  <view class="complaint-container">
    <!-- 投诉须知 -->
    <view class="notice-section">
      <view class="notice-header">
        <uni-icons type="info" size="18" color="#1890FF" />
        <text class="notice-title">投诉须知</text>
      </view>
      <view class="notice-content">
        <text>• 请如实填写投诉内容，提供准确信息</text>
        <text>• 可上传相关图片作为证据材料</text>
        <text>• 我们将在24小时内安排专人处理</text>
        <text>• 处理过程中会及时反馈进展情况</text>
      </view>
    </view>

    <form @submit="submitComplaint">
      <!-- 投诉类型 -->
      <view class="form-section">
        <view class="section-header">
          <text class="section-title">投诉类型</text>
          <text class="required-mark">*</text>
        </view>
        <view class="type-options">
          <view 
            class="type-item" 
            :class="{ active: formData.complaintType === type.value }"
            v-for="type in complaintTypes" 
            :key="type.value"
            @click="selectType(type.value)"
          >
            <view class="type-icon">
              <uni-icons :type="type.icon" size="24" :color="formData.complaintType === type.value ? '#1890FF' : '#8C8C8C'" />
            </view>
            <text class="type-text">{{ type.label }}</text>
          </view>
        </view>
      </view>

      <!-- 投诉标题 -->
      <view class="form-section">
        <view class="section-header">
          <text class="section-title">投诉标题</text>
          <text class="required-mark">*</text>
        </view>
        <view class="input-wrapper">
          <input 
            v-model="formData.complaintTitle"
            placeholder="请简要描述问题"
            maxlength="50"
            class="title-input"
          />
          <text class="char-count">{{ formData.complaintTitle.length }}/50</text>
        </view>
      </view>

      <!-- 问题描述 -->
      <view class="form-section">
        <view class="section-header">
          <text class="section-title">问题描述</text>
          <text class="required-mark">*</text>
        </view>
        <view class="textarea-wrapper">
          <textarea 
            v-model="formData.complaintContent"
            placeholder="请详细描述遇到的问题，包括时间、地点、具体情况等..."
            maxlength="500"
            class="content-textarea"
            auto-height
          />
          <text class="char-count">{{ formData.complaintContent.length }}/500</text>
        </view>
      </view>

      <!-- 图片上传 -->
      <view class="form-section">
        <view class="section-header">
          <text class="section-title">上传图片</text>
          <text class="optional-mark">（选填）</text>
        </view>
        <view class="image-upload">
          <view 
            class="upload-item" 
            v-for="(image, index) in imageList" 
            :key="index"
          >
            <image :src="image" class="upload-image" @click="previewImage(index)" />
            <view class="delete-btn" @click="deleteImage(index)">
              <uni-icons type="clear" size="16" color="#FFFFFF" />
            </view>
          </view>
          <view 
            class="upload-btn"
            @click="chooseImage"
            v-if="imageList.length < 3"
          >
            <uni-icons type="camera" size="32" color="#8C8C8C" />
            <text>添加图片</text>
          </view>
        </view>
        <text class="upload-tip">最多上传3张图片，每张不超过5MB</text>
      </view>

      <!-- 联系方式 -->
      <view class="form-section">
        <view class="section-header">
          <text class="section-title">联系方式</text>
          <text class="required-mark">*</text>
        </view>
        <view class="input-wrapper">
          <input 
            v-model="formData.contactPhone"
            type="number"
            placeholder="请输入手机号码"
            maxlength="11"
            class="phone-input"
          />
        </view>
      </view>

      <!-- 紧急程度 -->
    

      <!-- 期望处理时间 -->

    </form>

    <!-- 提交按钮 -->
    <view class="submit-section">
      <view class="submit-info">
        <text>提交后将生成投诉编号，请保留以便查询进度</text>
      </view>
      <button 
        class="submit-btn"
        :class="{ disabled: !canSubmit }"
        :disabled="!canSubmit"
        @click="submitComplaint"
      >
        提交投诉
      </button>
    </view>
  </view>
</template>

<script>
import { addComplaint } from '@/api/complaint'

export default {
  data() {
    return {
      formData: {
        complaintType: '',
        complaintTitle: '',
        complaintContent: '',
        contactPhone: '',
        expectedTime: '2',
        urgency: '2'
      },
      imageList: [],
      complaintTypes: [
        { value: '1', label: '物业服务', icon: 'gear' },
        { value: '2', label: '设施设备', icon: 'settings' },
        { value: '3', label: '环境卫生', icon: 'leaf' },
        { value: '4', label: '安全管理', icon: 'locked' },
        { value: '5', label: '收费争议', icon: 'wallet' },
        { value: '6', label: '其他问题', icon: 'more' }
      ],
      urgencyLevels: [
        { value: '1', label: '紧急', class: 'urgent' },
        { value: '2', label: '普通', class: 'normal' },
        { value: '3', label: '一般', class: 'low' }
      ],
      expectedTimes: [
        { value: '1', label: '立即处理' },
        { value: '2', label: '3天内处理' },
        { value: '3', label: '1周内处理' },
        { value: '4', label: '1月内处理' }
      ]
    }
  },
  computed: {
    canSubmit() {
      return this.formData.complaintType && 
             this.formData.complaintTitle.trim() &&
             this.formData.complaintContent.trim()
    }
  },
  methods: {
    selectType(type) {
      this.formData.complaintType = type
    },
    
    selectUrgency(urgency) {
      this.formData.urgency = urgency
    },
    
    selectTime(time) {
      this.formData.expectedTime = time
    },
    
    validatePhone(phone) {
      const phoneReg = /^1[3-9]\d{9}$/
      return phoneReg.test(phone)
    },
    
    async chooseImage() {
      try {
        const res = await uni.chooseImage({
          count: 3 - this.imageList.length,
          sizeType: ['compressed'],
          sourceType: ['camera', 'album']
        })
        
        // 检查图片大小
        for (let tempFilePath of res.tempFilePaths) {
          const fileInfo = await uni.getFileInfo({
            filePath: tempFilePath
          })
          
          if (fileInfo.size > 5 * 1024 * 1024) {
            uni.showToast({
              title: '图片大小不能超过5MB',
              icon: 'none'
            })
            continue
          }
          
          this.imageList.push(tempFilePath)
        }
      } catch (error) {
        console.error('选择图片失败', error)
      }
    },
    
    previewImage(index) {
      uni.previewImage({
        current: index,
        urls: this.imageList
      })
    },
    
    deleteImage(index) {
      this.imageList.splice(index, 1)
    },
    
    async submitComplaint() {
      if (!this.canSubmit) {
        uni.showToast({
          title: '请完善必填信息',
          icon: 'none'
        })
        return
      }
      
      // 确认提交
      const confirm = await uni.showModal({
        title: '确认提交',
        content: '确定要提交此投诉吗？',
        confirmText: '确认提交',
        cancelText: '再想想'
      })
      
	  console.log(confirm[1].confirm)
      if (!confirm[1].confirm) return
      
      try {
        uni.showLoading({
          title: '提交中...'
        })
        
        // TODO: aihaitao, 图片上传功能
        
        addComplaint(this.formData).then(res => {
          uni.hideLoading()
          uni.showModal({
            title: '提交成功',
            content: `投诉已提交成功！\n投诉编号：${res.data.complaintNo}\n我们将尽快处理您的投诉`,
            showCancel: false,
            confirmText: '知道了'
          })
          uni.navigateBack()
        })
        
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: error.message || '提交失败，请重试',
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

.complaint-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 200rpx;
}

.notice-section {
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

.form-section {
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
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .required-mark {
      color: #FF4D4F;
      margin-left: 4rpx;
    }
    
    .optional-mark {
      color: #8C8C8C;
      font-size: 22rpx;
      margin-left: 8rpx;
    }
  }
}

.type-options {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
  
  .type-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30rpx 20rpx;
    border: 2rpx solid #F0F0F0;
    border-radius: 16rpx;
    transition: all 0.3s ease;
    
    &.active {
      border-color: #1890FF;
      background: #F0F8FF;
    }
    
    .type-icon {
      margin-bottom: 12rpx;
    }
    
    .type-text {
      font-size: 24rpx;
      color: #262626;
      text-align: center;
    }
  }
}

.input-wrapper {
  position: relative;
  
  .title-input, .phone-input {
    width: 100%;
    border: 2rpx solid #F0F0F0;
    border-radius: 12rpx;
    font-size: 28rpx;
    color: #262626;
    background: #FAFAFA;
    
    &:focus {
      border-color: #1890FF;
      background: #FFFFFF;
    }
  }
  
  .char-count {
    position: absolute;
    right: 16rpx;
    bottom: 16rpx;
    font-size: 20rpx;
    color: #8C8C8C;
  }
}

.textarea-wrapper {
  position: relative;
  
  .content-textarea {
    width: 100%;
    min-height: 200rpx;
    padding: 24rpx;
    border: 2rpx solid #F0F0F0;
    border-radius: 12rpx;
    font-size: 28rpx;
    color: #262626;
    background: #FAFAFA;
    line-height: 1.6;
    
    &:focus {
      border-color: #1890FF;
      background: #FFFFFF;
    }
  }
  
  .char-count {
    position: absolute;
    right: 16rpx;
    bottom: 16rpx;
    font-size: 20rpx;
    color: #8C8C8C;
  }
}

.image-upload {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin-bottom: 16rpx;
  
  .upload-item {
    position: relative;
    width: 200rpx;
    height: 200rpx;
    
    .upload-image {
      width: 100%;
      height: 100%;
      border-radius: 12rpx;
      object-fit: cover;
    }
    
    .delete-btn {
      position: absolute;
      top: -10rpx;
      right: -10rpx;
      width: 40rpx;
      height: 40rpx;
      background: #FF4D4F;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  
  .upload-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 200rpx;
    height: 200rpx;
    border: 2rpx dashed #D9D9D9;
    border-radius: 12rpx;
    background: #FAFAFA;
    
    text {
      margin-top: 12rpx;
      font-size: 24rpx;
      color: #8C8C8C;
    }
    
    &:active {
      background: #F0F0F0;
    }
  }
}

.upload-tip {
  font-size: 20rpx;
  color: #8C8C8C;
}

.urgency-options {
  display: flex;
  gap: 30rpx;
  
  .urgency-item {
    display: flex;
    align-items: center;
    padding: 16rpx 24rpx;
    border: 2rpx solid #F0F0F0;
    border-radius: 24rpx;
    transition: all 0.3s ease;
    
    &.active {
      border-color: #1890FF;
      background: #F0F8FF;
    }
    
    .urgency-dot {
      width: 16rpx;
      height: 16rpx;
      border-radius: 50%;
      margin-right: 12rpx;
      
      &.urgent {
        background: #FF4D4F;
      }
      
      &.normal {
        background: #FA8C16;
      }
      
      &.low {
        background: #52C41A;
      }
    }
    
    text {
      font-size: 26rpx;
      color: #262626;
    }
  }
}

.time-options {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16rpx;
  
  .time-item {
    display: flex;
    align-items: center;
    padding: 20rpx;
    border: 2rpx solid #F0F0F0;
    border-radius: 12rpx;
    transition: all 0.3s ease;
    
    &.active {
      border-color: #1890FF;
      background: #F0F8FF;
    }
    
    text {
      margin-left: 12rpx;
      font-size: 26rpx;
      color: #262626;
    }
  }
}

.submit-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #FFFFFF;
  padding: 30rpx 40rpx;
  border-top: 1rpx solid #F0F0F0;
  
  .submit-info {
    text-align: center;
    margin-bottom: 20rpx;
    
    text {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
  
  .submit-btn {
    width: 100%;
    height: 88rpx;
    background: #1890FF;
    color: #FFFFFF;
    font-size: 32rpx;
    font-weight: 600;
    border-radius: 24rpx;
    border: none;
    
    &.disabled {
      background: #F0F0F0;
      color: #8C8C8C;
    }
    
    &:active:not(.disabled) {
      background: #096DD9;
    }
  }
}
</style>