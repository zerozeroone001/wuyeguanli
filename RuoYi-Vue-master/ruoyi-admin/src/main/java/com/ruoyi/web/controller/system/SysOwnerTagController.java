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
import com.ruoyi.system.domain.SysOwnerTag;
import com.ruoyi.system.service.ISysOwnerTagService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 业主标签Controller
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/system/ownerTag")
public class SysOwnerTagController extends BaseController
{
    @Autowired
    private ISysOwnerTagService sysOwnerTagService;

    /**
     * 查询业主标签列表
     */
    @PreAuthorize("@ss.hasPermi('system:ownerTag:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOwnerTag sysOwnerTag)
    {
        startPage();
        List<SysOwnerTag> list = sysOwnerTagService.selectSysOwnerTagList(sysOwnerTag);
        return getDataTable(list);
    }

    /**
     * 导出业主标签列表
     */
    @PreAuthorize("@ss.hasPermi('system:ownerTag:export')")
    @Log(title = "业主标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOwnerTag sysOwnerTag)
    {
        List<SysOwnerTag> list = sysOwnerTagService.selectSysOwnerTagList(sysOwnerTag);
        ExcelUtil<SysOwnerTag> util = new ExcelUtil<SysOwnerTag>(SysOwnerTag.class);
        util.exportExcel(response, list, "业主标签数据");
    }

    /**
     * 获取业主标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:ownerTag:query')")
    @GetMapping(value = "/{tagId}")
    public AjaxResult getInfo(@PathVariable("tagId") Long tagId)
    {
        return success(sysOwnerTagService.selectSysOwnerTagByTagId(tagId));
    }

    /**
     * 新增业主标签
     */
    @PreAuthorize("@ss.hasPermi('system:ownerTag:add')")
    @Log(title = "业主标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysOwnerTag sysOwnerTag)
    {
        return toAjax(sysOwnerTagService.insertSysOwnerTag(sysOwnerTag));
    }

    /**
     * 修改业主标签
     */
    @PreAuthorize("@ss.hasPermi('system:ownerTag:edit')")
    @Log(title = "业主标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysOwnerTag sysOwnerTag)
    {
        return toAjax(sysOwnerTagService.updateSysOwnerTag(sysOwnerTag));
    }

    /**
     * 删除业主标签
     */
    @PreAuthorize("@ss.hasPermi('system:ownerTag:remove')")
    @Log(title = "业主标签", businessType = BusinessType.DELETE)
	@DeleteMapping("/{tagIds}")
    public AjaxResult remove(@PathVariable Long[] tagIds)
    {
        return toAjax(sysOwnerTagService.deleteSysOwnerTagByTagIds(tagIds));
    }
}
