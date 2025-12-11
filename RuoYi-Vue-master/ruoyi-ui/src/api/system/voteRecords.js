import request from '@/utils/request'

// 查询投票记录列表
export function listVoteRecords(query) {
  return request({
    url: '/system/voteRecords/list',
    method: 'get',
    params: query
  })
}

// 查询用户投票详情
export function listVoteDetails(query) {
  return request({
    url: '/system/voteRecords/detail',
    method: 'get',
    params: query
  })
}
