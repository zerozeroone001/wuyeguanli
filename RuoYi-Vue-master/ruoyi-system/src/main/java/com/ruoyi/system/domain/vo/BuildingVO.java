package com.ruoyi.system.domain.vo;

/**
 * 楼栋信息视图对象
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
public class BuildingVO
{
    /** 楼栋名称 */
    private String buildingName;

    /** 是否有单元 */
    private Boolean hasUnit;

    /** 房屋数量 */
    private Integer roomCount;

    public String getBuildingName() 
    {
        return buildingName;
    }

    public void setBuildingName(String buildingName) 
    {
        this.buildingName = buildingName;
    }

    public Boolean getHasUnit() 
    {
        return hasUnit;
    }

    public void setHasUnit(Boolean hasUnit) 
    {
        this.hasUnit = hasUnit;
    }

    public Integer getRoomCount() 
    {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) 
    {
        this.roomCount = roomCount;
    }
}
