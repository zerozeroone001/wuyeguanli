package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.domain.dto.OwnerProfileImportDto;

/**
 * 业主信息扩展Service接口
 * 
 * @author ruoyi
 * @date 2025-09-15
 */
public interface ISysOwnerProfileService 
{
    /**
     * 查询业主信息扩展
     * 
     * @param ownerId 业主信息扩展主键
     * @return 业主信息扩展
     */
    public SysOwnerProfile selectSysOwnerProfileByOwnerId(Long ownerId);

    /**
     * 根据用户ID查询业主信息
     *
     * @param userId 用户ID
     * @return 业主信息扩展
     */
    public SysOwnerProfile selectSysOwnerProfileByUserId(Long userId);

    /**
     * 查询业主信息扩展列表
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 业主信息扩展集合
     */
    public List<SysOwnerProfile> selectSysOwnerProfileList(SysOwnerProfile sysOwnerProfile);

    /**
     * 新增业主信息扩展
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 结果
     */
    public int insertSysOwnerProfile(SysOwnerProfile sysOwnerProfile);

    /**
     * 修改业主信息扩展
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 结果
     */
    public int updateSysOwnerProfile(SysOwnerProfile sysOwnerProfile);

    /**
     * 批量删除业主信息扩展
     * 
     * @param ownerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysOwnerProfileByOwnerIds(Long[] ownerIds);

    /**
     * 批量删除业主信息扩展
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysOwnerProfileByUserIds(Long[] userIds);

    /**
     * 删除业主信息扩展信息
     * 
     * @param ownerId 业主信息扩展主键
     * @return 结果
     */
    public int deleteSysOwnerProfileByOwnerId(Long ownerId);

    /**
     * 导入业主数据
     * 
     * @param ownerList 业主数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importOwner(List<OwnerProfileImportDto> ownerList, boolean isUpdateSupport, String operName);

    /**
     * 审核业主认证
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 结果
     */
    public int auditOwnerProfile(SysOwnerProfile sysOwnerProfile);

    /**
     * 清除用户缓存
     * 
     * @param userId 用户ID
     */
    public void clearUserCache(Long userId);
}