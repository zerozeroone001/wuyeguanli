package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.domain.vo.MeetingVoteReportVO;
import com.ruoyi.system.domain.vo.VoteListExportVO;
import com.ruoyi.system.domain.vo.VoteResultVO;
import com.ruoyi.system.mapper.SysMeetingVoteMapper;
import com.ruoyi.system.mapper.SysPropertyMeetingMapper;
import com.ruoyi.system.service.IMeetingExportService;
import com.ruoyi.system.utils.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 会议导出服务实现类
 *
 * @author ruoyi
 * @date 2025-11-27
 */
@Service
public class MeetingExportServiceImpl implements IMeetingExportService {

    private static final Logger log = LoggerFactory.getLogger(MeetingExportServiceImpl.class);

    @Autowired
    private SysMeetingVoteMapper meetingVoteMapper;

    @Autowired
    private SysPropertyMeetingMapper propertyMeetingMapper;

    /**
     * 导出投票列表为Excel
     *
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @param response HTTP响应对象
     */
    @Override
    public void exportVoteListToExcel(Long meetingId, Long communityId, HttpServletResponse response) {
        try {
            // 1. 查询会议信息
            SysPropertyMeeting meeting = propertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
            if (meeting == null) {
                log.error("会议不存在，meetingId: {}", meetingId);
                throw new RuntimeException("会议不存在");
            }

            // 2. 查询投票列表数据
            List<VoteListExportVO> voteList = meetingVoteMapper.selectVoteListForExport(meetingId, communityId);

            // 3. 使用ExcelUtil导出
            ExcelUtil<VoteListExportVO> util = new ExcelUtil<>(VoteListExportVO.class);

            // 4. 设置文件名
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = meeting.getMeetingTitle() + "_投票列表_" + sdf.format(new Date());

            // 5. 导出Excel
            util.exportExcel(response, voteList, fileName);

            log.info("成功导出投票列表Excel，meetingId: {}, 记录数: {}", meetingId, voteList.size());

        } catch (Exception e) {
            log.error("导出投票列表Excel失败", e);
            throw new RuntimeException("导出Excel失败: " + e.getMessage());
        }
    }

    /**
     * 导出投票统计报告为PDF
     *
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @param response HTTP响应对象
     */
    @Override
    public void exportVoteReportToPdf(Long meetingId, Long communityId, HttpServletResponse response) {
        OutputStream out = null;
        try {
            // 1. 查询会议基本信息和统计数据
            MeetingVoteReportVO reportData = propertyMeetingMapper.selectMeetingVoteReportData(meetingId, communityId);
            if (reportData == null) {
                log.error("会议不存在，meetingId: {}", meetingId);
                throw new RuntimeException("会议不存在");
            }

            // 2. 查询表决结果统计
            List<VoteResultVO> voteResults = meetingVoteMapper.selectVoteResultsByMeeting(meetingId, communityId);
            if (voteResults == null || voteResults.isEmpty()) {
                log.warn("表决结果为空，meetingId: {}", meetingId);
                throw new RuntimeException("表决结果为空，无法生成报告");
            }

            // 3. 设置导出时间和议题结果
            reportData.setExportTime(new Date());
            reportData.setTopicResults(voteResults);

            // 4. 生成PDF
            byte[] pdfBytes = PdfUtils.generateVoteReportPdf(reportData);

            // 5. 设置响应头
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = reportData.getMeetingTitle() + "_表决报告_" + sdf.format(new Date()) + ".pdf";
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            response.setContentLength(pdfBytes.length);

            // 6. 输出PDF
            out = response.getOutputStream();
            out.write(pdfBytes);
            out.flush();

            log.info("成功导出投票统计报告PDF，meetingId: {}, 议题数: {}", meetingId, voteResults.size());

        } catch (Exception e) {
            log.error("导出投票统计报告PDF失败", e);
            throw new RuntimeException("导出PDF失败: " + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("关闭输出流失败", e);
                }
            }
        }
    }
}
