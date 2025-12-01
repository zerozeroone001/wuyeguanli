<template>
  <div class="app-container meeting-container">
    <!-- 顶部搜索栏和操作按钮 -->
    <div class="top-bar">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" class="search-form">
        <el-form-item>
          <el-input
            v-model="queryParams.meetingTitle"
            placeholder="请输入小区名称"
            clearable
            prefix-icon="el-icon-search"
            style="width: 250px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="queryParams.meetingLocation"
            placeholder="请输入小区"
            clearable
            style="width: 200px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>

      <div class="action-buttons">
        <el-button icon="el-icon-picture" size="small" plain>待发布</el-button>
        <el-button icon="el-icon-edit" size="small" plain>未开始</el-button>
        <el-button icon="el-icon-clock" size="small" plain>进行中</el-button>
        <el-button icon="el-icon-check" size="small" plain>已结束</el-button>
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd" v-hasPermi="['system:meeting:add']">新增</el-button>
      </div>
    </div>


    <!-- 会议列表卡片 - 每行一个 -->
    <div v-loading="loading" class="meeting-list">
      <el-card class="meeting-card-row" shadow="hover" v-for="meeting in meetingList" :key="meeting.meetingId">
        <div class="card-row-content">
          <!-- 左侧图片 -->
          <div class="card-left">
            <div class="meeting-image-wrapper">
              <img :src="meeting.coverImage || ''"
                   class="meeting-image"
                   @error="handleImageError">
              <div class="meeting-status-badge" :class="getMeetingStatusClass(meeting)">
                {{ getMeetingStatusText(meeting) }}
              </div>
            </div>
          </div>

          <!-- 中间内容 -->
          <div class="card-middle">
            <div class="meeting-header">
              <div class="meeting-title">{{ meeting.meetingTitle }}</div>
            </div>

            <div class="meeting-info">
              <div class="info-row">
                <div class="info-item">
                  <i class="el-icon-office-building"></i>
                  <span>归属小区：{{ meeting.communityName || '未知小区' }}</span>
                </div>
                <div class="info-item">
                  <span class="label">投票权总票数：</span>
                  <span class="value">{{ meeting.totalVoters || 0 }} 票</span>
                </div>
              </div>

              <div class="info-row">
                <div class="info-item">
                  <i class="el-icon-time"></i>
                  <span>活动时间：{{ parseTime(meeting.voteStartTime, '{y}-{m}-{d} {h}:{i}') }} - {{ parseTime(meeting.voteEndTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                </div>
                <div class="info-item">
                  <span class="label">投票权总面积：</span>
                  <span class="value">{{ meeting.totalVotingArea || 0 }} m²</span>
                </div>
              </div>

              <div class="info-row">
                <div class="info-item">
                  <i class="el-icon-location"></i>
                  <span>会议地点：{{ meeting.meetingLocation || '线上/待定' }}</span>
                </div>
                <div class="info-item">
                  <span class="label">投票率：</span>
                  <span class="value">{{ calculateParticipationRate(meeting) }}</span>
                </div>
              </div>

              <div class="info-row">
                <div class="info-item">
                  <span class="label">投票进度：</span>
                  <span class="value">{{ getMeetingStatusText(meeting) }}</span>
                </div>
                <div class="info-item">
                  <span class="label">投票面积占比：</span>
                  <span class="value">{{ meeting.votingAreaPercentage || '0%' }}</span>
                </div>
              </div>
            </div>
          </div>


          <!-- 右侧操作按钮 -->
          <div class="card-right">
            <!-- 第一层：图标按钮 -->
            <div class="icon-buttons-row">
              <div class="icon-btn-item" @click.stop="handleUpdate(meeting)" title="修改会议" v-hasPermi="['system:meeting:edit']">
                <i class="el-icon-edit"></i>
              </div>
              <div class="icon-btn-item" @click.stop="handleSmsNotify(meeting)" title="短信通知/投票">
                <i class="el-icon-message"></i>
              </div>
              <div class="icon-btn-item" @click.stop="handleViewVoteRecords(meeting)" title="投票记录">
                <i class="el-icon-document"></i>
              </div>
              <div class="icon-btn-item" @click.stop="handleViewVoteResults(meeting)" title="表决结果">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="icon-btn-item" @click.stop="handleExportResults(meeting)" title="结果导出">
                <i class="el-icon-download"></i>
              </div>
              <div class="icon-btn-item" @click.stop="handleDelete(meeting)" title="删除会议" v-hasPermi="['system:meeting:remove']">
                <i class="el-icon-delete"></i>
              </div>
            </div>

            <!-- 第二层：文字按钮 -->
            <div class="text-buttons-row">
              <div class="text-btn-item" @click.stop="handleViewTopics(meeting)">
                <span>活动管理</span>
              </div>
              <div class="text-btn-item" @click.stop="handleVoteManagement(meeting)">
                <span>投票/票权</span>
              </div>
              <div class="text-btn-item" @click.stop="handleVoteCount(meeting)">
                <span>唱票/统计</span>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改会议管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body custom-class="meeting-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="meeting-form">

        <div class="form-section-title">基本信息</div>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="会议标题" prop="meetingTitle">
              <el-input v-model="form.meetingTitle" placeholder="请输入会议标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属小区" prop="communityId">
              <el-input v-model="form.communityName" placeholder="请先在右上角选择小区" disabled />
              <input type="hidden" v-model="form.communityId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会议地点" prop="meetingLocation">
              <el-input v-model="form.meetingLocation" placeholder="请输入会议地点" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会议时间" prop="meetingTime">
              <el-date-picker clearable
                              v-model="form.meetingTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择会议时间"
                              style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="缩略图" prop="coverImage">
              <image-upload v-model="form.coverImage" :limit="1"/>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">投票设置</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="投票开始" prop="voteStartTime">
              <el-date-picker clearable
                              v-model="form.voteStartTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="选择开始时间"
                              style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投票结束" prop="voteEndTime">
              <el-date-picker clearable
                              v-model="form.voteEndTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="选择结束时间"
                              style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>

        </el-row>

        <div class="form-section-title">详细内容</div>
        <el-form-item label="会议内容" prop="meetingContent">
          <editor v-model="form.meetingContent" :min-height="192"/>
        </el-form-item>

        <div class="form-section-title">
          <span>议题管理</span>
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddTopic" style="float: right">添加议题</el-button>
        </div>
        <el-table :data="form.topics" border style="margin-bottom: 20px">
          <el-table-column label="议题标题" align="center" prop="topicTitle" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateTopic(scope.row)">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" class="text-danger" @click="handleDeleteTopic(scope.row)">删除</el-button>
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
<!--        <el-row>-->
<!--          <el-col :span="8">-->
<!--            <el-form-item label="同意票数" prop="agreeCount">-->
<!--              <el-input-number v-model="topicForm.agreeCount" controls-position="right" :min="0" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="8">-->
<!--            <el-form-item label="反对票数" prop="opposeCount">-->
<!--              <el-input-number v-model="topicForm.opposeCount" controls-position="right" :min="0" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="8">-->
<!--            <el-form-item label="弃权票数" prop="abstainCount">-->
<!--              <el-input-number v-model="topicForm.abstainCount" controls-position="right" :min="0" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
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

  </div>
</template>

<script>
import { listMeeting, getMeeting, delMeeting, addMeeting, updateMeeting } from "@/api/system/meeting"
import { exportVoteListExcel, exportVoteReportPdf } from '@/api/system/voteResults'
import { mapGetters } from "vuex"

export default {
  name: "Meeting",
  data() {
    return {
      // 当前激活的标签页
      activeTab: 'all',
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
        meetingType: 1,
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
        meetingType:'1',
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
    }
  },
  computed: {
    ...mapGetters([
      "currentCommunityId",
      "currentCommunityName",
      "communityOptions"
    ]),
    /**
     * 判断是否处于“全部小区”模式。
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
     * 组装带小区筛选条件的查询参数。
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
     * 将当前选择的小区写入表单，用于新增时直接提交。
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
     * 根据小区 ID 返回名称，优先使用缓存的下拉数据。
     */
    resolveCommunityName(communityId) {
      if (communityId === null || communityId === undefined) {
        return ""
      }
      const numericId = Number(communityId)
      const options = Array.isArray(this.communityOptions)
        ? this.communityOptions
        : []
      const matched = options.find(item => Number(item.id) === numericId)
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
        meetingType: 1,
        communityId: null,
        communityName: '',
        coverImage: null,
        meetingContent: null,
        meetingTime: null,
        meetingLocation: null,
        meetingStatus: null,
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
      const meetingIds = row.meetingId || this.ids
      this.$modal.confirm('是否确认删除会议管理编号为"' + meetingIds + '"的数据项？').then(function() {
        return delMeeting(meetingIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
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
    handleViewFeedbacks(meeting) {
      // 如果传入的是会议对象，先获取会议详情
      if (meeting.meetingId) {
        getMeeting(meeting.meetingId).then(response => {
          const topics = response.data.topics || [];
          // 汇总所有议题的反馈
          let allFeedbacks = [];
          topics.forEach(topic => {
            if (topic.feedbackList && topic.feedbackList.length > 0) {
              allFeedbacks = allFeedbacks.concat(topic.feedbackList);
            }
          });
          this.currentTopicFeedbacks = allFeedbacks;
          this.feedbackListDialogVisible = true;
        });
      } else {
        // 如果是议题对象
        this.currentTopicFeedbacks = meeting.feedbackList || [];
        this.feedbackListDialogVisible = true;
      }
    },
    /** 格式化投票选项 */
    formatVoteOption(option) {
      if (option === 0) return '同意';
      if (option === 1) return '反对';
      if (option === 2) return '弃权';
      return '未知';
    },
    /** 计算参与率 */
    calculateParticipationRate(meeting) {
      if (!meeting.totalVoters || meeting.totalVoters === 0) {
        return '0%';
      }
      const rate = ((meeting.actualVoters || 0) / meeting.totalVoters * 100).toFixed(1);
      return rate + '%';
    },
    /** 获取会议状态文本 */
    getMeetingStatusText(meeting) {
      return meeting.meetingStatus || '进行中';
    },
    /** 获取会议状态样式类 */
    getMeetingStatusClass(meeting) {
      const status = meeting.meetingStatus || '';
      if (status.includes('进行中') || status.includes('待行')) {
        return 'status-ongoing';
      } else if (status.includes('已结束')) {
        return 'status-finished';
      } else if (status.includes('未开始')) {
        return 'status-pending';
      }
      return 'status-ongoing';
    },
    /** 处理卡片点击 */
    handleCardClick(meeting) {
      this.handleViewTopics(meeting);
    },
    /** 处理图片加载失败 */
    handleImageError(e) {
      e.target.src = require('@/assets/images/profile.jpg');
    },
    /** 处理标签页切换 */
    handleTabClick(tab) {
      this.activeTab = tab;
      // 这里可以根据不同的tab加载不同的数据
      // 例如: this.getList();
    },
    /** 短信通知/投票 */
    handleSmsNotify(meeting) {
      this.$message.info('短信通知功能开发中');
    },
    /** 查看投票记录 */
    handleViewVoteRecords(meeting) {
      // 跳转到投票记录页面
      this.$router.push({
        path: '/system/voteRecords/index',
        query: {
          meetingId: meeting.meetingId,
          meetingTitle: meeting.meetingTitle
        }
      });
    },
    /** 查看表决结果 */
    handleViewVoteResults(meeting) {
      // 跳转到表决结果页面
      this.$router.push({
        path: '/system/voteResults/index',
        query: {
          meetingId: meeting.meetingId,
          meetingTitle: meeting.meetingTitle
        }
      });
    },
    /** 结果导出 */
    handleExportResults(meeting) {
      if (!meeting || !meeting.meetingId) {
        this.$message.warning('请选择有效的会议');
        return;
      }

      this.$confirm('请选择导出方式', '导出选项', {
        distinguishCancelAndClose: true,
        confirmButtonText: 'PDF报告',
        cancelButtonText: 'Excel列表',
        type: 'info',
        center: true
      }).then(() => {
        // 用户选择了 PDF 报告
        this.exportPdfReport(meeting);
      }).catch(action => {
        if (action === 'cancel') {
          // 用户选择了 Excel 列表
          this.exportExcelList(meeting);
        }
      });
    },

    /** 导出Excel投票列表 */
    exportExcelList(meeting) {
      const loading = this.$loading({
        lock: true,
        text: '正在生成Excel文件，请稍候...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      exportVoteListExcel({
        meetingId: meeting.meetingId,
        communityId: meeting.communityId || this.currentCommunityId
      }).then(response => {
        this.$message.success('Excel导出成功');
      }).catch(error => {
        console.error('导出失败', error);
        this.$message.error('导出失败: ' + (error.message || '未知错误'));
      }).finally(() => {
        loading.close();
      });
    },

    /** 导出PDF投票统计报告 */
    exportPdfReport(meeting) {
      const loading = this.$loading({
        lock: true,
        text: '正在生成PDF报告，请稍候...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      exportVoteReportPdf({
        meetingId: meeting.meetingId,
        communityId: meeting.communityId || this.currentCommunityId
      }).then(response => {
        this.$message.success('PDF导出成功');
      }).catch(error => {
        console.error('导出失败', error);
        this.$message.error('导出失败: ' + (error.message || '未知错误'));
      }).finally(() => {
        loading.close();
      });
    },
    /** 投票/票权管理 */
    handleVoteManagement(meeting) {
      this.$message.info('投票/票权管理功能开发中');
    },
    /** 唱票/统计 */
    handleVoteCount(meeting) {
      this.$message.info('唱票/统计功能开发中');
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
    }
  }
}
</script>

<style scoped lang="scss">
.meeting-container {
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
  padding: 0;
}

.top-bar {
  background: #fff;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e8e8e8;

  .search-form {
    margin: 0;

    ::v-deep .el-form-item {
      margin-bottom: 0;
      margin-right: 10px;
    }
  }

  .action-buttons {
    display: flex;
    gap: 10px;
  }
}

.status-tabs {
  background: #fff;
  display: flex;
  padding: 0 20px;
  border-bottom: 2px solid #e8e8e8;

  .tab-item {
    padding: 15px 30px;
    cursor: pointer;
    position: relative;
    color: #606266;
    font-size: 14px;
    transition: all 0.3s;

    &:hover {
      color: #409eff;
    }

    &.active {
      color: #409eff;
      font-weight: bold;

      &::after {
        content: '';
        position: absolute;
        bottom: -2px;
        left: 0;
        right: 0;
        height: 2px;
        background: #409eff;
      }
    }
  }
}

.meeting-list {
  padding: 20px;
  min-height: 400px;
}

.meeting-card-row {
  margin-bottom: 16px;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
  }

  ::v-deep .el-card__body {
    padding: 0;
  }
}

.card-row-content {
  display: flex;
  align-items: stretch;
}

.card-left {
  flex-shrink: 0;
  width: 200px;
  position: relative;

  .meeting-image-wrapper {
    position: relative;
    width: 100%;
    height: 100%;
    min-height: 180px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

    .meeting-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .meeting-status-badge {
      position: absolute;
      top: 10px;
      right: 10px;
      padding: 4px 12px;
      border-radius: 12px;
      font-size: 12px;
      color: #fff;
      font-weight: bold;

      &.status-ongoing {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.status-finished {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.status-pending {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }
  }
}

.card-middle {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;

  .meeting-header {
    margin-bottom: 15px;

    .meeting-title {
      font-size: 18px;
      font-weight: bold;
      color: #303133;
    }
  }

  .meeting-info {
    .info-row {
      display: flex;
      margin-bottom: 10px;
      gap: 40px;

      .info-item {
        display: flex;
        align-items: center;
        font-size: 14px;
        color: #606266;
        flex: 1;

        i {
          margin-right: 8px;
          color: #909399;
          font-size: 16px;
        }

        .label {
          color: #909399;
          margin-right: 5px;
        }

        .value {
          color: #303133;
          font-weight: 600;
        }
      }
    }
  }
}

.card-right {
  flex-shrink: 0;
  width: 350px;
  padding: 15px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: #fafafa;
  border-left: 1px solid #f0f0f0;

  // 第一层：图标按钮
  .icon-buttons-row {
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #e8e8e8;
    margin-bottom: 15px;

    .icon-btn-item {
      cursor: pointer;
      padding: 8px;
      border-radius: 50%;
      transition: all 0.3s;

      i {
        font-size: 22px;
        color: #909399;
        transition: all 0.3s;
      }

      &:hover {
        background: #e8f4ff;

        i {
          color: #409eff;
          transform: scale(1.15);
        }
      }
    }
  }

  // 第二层：文字按钮
  .text-buttons-row {
    display: flex;
    justify-content: space-around;
    align-items: center;
    gap: 5px;

    .text-btn-item {
      cursor: pointer;
      padding: 6px 8px;
      transition: all 0.3s;
      border-radius: 3px;

      span {
        font-size: 12px;
        color: #409eff;
        white-space: nowrap;
        transition: all 0.3s;
      }

      &:hover {
        background: #e8f4ff;

        span {
          color: #1890ff;
          font-weight: 600;
        }
      }
    }
  }
}

// 响应式调整
@media screen and (max-width: 1200px) {
  .card-left {
    width: 160px;
  }

  .card-right {
    width: 180px;

    .icon-buttons-row {
      .icon-btn-item i {
        font-size: 18px;
      }
    }

    .text-buttons-row {
      .text-btn-item span {
        font-size: 11px;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .meeting-container {
    padding: 0;
  }

  .top-bar {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .status-tabs {
    overflow-x: auto;

    .tab-item {
      padding: 12px 20px;
      white-space: nowrap;
    }
  }

  .card-row-content {
    flex-direction: column;
  }

  .card-left {
    width: 100%;
  }

  .card-middle {
    padding: 15px;
  }

  .card-right {
    width: 100%;
    padding: 15px;

    .icon-buttons-row {
      justify-content: space-around;
      margin-bottom: 10px;
    }

    .text-buttons-row {
      justify-content: space-around;
    }
  }
}

.form-section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  padding-left: 10px;
  border-left: 4px solid #409eff;
  margin: 20px 0 15px 0;
  line-height: 1.2;
  display: flex;
  align-items: center;
  justify-content: space-between;

  &:first-child {
    margin-top: 0;
  }
}

::v-deep .meeting-dialog {
  border-radius: 8px;

  .el-dialog__header {
    border-bottom: 1px solid #ebeef5;
    padding: 20px;
  }

  .el-dialog__body {
    padding: 20px 30px;
  }

  .el-dialog__footer {
    border-top: 1px solid #ebeef5;
    padding: 15px 20px;
  }
}
</style>
