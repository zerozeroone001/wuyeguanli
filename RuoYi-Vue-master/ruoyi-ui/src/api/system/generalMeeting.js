import request from '@/utils/request'

// 查询业主大会会议管理列表
export function listGeneralMeeting(query) {
  return request({
    url: '/system/generalMeeting/list',
    method: 'get',
    params: query
  })
}

// 查询业主大会会议管理详细
export function getGeneralMeeting(meetingId) {
  return request({
    url: '/system/generalMeeting/' + meetingId,
    method: 'get'
  })
}

// 新增业主大会会议管理
export function addGeneralMeeting(data) {
  return request({
    url: '/system/generalMeeting',
    method: 'post',
    data: data
  })
}

// 修改业主大会会议管理
export function updateGeneralMeeting(data) {
  return request({
    url: '/system/generalMeeting',
    method: 'put',
    data: data
  })
}

// 删除业主大会会议管理
export function delGeneralMeeting(meetingId) {
  return request({
    url: '/system/generalMeeting/' + meetingId,
    method: 'delete'
  })
}
