<template>
  <uni-popup ref="popup" type="center" :mask-click="false">
    <view class="phone-bind-modal">
      <view class="modal-header">
        <text class="modal-title">绑定手机号</text>
        <view class="close-btn" @click="closeModal">
          <uni-icons type="closeempty" size="20" color="#8C8C8C" />
        </view>
      </view>

      <view class="modal-body">
        <view class="tip-text">
          <uni-icons type="info" size="16" color="#1890FF" />
          <text class="tip-content">为了保障您的账户安全，请先绑定手机号</text>
        </view>

        <view class="form-item">
          <text class="label">姓名</text>
          <input
            class="input"
            v-model="userName"
            placeholder="请输入姓名"
            placeholder-class="input-placeholder"
            type="text"
          />
        </view>

        <view class="form-item">
          <text class="label">手机号码</text>
          <input
            class="input"
            v-model="phoneNumber"
            placeholder="请输入手机号码"
            placeholder-class="input-placeholder"
            type="number"
            maxlength="11"
          />
        </view>

        <view class="form-item">
          <text class="label">验证码</text>
          <view class="code-input-wrapper">
            <input
              class="input code-input"
              v-model="verifyCode"
              placeholder="请输入验证码"
              placeholder-class="input-placeholder"
              type="number"
              maxlength="6"
            />
            <button
              class="send-code-btn"
              :disabled="sendCodeDisabled"
              @click="sendCode"
            >
              {{ sendCodeText }}
            </button>
          </view>
        </view>
      </view>

      <view class="modal-footer">
        <button class="cancel-btn" @click="closeModal">取消</button>
        <button class="confirm-btn" @click="confirmBind">确认绑定</button>
      </view>
    </view>
  </uni-popup>
</template>

<script>
import { sendSmsCode, bindPhone } from '@/api/user.js'

export default {
  name: 'PhoneBindModal',
  data() {
    return {
      phoneNumber: '',
      userName: '',
      verifyCode: '',
      sendCodeDisabled: false,
      countdown: 60,
      sendCodeText: '发送验证码',
      timer: null
    }
  },
  methods: {
    // 打开弹窗
    open() {
      this.$refs.popup.open()
    },

    // 关闭弹窗
    closeModal() {
      this.$refs.popup.close()
      this.resetForm()
    },

    // 重置表单
    resetForm() {
      this.phoneNumber = ''
      this.verifyCode = ''
      this.userName = '' // Added this line
      this.sendCodeDisabled = false
      this.countdown = 60
      this.sendCodeText = '发送验证码'
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },

    // 验证手机号
    validatePhone() {
      if (!this.phoneNumber) {
        uni.showToast({
          title: '请输入手机号码',
          icon: 'none'
        })
        return false
      }

      const phoneReg = /^1[3-9]\d{9}$/
      if (!phoneReg.test(this.phoneNumber)) {
        uni.showToast({
          title: '请输入正确的手机号码',
          icon: 'none'
        })
        return false
      }

      return true
    },

    // 发送验证码
    async sendCode() {
      if (!this.validatePhone()) {
        return
      }

      try {
        uni.showLoading({ title: '发送中...' })

        await sendSmsCode({
          phoneNumber: this.phoneNumber
        })

        uni.hideLoading()
        uni.showToast({
          title: '验证码已发送',
          icon: 'success'
        })

        // 开始倒计时
        this.startCountdown()
      } catch (error) {
        uni.hideLoading()
        console.error('发送验证码失败', error)
        uni.showToast({
          title: error.msg || '发送失败，请稍后重试',
          icon: 'none'
        })
      }
    },

    // 开始倒计时
    startCountdown() {
      this.sendCodeDisabled = true
      this.countdown = 60
      this.sendCodeText = `${this.countdown}秒后重发`

      this.timer = setInterval(() => {
        this.countdown--
        if (this.countdown <= 0) {
          clearInterval(this.timer)
          this.timer = null
          this.sendCodeDisabled = false
          this.sendCodeText = '发送验证码'
        } else {
          this.sendCodeText = `${this.countdown}秒后重发`
        }
      }, 1000)
    },

    // 确认绑定
    async confirmBind() {
      if (!this.validatePhone()) {
        return
      }

      if (!this.userName) {
        uni.showToast({
          title: '请输入姓名',
          icon: 'none'
        })
        return
      }

      if (!this.verifyCode) {
        uni.showToast({
          title: '请输入验证码',
          icon: 'none'
        })
        return
      }

      if (this.verifyCode.length !== 6) {
        uni.showToast({
          title: '请输入6位验证码',
          icon: 'none'
        })
        return
      }

      try {
        uni.showLoading({ title: '绑定中...' })

        await bindPhone({
          phoneNumber: this.phoneNumber,
          code: this.verifyCode,
          userName: this.userName // Added this line
        }).then(res=>{
			if(res.data.isOwner==0){
				
				uni.showToast({
				  title: '房产未录入，请申请产权认证',
				  icon: 'none'
				})
				
				
				// 延迟关闭弹窗并触发成功回调
				setTimeout(() => {
				  this.closeModal()
				  this.$emit('房产未录入，请申请产权认证')
				}, 1500)
				
				
				setTimeout(() => {
				  uni.navigateTo({
				    url: '/pageB/property/add',
				    fail: (err) => {
				      console.error('跳转产权认证页面失败:', err);
				      uni.showToast({
				        title: '页面跳转失败',
				        icon: 'none'
				      });
				    }
				  });
				}, 1500)
				
			}else{
				uni.hideLoading()
		
				// 延迟关闭弹窗并触发成功回调
				setTimeout(() => {
				  this.closeModal()
				  this.$emit('success')
				}, 1500)
				
			}
		})
		// 更新用户信息
		await this.$store.dispatch('GetInfo')
        
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: error.msg || '绑定失败，请稍后重试',
          icon: 'none'
        })
      }
    }
  },
  beforeDestroy() {
    // 组件销毁时清除定时器
    if (this.timer) {
      clearInterval(this.timer)
      this.timer = null
    }
  }
}
</script>

<style lang="scss" scoped>
.phone-bind-modal {
  width: 620rpx;
  background: #FFFFFF;
  border-radius: 24rpx;
  overflow: hidden;
}

.modal-header {
  position: relative;
  padding: 40rpx 30rpx;
  border-bottom: 1rpx solid #F0F0F0;

  .modal-title {
    font-size: 36rpx;
    font-weight: 600;
    color: #262626;
  }

  .close-btn {
    position: absolute;
    top: 40rpx;
    right: 30rpx;
    padding: 8rpx;
  }
}

.modal-body {
  padding: 40rpx 30rpx;

  .tip-text {
    display: flex;
    align-items: center;
    gap: 12rpx;
    padding: 20rpx;
    background: #E6F7FF;
    border-radius: 12rpx;
    margin-bottom: 32rpx;

    .tip-content {
      flex: 1;
      font-size: 26rpx;
      color: #1890FF;
      line-height: 1.5;
    }
  }

  .form-item {
    margin-bottom: 32rpx;

    &:last-child {
      margin-bottom: 0;
    }

    .label {
      display: block;
      font-size: 28rpx;
      color: #595959;
      margin-bottom: 16rpx;
    }

    .input {
      width: 100%;
      height: 88rpx;
      padding: 0 24rpx;
      background: #F5F5F5;
      border-radius: 12rpx;
      font-size: 28rpx;
      color: #262626;
    }

    .input-placeholder {
      color: #BFBFBF;
    }

    .code-input-wrapper {
      display: flex;
      gap: 16rpx;

      .code-input {
        flex: 1;
      }

      .send-code-btn {
        width: 200rpx;
        height: 88rpx;
        line-height: 88rpx;
        background: linear-gradient(135deg, #1890FF, #40A9FF);
        color: #FFFFFF;
        border-radius: 12rpx;
        font-size: 26rpx;
        font-weight: 500;
        border: none;
        padding: 0;

        &[disabled] {
          background: #F5F5F5;
          color: #BFBFBF;
        }
      }
    }
  }
}

.modal-footer {
  display: flex;
  gap: 24rpx;
  padding: 30rpx;
  border-top: 1rpx solid #F0F0F0;

  .cancel-btn,
  .confirm-btn {
    flex: 1;
    height: 88rpx;
    border-radius: 12rpx;
    font-size: 32rpx;
    font-weight: 600;
    border: none;
  }

  .cancel-btn {
    background: #F5F5F5;
    color: #595959;
  }

  .confirm-btn {
    background: linear-gradient(135deg, #1890FF, #40A9FF);
    color: #FFFFFF;
  }
}
</style>
