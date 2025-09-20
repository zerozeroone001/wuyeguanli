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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="profileList">
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="真实姓名" align="center" prop="realName" />
      <el-table-column label="身份证号" align="center" prop="idCardNo" />
      <el-table-column label="楼栋房号" align="center" prop="address" />
      <el-table-column label="身份证正面" align="center" prop="idCardFrontUrl">
        <template slot-scope="scope">
          <image-preview :src="scope.row.idCardFrontUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="身份证反面" align="center" prop="idCardBackUrl">
        <template slot-scope="scope">
          <image-preview :src="scope.row.idCardBackUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="认证状态" align="center" prop="authStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_auth_status" :value="scope.row.authStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="审核备注" align="center" prop="authRemark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleAudit(scope.row)"
            v-if="scope.row.authStatus === '1'"
            v-hasPermi="['system:profile:audit']"
          >审核</el-button>
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
    <el-dialog title="业主认证审核" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
            <el-col :span="12">
                <el-form-item label="真实姓名：">{{ form.realName }}</el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="用户ID：">{{ form.userId }}</el-form-item>
            </el-col>
        </el-row>
        <el-form-item label="身份证号：">{{ form.idCardNo }}</el-form-item>
        <el-form-item label="楼栋房号：">{{ form.address }}</el-form-item>
        <el-form-item label="身份证正面：">
            <image-preview :src="form.idCardFrontUrl" :width="200" :height="120"/>
        </el-form-item>
        <el-form-item label="身份证反面：">
            <image-preview :src="form.idCardBackUrl" :width="200" :height="120"/>
        </el-form-item>
        <el-form-item label="审核状态" prop="authStatus">
          <el-radio-group v-model="form.authStatus">
            <el-radio label="2">通过</el-radio>
            <el-radio label="3">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" prop="authRemark">
          <el-input v-model="form.authRemark" type="textarea" placeholder="请输入审核备注，例如驳回原因" />
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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        realName: null,
        authStatus:'NOT',
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        authStatus: [
          { required: true, message: "审核状态不能为空", trigger: "blur" }
        ],
        authRemark: [
          { required: true, message: "审核备注不能为空", trigger: "blur" }
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
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: null,
        realName: null,
        idCardNo: null,
        address: null,
        idCardFrontUrl: null,
        idCardBackUrl: null,
        authStatus: "2",
        authRemark: null
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
    /** 审核按钮操作 */
    handleAudit(row) {
      this.reset();
      this.form = { ...row }; // 简化处理，直接复制行数据
      this.form.authStatus = '2'; // 默认通过
      this.open = true;
      this.title = "业主认证审核";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
            if (this.form.authStatus === '3' && !this.form.authRemark) {
                this.$modal.msgError("驳回认证必须填写审核备注");
                return;
            }
          auditProfile(this.form).then(response => {
            this.$modal.msgSuccess("审核操作成功");
            this.open = false;
            this.getList();
          });
        }
      });
    }
  }
};
</script>
