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

// 流转状态
export function changeStatus(data) {
  return request({
    url: '/system/meeting',
    method: 'put',
    data: data
  })

}

// 导出未投票用户表决票
export function exportBallot(meetingId, type) {
  return request({
    url: '/system/meeting/exportBallot?meetingId=' + meetingId + (type ? '&type=' + type : ''),
    method: 'post',
    responseType: 'blob'
  })
}

// 导出投票结果
export function exportVotingResults(meetingId) {
  return request({
    url: '/system/meeting/exportVotingResults?meetingId=' + meetingId,
    method: 'post',
    responseType: 'blob'
  })
}

// 导出投票明细公开表
export function exportVotingDetailsPublic(meetingId) {
  return request({
    url: '/system/meeting/exportVotingDetailsPublic?meetingId=' + meetingId,
    method: 'post',
    responseType: 'blob'
  })
}

// 导出会议文件
export function exportMeetingDocuments(meetingId) {
  return request({
    url: '/system/meeting/exportMeetingDocuments?meetingId=' + meetingId,
    method: 'post',
    responseType: 'blob'
  })
}

// 获取楼栋投票统计
export function getBuildingStats(meetingId) {
  return request({
    url: '/system/meeting/buildingStats/' + meetingId,
    method: 'get'
  })
}

// 复制会议
export function copyMeeting(meetingId) {
  return request({
    url: '/system/meeting/copy/' + meetingId,
    method: 'post'
  })
}
