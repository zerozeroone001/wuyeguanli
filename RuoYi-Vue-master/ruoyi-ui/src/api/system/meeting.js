import request from '@/utils/request'

// 查询会议管理列表
export function listMeeting(query) {
  return request({
    url: '/system/meeting/list',
    method: 'get',
    params: query
  })
}

// 查询会议管理详细
export function getMeeting(meetingId) {
  return request({
    url: '/system/meeting/' + meetingId,
    method: 'get'
  })
}

// 新增会议管理
export function addMeeting(data) {
  return request({
    url: '/system/meeting',
    method: 'post',
    data: data
  })
}

// 修改会议管理
export function updateMeeting(data) {
  return request({
    url: '/system/meeting',
    method: 'put',
    data: data
  })
}

// 删除会议管理
export function delMeeting(meetingId) {
  return request({
    url: '/system/meeting/' + meetingId,
    method: 'delete'
  })
}

// 发送会议通知
export function sendNotification(meetingId) {
  return request({
    url: '/system/meeting/notify/' + meetingId,
    method: 'post'
  })
}
