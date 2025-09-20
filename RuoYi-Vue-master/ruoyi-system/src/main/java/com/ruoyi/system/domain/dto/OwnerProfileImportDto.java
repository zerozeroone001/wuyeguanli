package com.ruoyi.system.domain.dto;

import com.ruoyi.common.annotation.Excel;

/**
 * 业主信息导入DTO对象
 */
public class OwnerProfileImportDto
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "姓名")
    private String realName;

    @Excel(name = "手机号码")
    private String phonenumber;

    @Excel(name = "身份证号")
    private String idCardNo;

    @Excel(name = "小区")
    private String communityName;

    @Excel(name = "门牌号")
    private String houseNumber;

    @Excel(name = "备注")
    private String remark;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}