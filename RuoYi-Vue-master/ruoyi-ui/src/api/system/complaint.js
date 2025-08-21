import request from '@/utils/request'

// 查询投诉管理列表
export function listComplaint(query) {
  return request({
    url: '/system/complaint/list',
    method: 'get',
    params: query
  })
}

// 查询投诉管理详细
export function getComplaint(complaintId) {
  return request({
    url: '/system/complaint/' + complaintId,
    method: 'get'
  })
}

// 新增投诉管理
export function addComplaint(data) {
  return request({
    url: '/system/complaint',
    method: 'post',
    data: data
  })
}

// 修改投诉管理
export function updateComplaint(data) {
  return request({
    url: '/system/complaint',
    method: 'put',
    data: data
  })
}

// 删除投诉管理
export function delComplaint(complaintId) {
  return request({
    url: '/system/complaint/' + complaintId,
    method: 'delete'
  })
}
