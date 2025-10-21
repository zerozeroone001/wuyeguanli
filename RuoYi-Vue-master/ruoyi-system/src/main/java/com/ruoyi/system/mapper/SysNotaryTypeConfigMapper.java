package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysNotaryTypeConfig;

/**
 * 公证类型配置Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-27
 */
public interface SysNotaryTypeConfigMapper 
{
    /**
     * 查询公证类型配置
     * 
     * @param configId 公证类型配置主键
     * @return 公证类型配置
     */
    public SysNotaryTypeConfig selectSysNotaryTypeConfigByConfigId(Long configId);

    /**
     * 查询公证类型配置列表
     * 
     * @param sysNotaryTypeConfig 公证类型配置
     * @return 公证类型配置集合
     */
    public List<SysNotaryTypeConfig> selectSysNotaryTypeConfigList(SysNotaryTypeConfig sysNotaryTypeConfig);

    /**
     * 新增公证类型配置
     * 
     * @param sysNotaryTypeConfig 公证类型配置
     * @return 结果
     */
    public int insertSysNotaryTypeConfig(SysNotaryTypeConfig sysNotaryTypeConfig);

    /**
     * 修改公证类型配置
     * 
     * @param sysNotaryTypeConfig 公证类型配置
     * @return 结果
     */
    public int updateSysNotaryTypeConfig(SysNotaryTypeConfig sysNotaryTypeConfig);

    /**
     * 删除公证类型配置
     * 
     * @param configId 公证类型配置主键
     * @return 结果
     */
    public int deleteSysNotaryTypeConfigByConfigId(Long configId);

    /**
     * 批量删除公证类型配置
     * 
     * @param configIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNotaryTypeConfigByConfigIds(Long[] configIds);
}
