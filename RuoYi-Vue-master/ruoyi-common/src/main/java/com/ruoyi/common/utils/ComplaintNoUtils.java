package com.ruoyi.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 投诉编号生成工具类
 * 
 * @author ruoyi
 */
public class ComplaintNoUtils
{
    private static final String PREFIX = "TS";
    private static final Random RANDOM = new Random();
    
    /**
     * 生成投诉编号
     * 格式：TS + 年月日 + 4位随机数
     * 例如：TS20250929001
     * 
     * @return 投诉编号
     */
    public static String generateComplaintNo()
    {
        // 获取当前日期
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // 生成4位随机数
        int randomNum = RANDOM.nextInt(9000) + 1000; // 1000-9999
        
        return PREFIX + dateStr + randomNum;
    }
    
    /**
     * 生成投诉编号（带时间戳）
     * 格式：TS + 时间戳后8位
     * 例如：TS12345678
     * 
     * @return 投诉编号
     */
    public static String generateComplaintNoWithTimestamp()
    {
        long timestamp = System.currentTimeMillis();
        String timestampStr = String.valueOf(timestamp);
        String suffix = timestampStr.substring(timestampStr.length() - 8);
        
        return PREFIX + suffix.toUpperCase();
    }
}