package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业主变更申请对象 sys_owner_change
 * 
 * @author ruoyi
 * @date 2025-12-03
 */
public class SysOwnerChange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 变更ID */
    private Long changeId;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 小区名称 */
    private String communityName;

    /** 房产ID */
    @Excel(name = "房产ID")
    private Long propertyId;

    /** 房产名称（楼栋-房号） */
    private String propertyName;

    /** 原业主姓名 */
    @Excel(name = "原业主姓名")
    private String oldOwnerName;

    /** 原业主电话 */
    @Excel(name = "原业主电话")
    private String oldOwnerPhone;

    /** 新业主姓名 */
    @Excel(name = "新业主姓名")
    private String newOwnerName;

    /** 新业主电话 */
    @Excel(name = "新业主电话")
    private String newOwnerPhone;

    /** 新业主身份证号 */
    @Excel(name = "新业主身份证号")
    private String newOwnerIdCard;

    /** 房产证图片 */
    private String propertyCertImage;

    /** 状态（0待审核 1通过 2驳回） */
    @Excel(name = "状态", readConverterExp = "0=待审核,1=通过,2=驳回")
    private String status;

    /** 审核备注 */
    private String auditRemark;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setChangeId(Long changeId) 
    {
        this.changeId = changeId;
    }

    public Long getChangeId() 
    {
        return changeId;
    }
    public void setCommunityId(Long communityId) 
    {
        this.communityId = communityId;
    }

    public Long getCommunityId() 
    {
        return communityId;
    }
    public void setPropertyId(Long propertyId) 
    {
        this.propertyId = propertyId;
    }

    public Long getPropertyId() 
    {
        return propertyId;
    }
    public void setOldOwnerName(String oldOwnerName) 
    {
        this.oldOwnerName = oldOwnerName;
    }

    public String getOldOwnerName() 
    {
        return oldOwnerName;
    }
    public void setOldOwnerPhone(String oldOwnerPhone) 
    {
        this.oldOwnerPhone = oldOwnerPhone;
    }

    public String getOldOwnerPhone() 
    {
        return oldOwnerPhone;
    }
    public void setNewOwnerName(String newOwnerName) 
    {
        this.newOwnerName = newOwnerName;
    }

    public String getNewOwnerName() 
    {
        return newOwnerName;
    }
    public void setNewOwnerPhone(String newOwnerPhone) 
    {
        this.newOwnerPhone = newOwnerPhone;
    }

    public String getNewOwnerPhone() 
    {
        return newOwnerPhone;
    }
    public void setNewOwnerIdCard(String newOwnerIdCard) 
    {
        this.newOwnerIdCard = newOwnerIdCard;
    }

    public String getNewOwnerIdCard() 
    {
        return newOwnerIdCard;
    }
    public void setPropertyCertImage(String propertyCertImage) 
    {
        this.propertyCertImage = propertyCertImage;
    }

    public String getPropertyCertImage() 
    {
        return propertyCertImage;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setAuditRemark(String auditRemark) 
    {
        this.auditRemark = auditRemark;
    }

    public String getAuditRemark() 
    {
        return auditRemark;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("changeId", getChangeId())
            .append("communityId", getCommunityId())
            .append("propertyId", getPropertyId())
            .append("oldOwnerName", getOldOwnerName())
            .append("oldOwnerPhone", getOldOwnerPhone())
            .append("newOwnerName", getNewOwnerName())
            .append("newOwnerPhone", getNewOwnerPhone())
            .append("newOwnerIdCard", getNewOwnerIdCard())
            .append("propertyCertImage", getPropertyCertImage())
            .append("status", getStatus())
            .append("auditRemark", getAuditRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
