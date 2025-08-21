<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联的业主大会ID" prop="meetingId">
        <el-input
          v-model="queryParams.meetingId"
          placeholder="请输入关联的业主大会ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票的业主用户ID (关联sys_user)" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入投票的业主用户ID (关联sys_user)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表决票编号 (唯一)" prop="voteTicketNo">
        <el-input
          v-model="queryParams.voteTicketNo"
          placeholder="请输入表决票编号 (唯一)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票选项" prop="voteOption">
        <el-input
          v-model="queryParams.voteOption"
          placeholder="请输入投票选项"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票来源" prop="voteSource">
        <el-input
          v-model="queryParams.voteSource"
          placeholder="请输入投票来源"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票时间" prop="voteTime">
        <el-date-picker clearable
          v-model="queryParams.voteTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择投票时间">
        </el-date-picker>
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
          v-hasPermi="['system:vote:add']"
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
          v-hasPermi="['system:vote:edit']"
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
          v-hasPermi="['system:vote:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:vote:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="voteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="投票ID" align="center" prop="voteId" />
      <el-table-column label="关联的业主大会ID" align="center" prop="meetingId" />
      <el-table-column label="投票的业主用户ID (关联sys_user)" align="center" prop="userId" />
      <el-table-column label="表决票编号 (唯一)" align="center" prop="voteTicketNo" />
      <el-table-column label="投票选项" align="center" prop="voteOption" />
      <el-table-column label="投票来源" align="center" prop="voteSource" />
      <el-table-column label="投票时间" align="center" prop="voteTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.voteTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:vote:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:vote:remove']"
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

    <!-- 添加或修改业主大会投票对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联的业主大会ID" prop="meetingId">
          <el-input v-model="form.meetingId" placeholder="请输入关联的业主大会ID" />
        </el-form-item>
        <el-form-item label="投票的业主用户ID (关联sys_user)" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入投票的业主用户ID (关联sys_user)" />
        </el-form-item>
        <el-form-item label="表决票编号 (唯一)" prop="voteTicketNo">
          <el-input v-model="form.voteTicketNo" placeholder="请输入表决票编号 (唯一)" />
        </el-form-item>
        <el-form-item label="投票选项" prop="voteOption">
          <el-input v-model="form.voteOption" placeholder="请输入投票选项" />
        </el-form-item>
        <el-form-item label="投票来源" prop="voteSource">
          <el-input v-model="form.voteSource" placeholder="请输入投票来源" />
        </el-form-item>
        <el-form-item label="投票时间" prop="voteTime">
          <el-date-picker clearable
            v-model="form.voteTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择投票时间">
          </el-date-picker>
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
import { listVote, getVote, delVote, addVote, updateVote } from "@/api/system/vote"

export default {
  name: "Vote",
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
      // 业主大会投票表格数据
      voteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        meetingId: null,
        userId: null,
        voteTicketNo: null,
        voteOption: null,
        voteSource: null,
        voteTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        meetingId: [
          { required: true, message: "关联的业主大会ID不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "投票的业主用户ID (关联sys_user)不能为空", trigger: "blur" }
        ],
        voteTicketNo: [
          { required: true, message: "表决票编号 (唯一)不能为空", trigger: "blur" }
        ],
        voteOption: [
          { required: true, message: "投票选项不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询业主大会投票列表 */
    getList() {
      this.loading = true
      listVote(this.queryParams).then(response => {
        this.voteList = response.rows
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
        voteId: null,
        meetingId: null,
        userId: null,
        voteTicketNo: null,
        voteOption: null,
        voteSource: null,
        voteTime: null,
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
      this.ids = selection.map(item => item.voteId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加业主大会投票"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const voteId = row.voteId || this.ids
      getVote(voteId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改业主大会投票"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.voteId != null) {
            updateVote(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addVote(this.form).then(response => {
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
      const voteIds = row.voteId || this.ids
      this.$modal.confirm('是否确认删除业主大会投票编号为"' + voteIds + '"的数据项？').then(function() {
        return delVote(voteIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/vote/export', {
        ...this.queryParams
      }, `vote_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
