<template>
  <view class="solicitation-container">
    <uni-list>
      <uni-list-item v-for="(item, index) in solicitations" :key="index" :title="item.title" :note="'截止日期: ' + item.deadline" showArrow link :to="'/pages/daily-management/solicitation-detail?id=' + item.id"></uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      solicitations: [
        {
          id: 1,
          title: '关于小区绿化升级改造的意见征询',
          deadline: '2025-09-15'
        },
        {
          id: 2,
          title: '关于增设快递柜的意见征询',
          deadline: '2025-09-10'
        },
        {
          id: 3,
          title: '关于调整垃圾分类时间的意见征询',
          deadline: '2025-09-05'
        }
      ]
    };
  }
};
</script>

<style scoped>
.solicitation-container {
  padding: 20rpx;
}
</style>