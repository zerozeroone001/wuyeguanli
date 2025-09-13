import request from '@/utils/request'

// 查询制度分类列表
export function listCategory(query) {
  return request({
    url: '/regulationCategory/list',
    method: 'get',
    params: query
  })
}

// 查询制度管理列表
export function listRegulation(query) {
  return request({
    url: '/regulation/list',
    method: 'get',
    params: query
  })
}

// 查询制度管理详细
export function getRegulation(regulationId) {
  return request({
    url: '/regulation/' + regulationId,
    method: 'get'
  })
}

// 建议：增加浏览次数接口
export function incrementViewCount(regulationId) {
    return request({
        url: '/regulation/incrementView/' + regulationId,
        method: 'put'
    })
}
