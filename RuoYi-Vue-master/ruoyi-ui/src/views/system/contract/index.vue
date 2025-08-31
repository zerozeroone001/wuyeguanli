<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="合同名称" prop="contractName">
        <el-input
          v-model="queryParams.contractName"
          placeholder="请输入合同名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="甲方" prop="partyA">
        <el-input
          v-model="queryParams.partyA"
          placeholder="请输入甲方"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="乙方" prop="partyB">
        <el-input
          v-model="queryParams.partyB"
          placeholder="请输入乙方"
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
          v-hasPermi="['system:contract:add']"
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
          v-hasPermi="['system:contract:edit']"
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
          v-hasPermi="['system:contract:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:contract:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="contractList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同ID" align="center" prop="contractId" />
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="合同名称" align="center" prop="contractName" />
      <el-table-column label="合同金额" align="center" prop="contractAmount" />
      <el-table-column label="负责人" align="center" prop="managerName" />
      <el-table-column label="联系电话" align="center" prop="managerPhone" />
      <el-table-column label="甲方" align="center" prop="partyA" />
      <el-table-column label="乙方" align="center" prop="partyB" />
      <el-table-column label="合同版本" align="center" prop="contractVersion" />
      <el-table-column label="生效日期" align="center" prop="effectiveDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.effectiveDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="失效日期" align="center" prop="expiryDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:contract:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:contract:remove']"
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

    <!-- 添加或修改物业服务合同对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同版本" prop="contractVersion">
              <el-input v-model="form.contractVersion" placeholder="请输入合同版本" />
            </el-form-item>
          </el-col>
        </el-row>
                <el-row>
          <el-col :span="12">
            <el-form-item label="甲方" prop="partyA">
              <el-input v-model="form.partyA" placeholder="请输入甲方" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="乙方" prop="partyB">
              <el-input v-model="form.partyB" placeholder="请输入乙方" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="负责人" prop="managerName">
              <el-input v-model="form.managerName" placeholder="请输入负责人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="managerPhone">
              <el-input v-model="form.managerPhone" placeholder="请输入负责人电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="合同金额" prop="contractAmount">
              <el-input-number v-model="form.contractAmount" placeholder="请输入合同金额" :precision="2" :step="100" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker clearable
                v-model="form.effectiveDate"
                style="width: 100%"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择生效日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker clearable
                v-model="form.expiryDate"
                style="width: 100%"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择失效日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="合同内容" prop="contractContent">
          <editor v-model="form.contractContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="重要条款">
          <div v-for="(item, index) in form.importantClausesList" :key="index" class="clause-item">
            <el-input v-model="item.title" placeholder="条款标题" class="clause-title"></el-input>
            <el-input v-model="item.content" type="textarea" placeholder="条款内容" class="clause-content"></el-input>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeClause(index)" class="clause-delete-btn"></el-button>
          </div>
          <el-button type="primary" icon="el-icon-plus" size="small" @click="addClause">添加条款</el-button>
        </el-form-item>
        <el-form-item label="合同附件" prop="fileUrl">
          <file-upload v-model="form.fileUrl"/>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
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
import { listContract, getContract, delContract, addContract, updateContract } from "@/api/system/contract";
import FileUpload from '@/components/FileUpload';

export default {
  name: "Contract",
  components: {
    FileUpload
  },
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
      // 物业服务合同表格数据
      contractList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractName: null,
        contractVersion: null,
        partyA: null,
        partyB: null,
        fileUrl: null,
        effectiveDate: null,
        expiryDate: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        contractName: [
          { required: true, message: "合同名称不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList();
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询物业服务合同列表 */
    getList() {
      this.loading = true
      listContract(this.queryParams).then(response => {
        this.contractList = response.rows
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
        contractId: null,
        contractNo: null,
        contractName: null,
        contractVersion: null,
        contractAmount: 0.00,
        partyA: null,
        partyB: null,
        managerName: null,
        managerPhone: null,
        contractContent: null,
        importantClauses: null,
        fileUrl: null,
        effectiveDate: null,
        expiryDate: null,
        status: "0",
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
      this.ids = selection.map(item => item.contractId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加物业服务合同"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const contractId = row.contractId || this.ids
      getContract(contractId).then(response => {
        const formData = response.data;
        // 安全地处理 importantClauses
        try {
          if (formData.importantClauses && typeof formData.importantClauses === 'string') {
            formData.importantClausesList = JSON.parse(formData.importantClauses);
          } else {
            formData.importantClausesList = [];
          }
        } catch (e) {
          formData.importantClausesList = [];
          console.error("重要条款JSON解析失败:", e);
        }
        this.form = formData;

        this.open = true
        this.title = "修改物业服务合同"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 使用 $set 确保响应式更新
          this.$set(this.form, 'importantClauses', JSON.stringify(this.form.importantClausesList));

          // 【调试日志】打印即将提交到后端的数据
          console.log("即将提交到后端的数据:", JSON.parse(JSON.stringify(this.form)));

          if (this.form.contractId != null) {
            updateContract(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addContract(this.form).then(response => {
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
      const contractIds = row.contractId || this.ids
      this.$modal.confirm('是否确认删除物业服务合同编号为"' + contractIds + '"的数据项？').then(function() {
        return delContract(contractIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 新增条款 */
    addClause() {
      // 防御性检查，确保 importantClausesList 是一个数组
      if (!Array.isArray(this.form.importantClausesList)) {
        this.$set(this.form, 'importantClausesList', []);
      }
      this.form.importantClausesList.push({ title: '', content: '' });
    },
    /** 删除条款 */
    removeClause(index) {
      this.form.importantClausesList.splice(index, 1);
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/contract/export', {
        ...this.queryParams
      }, `contract_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
