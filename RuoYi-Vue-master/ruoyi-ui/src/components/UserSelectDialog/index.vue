<template>
  <el-dialog
    title="选择用户"
    :visible.sync="visible"
    width="800px"
    append-to-body
    @close="handleClose"
  >
    <div class="user-select-container">
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input
            v-model="queryParams.phonenumber"
            placeholder="请输入手机号码"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input
            v-model="queryParams.userName"
            placeholder="请输入用户名"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input
            v-model="queryParams.nickName"
            placeholder="请输入昵称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 用户列表 -->
      <el-table
        ref="userTable"
        v-loading="loading"
        :data="userList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        highlight-current-row
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="用户名" align="center" prop="userName" />
        <el-table-column label="昵称" align="center" prop="nickName" />
        <el-table-column label="手机号码" align="center" prop="phonenumber" />
        <el-table-column label="邮箱" align="center" prop="email" />
        <el-table-column label="状态" align="center" prop="status">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
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
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleConfirm" :disabled="!selectedUser">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getUnboundUsers } from "@/api/system/owner";

export default {
  name: "UserSelectDialog",
  dicts: ['sys_normal_disable'],
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      userList: [],
      total: 0,
      selectedUser: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        phonenumber: null,
        userName: null,
        nickName: null
      }
    };
  },
  watch: {
    visible(val) {
      if (val) {
        this.resetQuery();
        this.getList();
      }
    }
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      getUnboundUsers(this.queryParams).then(response => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$modal.msgError("查询用户列表失败");
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      if (this.$refs.queryForm) {
        this.$refs.queryForm.resetFields();
      }
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        phonenumber: null,
        userName: null,
        nickName: null
      };
      this.selectedUser = null;
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      if (selection.length > 0) {
        this.selectedUser = selection[0];
      } else {
        this.selectedUser = null;
      }
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.selectedUser = row;
      // 更新表格选中状态
      this.$nextTick(() => {
        this.$refs.userTable && this.$refs.userTable.toggleRowSelection(row, true);
      });
    },
    /** 确认选择 */
    handleConfirm() {
      if (this.selectedUser) {
        this.$emit('confirm', this.selectedUser);
        this.handleClose();
      }
    },
    /** 关闭弹窗 */
    handleClose() {
      this.$emit('update:visible', false);
      this.resetQuery();
    }
  }
};
</script>

<style lang="scss" scoped>
.user-select-container {
  .el-form {
    margin-bottom: 20px;
  }
}
</style>
