import request from '@/utils/request'

// 查询资金流水列表
export function listFlow(query) {
  return request({
    url: '/system/flow/list',
    method: 'get',
    params: query
  })
}

// 查询资金流水详细
export function getFlow(flowId) {
  return request({
    url: '/system/flow/' + flowId,
    method: 'get'
  })
}

// 新增资金流水
export function addFlow(data) {
  return request({
    url: '/system/flow',
    method: 'post',
    data: data
  })
}

// 修改资金流水
export function updateFlow(data) {
  return request({
    url: '/system/flow',
    method: 'put',
    data: data
  })
}

// 删除资金流水
export function delFlow(flowId) {
  return request({
    url: '/system/flow/' + flowId,
    method: 'delete'
  })
}
