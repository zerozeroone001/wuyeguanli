<template>
  <view class="daily-management-container">
    <uni-list>
      <uni-list-item title="制度查阅" showArrow link to="/pages/daily-management/regulations"></uni-list-item>
      <uni-list-item title="业主大会" showArrow link to="/pages/daily-management/meetings"></uni-list-item>
      <uni-list-item title="意见征询" showArrow link to="/pages/daily-management/solicitation"></uni-list-item>
      <uni-list-item title="答业主问" showArrow link to="/pages/daily-management/qna"></uni-list-item>
      <uni-list-item title="我的投票" showArrow link to="/pages/daily-management/voting-history"></uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      title: '日常管理'
    };
  }
};
</script>

<style scoped>
.daily-management-container {
  padding: 20rpx;
}
</style>