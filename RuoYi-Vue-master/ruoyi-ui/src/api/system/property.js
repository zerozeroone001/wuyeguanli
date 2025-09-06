import request from '@/utils/request'

// 查询用户与房产关系列表
export function listProperty(query) {
  return request({
    url: '/system/property/list',
    method: 'get',
    params: query
  })
}

// 查询用户与房产关系详细
export function getProperty(associationId) {
  return request({
    url: '/system/property/' + associationId,
    method: 'get'
  })
}

// 新增用户与房产关系
export function addProperty(data) {
  return request({
    url: '/system/property',
    method: 'post',
    data: data
  })
}

// 修改用户与房产关系
export function updateProperty(data) {
  return request({
    url: '/system/property',
    method: 'put',
    data: data
  })
}

// 删除用户与房产关系
export function delProperty(associationId) {
  return request({
    url: '/system/property/' + associationId,
    method: 'delete'
  })
}
