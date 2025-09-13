package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysNotaryApplication;

/**
 * 公证服务申请Service接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface ISysNotaryApplicationService 
{
    /**
     * 查询公证服务申请
     * 
     * @param applicationId 公证服务申请主键
     * @return 公证服务申请
     */
    public SysNotaryApplication selectSysNotaryApplicationByApplicationId(Long applicationId);

    /**
     * 查询公证服务申请列表
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 公证服务申请集合
     */
    public List<SysNotaryApplication> selectSysNotaryApplicationList(SysNotaryApplication sysNotaryApplication);

    /**
     * 新增公证服务申请
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 结果
     */
    public int insertSysNotaryApplication(SysNotaryApplication sysNotaryApplication);

    /**
     * 修改公证服务申请
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 结果
     */
    public int updateSysNotaryApplication(SysNotaryApplication sysNotaryApplication);

    /**
     * 批量删除公证服务申请
     * 
     * @param applicationIds 需要删除的公证服务申请主键集合
     * @return 结果
     */
    public int deleteSysNotaryApplicationByApplicationIds(Long[] applicationIds);

    /**
     * 删除公证服务申请信息
     * 
     * @param applicationId 公证服务申请主键
     * @return 结果
     */
    public int deleteSysNotaryApplicationByApplicationId(Long applicationId);

    /**
     * 根据用户ID获取公证统计信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    public java.util.Map<String, Object> getNotaryStatsByUserId(Long userId);
}
