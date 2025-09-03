package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 意见征询对象 sys_opinion_consultation
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
public class SysOpinionConsultation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long consultationId;

    /** 创建人用户ID (关联sys_user表) */
    @Excel(name = "创建人用户ID (关联sys_user表)")
    private Long userId;

    /** 征询标题 */
    @Excel(name = "征询标题")
    private String title;

    /** 征询内容 */
    @Excel(name = "征询内容")
    private String content;

    /** 来源类型（MEETING_TOPIC, CONTRACT, COMPLAINT） */
    @Excel(name = "来源类型", readConverterExp = "M=EETING_TOPIC,,C=ONTRACT,,C=OMPLAINT")
    private String sourceType;

    /** 来源业务ID */
    @Excel(name = "来源业务ID")
    private Long sourceId;

    /** 状态（0=进行中, 1=已结束） */
    @Excel(name = "状态", readConverterExp = "0==进行中,,1==已结束")
    private String status;

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

    public void setSourceType(String sourceType) 
    {
        this.sourceType = sourceType;
    }

    public String getSourceType() 
    {
        return sourceType;
    }

    public void setSourceId(Long sourceId) 
    {
        this.sourceId = sourceId;
    }

    public Long getSourceId() 
    {
        return sourceId;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("consultationId", getConsultationId())
            .append("userId", getUserId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("sourceType", getSourceType())
            .append("sourceId", getSourceId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
