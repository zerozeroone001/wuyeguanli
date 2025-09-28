/**
 * 认证状态判断工具函数
 * 基于is_owner字段进行认证状态判断
 */

/**
 * 认证状态枚举
 */
export const AUTH_STATUS = {
  UNAUTHENTICATED: 0,  // 未认证业主
  OWNER: 1,           // 已认证业主
  COMMITTEE: 2        // 业委会成员
}

/**
 * 认证状态文本映射
 */
export const AUTH_STATUS_TEXT = {
  [AUTH_STATUS.UNAUTHENTICATED]: '未认证业主',
  [AUTH_STATUS.OWNER]: '已认证业主',
  [AUTH_STATUS.COMMITTEE]: '业委会成员'
}

/**
 * 判断用户是否已认证
 * @param {number} isOwner - is_owner字段值
 * @returns {boolean} 是否已认证
 */
export function isAuthenticated(isOwner) {
  return isOwner > 0
}

/**
 * 判断用户是否为业主
 * @param {number} isOwner - is_owner字段值
 * @returns {boolean} 是否为业主
 */
export function isOwner(isOwner) {
  return isOwner === AUTH_STATUS.OWNER
}

/**
 * 判断用户是否为业委会成员
 * @param {number} isOwner - is_owner字段值
 * @returns {boolean} 是否为业委会成员
 */
export function isCommitteeMember(isOwner) {
  return isOwner === AUTH_STATUS.COMMITTEE
}

/**
 * 获取认证状态文本
 * @param {number} isOwner - is_owner字段值
 * @returns {string} 认证状态文本
 */
export function getAuthStatusText(isOwner) {
  return AUTH_STATUS_TEXT[isOwner] || AUTH_STATUS_TEXT[AUTH_STATUS.UNAUTHENTICATED]
}

/**
 * 获取认证状态颜色
 * @param {number} isOwner - is_owner字段值
 * @returns {string} 认证状态对应的颜色
 */
export function getAuthStatusColor(isOwner) {
  switch (isOwner) {
    case AUTH_STATUS.UNAUTHENTICATED:
      return '#FF4D4F' // 红色 - 未认证
    case AUTH_STATUS.OWNER:
      return '#52C41A' // 绿色 - 已认证业主
    case AUTH_STATUS.COMMITTEE:
      return '#1890FF' // 蓝色 - 业委会成员
    default:
      return '#FF4D4F'
  }
}

/**
 * 获取认证状态图标
 * @param {number} isOwner - is_owner字段值
 * @returns {string} 认证状态对应的图标类型
 */
export function getAuthStatusIcon(isOwner) {
  switch (isOwner) {
    case AUTH_STATUS.UNAUTHENTICATED:
      return 'close-circle' // 未认证图标
    case AUTH_STATUS.OWNER:
      return 'home-filled' // 业主图标
    case AUTH_STATUS.COMMITTEE:
      return 'staff' // 业委会图标
    default:
      return 'close-circle'
  }
}

/**
 * 检查用户权限
 * @param {number} isOwner - is_owner字段值
 * @param {string} requiredAuth - 需要的认证级别 ('any' | 'owner' | 'committee')
 * @returns {boolean} 是否有权限
 */
export function hasAuthPermission(isOwner, requiredAuth = 'any') {
  switch (requiredAuth) {
    case 'any':
      return isAuthenticated(isOwner)
    case 'owner':
      return isOwner(isOwner)
    case 'committee':
      return isCommitteeMember(isOwner)
    default:
      return false
  }
}

/**
 * 获取认证进度百分比
 * @param {number} isOwner - is_owner字段值
 * @returns {number} 认证进度百分比 (0-100)
 */
export function getAuthProgress(isOwner) {
  switch (isOwner) {
    case AUTH_STATUS.UNAUTHENTICATED:
      return 0
    case AUTH_STATUS.OWNER:
      return 50
    case AUTH_STATUS.COMMITTEE:
      return 100
    default:
      return 0
  }
}

export default {
  AUTH_STATUS,
  AUTH_STATUS_TEXT,
  isAuthenticated,
  isOwner,
  isCommitteeMember,
  getAuthStatusText,
  getAuthStatusColor,
  getAuthStatusIcon,
  hasAuthPermission,
  getAuthProgress
}