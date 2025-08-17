<template>
  <view class="maintenance-fund-container">
    <uni-table border stripe emptyText="暂无更多数据">
      <uni-tr>
        <uni-th>日期</uni-th>
        <uni-th>摘要</uni-th>
        <uni-th>收入</uni-th>
        <uni-th>支出</uni-th>
        <uni-th>结余</uni-th>
      </uni-tr>
      <uni-tr v-for="(item, index) in fundData" :key="index">
        <uni-td>{{item.date}}</uni-td>
        <uni-td>{{item.summary}}</uni-td>
        <uni-td>{{item.income}}</uni-td>
        <uni-td>{{item.expense}}</uni-td>
        <uni-td>{{item.balance}}</uni-td>
      </uni-tr>
    </uni-table>
  </view>
</template>

<script>
export default {
  data() {
    return {
      fundData: [
        {
          date: '2025-08-10',
          summary: '业主缴纳',
          income: '100000.00',
          expense: '0.00',
          balance: '100000.00'
        },
        {
          date: '2025-08-20',
          summary: '电梯大修',
          income: '0.00',
          expense: '50000.00',
          balance: '50000.00'
        }
      ]
    };
  }
};
</script>

<style scoped>
.maintenance-fund-container {
  padding: 20rpx;
}
</style>