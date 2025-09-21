
package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMeetingTopicMapper;
import com.ruoyi.system.domain.SysMeetingTopic;
import com.ruoyi.system.service.ISysMeetingTopicService;

/**
 * 会议议题Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
@Service
public class SysMeetingTopicServiceImpl implements ISysMeetingTopicService 
{
    @Autowired
    private SysMeetingTopicMapper meetingTopicMapper;

    /**
     * 查询会议议题列表
     * 
     * @param meetingId 会议ID
     * @return 会议议题
     */
    @Override
    public List<SysMeetingTopic> selectMeetingTopicList(Long meetingId)
    {
        return meetingTopicMapper.selectMeetingTopicList(meetingId);
    }

    @Override
    public SysMeetingTopic selectMeetingTopicById(Long topicId) {
        return meetingTopicMapper.selectMeetingTopicById(topicId);
    }
}
