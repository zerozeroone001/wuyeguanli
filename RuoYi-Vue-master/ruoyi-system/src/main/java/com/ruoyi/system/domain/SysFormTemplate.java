package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 自定义表单模板对象 sys_form_template
 *
 * 用于存储问卷/调查等业务的表单结构与外观配置。
 *
 * @author ruoyi
 * @date 2025-10-23
 */
public class SysFormTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表单模板ID */
    private Long formId;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String formName;

    /** 模板类型（SURVEY/其它） */
    @Excel(name = "模板类型")
    private String formType;

    /** PC端表单结构（JSON） */
    private String formSchema;

    /** H5/小程序表单结构（JSON） */
    private String mobileSchema;

    /** 表单配置（JSON） */
    private String formConfig;

    /** 外观配置（JSON） */
    private String appearanceConfig;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getFormId()
    {
        return formId;
    }

    public void setFormId(Long formId)
    {
        this.formId = formId;
    }

    public String getFormName()
    {
        return formName;
    }

    public void setFormName(String formName)
    {
        this.formName = formName;
    }

    public String getFormType()
    {
        return formType;
    }

    public void setFormType(String formType)
    {
        this.formType = formType;
    }

    public String getFormSchema()
    {
        return formSchema;
    }

    public void setFormSchema(String formSchema)
    {
        this.formSchema = formSchema;
    }

    public String getMobileSchema()
    {
        return mobileSchema;
    }

    public void setMobileSchema(String mobileSchema)
    {
        this.mobileSchema = mobileSchema;
    }

    public String getFormConfig()
    {
        return formConfig;
    }

    public void setFormConfig(String formConfig)
    {
        this.formConfig = formConfig;
    }

    public String getAppearanceConfig()
    {
        return appearanceConfig;
    }

    public void setAppearanceConfig(String appearanceConfig)
    {
        this.appearanceConfig = appearanceConfig;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("formId", getFormId())
            .append("formName", getFormName())
            .append("formType", getFormType())
            .append("formSchema", getFormSchema())
            .append("mobileSchema", getMobileSchema())
            .append("formConfig", getFormConfig())
            .append("appearanceConfig", getAppearanceConfig())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}

