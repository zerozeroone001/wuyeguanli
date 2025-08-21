package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysOwnerProfile;

/**
 * 业主信息扩展Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface SysOwnerProfileMapper 
{
    /**
     * 查询业主信息扩展
     * 
     * @param userId 业主信息扩展主键
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
     * 删除业主信息扩展
     * 
     * @param userId 业主信息扩展主键
     * @return 结果
     */
    public int deleteSysOwnerProfileByUserId(Long userId);

    /**
     * 批量删除业主信息扩展
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysOwnerProfileByUserIds(Long[] userIds);
}
