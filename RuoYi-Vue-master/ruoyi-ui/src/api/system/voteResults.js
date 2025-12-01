import request from '@/utils/request'

// 查询会议表决结果统计
export function listVoteResults(query) {
  return request({
    url: '/system/voteResults/list',
    method: 'get',
    params: query
  })
}

// 导出表决结果
export function exportVoteResults(query) {
  return request({
    url: '/system/voteResults/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}


// 导出投票统计报告PDF
export function exportVoteReportPdf(query) {
  return request({
    url: '/system/meeting/export/voteReport',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
