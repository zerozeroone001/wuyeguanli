<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="关联计划" prop="planId">
        <el-select v-model="queryParams.planId" placeholder="请选择关联计划" clearable>
          <el-option
            v-for="plan in planOptions"
            :key="plan.planId"
            :label="plan.planName"
            :value="plan.planId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="检测地点" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入检测地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="记录状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="合格" value="pass" />
          <el-option label="不合格" value="fail" />
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
          v-hasPermi="['system:inspectionRecord:add']"
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
          v-hasPermi="['system:inspectionRecord:edit']"
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
          v-hasPermi="['system:inspectionRecord:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录ID" align="center" prop="recordId" />
      <el-table-column label="关联计划ID" align="center" prop="planId" />
      <el-table-column label="检测地点" align="center" prop="location" />
      <el-table-column label="实际检测日期" align="center" prop="inspectionDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.inspectionDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'pass' ? 'success' : 'danger'">{{ scope.row.status === 'pass' ? '合格' : '不合格' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:inspectionRecord:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:inspectionRecord:remove']"
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
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="关联计划" prop="planId">
              <el-select v-model="form.planId" placeholder="请选择关联计划">
                <el-option
                  v-for="plan in planOptions"
                  :key="plan.planId"
                  :label="plan.planName"
                  :value="plan.planId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测地点" prop="location">
              <el-input v-model="form.location" placeholder="请输入检测地点" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="实际检测日期" prop="inspectionDate">
              <el-date-picker clearable
                v-model="form.inspectionDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="center">检查项列表</el-divider>
        <el-table :data="form.checklist" style="width: 100%">
            <el-table-column label="项目名称">
                <template slot-scope="scope">
                    <el-input v-model="scope.row.itemName" placeholder="请输入项目名称" />
                </template>
            </el-table-column>
            <el-table-column label="评价标准">
                <template slot-scope="scope">
                    <el-input v-model="scope.row.standard" placeholder="请输入评价标准" />
                </template>
            </el-table-column>
            <el-table-column label="检测方法">
                <template slot-scope="scope">
                    <el-input v-model="scope.row.method" placeholder="请输入检测方法" />
                </template>
            </el-table-column>
            <el-table-column label="检测结果">
                <template slot-scope="scope">
                    <el-input v-model="scope.row.result" placeholder="请输入检测结果" />
                </template>
            </el-table-column>
            <el-table-column label="是否合格" width="80">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.isPass"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="备注">
                <template slot-scope="scope">
                    <el-input v-model="scope.row.notes" placeholder="请输入备注" />
                </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteChecklistItem(scope.$index)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button style="width: 100%; margin-top: 10px;" icon="el-icon-plus" @click="handleAddChecklistItem">添加检查项</el-button>
        <el-divider></el-divider>

        <el-form-item label="监督总结" prop="conclusion">
          <el-input v-model="form.conclusion" type="textarea" placeholder="请输入监督总结" />
        </el-form-item>
        <el-form-item label="监督报告内容" prop="reportContent">
          <el-input v-model="form.reportContent" type="textarea" placeholder="请输入监督报告内容" />
        </el-form-item>
        <el-form-item label="附件列表" prop="attachments">
          <file-upload v-model="form.attachments"/>
        </el-form-item>
        <el-form-item label="整改通知" prop="rectificationNotice">
          <el-input v-model="form.rectificationNotice" type="textarea" placeholder="请输入通知内容" />
        </el-form-item>
        <el-form-item label="记录状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="pass">合格</el-radio>
            <el-radio label="fail">不合格</el-radio>
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
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/system/inspectionRecord";
import { listPlan } from "@/api/system/inspectionPlan";
import FileUpload from '@/components/FileUpload';

export default {
  name: "InspectionRecord",
  components: { FileUpload },
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
      // 检测记录表格数据
      recordList: [],
      // 检测计划选项
      planOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planId: null,
        location: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        planId: [
          { required: true, message: "关联计划不能为空", trigger: "change" }
        ],
        location: [
          { required: true, message: "检测地点不能为空", trigger: "blur" }
        ],
        inspectionDate: [
          { required: true, message: "实际检测日期不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getPlanOptions();
  },
  methods: {
    /** 查询检测记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询所有检测计划 */
    getPlanOptions(){
        listPlan().then(response => {
            this.planOptions = response.rows;
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
        recordId: null,
        planId: null,
        location: null,
        inspectionDate: null,
        checklist: [], // 用于UI的统一数据模型
        items: null, // 最终提交给后端的JSON字符串
        results: null, // 最终提交给后端的JSON字符串
        conclusion: null,
        attachments: null,
        reportContent: null,
        rectificationNotice: null,
        status: "pass",
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
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
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加检测记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const recordId = row.recordId || this.ids[0]
      getRecord(recordId).then(response => {
        const data = response.data;
        // 将后端的items和results合并为前端的checklist
        let items = [];
        let results = [];
        try {
          items = JSON.parse(data.items) || [];
          results = JSON.parse(data.results) || [];
        } catch (e) {
          console.error("Parsing items/results JSON failed", e);
        }
        
        const checklist = items.map((item, index) => {
          const result = results[index] || {};
          return {
            itemName: item.item_name,
            standard: item.standard,
            method: item.method,
            result: result.result,
            isPass: result.is_pass,
            notes: result.notes
          };
        });

        data.checklist = checklist;
        this.form = data;
        this.open = true;
        this.title = "修改检测记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将前端的checklist拆分为后端的items和results
          const items = this.form.checklist.map(c => ({
            item_name: c.itemName,
            standard: c.standard,
            method: c.method
          }));
          const results = this.form.checklist.map(c => ({
            result: c.result,
            is_pass: c.isPass,
            notes: c.notes
          }));

          this.form.items = JSON.stringify(items);
          this.form.results = JSON.stringify(results);

          if (this.form.recordId != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecord(this.form).then(response => {
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
      const recordIds = row.recordId || this.ids;
      this.$modal.confirm('是否确认删除检测记录编号为"' + recordIds + '"的数据项？').then(function() {
        return delRecord(recordIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 添加检查项按钮操作 */
    handleAddChecklistItem() {
      this.form.checklist.push({
        itemName: '',
        standard: '',
        method: '',
        result: '',
        isPass: true,
        notes: ''
      });
    },
    /** 删除检查项按钮操作 */
    handleDeleteChecklistItem(index) {
      this.form.checklist.splice(index, 1);
    }
  }
};
</script>
