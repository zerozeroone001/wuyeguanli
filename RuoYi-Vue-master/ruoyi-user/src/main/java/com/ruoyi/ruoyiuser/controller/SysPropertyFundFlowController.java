package com.ruoyi.ruoyiuser.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysPropertyFundFlow;
import com.ruoyi.system.service.ISysPropertyFundFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 资金流水Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/flow")
public class SysPropertyFundFlowController extends BaseController
{
    @Autowired
    private ISysPropertyFundFlowService sysPropertyFundFlowService;

    /**
     * 查询资金流水列表
     */
    @PreAuthorize("@ss.hasPermi('system:flow:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyFundFlow sysPropertyFundFlow)
    {
        startPage();
        List<SysPropertyFundFlow> list = sysPropertyFundFlowService.selectSysPropertyFundFlowList(sysPropertyFundFlow);
        return getDataTable(list);
    }

    /**
     * 导出资金流水列表
     */
    @PreAuthorize("@ss.hasPermi('system:flow:export')")
    @Log(title = "资金流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyFundFlow sysPropertyFundFlow)
    {
        List<SysPropertyFundFlow> list = sysPropertyFundFlowService.selectSysPropertyFundFlowList(sysPropertyFundFlow);
        ExcelUtil<SysPropertyFundFlow> util = new ExcelUtil<SysPropertyFundFlow>(SysPropertyFundFlow.class);
        util.exportExcel(response, list, "资金流水数据");
    }

    /**
     * 获取资金流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:flow:query')")
    @GetMapping(value = "/{flowId}")
    public AjaxResult getInfo(@PathVariable("flowId") Long flowId)
    {
        return success(sysPropertyFundFlowService.selectSysPropertyFundFlowByFlowId(flowId));
    }

    /**
     * 新增资金流水
     */
    @PreAuthorize("@ss.hasPermi('system:flow:add')")
    @Log(title = "资金流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPropertyFundFlow sysPropertyFundFlow)
    {
        return toAjax(sysPropertyFundFlowService.insertSysPropertyFundFlow(sysPropertyFundFlow));
    }

    /**
     * 修改资金流水
     */
    @PreAuthorize("@ss.hasPermi('system:flow:edit')")
    @Log(title = "资金流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPropertyFundFlow sysPropertyFundFlow)
    {
        return toAjax(sysPropertyFundFlowService.updateSysPropertyFundFlow(sysPropertyFundFlow));
    }

    /**
     * 删除资金流水
     */
    @PreAuthorize("@ss.hasPermi('system:flow:remove')")
    @Log(title = "资金流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{flowIds}")
    public AjaxResult remove(@PathVariable Long[] flowIds)
    {
        return toAjax(sysPropertyFundFlowService.deleteSysPropertyFundFlowByFlowIds(flowIds));
    }

    @GetMapping("/overview")
    public AjaxResult getFundOverview() {
        // TODO: Implement this method to fetch real data
        return AjaxResult.success();
    }

    @GetMapping("/monthly-stats")
    public AjaxResult getMonthlyStats() {
        // TODO: Implement this method to fetch real data
        return AjaxResult.success();
    }

    @GetMapping("/transactions/recent")
    public AjaxResult getRecentTransactions() {
        // TODO: Implement this method to fetch real data
        return AjaxResult.success();
    }

    @GetMapping("/usage-analysis")
    public AjaxResult getUsageAnalysis() {
        // TODO: Implement this method to fetch real data
        return AjaxResult.success();
    }
}
