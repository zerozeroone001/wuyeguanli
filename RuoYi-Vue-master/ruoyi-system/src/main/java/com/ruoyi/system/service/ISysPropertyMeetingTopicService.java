package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysPropertyMeetingTopic;

/**
 * 会议议题Service接口
 * 
 * @author ruoyi
 * @date 2025-08-31
 */
public interface ISysPropertyMeetingTopicService 
{
    /**
     * 查询会议议题
     * 
     * @param topicId 会议议题主键
     * @return 会议议题
     */
    public SysPropertyMeetingTopic selectSysPropertyMeetingTopicByTopicId(Long topicId);

    /**
     * 查询会议议题列表
     * 
     * @param sysPropertyMeetingTopic 会议议题
     * @return 会议议题集合
     */
    public List<SysPropertyMeetingTopic> selectSysPropertyMeetingTopicList(SysPropertyMeetingTopic sysPropertyMeetingTopic);

    /**
     * 新增会议议题
     * 
     * @param sysPropertyMeetingTopic 会议议题
     * @return 结果
     */
    public int insertSysPropertyMeetingTopic(SysPropertyMeetingTopic sysPropertyMeetingTopic);

    /**
     * 修改会议议题
     * 
     * @param sysPropertyMeetingTopic 会议议题
     * @return 结果
     */
    public int updateSysPropertyMeetingTopic(SysPropertyMeetingTopic sysPropertyMeetingTopic);

    /**
     * 批量删除会议议题
     * 
     * @param topicIds 需要删除的会议议题主键集合
     * @return 结果
     */
    public int deleteSysPropertyMeetingTopicByTopicIds(Long[] topicIds);

    /**
     * 删除会议议题信息
     * 
     * @param topicId 会议议题主键
     * @return 结果
     */
    public int deleteSysPropertyMeetingTopicByTopicId(Long topicId);

    /**
     * 根据会议ID查询议题列表
     * 
     * @param meetingId 会议ID
     * @return 议题列表
     */
    public List<SysPropertyMeetingTopic> selectSysPropertyMeetingTopicListByMeetingId(Long meetingId);
}
