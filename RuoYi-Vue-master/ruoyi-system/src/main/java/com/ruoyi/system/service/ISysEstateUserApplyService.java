package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysEstateUserApply;

/**
 * 房产认证申请Service接口
 * 
 * @author ruoyi
 * @date 2025-12-07
 */
public interface ISysEstateUserApplyService 
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
     * 批量删除房产认证申请
     * 
     * @param applyIds 需要删除的房产认证申请主键集合
     * @return 结果
     */
    public int deleteSysEstateUserApplyByApplyIds(Long[] applyIds);

    /**
     * 删除房产认证申请信息
     * 
     * @param applyId 房产认证申请主键
     * @return 结果
     */
    public int deleteSysEstateUserApplyByApplyId(Long applyId);

    /**
     * 审核申请
     * @param applyId 申请ID
     * @param status 状态(1通过 2拒绝)
     * @param remark 审核备注
     * @return 结果
     */
    public int auditApply(Long applyId, String status, String remark);

    /**
     * 检查是否存在待审核或已通过的房产申请记录
     * @param userId 用户ID
     * @param propertyId 房产ID
     * @return 存在则返回true，否则false
     */
    public boolean checkPendingOrApprovedApply(Long userId, Long propertyId);
}
