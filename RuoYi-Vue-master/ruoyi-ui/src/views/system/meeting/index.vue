<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会议标题" prop="meetingTitle">
        <el-input
          v-model="queryParams.meetingTitle"
          placeholder="请输入会议标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会议时间" prop="meetingTime">
        <el-date-picker clearable
          v-model="queryParams.meetingTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择会议时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="会议地点" prop="meetingLocation">
        <el-input
          v-model="queryParams.meetingLocation"
          placeholder="请输入会议地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票开始时间" prop="voteStartTime">
        <el-date-picker clearable
          v-model="queryParams.voteStartTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择投票开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="投票结束时间" prop="voteEndTime">
        <el-date-picker clearable
          v-model="queryParams.voteEndTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择投票结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="应参与人数" prop="totalVoters">
        <el-input
          v-model="queryParams.totalVoters"
          placeholder="请输入应参与人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际参与人数" prop="actualVoters">
        <el-input
          v-model="queryParams.actualVoters"
          placeholder="请输入实际参与人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:meeting:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:meeting:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:meeting:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:meeting:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="meetingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会议ID" align="center" prop="meetingId" />
      <el-table-column label="会议标题" align="center" prop="meetingTitle" />
      <el-table-column label="会议类型" align="center" prop="meetingType" />
      <el-table-column label="会议内容" align="center" prop="meetingContent" />
      <el-table-column label="会议时间" align="center" prop="meetingTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.meetingTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会议地点" align="center" prop="meetingLocation" />
      <el-table-column label="会议状态(0-筹备中,1-进行中,2-已结束)" align="center" prop="meetingStatus" />
      <el-table-column label="投票开始时间" align="center" prop="voteStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.voteStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="投票结束时间" align="center" prop="voteEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.voteEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应参与人数" align="center" prop="totalVoters" />
      <el-table-column label="实际参与人数" align="center" prop="actualVoters" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:meeting:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:meeting:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改会议管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会议标题" prop="meetingTitle">
          <el-input v-model="form.meetingTitle" placeholder="请输入会议标题" />
        </el-form-item>
        <el-form-item label="会议内容">
          <editor v-model="form.meetingContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="会议时间" prop="meetingTime">
          <el-date-picker clearable
            v-model="form.meetingTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择会议时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="会议地点" prop="meetingLocation">
          <el-input v-model="form.meetingLocation" placeholder="请输入会议地点" />
        </el-form-item>
        <el-form-item label="投票开始时间" prop="voteStartTime">
          <el-date-picker clearable
            v-model="form.voteStartTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择投票开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="投票结束时间" prop="voteEndTime">
          <el-date-picker clearable
            v-model="form.voteEndTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择投票结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="应参与人数" prop="totalVoters">
          <el-input v-model="form.totalVoters" placeholder="请输入应参与人数" />
        </el-form-item>
        <el-form-item label="实际参与人数" prop="actualVoters">
          <el-input v-model="form.actualVoters" placeholder="请输入实际参与人数" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMeeting, getMeeting, delMeeting, addMeeting, updateMeeting } from "@/api/system/meeting"

export default {
  name: "Meeting",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 会议管理表格数据
      meetingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        meetingTitle: null,
        meetingType: null,
        meetingContent: null,
        meetingTime: null,
        meetingLocation: null,
        meetingStatus: null,
        voteStartTime: null,
        voteEndTime: null,
        totalVoters: null,
        actualVoters: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        meetingTitle: [
          { required: true, message: "会议标题不能为空", trigger: "blur" }
        ],
        meetingType: [
          { required: true, message: "会议类型不能为空", trigger: "change" }
        ],
        meetingTime: [
          { required: true, message: "会议时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询会议管理列表 */
    getList() {
      this.loading = true
      listMeeting(this.queryParams).then(response => {
        this.meetingList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        meetingId: null,
        meetingTitle: null,
        meetingType: null,
        meetingContent: null,
        meetingTime: null,
        meetingLocation: null,
        meetingStatus: null,
        voteStartTime: null,
        voteEndTime: null,
        totalVoters: null,
        actualVoters: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.meetingId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加会议管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const meetingId = row.meetingId || this.ids
      getMeeting(meetingId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改会议管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.meetingId != null) {
            updateMeeting(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMeeting(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const meetingIds = row.meetingId || this.ids
      this.$modal.confirm('是否确认删除会议管理编号为"' + meetingIds + '"的数据项？').then(function() {
        return delMeeting(meetingIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/meeting/export', {
        ...this.queryParams
      }, `meeting_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
