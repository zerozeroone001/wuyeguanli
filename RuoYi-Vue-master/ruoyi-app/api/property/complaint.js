import request from '@/utils/request'

// 提交投诉
export function addComplaint(data) {
  return request({
    url: '/property/complaint',
    method: 'post',
    data: data
  })
}

// 获取我的投诉列表
export function listMyComplaint(query) {
  return request({
    url: '/property/complaint/my',
    method: 'get',
    params: query
  })
}

// 获取投诉详情
export function getComplaint(complaintId) {
  return request({
    url: '/property/complaint/' + complaintId,
    method: 'get'
  })
}

// 获取处理记录
export function listComplaintHandle(complaintId) {
  return request({
    url: '/property/complaint/handle/' + complaintId,
    method: 'get'
  })
}

// 评价投诉处理
export function evaluateComplaint(data) {
  return request({
    url: '/property/complaint/evaluate',
    method: 'post',
    data: data
  })
}