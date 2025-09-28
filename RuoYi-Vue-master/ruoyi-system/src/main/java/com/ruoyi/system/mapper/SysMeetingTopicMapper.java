
package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysMeetingTopic;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据议题ID查询议题详细信息
     * 
     * @param topicId 议题ID
     * @return 议题对象
     */
    public SysMeetingTopic selectMeetingTopicById(Long topicId);

    /**
     * 增加投票计数
     * 
     * @param topicId 议题ID
     * @param choice 选项 (0-同意, 1-反对, 2-弃权)
     */
    public void incrementVoteCount(@Param("topicId") Long topicId, @Param("choice") String choice);

    /**
     * 减少投票计数
     * 
     * @param topicId 议题ID
     * @param choice 选项 (0-同意, 1-反对, 2-弃权)
     */
    public void decrementVoteCount(@Param("topicId") Long topicId, @Param("choice") String choice);
}
