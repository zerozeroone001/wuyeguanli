<template>
  <view class="notary-service-container">
    <uni-forms :modelValue="formData" ref="form" @submit="submit">
      <uni-forms-item label="选择文件" name="files">
        <uni-file-picker
          v-model="formData.files"
          file-mediatype="all"
          mode="grid"
          @select="select"
          @progress="progress"
          @success="success"
          @fail="fail"
        />
      </uni-forms-item>
      <button form-type="submit">申请公证</button>
    </uni-forms>
  </view>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        files: []
      }
    };
  },
  methods: {
    // 获取上传状态
    select(e){
      console.log('选择文件：',e)
    },
    // 获取上传进度
    progress(e){
      console.log('上传进度：',e)
    },
    // 上传成功
    success(e){
      console.log('上传成功')
    },
    // 上传失败
    fail(e){
      console.log('上传失败：',e)
    },
    submit(e) {
      this.$refs.form.validate().then(res=>{
        console.log('表单数据', res);
        uni.showToast({
          title: '公证申请已提交',
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
.notary-service-container {
  padding: 20rpx;
}
</style>