
import request from '@/utils/request'

// 获取会议详细信息
export function getMeeting(meetingId) {
  return request({
    url: '/meeting/' + meetingId,
    method: 'get'
  })
}

// 获取会议的议题列表
export function getMeetingTopics(meetingId) {
  return request({
    url: '/meeting/' + meetingId + '/topics',
    method: 'get'
  })
}

// 获取我的投票记录
export function getMyVotes(meetingId) {
  return request({
    url: '/meeting/' + meetingId + '/my-votes',
    method: 'get'
  })
}

// 提交投票
export function submitVote(data) {
    return request({
        url: '/meeting/vote',
        method: 'post',
        data: data // data is expected to be { meetingId, topicId, voteOption }
    });
}

// 提交意见
export function submitOpinion(data) {
  return request({
    url: '/meeting/opinion',
    method: 'post',
    data: data // data is expected to be { topicId, opinion }
  })
}
