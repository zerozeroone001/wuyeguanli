<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="计划名称" prop="planName">
        <el-input
          v-model="queryParams.planName"
          placeholder="请输入计划名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检测类型" prop="inspectionType">
        <el-input
          v-model="queryParams.inspectionType"
          placeholder="请输入检测类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="监督对象" prop="supervisionTarget">
        <el-input
          v-model="queryParams.supervisionTarget"
          placeholder="请输入监督对象"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="未开始" value="pending" />
          <el-option label="进行中" value="in_progress" />
          <el-option label="已完成" value="completed" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 按钮工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:inspectionPlan:add']"
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
          v-hasPermi="['system:inspectionPlan:edit']"
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
          v-hasPermi="['system:inspectionPlan:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="计划ID" align="center" prop="planId" />
      <el-table-column label="计划名称" align="center" prop="planName" />
      <el-table-column label="检测类型" align="center" prop="inspectionType" />
      <el-table-column label="监督对象" align="center" prop="supervisionTarget" />
      <el-table-column label="开始日期" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="inspector" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'pending' ? 'info' : scope.row.status === 'in_progress' ? 'primary' : 'success'">{{ scope.row.status === 'pending' ? '未开始' : scope.row.status === 'in_progress' ? '进行中' : '已完成' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:inspectionPlan:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:inspectionPlan:remove']"
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

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="form.planName" placeholder="请输入计划名称" />
        </el-form-item>
        <el-form-item label="检测类型" prop="inspectionType">
          <el-input v-model="form.inspectionType" placeholder="请输入检测类型" />
        </el-form-item>
        <el-form-item label="监督对象" prop="supervisionTarget">
          <el-input v-model="form.supervisionTarget" placeholder="请输入监督对象" />
        </el-form-item>
        <el-form-item label="计划周期">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
            ></el-date-picker>
        </el-form-item>
        <el-form-item label="负责人" prop="inspector">
          <el-input v-model="form.inspector" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="监督说明" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入监督说明" />
        </el-form-item>
        <el-form-item label="计划状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="pending">未开始</el-radio>
            <el-radio label="in_progress">进行中</el-radio>
            <el-radio label="completed">已完成</el-radio>
          </el-radio-group>
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
import { listPlan, getPlan, delPlan, addPlan, updatePlan } from "@/api/system/inspectionPlan";

export default {
  name: "InspectionPlan",
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
      // 检测计划表格数据
      planList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planName: null,
        inspectionType: null,
        supervisionTarget: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        planName: [
          { required: true, message: "计划名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询检测计划列表 */
    getList() {
      this.loading = true;
      listPlan(this.queryParams).then(response => {
        this.planList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        planId: null,
        planName: null,
        inspectionType: null,
        supervisionTarget: null,
        description: null,
        startDate: null,
        endDate: null,
        inspector: null,
        status: "pending",
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.dateRange = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.planId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加检测计划";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const planId = row.planId || this.ids[0]
      getPlan(planId).then(response => {
        this.form = response.data;
        this.dateRange = [this.form.startDate, this.form.endDate];
        this.open = true;
        this.title = "修改检测计划";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.startDate = this.dateRange[0];
          this.form.endDate = this.dateRange[1];
          if (this.form.planId != null) {
            updatePlan(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPlan(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const planIds = row.planId || this.ids;
      this.$modal.confirm('是否确认删除检测计划编号为"' + planIds + '"的数据项？').then(function() {
        return delPlan(planIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
