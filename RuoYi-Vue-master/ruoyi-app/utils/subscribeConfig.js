// 订阅消息配置
const SubscribeConfig = {
  // 模板ID配置 - 需要在微信公众平台配置后替换
  templates: {
    // 投诉处理通知模板
    complaint: '您的投诉处理模板ID',
    // 会议通知模板  
    meeting: 'BcUGoFjWAOUra4Js53I-RiH0OejhvAqKH82DxJo6tMc',
    // 缴费提醒模板
    payment: '您的缴费提醒模板ID',
    // 维修进度通知模板
    repair: '您的维修进度模板ID'
  },
  
  // 获取模板ID
  getTemplateId(type) {
    return this.templates[type] || ''
  },
  
  // 检查模板ID是否已配置
  isTemplateConfigured(type) {
    const templateId = this.getTemplateId(type)
    return templateId && templateId !== `您的${type}模板ID`
  }
}

export default SubscribeConfig