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
import com.ruoyi.system.domain.SysContractSupervision;
import com.ruoyi.system.service.ISysContractSupervisionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同履约监督Controller
 *
 * @author ruoyi
 * @date 2026-01-06
 */
@RestController
@RequestMapping("/system/contractSupervision")
public class SysContractSupervisionController extends BaseController
{
    @Autowired
    private ISysContractSupervisionService contractSupervisionService;

    /**
     * 查询合同履约监督列表
     */
    @PreAuthorize("@ss.hasPermi('system:contractSupervision:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysContractSupervision sysContractSupervision)
    {
        startPage();
        List<SysContractSupervision> list = contractSupervisionService.selectContractSupervisionList(sysContractSupervision);
        return getDataTable(list);
    }

    /**
     * 导出合同履约监督列表
     */
    @PreAuthorize("@ss.hasPermi('system:contractSupervision:export')")
    @Log(title = "合同履约监督", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysContractSupervision sysContractSupervision)
    {
        List<SysContractSupervision> list = contractSupervisionService.selectContractSupervisionList(sysContractSupervision);
        ExcelUtil<SysContractSupervision> util = new ExcelUtil<SysContractSupervision>(SysContractSupervision.class);
        util.exportExcel(response, list, "合同履约监督数据");
    }

    /**
     * 获取合同履约监督详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:contractSupervision:query')")
    @GetMapping(value = "/{supervisionId}")
    public AjaxResult getInfo(@PathVariable("supervisionId") Long supervisionId)
    {
        return success(contractSupervisionService.selectContractSupervisionById(supervisionId));
    }

    /**
     * 新增合同履约监督
     */
    @PreAuthorize("@ss.hasPermi('system:contractSupervision:add')")
    @Log(title = "合同履约监督", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysContractSupervision sysContractSupervision)
    {
        sysContractSupervision.setCreateBy(getUsername());
        return toAjax(contractSupervisionService.insertContractSupervision(sysContractSupervision));
    }

    /**
     * 修改合同履约监督
     */
    @PreAuthorize("@ss.hasPermi('system:contractSupervision:edit')")
    @Log(title = "合同履约监督", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysContractSupervision sysContractSupervision)
    {
        sysContractSupervision.setUpdateBy(getUsername());
        return toAjax(contractSupervisionService.updateContractSupervision(sysContractSupervision));
    }

    /**
     * 删除合同履约监督
     */
    @PreAuthorize("@ss.hasPermi('system:contractSupervision:remove')")
    @Log(title = "合同履约监督", businessType = BusinessType.DELETE)
    @DeleteMapping("/{supervisionIds}")
    public AjaxResult remove(@PathVariable Long[] supervisionIds)
    {
        return toAjax(contractSupervisionService.deleteContractSupervisionByIds(supervisionIds));
    }

    /**
     * 公示考核结果
     */
    @PreAuthorize("@ss.hasPermi('system:contractSupervision:publish')")
    @Log(title = "公示考核结果", businessType = BusinessType.UPDATE)
    @PutMapping("/publish/{supervisionId}")
    public AjaxResult publishResult(@PathVariable Long supervisionId)
    {
        return toAjax(contractSupervisionService.publishResult(supervisionId));
    }
}
