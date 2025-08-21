import request from '@/utils/request'

// 查询意见征询主列表
export function listPoll(query) {
  return request({
    url: '/system/poll/list',
    method: 'get',
    params: query
  })
}

// 查询意见征询主详细
export function getPoll(pollId) {
  return request({
    url: '/system/poll/' + pollId,
    method: 'get'
  })
}

// 新增意见征询主
export function addPoll(data) {
  return request({
    url: '/system/poll',
    method: 'post',
    data: data
  })
}

// 修改意见征询主
export function updatePoll(data) {
  return request({
    url: '/system/poll',
    method: 'put',
    data: data
  })
}

// 删除意见征询主
export function delPoll(pollId) {
  return request({
    url: '/system/poll/' + pollId,
    method: 'delete'
  })
}
