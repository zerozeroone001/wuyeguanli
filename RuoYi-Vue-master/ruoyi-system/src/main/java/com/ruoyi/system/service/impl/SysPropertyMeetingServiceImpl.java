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
public class SysPropertyMeetingServiceImpl implements ISysPropertyMeetingService {
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
    public SysPropertyMeeting selectSysPropertyMeetingByMeetingId(Long meetingId) {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting != null) {
            CommunityUtils.checkCommunityPermission(meeting.getCommunityId());

            // 计算投票面积占比
            if (meeting.getTotalVotingArea() != null && meeting.getTotalVotingArea().compareTo(java.math.BigDecimal.ZERO) > 0
                    && meeting.getParticipatedArea() != null) {
                java.math.BigDecimal percentage = meeting.getParticipatedArea()
                        .multiply(new java.math.BigDecimal(100))
                        .divide(meeting.getTotalVotingArea(), 2, java.math.RoundingMode.HALF_UP);
                meeting.setVotingAreaPercentage(percentage.toString() + "%");
            } else {
                meeting.setVotingAreaPercentage("0.00%");
            }

            if (StringUtils.isNotEmpty(meeting.getTopics())) {
                for (SysPropertyMeetingTopic topic : meeting.getTopics()) {
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
    public List<SysPropertyMeeting> selectSysPropertyMeetingList(SysPropertyMeeting sysPropertyMeeting) {
        List<SysPropertyMeeting> list = sysPropertyMeetingMapper.selectSysPropertyMeetingList(sysPropertyMeeting);
        for (SysPropertyMeeting meeting : list) {
            // 计算投票面积占比
            if (meeting.getTotalVotingArea() != null && meeting.getTotalVotingArea().compareTo(java.math.BigDecimal.ZERO) > 0
                    && meeting.getParticipatedArea() != null) {
                java.math.BigDecimal percentage = meeting.getParticipatedArea()
                        .multiply(new java.math.BigDecimal(100))
                        .divide(meeting.getTotalVotingArea(), 2, java.math.RoundingMode.HALF_UP);
                meeting.setVotingAreaPercentage(percentage.toString() + "%");
            } else {
                meeting.setVotingAreaPercentage("0.00%");
            }
        }
        return list;
    }

    @Override
    public List<SysPropertyMeeting> userSelectSysPropertyMeetingList(SysPropertyMeeting sysPropertyMeeting) {
        return sysPropertyMeetingMapper.userSelectSysPropertyMeetingList(sysPropertyMeeting);
    }

    @Override
    @Transactional
    public int insertSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting) {
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
            String voteNo = org.apache.commons.lang3.RandomStringUtils.random(16, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            for (SysPropertyMeetingTopic topic : topics) {
                SysMeetingVote vote = new SysMeetingVote();
                vote.setMeetingId(meeting.getMeetingId());
                vote.setTopicId(topic.getTopicId());
                vote.setUserId(owner.getUserId());
                vote.setUserName(owner.getUserName());
                vote.setCreateTime(DateUtils.getNowDate());
                vote.setVoteNo(voteNo);
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
    public int updateSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting) {
        enforceCommunityScope(sysPropertyMeeting.getCommunityId());
        sysPropertyMeeting.setUpdateTime(DateUtils.getNowDate());
        int rows = sysPropertyMeetingMapper.updateSysPropertyMeeting(sysPropertyMeeting);
        sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(sysPropertyMeeting.getMeetingId());
        insertTopics(sysPropertyMeeting);
        return rows;
    }

    @Override
    @Transactional
    public int deleteSysPropertyMeetingByMeetingIds(Long[] meetingIds) {
        for (Long meetingId : meetingIds) {
            SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
            if (meeting != null) {
                CommunityUtils.checkCommunityPermission(meeting.getCommunityId());
                sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(meetingId);
            }
        }
        return sysPropertyMeetingMapper.deleteSysPropertyMeetingByMeetingIds(meetingIds);
    }

    @Override
    @Transactional
    public int deleteSysPropertyMeetingByMeetingId(Long meetingId) {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting != null) {
            CommunityUtils.checkCommunityPermission(meeting.getCommunityId());
        }
        sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(meetingId);
        return sysPropertyMeetingMapper.deleteSysPropertyMeetingByMeetingId(meetingId);
    }

    public void insertTopics(SysPropertyMeeting sysPropertyMeeting) {
        List<SysPropertyMeetingTopic> topics = sysPropertyMeeting.getTopics();
        Long meetingId = sysPropertyMeeting.getMeetingId();
        if (StringUtils.isNotEmpty(topics)) {
            for (SysPropertyMeetingTopic topic : topics) {
                topic.setMeetingId(meetingId);
                sysPropertyMeetingTopicMapper.insertSysPropertyMeetingTopic(topic);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getMeetingMarks() {
        return sysPropertyMeetingMapper.getMeetingMarks(resolveCommunityIdForStatistics());
    }

    @Override
    public Long countOngoingMeetings() {
        return sysPropertyMeetingMapper.countOngoingMeetings();
    }

    @Override
    public Double getAverageParticipationRate() {
        return sysPropertyMeetingMapper.getAverageParticipationRate();
    }
    
    @Override
    public List<Map<String, Object>> getVoteParticipationTrend() {
        return sysPropertyMeetingMapper.getVoteParticipationTrend();
    }

    @Override
    public List<Map<String, Object>> getMeetingActivityStats() {
        return sysPropertyMeetingMapper.getMeetingActivityStats();
    }

    @Override
    public List<Map<String, Object>> getRecentVotes(int limit) {
        return sysPropertyMeetingMapper.getRecentVotes(limit);
    }
    
    @Override
    public Long countUpcomingMeetings() {
        return sysPropertyMeetingMapper.countUpcomingMeetings();
    }



    @Override
    public void sendCommitteeMeetingNotification(Long meetingId) {
        log.warn("业主委员会会议通知功能已移除，会议ID: {}", meetingId);
    }

    @Override
    public void sendGeneralMeetingNotification(Long meetingId) {
        log.warn("业主大会会议通知功能已移除，会议ID: {}", meetingId);
    }

    private void enforceCommunityScope(Long communityId) {
        if (communityId == null) {
            throw new ServiceException("会议必须绑定所属小区");
        }
        CommunityUtils.checkCommunityPermission(communityId);
    }

    private Long resolveCommunityIdForStatistics() {
        if (CommunityUtils.isCurrentUserAdmin()) {
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

        // 加载会议议题
        List<SysPropertyMeetingTopic> topics = sysPropertyMeetingTopicMapper.selectSysPropertyMeetingTopicListByMeetingId(meetingId);
        meeting.setTopics(topics);
        log.info("会议ID={}, 议题数量={}", meetingId, topics != null ? topics.size() : 0);

        // 导出空白表决票 (模板)
        if ("blank".equals(type)) {
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(meeting.getMeetingTitle() + "_空白表决票.docx", "UTF-8") + "\"");

            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource("template.docx");
            if (!resource.exists()) {
                throw new ServiceException("模板文件 template.docx 不存在，请联系管理员上传");
            }

            // 加载模板文档并替换议题
            try (java.io.InputStream is = resource.getInputStream();
                 org.apache.poi.xwpf.usermodel.XWPFDocument doc = new org.apache.poi.xwpf.usermodel.XWPFDocument(is);
                 java.io.OutputStream os = response.getOutputStream()) {

                // 替换文档中的议题占位符
                insertTopicsIntoDoc(doc, meeting.getTopics());

                // 替换其他基本信息占位符
                Map<String, String> params = new java.util.HashMap<>();
                params.put("@title", meeting.getMeetingTitle());
                params.put("{title}", meeting.getMeetingTitle());
                params.put("${title}", meeting.getMeetingTitle());
                // 替换固定标题文本
                params.put("业主大会会议表决票", meeting.getMeetingTitle());

                replaceTextInDoc(doc, params);

                // 输出文档
                doc.write(os);
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

        // 2. 获取小区所有业主的房产记录
        com.ruoyi.system.domain.EstateUserProperty query = new com.ruoyi.system.domain.EstateUserProperty();
        query.setCommunityId(meeting.getCommunityId());
        query.setUserType("00"); // 业主
        List<com.ruoyi.system.domain.EstateUserProperty> allOwners = estateUserPropertyMapper.selectEstateUserPropertyList(query);

        // 3. 按用户ID分组房产记录,并过滤已投票用户
        Map<Long, List<com.ruoyi.system.domain.EstateUserProperty>> userPropertiesMap = new java.util.LinkedHashMap<>();
        for (com.ruoyi.system.domain.EstateUserProperty owner : allOwners) {
            if (!votedUserIds.contains(owner.getUserId())) {
                userPropertiesMap.computeIfAbsent(owner.getUserId(), k -> new java.util.ArrayList<>()).add(owner);
            }
        }

        if (userPropertiesMap.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"msg\":\"没有未投票的业主\",\"code\":500}");
            return;
        }

        // 4. 准备ZIP输出
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(meeting.getMeetingTitle() + "_未投票表决票.zip", "UTF-8") + "\"");

        try (java.util.zip.ZipOutputStream zipOut = new java.util.zip.ZipOutputStream(response.getOutputStream())) {
            // 读取模板到内存
            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource("template.docx");
            if (!resource.exists()) {
                throw new ServiceException("模板文件 template.docx 不存在,请联系管理员上传");
            }

            java.io.ByteArrayOutputStream templateBaos = new java.io.ByteArrayOutputStream();
            try (java.io.InputStream is = resource.getInputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    templateBaos.write(buffer, 0, len);
                }
            }
            byte[] templateBytes = templateBaos.toByteArray();

            // 5. 按用户生成表决票
            for (Map.Entry<Long, List<com.ruoyi.system.domain.EstateUserProperty>> entry : userPropertiesMap.entrySet()) {
                Long userId = entry.getKey();

                try (java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(templateBytes);
                     org.apache.poi.xwpf.usermodel.XWPFDocument doc = new org.apache.poi.xwpf.usermodel.XWPFDocument(bais)) {

                    // 在循环内部根据用户ID和小区ID重新查询该用户在当前小区的所有房产
                    com.ruoyi.system.domain.EstateUserProperty propertyQuery = new com.ruoyi.system.domain.EstateUserProperty();
                    propertyQuery.setUserId(userId);
                    propertyQuery.setCommunityId(meeting.getCommunityId());
                    List<com.ruoyi.system.domain.EstateUserProperty> properties = estateUserPropertyMapper.selectEstateUserPropertyList(propertyQuery);

                    if (properties == null || properties.isEmpty()) {
                        log.warn("用户ID {} 在小区ID {} 下没有查询到房产记录", userId, meeting.getCommunityId());
                        continue;
                    }
                    // 查询用户的投票记录,获取voteNo
                    String voteNo = null;
                    List<SysMeetingVote> userVotes = sysMeetingVoteMapper.selectSysMeetingVoteList(
                            new SysMeetingVote() {{
                                setMeetingId(meetingId);
                                setUserId(userId);
                            }}
                    );

                    if (userVotes != null && !userVotes.isEmpty()) {
                        voteNo = userVotes.get(0).getVoteNo();
                        log.info("用户ID {} 的投票编号: {}", userId, voteNo);
                    } else {
                        log.warn("用户ID {} 在会议ID {} 下没有投票记录", userId, meetingId);
                        // 如果没有投票记录,生成一个新的编号
                        voteNo = org.apache.commons.lang3.RandomStringUtils.random(10, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                    }

                    // 获取用户基本信息(取第一条记录)
                    com.ruoyi.system.domain.EstateUserProperty firstProperty = properties.get(0);

                    // 用户姓名
                    String name = firstProperty.getUserName();
                    if (StringUtils.isEmpty(name)) name = "业主";

                    // 合并房号(逗号分隔)
                    List<String> rooms = new java.util.ArrayList<>();
                    java.math.BigDecimal totalArea = java.math.BigDecimal.ZERO;

                    log.info("用户 {} (ID:{}) 在小区 {} 的房产数量: {}", name, userId, meeting.getCommunityId(), properties.size());

                    for (com.ruoyi.system.domain.EstateUserProperty prop : properties) {
                        // 拼接房号: 楼栋+单元+房号
                        StringBuilder roomBuilder = new StringBuilder();
                        if (prop.getBuildingName() != null && !prop.getBuildingName().isEmpty()) {
                            roomBuilder.append(prop.getBuildingName());
                        }
                        if (prop.getUnitName() != null && !prop.getUnitName().isEmpty()) {
                            roomBuilder.append(prop.getUnitName());
                        }
                        if (prop.getRoomNumber() != null && !prop.getRoomNumber().isEmpty()) {
                            roomBuilder.append(prop.getRoomNumber());
                        }

                        String room = roomBuilder.toString();
                        if (!room.isEmpty()) {
                            rooms.add(room);
                            log.info("添加房号: {}, 面积: {}", room, prop.getArea());
                        }

                        // 累加面积
                        if (prop.getArea() != null) {
                            totalArea = totalArea.add(prop.getArea());
                        }
                    }

                    // 房号列表(逗号分隔)
                    String roomInfo = String.join(",", rooms);
                    if (roomInfo.isEmpty()) {
                        roomInfo = "未登记";
                    }
                    log.info("用户 {} 最终房号信息: {}", name, roomInfo);

                    // 总面积
                    String areaInfo = totalArea.toString();
                    log.info("用户 {} 总面积: {}", name, areaInfo);


// 电话号码
                    String phoneInfo = firstProperty.getPhonenumber();
                    if (StringUtils.isEmpty(phoneInfo)) {
                        phoneInfo = "";
                    }

                    // 准备占位符参数
                    Map<String, String> params = new java.util.HashMap<>();

                    // 姓名
                    params.put("@name", name);
                    params.put("{name}", name);
                    params.put("${name}", name);
                    params.put("业主姓名", name);  // 添加中文字段名


// 房号
                    params.put("@room", roomInfo);
                    params.put("{room}", roomInfo);
                    params.put("${room}", roomInfo);
                    params.put("房号", roomInfo);  // 添加中文字段名


// 面积
                    params.put("@area", areaInfo);
                    params.put("{area}", areaInfo);
                    params.put("${area}", areaInfo);
                    params.put("专有部分建筑面积(平方米)", areaInfo);  // 添加中文字段名
                    params.put("建筑面积", areaInfo);  // 添加简化的中文字段名


// 电话
                    params.put("@phone", phoneInfo);
                    params.put("{phone}", phoneInfo);
                    params.put("${phone}", phoneInfo);
                    params.put("电话", phoneInfo);  // 添加中文字段名

                    // 代理人信息(暂时留空,如需要可以从数据库获取)
                    params.put("代理人", "");
                    params.put("代理人联系号码", "");

                    // 编号
                    params.put("@number", voteNo);
                    params.put("{number}", voteNo);
                    params.put("${number}", voteNo);

                    // 日期
                    String dateInfo = DateUtils.getDate();
                    params.put("@date", dateInfo);
                    params.put("{date}", dateInfo);
                    params.put("${date}", dateInfo);

                    // 会议标题
                    String titleInfo = meeting.getMeetingTitle();
                    params.put("@title", titleInfo);
                    params.put("{title}", titleInfo);
                    params.put("${title}", titleInfo);

                    // 插入议题并替换占位符
                    log.info("开始处理用户 {}, 议题数量={}", name, meeting.getTopics() != null ? meeting.getTopics().size() : 0);
                    insertTopicsIntoDoc(doc, meeting.getTopics());

                    // 先填充表格单元格(左列=字段名,右列=值)
                    fillTableCellValues(doc, params);

                    // 再处理其他占位符(如标题、编号、日期等 - 这些字段可能在表格外)
                    Map<String, String> otherParams = new java.util.HashMap<>();
                    // 标题
                    otherParams.put("@title", meeting.getMeetingTitle());
                    otherParams.put("{title}", meeting.getMeetingTitle());
                    otherParams.put("${title}", meeting.getMeetingTitle());
                    // 替换固定标题文本
                    otherParams.put("业主大会会议表决票", meeting.getMeetingTitle());
                    // 编号(右上角)
                    otherParams.put("@number", voteNo);
                    otherParams.put("{number}", voteNo);
                    otherParams.put("${number}", voteNo);
                    // 日期
                    otherParams.put("@date", dateInfo);
                    otherParams.put("{date}", dateInfo);
                    otherParams.put("${date}", dateInfo);
                    replaceTextInDoc(doc, otherParams);

                    log.info("完成用户 {} 的表决票生成", name);

                    // 生成文件名(只用姓名,因为可能有多套房产)
                    String safeName = name.replaceAll("[\\\\/:*?\"<>|]", "_");
                    String fileName = safeName + ".docx";

                    zipOut.putNextEntry(new java.util.zip.ZipEntry(fileName));
                    doc.write(zipOut);
                    zipOut.closeEntry();
                } catch (Exception e) {
                    log.error("生成用户 {} 表决票失败", entry.getKey(), e);
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
                addCell(summaryTable, "本物业管理区域内投票权数", boldFont);
                addCell(summaryTable, String.valueOf(summary.getTotalOwners()) + " 户", normalFont);
                addCell(summaryTable, "专有部分总建筑面积", boldFont);
                addCell(summaryTable, summary.getTotalArea() + " m²", normalFont);

                addCell(summaryTable, "参与表决人数", boldFont);
                addCell(summaryTable, String.valueOf(summary.getParticipatedOwners()) + " 户", normalFont);
                addCell(summaryTable, "参与表决专有部分建筑面积", boldFont);
                addCell(summaryTable, summary.getParticipatedArea() + " m²", normalFont);

                addCell(summaryTable, "参与表决人数占比", boldFont);
                addCell(summaryTable, summary.getOwnerParticipationRate() + "%", normalFont);
                addCell(summaryTable, "参与表决专有部分建筑面积占比", boldFont);
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

                    // 从多
                    addCell(topicTable, "从多", normalFont);
                    addCell(topicTable, String.valueOf(topic.getFollowMajorityPeople() != null ? topic.getFollowMajorityPeople() : 0), normalFont);
                    addCell(topicTable, String.valueOf(topic.getFollowMajorityArea() != null ? topic.getFollowMajorityArea() : "0"), normalFont);
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

        // 获取所有投票记录 (复用导出列表的逻辑，包含详细的议题投票情况)
        List<com.ruoyi.system.domain.vo.VoteListExportVO> records = sysMeetingVoteService.selectVoteListForExport(meetingId, meeting.getCommunityId());

        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("投票明细(公示)");

            // 标题样式
            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // 标题行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"房号", "建筑面积(m²)", "业主姓名(脱敏)", "投票详情", "投票时间"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 数据行
            int rowIndex = 1;
            if (records != null) {
                for (com.ruoyi.system.domain.vo.VoteListExportVO record : records) {
                    // 只导出已投票的记录 (根据需求，公示通常只公示已投票的，或者全部？)
                    // 原逻辑是 selectVoteRecordsList(..., "1") 即已投票
                    // selectVoteListForExport 返回所有业主。我们需要过滤。
                    if (!"已投票".equals(record.getVoterStatus())) {
                        continue;
                    }

                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);

                    // 房号
                    row.createCell(0).setCellValue(record.getRoomNumber());

                    // 面积
                    row.createCell(1).setCellValue(record.getArea() != null ? record.getArea().toString() : "0");

                    // 姓名脱敏 (张三 -> 张*)
                    String name = record.getUserName();
                    if (StringUtils.isNotEmpty(name) && name.length() > 1) {
                        name = name.substring(0, 1) + StringUtils.repeat("*", name.length() - 1);
                    } else if (StringUtils.isEmpty(name)) {
                        name = "业主";
                    }
                    row.createCell(2).setCellValue(name);

                    // 投票详情 (议题1:同意; 议题2:反对)
                    row.createCell(3).setCellValue(record.getTopicVotes());

                    // 时间
                    if (record.getParticipationTime() != null) {
                        row.createCell(4).setCellValue(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, record.getParticipationTime()));
                    } else {
                        row.createCell(4).setCellValue("-");
                    }
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
                        try {
                            fis.close();
                        } catch (Exception e) {
                        }
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

    @Override
    public int updateMeetingStatus(SysPropertyMeeting sysPropertyMeeting) {
        // 仅更新基本信息，不涉及关联表操作，不检查社区权限
        sysPropertyMeeting.setUpdateTime(DateUtils.getNowDate());
        return sysPropertyMeetingMapper.updateSysPropertyMeeting(sysPropertyMeeting);
    }

    @Override
    public List<com.ruoyi.system.domain.vo.MeetingNotificationVO> getNotificationRecords(Long meetingId) {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting == null) {
            throw new ServiceException("会议不存在");
        }
        CommunityUtils.checkCommunityPermission(meeting.getCommunityId());

        return sysMeetingVoteMapper.selectNotificationRecords(meetingId, meeting.getCommunityId());
    }

    @Override
    public int stopMeeting(Long meetingId) {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting == null) {
            throw new ServiceException("会议不存在");
        }
        CommunityUtils.checkCommunityPermission(meeting.getCommunityId());

        // 只有进行中的会议才能停止
        if (!"1".equals(meeting.getMeetingStatus())) {
            throw new ServiceException("只有进行中的会议才能停止");
        }

        SysPropertyMeeting updateMeeting = new SysPropertyMeeting();
        updateMeeting.setMeetingId(meetingId);
        updateMeeting.setMeetingStatus("9"); // 9-已停止
        updateMeeting.setUpdateTime(DateUtils.getNowDate());
        updateMeeting.setUpdateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());

        return sysPropertyMeetingMapper.updateSysPropertyMeeting(updateMeeting);
    }

    private void insertTopicsIntoDoc(org.apache.poi.xwpf.usermodel.XWPFDocument doc, List<SysPropertyMeetingTopic> topics) {
        if (topics == null || topics.isEmpty()) return;

        // 策略:寻找包含 "@topic" 文本的表格行,作为模板行
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
            // 新策略:删除模板行之后的所有行(这些是模板中的默认议题)
            // 然后根据实际议题数量,复制模板行

            // 1. 先删除模板行之后的所有行
            int totalRows = targetTable.getRows().size();
            for (int i = totalRows - 1; i > templateRowIndex; i--) {
                targetTable.removeRow(i);
            }

            // 2. 根据议题数量复制模板行
            if (topics.size() == 1) {
                // 只有一个议题,直接替换模板行
                replaceInRow(templateRow, "@topic", topics.get(0).getTopicTitle());
                // 设置行高(800 twips ≈ 1.4cm)
                templateRow.setHeight(800);
            } else {
                // 多个议题:先插入 N-1 行,最后一行使用模板行本身
                for (int i = 0; i < topics.size() - 1; i++) {
                    SysPropertyMeetingTopic topic = topics.get(i);
                    // 在模板行之前插入新行
                    org.apache.poi.xwpf.usermodel.XWPFTableRow newRow = targetTable.insertNewTableRow(templateRowIndex + i);
                    if (newRow != null) {
                        // 设置行高(800 twips ≈ 1.4cm)
                        newRow.setHeight(800);
                        // 复制模板行的单元格结构
                        for (int cellIndex = 0; cellIndex < templateRow.getTableCells().size(); cellIndex++) {
                            org.apache.poi.xwpf.usermodel.XWPFTableCell templateCell = templateRow.getCell(cellIndex);
                            org.apache.poi.xwpf.usermodel.XWPFTableCell newCell = newRow.addNewTableCell();

                            // 复制单元格内容
                            String text = templateCell.getText();
                            if (text.contains("@topic")) {
                                newCell.setText(topic.getTopicTitle());
                            } else {
                                newCell.setText(text);
                            }

                            // 尝试复制基本样式
                            try {
                                if (templateCell.getCTTc() != null && newCell.getCTTc() != null) {
                                    // 复制单元格属性(边框、宽度等)
                                    if (templateCell.getCTTc().getTcPr() != null) {
                                        newCell.getCTTc().setTcPr(
                                                (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr)
                                                        templateCell.getCTTc().getTcPr().copy()
                                        );
                                    }
                                    // 设置垂直居中对齐
                                    if (newCell.getCTTc().getTcPr() == null) {
                                        newCell.getCTTc().addNewTcPr();
                                    }
                                    if (newCell.getCTTc().getTcPr().getVAlign() == null) {
                                        newCell.getCTTc().getTcPr().addNewVAlign();
                                    }
                                    newCell.getCTTc().getTcPr().getVAlign().setVal(
                                            org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc.CENTER
                                    );
                                }
                            } catch (Exception e) {
                                // 样式复制失败,忽略
                                log.warn("复制单元格样式失败", e);
                            }
                        }
                    }
                }
                // 最后一个议题使用模板行本身(保留完整样式)
                replaceInRow(templateRow, "@topic", topics.get(topics.size() - 1).getTopicTitle());
                // 设置行高
                templateRow.setHeight(800);
            }
        } else {
            // 备选策略:如果没有找到 @topic,尝试查找表头包含"议题"或"表决事项"的表格
            for (org.apache.poi.xwpf.usermodel.XWPFTable table : doc.getTables()) {
                if (table.getRows().size() > 0) {
                    StringBuilder headerTextBuilder = new StringBuilder();
                    for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : table.getRow(0).getTableCells()) {
                        headerTextBuilder.append(cell.getText());
                    }
                    String headerText = headerTextBuilder.toString();

                    if (headerText.contains("议题") || headerText.contains("表决事项")) {
                        // 删除表头之后的所有行
                        int totalRows = table.getRows().size();
                        for (int i = totalRows - 1; i > 0; i--) {
                            table.removeRow(i);
                        }

                        // 添加实际议题行
                        for (SysPropertyMeetingTopic topic : topics) {
                            org.apache.poi.xwpf.usermodel.XWPFTableRow row = table.createRow();
                            // 设置行高
                            row.setHeight(800);
                            // 假设第一列是议题
                            if (row.getTableCells().size() > 0) {
                                row.getCell(0).setText(topic.getTopicTitle());
                            }
                            // 设置所有单元格垂直居中
                            for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : row.getTableCells()) {
                                setCellVerticalCenter(cell);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 设置单元格垂直居中对齐
     */
    private void setCellVerticalCenter(org.apache.poi.xwpf.usermodel.XWPFTableCell cell) {
        try {
            if (cell.getCTTc() != null) {
                if (cell.getCTTc().getTcPr() == null) {
                    cell.getCTTc().addNewTcPr();
                }
                if (cell.getCTTc().getTcPr().getVAlign() == null) {
                    cell.getCTTc().getTcPr().addNewVAlign();
                }
                cell.getCTTc().getTcPr().getVAlign().setVal(
                        org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc.CENTER
                );
            }
        } catch (Exception e) {
            log.warn("设置单元格垂直居中失败", e);
        }
    }

    private void replaceInRow(org.apache.poi.xwpf.usermodel.XWPFTableRow row, String key, String value) {
        for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : row.getTableCells()) {
            // 设置垂直居中
            setCellVerticalCenter(cell);
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

        // 获取段落文本用于调试
        String paragraphText = p.getText();
        if (paragraphText != null && paragraphText.contains("编号")) {
            log.info("发现包含'编号'的段落: [{}]", paragraphText);
        }

        // 特殊处理:如果段落包含"编号:"且params中有编号值,则替换占位符或追加编号
        if (paragraphText != null && paragraphText.contains("编号:") && (params.containsKey("@number") || params.containsKey("{number}") || params.containsKey("${number}"))) {
            String voteNo = params.get("@number");
            if (voteNo == null) voteNo = params.get("{number}");
            if (voteNo == null) voteNo = params.get("${number}");
            
            if (voteNo != null && !paragraphText.contains(voteNo)) {
                String newText = paragraphText;
                
                // 优先替换"----"占位符(支持前后有空格的情况)
                if (paragraphText.contains("编号:----") || paragraphText.contains("编号: ----") || paragraphText.trim().equals("编号:----")) {
                    // 直接替换,保留原有的空格格式
                    newText = paragraphText.replace("----", voteNo);
                    log.info("替换编号占位符: [{}] -> [{}]", paragraphText, newText);
                }
                // 如果没有占位符,则在"编号:"后追加
                else if (!paragraphText.contains(voteNo)) {
                    newText = paragraphText.replace("编号:", "编号:" + voteNo);
                    log.info("追加编号值: [{}] -> [{}]", paragraphText, newText);
                }
                
                // 保留第一个 Run 的样式
                org.apache.poi.xwpf.usermodel.XWPFRun firstRun = runs.get(0);
                String fontFamily = firstRun.getFontFamily();
                int fontSize = firstRun.getFontSize();
                boolean bold = firstRun.isBold();
                boolean italic = firstRun.isItalic();
                String color = firstRun.getColor();
                
                removeAllRuns(p);
                
                org.apache.poi.xwpf.usermodel.XWPFRun newRun = p.createRun();
                newRun.setText(newText);
                
                // 恢复样式
                if (fontFamily != null) newRun.setFontFamily(fontFamily);
                if (fontSize != -1) newRun.setFontSize(fontSize);
                newRun.setBold(bold);
                newRun.setItalic(italic);
                if (color != null) newRun.setColor(color);
                
                return;
            }
        }

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
        log.info("开始替换文档占位符, 参数: {}", params.keySet());
        
        // 1. 处理普通段落
        int paragraphCount = 0;
        for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : doc.getParagraphs()) {
            String text = p.getText();
            if (text != null && !text.isEmpty()) {
                log.debug("处理段落 {}: {}", paragraphCount++, text);
            }
            replaceInParagraph(p, params);
        }
        
        // 2. 处理表格中的段落
        int tableCount = 0;
        for (org.apache.poi.xwpf.usermodel.XWPFTable tbl : doc.getTables()) {
            log.debug("处理表格 {}", tableCount++);
            for (org.apache.poi.xwpf.usermodel.XWPFTableRow row : tbl.getRows()) {
                for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : row.getTableCells()) {
                    for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(p, params);
                    }
                }
            }
        }
        
        // 3. 处理页眉
        for (org.apache.poi.xwpf.usermodel.XWPFHeader header : doc.getHeaderList()) {
            log.debug("处理页眉");
            for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : header.getParagraphs()) {
                String text = p.getText();
                if (text != null && !text.isEmpty()) {
                    log.debug("页眉段落: {}", text);
                }
                replaceInParagraph(p, params);
            }
            // 处理页眉中的表格
            for (org.apache.poi.xwpf.usermodel.XWPFTable tbl : header.getTables()) {
                for (org.apache.poi.xwpf.usermodel.XWPFTableRow row : tbl.getRows()) {
                    for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : row.getTableCells()) {
                        for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : cell.getParagraphs()) {
                            replaceInParagraph(p, params);
                        }
                    }
                }
            }
        }
        
        // 4. 处理页脚
        for (org.apache.poi.xwpf.usermodel.XWPFFooter footer : doc.getFooterList()) {
            log.debug("处理页脚");
            for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : footer.getParagraphs()) {
                String text = p.getText();
                if (text != null && !text.isEmpty()) {
                    log.debug("页脚段落: {}", text);
                }
                replaceInParagraph(p, params);
            }
            // 处理页脚中的表格
            for (org.apache.poi.xwpf.usermodel.XWPFTable tbl : footer.getTables()) {
                for (org.apache.poi.xwpf.usermodel.XWPFTableRow row : tbl.getRows()) {
                    for (org.apache.poi.xwpf.usermodel.XWPFTableCell cell : row.getTableCells()) {
                        for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : cell.getParagraphs()) {
                            replaceInParagraph(p, params);
                        }
                    }
                }
            }
        }
        
        log.info("文档占位符替换完成");
    }

    /**
     * 填充表格单元格的值(专门处理两列表格:左列=字段名,右列=值)
     *
     * @param doc    Word文档
     * @param params 字段名和值的映射
     */
    private void fillTableCellValues(org.apache.poi.xwpf.usermodel.XWPFDocument doc, Map<String, String> params) {
        for (org.apache.poi.xwpf.usermodel.XWPFTable tbl : doc.getTables()) {
            for (org.apache.poi.xwpf.usermodel.XWPFTableRow row : tbl.getRows()) {
                // 确保行至少有2个单元格(左列=字段名,右列=值)
                if (row.getTableCells().size() >= 2) {
                    org.apache.poi.xwpf.usermodel.XWPFTableCell leftCell = row.getCell(0);
                    org.apache.poi.xwpf.usermodel.XWPFTableCell rightCell = row.getCell(1);

                    // 获取左列单元格的文本
                    String leftText = leftCell.getText();

                    // 检查左列文本是否匹配任何字段名
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        String fieldName = entry.getKey();
                        String fieldValue = entry.getValue();

                        // 如果左列包含字段名称,则将值填充到右列
                        if (leftText.contains(fieldName)) {
                            // 清空右列的所有段落
                            while (rightCell.getParagraphs().size() > 0) {
                                rightCell.removeParagraph(0);
                            }

                            // 创建新段落并设置值
                            org.apache.poi.xwpf.usermodel.XWPFParagraph p = rightCell.addParagraph();
                            org.apache.poi.xwpf.usermodel.XWPFRun run = p.createRun();
                            run.setText(fieldValue != null ? fieldValue : "");

                            // 设置字体(可选,保持与模板一致)
                            run.setFontFamily("宋体");
                            run.setFontSize(12);

                            log.info("填充表格单元格: {} -> {}", fieldName, fieldValue);
                            break; // 找到匹配后跳出循环
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<SysPropertyMeeting> selectDeletedMeetingList(SysPropertyMeeting sysPropertyMeeting) {
        return sysPropertyMeetingMapper.selectDeletedMeetingList(sysPropertyMeeting);
    }

    @Override
    @Transactional
    public int restoreMeeting(Long meetingId) {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting != null) {
            CommunityUtils.checkCommunityPermission(meeting.getCommunityId());
        }
        return sysPropertyMeetingMapper.restoreMeeting(meetingId);
    }

    @Override
    @Transactional
    public int permanentlyDeleteMeeting(Long meetingId) {
        SysPropertyMeeting meeting = sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
        if (meeting != null) {
            CommunityUtils.checkCommunityPermission(meeting.getCommunityId());
            // 永久删除时也删除相关的议题
            sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(meetingId);
        }
        return sysPropertyMeetingMapper.permanentlyDeleteMeeting(meetingId);
    }
}
