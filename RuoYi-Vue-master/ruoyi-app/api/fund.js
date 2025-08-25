import request from '@/utils/request'

// 查询资金流水列表
export function getFundFlowList(query) {
  return request({
    url: '/user/flow/list',
    method: 'get',
    params: query
  })
}

// 获取资金概览
export function getFundOverview() {
  return request({
    url: '/user/flow/overview',
    method: 'get'
  })
}

// 获取月度收支统计
export function getMonthlyStats() {
  return request({
    url: '/user/flow/monthly-stats',
    method: 'get'
  })
}
