package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 投票用户查询服务接口
 * 
 * @author ruoyi
 */
public interface IVoteUserQueryService {
    
    /**
     * 通过电话号码查找用户ID
     * 
     * @param phoneNumber 电话号码
     * @return 用户ID，未找到返回null
     */
    Long findUserIdByPhone(String phoneNumber);
    
    /**
     * 通过电话号码查找用户对象
     * 
     * @param phoneNumber 电话号码
     * @return 用户对象，未找到返回null
     */
    SysUser findUserByPhone(String phoneNumber);
    
    /**
     * 通过业主姓名和房号查找用户ID
     * 
     * @param ownerName 业主姓名
     * @param roomNumber 房号
     * @return 用户ID，未找到返回null
     */
    Long findUserIdByNameAndRoom(String ownerName, String roomNumber);
}