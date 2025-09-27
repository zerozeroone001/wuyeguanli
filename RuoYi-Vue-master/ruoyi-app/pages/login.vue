<template>
  <view class="normal-login-container">
    <view class="logo-content align-center justify-center flex">
      <image style="width: 100rpx;height: 100rpx;" :src="globalConfig.appInfo.logo" mode="widthFix">
      </image>
      <text class="title">卓特物业</text>
    </view>
    <view class="login-form-content">
      
      <!-- #ifdef MP-WEIXIN -->
      <view class="weixin-login-container">
        <view class="avatar-wrapper">
          <button class="avatar-btn" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
            <image :src="avatarUrl" class="avatar-img"></image>
          </button>
          <view class="avatar-tips">点击选择头像</view>
        </view>
        <view class="input-item flex align-center">
          <view class="iconfont icon-user icon"></view>
          <input type="nickname" class="input" v-model="nickname" @blur="onNicknameBlur" placeholder="请输入昵称" />
        </view>
      </view>
      <!-- #endif -->

      <view class="action-btn">
        <button @click="handleWechatLogin" class="login-btn cu-btn block bg-green lg round">微信授权登录</button>
      </view>
      
      <view class="xieyi text-center">
        <text class="text-grey1">登录即代表同意</text>
        <text @click="handleUserAgrement" class="text-blue">《用户协议》</text>
        <text @click="handlePrivacy" class="text-blue">《隐私协议》</text>
      </view>
    </view>
     
  </view>
</template>

<script>
  import defaultAvatar from '@/static/images/profile.jpg'
  import upload from '@/utils/upload'

  export default {
    data() {
      return {
        globalConfig: getApp().globalData.config,
        avatarUrl: defaultAvatar,
        nickname: ''
      }
    },
    methods: {
      // 隐私协议
      handlePrivacy() {
        let site = this.globalConfig.appInfo.agreements[0]
        this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`)
      },
      // 用户协议
      handleUserAgrement() {
        let site = this.globalConfig.appInfo.agreements[1]
        this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`)
      },
      // 获取用户头像（微信自带头像选择组件）
      onChooseAvatar(e) {
        console.log('微信头像选择:', e)
        if (e.detail && e.detail.avatarUrl) {
          this.$modal.loading("头像上传中...")
          
          upload({ 
            url: '/common/upload',
            filePath: e.detail.avatarUrl
          }).then(res => {
            this.$modal.closeLoading()
            if (res.code === 200) {
              this.avatarUrl = res.url
              this.$modal.msgSuccess("头像上传成功")
            } else {
              this.$modal.msgError(res.msg || "头像上传失败")
            }
          }).catch(err => {
            this.$modal.closeLoading()
            console.error('头像上传失败:', err)
            this.$modal.msgError("头像上传失败")
          })
        }
      },
      // 昵称输入框失焦（兼容性处理）
      onNicknameBlur(e) {
        this.nickname = e.detail.value
      },
      // 登录成功后，处理函数
      loginSuccess(result) {
        // 设置用户信息
        this.$store.dispatch('GetInfo').then(res => {
          this.$tab.reLaunch('/pages/index')
        })
      },
      // 微信登录
      handleWechatLogin() {
        if (!this.nickname) {
          this.$modal.msgError("请输入昵称")
          return
        }

        this.$modal.loading("登录中，请耐心等待...")

        uni.login({
          provider: 'weixin',
          success: (loginRes) => {
            const loginData = {
              code: loginRes.code,
              userInfo: {
                nickName: this.nickname,
                avatarUrl: this.avatarUrl
              }
            }
            this.$store.dispatch('WechatLogin', loginData).then(() => {
              this.$modal.closeLoading()
              this.loginSuccess()
            }).catch((err) => {
              console.error(err)
              this.$modal.closeLoading()
            })
          },
          fail: (err) => {
            console.error('uni.login failed', err);
            this.$modal.closeLoading()
            this.$modal.msgError("微信登录授权失败")
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  page {
    background-color: #ffffff;
  }

  .normal-login-container {
    width: 100%;

    .logo-content {
      width: 100%;
      font-size: 21px;
      text-align: center;
      padding-top: 15%;

      image {
        border-radius: 4px;
      }

      .title {
        margin-left: 10px;
      }
    }

    .login-form-content {
      text-align: center;
      margin: 20px auto;
      margin-top: 15%;
      width: 80%;

      .weixin-login-container {
        .avatar-wrapper {
          margin-bottom: 20px;
          .avatar-btn {
            width: 180rpx;
            height: 180rpx;
            padding: 0;
            border-radius: 50%;
            background-color: #f5f6f7;
            border: 2px dashed #ddd;
            .avatar-img {
              width: 100%;
              height: 100%;
              border-radius: 50%;
            }
          }
          .avatar-tips {
            font-size: 24rpx;
            color: #999;
            margin-top: 10px;
          }
        }
      }

      .input-item {
        margin: 20px auto;
        background-color: #f5f6f7;
        height: 45px;
        border-radius: 20px;

        .icon {
          font-size: 38rpx;
          margin-left: 10px;
          color: #999;
        }

        .input {
          width: 100%;
          font-size: 14px;
          line-height: 20px;
          text-align: left;
          padding-left: 15px;
        }

      }

      .login-btn {
        margin-top: 40px;
        height: 45px;
      }
      
      .xieyi {
        color: #333;
        margin-top: 20px;
      }
    }
  }

</style>
