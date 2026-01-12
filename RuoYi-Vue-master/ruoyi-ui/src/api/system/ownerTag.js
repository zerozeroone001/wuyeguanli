import request from '@/utils/request'

// 查询业主标签列表
export function listOwnerTag(query) {
  return request({
    url: '/system/ownerTag/list',
    method: 'get',
    params: query
  })
}

// 查询业主标签详细
export function getOwnerTag(tagId) {
  return request({
    url: '/system/ownerTag/' + tagId,
    method: 'get'
  })
}

// 新增业主标签
export function addOwnerTag(data) {
  return request({
    url: '/system/ownerTag',
    method: 'post',
    data: data
  })
}

// 修改业主标签
export function updateOwnerTag(data) {
  return request({
    url: '/system/ownerTag',
    method: 'put',
    data: data
  })
}

// 删除业主标签
export function delOwnerTag(tagId) {
  return request({
    url: '/system/ownerTag/' + tagId,
    method: 'delete'
  })
}
