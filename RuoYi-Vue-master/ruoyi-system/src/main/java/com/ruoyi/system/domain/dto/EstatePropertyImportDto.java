package com.ruoyi.system.domain.dto;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;

/**
 * 房产信息导入DTO
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public class EstatePropertyImportDto
{
    /** 小区名称 */
    @Excel(name = "小区名称")
    private String communityName;

    /** 楼栋号 */
    @Excel(name = "楼栋号")
    private String buildingName;

    /** 单元号 */
    @Excel(name = "单元号")
    private String unitName;

    /** 楼层 */
    @Excel(name = "楼层")
    private Long floor;

    /** 房号 */
    @Excel(name = "房号")
    private String roomNumber;

    /** 建筑面积 */
    @Excel(name = "建筑面积")
    private BigDecimal area;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public String getCommunityName()
    {
        return communityName;
    }

    public void setCommunityName(String communityName)
    {
        this.communityName = communityName;
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

    public Long getFloor()
    {
        return floor;
    }

    public void setFloor(Long floor)
    {
        this.floor = floor;
    }

    public String getRoomNumber()
    {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber)
    {
        this.roomNumber = roomNumber;
    }

    public BigDecimal getArea()
    {
        return area;
    }

    public void setArea(BigDecimal area)
    {
        this.area = area;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
