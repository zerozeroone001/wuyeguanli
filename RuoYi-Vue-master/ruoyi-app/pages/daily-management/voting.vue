<template>
  <view class="voting-container">
    <uni-list>
      <uni-list-item v-for="(item, index) in votingItems" :key="index" :title="item.title" :note="'截止日期: ' + item.deadline" showArrow link :to="'/pages/daily-management/voting-detail?id=' + item.id"></uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      votingItems: [
        {
          id: 1,
          title: '关于聘请新物业公司的投票',
          deadline: '2025-09-20'
        },
        {
          id: 2,
          title: '关于动用维修基金维修电梯的投票',
          deadline: '2025-09-18'
        }
      ]
    };
  }
};
</script>

<style scoped>
.voting-container {
  padding: 20rpx;
}
</style>
