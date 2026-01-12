package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysContractEntryChecklistMapper;
import com.ruoyi.system.domain.SysContractEntryChecklist;
import com.ruoyi.system.service.ISysContractEntryChecklistService;

/**
 * 进驻查验清单Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@Service
public class SysContractEntryChecklistServiceImpl implements ISysContractEntryChecklistService
{
    @Autowired
    private SysContractEntryChecklistMapper sysContractEntryChecklistMapper;

    @Override
    public SysContractEntryChecklist selectSysContractEntryChecklistByChecklistId(Long checklistId)
    {
        return sysContractEntryChecklistMapper.selectSysContractEntryChecklistByChecklistId(checklistId);
    }

    @Override
    public List<SysContractEntryChecklist> selectSysContractEntryChecklistList(SysContractEntryChecklist sysContractEntryChecklist)
    {
        return sysContractEntryChecklistMapper.selectSysContractEntryChecklistList(sysContractEntryChecklist);
    }

    @Override
    public SysContractEntryChecklist selectByContractId(Long contractId)
    {
        return sysContractEntryChecklistMapper.selectByContractId(contractId);
    }

    @Override
    public int insertSysContractEntryChecklist(SysContractEntryChecklist sysContractEntryChecklist)
    {
        sysContractEntryChecklist.setCreateTime(DateUtils.getNowDate());
        return sysContractEntryChecklistMapper.insertSysContractEntryChecklist(sysContractEntryChecklist);
    }

    @Override
    public int updateSysContractEntryChecklist(SysContractEntryChecklist sysContractEntryChecklist)
    {
        sysContractEntryChecklist.setUpdateTime(DateUtils.getNowDate());
        return sysContractEntryChecklistMapper.updateSysContractEntryChecklist(sysContractEntryChecklist);
    }

    @Override
    public int deleteSysContractEntryChecklistByChecklistIds(Long[] checklistIds)
    {
        return sysContractEntryChecklistMapper.deleteSysContractEntryChecklistByChecklistIds(checklistIds);
    }

    @Override
    public int deleteSysContractEntryChecklistByChecklistId(Long checklistId)
    {
        return sysContractEntryChecklistMapper.deleteSysContractEntryChecklistByChecklistId(checklistId);
    }
}
