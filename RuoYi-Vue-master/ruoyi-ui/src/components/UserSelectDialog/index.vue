<template>
  <el-dialog
    title="选择候选人"
    :visible.sync="visible"
    width="900px"
    append-to-body
    @close="handleClose"
  >
    <div class="user-select-container">
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
        <el-form-item label="业主姓名" prop="userName">
          <el-input
            v-model="queryParams.userName"
            placeholder="请输入业主姓名"
            clearable
            @keyup.enter.native="handleQuery"
          />
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

      <!-- 业主列表 -->
      <el-table
        ref="userTable"
        v-loading="loading"
        :data="userList"
        @current-change="handleCurrentChange"
        @row-click="handleRowClick"
        highlight-current-row
      >
        <el-table-column width="55" align="center">
          <template slot-scope="scope">
            <el-radio v-model="selectedUserId" :label="scope.row.userId"><i></i></el-radio>
          </template>
        </el-table-column>
        <el-table-column label="姓名" align="center" prop="userName" />
        <el-table-column label="联系电话" align="center" prop="phonenumber" />
        <el-table-column label="详细地址" align="center" prop="roomNumber">
           <template slot-scope="scope">
             <!-- 尝试显示详细地址，可能是 roomNumber 或者 address 字段 -->
             <span>{{ scope.row.roomNumber || scope.row.address || '-' }}</span>
           </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
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
import { listOwner } from "@/api/system/owner";

export default {
  name: "UserSelectDialog",
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    communityId: {
      type: [Number, String],
      default: null
    }
  },
  data() {
    return {
      loading: false,
      userList: [],
      total: 0,
      selectedUserId: null,
      selectedUser: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        phonenumber: null,
        userName: null,
        communityId: null
      }
    };
  },
  watch: {
    visible(val) {
      if (val) {
        this.resetQuery();
        this.getList();
      }
    },
    communityId(val) {
      this.queryParams.communityId = val;
    }
  },
  methods: {
    /** 查询业主列表 */
    getList() {
      this.loading = true;
      // 确保使用传入的 communityId
      this.queryParams.communityId = this.communityId;
      
      listOwner(this.queryParams).then(response => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
        this.$modal.msgError("查询业主列表失败");
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
        communityId: this.communityId
      };
      this.selectedUser = null;
      this.selectedUserId = null;
      if (this.$refs.userTable) {
        this.$refs.userTable.setCurrentRow(null);
      }
    },
    /** 单选框选中数据 */
    handleCurrentChange(val) {
      if (val) {
        this.selectedUser = val;
        this.selectedUserId = val.userId;
      }
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.selectedUser = row;
      this.selectedUserId = row.userId;
      // 触发表格的单选高亮
      this.$refs.userTable.setCurrentRow(row);
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
      // 不要在这里清空 queryParams，留给 watch visible 处理或者 resetQuery
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