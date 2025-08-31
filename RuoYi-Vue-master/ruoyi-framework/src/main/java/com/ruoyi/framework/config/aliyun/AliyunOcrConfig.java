package com.ruoyi.framework.config.aliyun;

//import com.aliyun.ocr_api20210707.Client;
//import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class AliyunOcrConfig {

    @Value("${aliyun.ocr.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.ocr.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.ocr.endpoint}")
    private String endpoint;

//    @Bean
//    public Client aliyunOcrClient() throws Exception {
//        Config config = new Config()
//                .setAccessKeyId(accessKeyId)
//                .setAccessKeySecret(accessKeySecret);
//        config.endpoint = endpoint;
//        return new Client(config);
//    }
}
