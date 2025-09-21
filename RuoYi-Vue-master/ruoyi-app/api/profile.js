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

// 发送验证码
export function sendCode(data) {
  return request({
    url: '/system/user/profile/sendCode',
    method: 'post',
    data: data
  })
}

// 绑定手机号
export function bindPhone(data) {
  return request({
    url: '/system/user/profile/bindPhone',
    method: 'put',
    data: data
  })
}
