<template>
  <view class="container">
    <uni-list>
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'person-filled'}" title="昵称" :rightText="user.nickName" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'phone-filled'}" title="手机号码">
        <template v-slot:footer>
          <text v-if="user.phonenumber" class="list-item-right-text">{{ user.phonenumber }}</text>
          <button v-else @click="handleBind" class="mini-btn" type="primary" size="mini">绑定</button>
        </template>
      </uni-list-item>
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'email-filled'}" title="邮箱" :rightText="user.email" />
    <!--  <uni-list-item showExtraIcon="true" :extraIcon="{type: 'auth-filled'}" title="岗位" :rightText="postGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'staff-filled'}" title="角色" :rightText="roleGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'calendar-filled'}" title="创建日期" :rightText="user.createTime" /> -->
    </uni-list>

    <!-- 绑定手机号弹窗 -->
    <uni-popup ref="popup" type="center">
      <view class="popup-content">
        <uni-forms ref="form" :modelValue="form" :rules="rules">
          <uni-forms-item label="手机号" name="phonenumber">
            <uni-easyinput v-model="form.phonenumber" placeholder="请输入手机号码" />
          </uni-forms-item>
          <uni-forms-item label="验证码" name="code">
            <view class="code-wrapper">
              <uni-easyinput v-model="form.code" placeholder="请输入验证码" />
              <button @click="handleSendCode" :disabled="codeBtnDisabled" class="mini-btn code-btn" type="primary" size="mini">{{ codeBtnText }}</button>
            </view>
          </uni-forms-item>
        </uni-forms>
        <view class="popup-footer">
          <button @click="closePopup" class="mini-btn" size="mini">取消</button>
          <button @click="submitBind" class="mini-btn" type="primary" size="mini">确定</button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
  import { getUserProfile } from "@/api/system/user"
  import { sendCode, bindPhone } from "@/api/profile"

  export default {
    data() {
      return {
        user: {},
        roleGroup: "",
        postGroup: "",
        form: {
          phonenumber: '',
          code: ''
        },
        rules: {
          phonenumber: {
            rules: [{
              required: true,
              errorMessage: '请输入手机号码',
            }, {
              validateFunction: function(rule, value, data, callback) {
                const regex = /^1[3-9]\d{9}$/;
                if (!regex.test(value)) {
                  callback('请输入正确的手机号码');
                }
                return true;
              }
            }]
          },
          code: {
            rules: [{
              required: true,
              errorMessage: '请输入验证码',
            }]
          }
        },
        codeBtnText: '发送验证码',
        codeBtnDisabled: false,
        countdown: 60,
        timer: null
      }
    },
    onLoad() {
      this.getUser()
    },
    methods: {
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data
          this.roleGroup = response.roleGroup
          this.postGroup = response.postGroup
        })
      },
      handleBind() {
        this.$refs.popup.open()
      },
      closePopup() {
        this.$refs.popup.close()
      },
      handleSendCode() {
        this.$refs.form.validateField('phonenumber').then(() => {
          this.codeBtnDisabled = true;
          sendCode({ phonenumber: this.form.phonenumber }).then(res => {
            this.$modal.msgSuccess("验证码发送成功");
            this.timer = setInterval(() => {
              this.countdown--;
              this.codeBtnText = this.countdown + 's后重新发送';
              if (this.countdown === 0) {
                clearInterval(this.timer);
                this.timer = null;
                this.countdown = 60;
                this.codeBtnText = '发送验证码';
                this.codeBtnDisabled = false;
              }
            }, 1000);
          }).catch(() => {
            this.codeBtnDisabled = false;
          })
        })
      },
      submitBind() {
        this.$refs.form.validate().then(res => {
          bindPhone(this.form).then(() => {
            this.$modal.msgSuccess("绑定成功");
            this.closePopup();
            this.getUser(); // 重新获取用户信息
          })
        })
      }
    }
  }
</script>

<style lang="scss">
  page {
    background-color: #ffffff;
  }

  .list-item-right-text {
    font-size: 14px;
    color: #999;
  }

  .mini-btn {
    margin-left: 10px;
  }

  .popup-content {
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    width: 80vw;
  }

  .code-wrapper {
    display: flex;
    align-items: center;
  }

  .code-btn {
    margin-left: 10px;
    flex-shrink: 0;
  }

  .popup-footer {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
</style>
