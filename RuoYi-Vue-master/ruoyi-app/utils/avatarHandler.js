/**
 * 头像处理工具
 * 解决微信开发者工具中chooseAvatar组件的文件路径问题
 */

import upload from '@/utils/upload'

/**
 * 检测是否为开发环境
 */
export function isDevEnvironment() {
  try {
    const systemInfo = uni.getSystemInfoSync()
    return process.env.NODE_ENV === 'development' || 
           systemInfo.platform === 'devtools' ||
           systemInfo.environment === 'develop'
  } catch (e) {
    console.warn('获取系统信息失败:', e)
    return false
  }
}

/**
 * 处理头像选择
 * @param {string} avatarUrl - 头像URL
 * @param {function} onSuccess - 成功回调
 * @param {function} onError - 错误回调
 */
export function handleAvatarSelection(avatarUrl, onSuccess, onError) {
  if (!avatarUrl) {
    onError && onError('头像URL为空')
    return
  }

  // 开发环境下直接使用临时路径
  if (isDevEnvironment()) {
    console.log('开发环境，使用临时头像路径:', avatarUrl)
    onSuccess && onSuccess(avatarUrl, true) // true表示是临时路径
    return
  }

  // 生产环境下上传头像
  uni.showLoading({
    title: '头像上传中...',
    mask: true
  })

  upload({ 
    url: '/common/upload',
    filePath: avatarUrl
  }).then(res => {
    uni.hideLoading()
    if (res.code === 200) {
      onSuccess && onSuccess(res.url, false) // false表示是正式URL
    } else {
      onError && onError(res.msg || "头像上传失败")
    }
  }).catch(err => {
    uni.hideLoading()
    console.error('头像上传失败:', err)
    
    // 如果上传失败，在开发环境下使用临时路径
    if (isDevEnvironment()) {
      console.log('上传失败，开发环境使用临时路径:', avatarUrl)
      onSuccess && onSuccess(avatarUrl, true)
    } else {
      onError && onError("头像上传失败")
    }
  })
}

/**
 * 获取头像显示URL
 * @param {string} avatarUrl - 头像URL
 * @returns {string} 显示用的头像URL
 */
export function getAvatarDisplayUrl(avatarUrl) {
  if (!avatarUrl) {
    return '/static/avatar.png' // 默认头像
  }
  
  // 如果是临时路径（开发环境），直接返回
  if (isDevEnvironment() && avatarUrl.includes('tmp/')) {
    return avatarUrl
  }
  
  // 如果是完整URL，直接返回
  if (avatarUrl.startsWith('http')) {
    return avatarUrl
  }
  
  // 如果是相对路径，需要拼接baseUrl
  const config = getApp().globalData.config
  return config.baseUrl + avatarUrl
}