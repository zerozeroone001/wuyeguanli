import request from '@/utils/request'

// 查询法律咨询列表
export function getLegalConsultationList(query) {
  return request({
    url: '/user/consultation/list',
    method: 'get',
    params: query
  })
}

// 新增法律咨询
export function addLegalConsultation(data) {
  return request({
    url: '/user/consultation',
    method: 'post',
    data: data
  })
}
