package com.ruoyi.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysWarranty;
import com.ruoyi.system.service.ISysWarrantyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备保修Controller
 *
 * @author ruoyi
 * @date 2025-08-25
 */
@Controller
@RequestMapping("/system/warranty")
public class SysWarrantyController extends BaseController {
    private String prefix = "system/warranty";

    @Autowired
    private ISysWarrantyService sysWarrantyService;

    @GetMapping()
    public String warranty() {
        return prefix + "/warranty";
    }

    /**
     * 查询设备保修列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysWarranty sysWarranty) {
        startPage();
        List<SysWarranty> list = sysWarrantyService.selectSysWarrantyList(sysWarranty);
        return getDataTable(list);
    }

    /**
     * 导出设备保修列表
     */
    @Log(title = "设备保修", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysWarranty sysWarranty) {
        List<SysWarranty> list = sysWarrantyService.selectSysWarrantyList(sysWarranty);
        ExcelUtil<SysWarranty> util = new ExcelUtil<SysWarranty>(SysWarranty.class);
        return util.exportExcel(list, "warranty");
    }

    /**
     * 新增设备保修
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存设备保修
     */
    @Log(title = "设备保修", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysWarranty sysWarranty) {
        return toAjax(sysWarrantyService.insertSysWarranty(sysWarranty));
    }

    /**
     * 修改设备保修
     */
    @GetMapping("/edit/{warrantyId}")
    public String edit(@PathVariable("warrantyId") Long warrantyId, ModelMap mmap) {
        SysWarranty sysWarranty = sysWarrantyService.selectSysWarrantyById(warrantyId);
        mmap.put("sysWarranty", sysWarranty);
        return prefix + "/edit";
    }

    /**
     * 修改保存设备保修
     */
    @Log(title = "设备保修", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysWarranty sysWarranty) {
        return toAjax(sysWarrantyService.updateSysWarranty(sysWarranty));
    }

    /**
     * 删除设备保修
     */
    @Log(title = "设备保修", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysWarrantyService.deleteSysWarrantyByIds(ids));
    }
}
