<template>
  <view class="payment-container">
    <uni-list>
      <uni-list-item v-for="(item, index) in paymentItems" :key="index" :title="item.title" :note="'金额: ' + item.amount + '元'">
        <template v-slot:footer>
          <button size="mini" type="primary" @click="pay(item)">缴纳</button>
        </template>
      </uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      paymentItems: [
        {
          id: 1,
          title: '2025年8月物业费',
          amount: '300.00'
        },
        {
          id: 2,
          title: '2025年8月停车费',
          amount: '150.00'
        }
      ]
    };
  },
  methods: {
    pay(item) {
      console.log('pay for', item);
      uni.showToast({
        title: '缴纳成功',
        icon: 'success'
      });
    }
  }
};
</script>

<style scoped>
.payment-container {
  padding: 20rpx;
}
</style>
