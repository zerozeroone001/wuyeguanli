<template>
  <view class="resolutions-container">
    <uni-list>
      <uni-list-item v-for="(resolution, index) in resolutions" :key="index" :title="resolution.title" :note="resolution.date" showArrow link :to="'/pages/common/textview/index?title=' + resolution.title"></uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      resolutions: [
        {
          title: '第三届业主大会第一次会议决议',
          date: '2025-08-12'
        },
        {
          title: '关于选举产生第三届业主委员会的决议',
          date: '2025-07-28'
        },
        {
          title: '关于小区停车位租赁方案的决议',
          date: '2025-07-18'
        }
      ]
    };
  }
};
</script>

<style scoped>
.resolutions-container {
  padding: 20rpx;
}
</style>
