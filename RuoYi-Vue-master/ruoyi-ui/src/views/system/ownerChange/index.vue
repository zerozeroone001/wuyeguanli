<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="原业主" prop="oldOwnerName">
        <el-input
          v-model="queryParams.oldOwnerName"
          placeholder="请输入原业主姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="新业主" prop="newOwnerName">
        <el-input
          v-model="queryParams.newOwnerName"
          placeholder="请输入新业主姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待审核" value="0" />
          <el-option label="已通过" value="1" />
          <el-option label="已驳回" value="2" />
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:ownerChange:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:ownerChange:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ownerChangeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="小区名称" align="center" prop="communityName" />
      <el-table-column label="房产名称" align="center" prop="propertyName" />
      <el-table-column label="原业主" align="center" prop="oldOwnerName">
        <template slot-scope="scope">
          {{ scope.row.oldOwnerName }} ({{ scope.row.oldOwnerPhone }})
        </template>
      </el-table-column>
      <el-table-column label="新业主" align="center" prop="newOwnerName">
        <template slot-scope="scope">
          {{ scope.row.newOwnerName }} ({{ scope.row.newOwnerPhone }})
        </template>
      </el-table-column>
      <el-table-column label="房产证" align="center" prop="propertyCertImage">
        <template slot-scope="scope">
          <image-preview :src="scope.row.propertyCertImage" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="申请时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == '0'" type="warning">待审核</el-tag>
          <el-tag v-else-if="scope.row.status == '1'" type="success">已通过</el-tag>
          <el-tag v-else-if="scope.row.status == '2'" type="danger">已驳回</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAudit(scope.row)"
            v-if="scope.row.status == '0'"
            v-hasPermi="['system:ownerChange:audit']"
          >审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['system:ownerChange:query']"
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
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" prop="auditRemark" v-if="form.status == '2'">
          <el-input v-model="form.auditRemark" type="textarea" placeholder="请输入驳回原因" />
        </el-form-item>
        <div v-if="isView" class="view-info">
           <el-form-item label="原业主">{{ form.oldOwnerName }} - {{ form.oldOwnerPhone }}</el-form-item>
           <el-form-item label="新业主">{{ form.newOwnerName }} - {{ form.newOwnerPhone }}</el-form-item>
           <el-form-item label="新业主身份证">{{ form.newOwnerIdCard }}</el-form-item>
           <el-form-item label="房产证">
             <image-preview :src="form.propertyCertImage" />
           </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="!isView">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOwnerChange, getOwnerChange, delOwnerChange, auditOwnerChange } from "@/api/system/ownerChange";

export default {
  name: "OwnerChange",
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
      // 业主变更申请表格数据
      ownerChangeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否查看模式
      isView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        oldOwnerName: null,
        newOwnerName: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [
          { required: true, message: "审核结果不能为空", trigger: "change" }
        ],
        auditRemark: [
          { required: true, message: "审核备注不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询业主变更申请列表 */
    getList() {
      this.loading = true;
      listOwnerChange(this.queryParams).then(response => {
        this.ownerChangeList = response.rows;
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
        changeId: null,
        status: "1",
        auditRemark: null
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
      this.ids = selection.map(item => item.changeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 审核按钮操作 */
    handleAudit(row) {
      this.reset();
      this.isView = false;
      const changeId = row.changeId || this.ids
      getOwnerChange(changeId).then(response => {
        this.form = response.data;
        // 默认设置为通过
        this.form.status = "1";
        this.open = true;
        this.title = "审核业主变更申请";
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.reset();
      this.isView = true;
      getOwnerChange(row.changeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "查看申请详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.changeId != null) {
            auditOwnerChange(this.form).then(response => {
              this.$modal.msgSuccess("审核成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const changeIds = row.changeId || this.ids;
      this.$modal.confirm('是否确认删除业主变更申请编号为"' + changeIds + '"的数据项？').then(function() {
        return delOwnerChange(changeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/ownerChange/export', {
        ...this.queryParams
      }, `ownerChange_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
