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
import com.ruoyi.system.domain.SysAdmin;
import com.ruoyi.system.service.ISysAdminService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 管理员Controller
 * 
 * @author ruoyi
 * @date 2025-12-27
 */
@RestController
@RequestMapping("/system/admin")
public class SysAdminController extends BaseController
{
    @Autowired
    private ISysAdminService sysAdminService;

    /**
     * 查询管理员列表
     */
    @PreAuthorize("@ss.hasPermi('system:admin:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAdmin sysAdmin)
    {
        startPage();
        List<SysAdmin> list = sysAdminService.selectSysAdminList(sysAdmin);
        return getDataTable(list);
    }

    /**
     * 导出管理员列表
     */
    @PreAuthorize("@ss.hasPermi('system:admin:export')")
    @Log(title = "管理员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAdmin sysAdmin)
    {
        List<SysAdmin> list = sysAdminService.selectSysAdminList(sysAdmin);
        ExcelUtil<SysAdmin> util = new ExcelUtil<SysAdmin>(SysAdmin.class);
        util.exportExcel(response, list, "管理员数据");
    }

    /**
     * 获取管理员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:admin:query')")
    @GetMapping(value = "/{adminId}")
    public AjaxResult getInfo(@PathVariable("adminId") Long adminId)
    {
        return success(sysAdminService.selectSysAdminByAdminId(adminId));
    }

    /**
     * 新增管理员
     */
    @PreAuthorize("@ss.hasPermi('system:admin:add')")
    @Log(title = "管理员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAdmin sysAdmin)
    {
        return toAjax(sysAdminService.insertSysAdmin(sysAdmin));
    }

    /**
     * 修改管理员
     */
    @PreAuthorize("@ss.hasPermi('system:admin:edit')")
    @Log(title = "管理员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAdmin sysAdmin)
    {
        return toAjax(sysAdminService.updateSysAdmin(sysAdmin));
    }

    /**
     * 删除管理员
     */
    @PreAuthorize("@ss.hasPermi('system:admin:remove')")
    @Log(title = "管理员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{adminIds}")
    public AjaxResult remove(@PathVariable Long[] adminIds)
    {
        return toAjax(sysAdminService.deleteSysAdminByAdminIds(adminIds));
    }
}
