package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysFormTemplate;
import com.ruoyi.system.service.ISysFormTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 自定义表单模板Controller
 *
 * 用于后端管理问卷/调查等动态表单。
 *
 * @author ruoyi
 * @date 2025-10-23
 */
@RestController
@RequestMapping("/system/formTemplate")
public class SysFormTemplateController extends BaseController
{
    @Autowired
    private ISysFormTemplateService sysFormTemplateService;

    /**
     * 查询模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:formTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysFormTemplate sysFormTemplate)
    {
        startPage();
        List<SysFormTemplate> list = sysFormTemplateService.selectSysFormTemplateList(sysFormTemplate);
        return getDataTable(list);
    }

    /**
     * 轻量列表（下拉选项）
     */
    @PreAuthorize("@ss.hasPermi('system:formTemplate:list')")
    @GetMapping("/options")
    public AjaxResult options(SysFormTemplate sysFormTemplate)
    {
        List<SysFormTemplate> list = sysFormTemplateService.selectSysFormTemplateOptions(sysFormTemplate);
        return success(list);
    }

    /**
     * 导出模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:formTemplate:export')")
    @Log(title = "自定义表单模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysFormTemplate sysFormTemplate)
    {
        List<SysFormTemplate> list = sysFormTemplateService.selectSysFormTemplateList(sysFormTemplate);
        ExcelUtil<SysFormTemplate> util = new ExcelUtil<>(SysFormTemplate.class);
        util.exportExcel(response, list, "自定义表单模板数据");
    }

    /**
     * 获取模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:formTemplate:query')")
    @GetMapping("/{formId}")
    public AjaxResult getInfo(@PathVariable("formId") Long formId)
    {
        return success(sysFormTemplateService.selectSysFormTemplateByFormId(formId));
    }

    /**
     * 新增模板
     */
    @PreAuthorize("@ss.hasPermi('system:formTemplate:add')")
    @Log(title = "自定义表单模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFormTemplate sysFormTemplate)
    {
        sysFormTemplate.setCreateBy(getUsername());
        return toAjax(sysFormTemplateService.insertSysFormTemplate(sysFormTemplate));
    }

    /**
     * 修改模板
     */
    @PreAuthorize("@ss.hasPermi('system:formTemplate:edit')")
    @Log(title = "自定义表单模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFormTemplate sysFormTemplate)
    {
        sysFormTemplate.setUpdateBy(getUsername());
        return toAjax(sysFormTemplateService.updateSysFormTemplate(sysFormTemplate));
    }

    /**
     * 删除模板
     */
    @PreAuthorize("@ss.hasPermi('system:formTemplate:remove')")
    @Log(title = "自定义表单模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{formIds}")
    public AjaxResult remove(@PathVariable Long[] formIds)
    {
        return toAjax(sysFormTemplateService.deleteSysFormTemplateByFormIds(formIds));
    }
}

