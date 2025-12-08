package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysOwnerChange;

/**
 * 业主变更申请Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-03
 */
public interface SysOwnerChangeMapper 
{
    /**
     * 查询业主变更申请
     * 
     * @param changeId 业主变更申请主键
     * @return 业主变更申请
     */
    public SysOwnerChange selectSysOwnerChangeByChangeId(Long changeId);

    /**
     * 查询业主变更申请列表
     * 
     * @param sysOwnerChange 业主变更申请
     * @return 业主变更申请集合
     */
    public List<SysOwnerChange> selectSysOwnerChangeList(SysOwnerChange sysOwnerChange);

    /**
     * 新增业主变更申请
     * 
     * @param sysOwnerChange 业主变更申请
     * @return 结果
     */
    public int insertSysOwnerChange(SysOwnerChange sysOwnerChange);

    /**
     * 修改业主变更申请
     * 
     * @param sysOwnerChange 业主变更申请
     * @return 结果
     */
    public int updateSysOwnerChange(SysOwnerChange sysOwnerChange);

    /**
     * 删除业主变更申请
     * 
     * @param changeId 业主变更申请主键
     * @return 结果
     */
    public int deleteSysOwnerChangeByChangeId(Long changeId);

    /**
     * 批量删除业主变更申请
     * 
     * @param changeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysOwnerChangeByChangeIds(Long[] changeIds);

    /**
     * 根据用户ID删除业主变更申请
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteSysOwnerChangeByUserId(Long userId);
}
