package com.ruoyi.system.service;

import java.util.Map;

public interface WechatService {
    String getAccessToken();

    boolean sendSubscribeMessage(String openId, String templateId,String title,String address);
    
    /**
     * 发送订阅消息
     * @param openId 用户openId
     * @param templateId 模板ID
     * @param page 跳转页面
     * @param data 模板数据
     * @return 发送结果
     */
    boolean sendSubscribeMessage(String openId, String templateId, String page, Map<String, Object> data);
    
    /**
     * 发送投诉处理通知
     * @param openId 用户openId
     * @param complaintTitle 投诉标题
     * @param status 处理状态
     * @param remark 备注
     * @return 发送结果
     */
    boolean sendComplaintNotification(String openId, String complaintTitle, String status, String remark);
    
    /**
     * 发送会议通知
     * @param openId 用户openId
     * @param meetingTitle 会议标题
     * @param meetingTime 会议时间
     * @param meetingLocation 会议地点
     * @return 发送结果
     */
    boolean sendMeetingNotification(String openId, String meetingTitle, String meetingTime, String meetingLocation);
    
    /**
     * 发送缴费提醒
     * @param openId 用户openId
     * @param feeType 费用类型
     * @param amount 金额
     * @param dueDate 到期日期
     * @return 发送结果
     */
    boolean sendPaymentReminder(String openId, String feeType, String amount, String dueDate);
    
    /**
     * 发送维修进度通知
     * @param openId 用户openId
     * @param repairTitle 维修标题
     * @param status 维修状态
     * @param remark 备注
     * @return 发送结果
     */
    boolean sendRepairNotification(String openId, String repairTitle, String status, String remark);
}
