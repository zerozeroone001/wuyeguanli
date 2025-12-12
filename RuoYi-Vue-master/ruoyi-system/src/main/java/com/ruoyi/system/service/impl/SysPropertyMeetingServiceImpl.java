package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CommunityUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysMeetingFeedback;
import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.domain.SysPropertyMeetingTopic;
import com.ruoyi.system.mapper.SysPropertyMeetingTopicMapper;
import com.ruoyi.system.service.ISysMeetingFeedbackService;
import com.ruoyi.system.service.ISysMeetingVoteService;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysPropertyMeetingMapper;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.service.ISysPropertyMeetingService;

/**
 * 会议管理Service业务层处理。
 *
 * <p>围绕多小区权限改造，实现对查询、增删改的社区隔离。</p>
 */
@Service
public class SysPropertyMeetingServiceImpl implements ISysPropertyMeetingService
{
    private static final Logger log = LoggerFactory.getLogger(SysPropertyMeetingServiceImpl.class);

    @Autowired
    private SysPropertyMeetingMapper sysPropertyMeetingMapper;

    @Autowired
    private SysPropertyMeetingTopicMapper sysPropertyMeetingTopicMapper;

    @Autowired
    private ISysMeetingVoteService sysMeetingVoteService;

    @Autowired
    private ISysMeetingFeedbackService sysMeetingFeedbackService;

    @Autowired
    private com.ruoyi.system.mapper.EstateUserPropertyMapper estateUserPropertyMapper;

    @Autowired
    private com.ruoyi.system.mapper.SysMeetingVoteMapper sysMeetingVoteMapper;

    @Autowired
    private com.ruoyi.system.mapper.SysOwnerProfileMapper sysOwnerProfileMapper;

    @Override
    public SysPropertyMeeting selectSysPropertyMeetingByMeetingId(Long meetingId)
    {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting != null)
        {
            CommunityUtils.checkCommunityPermission(meeting.getCommunityId());
            
            // 填充统计数据
            try {
                com.ruoyi.system.domain.vo.MeetingVoteReportVO vo = sysPropertyMeetingMapper.selectMeetingVoteReportData(meeting.getMeetingId(), meeting.getCommunityId());
                if (vo != null) {
                    meeting.setTotalVoters(vo.getTotalOwners() == null ? 0L : vo.getTotalOwners().longValue());
                    meeting.setTotalVotingArea(vo.getTotalArea());
                    meeting.setParticipatedArea(vo.getParticipatedArea());
                    meeting.setVotingAreaPercentage(vo.getAreaParticipationRate() + "%");
                }
            } catch (Exception e) {
                log.error("获取会议统计数据失败: {}", meetingId, e);
            }

            if (StringUtils.isNotEmpty(meeting.getTopics()))
            {
                for (SysPropertyMeetingTopic topic : meeting.getTopics())
                {
                    List<SysMeetingVote> voteList = sysMeetingVoteService.selectSysMeetingVoteListByTopicId(topic.getTopicId());
                    List<SysMeetingFeedback> feedbackList = sysMeetingFeedbackService.selectSysMeetingFeedbackListByTopicId(topic.getTopicId());
                    topic.setVoteList(voteList);
                    topic.setFeedbackList(feedbackList);
                }
            }
        }
        return meeting;
    }

    @Override
    public List<SysPropertyMeeting> selectSysPropertyMeetingList(SysPropertyMeeting sysPropertyMeeting)
    {
        List<SysPropertyMeeting> list = sysPropertyMeetingMapper.selectSysPropertyMeetingList(sysPropertyMeeting);
        for (SysPropertyMeeting meeting : list) {
            try {
                com.ruoyi.system.domain.vo.MeetingVoteReportVO vo = sysPropertyMeetingMapper.selectMeetingVoteReportData(meeting.getMeetingId(), meeting.getCommunityId());
                if (vo != null) {
                    meeting.setTotalVoters(vo.getTotalOwners() == null ? 0L : vo.getTotalOwners().longValue());
                    meeting.setTotalVotingArea(vo.getTotalArea());
                    meeting.setParticipatedArea(vo.getParticipatedArea());
                    meeting.setVotingAreaPercentage(vo.getAreaParticipationRate() + "%");
                }
            } catch (Exception e) {
                log.error("获取会议统计数据失败: {}", meeting.getMeetingId(), e);
            }
        }
        return list;
    }

    @Override
    @Transactional
    public int insertSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting)
    {
        enforceCommunityScope(sysPropertyMeeting.getCommunityId());
        sysPropertyMeeting.setCreateTime(DateUtils.getNowDate());
        int rows = sysPropertyMeetingMapper.insertSysPropertyMeeting(sysPropertyMeeting);
        insertTopics(sysPropertyMeeting);
        
        // 生成初始投票记录
        generateInitialVotes(sysPropertyMeeting);
        
        return rows;
    }

    private void generateInitialVotes(SysPropertyMeeting meeting) {
        Long communityId = meeting.getCommunityId();
        if (communityId == null) return;

        // 1. 获取小区所有业主
        com.ruoyi.system.domain.SysOwnerProfile query = new com.ruoyi.system.domain.SysOwnerProfile();
        query.setCommunityId(communityId);
        List<com.ruoyi.system.domain.SysOwnerProfile> owners = sysOwnerProfileMapper.selectSysOwnerProfileList(query);

        if (owners == null || owners.isEmpty()) return;

        List<SysPropertyMeetingTopic> topics = meeting.getTopics();
        if (topics == null || topics.isEmpty()) return;

        List<SysMeetingVote> voteList = new java.util.ArrayList<>();
        java.util.Set<Long> processedUserIds = new java.util.HashSet<>();

        for (com.ruoyi.system.domain.SysOwnerProfile owner : owners) {
             if (owner.getUserId() == null) continue;
             if (processedUserIds.contains(owner.getUserId())) continue;
             processedUserIds.add(owner.getUserId());

             for (SysPropertyMeetingTopic topic : topics) {
                SysMeetingVote vote = new SysMeetingVote();
                vote.setMeetingId(meeting.getMeetingId());
                vote.setTopicId(topic.getTopicId());
                vote.setUserId(owner.getUserId());
                vote.setUserName(owner.getUserName());
                vote.setCreateTime(DateUtils.getNowDate());
                // voteOption, voteType, flieUrl 默认 null
                voteList.add(vote);
            }
        }

        // 批量插入
        if (!voteList.isEmpty()) {
            // 分批处理，避免一次插入过多
            int batchSize = 1000;
            for (int i = 0; i < voteList.size(); i += batchSize) {
                int end = Math.min(i + batchSize, voteList.size());
                sysMeetingVoteMapper.batchInsertSysMeetingVote(voteList.subList(i, end));
            }
        }
    }

    @Override
    @Transactional
    public int updateSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting)
    {
        enforceCommunityScope(sysPropertyMeeting.getCommunityId());
        sysPropertyMeeting.setUpdateTime(DateUtils.getNowDate());
        int rows = sysPropertyMeetingMapper.updateSysPropertyMeeting(sysPropertyMeeting);
        sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(sysPropertyMeeting.getMeetingId());
        insertTopics(sysPropertyMeeting);
        return rows;
    }

    @Override
    @Transactional
    public int deleteSysPropertyMeetingByMeetingIds(Long[] meetingIds)
    {
        for (Long meetingId : meetingIds)
        {
            SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
            if (meeting != null)
            {
                CommunityUtils.checkCommunityPermission(meeting.getCommunityId());
                sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(meetingId);
            }
        }
        return sysPropertyMeetingMapper.deleteSysPropertyMeetingByMeetingIds(meetingIds);
    }

    @Override
    @Transactional
    public int deleteSysPropertyMeetingByMeetingId(Long meetingId)
    {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting != null)
        {
            CommunityUtils.checkCommunityPermission(meeting.getCommunityId());
        }
        sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(meetingId);
        return sysPropertyMeetingMapper.deleteSysPropertyMeetingByMeetingId(meetingId);
    }

    public void insertTopics(SysPropertyMeeting sysPropertyMeeting)
    {
        List<SysPropertyMeetingTopic> topics = sysPropertyMeeting.getTopics();
        Long meetingId = sysPropertyMeeting.getMeetingId();
        if (StringUtils.isNotEmpty(topics))
        {
            for (SysPropertyMeetingTopic topic : topics)
            {
                topic.setMeetingId(meetingId);
                sysPropertyMeetingTopicMapper.insertSysPropertyMeetingTopic(topic);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getMeetingMarks()
    {
        return sysPropertyMeetingMapper.getMeetingMarks(resolveCommunityIdForStatistics());
    }

    @Override
    public Long countOngoingMeetings()
    {
        return null;
    }

    @Override
    public Double getAverageParticipationRate()
    {
        return null;
    }

    @Override
    public Long countUpcomingMeetings()
    {
        return null;
    }

    @Override
    public List<Map<String, Object>> getVoteParticipationTrend()
    {
        return null;
    }

    @Override
    public List<Map<String, Object>> getMeetingActivityStats()
    {
        return null;
    }

    @Override
    public List<Map<String, Object>> getRecentVotes(int limit)
    {
        return null;
    }

    @Override
    public void sendCommitteeMeetingNotification(Long meetingId)
    {
        log.warn("业主委员会会议通知功能已移除，会议ID: {}", meetingId);
    }

    @Override
    public void sendGeneralMeetingNotification(Long meetingId)
    {
        log.warn("业主大会会议通知功能已移除，会议ID: {}", meetingId);
    }

    private void enforceCommunityScope(Long communityId)
    {
        if (communityId == null)
        {
            throw new ServiceException("会议必须绑定所属小区");
        }
        CommunityUtils.checkCommunityPermission(communityId);
    }

    private Long resolveCommunityIdForStatistics()
    {
        if (CommunityUtils.isCurrentUserAdmin())
        {
            return CommunityUtils.getCurrentCommunityId();
        }
        return CommunityUtils.requireCurrentCommunityId("当前账号未绑定任何小区");
    }

    @Override
    public void exportBallot(javax.servlet.http.HttpServletResponse response, Long meetingId, String type) throws java.io.IOException {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting == null) {
            throw new ServiceException("会议不存在");
        }

        // 导出空白表决票 (模板)
        if ("blank".equals(type)) {
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(meeting.getMeetingTitle() + "_空白表决票.docx", "UTF-8") + "\"");
            
            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource("template.docx");
            if (!resource.exists()) {
                 throw new ServiceException("模板文件 template.docx 不存在，请联系管理员上传");
            }
            
            try (java.io.InputStream is = resource.getInputStream();
                 java.io.OutputStream os = response.getOutputStream()) {
                org.apache.commons.io.IOUtils.copy(is, os);
            }
            return;
        }

        // 导出填充表决票 (未投票用户)
        // 1. 获取已投票用户ID集合
        List<com.ruoyi.system.domain.vo.VoteRecordVO> votedRecords = sysMeetingVoteMapper.selectVoteRecordsList(meetingId, null, null, "1");
        java.util.Set<Long> votedUserIds = new java.util.HashSet<>();
        if (votedRecords != null) {
            for (com.ruoyi.system.domain.vo.VoteRecordVO record : votedRecords) {
                votedUserIds.add(record.getUserId());
            }
        }

        // 2. 获取小区所有业主
        com.ruoyi.system.domain.EstateUserProperty query = new com.ruoyi.system.domain.EstateUserProperty();
        query.setCommunityId(meeting.getCommunityId());
        query.setUserType("00"); // 业主
        List<com.ruoyi.system.domain.EstateUserProperty> allOwners = estateUserPropertyMapper.selectEstateUserPropertyList(query);

        // 3. 过滤未投票用户
        List<com.ruoyi.system.domain.EstateUserProperty> unvotedOwners = new java.util.ArrayList<>();
        for (com.ruoyi.system.domain.EstateUserProperty owner : allOwners) {
            if (!votedUserIds.contains(owner.getUserId())) {
                unvotedOwners.add(owner);
            }
        }
        
        if (unvotedOwners.isEmpty()) {
             response.setContentType("application/json;charset=UTF-8");
             response.getWriter().write("{\"msg\":\"没有未投票的业主\",\"code\":500}");
             return;
        }

        // 4. 准备ZIP输出
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(meeting.getMeetingTitle() + "_未投票表决票.zip", "UTF-8") + "\"");
        
        try (java.util.zip.ZipOutputStream zipOut = new java.util.zip.ZipOutputStream(response.getOutputStream())) {
             // 假设模板在 resources 目录下
             org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource("template.docx");
             if (!resource.exists()) {
                  throw new ServiceException("模板文件 template.docx 不存在，请联系管理员上传");
             }

             // 读取模板到内存，避免重复IO
             java.io.ByteArrayOutputStream templateBaos = new java.io.ByteArrayOutputStream();
             try (java.io.InputStream is = resource.getInputStream()) {
                 byte[] buffer = new byte[1024];
                 int len;
                 while ((len = is.read(buffer)) != -1) {
                     templateBaos.write(buffer, 0, len);
                 }
             }
             byte[] templateBytes = templateBaos.toByteArray();

             for (com.ruoyi.system.domain.EstateUserProperty owner : unvotedOwners) {
                 try (java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(templateBytes);
                      org.apache.poi.xwpf.usermodel.XWPFDocument doc = new org.apache.poi.xwpf.usermodel.XWPFDocument(bais)) {
                      
                      Map<String, String> params = new java.util.HashMap<>();
                      String name = owner.getRealName();
                      if (StringUtils.isEmpty(name)) name = owner.getUserName();
                      if (StringUtils.isEmpty(name)) name = "业主";
                      
                      params.put("@name", name);
                      params.put("{name}", name);
                      params.put("${name}", name);
                      
                      String roomInfo = (owner.getBuildingName() != null ? owner.getBuildingName() : "") + 
                                        (owner.getUnitName() != null ? owner.getUnitName() : "") + 
                                        (owner.getRoomNumber() != null ? owner.getRoomNumber() : "");
                      params.put("@room", roomInfo);
                      params.put("{room}", roomInfo);
                      params.put("${room}", roomInfo);
                      
                      String areaInfo = owner.getArea() != null ? owner.getArea().toString() : "0";
                      params.put("@area", areaInfo);
                      params.put("{area}", areaInfo);
                      params.put("${area}", areaInfo);
                      
                      String dateInfo = DateUtils.getDate();
                      params.put("@date", dateInfo);
                      params.put("{date}", dateInfo);
                      params.put("${date}", dateInfo);
                      
                      String titleInfo = meeting.getMeetingTitle();
                      params.put("@title", titleInfo);
                      params.put("{title}", titleInfo);
                      params.put("${title}", titleInfo);

                      insertTopicsIntoDoc(doc, meeting.getTopics());
                      replaceTextInDoc(doc, params);

                      String safeName = name.replaceAll("[\\\\/:*?\"<>|]", "_");
                      String fileName = safeName + "_" + (owner.getRoomNumber()!=null?owner.getRoomNumber():"") + ".docx";
                      zipOut.putNextEntry(new java.util.zip.ZipEntry(fileName));
                      doc.write(zipOut);
                      zipOut.closeEntry();
                 } catch (Exception e) {
                     log.error("生成用户 {} 表决票失败", owner.getUserName(), e);
                 }
             }
        }
    }

    @Override
    public void exportVotingResults(javax.servlet.http.HttpServletResponse response, Long meetingId) throws java.io.IOException {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting == null) {
            throw new ServiceException("会议不存在");
        }
        CommunityUtils.checkCommunityPermission(meeting.getCommunityId());

        // 1. 获取基础统计数据
        com.ruoyi.system.domain.vo.MeetingVoteReportVO summary = sysPropertyMeetingMapper.selectMeetingVoteReportData(meeting.getMeetingId(), meeting.getCommunityId());
        
        // 2. 获取详细议题投票结果
        List<com.ruoyi.system.domain.vo.VoteResultVO> topicResults = sysMeetingVoteMapper.selectVoteResultsByMeeting(meeting.getMeetingId(), meeting.getCommunityId());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(meeting.getMeetingTitle() + "_投票结果报告.pdf", "UTF-8") + "\"");

        com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
        try {
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // 设置字体 (使用 iTextAsian 支持中文)
            com.itextpdf.text.pdf.BaseFont bfChinese = com.itextpdf.text.pdf.BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", com.itextpdf.text.pdf.BaseFont.NOT_EMBEDDED);
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(bfChinese, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(bfChinese, 14, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(bfChinese, 12, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(bfChinese, 12, com.itextpdf.text.Font.BOLD);

            // 1. 标题
            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph(meeting.getMeetingTitle() + " - 投票结果报告", titleFont);
            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // 2. 会议基本信息
            document.add(new com.itextpdf.text.Paragraph("一、会议基本信息", headerFont));
            document.add(new com.itextpdf.text.Paragraph(" ", normalFont)); // 空行

            com.itextpdf.text.pdf.PdfPTable basicTable = new com.itextpdf.text.pdf.PdfPTable(2);
            basicTable.setWidthPercentage(100);
            basicTable.setSpacingAfter(10);
            basicTable.setWidths(new float[]{3f, 7f});

            addCell(basicTable, "会议名称", boldFont);
            addCell(basicTable, meeting.getMeetingTitle(), normalFont);
            addCell(basicTable, "所属小区", boldFont);
            addCell(basicTable, meeting.getCommunityName(), normalFont);
            addCell(basicTable, "会议地点", boldFont);
            addCell(basicTable, meeting.getMeetingLocation(), normalFont);
            addCell(basicTable, "会议时间", boldFont);
            addCell(basicTable, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, meeting.getMeetingTime()), normalFont);
            addCell(basicTable, "投票起止时间", boldFont);
            addCell(basicTable, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, meeting.getVoteStartTime()) + " 至 " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, meeting.getVoteEndTime()), normalFont);

            document.add(basicTable);

            // 3. 总体参与情况
            document.add(new com.itextpdf.text.Paragraph("二、总体参与情况", headerFont));
            document.add(new com.itextpdf.text.Paragraph(" ", normalFont));

            com.itextpdf.text.pdf.PdfPTable summaryTable = new com.itextpdf.text.pdf.PdfPTable(4);
            summaryTable.setWidthPercentage(100);
            summaryTable.setSpacingAfter(20);
            
            if (summary != null) {
                addCell(summaryTable, "总户数", boldFont);
                addCell(summaryTable, String.valueOf(summary.getTotalOwners()) + " 户", normalFont);
                addCell(summaryTable, "总建筑面积", boldFont);
                addCell(summaryTable, summary.getTotalArea() + " m²", normalFont);

                addCell(summaryTable, "参与投票户数", boldFont);
                addCell(summaryTable, String.valueOf(summary.getParticipatedOwners()) + " 户", normalFont);
                addCell(summaryTable, "参与投票面积", boldFont);
                addCell(summaryTable, summary.getParticipatedArea() + " m²", normalFont);

                addCell(summaryTable, "户数参与率", boldFont);
                addCell(summaryTable, summary.getOwnerParticipationRate() + "%", normalFont);
                addCell(summaryTable, "面积参与率", boldFont);
                addCell(summaryTable, summary.getAreaParticipationRate() + "%", normalFont);
            } else {
                com.itextpdf.text.pdf.PdfPCell noDataCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("暂无统计数据", normalFont));
                noDataCell.setColspan(4);
                noDataCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                summaryTable.addCell(noDataCell);
            }
            document.add(summaryTable);

            // 4. 议题表决结果详情
            document.add(new com.itextpdf.text.Paragraph("三、议题表决结果详情", headerFont));
            document.add(new com.itextpdf.text.Paragraph(" ", normalFont));

            if (topicResults != null && !topicResults.isEmpty()) {
                int index = 1;
                for (com.ruoyi.system.domain.vo.VoteResultVO topic : topicResults) {
                    document.add(new com.itextpdf.text.Paragraph(index + ". 议题：" + topic.getTopicTitle(), boldFont));
                    document.add(new com.itextpdf.text.Paragraph(" ", normalFont));

                    com.itextpdf.text.pdf.PdfPTable topicTable = new com.itextpdf.text.pdf.PdfPTable(5);
                    topicTable.setWidthPercentage(100);
                    topicTable.setWidths(new float[]{2f, 2f, 2f, 2f, 2f});
                    topicTable.setSpacingAfter(15);

                    // 表头
                    addCell(topicTable, "选项", boldFont);
                    addCell(topicTable, "票数", boldFont);
                    addCell(topicTable, "面积(m²)", boldFont);
                    addCell(topicTable, "人数占比", boldFont);
                    addCell(topicTable, "面积占比", boldFont);

                    // 同意
                    addCell(topicTable, "同意", normalFont);
                    addCell(topicTable, String.valueOf(topic.getAgreePeople()), normalFont);
                    addCell(topicTable, String.valueOf(topic.getAgreeArea()), normalFont);
                    addCell(topicTable, topic.getAgreePeopleRate() + "%", normalFont);
                    addCell(topicTable, topic.getAgreeAreaRate() + "%", normalFont);

                    // 反对
                    addCell(topicTable, "反对", normalFont);
                    addCell(topicTable, String.valueOf(topic.getOpposePeople()), normalFont);
                    addCell(topicTable, String.valueOf(topic.getOpposeArea()), normalFont);
                    addCell(topicTable, "-", normalFont);
                    addCell(topicTable, "-", normalFont);

                    // 弃权
                    addCell(topicTable, "弃权", normalFont);
                    addCell(topicTable, String.valueOf(topic.getAbstainPeople()), normalFont);
                    addCell(topicTable, String.valueOf(topic.getAbstainArea()), normalFont);
                    addCell(topicTable, "-", normalFont);
                    addCell(topicTable, "-", normalFont);
                    
                    document.add(topicTable);
                    
                    // 结果判定建议
                    String passStatus = Boolean.TRUE.equals(topic.getIsPassed()) ? "通过" : "未通过/待定";
                    com.itextpdf.text.Paragraph statusPara = new com.itextpdf.text.Paragraph("系统判定结果：" + passStatus, normalFont);
                    statusPara.setSpacingAfter(10);
                    document.add(statusPara);

                    index++;
                }
            } else {
                document.add(new com.itextpdf.text.Paragraph("暂无议题数据", normalFont));
            }

            // 5. 底部信息
            document.add(new com.itextpdf.text.Paragraph(" ", normalFont));
            com.itextpdf.text.Paragraph footer = new com.itextpdf.text.Paragraph("导出时间：" + DateUtils.getTime(), normalFont);
            footer.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            document.add(footer);

        } catch (com.itextpdf.text.DocumentException e) {
            log.error("PDF生成失败", e);
            throw new java.io.IOException("PDF生成失败");
        } finally {
            document.close();
        }
    }

    private void addCell(com.itextpdf.text.pdf.PdfPTable table, String text, com.itextpdf.text.Font font) {
        com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(text != null ? text : "", font));
        cell.setPadding(5);
        cell.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    @Override
    public void exportVotingDetailsPublic(javax.servlet.http.HttpServletResponse response, Long meetingId) throws java.io.IOException {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting == null) {
            throw new ServiceException("会议不存在");
        }
        CommunityUtils.checkCommunityPermission(meeting.getCommunityId());

        // 获取所有投票记录 (1=已投票)
        List<com.ruoyi.system.domain.vo.VoteRecordVO> records = sysMeetingVoteMapper.selectVoteRecordsList(meetingId, null, null, "1");

        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("投票明细(公示)");
            
            // 标题样式
            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // 标题行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"房号", "建筑面积(m²)", "业主姓名(脱敏)", "投票选项", "投票时间"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 数据行
            int rowIndex = 1;
            if (records != null) {
                for (com.ruoyi.system.domain.vo.VoteRecordVO record : records) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                    
                    // 房号
                    row.createCell(0).setCellValue(record.getRoomNumber());
                    
                    // 面积
                    row.createCell(1).setCellValue(record.getArea() != null ? record.getArea().toString() : "0");
                    
                    // 姓名脱敏 (张三 -> 张*)
                    String name = record.getRealName();
                    if (StringUtils.isEmpty(name)) name = record.getUserName();
                    if (StringUtils.isNotEmpty(name) && name.length() > 1) {
                        name = name.substring(0, 1) + StringUtils.repeat("*", name.length() - 1);
                    }
                    row.createCell(2).setCellValue(name);
                    
                    // 投票选项
                    String option = "未知";
                    if (record.getVoteOption() != null) {
                        switch (record.getVoteOption()) {
                            case 0: option = "同意"; break;
                            case 1: option = "反对"; break;
                            case 2: option = "弃权"; break;
                        }
                    }
                    row.createCell(3).setCellValue(option);
                    
                    // 时间
                    row.createCell(4).setCellValue(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, record.getParticipationTime()));
                }
            }
            
             // 自动列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(meeting.getMeetingTitle() + "_投票明细公示表.xlsx", "UTF-8") + "\"");
            workbook.write(response.getOutputStream());
        }
    }

    @Override
    public void exportMeetingDocuments(javax.servlet.http.HttpServletResponse response, Long meetingId) throws java.io.IOException {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting == null) {
            throw new ServiceException("会议不存在");
        }
        CommunityUtils.checkCommunityPermission(meeting.getCommunityId());

        // 收集文件路径
        List<String> filePaths = new java.util.ArrayList<>();
        
        // 1. 议题附件
        if (meeting.getTopics() != null) {
            for (SysPropertyMeetingTopic topic : meeting.getTopics()) {
                if (StringUtils.isNotEmpty(topic.getFiles())) {
                    String[] files = topic.getFiles().split(",");
                    for (String file : files) {
                        if (StringUtils.isNotEmpty(file)) {
                            filePaths.add(file.trim());
                        }
                    }
                }
            }
        }
        
        // 2. 会议封面 (可选)
        if (StringUtils.isNotEmpty(meeting.getCoverImage())) {
            filePaths.add(meeting.getCoverImage().trim());
        }

        if (filePaths.isEmpty()) {
             response.setContentType("application/json;charset=UTF-8");
             response.getWriter().write("{\"msg\":\"该会议没有相关文件\",\"code\":500}");
             return;
        }

        response.reset();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(meeting.getMeetingTitle() + "_会议文件.zip", "UTF-8") + "\"");
        // 允许跨域，防止前端某些框架下无法下载
        response.setHeader("Access-Control-Allow-Origin", "*");

        StringBuilder errorLog = new StringBuilder();
        errorLog.append("文件导出日志:\n");

        try (java.util.zip.ZipOutputStream zipOut = new java.util.zip.ZipOutputStream(response.getOutputStream())) {
            // 防止文件名重复
            java.util.Set<String> entryNames = new java.util.HashSet<>();
            boolean hasFile = false;
            
            for (String filePath : filePaths) {
                java.io.InputStream fis = null;
                String fileName = null;
                
                try {
                    // 判断是否为远程文件 (HTTP/HTTPS)
                    if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
                        log.info("正在下载远程文件: {}", filePath);
                        java.net.URL url = new java.net.URL(filePath);
                        java.net.URLConnection conn = url.openConnection();
                        // 设置User-Agent，防止被拦截
                        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                        conn.setConnectTimeout(10000); // 10秒连接超时
                        conn.setReadTimeout(60000);    // 60秒读取超时
                        fis = conn.getInputStream();
                        
                        // 从URL提取文件名
                        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                        if (fileName.contains("?")) {
                            fileName = fileName.substring(0, fileName.indexOf("?"));
                        }
                        // URL解码文件名
                        try {
                            fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
                        } catch (Exception e) {
                            // ignore
                        }
                    } else {
                        // 处理本地文件
                        String localPath = null;
                        if (filePath.contains("/profile")) {
                            localPath = com.ruoyi.common.config.RuoYiConfig.getProfile() + StringUtils.substringAfter(filePath, "/profile");
                        } else {
                            localPath = com.ruoyi.common.config.RuoYiConfig.getProfile() + filePath;
                        }

                        localPath = localPath.replace("/", java.io.File.separator).replace("\\", java.io.File.separator);
                        java.io.File file = new java.io.File(localPath);
                        
                        if (file.exists()) {
                            fis = new java.io.FileInputStream(file);
                            fileName = file.getName();
                        } else {
                            errorLog.append("本地文件不存在: ").append(localPath).append("\n");
                        }
                    }

                    // 如果成功获取到流和文件名，写入ZIP
                    if (fis != null && StringUtils.isNotEmpty(fileName)) {
                        // 重名处理
                        int count = 1;
                        String originalFileName = fileName;
                        while (entryNames.contains(fileName)) {
                            String name = com.ruoyi.common.utils.file.FileUtils.getName(originalFileName);
                            String ext = "." + com.ruoyi.common.utils.StringUtils.substringAfterLast(originalFileName, ".");
                            if (ext.equals(".")) ext = "";
                            fileName = name + "(" + count++ + ")" + ext;
                        }
                        entryNames.add(fileName);
                        
                        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(fileName);
                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[4096];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        zipOut.closeEntry();
                        hasFile = true;
                    }
                } catch (Exception e) {
                    log.error("添加文件到ZIP失败: {}", filePath, e);
                    errorLog.append("导出失败: ").append(filePath).append(" - 原因: ").append(e.getMessage()).append("\n");
                } finally {
                    if (fis != null) {
                        try { fis.close(); } catch (Exception e) {}
                    }
                }
            }
            
            // 总是写入日志文件，方便排查
            if (errorLog.length() > 10) { // "文件导出日志:\n".length() > 10
                java.util.zip.ZipEntry logEntry = new java.util.zip.ZipEntry("error_log.txt");
                zipOut.putNextEntry(logEntry);
                zipOut.write(errorLog.toString().getBytes("UTF-8"));
                zipOut.closeEntry();
            }
            
            if (!hasFile) {
                java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("readme.txt");
                zipOut.putNextEntry(zipEntry);
                zipOut.write("未找到任何可导出的文件。请查看 error_log.txt 了解详情。".getBytes("UTF-8"));
                zipOut.closeEntry();
            }
        }
    }

    @Override
    public List<Map<String, Object>> getBuildingVoteStats(Long meetingId) {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting == null) {
            throw new ServiceException("会议不存在");
        }
        // 检查权限
        CommunityUtils.checkCommunityPermission(meeting.getCommunityId());

        List<Map<String, Object>> stats = sysPropertyMeetingMapper.getBuildingVoteStats(meetingId, meeting.getCommunityId());
        
        // 计算百分比
        for (Map<String, Object> item : stats) {
            Number totalNum = (Number) item.get("totalHouseholds");
            Number votedNum = (Number) item.get("votedHouseholds");
            long total = totalNum != null ? totalNum.longValue() : 0L;
            long voted = votedNum != null ? votedNum.longValue() : 0L;
            
            if (total > 0) {
                double percent = (double) voted * 100 / total;
                item.put("votePercentage", String.format("%.2f%%", percent));
            } else {
                item.put("votePercentage", "0.00%");
            }
            // 确保前端能拿到数值类型，方便可能的排序
            item.put("totalHouseholds", total);
            item.put("votedHouseholds", voted);
        }
        return stats;
    }

    @Override
    @Transactional
    public int copyMeeting(Long meetingId) {
        SysPropertyMeeting origin = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (origin == null) {
            throw new ServiceException("会议不存在");
        }
        // 检查权限
        CommunityUtils.checkCommunityPermission(origin.getCommunityId());

        // 1. 复制会议基本信息
        SysPropertyMeeting copy = new SysPropertyMeeting();
        copy.setCommunityId(origin.getCommunityId());
        copy.setMeetingTitle(origin.getMeetingTitle() + " (副本)");
        copy.setMeetingType(origin.getMeetingType());
        copy.setMeetingContent(origin.getMeetingContent());
        copy.setMeetingLocation(origin.getMeetingLocation());
        copy.setCoverImage(origin.getCoverImage());
        copy.setMeetingTime(origin.getMeetingTime());
        
        // 重置状态和时间 (0=未开始)
        copy.setMeetingStatus("0"); 
        copy.setCreateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());
        copy.setCreateTime(DateUtils.getNowDate());
        
        // 清空投票时间
        copy.setVoteStartTime(null);
        copy.setVoteEndTime(null);
        
        int rows = sysPropertyMeetingMapper.insertSysPropertyMeeting(copy);
        
        // 2. 复制议题
        SysPropertyMeetingTopic topicQuery = new SysPropertyMeetingTopic();
        topicQuery.setMeetingId(meetingId);
        List<SysPropertyMeetingTopic> topics = sysPropertyMeetingTopicMapper.selectSysPropertyMeetingTopicList(topicQuery);
        
        if (topics != null && !topics.isEmpty()) {
            for (SysPropertyMeetingTopic originTopic : topics) {
                SysPropertyMeetingTopic copyTopic = new SysPropertyMeetingTopic();
                copyTopic.setMeetingId(copy.getMeetingId()); // 使用新生成的ID
                copyTopic.setTopicTitle(originTopic.getTopicTitle());
                copyTopic.setTopicContent(originTopic.getTopicContent());
                // 复制文件路径
                copyTopic.setFiles(originTopic.getFiles());
                copyTopic.setCongduo(originTopic.getCongduo());
                
                copyTopic.setCreateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());
                copyTopic.setCreateTime(DateUtils.getNowDate());
                
                sysPropertyMeetingTopicMapper.insertSysPropertyMeetingTopic(copyTopic);
            }
        }
        
        return rows;
    }

    private void insertTopicsIntoDoc(org.apache.poi.xwpf.usermodel.XWPFDocument doc, List<SysPropertyMeetingTopic> topics) {
        if (topics == null || topics.isEmpty()) return;

        // 策略：寻找包含 "@topic" 文本的表格行，作为模板行
        org.apache.poi.xwpf.usermodel.XWPFTable targetTable = null;
        org.apache.poi.xwpf.usermodel.XWPFTableRow templateRow = null;
        int templateRowIndex = -1;

        for (org.apache.poi.xwpf.usermodel.XWPFTable table : doc.getTables()) {
            for (int i = 0; i < table.getRows().size(); i++) {
                org.apache.poi.xwpf.usermodel.XWPFTableRow row = table.getRow(i);
                for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : row.getTableCells()) {
                    if (cell.getText().contains("@topic")) {
                        targetTable = table;
                        templateRow = row;
                        templateRowIndex = i;
                        break;
                    }
                }
                if (targetTable != null) break;
            }
            if (targetTable != null) break;
        }

        if (targetTable != null && templateRow != null) {
            // 使用模板行策略
            // 如果只有一个议题，直接替换模板行中的 @topic
            if (topics.size() == 1) {
                replaceInRow(templateRow, "@topic", topics.get(0).getTopicTitle());
            } else {
                // 如果有多个议题，保留模板行在最后，先插入前面的
                // 为了保持样式，我们应该复制模板行。但POI复制行比较复杂。
                // 简化策略：复用模板行给最后一个议题，前面的议题插入新行（样式可能丢失，但内容在）
                // 或者：每次都插入新行，第一列设置内容。
                
                // 更好的策略：
                // 1. 对于前 N-1 个议题，在模板行之前插入新行
                // 2. 将模板行的内容（包括可能的勾选框）复制到新行（简易复制）
                // 3. 替换新行中的 @topic
                // 4. 最后一个议题使用模板行本身
                
                // 但由于 POI 3.x/4.x 的 insertNewTableRow 功能有限，且复制单元格样式繁琐。
                // 这里采用最基础的实现：只保证文字内容进去。
                
                for (int i = 0; i < topics.size() - 1; i++) {
                    SysPropertyMeetingTopic topic = topics.get(i);
                    org.apache.poi.xwpf.usermodel.XWPFTableRow newRow = targetTable.insertNewTableRow(templateRowIndex + i);
                    if (newRow != null) {
                         // 尝试按照模板行的单元格数量创建单元格
                         for (org.apache.poi.xwpf.usermodel.XWPFTableCell templateCell : templateRow.getTableCells()) {
                             org.apache.poi.xwpf.usermodel.XWPFTableCell newCell = newRow.addNewTableCell();
                             String text = templateCell.getText();
                             if (text.contains("@topic")) {
                                 newCell.setText(topic.getTopicTitle());
                             } else {
                                 newCell.setText(text);
                             }
                             // 注意：样式（边框、对齐、字体）这里没有复制，可能会很难看。
                             // 如果是严格格式的表决票，建议模板里直接预留足够多的行，或者使用 Freemarker 生成 xml。
                         }
                    }
                }
                // 最后一个议题复用模板行（保留了样式）
                replaceInRow(templateRow, "@topic", topics.get(topics.size() - 1).getTopicTitle());
            }
        } else {
            // 备选策略：如果没有找到 @topic，尝试查找表头包含“议题”或“表决事项”的表格
             for (org.apache.poi.xwpf.usermodel.XWPFTable table : doc.getTables()) {
                 if (table.getRows().size() > 0) {
                     StringBuilder headerTextBuilder = new StringBuilder();
                     for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : table.getRow(0).getTableCells()) {
                         headerTextBuilder.append(cell.getText());
                     }
                     String headerText = headerTextBuilder.toString();
                     
                     if (headerText.contains("议题") || headerText.contains("表决事项")) {
                         // 在表格末尾追加行
                         for (SysPropertyMeetingTopic topic : topics) {
                             org.apache.poi.xwpf.usermodel.XWPFTableRow row = table.createRow();
                             // 假设第一列是议题
                             if (row.getTableCells().size() > 0) {
                                 row.getCell(0).setText(topic.getTopicTitle());
                             }
                         }
                         break;
                     }
                 }
             }
        }
    }

    private void replaceInRow(org.apache.poi.xwpf.usermodel.XWPFTableRow row, String key, String value) {
        for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : row.getTableCells()) {
            for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : cell.getParagraphs()) {
                Map<String, String> singleParam = new java.util.HashMap<>();
                singleParam.put(key, value);
                replaceInParagraph(p, singleParam);
            }
        }
    }

    private void replaceInParagraph(org.apache.poi.xwpf.usermodel.XWPFParagraph p, Map<String, String> params) {
        List<org.apache.poi.xwpf.usermodel.XWPFRun> runs = p.getRuns();
        if (runs == null || runs.isEmpty()) return;

        // 1. 尝试简单替换（针对未分割的情况）
        boolean hit = false;
        for (org.apache.poi.xwpf.usermodel.XWPFRun r : runs) {
            String text = r.getText(0);
            if (text != null && !text.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (text.contains(entry.getKey())) {
                        log.info("Simple replacement hit: key={} in text={}", entry.getKey(), text);
                        text = text.replace(entry.getKey(), entry.getValue());
                        r.setText(text, 0);
                        hit = true;
                    }
                }
            }
        }
        if (hit) return; // 如果简单替换成功了，就不继续处理

        // 2. 复杂替换（针对 Key 被 Word 分割成多个 Run 的情况）
        String paragraphText = p.getText();
        
        // 只有当段落包含任意一个 Key 时才进行处理
        boolean containsKey = false;
        for (String key : params.keySet()) {
            if (paragraphText.contains(key)) {
                containsKey = true;
                log.info("Complex replacement hit: key={} in paragraph={}", key, paragraphText);
                break;
            }
        }
        
        if (containsKey) {
            String newText = paragraphText;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                newText = newText.replace(entry.getKey(), entry.getValue());
            }
            
            // 保留第一个 Run 的样式（如果存在），移除其他 Runs
            org.apache.poi.xwpf.usermodel.XWPFRun firstRun = runs.get(0);
            String fontFamily = firstRun.getFontFamily();
            int fontSize = firstRun.getFontSize();
            boolean bold = firstRun.isBold();
            boolean italic = firstRun.isItalic();
            String color = firstRun.getColor();
            // ... 其他样式属性视需要获取

            removeAllRuns(p);
            
            org.apache.poi.xwpf.usermodel.XWPFRun newRun = p.createRun();
            newRun.setText(newText);
            
            // 尽力恢复样式
            if (fontFamily != null) newRun.setFontFamily(fontFamily);
            if (fontSize != -1) newRun.setFontSize(fontSize);
            newRun.setBold(bold);
            newRun.setItalic(italic);
            if (color != null) newRun.setColor(color);
        }
    }
    
    private void removeAllRuns(org.apache.poi.xwpf.usermodel.XWPFParagraph p) {
        int size = p.getRuns().size();
        for (int i = size - 1; i >= 0; i--) {
            p.removeRun(i);
        }
    }

    private void replaceTextInDoc(org.apache.poi.xwpf.usermodel.XWPFDocument doc, Map<String, String> params) {
        for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : doc.getParagraphs()) {
            replaceInParagraph(p, params);
        }
        for (org.apache.poi.xwpf.usermodel.XWPFTable tbl : doc.getTables()) {
            for (org.apache.poi.xwpf.usermodel.XWPFTableRow row : tbl.getRows()) {
                for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : row.getTableCells()) {
                    for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(p, params);
                    }
                }
            }
        }
    }
}
