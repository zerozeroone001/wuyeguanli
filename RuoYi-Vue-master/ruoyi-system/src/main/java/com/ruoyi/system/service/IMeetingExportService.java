package com.ruoyi.system.service;

import javax.servlet.http.HttpServletResponse;

/**
 * 会议导出服务接口
 *
 * @author ruoyi
 * @date 2025-11-27
 */
public interface IMeetingExportService {

    /**
     * 导出投票列表为Excel
     *
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @param response HTTP响应对象
     */
    void exportVoteListToExcel(Long meetingId, Long communityId, HttpServletResponse response);

    /**
     * 导出投票统计报告为PDF
     *
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @param response HTTP响应对象
     */
    void exportVoteReportToPdf(Long meetingId, Long communityId, HttpServletResponse response);
}
