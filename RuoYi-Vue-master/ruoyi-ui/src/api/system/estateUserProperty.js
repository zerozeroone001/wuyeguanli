import request from '@/utils/request'

// 查询房屋申请列表
export function listEstateUserProperty(query) {
  return request({
    url: '/system/estateUserProperty/list',
    method: 'get',
    params: query
  })
}

// 查询房屋申请详情
export function getEstateUserProperty(associationId) {
  return request({
    url: `/system/estateUserProperty/${associationId}`,
    method: 'get'
  })
}

// 审核房屋申请
export function auditEstateUserProperty(data) {
  return request({
    url: '/system/estateUserProperty/audit',
    method: 'put',
    data
  })
}

// 删除房屋关联
export function delEstateUserProperty(associationId) {
  return request({
    url: `/system/estateUserProperty/${associationId}`,
    method: 'delete'
  })
}
