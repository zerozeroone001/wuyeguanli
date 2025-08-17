<template>
  <view class="qna-container">
    <uni-collapse accordion v-model="accordionVal">
      <uni-collapse-item v-for="(item, index) in qnaList" :key="index" :title="item.question">
        <view class="content">
          <text>{{ item.answer }}</text>
        </view>
      </uni-collapse-item>
    </uni-collapse>
  </view>
</template>

<script>
export default {
  data() {
    return {
      accordionVal: '1',
      qnaList: [
        {
          question: '如何缴纳物业费？',
          answer: '您可以通过小区的物业APP或者前往物业服务中心进行缴纳。'
        },
        {
          question: '小区的停车位如何申请？',
          answer: '您可以联系物业服务中心，了解当前可用的停车位信息，并办理租赁手续。'
        },
        {
          question: '发现公共设施损坏如何报修？',
          answer: '您可以通过小区的物业APP进行在线报修，或者直接联系物业服务中心。'
        }
      ]
    };
  }
};
</script>

<style scoped>
.qna-container {
  padding: 20rpx;
}
.content {
    padding: 15px;
}
</style>
