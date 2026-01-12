package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 房产认证申请对象 sys_estate_user_apply
 * 
 * @author ruoyi
 * @date 2025-12-07
 */
public class SysEstateUserApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    private Long applyId;

    /** 关联ID */
    private Long associationId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 房产ID */
    @Excel(name = "房产ID")
    private Long propertyId;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 楼栋ID/Name (存储BigInt) */
    @Excel(name = "楼栋")
    private Long buildingName;

    /** 用户类型（00业主 01家属 02租户） */
    @Excel(name = "用户类型")
    private String userType;

    /** 审核状态（0待审核 1已通过 2已驳回） */
    @Excel(name = "审核状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 业主姓名 */
    @Excel(name = "业主姓名")
    private String ownerName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String ownerPhone;

    /** 小区名称 */
    @Excel(name = "小区名称")
    private String communityName;

    /** 房号 */
    @Excel(name = "房号")
    private String roomNumber;

    /** 楼栋名称(字符串) */
    private String buildingNameStr;

    public void setApplyId(Long applyId) 
    {
        this.applyId = applyId;
    }

    public Long getApplyId() 
    {
        return applyId;
    }

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

    public void setBuildingName(Long buildingName) 
    {
        this.buildingName = buildingName;
    }

    public Long getBuildingName() 
    {
        return buildingName;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBuildingNameStr() {
        return buildingNameStr;
    }

    public void setBuildingNameStr(String buildingNameStr) {
        this.buildingNameStr = buildingNameStr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("applyId", getApplyId())
            .append("associationId", getAssociationId())
            .append("userId", getUserId())
            .append("propertyId", getPropertyId())
            .append("communityId", getCommunityId())
            .append("buildingName", getBuildingName())
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
