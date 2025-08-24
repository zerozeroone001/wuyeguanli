
package com.ruoyi.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMessageMapper;
import com.ruoyi.system.service.ISysMessageService;

/**
 * 消息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
@Service
public class SysMessageServiceImpl implements ISysMessageService 
{
    @Autowired
    private SysMessageMapper messageMapper;

    /**
     * 获取未读消息数
     * 
     * @param userId 用户ID
     * @return 未读消息数
     */
    @Override
    public int getUnreadCount(Long userId)
    {
        return messageMapper.getUnreadCount(userId);
    }
}
