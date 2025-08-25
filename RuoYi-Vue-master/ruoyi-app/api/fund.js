import request from '@/utils/request'

// 查询资金流水列表
export function getFundFlowList(query) {
  return request({
    url: '/user/flow/list',
    method: 'get',
    params: query
  })
}
