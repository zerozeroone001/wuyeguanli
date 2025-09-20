import request from '@/utils/request'

// 查询检测记录列表
export function listRecord(query) {
  return request({
    url: '/system/inspectionRecord/list',
    method: 'get',
    params: query
  })
}

// 查询检测记录详细
export function getRecord(recordId) {
  return request({
    url: '/system/inspectionRecord/' + recordId,
    method: 'get'
  })
}

// 新增检测记录
export function addRecord(data) {
  return request({
    url: '/system/inspectionRecord',
    method: 'post',
    data: data
  })
}

// 修改检测记录
export function updateRecord(data) {
  return request({
    url: '/system/inspectionRecord',
    method: 'put',
    data: data
  })
}

// 删除检测记录
export function delRecord(recordId) {
  return request({
    url: '/system/inspectionRecord/' + recordId,
    method: 'delete'
  })
}
