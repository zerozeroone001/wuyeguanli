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
import com.ruoyi.system.domain.SysGeneralMeeting;
import com.ruoyi.system.service.ISysGeneralMeetingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 业主大会会议管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/generalMeeting")
public class SysGeneralMeetingController extends BaseController
{
    @Autowired
    private ISysGeneralMeetingService sysGeneralMeetingService;

    /**
     * 查询业主大会会议管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:generalMeeting:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysGeneralMeeting sysGeneralMeeting)
    {
        startPage();
        List<SysGeneralMeeting> list = sysGeneralMeetingService.selectSysGeneralMeetingList(sysGeneralMeeting);
        return getDataTable(list);
    }

    /**
     * 导出业主大会会议管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:generalMeeting:export')")
    @Log(title = "业主大会会议管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysGeneralMeeting sysGeneralMeeting)
    {
        List<SysGeneralMeeting> list = sysGeneralMeetingService.selectSysGeneralMeetingList(sysGeneralMeeting);
        ExcelUtil<SysGeneralMeeting> util = new ExcelUtil<SysGeneralMeeting>(SysGeneralMeeting.class);
        util.exportExcel(response, list, "业主大会会议管理数据");
    }

    /**
     * 获取业主大会会议管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:generalMeeting:query')")
    @GetMapping(value = "/{meetingId}")
    public AjaxResult getInfo(@PathVariable("meetingId") Long meetingId)
    {
        return success(sysGeneralMeetingService.selectSysGeneralMeetingByMeetingId(meetingId));
    }

    /**
     * 新增业主大会会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:generalMeeting:add')")
    @Log(title = "业主大会会议管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysGeneralMeeting sysGeneralMeeting)
    {
        return toAjax(sysGeneralMeetingService.insertSysGeneralMeeting(sysGeneralMeeting));
    }

    /**
     * 修改业主大会会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:generalMeeting:edit')")
    @Log(title = "业主大会会议管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysGeneralMeeting sysGeneralMeeting)
    {
        return toAjax(sysGeneralMeetingService.updateSysGeneralMeeting(sysGeneralMeeting));
    }

    /**
     * 删除业主大会会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:generalMeeting:remove')")
    @Log(title = "业主大会会议管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{meetingIds}")
    public AjaxResult remove(@PathVariable Long[] meetingIds)
    {
        return toAjax(sysGeneralMeetingService.deleteSysGeneralMeetingByMeetingIds(meetingIds));
    }
}
