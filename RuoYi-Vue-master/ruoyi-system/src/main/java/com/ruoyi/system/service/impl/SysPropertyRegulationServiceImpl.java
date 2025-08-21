package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysPropertyRegulationMapper;
import com.ruoyi.system.domain.SysPropertyRegulation;
import com.ruoyi.system.service.ISysPropertyRegulationService;

/**
 * 物业制度管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysPropertyRegulationServiceImpl implements ISysPropertyRegulationService 
{
    @Autowired
    private SysPropertyRegulationMapper sysPropertyRegulationMapper;

    /**
     * 查询物业制度管理
     * 
     * @param regulationId 物业制度管理主键
     * @return 物业制度管理
     */
    @Override
    public SysPropertyRegulation selectSysPropertyRegulationByRegulationId(Long regulationId)
    {
        return sysPropertyRegulationMapper.selectSysPropertyRegulationByRegulationId(regulationId);
    }

    /**
     * 查询物业制度管理列表
     * 
     * @param sysPropertyRegulation 物业制度管理
     * @return 物业制度管理
     */
    @Override
    public List<SysPropertyRegulation> selectSysPropertyRegulationList(SysPropertyRegulation sysPropertyRegulation)
    {
        return sysPropertyRegulationMapper.selectSysPropertyRegulationList(sysPropertyRegulation);
    }

    /**
     * 新增物业制度管理
     * 
     * @param sysPropertyRegulation 物业制度管理
     * @return 结果
     */
    @Override
    public int insertSysPropertyRegulation(SysPropertyRegulation sysPropertyRegulation)
    {
        sysPropertyRegulation.setCreateTime(DateUtils.getNowDate());
        return sysPropertyRegulationMapper.insertSysPropertyRegulation(sysPropertyRegulation);
    }

    /**
     * 修改物业制度管理
     * 
     * @param sysPropertyRegulation 物业制度管理
     * @return 结果
     */
    @Override
    public int updateSysPropertyRegulation(SysPropertyRegulation sysPropertyRegulation)
    {
        sysPropertyRegulation.setUpdateTime(DateUtils.getNowDate());
        return sysPropertyRegulationMapper.updateSysPropertyRegulation(sysPropertyRegulation);
    }

    /**
     * 批量删除物业制度管理
     * 
     * @param regulationIds 需要删除的物业制度管理主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyRegulationByRegulationIds(Long[] regulationIds)
    {
        return sysPropertyRegulationMapper.deleteSysPropertyRegulationByRegulationIds(regulationIds);
    }

    /**
     * 删除物业制度管理信息
     * 
     * @param regulationId 物业制度管理主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyRegulationByRegulationId(Long regulationId)
    {
        return sysPropertyRegulationMapper.deleteSysPropertyRegulationByRegulationId(regulationId);
    }
}
