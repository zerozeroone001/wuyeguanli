package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 法律咨询对象 sys_legal_consultation
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysLegalConsultation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 咨询ID */
    private Long consultationId;

    /** 咨询人用户ID (关联sys_user) */
    @Excel(name = "咨询人用户ID (关联sys_user)")
    private Long userId;

    /** 咨询标题 */
    @Excel(name = "咨询标题")
    private String title;

    /** 咨询内容 */
    @Excel(name = "咨询内容")
    private String content;

    /** 关联的自定义表单数据ID */
    @Excel(name = "关联的自定义表单数据ID")
    private Long formDataId;

    /** 状态（0待处理 1已回复） */
    @Excel(name = "状态", readConverterExp = "0=待处理,1=已回复")
    private String status;

    /** 回复内容 */
    @Excel(name = "回复内容")
    private String replyContent;

    /** 回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "回复时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date replyTime;

    /** 指派的法律专业人员 */
    @Excel(name = "指派的法律专业人员")
    private String assignee;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setConsultationId(Long consultationId) 
    {
        this.consultationId = consultationId;
    }

    public Long getConsultationId() 
    {
        return consultationId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
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

    public void setFormDataId(Long formDataId) 
    {
        this.formDataId = formDataId;
    }

    public Long getFormDataId() 
    {
        return formDataId;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setReplyContent(String replyContent) 
    {
        this.replyContent = replyContent;
    }

    public String getReplyContent() 
    {
        return replyContent;
    }

    public void setReplyTime(Date replyTime) 
    {
        this.replyTime = replyTime;
    }

    public Date getReplyTime() 
    {
        return replyTime;
    }

    public void setAssignee(String assignee) 
    {
        this.assignee = assignee;
    }

    public String getAssignee() 
    {
        return assignee;
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
            .append("consultationId", getConsultationId())
            .append("userId", getUserId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("formDataId", getFormDataId())
            .append("status", getStatus())
            .append("replyContent", getReplyContent())
            .append("replyTime", getReplyTime())
            .append("assignee", getAssignee())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
