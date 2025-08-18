import request from '@/utils/request'

// 获取会议列表
export function listMeeting(query) {
  return request({
    url: '/property/meeting/list',
    method: 'get',
    params: query
  })
}

// 获取会议详情
export function getMeeting(meetingId) {
  return request({
    url: '/property/meeting/' + meetingId,
    method: 'get'
  })
}

// 获取会议议题
export function listMeetingTopic(query) {
  return request({
    url: '/property/meeting/topic/list',
    method: 'get',
    params: query
  })
}

// 提交投票
export function submitVote(data) {
  return request({
    url: '/property/meeting/vote',
    method: 'post',
    data: data
  })
}

// 获取投票结果
export function getVoteResult(meetingId) {
  return request({
    url: '/property/meeting/result/' + meetingId,
    method: 'get'
  })
}

// 获取我的投票记录
export function getMyVoteRecords(query) {
  return request({
    url: '/property/meeting/vote/my',
    method: 'get',
    params: query
  })
}