package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysEstateUserApply;

/**
 * 房产认证申请Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-07
 */
public interface SysEstateUserApplyMapper 
{
    /**
     * 查询房产认证申请
     * 
     * @param applyId 房产认证申请主键
     * @return 房产认证申请
     */
    public SysEstateUserApply selectSysEstateUserApplyByApplyId(Long applyId);

    /**
     * 查询房产认证申请列表
     * 
     * @param sysEstateUserApply 房产认证申请
     * @return 房产认证申请集合
     */
    public List<SysEstateUserApply> selectSysEstateUserApplyList(SysEstateUserApply sysEstateUserApply);

    /**
     * 新增房产认证申请
     * 
     * @param sysEstateUserApply 房产认证申请
     * @return 结果
     */
    public int insertSysEstateUserApply(SysEstateUserApply sysEstateUserApply);

    /**
     * 修改房产认证申请
     * 
     * @param sysEstateUserApply 房产认证申请
     * @return 结果
     */
    public int updateSysEstateUserApply(SysEstateUserApply sysEstateUserApply);

    /**
     * 删除房产认证申请
     * 
     * @param applyId 房产认证申请主键
     * @return 结果
     */
    public int deleteSysEstateUserApplyByApplyId(Long applyId);

    /**
     * 批量删除房产认证申请
     * 
     * @param applyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysEstateUserApplyByApplyIds(Long[] applyIds);

    /**
     * 查询是否存在待审核或已通过的房产申请记录
     * @param sysEstateUserApply 包含用户ID和房产ID
     * @return 记录数
     */
    public int countPendingOrApprovedApply(SysEstateUserApply sysEstateUserApply);
}
