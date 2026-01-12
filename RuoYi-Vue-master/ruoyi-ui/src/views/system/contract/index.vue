<template>
  <div class="app-container">
    <!-- Tab页签 -->
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="合同列表" name="contract">
        <!-- 合同列表查询 -->
        <el-form :model="contractQuery" ref="contractQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="合同名称" prop="contractName">
            <el-input v-model="contractQuery.contractName" placeholder="请输入合同名称" clearable @keyup.enter.native="handleContractQuery" />
          </el-form-item>
          <el-form-item label="合同类型" prop="contractType">
            <el-select v-model="contractQuery.contractType" placeholder="请选择" clearable>
              <el-option v-for="dict in dict.type.sys_contract_type" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="合同阶段" prop="contractStage">
            <el-select v-model="contractQuery.contractStage" placeholder="请选择" clearable>
              <el-option v-for="dict in dict.type.sys_contract_stage" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleContractQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetContractQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- 操作按钮 -->
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddContract" v-hasPermi="['system:contract:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="contractSingle" @click="handleUpdateContract" v-hasPermi="['system:contract:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="contractMultiple" @click="handleDeleteContract" v-hasPermi="['system:contract:remove']">删除</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getContractList"></right-toolbar>
        </el-row>

        <!-- 合同列表表格 -->
        <el-table v-loading="contractLoading" :data="contractList" @selection-change="handleContractSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="合同编号" align="center" prop="contractNo" width="120" />
          <el-table-column label="合同名称" align="center" prop="contractName" min-width="150" show-overflow-tooltip />
          <el-table-column label="合同类型" align="center" prop="contractType" width="100">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.sys_contract_type" :value="scope.row.contractType" />
            </template>
          </el-table-column>
          <el-table-column label="合同阶段" align="center" prop="contractStage" width="160">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.sys_contract_stage" :value="scope.row.contractStage" />
            </template>
          </el-table-column>
          <el-table-column label="甲方" align="center" prop="partyA" width="120" show-overflow-tooltip />
          <el-table-column label="乙方" align="center" prop="partyB" width="120" show-overflow-tooltip />
          <el-table-column label="合同金额" align="center" prop="contractAmount" width="100" />
          <el-table-column label="生效日期" align="center" prop="effectiveDate" width="100">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.effectiveDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="320" fixed="right">
            <template slot-scope="scope">
              <!-- 详情按钮始终显示 -->
              <el-button size="mini" type="text" icon="el-icon-view" @click="handleViewContract(scope.row)">详情</el-button>

              <!-- 待审核状态：显示审核按钮 -->
              <el-button v-if="scope.row.contractStage === 'pending_accept'" size="mini" type="text" icon="el-icon-check" @click="handleReviewContract(scope.row)" v-hasPermi="['system:contract:edit']">审核</el-button>

              <!-- 待制作查验清单状态：显示上传查验清单按钮 -->
              <el-button v-if="scope.row.contractStage === 'pending_entry_checklist'" size="mini" type="text" icon="el-icon-upload2" @click="handleUploadEntryChecklist(scope.row)" v-hasPermi="['system:contract:edit']">上传查验清单</el-button>

              <!-- 待上传物业查验清单状态：显示下载查验清单和上传物业查验清单按钮 -->
              <el-button v-if="scope.row.contractStage === 'entry_checklist_uploaded'" size="mini" type="text" icon="el-icon-download" @click="handleDownloadFile(scope.row.entryChecklistUrl)">下载查验清单</el-button>
              <el-button v-if="scope.row.contractStage === 'entry_checklist_uploaded'" size="mini" type="text" icon="el-icon-upload2" @click="handleUploadPropertyEntryChecklist(scope.row)" v-hasPermi="['system:contract:edit']">上传物业查验清单</el-button>

              <!-- 待上传合同月履行清单状态：显示上传合同月履行清单按钮 -->
              <el-button v-if="scope.row.contractStage === 'pending_monthly_checklist'" size="mini" type="text" icon="el-icon-upload2" @click="handleUploadMonthlyChecklist(scope.row)" v-hasPermi="['system:contract:edit']">上传合同月履行清单</el-button>

              <!-- 合同月履行清单已上传状态：显示下载月履行清单和上传物业月履行清单按钮 -->
              <el-button v-if="scope.row.contractStage === 'monthly_checklist_uploaded'" size="mini" type="text" icon="el-icon-download" @click="handleDownloadFile(scope.row.monthlyChecklistUrl)">下载月履行清单</el-button>
              <el-button v-if="scope.row.contractStage === 'monthly_checklist_uploaded'" size="mini" type="text" icon="el-icon-upload2" @click="handleUploadPropertyMonthlyChecklist(scope.row)" v-hasPermi="['system:contract:edit']">上传物业月履行清单</el-button>

              <!-- 合同月履行清单待审核状态：显示审核按钮 -->
              <el-button v-if="scope.row.contractStage === 'monthly_pending_review'" size="mini" type="text" icon="el-icon-check" @click="handleReviewMonthlyChecklist(scope.row)" v-hasPermi="['system:contract:edit']">月履行清单审核</el-button>

              <!-- 待上传整改通知单状态 -->
              <el-button v-if="scope.row.contractStage === 'pending_rectification_notice'" size="mini" type="text" icon="el-icon-upload2" @click="handleUploadRectificationNotice(scope.row)" v-hasPermi="['system:contract:edit']">上传整改通知单</el-button>

              <!-- 待上传整改结果告知单状态 -->
              <el-button v-if="scope.row.contractStage === 'pending_rectification_result'" size="mini" type="text" icon="el-icon-upload2" @click="handleUploadRectificationResult(scope.row)" v-hasPermi="['system:contract:edit']">上传整改结果评定通知书</el-button>

              <!-- 待上传年度履行报告状态 -->
              <el-button v-if="scope.row.contractStage === 'pending_annual_report'" size="mini" type="text" icon="el-icon-upload2" @click="handleUploadAnnualReport(scope.row)" v-hasPermi="['system:contract:edit']">上传年度履行报告</el-button>

              <!-- 修改和删除按钮 -->
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateContract(scope.row)" v-hasPermi="['system:contract:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteContract(scope.row)" v-hasPermi="['system:contract:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="contractTotal > 0" :total="contractTotal" :page.sync="contractQuery.pageNum" :limit.sync="contractQuery.pageSize" @pagination="getContractList" />
      </el-tab-pane>

<!--      <el-tab-pane label="进驻查验" name="entryChecklist">-->
<!--        <el-form :model="entryQuery" ref="entryQueryForm" size="small" :inline="true" label-width="68px">-->
<!--          <el-form-item label="清单名称" prop="checklistName">-->
<!--            <el-input v-model="entryQuery.checklistName" placeholder="请输入清单名称" clearable @keyup.enter.native="getEntryList" />-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary" icon="el-icon-search" size="mini" @click="getEntryList">搜索</el-button>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--        <el-row :gutter="10" class="mb8">-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddEntry" v-hasPermi="['system:contract:add']">新增</el-button>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-table v-loading="entryLoading" :data="entryList">-->
<!--          <el-table-column label="ID" align="center" prop="checklistId" width="60" />-->
<!--          <el-table-column label="清单名称" align="center" prop="checklistName" min-width="150" show-overflow-tooltip />-->
<!--          <el-table-column label="关联合同" align="center" prop="contractId" width="100" />-->
<!--          <el-table-column label="状态" align="center" prop="status" width="100">-->
<!--            <template slot-scope="scope">-->
<!--              <el-tag :type="scope.row.status === '3' ? 'success' : (scope.row.status === '1' ? 'primary' : 'info')">-->
<!--                {{ scope.row.status === '0' ? '草稿' : scope.row.status === '1' ? '已发布' : scope.row.status === '2' ? '物业已下载' : '物业已上传' }}-->
<!--              </el-tag>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="上传时间" align="center" prop="uploadTime" width="160" />-->
<!--          <el-table-column label="操作" align="center" width="150">-->
<!--            <template slot-scope="scope">-->
<!--              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateEntry(scope.row)">修改</el-button>-->
<!--              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteEntry(scope.row)">删除</el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--        <pagination v-show="entryTotal > 0" :total="entryTotal" :page.sync="entryQuery.pageNum" :limit.sync="entryQuery.pageSize" @pagination="getEntryList" />-->
<!--      </el-tab-pane>-->

<!--      <el-tab-pane label="月履行管理" name="monthlyChecklist">-->
<!--        <el-form :model="monthlyQuery" ref="monthlyQueryForm" size="small" :inline="true" label-width="68px">-->
<!--          <el-form-item label="年月" prop="yearMonth">-->
<!--            <el-date-picker v-model="monthlyQuery.yearMonth" type="month" value-format="yyyy-MM" placeholder="选择年月" />-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary" icon="el-icon-search" size="mini" @click="getMonthlyList">搜索</el-button>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--        <el-row :gutter="10" class="mb8">-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddMonthly" v-hasPermi="['system:contract:add']">新增</el-button>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-table v-loading="monthlyLoading" :data="monthlyList">-->
<!--          <el-table-column label="ID" align="center" prop="checklistId" width="60" />-->
<!--          <el-table-column label="年月" align="center" prop="yearMonth" width="100" />-->
<!--          <el-table-column label="清单名称" align="center" prop="checklistName" min-width="150" show-overflow-tooltip />-->
<!--          <el-table-column label="履行状态" align="center" prop="performanceStatus" width="100">-->
<!--            <template slot-scope="scope">-->
<!--              <dict-tag :options="performanceStatusOptions" :value="scope.row.performanceStatus" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="状态" align="center" prop="status" width="100">-->
<!--            <template slot-scope="scope">-->
<!--              <el-tag :type="scope.row.status === '3' ? 'success' : 'info'">-->
<!--                {{ scope.row.status === '0' ? '草稿' : scope.row.status === '1' ? '已发布' : scope.row.status === '2' ? '物业已下载' : '物业已上传' }}-->
<!--              </el-tag>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="操作" align="center" width="150">-->
<!--            <template slot-scope="scope">-->
<!--              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateMonthly(scope.row)">修改</el-button>-->
<!--              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteMonthly(scope.row)">删除</el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--        <pagination v-show="monthlyTotal > 0" :total="monthlyTotal" :page.sync="monthlyQuery.pageNum" :limit.sync="monthlyQuery.pageSize" @pagination="getMonthlyList" />-->
<!--      </el-tab-pane>-->

<!--      <el-tab-pane label="考核管理" name="assessment">-->
<!--        <el-form :model="assessmentQuery" ref="assessmentQueryForm" size="small" :inline="true" label-width="68px">-->
<!--          <el-form-item label="考核年度" prop="assessmentYear">-->
<!--            <el-date-picker v-model="assessmentQuery.assessmentYear" type="year" value-format="yyyy" placeholder="选择年度" />-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary" icon="el-icon-search" size="mini" @click="getAssessmentList">搜索</el-button>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--        <el-row :gutter="10" class="mb8">-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddAssessment" v-hasPermi="['system:contract:add']">新增考核</el-button>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-table v-loading="assessmentLoading" :data="assessmentList">-->
<!--          <el-table-column label="ID" align="center" prop="assessmentId" width="60" />-->
<!--          <el-table-column label="考核年度" align="center" prop="assessmentYear" width="80" />-->
<!--          <el-table-column label="考核名称" align="center" prop="assessmentName" min-width="150" show-overflow-tooltip />-->
<!--          <el-table-column label="满意度平均分" align="center" prop="satisfactionAvgScore" width="110">-->
<!--            <template slot-scope="scope">-->
<!--              <el-rate v-model="scope.row.satisfactionAvgScore" disabled show-score text-color="#ff9900" :max="5" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="完成义务平均分" align="center" prop="obligationAvgScore" width="120">-->
<!--            <template slot-scope="scope">-->
<!--              <el-rate v-model="scope.row.obligationAvgScore" disabled show-score text-color="#ff9900" :max="5" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="参与人数" align="center" prop="totalParticipants" width="80" />-->
<!--          <el-table-column label="状态" align="center" prop="status" width="80">-->
<!--            <template slot-scope="scope">-->
<!--              <dict-tag :options="assessmentStatusOptions" :value="scope.row.status" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="操作" align="center" width="180">-->
<!--            <template slot-scope="scope">-->
<!--              <el-button size="mini" type="text" icon="el-icon-view" @click="handleViewScores(scope.row)">评分详情</el-button>-->
<!--              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateAssessment(scope.row)">修改</el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--        <pagination v-show="assessmentTotal > 0" :total="assessmentTotal" :page.sync="assessmentQuery.pageNum" :limit.sync="assessmentQuery.pageSize" @pagination="getAssessmentList" />-->
<!--      </el-tab-pane>-->

<!--      <el-tab-pane label="整改记录" name="rectification">-->
<!--        <el-form :model="rectificationQuery" ref="rectificationQueryForm" size="small" :inline="true" label-width="68px">-->
<!--          <el-form-item label="状态" prop="status">-->
<!--            <el-select v-model="rectificationQuery.status" placeholder="请选择" clearable>-->
<!--              <el-option v-for="dict in rectificationStatusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary" icon="el-icon-search" size="mini" @click="getRectificationList">搜索</el-button>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--        <el-row :gutter="10" class="mb8">-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddRectification" v-hasPermi="['system:contract:add']">新增整改</el-button>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-table v-loading="rectificationLoading" :data="rectificationList">-->
<!--          <el-table-column label="ID" align="center" prop="rectificationId" width="60" />-->
<!--          <el-table-column label="来源类型" align="center" prop="sourceType" width="100">-->
<!--            <template slot-scope="scope">-->
<!--              {{ scope.row.sourceType === 'monthly_checklist' ? '月履行' : '考核' }}-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="整改通知内容" align="center" prop="noticeContent" min-width="200" show-overflow-tooltip />-->
<!--          <el-table-column label="通知时间" align="center" prop="noticeTime" width="160" />-->
<!--          <el-table-column label="状态" align="center" prop="status" width="80">-->
<!--            <template slot-scope="scope">-->
<!--              <dict-tag :options="rectificationStatusOptions" :value="scope.row.status" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="操作" align="center" width="150">-->
<!--            <template slot-scope="scope">-->
<!--              <el-button size="mini" type="text" icon="el-icon-view" @click="handleViewRectification(scope.row)">详情</el-button>-->
<!--              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateRectification(scope.row)">修改</el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--        <pagination v-show="rectificationTotal > 0" :total="rectificationTotal" :page.sync="rectificationQuery.pageNum" :limit.sync="rectificationQuery.pageSize" @pagination="getRectificationList" />-->
<!--      </el-tab-pane>-->
    </el-tabs>

    <!-- 添加或修改合同对话框 -->
    <el-dialog :title="contractTitle" :visible.sync="contractOpen" width="800px" append-to-body>
      <el-form ref="contractForm" :model="contractForm" :rules="contractRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属小区" prop="communityId">
              <el-select v-model="contractForm.communityId" placeholder="请选择小区" style="width:100%" filterable>
                <el-option v-for="item in communityList" :key="item.communityId" :label="item.communityName" :value="item.communityId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同类型" prop="contractType">
              <el-select v-model="contractForm.contractType" placeholder="请选择" style="width:100%">
                <el-option v-for="dict in dict.type.sys_contract_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="contractForm.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同金额" prop="contractAmount">
              <el-input-number v-model="contractForm.contractAmount" :precision="2" :step="100" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="甲方" prop="partyA">
              <el-input v-model="contractForm.partyA" placeholder="请输入甲方" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="乙方" prop="partyB">
              <el-input v-model="contractForm.partyB" placeholder="请输入乙方" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker clearable v-model="contractForm.effectiveDate" style="width: 100%" type="date" value-format="yyyy-MM-dd" placeholder="请选择生效日期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="expiryDate">
              <el-date-picker clearable v-model="contractForm.expiryDate" style="width: 100%" type="date" value-format="yyyy-MM-dd" placeholder="请选择失效日期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="合同内容" prop="contractContent">
          <editor v-model="contractForm.contractContent" :min-height="192" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="contractForm.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitContractForm">确 定</el-button>
        <el-button @click="contractOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 合同详情对话框 -->
    <el-dialog title="合同详情" :visible.sync="detailDialogOpen" width="900px" append-to-body>
      <el-tabs v-model="detailActiveTab">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="合同编号">{{ contractDetail.contractNo }}</el-descriptions-item>
            <el-descriptions-item label="合同名称">{{ contractDetail.contractName }}</el-descriptions-item>
            <el-descriptions-item label="合同类型">
              <dict-tag :options="dict.type.sys_contract_type" :value="contractDetail.contractType" />
            </el-descriptions-item>
            <el-descriptions-item label="合同阶段">
              <dict-tag :options="dict.type.sys_contract_stage" :value="contractDetail.contractStage" />
            </el-descriptions-item>
            <el-descriptions-item label="甲方">{{ contractDetail.partyA }}</el-descriptions-item>
            <el-descriptions-item label="乙方">{{ contractDetail.partyB }}</el-descriptions-item>
            <el-descriptions-item label="合同金额">{{ contractDetail.contractAmount }}</el-descriptions-item>
            <el-descriptions-item label="生效日期">{{ parseTime(contractDetail.effectiveDate, '{y}-{m}-{d}') }}</el-descriptions-item>
            <el-descriptions-item label="失效日期">{{ parseTime(contractDetail.expiryDate, '{y}-{m}-{d}') }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ parseTime(contractDetail.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="合同内容" :span="2">
              <div v-html="contractDetail.contractContent"></div>
            </el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ contractDetail.remark }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 状态流转 -->
        <el-tab-pane label="状态流转" name="timeline">
          <el-timeline>
            <el-timeline-item v-if="contractDetail.createTime" timestamp="" placement="top">
              <el-card>
                <h4>合同创建</h4>
                <p>时间：{{ parseTime(contractDetail.createTime) }}</p>
                <p>创建人：{{ contractDetail.createBy }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item v-if="contractDetail.applyTime" timestamp="" placement="top">
              <el-card>
                <h4>合同申请</h4>
                <p>时间：{{ parseTime(contractDetail.applyTime) }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item v-if="contractDetail.acceptTime" timestamp="" placement="top">
              <el-card>
                <h4>合同受理</h4>
                <p>时间：{{ parseTime(contractDetail.acceptTime) }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item v-if="contractDetail.completeTime" timestamp="" placement="top">
              <el-card>
                <h4>审核完成</h4>
                <p>时间：{{ parseTime(contractDetail.completeTime) }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item v-if="contractDetail.entryChecklistUrl" timestamp="" placement="top">
              <el-card>
                <h4>查验清单已上传</h4>
                <p>时间：{{ parseTime(contractDetail.updateTime) }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item v-if="contractDetail.propertyEntryChecklistUrl" timestamp="" placement="top">
              <el-card>
                <h4>物业查验清单已上传</h4>
                <p>时间：{{ parseTime(contractDetail.updateTime) }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item v-if="contractDetail.propertyMonthlyChecklistUrl" timestamp="" placement="top">
              <el-card>
                <h4>物业月履行清单已上传</h4>
                <p>时间：{{ parseTime(contractDetail.updateTime) }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item v-if="contractDetail.rectificationNoticeUrl" timestamp="" placement="top">
              <el-card>
                <h4>整改通知单已上传</h4>
                <p>时间：{{ parseTime(contractDetail.updateTime) }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item v-if="contractDetail.rectificationResultUrl" timestamp="" placement="top">
              <el-card>
                <h4>整改结果评定通知书已上传</h4>
                <p>时间：{{ parseTime(contractDetail.updateTime) }}</p>
              </el-card>
            </el-timeline-item>
            <el-timeline-item v-if="contractDetail.annualReportUrl" timestamp="" placement="top">
              <el-card>
                <h4>年度履行报告已上传</h4>
                <p>时间：{{ parseTime(contractDetail.updateTime) }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>

        <!-- 文件列表 -->
        <el-tab-pane label="文件列表" name="files">
          <el-table :data="contractFiles" style="width: 100%">
            <el-table-column prop="fileName" label="文件名称" min-width="200" show-overflow-tooltip />
            <el-table-column prop="fileType" label="文件类型" width="150" />
            <el-table-column label="操作" width="200" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreviewFile(scope.row.fileUrl)">预览</el-button>
                <el-button size="mini" type="text" icon="el-icon-download" @click="handleDownloadFile(scope.row.fileUrl)">下载</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 考核评分详情对话框 -->
    <el-dialog title="评分详情" :visible.sync="scoreDialogOpen" width="700px" append-to-body>
      <el-table :data="scoreList" v-loading="scoreLoading">
        <el-table-column label="业主ID" align="center" prop="userId" width="80" />
        <el-table-column label="满意度评分" align="center" prop="satisfactionScore" width="120">
          <template slot-scope="scope">
            <el-rate v-model="scope.row.satisfactionScore" disabled :max="5" />
          </template>
        </el-table-column>
        <el-table-column label="满意度备注" align="center" prop="satisfactionComment" show-overflow-tooltip />
        <el-table-column label="义务评分" align="center" prop="obligationScore" width="120">
          <template slot-scope="scope">
            <el-rate v-model="scope.row.obligationScore" disabled :max="5" />
          </template>
        </el-table-column>
        <el-table-column label="义务备注" align="center" prop="obligationComment" show-overflow-tooltip />
        <el-table-column label="提交时间" align="center" prop="submitTime" width="160" />
      </el-table>
    </el-dialog>

    <!-- 审核弹窗（待审核状态） -->
    <el-dialog title="合同审核" :visible.sync="reviewDialogOpen" width="400px" append-to-body>
      <div style="text-align: center; padding: 20px 0;">
        <p style="margin-bottom: 20px;">确认完成合同审核？</p>
        <el-button type="primary" @click="confirmReviewContract">{{ reviewButtonText }}</el-button>
      </div>
    </el-dialog>

    <!-- 月履行清单审核弹窗 -->
    <el-dialog title="合同月履行清单审核" :visible.sync="monthlyReviewDialogOpen" width="500px" append-to-body>
      <div style="text-align: center; padding: 20px 0;">
        <p style="margin-bottom: 20px;">请选择审核结果：</p>
        <el-button type="danger" @click="confirmMonthlyReview('incomplete')" style="margin-right: 20px;">未履行/未完全履行</el-button>
        <el-button type="success" @click="confirmMonthlyReview('complete')">完全履行</el-button>
      </div>
    </el-dialog>

    <!-- 文件上传弹窗 -->
    <el-dialog :title="uploadDialogTitle" :visible.sync="uploadDialogOpen" width="500px" append-to-body>
      <el-upload
        class="upload-demo"
        :action="uploadUrl"
        :headers="uploadHeaders"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :limit="1"
        :file-list="uploadFileList">
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">支持任意文件类型</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="uploadDialogOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listContract, getContract, addContract, updateContract, delContract, reviewContract, uploadEntryChecklist, uploadPropertyEntryChecklist, uploadMonthlyChecklist, uploadPropertyMonthlyChecklist, reviewMonthlyChecklist, uploadRectificationNotice, uploadRectificationResult, uploadAnnualReport, listEntryChecklist, delEntryChecklist, listMonthlyChecklist, delMonthlyChecklist, listAssessment, getScoresByAssessment, listRectification } from "@/api/system/contract";
import { listCommunity } from "@/api/system/community";
import { getToken } from "@/utils/auth";

export default {
  name: "Contract",
  dicts: ['sys_contract_type', 'sys_contract_stage', 'sys_performance_status', 'sys_assessment_status', 'sys_rectification_status'],
  data() {
    return {
      activeTab: "contract",
      showSearch: true,
      // 合同数据
      contractLoading: false,
      contractList: [],
      contractTotal: 0,
      contractIds: [],
      contractSingle: true,
      contractMultiple: true,
      contractQuery: { pageNum: 1, pageSize: 10, contractName: null, contractType: null, contractStage: null },
      contractOpen: false,
      contractTitle: "",
      contractForm: {},
      contractRules: {
        contractName: [{ required: true, message: "合同名称不能为空", trigger: "blur" }],
        communityId: [{ required: true, message: "请选择所属小区", trigger: "change" }],
        contractType: [{ required: true, message: "请选择合同类型", trigger: "change" }]
      },
      // 详情对话框
      detailDialogOpen: false,
      detailActiveTab: "basic",
      contractDetail: {},
      contractFiles: [],
      // 审核弹窗
      reviewDialogOpen: false,
      reviewContractRow: null,
      reviewButtonText: "完成审核",
      // 月履行清单审核弹窗
      monthlyReviewDialogOpen: false,
      monthlyReviewContractRow: null,
      // 文件上传弹窗
      uploadDialogOpen: false,
      uploadDialogTitle: "",
      uploadContractRow: null,
      uploadType: "",
      uploadFileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      uploadHeaders: { Authorization: "Bearer " + getToken() },
      // 进驻查验数据
      entryLoading: false,
      entryList: [],
      entryTotal: 0,
      entryQuery: { pageNum: 1, pageSize: 10, checklistName: null },
      // 月履行数据
      monthlyLoading: false,
      monthlyList: [],
      monthlyTotal: 0,
      monthlyQuery: { pageNum: 1, pageSize: 10, yearMonth: null },
      // 考核数据
      assessmentLoading: false,
      assessmentList: [],
      assessmentTotal: 0,
      assessmentQuery: { pageNum: 1, pageSize: 10, assessmentYear: null },
      // 整改数据
      rectificationLoading: false,
      rectificationList: [],
      rectificationTotal: 0,
      rectificationQuery: { pageNum: 1, pageSize: 10, status: null },
      // 评分详情
      scoreDialogOpen: false,
      scoreLoading: false,
      scoreList: [],
      // 小区列表
      communityList: []
    };
  },
  created() {
    this.getContractList();
    this.getCommunityList();
  },
  methods: {
    handleTabClick(tab) {
      if (tab.name === "contract") this.getContractList();
      else if (tab.name === "entryChecklist") this.getEntryList();
      else if (tab.name === "monthlyChecklist") this.getMonthlyList();
      else if (tab.name === "assessment") this.getAssessmentList();
      else if (tab.name === "rectification") this.getRectificationList();
    },
    // ========== 合同列表 ==========
    getContractList() {
      this.contractLoading = true;
      listContract(this.contractQuery).then(response => {
        this.contractList = response.rows;
        this.contractTotal = response.total;
        this.contractLoading = false;
      });
    },
    handleContractQuery() { this.contractQuery.pageNum = 1; this.getContractList(); },
    resetContractQuery() { this.resetForm("contractQueryForm"); this.handleContractQuery(); },
    handleContractSelectionChange(selection) {
      this.contractIds = selection.map(item => item.contractId);
      this.contractSingle = selection.length !== 1;
      this.contractMultiple = !selection.length;
    },
    handleAddContract() {
      this.resetContractForm();
      this.contractOpen = true;
      this.contractTitle = "添加物业服务合同";
    },
    handleUpdateContract(row) {
      this.resetContractForm();
      const contractId = row.contractId || this.contractIds[0];
      getContract(contractId).then(response => {
        this.contractForm = response.data;
        this.contractOpen = true;
        this.contractTitle = "修改物业服务合同";
      });
    },
    handleViewContract(row) {
      getContract(row.contractId).then(response => {
        this.contractDetail = response.data;
        this.detailActiveTab = "basic";
        // 构建文件列表
        this.contractFiles = [];
        if (this.contractDetail.entryChecklistUrl) {
          this.contractFiles.push({
            fileName: "查验清单",
            fileType: "查验清单",
            fileUrl: this.contractDetail.entryChecklistUrl
          });
        }
        if (this.contractDetail.propertyEntryChecklistUrl) {
          this.contractFiles.push({
            fileName: "物业查验清单",
            fileType: "查验清单",
            fileUrl: this.contractDetail.propertyEntryChecklistUrl
          });
        }
        if (this.contractDetail.propertyMonthlyChecklistUrl) {
          this.contractFiles.push({
            fileName: "物业月履行清单",
            fileType: "月履行清单",
            fileUrl: this.contractDetail.propertyMonthlyChecklistUrl
          });
        }
        if (this.contractDetail.rectificationNoticeUrl) {
          this.contractFiles.push({
            fileName: "整改通知单",
            fileType: "整改文件",
            fileUrl: this.contractDetail.rectificationNoticeUrl
          });
        }
        if (this.contractDetail.rectificationResultUrl) {
          this.contractFiles.push({
            fileName: "整改结果评定通知书",
            fileType: "整改文件",
            fileUrl: this.contractDetail.rectificationResultUrl
          });
        }
        if (this.contractDetail.annualReportUrl) {
          this.contractFiles.push({
            fileName: "年度履行报告",
            fileType: "年度报告",
            fileUrl: this.contractDetail.annualReportUrl
          });
        }
        this.detailDialogOpen = true;
      });
    },
    resetContractForm() {
      this.contractForm = { contractId: null, communityId: null, contractName: null, contractType: "review", partyA: null, partyB: null, effectiveDate: null, expiryDate: null, contractAmount: 0, contractContent: null, remark: null };
    },
    // 获取小区列表
    getCommunityList() {
      listCommunity({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.communityList = response.rows || [];
      });
    },
    submitContractForm() {
      this.$refs["contractForm"].validate(valid => {
        if (valid) {
          if (this.contractForm.contractId != null) {
            updateContract(this.contractForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.contractOpen = false;
              this.getContractList();
            });
          } else {
            addContract(this.contractForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.contractOpen = false;
              this.getContractList();
            });
          }
        }
      });
    },
    handleDeleteContract(row) {
      const ids = row.contractId || this.contractIds;
      this.$modal.confirm('是否确认删除？').then(() => delContract(ids)).then(() => {
        this.getContractList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // ========== 审核合同（待审核状态） ==========
    handleReviewContract(row) {
      this.reviewContractRow = row;
      // 根据合同类型设置按钮文字
      if (row.contractType === 'customize') {
        this.reviewButtonText = "完成定制";
      } else if (row.contractType === 'review') {
        this.reviewButtonText = "完成审核";
      } else if (row.contractType === 'modify') {
        this.reviewButtonText = "完成修改";
      } else {
        this.reviewButtonText = "完成审核";
      }
      this.reviewDialogOpen = true;
    },
    confirmReviewContract() {
      reviewContract(this.reviewContractRow.contractId).then(response => {
        this.$modal.msgSuccess("审核成功");
        this.reviewDialogOpen = false;
        this.getContractList();
      });
    },
    // ========== 月履行清单审核 ==========
    handleReviewMonthlyChecklist(row) {
      this.monthlyReviewContractRow = row;
      this.monthlyReviewDialogOpen = true;
    },
    confirmMonthlyReview(result) {
      reviewMonthlyChecklist(this.monthlyReviewContractRow.contractId, result).then(response => {
        this.$modal.msgSuccess("审核成功");
        this.monthlyReviewDialogOpen = false;
        this.getContractList();
      });
    },
    // ========== 文件上传相关 ==========
    handleUploadEntryChecklist(row) {
      this.uploadContractRow = row;
      this.uploadType = "entryChecklist";
      this.uploadDialogTitle = "上传查验清单";
      this.uploadFileList = [];
      this.uploadDialogOpen = true;
    },
    handleUploadPropertyEntryChecklist(row) {
      this.uploadContractRow = row;
      this.uploadType = "propertyEntryChecklist";
      this.uploadDialogTitle = "上传物业查验清单";
      this.uploadFileList = [];
      this.uploadDialogOpen = true;
    },
    handleUploadMonthlyChecklist(row) {
      this.uploadContractRow = row;
      this.uploadType = "monthlyChecklist";
      this.uploadDialogTitle = "上传合同月履行清单";
      this.uploadFileList = [];
      this.uploadDialogOpen = true;
    },
    handleUploadPropertyMonthlyChecklist(row) {
      this.uploadContractRow = row;
      this.uploadType = "propertyMonthlyChecklist";
      this.uploadDialogTitle = "上传物业合同月履行清单";
      this.uploadFileList = [];
      this.uploadDialogOpen = true;
    },
    handleUploadRectificationNotice(row) {
      this.uploadContractRow = row;
      this.uploadType = "rectificationNotice";
      this.uploadDialogTitle = "上传整改通知单";
      this.uploadFileList = [];
      this.uploadDialogOpen = true;
    },
    handleUploadRectificationResult(row) {
      this.uploadContractRow = row;
      this.uploadType = "rectificationResult";
      this.uploadDialogTitle = "上传整改结果评定通知书";
      this.uploadFileList = [];
      this.uploadDialogOpen = true;
    },
    handleUploadAnnualReport(row) {
      this.uploadContractRow = row;
      this.uploadType = "annualReport";
      this.uploadDialogTitle = "上传年度履行报告";
      this.uploadFileList = [];
      this.uploadDialogOpen = true;
    },
    beforeUpload(file) {
      // 不限制文件类型
      return true;
    },
    handleUploadSuccess(response, file) {
      if (response.code === 200) {
        const fileUrl = response.url || response.fileName;
        let uploadPromise;

        switch (this.uploadType) {
          case "entryChecklist":
            uploadPromise = uploadEntryChecklist(this.uploadContractRow.contractId, fileUrl);
            break;
          case "propertyEntryChecklist":
            uploadPromise = uploadPropertyEntryChecklist(this.uploadContractRow.contractId, fileUrl);
            break;
          case "monthlyChecklist":
            uploadPromise = uploadMonthlyChecklist(this.uploadContractRow.contractId, fileUrl);
            break;
          case "propertyMonthlyChecklist":
            uploadPromise = uploadPropertyMonthlyChecklist(this.uploadContractRow.contractId, fileUrl);
            break;
          case "rectificationNotice":
            uploadPromise = uploadRectificationNotice(this.uploadContractRow.contractId, fileUrl);
            break;
          case "rectificationResult":
            uploadPromise = uploadRectificationResult(this.uploadContractRow.contractId, fileUrl);
            break;
          case "annualReport":
            uploadPromise = uploadAnnualReport(this.uploadContractRow.contractId, fileUrl);
            break;
        }

        if (uploadPromise) {
          uploadPromise.then(() => {
            this.$modal.msgSuccess("上传成功");
            this.uploadDialogOpen = false;
            this.getContractList();
          });
        }
      } else {
        this.$modal.msgError(response.msg || "上传失败");
      }
    },
    handleUploadError() {
      this.$modal.msgError("上传失败");
    },
    handleDownloadFile(url) {
      if (url) {
        window.open(url, "_blank");
      } else {
        this.$modal.msgWarning("文件不存在");
      }
    },
    handlePreviewFile(url) {
      if (url) {
        // 判断文件类型
        const fileExt = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
        const imageExts = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
        const pdfExts = ['pdf'];
        
        if (imageExts.includes(fileExt)) {
          // 图片直接预览
          this.$alert(`<img src="${url}" style="max-width:100%" />`, "图片预览", {
            dangerouslyUseHTMLString: true,
            customClass: 'image-preview-dialog'
          });
        } else if (pdfExts.includes(fileExt)) {
          // PDF 在新窗口打开
          window.open(url, "_blank");
        } else {
          // 其他文件类型直接下载
          this.$modal.msgWarning("该文件类型不支持预览，请下载后查看");
          this.handleDownloadFile(url);
        }
      } else {
        this.$modal.msgWarning("文件不存在");
      }
    },
    // ========== 进驻查验 ==========
    getEntryList() {
      this.entryLoading = true;
      listEntryChecklist(this.entryQuery).then(response => {
        this.entryList = response.rows;
        this.entryTotal = response.total;
        this.entryLoading = false;
      });
    },
    handleAddEntry() { this.$modal.msgWarning("功能开发中"); },
    handleUpdateEntry(row) { this.$modal.msgWarning("功能开发中"); },
    handleDeleteEntry(row) {
      this.$modal.confirm('是否确认删除？').then(() => delEntryChecklist(row.checklistId)).then(() => {
        this.getEntryList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // ========== 月履行 ==========
    getMonthlyList() {
      this.monthlyLoading = true;
      listMonthlyChecklist(this.monthlyQuery).then(response => {
        this.monthlyList = response.rows;
        this.monthlyTotal = response.total;
        this.monthlyLoading = false;
      });
    },
    handleAddMonthly() { this.$modal.msgWarning("功能开发中"); },
    handleUpdateMonthly(row) { this.$modal.msgWarning("功能开发中"); },
    handleDeleteMonthly(row) {
      this.$modal.confirm('是否确认删除？').then(() => delMonthlyChecklist(row.checklistId)).then(() => {
        this.getMonthlyList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // ========== 考核管理 ==========
    getAssessmentList() {
      this.assessmentLoading = true;
      listAssessment(this.assessmentQuery).then(response => {
        this.assessmentList = response.rows;
        this.assessmentTotal = response.total;
        this.assessmentLoading = false;
      });
    },
    handleAddAssessment() { this.$modal.msgWarning("功能开发中"); },
    handleUpdateAssessment(row) { this.$modal.msgWarning("功能开发中"); },
    handleViewScores(row) {
      this.scoreDialogOpen = true;
      this.scoreLoading = true;
      getScoresByAssessment(row.assessmentId).then(response => {
        this.scoreList = response.data || [];
        this.scoreLoading = false;
      });
    },
    // ========== 整改记录 ==========
    getRectificationList() {
      this.rectificationLoading = true;
      listRectification(this.rectificationQuery).then(response => {
        this.rectificationList = response.rows;
        this.rectificationTotal = response.total;
        this.rectificationLoading = false;
      });
    },
    handleAddRectification() { this.$modal.msgWarning("功能开发中"); },
    handleUpdateRectification(row) { this.$modal.msgWarning("功能开发中"); },
    handleViewRectification(row) { this.$modal.msgWarning("功能开发中"); }
  }
};
</script>

<style scoped>
/* 时间线样式 */
.el-timeline-item h4 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 16px;
}

.el-timeline-item p {
  margin: 5px 0;
  color: #606266;
  font-size: 14px;
}

.el-timeline-item .el-card {
  border-radius: 4px;
}

/* 图片预览对话框样式 */
::v-deep .image-preview-dialog {
  max-width: 80%;
}

::v-deep .image-preview-dialog .el-message-box__message {
  text-align: center;
}
</style>
