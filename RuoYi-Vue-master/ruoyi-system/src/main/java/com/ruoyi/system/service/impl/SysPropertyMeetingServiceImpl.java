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
        return rows;
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
}
