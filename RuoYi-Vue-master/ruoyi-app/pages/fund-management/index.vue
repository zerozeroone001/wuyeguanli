<template>
  <view class="fund-management-container">
    <uni-list>
      <uni-list-item title="经营性收益收支明细" showArrow link to="/pages/fund-management/operating-income"></uni-list-item>
      <uni-list-item title="专项维修资金收支及使用情况" showArrow link to="/pages/fund-management/maintenance-fund"></uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      title: '资金管理'
    };
  }
};
</script>

<style scoped>
.fund-management-container {
  padding: 20rpx;
}
</style>