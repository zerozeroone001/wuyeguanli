package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysMeetingFeedback;

/**
 * 会议意见反馈Mapper接口
 *
 * @author ruoyi
 * @date 2025-09-03
 */
public interface SysMeetingFeedbackMapper
{
    /**
     * 查询会议意见反馈列表
     *
     * @param topicId 议题ID
     * @return 会议意见反馈集合
     */
    public List<SysMeetingFeedback> selectSysMeetingFeedbackListByTopicId(Long topicId);
}
