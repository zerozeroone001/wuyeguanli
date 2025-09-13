import request from '@/utils/request'

// 查询公证列表
export function listNotary(query) {
  return request({
    url: '/app/notary/list',
    method: 'get',
    params: query
  })
}

// 获取公证详情
export function getNotaryDetail(notaryId) {
  return request({
    url: '/app/notary/' + notaryId,
    method: 'get'
  })
}

// 新增公证申请
export function addNotary(data) {
  return request({
    url: '/app/notary',
    method: 'post',
    data: data
  })
}

// 修改公证申请
export function updateNotary(data) {
  return request({
    url: '/app/notary',
    method: 'put',
    data: data
  })
}

// 删除公证申请
export function deleteNotary(notaryId) {
  return request({
    url: '/app/notary/' + notaryId,
    method: 'delete'
  })
}

// 获取首页统计数据
export function getNotaryStats() {
  return request({
    url: '/app/notary/stats',
    method: 'get'
  })
}

// 获取公证处列表
export function getNotaryOfficeList() {
  return request({
    url: '/app/notary/office/list',
    method: 'get'
  })
}
