import request from '@/utils/request'

// 查询业主委员会会议管理列表
export function listCommitteeMeeting(query) {
  return request({
    url: '/system/committeeMeeting/list',
    method: 'get',
    params: query
  })
}

// 查询业主委员会会议管理详细
export function getCommitteeMeeting(meetingId) {
  return request({
    url: '/system/committeeMeeting/' + meetingId,
    method: 'get'
  })
}

// 新增业主委员会会议管理
export function addCommitteeMeeting(data) {
  return request({
    url: '/system/committeeMeeting',
    method: 'post',
    data: data
  })
}

// 修改业主委员会会议管理
export function updateCommitteeMeeting(data) {
  return request({
    url: '/system/committeeMeeting',
    method: 'put',
    data: data
  })
}

// 删除业主委员会会议管理
export function delCommitteeMeeting(meetingId) {
  return request({
    url: '/system/committeeMeeting/' + meetingId,
    method: 'delete'
  })
}
