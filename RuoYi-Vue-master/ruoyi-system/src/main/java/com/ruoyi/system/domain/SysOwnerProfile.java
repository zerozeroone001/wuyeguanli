package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 业主信息扩展对象 sys_owner_profile
 *
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysOwnerProfile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID (主键, 关联sys_user.user_id)
     */
    private Long userId;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名")
    private String realName;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    private String idCardNo;

    /**
     * 身份证正面照片URL
     */
    @Excel(name = "身份证正面照片URL")
    private String idCardFrontUrl;

    /**
     * 身份证反面照片URL
     */
    @Excel(name = "身份证反面照片URL")
    private String idCardBackUrl;

    /**
     * 实名认证状态（0未认证 1待审核 2已认证 3认证失败）
     */
    @Excel(name = "实名认证状态", readConverterExp = "0=未认证,1=待审核,2=已认证,3=认证失败")
    private String authStatus;

    /**
     * 楼栋号
     */
    @Excel(name = "楼栋号")
    private String buildingNo;

    /**
     * 单元号
     */
    @Excel(name = "单元号")
    private String unitNo;

    /**
     * 房号
     */
    @Excel(name = "房号")
    private String roomNo;

    /**
     * 是否为业委会成员（Y是 N否）
     */
    @Excel(name = "是否为业委会成员", readConverterExp = "Y=是,N=否")
    private String isCommitteeMember;

    /**
     * 删除标志（0存在 2删除）
     */
    private String delFlag;
    private boolean authRemark;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardFrontUrl(String idCardFrontUrl) {
        this.idCardFrontUrl = idCardFrontUrl;
    }

    public String getIdCardFrontUrl() {
        return idCardFrontUrl;
    }

    public void setIdCardBackUrl(String idCardBackUrl) {
        this.idCardBackUrl = idCardBackUrl;
    }

    public String getIdCardBackUrl() {
        return idCardBackUrl;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setIsCommitteeMember(String isCommitteeMember) {
        this.isCommitteeMember = isCommitteeMember;
    }

    public String getIsCommitteeMember() {
        return isCommitteeMember;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("realName", getRealName())
                .append("idCardNo", getIdCardNo())
                .append("idCardFrontUrl", getIdCardFrontUrl())
                .append("idCardBackUrl", getIdCardBackUrl())
                .append("authStatus", getAuthStatus())
                .append("authRemark", getAuthRemark())
                .append("buildingNo", getBuildingNo())
                .append("unitNo", getUnitNo())
                .append("roomNo", getRoomNo())
                .append("isCommitteeMember", getIsCommitteeMember())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }

    public boolean getAuthRemark() {
        return authRemark;
    }

    public void setAuthRemark(boolean authRemark) {
        this.authRemark = authRemark;
    }
}
