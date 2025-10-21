package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资金流水对象 sys_property_fund_flow
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysPropertyFundFlow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水ID */
    private Long flowId;

    /** 流水编号 */
    @Excel(name = "流水编号")
    private String flowNo;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 流水类型(1-经营性收入,2-经营性支出,3-维修资金收入,4-维修资金支出) */
    @Excel(name = "流水类型(1-经营性收入,2-经营性支出,3-维修资金收入,4-维修资金支出)")
    private String flowType;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 经手人 */
    @Excel(name = "经手人")
    private String handler;

    /** 审批状态(0-待审批,1-已审批,2-已拒绝) */
    @Excel(name = "审批状态(0-待审批,1-已审批,2-已拒绝)")
    private String approvalStatus;

    /** 审批人ID */
    @Excel(name = "审批人ID")
    private Long approverId;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approvalTime;

    /** 审批备注 */
    @Excel(name = "审批备注")
    private String approvalNote;

    /** 流水日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "流水日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date flowDate;

    public void setFlowId(Long flowId) 
    {
        this.flowId = flowId;
    }

    public Long getFlowId() 
    {
        return flowId;
    }

    public void setFlowNo(String flowNo) 
    {
        this.flowNo = flowNo;
    }

    public String getFlowNo() 
    {
        return flowNo;
    }

    public Long getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }

    public void setFlowType(String flowType) 
    {
        this.flowType = flowType;
    }

    public String getFlowType() 
    {
        return flowType;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setHandler(String handler) 
    {
        this.handler = handler;
    }

    public String getHandler() 
    {
        return handler;
    }

    public void setApprovalStatus(String approvalStatus) 
    {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalStatus() 
    {
        return approvalStatus;
    }

    public void setApproverId(Long approverId) 
    {
        this.approverId = approverId;
    }

    public Long getApproverId() 
    {
        return approverId;
    }

    public void setApprovalTime(Date approvalTime) 
    {
        this.approvalTime = approvalTime;
    }

    public Date getApprovalTime() 
    {
        return approvalTime;
    }

    public void setApprovalNote(String approvalNote) 
    {
        this.approvalNote = approvalNote;
    }

    public String getApprovalNote() 
    {
        return approvalNote;
    }

    public void setFlowDate(Date flowDate) 
    {
        this.flowDate = flowDate;
    }

    public Date getFlowDate() 
    {
        return flowDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("flowId", getFlowId())
            .append("flowNo", getFlowNo())
            .append("communityId", getCommunityId())
            .append("flowType", getFlowType())
            .append("amount", getAmount())
            .append("title", getTitle())
            .append("description", getDescription())
            .append("handler", getHandler())
            .append("approvalStatus", getApprovalStatus())
            .append("approverId", getApproverId())
            .append("approvalTime", getApprovalTime())
            .append("approvalNote", getApprovalNote())
            .append("flowDate", getFlowDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
