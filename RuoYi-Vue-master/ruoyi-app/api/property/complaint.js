import request from '@/utils/request'

// 提交投诉
export function addComplaint(data) {
  return request({
    url: '/userapi/complaint',
    method: 'post',
    data: data
  })
}

// 获取我的投诉列表
export function listMyComplaint(query) {
  return request({
    url: '/userapi/complaint/my',
    method: 'get',
    params: query
  })
}

// 获取投诉详情
export function getComplaint(complaintId) {
  return request({
    url: '/userapi/complaint/' + complaintId,
    method: 'get'
  })
}

// 获取处理记录
export function listComplaintHandle(complaintId) {
  return request({
    url: '/userapi/complaint/handle/' + complaintId,
    method: 'get'
  })
}

// 评价投诉处理
export function evaluateComplaint(data) {
  return request({
    url: '/userapi/complaint/evaluate',
    method: 'post',
    data: data
  })
}
 
 