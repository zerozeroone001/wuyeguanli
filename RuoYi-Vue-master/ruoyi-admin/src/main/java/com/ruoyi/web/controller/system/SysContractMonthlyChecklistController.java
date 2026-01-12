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
import com.ruoyi.system.domain.SysContractMonthlyChecklist;
import com.ruoyi.system.service.ISysContractMonthlyChecklistService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 月履行清单Controller
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@RestController
@RequestMapping("/system/contract/monthlyChecklist")
public class SysContractMonthlyChecklistController extends BaseController
{
    @Autowired
    private ISysContractMonthlyChecklistService sysContractMonthlyChecklistService;

    @PreAuthorize("@ss.hasPermi('system:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysContractMonthlyChecklist sysContractMonthlyChecklist)
    {
        sysContractMonthlyChecklist.setCommunityId(resolveCommunityId(sysContractMonthlyChecklist.getCommunityId()));
        startPage();
        List<SysContractMonthlyChecklist> list = sysContractMonthlyChecklistService.selectSysContractMonthlyChecklistList(sysContractMonthlyChecklist);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:contract:export')")
    @Log(title = "月履行清单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysContractMonthlyChecklist sysContractMonthlyChecklist)
    {
        sysContractMonthlyChecklist.setCommunityId(resolveCommunityId(sysContractMonthlyChecklist.getCommunityId()));
        List<SysContractMonthlyChecklist> list = sysContractMonthlyChecklistService.selectSysContractMonthlyChecklistList(sysContractMonthlyChecklist);
        ExcelUtil<SysContractMonthlyChecklist> util = new ExcelUtil<SysContractMonthlyChecklist>(SysContractMonthlyChecklist.class);
        util.exportExcel(response, list, "月履行清单数据");
    }

    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/{checklistId}")
    public AjaxResult getInfo(@PathVariable("checklistId") Long checklistId)
    {
        return success(sysContractMonthlyChecklistService.selectSysContractMonthlyChecklistByChecklistId(checklistId));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/contract/{contractId}")
    public AjaxResult getByContractId(@PathVariable("contractId") Long contractId)
    {
        return success(sysContractMonthlyChecklistService.selectByContractId(contractId));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:add')")
    @Log(title = "月履行清单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysContractMonthlyChecklist sysContractMonthlyChecklist)
    {
        sysContractMonthlyChecklist.setCommunityId(requireResolvedCommunityId(sysContractMonthlyChecklist.getCommunityId(), "请选择小区后再操作"));
        return toAjax(sysContractMonthlyChecklistService.insertSysContractMonthlyChecklist(sysContractMonthlyChecklist));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:edit')")
    @Log(title = "月履行清单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysContractMonthlyChecklist sysContractMonthlyChecklist)
    {
        return toAjax(sysContractMonthlyChecklistService.updateSysContractMonthlyChecklist(sysContractMonthlyChecklist));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:remove')")
    @Log(title = "月履行清单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{checklistIds}")
    public AjaxResult remove(@PathVariable Long[] checklistIds)
    {
        return toAjax(sysContractMonthlyChecklistService.deleteSysContractMonthlyChecklistByChecklistIds(checklistIds));
    }
}
