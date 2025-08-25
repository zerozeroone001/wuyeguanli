package com.ruoyi.user.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.service.ISysMeetingTopicService;
import com.ruoyi.system.service.ISysPropertyMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 会议管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/meeting")
public class PropertyMeetingController extends BaseController
{
    @Autowired
    private ISysPropertyMeetingService sysPropertyMeetingService;

    /**
     * 查询会议管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyMeeting sysPropertyMeeting)
    {
        startPage();
        List<SysPropertyMeeting> list = sysPropertyMeetingService.selectSysPropertyMeetingList(sysPropertyMeeting);
        return getDataTable(list);
    }

    /**
     * 导出会议管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:export')")
    @Log(title = "会议管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyMeeting sysPropertyMeeting)
    {
        List<SysPropertyMeeting> list = sysPropertyMeetingService.selectSysPropertyMeetingList(sysPropertyMeeting);
        ExcelUtil<SysPropertyMeeting> util = new ExcelUtil<SysPropertyMeeting>(SysPropertyMeeting.class);
        util.exportExcel(response, list, "会议管理数据");
    }

    /**
     * 获取会议管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:query')")
    @GetMapping(value = "/{meetingId}")
    public AjaxResult getInfo(@PathVariable("meetingId") Long meetingId)
    {
        return success(sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(meetingId));
    }

    /**
     * 新增会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:add')")
    @Log(title = "会议管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPropertyMeeting sysPropertyMeeting)
    {
        return toAjax(sysPropertyMeetingService.insertSysPropertyMeeting(sysPropertyMeeting));
    }

    /**
     * 修改会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:edit')")
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPropertyMeeting sysPropertyMeeting)
    {
        return toAjax(sysPropertyMeetingService.updateSysPropertyMeeting(sysPropertyMeeting));
    }

    /**
     * 删除会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:remove')")
    @Log(title = "会议管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{meetingIds}")
    public AjaxResult remove(@PathVariable Long[] meetingIds)
    {
        return toAjax(sysPropertyMeetingService.deleteSysPropertyMeetingByMeetingIds(meetingIds));
    }

    @GetMapping("/marks")
    public AjaxResult getMeetingMarks() {
        return AjaxResult.success(sysPropertyMeetingService.getMeetingMarks());
    }


    @Autowired
    private ISysMeetingTopicService meetingTopicService;

        @GetMapping("/{meetingId}/topics")
    public AjaxResult getMeetingTopics(@PathVariable("meetingId") Long meetingId) {
        return AjaxResult.success(meetingTopicService.selectMeetingTopicList(meetingId));
    }




}
