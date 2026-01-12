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

// 查询小区下的楼栋列表
export function listBuildings(query) {
  return request({
    url: '/system/property/buildings',
    method: 'get',
    params: query
  })
}

// 查询楼栋下的房号列表
export function listRooms(query) {
  return request({
    url: '/system/property/rooms',
    method: 'get',
    params: query
  })
}

// 查询房产树形结构(楼栋→单元→房号)
export function getPropertyTree(query) {
  return request({
    url: '/system/property/tree',
    method: 'get',
    params: query
  })
}
