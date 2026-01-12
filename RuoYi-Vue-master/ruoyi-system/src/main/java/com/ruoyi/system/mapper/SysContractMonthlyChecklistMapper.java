package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysContractMonthlyChecklist;

/**
 * 月履行清单Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public interface SysContractMonthlyChecklistMapper
{
    public SysContractMonthlyChecklist selectSysContractMonthlyChecklistByChecklistId(Long checklistId);

    public List<SysContractMonthlyChecklist> selectSysContractMonthlyChecklistList(SysContractMonthlyChecklist sysContractMonthlyChecklist);

    public List<SysContractMonthlyChecklist> selectByContractId(Long contractId);

    public int insertSysContractMonthlyChecklist(SysContractMonthlyChecklist sysContractMonthlyChecklist);

    public int updateSysContractMonthlyChecklist(SysContractMonthlyChecklist sysContractMonthlyChecklist);

    public int deleteSysContractMonthlyChecklistByChecklistId(Long checklistId);

    public int deleteSysContractMonthlyChecklistByChecklistIds(Long[] checklistIds);
}
