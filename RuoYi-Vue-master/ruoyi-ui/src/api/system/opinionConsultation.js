import request from '@/utils/request'

// 查询意见征询列表
export function listOpinionConsultation(query) {
  return request({
    url: '/system/opinionConsultation/list',
    method: 'get',
    params: query
  })
}

// 查询意见征询详细
export function getOpinionConsultation(consultationId) {
  return request({
    url: '/system/opinionConsultation/' + consultationId,
    method: 'get'
  })
}

// 新增意见征询
export function addOpinionConsultation(data) {
  return request({
    url: '/system/opinionConsultation',
    method: 'post',
    data: data
  })
}

// 修改意见征询
export function updateOpinionConsultation(data) {
  return request({
    url: '/system/opinionConsultation',
    method: 'put',
    data: data
  })
}

// 删除意见征询
export function delOpinionConsultation(consultationId) {
  return request({
    url: '/system/opinionConsultation/' + consultationId,
    method: 'delete'
  })
}
