package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 房产信息对象 estate_property
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
public class EstateProperty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房产ID */
    private Long propertyId;

    /** 所属小区ID */
    @Excel(name = "所属小区ID")
    private Long communityId;

    /** 小区名称 */
    private String communityName;

    /** 小区地址 */
    private String communityAddress;

    /** 楼栋名称 */
    @Excel(name = "楼栋名称")
    private String buildingName;

    /** 单元名称 */
    @Excel(name = "单元名称")
    private String unitName;

    /** 房号 */
    @Excel(name = "房号")
    private String roomNumber;

    /** 所在楼层 */
    @Excel(name = "所在楼层")
    private Long floor;

    /** 建筑面积（平方米） */
    @Excel(name = "建筑面积", readConverterExp = "平=方米")
    private BigDecimal area;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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

    public void setBuildingName(String buildingName) 
    {
        this.buildingName = buildingName;
    }

    public String getBuildingName() 
    {
        return buildingName;
    }

    public void setUnitName(String unitName) 
    {
        this.unitName = unitName;
    }

    public String getUnitName() 
    {
        return unitName;
    }

    public void setRoomNumber(String roomNumber) 
    {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() 
    {
        return roomNumber;
    }

    public void setFloor(Long floor) 
    {
        this.floor = floor;
    }

    public Long getFloor() 
    {
        return floor;
    }

    public void setArea(BigDecimal area) 
    {
        this.area = area;
    }

    public BigDecimal getArea() 
    {
        return area;
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
            .append("propertyId", getPropertyId())
            .append("communityId", getCommunityId())
            .append("communityName", getCommunityName())
            .append("communityAddress", getCommunityAddress())
            .append("buildingName", getBuildingName())
            .append("unitName", getUnitName())
            .append("roomNumber", getRoomNumber())
            .append("floor", getFloor())
            .append("area", getArea())
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
