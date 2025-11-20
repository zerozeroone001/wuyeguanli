<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标题" prop="bannerTitle">
        <el-input
          v-model="queryParams.bannerTitle"
          placeholder="请输入轮播图标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="链接类型" prop="linkType">
        <el-select v-model="queryParams.linkType" placeholder="请选择链接类型" clearable>
          <el-option label="业主大会" value="meeting" />
          <el-option label="意见征询" value="opinion" />
          <el-option label="自定义链接" value="url" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="轮播图状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
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
          v-hasPermi="['system:banner:add']"
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
          v-hasPermi="['system:banner:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bannerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="轮播图ID" align="center" prop="bannerId" width="80" />
      <el-table-column label="标题" align="center" prop="bannerTitle" :show-overflow-tooltip="true" />
      <el-table-column label="图片" align="center" prop="bannerImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.bannerImage" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="链接类型" align="center" prop="linkType" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.linkType === 'meeting'">业主大会</span>
          <span v-else-if="scope.row.linkType === 'opinion'">意见征询</span>
          <span v-else-if="scope.row.linkType === 'url'">自定义链接</span>
          <span v-else>无链接</span>
        </template>
      </el-table-column>
      <el-table-column label="关联ID" align="center" prop="linkId" width="80" />
      <el-table-column label="排序" align="center" prop="sortOrder" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:banner:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:banner:remove']"
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

    <!-- 添加或修改轮播图对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body @close="cancel">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="轮播图标题" prop="bannerTitle">
          <el-input v-model="form.bannerTitle" placeholder="请输入轮播图标题" />
        </el-form-item>
        <el-form-item label="轮播图图片" prop="bannerImage">
          <image-upload v-model="form.bannerImage" :limit="1"/>

        </el-form-item>

        <el-form-item label="链接类型" prop="linkType">
          {{form.bannerImage}}
          <el-select v-model="form.linkType" placeholder="请选择链接类型" @change="handleLinkTypeChange">
            <el-option label="无链接" value="" />
            <el-option label="业主大会" value="meeting" />
            <el-option label="意见征询" value="opinion" />
            <el-option label="自定义链接" value="url" />
          </el-select>
        </el-form-item>
        <el-form-item :label="relationLabel" prop="linkId" v-if="form.linkType === 'meeting' || form.linkType === 'opinion'">
          <el-select v-model="form.linkId" placeholder="请选择" clearable filterable :loading="relationLoading">
            <el-option
              v-for="item in relationOptions"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            ></el-option>
          </el-select>
          <span class="text-info">请从列表中选择具体的{{ relationLabel }}</span>
        </el-form-item>
        <el-form-item label="自定义链接" prop="linkUrl" v-if="form.linkType === 'url'">
          <el-input v-model="form.linkUrl" placeholder="请输入自定义链接URL" />
          <span class="text-info">提示：请输入完整的页面路径，如：/pages/xxx/xxx</span>
        </el-form-item>
        <el-form-item label="显示顺序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
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
import { listBanner, getBanner, delBanner, addBanner, updateBanner } from "@/api/system/banner";
import { listMeeting } from "@/api/system/meeting";
import { listOpinionConsultation } from "@/api/system/opinionConsultation";

export default {
  name: "Banner",
  dicts: ['sys_normal_disable'],
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
      // 轮播图表格数据
      bannerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bannerTitle: null,
        linkType: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        bannerTitle: [
          { required: true, message: "轮播图标题不能为空", trigger: "blur" }
        ],
        bannerImage: [
          { required: true, message: "轮播图图片不能为空", trigger: "blur" }
        ],
        sortOrder: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" }
        ]
      },
      // 关联数据
      relationLoading: false,
      relationOptions: []
    };
  },
  computed: {
    relationLabel() {
      if (this.form.linkType === 'meeting') {
        return '业主大会'
      } else if (this.form.linkType === 'opinion') {
        return '意见征询'
      }
      return '关联内容'
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询轮播图列表 */
    getList() {
      this.loading = true;
      listBanner(this.queryParams).then(response => {
        this.bannerList = response.rows;
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
        bannerId: null,
        bannerTitle: null,
        bannerImage: null,
        linkType: null,
        linkId: null,
        linkUrl: null,
        sortOrder: 0,
        status: "0",
        remark: null
      };
      this.relationOptions = [];
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
      this.ids = selection.map(item => item.bannerId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加轮播图";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const bannerId = row.bannerId || this.ids
      getBanner(bannerId).then(response => {
        this.form = response.data;
        // 如果有链接类型，加载关联数据
        if (this.form.linkType === 'meeting' || this.form.linkType === 'opinion') {
          this.loadRelationOptions(this.form.linkType)
        }
        this.open = true;
        this.title = "修改轮播图";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.bannerId != null) {
            updateBanner(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBanner(this.form).then(response => {
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
      const bannerIds = row.bannerId || this.ids;
      this.$modal.confirm('是否确认删除轮播图编号为"' + bannerIds + '"的数据项？').then(function() {
        return delBanner(bannerIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 链接类型改变时清空相关字段 */
    handleLinkTypeChange(value) {
      this.form.linkId = null;
      this.form.linkUrl = null;
      this.relationOptions = [];

      // 如果选择了业主大会或意见征询，加载相应数据
      if (value === 'meeting' || value === 'opinion') {
        this.loadRelationOptions(value);
      }
    },
    /** 加载关联数据选项 */
    loadRelationOptions(type) {
      this.relationLoading = true;
      this.relationOptions = [];

      if (type === 'meeting') {
        // 加载业主大会列表
        listMeeting({ pageNum: 1, pageSize: 100 }).then(response => {
          this.relationOptions = response.rows.map(item => ({
            id: item.meetingId,
            title: item.meetingTitle
          }));
        }).finally(() => {
          this.relationLoading = false;
        });
      } else if (type === 'opinion') {
        // 加载意见征询列表
        listOpinionConsultation({ pageNum: 1, pageSize: 100 }).then(response => {
          this.relationOptions = response.rows.map(item => ({
            id: item.consultationId,
            title: item.title
          }));
        }).finally(() => {
          this.relationLoading = false;
        });
      }
    }
  }
};
</script>

<style scoped>
.text-info {
  font-size: 12px;
  color: #909399;
  margin-left: 10px;
  display: block;
  margin-top: 5px;
}

.image-preview-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  display: inline-block;
}
</style>
