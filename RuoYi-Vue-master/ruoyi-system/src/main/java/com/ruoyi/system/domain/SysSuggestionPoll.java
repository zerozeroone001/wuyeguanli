package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 意见征询主对象 sys_suggestion_poll
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysSuggestionPoll extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 征询ID */
    private Long pollId;

    /** 征询标题 */
    @Excel(name = "征询标题")
    private String title;

    /** 征询描述 */
    @Excel(name = "征询描述")
    private String description;

    /** 关联的自定义表单ID */
    @Excel(name = "关联的自定义表单ID")
    private Long formId;

    /** 关联表单名称 */
    private String formName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 状态（0未开始 1进行中 2已结束） */
    @Excel(name = "状态", readConverterExp = "0=未开始,1=进行中,2=已结束")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setPollId(Long pollId) 
    {
        this.pollId = pollId;
    }

    public Long getPollId() 
    {
        return pollId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setFormId(Long formId) 
    {
        this.formId = formId;
    }

    public Long getFormId() 
    {
        return formId;
    }

    public String getFormName()
    {
        return formName;
    }

    public void setFormName(String formName)
    {
        this.formName = formName;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
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
            .append("pollId", getPollId())
            .append("title", getTitle())
            .append("description", getDescription())
            .append("formId", getFormId())
            .append("formName", getFormName())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
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
