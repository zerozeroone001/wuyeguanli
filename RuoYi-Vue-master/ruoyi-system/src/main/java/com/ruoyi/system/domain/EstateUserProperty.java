package com.ruoyi.system.domain;

import java.math.BigDecimal;
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

    /** 小区名称 */
    private String communityName;

    /** 小区地址 */
    private String communityAddress;

    /** 楼栋名称 */
    private String buildingName;

    /** 单元名称 */
    private String unitName;

    /** 房号 */
    private String roomNumber;

    /** 所在楼层 */
    private Long floor;

    /** 建筑面积 */
    private BigDecimal area;

    /** 关键词（模糊搜索） */
    private String keyword;

    /** 用户名（账号） */
    private String userName;

    /** 昵称 */
    private String nickName;

    /** 手机号 */
    private String phonenumber;

    /** 真实姓名 */
    private String realName;

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

    public String getCommunityName()
    {
        return communityName;
    }

    public void setCommunityName(String communityName)
    {
        this.communityName = communityName;
    }

    public String getCommunityAddress()
    {
        return communityAddress;
    }

    public void setCommunityAddress(String communityAddress)
    {
        this.communityAddress = communityAddress;
    }

    public String getBuildingName()
    {
        return buildingName;
    }

    public void setBuildingName(String buildingName)
    {
        this.buildingName = buildingName;
    }

    public String getUnitName()
    {
        return unitName;
    }

    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }

    public String getRoomNumber()
    {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber)
    {
        this.roomNumber = roomNumber;
    }

    public Long getFloor()
    {
        return floor;
    }

    public void setFloor(Long floor)
    {
        this.floor = floor;
    }

    public BigDecimal getArea()
    {
        return area;
    }

    public void setArea(BigDecimal area)
    {
        this.area = area;
    }

    public void setUserType(String userType) 
    {
        this.userType = userType;
    }

    public String getUserType() 
    {
        return userType;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
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
            .append("communityName", getCommunityName())
            .append("communityAddress", getCommunityAddress())
            .append("buildingName", getBuildingName())
            .append("unitName", getUnitName())
            .append("roomNumber", getRoomNumber())
            .append("floor", getFloor())
            .append("area", getArea())
            .append("keyword", getKeyword())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("phonenumber", getPhonenumber())
            .append("realName", getRealName())
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
