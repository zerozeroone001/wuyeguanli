package com.ruoyi.system.service;

public interface SmsService {
    boolean sendSms(String phoneNumber, String code);

    boolean sendWechatLinkSms(String phoneNumber, Long meetingId);
}
