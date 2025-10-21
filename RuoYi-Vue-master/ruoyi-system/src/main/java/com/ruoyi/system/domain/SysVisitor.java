package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 访客登记对象 sys_visitor
 *
 * @author ruoyi
 * @date 2025-08-25
 */
public class SysVisitor extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 访客ID */
    private Long visitorId;

    /** 访客姓名 */
    @Excel(name = "访客姓名")
    @NotBlank(message = "访客姓名不能为空")
    private String visitorName;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 访客手机号 */
    @Excel(name = "访客手机号")
    @NotBlank(message = "访客手机号不能为空")
    @Size(min = 0, max = 11, message = "访客手机号长度不能超过11个字符")
    private String visitorPhone;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 来访日期 */
    @Excel(name = "来访日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date visitDate;

    /** 事由 */
    @Excel(name = "事由")
    @NotBlank(message = "事由不能为空")
    private String reason;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlate;

    /** 状态（0待审核 1已通过 2已拒绝） */
    @Excel(name = "状态", readConverterExp = "0=待审核,1=已通过,2=已拒绝")
    private String status;

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("visitorId", getVisitorId())
                .append("visitorName", getVisitorName())
                .append("communityId", getCommunityId())
                .append("visitorPhone", getVisitorPhone())
                .append("idCard", getIdCard())
                .append("visitDate", getVisitDate())
                .append("reason", getReason())
                .append("licensePlate", getLicensePlate())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
