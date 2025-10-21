package com.ruoyi.framework.config.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置
 * 
 * @author Gemini
 */
@Configuration
public class AliyunOssConfig {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId:}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret:}")
    private String accessKeySecret;

    @Autowired
    private ISysConfigService configService;

    @Bean
    public OSS ossClient() {
        String dbAccessKeyId = configService.selectConfigByKey("sys_ali_accessKeyId");
        String dbAccessKeySecret = configService.selectConfigByKey("sys_ali_accessKeySecret");

        String resolvedAccessKeyId = StringUtils.isNotEmpty(dbAccessKeyId) ? dbAccessKeyId : accessKeyId;
        String resolvedAccessKeySecret = StringUtils.isNotEmpty(dbAccessKeySecret) ? dbAccessKeySecret : accessKeySecret;

        return new OSSClientBuilder().build(endpoint, resolvedAccessKeyId, resolvedAccessKeySecret);
    }
}
