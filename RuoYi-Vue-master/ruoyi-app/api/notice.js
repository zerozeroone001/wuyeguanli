import request from '@/utils/request'

// 查询公告列表
export function listNotice(query) {
  return request({
    url: '/notice/list',
    method: 'get',
    params: query
  })
}

// 查询公告详细
export function getNotice(noticeId) {
  return request({
    url: '/notice/' + noticeId,
    method: 'get'
  })
}
