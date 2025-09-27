// 订阅消息API
import request from '@/utils/request'

// 发送投诉处理通知
export function sendComplaintNotification(data) {
  return request({
    url: '/subscribe/complaint',
    method: 'post',
    data: data
  })
}

// 发送会议通知
export function sendMeetingNotification(data) {
  return request({
    url: '/subscribe/meeting',
    method: 'post',
    data: data
  })
}

// 发送缴费提醒
export function sendPaymentReminder(data) {
  return request({
    url: '/subscribe/payment',
    method: 'post',
    data: data
  })
}

// 发送维修进度通知
export function sendRepairNotification(data) {
  return request({
    url: '/subscribe/repair',
    method: 'post',
    data: data
  })
}