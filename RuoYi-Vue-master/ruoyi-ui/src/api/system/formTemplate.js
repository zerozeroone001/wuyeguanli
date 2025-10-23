import request from '@/utils/request'

// 查询表单模板列表
export function listFormTemplate(query) {
  return request({
    url: '/system/formTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询表单模板选项列表（轻量）
export function listFormTemplateOptions(query) {
  return request({
    url: '/system/formTemplate/options',
    method: 'get',
    params: query
  })
}

// 查询表单模板详细
export function getFormTemplate(formId) {
  return request({
    url: '/system/formTemplate/' + formId,
    method: 'get'
  })
}

// 新增表单模板
export function addFormTemplate(data) {
  return request({
    url: '/system/formTemplate',
    method: 'post',
    data: data
  })
}

// 修改表单模板
export function updateFormTemplate(data) {
  return request({
    url: '/system/formTemplate',
    method: 'put',
    data: data
  })
}

// 删除表单模板
export function delFormTemplate(formId) {
  return request({
    url: '/system/formTemplate/' + formId,
    method: 'delete'
  })
}

