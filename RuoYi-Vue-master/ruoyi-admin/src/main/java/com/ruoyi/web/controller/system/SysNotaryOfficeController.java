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
import com.ruoyi.system.domain.SysNotaryOffice;
import com.ruoyi.system.service.ISysNotaryOfficeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 公证处信息Controller
 * 
 * @author ruoyi
 * @date 2025-09-11
 */
@RestController
@RequestMapping("/system/office")
public class SysNotaryOfficeController extends BaseController
{
    @Autowired
    private ISysNotaryOfficeService sysNotaryOfficeService;

    /**
     * 查询公证处信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:office:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotaryOffice sysNotaryOffice)
    {
        startPage();
        List<SysNotaryOffice> list = sysNotaryOfficeService.selectSysNotaryOfficeList(sysNotaryOffice);
        return getDataTable(list);
    }

    /**
     * 导出公证处信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:office:export')")
    @Log(title = "公证处信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNotaryOffice sysNotaryOffice)
    {
        List<SysNotaryOffice> list = sysNotaryOfficeService.selectSysNotaryOfficeList(sysNotaryOffice);
        ExcelUtil<SysNotaryOffice> util = new ExcelUtil<SysNotaryOffice>(SysNotaryOffice.class);
        util.exportExcel(response, list, "公证处信息数据");
    }

    /**
     * 获取公证处信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:office:query')")
    @GetMapping(value = "/{officeId}")
    public AjaxResult getInfo(@PathVariable("officeId") Long officeId)
    {
        return success(sysNotaryOfficeService.selectSysNotaryOfficeByOfficeId(officeId));
    }

    /**
     * 新增公证处信息
     */
    @PreAuthorize("@ss.hasPermi('system:office:add')")
    @Log(title = "公证处信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysNotaryOffice sysNotaryOffice)
    {
        return toAjax(sysNotaryOfficeService.insertSysNotaryOffice(sysNotaryOffice));
    }

    /**
     * 修改公证处信息
     */
    @PreAuthorize("@ss.hasPermi('system:office:edit')")
    @Log(title = "公证处信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysNotaryOffice sysNotaryOffice)
    {
        return toAjax(sysNotaryOfficeService.updateSysNotaryOffice(sysNotaryOffice));
    }

    /**
     * 删除公证处信息
     */
    @PreAuthorize("@ss.hasPermi('system:office:remove')")
    @Log(title = "公证处信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{officeIds}")
    public AjaxResult remove(@PathVariable Long[] officeIds)
    {
        return toAjax(sysNotaryOfficeService.deleteSysNotaryOfficeByOfficeIds(officeIds));
    }
}
