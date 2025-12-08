import request from '@/utils/request'

// 查询房产认证申请列表
export function listApply(query) {
  return request({
    url: '/system/apply/list',
    method: 'get',
    params: query
  })
}

// 查询房产认证申请详细
export function getApply(applyId) {
  return request({
    url: '/system/apply/' + applyId,
    method: 'get'
  })
}

// 新增房产认证申请
export function addApply(data) {
  return request({
    url: '/system/apply',
    method: 'post',
    data: data
  })
}

// 修改房产认证申请
export function updateApply(data) {
  return request({
    url: '/system/apply',
    method: 'put',
    data: data
  })
}

// 删除房产认证申请
export function delApply(applyId) {
  return request({
    url: '/system/apply/' + applyId,
    method: 'delete'
  })
}

// 审核房产认证申请
export function auditApply(data) {
    return request({
      url: '/system/apply/audit',
      method: 'put',
      data: data
    })
}
