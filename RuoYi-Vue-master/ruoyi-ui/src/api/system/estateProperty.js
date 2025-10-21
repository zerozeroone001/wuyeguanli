import request from '@/utils/request'

// 查询房产信息列表
export function listEstateProperty(query) {
  return request({
    url: '/system/property/list',
    method: 'get',
    params: query
  })
}

// 查询房产信息详细
export function getEstateProperty(propertyId) {
  return request({
    url: '/system/property/' + propertyId,
    method: 'get'
  })
}

// 新增房产信息
export function addEstateProperty(data) {
  return request({
    url: '/system/property',
    method: 'post',
    data: data
  })
}

// 修改房产信息
export function updateEstateProperty(data) {
  return request({
    url: '/system/property',
    method: 'put',
    data: data
  })
}

// 删除房产信息
export function delEstateProperty(propertyId) {
  return request({
    url: '/system/property/' + propertyId,
    method: 'delete'
  })
}
