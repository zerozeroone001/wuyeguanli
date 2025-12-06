import request from '@/utils/request'

// 查询业主变更申请列表
export function listOwnerChange(query) {
  return request({
    url: '/system/ownerChange/list',
    method: 'get',
    params: query
  })
}

// 查询业主变更申请详细
export function getOwnerChange(changeId) {
  return request({
    url: '/system/ownerChange/' + changeId,
    method: 'get'
  })
}

// 新增业主变更申请
export function addOwnerChange(data) {
  return request({
    url: '/system/ownerChange',
    method: 'post',
    data: data
  })
}

// 修改业主变更申请
export function updateOwnerChange(data) {
  return request({
    url: '/system/ownerChange',
    method: 'put',
    data: data
  })
}

// 删除业主变更申请
export function delOwnerChange(changeId) {
  return request({
    url: '/system/ownerChange/' + changeId,
    method: 'delete'
  })
}

// 审核业主变更申请
export function auditOwnerChange(data) {
  return request({
    url: '/system/ownerChange/audit',
    method: 'put',
    data: data
  })
}
