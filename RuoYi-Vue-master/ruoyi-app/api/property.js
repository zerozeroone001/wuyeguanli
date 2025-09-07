
import request from '@/utils/request'

// 查询当前用户已审核的房产列表
export function listMyProperty(query) {
  return request({
    url: '/app/user-property/list',
    method: 'get',
    params: query
  })
}

// 查询可供选择的房产列表
export function listAllProperty(query) {
  return request({
    url: '/app/property/list',
    method: 'get',
    params: query
  })
}

// 提交房产审核
export function addMyProperty(data) {
  return request({
    url: '/app/user-property/add',
    method: 'post',
    data: data
  })
}

// 获取房产详细信息
export function getPropertyDetails(propertyId) {
  return request({
    url: '/app/property/' + propertyId,
    method: 'get'
  })
}

// 查询小区列表
export function listCommunity(query) {
  return request({
    url: '/app/community/list',
    method: 'get',
    params: query
  })
}
