package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公证类型配置对象 sys_notary_type_config
 * 
 * @author ruoyi
 * @date 2025-01-27
 */
public class SysNotaryTypeConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置ID */
    private Long configId;

    /** 类型代码 */
    @Excel(name = "类型代码")
    private String typeCode;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String typeName;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 基础费用 */
    @Excel(name = "基础费用")
    private java.math.BigDecimal baseFee;

    /** 加急费用 */
    @Excel(name = "加急费用")
    private java.math.BigDecimal urgentFee;

    /** 处理天数 */
    @Excel(name = "处理天数")
    private Integer processingDays;

    /** 加急处理天数 */
    @Excel(name = "加急处理天数")
    private Integer urgentDays;

    /** 必需材料（JSON格式） */
    @Excel(name = "必需材料")
    private String requiredDocuments;

    /** 可选材料（JSON格式） */
    @Excel(name = "可选材料")
    private String optionalDocuments;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortOrder;

    public void setConfigId(Long configId) 
    {
        this.configId = configId;
    }

    public Long getConfigId() 
    {
        return configId;
    }

    public void setTypeCode(String typeCode) 
    {
        this.typeCode = typeCode;
    }

    public String getTypeCode() 
    {
        return typeCode;
    }

    public void setTypeName(String typeName) 
    {
        this.typeName = typeName;
    }

    public String getTypeName() 
    {
        return typeName;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setBaseFee(java.math.BigDecimal baseFee) 
    {
        this.baseFee = baseFee;
    }

    public java.math.BigDecimal getBaseFee() 
    {
        return baseFee;
    }

    public void setUrgentFee(java.math.BigDecimal urgentFee) 
    {
        this.urgentFee = urgentFee;
    }

    public java.math.BigDecimal getUrgentFee() 
    {
        return urgentFee;
    }

    public void setProcessingDays(Integer processingDays) 
    {
        this.processingDays = processingDays;
    }

    public Integer getProcessingDays() 
    {
        return processingDays;
    }

    public void setUrgentDays(Integer urgentDays) 
    {
        this.urgentDays = urgentDays;
    }

    public Integer getUrgentDays() 
    {
        return urgentDays;
    }

    public void setRequiredDocuments(String requiredDocuments) 
    {
        this.requiredDocuments = requiredDocuments;
    }

    public String getRequiredDocuments() 
    {
        return requiredDocuments;
    }

    public void setOptionalDocuments(String optionalDocuments) 
    {
        this.optionalDocuments = optionalDocuments;
    }

    public String getOptionalDocuments() 
    {
        return optionalDocuments;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setSortOrder(Integer sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder() 
    {
        return sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("typeCode", getTypeCode())
            .append("typeName", getTypeName())
            .append("description", getDescription())
            .append("baseFee", getBaseFee())
            .append("urgentFee", getUrgentFee())
            .append("processingDays", getProcessingDays())
            .append("urgentDays", getUrgentDays())
            .append("requiredDocuments", getRequiredDocuments())
            .append("optionalDocuments", getOptionalDocuments())
            .append("status", getStatus())
            .append("sortOrder", getSortOrder())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
