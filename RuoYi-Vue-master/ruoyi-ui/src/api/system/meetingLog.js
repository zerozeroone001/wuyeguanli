import request from '@/utils/request'

// 查询业主大会日志列表
export function listMeetingLog(query) {
  return request({
    url: '/system/meetingLog/list',
    method: 'get',
    params: query
  })
}

// 查询业主大会日志详细
export function getMeetingLog(logId) {
  return request({
    url: '/system/meetingLog/' + logId,
    method: 'get'
  })
}

// 查询指定会议的所有日志
export function getLogsByMeetingId(meetingId) {
  return request({
    url: '/system/meetingLog/meeting/' + meetingId,
    method: 'get'
  })
}

// 新增业主大会日志
export function addMeetingLog(data) {
  return request({
    url: '/system/meetingLog',
    method: 'post',
    data: data
  })
}

// 修改业主大会日志
export function updateMeetingLog(data) {
  return request({
    url: '/system/meetingLog',
    method: 'put',
    data: data
  })
}

// 删除业主大会日志
export function delMeetingLog(logId) {
  return request({
    url: '/system/meetingLog/' + logId,
    method: 'delete'
  })
}
