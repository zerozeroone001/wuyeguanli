<template>
  <view class="announcements-container">
    <uni-list>
      <uni-list-item v-for="(announcement, index) in announcements" :key="index" :title="announcement.title" :note="announcement.date" showArrow link :to="'/pages/common/textview/index?title=' + announcement.title"></uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      announcements: [
        {
          title: '关于召开第三届业主大会第一次会议的公告',
          date: '2025-08-10'
        },
        {
          title: '关于选举产生第三届业主委员会的公告',
          date: '2025-07-25'
        },
        {
          title: '关于小区停车位租赁方案的公告',
          date: '2025-07-15'
        }
      ]
    };
  }
};
</script>

<style scoped>
.announcements-container {
  padding: 20rpx;
}
</style>
