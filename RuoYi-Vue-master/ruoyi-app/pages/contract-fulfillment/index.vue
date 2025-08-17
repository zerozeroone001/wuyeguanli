<template>
  <view class="contract-fulfillment-container">
    <uni-card title="物业服务合同" sub-title="合同编号: 2025-001" extra="已生效">
      <view class="content">
        <view class="item"><text class="label">甲方:</text><text>全体业主</text></view>
        <view class="item"><text class="label">乙方:</text><text>XX物业服务有限公司</text></view>
        <view class="item"><text class="label">服务期限:</text><text>2025年1月1日 - 2027年12月31日</text></view>
      </view>
      <template v-slot:actions>
        <view class="actions">
          <button @click="viewContract">查看合同附件</button>
        </view>
      </template>
    </uni-card>
  </view>
</template>

<script>
export default {
  methods: {
    viewContract() {
      uni.navigateTo({ url: '/pages/common/textview/index?title=物业服务合同附件' });
    }
  }
};
</script>

<style scoped>
.contract-fulfillment-container {
  padding: 20rpx;
}
.content .item {
  margin-bottom: 10rpx;
}
.content .label {
  font-weight: bold;
  margin-right: 10rpx;
}
.actions {
  display: flex;
  justify-content: flex-end;
  padding: 10rpx;
}
</style>