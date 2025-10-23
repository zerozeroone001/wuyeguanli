package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 表单提交记录对象 sys_form_submission
 *
 * 记录业主/用户的问卷填写内容。
 *
 * @author ruoyi
 * @date 2025-10-23
 */
public class SysFormSubmission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 提交ID */
    private Long submissionId;

    /** 表单模板ID */
    @Excel(name = "表单ID")
    private Long formId;

    /** 关联征询/问卷ID */
    @Excel(name = "问卷ID")
    private Long pollId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 关联小区ID */
    private Long communityId;

    /** 提交终端（pc/mobile） */
    private String clientType;

    /** 提交IP */
    private String submitIp;

    /** 状态（0有效 1撤销） */
    private String status;

    /** 答案内容（JSON） */
    private String answersData;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    public Long getSubmissionId()
    {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId)
    {
        this.submissionId = submissionId;
    }

    public Long getFormId()
    {
        return formId;
    }

    public void setFormId(Long formId)
    {
        this.formId = formId;
    }

    public Long getPollId()
    {
        return pollId;
    }

    public void setPollId(Long pollId)
    {
        this.pollId = pollId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }

    public String getClientType()
    {
        return clientType;
    }

    public void setClientType(String clientType)
    {
        this.clientType = clientType;
    }

    public String getSubmitIp()
    {
        return submitIp;
    }

    public void setSubmitIp(String submitIp)
    {
        this.submitIp = submitIp;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getAnswersData()
    {
        return answersData;
    }

    public void setAnswersData(String answersData)
    {
        this.answersData = answersData;
    }

    public Date getSubmitTime()
    {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("submissionId", getSubmissionId())
            .append("formId", getFormId())
            .append("pollId", getPollId())
            .append("userId", getUserId())
            .append("communityId", getCommunityId())
            .append("clientType", getClientType())
            .append("submitIp", getSubmitIp())
            .append("status", getStatus())
            .append("answersData", getAnswersData())
            .append("submitTime", getSubmitTime())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}

