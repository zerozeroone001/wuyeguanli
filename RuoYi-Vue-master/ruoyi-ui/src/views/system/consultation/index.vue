<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="咨询人用户" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入咨询人用户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="咨询标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入咨询标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="回复时间" prop="replyTime">
        <el-date-picker clearable
          v-model="queryParams.replyTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择回复时间">
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
          v-hasPermi="['system:consultation:add']"
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
          v-hasPermi="['system:consultation:edit']"
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
          v-hasPermi="['system:consultation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:consultation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="consultationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="咨询ID" align="center" prop="consultationId" />
      <el-table-column label="咨询人用户" align="center" prop="userId" />
      <el-table-column label="所属小区" align="center" prop="communityName" />
      <el-table-column label="咨询标题" align="center" prop="title" />
      <el-table-column label="咨询内容" align="center" prop="content" />

      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:consultation:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:consultation:remove']"
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

    <!-- 添加或修改法律咨询对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="咨询人用户ID (关联sys_user)" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入咨询人用户ID (关联sys_user)" />
        </el-form-item>
        <el-form-item label="所属小区" prop="communityId">
          <el-input v-model="form.communityName" placeholder="请先在右上角选择小区" disabled />
          <input type="hidden" v-model="form.communityId" />
        </el-form-item>
        <el-form-item label="咨询标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入咨询标题" />
        </el-form-item>
        <el-form-item label="咨询内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="关联的自定义表单数据ID" prop="formDataId">
          <el-input v-model="form.formDataId" placeholder="请输入关联的自定义表单数据ID" />
        </el-form-item>
        <el-form-item label="回复内容">
          <editor v-model="form.replyContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="回复时间" prop="replyTime">
          <el-date-picker clearable
            v-model="form.replyTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择回复时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="指派的法律专业人员" prop="assignee">
          <el-input v-model="form.assignee" placeholder="请输入指派的法律专业人员" />
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
import { listConsultation, getConsultation, delConsultation, addConsultation, updateConsultation } from "@/api/system/consultation"
import { mapGetters } from "vuex"

export default {
  name: "Consultation",
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
      // 法律咨询表格数据
      consultationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        title: null,
        content: null,
        formDataId: null,
        status: null,
        replyContent: null,
        replyTime: null,
        assignee: null,
        communityId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "咨询人用户ID (关联sys_user)不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "咨询标题不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "咨询内容不能为空", trigger: "blur" }
        ],
      }
    }
  },
  computed: {
    ...mapGetters([
      "currentCommunityId",
      "currentCommunityName",
      "communityOptions"
    ]),
    /**
     * 顶部选择为“全部小区”时返回 true。
     */
    isAllCommunitySelected() {
      const value = this.currentCommunityId
      if (value === null || value === undefined) {
        return true
      }
      return Number(value) === 0
    }
  },
  created() {
    this.applyCommunityFilter()
    this.getList()
  },
  methods: {
    /**
     * 根据顶部选择的小区同步查询条件。
     */
    applyCommunityFilter() {
      if (this.isAllCommunitySelected) {
        this.queryParams.communityId = null
      } else {
        this.queryParams.communityId = Number(this.currentCommunityId)
      }
    },
    /**
     * 构建请求参数，必要时携带小区 ID。
     */
    buildQueryParams() {
      const params = { ...this.queryParams }
      if (this.isAllCommunitySelected) {
        delete params.communityId
      } else {
        const numericId = Number(this.currentCommunityId)
        this.queryParams.communityId = numericId
        params.communityId = numericId
      }
      return params
    },
    /**
     * 根据小区 ID 返回对应的小区名称。
     */
    resolveCommunityName(communityId) {
      if (communityId === null || communityId === undefined) {
        return ""
      }
      const numericId = Number(communityId)
      const options = Array.isArray(this.communityOptions) ? this.communityOptions : []
      const target = options.find(item => Number(item.id) === numericId)
      if (target && target.name) {
        return target.name
      }
      if (
        this.currentCommunityId !== null &&
        this.currentCommunityId !== undefined &&
        Number(this.currentCommunityId) === numericId
      ) {
        return this.currentCommunityName || ""
      }
      return ""
    },
    /**
     * 为表单对象补全小区信息。
     */
    attachCommunityInfo(target, communityId) {
      const numericId =
        communityId === null || communityId === undefined ? null : Number(communityId)
      target.communityId = numericId
      target.communityName = this.resolveCommunityName(numericId)
    },
    /** 查询法律咨询列表 */
    getList() {
      this.loading = true
      const params = this.buildQueryParams()
      listConsultation(params).then(response => {
        this.consultationList = Array.isArray(response.rows)
          ? response.rows.map(item => ({
              ...item,
              communityName: this.resolveCommunityName(item.communityId)
            }))
          : []
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
        consultationId: null,
        userId: null,
        title: null,
        content: null,
        formDataId: null,
        status: null,
        replyContent: null,
        replyTime: null,
        assignee: null,
        communityId: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        communityId: null,
        communityName: ""
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.applyCommunityFilter()
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.applyCommunityFilter()
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.consultationId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      if (this.isAllCommunitySelected) {
        this.$message.warning("请先在右上角选择具体小区，再新增咨询。")
        return
      }
      this.reset()
      this.attachCommunityInfo(this.form, this.currentCommunityId)
      this.open = true
      this.title = "添加法律咨询"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const consultationId = row.consultationId || this.ids
      getConsultation(consultationId).then(response => {
        this.form = response.data
        this.attachCommunityInfo(this.form, response.data.communityId)
        this.open = true
        this.title = "修改法律咨询"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.communityId === null || this.form.communityId === undefined) {
            if (!this.isAllCommunitySelected) {
              this.form.communityId = Number(this.currentCommunityId)
              this.form.communityName = this.resolveCommunityName(this.form.communityId)
            }
          }
          const numericId = Number(this.form.communityId)
          if (Number.isNaN(numericId) || numericId === 0) {
            this.$message.error("请先为咨询记录指定所属小区。")
            return
          }
          this.form.communityId = numericId
          if (this.form.consultationId != null) {
            updateConsultation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addConsultation(this.form).then(response => {
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
      const consultationIds = row.consultationId || this.ids
      this.$modal.confirm('是否确认删除法律咨询编号为"' + consultationIds + '"的数据项？').then(function() {
        return delConsultation(consultationIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      const params = this.buildQueryParams()
      this.download('system/consultation/export', params, `consultation_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
