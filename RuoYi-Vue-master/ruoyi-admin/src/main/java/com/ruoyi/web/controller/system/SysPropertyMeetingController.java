package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.service.MeetingNoticeService;
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
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.service.ISysPropertyMeetingService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.WechatService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import java.text.SimpleDateFormat;

/**
 * 会议管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/meeting")
public class SysPropertyMeetingController extends BaseController
{
    @Autowired
    private ISysPropertyMeetingService sysPropertyMeetingService;
    @Autowired
    MeetingNoticeService meetingNoticeService;

    /**
     * 查询会议管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyMeeting sysPropertyMeeting)
    {
        sysPropertyMeeting.setCommunityId(resolveCommunityId(sysPropertyMeeting.getCommunityId()));
        try {
            sysPropertyMeeting.getParams().put("userId", getUserId());
        } catch (Exception e) {
            logger.warn("获取用户ID失败，无法计算投票状态: {}", e.getMessage());
        }
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
        sysPropertyMeeting.setCommunityId(resolveCommunityId(sysPropertyMeeting.getCommunityId()));
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
        sysPropertyMeeting.setCommunityId(requireResolvedCommunityId(sysPropertyMeeting.getCommunityId(), "请选择小区后再新增会议"));
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
        sysPropertyMeeting.setCommunityId(requireResolvedCommunityId(sysPropertyMeeting.getCommunityId(), "请选择小区后再修改会议"));
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

    /**
     * 发送会议通知
     * 
     * @param meetingId 会议ID
     * @return 发送结果
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:notify')")
    @Log(title = "会议通知", businessType = BusinessType.UPDATE)
    @PostMapping("/notify/{meetingId}")
    public AjaxResult sendMeetingNotification(@PathVariable("meetingId") Long meetingId) {
        return toAjax(meetingNoticeService.notice(meetingId));
    }

    /**
     * 导出表决票（未投票用户）
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:exportBallot')")
    @Log(title = "会议管理", businessType = BusinessType.EXPORT)
    @PostMapping("/exportBallot")
    public void exportBallot(HttpServletResponse response, Long meetingId, String type) throws java.io.IOException
    {
        sysPropertyMeetingService.exportBallot(response, meetingId, type);
    }

    /**
     * 导出投票结果
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:exportVotingResults')")
    @Log(title = "会议管理", businessType = BusinessType.EXPORT)
    @PostMapping("/exportVotingResults")
    public void exportVotingResults(HttpServletResponse response, Long meetingId) throws java.io.IOException
    {
        sysPropertyMeetingService.exportVotingResults(response, meetingId);
    }

    /**
     * 导出投票明细公开表
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:exportVotingDetailsPublic')")
    @Log(title = "会议管理", businessType = BusinessType.EXPORT)
    @PostMapping("/exportVotingDetailsPublic")
    public void exportVotingDetailsPublic(HttpServletResponse response, Long meetingId) throws java.io.IOException
    {
        sysPropertyMeetingService.exportVotingDetailsPublic(response, meetingId);
    }

    /**
     * 导出会议文件
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:exportMeetingDocuments')")
    @Log(title = "会议管理", businessType = BusinessType.EXPORT)
    @PostMapping("/exportMeetingDocuments")
    public void exportMeetingDocuments(HttpServletResponse response, Long meetingId) throws java.io.IOException
    {
        sysPropertyMeetingService.exportMeetingDocuments(response, meetingId);
    }

    /**
     * 获取楼栋投票统计
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:voteRights')")
    @GetMapping("/buildingStats/{meetingId}")
    public AjaxResult getBuildingStats(@PathVariable("meetingId") Long meetingId)
    {
        return success(sysPropertyMeetingService.getBuildingVoteStats(meetingId));
    }

    /**
     * 复制会议
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:copy')")
    @Log(title = "会议管理", businessType = BusinessType.INSERT)
    @PostMapping("/copy/{meetingId}")
    public AjaxResult copy(@PathVariable Long meetingId)
    {
        return toAjax(sysPropertyMeetingService.copyMeeting(meetingId));
    }

    /**
     * 获取会议通知记录
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:smsNotify')")
    @GetMapping("/notificationRecords/{meetingId}")
    public AjaxResult getNotificationRecords(@PathVariable("meetingId") Long meetingId)
    {
        return success(sysPropertyMeetingService.getNotificationRecords(meetingId));
    }

    /**
     * 停止会议
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:stop')")
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    @PutMapping("/stop/{meetingId}")
    public AjaxResult stop(@PathVariable Long meetingId)
    {
        return toAjax(sysPropertyMeetingService.stopMeeting(meetingId));
    }

    /**
     * 查询已删除的会议列表(回收站)
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:list')")
    @GetMapping("/deleted/list")
    public TableDataInfo deletedList(SysPropertyMeeting sysPropertyMeeting)
    {
        startPage();
        List<SysPropertyMeeting> list = sysPropertyMeetingService.selectDeletedMeetingList(sysPropertyMeeting);
        return getDataTable(list);
    }

    /**
     * 恢复已删除的会议
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:edit')")
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    @PutMapping("/restore/{meetingId}")
    public AjaxResult restore(@PathVariable Long meetingId)
    {
        return toAjax(sysPropertyMeetingService.restoreMeeting(meetingId));
    }

    /**
     * 永久删除会议
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:remove')")
    @Log(title = "会议管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/permanent/{meetingId}")
    public AjaxResult permanentDelete(@PathVariable Long meetingId)
    {
        return toAjax(sysPropertyMeetingService.permanentlyDeleteMeeting(meetingId));
    }
}
