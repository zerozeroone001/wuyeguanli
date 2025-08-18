import request from '@/utils/request'

// 获取制度文件列表
export function listRegulation(query) {
  return request({
    url: '/property/regulation/list',
    method: 'get',
    params: query
  })
}

// 获取制度文件详情
export function getRegulation(regulationId) {
  return request({
    url: '/property/regulation/' + regulationId,
    method: 'get'
  })
}

// 下载制度文件
export function downloadRegulation(regulationId) {
  return request({
    url: '/property/regulation/download/' + regulationId,
    method: 'get',
    responseType: 'blob'
  })
}
 
 