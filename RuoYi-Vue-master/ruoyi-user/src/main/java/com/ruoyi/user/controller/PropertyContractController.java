package com.ruoyi.user.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysPropertyContract;
import com.ruoyi.system.service.ISysPropertyContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 物业服务合同Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/contract")
public class PropertyContractController extends BaseController
{
    @Autowired
    private ISysPropertyContractService sysPropertyContractService;

    /**
     * 查询物业服务合同列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyContract sysPropertyContract)
    {
        startPage();
        List<SysPropertyContract> list = sysPropertyContractService.selectSysPropertyContractList(sysPropertyContract);
        return getDataTable(list);
    }

    /**
     * 导出物业服务合同列表
     */
    @Log(title = "物业服务合同", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyContract sysPropertyContract)
    {
        List<SysPropertyContract> list = sysPropertyContractService.selectSysPropertyContractList(sysPropertyContract);
        ExcelUtil<SysPropertyContract> util = new ExcelUtil<SysPropertyContract>(SysPropertyContract.class);
        util.exportExcel(response, list, "物业服务合同数据");
    }

    /**
     * 获取物业服务合同详细信息
     */
    @GetMapping(value = "/{contractId}")
    public AjaxResult getInfo(@PathVariable("contractId") Long contractId)
    {
        return success(sysPropertyContractService.selectSysPropertyContractByContractId(contractId));
    }




    @GetMapping("/download/{contractId}")
    public void downloadContract(@PathVariable("contractId") Long contractId, HttpServletResponse response) {
        // TODO: Implement this method to download the file
    }
}
