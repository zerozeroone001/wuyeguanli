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
import com.ruoyi.system.domain.SysPropertyComplaint;
import com.ruoyi.system.service.ISysPropertyComplaintService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 投诉管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/complaint")
public class SysPropertyComplaintController extends BaseController
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
        sysPropertyComplaint.setCommunityId(resolveCommunityId(sysPropertyComplaint.getCommunityId()));
        startPage();
        List<SysPropertyComplaint> list = sysPropertyComplaintService.selectSysPropertyComplaintList(sysPropertyComplaint);
        return getDataTable(list);
    }

    /**
     * 导出投诉管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:complaint:export')")
    @Log(title = "投诉管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyComplaint sysPropertyComplaint)
    {
        sysPropertyComplaint.setCommunityId(resolveCommunityId(sysPropertyComplaint.getCommunityId()));
        List<SysPropertyComplaint> list = sysPropertyComplaintService.selectSysPropertyComplaintList(sysPropertyComplaint);
        ExcelUtil<SysPropertyComplaint> util = new ExcelUtil<SysPropertyComplaint>(SysPropertyComplaint.class);
        util.exportExcel(response, list, "投诉管理数据");
    }

    /**
     * 获取投诉管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:complaint:query')")
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
        sysPropertyComplaint.setCommunityId(requireResolvedCommunityId(sysPropertyComplaint.getCommunityId(), "请选择小区后再新增投诉"));
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
        sysPropertyComplaint.setCommunityId(requireResolvedCommunityId(sysPropertyComplaint.getCommunityId(), "请选择小区后再修改投诉"));
        return toAjax(sysPropertyComplaintService.updateSysPropertyComplaint(sysPropertyComplaint));
    }

    /**
     * 删除投诉管理
     */
    @PreAuthorize("@ss.hasPermi('system:complaint:remove')")
    @Log(title = "投诉管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{complaintIds}")
    public AjaxResult remove(@PathVariable Long[] complaintIds)
    {
        return toAjax(sysPropertyComplaintService.deleteSysPropertyComplaintByComplaintIds(complaintIds));
    }
}
