package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Map;
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
import com.ruoyi.system.domain.SysNotaryApplication;
import com.ruoyi.system.service.ISysNotaryApplicationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 公证服务申请Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/notary")
public class SysNotaryApplicationController extends BaseController
{
    @Autowired
    private ISysNotaryApplicationService sysNotaryApplicationService;

    /**
     * 查询公证服务申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:application:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotaryApplication sysNotaryApplication)
    {
        startPage();
        List<SysNotaryApplication> list = sysNotaryApplicationService.selectSysNotaryApplicationList(sysNotaryApplication);
        return getDataTable(list);
    }

    /**
     * 导出公证服务申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:application:export')")
    @Log(title = "公证服务申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNotaryApplication sysNotaryApplication)
    {
        List<SysNotaryApplication> list = sysNotaryApplicationService.selectSysNotaryApplicationList(sysNotaryApplication);
        ExcelUtil<SysNotaryApplication> util = new ExcelUtil<SysNotaryApplication>(SysNotaryApplication.class);
        util.exportExcel(response, list, "公证服务申请数据");
    }

    /**
     * 获取公证服务申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:application:query')")
    @GetMapping(value = "/{applicationId}")
    public AjaxResult getInfo(@PathVariable("applicationId") Long applicationId)
    {
        return success(sysNotaryApplicationService.selectSysNotaryApplicationByApplicationId(applicationId));
    }

    /**
     * 新增公证服务申请
     */
    @PreAuthorize("@ss.hasPermi('system:application:add')")
    @Log(title = "公证服务申请", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody SysNotaryApplication sysNotaryApplication)
    {
        return toAjax(sysNotaryApplicationService.insertSysNotaryApplication(sysNotaryApplication));
    }

    /**
     * 修改公证服务申请
     */
    @PreAuthorize("@ss.hasPermi('system:application:edit')")
    @Log(title = "公证服务申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysNotaryApplication sysNotaryApplication)
    {
        return toAjax(sysNotaryApplicationService.updateSysNotaryApplication(sysNotaryApplication));
    }

    /**
     * 删除公证服务申请
     */
    @PreAuthorize("@ss.hasPermi('system:application:remove')")
    @Log(title = "公证服务申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{applicationIds}")
    public AjaxResult remove(@PathVariable Long[] applicationIds)
    {
        return toAjax(sysNotaryApplicationService.deleteSysNotaryApplicationByApplicationIds(applicationIds));
    }

    /**
     * 审核公证申请
     */
    @PreAuthorize("@ss.hasPermi('system:application:review')")
    @Log(title = "公证申请审核", businessType = BusinessType.UPDATE)
    @PostMapping("/review")
    public AjaxResult review(@RequestBody Map<String, Object> params)
    {
        Long notaryId = Long.valueOf(params.get("notaryId").toString());
        String reviewResult = (String) params.get("reviewResult"); // approved, rejected
        String reviewComment = (String) params.get("reviewComment");
        return toAjax(sysNotaryApplicationService.reviewApplication(notaryId, reviewResult, reviewComment));
    }

    /**
     * 分配公证员
     */
    @PreAuthorize("@ss.hasPermi('system:application:assign')")
    @Log(title = "分配公证员", businessType = BusinessType.UPDATE)
    @PostMapping("/assign")
    public AjaxResult assignNotary(@RequestBody Map<String, Object> params)
    {
        Long notaryId = Long.valueOf(params.get("notaryId").toString());
        Long notaryPersonId = Long.valueOf(params.get("notaryPersonId").toString());
        return toAjax(sysNotaryApplicationService.assignNotary(notaryId, notaryPersonId));
    }

    /**
     * 更新公证进度
     */
    @PreAuthorize("@ss.hasPermi('system:application:progress')")
    @Log(title = "更新公证进度", businessType = BusinessType.UPDATE)
    @PostMapping("/progress")
    public AjaxResult updateProgress(@RequestBody Map<String, Object> params)
    {
        Long notaryId = Long.valueOf(params.get("notaryId").toString());
        Integer progress = Integer.valueOf(params.get("progress").toString());
        String comment = (String) params.get("comment");
        return toAjax(sysNotaryApplicationService.updateProgress(notaryId, progress, comment));
    }

    /**
     * 完成公证
     */
    @PreAuthorize("@ss.hasPermi('system:application:complete')")
    @Log(title = "完成公证", businessType = BusinessType.UPDATE)
    @PostMapping("/complete")
    public AjaxResult completeNotary(@RequestBody Map<String, Object> params)
    {
        Long notaryId = Long.valueOf(params.get("notaryId").toString());
        String certificateNo = (String) params.get("certificateNo");
        String certificateUrl = (String) params.get("certificateUrl");
        String comment = (String) params.get("comment");
        return toAjax(sysNotaryApplicationService.completeNotary(notaryId, certificateNo, certificateUrl, comment));
    }

    /**
     * 获取公证统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:application:stats')")
    @GetMapping("/stats")
    public AjaxResult getNotaryStats()
    {
        Map<String, Object> stats = sysNotaryApplicationService.getNotaryManagementStats();
        return success(stats);
    }

    /**
     * 获取公证流程日志
     */
    @PreAuthorize("@ss.hasPermi('system:application:query')")
    @GetMapping("/{notaryId}/process")
    public AjaxResult getProcessLogs(@PathVariable("notaryId") Long notaryId)
    {
        return success(sysNotaryApplicationService.getProcessLogs(notaryId));
    }

    /**
     * 批量操作公证申请
     */
    @PreAuthorize("@ss.hasPermi('system:application:batch')")
    @Log(title = "批量操作公证申请", businessType = BusinessType.UPDATE)
    @PostMapping("/batch")
    @SuppressWarnings("unchecked")
    public AjaxResult batchOperation(@RequestBody Map<String, Object> params)
    {
        Long[] notaryIds = ((List<Long>) params.get("notaryIds")).toArray(new Long[0]);
        String operation = (String) params.get("operation");
        String comment = (String) params.get("comment");
        return toAjax(sysNotaryApplicationService.batchOperation(notaryIds, operation, comment));
    }

    /**
     * 获取公证类型配置
     */
    @PreAuthorize("@ss.hasPermi('system:application:config')")
    @GetMapping("/type/config")
    public AjaxResult getTypeConfigs()
    {
        return success(sysNotaryApplicationService.getNotaryTypeConfigs());
    }

    /**
     * 更新公证类型配置
     */
    @PreAuthorize("@ss.hasPermi('system:application:config')")
    @Log(title = "更新公证类型配置", businessType = BusinessType.UPDATE)
    @PostMapping("/type/config")
    public AjaxResult updateTypeConfig(@RequestBody Map<String, Object> config)
    {
        return toAjax(sysNotaryApplicationService.updateTypeConfig(config));
    }
}
