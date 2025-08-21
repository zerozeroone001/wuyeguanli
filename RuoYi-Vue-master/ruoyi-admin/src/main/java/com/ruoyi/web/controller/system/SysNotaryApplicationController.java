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
import com.ruoyi.system.domain.SysNotaryApplication;
import com.ruoyi.system.service.ISysNotaryApplicationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 公证服务申请Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/application")
public class SysNotaryApplicationController extends BaseController
{
    @Autowired
    private ISysNotaryApplicationService sysNotaryApplicationService;

    /**
     * 查询公证服务申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:application:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotaryApplication sysNotaryApplication)
    {
        startPage();
        List<SysNotaryApplication> list = sysNotaryApplicationService.selectSysNotaryApplicationList(sysNotaryApplication);
        return getDataTable(list);
    }

    /**
     * 导出公证服务申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:application:export')")
    @Log(title = "公证服务申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNotaryApplication sysNotaryApplication)
    {
        List<SysNotaryApplication> list = sysNotaryApplicationService.selectSysNotaryApplicationList(sysNotaryApplication);
        ExcelUtil<SysNotaryApplication> util = new ExcelUtil<SysNotaryApplication>(SysNotaryApplication.class);
        util.exportExcel(response, list, "公证服务申请数据");
    }

    /**
     * 获取公证服务申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:application:query')")
    @GetMapping(value = "/{applicationId}")
    public AjaxResult getInfo(@PathVariable("applicationId") Long applicationId)
    {
        return success(sysNotaryApplicationService.selectSysNotaryApplicationByApplicationId(applicationId));
    }

    /**
     * 新增公证服务申请
     */
    @PreAuthorize("@ss.hasPermi('system:application:add')")
    @Log(title = "公证服务申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysNotaryApplication sysNotaryApplication)
    {
        return toAjax(sysNotaryApplicationService.insertSysNotaryApplication(sysNotaryApplication));
    }

    /**
     * 修改公证服务申请
     */
    @PreAuthorize("@ss.hasPermi('system:application:edit')")
    @Log(title = "公证服务申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysNotaryApplication sysNotaryApplication)
    {
        return toAjax(sysNotaryApplicationService.updateSysNotaryApplication(sysNotaryApplication));
    }

    /**
     * 删除公证服务申请
     */
    @PreAuthorize("@ss.hasPermi('system:application:remove')")
    @Log(title = "公证服务申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{applicationIds}")
    public AjaxResult remove(@PathVariable Long[] applicationIds)
    {
        return toAjax(sysNotaryApplicationService.deleteSysNotaryApplicationByApplicationIds(applicationIds));
    }
}
