package com.ruoyi.common.utils;

import java.util.Random;

/**
 * 短信验证码工具类
 *
 * @author ruoyi
 */
public class SmsUtils
{
    /**
     * 生成6位数字验证码
     *
     * @return 验证码
     */
    public static String generateCode()
    {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // 生成100000-999999之间的随机数
        return String.valueOf(code);
    }

    /**
     * 生成指定长度的数字验证码
     *
     * @param length 验证码长度
     * @return 验证码
     */
    public static String generateCode(int length)
    {
        if (length <= 0)
        {
            length = 6;
        }

        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 发送短信验证码(此处为模拟发送,实际项目需要对接第三方短信平台)
     *
     * @param phone 手机号
     * @param code 验证码
     * @return 是否发送成功
     */
    public static boolean sendSms(String phone, String code)
    {
        // TODO: 实际项目中应该调用第三方短信平台API
        // 例如: 阿里云短信服务、腾讯云短信服务等

        // 这里只是打印日志,模拟发送成功
        System.out.println("========================================");
        System.out.println("发送短信验证码:");
        System.out.println("手机号: " + phone);
        System.out.println("验证码: " + code);
        System.out.println("========================================");

        return true;
    }

    /**
     * 验证手机号格式
     *
     * @param phone 手机号
     * @return 是否合法
     */
    public static boolean isValidPhone(String phone)
    {
        if (StringUtils.isEmpty(phone))
        {
            return false;
        }

        // 手机号正则表达式: 1开头,第二位是3-9,后面9位数字
        String regex = "^1[3-9]\\d{9}$";
        return phone.matches(regex);
    }

    /**
     * 隐藏手机号中间4位
     *
     * @param phone 手机号
     * @return 隐藏后的手机号
     */
    public static String hidePhone(String phone)
    {
        if (StringUtils.isEmpty(phone) || phone.length() != 11)
        {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
