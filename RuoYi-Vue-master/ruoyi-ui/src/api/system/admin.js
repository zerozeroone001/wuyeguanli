import request from '@/utils/request'

// 查询管理员列表
export function listAdmin(query) {
  return request({
    url: '/system/admin/list',
    method: 'get',
    params: query
  })
}

// 查询管理员详细
export function getAdmin(adminId) {
  return request({
    url: '/system/admin/' + adminId,
    method: 'get'
  })
}

// 新增管理员
export function addAdmin(data) {
  return request({
    url: '/system/admin',
    method: 'post',
    data: data
  })
}

// 修改管理员
export function updateAdmin(data) {
  return request({
    url: '/system/admin',
    method: 'put',
    data: data
  })
}

// 删除管理员
export function delAdmin(adminId) {
  return request({
    url: '/system/admin/' + adminId,
    method: 'delete'
  })
}
