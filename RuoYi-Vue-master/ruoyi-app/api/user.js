import request from '@/utils/request'

// 发送验证码
export function sendSmsCode(data) {
  return request({
    url: '/user/info/sendCode',
    method: 'post',
    data: data
  })
}

// 绑定手机号
export function bindPhone(data) {
  return request({
    url: '/user/info/bindPhone',
    method: 'post',
    data: data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/user/info/getInfo',
    method: 'get'
  })
}

// 更新用户信息
export function updateUserProfile(data) {
  return request({
    url: '/user/profile',
    method: 'put',
    data: data
  })
}
