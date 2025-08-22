<template>
  <div class="dashboard-container">
    <!-- 页面标题 -->
    <div class="dashboard-header">
      <h2>
        <i class="el-icon-monitor"></i>
        智慧物业管理仪表盘
      </h2>
    </div>

    <!-- 统计卡片 -->
    <property-panel-group
      :statistics="statistics"
      @cardClick="handleCardClick"
    />

    <!-- 待办事项和最近活动 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :lg="12">
        <todo-list
          :todo-list="todoList"
          @todoClick="handleTodoClick"
          @refresh="refreshTodoList"
        />
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <recent-activity
          :activity-list="activityList"
          @refresh="refreshActivity"
          @viewMore="viewMoreActivity"
        />
      </el-col>
    </el-row>

    <!-- 数据图表 -->
    <div style="margin-top: 20px;">
      <property-charts :chart-data="chartData" />
    </div>
  </div>
</template>

<script>
import PropertyPanelGroup from './dashboard/PropertyPanelGroup'
import TodoList from './dashboard/TodoList'
import RecentActivity from './dashboard/RecentActivity'
import PropertyCharts from './dashboard/PropertyCharts'
import { getStatistics, getTodoList, getChartData, getRecentActivity } from '@/api/system/dashboard'

export default {
  name: 'Index',
  components: {
    PropertyPanelGroup,
    TodoList,
    RecentActivity,
    PropertyCharts
  },
  data() {
    return {
      // 统计数据
      statistics: {},
      // 待办事项
      todoList: [],
      // 最近活动
      activityList: [],
      // 图表数据
      chartData: {},
      // 加载状态
      loading: {
        statistics: false,
        todo: false,
        activity: false,
        chart: false
      }
    }
  },
  created() {
    this.initDashboard()
  },
  methods: {
    // 初始化仪表盘
    async initDashboard() {
      await Promise.all([
        this.loadStatistics(),
        this.loadTodoList(),
        this.loadActivity(),
        this.loadChartData()
      ])
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        this.loading.statistics = true
        const response = await getStatistics()
        this.statistics = response.data || {}
      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.$modal.msgError('加载统计数据失败')
      } finally {
        this.loading.statistics = false
      }
    },

    // 加载待办事项
    async loadTodoList() {
      try {
        this.loading.todo = true
        const response = await getTodoList()
        this.todoList = response.data || []
      } catch (error) {
        console.error('加载待办事项失败:', error)
        this.$modal.msgError('加载待办事项失败')
      } finally {
        this.loading.todo = false
      }
    },

    // 加载最近活动
    async loadActivity() {
      try {
        this.loading.activity = true
        const response = await getRecentActivity()
        this.activityList = response.data || []
      } catch (error) {
        console.error('加载最近活动失败:', error)
        this.$modal.msgError('加载最近活动失败')
      } finally {
        this.loading.activity = false
      }
    },

    // 加载图表数据
    async loadChartData() {
      try {
        this.loading.chart = true
        const response = await getChartData()
        this.chartData = response.data || {}
      } catch (error) {
        console.error('加载图表数据失败:', error)
        this.$modal.msgError('加载图表数据失败')
      } finally {
        this.loading.chart = false
      }
    },

    // 统计卡片点击事件
    handleCardClick(type) {
      const routeMap = {
        'owner': '/system/user',
        'vote': '/system/meeting',
        'complaint': '/system/complaint',
        'fund': '/system/flow'
      }

      if (routeMap[type]) {
        this.$router.push(routeMap[type])
      }
    },

    // 待办事项点击事件
    handleTodoClick(item) {
      if (item.url) {
        this.$router.push(item.url)
      }
    },

    // 刷新待办事项
    async refreshTodoList() {
      await this.loadTodoList()
      this.$message.success('待办事项已刷新')
    },

    // 刷新最近活动
    async refreshActivity() {
      await this.loadActivity()
      this.$message.success('最近活动已刷新')
    },

    // 查看更多活动
    viewMoreActivity() {
      this.$router.push('/monitor/operlog')
    },

    // 刷新所有数据
    async refreshAll() {
      this.$message.info('正在刷新数据...')
      await this.initDashboard()
      this.$message.success('数据刷新完成')
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);

  .dashboard-header {
    margin-bottom: 10px;

    h2 {
      margin: 0;
      color: #262626;
      font-size: 24px;
      font-weight: 600;

      i {
        margin-right: 12px;
        color: #1890ff;
      }
    }

    .dashboard-subtitle {
      margin: 8px 0 0 0;
      color: #8c8c8c;
      font-size: 14px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .dashboard-container {
    padding: 16px;

    .dashboard-header {
      h2 {
        font-size: 20px;
      }
    }
  }
}
</style>
