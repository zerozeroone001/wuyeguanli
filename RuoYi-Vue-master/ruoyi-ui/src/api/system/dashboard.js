import request from '@/utils/request'

// 获取统计数据
export function getStatistics() {
  return request({
    url: '/system/dashboard/statistics',
    method: 'get'
  })
}

// 获取待办事项列表
export function getTodoList() {
  return request({
    url: '/system/dashboard/todoList',
    method: 'get'
  })
}

// 获取图表数据
export function getChartData() {
  return request({
    url: '/system/dashboard/chartData',
    method: 'get'
  })
}

// 获取最近活动
export function getRecentActivity() {
  return request({
    url: '/system/dashboard/recentActivity',
    method: 'get'
  })
}

// 完成待办事项
export function completeTodo(todoId) {
  return request({
    url: `/system/dashboard/todo/${todoId}/complete`,
    method: 'put'
  })
}

// 删除待办事项
export function deleteTodo(todoId) {
  return request({
    url: `/system/dashboard/todo/${todoId}`,
    method: 'delete'
  })
}

// 添加待办事项
export function addTodo(data) {
  return request({
    url: '/system/dashboard/todo',
    method: 'post',
    data: data
  })
}
