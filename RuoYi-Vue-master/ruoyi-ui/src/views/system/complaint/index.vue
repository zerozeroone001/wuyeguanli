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
        <el-date-picker clearable
          v-model="queryParams.handleTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择处理时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="完成时间" prop="completeTime">
        <el-date-picker clearable
          v-model="queryParams.completeTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择完成时间">
        </el-date-picker>
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
      <el-table-column label="投诉编号" align="center" prop="complaintNo" />
      <el-table-column label="投诉人ID" align="center" prop="userId" />
      <el-table-column label="投诉类型" align="center" prop="complaintType" />
      <el-table-column label="投诉标题" align="center" prop="complaintTitle" />
      <el-table-column label="投诉内容" align="center" prop="complaintContent" />
      <el-table-column label="联系电话" align="center" prop="contactPhone" />
      <el-table-column label="状态" align="center" prop="status" />
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:complaint:remove']"
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

    <!-- 添加或修改投诉管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="投诉编号" prop="complaintNo">
          <el-input v-model="form.complaintNo" placeholder="请输入投诉编号" />
        </el-form-item>
        <el-form-item label="投诉人ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入投诉人ID" />
        </el-form-item>
        <el-form-item label="投诉标题" prop="complaintTitle">
          <el-input v-model="form.complaintTitle" placeholder="请输入投诉标题" />
        </el-form-item>
        <el-form-item label="投诉内容">
          <editor v-model="form.complaintContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="紧急程度(1-紧急,2-普通,3-一般)" prop="urgency">
          <el-input v-model="form.urgency" placeholder="请输入紧急程度(1-紧急,2-普通,3-一般)" />
        </el-form-item>
        <el-form-item label="期望处理时间(1-立即,2-3天内,3-1周内,4-1月内)" prop="expectedTime">
          <el-input v-model="form.expectedTime" placeholder="请输入期望处理时间(1-立即,2-3天内,3-1周内,4-1月内)" />
        </el-form-item>
        <el-form-item label="处理人ID" prop="handlerId">
          <el-input v-model="form.handlerId" placeholder="请输入处理人ID" />
        </el-form-item>
        <el-form-item label="处理时间" prop="handleTime">
          <el-date-picker clearable
            v-model="form.handleTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择处理时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="完成时间" prop="completeTime">
          <el-date-picker clearable
            v-model="form.completeTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择完成时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="满意度(1-5分)" prop="satisfaction">
          <el-input v-model="form.satisfaction" placeholder="请输入满意度(1-5分)" />
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
import { listComplaint, getComplaint, delComplaint, addComplaint, updateComplaint } from "@/api/system/complaint"

export default {
  name: "Complaint",
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
      // 投诉管理表格数据
      complaintList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
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
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        complaintNo: [
          { required: true, message: "投诉编号不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "投诉人ID不能为空", trigger: "blur" }
        ],
        complaintType: [
          { required: true, message: "投诉类型(1-物业服务,2-设施设备,3-环境卫生,4-安全管理,5-收费争议,6-其他)不能为空", trigger: "change" }
        ],
        complaintTitle: [
          { required: true, message: "投诉标题不能为空", trigger: "blur" }
        ],
        complaintContent: [
          { required: true, message: "投诉内容不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询投诉管理列表 */
    getList() {
      this.loading = true
      listComplaint(this.queryParams).then(response => {
        this.complaintList = response.rows
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
      this.ids = selection.map(item => item.complaintId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加投诉管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const complaintId = row.complaintId || this.ids
      getComplaint(complaintId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改投诉管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.complaintId != null) {
            updateComplaint(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addComplaint(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除投诉管理编号为"' + complaintIds + '"的数据项？').then(function() {
        return delComplaint(complaintIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/complaint/export', {
        ...this.queryParams
      }, `complaint_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
