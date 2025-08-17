<template>
  <view class="meetings-container">
    <uni-list>
      <uni-list-item title="会议日历" showArrow link to="/pages/daily-management/calendar"></uni-list-item>
      <uni-list-item title="会议公告" showArrow link to="/pages/daily-management/announcements"></uni-list-item>
      <uni-list-item title="在线投票" showArrow link to="/pages/daily-management/voting"></uni-list-item>
      <uni-list-item title="会议决议" showArrow link to="/pages/daily-management/resolutions"></uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      title: '业主大会'
    };
  }
};
</script>

<style scoped>
.meetings-container {
  padding: 20rpx;
}
</style>