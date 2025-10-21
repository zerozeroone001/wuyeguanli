<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="会议标题" prop="meetingTitle">
        <el-input
          v-model="queryParams.meetingTitle"
          placeholder="请输入会议标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会议时间" prop="meetingTime">
        <el-date-picker clearable
                        v-model="queryParams.meetingTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择会议时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="会议地点" prop="meetingLocation">
        <el-input
          v-model="queryParams.meetingLocation"
          placeholder="请输入会议地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票开始时间" label-width="120px" prop="voteStartTime">
        <el-date-picker clearable
                        v-model="queryParams.voteStartTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择投票开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="投票结束时间" label-width="120px" prop="voteEndTime">
        <el-date-picker clearable
                        v-model="queryParams.voteEndTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择投票结束时间">
        </el-date-picker>
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
          v-hasPermi="['system:meeting:add']"
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
          v-hasPermi="['system:meeting:edit']"
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
          v-hasPermi="['system:meeting:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:meeting:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="meetingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会议ID" align="center" prop="meetingId" />
      <el-table-column label="会议标题" align="center" prop="meetingTitle" />
      <el-table-column label="会议类型" align="center" prop="meetingType" >
        <template slot-scope="scope">
          <span>{{ scope.row.meetingType === 1 ? '业主大会' : '业主委员会' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会议内容" align="center" prop="meetingContent" />
      <el-table-column label="会议时间" align="center" prop="meetingTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.meetingTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会议地点" align="center" prop="meetingLocation" />
      <el-table-column label="会议状态" align="center" prop="meetingStatus">
        <template slot-scope="scope">
          <span v-if="scope.row.meetingStatus === '0'">筹备中</span>
          <span v-else-if="scope.row.meetingStatus === '1'">进行中</span>
          <span v-else-if="scope.row.meetingStatus === '2'">已结束</span>
        </template>
      </el-table-column>
      <el-table-column label="投票开始时间" align="center" prop="voteStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.voteStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="投票结束时间" align="center" prop="voteEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.voteEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应参与人数" align="center" prop="totalVoters" />
      <el-table-column label="实际参与人数" align="center" prop="actualVoters" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewTopics(scope.row)"
          >查看议题</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:meeting:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:meeting:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.meetingStatus == '1'"
            size="mini"
            type="text"
            icon="el-icon-s-promotion"
            @click="handleSendNotification(scope.row)"
          >发送通知</el-button>
          <el-button
            v-if="scope.row.meetingStatus == '1'"
            size="mini"
            type="text"
            icon="el-icon-upload2"
            @click="handleImportVotes(scope.row)"
            v-hasPermi="['system:meeting:import']"
          >导入投票</el-button>
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

    <!-- 添加或修改会议管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会议标题" prop="meetingTitle">
          <el-input v-model="form.meetingTitle" placeholder="请输入会议标题" />
        </el-form-item>
        <el-form-item label="所属小区" prop="communityId">
          <!-- 仅展示所属小区名称，真实的 communityId 通过隐藏字段提交 -->
          <el-input v-model="form.communityName" placeholder="请先在右上角选择小区" disabled />
          <input type="hidden" v-model="form.communityId" />
        </el-form-item>
<!--        <el-form-item label="会议类型" prop="meetingType">-->
<!--          <el-select v-model="form.meetingType" placeholder="请选择会议类型" style="width:100%">-->
<!--            &lt;!&ndash; TODO: 此处应改为使用字典管理动态获取 &ndash;&gt;-->
<!--            <el-option label="业主大会" value="1"></el-option>-->
<!--            <el-option label="业委会会议" value="2"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item label="会议内容">
          <editor v-model="form.meetingContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="会议时间" prop="meetingTime">
          <el-date-picker clearable
                          v-model="form.meetingTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择会议时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="会议地点" prop="meetingLocation">
          <el-input v-model="form.meetingLocation" placeholder="请输入会议地点" />
        </el-form-item>
        <el-form-item label="投票开始时间" prop="voteStartTime">
          <el-date-picker clearable
                          v-model="form.voteStartTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择投票开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="投票结束时间" prop="voteEndTime">
          <el-date-picker clearable
                          v-model="form.voteEndTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择投票结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="应参与人数" prop="totalVoters">
          <el-input v-model="form.totalVoters" placeholder="请输入应参与人数" />
        </el-form-item>
        <el-form-item label="实际参与人数" prop="actualVoters">
          <el-input v-model="form.actualVoters" placeholder="请输入实际参与人数" />
        </el-form-item>

        <el-divider content-position="center">议题管理</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddTopic">添加议题</el-button>
          </el-col>
        </el-row>
        <el-table :data="form.topics">
          <el-table-column label="议题标题" align="center" prop="topicTitle" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateTopic(scope.row)">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteTopic(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改议题对话框 -->
    <el-dialog :title="topicTitle" :visible.sync="topicOpen" width="1200px" append-to-body>
      <el-form ref="topicForm" :model="topicForm" :rules="topicRules" label-width="100px">
        <el-form-item label="议题标题" prop="topicTitle">
          <el-input v-model="topicForm.topicTitle" placeholder="请输入议题标题" />
        </el-form-item>
        <el-form-item label="议题内容">
          <editor v-model="topicForm.topicContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="附件">
          <file-upload v-model="topicForm.files"/>
        </el-form-item>
        <el-row>
          <el-col :span="8">
            <el-form-item label="同意票数" prop="agreeCount">
              <el-input-number v-model="topicForm.agreeCount" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="反对票数" prop="opposeCount">
              <el-input-number v-model="topicForm.opposeCount" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="弃权票数" prop="abstainCount">
              <el-input-number v-model="topicForm.abstainCount" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTopicForm">确 定</el-button>
        <el-button @click="cancelTopic">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 议题列表对话框 -->
    <el-dialog title="议题列表" :visible.sync="topicListDialogVisible" width="60%" append-to-body>
      <el-table :data="currentMeetingTopics" border>
        <el-table-column label="议题标题" prop="topicTitle" align="center"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleViewVotes(scope.row)">投票列表</el-button>
            <el-button size="mini" type="text" @click="handleViewFeedbacks(scope.row)">意见反馈</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 投票列表对话框 -->
    <el-dialog title="投票列表" :visible.sync="voteListDialogVisible" width="50%" append-to-body>
      <el-table :data="currentTopicVotes" border>
        <el-table-column label="投票人" prop="userName" align="center"></el-table-column>
        <el-table-column label="投票选项" prop="voteOption" align="center">
          <template slot-scope="scope">
            <span>{{ formatVoteOption(scope.row.voteOption) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="投票时间" prop="createTime" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 意见反馈列表对话框 -->
    <el-dialog title="意见反馈列表" :visible.sync="feedbackListDialogVisible" width="50%" append-to-body>
      <el-table :data="currentTopicFeedbacks" border>
        <el-table-column label="反馈人" prop="userName" align="center"></el-table-column>
        <el-table-column label="反馈内容" prop="content"></el-table-column>
        <el-table-column label="反馈时间" prop="createTime" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 导入投票对话框 -->
    <el-dialog
      title="导入投票"
      :visible.sync="importVoteDialogVisible"
      width="60%"
      append-to-body
      :close-on-click-modal="false">
      <div class="import-vote-content">
        <el-steps :active="importVoteForm.isUploading ? 1 : 0" finish-status="success">
          <el-step title="选择文件" description="选择投票表图片文件"></el-step>
          <el-step title="上传处理" description="上传并识别投票数据"></el-step>
          <el-step title="完成导入" description="保存投票结果"></el-step>
        </el-steps>

        <div class="file-upload-section" style="margin-top: 20px;">
          <el-alert
            title="使用说明"
            type="info"
            :closable="false"
            style="margin-bottom: 15px;">
            <div>
              <p>• <strong>选择文件夹</strong>：批量导入整个文件夹内的所有投票图片</p>
              <p>• <strong>选择文件</strong>：手动选择多个投票图片文件</p>
              <p>• 支持格式：JPG、PNG、JPEG、BMP、GIF</p>
              <p>• 文件限制：单个文件不超过10MB，总计不超过100MB</p>
              <p>• 系统将自动识别投票内容并导入数据库</p>
            </div>
          </el-alert>

          <div class="upload-area">
            <input
              type="file"
              ref="fileInput"
              multiple
              accept="image/*"
              webkitdirectory
              @change="handleFileChange"
              style="display: none;">

            <input
              type="file"
              ref="singleFileInput"
              multiple
              accept="image/*"
              @change="handleFileChange"
              style="display: none;">

            <el-button-group>
              <el-button
                type="primary"
                icon="el-icon-folder-opened"
                @click="selectFolder"
                :disabled="importVoteForm.isUploading">
                选择文件夹
              </el-button>
              <el-button
                type="primary"
                plain
                icon="el-icon-document"
                @click="selectFiles"
                :disabled="importVoteForm.isUploading">
                选择文件
              </el-button>
            </el-button-group>

            <div v-if="importVoteForm.files.length > 0" class="file-list">
              <p>已选择 {{ importVoteForm.files.length }} 个文件：</p>
              <ul>
                <li v-for="(file, index) in importVoteForm.files" :key="index">
                  {{ file.name }} ({{ (file.size / 1024 / 1024).toFixed(2) }}MB)
                </li>
              </ul>
            </div>
          </div>

          <div v-if="importVoteForm.isUploading" class="upload-progress">
            <p>正在处理中，请稍候...</p>
            <el-progress
              :percentage="importVoteForm.uploadProgress"
              :stroke-width="10"
              status="active">
            </el-progress>
          </div>

          <div v-if="importVoteForm.processResults.length > 0" class="process-results">
            <h4>处理结果：</h4>
            <el-table :data="importVoteForm.processResults" border size="small">
              <el-table-column label="文件名" prop="fileName" width="200"></el-table-column>
              <el-table-column label="文件大小" width="100">
                <template slot-scope="scope">
                  {{ formatFileSize(scope.row.fileSize) }}
                </template>
              </el-table-column>
              <el-table-column label="状态" width="80">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.success ? 'success' : 'danger'">
                    {{ scope.row.success ? '成功' : '失败' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="投票记录数" width="100">
                <template slot-scope="scope">
                  {{ scope.row.voteRecords ? scope.row.voteRecords.length : 0 }}
                </template>
              </el-table-column>
              <el-table-column label="业主信息" width="150">
                <template slot-scope="scope">
                  <span v-if="scope.row.ownerInfo">
                    {{ scope.row.ownerInfo.ownerName }}<br/>
                    {{ scope.row.ownerInfo.phoneNumber }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="处理结果" prop="message"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeImportVoteDialog">取消</el-button>
        <el-button
          type="primary"
          @click="startImportVotes"
          :loading="importVoteForm.isUploading"
          :disabled="importVoteForm.files.length === 0">
          {{ importVoteForm.isUploading ? '处理中...' : '开始导入' }}
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listMeeting, getMeeting, delMeeting, addMeeting, updateMeeting, sendNotification } from "@/api/system/meeting"
import { importVotes } from "@/api/system/voteImport"
import { getToken } from "@/utils/auth"
import { mapGetters } from "vuex"
import axios from 'axios'

export default {
  name: "Meeting",
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
      // 会议管理表格数据
      meetingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        meetingTitle: null,
        meetingType: '2',
        communityId: null,
        meetingContent: null,
        meetingTime: null,
        meetingLocation: null,
        meetingStatus: null,
        voteStartTime: null,
        voteEndTime: null,
        totalVoters: null,
        actualVoters: null,
      },
      // 表单参数
      form: {
        meetingType:'2',
        communityId: null,
        communityName: ''
      },
      // 表单校验
      rules: {
        meetingTitle: [
          { required: true, message: "会议标题不能为空", trigger: "blur" }
        ],
        meetingType: [
          { required: true, message: "会议类型不能为空", trigger: "change" }
        ],
        communityId: [
          { required: true, message: "所属小区不能为空", trigger: "change" }
        ],
        meetingTime: [
          { required: true, message: "会议时间不能为空", trigger: "blur" }
        ],
      },
      // 议题弹出层标题
      topicTitle: "",
      // 是否显示议题弹出层
      topicOpen: false,
      // 议题表单参数
      topicForm: {},
      // 议题表单校验
      topicRules: {
        topicTitle: [
          { required: true, message: "议题标题不能为空", trigger: "blur" }
        ]
      },
      // 议题列表对话框
      topicListDialogVisible: false,
      currentMeetingTopics: [],
      // 投票列表对话框
      voteListDialogVisible: false,
      currentTopicVotes: [],
      // 意见反馈列表对话框
      feedbackListDialogVisible: false,
      currentTopicFeedbacks: [],
      // 导入投票对话框
      importVoteDialogVisible: false,
      currentMeeting: {},
      importVoteForm: {
        files: [],
        uploadProgress: 0,
        isUploading: false,
        processResults: []
      }
    }
  },
  computed: {
    ...mapGetters([
      "currentCommunityId",
      "currentCommunityName",
      "communityOptions"
    ]),
    /**
     * 判断是否选择了“全部小区”。
     * 约定：null、undefined、0 都视为未限定具体小区。
     */
    isAllCommunitySelected() {
      const value = this.currentCommunityId
      if (value === null || value === undefined) {
        return true
      }
      return Number(value) === 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /**
     * 构建带有小区过滤条件的查询参数。
     * 当顶部选择的是具体小区时追加 communityId，否则移除该字段。
     */
    buildQueryParams() {
      const params = { ...this.queryParams }
      if (this.isAllCommunitySelected) {
        this.queryParams.communityId = null
        delete params.communityId
      } else {
        const numericId = Number(this.currentCommunityId)
        this.queryParams.communityId = numericId
        params.communityId = numericId
      }
      return params
    },
    /**
     * 根据 currentCommunityId 预填表单中的小区信息。
     * 仅在选择了具体小区时写入 ID 与名称，避免提交缺失字段。
     */
    applyCurrentCommunityToForm() {
      if (this.isAllCommunitySelected) {
        this.form.communityId = null
        this.form.communityName = ""
        return
      }
      const numericId = Number(this.currentCommunityId)
      this.form.communityId = numericId
      this.form.communityName = this.resolveCommunityName(numericId)
    },
    /**
     * 根据小区 ID 返回对应的小区名称。
     * 先匹配缓存的下拉列表，再退回当前小区名称，避免额外的网络请求。
     */
    resolveCommunityName(communityId) {
      if (communityId === null || communityId === undefined) {
        return ""
      }
      const numericId = Number(communityId)
      const options = Array.isArray(this.communityOptions)
        ? this.communityOptions
        : []
      const matched = options.find(
        item => Number(item.id) === numericId
      )
      if (matched && matched.name) {
        return matched.name
      }
      if (
        this.currentCommunityId !== null &&
        this.currentCommunityId !== undefined &&
        Number(this.currentCommunityId) === numericId
      ) {
        return this.currentCommunityName || ""
      }
      return ""
    },
    /** 查询会议管理列表 */
    getList() {
      this.loading = true
      const params = this.buildQueryParams()
      listMeeting(params).then(response => {
        this.meetingList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        meetingId: null,
        meetingTitle: null,
        meetingType: '2',
        communityId: null,
        communityName: '',
        meetingContent: null,
        meetingTime: null,
        meetingLocation: null,
        meetingStatus: 1,
        voteStartTime: null,
        voteEndTime: null,
        totalVoters: null,
        actualVoters: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        topics: []
      }
      this.applyCurrentCommunityToForm()
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.meetingId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      if (this.isAllCommunitySelected) {
        this.$message.warning("请先在右上角选择具体小区，再新增会议。")
        return
      }
      this.reset()
      this.open = true
      this.title = "添加会议管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const meetingId = row.meetingId || this.ids
      getMeeting(meetingId).then(response => {
        const numericId =
          response.data.communityId !== null && response.data.communityId !== undefined
            ? Number(response.data.communityId)
            : null
        this.form = {
          ...response.data,
          communityId: numericId,
          communityName: this.resolveCommunityName(numericId)
        }
        this.open = true
        this.title = "修改会议管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (
            (this.form.communityId === null || this.form.communityId === undefined) &&
            !this.isAllCommunitySelected
          ) {
            // 兜底填充过滤条件对应的小区 ID，避免遗漏。
            this.form.communityId = Number(this.currentCommunityId)
          }
          if (
            this.form.communityId === null ||
            this.form.communityId === undefined ||
            Number(this.form.communityId) === 0
          ) {
            this.$message.error("当前记录缺少小区信息，请选择具体小区后再提交。")
            return
          }
          if (this.form.meetingId != null) {
            updateMeeting(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMeeting(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const meetingIds = row.meetingId || this.ids;
      this.$modal.confirm('是否确认删除会议管理编号为"' + meetingIds + '"的数据项？').then(function() {
        return delMeeting(meetingIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 发送通知按钮操作 */
    handleSendNotification(row) {
      this.$modal.confirm('是否确认发送【' + row.meetingTitle + '】会议的订阅消息通知？').then(function() {
        return sendNotification(row.meetingId);
      }).then(() => {
        this.$modal.msgSuccess("发送成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const params = this.buildQueryParams()
      this.download('system/meeting/export', params, `meeting_${new Date().getTime()}.xlsx`)
    },
    /** 查看议题按钮操作 */
    handleViewTopics(row) {
      getMeeting(row.meetingId).then(response => {
        this.currentMeetingTopics = response.data.topics || [];
        this.topicListDialogVisible = true;
      });
    },
    /** 查看投票列表按钮操作 */
    handleViewVotes(topic) {
      this.currentTopicVotes = topic.voteList || [];
      this.voteListDialogVisible = true;
    },
    /** 查看意见反馈列表按钮操作 */
    handleViewFeedbacks(topic) {
      this.currentTopicFeedbacks = topic.feedbackList || [];
      this.feedbackListDialogVisible = true;
    },
    /** 格式化投票选项 */
    formatVoteOption(option) {
      if (option === 0) return '同意';
      if (option === 1) return '反对';
      if (option === 2) return '弃权';
      return '未知';
    },
    // 议题表单重置
    resetTopic() {
      this.topicForm = {
        topicId: null,
        topicTitle: null,
        topicContent: null,
        files: null,
        agreeCount: 0,
        opposeCount: 0,
        abstainCount: 0
      };
      this.resetForm("topicForm");
    },
    // 取消议题按钮
    cancelTopic() {
      this.topicOpen = false;
      this.resetTopic();
    },
    /** 新增议题按钮操作 */
    handleAddTopic() {
      this.resetTopic();
      this.topicOpen = true;
      this.topicTitle = "添加议题";
    },
    /** 修改议题按钮操作 */
    handleUpdateTopic(row) {
      this.resetTopic();
      // 注意：这里需要深拷贝，以防在表单中修改但未提交时，列表中的数据也跟着变
      this.topicForm = JSON.parse(JSON.stringify(row));
      this.topicOpen = true;
      this.topicTitle = "修改议题";
    },
    /** 删除议题按钮操作 */
    handleDeleteTopic(row) {
      const topics = this.form.topics;
      const index = topics.findIndex(t => t.topicId === row.topicId);
      if (index !== -1) {
        topics.splice(index, 1);
      }
    },
    /** 提交议题按钮 */
    submitTopicForm() {
      this.$refs["topicForm"].validate(valid => {
        if (valid) {
          // 克隆一个副本，而不是直接用引用
          const topicData = { ...this.topicForm };
          // 如果是修改
          if (topicData.topicId != null) {
            const index = this.form.topics.findIndex(t => t.topicId === topicData.topicId);
            if (index !== -1) {
              // 使用 $set 确保视图更新
              this.$set(this.form.topics, index, topicData);
            }
          } else {
            // 如果是新增
            topicData.topicId = new Date().getTime(); // 分配一个临时的唯一ID
            this.form.topics.push(topicData);
          }
          this.topicOpen = false;
          this.resetTopic();
        }
      });
    },
    /** 导入投票按钮操作 */
    handleImportVotes(row) {
      this.currentMeeting = row;
      this.importVoteDialogVisible = true;
      this.resetImportVoteForm();
    },
    /** 重置导入投票表单 */
    resetImportVoteForm() {
      this.importVoteForm = {
        files: [],
        uploadProgress: 0,
        isUploading: false,
        processResults: []
      };
    },
    /** 选择文件夹 */
    selectFolder() {
      this.$refs.fileInput.click();
    },
    /** 选择文件 */
    selectFiles() {
      this.$refs.singleFileInput.click();
    },
    /** 文件选择处理 */
    handleFileChange(event) {
      const files = Array.from(event.target.files);
      if (files.length === 0) {
        return;
      }

      // 验证文件类型
      const validFiles = files.filter(file => {
        const isImage = file.type.startsWith('image/');
        if (!isImage) {
          this.$message.warning(`文件 ${file.name} 不是图片格式，已跳过`);
        }
        return isImage;
      });

      this.importVoteForm.files = validFiles;

      // 显示选择结果
      const isFolder = event.target.hasAttribute('webkitdirectory');
      const message = isFolder
        ? `已从文件夹选择 ${validFiles.length} 个图片文件`
        : `已选择 ${validFiles.length} 个图片文件`;

      this.$message.success(message);

      // 重置input值，以便可以重新选择相同文件夹
      event.target.value = '';
    },
    /** 开始导入投票 */
    async startImportVotes() {
      if (this.importVoteForm.files.length === 0) {
        this.$message.warning('请先选择投票图片文件');
        return;
      }

      this.importVoteForm.isUploading = true;
      this.importVoteForm.uploadProgress = 0;
      this.importVoteForm.processResults = [];

      try {
        const formData = new FormData();
        formData.append('meetingId', this.currentMeeting.meetingId);

        // 添加所有文件
        this.importVoteForm.files.forEach((file, index) => {
          formData.append('files', file);
        });

        // 调用后端接口（使用原生axios支持上传进度）
        const response = await axios.post(
          `${process.env.VUE_APP_BASE_API}/system/vote/import/${this.currentMeeting.meetingId}`,
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Authorization': 'Bearer ' + getToken()
            },
            onUploadProgress: (progressEvent) => {
              this.importVoteForm.uploadProgress = Math.round(
                (progressEvent.loaded * 100) / progressEvent.total
              );
            }
          }
        );

        if (response.data.code === 200) {
          this.importVoteForm.processResults = response.data.data.results || [];
          this.$message.success(response.data.data.message || '投票导入完成');
          this.getList(); // 刷新列表

          // 显示导入摘要
          if (response.data.data.summary) {
            const summary = response.data.data.summary;
            this.$notify({
              title: '导入完成',
              message: `成功处理 ${summary.successFiles} 个文件，失败 ${summary.failedFiles} 个文件，共导入 ${summary.totalVoteRecords} 条投票记录`,
              type: 'success',
              duration: 5000
            });
          }
        } else {
          this.$message.error(response.data.msg || '导入失败');
        }
      } catch (error) {
        console.error('导入投票失败:', error);
        this.$message.error('导入投票失败，请重试');
      } finally {
        this.importVoteForm.isUploading = false;
      }
    },
    /** 关闭导入投票对话框 */
    closeImportVoteDialog() {
      this.importVoteDialogVisible = false;
      this.resetImportVoteForm();
    },
    /** 格式化文件大小 */
    formatFileSize(size) {
      if (!size || size <= 0) {
        return '0 B';
      }

      const units = ['B', 'KB', 'MB', 'GB'];
      let fileSize = size;
      let unitIndex = 0;

      while (fileSize >= 1024 && unitIndex < units.length - 1) {
        fileSize /= 1024;
        unitIndex++;
      }

      return `${fileSize.toFixed(2)} ${units[unitIndex]}`;
    }
  }
}
</script>

<style scoped>
.import-vote-content {
  padding: 10px 0;
}

.upload-area {
  text-align: center;
  padding: 20px;
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  background-color: #fafafa;
  transition: border-color 0.3s;
}

.upload-area:hover {
  border-color: #409EFF;
}

.file-list {
  margin-top: 15px;
  text-align: left;
}

.file-list ul {
  list-style-type: none;
  padding-left: 0;
}

.file-list li {
  padding: 5px 0;
  border-bottom: 1px solid #eee;
}

.upload-progress {
  margin-top: 20px;
  text-align: center;
}

.process-results {
  margin-top: 20px;
}

.process-results h4 {
  margin-bottom: 10px;
  color: #333;
}
</style>
