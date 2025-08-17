<template>
  <view class="calendar-container">
    <uni-calendar :insert="true" :lunar="true" :start-date="'2025-01-01'" :end-date="'2025-12-31'" @change="change" />
  </view>
</template>

<script>
export default {
  methods: {
    change(e) {
      console.log(e);
    }
  }
};
</script>

<style scoped>
.calendar-container {
  padding: 20rpx;
}
</style>
