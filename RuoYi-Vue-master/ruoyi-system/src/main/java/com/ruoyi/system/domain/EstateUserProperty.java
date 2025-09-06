package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户与房产关系对象 estate_user_property
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
public class EstateUserProperty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联ID */
    private Long associationId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 房产ID */
    @Excel(name = "房产ID")
    private Long propertyId;

    /** 小区ID（冗余字段，方便查询） */
    @Excel(name = "小区ID", readConverterExp = "冗=余字段，方便查询")
    private Long communityId;

    /** 用户类型（00业主 01家属 02租户） */
    @Excel(name = "用户类型", readConverterExp = "0=0业主,0=1家属,0=2租户")
    private String userType;

    /** 审核状态（0待审核 1已通过 2已驳回） */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=已通过,2=已驳回")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setAssociationId(Long associationId) 
    {
        this.associationId = associationId;
    }

    public Long getAssociationId() 
    {
        return associationId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setPropertyId(Long propertyId) 
    {
        this.propertyId = propertyId;
    }

    public Long getPropertyId() 
    {
        return propertyId;
    }

    public void setCommunityId(Long communityId) 
    {
        this.communityId = communityId;
    }

    public Long getCommunityId() 
    {
        return communityId;
    }

    public void setUserType(String userType) 
    {
        this.userType = userType;
    }

    public String getUserType() 
    {
        return userType;
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
            .append("associationId", getAssociationId())
            .append("userId", getUserId())
            .append("propertyId", getPropertyId())
            .append("communityId", getCommunityId())
            .append("userType", getUserType())
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
