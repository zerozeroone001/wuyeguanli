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
      <el-table-column label="真实姓名" align="center" prop="userName" />
      <el-table-column label="联系号码" align="center" prop="phonenumber" />
      <el-table-column label="所属小区" align="center" prop="communityName" />
      <el-table-column label="房产信息" align="center" prop="mergedProperties" show-overflow-tooltip />
      <el-table-column label="房产标签" align="center" prop="propertyTag">
        <template slot-scope="scope">
          <el-tag :type="scope.row.propertyTag === '多套房' ? 'warning' : 'success'">{{ scope.row.propertyTag }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="房产面积(㎡)" align="center" prop="propertyArea" />
      <el-table-column label="业委会/业主" align="center" prop="isOwner">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_owner_type" :value="scope.row.isOwner"/>
        </template>
      </el-table-column>
      <el-table-column label="票权状态" align="center" prop="status">
        <template slot-scope="scope">
          <div>{{scope.row.userId?'正常':'未绑定'}}</div>
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

    <!-- 添加业主信息对话框 -->
    <el-dialog :title="titleAdd" :visible.sync="openAdd" width="800px" append-to-body>
      <el-form ref="formAdd" :model="formAdd" :rules="rules" label-width="100px">
        <el-form-item label="所属小区" prop="communityId">
          <el-input v-model="formAdd.communityName" placeholder="请先在右上角选择小区" disabled />
          <input type="hidden" v-model="formAdd.communityId" />
        </el-form-item>
             <el-form-item label="真实姓名" prop="realName">
               <el-input v-model="formAdd.realName" placeholder="请输入真实姓名" />
             </el-form-item>
             <el-form-item label="手机号码" prop="phonenumber">
               <el-input v-model="formAdd.phonenumber" placeholder="请输入手机号码" />
             </el-form-item>



        <el-form-item label="备注" prop="remark">
          <el-input v-model="formAdd.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>


            <el-form-item label="楼栋号" prop="buildingNo">
              <el-select v-model="formAdd.buildingNo" placeholder="请选择楼栋号" filterable @change="handleBuildingChangeAdd">
                <el-option v-for="item in buildingOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
            <el-form-item label="房号" prop="roomNo">
              <el-select v-model="formAdd.roomNo" placeholder="请选择房号" filterable>
                <el-option v-for="item in roomOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddForm">保存基本信息</el-button>
        <el-button @click="cancelAdd">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 修改业主信息对话框 -->
    <el-dialog :title="titleEdit" :visible.sync="openEdit" width="800px" append-to-body>
      <el-form ref="formEdit" :model="formEdit" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
             <el-form-item label="真实姓名" prop="realName">
               <el-input v-model="formEdit.realName" placeholder="请输入真实姓名" />
             </el-form-item>
          </el-col>
          <el-col :span="12">
             <el-form-item label="手机号码" prop="phonenumber">
               <el-input v-model="formEdit.phonenumber" placeholder="请输入手机号码" />
             </el-form-item>
          </el-col>
        </el-row>


        <el-form-item label="备注" prop="remark">
          <el-input v-model="formEdit.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>

        <el-divider content-position="left">名下房产信息</el-divider>

        <div style="margin-bottom: 10px;">
            <el-button type="primary" size="mini" icon="el-icon-plus" @click="showAddPropertyForm" v-if="!isAddingProperty">新增房产</el-button>
        </div>

        <!-- Add Property Form -->
        <div v-if="isAddingProperty" class="add-property-box" style="background: #f8f8f9; padding: 15px; margin-bottom: 15px; border-radius: 4px; border: 1px solid #ebeef5;">
            <el-form :model="newProperty" ref="propertyForm" label-width="80px" :inline="true" size="small">
                <el-form-item label="所属小区">
                   <el-input v-model="formEdit.communityName" disabled style="width: 150px;"></el-input>
                </el-form-item>
                <el-form-item label="楼栋号">
                  <el-select v-model="newProperty.buildingNo" placeholder="楼栋" filterable @change="handleNewBuildingChange" style="width: 120px;">
                    <el-option v-for="item in buildingOptions" :key="item" :label="item" :value="item" />
                  </el-select>
                </el-form-item>
                <el-form-item label="房号">
                  <el-select v-model="newProperty.roomNo" placeholder="房号" filterable style="width: 120px;">
                    <el-option v-for="item in roomOptions" :key="item" :label="item" :value="item" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitNewProperty">保存</el-button>
                    <el-button @click="cancelNewProperty">取消</el-button>
                </el-form-item>
            </el-form>
        </div>

        <!-- Property List Table -->
        <el-table :data="ownerPropertyList" style="width: 100%; margin-bottom: 10px;" size="mini" border>
            <el-table-column prop="communityName" label="小区" width="150"></el-table-column>
            <el-table-column prop="buildingName" label="楼栋" width="100"></el-table-column>
            <el-table-column prop="roomNumber" label="房号" width="100"></el-table-column>
            <el-table-column prop="area" label="面积(㎡)" width="100"></el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-delete" class="text-danger" @click="handleDeleteProperty(scope.row)">移除</el-button>
                </template>
            </el-table-column>
        </el-table>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEditForm">保存基本信息</el-button>
        <el-button @click="cancelEdit">关 闭</el-button>
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

    <!-- 房产合并与拆分弹窗 -->
    <property-transfer-dialog ref="transferDialog" @refresh="getList" />

  </div>
</template>

<script>
import { listOwner, getOwner, delOwner, addOwner, updateOwner, changeUserStatus, changeUserIdentity, addOwnerProperty } from "@/api/system/owner";
import { listEstateUserProperty, delEstateUserProperty } from "@/api/system/estateUserProperty";
import { listBuildings, listRooms } from "@/api/system/estateProperty";
import { getToken } from "@/utils/auth";
import PropertyTransferDialog from "./PropertyTransferDialog";
import { mapGetters } from "vuex";

export default {
  name: "Owner",
  components: {
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
      // 业主房产列表
      ownerPropertyList: [],
      // 是否正在新增房产
      isAddingProperty: false,
      // 新增房产数据
      newProperty: {
        buildingNo: null,
        roomNo: null
      },

      // 新增弹窗
      openAdd: false,
      titleAdd: "添加业主信息",
      formAdd: {},

      // 修改弹窗
      openEdit: false,
      titleEdit: "修改业主信息",
      formEdit: {},

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
      // 自定义字典：业主绑定状态
      sys_owner_bind_status: [
        { value: '0', label: '正常' },
        { value: '1', label: '停用' },
        { value: '2', label: '未绑定' }
      ]
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
    // 取消按钮 (新增)
    cancelAdd() {
      this.openAdd = false;
      this.resetAdd();
    },
    // 取消按钮 (修改)
    cancelEdit() {
      this.openEdit = false;
      this.resetEdit();
    },
    // 表单重置 (新增)
    resetAdd() {
      this.formAdd = {
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
      this.buildingOptions = [];
      this.roomOptions = [];
      this.resetForm("formAdd");
    },
    // 表单重置 (修改)
    resetEdit() {
      this.formEdit = {
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
      this.buildingOptions = [];
      this.roomOptions = [];
      this.resetForm("formEdit");
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
      this.resetAdd();
      this.attachCommunityInfo(this.formAdd, this.currentCommunityId);
      this.getBuildingList(this.formAdd.communityId);
      this.openAdd = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.resetEdit();
      const ownerId = row.ownerId || this.ids[0]
      getOwner(ownerId).then(response => {
        this.formEdit = response.data;
        this.attachCommunityInfo(this.formEdit, response.data.communityId);

        if(this.formEdit.communityId) {
            this.getBuildingList(this.formEdit.communityId);
            this.loadOwnerProperties();
        }

        this.openEdit = true;
      });
    },
    /** 加载业主房产列表 */
    loadOwnerProperties() {
        if (!this.formEdit.ownerId) { // Change check to ownerId or just proceed
            this.ownerPropertyList = [];
            return;
        }
        listEstateUserProperty({
            userId: this.formEdit.userId,
            communityId: this.formEdit.communityId,
            ownerNo: this.formEdit.ownerNo // Pass ownerNo
        }).then(res => {
            this.ownerPropertyList = res.rows;
        });
    },
    /** 显示新增房产表单 */
    showAddPropertyForm() {
        this.isAddingProperty = true;
        this.newProperty = { buildingNo: null, roomNo: null };
        this.roomOptions = []; // Clear options for the new select
        // Ensure building options are loaded (should be loaded in handleUpdate)
    },
    /** 新增房产楼栋变更 */
    handleNewBuildingChange(value) {
        this.newProperty.roomNo = null;
        if (value && this.formEdit.communityId) {
            this.getRoomList(this.formEdit.communityId, value);
        }
    },
    /** 提交新增房产 */
    submitNewProperty() {
        if (!this.newProperty.buildingNo || !this.newProperty.roomNo) {
            this.$message.error("请选择楼栋和房号");
            return;
        }

        // 构建新增数据
        const data = {
            ownerId: this.formEdit.ownerId,
            communityId: this.formEdit.communityId,
            buildingNo: this.newProperty.buildingNo,
            roomNo: this.newProperty.roomNo
        };

        addOwnerProperty(data).then(res => {
            this.$message.success("房产添加成功");
            this.isAddingProperty = false;
            this.loadOwnerProperties(); // 刷新列表
            this.getList(); // 刷新主列表
        });
    },
    /** 取消新增房产 */
    cancelNewProperty() {
        this.isAddingProperty = false;
    },
    /** 删除房产 */
    handleDeleteProperty(row) {
        this.$confirm('确认要移除该房产吗？', '提示', {
            type: 'warning'
        }).then(() => {
            delEstateUserProperty(row.associationId).then(res => {
                this.$message.success("移除成功");
                this.loadOwnerProperties();
                this.getList();
            });
        }).catch(() => {});
    },
    /** 获取楼栋列表 */
    getBuildingList(communityId) {
      listBuildings({ communityId: communityId }).then(response => {
        this.buildingOptions = response.data;
      });
    },
    /** 获取房号列表 */
    getRoomList(communityId, buildingName) {
      listRooms({
        communityId: communityId,
        buildingName: buildingName,
        // ownerId: this.form.ownerId // Remove this dependency as it might confuse backend or not needed for listing rooms
      }).then(response => {
        this.roomOptions = response.data;
      });
    },
    /** 楼栋改变事件 (Add) */
    handleBuildingChangeAdd(value) {
      this.formAdd.roomNo = null;
      this.roomOptions = [];
      if (value) {
        this.getRoomList(this.formAdd.communityId, value);
      }
    },
    /** 提交按钮 (新增) */
    submitAddForm() {
      this.$refs["formAdd"].validate(valid => {
        if (valid) {
          if (this.formAdd.communityId === null || this.formAdd.communityId === undefined) {
            if (!this.isAllCommunitySelected) {
              this.formAdd.communityId = Number(this.currentCommunityId);
              this.formAdd.communityName = this.resolveCommunityName(this.formAdd.communityId);
            }
          }
          const numericId = Number(this.formAdd.communityId);
          if (Number.isNaN(numericId) || numericId === 0) {
            this.$message.error("请先为业主选择所属小区。");
            return;
          }
          this.formAdd.communityId = numericId;

          addOwner(this.formAdd).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.openAdd = false;
              this.getList();
          });
        }
      });
    },
    /** 提交按钮 (修改) */
    submitEditForm() {
      this.$refs["formEdit"].validate(valid => {
        if (valid) {
          const numericId = Number(this.formEdit.communityId);
          if (Number.isNaN(numericId) || numericId === 0) {
            this.$message.error("数据异常：缺少小区信息。");
            return;
          }
          this.formEdit.communityId = numericId;

          updateOwner(this.formEdit).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.openEdit = false;
              this.getList();
          });
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
