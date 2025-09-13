import request from '@/utils/request'
import { getToken } from '@/utils/auth'

// 通用上传请求
export function uploadFile(filePath) {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: request.baseUrl + '/common/upload',
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': 'Bearer ' + getToken()
      },
      success: (uploadFileRes) => {
        const res = JSON.parse(uploadFileRes.data)
        if (res.code === 200) {
          resolve(res)
        } else {
          reject(res)
        }
      },
      fail: (err) => {
        reject(err)
      }
    })
  })
}
