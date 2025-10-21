package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.dto.WechatLoginDto;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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

    @Autowired
    private ISysUserService userService;

    @Override
    public String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        url = url.replace("APPID", appid).replace("APPSECRET", secret);
        String result = HttpUtils.sendGet(url);
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject.getString("access_token");
    }

    @Override
    public SysUser wechatLogin(WechatLoginDto wechatLoginDto) {
        // 1. 校验appid和secret
        if (StringUtils.isAnyEmpty(appid, secret)) {
            throw new ServiceException("系统配置错误，微信登录功能暂不可用");
        }

        // 2. 调用微信接口获取openid
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid +
                "&secret=" + secret + "&js_code=" + wechatLoginDto.getCode() + "&grant_type=authorization_code";
        System.out.println(wechatLoginDto.getCode()+"+++++++++++++++++++++");
        String result = HttpUtils.sendGet(url);

        if (StringUtils.isEmpty(result)) {
            throw new ServiceException("调用微信接口失败，请稍后重试");
        }

        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        Integer errcode = jsonObject.getInteger("errcode");

        if (StringUtils.isEmpty(openid)) {
            throw new ServiceException("微信登录失败，errcode: " + errcode);
        }

        // 3. 根据openid查找或创建用户
        SysUser user = userService.selectUserByOpenid(openid);

        // 获取前端传递的用户信息
        Map<String, Object> userInfo = wechatLoginDto.getUserInfo();
        String nickName = null;
        String avatarUrl = null;

        if (userInfo != null) {
            nickName = (String) userInfo.get("nickName");
            avatarUrl = (String) userInfo.get("avatarUrl");
        }

        if (user == null) {
            // 创建新用户
            user = new SysUser();
            user.setOpenid(openid);

            user.setUserName("wx_user_" + ThreadLocalRandom.current().nextLong(100000, 999999)); // 生成一个唯一的用户名

            // 设置用户信息
            if (StringUtils.isNotEmpty(nickName)) {
                user.setNickName(nickName);
            } else {
                user.setNickName("微信用户");
            }

            if (StringUtils.isNotEmpty(avatarUrl)) {
                user.setAvatar(avatarUrl);
            } else {
                user.setAvatar("");
            }

            // RuoYi需要一个密码，我们设置一个随机的强密码，但登录不使用它
            user.setPassword(String.valueOf(System.currentTimeMillis()));
            userService.insertWxUser(user);
            // 重新查询一次，确保用户信息完整
            user = userService.selectUserByOpenid(openid);
        }
        return user;
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
    public String getPageLink(Long meetingId) {
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/generate_urllink?access_token=" + accessToken;
        JSONObject data = new JSONObject();
        data.put("path", "pages/property/meeting/vote");
        data.put("query", "id="+meetingId);
        data.put("expire_type", "1");
        data.put("expire_interval", "30");
        String result = HttpUtils.sendPost(url, data.toJSONString());
        JSONObject response = JSON.parseObject(result);
        if (response.getInteger("errcode") == 0) {
            return response.getString("url_link");
        }
        return "";
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
