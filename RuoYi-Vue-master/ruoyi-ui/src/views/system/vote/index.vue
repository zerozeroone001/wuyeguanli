<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会议ID" prop="meetingId">
        <el-input
          v-model="queryParams.meetingId"
          placeholder="请输入会议ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="议题ID" prop="topicId">
        <el-input
          v-model="queryParams.topicId"
          placeholder="请输入议题ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票选项" prop="voteOption">
        <el-select
          v-model="queryParams.voteOption"
          placeholder="请选择投票选项"
          clearable
        >
          <el-option label="同意" value="0" />
          <el-option label="反对" value="1" />
          <el-option label="弃权" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="投票方式" prop="voteType">
        <el-select
          v-model="queryParams.voteType"
          placeholder="请选择投票方式"
          clearable
        >
          <el-option label="小程序投票" value="0" />
          <el-option label="纸质投票" value="1" />
          <el-option label="语音投票" value="2" />
        </el-select>
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
      <el-table-column label="投票编号" align="center" prop="voteNo" />
      <el-table-column label="会议ID" align="center" prop="meetingId" />
      <el-table-column label="议题ID" align="center" prop="topicId" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="用户昵称" align="center" prop="userName" />
      <el-table-column label="投票选项" align="center" prop="voteOption" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.voteOption === 0" type="success">同意</el-tag>
          <el-tag v-else-if="scope.row.voteOption === 1" type="danger">反对</el-tag>
          <el-tag v-else-if="scope.row.voteOption === 2" type="info">弃权</el-tag>
          <span v-else>{{ scope.row.voteOption }}</span>
        </template>
      </el-table-column>
      <el-table-column label="投票方式" align="center" prop="voteType" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.voteType === 0" type="primary">小程序投票</el-tag>
          <el-tag v-else-if="scope.row.voteType === 1" type="warning">纸质投票</el-tag>
          <el-tag v-else-if="scope.row.voteType === 2" type="success">语音投票</el-tag>
          <span v-else>{{ scope.row.voteType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件地址" align="center" prop="flieUrl" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
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
        <el-form-item label="投票编号" prop="voteNo">
          <el-input v-model="form.voteNo" placeholder="请输入投票编号" />
        </el-form-item>
        <el-form-item label="会议ID" prop="meetingId">
          <el-input v-model="form.meetingId" placeholder="请输入会议ID" />
        </el-form-item>
        <el-form-item label="议题ID" prop="topicId">
          <el-input v-model="form.topicId" placeholder="请输入议题ID" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="用户昵称" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户昵称" />
        </el-form-item>
        <el-form-item label="投票选项" prop="voteOption">
          <el-select v-model="form.voteOption" placeholder="请选择投票选项">
            <el-option label="同意" value="0" />
            <el-option label="反对" value="1" />
            <el-option label="弃权" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="投票方式" prop="voteType">
          <el-select v-model="form.voteType" placeholder="请选择投票方式">
            <el-option label="小程序投票" value="0" />
            <el-option label="纸质投票" value="1" />
            <el-option label="语音投票" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="文件地址" prop="flieUrl">
          <el-input v-model="form.flieUrl" placeholder="请输入文件地址" />
        </el-form-item>
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
        topicId: null,
        userId: null,
        voteOption: null,
        voteType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        meetingId: [
          { required: true, message: "会议ID不能为空", trigger: "blur" }
        ],
        topicId: [
          { required: true, message: "议题ID不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        voteOption: [
          { required: true, message: "投票选项不能为空", trigger: "change" }
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
        voteNo: null,
        meetingId: null,
        topicId: null,
        userId: null,
        userName: null,
        voteOption: null,
        voteType: null,
        flieUrl: null,
        createTime: null,
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
