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
      <el-table-column label="投票编号" prop="voteNo" align="center" width="200" />
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
      <el-table-column label="实际票权" prop="actualVoteRight" align="center"  />
      <el-table-column label="投票时间" prop="participationTime" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.participationTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            @click="handleViewDetails(scope.row)"
          >查看详情</el-button>
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

    <!-- 投票详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-table v-loading="detailLoading" :data="detailList" border stripe>
        <el-table-column label="议题" prop="topicTitle" align="center" />
        <el-table-column label="姓名" prop="userName" align="center" width="120" />
        <el-table-column label="投票选项" align="center" width="100">
          <template slot-scope="scope">
            <el-tag :type="getVoteOptionType(scope.row.voteOption)">
              {{ getVoteOptionText(scope.row.voteOption) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="投票时间" align="center" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.voteTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="投票方式" align="center" width="120">
          <template slot-scope="scope">
            {{ getVoteTypeText(scope.row.voteType) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 操作日志区域 -->
      <div style="margin-top: 20px;">
        <div style="font-size: 16px; font-weight: bold; margin-bottom: 10px; color: #303133;">
          <i class="el-icon-document"></i> 操作日志
        </div>
        <el-table v-loading="logLoading" :data="meetingLogList" border stripe max-height="300">
          <el-table-column label="日志类型" align="center" width="180">
            <template slot-scope="scope">
              <el-tag :type="getLogTypeTag(scope.row.logType)" size="small">
                {{ getLogTypeText(scope.row.logType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="日志描述" prop="logDesc" align="center" show-overflow-tooltip />
          <el-table-column label="操作人" prop="operatorName" align="center" width="120">
            <template slot-scope="scope">
              <span>{{ scope.row.operatorName || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listVoteRecords, listVoteDetails } from '@/api/system/voteRecords'
import { listMeetingLog } from '@/api/system/meetingLog'
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
      // 定时器
      timer: null,
      // 详情弹窗开关
      detailOpen: false,
      // 详情弹窗标题
      detailTitle: '',
      // 详情列表数据
      detailList: [],
      // 详情加载中
      detailLoading: false,
      // 日志列表数据
      meetingLogList: [],
      // 日志加载中
      logLoading: false
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
      this.detailTitle = `投票详情 - ${row.userName}`
      this.detailOpen = true
      this.detailLoading = true
      this.logLoading = true
      this.detailList = []
      this.meetingLogList = []

      const query = {
        meetingId: this.queryParams.meetingId,
        userId: row.userId
      }

      // 查询投票详情
      listVoteDetails(query).then(response => {
        this.detailList = response.rows
        this.detailLoading = false
      }).catch(() => {
        this.detailLoading = false
      })

      // 查询操作日志
      const logQuery = {
        meetingId: this.queryParams.meetingId,
        userId: row.userId
      }
      listMeetingLog(logQuery).then(response => {
        this.meetingLogList = response.rows || []
        this.logLoading = false
      }).catch(() => {
        this.logLoading = false
      })
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
    /** 获取投票选项文本 */
    getVoteOptionText(option) {
      const map = {
        0: '同意',
        1: '反对',
        2: '弃权'
      }
      return map[option] || '未知'
    },
    /** 获取投票选项标签类型 */
    getVoteOptionType(option) {
      const map = {
        0: 'success',
        1: 'danger',
        2: 'info'
      }
      return map[option] || ''
    },
    /** 获取投票方式文本 */
    getVoteTypeText(type) {
      const map = {
        0: '小程序投票',
        1: '纸质投票',
        2: '语音投票'
      }
      return map[type] || '未知'
    },
    /** 获取日志类型文本 */
    getLogTypeText(type) {
      const map = {
        1: '线上-已参会未投票',
        2: '线上-已参会已投票',
        3: '线下拜访-已送无人',
        4: '线下拜访-已送未收',
        5: '线下拜访-已收未投',
        6: '线下拜访-已投待唱',
        7: '线下拜访-已唱',
        8: '短信通知',
        9: '电话通知'
      }
      return map[type] || '未知类型'
    },
    /** 获取日志类型标签样式 */
    getLogTypeTag(type) {
      // 线上相关: success
      if (type === 1 || type === 2) return 'success'
      // 线下拜访相关: warning
      if (type >= 3 && type <= 7) return 'warning'
      // 通知相关: info
      if (type === 8 || type === 9) return 'info'
      return ''
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
