import request from '@/utils/request'

// 查询小区信息列表
export function listCommunity(query) {
  return request({
    url: '/system/community/list',
    method: 'get',
    params: query
  })
}

// 查询小区信息详细
export function getCommunity(communityId) {
  return request({
    url: '/system/community/' + communityId,
    method: 'get'
  })
}

// 新增小区信息
export function addCommunity(data) {
  return request({
    url: '/system/community',
    method: 'post',
    data: data
  })
}

// 修改小区信息
export function updateCommunity(data) {
  return request({
    url: '/system/community',
    method: 'put',
    data: data
  })
}

// 删除小区信息
export function delCommunity(communityId) {
  return request({
    url: '/system/community/' + communityId,
    method: 'delete'
  })
}
