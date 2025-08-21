package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 投诉管理对象 sys_property_complaint
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysPropertyComplaint extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 投诉ID */
    private Long complaintId;

    /** 投诉编号 */
    @Excel(name = "投诉编号")
    private String complaintNo;

    /** 投诉人ID */
    @Excel(name = "投诉人ID")
    private Long userId;

    /** 投诉类型(1-物业服务,2-设施设备,3-环境卫生,4-安全管理,5-收费争议,6-其他) */
    @Excel(name = "投诉类型(1-物业服务,2-设施设备,3-环境卫生,4-安全管理,5-收费争议,6-其他)")
    private String complaintType;

    /** 投诉标题 */
    @Excel(name = "投诉标题")
    private String complaintTitle;

    /** 投诉内容 */
    @Excel(name = "投诉内容")
    private String complaintContent;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 紧急程度(1-紧急,2-普通,3-一般) */
    @Excel(name = "紧急程度(1-紧急,2-普通,3-一般)")
    private String urgency;

    /** 期望处理时间(1-立即,2-3天内,3-1周内,4-1月内) */
    @Excel(name = "期望处理时间(1-立即,2-3天内,3-1周内,4-1月内)")
    private String expectedTime;

    /** 状态(0-待处理,1-处理中,2-已完成,3-已关闭) */
    @Excel(name = "状态(0-待处理,1-处理中,2-已完成,3-已关闭)")
    private String status;

    /** 处理人ID */
    @Excel(name = "处理人ID")
    private Long handlerId;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handleTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completeTime;

    /** 满意度(1-5分) */
    @Excel(name = "满意度(1-5分)")
    private String satisfaction;

    public void setComplaintId(Long complaintId) 
    {
        this.complaintId = complaintId;
    }

    public Long getComplaintId() 
    {
        return complaintId;
    }

    public void setComplaintNo(String complaintNo) 
    {
        this.complaintNo = complaintNo;
    }

    public String getComplaintNo() 
    {
        return complaintNo;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setComplaintType(String complaintType) 
    {
        this.complaintType = complaintType;
    }

    public String getComplaintType() 
    {
        return complaintType;
    }

    public void setComplaintTitle(String complaintTitle) 
    {
        this.complaintTitle = complaintTitle;
    }

    public String getComplaintTitle() 
    {
        return complaintTitle;
    }

    public void setComplaintContent(String complaintContent) 
    {
        this.complaintContent = complaintContent;
    }

    public String getComplaintContent() 
    {
        return complaintContent;
    }

    public void setContactPhone(String contactPhone) 
    {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }

    public void setUrgency(String urgency) 
    {
        this.urgency = urgency;
    }

    public String getUrgency() 
    {
        return urgency;
    }

    public void setExpectedTime(String expectedTime) 
    {
        this.expectedTime = expectedTime;
    }

    public String getExpectedTime() 
    {
        return expectedTime;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setHandlerId(Long handlerId) 
    {
        this.handlerId = handlerId;
    }

    public Long getHandlerId() 
    {
        return handlerId;
    }

    public void setHandleTime(Date handleTime) 
    {
        this.handleTime = handleTime;
    }

    public Date getHandleTime() 
    {
        return handleTime;
    }

    public void setCompleteTime(Date completeTime) 
    {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime() 
    {
        return completeTime;
    }

    public void setSatisfaction(String satisfaction) 
    {
        this.satisfaction = satisfaction;
    }

    public String getSatisfaction() 
    {
        return satisfaction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("complaintId", getComplaintId())
            .append("complaintNo", getComplaintNo())
            .append("userId", getUserId())
            .append("complaintType", getComplaintType())
            .append("complaintTitle", getComplaintTitle())
            .append("complaintContent", getComplaintContent())
            .append("contactPhone", getContactPhone())
            .append("urgency", getUrgency())
            .append("expectedTime", getExpectedTime())
            .append("status", getStatus())
            .append("handlerId", getHandlerId())
            .append("handleTime", getHandleTime())
            .append("completeTime", getCompleteTime())
            .append("satisfaction", getSatisfaction())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
