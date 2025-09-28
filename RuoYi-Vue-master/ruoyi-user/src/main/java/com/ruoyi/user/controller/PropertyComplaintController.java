package com.ruoyi.user.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ComplaintNoUtils;
import com.ruoyi.system.domain.SysPropertyComplaint;
import com.ruoyi.system.service.ISysPropertyComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 投诉管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/complaint")
public class PropertyComplaintController extends BaseController
{
    @Autowired
    private ISysPropertyComplaintService sysPropertyComplaintService;

    /**
     * 查询投诉管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:complaint:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyComplaint sysPropertyComplaint)
    {
        startPage();
        List<SysPropertyComplaint> list = sysPropertyComplaintService.selectSysPropertyComplaintList(sysPropertyComplaint);
        return getDataTable(list);
    }

    /**
     * 导出投诉管理列表
     */
    @Log(title = "投诉管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyComplaint sysPropertyComplaint)
    {
        List<SysPropertyComplaint> list = sysPropertyComplaintService.selectSysPropertyComplaintList(sysPropertyComplaint);
        ExcelUtil<SysPropertyComplaint> util = new ExcelUtil<SysPropertyComplaint>(SysPropertyComplaint.class);
        util.exportExcel(response, list, "投诉管理数据");
    }

    /**
     * 获取投诉管理详细信息
     */
    @GetMapping(value = "/{complaintId}")
    public AjaxResult getInfo(@PathVariable("complaintId") Long complaintId)
    {
        return success(sysPropertyComplaintService.selectSysPropertyComplaintByComplaintId(complaintId));
    }

    /**
     * 新增投诉管理
     */
    @PreAuthorize("@ss.hasPermi('system:complaint:add')")
    @Log(title = "投诉管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPropertyComplaint sysPropertyComplaint)
    {
        // 自动生成投诉编号
        if (StringUtils.isEmpty(sysPropertyComplaint.getComplaintNo())) {
            sysPropertyComplaint.setComplaintNo(ComplaintNoUtils.generateComplaintNo());
        }
        // 设置创建人
        sysPropertyComplaint.setCreateBy(SecurityUtils.getUsername());
        // 设置默认状态为待处理
        if (StringUtils.isEmpty(sysPropertyComplaint.getStatus())) {
            sysPropertyComplaint.setStatus("0");
        }
        return toAjax(sysPropertyComplaintService.insertSysPropertyComplaint(sysPropertyComplaint));
    }

    /**
     * 修改投诉管理
     */
    @PreAuthorize("@ss.hasPermi('system:complaint:edit')")
    @Log(title = "投诉管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPropertyComplaint sysPropertyComplaint)
    {
        // 设置更新人
        sysPropertyComplaint.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(sysPropertyComplaintService.updateSysPropertyComplaint(sysPropertyComplaint));
    }

    /**
     * 删除投诉管理
     */
    @Log(title = "投诉管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{complaintIds}")
    public AjaxResult remove(@PathVariable Long[] complaintIds)
    {
        return toAjax(sysPropertyComplaintService.deleteSysPropertyComplaintByComplaintIds(complaintIds));
    }

    /**
     * 获取投诉统计信息
     */
    @GetMapping("/stats")
    public AjaxResult getComplaintStats()
    {
        return AjaxResult.success(sysPropertyComplaintService.getComplaintStats());
    }

    /**
     * 分配投诉处理人
     */
    @PreAuthorize("@ss.hasPermi('system:complaint:assign')")
    @Log(title = "分配投诉处理人", businessType = BusinessType.UPDATE)
    @PutMapping("/assign")
    public AjaxResult assignComplaint(@RequestBody Map<String, Object> params)
    {
        Long complaintId = Long.valueOf(params.get("complaintId").toString());
        Long handlerId = Long.valueOf(params.get("handlerId").toString());
        
        SysPropertyComplaint complaint = new SysPropertyComplaint();
        complaint.setComplaintId(complaintId);
        complaint.setHandlerId(handlerId);
        complaint.setStatus("1"); // 设置为处理中
        complaint.setHandleTime(new Date());
        complaint.setUpdateBy(SecurityUtils.getUsername());
        
        return toAjax(sysPropertyComplaintService.updateSysPropertyComplaint(complaint));
    }

    /**
     * 完成投诉处理
     */
    @PreAuthorize("@ss.hasPermi('system:complaint:complete')")
    @Log(title = "完成投诉处理", businessType = BusinessType.UPDATE)
    @PutMapping("/complete")
    public AjaxResult completeComplaint(@RequestBody Map<String, Object> params)
    {
        Long complaintId = Long.valueOf(params.get("complaintId").toString());
        String satisfaction = params.get("satisfaction") != null ? params.get("satisfaction").toString() : null;
        
        SysPropertyComplaint complaint = new SysPropertyComplaint();
        complaint.setComplaintId(complaintId);
        complaint.setStatus("2"); // 设置为已完成
        complaint.setCompleteTime(new Date());
        if (StringUtils.isNotEmpty(satisfaction)) {
            complaint.setSatisfaction(satisfaction);
        }
        complaint.setUpdateBy(SecurityUtils.getUsername());
        
        return toAjax(sysPropertyComplaintService.updateSysPropertyComplaint(complaint));
    }

    /**
     * 关闭投诉
     */
    @PreAuthorize("@ss.hasPermi('system:complaint:close')")
    @Log(title = "关闭投诉", businessType = BusinessType.UPDATE)
    @PutMapping("/close")
    public AjaxResult closeComplaint(@RequestBody Map<String, Object> params)
    {
        Long complaintId = Long.valueOf(params.get("complaintId").toString());
        
        SysPropertyComplaint complaint = new SysPropertyComplaint();
        complaint.setComplaintId(complaintId);
        complaint.setStatus("3"); // 设置为已关闭
        complaint.setUpdateBy(SecurityUtils.getUsername());
        
        return toAjax(sysPropertyComplaintService.updateSysPropertyComplaint(complaint));
    }

    /**
     * 获取待处理投诉数量
     */
    @GetMapping("/pending/count")
    public AjaxResult getPendingCount()
    {
        return AjaxResult.success(sysPropertyComplaintService.countPendingComplaints());
    }

    /**
     * 获取紧急投诉数量
     */
    @GetMapping("/urgent/count")
    public AjaxResult getUrgentCount()
    {
        return AjaxResult.success(sysPropertyComplaintService.countUrgentComplaints());
    }

    /**
     * 获取投诉趋势数据
     */
    @GetMapping("/trend")
    public AjaxResult getComplaintTrend()
    {
        return AjaxResult.success(sysPropertyComplaintService.getComplaintTrend());
    }

    /**
     * 获取最近投诉记录
     */
    @GetMapping("/recent")
    public AjaxResult getRecentComplaints(@RequestParam(defaultValue = "10") int limit)
    {
        return AjaxResult.success(sysPropertyComplaintService.getRecentComplaints(limit));
    }

    /**
     * 按类型统计投诉
     */
    @GetMapping("/type/stats")
    public AjaxResult getComplaintTypeStats()
    {
        return AjaxResult.success(sysPropertyComplaintService.getComplaintTypeStats());
    }

    /**
     * 按状态统计投诉
     */
    @GetMapping("/status/stats")
    public AjaxResult getComplaintStatusStats()
    {
        return AjaxResult.success(sysPropertyComplaintService.getComplaintStatusStats());
    }

    /**
     * 获取投诉增长率
     */
    @GetMapping("/growth/rate")
    public AjaxResult getComplaintGrowthRate()
    {
        return AjaxResult.success(sysPropertyComplaintService.getComplaintGrowthRate());
    }
}
