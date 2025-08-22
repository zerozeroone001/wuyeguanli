import request from '@/utils/request'

// 获取首页数据
export function getHomeData() {
  return request({
    url: '/app/home/data',
    method: 'get'
  })
}

// 获取公告列表
export function getNotices() {
  return request({
    url: '/app/home/notices',
    method: 'get'
  })
}

// 获取最新制度文件
export function getLatestRegulations() {
  return request({
    url: '/app/home/regulations',
    method: 'get'
  })
}

// 获取天气信息
export function getWeather() {
  return request({
    url: '/app/home/weather',
    method: 'get'
  })
}

// 获取系统动态
export function getDynamics() {
  return request({
    url: '/app/home/dynamics',
    method: 'get'
  })
}

// 获取公告详情
export function getNoticeDetail(noticeId) {
  return request({
    url: '/system/notice/' + noticeId,
    method: 'get'
  })
}