<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="监督标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="关联合同" prop="contractId">
        <el-select v-model="queryParams.contractId" placeholder="请选择合同" clearable filterable>
          <el-option v-for="item in contractList" :key="item.contractId" :label="item.contractName" :value="item.contractId" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否公示" prop="isPublished">
        <el-select v-model="queryParams.isPublished" placeholder="请选择" clearable>
          <el-option label="是" value="1" />
          <el-option label="否" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:contractSupervision:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:contractSupervision:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:contractSupervision:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="supervisionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="supervisionId" width="60" />
      <el-table-column label="监督标题" align="center" prop="title" min-width="150" show-overflow-tooltip />
      <el-table-column label="关联合同" align="center" prop="contractName" min-width="120" show-overflow-tooltip />
      <el-table-column label="视频" align="center" width="60">
        <template slot-scope="scope">
          <span v-if="hasFiles(scope.row.videoUrls)">{{ getFileCount(scope.row.videoUrls) }}个</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="考核细则" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="hasFiles(scope.row.rulesUrls)">{{ getFileCount(scope.row.rulesUrls) }}个</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="考核通知" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="hasFiles(scope.row.noticeUrls)">{{ getFileCount(scope.row.noticeUrls) }}个</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="考核表" align="center" width="70">
        <template slot-scope="scope">
          <span v-if="hasFiles(scope.row.tableUrls)">{{ getFileCount(scope.row.tableUrls) }}个</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="考核结果" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="hasFiles(scope.row.resultUrls)">{{ getFileCount(scope.row.resultUrls) }}个</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="是否公示" align="center" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isPublished === '1' ? 'success' : 'info'">
            {{ scope.row.isPublished === '1' ? '已公示' : '未公示' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:contractSupervision:edit']">修改</el-button>
          <el-button v-if="hasFiles(scope.row.resultUrls) && scope.row.isPublished !== '1'" size="mini" type="text" icon="el-icon-s-promotion" @click="handlePublish(scope.row)" v-hasPermi="['system:contractSupervision:publish']">公示考核结果</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:contractSupervision:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="关联合同" prop="contractId">
              <el-select v-model="form.contractId" placeholder="请选择合同" style="width:100%" filterable @change="handleContractChange">
                <el-option v-for="item in contractList" :key="item.contractId" :label="item.contractName" :value="item.contractId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监督标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入监督标题" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="监督内容" prop="content">
          <editor v-model="form.content" :min-height="200" />
        </el-form-item>

        <!-- 视频上传 -->
        <el-form-item label="视频上传">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="(res, file) => handleUploadSuccess(res, file, 'videoUrls')"
            :on-remove="(file) => handleRemove(file, 'videoUrls')"
            :file-list="videoFileList"
            :before-upload="beforeVideoUpload"
            multiple
            accept="video/*">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">支持mp4、avi、mov等视频格式</div>
          </el-upload>
        </el-form-item>

        <!-- 考核细则上传 -->
        <el-form-item label="考核细则">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="(res, file) => handleUploadSuccess(res, file, 'rulesUrls')"
            :on-remove="(file) => handleRemove(file, 'rulesUrls')"
            :file-list="rulesFileList"
            multiple>
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">支持任意文件类型</div>
          </el-upload>
        </el-form-item>

        <!-- 考核通知上传 -->
        <el-form-item label="考核通知">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="(res, file) => handleUploadSuccess(res, file, 'noticeUrls')"
            :on-remove="(file) => handleRemove(file, 'noticeUrls')"
            :file-list="noticeFileList"
            multiple>
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">支持任意文件类型</div>
          </el-upload>
        </el-form-item>

        <!-- 考核表上传 -->
        <el-form-item label="考核表">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="(res, file) => handleUploadSuccess(res, file, 'tableUrls')"
            :on-remove="(file) => handleRemove(file, 'tableUrls')"
            :file-list="tableFileList"
            multiple>
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">支持任意文件类型</div>
          </el-upload>
        </el-form-item>

        <!-- 考核结果上传 -->
        <el-form-item label="考核结果">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="(res, file) => handleUploadSuccess(res, file, 'resultUrls')"
            :on-remove="(file) => handleRemove(file, 'resultUrls')"
            :file-list="resultFileList"
            multiple>
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">支持任意文件类型</div>
          </el-upload>
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
    <el-dialog title="监督详情" :visible.sync="viewOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="监督标题">{{ viewForm.title }}</el-descriptions-item>
        <el-descriptions-item label="关联合同">{{ viewForm.contractName }}</el-descriptions-item>
        <el-descriptions-item label="是否公示">
          <el-tag :type="viewForm.isPublished === '1' ? 'success' : 'info'">
            {{ viewForm.isPublished === '1' ? '已公示' : '未公示' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="公示时间">{{ viewForm.publishTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewForm.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewForm.remark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">监督内容</el-divider>
      <div v-html="viewForm.content" class="content-html"></div>

      <el-divider content-position="left">上传文件</el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <h4>视频文件</h4>
          <div v-if="hasFiles(viewForm.videoUrls)">
            <div v-for="(url, index) in parseUrls(viewForm.videoUrls)" :key="'video-' + index" class="file-item">
              <el-link type="primary" :href="url" target="_blank">视频{{ index + 1 }}</el-link>
            </div>
          </div>
          <div v-else class="no-file">暂无视频</div>
        </el-col>
        <el-col :span="12">
          <h4>考核细则</h4>
          <div v-if="hasFiles(viewForm.rulesUrls)">
            <div v-for="(url, index) in parseUrls(viewForm.rulesUrls)" :key="'rules-' + index" class="file-item">
              <el-link type="primary" :href="url" target="_blank">文件{{ index + 1 }}</el-link>
            </div>
          </div>
          <div v-else class="no-file">暂无文件</div>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 15px;">
        <el-col :span="12">
          <h4>考核通知</h4>
          <div v-if="hasFiles(viewForm.noticeUrls)">
            <div v-for="(url, index) in parseUrls(viewForm.noticeUrls)" :key="'notice-' + index" class="file-item">
              <el-link type="primary" :href="url" target="_blank">文件{{ index + 1 }}</el-link>
            </div>
          </div>
          <div v-else class="no-file">暂无文件</div>
        </el-col>
        <el-col :span="12">
          <h4>考核表</h4>
          <div v-if="hasFiles(viewForm.tableUrls)">
            <div v-for="(url, index) in parseUrls(viewForm.tableUrls)" :key="'table-' + index" class="file-item">
              <el-link type="primary" :href="url" target="_blank">文件{{ index + 1 }}</el-link>
            </div>
          </div>
          <div v-else class="no-file">暂无文件</div>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 15px;">
        <el-col :span="12">
          <h4>考核结果</h4>
          <div v-if="hasFiles(viewForm.resultUrls)">
            <div v-for="(url, index) in parseUrls(viewForm.resultUrls)" :key="'result-' + index" class="file-item">
              <el-link type="primary" :href="url" target="_blank">文件{{ index + 1 }}</el-link>
            </div>
          </div>
          <div v-else class="no-file">暂无文件</div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { listContractSupervision, getContractSupervision, addContractSupervision, updateContractSupervision, delContractSupervision, publishSupervisionResult, listContract } from "@/api/system/contract";
import { getToken } from "@/utils/auth";

export default {
  name: "ContractSupervision",
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
      // 履约监督列表
      supervisionList: [],
      // 合同列表
      contractList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情弹出层
      viewOpen: false,
      // 详情数据
      viewForm: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        contractId: null,
        isPublished: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        contractId: [{ required: true, message: "请选择关联合同", trigger: "change" }],
        title: [{ required: true, message: "请输入监督标题", trigger: "blur" }]
      },
      // 上传相关
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      uploadHeaders: { Authorization: "Bearer " + getToken() },
      // 文件列表
      videoFileList: [],
      rulesFileList: [],
      noticeFileList: [],
      tableFileList: [],
      resultFileList: [],
      // 临时存储上传的URL
      uploadedUrls: {
        videoUrls: [],
        rulesUrls: [],
        noticeUrls: [],
        tableUrls: [],
        resultUrls: []
      }
    };
  },
  created() {
    this.getList();
    this.getContractList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listContractSupervision(this.queryParams).then(response => {
        this.supervisionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询合同列表 */
    getContractList() {
      listContract({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.contractList = response.rows || [];
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
        supervisionId: null,
        contractId: null,
        communityId: null,
        title: null,
        content: null,
        videoUrls: null,
        rulesUrls: null,
        noticeUrls: null,
        tableUrls: null,
        resultUrls: null,
        remark: null
      };
      this.videoFileList = [];
      this.rulesFileList = [];
      this.noticeFileList = [];
      this.tableFileList = [];
      this.resultFileList = [];
      this.uploadedUrls = {
        videoUrls: [],
        rulesUrls: [],
        noticeUrls: [],
        tableUrls: [],
        resultUrls: []
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
      this.ids = selection.map(item => item.supervisionId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增合同履约监督";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const supervisionId = row.supervisionId || this.ids[0];
      getContractSupervision(supervisionId).then(response => {
        this.form = response.data;
        // 解析已有文件
        this.parseExistingFiles();
        this.open = true;
        this.title = "修改合同履约监督";
      });
    },
    /** 查看详情 */
    handleView(row) {
      getContractSupervision(row.supervisionId).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 解析已有文件到文件列表 */
    parseExistingFiles() {
      const fields = ['videoUrls', 'rulesUrls', 'noticeUrls', 'tableUrls', 'resultUrls'];
      const fileLists = ['videoFileList', 'rulesFileList', 'noticeFileList', 'tableFileList', 'resultFileList'];
      
      fields.forEach((field, index) => {
        if (this.form[field]) {
          try {
            const urls = JSON.parse(this.form[field]);
            this[fileLists[index]] = urls.map((url, i) => ({
              name: `文件${i + 1}`,
              url: url
            }));
            this.uploadedUrls[field] = urls;
          } catch (e) {
            this[fileLists[index]] = [];
            this.uploadedUrls[field] = [];
          }
        }
      });
    },
    /** 合同选择变化 */
    handleContractChange(contractId) {
      const contract = this.contractList.find(c => c.contractId === contractId);
      if (contract) {
        this.form.communityId = contract.communityId;
      }
    },
    /** 视频上传前校验 */
    beforeVideoUpload(file) {
      const isVideo = file.type.startsWith('video/');
      if (!isVideo) {
        this.$message.error('请上传视频文件！');
      }
      return isVideo;
    },
    /** 文件上传成功 */
    handleUploadSuccess(response, file, field) {
      if (response.code === 200) {
        const url = response.url || response.fileName;
        this.uploadedUrls[field].push(url);
        this.$message.success('上传成功');
      } else {
        this.$message.error(response.msg || '上传失败');
      }
    },
    /** 文件移除 */
    handleRemove(file, field) {
      const url = file.url || file.response?.url || file.response?.fileName;
      const index = this.uploadedUrls[field].indexOf(url);
      if (index > -1) {
        this.uploadedUrls[field].splice(index, 1);
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将上传的URL转为JSON字符串
          this.form.videoUrls = this.uploadedUrls.videoUrls.length > 0 ? JSON.stringify(this.uploadedUrls.videoUrls) : null;
          this.form.rulesUrls = this.uploadedUrls.rulesUrls.length > 0 ? JSON.stringify(this.uploadedUrls.rulesUrls) : null;
          this.form.noticeUrls = this.uploadedUrls.noticeUrls.length > 0 ? JSON.stringify(this.uploadedUrls.noticeUrls) : null;
          this.form.tableUrls = this.uploadedUrls.tableUrls.length > 0 ? JSON.stringify(this.uploadedUrls.tableUrls) : null;
          this.form.resultUrls = this.uploadedUrls.resultUrls.length > 0 ? JSON.stringify(this.uploadedUrls.resultUrls) : null;

          if (this.form.supervisionId != null) {
            updateContractSupervision(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addContractSupervision(this.form).then(response => {
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
      const supervisionIds = row.supervisionId || this.ids;
      this.$modal.confirm('是否确认删除？').then(() => {
        return delContractSupervision(supervisionIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 公示考核结果 */
    handlePublish(row) {
      this.$modal.confirm('确认公示该考核结果？公示后将对外展示。').then(() => {
        return publishSupervisionResult(row.supervisionId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("公示成功");
      }).catch(() => {});
    },
    /** 判断是否有文件 */
    hasFiles(urlsStr) {
      if (!urlsStr) return false;
      try {
        const urls = JSON.parse(urlsStr);
        return Array.isArray(urls) && urls.length > 0;
      } catch (e) {
        return false;
      }
    },
    /** 获取文件数量 */
    getFileCount(urlsStr) {
      if (!urlsStr) return 0;
      try {
        const urls = JSON.parse(urlsStr);
        return Array.isArray(urls) ? urls.length : 0;
      } catch (e) {
        return 0;
      }
    },
    /** 解析URL字符串 */
    parseUrls(urlsStr) {
      if (!urlsStr) return [];
      try {
        const urls = JSON.parse(urlsStr);
        return Array.isArray(urls) ? urls : [];
      } catch (e) {
        return [];
      }
    }
  }
};
</script>

<style scoped>
.content-html {
  padding: 10px;
  background: #f5f5f5;
  border-radius: 4px;
  min-height: 100px;
}
.file-item {
  margin-bottom: 5px;
}
.no-file {
  color: #999;
  font-size: 14px;
}
h4 {
  margin: 0 0 10px 0;
  color: #606266;
}
</style>
