import request from '@/utils/request'

// 新增投诉
export function addComplaint(data) {
  return request({
    url: '/userapi/complaint',
    method: 'post',
    data: data
  })
}
