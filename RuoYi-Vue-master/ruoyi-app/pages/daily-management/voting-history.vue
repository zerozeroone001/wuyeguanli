<template>
  <view class="voting-history-container">
    <uni-list>
      <uni-list-item v-for="(item, index) in votingHistory" :key="index" :title="item.title" :note="'投票时间: ' + item.date" :right-text="item.result"></uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      votingHistory: [
        {
          title: '关于选举第三届业主委员会的投票',
          date: '2025-07-26',
          result: '同意'
        },
        {
          title: '关于小区停车位租赁方案的投票',
          date: '2025-07-16',
          result: '反对'
        }
      ]
    };
  }
};
</script>

<style scoped>
.voting-history-container {
  padding: 20rpx;
}
</style>
