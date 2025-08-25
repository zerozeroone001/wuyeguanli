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
import com.ruoyi.framework.web.service.SysPermissionService;
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
import java.util.Set;
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

    @Autowired
    private SysPermissionService permissionService;

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

        if (user == null) {
            user = new SysUser();
            user.setOpenid(openid);
            user.setUserName("wx_user_" + ThreadLocalRandom.current().nextLong(100000, 999999)); // 生成一个唯一的用户名


            // 尝试从DTO获取用户信息，如果获取不到则使用默认值
            Map<String, Object> userInfo = wechatLoginDto.getUserInfo();
            if (userInfo != null) {
                user.setNickName((String) userInfo.get("nickName"));
                user.setAvatar((String) userInfo.get("avatarUrl"));
            } else {
                user.setNickName("微信用户");
                // 可以设置一个默认头像URL
                 user.setAvatar("");
            }
            // RuoYi需要一个密码，我们设置一个随机的强密码，但登录不使用它
            user.setPassword(String.valueOf(System.currentTimeMillis()));
            userService.insertWxUser(user);
            // 重新查询一次，确保用户信息完整
            user = userService.selectUserByOpenid(openid);
        }
        System.out.println(user);
        // 4. 生成Token
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));

        LoginUser loginUser = new LoginUser(user.getUserId(), user.getDeptId(), user,new HashSet<>());
        
        recordLoginInfo(loginUser.getUserId());
        
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
