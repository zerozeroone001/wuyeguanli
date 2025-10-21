import request from '@/utils/request'

// 查询公证申请列表
export function listNotary(query) {
  return request({
    url: '/system/notary/list',
    method: 'get',
    params: query
  })
}

// 查询公证申请详细
export function getNotary(notaryId) {
  return request({
    url: '/system/notary/' + notaryId,
    method: 'get'
  })
}

// 新增公证申请
export function addNotary(data) {
  return request({
    url: '/system/notary',
    method: 'post',
    data: data
  })
}

// 修改公证申请
export function updateNotary(data) {
  return request({
    url: '/system/notary',
    method: 'put',
    data: data
  })
}

// 删除公证申请
export function delNotary(notaryId) {
  return request({
    url: '/system/notary/' + notaryId,
    method: 'delete'
  })
}

// 审核公证申请
export function reviewNotary(data) {
  return request({
    url: '/system/notary/review',
    method: 'post',
    data: data
  })
}

// 分配公证员
export function assignNotary(data) {
  return request({
    url: '/system/notary/assign',
    method: 'post',
    data: data
  })
}

// 更新公证进度
export function updateProgress(data) {
  return request({
    url: '/system/notary/progress',
    method: 'post',
    data: data
  })
}

// 完成公证
export function completeNotary(data) {
  return request({
    url: '/system/notary/complete',
    method: 'post',
    data: data
  })
}

// 获取公证统计数据
export function getNotaryStats() {
  return request({
    url: '/system/notary/stats',
    method: 'get'
  })
}

// 获取公证流程日志
export function getProcessLogs(notaryId) {
  return request({
    url: '/system/notary/' + notaryId + '/process',
    method: 'get'
  })
}

// 批量操作公证申请
export function batchOperation(data) {
  return request({
    url: '/system/notary/batch',
    method: 'post',
    data: data
  })
}

// 获取公证类型配置
export function getTypeConfigs() {
  return request({
    url: '/system/notary/type/config',
    method: 'get'
  })
}

// 更新公证类型配置
export function updateTypeConfig(data) {
  return request({
    url: '/system/notary/type/config',
    method: 'post',
    data: data
  })
}
