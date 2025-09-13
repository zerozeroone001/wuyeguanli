package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公证处信息对象 sys_notary_office
 * 
 * @author ruoyi
 * @date 2025-09-11
 */
public class SysNotaryOffice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公证处ID */
    private Long officeId;

    /** 公证处名称 */
    @Excel(name = "公证处名称")
    private String name;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 工作时间 */
    @Excel(name = "工作时间")
    private String officeHours;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setOfficeId(Long officeId) 
    {
        this.officeId = officeId;
    }

    public Long getOfficeId() 
    {
        return officeId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setOfficeHours(String officeHours) 
    {
        this.officeHours = officeHours;
    }

    public String getOfficeHours() 
    {
        return officeHours;
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
            .append("officeId", getOfficeId())
            .append("name", getName())
            .append("address", getAddress())
            .append("phone", getPhone())
            .append("officeHours", getOfficeHours())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
