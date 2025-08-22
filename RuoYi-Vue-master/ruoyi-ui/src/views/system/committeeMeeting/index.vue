<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会议主题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入会议主题"
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
      <el-form-item label="会议地点" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入会议地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会议记录文件URL" prop="recordFileUrl">
        <el-input
          v-model="queryParams.recordFileUrl"
          placeholder="请输入会议记录文件URL"
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
          v-hasPermi="['system:committeeMeeting:add']"
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
          v-hasPermi="['system:committeeMeeting:edit']"
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
          v-hasPermi="['system:committeeMeeting:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:committeeMeeting:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="committeeMeetingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会议ID" align="center" prop="meetingId" />
      <el-table-column label="会议主题" align="center" prop="title" />
      <el-table-column label="会议时间" align="center" prop="meetingTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.meetingTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会议地点" align="center" prop="location" />
      <el-table-column label="会议通知内容" align="center" prop="noticeContent" />
      <el-table-column label="会议记录文件URL" align="center" prop="recordFileUrl" />
      <el-table-column label="会议决议内容" align="center" prop="resolutionContent" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:committeeMeeting:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:committeeMeeting:remove']"
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

    <!-- 添加或修改业主委员会会议管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会议主题" prop="title">
          <el-input v-model="form.title" placeholder="请输入会议主题" />
        </el-form-item>
        <el-form-item label="会议时间" prop="meetingTime">
          <el-date-picker clearable
            v-model="form.meetingTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择会议时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="会议地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入会议地点" />
        </el-form-item>
        <el-form-item label="会议通知内容">
          <editor v-model="form.noticeContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="会议记录文件URL" prop="recordFileUrl">
          <el-input v-model="form.recordFileUrl" placeholder="请输入会议记录文件URL" />
        </el-form-item>
        <el-form-item label="会议决议内容">
          <editor v-model="form.resolutionContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listCommitteeMeeting, getCommitteeMeeting, delCommitteeMeeting, addCommitteeMeeting, updateCommitteeMeeting } from "@/api/system/committeeMeeting"

export default {
  name: "CommitteeMeeting",
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
      // 业主委员会会议管理表格数据
      committeeMeetingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        meetingTime: null,
        location: null,
        noticeContent: null,
        recordFileUrl: null,
        resolutionContent: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "会议主题不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询业主委员会会议管理列表 */
    getList() {
      this.loading = true
      listCommitteeMeeting(this.queryParams).then(response => {
        this.committeeMeetingList = response.rows
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
        title: null,
        meetingTime: null,
        location: null,
        noticeContent: null,
        recordFileUrl: null,
        resolutionContent: null,
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.title = "添加业主委员会会议管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const meetingId = row.meetingId || this.ids
      getCommitteeMeeting(meetingId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改业主委员会会议管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.meetingId != null) {
            updateCommitteeMeeting(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCommitteeMeeting(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除业主委员会会议管理编号为"' + meetingIds + '"的数据项？').then(function() {
        return delCommitteeMeeting(meetingIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/committeeMeeting/export', {
        ...this.queryParams
      }, `committeeMeeting_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
