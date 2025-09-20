package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysInspectionPlan;
import com.ruoyi.system.service.ISysInspectionPlanService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/system/inspectionPlan")
public class SysInspectionPlanController extends BaseController {
    @Autowired
    private ISysInspectionPlanService inspectionPlanService;

    @PreAuthorize("@ss.hasPermi('system:inspectionPlan:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysInspectionPlan sysInspectionPlan) {
        startPage();
        List<SysInspectionPlan> list = inspectionPlanService.selectSysInspectionPlanList(sysInspectionPlan);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:inspectionPlan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") Long planId) {
        return success(inspectionPlanService.selectSysInspectionPlanByPlanId(planId));
    }

    @PreAuthorize("@ss.hasPermi('system:inspectionPlan:add')")
    @Log(title = "检测计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysInspectionPlan sysInspectionPlan) {
        return toAjax(inspectionPlanService.insertSysInspectionPlan(sysInspectionPlan));
    }

    @PreAuthorize("@ss.hasPermi('system:inspectionPlan:edit')")
    @Log(title = "检测计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysInspectionPlan sysInspectionPlan) {
        return toAjax(inspectionPlanService.updateSysInspectionPlan(sysInspectionPlan));
    }

    @PreAuthorize("@ss.hasPermi('system:inspectionPlan:remove')")
    @Log(title = "检测计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{planIds}")
    public AjaxResult remove(@PathVariable Long[] planIds) {
        return toAjax(inspectionPlanService.deleteSysInspectionPlanByPlanIds(planIds));
    }
}
