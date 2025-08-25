import request from '@/utils/request'

// 新增访客登记
export function addVisitor(data) {
  return request({
    url: '/userapi/visitor/add',
    method: 'post',
    data: data
  })
}
