package com.ruoyi.system.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.SmsService;
import com.ruoyi.system.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private ISysUserService userService;

    @Override
    public boolean sendSms(String phoneNumber, String code) {
        String accessKeyId = configService.selectConfigByKey("sys_ali_accessKeyId");
        String accessKeySecret = configService.selectConfigByKey("sys_ali_accessKeySecret");
        String endpoint = configService.selectConfigByKey("sys_ali_endpoint");
        String signName = configService.selectConfigByKey("sys_ali_signName");
        String templateCode = configService.selectConfigByKey("sys_ali_templateCode");


        try {
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret);
            config.endpoint = endpoint;
            Client client = new Client(config);

            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phoneNumber)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam("{\"code\":\"" + code + "\"}");

            SendSmsResponse response = client.sendSms(sendSmsRequest);
            System.out.println(response.getBody().toString());
            // 根据阿里云返回的Code判断发送是否成功
            return "OK".equals(response.getBody().getCode());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sendWechatLinkSms(String phoneNumber, Long meetingId) {
        // 调用WechatService的getPageLink方法获取小程序跳转url
        String url = "";
        // WechatService 在同模块注入
        url = wechatService.getPageLink(meetingId);

        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        // 截取短链后缀，格式如 https://wxaurl.cn/CC6whoWlbEv 获取 CC6whoWlbEv
        String linkPath = "";
            int idx = url.lastIndexOf("/");
            if (idx != -1 && idx + 1 < url.length()) {
                linkPath = url.substring(idx + 1);
            } else {
                linkPath = url;
            }


        // 通过用户表 is_owner 字段获取业主手机号列表
        List<String> ownersPhones = userService.selectOwnerPhoneList();

        boolean allSuccess = true;
        if (ownersPhones != null && !ownersPhones.isEmpty()) {
            for (String phone : ownersPhones) {
                try {
                    // 发送短信 link_path 作为参数
                    String accessKeyId = configService.selectConfigByKey("sys_ali_accessKeyId");
                    String accessKeySecret = configService.selectConfigByKey("sys_ali_accessKeySecret");
                    String endpoint = configService.selectConfigByKey("sys_ali_endpoint");
                    String signName = configService.selectConfigByKey("sys_ali_signName");
                    String templateCode = configService.selectConfigByKey("sys_ali_wechat_link_templateCode"); // 专用模板

                    Config config = new Config()
                            .setAccessKeyId(accessKeyId)
                            .setAccessKeySecret(accessKeySecret);
                    config.endpoint = endpoint;
                    Client client = new Client(config);

                    SendSmsRequest sendSmsRequest = new SendSmsRequest()
                            .setPhoneNumbers(phone)
                            .setSignName(signName)
                            .setTemplateCode(templateCode)
                            .setTemplateParam("{\"link_path\":\"" + linkPath + "\"}");

                    SendSmsResponse response = client.sendSms(sendSmsRequest);
                    if (!"OK".equals(response.getBody().getCode())) {
                        allSuccess = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    allSuccess = false;
                }
            }
        } else {
            return false;
        }
        return allSuccess;
    }
}
