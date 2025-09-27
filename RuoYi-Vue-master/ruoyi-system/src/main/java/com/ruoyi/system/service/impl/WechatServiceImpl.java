package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.service.WechatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WechatServiceImpl implements WechatService {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;
    
    @Value("${wx.subscribe.complaint}")
    private String complaintTemplateId;
    
    @Value("${wx.subscribe.meeting}")
    private String meetingTemplateId;
    
    @Value("${wx.subscribe.payment}")
    private String paymentTemplateId;
    
    @Value("${wx.subscribe.repair}")
    private String repairTemplateId;

    @Override
    public String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        url = url.replace("APPID", appid).replace("APPSECRET", secret);
        String result = HttpUtils.sendGet(url);
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject.getString("access_token");
    }

    @Override
    public boolean sendSubscribeMessage(String openId, String templateId,String title,String address) {
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
        JSONObject data = new JSONObject();
        data.put("touser", openId);
        data.put("template_id", templateId);
        data.put("page", "index");
        // 设置消息内容
        JSONObject thing1 = new JSONObject();
        thing1.put("value", title);
        JSONObject thing2 = new JSONObject();
        thing2.put("value", address);
        data.put("data", new JSONObject().fluentPut("thing1", thing1).fluentPut("thing2", thing2));
        String result = HttpUtils.sendPost(url, data.toJSONString());
        JSONObject response = JSON.parseObject(result);
        return response.getInteger("errcode") == 0;
    }
    
    @Override
    public boolean sendSubscribeMessage(String openId, String templateId, String page, Map<String, Object> data) {
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
        
        JSONObject requestData = new JSONObject();
        requestData.put("touser", openId);
        requestData.put("template_id", templateId);
        requestData.put("page", page);
        
        // 构建模板数据
        JSONObject templateData = new JSONObject();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            JSONObject valueObj = new JSONObject();
            valueObj.put("value", entry.getValue());
            templateData.put(entry.getKey(), valueObj);
        }
        requestData.put("data", templateData);
        System.out.println(requestData);
        
        String result = HttpUtils.sendPost(url, requestData.toJSONString());
        JSONObject response = JSON.parseObject(result);
        return response.getInteger("errcode") == 0;
    }
    
    @Override
    public boolean sendComplaintNotification(String openId, String complaintTitle, String status, String remark) {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("thing1", complaintTitle);
        data.put("phrase2", status);
        data.put("thing2", remark);
        return sendSubscribeMessage(openId, complaintTemplateId, "pages/property/complaint/index", data);
    }
    
    @Override
    public boolean sendMeetingNotification(String openId, String meetingTitle, String meetingTime, String meetingLocation) {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("thing1", meetingTitle);
        data.put("time2", meetingTime);
        data.put("thing2", meetingLocation);
        return sendSubscribeMessage(openId, meetingTemplateId, "pages/property/meeting/index", data);
    }
    
    @Override
    public boolean sendPaymentReminder(String openId, String feeType, String amount, String dueDate) {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("thing1", feeType);
        data.put("amount2", amount);
        data.put("time3", dueDate);
        return sendSubscribeMessage(openId, paymentTemplateId, "pages/fund-management/index", data);
    }
    
    @Override
    public boolean sendRepairNotification(String openId, String repairTitle, String status, String remark) {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("thing1", repairTitle);
        data.put("phrase2", status);
        data.put("thing3", remark);
        return sendSubscribeMessage(openId, repairTemplateId, "pages/repair/index", data);
    }
}
