<template>
  <view class="auth-container">
    <!-- Status Display -->
    <view v-if="profile.authStatus === '1' || profile.authStatus === '2'" class="status-display">
      <uni-icons :type="statusInfo.icon" :color="statusInfo.color" size="60"></uni-icons>
      <view class="status-title" :style="{ color: statusInfo.color }">{{ statusInfo.title }}</view>
      <view class="status-remark">{{ statusInfo.remark }}</view>
    </view>

    <!-- Auth Form -->
    <view v-else>
      <view class="auth-tips">
        <uni-icons type="info-filled" size="18" color="#1890FF"></uni-icons>
        <text>为保障您的权益，请填写真实的身份信息，认证成功后不可修改。</text>
      </view>

      <uni-forms ref="form" :modelValue="formData" :rules="rules" class="auth-form">
        <uni-forms-item name="realName" label="真实姓名">
          <uni-easyinput v-model="formData.realName" placeholder="请输入您的真实姓名" />
        </uni-forms-item>
        <uni-forms-item name="idCardNo" label="身份证号">
          <uni-easyinput v-model="formData.idCardNo" placeholder="请输入您的18位身份证号码" />
        </uni-forms-item>
        <uni-forms-item name="buildingNo" label="楼栋号">
          <uni-easyinput v-model="formData.buildingNo" placeholder="例如：1号楼" />
        </uni-forms-item>
        <uni-forms-item name="unitNo" label="单元号">
          <uni-easyinput v-model="formData.unitNo" placeholder="例如：1单元" />
        </uni-forms-item>
        <uni-forms-item name="roomNo" label="房号">
          <uni-easyinput v-model="formData.roomNo" placeholder="例如：101" />
        </uni-forms-item>
      </uni-forms>

      <view class="action-buttons">
        <button class="submit-btn" @click="submitAuth">立即认证</button>
      </view>

      <view v-if="profile.authStatus === '3'" class="failure-reason">
        <text>上次驳回原因：{{ profile.remark || '无' }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { getMyProfile, submitAuth } from '@/api/profile';

export default {
  data() {
    return {
      profile: {
        authStatus: '0', // 0-未认证 1-待审核 2-已认证 3-认证失败
        remark: ''
      },
      formData: {
        realName: '',
        idCardNo: '',
        buildingNo: '',
        unitNo: '',
        roomNo: ''
      },
      rules: {
        realName: { rules: [{ required: true, errorMessage: '真实姓名不能为空' }] },
        // idCardNo: { rules: [{ required: true, errorMessage: '身份证号不能为空' }, { pattern: /^\d{17}(\d|X)$/i, errorMessage: '请输入正确的18位身份证号码' }] },
		idCardNo: { rules: [{ required: true, errorMessage: '身份证号不能为空' }] },
        buildingNo: { rules: [{ required: true, errorMessage: '楼栋号不能为空' }] },
        unitNo: { rules: [{ required: true, errorMessage: '单元号不能为空' }] },
        roomNo: { rules: [{ required: true, errorMessage: '房号不能为空' }] }
      }
    }
  },
  onLoad() {
    this.fetchMyProfile();
  },
  computed: {
    statusInfo() {
      switch (this.profile.authStatus) {
        case '1':
          return {
            icon: 'paperplane-filled',
            color: '#1890FF',
            title: '审核中',
            remark: '您的认证申请已提交，正在等待管理员审核，请耐心等待。'
          };
        case '2':
          return {
            icon: 'checkbox-filled',
            color: '#52C41A',
            title: '已认证',
            remark: '恭喜您，您已通过业主认证！'
          };
        default:
          return {};
      }
    }
  },
  methods: {
    fetchMyProfile() {
      getMyProfile().then(response => {
        if (response.data) {
          this.profile = response.data;
          // 如果是认证失败，将已有信息填入表单，方便用户修改
          if (this.profile.authStatus === '3') {
            this.formData = { ...this.formData, ...this.profile };
          }
        }
      });
    },
    submitAuth() {
      this.$refs.form.validate().then(res => {
        uni.showLoading({
          title: '提交中...'
        });
        submitAuth(this.formData).then(() => {
          uni.hideLoading();
          uni.showToast({
            title: '提交成功',
            icon: 'success'
          });
          // 刷新状态
          this.fetchMyProfile();
        }).catch(() => {
          uni.hideLoading();
        });
      }).catch(err => {
        console.log('表单错误', err);
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #F5F5F5;
}

.auth-container {
  padding: 32rpx;
}

.status-display {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80rpx 40rpx;
  background-color: #FFFFFF;
  border-radius: 16rpx;
  text-align: center;

  .status-title {
    font-size: 40rpx;
    font-weight: 600;
    margin-top: 30rpx;
  }

  .status-remark {
    font-size: 28rpx;
    color: #8C8C8C;
    margin-top: 20rpx;
    line-height: 1.6;
  }
}

.auth-tips {
  display: flex;
  align-items: flex-start;
  background-color: #E6F7FF;
  border: 1rpx solid #BAE7FF;
  padding: 20rpx;
  border-radius: 8rpx;
  margin-bottom: 30rpx;
  font-size: 24rpx;
  color: #1890FF;

  text {
    margin-left: 12rpx;
    line-height: 1.5;
  }
}

.auth-form {
  background-color: #FFFFFF;
  padding: 20rpx 30rpx;
  border-radius: 16rpx;

  ::v-deep .uni-forms-item__label {
    font-size: 28rpx;
    font-weight: 500;
    color: #262626;
  }
}

.action-buttons {
  margin-top: 40rpx;

  .submit-btn {
    width: 100%;
    height: 88rpx;
    background: $uni-color-primary;
    color: #FFFFFF;
    font-size: 32rpx;
    font-weight: 500;
    border-radius: 44rpx;
    border: none;

    &:active {
      background: #096DD9;
    }
  }
}

.failure-reason {
  margin-top: 30rpx;
  padding: 20rpx;
  background-color: #FFF1F0;
  border: 1rpx solid #FFCCC7;
  border-radius: 8rpx;
  font-size: 26rpx;
  color: #D4380D;
  text-align: center;
}
</style>
