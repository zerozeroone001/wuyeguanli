package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SysMeetingVote extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long voteId;

    private Long topicId;

    private Long userId;

    private Integer voteOption;

    public void setVoteId(Long voteId)
    {
        this.voteId = voteId;
    }

    public Long getVoteId()
    {
        return voteId;
    }
    public void setTopicId(Long topicId)
    {
        this.topicId = topicId;
    }

    public Long getTopicId()
    {
        return topicId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setVoteOption(Integer voteOption)
    {
        this.voteOption = voteOption;
    }

    public Integer getVoteOption()
    {
        return voteOption;
    }
}