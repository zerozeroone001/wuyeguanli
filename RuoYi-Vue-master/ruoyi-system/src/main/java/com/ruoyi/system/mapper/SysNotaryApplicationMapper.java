package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysNotaryApplication;

/**
 * 公证服务申请Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface SysNotaryApplicationMapper 
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
     * 删除公证服务申请
     * 
     * @param applicationId 公证服务申请主键
     * @return 结果
     */
    public int deleteSysNotaryApplicationByApplicationId(Long applicationId);

    /**
     * 批量删除公证服务申请
     * 
     * @param applicationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNotaryApplicationByApplicationIds(Long[] applicationIds);
    
    /**
     * 根据用户ID统计各类申请数量
     *
     * @param userId 用户ID
     * @return 结果
     */
    public List<java.util.Map<String, Object>> countByStatus(Long userId);
    
    /**
     * 统计所有申请按状态分组
     *
     * @return 结果
     */
    public List<java.util.Map<String, Object>> countAllByStatus();
}
