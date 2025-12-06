<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="真实姓名" prop="realName">
        <el-input
          v-model="queryParams.realName"
          placeholder="请输入真实姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房产类型" prop="propertyTag">
        <el-select v-model="queryParams.propertyTag" placeholder="请选择房产类型" clearable>
          <el-option label="单套房" value="单套房" />
          <el-option label="多套房" value="多套房" />
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
          v-hasPermi="['system:owner:add']"
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
          v-hasPermi="['system:owner:edit']"
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
          v-hasPermi="['system:owner:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:owner:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['system:owner:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-share"
          size="mini"
          :disabled="single"
          @click="handleTransfer"
          v-hasPermi="['system:owner:edit']"
        >合并/拆分</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ownerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="真实姓名" align="center" prop="realName" />
      <el-table-column label="联系号码" align="center" prop="phonenumber" />
      <el-table-column label="所属小区" align="center" prop="communityName" />
      <el-table-column label="房产信息" align="center" prop="mergedProperties" show-overflow-tooltip />
      <el-table-column label="房产标签" align="center" prop="propertyTag">
        <template slot-scope="scope">
          <el-tag :type="scope.row.propertyTag === '多套房' ? 'warning' : 'success'">{{ scope.row.propertyTag }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="房产面积(㎡)" align="center" prop="propertyArea" />
      <el-table-column label="业委会成员" align="center" prop="isCommitteeMember">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isCommitteeMember"/>
        </template>
      </el-table-column>
      <el-table-column label="业委会/业主" align="center" prop="isOwner">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_owner_type" :value="scope.row.isOwner"/>
        </template>
      </el-table-column>
      <el-table-column label="票权状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:owner:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            :icon="scope.row.status == '0' ? 'el-icon-lock' : 'el-icon-unlock'"
            :class="scope.row.status == '0' ? 'text-danger' : 'text-success'"
            @click="handleStatusChange(scope.row)"
            v-hasPermi="['system:owner:edit']"
          >{{ scope.row.status == '0' ? '禁用' : '恢复' }}</el-button>
          <el-dropdown size="mini" @command="(command) => handleIdentityChange(scope.row, command)" v-hasPermi="['system:owner:edit']">
            <el-button size="mini" type="text" icon="el-icon-user">
              设置身份<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="1" :disabled="scope.row.isOwner == 1">
                <i class="el-icon-user-solid"></i>设为业主
              </el-dropdown-item>
              <el-dropdown-item command="2" :disabled="scope.row.isOwner == 2">
                <i class="el-icon-s-custom"></i>设为业委会
              </el-dropdown-item>
              <el-dropdown-item command="0" :disabled="scope.row.isOwner == 0">
                <i class="el-icon-user"></i>取消身份
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:owner:remove']"
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

    <!-- 添加或修改业主信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="所属小区" prop="communityId">
          <el-input v-model="form.communityName" placeholder="请先在右上角选择小区" disabled />
          <input type="hidden" v-model="form.communityId" />
        </el-form-item>
        <el-form-item label="绑定用户" prop="userId">
          <el-input
            v-model="selectedUserName"
            placeholder="请选择要绑定的用户"
            readonly
            @click="showUserSelectDialog"
          >
            <el-button slot="append" icon="el-icon-search" @click="showUserSelectDialog">选择用户</el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCardNo">
          <el-input v-model="form.idCardNo" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="楼栋号" prop="buildingNo">
          <el-select v-model="form.buildingNo" placeholder="请选择楼栋号" filterable @change="handleBuildingChange">
            <el-option
              v-for="item in buildingOptions"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="房号" prop="roomNo">
          <el-select v-model="form.roomNo" placeholder="请选择房号" filterable>
            <el-option
              v-for="item in roomOptions"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="业委会成员" prop="isCommitteeMember">
          <el-radio-group v-model="form.isCommitteeMember">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 业主导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?isUpdateSupport=' + upload.isUpdateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.isUpdateSupport" /> 是否更新已经存在的业主数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="handleDownloadTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户选择弹窗 -->
    <user-select-dialog
      :visible.sync="userSelectVisible"
      @confirm="handleUserSelect"
    />

    <!-- 房产合并与拆分弹窗 -->
    <property-transfer-dialog ref="transferDialog" @refresh="getList" />

  </div>
</template>

<script>
import { listOwner, getOwner, delOwner, addOwner, updateOwner, changeUserStatus, changeUserIdentity } from "@/api/system/owner";
import { listBuildings, listRooms } from "@/api/system/estateProperty";
import { getToken } from "@/utils/auth";
import UserSelectDialog from "@/components/UserSelectDialog";
import PropertyTransferDialog from "./PropertyTransferDialog";
import { mapGetters } from "vuex";

export default {
  name: "Owner",
  components: {
    UserSelectDialog,
    PropertyTransferDialog
  },
  dicts: ['sys_yes_no', 'sys_auth_status', 'sys_owner_type', 'sys_normal_disable'],
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
      // 业主信息表格数据
      ownerList: [],
      // 楼栋选项
      buildingOptions: [],
      // 房号选项
      roomOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 用户选择弹窗显示状态
      userSelectVisible: false,
      // 选中的用户名（用于显示）
      selectedUserName: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        authStatus:2,
        realName: null,
        phonenumber: null,
        contactNumber: null,
        communityId: null,
        propertyTag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        realName: [
          { required: true, message: "真实姓名不能为空", trigger: "blur" }
        ],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" }
        ],
      },
      // 导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        isUpdateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/owner/importData"
      },
    };
  },
  computed: {
    ...mapGetters([
      "currentCommunityId",
      "currentCommunityName",
      "communityOptions"
    ]),
    /**
     * 顶部选择为“全部小区”时返回 true。
     */
    isAllCommunitySelected() {
      const value = this.currentCommunityId;
      if (value === null || value === undefined) {
        return true;
      }
      return Number(value) === 0;
    }
  },
  created() {
    this.applyCommunityFilter();
    this.getList();
  },
  methods: {
    /**
     * 根据顶部选择的小区同步查询条件。
     */
    applyCommunityFilter() {
      if (this.isAllCommunitySelected) {
        this.queryParams.communityId = null;
      } else {
        this.queryParams.communityId = Number(this.currentCommunityId);
      }
    },
    /**
     * 构建请求参数，必要时携带小区 ID。
     */
    buildQueryParams() {
      const params = { ...this.queryParams };
      if (this.isAllCommunitySelected) {
        delete params.communityId;
      } else {
        const numericId = Number(this.currentCommunityId);
        this.queryParams.communityId = numericId;
        params.communityId = numericId;
      }
      return params;
    },
    /**
     * 根据小区 ID 返回对应的小区名称。
     */
    resolveCommunityName(communityId) {
      if (communityId === null || communityId === undefined) {
        return "";
      }
      const numericId = Number(communityId);
      const options = Array.isArray(this.communityOptions) ? this.communityOptions : [];
      const target = options.find(item => Number(item.id) === numericId);
      if (target && target.name) {
        return target.name;
      }
      if (
        this.currentCommunityId !== null &&
        this.currentCommunityId !== undefined &&
        Number(this.currentCommunityId) === numericId
      ) {
        return this.currentCommunityName || "";
      }
      return "";
    },
    /**
     * 为表单对象补全小区信息。
     */
    attachCommunityInfo(target, communityId) {
      const numericId =
        communityId === null || communityId === undefined ? null : Number(communityId);
      target.communityId = numericId;
      target.communityName = this.resolveCommunityName(numericId);
    },
    /** 查询业主信息列表 */
    getList() {
      this.loading = true;
      const params = this.buildQueryParams();
      listOwner(params).then(response => {
        this.ownerList = Array.isArray(response.rows)
          ? response.rows.map(item => ({
              ...item,
              communityName: this.resolveCommunityName(item.communityId)
            }))
          : [];
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
        userId: null,
        realName: null,
        idCardNo: null,
        idCardFrontUrl: null,
        idCardBackUrl: null,
        authStatus: "2",
        buildingNo: null,
        roomNo: null,
        communityId: null,
        communityName: "",
        isCommitteeMember: "N",
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        authRemark: null,
        phonenumber: null,
        contactNumber: null
      };
      this.selectedUserName = null;
      this.buildingOptions = [];
      this.roomOptions = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.applyCommunityFilter();
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.applyCommunityFilter();
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.ownerId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      if (this.isAllCommunitySelected) {
        this.$message.warning("请先在右上角选择具体小区，再新增业主。");
        return;
      }
      this.reset();
      this.attachCommunityInfo(this.form, this.currentCommunityId);
      this.getBuildingList(this.form.communityId);
      this.open = true;
      this.title = "添加业主信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const ownerId = row.ownerId || this.ids[0]
      getOwner(ownerId).then(response => {
        this.form = response.data;
        this.attachCommunityInfo(this.form, response.data.communityId);

        if(this.form.communityId) {
            this.getBuildingList(this.form.communityId);
            if(this.form.buildingNo) {
                this.getRoomList(this.form.communityId, this.form.buildingNo);
            }
        }

        this.open = true;
        this.title = "修改业主信息";
      });
    },
    /** 获取楼栋列表 */
    getBuildingList(communityId) {
      listBuildings({ communityId: communityId }).then(response => {
        this.buildingOptions = response.data;
      });
    },
    /** 获取房号列表 */
    getRoomList(communityId, buildingName) {
      listRooms({ communityId: communityId, buildingName: buildingName }).then(response => {
        this.roomOptions = response.data;
      });
    },
    /** 楼栋改变事件 */
    handleBuildingChange(value) {
      this.form.roomNo = null;
      this.roomOptions = [];
      if (value) {
        this.getRoomList(this.form.communityId, value);
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.communityId === null || this.form.communityId === undefined) {
            if (!this.isAllCommunitySelected) {
              this.form.communityId = Number(this.currentCommunityId);
              this.form.communityName = this.resolveCommunityName(this.form.communityId);
            }
          }
          const numericId = Number(this.form.communityId);
          if (Number.isNaN(numericId) || numericId === 0) {
            this.$message.error("请先为业主选择所属小区。");
            return;
          }
          this.form.communityId = numericId;
          if (this.form.ownerId != null) {
            updateOwner(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOwner(this.form).then(response => {
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
      const ownerIds = row.ownerId || this.ids;
      this.$modal.confirm('是否确认删除业主信息编号为"' + ownerIds + '"的数据项？').then(function() {
        return delOwner(ownerIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const params = this.buildQueryParams();
      this.download('system/owner/export', params, `owner_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "业主数据导入";
      this.upload.open = true;
    },
    /** 下载模板按钮操作 */
    handleDownloadTemplate() {
      this.download('system/owner/importTemplate', {
      }, `owner_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 状态修改按钮操作 */
    handleStatusChange(row) {
      const text = row.status === "0" ? "禁用" : "恢复";
      const newStatus = row.status === "0" ? "1" : "0";
      this.$modal.confirm('确认要"' + text + '""' + row.realName + '"用户吗？').then(function() {
        return changeUserStatus(row.userId, newStatus);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        // 操作失败时不需要修改状态，因为getList会重新获取数据
      });
    },
    /** 身份设置按钮操作 */
    handleIdentityChange(row, command) {
      const identityMap = {
        '0': '取消身份',
        '1': '设为业主',
        '2': '设为业委会'
      };
      const text = identityMap[command];
      this.$modal.confirm('确认要将"' + row.realName + '"' + text + '吗？').then(function() {
        return changeUserIdentity(row.userId, command);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        // 操作失败时不需要修改状态，因为getList会重新获取数据
      });
    },
    /** 显示用户选择弹窗 */
    showUserSelectDialog() {
      this.userSelectVisible = true;
    },
    /** 处理用户选择 */
    handleUserSelect(user) {
      this.form.userId = user.userId;
      this.selectedUserName = user.userName;
      this.form.phonenumber = user.phonenumber;
      // 如果真实姓名为空，使用昵称填充
      if (!this.form.realName && user.nickName) {
        this.form.realName = user.nickName;
      }
    },
    /** 房产合并与拆分操作 */
    handleTransfer(row) {
      const ownerId = row.ownerId || this.ids[0];
      if (!ownerId) {
        this.$message.warning("请选择一条数据");
        return;
      }
      const selectedRow = this.ownerList.find(item => item.ownerId === ownerId);
      if (selectedRow) {
        if (!selectedRow.userId) {
          this.$message.warning("该业主尚未绑定系统用户，无法进行房产转移操作");
          return;
        }
        this.$refs.transferDialog.init(selectedRow);
      }
    }
  }
};
</script>
