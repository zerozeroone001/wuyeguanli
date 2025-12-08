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
export function getOwner(ownerId) {
  return request({
    url: '/system/owner/' + ownerId,
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
export function delOwner(ownerId) {
  return request({
    url: '/system/owner/' + ownerId,
    method: 'delete'
  })
}

// 修改用户状态
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/owner/changeStatus',
    method: 'put',
    data: data
  })
}

// 修改用户身份
export function changeUserIdentity(userId, isOwner) {
  const data = {
    userId,
    isOwner
  }
  return request({
    url: '/system/owner/changeIdentity',
    method: 'put',
    data: data
  })
}

// 查询可绑定的用户列表
export function getUnboundUsers(query) {
  return request({
    url: '/system/owner/unboundUsers',
    method: 'get',
    params: query
  })
}

// 房产合并与拆分
export function transferProperties(data) {
  return request({
    url: '/system/owner/transfer',
    method: 'post',
    data: data
  })
}

// 为已有业主添加房产
export function addOwnerProperty(data) {
  return request({
    url: '/system/owner/addProperty',
    method: 'post',
    data: data
  })
}
