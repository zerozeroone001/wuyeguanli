package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysContractEntryChecklist;
import com.ruoyi.system.service.ISysContractEntryChecklistService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 进驻查验清单Controller
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@RestController
@RequestMapping("/system/contract/entryChecklist")
public class SysContractEntryChecklistController extends BaseController
{
    @Autowired
    private ISysContractEntryChecklistService sysContractEntryChecklistService;

    /**
     * 查询进驻查验清单列表
     */
    @PreAuthorize("@ss.hasPermi('system:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysContractEntryChecklist sysContractEntryChecklist)
    {
        sysContractEntryChecklist.setCommunityId(resolveCommunityId(sysContractEntryChecklist.getCommunityId()));
        startPage();
        List<SysContractEntryChecklist> list = sysContractEntryChecklistService.selectSysContractEntryChecklistList(sysContractEntryChecklist);
        return getDataTable(list);
    }

    /**
     * 导出进驻查验清单列表
     */
    @PreAuthorize("@ss.hasPermi('system:contract:export')")
    @Log(title = "进驻查验清单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysContractEntryChecklist sysContractEntryChecklist)
    {
        sysContractEntryChecklist.setCommunityId(resolveCommunityId(sysContractEntryChecklist.getCommunityId()));
        List<SysContractEntryChecklist> list = sysContractEntryChecklistService.selectSysContractEntryChecklistList(sysContractEntryChecklist);
        ExcelUtil<SysContractEntryChecklist> util = new ExcelUtil<SysContractEntryChecklist>(SysContractEntryChecklist.class);
        util.exportExcel(response, list, "进驻查验清单数据");
    }

    /**
     * 获取进驻查验清单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/{checklistId}")
    public AjaxResult getInfo(@PathVariable("checklistId") Long checklistId)
    {
        return success(sysContractEntryChecklistService.selectSysContractEntryChecklistByChecklistId(checklistId));
    }

    /**
     * 根据合同ID获取进驻查验清单
     */
    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/contract/{contractId}")
    public AjaxResult getByContractId(@PathVariable("contractId") Long contractId)
    {
        return success(sysContractEntryChecklistService.selectByContractId(contractId));
    }

    /**
     * 新增进驻查验清单
     */
    @PreAuthorize("@ss.hasPermi('system:contract:add')")
    @Log(title = "进驻查验清单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysContractEntryChecklist sysContractEntryChecklist)
    {
        sysContractEntryChecklist.setCommunityId(requireResolvedCommunityId(sysContractEntryChecklist.getCommunityId(), "请选择小区后再操作"));
        return toAjax(sysContractEntryChecklistService.insertSysContractEntryChecklist(sysContractEntryChecklist));
    }

    /**
     * 修改进驻查验清单
     */
    @PreAuthorize("@ss.hasPermi('system:contract:edit')")
    @Log(title = "进驻查验清单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysContractEntryChecklist sysContractEntryChecklist)
    {
        return toAjax(sysContractEntryChecklistService.updateSysContractEntryChecklist(sysContractEntryChecklist));
    }

    /**
     * 删除进驻查验清单
     */
    @PreAuthorize("@ss.hasPermi('system:contract:remove')")
    @Log(title = "进驻查验清单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{checklistIds}")
    public AjaxResult remove(@PathVariable Long[] checklistIds)
    {
        return toAjax(sysContractEntryChecklistService.deleteSysContractEntryChecklistByChecklistIds(checklistIds));
    }
}
