
package com.ruoyi.system.service;

/**
 * 消息Service接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface ISysMessageService 
{
    /**
     * 获取未读消息数
     * 
     * @param userId 用户ID
     * @return 未读消息数
     */
    public int getUnreadCount(Long userId);
}
