<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属会议ID" prop="meetingId">
        <el-input
          v-model="queryParams.meetingId"
          placeholder="请输入所属会议ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="议题标题" prop="topicTitle">
        <el-input
          v-model="queryParams.topicTitle"
          placeholder="请输入议题标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="同意票数" prop="agreeCount">
        <el-input
          v-model="queryParams.agreeCount"
          placeholder="请输入同意票数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="反对票数" prop="opposeCount">
        <el-input
          v-model="queryParams.opposeCount"
          placeholder="请输入反对票数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="弃权票数" prop="abstainCount">
        <el-input
          v-model="queryParams.abstainCount"
          placeholder="请输入弃权票数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示顺序" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入显示顺序"
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
          v-hasPermi="['system:topic:add']"
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
          v-hasPermi="['system:topic:edit']"
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
          v-hasPermi="['system:topic:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:topic:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="topicList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="议题ID" align="center" prop="topicId" />
      <el-table-column label="所属会议ID" align="center" prop="meetingId" />
      <el-table-column label="议题标题" align="center" prop="topicTitle" />
      <el-table-column label="议题内容" align="center" prop="topicContent" />
      <el-table-column label="附件URL列表" align="center" prop="files" />
      <el-table-column label="同意票数" align="center" prop="agreeCount" />
      <el-table-column label="反对票数" align="center" prop="opposeCount" />
      <el-table-column label="弃权票数" align="center" prop="abstainCount" />
      <el-table-column label="显示顺序" align="center" prop="orderNum" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:topic:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:topic:remove']"
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

    <!-- 添加或修改会议议题对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属会议ID" prop="meetingId">
          <el-input v-model="form.meetingId" placeholder="请输入所属会议ID" />
        </el-form-item>
        <el-form-item label="议题标题" prop="topicTitle">
          <el-input v-model="form.topicTitle" placeholder="请输入议题标题" />
        </el-form-item>
        <el-form-item label="议题内容">
          <editor v-model="form.topicContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="附件URL列表" prop="files">
          <el-input v-model="form.files" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="同意票数" prop="agreeCount">
          <el-input v-model="form.agreeCount" placeholder="请输入同意票数" />
        </el-form-item>
        <el-form-item label="反对票数" prop="opposeCount">
          <el-input v-model="form.opposeCount" placeholder="请输入反对票数" />
        </el-form-item>
        <el-form-item label="弃权票数" prop="abstainCount">
          <el-input v-model="form.abstainCount" placeholder="请输入弃权票数" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
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
import { listTopic, getTopic, delTopic, addTopic, updateTopic } from "@/api/system/topic"

export default {
  name: "Topic",
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
      // 会议议题表格数据
      topicList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        meetingId: null,
        topicTitle: null,
        topicContent: null,
        files: null,
        agreeCount: null,
        opposeCount: null,
        abstainCount: null,
        orderNum: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        meetingId: [
          { required: true, message: "所属会议ID不能为空", trigger: "blur" }
        ],
        topicTitle: [
          { required: true, message: "议题标题不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询会议议题列表 */
    getList() {
      this.loading = true
      listTopic(this.queryParams).then(response => {
        this.topicList = response.rows
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
        topicId: null,
        meetingId: null,
        topicTitle: null,
        topicContent: null,
        files: null,
        agreeCount: null,
        opposeCount: null,
        abstainCount: null,
        orderNum: null,
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
      this.ids = selection.map(item => item.topicId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加会议议题"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const topicId = row.topicId || this.ids
      getTopic(topicId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改会议议题"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.topicId != null) {
            updateTopic(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTopic(this.form).then(response => {
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
      const topicIds = row.topicId || this.ids
      this.$modal.confirm('是否确认删除会议议题编号为"' + topicIds + '"的数据项？').then(function() {
        return delTopic(topicIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/topic/export', {
        ...this.queryParams
      }, `topic_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
