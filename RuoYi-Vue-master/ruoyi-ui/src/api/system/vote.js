import request from '@/utils/request'

// 查询业主大会投票列表
export function listVote(query) {
  return request({
    url: '/system/vote/list',
    method: 'get',
    params: query
  })
}

// 查询业主大会投票详细
export function getVote(voteId) {
  return request({
    url: '/system/vote/' + voteId,
    method: 'get'
  })
}

// 新增业主大会投票
export function addVote(data) {
  return request({
    url: '/system/vote',
    method: 'post',
    data: data
  })
}

// 修改业主大会投票
export function updateVote(data) {
  return request({
    url: '/system/vote',
    method: 'put',
    data: data
  })
}

// 删除业主大会投票
export function delVote(voteId) {
  return request({
    url: '/system/vote/' + voteId,
    method: 'delete'
  })
}
