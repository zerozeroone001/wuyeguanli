<template>
  <view class="auth-container">
    <view class="auth-tips">
      <uni-icons type="info-filled" size="18" color="#1890FF"></uni-icons>
      <text>为保障您的权益，请填写真实的身份信息，认证成功后不可修改。</text>
    </view>

    <uni-forms ref="form" :modelValue="formData" :rules="rules" class="auth-form">
      <uni-forms-item name="realName" label="真实姓名">
        <uni-easyinput v-model="formData.realName" placeholder="请输入您的真实姓名" />
      </uni-forms-item>
      <uni-forms-item name="idCard" label="身份证号">
        <uni-easyinput v-model="formData.idCard" placeholder="请输入您的18位身份证号码" />
      </uni-forms-item>
      <uni-forms-item name="phone" label="联系电话">
        <uni-easyinput v-model="formData.phone" placeholder="请输入您的手机号码" />
      </uni-forms-item>
      <uni-forms-item name="address" label="楼栋房号">
        <uni-easyinput v-model="formData.address" placeholder="例如：1号楼1单元101" />
      </uni-forms-item>
    </uni-forms>

    <view class="action-buttons">
      <button class="submit-btn" @click="submitAuth">立即认证</button>
    </view>

    <view class="auth-status" v-if="userInfo.authStatus">
      <text>当前状态：</text>
      <text :class="['status-text', getStatusClass(userInfo.authStatus)]">{{ getStatusText(userInfo.authStatus) }}</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        realName: '',
        idCard: '',
        phone: '',
        address: ''
      },
      rules: {
        realName: {
          rules: [{
            required: true,
            errorMessage: '真实姓名不能为空'
          }]
        },
        idCard: {
          rules: [{
            required: true,
            errorMessage: '身份证号不能为空'
          }, {
            pattern: /^\d{17}(\d|X)$/i,
            errorMessage: '请输入正确的18位身份证号码'
          }]
        },
        phone: {
          rules: [{
            required: true,
            errorMessage: '联系电话不能为空'
          }, {
            pattern: /^1[3-9]\d{9}$/,
            errorMessage: '请输入正确的手机号码'
          }]
        },
        address: {
          rules: [{
            required: true,
            errorMessage: '楼栋房号不能为空'
          }]
        }
      },
      // 模拟的用户信息
      userInfo: {
        authStatus: '0' // 0-未认证 1-已认证 2-认证失败
      }
    }
  },
  methods: {
    submitAuth() {
      this.$refs.form.validate().then(res => {
        console.log('表单数据', res);
        uni.showLoading({
          title: '认证中...'
        });

        // 模拟API调用
        setTimeout(() => {
          uni.hideLoading();
          // 模拟成功
          if (res.realName === '张三' && res.idCard.startsWith('440')) {
            uni.showToast({
              title: '认证成功',
              icon: 'success'
            });
            this.userInfo.authStatus = '1';
          } else {
            // 模拟失败
            uni.showModal({
              title: '认证失败',
              content: '信息有误或与系统预留信息不符，请检查后重试。',
              showCancel: false
            });
            this.userInfo.authStatus = '2';
          }
        }, 1500);

      }).catch(err => {
        console.log('表单错误', err);
      });
    },
    getStatusText(status) {
      const map = {
        '0': '未认证',
        '1': '已认证',
        '2': '认证失败'
      }
      return map[status] || '未知';
    },
    getStatusClass(status) {
      const map = {
        '0': 'status-unauthed',
        '1': 'status-authed',
        '2': 'status-failed'
      }
      return map[status] || '';
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FFFFFF;
}

.auth-container {
  padding: 40rpx;
}

.auth-tips {
  display: flex;
  align-items: flex-start;
  background-color: #E6F7FF;
  border: 1rpx solid #BAE7FF;
  padding: 20rpx;
  border-radius: 16rpx;
  margin-bottom: 40rpx;
  font-size: 24rpx;
  color: #1890FF;

  text {
    margin-left: 12rpx;
    line-height: 1.5;
  }
}

.auth-form {
  ::v-deep .uni-forms-item__label {
    font-size: 28rpx;
    font-weight: 500;
    color: #262626;
  }
}

.action-buttons {
  margin-top: 60rpx;

  .submit-btn {
    width: 100%;
    height: 88rpx;
    background: #1890FF;
    color: #FFFFFF;
    font-size: 32rpx;
    font-weight: 600;
    border-radius: 24rpx;
    border: none;

    &:active {
      background: #096DD9;
    }
  }
}

.auth-status {
  margin-top: 40rpx;
  text-align: center;
  font-size: 26rpx;
  color: #595959;

  .status-text {
    font-weight: 600;
    margin-left: 10rpx;

    &.status-unauthed {
      color: #FA8C16;
    }
    &.status-authed {
      color: #52C41A;
    }
    &.status-failed {
      color: #FF4D4F;
    }
  }
}
</style>