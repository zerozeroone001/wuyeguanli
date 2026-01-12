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
import com.ruoyi.system.domain.SysContractRectification;
import com.ruoyi.system.service.ISysContractRectificationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 整改记录Controller
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@RestController
@RequestMapping("/system/contract/rectification")
public class SysContractRectificationController extends BaseController
{
    @Autowired
    private ISysContractRectificationService sysContractRectificationService;

    @PreAuthorize("@ss.hasPermi('system:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysContractRectification sysContractRectification)
    {
        sysContractRectification.setCommunityId(resolveCommunityId(sysContractRectification.getCommunityId()));
        startPage();
        List<SysContractRectification> list = sysContractRectificationService.selectSysContractRectificationList(sysContractRectification);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:contract:export')")
    @Log(title = "整改记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysContractRectification sysContractRectification)
    {
        sysContractRectification.setCommunityId(resolveCommunityId(sysContractRectification.getCommunityId()));
        List<SysContractRectification> list = sysContractRectificationService.selectSysContractRectificationList(sysContractRectification);
        ExcelUtil<SysContractRectification> util = new ExcelUtil<SysContractRectification>(SysContractRectification.class);
        util.exportExcel(response, list, "整改记录数据");
    }

    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/{rectificationId}")
    public AjaxResult getInfo(@PathVariable("rectificationId") Long rectificationId)
    {
        return success(sysContractRectificationService.selectSysContractRectificationByRectificationId(rectificationId));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/contract/{contractId}")
    public AjaxResult getByContractId(@PathVariable("contractId") Long contractId)
    {
        return success(sysContractRectificationService.selectByContractId(contractId));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:add')")
    @Log(title = "整改记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysContractRectification sysContractRectification)
    {
        sysContractRectification.setCommunityId(requireResolvedCommunityId(sysContractRectification.getCommunityId(), "请选择小区后再操作"));
        return toAjax(sysContractRectificationService.insertSysContractRectification(sysContractRectification));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:edit')")
    @Log(title = "整改记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysContractRectification sysContractRectification)
    {
        return toAjax(sysContractRectificationService.updateSysContractRectification(sysContractRectification));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:remove')")
    @Log(title = "整改记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{rectificationIds}")
    public AjaxResult remove(@PathVariable Long[] rectificationIds)
    {
        return toAjax(sysContractRectificationService.deleteSysContractRectificationByRectificationIds(rectificationIds));
    }
}
