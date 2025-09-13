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
import com.ruoyi.system.domain.SysRegulationCategory;
import com.ruoyi.system.service.ISysRegulationCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 制度分类Controller
 * 
 * @author ruoyi
 * @date 2025-09-13
 */
@RestController
@RequestMapping("/system/category")
public class SysRegulationCategoryController extends BaseController
{
    @Autowired
    private ISysRegulationCategoryService sysRegulationCategoryService;

    /**
     * 查询制度分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRegulationCategory sysRegulationCategory)
    {
        startPage();
        List<SysRegulationCategory> list = sysRegulationCategoryService.selectSysRegulationCategoryList(sysRegulationCategory);
        return getDataTable(list);
    }

    /**
     * 导出制度分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:category:export')")
    @Log(title = "制度分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRegulationCategory sysRegulationCategory)
    {
        List<SysRegulationCategory> list = sysRegulationCategoryService.selectSysRegulationCategoryList(sysRegulationCategory);
        ExcelUtil<SysRegulationCategory> util = new ExcelUtil<SysRegulationCategory>(SysRegulationCategory.class);
        util.exportExcel(response, list, "制度分类数据");
    }

    /**
     * 获取制度分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId)
    {
        return success(sysRegulationCategoryService.selectSysRegulationCategoryByCategoryId(categoryId));
    }

    /**
     * 新增制度分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:add')")
    @Log(title = "制度分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysRegulationCategory sysRegulationCategory)
    {
        return toAjax(sysRegulationCategoryService.insertSysRegulationCategory(sysRegulationCategory));
    }

    /**
     * 修改制度分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:edit')")
    @Log(title = "制度分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysRegulationCategory sysRegulationCategory)
    {
        return toAjax(sysRegulationCategoryService.updateSysRegulationCategory(sysRegulationCategory));
    }

    /**
     * 删除制度分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:remove')")
    @Log(title = "制度分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(sysRegulationCategoryService.deleteSysRegulationCategoryByCategoryIds(categoryIds));
    }
}
