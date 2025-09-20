<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="制度名称" prop="regulationName">
        <el-input
          v-model="queryParams.regulationName"
          placeholder="请输入制度名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="制度分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择制度分类" clearable>
          <el-option
            v-for="category in categoryList"
            :key="category.categoryId"
            :label="category.categoryName"
            :value="category.categoryId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="制度类型" prop="regulationType">
        <el-input
          v-model="queryParams.regulationType"
          placeholder="请输入制度类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="草稿" value="draft" />
          <el-option label="生效中" value="active" />
          <el-option label="已失效" value="expired" />
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
          v-hasPermi="['system:regulation:add']"
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
          v-hasPermi="['system:regulation:edit']"
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
          v-hasPermi="['system:regulation:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="regulationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="制度ID" align="center" prop="regulationId" width="80"/>
      <el-table-column label="制度名称" align="center" prop="regulationName" :show-overflow-tooltip="true" />
      <el-table-column label="制度分类" align="center" prop="categoryName" />
      <el-table-column label="制度类型" align="center" prop="regulationType" />
      <el-table-column label="发布部门" align="center" prop="publishDept" />
      <el-table-column label="版本号" align="center" prop="version" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 'active' ? 'success' : scope.row.status === 'draft' ? 'info' : 'danger'">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="浏览次数" align="center" prop="viewCount" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:regulation:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:regulation:remove']"
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

    <!-- 添加或修改物业制度管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row>
              <el-col :span="12">
                <el-form-item label="制度名称" prop="regulationName">
                  <el-input v-model="form.regulationName" placeholder="请输入制度名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="制度分类" prop="categoryId">
                  <el-select v-model="form.categoryId" placeholder="请选择制度分类" style="width: 100%;">
                    <el-option
                      v-for="category in categoryList"
                      :key="category.categoryId"
                      :label="category.categoryName"
                      :value="category.categoryId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="制度类型" prop="regulationType">
                  <el-input v-model="form.regulationType" placeholder="例如: 管理规约, 议事规则" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发布部门" prop="publishDept">
                  <el-input v-model="form.publishDept" placeholder="请输入发布部门" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="版本号" prop="version">
                  <el-input v-model="form.version" placeholder="例如: 1.0" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生效日期" prop="effectiveDate">
                  <el-date-picker clearable v-model="form.effectiveDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择生效日期" style="width: 100%;"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="状态" prop="status">
                  <el-radio-group v-model="form.status">
                    <el-radio label="draft">草稿</el-radio>
                    <el-radio label="active">生效中</el-radio>
                    <el-radio label="expired">已失效</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否重要" prop="isImportant">
                  <el-switch v-model="form.isImportant" :active-value="1" :inactive-value="0"></el-switch>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="制度摘要" prop="summary">
              <el-input v-model="form.summary" type="textarea" placeholder="请输入制度摘要" />
            </el-form-item>
          </el-tab-pane>

          <el-tab-pane label="制度内容" name="content">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="addChapter">添加章节</el-button>
            <div v-for="(chapter, chapterIndex) in form.chapters" :key="chapterIndex" class="chapter-block">
              <el-divider>章节 {{ chapterIndex + 1 }}</el-divider>
              <el-form-item :label="`章节标题`" :prop="`chapters.${chapterIndex}.title`" :rules="{ required: true, message: '章节标题不能为空', trigger: 'blur' }">
                <el-input v-model="chapter.title" placeholder="请输入章节标题">
                  <el-button slot="append" icon="el-icon-delete" @click="removeChapter(chapterIndex)"></el-button>
                </el-input>
              </el-form-item>
              <div v-for="(article, articleIndex) in chapter.articles" :key="articleIndex" class="article-block">
                <el-form-item :label="`条款标题`" :prop="`chapters.${chapterIndex}.articles.${articleIndex}.title`" :rules="{ required: true, message: '条款标题不能为空', trigger: 'blur' }">
                  <el-input v-model="article.title" placeholder="请输入条款标题">
                    <el-button slot="append" icon="el-icon-delete" @click="removeArticle(chapterIndex, articleIndex)"></el-button>
                  </el-input>
                </el-form-item>
                <el-form-item :label="`条款内容`" :prop="`chapters.${chapterIndex}.articles.${articleIndex}.content`">
                  <editor v-model="article.content" :min-height="192"/>
                </el-form-item>
                <el-form-item :label="`条款备注`" :prop="`chapters.${chapterIndex}.articles.${articleIndex}.note`">
                  <el-input v-model="article.note" placeholder="请输入条款备注" />
                </el-form-item>
              </div>
              <el-button type="success" icon="el-icon-plus" size="mini" plain @click="addArticle(chapterIndex)">添加条款</el-button>
            </div>
          </el-tab-pane>

          <el-tab-pane label="修订历史" name="history">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="addRevision">添加修订记录</el-button>
            <div v-for="(revision, index) in form.revisionHistory" :key="index" class="chapter-block">
              <el-divider>修订记录 {{ index + 1 }}</el-divider>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="版本号" :prop="`revisionHistory.${index}.version`" :rules="{ required: true, message: '版本号不能为空', trigger: 'blur' }">
                    <el-input v-model="revision.version" placeholder="请输入版本号"/>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="修订日期" :prop="`revisionHistory.${index}.date`" :rules="{ required: true, message: '修订日期不能为空', trigger: 'blur' }">
                    <el-date-picker clearable v-model="revision.date" type="date" value-format="yyyy-MM-dd" placeholder="请选择修订日期" style="width: 100%;"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="修订人/部门" :prop="`revisionHistory.${index}.author`">
                <el-input v-model="revision.author" placeholder="请输入修订人/部门"/>
              </el-form-item>
              <el-form-item label="修订描述" :prop="`revisionHistory.${index}.description`">
                <el-input v-model="revision.description" type="textarea" placeholder="请输入修订描述"/>
              </el-form-item>
              <el-button type="danger" icon="el-icon-delete" size="mini" plain @click="removeRevision(index)">删除此记录</el-button>
            </div>
          </el-tab-pane>

          <el-tab-pane label="文件与统计" name="file">
            <el-form-item label="上传文件" prop="filePath">
              <file-upload v-model="form.filePath"/>
            </el-form-item>
            <el-row>
              <el-col :span="12">
                <el-form-item label="文件大小(bytes)" prop="fileSize">
                  <el-input-number v-model="form.fileSize" controls-position="right" :min="0" style="width: 100%;"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="浏览次数">
                  <el-input :value="form.viewCount || 0" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="下载次数">
                  <el-input :value="form.downloadCount || 0" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="收藏次数">
                  <el-input :value="form.favoriteCount || 0" :disabled="true" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRegulation, getRegulation, delRegulation, addRegulation, updateRegulation } from "@/api/system/propertyRegulation";
import { listCategory } from "@/api/system/regulationCategory"; // 假设分类API已创建

export default {
  name: "PropertyRegulation",
  data() {
    return {
      // UI状态
      loading: true,
      open: false,
      showSearch: true,
      single: true,
      multiple: true,
      activeTab: 'basic',
      title: "",
      // 数据
      regulationList: [],
      categoryList: [],
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        regulationName: null,
        categoryId: null,
        regulationType: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        regulationName: [
          { required: true, message: "制度名称不能为空", trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: "制度分类不能为空", trigger: "change" }
        ],
        publishDept: [
          { required: true, message: "发布部门不能为空", trigger: "blur" }
        ],
        version: [
          { required: true, message: "版本号不能为空", trigger: "blur" }
        ],
        effectiveDate: [
          { required: true, message: "生效日期不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getCategoryList();
  },
  methods: {
    /** 查询制度列表 */
    getList() {
      this.loading = true;
      listRegulation(this.queryParams).then(response => {
        this.regulationList = response.rows.map(item => {
          const category = this.categoryList.find(c => c.categoryId === item.categoryId);
          item.categoryName = category ? category.categoryName : '未知';
          return item;
        });
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询分类列表 */
    getCategoryList(){
      listCategory().then(response => {
        this.categoryList = response.rows;
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
        regulationId: null,
        regulationName: null,
        categoryId: null,
        regulationType: null,
        summary: null,
        publishDept: null,
        isImportant: 0,
        chapters: [],
        revisionHistory: [],
        filePath: null,
        effectiveDate: null,
        version: null,
        status: "draft",
        viewCount: 0,
        downloadCount: 0,
        favoriteCount: 0,
        fileSize: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.activeTab = 'basic';
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
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.regulationId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加物业制度";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const regulationId = row.regulationId || this.ids[0];
      getRegulation(regulationId).then(response => {
        this.form = response.data;
        // 解析JSON字符串
        try {
          this.form.chapters = JSON.parse(response.data.chapters);
        } catch(e) {
          this.form.chapters = [];
        }
        try {
          this.form.revisionHistory = JSON.parse(response.data.revisionHistory);
        } catch(e) {
          this.form.revisionHistory = [];
        }
        this.open = true;
        this.title = "修改物业制度";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let formData = JSON.parse(JSON.stringify(this.form));
          // 转换JSON为字符串
          formData.chapters = JSON.stringify(formData.chapters);
          formData.revisionHistory = JSON.stringify(formData.revisionHistory);

          if (formData.regulationId != null) {
            updateRegulation(formData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRegulation(formData).then(response => {
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
      const regulationIds = row.regulationId || this.ids;
      this.$modal.confirm('是否确认删除制度ID为"' + regulationIds + '"的数据项？').then(function() {
        return delRegulation(regulationIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // 动态表单方法
    addChapter() {
      this.form.chapters.push({ title: '', articles: [] });
    },
    removeChapter(index) {
      this.form.chapters.splice(index, 1);
    },
    addArticle(chapterIndex) {
      this.form.chapters[chapterIndex].articles.push({ title: '', content: '', note: '' });
    },
    removeArticle(chapterIndex, articleIndex) {
      this.form.chapters[chapterIndex].articles.splice(articleIndex, 1);
    },
    addRevision() {
      this.form.revisionHistory.push({ version: '', date: '', author: '', description: '' });
    },
    removeRevision(index) {
      this.form.revisionHistory.splice(index, 1);
    }
  }
};
</script>

<style scoped>
.chapter-block {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #f0f0f0;
  border-radius: 5px;
}
.article-block {
  margin-left: 20px;
  margin-top: 15px;
  padding: 10px;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
}
</style>
