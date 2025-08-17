<template>
  <view class="repair-container">
    <uni-forms :modelValue="formData" ref="form" @submit="submit">
      <uni-forms-item label="报修位置" name="location">
        <uni-easyinput type="text" v-model="formData.location" placeholder="请输入报修位置"></uni-easyinput>
      </uni-forms-item>
      <uni-forms-item label="问题描述" name="description">
        <uni-easyinput type="textarea" v-model="formData.description" placeholder="请输入问题描述"></uni-easyinput>
      </uni-forms-item>
      <uni-forms-item label="上传图片" name="files">
        <uni-file-picker
          v-model="formData.files"
          file-mediatype="image"
          mode="grid"
        />
      </uni-forms-item>
      <button form-type="submit">提交</button>
    </uni-forms>
  </view>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        location: '',
        description: '',
        files: []
      }
    };
  },
  methods: {
    submit(e) {
      this.$refs.form.validate().then(res=>{
        console.log('表单数据', res);
        uni.showToast({
          title: '报修已提交',
          icon: 'success'
        });
        // 在这里添加提交到后端的逻辑
      }).catch(err =>{
        console.log('表单错误', err);
      })
    }
  }
};
</script>

<style scoped>
.repair-container {
  padding: 20rpx;
}
</style>
