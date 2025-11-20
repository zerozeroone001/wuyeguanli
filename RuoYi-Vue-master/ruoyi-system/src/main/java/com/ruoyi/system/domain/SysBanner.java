package com.ruoyi.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 轮播图管理对象 sys_banner
 *
 * @author ruoyi
 */
public class SysBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 轮播图ID */
    private Long bannerId;

    /** 轮播图标题 */
    private String bannerTitle;

    /** 轮播图图片URL */
    private String bannerImage;

    /** 链接类型：meeting-业主大会，opinion-意见征询，url-自定义链接 */
    private String linkType;

    /** 关联的业务ID（业主大会ID或意见征询ID） */
    private Long linkId;

    /** 自定义链接URL */
    private String linkUrl;

    /** 显示顺序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    /** 小区ID */
    private Long communityId;

    public void setBannerId(Long bannerId)
    {
        this.bannerId = bannerId;
    }

    public Long getBannerId()
    {
        return bannerId;
    }

    public void setBannerTitle(String bannerTitle)
    {
        this.bannerTitle = bannerTitle;
    }

    @NotBlank(message = "轮播图标题不能为空")
    @Size(min = 0, max = 100, message = "轮播图标题不能超过100个字符")
    public String getBannerTitle()
    {
        return bannerTitle;
    }

    public void setBannerImage(String bannerImage)
    {
        this.bannerImage = bannerImage;
    }

    @NotBlank(message = "轮播图图片不能为空")
    @Size(min = 0, max = 500, message = "轮播图图片URL不能超过500个字符")
    public String getBannerImage()
    {
        return bannerImage;
    }

    public void setLinkType(String linkType)
    {
        this.linkType = linkType;
    }

    public String getLinkType()
    {
        return linkType;
    }

    public void setLinkId(Long linkId)
    {
        this.linkId = linkId;
    }

    public Long getLinkId()
    {
        return linkId;
    }

    public void setLinkUrl(String linkUrl)
    {
        this.linkUrl = linkUrl;
    }

    public String getLinkUrl()
    {
        return linkUrl;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }

    public Long getCommunityId()
    {
        return communityId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("bannerId", getBannerId())
            .append("bannerTitle", getBannerTitle())
            .append("bannerImage", getBannerImage())
            .append("linkType", getLinkType())
            .append("linkId", getLinkId())
            .append("linkUrl", getLinkUrl())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("communityId", getCommunityId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
