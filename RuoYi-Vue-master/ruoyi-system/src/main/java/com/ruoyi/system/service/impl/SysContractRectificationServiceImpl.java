package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysContractRectificationMapper;
import com.ruoyi.system.domain.SysContractRectification;
import com.ruoyi.system.service.ISysContractRectificationService;

/**
 * 整改记录Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@Service
public class SysContractRectificationServiceImpl implements ISysContractRectificationService
{
    @Autowired
    private SysContractRectificationMapper sysContractRectificationMapper;

    @Override
    public SysContractRectification selectSysContractRectificationByRectificationId(Long rectificationId)
    {
        return sysContractRectificationMapper.selectSysContractRectificationByRectificationId(rectificationId);
    }

    @Override
    public List<SysContractRectification> selectSysContractRectificationList(SysContractRectification sysContractRectification)
    {
        return sysContractRectificationMapper.selectSysContractRectificationList(sysContractRectification);
    }

    @Override
    public List<SysContractRectification> selectByContractId(Long contractId)
    {
        return sysContractRectificationMapper.selectByContractId(contractId);
    }

    @Override
    public int insertSysContractRectification(SysContractRectification sysContractRectification)
    {
        sysContractRectification.setCreateTime(DateUtils.getNowDate());
        return sysContractRectificationMapper.insertSysContractRectification(sysContractRectification);
    }

    @Override
    public int updateSysContractRectification(SysContractRectification sysContractRectification)
    {
        sysContractRectification.setUpdateTime(DateUtils.getNowDate());
        return sysContractRectificationMapper.updateSysContractRectification(sysContractRectification);
    }

    @Override
    public int deleteSysContractRectificationByRectificationIds(Long[] rectificationIds)
    {
        return sysContractRectificationMapper.deleteSysContractRectificationByRectificationIds(rectificationIds);
    }

    @Override
    public int deleteSysContractRectificationByRectificationId(Long rectificationId)
    {
        return sysContractRectificationMapper.deleteSysContractRectificationByRectificationId(rectificationId);
    }
}
