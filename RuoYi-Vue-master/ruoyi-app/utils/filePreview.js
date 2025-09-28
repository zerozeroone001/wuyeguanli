/**
 * 文件预览工具函数
 * 提供统一的文件预览功能，支持多种文件格式
 */

/**
 * 构建正确的文件访问URL
 * @param {string} fileUrl 文件URL
 * @param {string} baseUrl 基础URL
 * @returns {string} 完整的文件访问URL
 */
function buildFileUrl(fileUrl, baseUrl) {
  if (!fileUrl) return '';
  
  if (fileUrl.startsWith('http://') || fileUrl.startsWith('https://')) {
    // 如果是完整的URL，直接使用
    return fileUrl;
  } else if (fileUrl.startsWith('/profile/')) {
    // 如果已经是profile路径，直接拼接baseUrl
    return baseUrl + fileUrl;
  } else {
    // 如果是相对路径，添加profile前缀
    return baseUrl + '/profile' + fileUrl;
  }
}

/**
 * 根据文件名获取文件扩展名
 * @param {string} fileName 文件名
 * @returns {string} 文件扩展名
 */
function getFileExtension(fileName) {
  if (!fileName) return '';
  return fileName.split('.').pop().toLowerCase();
}

/**
 * 预览文件
 * @param {Object} file 文件对象
 * @param {string} file.fileUrl 文件URL
 * @param {string} file.fileName 文件名
 * @param {string} baseUrl 基础URL
 * @param {Object} options 选项
 * @param {boolean} options.showLoading 是否显示加载提示
 * @param {boolean} options.enableConsoleLog 是否启用控制台日志
 */
function previewFile(file, baseUrl, options = {}) {
  const {
    showLoading = true,
    enableConsoleLog = true
  } = options;

  if (!file || !file.fileUrl) {
    uni.showToast({
      title: '无效的文件信息',
      icon: 'none'
    });
    return;
  }

  const fileUrl = buildFileUrl(file.fileUrl, baseUrl);
  const fileName = file.fileName || file.name || '';
  const ext = getFileExtension(fileName);

  if (enableConsoleLog) {
    console.log('文件预览URL:', fileUrl);
    console.log('文件扩展名:', ext);
  }

  // 根据文件类型选择预览方式
  if (['png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp'].includes(ext)) {
    // 图片文件使用预览图片
    uni.previewImage({
      urls: [fileUrl],
      fail: (error) => {
        if (enableConsoleLog) {
          console.error('图片预览失败:', error);
        }
        uni.showToast({
          title: '图片预览失败',
          icon: 'none'
        });
      }
    });
  } else if (['pdf'].includes(ext)) {
    // PDF文件使用webview预览
    uni.navigateTo({
      url: `/pages/common/webview/webview?title=${encodeURIComponent(fileName)}&url=${encodeURIComponent(fileUrl)}`,
      fail: (error) => {
        if (enableConsoleLog) {
          console.error('webview打开失败:', error);
        }
        uni.showToast({
          title: '文件预览失败',
          icon: 'none'
        });
      }
    });
  } else {
    // 其他文件使用下载预览
    if (showLoading) {
      uni.showLoading({
        title: '文件加载中...'
      });
    }

    uni.downloadFile({
      url: fileUrl,
      success: (res) => {
        if (enableConsoleLog) {
          console.log('文件下载成功:', res);
        }
        if (res.statusCode === 200) {
          uni.openDocument({
            filePath: res.tempFilePath,
            showMenu: true,
            fail: (error) => {
              if (enableConsoleLog) {
                console.error('文件预览失败:', error);
              }
              uni.showToast({
                title: '不支持预览该文件格式',
                icon: 'none'
              });
            }
          });
        } else {
          uni.showToast({
            title: `文件下载失败，状态码：${res.statusCode}`,
            icon: 'none'
          });
        }
      },
      fail: (error) => {
        if (enableConsoleLog) {
          console.error('文件下载失败:', error);
        }
        uni.showToast({
          title: '文件下载失败，请检查网络连接',
          icon: 'none'
        });
      },
      complete: () => {
        if (showLoading) {
          uni.hideLoading();
        }
      }
    });
  }
}

/**
 * 预览附件（兼容不同的附件对象结构）
 * @param {Object} attachment 附件对象
 * @param {string} attachment.url 附件URL
 * @param {string} attachment.name 附件名称
 * @param {string} baseUrl 基础URL
 * @param {Object} options 选项
 */
function previewAttachment(attachment, baseUrl, options = {}) {
  const file = {
    fileUrl: attachment.url,
    fileName: attachment.name
  };
  previewFile(file, baseUrl, options);
}

/**
 * 获取文件图标类型
 * @param {string} fileName 文件名
 * @returns {string} 图标类型
 */
function getFileIcon(fileName) {
  if (!fileName) return 'document';
  
  const ext = getFileExtension(fileName);
  
  if (['png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp'].includes(ext)) {
    return 'image';
  }
  if (['zip', 'rar', '7z', 'tar', 'gz'].includes(ext)) {
    return 'folder';
  }
  if (['pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt'].includes(ext)) {
    return 'paperclip';
  }
  if (['mp4', 'avi', 'mov', 'wmv', 'flv'].includes(ext)) {
    return 'videocam';
  }
  if (['mp3', 'wav', 'flac', 'aac'].includes(ext)) {
    return 'sound';
  }
  
  return 'document';
}

module.exports = {
  buildFileUrl,
  getFileExtension,
  previewFile,
  previewAttachment,
  getFileIcon
};