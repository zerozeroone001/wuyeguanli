<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :model="queryParams"
      size="small"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="小区名称" prop="communityName">
        <el-input
          v-model="queryParams.communityName"
          placeholder="请输入小区名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="楼栋" prop="buildingName">
        <el-input
          v-model="queryParams.buildingName"
          placeholder="请输入楼栋"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单元" prop="unitName">
        <el-input
          v-model="queryParams.unitName"
          placeholder="请输入单元"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="门牌号" prop="roomNumber">
        <el-input
          v-model="queryParams.roomNumber"
          placeholder="请输入门牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择审核状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_auth_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户信息" prop="keyword">
        <el-input
          v-model="queryParams.keyword"
          placeholder="请输入姓名或手机号"
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
          type="success"
          plain
          icon="el-icon-check"
          size="mini"
          :disabled="multiple"
          @click="openBatchAudit('1')"
          v-hasPermi="['system:owner:audit']"
        >批量通过</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-close"
          size="mini"
          :disabled="multiple"
          @click="openBatchAudit('2')"
          v-hasPermi="['system:owner:audit']"
        >批量驳回</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table
      v-loading="loading"
      :data="dataList"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="小区" prop="communityName" align="center" min-width="140" show-overflow-tooltip />
      <el-table-column label="楼栋" prop="buildingName" align="center" min-width="100" />
      <el-table-column label="单元" prop="unitName" align="center" min-width="100" />
      <el-table-column label="门牌号" prop="roomNumber" align="center" min-width="100" />
      <el-table-column label="申请人" align="center" min-width="160">
        <template slot-scope="scope">
          <div class="user-info">
            <div class="user-name">{{ scope.row.realName || scope.row.nickName || '-' }}</div>
            <div class="user-extra">{{ scope.row.phonenumber || scope.row.userName || '-' }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" prop="status" align="center" min-width="110">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_auth_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="提交时间" prop="createTime" align="center" min-width="160">
        <template slot-scope="scope">
          {{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="openAudit(scope.row)"
            v-if="scope.row.status === '0'"
            v-hasPermi="['system:owner:audit']"
          >审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="openDetail(scope.row)"
          >详情</el-button>
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

    <el-dialog :title="auditTitle" :visible.sync="auditOpen" width="520px" append-to-body>
      <el-form ref="auditForm" :model="auditData" :rules="auditRules" label-width="90px">
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="auditData.status">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="auditData.remark"
            type="textarea"
            :rows="3"
            maxlength="200"
            show-word-limit
            placeholder="请输入审核备注"
          />
        </el-form-item>
        <el-alert
          v-if="auditData.status === '1'"
          title="审核通过后系统将自动将用户标记为业主，并补全业主档案。"
          type="info"
          :closable="false"
          show-icon
        />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="submitLoading" @click="submitAudit">确 定</el-button>
        <el-button @click="cancelAudit">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="申请详情" :visible.sync="detailOpen" width="520px" append-to-body>
      <el-descriptions :column="1" border size="small">
        <el-descriptions-item label="小区">{{ currentRow.communityName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="楼栋">{{ currentRow.buildingName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="单元">{{ currentRow.unitName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="门牌号">{{ currentRow.roomNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ currentRow.realName || currentRow.nickName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentRow.phonenumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <dict-tag :options="dict.type.sys_auth_status" :value="currentRow.status" />
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ currentRow.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">
          {{ parseTime(currentRow.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
          {{ parseTime(currentRow.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') || '-' }}
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listEstateUserProperty,
  getEstateUserProperty,
  auditEstateUserProperty
} from '@/api/system/estateUserProperty'

export default {
  name: 'HouseAudit',
  dicts: ['sys_auth_status'],
  data() {
    return {
      loading: false,
      submitLoading: false,
      showSearch: true,
      dataList: [],
      total: 0,
      ids: [],
      single: true,
      multiple: true,
      auditOpen: false,
      auditTitle: '房屋审核',
      auditData: {
        associationId: null,
        status: '1',
        remark: ''
      },
      auditRules: {
        status: [{ required: true, message: '请选择审核结果', trigger: 'change' }]
      },
      detailOpen: false,
      currentRow: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        communityName: '',
        buildingName: '',
        unitName: '',
        roomNumber: '',
        status: '',
        keyword: ''
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (!params.keyword) {
        delete params.keyword
      }
      listEstateUserProperty(params)
        .then(res => {
          this.dataList = res.rows || []
          this.total = res.total || 0
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.associationId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    openAudit(row) {
      this.auditTitle = `房屋审核 - ${row.communityName || ''}${row.buildingName ? ' · ' + row.buildingName : ''}${row.unitName ? ' · ' + row.unitName : ''}${row.roomNumber ? ' · ' + row.roomNumber : ''}`
      this.auditData = {
        associationId: row.associationId,
        status: '1',
        remark: ''
      }
      this.auditOpen = true
      this.$nextTick(() => this.resetForm('auditForm'))
    },
    openBatchAudit(status) {
      if (!this.ids.length) {
        this.$modal.msgWarning('请选择需要审核的记录')
        return
      }
      this.auditTitle = status === '1' ? '批量通过' : '批量驳回'
      this.auditData = {
        associationId: null,
        status,
        remark: ''
      }
      this.auditOpen = true
      this.$nextTick(() => this.resetForm('auditForm'))
    },
    cancelAudit() {
      this.auditOpen = false
      this.submitLoading = false
    },
    submitAudit() {
      this.$refs.auditForm.validate(valid => {
        if (!valid) return
        const targets = this.auditData.associationId ? [this.auditData.associationId] : this.ids
        if (!targets.length) {
          this.$modal.msgWarning('未选择任何记录')
          return
        }
        this.submitLoading = true
        Promise.all(
          targets.map(id =>
            auditEstateUserProperty({
              associationId: id,
              status: this.auditData.status,
              remark: this.auditData.remark
            })
          )
        )
          .then(() => {
            const msg = this.auditData.status === '1' ? '审核通过成功' : '审核驳回成功'
            this.$modal.msgSuccess(msg)
            this.auditOpen = false
            this.getList()
          })
          .finally(() => {
            this.submitLoading = false
          })
      })
    },
    openDetail(row) {
      getEstateUserProperty(row.associationId).then(res => {
        this.currentRow = res.data || row
        this.detailOpen = true
      })
    }
  }
}
</script>

<style scoped>
.user-info {
  display: flex;
  flex-direction: column;
  line-height: 1.4;
}

.user-name {
  font-weight: 500;
  color: #303133;
}

.user-extra {
  font-size: 12px;
  color: #909399;
}
</style>
