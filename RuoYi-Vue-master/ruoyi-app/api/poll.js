/**
 * 意见征询问卷接口封装
 * 负责对接 /poll 服务，提供列表、详情与提交能力
 */
import request from '@/utils/request'

/**
 * 获取进行中的意见征询列表
 * @returns {Promise<Array>} 问卷数组
 */
export function listSuggestionPolls() {
  return request({
    url: '/poll/list',
    method: 'get'
  })
}

/**
 * 获取指定问卷详情，包含表单模板与已填记录
 * @param {number|string} pollId 征询ID
 * @returns {Promise<Object>}
 */
export function getSuggestionPollDetail(pollId) {
  return request({
    url: `/poll/${pollId}`,
    method: 'get'
  })
}

/**
 * 提交意见征询问卷
 * @param {number|string} pollId 征询ID
 * @param {Object} data 提交载荷，至少包含 answers 数组
 * @returns {Promise<Object>}
 */
export function submitSuggestionPoll(pollId, data) {
  return request({
    url: `/poll/${pollId}/submit`,
    method: 'post',
    data
  })
}
