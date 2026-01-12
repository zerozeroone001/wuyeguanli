package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysContractEntryChecklist;

/**
 * 进驻查验清单Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public interface SysContractEntryChecklistMapper
{
    /**
     * 查询进驻查验清单
     *
     * @param checklistId 进驻查验清单主键
     * @return 进驻查验清单
     */
    public SysContractEntryChecklist selectSysContractEntryChecklistByChecklistId(Long checklistId);

    /**
     * 查询进驻查验清单列表
     *
     * @param sysContractEntryChecklist 进驻查验清单
     * @return 进驻查验清单集合
     */
    public List<SysContractEntryChecklist> selectSysContractEntryChecklistList(SysContractEntryChecklist sysContractEntryChecklist);

    /**
     * 根据合同ID查询进驻查验清单
     *
     * @param contractId 合同ID
     * @return 进驻查验清单
     */
    public SysContractEntryChecklist selectByContractId(Long contractId);

    /**
     * 新增进驻查验清单
     *
     * @param sysContractEntryChecklist 进驻查验清单
     * @return 结果
     */
    public int insertSysContractEntryChecklist(SysContractEntryChecklist sysContractEntryChecklist);

    /**
     * 修改进驻查验清单
     *
     * @param sysContractEntryChecklist 进驻查验清单
     * @return 结果
     */
    public int updateSysContractEntryChecklist(SysContractEntryChecklist sysContractEntryChecklist);

    /**
     * 删除进驻查验清单
     *
     * @param checklistId 进驻查验清单主键
     * @return 结果
     */
    public int deleteSysContractEntryChecklistByChecklistId(Long checklistId);

    /**
     * 批量删除进驻查验清单
     *
     * @param checklistIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysContractEntryChecklistByChecklistIds(Long[] checklistIds);
}
