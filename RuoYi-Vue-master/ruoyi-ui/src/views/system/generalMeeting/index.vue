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
              <div class="meeting-title">
                {{ meeting.meetingTitle }}
                <el-tag size="small" v-if="meeting.meetingTag == 1" type="success" style="margin-left: 10px;">业主大会</el-tag>
                <el-tag size="small" v-else-if="meeting.meetingTag == 2" type="warning" style="margin-left: 10px;">招标会议</el-tag>
                <el-tag size="small" v-else-if="meeting.meetingTag == 3" type="danger" style="margin-left: 10px;">选举会议</el-tag>
              </div>
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
                  <span class="label">投票剩余：</span>
                  <span class="value" style="color: #F56C6C">{{ calculateRemainingTime(meeting) }}</span>
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

              <div class="icon-btn-item" @click.stop="handleSmsNotify(meeting)" title="短信通知/投票">
                <i class="el-icon-message"></i>
              </div>
              <div class="icon-btn-item" @click.stop="handleViewVoteRecords(meeting)" title="投票记录">
                <i class="el-icon-document"></i>
              </div>
              <div class="icon-btn-item" @click.stop="handleViewVoteResults(meeting)" title="表决结果">
                <i class="el-icon-pie-chart"></i>
              </div>
              <el-popover
                placement="bottom"
                width="150"
                trigger="click">
                <div class="meeting-action-list">
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleExportVotingResults(meeting)" v-hasPermi="['system:meeting:exportVotingResults']">投票结果</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleExportVotingDetailsPublic(meeting)" v-hasPermi="['system:meeting:exportVotingDetailsPublic']">投票明细公开表</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleExportMeetingDocuments(meeting)" v-hasPermi="['system:meeting:exportMeetingDocuments']">会议文件</el-button>
                </div>
                <div class="icon-btn-item" slot="reference" title="更多导出选项">
                  <i class="el-icon-download"></i>
                </div>
              </el-popover>

            </div>

            <!-- 第二层：文字按钮 -->
            <div class="text-buttons-row">
              <el-popover
                placement="bottom"
                width="120"
                trigger="click">
                <div class="meeting-action-list">
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleExportBallot(meeting)" v-hasPermi="['system:meeting:exportBallot']">表决票导出</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handlePublicizeResults(meeting)" v-hasPermi="['system:meeting:publicize']">公示结果</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleUpdate(meeting)" v-hasPermi="['system:meeting:edit']">编辑活动</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleCopyActivity(meeting)" v-hasPermi="['system:meeting:copy']">复制活动</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0;color:#F56C6C" @click="handleDelete(meeting)" v-hasPermi="['system:meeting:remove']">删除活动</el-button>
                </div>
                <div class="text-btn-item" slot="reference">
                  <span>活动管理</span>
                </div>
              </el-popover>
              <el-popover
                placement="bottom"
                width="150"
                trigger="click">
                <div class="meeting-action-list">
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleVoteManagement(meeting)" v-hasPermi="['system:meeting:voteRights']">票权管理</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleViewVoteRecords(meeting)" v-hasPermi="['system:meeting:viewVoteRecords']">投票记录</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handlePreserveCertificate(meeting)" v-hasPermi="['system:meeting:preserveCert']">保全证书</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleSmsNotify(meeting)" v-hasPermi="['system:meeting:smsNotify']">短信/电话通知投票</el-button>
                </div>
                <div class="text-btn-item" slot="reference">
                  <span>投票/票权</span>
                </div>
              </el-popover>
              <el-popover
                placement="bottom"
                width="120"
                trigger="click">
                <div class="meeting-action-list">
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleVoteCount(meeting)" v-hasPermi="['system:meeting:voteRights']">唱票计票</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleViewVoteResults(meeting)" v-hasPermi="['system:meeting:viewVoteRecords']">表决结果</el-button>
                   <el-button type="text" size="small" style="display:block;width:100%;text-align:left;margin-left:0" @click="handleBuildingStats(meeting)" v-hasPermi="['system:meeting:voteRights']">楼栋统计</el-button>
                </div>
                <div class="text-btn-item" slot="reference">
                  <span>唱票/统计</span>
                </div>
              </el-popover>
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
          <el-col :span="24">
             <el-form-item label="会议标签" prop="meetingTag">
               <el-radio-group v-model="form.meetingTag">
                 <el-radio :label="1">业主大会</el-radio>
                 <el-radio :label="2">招标会议</el-radio>
                 <el-radio :label="3">选举会议</el-radio>
               </el-radio-group>
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
          <el-col :span="12">
            <el-form-item label="展示参与人数" prop="showParticipantCount">
              <el-switch
                v-model="form.showParticipantCount"
                active-value="1"
                inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">详细内容</div>
        <el-form-item label="会议内容" prop="meetingContent">
          <editor v-model="form.meetingContent" :min-height="192"/>
        </el-form-item>

        <div class="form-section-title">
          <span>{{ topicSectionTitle }}</span>
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddTopic" style="float: right">{{ addTopicButtonText }}</el-button>
        </div>
        <el-table :data="form.topics" border style="margin-bottom: 20px">
          <el-table-column :label="topicColumnLabel" align="center" prop="topicTitle" />
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
      <el-form ref="topicForm" :model="topicForm" :rules="topicRules" label-width="120px">
        <el-form-item :label="topicTitleLabel" prop="topicTitle">
           <div style="display: flex; align-items: center;">
             <el-input v-model="topicForm.topicTitle" :placeholder="topicTitlePlaceholder" :readonly="form.meetingTag === 3"/>
             <el-button v-if="form.meetingTag === 3" type="primary" size="mini" @click="handleSelectUser" style="margin-left: 10px;">选择候选人</el-button>
           </div>
        </el-form-item>

        <el-form-item :label="avatarLabel" v-if="form.meetingTag === 2 || form.meetingTag === 3">
          <image-upload v-model="topicForm.avatar" :limit="1"/>
        </el-form-item>

        <el-form-item :label="topicContentLabel">
          <editor v-model="topicForm.topicContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="附件">
          <file-upload v-model="topicForm.files"/>
        </el-form-item>
        <el-form-item label="从多选项">
           <el-switch v-model="topicForm.congduo" active-text="开启" inactive-text="关闭" active-value="1" inactive-value="0"></el-switch>
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

    <!-- 投票导入对话框 -->
    <el-dialog title="投票导入" :visible.sync="voteImportDialogVisible" width="600px" append-to-body>
      <el-form :model="importForm" ref="importForm" label-width="80px" class="dialog-form">
        <el-form-item label="会议名称">
          <el-input v-model="currentImportMeetingTitle" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="上传文件">
          <el-upload
            class="upload-demo"
            ref="upload"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-change="handleFileChange"
            :multiple="true"
            :directory="true"
            :auto-upload="false"
            drag
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件/文件夹拖到此处，或<em>点击选择</em></div>
            <div class="el-upload__tip" slot="tip">
              支持上传多个文件或整个文件夹。单个文件大小不超过10MB，总大小不超过100MB。支持图片类型：jpg/png/bmp/gif。
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="导入结果" v-if="importResultsSummary">
          <div class="import-summary">
            <p>总文件数: {{ importResultsSummary.totalFiles }}</p>
            <p>成功文件数: {{ importResultsSummary.successFiles }}</p>
            <p>失败文件数: {{ importResultsSummary.failedFiles }}</p>
            <p>导入投票记录数: {{ importResultsSummary.totalVoteRecords }}</p>
            <p>成功率: {{ importResultsSummary.successRate.toFixed(2) }}%</p>
          </div>
          <el-collapse v-if="importResults.length > 0">
            <el-collapse-item title="查看详细结果">
              <div v-for="(result, index) in importResults" :key="index" class="result-item">
                <p>文件: {{ result.fileName }} <el-tag :type="result.success ? 'success' : 'danger'">{{ result.success ? '成功' : '失败' }}</el-tag></p>
                <p v-if="!result.success" style="color:red;">错误: {{ result.message }}</p>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpload">开始导入</el-button>
        <el-button @click="voteImportDialogVisible = false">取 消</el-button>
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

    <!-- 楼栋统计对话框 -->
    <el-dialog :title="'楼栋统计 - ' + currentImportMeetingTitle" :visible.sync="buildingStatsDialogVisible" width="700px" append-to-body>
      <el-table :data="buildingStatsList" border v-loading="buildingStatsLoading" height="500" stripe>
        <el-table-column label="楼栋" prop="buildingName" align="center" sortable></el-table-column>
        <el-table-column label="单元" prop="unitName" align="center" sortable></el-table-column>
        <el-table-column label="总户数" prop="totalHouseholds" align="center" sortable></el-table-column>
        <el-table-column label="有投票" prop="votedHouseholds" align="center" sortable></el-table-column>
        <el-table-column label="投票占比" prop="votePercentage" align="center" sortable>
           <template slot-scope="scope">
             <el-progress :percentage="parseFloat(scope.row.votePercentage)" :color="customColors"></el-progress>
           </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="buildingStatsDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <user-select-dialog :visible.sync="userSelectVisible" @confirm="handleUserSelectConfirm" />

  </div>
</template>

<script>
import { listMeeting, getMeeting, delMeeting, addMeeting, updateMeeting, exportBallot, copyMeeting, exportVotingResults, exportVotingDetailsPublic, exportMeetingDocuments, getBuildingStats } from "@/api/system/meeting"
import { exportVoteListExcel, exportVoteReportPdf } from '@/api/system/voteResults'
import { mapGetters } from "vuex"
import UserSelectDialog from "@/components/UserSelectDialog";

export default {
  name: "Meeting",
  components: { UserSelectDialog },
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
      // 投票导入对话框相关
      voteImportDialogVisible: false,
      currentImportMeetingId: null,
      currentImportMeetingTitle: '',
      importForm: {}, // 用于表单验证等
      fileList: [], // el-upload 的文件列表
      uploadHeaders: {}, // el-upload 的请求头
      importResultsSummary: null, // 导入结果汇总
      importResults: [], // 导入文件详细结果
      // 楼栋统计
      buildingStatsDialogVisible: false,
      buildingStatsList: [],
      buildingStatsLoading: false,
      customColors: [
        {color: '#f56c6c', percentage: 20},
        {color: '#e6a23c', percentage: 40},
        {color: '#5cb87a', percentage: 60},
        {color: '#1989fa', percentage: 80},
        {color: '#6f7ad3', percentage: 100}
      ],
      // 用户选择弹窗
      userSelectVisible: false,
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
    },
    // 上传URL的计算属性
    uploadUrl() {
      return process.env.VUE_APP_BASE_API + '/system/vote/import/single/' + this.currentImportMeetingId;
    },
    // 动态计算议题标题的标签
    topicTitleLabel() {
      if (this.form.meetingTag === 2) return "投标公司名称";
      if (this.form.meetingTag === 3) return "候选人姓名";
      return "议题标题";
    },
    // 动态计算议题标题的占位符
    topicTitlePlaceholder() {
      if (this.form.meetingTag === 2) return "请输入投标公司名称";
      if (this.form.meetingTag === 3) return "请选择候选人";
      return "请输入议题标题";
    },
    // 动态计算内容的标签
    topicContentLabel() {
      if (this.form.meetingTag === 2) return "公司简介";
      if (this.form.meetingTag === 3) return "候选人简介";
      return "议题内容";
    },
    // 动态计算头像/Logo的标签
    avatarLabel() {
      if (this.form.meetingTag === 2) return "公司Logo";
      if (this.form.meetingTag === 3) return "候选人照片";
      return "图片";
    },
    // 动态计算议题板块标题
    topicSectionTitle() {
      if (this.form.meetingTag === 2) return "招标企业";
      if (this.form.meetingTag === 3) return "候选人管理";
      return "议题管理";
    },
    // 动态计算添加议题按钮文本
    addTopicButtonText() {
      if (this.form.meetingTag === 2) return "添加企业";
      if (this.form.meetingTag === 3) return "添加候选人";
      return "添加议题";
    },
    // 动态计算列表列标题
    topicColumnLabel() {
      if (this.form.meetingTag === 2) return "企业名称";
      if (this.form.meetingTag === 3) return "候选人姓名";
      return "议题标题";
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
        meetingTag: 1, // 默认业主大会
        communityId: null,
        communityName: '',
        coverImage: null,
        meetingContent: null,
        meetingTime: null,
        meetingLocation: null,
        meetingStatus: null,
        voteStartTime: null,
        voteEndTime: null,
        showParticipantCount: "1",
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
        console.log("Meeting Data:", response.data); // Debug log
        console.log("showParticipantCount:", response.data.showParticipantCount); // Debug log
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
    /** 计算投票剩余时间 */
    calculateRemainingTime(meeting) {
      if (!meeting.voteEndTime) return '-';

      const now = new Date().getTime();
      const end = new Date(meeting.voteEndTime).getTime();
      const start = meeting.voteStartTime ? new Date(meeting.voteStartTime).getTime() : 0;

      // 如果还没开始
      if (start > 0 && now < start) {
         return '未开始';
      }

      // 如果已经结束
      if (now >= end) {
        return '已结束';
      }

      // 计算剩余时间
      const diff = end - now;
      const days = Math.floor(diff / (1000 * 60 * 60 * 24));

      if (days > 0) {
          return `剩余 ${days} 天`;
      } else {
          const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
          // 如果不足1小时
          if (hours === 0) {
             const minutes = Math.ceil((diff % (1000 * 60 * 60)) / (1000 * 60));
             return `剩余 ${minutes} 分钟`;
          }
          return `剩余 ${hours} 小时`;
      }
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

    /** 通用文件下载处理 */
    handleDownloadBlob(promise, defaultName) {
        const loading = this.$loading({
          lock: true,
          text: '正在生成文件，请稍候...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        promise.then(blob => {
          if (blob.type === 'application/json') {
            const reader = new FileReader();
            reader.onload = (e) => {
                try {
                    const res = JSON.parse(e.target.result);
                    this.$message.error(res.msg || '导出失败');
                } catch (err) {
                    this.$message.error('导出失败');
                }
                loading.close();
            };
            reader.readAsText(blob);
            return;
          }
          const link = document.createElement('a')
          const url = window.URL.createObjectURL(blob)
          link.href = url
          link.download = defaultName
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功');
          loading.close();
        }).catch((e) => {
          console.error(e);
          this.$message.error('导出失败');
          loading.close();
        });
    },

    /** 导出投票结果 */
    handleExportVotingResults(meeting) {
      if (!meeting || !meeting.meetingId) {
        this.$message.warning('请选择有效的会议');
        return;
      }
      this.handleDownloadBlob(
        exportVotingResults(meeting.meetingId),
        meeting.meetingTitle + '_投票结果.xlsx'
      );
    },

    /** 导出投票明细公开表 */
    handleExportVotingDetailsPublic(meeting) {
      if (!meeting || !meeting.meetingId) {
        this.$message.warning('请选择有效的会议');
        return;
      }
      this.$modal.confirm('确认导出投票明细公开表？此表包含脱敏后的业主投票信息，可用于公示。').then(() => {
        this.handleDownloadBlob(
          exportVotingDetailsPublic(meeting.meetingId),
          meeting.meetingTitle + '_投票明细公示表.xlsx'
        );
      }).catch(() => {});
    },

    /** 导出会议文件 */
    handleExportMeetingDocuments(meeting) {
      if (!meeting || !meeting.meetingId) {
        this.$message.warning('请选择有效的会议');
        return;
      }
      this.handleDownloadBlob(
        exportMeetingDocuments(meeting.meetingId),
        meeting.meetingTitle + '_会议文件.zip'
      );
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
      this.$router.push({ path: '/user/service/owner', query: { communityId: meeting.communityId } });
    },
    /** 唱票/统计 */
    handleVoteCount(meeting) {
      this.$message.info('唱票/统计功能开发中');
    },
    /** 表决票导出 */
    handleExportBallot(meeting) {
      this.$confirm('请选择导出类型', '表决票导出', {
        distinguishCancelAndClose: true,
        confirmButtonText: '空白表决票',
        cancelButtonText: '未投票用户表决票(填充)',
        type: 'info'
      }).then(() => {
        // 导出空白表决票
        this.downloadBlankBallot(meeting);
      }).catch(action => {
        if (action === 'cancel') {
          // 导出填充表决票
          this.downloadFilledBallot(meeting);
        }
      });
    },
    /** 导出空白表决票 */
    downloadBlankBallot(meeting) {
      const loading = this.$loading({
        lock: true,
        text: '正在生成空白表决票，请稍候...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      // 假设后端有一个 exportBlankBallot 接口，暂时复用 exportBallot 并加参数，或者新建接口
      // 这里为了演示，我们修改 exportBallot 传参 type=blank
      // 但根据需求 "空白则只导出模板"，可能是指下载模板文件
      // 我们先尝试用 exportBallot 接口加参数，如果后端没支持，可能需要改后端
      // 鉴于当前上下文不能改后端，我们假设后端 exportBallot 支持 type 参数
      // 或者我们直接请求模板文件

      // 修改策略：调用 exportBallot 并传递参数
      this.handleDownloadBlob(
        exportBallot(meeting.meetingId, 'blank'),
        meeting.meetingTitle + '_空白表决票.docx' // 假设空白票是单文件
      );
      loading.close();
    },
    /** 导出填充表决票 */
    downloadFilledBallot(meeting) {
      this.$modal.confirm('确认导出当前会议未投票用户的表决票？').then(() => {
        const loading = this.$loading({
          lock: true,
          text: '正在生成表决票压缩包，请稍候...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        exportBallot(meeting.meetingId, 'filled').then(blob => {
          if (blob.type === 'application/json') {
            const reader = new FileReader();
            reader.onload = (e) => {
                try {
                    const res = JSON.parse(e.target.result);
                    this.$message.error(res.msg || '导出失败');
                } catch (err) {
                    this.$message.error('导出失败');
                }
                loading.close();
            };
            reader.readAsText(blob);
            return;
          }
          const link = document.createElement('a')
          const url = window.URL.createObjectURL(blob)
          link.href = url
          link.download = meeting.meetingTitle + '_未投票表决票.zip'
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功');
          loading.close();
        }).catch((e) => {
          console.error(e);
          this.$message.error('表决票导出失败');
          loading.close();
        });
      }).catch(() => {});
    },
    /** 公示结果 */
    handlePublicizeResults(meeting) {
      this.$modal.confirm('确认公示当前会议的表决结果？此操作将使结果对外可见。').then(() => {
        // 假设有一个新的API用于公示结果，这里使用占位符
        this.$message.success('公示结果功能开发中，会议ID：' + meeting.meetingId);
        // TODO: 调用后端接口公示结果
        // publicizeResultsApi(meeting.meetingId).then(() => {
        //   this.$message.success('表决结果已成功公示');
        //   this.getList(); // 刷新列表，可能状态会变化
        // }).catch(() => {
        //   this.$message.error('公示结果失败');
        // });
      }).catch(() => {});
    },
    /** 复制活动 */
    handleCopyActivity(meeting) {
      this.$modal.confirm('确认复制当前活动？复制后您可以在草稿中编辑。').then(() => {
        copyMeeting(meeting.meetingId).then(response => {
          this.$modal.msgSuccess("复制成功");
          this.getList();
        });
      }).catch(() => {});
    },
    /** 打开用户选择弹窗 */
    handleSelectUser() {
      this.userSelectVisible = true;
    },
    /** 用户选择确认 */
    handleUserSelectConfirm(user) {
      // 自动填充信息
      this.topicForm.topicTitle = user.nickName || user.userName;
      this.topicForm.candidateId = user.userId;
      // 如果用户有头像，尽量使用（这里假设是相对路径或完整URL）
      if (user.avatar) {
          this.topicForm.avatar = user.avatar;
      }
      // 可以在内容中自动填充简介模板
      if (!this.topicForm.topicContent) {
          this.topicForm.topicContent = `<p><strong>姓名：</strong>${user.nickName || user.userName}</p><p><strong>电话：</strong>${user.phonenumber || ''}</p><p><strong>简介：</strong></p>`;
      }
    },
    // 议题表单重置
    resetTopic() {
      this.topicForm = {
        topicId: null,
        topicTitle: null,
        topicContent: null,
        files: null,
        avatar: null,
        candidateId: null,
        agreeCount: 0,
        opposeCount: 0,
        abstainCount: 0,
        congduo: "0"
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
    // 投票导入相关方法
    /** 唱票/统计 (打开导入对话框) */
    handleVoteCount(meeting) {
      this.currentImportMeetingId = meeting.meetingId;
      this.currentImportMeetingTitle = meeting.meetingTitle;
      this.fileList = []; // 清空文件列表
      this.importResultsSummary = null; // 清空结果
      this.importResults = []; // 清空详细结果
      // 设置上传请求头，包含token
      this.uploadHeaders = { Authorization: 'Bearer ' + this.$store.getters.token };
      this.voteImportDialogVisible = true;
      // 重置上传组件（如果有）
      this.$nextTick(() => {
        if (this.$refs.upload) {
          this.$refs.upload.clearFiles();
        }
      });
    },
    // 手动提交上传
    submitUpload() {
      if (this.fileList.length === 0) {
        this.$modal.msgWarning("请选择要上传的文件！");
        return;
      }
      this.$refs.upload.submit(); // 触发 el-upload 组件的上传
      this.$modal.loading("正在导入投票数据，请稍候...");
    },
    // 文件状态改变时的回调
    handleFileChange(file, fileList) {
      this.fileList = fileList;
      this.importResultsSummary = null; // 清空结果
      this.importResults = []; // 清空详细结果
    },
    // 文件上传成功时的回调
    handleUploadSuccess(response, file, fileList) {
      this.$modal.closeLoading();
      if (response.code === 200) {
        this.$modal.msgSuccess(response.message || "文件上传成功");
        this.importResultsSummary = response.summary;
        this.importResults = response.results.map(r => ({
          fileName: r.fileName,
          success: r.success,
          message: r.message
        }));
        this.getList(); // 刷新会议列表，可能统计数据有变
      } else {
        this.$modal.msgError(response.message || "文件上传失败");
        this.importResultsSummary = {
          totalFiles: fileList.length,
          successFiles: 0,
          failedFiles: fileList.length,
          totalVoteRecords: 0,
          successRate: 0
        };
        this.importResults = fileList.map(f => ({
          fileName: f.name,
          success: false,
          message: response.message || "上传失败"
        }));
      }
    },
    // 文件上传失败时的回调
    handleUploadError(err, file, fileList) {
      this.$modal.closeLoading();
      let errorMsg = "文件上传失败，请重试！";
      try {
        const error = JSON.parse(err.message);
        errorMsg = error.msg || errorMsg;
      } catch (e) {
        // ignore
      }
      this.$modal.msgError(errorMsg);
      this.importResultsSummary = {
        totalFiles: fileList.length,
        successFiles: 0,
        failedFiles: fileList.length,
        totalVoteRecords: 0,
        successRate: 0
      };
      this.importResults = fileList.map(f => ({
        fileName: f.name,
        success: false,
        message: errorMsg
      }));
    },
    /** 保全证书 */
    handlePreserveCertificate(meeting) {
      this.$message.info('保全证书功能开发中，会议ID：' + meeting.meetingId);
      // TODO: 调用后端接口生成或查看保全证书
    },
    /** 楼栋统计 */
    handleBuildingStats(meeting) {
      this.buildingStatsDialogVisible = true;
      this.buildingStatsLoading = true;
      this.currentImportMeetingTitle = meeting.meetingTitle;

      getBuildingStats(meeting.meetingId).then(response => {
        this.buildingStatsList = response.data;
        this.buildingStatsLoading = false;
      }).catch(() => {
        this.buildingStatsLoading = false;
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
