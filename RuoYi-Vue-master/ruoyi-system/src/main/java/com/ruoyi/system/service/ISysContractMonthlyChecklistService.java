package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysContractMonthlyChecklist;

/**
 * 月履行清单Service接口
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public interface ISysContractMonthlyChecklistService
{
    public SysContractMonthlyChecklist selectSysContractMonthlyChecklistByChecklistId(Long checklistId);

    public List<SysContractMonthlyChecklist> selectSysContractMonthlyChecklistList(SysContractMonthlyChecklist sysContractMonthlyChecklist);

    public List<SysContractMonthlyChecklist> selectByContractId(Long contractId);

    public int insertSysContractMonthlyChecklist(SysContractMonthlyChecklist sysContractMonthlyChecklist);

    public int updateSysContractMonthlyChecklist(SysContractMonthlyChecklist sysContractMonthlyChecklist);

    public int deleteSysContractMonthlyChecklistByChecklistIds(Long[] checklistIds);

    public int deleteSysContractMonthlyChecklistByChecklistId(Long checklistId);
}
