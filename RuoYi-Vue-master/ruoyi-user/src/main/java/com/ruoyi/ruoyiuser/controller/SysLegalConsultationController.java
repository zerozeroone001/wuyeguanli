package com.ruoyi.ruoyiuser.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysLegalConsultation;
import com.ruoyi.system.service.ISysLegalConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 法律咨询Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/consultation")
public class SysLegalConsultationController extends BaseController
{
    @Autowired
    private ISysLegalConsultationService sysLegalConsultationService;

    /**
     * 查询法律咨询列表
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLegalConsultation sysLegalConsultation)
    {
        startPage();
        List<SysLegalConsultation> list = sysLegalConsultationService.selectSysLegalConsultationList(sysLegalConsultation);
        return getDataTable(list);
    }

    /**
     * 导出法律咨询列表
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:export')")
    @Log(title = "法律咨询", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLegalConsultation sysLegalConsultation)
    {
        List<SysLegalConsultation> list = sysLegalConsultationService.selectSysLegalConsultationList(sysLegalConsultation);
        ExcelUtil<SysLegalConsultation> util = new ExcelUtil<SysLegalConsultation>(SysLegalConsultation.class);
        util.exportExcel(response, list, "法律咨询数据");
    }

    /**
     * 获取法律咨询详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:query')")
    @GetMapping(value = "/{consultationId}")
    public AjaxResult getInfo(@PathVariable("consultationId") Long consultationId)
    {
        return success(sysLegalConsultationService.selectSysLegalConsultationByConsultationId(consultationId));
    }

    /**
     * 新增法律咨询
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:add')")
    @Log(title = "法律咨询", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLegalConsultation sysLegalConsultation)
    {
        return toAjax(sysLegalConsultationService.insertSysLegalConsultation(sysLegalConsultation));
    }

    /**
     * 修改法律咨询
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:edit')")
    @Log(title = "法律咨询", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLegalConsultation sysLegalConsultation)
    {
        return toAjax(sysLegalConsultationService.updateSysLegalConsultation(sysLegalConsultation));
    }

    /**
     * 删除法律咨询
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:remove')")
    @Log(title = "法律咨询", businessType = BusinessType.DELETE)
	@DeleteMapping("/{consultationIds}")
    public AjaxResult remove(@PathVariable Long[] consultationIds)
    {
        return toAjax(sysLegalConsultationService.deleteSysLegalConsultationByConsultationIds(consultationIds));
    }

    @GetMapping("/experts")
    public AjaxResult getExperts() {
        // TODO: Implement this method to fetch real data
        return AjaxResult.success();
    }

    @GetMapping("/faq")
    public AjaxResult getFaq() {
        // TODO: Implement this method to fetch real data
        return AjaxResult.success();
    }

    @GetMapping("/lawyers")
    public AjaxResult getLawyers() {
        // TODO: Implement this method to fetch real data
        return AjaxResult.success();
    }
}
