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
import com.ruoyi.system.domain.SysPropertyRegulation;
import com.ruoyi.system.service.ISysPropertyRegulationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

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
}
