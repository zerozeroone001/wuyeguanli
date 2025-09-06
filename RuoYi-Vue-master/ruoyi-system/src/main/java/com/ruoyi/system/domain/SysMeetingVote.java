package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysMeetingVote extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long voteId;

    private Long topicId;

    private Long userId;

    private String userName;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setVoteOption(Integer voteOption)
    {
        this.voteOption = voteOption;
    }

    public Integer getVoteOption()
    {
        return voteOption;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("voteId", getVoteId())
                .append("topicId", getTopicId())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("voteOption", getVoteOption())
                .append("createTime", getCreateTime())
                .toString();
    }
}