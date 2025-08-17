<template>
  <view class="legal-consultation-container">
    <uni-forms :modelValue="formData" ref="form" @submit="submit">
      <uni-forms-item label="咨询类型" name="type">
        <uni-data-select
          v-model="formData.type"
          :localdata="range"
        ></uni-data-select>
      </uni-forms-item>
      <uni-forms-item label="咨询内容" name="content">
        <uni-easyinput type="textarea" v-model="formData.content" placeholder="请输入咨询内容"></uni-easyinput>
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
        type: 0,
        content: ''
      },
      range: [
        { value: 0, text: "民事" },
        { value: 1, text: "行政" },
        { value: 2, text: "刑事" },
        { value: 3, text: "涉外" },
      ],
    };
  },
  methods: {
    submit(e) {
      this.$refs.form.validate().then(res=>{
        console.log('表单数据', res);
        uni.showToast({
          title: '咨询已提交',
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
.legal-consultation-container {
  padding: 20rpx;
}
</style>