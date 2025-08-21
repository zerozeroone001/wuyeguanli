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
          v-hasPermi="['system:generalMeeting:add']"
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
          v-hasPermi="['system:generalMeeting:edit']"
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
          v-hasPermi="['system:generalMeeting:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:generalMeeting:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="generalMeetingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会议ID" align="center" prop="meetingId" />
      <el-table-column label="会议主题" align="center" prop="title" />
      <el-table-column label="会议行事历信息" align="center" prop="calendarInfo" />
      <el-table-column label="会议公告" align="center" prop="announcement" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="线上投票结果" align="center" prop="onlineVoteResult" />
      <el-table-column label="线下投票结果" align="center" prop="offlineVoteResult" />
      <el-table-column label="语音投票结果" align="center" prop="voiceVoteResult" />
      <el-table-column label="最终决议" align="center" prop="finalResolution" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:generalMeeting:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:generalMeeting:remove']"
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

    <!-- 添加或修改业主大会会议管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会议主题" prop="title">
          <el-input v-model="form.title" placeholder="请输入会议主题" />
        </el-form-item>
        <el-form-item label="会议行事历信息" prop="calendarInfo">
          <el-input v-model="form.calendarInfo" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="会议公告" prop="announcement">
          <el-input v-model="form.announcement" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="线上投票结果" prop="onlineVoteResult">
          <el-input v-model="form.onlineVoteResult" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="线下投票结果" prop="offlineVoteResult">
          <el-input v-model="form.offlineVoteResult" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="语音投票结果" prop="voiceVoteResult">
          <el-input v-model="form.voiceVoteResult" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="最终决议" prop="finalResolution">
          <el-input v-model="form.finalResolution" type="textarea" placeholder="请输入内容" />
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
import { listGeneralMeeting, getGeneralMeeting, delGeneralMeeting, addGeneralMeeting, updateGeneralMeeting } from "@/api/system/generalMeeting"

export default {
  name: "GeneralMeeting",
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
      // 业主大会会议管理表格数据
      generalMeetingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        calendarInfo: null,
        announcement: null,
        status: null,
        onlineVoteResult: null,
        offlineVoteResult: null,
        voiceVoteResult: null,
        finalResolution: null,
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
    /** 查询业主大会会议管理列表 */
    getList() {
      this.loading = true
      listGeneralMeeting(this.queryParams).then(response => {
        this.generalMeetingList = response.rows
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
        calendarInfo: null,
        announcement: null,
        status: null,
        onlineVoteResult: null,
        offlineVoteResult: null,
        voiceVoteResult: null,
        finalResolution: null,
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
      this.title = "添加业主大会会议管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const meetingId = row.meetingId || this.ids
      getGeneralMeeting(meetingId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改业主大会会议管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.meetingId != null) {
            updateGeneralMeeting(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addGeneralMeeting(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除业主大会会议管理编号为"' + meetingIds + '"的数据项？').then(function() {
        return delGeneralMeeting(meetingIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/generalMeeting/export', {
        ...this.queryParams
      }, `generalMeeting_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
