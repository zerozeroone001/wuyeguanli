<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="投诉编号" prop="complaintNo">
        <el-input
          v-model="queryParams.complaintNo"
          placeholder="请输入投诉编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投诉人ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入投诉人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投诉标题" prop="complaintTitle">
        <el-input
          v-model="queryParams.complaintTitle"
          placeholder="请输入投诉标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="contactPhone">
        <el-input
          v-model="queryParams.contactPhone"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="处理时间" prop="handleTime">
        <el-date-picker
          v-model="queryParams.handleTime"
          clearable
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择处理时间"
        />
      </el-form-item>
      <el-form-item label="完成时间" prop="completeTime">
        <el-date-picker
          v-model="queryParams.completeTime"
          clearable
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择完成时间"
        />
      </el-form-item>
      <el-form-item label="满意度(1-5分)" prop="satisfaction">
        <el-input
          v-model="queryParams.satisfaction"
          placeholder="请输入满意度(1-5分)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb8">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>总投诉数</span>
          </div>
          <div class="text item">
            <span class="number">{{ stats.total || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>待处理</span>
          </div>
          <div class="text item">
            <span class="number warning">{{ stats.pending || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>处理中</span>
          </div>
          <div class="text item">
            <span class="number primary">{{ stats.processing || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>已完成</span>
          </div>
          <div class="text item">
            <span class="number success">{{ stats.completed || 0 }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:complaint:add']"
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
          v-hasPermi="['system:complaint:edit']"
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
          v-hasPermi="['system:complaint:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:complaint:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="complaintList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="投诉ID" align="center" prop="complaintId" />
      <el-table-column label="投诉编号" align="center" prop="complaintNo">
        <template slot-scope="scope">
          <span>{{ scope.row.complaintNo || '未生成' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属小区" align="center" prop="communityName" />
      <el-table-column label="投诉人ID" align="center" prop="userId" />
      <el-table-column label="投诉类型" align="center" prop="complaintType">
        <template slot-scope="scope">
          <span>{{ formatComplaintType(scope.row.complaintType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="投诉标题" align="center" prop="complaintTitle" show-overflow-tooltip />
      <el-table-column label="投诉内容" align="center" prop="complaintContent" show-overflow-tooltip />
      <el-table-column label="联系电话" align="center" prop="contactPhone" />
      <el-table-column label="紧急程度" align="center" prop="urgency">
        <template slot-scope="scope">
          <el-tag :type="formatUrgency(scope.row.urgency).type" size="small">
            {{ formatUrgency(scope.row.urgency).text }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="formatStatus(scope.row.status).type" size="small">
            {{ formatStatus(scope.row.status).text }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="处理人ID" align="center" prop="handlerId" />
      <el-table-column label="处理时间" align="center" prop="handleTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.handleTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="completeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="满意度(1-5分)" align="center" prop="satisfaction" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:complaint:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-user"
            @click="handleAssign(scope.row)"
            v-hasPermi="['system:complaint:assign']"
            v-if="scope.row.status === '0'"
          >指派</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleComplete(scope.row)"
            v-hasPermi="['system:complaint:complete']"
            v-if="scope.row.status === '1'"
          >完成</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleClose(scope.row)"
            v-hasPermi="['system:complaint:close']"
            v-if="scope.row.status !== '3'"
          >关闭</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:complaint:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增 / 编辑投诉管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="投诉编号" prop="complaintNo">
          <el-input
            v-model="form.complaintNo"
            :placeholder="form.complaintId ? '投诉编号' : '系统自动生成'"
            :disabled="!form.complaintId"
            readonly
          />
        </el-form-item>
        <el-form-item label="投诉人ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入投诉人ID" />
        </el-form-item>
        <el-form-item label="所属小区" prop="communityId">
          <el-input v-model="form.communityName" placeholder="请先在右上角选择小区" disabled />
          <input type="hidden" v-model="form.communityId" />
        </el-form-item>
        <el-form-item label="投诉标题" prop="complaintTitle">
          <el-input v-model="form.complaintTitle" placeholder="请输入投诉标题" />
        </el-form-item>
        <el-form-item label="投诉内容" prop="complaintContent">
          <editor v-model="form.complaintContent" :min-height="192" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="紧急程度" prop="urgency">
          <el-input v-model="form.urgency" placeholder="请输入紧急程度(1-紧急,2-较紧急,3-一般)" />
        </el-form-item>
        <el-form-item label="期望处理时间" prop="expectedTime">
          <el-input
            v-model="form.expectedTime"
            placeholder="请输入期望处理时间(1-立即,2-3天内,3-1周内,4-1月内)"
          />
        </el-form-item>
        <el-form-item label="处理人ID" prop="handlerId">
          <el-input v-model="form.handlerId" placeholder="请输入处理人ID" />
        </el-form-item>
        <el-form-item label="处理时间" prop="handleTime">
          <el-date-picker
            v-model="form.handleTime"
            clearable
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择处理时间"
          />
        </el-form-item>
        <el-form-item label="完成时间" prop="completeTime">
          <el-date-picker
            v-model="form.completeTime"
            clearable
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择完成时间"
          />
        </el-form-item>
        <el-form-item label="满意度" prop="satisfaction">
          <el-input v-model="form.satisfaction" placeholder="请输入满意度(1-5分)" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>

    <!-- 指派处理人对话框 -->
    <el-dialog title="指派处理人" :visible.sync="assignDialog" width="400px" append-to-body>
      <el-form ref="assignForm" :model="assignForm" label-width="90px">
        <el-form-item label="处理人ID" required>
          <el-input v-model="assignForm.handlerId" placeholder="请输入处理人ID" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmAssign">确定</el-button>
        <el-button @click="assignDialog = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 完成处理对话框 -->
    <el-dialog title="完成处理" :visible.sync="completeDialog" width="400px" append-to-body>
      <el-form ref="completeForm" :model="completeForm" label-width="90px">
        <el-form-item label="满意度">
          <el-select v-model="completeForm.satisfaction" placeholder="请选择满意度">
            <el-option label="1分" value="1"></el-option>
            <el-option label="2分" value="2"></el-option>
            <el-option label="3分" value="3"></el-option>
            <el-option label="4分" value="4"></el-option>
            <el-option label="5分" value="5"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmComplete">确定</el-button>
        <el-button @click="completeDialog = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listComplaint,
  getComplaint,
  delComplaint,
  addComplaint,
  updateComplaint,
  getComplaintStats,
  assignComplaint,
  completeComplaint,
  closeComplaint
} from "@/api/system/complaint"
import { mapGetters } from "vuex"

export default {
  name: "Complaint",
  data() {
    return {
      // 表格加载遮罩
      loading: true,
      // 当前选中的投诉主键集合
      ids: [],
      // 需要单选时的按钮禁用态
      single: true,
      // 需要多选时的按钮禁用态
      multiple: true,
      // 是否显示高级搜索
      showSearch: true,
      // 列表总条数
      total: 0,
      // 投诉管理列表数据
      complaintList: [],
      // 弹窗标题
      title: "",
      // 弹窗显隐
      open: false,
      // 查询参数，需要和后端分页接口保持一致
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        complaintNo: null,
        userId: null,
        complaintType: null,
        complaintTitle: null,
        complaintContent: null,
        contactPhone: null,
        urgency: null,
        expectedTime: null,
        status: null,
        handlerId: null,
        handleTime: null,
        completeTime: null,
        satisfaction: null,
        communityId: null
      },
      // 表单数据模型
      form: {},
      // 表单校验规则
      rules: {
        userId: [
          { required: true, message: "投诉人ID不能为空", trigger: "blur" }
        ],
        complaintType: [
          { required: true, message: "投诉类型不能为空", trigger: "change" }
        ],
        complaintTitle: [
          { required: true, message: "投诉标题不能为空", trigger: "blur" }
        ],
        complaintContent: [
          { required: true, message: "投诉内容不能为空", trigger: "blur" }
        ]
      },
      // 首页统计数据
      stats: {},
      // 指派处理人对话框显隐
      assignDialog: false,
      // 指派处理人表单
      assignForm: {
        complaintId: null,
        handlerId: null
      },
      // 完成处理对话框显隐
      completeDialog: false,
      // 完成处理表单
      completeForm: {
        complaintId: null,
        satisfaction: null
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
     * 顶部选择“全部小区”时返回 true
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
    this.getList()
    this.loadStats()
  },
  methods: {
    /**
     * 根据顶部的小区选择同步查询条件
     */
    applyCommunityFilter() {
      if (this.isAllCommunitySelected) {
        this.queryParams.communityId = null
      } else {
        this.queryParams.communityId = Number(this.currentCommunityId)
      }
    },
    /**
     * 构建请求参数，确保带上社区维度
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
     * 根据社区 ID 返回对应名称（优先读取缓存的下拉数据）
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
     * 给表单/查询对象补齐社区信息
     */
    attachCommunityInfo(target, communityId) {
      const numericId =
        communityId === null || communityId === undefined ? null : Number(communityId)
      target.communityId = numericId
      target.communityName = this.resolveCommunityName(numericId)
    },
    /** 查询投诉管理列表 */
    getList() {
      this.loading = true
      const params = this.buildQueryParams()
      listComplaint(params).then(response => {
        this.complaintList = Array.isArray(response.rows)
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
        complaintId: null,
        complaintNo: null,
        userId: null,
        complaintType: null,
        complaintTitle: null,
        complaintContent: null,
        contactPhone: null,
        urgency: null,
        expectedTime: null,
        status: null,
        handlerId: null,
        handleTime: null,
        completeTime: null,
        satisfaction: null,
        communityId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
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
    // 多选变化事件
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.complaintId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    /** 新增按钮操作 */
    handleAdd() {
      if (this.isAllCommunitySelected) {
        this.$message.warning("请先在右上角选择具体小区，再新增投诉。")
        return
      }
      this.reset()
      this.attachCommunityInfo(this.form, this.currentCommunityId)
      this.open = true
      this.title = "新增投诉管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const complaintId = row.complaintId || this.ids
      getComplaint(complaintId).then(response => {
        this.form = response.data
        this.attachCommunityInfo(this.form, response.data.communityId)
        this.open = true
        this.title = "修改投诉管理"
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
            this.$message.error("请先为投诉记录指定所属小区。")
            return
          }
          this.form.communityId = numericId
          if (this.form.complaintId != null) {
            updateComplaint(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addComplaint(this.form).then(() => {
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
      const complaintIds = row.complaintId || this.ids
      this.$modal.confirm(`是否确认删除投诉管理编号为"${complaintIds}"的数据项？`).then(function() {
        return delComplaint(complaintIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      const params = this.buildQueryParams()
      this.download("system/complaint/export", params, `complaint_${new Date().getTime()}.xlsx`)
    },
    /** 加载统计数据 */
    loadStats() {
      getComplaintStats().then(response => {
        this.stats = response.data
      })
    },
    /** 指派处理人 */
    handleAssign(row) {
      this.assignForm.complaintId = row.complaintId
      this.assignDialog = true
    },
    /** 确认指派 */
    confirmAssign() {
      assignComplaint(this.assignForm).then(() => {
        this.$modal.msgSuccess("指派成功")
        this.assignDialog = false
        this.getList()
        this.loadStats()
      })
    },
    /** 完成处理 */
    handleComplete(row) {
      this.completeForm.complaintId = row.complaintId
      this.completeDialog = true
    },
    /** 确认完成 */
    confirmComplete() {
      completeComplaint(this.completeForm).then(() => {
        this.$modal.msgSuccess("处理完成")
        this.completeDialog = false
        this.getList()
        this.loadStats()
      })
    },
    /** 关闭投诉 */
    handleClose(row) {
      this.$modal.confirm(`是否确认关闭投诉编号"${row.complaintNo}"？`).then(function() {
        return closeComplaint({ complaintId: row.complaintId })
      }).then(() => {
        this.getList()
        this.loadStats()
        this.$modal.msgSuccess("关闭成功")
      }).catch(() => {})
    },
    /** 格式化状态标签 */
    formatStatus(status) {
      const statusMap = {
        "0": { text: "待处理", type: "warning" },
        "1": { text: "处理中", type: "primary" },
        "2": { text: "已完成", type: "success" },
        "3": { text: "已关闭", type: "info" }
      }
      return statusMap[status] || { text: "未知", type: "info" }
    },
    /** 格式化紧急程度 */
    formatUrgency(urgency) {
      const urgencyMap = {
        "1": { text: "紧急", type: "danger" },
        "2": { text: "较紧急", type: "warning" },
        "3": { text: "一般", type: "info" }
      }
      return urgencyMap[urgency] || { text: "未知", type: "info" }
    },
    /** 格式化投诉类型 */
    formatComplaintType(type) {
      const typeMap = {
        "1": "物业服务",
        "2": "设施设备",
        "3": "环境卫生",
        "4": "安全管理",
        "5": "收费问题",
        "6": "其他"
      }
      return typeMap[type] || "未知"
    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}

.number {
  font-size: 24px;
  font-weight: bold;
}

.number.warning {
  color: #E6A23C;
}

.number.primary {
  color: #409EFF;
}

.number.success {
  color: #67C23A;
}

.text {
  text-align: center;
  padding: 20px 0;
}
</style>
