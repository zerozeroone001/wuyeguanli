package com.ruoyi.user.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.user.domain.dto.WechatLoginDto;
import com.ruoyi.user.service.IWechatLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WechatLoginServiceImpl implements IWechatLoginService {

    private static final Logger log = LoggerFactory.getLogger(WechatLoginServiceImpl.class);

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Override
    public String wechatLogin(WechatLoginDto wechatLoginDto) {
        // 1. 校验appid和secret
        if (StringUtils.isAnyEmpty(appid, secret)) {
            log.error("微信登录配置缺失：wx.appid或wx.secret未配置");
            throw new ServiceException("系统配置错误，微信登录功能暂不可用");
        }

        // 2. 调用微信接口获取openid
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid +
                "&secret=" + secret + "&js_code=" + wechatLoginDto.getCode() + "&grant_type=authorization_code";
        System.out.println(wechatLoginDto.getCode()+"+++++++++++++++++++++");
        String result = HttpUtils.sendGet(url);
        log.info("WeChat API response: {}", result);

        if (StringUtils.isEmpty(result)) {
            throw new ServiceException("调用微信接口失败，请稍后重试");
        }

        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        Integer errcode = jsonObject.getInteger("errcode");

        if (StringUtils.isEmpty(openid)) {
            log.error("调用微信jscode2session接口失败: {}", result);
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
        } else {
            // 用户已存在，更新用户信息
            boolean needUpdate = false;
            
            if (StringUtils.isNotEmpty(nickName) && !nickName.equals(user.getNickName())) {
                user.setNickName(nickName);
                needUpdate = true;
            }
            
            if (StringUtils.isNotEmpty(avatarUrl) && !avatarUrl.equals(user.getAvatar()) && StringUtils.isEmpty(user.getAvatar())) {
                user.setAvatar(avatarUrl);
                needUpdate = true;
            }
            
            // 如果有信息更新，则保存到数据库
            if (needUpdate) {
                userService.updateUserProfile(user);
                log.info("更新用户信息 - 用户ID: {}, 昵称: {}, 头像: {}", user.getUserId(), nickName, avatarUrl);
            }
        }
        System.out.println(user);
        // 4. 生成Token
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));

        LoginUser loginUser = new LoginUser(user.getUserId(), user.getDeptId(), user,new HashSet<>());
        
//        recordLoginInfo(loginUser.getUserId());
        
        return tokenService.createToken(loginUser);
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
