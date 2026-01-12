package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业主大会日志对象 sys_meeting_log
 * 
 * @author ruoyi
 * @date 2025-12-26
 */
public class SysMeetingLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long logId;

    /** 业主大会ID */
    @Excel(name = "业主大会ID")
    private Long meetingId;

    /** 会议标题 */
    @Excel(name = "会议标题")
    private String meetingTitle;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    private String userName;

    /** 用户手机号 */
    @Excel(name = "用户手机号")
    private String phone;

    /** 日志类型(1线上-已参会未投票 2线上-已参会已投票 3线下拜访-已送无人 4线下拜访-已送未收 5线下拜访-已收未投 6线下拜访-已投待唱 7线下拜访-已唱) */
    @Excel(name = "日志类型", readConverterExp = "1=线上-已参会未投票,2=线上-已参会已投票,3=线下拜访-已送无人,4=线下拜访-已送未收,5=线下拜访-已收未投,6=线下拜访-已投待唱,7=线下拜访-已唱")
    private Integer logType;

    /** 日志类型名称 */
    @Excel(name = "日志类型名称")
    private String logTypeName;

    /** 日志描述 */
    @Excel(name = "日志描述")
    private String logDesc;

    /** 操作人ID(线下操作时记录) */
    private Long operatorId;

    /** 操作人姓名 */
    @Excel(name = "操作人姓名")
    private String operatorName;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setMeetingId(Long meetingId) 
    {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() 
    {
        return meetingId;
    }

    public String getMeetingTitle()
    {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle)
    {
        this.meetingTitle = meetingTitle;
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

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setLogType(Integer logType) 
    {
        this.logType = logType;
    }

    public Integer getLogType() 
    {
        return logType;
    }

    public String getLogTypeName()
    {
        return logTypeName;
    }

    public void setLogTypeName(String logTypeName)
    {
        this.logTypeName = logTypeName;
    }

    public void setLogDesc(String logDesc) 
    {
        this.logDesc = logDesc;
    }

    public String getLogDesc() 
    {
        return logDesc;
    }

    public void setOperatorId(Long operatorId) 
    {
        this.operatorId = operatorId;
    }

    public Long getOperatorId() 
    {
        return operatorId;
    }

    public void setOperatorName(String operatorName) 
    {
        this.operatorName = operatorName;
    }

    public String getOperatorName() 
    {
        return operatorName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("meetingId", getMeetingId())
            .append("meetingTitle", getMeetingTitle())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("phone", getPhone())
            .append("logType", getLogType())
            .append("logTypeName", getLogTypeName())
            .append("logDesc", getLogDesc())
            .append("operatorId", getOperatorId())
            .append("operatorName", getOperatorName())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .toString();
    }
}
