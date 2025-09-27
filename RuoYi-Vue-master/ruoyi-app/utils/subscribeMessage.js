// 订阅消息工具类
const SubscribeMessageUtils = {
  /**
   * 请求订阅消息授权
   * @param {string} templateId 模板ID
   * @param {string} scene 场景值
   * @returns {Promise<boolean>} 是否授权成功
   */
  requestSubscribe(templateId, scene = '') {
    return new Promise((resolve, reject) => {
      uni.requestSubscribeMessage({
        tmplIds: [templateId],
        success: (res) => {
          console.log('订阅消息授权结果:', res);
          // 检查授权结果
          if (res[templateId] === 'accept') {
            resolve(true);
          } else {
            console.log('用户拒绝订阅消息');
            resolve(false);
          }
        },
        fail: (err) => {
          console.error('订阅消息授权失败:', err);
          reject(err);
        }
      });
    });
  },

  /**
   * 批量请求订阅消息授权
   * @param {Array<string>} templateIds 模板ID数组
   * @param {string} scene 场景值
   * @returns {Promise<Object>} 授权结果
   */
  requestMultipleSubscribe(templateIds, scene = '') {
    return new Promise((resolve, reject) => {
      uni.requestSubscribeMessage({
        tmplIds: templateIds,
        success: (res) => {
          console.log('批量订阅消息授权结果:', res);
          resolve(res);
        },
        fail: (err) => {
          console.error('批量订阅消息授权失败:', err);
          reject(err);
        }
      });
    });
  },

  /**
   * 检查订阅消息状态
   * @param {string} templateId 模板ID
   * @returns {Promise<Object>} 订阅状态
   */
  checkSubscribeStatus(templateId) {
    return new Promise((resolve, reject) => {
      uni.getSetting({
        success: (res) => {
          const subscribeAuthSetting = res.authSetting['scope.subscribeMessage'];
          resolve({
            templateId,
            status: subscribeAuthSetting
          });
        },
        fail: reject
      });
    });
  }
};

export default SubscribeMessageUtils;