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
      <el-form-item label="投票开始时间" prop="voteStartTime">
        <el-date-picker clearable
          v-model="queryParams.voteStartTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择投票开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="投票结束时间" prop="voteEndTime">
        <el-date-picker clearable
          v-model="queryParams.voteEndTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择投票结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="应参与人数" prop="totalVoters">
        <el-input
          v-model="queryParams.totalVoters"
          placeholder="请输入应参与人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际参与人数" prop="actualVoters">
        <el-input
          v-model="queryParams.actualVoters"
          placeholder="请输入实际参与人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
      <el-table-column label="会议状态" align="center" prop="meetingStatus" />
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
        <el-form-item label="会议类型" prop="meetingType">
          <el-select v-model="form.meetingType" placeholder="请选择会议类型" style="width:100%">
            <!-- TODO: 此处应改为使用字典管理动态获取 -->
            <el-option label="业主大会" value="1"></el-option>
            <el-option label="业委会会议" value="2"></el-option>
          </el-select>
        </el-form-item>
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

  </div>
</template>

<script>
import { listMeeting, getMeeting, delMeeting, addMeeting, updateMeeting } from "@/api/system/meeting"

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
        meetingType: null,
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
      form: {},
      // 表单校验
      rules: {
        meetingTitle: [
          { required: true, message: "会议标题不能为空", trigger: "blur" }
        ],
        meetingType: [
          { required: true, message: "会议类型不能为空", trigger: "change" }
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
  created() {
    this.getList()
  },
  methods: {
    /** 查询会议管理列表 */
    getList() {
      this.loading = true
      listMeeting(this.queryParams).then(response => {
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
        meetingType: null,
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
      this.reset()
      this.open = true
      this.title = "添加会议管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const meetingId = row.meetingId || this.ids
      getMeeting(meetingId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改会议管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
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
      this.download('system/meeting/export', {
        ...this.queryParams
      }, `meeting_${new Date().getTime()}.xlsx`)
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
    }
  }
}
</script>