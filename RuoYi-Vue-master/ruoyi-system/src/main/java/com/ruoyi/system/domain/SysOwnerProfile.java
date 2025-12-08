package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业主信息扩展对象 sys_owner_profile
 * 
 * @author ruoyi
 * @date 2025-09-15
 */
public class SysOwnerProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 业主ID (主键) */
    private Long ownerId;

    /** 业主编号 */
    @Excel(name = "业主编号")
    private String ownerNo;

    /** 用户ID (外键, 可选) */
    private Long userId;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 房产面积 */
    private BigDecimal propertyArea;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    /** 是否业主（0否 1业主 2业委会） */
    private int isOwner;

    /** 用户头像 */
    private String avatar;

    /** 用户姓名 (Snapshot) */
    @Excel(name = "用户姓名")
    private String userName;

    /** 真实姓名 (DTO/Transient) */
    private String realName;

    /** 用户昵称 */
    private String nickName;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 楼栋号 (临时字段) */
    private String buildingNo;

    /** 单元号 (临时字段) */
    private String unitNo;

    /** 房号 (临时字段) */
    private String roomNo;

    /** 账号状态（0正常 1停用） */
    private String status;

    /** 合并后的房产信息 */
    private String mergedProperties;

    /** 房产数量 */
    private Integer propertyCount;

    /** 房产标签（单套房/多套房） */
    private String propertyTag;

    public void setOwnerId(Long ownerId) 
    {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() 
    {
        return ownerId;
    }

    public void setOwnerNo(String ownerNo) 
    {
        this.ownerNo = ownerNo;
    }

    public String getOwnerNo() 
    {
        return ownerNo;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setCommunityId(Long communityId) 
    {
        this.communityId = communityId;
    }

    public Long getCommunityId() 
    {
        return communityId;
    }
    
    public void setPropertyArea(BigDecimal propertyArea)
    {
        this.propertyArea = propertyArea;
    }
    
    public BigDecimal getPropertyArea()
    {
        return propertyArea;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public void setIsOwner(int isOwner)
    {
        this.isOwner = isOwner;
    }

    public int getIsOwner()
    {
        return isOwner;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setBuildingNo(String buildingNo) 
    {
        this.buildingNo = buildingNo;
    }

    public String getBuildingNo() 
    {
        return buildingNo;
    }
    public void setUnitNo(String unitNo) 
    {
        this.unitNo = unitNo;
    }

    public String getUnitNo() 
    {
        return unitNo;
    }
    public void setRoomNo(String roomNo) 
    {
        this.roomNo = roomNo;
    }

    public String getRoomNo() 
    {
        return roomNo;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setMergedProperties(String mergedProperties) 
    {
        this.mergedProperties = mergedProperties;
    }

    public String getMergedProperties() 
    {
        return mergedProperties;
    }

    public void setPropertyCount(Integer propertyCount) 
    {
        this.propertyCount = propertyCount;
    }

    public Integer getPropertyCount() 
    {
        return propertyCount;
    }

    public void setPropertyTag(String propertyTag) 
    {
        this.propertyTag = propertyTag;
    }

    public String getPropertyTag() 
    {
        return propertyTag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ownerId", getOwnerId())
            .append("userId", getUserId())
            .append("communityId", getCommunityId())
            .append("propertyArea", getPropertyArea())
            .append("isOwner", getIsOwner())
            .append("avatar", getAvatar())
            .append("nickName", getNickName())
            .append("userName", getUserName())
            .append("realName", getRealName())
            .append("phonenumber", getPhonenumber())
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
