package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会议通知记录对象 sys_meeting_notice
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public class SysMeetingNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 通知ID */
    private Long noticeId;

    /** 会议id */
    @Excel(name = "会议id")
    private Long meetingId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    private String userName;

    /** 通知方式（备用） */
    @Excel(name = "通知方式（备用）")
    private Integer voteOption;

    /** 通知方式 0短信通知 1电话通知 2线下拜访 */
    @Excel(name = "通知方式", readConverterExp = "0=短信通知,1=电话通知,2=线下拜访")
    private Integer voteType;

    /** 文件地址/录音地址 */
    @Excel(name = "文件地址/录音地址")
    private String flieUrl;

    /** 线下状态: 0-未送, 1-已送无人, 2-已送未收, 3-已收未投, 4-已投待唱, 5-已唱 */
    private Integer offlineStatus;

    /** 拜访人ID(管理员ID) */
    private Long visitorId;

    /** 拜访人姓名 */
    private String visitorName;

    public void setNoticeId(Long noticeId) 
    {
        this.noticeId = noticeId;
    }

    public Long getNoticeId() 
    {
        return noticeId;
    }
    public void setMeetingId(Long meetingId) 
    {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() 
    {
        return meetingId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setVoteOption(Integer voteOption) 
    {
        this.voteOption = voteOption;
    }

    public Integer getVoteOption() 
    {
        return voteOption;
    }
    public void setVoteType(Integer voteType) 
    {
        this.voteType = voteType;
    }

    public Integer getVoteType() 
    {
        return voteType;
    }
    public void setFlieUrl(String flieUrl) 
    {
        this.flieUrl = flieUrl;
    }

    public String getFlieUrl() 
    {
        return flieUrl;
    }

    public Integer getOfflineStatus() 
    {
        return offlineStatus;
    }

    public void setOfflineStatus(Integer offlineStatus) 
    {
        this.offlineStatus = offlineStatus;
    }

    public Long getVisitorId() 
    {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) 
    {
        this.visitorId = visitorId;
    }

    public String getVisitorName() 
    {
        return visitorName;
    }

    public void setVisitorName(String visitorName) 
    {
        this.visitorName = visitorName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("meetingId", getMeetingId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("voteOption", getVoteOption())
            .append("voteType", getVoteType())
            .append("flieUrl", getFlieUrl())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
