package com.ruoyi.system.domain.dto;

import com.ruoyi.common.annotation.Excel;

/**
 * 业主信息导入DTO对象
 */
public class OwnerProfileImportDto
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "姓名")
    private String userName;

    @Excel(name = "电话")
    private String phonenumber;

    @Excel(name = "标签")
    private String tagName;

    @Excel(name = "小区名称")
    private String communityName;

    @Excel(name = "楼栋")
    private String buildingNo;

    @Excel(name = "单元")
    private String unitNo;

    @Excel(name = "房号")
    private String roomNo;

    @Excel(name = "备注")
    private String remark;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}