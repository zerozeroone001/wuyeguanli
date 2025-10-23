<template>
  <div class="app-container poll-management">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="征询标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入征询标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表单模板" prop="formId">
        <el-select
          v-model="queryParams.formId"
          placeholder="请选择表单模板"
          clearable
          filterable
          @change="handleQuery"
        >
          <el-option
            v-for="item in formTemplateOptions"
            :key="item.formId"
            :label="item.formName"
            :value="item.formId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker
          clearable
          v-model="queryParams.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始时间"
        />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker
          clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择结束时间"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:poll:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:poll:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:poll:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:poll:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-collection" size="mini" @click="openTemplateManager" v-hasPermi="['system:poll:edit']">模板管理</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="pollList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="征询ID" align="center" prop="pollId" width="100" />
      <el-table-column label="征询标题" align="center" prop="title" />
      <el-table-column label="征询描述" align="center" prop="description" />
      <el-table-column label="表单模板" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.formName || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="模板ID" align="center" prop="formId" width="120" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:poll:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit-outline" @click="handleDesign(scope.row)" v-hasPermi="['system:poll:edit']">设计表单</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleViewSubmissions(scope.row)" v-hasPermi="['system:poll:query']">提交记录</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:poll:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="520px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="征询标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入征询标题" />
        </el-form-item>
        <el-form-item label="征询描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请填写征询描述" />
        </el-form-item>
        <el-form-item label="表单模板" prop="formId">
          <el-select v-model="form.formId" placeholder="请选择表单模板" clearable filterable>
            <el-option
              v-for="item in formTemplateOptions"
              :key="item.formId"
              :label="item.formName"
              :value="item.formId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            clearable
            v-model="form.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开始时间"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            clearable
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结束时间"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="未开始" value="0" />
            <el-option label="进行中" value="1" />
            <el-option label="已结束" value="2" />
          </el-select>
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

    <el-dialog title="模板管理" :visible.sync="templateDialogVisible" width="720px" append-to-body>
      <div class="template-toolbar">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleTemplateAdd">新增模板</el-button>
      </div>
      <el-table v-loading="templateLoading" :data="templateList" border size="small">
        <el-table-column label="模板ID" prop="formId" align="center" width="100" />
        <el-table-column label="模板名称" prop="formName" align="center" />
        <el-table-column label="类型" prop="formType" align="center" width="120" />
        <el-table-column label="状态" prop="status" align="center" width="120">
          <template slot-scope="scope">
            <el-tag :type="templateStatusTag(scope.row.status)">{{ templateStatusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="160">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-edit" @click="handleTemplateEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" @click="handleTemplateDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog :title="templateFormTitle" :visible.sync="templateFormVisible" width="440px" append-to-body>
      <el-form ref="templateFormRef" :model="templateForm" :rules="templateRules" label-width="90px">
        <el-form-item label="模板名称" prop="formName">
          <el-input v-model="templateForm.formName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板类型" prop="formType">
          <el-select v-model="templateForm.formType" placeholder="请选择模板类型">
            <el-option label="问卷" value="SURVEY" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="templateForm.status" placeholder="请选择状态">
            <el-option label="正常" value="0" />
            <el-option label="停用" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="templateForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTemplateForm">确 定</el-button>
        <el-button @click="cancelTemplateForm">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 提交记录弹窗 -->
    <el-dialog title="提交记录" :visible.sync="submissionsDialogVisible" width="1000px" append-to-body>
      <div class="submissions-toolbar">
        <span class="submissions-title">{{ currentPollTitle }} - 提交记录</span>
        <el-button type="primary" icon="el-icon-refresh" size="mini" @click="loadSubmissions">刷新</el-button>
      </div>
      <el-table v-loading="submissionsLoading" :data="submissionsList" border size="small">
        <el-table-column label="提交ID" prop="submissionId" align="center" width="100" />
        <el-table-column label="用户ID" prop="userId" align="center" width="100" />
        <el-table-column label="提交终端" prop="clientType" align="center" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.clientType === 'pc' ? 'primary' : 'success'" size="mini">
              {{ scope.row.clientType === 'pc' ? 'PC端' : '移动端' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="提交IP" prop="submitIp" align="center" width="120" />
        <el-table-column label="状态" prop="status" align="center" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'" size="mini">
              {{ scope.row.status === '0' ? '有效' : '撤销' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" prop="submitTime" align="center" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.submitTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" align="center" />
        <el-table-column label="操作" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-view" @click="handleViewSubmissionDetail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="submissionsTotal > 0"
        :total="submissionsTotal"
        :page.sync="submissionsQueryParams.pageNum"
        :limit.sync="submissionsQueryParams.pageSize"
        @pagination="loadSubmissions"
      />
    </el-dialog>

    <!-- 提交记录详情弹窗 -->
    <el-dialog title="提交记录详情" :visible.sync="submissionDetailVisible" width="900px" append-to-body>
      <div v-if="currentSubmission">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="提交用户昵称">{{ currentSubmission.nickName || currentSubmission.userName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="手机号码">{{ currentSubmission.phonenumber || '-' }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ parseTime(currentSubmission.submitTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
          <el-descriptions-item label="所属小区门牌号">{{ formatPropertyAddress(currentSubmission) }}</el-descriptions-item>

          <el-descriptions-item label="状态">
            <el-tag :type="currentSubmission.status === '0' ? 'success' : 'danger'" size="mini">
              {{ currentSubmission.status === '0' ? '有效' : '撤销' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <div style="margin-top: 20px;">
          <h4>提交内容：</h4>
          <el-table :data="formatAnswersTable(currentSubmission.answersData)" border size="small" style="margin-top: 10px;">
            <el-table-column label="字段名称" prop="fieldName" width="200" />
            <el-table-column label="用户填写内容" prop="fieldValue" />
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPoll, getPoll, delPoll, addPoll, updatePoll, getPollSubmissions, getSubmissionDetail } from '@/api/system/poll'
import { listFormTemplateOptions, addFormTemplate, updateFormTemplate, delFormTemplate, getFormTemplate } from '@/api/system/formTemplate'

export default {
  name: 'Poll',
  data() {
    return {
      loading: false,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      pollList: [],
      formTemplateOptions: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        formId: null,
        startTime: null,
        endTime: null,
        status: null
      },
      form: {
        pollId: null,
        title: null,
        description: null,
        formId: null,
        startTime: null,
        endTime: null,
        status: '0',
        remark: null
      },
      rules: {
        title: [{ required: true, message: '征询标题不能为空', trigger: 'blur' }],
        formId: [{ required: true, message: '请选择表单模板', trigger: 'change' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      },
      templateDialogVisible: false,
      templateFormVisible: false,
      templateLoading: false,
      templateList: [],
      templateForm: {
        formId: null,
        formName: '',
        formType: 'SURVEY',
        status: '0',
        remark: ''
      },
      templateFormTitle: '新增模板',
      templateRules: {
        formName: [{ required: true, message: '模板名称不能为空', trigger: 'blur' }]
      },
      // 提交记录相关
      submissionsDialogVisible: false,
      submissionsLoading: false,
      submissionsList: [],
      submissionsTotal: 0,
      submissionsQueryParams: {
        pageNum: 1,
        pageSize: 10
      },
      currentPollId: null,
      currentPollTitle: '',
      submissionDetailVisible: false,
      currentSubmission: null
    }
  },
  created() {
    this.getList()
    this.loadFormTemplateOptions()
  },
  methods: {
    loadFormTemplateOptions() {
      listFormTemplateOptions({ status: '0' }).then(res => {
        this.formTemplateOptions = res?.data || []
      })
    },
    getList() {
      this.loading = true
      listPoll(this.queryParams).then(response => {
        this.pollList = response.rows || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        title: null,
        formId: null,
        startTime: null,
        endTime: null,
        status: null
      }
      this.getList()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.pollId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    resetFormData() {
      this.form = {
        pollId: null,
        title: null,
        description: null,
        formId: null,
        startTime: null,
        endTime: null,
        status: '0',
        remark: null
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    handleAdd() {
      this.resetFormData()
      this.open = true
      this.title = '添加意见征询'
    },
    handleUpdate(row) {
      const pollId = row?.pollId || this.ids[0]
      if (!pollId) {
        this.$message.warning('请选择需要修改的记录')
        return
      }
      this.resetFormData()
      getPoll(pollId).then(response => {
        this.form = Object.assign({}, response.data || {})
        this.form.status = this.form.status != null ? String(this.form.status) : '0'
        this.open = true
        this.title = '修改意见征询'
      })
    },
    cancel() {
      this.open = false
      this.resetFormData()
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        const payload = Object.assign({}, this.form)
        if (payload.pollId) {
          updatePoll(payload).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addPoll(payload).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const pollIds = row?.pollId ? [row.pollId] : this.ids
      if (!pollIds.length) {
        this.$modal.msgWarning('请选择要删除的记录')
        return
      }
      this.$modal.confirm(`是否确认删除编号为"${pollIds}"的问卷？`).then(() => delPoll(pollIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    statusLabel(status) {
      const map = { '0': '未开始', '1': '进行中', '2': '已结束' }
      return map[String(status)] || '未开始'
    },
    statusTagType(status) {
      const key = String(status)
      if (key === '1') return 'success'
      if (key === '2') return 'info'
      return 'warning'
    },
    defaultAppearanceConfig(poll) {
      return {
        showTitle: true,
        title: poll?.title || '在线问卷',
        showDescription: true,
        description: poll?.description || '',
        showSerialNumber: false,
        submitButtonText: '提交',
        submitButtonColor: '#1890FF'
      }
    },
    async handleDesign(row) {
      let poll = row
      if (!poll.formId) {
        try {
          const detailRes = await getPoll(row.pollId)
          if (detailRes && detailRes.data) {
            poll = detailRes.data
          }
        } catch (error) {}
        try {
          const createRes = await addFormTemplate({
            formName: (poll.title || '问卷表单') + '模板',
            formType: 'SURVEY',
            status: '0',
            formSchema: '[]',
            mobileSchema: '[]',
            formConfig: JSON.stringify({ labelWidth: 90 }),
            appearanceConfig: JSON.stringify(this.defaultAppearanceConfig(poll))
          })
          const newFormId = createRes?.data?.formId || createRes?.formId || createRes
          if (!newFormId) {
            this.$modal.msgError('创建表单模板失败')
            return
          }
          const payload = {
            pollId: poll.pollId,
            title: poll.title,
            description: poll.description,
            formId: newFormId,
            startTime: poll.startTime,
            endTime: poll.endTime,
            status: poll.status != null ? String(poll.status) : '0',
            remark: poll.remark
          }
          await updatePoll(payload)
          poll.formId = newFormId
          this.loadFormTemplateOptions()
          this.getList()
          this.$modal.msgSuccess('已创建并绑定新的表单模板')
        } catch (error) {
          this.$modal.msgError('创建表单模板失败，请重试')
          return
        }
      }
      this.$router.push({
        path: '/system/poll/designer',
        query: { pollId: poll.pollId, formId: poll.formId }
      })
    },
    handleExport() {
      this.download('system/poll/export', { ...this.queryParams }, `poll_${new Date().getTime()}.xlsx`)
    },
    openTemplateManager() {
      this.templateDialogVisible = true
      this.loadTemplateList()
    },
    loadTemplateList() {
      this.templateLoading = true
      listFormTemplateOptions().then(res => {
        this.templateList = res?.data || []
        this.templateLoading = false
      }).catch(() => {
        this.templateLoading = false
      })
    },
    resetTemplateForm() {
      this.templateForm = {
        formId: null,
        formName: '',
        formType: 'SURVEY',
        status: '0',
        remark: ''
      }
      if (this.$refs.templateFormRef) {
        this.$refs.templateFormRef.resetFields()
      }
    },
    handleTemplateAdd() {
      this.resetTemplateForm()
      this.templateFormTitle = '新增模板'
      this.templateFormVisible = true
    },
    handleTemplateEdit(row) {
      getFormTemplate(row.formId).then(res => {
        const data = res?.data || res
        this.templateForm = {
          formId: data.formId,
          formName: data.formName,
          formType: data.formType || 'SURVEY',
          status: data.status != null ? String(data.status) : '0',
          remark: data.remark || ''
        }
        this.templateFormTitle = '编辑模板'
        this.templateFormVisible = true
      })
    },
    handleTemplateDelete(row) {
      this.$modal.confirm(`确认删除模板“${row.formName}”吗？`).then(() => {
        return delFormTemplate(row.formId)
      }).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.loadTemplateList()
        this.loadFormTemplateOptions()
      }).catch(() => {})
    },
    submitTemplateForm() {
      this.$refs.templateFormRef.validate(valid => {
        if (!valid) {
          return
        }
        const payload = Object.assign({}, this.templateForm)
        const request = payload.formId ? updateFormTemplate(payload) : addFormTemplate(payload)
        request.then(() => {
          this.$modal.msgSuccess('保存成功')
          this.templateFormVisible = false
          this.loadTemplateList()
          this.loadFormTemplateOptions()
        })
      })
    },
    cancelTemplateForm() {
      this.templateFormVisible = false
      this.resetTemplateForm()
    },
    templateStatusLabel(status) {
      const map = { '0': '正常', '1': '停用' }
      return map[String(status)] || '正常'
    },
    templateStatusTag(status) {
      const key = String(status)
      if (key === '1') return 'info'
      return 'success'
    },
    // 提交记录相关方法
    handleViewSubmissions(row) {
      this.currentPollId = row.pollId
      this.currentPollTitle = row.title
      this.submissionsQueryParams.pageNum = 1
      this.submissionsDialogVisible = true
      this.loadSubmissions()
    },
    loadSubmissions() {
      this.submissionsLoading = true
      getPollSubmissions(this.currentPollId, this.submissionsQueryParams).then(response => {
        this.submissionsList = response.rows || []
        this.submissionsTotal = response.total || 0
        this.submissionsLoading = false
      }).catch(() => {
        this.submissionsLoading = false
      })
    },
    handleViewSubmissionDetail(row) {
      this.currentSubmission = row
      this.submissionDetailVisible = true
    },
    formatAnswersData(answersData) {
      if (!answersData) return '暂无提交内容'
      try {
        const data = JSON.parse(answersData)
        return JSON.stringify(data, null, 2)
      } catch (e) {
        return answersData
      }
    },
    formatPropertyAddress(submission) {
      if (!submission) return '-'
      const parts = []
      if (submission.communityName) parts.push(submission.communityName)
      if (submission.buildingName) parts.push(submission.buildingName)
      if (submission.unitName) parts.push(submission.unitName)
      if (submission.roomNumber) parts.push(submission.roomNumber)
      return parts.length > 0 ? parts.join(' ') : '-'
    },
    formatAnswersTable(answersData) {
      if (!answersData) return []
      try {
        const data = JSON.parse(answersData)
        const tableData = []

        // 如果是对象，转换为表格数据
        if (typeof data === 'object' && data !== null) {
          for (const [key, value] of Object.entries(data)) {
            tableData.push({
              fieldName: key,
              fieldValue: this.formatFieldValue(value)
            })
          }
        }

        return tableData
      } catch (e) {
        return [{
          fieldName: '原始数据',
          fieldValue: answersData
        }]
      }
    },
    formatFieldValue(value) {
      if (value === null || value === undefined) return '-'
      if (typeof value === 'object') {
        return JSON.stringify(value, null, 2)
      }
      if (Array.isArray(value)) {
        return value.join(', ')
      }
      return String(value)
    }
  }
}
</script>

<style scoped>
.poll-management {
  background: #fff;
  padding-bottom: 10px;
}

.poll-management .mb8 {
  margin-bottom: 15px;
}

.poll-management .el-table {
  margin-top: 10px;
}

.template-toolbar {
  margin-bottom: 12px;
  text-align: right;
}

.submissions-toolbar {
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.submissions-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
</style>
