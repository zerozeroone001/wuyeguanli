<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称" prop="nickName">
      <el-input v-model="form.nickName" maxlength="30" />
    </el-form-item>
    <el-form-item label="手机号码" prop="phonenumber">
      <el-input v-model="form.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="form.email" maxlength="50" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="form.sex">
        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/system/user"

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      form: {
        nickName: "",
        phonenumber: "",
        email: "",
        sex: undefined,
        avatar: ""
      },
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    }
  },
  watch: {
    user: {
      handler(user) {
        if (user) {
          const resolvedAvatar = this.resolveAvatar(user.avatar)
          this.form = {
            nickName: user.nickName,
            phonenumber: user.phonenumber,
            email: user.email,
            sex: user.sex,
            avatar: resolvedAvatar
          }
        }
      },
      immediate: true
    }
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.avatar = this.resolveAvatar(this.form.avatar)
          updateUserProfile(this.form).then(response => {
            this.$modal.msgSuccess("修改成功")
            this.user.nickName = this.form.nickName
            this.user.phonenumber = this.form.phonenumber
            this.user.email = this.form.email
            this.user.sex = this.form.sex
            this.user.avatar = this.form.avatar
          })
        }
      })
    },
    close() {
      this.$tab.closePage()
    },
    updateAvatar(url) {
      if (!this.form) {
        this.form = {}
      }
      this.form.avatar = url
    },
    resolveAvatar(avatar) {
      if (avatar && avatar.startsWith("http")) {
        return avatar
      }
      const storeAvatar = this.$store?.getters?.avatar
      if (storeAvatar && storeAvatar.startsWith("http")) {
        return storeAvatar
      }
      if (avatar && avatar !== "") {
        return `${process.env.VUE_APP_BASE_API || ""}${avatar}`
      }
      return ""
    }
  }
}
</script>
