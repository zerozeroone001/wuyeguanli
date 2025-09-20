import request from '@/utils/request'

// 查询检测计划列表
export function listPlan(query) {
  return request({
    url: '/system/inspectionPlan/list',
    method: 'get',
    params: query
  })
}

// 查询检测计划详细
export function getPlan(planId) {
  return request({
    url: '/system/inspectionPlan/' + planId,
    method: 'get'
  })
}

// 新增检测计划
export function addPlan(data) {
  return request({
    url: '/system/inspectionPlan',
    method: 'post',
    data: data
  })
}

// 修改检测计划
export function updatePlan(data) {
  return request({
    url: '/system/inspectionPlan',
    method: 'put',
    data: data
  })
}

// 删除检测计划
export function delPlan(planId) {
  return request({
    url: '/system/inspectionPlan/' + planId,
    method: 'delete'
  })
}
