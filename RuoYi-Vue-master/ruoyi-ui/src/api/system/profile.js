import request from '@/utils/request'

// 查询业主信息扩展列表
export function listProfile(query) {
  return request({
    url: '/system/profile/list',
    method: 'get',
    params: query
  })
}

// 查询业主信息扩展详细
export function getProfile(userId) {
  return request({
    url: '/system/profile/' + userId,
    method: 'get'
  })
}

// 新增业主信息扩展
export function addProfile(data) {
  return request({
    url: '/system/profile',
    method: 'post',
    data: data
  })
}

// 修改业主信息扩展
export function updateProfile(data) {
  return request({
    url: '/system/profile',
    method: 'put',
    data: data
  })
}

// 删除业主信息扩展
export function delProfile(userId) {
  return request({
    url: '/system/profile/' + userId,
    method: 'delete'
  })
}
