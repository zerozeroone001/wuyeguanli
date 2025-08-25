package com.ruoyi.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 用户端启动程序
 * 
 * @author ruoyi
 */

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }, scanBasePackages = {""})

public class RuoyiUserApplication {

    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoyiUserApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  用户端启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
