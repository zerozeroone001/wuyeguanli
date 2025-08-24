# RuoYi-User 启动指南

## 问题解决方案总结

### 主要问题
您的ruoyi-user模块无法调用service模块的原因是**Spring Boot包扫描配置问题**。

### 解决方案

#### 1. 修改启动类 (已完成)
在 `RuoyiUserApplication.java` 中添加了：
- `scanBasePackages = {"com.ruoyi"}` - 扫描所有ruoyi包
- `exclude = { DataSourceAutoConfiguration.class }` - 排除数据源自动配置

#### 2. 创建配置类 (已完成)
新增 `UserApplicationConfig.java` 配置类：
- `@ComponentScan` - 明确指定要扫描的包
- `@MapperScan` - 配置MyBatis Mapper扫描

#### 3. 添加必要的配置文件 (已完成)
- `mybatis/mybatis-config.xml` - MyBatis配置
- `i18n/messages.properties` - 国际化配置

#### 4. 创建测试控制器 (已完成)
- `TestController.java` - 用于验证Service注入是否正常

## 启动步骤

### 1. 检查数据库连接
确保 `application-druid.yml` 中的数据库配置正确：
```yaml
url: jdbc:mysql://182.92.244.123:3306/yewu?...
username: root
password: 45625852@Qk..
```

### 2. 启动应用
```bash
# 方式1: 使用IDE启动
运行 RuoyiUserApplication.java 主类

# 方式2: 使用Maven启动 (如果安装了Maven)
mvn spring-boot:run

# 方式3: 使用Java命令启动
java -jar target/ruoyi-user-0.0.1-SNAPSHOT.jar
```

### 3. 验证启动
访问测试接口：
- 健康检查: http://localhost:8081/test/health
- Service测试: http://localhost:8081/test/service

### 4. 预期结果
如果配置正确，您应该看到：
- 控制台输出: "用户端启动成功"
- 测试接口返回成功信息
- 无Service注入相关错误

## 常见问题排查

### 1. 如果仍然无法注入Service
检查以下几点：
- 确保ruoyi-system依赖已正确添加到pom.xml
- 确保数据库连接正常
- 确保Redis连接正常（如果使用）

### 2. 如果出现端口冲突
修改 `application.yml` 中的端口：
```yaml
server:
  port: 8082  # 改为其他端口
```

### 3. 如果出现数据库连接问题
检查：
- 数据库服务是否启动
- 网络连接是否正常
- 用户名密码是否正确

## 项目结构说明

```
ruoyi-user/
├── src/main/java/com/ruoyi/ruoyiuser/
│   ├── RuoyiUserApplication.java     # 启动类
│   ├── config/
│   │   └── UserApplicationConfig.java # 配置类
│   └── controller/
│       ├── TestController.java        # 测试控制器
│       └── [其他控制器]
└── src/main/resources/
    ├── application.yml               # 主配置文件
    ├── application-druid.yml         # 数据库配置
    ├── mybatis/
    │   └── mybatis-config.xml       # MyBatis配置
    └── i18n/
        └── messages.properties      # 国际化配置
```

## 下一步建议

1. **测试现有控制器**：验证其他控制器是否能正常调用Service
2. **添加用户端特定功能**：基于移动端需求开发新的API
3. **配置跨域**：如果需要前端调用，配置CORS
4. **安全配置**：根据需要配置Spring Security

如果按照以上步骤仍有问题，请提供具体的错误日志信息。