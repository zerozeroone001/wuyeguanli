package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业主大会会议管理对象 sys_general_meeting
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysGeneralMeeting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会议ID */
    private Long meetingId;

    /** 会议主题 */
    @Excel(name = "会议主题")
    private String title;

    /** 会议行事历信息 */
    @Excel(name = "会议行事历信息")
    private String calendarInfo;

    /** 会议公告 */
    @Excel(name = "会议公告")
    private String announcement;

    /** 状态（0未开始 1投票中 2已结束） */
    @Excel(name = "状态", readConverterExp = "0=未开始,1=投票中,2=已结束")
    private String status;

    /** 线上投票结果（JSON格式） */
    @Excel(name = "线上投票结果", readConverterExp = "J=SON格式")
    private String onlineVoteResult;

    /** 线下投票结果（JSON格式） */
    @Excel(name = "线下投票结果", readConverterExp = "J=SON格式")
    private String offlineVoteResult;

    /** 语音投票结果（JSON格式） */
    @Excel(name = "语音投票结果", readConverterExp = "J=SON格式")
    private String voiceVoteResult;

    /** 最终决议 */
    @Excel(name = "最终决议")
    private String finalResolution;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setMeetingId(Long meetingId) 
    {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() 
    {
        return meetingId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setCalendarInfo(String calendarInfo) 
    {
        this.calendarInfo = calendarInfo;
    }

    public String getCalendarInfo() 
    {
        return calendarInfo;
    }

    public void setAnnouncement(String announcement) 
    {
        this.announcement = announcement;
    }

    public String getAnnouncement() 
    {
        return announcement;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setOnlineVoteResult(String onlineVoteResult) 
    {
        this.onlineVoteResult = onlineVoteResult;
    }

    public String getOnlineVoteResult() 
    {
        return onlineVoteResult;
    }

    public void setOfflineVoteResult(String offlineVoteResult) 
    {
        this.offlineVoteResult = offlineVoteResult;
    }

    public String getOfflineVoteResult() 
    {
        return offlineVoteResult;
    }

    public void setVoiceVoteResult(String voiceVoteResult) 
    {
        this.voiceVoteResult = voiceVoteResult;
    }

    public String getVoiceVoteResult() 
    {
        return voiceVoteResult;
    }

    public void setFinalResolution(String finalResolution) 
    {
        this.finalResolution = finalResolution;
    }

    public String getFinalResolution() 
    {
        return finalResolution;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("meetingId", getMeetingId())
            .append("title", getTitle())
            .append("calendarInfo", getCalendarInfo())
            .append("announcement", getAnnouncement())
            .append("status", getStatus())
            .append("onlineVoteResult", getOnlineVoteResult())
            .append("offlineVoteResult", getOfflineVoteResult())
            .append("voiceVoteResult", getVoiceVoteResult())
            .append("finalResolution", getFinalResolution())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
