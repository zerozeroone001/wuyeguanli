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
import com.ruoyi.system.domain.SysEstateUserApply;
import com.ruoyi.system.service.ISysEstateUserApplyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 房产认证申请Controller
 * 
 * @author ruoyi
 * @date 2025-12-07
 */
@RestController
@RequestMapping("/system/apply")
public class SysEstateUserApplyController extends BaseController
{
    @Autowired
    private ISysEstateUserApplyService sysEstateUserApplyService;

    /**
     * 查询房产认证申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:apply:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysEstateUserApply sysEstateUserApply)
    {
        startPage();
        List<SysEstateUserApply> list = sysEstateUserApplyService.selectSysEstateUserApplyList(sysEstateUserApply);
        return getDataTable(list);
    }

    /**
     * 导出房产认证申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:apply:export')")
    @Log(title = "房产认证申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysEstateUserApply sysEstateUserApply)
    {
        List<SysEstateUserApply> list = sysEstateUserApplyService.selectSysEstateUserApplyList(sysEstateUserApply);
        ExcelUtil<SysEstateUserApply> util = new ExcelUtil<SysEstateUserApply>(SysEstateUserApply.class);
        util.exportExcel(response, list, "房产认证申请数据");
    }

    /**
     * 获取房产认证申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:apply:query')")
    @GetMapping(value = "/{applyId}")
    public AjaxResult getInfo(@PathVariable("applyId") Long applyId)
    {
        return success(sysEstateUserApplyService.selectSysEstateUserApplyByApplyId(applyId));
    }

    /**
     * 新增房产认证申请
     */
    @PreAuthorize("@ss.hasPermi('system:apply:add')")
    @Log(title = "房产认证申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysEstateUserApply sysEstateUserApply)
    {
        return toAjax(sysEstateUserApplyService.insertSysEstateUserApply(sysEstateUserApply));
    }

    /**
     * 修改房产认证申请
     */
    @PreAuthorize("@ss.hasPermi('system:apply:edit')")
    @Log(title = "房产认证申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysEstateUserApply sysEstateUserApply)
    {
        return toAjax(sysEstateUserApplyService.updateSysEstateUserApply(sysEstateUserApply));
    }

    /**
     * 删除房产认证申请
     */
    @PreAuthorize("@ss.hasPermi('system:apply:remove')")
    @Log(title = "房产认证申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{applyIds}")
    public AjaxResult remove(@PathVariable Long[] applyIds)
    {
        return toAjax(sysEstateUserApplyService.deleteSysEstateUserApplyByApplyIds(applyIds));
    }
    
    /**
     * 审核房产认证申请
     */
    @PreAuthorize("@ss.hasPermi('system:apply:audit')")
    @Log(title = "房产认证申请审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody SysEstateUserApply sysEstateUserApply)
    {
        return toAjax(sysEstateUserApplyService.auditApply(sysEstateUserApply.getApplyId(), sysEstateUserApply.getStatus(), sysEstateUserApply.getRemark()));
    }
}
