package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 管理员对象 sys_admin
 * 
 * @author ruoyi
 * @date 2025-12-27
 */
public class SysAdmin extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 管理员ID */
    private Long adminId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 管理员姓名 */
    @Excel(name = "管理员姓名")
    private String adminName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 用户名（关联查询字段，非表字段） */
    @Excel(name = "用户名")
    private String userName;

    /** 小区名称（关联查询字段，非表字段） */
    @Excel(name = "小区名称")
    private String communityName;

    public void setAdminId(Long adminId) 
    {
        this.adminId = adminId;
    }

    public Long getAdminId() 
    {
        return adminId;
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

    public void setAdminName(String adminName) 
    {
        this.adminName = adminName;
    }

    public String getAdminName() 
    {
        return adminName;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getCommunityName() 
    {
        return communityName;
    }

    public void setCommunityName(String communityName) 
    {
        this.communityName = communityName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("adminId", getAdminId())
            .append("userId", getUserId())
            .append("communityId", getCommunityId())
            .append("adminName", getAdminName())
            .append("phone", getPhone())
            .append("userName", getUserName())
            .append("communityName", getCommunityName())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
