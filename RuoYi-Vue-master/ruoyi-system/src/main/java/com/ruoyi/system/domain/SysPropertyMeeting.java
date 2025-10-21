package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会议管理对象 sys_property_meeting
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysPropertyMeeting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会议ID */
    private Long meetingId;

    /** 会议标题 */
    @Excel(name = "会议标题")
    private String meetingTitle;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 会议类型 */
    @Excel(name = "会议类型")
    private String meetingType;

    /** 会议内容 */
    @Excel(name = "会议内容")
    private String meetingContent;

    /** 会议时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会议时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date meetingTime;

    /** 会议地点 */
    @Excel(name = "会议地点")
    private String meetingLocation;

    /** 会议状态(0-筹备中,1-进行中,2-已结束) */
    @Excel(name = "会议状态(0-筹备中,1-进行中,2-已结束)")
    private String meetingStatus;

    /** 投票开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投票开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voteStartTime;

    /** 投票结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投票结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voteEndTime;

    /** 应参与人数 */
    @Excel(name = "应参与人数")
    private Long totalVoters;

    /** 实际参与人数 */
    @Excel(name = "实际参与人数")
    private Long actualVoters;

    /** 议题列表 */
    private List<SysPropertyMeetingTopic> topics;

    public void setMeetingId(Long meetingId) 
    {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() 
    {
        return meetingId;
    }

    public void setMeetingTitle(String meetingTitle) 
    {
        this.meetingTitle = meetingTitle;
    }

    public String getMeetingTitle() 
    {
        return meetingTitle;
    }

    public Long getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }

    public void setMeetingType(String meetingType) 
    {
        this.meetingType = meetingType;
    }

    public String getMeetingType() 
    {
        return meetingType;
    }

    public void setMeetingContent(String meetingContent) 
    {
        this.meetingContent = meetingContent;
    }

    public String getMeetingContent() 
    {
        return meetingContent;
    }

    public void setMeetingTime(Date meetingTime) 
    {
        this.meetingTime = meetingTime;
    }

    public Date getMeetingTime() 
    {
        return meetingTime;
    }

    public void setMeetingLocation(String meetingLocation) 
    {
        this.meetingLocation = meetingLocation;
    }

    public String getMeetingLocation() 
    {
        return meetingLocation;
    }

    public void setMeetingStatus(String meetingStatus) 
    {
        this.meetingStatus = meetingStatus;
    }

    public String getMeetingStatus() 
    {
        return meetingStatus;
    }

    public void setVoteStartTime(Date voteStartTime) 
    {
        this.voteStartTime = voteStartTime;
    }

    public Date getVoteStartTime() 
    {
        return voteStartTime;
    }

    public void setVoteEndTime(Date voteEndTime) 
    {
        this.voteEndTime = voteEndTime;
    }

    public Date getVoteEndTime() 
    {
        return voteEndTime;
    }

    public void setTotalVoters(Long totalVoters) 
    {
        this.totalVoters = totalVoters;
    }

    public Long getTotalVoters() 
    {
        return totalVoters;
    }

    public void setActualVoters(Long actualVoters) 
    {
        this.actualVoters = actualVoters;
    }

    public Long getActualVoters() 
    {
        return actualVoters;
    }

    public List<SysPropertyMeetingTopic> getTopics()
    {
        return topics;
    }

    public void setTopics(List<SysPropertyMeetingTopic> topics)
    {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("meetingId", getMeetingId())
            .append("meetingTitle", getMeetingTitle())
            .append("communityId", getCommunityId())
            .append("meetingType", getMeetingType())
            .append("meetingContent", getMeetingContent())
            .append("meetingTime", getMeetingTime())
            .append("meetingLocation", getMeetingLocation())
            .append("meetingStatus", getMeetingStatus())
            .append("voteStartTime", getVoteStartTime())
            .append("voteEndTime", getVoteEndTime())
            .append("totalVoters", getTotalVoters())
            .append("actualVoters", getActualVoters())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("topics", getTopics())
            .toString();
    }

}
