package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公证服务申请对象 sys_notary_application
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysNotaryApplication extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    private Long applicationId;

    /** 申请人用户ID (关联sys_user) */
    @Excel(name = "申请人用户ID (关联sys_user)")
    private Long userId;

    /** 申请公证的文件类型或名称 */
    @Excel(name = "申请公证的文件类型或名称")
    private String fileType;

    /** 相关文件URL (可选) */
    @Excel(name = "相关文件URL (可选)")
    private String fileUrl;

    /** 申请信息 (JSON格式) */
    @Excel(name = "申请信息 (JSON格式)")
    private String applicationInfo;

    /** 支付状态（0待支付 1已支付 2已退款） */
    @Excel(name = "支付状态", readConverterExp = "0=待支付,1=已支付,2=已退款")
    private String paymentStatus;

    /** 支付单号 (关联微信支付) */
    @Excel(name = "支付单号 (关联微信支付)")
    private String paymentNo;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private BigDecimal paymentAmount;

    /** 办理状态（0待受理 1办理中 2已完成 3已驳回） */
    @Excel(name = "办理状态", readConverterExp = "0=待受理,1=办理中,2=已完成,3=已驳回")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setApplicationId(Long applicationId) 
    {
        this.applicationId = applicationId;
    }

    public Long getApplicationId() 
    {
        return applicationId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }

    public void setApplicationInfo(String applicationInfo) 
    {
        this.applicationInfo = applicationInfo;
    }

    public String getApplicationInfo() 
    {
        return applicationInfo;
    }

    public void setPaymentStatus(String paymentStatus) 
    {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() 
    {
        return paymentStatus;
    }

    public void setPaymentNo(String paymentNo) 
    {
        this.paymentNo = paymentNo;
    }

    public String getPaymentNo() 
    {
        return paymentNo;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) 
    {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPaymentAmount() 
    {
        return paymentAmount;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("applicationId", getApplicationId())
            .append("userId", getUserId())
            .append("fileType", getFileType())
            .append("fileUrl", getFileUrl())
            .append("applicationInfo", getApplicationInfo())
            .append("paymentStatus", getPaymentStatus())
            .append("paymentNo", getPaymentNo())
            .append("paymentAmount", getPaymentAmount())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
