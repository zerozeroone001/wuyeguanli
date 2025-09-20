import request from '@/utils/request'

// 查询公证服务申请列表
export function listApplication(query) {
  return request({
    url: '/system/notary/list',
    method: 'get',
    params: query
  })
}

// 查询公证服务申请详细
export function getApplication(applicationId) {
  return request({
    url: '/system/notary/' + applicationId,
    method: 'get'
  })
}

// 新增公证服务申请
export function addApplication(data) {
  return request({
    url: '/system/notary',
    method: 'post',
    data: data
  })
}

// 修改公证服务申请
export function updateApplication(data) {
  return request({
    url: '/system/notary',
    method: 'put',
    data: data
  })
}

// 删除公证服务申请
export function delApplication(applicationId) {
  return request({
    url: '/system/notary/' + applicationId,
    method: 'delete'
  })
}
