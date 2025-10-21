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
import com.ruoyi.system.domain.SysPropertyContract;
import com.ruoyi.system.service.ISysPropertyContractService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物业服务合同Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/contract")
public class SysPropertyContractController extends BaseController
{
    @Autowired
    private ISysPropertyContractService sysPropertyContractService;

    /**
     * 查询物业服务合同列表
     */
    @PreAuthorize("@ss.hasPermi('system:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyContract sysPropertyContract)
    {
        // 列表查询前根据当前账号解析小区条件，普通账号自动绑定自身小区
        sysPropertyContract.setCommunityId(resolveCommunityId(sysPropertyContract.getCommunityId()));
        startPage();
        List<SysPropertyContract> list = sysPropertyContractService.selectSysPropertyContractList(sysPropertyContract);
        return getDataTable(list);
    }

    /**
     * 导出物业服务合同列表
     */
    @PreAuthorize("@ss.hasPermi('system:contract:export')")
    @Log(title = "物业服务合同", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyContract sysPropertyContract)
    {
        sysPropertyContract.setCommunityId(resolveCommunityId(sysPropertyContract.getCommunityId()));
        List<SysPropertyContract> list = sysPropertyContractService.selectSysPropertyContractList(sysPropertyContract);
        ExcelUtil<SysPropertyContract> util = new ExcelUtil<SysPropertyContract>(SysPropertyContract.class);
        util.exportExcel(response, list, "物业服务合同数据");
    }

    /**
     * 获取物业服务合同详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/{contractId}")
    public AjaxResult getInfo(@PathVariable("contractId") Long contractId)
    {
        return success(sysPropertyContractService.selectSysPropertyContractByContractId(contractId));
    }

    /**
     * 新增物业服务合同
     */
    @PreAuthorize("@ss.hasPermi('system:contract:add')")
    @Log(title = "物业服务合同", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPropertyContract sysPropertyContract)
    {
        sysPropertyContract.setCommunityId(requireResolvedCommunityId(sysPropertyContract.getCommunityId(), "请选择小区后再新增合同"));
        return toAjax(sysPropertyContractService.insertSysPropertyContract(sysPropertyContract));
    }

    /**
     * 修改物业服务合同
     */
    @PreAuthorize("@ss.hasPermi('system:contract:edit')")
    @Log(title = "物业服务合同", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPropertyContract sysPropertyContract)
    {
        sysPropertyContract.setCommunityId(requireResolvedCommunityId(sysPropertyContract.getCommunityId(), "请选择小区后再修改合同"));
        return toAjax(sysPropertyContractService.updateSysPropertyContract(sysPropertyContract));
    }

    /**
     * 删除物业服务合同
     */
    @PreAuthorize("@ss.hasPermi('system:contract:remove')")
    @Log(title = "物业服务合同", businessType = BusinessType.DELETE)
	@DeleteMapping("/{contractIds}")
    public AjaxResult remove(@PathVariable Long[] contractIds)
    {
        return toAjax(sysPropertyContractService.deleteSysPropertyContractByContractIds(contractIds));
    }
}
