<template>
  <view class="visitor-container">
    <uni-forms :modelValue="formData" ref="form" @submit="submit">
      <uni-forms-item label="访客姓名" name="name">
        <uni-easyinput type="text" v-model="formData.name" placeholder="请输入访客姓名"></uni-easyinput>
      </uni-forms-item>
      <uni-forms-item label="预计到访时间" name="time">
        <uni-datetime-picker type="datetime" v-model="formData.time"></uni-datetime-picker>
      </uni-forms-item>
      <button form-type="submit">生成通行码</button>
    </uni-forms>

    <view class="qrcode-container" v-if="qrcode">
      <image :src="qrcode" style="width: 200px; height: 200px;"></image>
      <text>请访客在门口扫码通行</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        name: '',
        time: ''
      },
      qrcode: ''
    };
  },
  methods: {
    submit(e) {
      this.$refs.form.validate().then(res=>{
        console.log('表单数据', res);
        this.qrcode = 'https://via.placeholder.com/200.png?text=通行码';
        uni.showToast({
          title: '通行码已生成',
          icon: 'success'
        });
      }).catch(err =>{
        console.log('表单错误', err);
      })
    }
  }
};
</script>

<style scoped>
.visitor-container {
  padding: 20rpx;
}
.qrcode-container {
  margin-top: 40rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
