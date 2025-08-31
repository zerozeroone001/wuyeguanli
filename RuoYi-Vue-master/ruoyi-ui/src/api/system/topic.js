import request from '@/utils/request'

// 查询会议议题列表
export function listTopic(query) {
  return request({
    url: '/system/topic/list',
    method: 'get',
    params: query
  })
}

// 查询会议议题详细
export function getTopic(topicId) {
  return request({
    url: '/system/topic/' + topicId,
    method: 'get'
  })
}

// 新增会议议题
export function addTopic(data) {
  return request({
    url: '/system/topic',
    method: 'post',
    data: data
  })
}

// 修改会议议题
export function updateTopic(data) {
  return request({
    url: '/system/topic',
    method: 'put',
    data: data
  })
}

// 删除会议议题
export function delTopic(topicId) {
  return request({
    url: '/system/topic/' + topicId,
    method: 'delete'
  })
}
