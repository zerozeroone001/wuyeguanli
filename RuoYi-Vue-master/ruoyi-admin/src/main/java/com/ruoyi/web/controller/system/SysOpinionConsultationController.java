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
import com.ruoyi.system.domain.SysOpinionConsultation;
import com.ruoyi.system.service.ISysOpinionConsultationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 意见征询Controller
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
@RestController
@RequestMapping("/system/opinionConsultation")
public class SysOpinionConsultationController extends BaseController
{
    @Autowired
    private ISysOpinionConsultationService sysOpinionConsultationService;

    /**
     * 查询意见征询列表
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOpinionConsultation sysOpinionConsultation)
    {
        startPage();
        List<SysOpinionConsultation> list = sysOpinionConsultationService.selectSysOpinionConsultationList(sysOpinionConsultation);
        return getDataTable(list);
    }

    /**
     * 导出意见征询列表
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:export')")
    @Log(title = "意见征询", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOpinionConsultation sysOpinionConsultation)
    {
        List<SysOpinionConsultation> list = sysOpinionConsultationService.selectSysOpinionConsultationList(sysOpinionConsultation);
        ExcelUtil<SysOpinionConsultation> util = new ExcelUtil<SysOpinionConsultation>(SysOpinionConsultation.class);
        util.exportExcel(response, list, "意见征询数据");
    }

    /**
     * 获取意见征询详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:query')")
    @GetMapping(value = "/{consultationId}")
    public AjaxResult getInfo(@PathVariable("consultationId") Long consultationId)
    {
        return success(sysOpinionConsultationService.selectSysOpinionConsultationByConsultationId(consultationId));
    }

    /**
     * 新增意见征询
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:add')")
    @Log(title = "意见征询", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysOpinionConsultation sysOpinionConsultation)
    {
        return toAjax(sysOpinionConsultationService.insertSysOpinionConsultation(sysOpinionConsultation));
    }

    /**
     * 修改意见征询
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:edit')")
    @Log(title = "意见征询", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysOpinionConsultation sysOpinionConsultation)
    {
        return toAjax(sysOpinionConsultationService.updateSysOpinionConsultation(sysOpinionConsultation));
    }

    /**
     * 删除意见征询
     */
    @PreAuthorize("@ss.hasPermi('system:consultation:remove')")
    @Log(title = "意见征询", businessType = BusinessType.DELETE)
	@DeleteMapping("/{consultationIds}")
    public AjaxResult remove(@PathVariable Long[] consultationIds)
    {
        return toAjax(sysOpinionConsultationService.deleteSysOpinionConsultationByConsultationIds(consultationIds));
    }
}
