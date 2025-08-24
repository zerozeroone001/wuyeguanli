
package com.ruoyi.system.mapper;

/**
 * 消息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface SysMessageMapper 
{
    /**
     * 获取未读消息数
     * 
     * @param userId 用户ID
     * @return 未读消息数
     */
    public int getUnreadCount(Long userId);
}
