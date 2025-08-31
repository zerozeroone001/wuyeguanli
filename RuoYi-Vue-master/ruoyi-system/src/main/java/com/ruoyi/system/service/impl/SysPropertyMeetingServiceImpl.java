package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysPropertyMeetingTopic;
import com.ruoyi.system.mapper.SysPropertyMeetingTopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysPropertyMeetingMapper;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.service.ISysPropertyMeetingService;

/**
 * 会议管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysPropertyMeetingServiceImpl implements ISysPropertyMeetingService 
{
    @Autowired
    private SysPropertyMeetingMapper sysPropertyMeetingMapper;

    @Autowired
    private SysPropertyMeetingTopicMapper sysPropertyMeetingTopicMapper;

    /**
     * 查询会议管理
     * 
     * @param meetingId 会议管理主键
     * @return 会议管理
     */
    @Override
    public SysPropertyMeeting selectSysPropertyMeetingByMeetingId(Long meetingId)
    {
        return sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
    }

    /**
     * 查询会议管理列表
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 会议管理
     */
    @Override
    public List<SysPropertyMeeting> selectSysPropertyMeetingList(SysPropertyMeeting sysPropertyMeeting)
    {
        return sysPropertyMeetingMapper.selectSysPropertyMeetingList(sysPropertyMeeting);
    }

    /**
     * 新增会议管理
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting)
    {
        sysPropertyMeeting.setCreateTime(DateUtils.getNowDate());
        int rows = sysPropertyMeetingMapper.insertSysPropertyMeeting(sysPropertyMeeting);
        insertTopics(sysPropertyMeeting);
        return rows;
    }

    /**
     * 修改会议管理
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting)
    {
        sysPropertyMeeting.setUpdateTime(DateUtils.getNowDate());
        int rows = sysPropertyMeetingMapper.updateSysPropertyMeeting(sysPropertyMeeting);
        sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(sysPropertyMeeting.getMeetingId());
        insertTopics(sysPropertyMeeting);
        return rows;
    }

    /**
     * 批量删除会议管理
     * 
     * @param meetingIds 需要删除的会议管理主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysPropertyMeetingByMeetingIds(Long[] meetingIds)
    {
        for (Long meetingId : meetingIds) {
            sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(meetingId);
        }
        return sysPropertyMeetingMapper.deleteSysPropertyMeetingByMeetingIds(meetingIds);
    }

    /**
     * 删除会议管理信息
     * 
     * @param meetingId 会议管理主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysPropertyMeetingByMeetingId(Long meetingId)
    {
        sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByMeetingId(meetingId);
        return sysPropertyMeetingMapper.deleteSysPropertyMeetingByMeetingId(meetingId);
    }

    /**
     * 新增议题信息
     * 
     * @param sysPropertyMeeting 会议管理对象
     */
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
    public List<Map<String, Object>> getMeetingMarks() {
        return sysPropertyMeetingMapper.getMeetingMarks();
    }

    @Override
    public Long countOngoingMeetings() {
        return null;
    }

    @Override
    public Double getAverageParticipationRate() {
        return null;
    }

    @Override
    public Long countUpcomingMeetings() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getVoteParticipationTrend() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getMeetingActivityStats() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getRecentVotes(int limit) {
        return null;
    }
}
