package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业主委员会会议管理对象 sys_committee_meeting
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysCommitteeMeeting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会议ID */
    private Long meetingId;

    /** 会议主题 */
    @Excel(name = "会议主题")
    private String title;

    /** 会议时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会议时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date meetingTime;

    /** 会议地点 */
    @Excel(name = "会议地点")
    private String location;

    /** 会议通知内容 */
    @Excel(name = "会议通知内容")
    private String noticeContent;

    /** 会议记录文件URL */
    @Excel(name = "会议记录文件URL")
    private String recordFileUrl;

    /** 会议决议内容 */
    @Excel(name = "会议决议内容")
    private String resolutionContent;

    /** 状态（0未开始 1进行中 2已结束） */
    @Excel(name = "状态", readConverterExp = "0=未开始,1=进行中,2=已结束")
    private String status;

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

    public void setMeetingTime(Date meetingTime) 
    {
        this.meetingTime = meetingTime;
    }

    public Date getMeetingTime() 
    {
        return meetingTime;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setNoticeContent(String noticeContent) 
    {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent() 
    {
        return noticeContent;
    }

    public void setRecordFileUrl(String recordFileUrl) 
    {
        this.recordFileUrl = recordFileUrl;
    }

    public String getRecordFileUrl() 
    {
        return recordFileUrl;
    }

    public void setResolutionContent(String resolutionContent) 
    {
        this.resolutionContent = resolutionContent;
    }

    public String getResolutionContent() 
    {
        return resolutionContent;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("meetingTime", getMeetingTime())
            .append("location", getLocation())
            .append("noticeContent", getNoticeContent())
            .append("recordFileUrl", getRecordFileUrl())
            .append("resolutionContent", getResolutionContent())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
