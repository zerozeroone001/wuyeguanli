package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysMeetingFeedback;

/**
 * 会议意见反馈Service接口
 *
 * @author ruoyi
 * @date 2025-09-03
 */
public interface ISysMeetingFeedbackService
{
    /**
     * 查询会议意见反馈列表
     *
     * @param topicId 议题ID
     * @return 会议意见反馈集合
     */
    public List<SysMeetingFeedback> selectSysMeetingFeedbackListByTopicId(Long topicId);
}
