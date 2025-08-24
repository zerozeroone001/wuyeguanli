
package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SysSupervision extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long supervisionId;

    private String title;

    private String content;

    private String supervisionType;

    private String status;

    private String urgency;

    private String expertName;

    private Integer replyCount;

    private String lastReplyTime;

    public void setSupervisionId(Long supervisionId)
    {
        this.supervisionId = supervisionId;
    }

    public Long getSupervisionId()
    {
        return supervisionId;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setSupervisionType(String supervisionType)
    {
        this.supervisionType = supervisionType;
    }

    public String getSupervisionType()
    {
        return supervisionType;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setUrgency(String urgency)
    {
        this.urgency = urgency;
    }

    public String getUrgency()
    {
        return urgency;
    }
    public void setExpertName(String expertName)
    {
        this.expertName = expertName;
    }

    public String getExpertName()
    {
        return expertName;
    }
    public void setReplyCount(Integer replyCount)
    {
        this.replyCount = replyCount;
    }

    public Integer getReplyCount()
    {
        return replyCount;
    }
    public void setLastReplyTime(String lastReplyTime)
    {
        this.lastReplyTime = lastReplyTime;
    }

    public String getLastReplyTime()
    {
        return lastReplyTime;
    }
}
