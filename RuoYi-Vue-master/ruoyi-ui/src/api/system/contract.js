import request from '@/utils/request'

// ===================== 合同主表 =====================
// 查询物业服务合同列表
export function listContract(query) {
  return request({
    url: '/system/contract/list',
    method: 'get',
    params: query
  })
}

// 查询物业服务合同详细
export function getContract(contractId) {
  return request({
    url: '/system/contract/' + contractId,
    method: 'get'
  })
}

// 新增物业服务合同
export function addContract(data) {
  return request({
    url: '/system/contract',
    method: 'post',
    data: data
  })
}

// 修改物业服务合同
export function updateContract(data) {
  return request({
    url: '/system/contract',
    method: 'put',
    data: data
  })
}

// 删除物业服务合同
export function delContract(contractId) {
  return request({
    url: '/system/contract/' + contractId,
    method: 'delete'
  })
}

// 更新合同阶段
export function updateContractStage(contractId, newStage) {
  return request({
    url: '/system/contract/stage/' + contractId + '/' + newStage,
    method: 'put'
  })
}

// 审核合同（待审核 -> 待制作查验清单）
export function reviewContract(contractId) {
  return request({
    url: '/system/contract/review/' + contractId,
    method: 'put'
  })
}

// 上传查验清单
export function uploadEntryChecklist(contractId, fileUrl) {
  return request({
    url: '/system/contract/uploadEntryChecklist/' + contractId,
    method: 'put',
    data: { fileUrl }
  })
}

// 上传物业查验清单
export function uploadPropertyEntryChecklist(contractId, fileUrl) {
  return request({
    url: '/system/contract/uploadPropertyEntryChecklist/' + contractId,
    method: 'put',
    data: { fileUrl }
  })
}

// 上传合同月履行清单
export function uploadMonthlyChecklist(contractId, fileUrl) {
  return request({
    url: '/system/contract/uploadMonthlyChecklist/' + contractId,
    method: 'put',
    data: { fileUrl }
  })
}

// 上传物业月履行清单
export function uploadPropertyMonthlyChecklist(contractId, fileUrl) {
  return request({
    url: '/system/contract/uploadPropertyMonthlyChecklist/' + contractId,
    method: 'put',
    data: { fileUrl }
  })
}

// 月履行清单审核
export function reviewMonthlyChecklist(contractId, result) {
  return request({
    url: '/system/contract/reviewMonthlyChecklist/' + contractId,
    method: 'put',
    data: { result }
  })
}

// 上传整改通知单
export function uploadRectificationNotice(contractId, fileUrl) {
  return request({
    url: '/system/contract/uploadRectificationNotice/' + contractId,
    method: 'put',
    data: { fileUrl }
  })
}

// 上传整改结果评定通知书
export function uploadRectificationResult(contractId, fileUrl) {
  return request({
    url: '/system/contract/uploadRectificationResult/' + contractId,
    method: 'put',
    data: { fileUrl }
  })
}

// 上传年度履行报告
export function uploadAnnualReport(contractId, fileUrl) {
  return request({
    url: '/system/contract/uploadAnnualReport/' + contractId,
    method: 'put',
    data: { fileUrl }
  })
}

// ===================== 进驻查验清单 =====================
export function listEntryChecklist(query) {
  return request({
    url: '/system/contract/entryChecklist/list',
    method: 'get',
    params: query
  })
}

export function getEntryChecklist(checklistId) {
  return request({
    url: '/system/contract/entryChecklist/' + checklistId,
    method: 'get'
  })
}

export function getEntryChecklistByContract(contractId) {
  return request({
    url: '/system/contract/entryChecklist/contract/' + contractId,
    method: 'get'
  })
}

export function addEntryChecklist(data) {
  return request({
    url: '/system/contract/entryChecklist',
    method: 'post',
    data: data
  })
}

export function updateEntryChecklist(data) {
  return request({
    url: '/system/contract/entryChecklist',
    method: 'put',
    data: data
  })
}

export function delEntryChecklist(checklistId) {
  return request({
    url: '/system/contract/entryChecklist/' + checklistId,
    method: 'delete'
  })
}

// ===================== 月履行清单 =====================
export function listMonthlyChecklist(query) {
  return request({
    url: '/system/contract/monthlyChecklist/list',
    method: 'get',
    params: query
  })
}

export function getMonthlyChecklist(checklistId) {
  return request({
    url: '/system/contract/monthlyChecklist/' + checklistId,
    method: 'get'
  })
}

export function getMonthlyChecklistByContract(contractId) {
  return request({
    url: '/system/contract/monthlyChecklist/contract/' + contractId,
    method: 'get'
  })
}

export function addMonthlyChecklist(data) {
  return request({
    url: '/system/contract/monthlyChecklist',
    method: 'post',
    data: data
  })
}

export function updateMonthlyChecklist(data) {
  return request({
    url: '/system/contract/monthlyChecklist',
    method: 'put',
    data: data
  })
}

export function delMonthlyChecklist(checklistId) {
  return request({
    url: '/system/contract/monthlyChecklist/' + checklistId,
    method: 'delete'
  })
}

// ===================== 考核活动 =====================
export function listAssessment(query) {
  return request({
    url: '/system/contract/assessment/list',
    method: 'get',
    params: query
  })
}

export function getAssessment(assessmentId) {
  return request({
    url: '/system/contract/assessment/' + assessmentId,
    method: 'get'
  })
}

export function getAssessmentByContract(contractId) {
  return request({
    url: '/system/contract/assessment/contract/' + contractId,
    method: 'get'
  })
}

export function addAssessment(data) {
  return request({
    url: '/system/contract/assessment',
    method: 'post',
    data: data
  })
}

export function updateAssessment(data) {
  return request({
    url: '/system/contract/assessment',
    method: 'put',
    data: data
  })
}

export function delAssessment(assessmentId) {
  return request({
    url: '/system/contract/assessment/' + assessmentId,
    method: 'delete'
  })
}

// ===================== 业主评分 =====================
export function listAssessmentScore(query) {
  return request({
    url: '/system/contract/assessmentScore/list',
    method: 'get',
    params: query
  })
}

export function getAssessmentScore(scoreId) {
  return request({
    url: '/system/contract/assessmentScore/' + scoreId,
    method: 'get'
  })
}

export function getScoresByAssessment(assessmentId) {
  return request({
    url: '/system/contract/assessmentScore/assessment/' + assessmentId,
    method: 'get'
  })
}

export function submitAssessmentScore(data) {
  return request({
    url: '/system/contract/assessmentScore/submit',
    method: 'post',
    data: data
  })
}

// ===================== 整改记录 =====================
export function listRectification(query) {
  return request({
    url: '/system/contract/rectification/list',
    method: 'get',
    params: query
  })
}

export function getRectification(rectificationId) {
  return request({
    url: '/system/contract/rectification/' + rectificationId,
    method: 'get'
  })
}

export function getRectificationByContract(contractId) {
  return request({
    url: '/system/contract/rectification/contract/' + contractId,
    method: 'get'
  })
}

export function addRectification(data) {
  return request({
    url: '/system/contract/rectification',
    method: 'post',
    data: data
  })
}

export function updateRectification(data) {
  return request({
    url: '/system/contract/rectification',
    method: 'put',
    data: data
  })
}

export function delRectification(rectificationId) {
  return request({
    url: '/system/contract/rectification/' + rectificationId,
    method: 'delete'
  })
}

// ===================== 合同履约监督 =====================
// 查询合同履约监督列表
export function listContractSupervision(query) {
  return request({
    url: '/system/contractSupervision/list',
    method: 'get',
    params: query
  })
}

// 查询合同履约监督详细
export function getContractSupervision(supervisionId) {
  return request({
    url: '/system/contractSupervision/' + supervisionId,
    method: 'get'
  })
}

// 新增合同履约监督
export function addContractSupervision(data) {
  return request({
    url: '/system/contractSupervision',
    method: 'post',
    data: data
  })
}

// 修改合同履约监督
export function updateContractSupervision(data) {
  return request({
    url: '/system/contractSupervision',
    method: 'put',
    data: data
  })
}

// 删除合同履约监督
export function delContractSupervision(supervisionId) {
  return request({
    url: '/system/contractSupervision/' + supervisionId,
    method: 'delete'
  })
}

// 公示考核结果
export function publishSupervisionResult(supervisionId) {
  return request({
    url: '/system/contractSupervision/publish/' + supervisionId,
    method: 'put'
  })
}

