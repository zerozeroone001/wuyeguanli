package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysContractEntryChecklist;

/**
 * 进驻查验清单Service接口
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public interface ISysContractEntryChecklistService
{
    public SysContractEntryChecklist selectSysContractEntryChecklistByChecklistId(Long checklistId);

    public List<SysContractEntryChecklist> selectSysContractEntryChecklistList(SysContractEntryChecklist sysContractEntryChecklist);

    public SysContractEntryChecklist selectByContractId(Long contractId);

    public int insertSysContractEntryChecklist(SysContractEntryChecklist sysContractEntryChecklist);

    public int updateSysContractEntryChecklist(SysContractEntryChecklist sysContractEntryChecklist);

    public int deleteSysContractEntryChecklistByChecklistIds(Long[] checklistIds);

    public int deleteSysContractEntryChecklistByChecklistId(Long checklistId);
}
