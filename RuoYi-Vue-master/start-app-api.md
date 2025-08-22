# 小程序API接口对接完成说明

## 已完成的工作

### 1. 后台API控制器创建
为小程序专门创建了以下控制器，路径映射为 `/property/*` 和 `/app/*`：

- **PropertyComplaintController** - 投诉管理接口
  - `POST /property/complaint` - 提交投诉
  - `GET /property/complaint/my` - 获取我的投诉列表
  - `GET /property/complaint/{id}` - 获取投诉详情
  - `GET /property/complaint/handle/{id}` - 获取处理记录
  - `POST /property/complaint/evaluate` - 评价投诉处理

- **PropertyMeetingController** - 会议管理接口
  - `GET /property/meeting/list` - 获取会议列表
  - `GET /property/meeting/{id}` - 获取会议详情
  - `GET /property/meeting/topic/list` - 获取会议议题
  - `POST /property/meeting/vote` - 提交投票
  - `GET /property/meeting/result/{id}` - 获取投票结果
  - `GET /property/meeting/vote/my` - 获取我的投票记录

- **PropertyFundController** - 资金管理接口
  - `GET /property/fund/overview` - 获取资金概览
  - `GET /property/fund/flow/list` - 获取资金流水列表
  - `GET /property/fund/flow/{id}` - 获取资金流水详情
  - `GET /property/fund/statistics` - 获取资金统计数据

- **PropertyRegulationController** - 制度管理接口
  - `GET /property/regulation/list` - 获取制度文件列表
  - `GET /property/regulation/{id}` - 获取制度文件详情
  - `GET /property/regulation/download/{id}` - 下载制度文件
  - `GET /property/regulation/categories` - 获取制度分类
  - `GET /property/regulation/search` - 搜索制度文件

- **AppAuthController** - 用户认证接口
  - `POST /app/login` - 用户登录
  - `POST /app/wxLogin` - 微信小程序登录
  - `GET /app/getInfo` - 获取用户信息
  - `POST /app/updateProfile` - 更新用户资料
  - `GET /app/captcha` - 获取验证码
  - `POST /app/ownerAuth` - 业主认证
  - `GET /app/authStatus` - 获取认证状态

- **AppHomeController** - 首页数据接口
  - `GET /app/home/data` - 获取首页数据
  - `GET /app/home/notices` - 获取公告列表
  - `GET /app/home/regulations` - 获取最新制度文件
  - `GET /app/home/weather` - 获取天气信息
  - `GET /app/home/dynamics` - 获取系统动态

### 2. 小程序API配置更新
- 更新了 `config.js` 中的 baseUrl 配置
- 更新了 `api/login.js` 中的登录相关接口路径
- 更新了 `api/system/user.js` 中的用户相关接口路径
- 创建了 `api/home.js` 首页数据接口文件

### 3. 接口测试页面
创建了 `pages/test/api-test.vue` 测试页面，可以测试所有API接口的连通性。

## 启动和测试步骤

### 1. 启动后台服务
```bash
cd RuoYi-Vue-master
# 启动后台服务（默认端口8080）
mvn spring-boot:run -pl ruoyi-admin
```

### 2. 启动小程序
```bash
cd RuoYi-Vue-master/ruoyi-app
# 在HBuilderX中打开项目
# 或使用uni-app CLI
npm install
npm run dev:mp-weixin
```

### 3. 测试接口
1. 在小程序中导航到测试页面：`/pages/test/api-test`
2. 点击各个测试按钮验证接口连通性
3. 查看控制台输出和页面显示的测试结果

## 注意事项

### 1. 数据库配置
确保后台服务的数据库配置正确，相关表结构已创建：
- sys_property_complaint（投诉管理表）
- sys_property_meeting（会议管理表）
- sys_property_fund_flow（资金流水表）
- sys_property_regulation（制度管理表）

### 2. 权限配置
- 小程序用户需要适当的角色权限
- 可以在系统管理中配置相关菜单和权限

### 3. 跨域配置
如果出现跨域问题，请检查后台的跨域配置。

### 4. Token认证
- 登录后会获得JWT token
- 后续API调用会自动携带token进行认证

## 待完善功能

以下功能在控制器中标注了 TODO，需要根据具体业务需求实现：
1. 微信小程序登录逻辑
2. 投诉处理记录查询
3. 会议议题和投票功能
4. 资金统计计算逻辑
5. 制度文件下载功能
6. 业主认证审核流程
7. 天气API集成

## 接口文档
可以通过Swagger UI查看完整的API文档：
http://localhost:8080/swagger-ui.html

## 技术栈
- 后端：Spring Boot + MyBatis Plus + MySQL
- 前端：uni-app + Vue.js
- 认证：JWT + Spring Security
- 文档：Swagger

## 联系支持
如有问题，请检查：
1. 后台服务是否正常启动
2. 数据库连接是否正常
3. 小程序配置的baseUrl是否正确
4. 网络连接是否正常