package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysPropertyRegulation;

/**
 * 物业制度管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface SysPropertyRegulationMapper 
{
    /**
     * 查询物业制度管理
     * 
     * @param regulationId 物业制度管理主键
     * @return 物业制度管理
     */
    public SysPropertyRegulation selectSysPropertyRegulationByRegulationId(Long regulationId);

    /**
     * 查询物业制度管理列表
     * 
     * @param sysPropertyRegulation 物业制度管理
     * @return 物业制度管理集合
     */
    public List<SysPropertyRegulation> selectSysPropertyRegulationList(SysPropertyRegulation sysPropertyRegulation);

    /**
     * 新增物业制度管理
     * 
     * @param sysPropertyRegulation 物业制度管理
     * @return 结果
     */
    public int insertSysPropertyRegulation(SysPropertyRegulation sysPropertyRegulation);

    /**
     * 修改物业制度管理
     * 
     * @param sysPropertyRegulation 物业制度管理
     * @return 结果
     */
    public int updateSysPropertyRegulation(SysPropertyRegulation sysPropertyRegulation);

    /**
     * 删除物业制度管理
     * 
     * @param regulationId 物业制度管理主键
     * @return 结果
     */
    public int deleteSysPropertyRegulationByRegulationId(Long regulationId);

    /**
     * 批量删除物业制度管理
     * 
     * @param regulationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysPropertyRegulationByRegulationIds(Long[] regulationIds);
}
