import request from '@/utils/request'

// 查询用户关联的物业服务合同列表
export function listUserContract(query) {
  return request({
    url: '/contract/list',
    method: 'get',
    params: query
  })
}

// 获取物业服务合同详细信息
export function getUserContract(contractId) {
  return request({
    url: '/contract/' + contractId,
    method: 'get'
  })
}
