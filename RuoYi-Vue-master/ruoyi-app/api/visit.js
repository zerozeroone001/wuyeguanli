import request from '@/utils/request'

// 检查当前用户是否为管理员
export function checkAdmin() {
  return request({
    url: '/system/visit/checkAdmin',
    method: 'get'
  })
}

// 获取小区楼栋列表
export function getBuildingList(communityId) {
  return request({
    url: '/system/visit/buildings',
    method: 'get',
    params: { communityId }
  })
}

// 获取楼栋的单元列表
export function getUnitList(communityId, buildingName) {
  return request({
    url: '/system/visit/units',
    method: 'get',
    params: { communityId, buildingName }
  })
}

// 获取房屋列表及业主信息
export function getRoomList(communityId, buildingName, unitName, meetingId) {
  return request({
    url: '/system/visit/rooms',
    method: 'get',
    params: { communityId, buildingName, unitName, meetingId }
  })
}

// 更新线下通知状态
export function updateOfflineStatus(data) {
  return request({
    url: '/system/visit/updateStatus',
    method: 'post',
    data: data
  })
}
