package com.ruoyi.ruoyiuser.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysCommitteeMeeting;
import com.ruoyi.system.service.ISysCommitteeMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 业主委员会会议管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/committeeMeeting")
public class SysCommitteeMeetingController extends BaseController
{
    @Autowired
    private ISysCommitteeMeetingService sysCommitteeMeetingService;

    /**
     * 查询业主委员会会议管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:committeeMeeting:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCommitteeMeeting sysCommitteeMeeting)
    {
        startPage();
        List<SysCommitteeMeeting> list = sysCommitteeMeetingService.selectSysCommitteeMeetingList(sysCommitteeMeeting);
        return getDataTable(list);
    }

    /**
     * 导出业主委员会会议管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:committeeMeeting:export')")
    @Log(title = "业主委员会会议管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCommitteeMeeting sysCommitteeMeeting)
    {
        List<SysCommitteeMeeting> list = sysCommitteeMeetingService.selectSysCommitteeMeetingList(sysCommitteeMeeting);
        ExcelUtil<SysCommitteeMeeting> util = new ExcelUtil<SysCommitteeMeeting>(SysCommitteeMeeting.class);
        util.exportExcel(response, list, "业主委员会会议管理数据");
    }

    /**
     * 获取业主委员会会议管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:committeeMeeting:query')")
    @GetMapping(value = "/{meetingId}")
    public AjaxResult getInfo(@PathVariable("meetingId") Long meetingId)
    {
        return success(sysCommitteeMeetingService.selectSysCommitteeMeetingByMeetingId(meetingId));
    }

    /**
     * 新增业主委员会会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:committeeMeeting:add')")
    @Log(title = "业主委员会会议管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCommitteeMeeting sysCommitteeMeeting)
    {
        return toAjax(sysCommitteeMeetingService.insertSysCommitteeMeeting(sysCommitteeMeeting));
    }

    /**
     * 修改业主委员会会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:committeeMeeting:edit')")
    @Log(title = "业主委员会会议管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCommitteeMeeting sysCommitteeMeeting)
    {
        return toAjax(sysCommitteeMeetingService.updateSysCommitteeMeeting(sysCommitteeMeeting));
    }

    /**
     * 删除业主委员会会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:committeeMeeting:remove')")
    @Log(title = "业主委员会会议管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{meetingIds}")
    public AjaxResult remove(@PathVariable Long[] meetingIds)
    {
        return toAjax(sysCommitteeMeetingService.deleteSysCommitteeMeetingByMeetingIds(meetingIds));
    }
}
