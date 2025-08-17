<template>
  <view class="complaints-container">
    <uni-forms :modelValue="formData" ref="form" @submit="submit">
      <uni-forms-item label="投诉标题" name="title">
        <uni-easyinput type="text" v-model="formData.title" placeholder="请输入投诉标题"></uni-easyinput>
      </uni-forms-item>
      <uni-forms-item label="投诉内容" name="content">
        <uni-easyinput type="textarea" v-model="formData.content" placeholder="请输入投诉内容"></uni-easyinput>
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
        title: '',
        content: ''
      }
    };
  },
  methods: {
    submit(e) {
      this.$refs.form.validate().then(res=>{
        console.log('表单数据', res);
        uni.showToast({
          title: '投诉已提交',
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
.complaints-container {
  padding: 20rpx;
}
</style>
