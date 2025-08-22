<template>
  <div class="property-charts">
    <el-row :gutter="20">
      <!-- 投票参与率趋势图 -->
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <div class="chart-header">
            <h4>投票参与率趋势</h4>
            <span class="chart-subtitle">最近7天</span>
          </div>
          <div ref="voteChart" class="chart-container"></div>
        </div>
      </el-col>

      <!-- 投诉处理统计 -->
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <div class="chart-header">
            <h4>投诉处理统计</h4>
            <span class="chart-subtitle">最近30天</span>
          </div>
          <div ref="complaintChart" class="chart-container"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 资金收支分析 -->
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <div class="chart-header">
            <h4>资金收支分析</h4>
            <span class="chart-subtitle">本月统计</span>
          </div>
          <div ref="fundChart" class="chart-container"></div>
        </div>
      </el-col>

      <!-- 会议活动统计 -->
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <div class="chart-header">
            <h4>会议活动统计</h4>
            <span class="chart-subtitle">最近3个月</span>
          </div>
          <div ref="meetingChart" class="chart-container"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons')

export default {
  name: 'PropertyCharts',
  props: {
    chartData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      voteChart: null,
      complaintChart: null,
      fundChart: null,
      meetingChart: null
    }
  },
  watch: {
    chartData: {
      handler() {
        this.$nextTick(() => {
          this.initCharts()
        })
      },
      deep: true
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    initCharts() {
      this.destroyCharts()
      this.initVoteChart()
      this.initComplaintChart()
      this.initFundChart()
      this.initMeetingChart()
    },

    destroyCharts() {
      if (this.voteChart) {
        this.voteChart.dispose()
        this.voteChart = null
      }
      if (this.complaintChart) {
        this.complaintChart.dispose()
        this.complaintChart = null
      }
      if (this.fundChart) {
        this.fundChart.dispose()
        this.fundChart = null
      }
      if (this.meetingChart) {
        this.meetingChart.dispose()
        this.meetingChart = null
      }
    },

    initVoteChart() {
      this.voteChart = echarts.init(this.$refs.voteChart, 'macarons')
      const data = this.chartData.voteParticipation || []

      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>参与率: {c}%'
        },
        grid: {
          top: '10%',
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date),
          axisLine: {
            lineStyle: {
              color: '#e6e6e6'
            }
          }
        },
        yAxis: {
          type: 'value',
          name: '参与率(%)',
          max: 100,
          axisLine: {
            lineStyle: {
              color: '#e6e6e6'
            }
          }
        },
        series: [{
          data: data.map(item => item.rate),
          type: 'line',
          smooth: true,
          symbol: 'circle',
          symbolSize: 6,
          lineStyle: {
            color: '#1890ff'
          },
          itemStyle: {
            color: '#1890ff'
          },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [{
                offset: 0, color: 'rgba(24, 144, 255, 0.3)'
              }, {
                offset: 1, color: 'rgba(24, 144, 255, 0.1)'
              }]
            }
          }
        }]
      }

      this.voteChart.setOption(option)
    },

    initComplaintChart() {
      this.complaintChart = echarts.init(this.$refs.complaintChart, 'macarons')
      const data = this.chartData.complaintTrend || []

      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: function(params) {
            let result = params[0].name + '<br/>'
            params.forEach(param => {
              result += param.seriesName + ': ' + param.value + '件<br/>'
            })
            return result
          }
        },
        legend: {
          data: ['新增投诉', '处理完成'],
          bottom: 0
        },
        grid: {
          top: '10%',
          left: '3%',
          right: '4%',
          bottom: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date),
          axisLine: {
            lineStyle: {
              color: '#e6e6e6'
            }
          }
        },
        yAxis: {
          type: 'value',
          name: '数量(件)',
          axisLine: {
            lineStyle: {
              color: '#e6e6e6'
            }
          }
        },
        series: [
          {
            name: '新增投诉',
            type: 'bar',
            data: data.map(item => item.newCount),
            itemStyle: {
              color: '#ff7875'
            }
          },
          {
            name: '处理完成',
            type: 'bar',
            data: data.map(item => item.completeCount),
            itemStyle: {
              color: '#73d13d'
            }
          }
        ]
      }

      this.complaintChart.setOption(option)
    },

    initFundChart() {
      this.fundChart = echarts.init(this.$refs.fundChart, 'macarons')
      const data = this.chartData.fundAnalysis || {}

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ¥{c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['经营性收入', '经营性支出', '维修资金收入', '维修资金支出']
        },
        series: [
          {
            name: '资金分析',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['60%', '50%'],
            data: [
              { value: data.operatingIncome || 0, name: '经营性收入', itemStyle: { color: '#52c41a' } },
              { value: data.operatingExpense || 0, name: '经营性支出', itemStyle: { color: '#ff7875' } },
              { value: data.maintenanceIncome || 0, name: '维修资金收入', itemStyle: { color: '#1890ff' } },
              { value: data.maintenanceExpense || 0, name: '维修资金支出', itemStyle: { color: '#faad14' } }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }

      this.fundChart.setOption(option)
    },

    initMeetingChart() {
      this.meetingChart = echarts.init(this.$refs.meetingChart, 'macarons')
      const data = this.chartData.meetingActivity || []

      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>会议数量: {c}场'
        },
        grid: {
          top: '10%',
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.month),
          axisLine: {
            lineStyle: {
              color: '#e6e6e6'
            }
          }
        },
        yAxis: {
          type: 'value',
          name: '数量(场)',
          axisLine: {
            lineStyle: {
              color: '#e6e6e6'
            }
          }
        },
        series: [{
          data: data.map(item => item.count),
          type: 'bar',
          barWidth: '60%',
          itemStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [{
                offset: 0, color: '#1890ff'
              }, {
                offset: 1, color: '#69c0ff'
              }]
            }
          }
        }]
      }

      this.meetingChart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.property-charts {
  .chart-wrapper {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h4 {
        margin: 0;
        color: #262626;
        font-size: 16px;
        font-weight: 600;
      }

      .chart-subtitle {
        font-size: 12px;
        color: #8c8c8c;
      }
    }

    .chart-container {
      height: 300px;
      width: 100%;
    }
  }
}
</style>
