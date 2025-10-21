package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公证流程记录对象 sys_notary_process_log
 * 
 * @author ruoyi
 * @date 2025-01-27
 */
public class SysNotaryProcessLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long logId;

    /** 公证ID */
    @Excel(name = "公证ID")
    private Long notaryId;

    /** 流程步骤 */
    @Excel(name = "流程步骤")
    private String processStep;

    /** 步骤名称 */
    @Excel(name = "步骤名称")
    private String stepName;

    /** 操作动作 */
    @Excel(name = "操作动作")
    private String action;

    /** 操作内容 */
    @Excel(name = "操作内容")
    private String content;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 操作人ID */
    @Excel(name = "操作人ID")
    private Long operatorId;

    /** 操作人姓名 */
    @Excel(name = "操作人姓名")
    private String operatorName;

    /** 操作时间 */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date operationTime;

    /** 操作时间 */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date operateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String comment;

    /** 附件URL */
    @Excel(name = "附件URL")
    private String attachmentUrl;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setNotaryId(Long notaryId) 
    {
        this.notaryId = notaryId;
    }

    public Long getNotaryId() 
    {
        return notaryId;
    }

    public void setProcessStep(String processStep) 
    {
        this.processStep = processStep;
    }

    public String getProcessStep() 
    {
        return processStep;
    }

    public void setStepName(String stepName) 
    {
        this.stepName = stepName;
    }

    public String getStepName() 
    {
        return stepName;
    }

    public void setAction(String action) 
    {
        this.action = action;
    }

    public String getAction() 
    {
        return action;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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

    public void setOperationTime(java.util.Date operationTime) 
    {
        this.operationTime = operationTime;
    }

    public java.util.Date getOperationTime() 
    {
        return operationTime;
    }

    public void setOperateTime(java.util.Date operateTime) 
    {
        this.operateTime = operateTime;
    }

    public java.util.Date getOperateTime() 
    {
        return operateTime;
    }

    public void setComment(String comment) 
    {
        this.comment = comment;
    }

    public String getComment() 
    {
        return comment;
    }

    public void setAttachmentUrl(String attachmentUrl) 
    {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAttachmentUrl() 
    {
        return attachmentUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("notaryId", getNotaryId())
            .append("processStep", getProcessStep())
            .append("stepName", getStepName())
            .append("action", getAction())
            .append("content", getContent())
            .append("status", getStatus())
            .append("operatorId", getOperatorId())
            .append("operatorName", getOperatorName())
            .append("operationTime", getOperationTime())
            .append("operateTime", getOperateTime())
            .append("comment", getComment())
            .append("attachmentUrl", getAttachmentUrl())
            .append("createTime", getCreateTime())
            .toString();
    }
}
