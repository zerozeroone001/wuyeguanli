<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="adminName">
        <el-input
          v-model="queryParams.adminName"
          placeholder="请输入管理员姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="小区" prop="communityId">
        <el-select v-model="queryParams.communityId" placeholder="请选择小区" clearable>
          <el-option
            v-for="item in communityOptions"
            :key="item.communityId"
            :label="item.communityName"
            :value="item.communityId"
          />
        </el-select>
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
          v-hasPermi="['system:admin:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:admin:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:admin:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="adminList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="管理员ID" align="center" prop="adminId" width="100" />
      <el-table-column label="姓名" align="center" prop="adminName" :show-overflow-tooltip="true" />
      <el-table-column label="用户名" align="center" prop="userName" :show-overflow-tooltip="true" />
      <el-table-column label="小区名称" align="center" prop="communityName" :show-overflow-tooltip="true" />
      <el-table-column label="联系电话" align="center" prop="phone" width="120" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:admin:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:admin:remove']"
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

    <!-- 添加或修改管理员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body @close="cancel">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择用户" clearable filterable :loading="userLoading">
            <el-option
              v-for="item in userOptions"
              :key="item.userId"
              :label="item.userName"
              :value="item.userId"
            >
              <span style="float: left">{{ item.userName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.phonenumber }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="小区" prop="communityId">
          <el-select v-model="form.communityId" placeholder="请选择小区" clearable filterable>
            <el-option
              v-for="item in communityOptions"
              :key="item.communityId"
              :label="item.communityName"
              :value="item.communityId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="管理员姓名" prop="adminName">
          <el-input v-model="form.adminName" placeholder="请输入管理员姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import { listAdmin, getAdmin, delAdmin, addAdmin, updateAdmin } from "@/api/system/admin";
import { listUser } from "@/api/system/user";
import { listCommunity } from "@/api/system/community";

export default {
  name: "Admin",
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
      // 管理员表格数据
      adminList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        adminName: null,
        phone: null,
        communityId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户不能为空", trigger: "change" }
        ],
        communityId: [
          { required: true, message: "小区不能为空", trigger: "change" }
        ],
        adminName: [
          { required: true, message: "管理员姓名不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ]
      },
      // 用户选项
      userOptions: [],
      userLoading: false,
      // 小区选项
      communityOptions: []
    };
  },
  created() {
    this.getList();
    this.getCommunityList();
  },
  methods: {
    /** 查询管理员列表 */
    getList() {
      this.loading = true;
      listAdmin(this.queryParams).then(response => {
        this.adminList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询用户列表 */
    getUserList() {
      this.userLoading = true;
      listUser({ pageNum: 1, pageSize: 100 }).then(response => {
        this.userOptions = response.rows;
        this.userLoading = false;
      });
    },
    /** 查询小区列表 */
    getCommunityList() {
      listCommunity({ pageNum: 1, pageSize: 100 }).then(response => {
        this.communityOptions = response.rows;
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
        adminId: null,
        userId: null,
        communityId: null,
        adminName: null,
        phone: null,
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
      this.ids = selection.map(item => item.adminId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getUserList();
      this.open = true;
      this.title = "添加管理员";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getUserList();
      const adminId = row.adminId || this.ids
      getAdmin(adminId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改管理员";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.adminId != null) {
            updateAdmin(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAdmin(this.form).then(response => {
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
      const adminIds = row.adminId || this.ids;
      this.$modal.confirm('是否确认删除管理员编号为"' + adminIds + '"的数据项？').then(function() {
        return delAdmin(adminIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/admin/export', {
        ...this.queryParams
      }, `管理员_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
