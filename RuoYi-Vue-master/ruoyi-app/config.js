// 应用全局配置
module.exports = {
  // 开发环境
  // baseUrl: 'http://www.zhuote.cloud/userapi',
  // 生产环境
  baseUrl: 'http://localhost:8081/userapi',
  
  // 应用信息
  appInfo: {
    // 应用名称
    name: "卓特物业管理",
    // 应用版本
    version: "2.0.0",
    // 应用logo
    logo: "/static/logo.png",
    // 官方网站
    site_url: "http://ruoyi.vip",
    // 政策协议
    agreements: [{
        title: "隐私政策",
        url: "https://ruoyi.vip/protocol.html"
      },
      {
        title: "用户服务协议",
        url: "https://ruoyi.vip/protocol.html"
      }
    ]
  },
  
  // 物业系统特有配置
  property: {
    // 小区信息
    communityInfo: {
      name: "卓特花园小区",
      address: "北京市朝阳区科技路88号",
      totalHouseholds: 1256,
      totalBuildings: 12,
      phone: "010-88888888"
    },
    
    // 功能开关
    features: {
      voting: true,        // 投票功能
      complaint: true,     // 投诉功能
      fundQuery: true,     // 资金查询
      regulation: true,    // 制度查阅
      notary: true,        // 公证服务
      consultation: true   // 法律咨询
    },
    
    // 主题色彩配置
    theme: {
      primaryColor: '#1890FF',      // 主色调
      successColor: '#52C41A',      // 成功色
      warningColor: '#FAAD14',      // 警告色
      errorColor: '#F5222D',        // 错误色
      textColor: '#262626',         // 主要文字色
      textColorSecondary: '#8C8C8C' // 次要文字色
    }
  }
}
