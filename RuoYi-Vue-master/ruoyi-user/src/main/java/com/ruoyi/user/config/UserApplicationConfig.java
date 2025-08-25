package com.ruoyi.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 用户端应用配置类
 * 
 * @author ruoyi
 */
@Configuration
@ComponentScan(basePackages = {
    "com.ruoyi.system",
    "com.ruoyi.common",
    "com.ruoyi.framework",
    "com.ruoyi.ruoyiuser"
})
@MapperScan(basePackages = {
    "com.ruoyi.system.mapper",
    "com.ruoyi.ruoyiuser.mapper"
})
public class UserApplicationConfig {
    
}