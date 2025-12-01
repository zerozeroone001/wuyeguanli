package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.IMeetingExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 会议导出Controller
 *
 * @author ruoyi
 * @date 2025-11-27
 */
@RestController
@RequestMapping("/system/meeting/export")
public class MeetingExportController extends BaseController {

    @Autowired
    private IMeetingExportService meetingExportService;

    /**
     * 导出投票列表为Excel
     *
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @param response HTTP响应对象
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:export')")
    @Log(title = "导出投票列表", businessType = BusinessType.EXPORT)
    @GetMapping("/voteList")
    public void exportVoteList(
            @RequestParam("meetingId") Long meetingId,
            @RequestParam("communityId") Long communityId,
            HttpServletResponse response) {
        try {
            meetingExportService.exportVoteListToExcel(meetingId, communityId, response);
        } catch (Exception e) {
            logger.error("导出投票列表失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 导出投票统计报告为PDF
     *
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @param response HTTP响应对象
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:export')")
    @Log(title = "导出投票统计报告", businessType = BusinessType.EXPORT)
    @GetMapping("/voteReport")
    public void exportVoteReport(
            @RequestParam("meetingId") Long meetingId,
            @RequestParam("communityId") Long communityId,
            HttpServletResponse response) {
        try {
            meetingExportService.exportVoteReportToPdf(meetingId, communityId, response);
        } catch (Exception e) {
            logger.error("导出投票统计报告失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }
}
