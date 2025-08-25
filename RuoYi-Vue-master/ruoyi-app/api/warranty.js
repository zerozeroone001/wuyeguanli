import request from '@/utils/request'

// 新增设备保修
export function addWarranty(data) {
  return request({
    url: '/warranty/add',
    method: 'post',
    data: data
  })
}
