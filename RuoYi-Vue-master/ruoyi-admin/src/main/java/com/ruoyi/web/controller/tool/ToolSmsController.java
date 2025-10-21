package com.ruoyi.web.controller.tool;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SmsUtils;
import com.ruoyi.system.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/tool/sms")
public class ToolSmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisCache redisCache;

    private final static String SMS_CODE_PREFIX = "sms_code:";

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号码
     * @return 操作结果
     */
    @GetMapping("/send")
    public AjaxResult sendSmsCode(@RequestParam String phoneNumber) {
        // 1. 生成6位随机验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        // 2. 将验证码存入Redis，有效期5分钟
        redisCache.setCacheObject(SMS_CODE_PREFIX + phoneNumber, code, 5, TimeUnit.MINUTES);

        // 3. 发送短信
        boolean isSuccess = smsService.sendSms(phoneNumber, code);

        if (isSuccess) {
            return AjaxResult.success("短信发送成功");
        } else {
            return AjaxResult.error("短信发送失败");
        }
    }
}
