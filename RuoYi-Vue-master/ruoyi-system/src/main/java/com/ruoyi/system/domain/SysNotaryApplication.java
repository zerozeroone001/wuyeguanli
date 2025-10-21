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

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

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

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    /** 完成时间 */
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date completedTime;

    /** 拒绝原因 */
    @Excel(name = "拒绝原因")
    private String rejectedReason;

    /** 公证费用 */
    @Excel(name = "公证费用")
    private java.math.BigDecimal feeAmount;

    /** 支付状态（0未支付 1已支付 2已退款） */
    @Excel(name = "支付状态", readConverterExp = "0=未支付,1=已支付,2=已退款")
    private String paymentStatus;

    /** 支付时间 */
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    /** 支付方式 */
    @Excel(name = "支付方式")
    private String paymentMethod;

    /** 交易流水号 */
    @Excel(name = "交易流水号")
    private String transactionId;

    /** 审核人员ID */
    @Excel(name = "审核人员ID")
    private Long reviewerId;

    /** 审核时间 */
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String reviewComment;

    /** 公证员ID */
    @Excel(name = "公证员ID")
    private Long notaryPersonId;

    /** 公证书编号 */
    @Excel(name = "公证书编号")
    private String certificateNo;

    /** 证书类型 */
    @Excel(name = "证书类型")
    private String certificateType;

    /** 有效期（天数） */
    @Excel(name = "有效期（天数）")
    private Integer validityPeriod;

    /** 过期时间 */
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;

    /** 是否加急（0否 1是） */
    @Excel(name = "是否加急", readConverterExp = "0=否,1=是")
    private String isUrgent;

    /** 加急费用 */
    @Excel(name = "加急费用")
    private java.math.BigDecimal urgentFee;

    /** 公证处名称 */
    @Excel(name = "公证处名称")
    private String notaryOfficeName;

    /** 申请人类型（individual个人 company企业） */
    @Excel(name = "申请人类型", readConverterExp = "individual=个人,company=企业")
    private String applicantType;

    /** 企业名称 */
    @Excel(name = "企业名称")
    private String companyName;

    /** 法定代表人 */
    @Excel(name = "法定代表人")
    private String legalRepresentative;

    /** 营业执照号 */
    @Excel(name = "营业执照号")
    private String businessLicense;

    /** 联系邮箱 */
    @Excel(name = "联系邮箱")
    private String contactEmail;

    /** 紧急联系人 */
    @Excel(name = "紧急联系人")
    private String emergencyContact;

    /** 紧急联系电话 */
    @Excel(name = "紧急联系电话")
    private String emergencyPhone;

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

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getCommunityId() {
        return communityId;
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

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setCompletedTime(Date completedTime) {
        this.completedTime = completedTime;
    }

    public Date getCompletedTime() {
        return completedTime;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setFeeAmount(java.math.BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public java.math.BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setNotaryPersonId(Long notaryPersonId) {
        this.notaryPersonId = notaryPersonId;
    }

    public Long getNotaryPersonId() {
        return notaryPersonId;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Integer getValidityPeriod() {
        return validityPeriod;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setUrgentFee(java.math.BigDecimal urgentFee) {
        this.urgentFee = urgentFee;
    }

    public java.math.BigDecimal getUrgentFee() {
        return urgentFee;
    }

    public void setNotaryOfficeName(String notaryOfficeName) {
        this.notaryOfficeName = notaryOfficeName;
    }

    public String getNotaryOfficeName() {
        return notaryOfficeName;
    }

    public void setApplicantType(String applicantType) {
        this.applicantType = applicantType;
    }

    public String getApplicantType() {
        return applicantType;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
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
                .append("communityId", getCommunityId())
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
