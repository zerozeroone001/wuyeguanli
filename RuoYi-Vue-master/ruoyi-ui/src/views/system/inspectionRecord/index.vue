<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="关联合同" prop="contractId">
        <el-select v-model="queryParams.contractId" placeholder="请选择合同" clearable filterable>
          <el-option
            v-for="contract in contractOptions"
            :key="contract.contractId"
            :label="contract.contractName"
            :value="contract.contractId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="草稿" value="0" />
          <el-option label="已提交" value="1" />
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
        >创建</el-button>
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
      <el-table-column label="记录ID" align="center" prop="recordId" width="80" />
      <el-table-column label="关联合同" align="center" prop="contractName" min-width="200" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'info'">
            {{ scope.row.status === '1' ? '已提交' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >详情</el-button>
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
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="220px">
        <el-form-item label="关联合同" prop="contractId">
          <el-select v-model="form.contractId" placeholder="请选择合同" filterable style="width: 100%">
            <el-option
              v-for="contract in contractOptions"
              :key="contract.contractId"
              :label="contract.contractName"
              :value="contract.contractId"
            />
          </el-select>
        </el-form-item>

        <el-divider content-position="center">上传文件</el-divider>

        <el-form-item label="1. 封面及目录">
          <file-upload v-model="form.coverTocUrl"/>
        </el-form-item>
        <el-form-item label="2. 前言">
          <file-upload v-model="form.prefaceUrl"/>
        </el-form-item>
        <el-form-item label="3. 解除终止查验报告">
          <file-upload v-model="form.terminationReportUrl"/>
        </el-form-item>
        <el-form-item label="4. 进场前承接查验清单">
          <file-upload v-model="form.entryChecklistUrl"/>
        </el-form-item>
        <el-form-item label="5. 履行告知清单">
          <file-upload v-model="form.performanceNoticeUrl"/>
        </el-form-item>
        <el-form-item label="6. 整改通知单">
          <file-upload v-model="form.rectificationNoticeUrl"/>
        </el-form-item>
        <el-form-item label="7. 整改结果告知单">
          <file-upload v-model="form.rectificationResultUrl"/>
        </el-form-item>
        <el-form-item label="8. 履行结果评定通知书">
          <file-upload v-model="form.assessmentNoticeUrl"/>
        </el-form-item>
        <el-form-item label="9. 履行年度报告">
          <file-upload v-model="form.annualReportUrl"/>
        </el-form-item>
        <el-form-item label="10. 解除终止查验报告(二)">
          <file-upload v-model="form.terminationReportUrl2"/>
        </el-form-item>
        <el-form-item label="11. 查验结论分析">
          <file-upload v-model="form.conclusionAnalysisUrl"/>
        </el-form-item>
        <el-form-item label="12. 法律意见书">
          <file-upload v-model="form.legalOpinionUrl"/>
        </el-form-item>

        <el-divider></el-divider>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">草稿</el-radio>
            <el-radio label="1">已提交</el-radio>
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

    <!-- 详情对话框 -->
    <el-dialog title="承接查验详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="关联合同">{{ detailData.contractName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === '1' ? 'success' : 'info'">
            {{ detailData.status === '1' ? '已提交' : '草稿' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(detailData.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="center">文件列表</el-divider>

      <el-table :data="fileList" style="width: 100%">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="文件名称" prop="name" min-width="250" />
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.url ? 'success' : 'info'">
              {{ scope.row.url ? '已上传' : '未上传' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.url"
              size="mini"
              type="text"
              icon="el-icon-download"
              @click="handleDownload(scope.row.url)"
            >下载</el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/system/inspectionRecord";
import { listContract } from "@/api/system/contract";
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
      // 合同选项
      contractOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情对话框
      detailOpen: false,
      // 详情数据
      detailData: {},
      // 文件列表
      fileList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractId: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        contractId: [
          { required: true, message: "请选择关联合同", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getContractOptions();
  },
  methods: {
    /** 查询承接查验记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询合同选项 */
    getContractOptions() {
      listContract().then(response => {
        this.contractOptions = response.rows;
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
        contractId: null,
        status: "0",
        coverTocUrl: null,
        prefaceUrl: null,
        terminationReportUrl: null,
        entryChecklistUrl: null,
        performanceNoticeUrl: null,
        rectificationNoticeUrl: null,
        rectificationResultUrl: null,
        assessmentNoticeUrl: null,
        annualReportUrl: null,
        terminationReportUrl2: null,
        conclusionAnalysisUrl: null,
        legalOpinionUrl: null,
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
      this.ids = selection.map(item => item.recordId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "创建承接查验";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const recordId = row.recordId || this.ids[0];
      getRecord(recordId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改承接查验";
      });
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      getRecord(row.recordId).then(response => {
        this.detailData = response.data;
        this.fileList = [
          { name: '1. 承接查验报告及法律意见书封面及目录', url: this.detailData.coverTocUrl },
          { name: '2. 前言', url: this.detailData.prefaceUrl },
          { name: '3. 物业服务合同解除终止查验报告', url: this.detailData.terminationReportUrl },
          { name: '4. 物业服务人进场前承接查验清单', url: this.detailData.entryChecklistUrl },
          { name: '5. 物业服务合同履行告知清单', url: this.detailData.performanceNoticeUrl },
          { name: '6. 物业服务整改通知单', url: this.detailData.rectificationNoticeUrl },
          { name: '7. 物业服务整改结果告知单', url: this.detailData.rectificationResultUrl },
          { name: '8. 物业服务履行结果评定通知书', url: this.detailData.assessmentNoticeUrl },
          { name: '9. 物业服务合同履行年度报告', url: this.detailData.annualReportUrl },
          { name: '10. 物业服务合同解除终止查验报告(二)', url: this.detailData.terminationReportUrl2 },
          { name: '11. 查验结论分析', url: this.detailData.conclusionAnalysisUrl },
          { name: '12. 物业服务项目承接查验法律意见书', url: this.detailData.legalOpinionUrl }
        ];
        this.detailOpen = true;
      });
    },
    /** 下载文件 */
    handleDownload(url) {
      if (url) {
        window.open(url, '_blank');
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecord(this.form).then(response => {
              this.$modal.msgSuccess("创建成功");
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
      this.$modal.confirm('是否确认删除选中的承接查验记录？').then(function() {
        return delRecord(recordIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
