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
import com.ruoyi.system.domain.SysContractAssessment;
import com.ruoyi.system.service.ISysContractAssessmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同考核活动Controller
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@RestController
@RequestMapping("/system/contract/assessment")
public class SysContractAssessmentController extends BaseController
{
    @Autowired
    private ISysContractAssessmentService sysContractAssessmentService;

    @PreAuthorize("@ss.hasPermi('system:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysContractAssessment sysContractAssessment)
    {
        sysContractAssessment.setCommunityId(resolveCommunityId(sysContractAssessment.getCommunityId()));
        startPage();
        List<SysContractAssessment> list = sysContractAssessmentService.selectSysContractAssessmentList(sysContractAssessment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:contract:export')")
    @Log(title = "合同考核活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysContractAssessment sysContractAssessment)
    {
        sysContractAssessment.setCommunityId(resolveCommunityId(sysContractAssessment.getCommunityId()));
        List<SysContractAssessment> list = sysContractAssessmentService.selectSysContractAssessmentList(sysContractAssessment);
        ExcelUtil<SysContractAssessment> util = new ExcelUtil<SysContractAssessment>(SysContractAssessment.class);
        util.exportExcel(response, list, "合同考核活动数据");
    }

    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/{assessmentId}")
    public AjaxResult getInfo(@PathVariable("assessmentId") Long assessmentId)
    {
        return success(sysContractAssessmentService.selectSysContractAssessmentByAssessmentId(assessmentId));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/contract/{contractId}")
    public AjaxResult getByContractId(@PathVariable("contractId") Long contractId)
    {
        return success(sysContractAssessmentService.selectByContractId(contractId));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:add')")
    @Log(title = "合同考核活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysContractAssessment sysContractAssessment)
    {
        sysContractAssessment.setCommunityId(requireResolvedCommunityId(sysContractAssessment.getCommunityId(), "请选择小区后再操作"));
        return toAjax(sysContractAssessmentService.insertSysContractAssessment(sysContractAssessment));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:edit')")
    @Log(title = "合同考核活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysContractAssessment sysContractAssessment)
    {
        return toAjax(sysContractAssessmentService.updateSysContractAssessment(sysContractAssessment));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:remove')")
    @Log(title = "合同考核活动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{assessmentIds}")
    public AjaxResult remove(@PathVariable Long[] assessmentIds)
    {
        return toAjax(sysContractAssessmentService.deleteSysContractAssessmentByAssessmentIds(assessmentIds));
    }
}
