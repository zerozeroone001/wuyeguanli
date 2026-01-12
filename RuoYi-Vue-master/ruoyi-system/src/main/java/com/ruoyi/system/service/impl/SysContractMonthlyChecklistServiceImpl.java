package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysContractMonthlyChecklistMapper;
import com.ruoyi.system.domain.SysContractMonthlyChecklist;
import com.ruoyi.system.service.ISysContractMonthlyChecklistService;

/**
 * 月履行清单Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@Service
public class SysContractMonthlyChecklistServiceImpl implements ISysContractMonthlyChecklistService
{
    @Autowired
    private SysContractMonthlyChecklistMapper sysContractMonthlyChecklistMapper;

    @Override
    public SysContractMonthlyChecklist selectSysContractMonthlyChecklistByChecklistId(Long checklistId)
    {
        return sysContractMonthlyChecklistMapper.selectSysContractMonthlyChecklistByChecklistId(checklistId);
    }

    @Override
    public List<SysContractMonthlyChecklist> selectSysContractMonthlyChecklistList(SysContractMonthlyChecklist sysContractMonthlyChecklist)
    {
        return sysContractMonthlyChecklistMapper.selectSysContractMonthlyChecklistList(sysContractMonthlyChecklist);
    }

    @Override
    public List<SysContractMonthlyChecklist> selectByContractId(Long contractId)
    {
        return sysContractMonthlyChecklistMapper.selectByContractId(contractId);
    }

    @Override
    public int insertSysContractMonthlyChecklist(SysContractMonthlyChecklist sysContractMonthlyChecklist)
    {
        sysContractMonthlyChecklist.setCreateTime(DateUtils.getNowDate());
        return sysContractMonthlyChecklistMapper.insertSysContractMonthlyChecklist(sysContractMonthlyChecklist);
    }

    @Override
    public int updateSysContractMonthlyChecklist(SysContractMonthlyChecklist sysContractMonthlyChecklist)
    {
        sysContractMonthlyChecklist.setUpdateTime(DateUtils.getNowDate());
        return sysContractMonthlyChecklistMapper.updateSysContractMonthlyChecklist(sysContractMonthlyChecklist);
    }

    @Override
    public int deleteSysContractMonthlyChecklistByChecklistIds(Long[] checklistIds)
    {
        return sysContractMonthlyChecklistMapper.deleteSysContractMonthlyChecklistByChecklistIds(checklistIds);
    }

    @Override
    public int deleteSysContractMonthlyChecklistByChecklistId(Long checklistId)
    {
        return sysContractMonthlyChecklistMapper.deleteSysContractMonthlyChecklistByChecklistId(checklistId);
    }
}
