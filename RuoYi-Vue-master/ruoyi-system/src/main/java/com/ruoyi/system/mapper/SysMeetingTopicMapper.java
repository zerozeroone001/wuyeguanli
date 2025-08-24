
package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysMeetingTopic;

/**
 * 会议议题Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface SysMeetingTopicMapper 
{
    /**
     * 查询会议议题列表
     * 
     * @param meetingId 会议ID
     * @return 会议议题集合
     */
    public List<SysMeetingTopic> selectMeetingTopicList(Long meetingId);
}
