<template>
  <view class="regulations-container">
    <uni-list>
      <uni-list-item v-for="(regulation, index) in regulations" :key="index" :title="regulation.title" showArrow link :to="'/pages/common/textview/index?title=' + regulation.title"></uni-list-item>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      regulations: [
        {
          title: '业主大会议事规则'
        },
        {
          title: '管理规约'
        },
        {
          title: '财务管理制度'
        },
        {
          title: '印章管理制度'
        }
      ]
    };
  }
};
</script>

<style scoped>
.regulations-container {
  padding: 20rpx;
}
</style>