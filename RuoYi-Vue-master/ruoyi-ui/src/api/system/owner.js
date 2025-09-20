import request from '@/utils/request'

// 查询业主信息扩展列表
export function listOwner(query) {
  return request({
    url: '/system/owner/list',
    method: 'get',
    params: query
  })
}

// 查询业主信息扩展详细
export function getOwner(userId) {
  return request({
    url: '/system/owner/' + userId,
    method: 'get'
  })
}

// 新增业主信息扩展
export function addOwner(data) {
  return request({
    url: '/system/owner',
    method: 'post',
    data: data
  })
}

// 修改业主信息扩展
export function updateOwner(data) {
  return request({
    url: '/system/owner',
    method: 'put',
    data: data
  })
}

// 删除业主信息扩展
export function delOwner(userId) {
  return request({
    url: '/system/owner/' + userId,
    method: 'delete'
  })
}
