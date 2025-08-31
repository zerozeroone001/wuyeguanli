package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysPropertyMeetingTopicMapper;
import com.ruoyi.system.domain.SysPropertyMeetingTopic;
import com.ruoyi.system.service.ISysPropertyMeetingTopicService;

/**
 * 会议议题Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-31
 */
@Service
public class SysPropertyMeetingTopicServiceImpl implements ISysPropertyMeetingTopicService 
{
    @Autowired
    private SysPropertyMeetingTopicMapper sysPropertyMeetingTopicMapper;

    /**
     * 查询会议议题
     * 
     * @param topicId 会议议题主键
     * @return 会议议题
     */
    @Override
    public SysPropertyMeetingTopic selectSysPropertyMeetingTopicByTopicId(Long topicId)
    {
        return sysPropertyMeetingTopicMapper.selectSysPropertyMeetingTopicByTopicId(topicId);
    }

    /**
     * 查询会议议题列表
     * 
     * @param sysPropertyMeetingTopic 会议议题
     * @return 会议议题
     */
    @Override
    public List<SysPropertyMeetingTopic> selectSysPropertyMeetingTopicList(SysPropertyMeetingTopic sysPropertyMeetingTopic)
    {
        return sysPropertyMeetingTopicMapper.selectSysPropertyMeetingTopicList(sysPropertyMeetingTopic);
    }

    /**
     * 新增会议议题
     * 
     * @param sysPropertyMeetingTopic 会议议题
     * @return 结果
     */
    @Override
    public int insertSysPropertyMeetingTopic(SysPropertyMeetingTopic sysPropertyMeetingTopic)
    {
        sysPropertyMeetingTopic.setCreateTime(DateUtils.getNowDate());
        return sysPropertyMeetingTopicMapper.insertSysPropertyMeetingTopic(sysPropertyMeetingTopic);
    }

    /**
     * 修改会议议题
     * 
     * @param sysPropertyMeetingTopic 会议议题
     * @return 结果
     */
    @Override
    public int updateSysPropertyMeetingTopic(SysPropertyMeetingTopic sysPropertyMeetingTopic)
    {
        sysPropertyMeetingTopic.setUpdateTime(DateUtils.getNowDate());
        return sysPropertyMeetingTopicMapper.updateSysPropertyMeetingTopic(sysPropertyMeetingTopic);
    }

    /**
     * 批量删除会议议题
     * 
     * @param topicIds 需要删除的会议议题主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyMeetingTopicByTopicIds(Long[] topicIds)
    {
        return sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByTopicIds(topicIds);
    }

    /**
     * 删除会议议题信息
     * 
     * @param topicId 会议议题主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyMeetingTopicByTopicId(Long topicId)
    {
        return sysPropertyMeetingTopicMapper.deleteSysPropertyMeetingTopicByTopicId(topicId);
    }
}
