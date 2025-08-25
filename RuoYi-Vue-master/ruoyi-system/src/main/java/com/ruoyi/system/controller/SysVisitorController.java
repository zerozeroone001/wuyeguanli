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
import com.ruoyi.system.domain.SysVisitor;
import com.ruoyi.system.service.ISysVisitorService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 访客登记Controller
 *
 * @author ruoyi
 * @date 2025-08-25
 */
@Controller
@RequestMapping("/system/visitor")
public class SysVisitorController extends BaseController {
    private String prefix = "system/visitor";

    @Autowired
    private ISysVisitorService sysVisitorService;

    @GetMapping()
    public String visitor() {
        return prefix + "/visitor";
    }

    /**
     * 查询访客登记列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysVisitor sysVisitor) {
        startPage();
        List<SysVisitor> list = sysVisitorService.selectSysVisitorList(sysVisitor);
        return getDataTable(list);
    }

    /**
     * 导出访客登记列表
     */
    @Log(title = "访客登记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysVisitor sysVisitor) {
        List<SysVisitor> list = sysVisitorService.selectSysVisitorList(sysVisitor);
        ExcelUtil<SysVisitor> util = new ExcelUtil<SysVisitor>(SysVisitor.class);
        return util.exportExcel(list, "visitor");
    }

    /**
     * 新增访客登记
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存访客登记
     */
    @Log(title = "访客登记", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysVisitor sysVisitor) {
        return toAjax(sysVisitorService.insertSysVisitor(sysVisitor));
    }

    /**
     * 修改访客登记
     */
    @GetMapping("/edit/{visitorId}")
    public String edit(@PathVariable("visitorId") Long visitorId, ModelMap mmap) {
        SysVisitor sysVisitor = sysVisitorService.selectSysVisitorById(visitorId);
        mmap.put("sysVisitor", sysVisitor);
        return prefix + "/edit";
    }

    /**
     * 修改保存访客登记
     */
    @Log(title = "访客登记", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysVisitor sysVisitor) {
        return toAjax(sysVisitorService.updateSysVisitor(sysVisitor));
    }

    /**
     * 删除访客登记
     */
    @Log(title = "访客登记", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysVisitorService.deleteSysVisitorByIds(ids));
    }
}
