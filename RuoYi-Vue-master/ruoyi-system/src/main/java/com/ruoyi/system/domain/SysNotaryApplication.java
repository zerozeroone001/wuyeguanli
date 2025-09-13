package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 公证服务申请对象 sys_notary_application
 *
 * @author ruoyi
 * @date 2025-09-11
 */
public class SysNotaryApplication extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公证ID */
    private Long notaryId;

    /** 公证业务编号 */
    @Excel(name = "公证业务编号")
    private String notaryNo;

    /** 公证标题 */
    @Excel(name = "公证标题")
    private String title;

    /** 公证类型 */
    @Excel(name = "公证类型")
    private String type;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 是否加急 (0-否, 1-是) */
    @Excel(name = "是否加急", readConverterExp = "0=否,1=是")
    private Integer urgent;

    /** 申请说明 */
    @Excel(name = "申请说明")
    private String description;

    /** 联系人姓名 */
    @Excel(name = "联系人姓名")
    private String contactName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 通讯地址 */
    @Excel(name = "通讯地址")
    private String address;

    /** 关联公证处ID */
    @Excel(name = "关联公证处ID")
    private Long notaryOfficeId;

    /** 申请人ID */
    @Excel(name = "申请人ID")
    private Long userId;

    /** 办理进度百分比 */
    @Excel(name = "办理进度百分比")
    private Integer progress;

    /** 电子公证证书URL */
    @Excel(name = "电子公证证书URL")
    private String certificateUrl;

    /** 预计完成时间 */
    @Excel(name = "预计完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expectedTime;

    /** 申请时间 */
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    public Long getNotaryId() {
        return notaryId;
    }

    public void setNotaryId(Long notaryId) {
        this.notaryId = notaryId;
    }

    public String getNotaryNo() {
        return notaryNo;
    }

    public void setNotaryNo(String notaryNo) {
        this.notaryNo = notaryNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUrgent() {
        return urgent;
    }

    public void setUrgent(Integer urgent) {
        this.urgent = urgent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getNotaryOfficeId() {
        return notaryOfficeId;
    }

    public void setNotaryOfficeId(Long notaryOfficeId) {
        this.notaryOfficeId = notaryOfficeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public Date getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(Date expectedTime) {
        this.expectedTime = expectedTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("notaryId", getNotaryId())
                .append("notaryNo", getNotaryNo())
                .append("title", getTitle())
                .append("type", getType())
                .append("status", getStatus())
                .append("urgent", getUrgent())
                .append("description", getDescription())
                .append("contactName", getContactName())
                .append("contactPhone", getContactPhone())
                .append("idCard", getIdCard())
                .append("address", getAddress())
                .append("notaryOfficeId", getNotaryOfficeId())
                .append("userId", getUserId())
                .append("progress", getProgress())
                .append("certificateUrl", getCertificateUrl())
                .append("expectedTime", getExpectedTime())
                .append("applyTime", getApplyTime())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}