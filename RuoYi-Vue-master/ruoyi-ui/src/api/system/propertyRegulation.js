import request from '@/utils/request'

// 查询物业制度管理列表
export function listRegulation(query) {
  return request({
    url: '/system/regulation/list',
    method: 'get',
    params: query
  })
}

// 查询物业制度管理详细
export function getRegulation(regulationId) {
  return request({
    url: '/system/regulation/' + regulationId,
    method: 'get'
  })
}

// 新增物业制度管理
export function addRegulation(data) {
  return request({
    url: '/system/regulation',
    method: 'post',
    data: data
  })
}

// 修改物业制度管理
export function updateRegulation(data) {
  return request({
    url: '/system/regulation',
    method: 'put',
    data: data
  })
}

// 删除物业制度管理
export function delRegulation(regulationId) {
  return request({
    url: '/system/regulation/' + regulationId,
    method: 'delete'
  })
}