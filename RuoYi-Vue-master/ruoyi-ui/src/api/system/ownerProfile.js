import request from '@/utils/request'

// 查询业主信息扩展列表
export function listOwnerProfile(query) {
  return request({
    url: '/system/profile/list',
    method: 'get',
    params: query
  })
}

// 审核业主认证
export function auditProfile(data) {
    return request({
      url: '/system/profile/audit',
      method: 'put',
      data: data
    })
  }
