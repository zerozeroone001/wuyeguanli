
package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysMeetingTopic;

/**
 * 会议议题Service接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface ISysMeetingTopicService 
{
    /**
     * 查询会议议题列表
     * 
     * @param meetingId 会议ID
     * @return 会议议题集合
     */
    public List<SysMeetingTopic> selectMeetingTopicList(Long meetingId);

    /**
     * 根据议题ID查询议题详细信息
     * 
     * @param topicId 议题ID
     * @return 议题对象
     */
    public SysMeetingTopic selectMeetingTopicById(Long topicId);
}
