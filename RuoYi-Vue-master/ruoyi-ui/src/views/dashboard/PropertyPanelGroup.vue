<template>
  <el-row :gutter="20" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleCardClick('owner')">
        <div class="card-panel-icon-wrapper icon-owner">
          <i class="el-icon-user-solid card-panel-icon"></i>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">业主总数</div>
          <count-to
            :start-val="0"
            :end-val="statistics.ownerStats ? statistics.ownerStats.total : 0"
            :duration="2600"
            class="card-panel-num"
          />
          <div class="card-panel-trend">
            <span
              :class="['trend', statistics.ownerStats && statistics.ownerStats.trend === 'up' ? 'trend-up' : 'trend-stable']"
            >
              <i :class="statistics.ownerStats && statistics.ownerStats.trend === 'up' ? 'el-icon-top' : 'el-icon-minus'"></i>
              {{ statistics.ownerStats ? statistics.ownerStats.monthNew : 0 }} 本月
            </span>
          </div>
        </div>
      </div>
    </el-col>

    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleCardClick('vote')">
        <div class="card-panel-icon-wrapper icon-vote">
          <i class="el-icon-s-check card-panel-icon"></i>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">投票进行中</div>
          <count-to
            :start-val="0"
            :end-val="statistics.voteStats ? statistics.voteStats.ongoing : 0"
            :duration="3000"
            class="card-panel-num"
          />
          <div class="card-panel-trend">
            <span
              :class="['trend', statistics.voteStats && statistics.voteStats.trend === 'up' ? 'trend-up' : 'trend-down']"
            >
              <i :class="statistics.voteStats && statistics.voteStats.trend === 'up' ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ statistics.voteStats ? Math.round(statistics.voteStats.participationRate) : 0 }}% 参与率
            </span>
          </div>
        </div>
      </div>
    </el-col>

    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleCardClick('complaint')">
        <div class="card-panel-icon-wrapper icon-complaint">
          <i class="el-icon-warning-outline card-panel-icon"></i>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">投诉待处理</div>
          <count-to
            :start-val="0"
            :end-val="statistics.complaintStats ? statistics.complaintStats.pending : 0"
            :duration="3200"
            class="card-panel-num"
          />
          <div class="card-panel-trend">
            <span
              :class="['trend', statistics.complaintStats && statistics.complaintStats.trend === 'up' ? 'trend-up' : 'trend-down']"
            >
              <i :class="statistics.complaintStats && statistics.complaintStats.trend === 'up' ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ statistics.complaintStats ? Math.round(statistics.complaintStats.growth) : 0 }}% 环比
            </span>
          </div>
        </div>
      </div>
    </el-col>

    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleCardClick('fund')">
        <div class="card-panel-icon-wrapper icon-fund">
          <i class="el-icon-money card-panel-icon"></i>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">资金余额</div>
          <div class="card-panel-num fund-amount">
            {{ formatCurrency(statistics.fundStats ? statistics.fundStats.total : 0) }}
          </div>
          <div class="card-panel-trend">
            <span
              :class="['trend', statistics.fundStats && statistics.fundStats.trend === 'up' ? 'trend-up' : 'trend-down']"
            >
              <i :class="statistics.fundStats && statistics.fundStats.trend === 'up' ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ statistics.fundStats ? Math.round(statistics.fundStats.growth) : 0 }}% 增长
            </span>
          </div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  name: 'PropertyPanelGroup',
  components: {
    CountTo
  },
  props: {
    statistics: {
      type: Object,
      default: () => ({})
    }
  },
  methods: {
    handleCardClick(type) {
      this.$emit('cardClick', type)
    },
    formatCurrency(amount) {
      if (!amount) return '¥0'
      const num = parseFloat(amount)
      if (num >= 10000) {
        return `¥${(num / 10000).toFixed(1)}万`
      }
      return `¥${num.toLocaleString()}`
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 120px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    border-radius: 8px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 4px 4px 40px rgba(0, 0, 0, .12);

      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-owner {
        background: #409EFF;
      }

      .icon-vote {
        background: #67C23A;
      }

      .icon-complaint {
        background: #F56C6C;
      }

      .icon-fund {
        background: #E6A23C;
      }
    }

    .icon-owner {
      color: #409EFF;
    }

    .icon-vote {
      color: #67C23A;
    }

    .icon-complaint {
      color: #F56C6C;
    }

    .icon-fund {
      color: #E6A23C;
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
        color: rgba(0, 0, 0, 0.85);
        line-height: 1;
      }

      .fund-amount {
        font-size: 18px;
        color: rgba(0, 0, 0, 0.85);
        line-height: 1;
      }

      .card-panel-trend {
        margin-top: 8px;
        font-size: 12px;

        .trend {
          display: inline-flex;
          align-items: center;

          i {
            margin-right: 2px;
          }
        }

        .trend-up {
          color: #67C23A;
        }

        .trend-down {
          color: #F56C6C;
        }

        .trend-stable {
          color: #909399;
        }
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
