package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMeetingFeedbackMapper;
import com.ruoyi.system.domain.SysMeetingFeedback;
import com.ruoyi.system.service.ISysMeetingFeedbackService;

/**
 * 会议意见反馈Service业务层处理
 *
 * @author ruoyi
 * @date 2025-09-03
 */
@Service
public class SysMeetingFeedbackServiceImpl implements ISysMeetingFeedbackService
{
    @Autowired
    private SysMeetingFeedbackMapper sysMeetingFeedbackMapper;

    /**
     * 查询会议意见反馈列表
     *
     * @param topicId 议题ID
     * @return 会议意见反馈
     */
    @Override
    public List<SysMeetingFeedback> selectSysMeetingFeedbackListByTopicId(Long topicId)
    {
        return sysMeetingFeedbackMapper.selectSysMeetingFeedbackListByTopicId(topicId);
    }
}
