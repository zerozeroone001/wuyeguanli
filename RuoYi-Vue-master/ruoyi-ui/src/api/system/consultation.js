import request from '@/utils/request'

// 查询法律咨询列表
export function listConsultation(query) {
  return request({
    url: '/system/consultation/list',
    method: 'get',
    params: query
  })
}

// 查询法律咨询详细
export function getConsultation(consultationId) {
  return request({
    url: '/system/consultation/' + consultationId,
    method: 'get'
  })
}

// 新增法律咨询
export function addConsultation(data) {
  return request({
    url: '/system/consultation',
    method: 'post',
    data: data
  })
}

// 修改法律咨询
export function updateConsultation(data) {
  return request({
    url: '/system/consultation',
    method: 'put',
    data: data
  })
}

// 删除法律咨询
export function delConsultation(consultationId) {
  return request({
    url: '/system/consultation/' + consultationId,
    method: 'delete'
  })
}
