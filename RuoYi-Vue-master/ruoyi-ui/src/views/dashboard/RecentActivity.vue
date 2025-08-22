<template>
  <div class="recent-activity">
    <div class="activity-header">
      <h4>
        <i class="el-icon-time"></i>
        最近活动
      </h4>
      <el-button size="mini" type="text" @click="refreshActivity">
        <i class="el-icon-refresh"></i>
        刷新
      </el-button>
    </div>

    <div v-if="activityList.length === 0" class="empty-activity">
      <i class="el-icon-document-copy"></i>
      <p>暂无活动记录</p>
    </div>

    <div v-else class="activity-timeline">
      <div
        v-for="(item, index) in activityList"
        :key="index"
        class="activity-item"
      >
        <div class="activity-time">
          {{ formatTime(item.createTime) }}
        </div>
        <div class="activity-content">
          <div class="activity-icon">
            <i :class="getActivityIcon(item.type)"></i>
          </div>
          <div class="activity-text">
            <span class="activity-title">{{ item.title }}</span>
            <span class="activity-desc">{{ item.description }}</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="activityList.length > 0" class="activity-footer">
      <el-button size="mini" type="text" @click="viewMore">
        查看更多
        <i class="el-icon-arrow-right"></i>
      </el-button>
    </div>
  </div>
</template>

<script>
import { parseTime } from '@/utils/ruoyi'

export default {
  name: 'RecentActivity',
  props: {
    activityList: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    refreshActivity() {
      this.$emit('refresh')
    },
    viewMore() {
      this.$emit('viewMore')
    },
    formatTime(time) {
      if (!time) return ''
      const now = new Date()
      const activityTime = new Date(time)
      const diff = now - activityTime

      const minutes = Math.floor(diff / (1000 * 60))
      const hours = Math.floor(diff / (1000 * 60 * 60))
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))

      if (minutes < 1) {
        return '刚刚'
      } else if (minutes < 60) {
        return `${minutes}分钟前`
      } else if (hours < 24) {
        return `${hours}小时前`
      } else if (days < 7) {
        return `${days}天前`
      } else {
        return parseTime(time, '{m}-{d}')
      }
    },
    getActivityIcon(type) {
      const iconMap = {
        'complaint': 'el-icon-warning',
        'meeting': 'el-icon-date',
        'fund': 'el-icon-money',
        'vote': 'el-icon-s-check',
        'contract': 'el-icon-document'
      }
      return iconMap[type] || 'el-icon-info'
    }
  }
}
</script>

<style lang="scss" scoped>
.recent-activity {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  height: 100%;

  .activity-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h4 {
      margin: 0;
      color: #262626;
      font-size: 16px;
      font-weight: 600;

      i {
        margin-right: 8px;
        color: #1890ff;
      }
    }
  }

  .empty-activity {
    text-align: center;
    padding: 40px 0;
    color: #999;

    i {
      font-size: 48px;
      color: #d9d9d9;
      margin-bottom: 16px;
      display: block;
    }

    p {
      margin: 0;
      font-size: 14px;
    }
  }

  .activity-timeline {
    max-height: 400px;
    overflow-y: auto;

    .activity-item {
      display: flex;
      margin-bottom: 16px;

      &:last-child {
        margin-bottom: 0;
      }

      .activity-time {
        width: 80px;
        font-size: 12px;
        color: #8c8c8c;
        text-align: right;
        padding-right: 12px;
        line-height: 20px;
        flex-shrink: 0;
      }

      .activity-content {
        flex: 1;
        display: flex;
        align-items: flex-start;

        .activity-icon {
          width: 20px;
          height: 20px;
          border-radius: 50%;
          background: #f0f0f0;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          flex-shrink: 0;

          i {
            font-size: 12px;
            color: #666;
          }
        }

        .activity-text {
          flex: 1;

          .activity-title {
            display: block;
            font-size: 14px;
            color: #262626;
            line-height: 20px;
            margin-bottom: 2px;
          }

          .activity-desc {
            display: block;
            font-size: 12px;
            color: #8c8c8c;
            line-height: 18px;
          }
        }
      }
    }
  }

  .activity-footer {
    text-align: center;
    margin-top: 16px;
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
  }
}
</style>
