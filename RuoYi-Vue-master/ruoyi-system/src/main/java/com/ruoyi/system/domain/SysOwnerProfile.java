package com.ruoyi.system.domain;

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

    /** 用户ID (外键, 可选) */
    private Long userId;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCardNo;

    /** 身份证正面照片URL */
    private String idCardFrontUrl;

    /** 身份证反面照片URL */
    private String idCardBackUrl;

    /** 实名认证状态（0未认证 1待审核 2已认证 3认证失败） */
    private int authStatus;

    /** 楼栋号 */
    private String buildingNo;

    /** 单元号 */
    private String unitNo;

    /** 房号 */
    private String roomNo;

    /** 是否为业委会成员（Y是 N否） */
    private String isCommitteeMember;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    /** 审核备注（例如，失败原因） */
    private String authRemark;

    /** 手机号码 */
    private String phonenumber;

    /** 联系号码 */
    private String contactNumber;

    /** 是否业主（0否 1业主 2业委会） */
    private int isOwner;

    /** 用户头像 */
    private String avatar;

    /** 用户昵称 */
    private String nickName;

    /** 账号状态（0正常 1停用） */
    private String status;

    public void setOwnerId(Long ownerId) 
    {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() 
    {
        return ownerId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }
    public void setIdCardNo(String idCardNo) 
    {
        this.idCardNo = idCardNo;
    }

    public String getIdCardNo() 
    {
        return idCardNo;
    }
    public void setIdCardFrontUrl(String idCardFrontUrl) 
    {
        this.idCardFrontUrl = idCardFrontUrl;
    }

    public String getIdCardFrontUrl() 
    {
        return idCardFrontUrl;
    }
    public void setIdCardBackUrl(String idCardBackUrl) 
    {
        this.idCardBackUrl = idCardBackUrl;
    }

    public String getIdCardBackUrl() 
    {
        return idCardBackUrl;
    }
    public void setAuthStatus(int authStatus)
    {
        this.authStatus = authStatus;
    }

    public int getAuthStatus()
    {
        return authStatus;
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
    public void setIsCommitteeMember(String isCommitteeMember) 
    {
        this.isCommitteeMember = isCommitteeMember;
    }

    public String getIsCommitteeMember() 
    {
        return isCommitteeMember;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setAuthRemark(String authRemark) 
    {
        this.authRemark = authRemark;
    }

    public String getAuthRemark() 
    {
        return authRemark;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setContactNumber(String contactNumber) 
    {
        this.contactNumber = contactNumber;
    }

    public String getContactNumber() 
    {
        return contactNumber;
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

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ownerId", getOwnerId())
            .append("userId", getUserId())
            .append("realName", getRealName())
            .append("idCardNo", getIdCardNo())
            .append("idCardFrontUrl", getIdCardFrontUrl())
            .append("idCardBackUrl", getIdCardBackUrl())
            .append("authStatus", getAuthStatus())
            .append("buildingNo", getBuildingNo())
            .append("unitNo", getUnitNo())
            .append("roomNo", getRoomNo())
            .append("isCommitteeMember", getIsCommitteeMember())
            .append("phonenumber", getPhonenumber())
            .append("contactNumber", getContactNumber())
            .append("isOwner", getIsOwner())
            .append("avatar", getAvatar())
            .append("nickName", getNickName())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("authRemark", getAuthRemark())
            .toString();
    }
}
