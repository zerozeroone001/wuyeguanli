import upload from '@/utils/upload'
import request from '@/utils/request'

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/system/user/profile/updatePwd',
    method: 'put',
    data: data
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/user/getInfo',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/user/updateProfile',
    method: 'post',
    data: data
  })
}

// 业主认证
export function ownerAuth(data) {
  return request({
    url: '/user/ownerAuth',
    method: 'post',
    data: data
  })
}

// 获取认证状态
export function getAuthStatus() {
  return request({
    url: '/user/authStatus',
    method: 'get'
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return upload({
    url: '/system/user/profile/avatar',
    name: data.name,
    filePath: data.filePath
  })
}
