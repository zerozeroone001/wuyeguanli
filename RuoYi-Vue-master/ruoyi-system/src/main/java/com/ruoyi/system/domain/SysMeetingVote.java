package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业主大会投票对象 sys_meeting_vote
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysMeetingVote extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 投票ID */
    private Long voteId;

    /** 关联的业主大会ID */
    @Excel(name = "关联的业主大会ID")
    private Long meetingId;

    /** 投票的业主用户ID (关联sys_user) */
    @Excel(name = "投票的业主用户ID (关联sys_user)")
    private Long userId;

    /** 表决票编号 (唯一) */
    @Excel(name = "表决票编号 (唯一)")
    private String voteTicketNo;

    /** 投票选项（0同意 1反对 2弃权） */
    @Excel(name = "投票选项", readConverterExp = "0=同意,1=反对,2=弃权")
    private String voteOption;

    /** 投票来源（0线上 1线下 2语音） */
    @Excel(name = "投票来源", readConverterExp = "0=线上,1=线下,2=语音")
    private String voteSource;

    /** 投票时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投票时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voteTime;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setVoteId(Long voteId) 
    {
        this.voteId = voteId;
    }

    public Long getVoteId() 
    {
        return voteId;
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

    public void setVoteTicketNo(String voteTicketNo) 
    {
        this.voteTicketNo = voteTicketNo;
    }

    public String getVoteTicketNo() 
    {
        return voteTicketNo;
    }

    public void setVoteOption(String voteOption) 
    {
        this.voteOption = voteOption;
    }

    public String getVoteOption() 
    {
        return voteOption;
    }

    public void setVoteSource(String voteSource) 
    {
        this.voteSource = voteSource;
    }

    public String getVoteSource() 
    {
        return voteSource;
    }

    public void setVoteTime(Date voteTime) 
    {
        this.voteTime = voteTime;
    }

    public Date getVoteTime() 
    {
        return voteTime;
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
            .append("voteId", getVoteId())
            .append("meetingId", getMeetingId())
            .append("userId", getUserId())
            .append("voteTicketNo", getVoteTicketNo())
            .append("voteOption", getVoteOption())
            .append("voteSource", getVoteSource())
            .append("voteTime", getVoteTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
