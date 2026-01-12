package com.ruoyi.system.service;

public interface SmsService {
    boolean sendSms(String phoneNumber, String code);

    boolean sendWechatLinkSms(String phoneNumber, Long meetingId);

    /**
     * 发送单条会议通知短信
     * @param phoneNumber 手机号
     * @param meetingId 会议ID
     * @return 结果
     */
    boolean sendSingleMeetingSms(String phoneNumber, Long meetingId);
}
