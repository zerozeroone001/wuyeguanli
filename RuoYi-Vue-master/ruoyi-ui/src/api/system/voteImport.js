import request from '@/utils/request'

// 批量导入投票
export function importVotes(meetingId, formData) {
  return request({
    url: `/system/vote/import/${meetingId}`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 导入单个投票文件
export function importSingleVote(meetingId, formData) {
  return request({
    url: `/system/vote/import/single/${meetingId}`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 验证投票文件
export function validateVoteFiles(formData) {
  return request({
    url: '/system/vote/validate',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取导入进度
export function getImportProgress(taskId) {
  return request({
    url: `/system/vote/progress/${taskId}`,
    method: 'get'
  })
}
