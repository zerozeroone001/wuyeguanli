<template>
  <div class="app-container vote-records-container">
    <!-- 顶部搜索栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" class="search-form">
      <el-form-item label="搜索项目:" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入/房号/姓名/手机号"
          clearable
          style="width: 250px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票状态:" prop="voteStatus">
        <el-select v-model="queryParams.voteStatus" placeholder="请选择" clearable style="width: 150px">
          <el-option label="全部" value=""></el-option>
          <el-option label="已投票" value="1"></el-option>
          <el-option label="未投票" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
      </el-form-item>
      <el-form-item label="查询时间:">
        <span class="query-time-label">{{ currentTime }}</span>
      </el-form-item>
      <el-form-item label="重新获取:">
        <el-switch v-model="autoRefresh" active-color="#13ce66" />
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="voteRecordsList"
      border
      stripe
      style="width: 100%"
      :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="姓名" prop="userName" align="center" width="140" />
      <el-table-column label="投票编号" prop="voteNo" align="center" width="120" />
      <el-table-column label="房号" prop="roomNumber" align="center"  />
      <el-table-column label="面积(m²)" prop="area" align="center"  />
      <el-table-column label="票权状态" align="center" >
        <template slot-scope="scope">
          <el-tag :type="scope.row.voteRightStatus === '正常' ? 'success' : 'danger'" size="mini">
            {{ scope.row.voteRightStatus }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="选民状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.voterStatus === '已投票' ? 'success' : 'info'" size="mini">
            {{ scope.row.voterStatus }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="认证状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="getAuthStatusType(scope.row.authStatus)" size="mini">
            {{ getAuthStatusText(scope.row.authStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="实际票权" prop="actualVoteRight" align="center"  />
      <el-table-column label="投票时间" prop="participationTime" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.participationTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleViewDetails(scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listVoteRecords } from '@/api/system/voteRecords'
import { mapGetters } from 'vuex'

export default {
  name: 'VoteRecords',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 投票记录表格数据
      voteRecordsList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        voteStatus: '',
        communityId: null,
        meetingId: null
      },
      // 当前时间
      currentTime: '',
      // 自动刷新
      autoRefresh: false,
      // 定时器
      timer: null
    }
  },
  computed: {
    ...mapGetters(['currentCommunityId', 'currentCommunityName'])
  },
  created() {
    // 获取路由参数中的会议ID
    this.queryParams.meetingId = this.$route.query.meetingId
    this.queryParams.communityId = this.currentCommunityId
    this.updateCurrentTime()
    this.getList()

    // 设置定时器更新时间
    this.timer = setInterval(() => {
      this.updateCurrentTime()
      if (this.autoRefresh) {
        this.getList()
      }
    }, 60000) // 每分钟更新一次
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    /** 查询投票记录列表 */
    getList() {
      this.loading = true
      listVoteRecords(this.queryParams).then(response => {
        this.voteRecordsList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 查看详情 */
    handleViewDetails(row) {
      this.$message.info('查看详情功能开发中')
    },
    /** 获取认证状态文本 */
    getAuthStatusText(status) {
      const statusMap = {
        0: '未认证',
        1: '待审核',
        2: '已认证',
        3: '认证失败'
      }
      return statusMap[status] || '未知'
    },
    /** 获取认证状态标签类型 */
    getAuthStatusType(status) {
      const typeMap = {
        0: 'info',
        1: 'warning',
        2: 'success',
        3: 'danger'
      }
      return typeMap[status] || 'info'
    },
    /** 更新当前时间 */
    updateCurrentTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      const seconds = String(now.getSeconds()).padStart(2, '0')
      this.currentTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style scoped lang="scss">
.vote-records-container {
  padding: 20px;
  background: #f5f5f5;
}

.search-form {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;

  ::v-deep .el-form-item {
    margin-bottom: 10px;
  }

  ::v-deep .el-form-item__label {
    font-weight: 600;
  }

  .query-time-label {
    color: #606266;
    font-size: 14px;
  }
}

::v-deep .el-table {
  background: #fff;
  border-radius: 4px;

  th {
    font-weight: 600;
  }

  .el-button--text {
    color: #409eff;

    &:hover {
      color: #66b1ff;
    }
  }
}

::v-deep .pagination-container {
  background: #fff;
  margin-top: 20px;
  padding: 20px;
  border-radius: 4px;
}
</style>
