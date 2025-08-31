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
      <el-table-column label="生效日期" align="center" prop="effectiveDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.effectiveDate, '{y}-{m}-{d}') }}</span>
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
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="合同编号" prop="contractNo" v-if="form.contractId">
          <el-input v-model="form.contractNo" placeholder="系统自动生成" :disabled="true" />
        </el-form-item>
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
        <el-form-item label="合同金额" prop="contractAmount">
          <el-input-number v-model="form.contractAmount" placeholder="请输入合同金额" :precision="2" :step="100" style="width:100%"></el-input-number>
        </el-form-item>
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
        <el-form-item label="相关文档">
          <el-upload
            ref="upload"
            :action="upload.url"
            :headers="upload.headers"
            :file-list="upload.fileList"
            :on-success="handleUploadSuccess"
            :on-remove="handleFileRemove"
            multiple
            :limit="9"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
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
import { getToken } from "@/utils/auth";

export default {
  name: "Contract",
  data() {
    return {
      // 上传参数
      upload: {
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/file/upload",
        // 上传的文件列表
        fileList: []
      },
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
        importantClausesList: [],
        fileIds: [],
      }
      this.upload.fileList = [];
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
        }
        this.form = formData;
        // 处理文件列表回显
        this.upload.fileList = (this.form.fileList || []).map(file => {
            return { name: file.fileOriginName, url: file.fileUrl, fileId: file.fileId };
        });

        this.open = true
        this.title = "修改物业服务合同"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 转换重要条款为JSON字符串
          this.$set(this.form, 'importantClauses', JSON.stringify(this.form.importantClausesList));
          // 收集文件ID
          this.form.fileIds = this.upload.fileList.map(file => file.fileId);

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
      if (!Array.isArray(this.form.importantClausesList)) {
        this.$set(this.form, 'importantClausesList', []);
      }
      this.form.importantClausesList.push({ title: '', content: '' });
    },
    /** 删除条款 */
    removeClause(index) {
      this.form.importantClausesList.splice(index, 1);
    },
    /** 文件上传成功处理 */
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        // 上传成功后，response.data 就是我们后端返回的 SysFileInfo 对象
        // 我们需要将 el-upload 的内部 file 对象和我们的后端数据关联起来
        // 最好的方法是直接使用 el-upload 返回的 fileList，并更新它
        const updatedList = fileList.map(f => {
          // 找到刚刚上传成功的这个文件
          if (f.uid === file.uid) {
            // 将后端返回的数据附加到这个文件对象上，以便后续使用
            return { 
              ...f, 
              name: response.data.fileOriginName, 
              url: response.data.fileUrl, 
              fileId: response.data.fileId 
            };
          }
          return f;
        });
        // 直接用 el-upload 的最新列表覆盖我们的列表
        this.upload.fileList = updatedList;
      } else {
        this.$modal.msgError(response.msg);
        this.$refs.upload.handleRemove(file);
      }
    },
    /** 文件列表移除处理 */
    handleFileRemove(file, fileList) {
      this.upload.fileList = fileList;
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

<style scoped>
.clause-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.clause-title {
  width: 150px;
  margin-right: 10px;
}
.clause-content {
  flex: 1;
  margin-right: 10px;
}
</style>