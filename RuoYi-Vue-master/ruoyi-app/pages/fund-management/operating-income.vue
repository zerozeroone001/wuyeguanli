<template>
  <view class="operating-income-container">
    <uni-table border stripe emptyText="暂无更多数据">
      <uni-tr>
        <uni-th>日期</uni-th>
        <uni-th>摘要</uni-th>
        <uni-th>收入</uni-th>
        <uni-th>支出</uni-th>
        <uni-th>结余</uni-th>
      </uni-tr>
      <uni-tr v-for="(item, index) in incomeData" :key="index">
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
      incomeData: [
        {
          date: '2025-08-13',
          summary: '广告收入',
          income: '5000.00',
          expense: '0.00',
          balance: '5000.00'
        },
        {
          date: '2025-08-14',
          summary: '场地租赁',
          income: '2000.00',
          expense: '0.00',
          balance: '7000.00'
        },
        {
          date: '2025-08-15',
          summary: '维修支出',
          income: '0.00',
          expense: '1500.00',
          balance: '5500.00'
        }
      ]
    };
  }
};
</script>

<style scoped>
.operating-income-container {
  padding: 20rpx;
}
</style>