package com.ruoyi.ruoyiuser.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysPropertyRegulation;
import com.ruoyi.system.service.ISysPropertyRegulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 物业制度管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/regulation")
public class SysPropertyRegulationController extends BaseController
{
    @Autowired
    private ISysPropertyRegulationService sysPropertyRegulationService;

    /**
     * 查询物业制度管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:regulation:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyRegulation sysPropertyRegulation)
    {
        startPage();
        List<SysPropertyRegulation> list = sysPropertyRegulationService.selectSysPropertyRegulationList(sysPropertyRegulation);
        return getDataTable(list);
    }

    /**
     * 导出物业制度管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:regulation:export')")
    @Log(title = "物业制度管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyRegulation sysPropertyRegulation)
    {
        List<SysPropertyRegulation> list = sysPropertyRegulationService.selectSysPropertyRegulationList(sysPropertyRegulation);
        ExcelUtil<SysPropertyRegulation> util = new ExcelUtil<SysPropertyRegulation>(SysPropertyRegulation.class);
        util.exportExcel(response, list, "物业制度管理数据");
    }

    /**
     * 获取物业制度管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:regulation:query')")
    @GetMapping(value = "/{regulationId}")
    public AjaxResult getInfo(@PathVariable("regulationId") Long regulationId)
    {
        return success(sysPropertyRegulationService.selectSysPropertyRegulationByRegulationId(regulationId));
    }

    /**
     * 新增物业制度管理
     */
    @PreAuthorize("@ss.hasPermi('system:regulation:add')")
    @Log(title = "物业制度管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPropertyRegulation sysPropertyRegulation)
    {
        return toAjax(sysPropertyRegulationService.insertSysPropertyRegulation(sysPropertyRegulation));
    }

    /**
     * 修改物业制度管理
     */
    @PreAuthorize("@ss.hasPermi('system:regulation:edit')")
    @Log(title = "物业制度管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPropertyRegulation sysPropertyRegulation)
    {
        return toAjax(sysPropertyRegulationService.updateSysPropertyRegulation(sysPropertyRegulation));
    }

    /**
     * 删除物业制度管理
     */
    @PreAuthorize("@ss.hasPermi('system:regulation:remove')")
    @Log(title = "物业制度管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{regulationIds}")
    public AjaxResult remove(@PathVariable Long[] regulationIds)
    {
        return toAjax(sysPropertyRegulationService.deleteSysPropertyRegulationByRegulationIds(regulationIds));
    }


    @GetMapping("/recent")
    public AjaxResult getRecentRegulations() {
        // TODO: Implement this method to fetch real data
        return AjaxResult.success();
    }

    @GetMapping("/download/{regulationId}")
    public void downloadRegulation(@PathVariable("regulationId") Long regulationId, HttpServletResponse response) {
        // TODO: Implement this method to download the file
    }
}
