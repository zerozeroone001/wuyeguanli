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
import com.ruoyi.system.domain.SysOwnerChange;
import com.ruoyi.system.service.ISysOwnerChangeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 业主变更申请Controller
 * 
 * @author ruoyi
 * @date 2025-12-03
 */
@RestController
@RequestMapping("/system/ownerChange")
public class SysOwnerChangeController extends BaseController
{
    @Autowired
    private ISysOwnerChangeService sysOwnerChangeService;

    /**
     * 查询业主变更申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:ownerChange:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOwnerChange sysOwnerChange)
    {
        startPage();
        List<SysOwnerChange> list = sysOwnerChangeService.selectSysOwnerChangeList(sysOwnerChange);
        return getDataTable(list);
    }

    /**
     * 导出业主变更申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:ownerChange:export')")
    @Log(title = "业主变更申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOwnerChange sysOwnerChange)
    {
        List<SysOwnerChange> list = sysOwnerChangeService.selectSysOwnerChangeList(sysOwnerChange);
        ExcelUtil<SysOwnerChange> util = new ExcelUtil<SysOwnerChange>(SysOwnerChange.class);
        util.exportExcel(response, list, "业主变更申请数据");
    }

    /**
     * 获取业主变更申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:ownerChange:query')")
    @GetMapping(value = "/{changeId}")
    public AjaxResult getInfo(@PathVariable("changeId") Long changeId)
    {
        return success(sysOwnerChangeService.selectSysOwnerChangeByChangeId(changeId));
    }

    /**
     * 新增业主变更申请
     */
    @PreAuthorize("@ss.hasPermi('system:ownerChange:add')")
    @Log(title = "业主变更申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysOwnerChange sysOwnerChange)
    {
        sysOwnerChange.setCreateBy(getUsername());
        return toAjax(sysOwnerChangeService.insertSysOwnerChange(sysOwnerChange));
    }

    /**
     * 修改业主变更申请
     */
    @PreAuthorize("@ss.hasPermi('system:ownerChange:edit')")
    @Log(title = "业主变更申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysOwnerChange sysOwnerChange)
    {
        sysOwnerChange.setUpdateBy(getUsername());
        return toAjax(sysOwnerChangeService.updateSysOwnerChange(sysOwnerChange));
    }

    /**
     * 审核业主变更申请
     */
    @PreAuthorize("@ss.hasPermi('system:ownerChange:audit')")
    @Log(title = "业主变更申请审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody SysOwnerChange sysOwnerChange)
    {
        sysOwnerChange.setUpdateBy(getUsername());
        return toAjax(sysOwnerChangeService.auditSysOwnerChange(sysOwnerChange));
    }

    /**
     * 删除业主变更申请
     */
    @PreAuthorize("@ss.hasPermi('system:ownerChange:remove')")
    @Log(title = "业主变更申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{changeIds}")
    public AjaxResult remove(@PathVariable Long[] changeIds)
    {
        return toAjax(sysOwnerChangeService.deleteSysOwnerChangeByChangeIds(changeIds));
    }
}
