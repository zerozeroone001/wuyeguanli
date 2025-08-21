package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物业制度管理对象 sys_property_regulation
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysPropertyRegulation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 制度ID */
    private Long regulationId;

    /** 制度名称 */
    @Excel(name = "制度名称")
    private String regulationName;

    /** 制度类型 */
    @Excel(name = "制度类型")
    private String regulationType;

    /** 制度内容 */
    @Excel(name = "制度内容")
    private String regulationContent;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    /** 生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生效日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date effectiveDate;

    /** 版本号 */
    @Excel(name = "版本号")
    private String version;

    /** 状态(0-停用,1-正常) */
    @Excel(name = "状态(0-停用,1-正常)")
    private String status;

    public void setRegulationId(Long regulationId) 
    {
        this.regulationId = regulationId;
    }

    public Long getRegulationId() 
    {
        return regulationId;
    }

    public void setRegulationName(String regulationName) 
    {
        this.regulationName = regulationName;
    }

    public String getRegulationName() 
    {
        return regulationName;
    }

    public void setRegulationType(String regulationType) 
    {
        this.regulationType = regulationType;
    }

    public String getRegulationType() 
    {
        return regulationType;
    }

    public void setRegulationContent(String regulationContent) 
    {
        this.regulationContent = regulationContent;
    }

    public String getRegulationContent() 
    {
        return regulationContent;
    }

    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }

    public void setEffectiveDate(Date effectiveDate) 
    {
        this.effectiveDate = effectiveDate;
    }

    public Date getEffectiveDate() 
    {
        return effectiveDate;
    }

    public void setVersion(String version) 
    {
        this.version = version;
    }

    public String getVersion() 
    {
        return version;
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
            .append("regulationId", getRegulationId())
            .append("regulationName", getRegulationName())
            .append("regulationType", getRegulationType())
            .append("regulationContent", getRegulationContent())
            .append("filePath", getFilePath())
            .append("effectiveDate", getEffectiveDate())
            .append("version", getVersion())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
