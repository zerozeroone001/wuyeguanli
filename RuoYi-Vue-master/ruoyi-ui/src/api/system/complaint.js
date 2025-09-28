import request from '@/utils/request'

// 查询投诉管理列表
export function listComplaint(query) {
  return request({
    url: '/system/complaint/list',
    method: 'get',
    params: query
  })
}

// 查询投诉管理详细
export function getComplaint(complaintId) {
  return request({
    url: '/system/complaint/' + complaintId,
    method: 'get'
  })
}

// 新增投诉管理
export function addComplaint(data) {
  return request({
    url: '/system/complaint',
    method: 'post',
    data: data
  })
}

// 修改投诉管理
export function updateComplaint(data) {
  return request({
    url: '/system/complaint',
    method: 'put',
    data: data
  })
}

// 删除投诉管理
export function delComplaint(complaintId) {
  return request({
    url: '/system/complaint/' + complaintId,
    method: 'delete'
  })
}

// 获取投诉统计信息
export function getComplaintStats() {
  return request({
    url: '/system/complaint/stats',
    method: 'get'
  })
}

// 分配投诉处理人
export function assignComplaint(data) {
  return request({
    url: '/system/complaint/assign',
    method: 'put',
    data: data
  })
}

// 完成投诉处理
export function completeComplaint(data) {
  return request({
    url: '/system/complaint/complete',
    method: 'put',
    data: data
  })
}

// 关闭投诉
export function closeComplaint(data) {
  return request({
    url: '/system/complaint/close',
    method: 'put',
    data: data
  })
}

// 获取待处理投诉数量
export function getPendingCount() {
  return request({
    url: '/system/complaint/pending/count',
    method: 'get'
  })
}

// 获取紧急投诉数量
export function getUrgentCount() {
  return request({
    url: '/system/complaint/urgent/count',
    method: 'get'
  })
}

// 获取投诉趋势数据
export function getComplaintTrend() {
  return request({
    url: '/system/complaint/trend',
    method: 'get'
  })
}

// 获取最近投诉记录
export function getRecentComplaints(limit) {
  return request({
    url: '/system/complaint/recent',
    method: 'get',
    params: { limit }
  })
}

// 按类型统计投诉
export function getComplaintTypeStats() {
  return request({
    url: '/system/complaint/type/stats',
    method: 'get'
  })
}

// 按状态统计投诉
export function getComplaintStatusStats() {
  return request({
    url: '/system/complaint/status/stats',
    method: 'get'
  })
}

// 获取投诉增长率
export function getComplaintGrowthRate() {
  return request({
    url: '/system/complaint/growth/rate',
    method: 'get'
  })
}
