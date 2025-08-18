import request from '@/utils/request'

// 获取资金流水列表
export function listFundFlow(query) {
  return request({
    url: '/property/fund/flow/list',
    method: 'get',
    params: query
  })
}

// 获取资金概览
export function getFundOverview() {
  return request({
    url: '/property/fund/overview',
    method: 'get'
  })
}

// 获取资金流水详情
export function getFundFlowDetail(flowId) {
  return request({
    url: '/property/fund/flow/' + flowId,
    method: 'get'
  })
}

// 获取资金统计数据
export function getFundStatistics(query) {
  return request({
    url: '/property/fund/statistics',
    method: 'get',
    params: query
  })
}
 
 