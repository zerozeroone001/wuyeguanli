import request from '@/utils/request'

// 提交业主认证申请
export function submitAuth(data) {
  return request({
    url: '/ownerProfile/submitAuth',
    method: 'post',
    data: data
  })
}

// 获取我的认证信息
export function getMyProfile() {
  return request({
    url: '/ownerProfile/myProfile',
    method: 'get'
  })
}
