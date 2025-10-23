<template>
  <view class="poll-detail-container">
    <!-- 问卷信息 -->
    <view class="poll-info">
      <view class="poll-header">
        <text class="poll-title">{{ pollData.title }}</text>
        <view class="poll-status" :class="pollStatusClass">
          <text class="status-text">{{ pollStatusText }}</text>
        </view>
      </view>
      <view class="poll-description">
        <text>{{ pollData.description }}</text>
      </view>
      <view class="poll-time">
        <view class="time-item" v-if="pollData.startTime">
          <uni-icons type="calendar" size="14" color="#8C8C8C" />
          <text class="time-text">开始时间：{{ formatDate(pollData.startTime) }}</text>
        </view>
        <view class="time-item" v-if="pollData.endTime">
          <uni-icons type="clock" size="14" color="#8C8C8C" />
          <text class="time-text">截止时间：{{ formatDate(pollData.endTime) }}</text>
        </view>
      </view>
    </view>

    <!-- 已提交状态 -->
    <view class="submitted-status" v-if="submissionData">
      <view class="status-card">
        <view class="status-icon">
          <uni-icons type="checkmarkempty" size="24" color="#52C41A" />
        </view>
        <view class="status-content">
          <text class="status-title">您已完成此问卷</text>
          <text class="status-desc">提交时间：{{ formatDate(submissionData.submitTime) }}</text>
        </view>
        <view class="status-action">
          <button size="mini" type="primary" plain @click="viewSubmission">查看答案</button>
        </view>
      </view>
    </view>

    <!-- 动态表单 -->
    <view class="form-container" v-if="!submissionData && formSchema">
      <uni-forms ref="formRef" :model="formData" :rules="formRules" label-position="top">
        <view 
          class="form-field" 
          v-for="(field, index) in formSchema.fields" 
          :key="field.field"
        >
          <!-- 文本输入框 -->
          <uni-forms-item
		    style="width: 100%;"
            v-if="field.type === 'input'" 
            :name="field.field" 
            :label="field.label"
            :required="field.required"
          >
            <uni-easyinput 
              v-model="formData[field.field]" 
              :placeholder="field.placeholder || `请输入${field.label}`"
              :maxlength="field.maxlength || 100"
            />
          </uni-forms-item>

          <!-- 文本域 -->
          <uni-forms-item 
            v-if="field.type === 'textarea'" 
            :name="field.field" 
            :label="field.label"
            :required="field.required"
          >
            <textarea 
              v-model="formData[field.field]" 
              :placeholder="field.placeholder || `请输入${field.label}`"
              :maxlength="field.maxlength || 500"
              class="textarea-input"
            />
          </uni-forms-item>

          <!-- 单选框 -->
          <uni-forms-item 
            v-if="field.type === 'radio'" 
            :name="field.field" 
            :label="field.label"
            :required="field.required"
          >
            <uni-data-checkbox 
              v-model="formData[field.field]" 
              :localdata="field.options"
              :multiple="false"
            />
          </uni-forms-item>

          <!-- 多选框 -->
          <uni-forms-item 
            v-if="field.type === 'checkbox'" 
            :name="field.field" 
            :label="field.label"
            :required="field.required"
          >
            <uni-data-checkbox 
              v-model="formData[field.field]" 
              :localdata="field.options"
              :multiple="true"
            />
          </uni-forms-item>

          <!-- 下拉选择 -->
          <uni-forms-item 
            v-if="field.type === 'select'" 
            :name="field.field" 
            :label="field.label"
            :required="field.required"
          >
            <picker 
              :value="getSelectIndex(field.field)" 
              :range="field.options" 
              range-key="label"
              @change="handleSelectChange(field.field, $event)"
            >
              <view class="picker-input">
                <text class="picker-text">{{ getSelectText(field.field) || `请选择${field.label}` }}</text>
                <uni-icons type="arrowdown" size="16" color="#C0C4CC" />
              </view>
            </picker>
          </uni-forms-item>

          <!-- 日期选择 -->
          <uni-forms-item 
            v-if="field.type === 'date'" 
            :name="field.field" 
            :label="field.label"
            :required="field.required"
          >
            <picker 
              mode="date" 
              :value="formData[field.field]" 
              @change="handleDateChange(field.field, $event)"
            >
              <view class="picker-input">
                <text class="picker-text">{{ formData[field.field] || `请选择${field.label}` }}</text>
                <uni-icons type="calendar" size="16" color="#C0C4CC" />
              </view>
            </picker>
          </uni-forms-item>

          <!-- 数字输入 -->
          <uni-forms-item 
            v-if="field.type === 'number'" 
            :name="field.field" 
            :label="field.label"
            :required="field.required"
          >
            <uni-easyinput 
              v-model="formData[field.field]" 
              type="number"
              :placeholder="field.placeholder || `请输入${field.label}`"
            />
          </uni-forms-item>
        </view>
      </uni-forms>
    </view>

    <!-- 提交按钮 -->
    <view class="submit-section" v-if="!submissionData && formSchema">
      <view class="submit-tips">
        <uni-icons type="info" size="16" color="#1890FF" />
        <text class="tips-text">请仔细填写问卷内容，提交后将无法修改</text>
      </view>
      <button 
        class="submit-btn" 
        type="primary" 
        @click="handleSubmit"
        :disabled="submitting"
      >
        <text v-if="!submitting">提交问卷</text>
        <text v-else>提交中...</text>
      </button>
    </view>

    <!-- 底部安全距离 -->
    <view class="safe-area-bottom"></view>
  </view>
</template>

<script>
import { getSuggestionPollDetail, submitSuggestionPoll } from '@/api/poll.js'
import { formatDate } from '@/utils/common.js'

export default {
  components: {
    'uni-forms': () => import('@/uni_modules/uni-forms/components/uni-forms/uni-forms.vue'),
    'uni-forms-item': () => import('@/uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.vue'),
    'uni-easyinput': () => import('@/uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.vue'),
    'uni-data-checkbox': () => import('@/uni_modules/uni-data-checkbox/components/uni-data-checkbox/uni-data-checkbox.vue'),
    'uni-icons': () => import('@/uni_modules/uni-icons/components/uni-icons/uni-icons.vue'),
    'uni-load-more': () => import('@/uni_modules/uni-load-more/components/uni-load-more/uni-load-more.vue')
  },
  data() {
    return {
      pollId: null,
      pollData: {},
      formSchema: null,
      submissionData: null,
      formData: {},
      formRules: {},
      submitting: false
    }
  },
  
  computed: {
    pollStatusClass() {
      const now = new Date()
      const startTime = this.pollData.startTime ? new Date(this.pollData.startTime) : null
      const endTime = this.pollData.endTime ? new Date(this.pollData.endTime) : null
      
      if (startTime && now < startTime) {
        return 'status-pending'
      } else if (endTime && now > endTime) {
        return 'status-ended'
      } else {
        return 'status-active'
      }
    },
    
    pollStatusText() {
      const now = new Date()
      const startTime = this.pollData.startTime ? new Date(this.pollData.startTime) : null
      const endTime = this.pollData.endTime ? new Date(this.pollData.endTime) : null
      
      if (startTime && now < startTime) {
        return '未开始'
      } else if (endTime && now > endTime) {
        return '已结束'
      } else {
        return '进行中'
      }
    }
  },
  
  onLoad(options) {
    this.pollId = options.pollId
    if (this.pollId) {
      this.loadPollDetail()
    } else {
      uni.showToast({
        title: '无效的问卷ID',
        icon: 'none',
        complete: () => {
          setTimeout(() => uni.navigateBack(), 1500)
        }
      })
    }
  },
  
  methods: {
    async loadPollDetail() {
      uni.showLoading({ title: '加载中...' })
      
      try {
        const response = await getSuggestionPollDetail(this.pollId)
        const data = response.data
        
        this.pollData = data.poll || {}
        this.submissionData = data.submission || null
        
        // 解析表单结构
        if (data.form && data.form.mobileSchema) {
          try {
            const mobileSchema = JSON.parse(data.form.mobileSchema)
            console.log('解析到的表单结构:', mobileSchema)
            // 将数组格式转换为对象格式
            this.formSchema = {
              fields: mobileSchema
            }
            this.initFormData()
            this.initFormRules()
            console.log('初始化后的表单数据:', this.formData)
            console.log('初始化后的表单规则:', this.formRules)
          } catch (error) {
            console.error('解析表单结构失败:', error)
            uni.showToast({
              title: '表单结构解析失败',
              icon: 'none'
            })
          }
        }
        
      } catch (error) {
        console.error('加载问卷详情失败:', error)
        uni.showToast({
          title: '加载失败，请重试',
          icon: 'none'
        })
      } finally {
        uni.hideLoading()
      }
    },
    
    initFormData() {
      if (!this.formSchema || !this.formSchema.fields) return
      
      const data = {}
      this.formSchema.fields.forEach(field => {
        if (field.type === 'checkbox') {
          data[field.field] = []
        } else {
          data[field.field] = ''
        }
      })
      this.formData = data
    },
    
    initFormRules() {
      if (!this.formSchema || !this.formSchema.fields) return
      
      const rules = {}
      this.formSchema.fields.forEach(field => {
        if (field.required) {
          rules[field.field] = {
            required: true,
            message: `请填写${field.label}`,
            trigger: 'blur'
          }
        }
      })
      this.formRules = rules
    },
    
    async handleSubmit() {
      // 检查问卷是否在有效期内
      if (!this.isPollActive()) {
        uni.showToast({
          title: '问卷已结束，无法提交',
          icon: 'none'
        })
        return
      }
      
      // 表单验证
      try {
        await this.$refs.formRef.validate()
      } catch (error) {
        uni.showToast({
          title: '请完善必填项',
          icon: 'none'
        })
        return
      }
      
      // 确认提交
      const confirm = await uni.showModal({
        title: '确认提交',
        content: '确定要提交此问卷吗？提交后将无法修改。',
        confirmText: '确认提交',
        cancelText: '取消'
      })
      
      if (!confirm[1].confirm) return
      
      this.submitting = true
      
      try {
        const submitData = {
          answers: this.formData,
          clientType: 'mobile',
          remark: ''
        }
        
        await submitSuggestionPoll(this.pollId, submitData)
        
        uni.showToast({
          title: '提交成功',
          icon: 'success',
          duration: 2000,
          complete: () => {
            setTimeout(() => {
              uni.navigateBack()
            }, 2000)
          }
        })
        
      } catch (error) {
        console.error('提交问卷失败:', error)
        uni.showToast({
          title: error.msg || '提交失败，请重试',
          icon: 'none'
        })
      } finally {
        this.submitting = false
      }
    },
    
    viewSubmission() {
      // 显示已提交的答案
      const answers = this.submissionData.answersData ? 
        JSON.parse(this.submissionData.answersData) : {}
      
      let content = '您的答案：\n\n'
      Object.keys(answers).forEach(key => {
        const field = this.formSchema?.fields?.find(f => f.field === key)
        if (field) {
          content += `${field.label}：${answers[key]}\n`
        }
      })
      
      uni.showModal({
        title: '查看答案',
        content: content,
        showCancel: false,
        confirmText: '知道了'
      })
    },
    
    isPollActive() {
      const now = new Date()
      const startTime = this.pollData.startTime ? new Date(this.pollData.startTime) : null
      const endTime = this.pollData.endTime ? new Date(this.pollData.endTime) : null
      
      if (startTime && now < startTime) return false
      if (endTime && now > endTime) return false
      
      return true
    },
    
    
    getSelectIndex(fieldName) {
      const value = this.formData[fieldName]
      const field = this.formSchema.fields.find(f => f.field === fieldName)
      if (!field || !field.options) return 0
      
      const index = field.options.findIndex(option => option.value === value)
      return index >= 0 ? index : 0
    },
    
    getSelectText(fieldName) {
      const value = this.formData[fieldName]
      const field = this.formSchema.fields.find(f => f.field === fieldName)
      if (!field || !field.options) return ''
      
      const option = field.options.find(option => option.value === value)
      return option ? option.label : ''
    },
    
    handleSelectChange(fieldName, event) {
      const field = this.formSchema.fields.find(f => f.field === fieldName)
      if (!field || !field.options) return
      
      const index = event.detail.value
      this.$set(this.formData, fieldName, field.options[index].value)
    },
    
    handleDateChange(fieldName, event) {
      this.$set(this.formData, fieldName, event.detail.value)
    },
    
    formatDate(dateString) {
      if (!dateString) return ''
      return formatDate(dateString, 'yyyy-MM-dd hh:mm')
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.poll-detail-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.poll-info {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .poll-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 24rpx;
    
    .poll-title {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
    }
    
    .poll-status {
      padding: 8rpx 16rpx;
      border-radius: 16rpx;
      
      .status-text {
        font-size: 22rpx;
        font-weight: 500;
      }
      
      &.status-active {
        background: #F6FFED;
        .status-text {
          color: #52C41A;
        }
      }
      
      &.status-pending {
        background: #FFF2E8;
        .status-text {
          color: #FA8C16;
        }
      }
      
      &.status-ended {
        background: #FFF1F0;
        .status-text {
          color: #FF4D4F;
        }
      }
    }
  }
  
  .poll-description {
    margin-bottom: 24rpx;
    
    text {
      font-size: 26rpx;
      color: #595959;
      line-height: 1.6;
    }
  }
  
  .poll-time {
    .time-item {
      display: flex;
      align-items: center;
      margin-bottom: 12rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .time-text {
        margin-left: 8rpx;
        font-size: 24rpx;
        color: #8C8C8C;
      }
    }
  }
}

.submitted-status {
  margin: 0 20rpx 20rpx;
  
  .status-card {
    background: #FFFFFF;
    border-radius: 24rpx;
    padding: 30rpx;
    border: 1rpx solid #F0F0F0;
    display: flex;
    align-items: center;
    
    .status-icon {
      margin-right: 20rpx;
    }
    
    .status-content {
      flex: 1;
      
      .status-title {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #262626;
        margin-bottom: 8rpx;
      }
      
      .status-desc {
        font-size: 24rpx;
        color: #8C8C8C;
      }
    }
    
    .status-action {
      margin-left: 20rpx;
    }
  }
}

.form-container {
  margin: 0 20rpx 20rpx;
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .form-field {
    margin-bottom: 40rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .textarea-input {
    width: 100%;
    min-height: 120rpx;
    background-color: #F8F9FA;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    padding: 20rpx;
    font-size: 28rpx;
    box-sizing: border-box;
  }
  
  .picker-input {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #F8F9FA;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    padding: 20rpx;
    min-height: 80rpx;
    
    .picker-text {
      font-size: 28rpx;
      color: #262626;
      flex: 1;
    }
  }
}

.submit-section {
  margin: 0 20rpx 20rpx;
  
  .submit-tips {
    display: flex;
    align-items: center;
    background: #E6F7FF;
    border-radius: 16rpx;
    padding: 20rpx;
    margin-bottom: 30rpx;
    
    .tips-text {
      margin-left: 12rpx;
      font-size: 24rpx;
      color: #1890FF;
    }
  }
  
  .submit-btn {
    width: 100%;
    height: 88rpx;
    background: linear-gradient(135deg, #1890FF 0%, #40A9FF 100%);
    border-radius: 24rpx;
    font-size: 32rpx;
    font-weight: 600;
    color: #FFFFFF;
    border: none;
    
    &:disabled {
      background: #D9D9D9;
      color: #8C8C8C;
    }
  }
}

.safe-area-bottom {
  height: 120rpx;
}
</style>
