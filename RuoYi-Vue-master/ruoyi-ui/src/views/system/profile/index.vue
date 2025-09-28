<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="真实姓名" prop="realName">
        <el-input
          v-model="queryParams.realName"
          placeholder="请输入真实姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="认证状态" prop="authStatus">
        <el-select v-model="queryParams.authStatus" placeholder="请选择认证状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_auth_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="手机号码" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入手机号码"
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
          type="success"
          plain
          icon="el-icon-check"
          size="mini"
          :disabled="multiple"
          @click="handleBatchAudit('2')"
          v-hasPermi="['system:owner:audit']"
        >批量通过</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-close"
          size="mini"
          :disabled="multiple"
          @click="handleBatchAudit('3')"
          v-hasPermi="['system:owner:audit']"
        >批量驳回</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="profileList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :selectable="checkSelectable" />
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="真实姓名" align="center" prop="realName" width="100" />
      <el-table-column label="手机号码" align="center" prop="phonenumber" width="120" />
      <el-table-column label="身份证号" align="center" prop="idCardNo" width="180" show-overflow-tooltip />
      <el-table-column label="楼栋房号" align="center" prop="address" width="150" show-overflow-tooltip />
      <el-table-column label="身份证正面" align="center" prop="idCardFrontUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.idCardFrontUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="身份证反面" align="center" prop="idCardBackUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.idCardBackUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="认证状态" align="center" prop="authStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_auth_status" :value="scope.row.authStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="审核备注" align="center" prop="authRemark" show-overflow-tooltip />
      <el-table-column label="申请时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核时间" align="center" prop="updateTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleAudit(scope.row)"
            v-if="scope.row.authStatus === 1"
            v-hasPermi="['system:owner:audit']"
          >审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['system:owner:query']"
          >查看</el-button>
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

    <!-- 审核对话框 -->
    <el-dialog title="业主认证审核" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-card class="box-card" style="margin-bottom: 20px;">
          <div slot="header" class="clearfix">
            <span>申请人信息</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="真实姓名：">{{ form.realName }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户ID：">{{ form.userId }}</el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="手机号码：">{{ form.phonenumber }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请时间：">{{ parseTime(form.createTime) }}</el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="身份证号：">{{ form.idCardNo }}</el-form-item>
          <el-form-item label="楼栋房号：">{{ form.address }}</el-form-item>
        </el-card>

        <el-card class="box-card" style="margin-bottom: 20px;">
          <div slot="header" class="clearfix">
            <span>身份证件</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="身份证正面：">
                <image-preview :src="form.idCardFrontUrl" :width="250" :height="150"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="身份证反面：">
                <image-preview :src="form.idCardBackUrl" :width="250" :height="150"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>审核信息</span>
          </div>
          <el-form-item label="审核状态" prop="authStatus">
            <el-radio-group v-model="form.authStatus" @change="handleStatusChange">
              <el-radio label="2">
                <span style="color: #67C23A;">✓ 通过</span>
              </el-radio>
              <el-radio label="3">
                <span style="color: #F56C6C;">✗ 驳回</span>
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            label="审核备注"
            prop="authRemark"
            :rules="form.authStatus === '3' ? [{ required: true, message: '驳回时必须填写审核备注', trigger: 'blur' }] : []"
          >
            <el-input
              v-model="form.authRemark"
              type="textarea"
              :rows="4"
              :placeholder="form.authStatus === '2' ? '请输入审核备注（可选）' : '请输入驳回原因（必填）'"
            />
          </el-form-item>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="业主认证信息详情" :visible.sync="viewOpen" width="700px" append-to-body>
      <el-form :model="viewForm" label-width="120px">
        <el-card class="box-card" style="margin-bottom: 20px;">
          <div slot="header" class="clearfix">
            <span>申请人信息</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="真实姓名：">{{ viewForm.realName }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户ID：">{{ viewForm.userId }}</el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="手机号码：">{{ viewForm.phonenumber }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请时间：">{{ parseTime(viewForm.createTime) }}</el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="身份证号：">{{ viewForm.idCardNo }}</el-form-item>
          <el-form-item label="楼栋房号：">{{ viewForm.address }}</el-form-item>
        </el-card>

        <el-card class="box-card" style="margin-bottom: 20px;">
          <div slot="header" class="clearfix">
            <span>身份证件</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="身份证正面：">
                <image-preview :src="viewForm.idCardFrontUrl" :width="200" :height="120"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="身份证反面：">
                <image-preview :src="viewForm.idCardBackUrl" :width="200" :height="120"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card" v-if="viewForm.authStatus !== '1'">
          <div slot="header" class="clearfix">
            <span>审核信息</span>
          </div>
          <el-form-item label="认证状态：">
            <dict-tag :options="dict.type.sys_auth_status" :value="viewForm.authStatus"/>
          </el-form-item>
          <el-form-item label="审核备注：">{{ viewForm.authRemark || '无' }}</el-form-item>
          <el-form-item label="审核时间：">{{ parseTime(viewForm.updateTime) }}</el-form-item>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 批量审核对话框 -->
    <el-dialog title="批量审核" :visible.sync="batchOpen" width="500px" append-to-body>
      <el-form ref="batchForm" :model="batchForm" :rules="batchRules" label-width="100px">
        <el-alert
          :title="`已选择 ${ids.length} 条待审核记录`"
          type="info"
          show-icon
          :closable="false"
          style="margin-bottom: 20px;"
        />
        <el-form-item label="审核状态" prop="authStatus">
          <el-radio-group v-model="batchForm.authStatus" @change="handleBatchStatusChange">
            <el-radio label="2">
              <span style="color: #67C23A;">✓ 批量通过</span>
            </el-radio>
            <el-radio label="3">
              <span style="color: #F56C6C;">✗ 批量驳回</span>
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="审核备注"
          prop="authRemark"
          :rules="batchForm.authStatus === '3' ? [{ required: true, message: '批量驳回时必须填写审核备注', trigger: 'blur' }] : []"
        >
          <el-input
            v-model="batchForm.authRemark"
            type="textarea"
            :rows="4"
            :placeholder="batchForm.authStatus === '2' ? '请输入批量审核备注（可选）' : '请输入批量驳回原因（必填）'"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBatchForm" :loading="batchLoading">确 定</el-button>
        <el-button @click="batchOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOwnerProfile, auditProfile } from "@/api/system/ownerProfile";

export default {
  name: "OwnerProfileAudit",
  dicts: ['sys_auth_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 业主信息表格数据
      profileList: [],
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看弹出层
      viewOpen: false,
      // 是否显示批量审核弹出层
      batchOpen: false,
      // 提交按钮加载状态
      submitLoading: false,
      // 批量审核按钮加载状态
      batchLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        realName: null,
        phonenumber: null,
        authStatus: 1, // 默认查询待审核状态
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 批量审核表单参数
      batchForm: {},
      // 表单校验
      rules: {
        authStatus: [
          { required: true, message: "审核状态不能为空", trigger: "blur" }
        ]
      },
      // 批量审核表单校验
      batchRules: {
        authStatus: [
          { required: true, message: "审核状态不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询业主信息列表 */
    getList() {
      this.loading = true;
      listOwnerProfile(this.queryParams).then(response => {
        this.profileList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.ownerId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // 判断是否可选中
    checkSelectable(row) {
      return row.authStatus === '1'; // 只有待审核状态才能被选中
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        ownerId: null,
        userId: null,
        realName: null,
        phonenumber: null,
        idCardNo: null,
        address: null,
        idCardFrontUrl: null,
        idCardBackUrl: null,
        authStatus: "2",
        authRemark: null,
        createTime: null
      };
      this.resetForm("form");
    },
    // 查看表单重置
    resetView() {
      this.viewForm = {};
    },
    // 批量审核表单重置
    resetBatch() {
      this.batchForm = {
        authStatus: "2",
        authRemark: null
      };
      this.resetForm("batchForm");
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
    /** 审核按钮操作 */
    handleAudit(row) {
      this.reset();
      this.form = { ...row };
      this.form.authStatus = '2'; // 默认通过
      this.open = true;
      this.title = "业主认证审核";
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.resetView();
      this.viewForm = { ...row };
      this.viewOpen = true;
    },
    /** 批量审核按钮操作 */
    handleBatchAudit(authStatus) {
      if (this.ids.length === 0) {
        this.$modal.msgError("请选择要审核的数据");
        return;
      }
      this.resetBatch();
      this.batchForm.authStatus = authStatus;
      this.batchOpen = true;
    },
    /** 审核状态变化 */
    handleStatusChange(value) {
      if (value === '2') {
        this.form.authRemark = '';
      }
    },
    /** 批量审核状态变化 */
    handleBatchStatusChange(value) {
      if (value === '2') {
        this.batchForm.authRemark = '';
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          auditProfile(this.form).then(response => {
            this.$modal.msgSuccess("审核操作成功，用户端状态已更新");
            this.open = false;
            this.submitLoading = false;
            this.getList();
          }).catch(() => {
            this.submitLoading = false;
          });
        }
      });
    },
    /** 批量审核提交按钮 */
    submitBatchForm() {
      this.$refs["batchForm"].validate(valid => {
        if (valid) {
          this.batchLoading = true;
          const promises = this.ids.map(ownerId => {
            const auditData = {
              ownerId: ownerId,
              authStatus: this.batchForm.authStatus,
              authRemark: this.batchForm.authRemark
            };
            return auditProfile(auditData);
          });

          Promise.all(promises).then(() => {
            this.$modal.msgSuccess("批量审核操作成功，用户端状态已更新");
            this.batchOpen = false;
            this.batchLoading = false;
            this.getList();
          }).catch(() => {
            this.batchLoading = false;
          });
        }
      });
    }
  }
};
</script>

<style scoped>
.box-card {
  margin-bottom: 15px;
}

.box-card .el-card__header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.box-card .el-card__header span {
  font-weight: 600;
  color: #303133;
}

.el-form-item {
  margin-bottom: 15px;
}

.el-radio-group .el-radio {
  margin-right: 20px;
}

.el-radio-group .el-radio span {
  font-size: 14px;
  font-weight: 500;
}

.dialog-footer {
  text-align: right;
  padding-top: 20px;
}

.el-table .el-table__header {
  background-color: #fafafa;
}

.el-table .el-table__header th {
  background-color: #fafafa !important;
  font-weight: 600;
}

.el-button--success.is-plain {
  border-color: #67c23a;
  color: #67c23a;
}

.el-button--danger.is-plain {
  border-color: #f56c6c;
  color: #f56c6c;
}

.el-alert {
  border-radius: 4px;
}

.el-alert--info {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}

.image-preview {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  transition: all 0.3s;
}

.image-preview:hover {
  border-color: #409eff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>
