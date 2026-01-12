<template>
  <div class="app-container visit-container">
    <!-- 权限检查提示 -->
    <el-alert
      v-if="!isAdmin"
      title="权限不足"
      type="warning"
      description="您不是管理员,无法使用上门拜访功能"
      :closable="false"
      show-icon
      style="margin-bottom: 20px;">
    </el-alert>

    <div v-if="isAdmin">
      <!-- 顶部筛选区域 -->
      <el-card shadow="never" class="filter-card">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          <el-form-item label="选择会议">
            <el-select 
              v-model="queryParams.meetingId" 
              placeholder="请选择会议" 
              filterable
              @change="handleMeetingChange"
              style="width: 300px;">
              <el-option
                v-for="meeting in meetingList"
                :key="meeting.meetingId"
                :label="meeting.meetingTitle"
                :value="meeting.meetingId">
                <span style="float: left">{{ meeting.meetingTitle }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ meeting.communityName }}</span>
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="选择楼栋">
            <el-select 
              v-model="queryParams.buildingName" 
              placeholder="请选择楼栋"
              @change="handleBuildingChange"
              :disabled="!queryParams.meetingId"
              style="width: 200px;">
              <el-option
                v-for="building in buildingList"
                :key="building.buildingName"
                :label="building.buildingName"
                :value="building.buildingName">
                <span>{{ building.buildingName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ building.roomCount }}户</span>
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="选择单元" v-if="showUnitSelect">
            <el-select 
              v-model="queryParams.unitName" 
              placeholder="请选择单元"
              @change="handleUnitChange"
              clearable
              style="width: 150px;">
              <el-option
                v-for="unit in unitList"
                :key="unit"
                :label="unit"
                :value="unit">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 统计信息 -->
      <el-row :gutter="20" style="margin-top: 20px;" v-if="roomList.length > 0">
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">总户数</div>
              <div class="stat-value">{{ statistics.total }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">已投票</div>
              <div class="stat-value" style="color: #67C23A;">{{ statistics.voted }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">未投票</div>
              <div class="stat-value" style="color: #F56C6C;">{{ statistics.notVoted }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">投票率</div>
              <div class="stat-value" style="color: #409EFF;">{{ statistics.voteRate }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 房屋列表 -->
      <el-card shadow="never" style="margin-top: 20px;">
        <div slot="header" class="clearfix">
          <span>房屋列表</span>
          <div style="float: right;">
            <el-tag size="small" style="margin-right: 10px;">
              <i class="el-icon-info"></i> 状态说明
            </el-tag>
            <el-tag type="info" size="small" style="margin-right: 5px;">0-未送</el-tag>
            <el-tag type="warning" size="small" style="margin-right: 5px;">1-已送无人</el-tag>
            <el-tag type="primary" size="small" style="margin-right: 5px;">2-已送未收</el-tag>
            <el-tag type="success" size="small" style="margin-right: 5px;">3-已收未投</el-tag>
            <el-tag type="success" size="small" style="margin-right: 5px;">4-已投待唱</el-tag>
            <el-tag type="success" size="small">5-已唱</el-tag>
          </div>
        </div>

        <el-table
          v-loading="loading"
          :data="roomList"
          border
          stripe
          style="width: 100%">
          <el-table-column label="房号" prop="roomNumber" align="center" width="120" />
          <el-table-column label="业主姓名" prop="ownerName" align="center" width="120" />
          <el-table-column label="联系电话" prop="phone" align="center" width="150" />
          
          <el-table-column label="投票状态" align="center" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.hasVoted ? 'success' : 'danger'" size="small">
                {{ scope.row.hasVoted ? '已投票' : '未投票' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="短信通知" align="center" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.smsNotifyStatus === 1 ? 'success' : 'info'" size="small">
                {{ scope.row.smsNotifyStatus === 1 ? '已通知' : '未通知' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="电话通知" align="center" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.phoneNotifyStatus === 1 ? 'success' : 'info'" size="small">
                {{ scope.row.phoneNotifyStatus === 1 ? '已通知' : '未通知' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="线下拜访状态" align="center" width="150">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.offlineStatus)" size="small">
                {{ getStatusText(scope.row.offlineStatus) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" width="300">
            <template slot-scope="scope">
              <el-button-group>
                <el-button size="mini" type="warning" @click="handleUpdateStatus(scope.row, 1)">已送无人</el-button>
                <el-button size="mini" type="primary" @click="handleUpdateStatus(scope.row, 2)">已送未收</el-button>
                <el-button size="mini" type="success" @click="handleUpdateStatus(scope.row, 3)">已收未投</el-button>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import { checkAdmin, getBuildingList, getUnitList, getRoomList, updateOfflineStatus } from '@/api/system/visit'
import { listMeeting } from '@/api/system/meeting'

export default {
  name: 'Visit',
  data() {
    return {
      // 权限相关
      isAdmin: false,
      adminInfo: {},
      // 加载状态
      loading: false,
      // 会议列表
      meetingList: [],
      // 楼栋列表
      buildingList: [],
      // 单元列表
      unitList: [],
      // 房屋列表
      roomList: [],
      // 查询参数
      queryParams: {
        meetingId: null,
        buildingName: null,
        unitName: null
      },
      // 是否显示单元选择
      showUnitSelect: false
    }
  },
  computed: {
    // 统计信息
    statistics() {
      const total = this.roomList.length
      const voted = this.roomList.filter(room => room.hasVoted).length
      const notVoted = total - voted
      const voteRate = total > 0 ? ((voted / total) * 100).toFixed(2) + '%' : '0%'
      
      return {
        total,
        voted,
        notVoted,
        voteRate
      }
    }
  },
  created() {
    this.checkAdminPermission()
  },
  methods: {
    // 检查管理员权限
    async checkAdminPermission() {
      try {
        const response = await checkAdmin()
        if (response.code === 200) {
          this.isAdmin = response.data.isAdmin
          if (this.isAdmin) {
            this.adminInfo = response.data
            this.loadMeetingList()
          }
        }
      } catch (error) {
        console.error('检查管理员权限失败:', error)
        this.$message.error('检查权限失败')
      }
    },

    // 加载会议列表
    async loadMeetingList() {
      try {
        const response = await listMeeting({
          communityId: this.adminInfo.communityId,
          meetingType: 1,
          pageNum: 1,
          pageSize: 100
        })
        if (response.code === 200) {
          this.meetingList = response.rows || []
        }
      } catch (error) {
        console.error('加载会议列表失败:', error)
      }
    },

    // 会议变更
    async handleMeetingChange(meetingId) {
      if (!meetingId) return
      
      this.queryParams.buildingName = null
      this.queryParams.unitName = null
      this.buildingList = []
      this.unitList = []
      this.roomList = []
      
      try {
        const response = await getBuildingList(this.adminInfo.communityId)
        if (response.code === 200) {
          this.buildingList = response.data || []
        }
      } catch (error) {
        console.error('加载楼栋列表失败:', error)
        this.$message.error('加载楼栋列表失败')
      }
    },

    // 楼栋变更
    async handleBuildingChange(buildingName) {
      if (!buildingName) return
      
      this.queryParams.unitName = null
      this.unitList = []
      this.roomList = []
      
      // 检查是否有单元
      const building = this.buildingList.find(b => b.buildingName === buildingName)
      this.showUnitSelect = building && building.hasUnit
      
      if (this.showUnitSelect) {
        try {
          const response = await getUnitList(this.adminInfo.communityId, buildingName)
          if (response.code === 200) {
            this.unitList = response.data || []
          }
        } catch (error) {
          console.error('加载单元列表失败:', error)
          this.$message.error('加载单元列表失败')
        }
      }
    },

    // 单元变更
    handleUnitChange() {
      this.roomList = []
    },

    // 查询房屋列表
    async handleQuery() {
      if (!this.queryParams.meetingId) {
        this.$message.warning('请先选择会议')
        return
      }
      if (!this.queryParams.buildingName) {
        this.$message.warning('请先选择楼栋')
        return
      }
      
      this.loading = true
      try {
        const response = await getRoomList(
          this.adminInfo.communityId,
          this.queryParams.buildingName,
          this.queryParams.unitName,
          this.queryParams.meetingId
        )
        if (response.code === 200) {
          this.roomList = response.data || []
        }
      } catch (error) {
        console.error('加载房屋列表失败:', error)
        this.$message.error('加载房屋列表失败')
      } finally {
        this.loading = false
      }
    },

    // 重置查询
    resetQuery() {
      this.queryParams = {
        meetingId: null,
        buildingName: null,
        unitName: null
      }
      this.buildingList = []
      this.unitList = []
      this.roomList = []
      this.showUnitSelect = false
    },

    // 更新状态
    async handleUpdateStatus(row, status) {
      if (!row.userId) {
        this.$message.warning('该房屋未绑定业主,无法更新状态')
        return
      }

      try {
        const response = await updateOfflineStatus({
          meetingId: this.queryParams.meetingId,
          userId: row.userId,
          offlineStatus: status,
          visitorId: this.adminInfo.adminId,
          visitorName: this.adminInfo.adminName
        })
        
        if (response.code === 200) {
          this.$message.success('状态更新成功')
          // 刷新列表
          this.handleQuery()
        } else {
          this.$message.error(response.msg || '状态更新失败')
        }
      } catch (error) {
        console.error('更新状态失败:', error)
        this.$message.error('状态更新失败')
      }
    },

    // 获取状态类型
    getStatusType(status) {
      const typeMap = {
        0: 'info',
        1: 'warning',
        2: 'primary',
        3: 'success',
        4: 'success',
        5: 'success'
      }
      return typeMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        0: '未送',
        1: '已送无人',
        2: '已送未收',
        3: '已收未投',
        4: '已投待唱',
        5: '已唱'
      }
      return textMap[status] || '未知'
    }
  }
}
</script>

<style scoped lang="scss">
.visit-container {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 10px 0;
  
  .stat-label {
    font-size: 14px;
    color: #909399;
    margin-bottom: 10px;
  }
  
  .stat-value {
    font-size: 28px;
    font-weight: bold;
    color: #303133;
  }
}

::v-deep .el-card__header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
}
</style>
