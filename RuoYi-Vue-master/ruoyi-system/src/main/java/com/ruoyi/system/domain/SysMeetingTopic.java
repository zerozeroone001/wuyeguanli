
package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SysMeetingTopic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long topicId;

    private Long meetingId;

    private String topicTitle;

    private String topicContent;

    private Integer agreeCount;

    private Integer opposeCount;

    private Integer abstainCount;

    public void setTopicId(Long topicId)
    {
        this.topicId = topicId;
    }

    public Long getTopicId()
    {
        return topicId;
    }
    public void setMeetingId(Long meetingId)
    {
        this.meetingId = meetingId;
    }

    public Long getMeetingId()
    {
        return meetingId;
    }
    public void setTopicTitle(String topicTitle)
    {
        this.topicTitle = topicTitle;
    }

    public String getTopicTitle()
    {
        return topicTitle;
    }
    public void setTopicContent(String topicContent)
    {
        this.topicContent = topicContent;
    }

    public String getTopicContent()
    {
        return topicContent;
    }
    public void setAgreeCount(Integer agreeCount)
    {
        this.agreeCount = agreeCount;
    }

    public Integer getAgreeCount()
    {
        return agreeCount;
    }
    public void setOpposeCount(Integer opposeCount)
    {
        this.opposeCount = opposeCount;
    }

    public Integer getOpposeCount()
    {
        return opposeCount;
    }
    public void setAbstainCount(Integer abstainCount)
    {
        this.abstainCount = abstainCount;
    }

    public Integer getAbstainCount()
    {
        return abstainCount;
    }
}
