import request from '@/utils/request'

// 查询轮播图列表（小程序端）
export function listBanner(query) {
  return request({
    url: '/banner/list',
    method: 'get',
    params: query
  })
}
