package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物业服务合同对象 sys_property_contract
 *
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysPropertyContract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 合同ID */
    private Long contractId;

    /** 合同编号 */
    @Excel(name = "合同编号")
    private String contractNo;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 合同名称 */
    @Excel(name = "合同名称")
    private String contractName;

    /** 合同版本 */
    @Excel(name = "合同版本")
    private String contractVersion;

    /** 合同文件URL */
    @Excel(name = "合同文件URL")
    private String fileUrl;

    /** 生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生效日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date effectiveDate;

    /** 失效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "失效日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /** 合同金额 */
    @Excel(name = "合同金额")
    private BigDecimal contractAmount;

    /** 甲方 */
    @Excel(name = "甲方")
    private String partyA;

    /** 乙方 */
    @Excel(name = "乙方")
    private String partyB;

    /** 合同内容 */
    private String contractContent;

    /** 重要条款 */
    private String importantClauses;

    /** 负责人姓名 */
    @Excel(name = "负责人姓名")
    private String managerName;

    /** 负责人电话 */
    @Excel(name = "负责人电话")
    private String managerPhone;

    /** 状态（0正常 1已归档） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=已归档")
    private String status;

    /** 合同类型：customize(定制)/review(审核)/modify(修改) */
    @Excel(name = "合同类型")
    private String contractType;

    /** 合同阶段状态码 */
    @Excel(name = "合同阶段")
    private String contractStage;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 申请人ID */
    private Long applyUserId;

    /** 受理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date acceptTime;

    /** 受理人ID */
    private Long acceptUserId;

    /** 完成时间（定制/审核/修改完成时间） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    /** 履行状态：incomplete(未完全履行)/complete(完全履行) */
    private String performanceStatus;

    /** 查验清单文件URL */
    private String entryChecklistUrl;

    /** 物业上传的查验清单URL */
    private String propertyEntryChecklistUrl;

    /** 月履行清单文件URL */
    private String monthlyChecklistUrl;

    /** 物业上传的月履行清单URL */
    private String propertyMonthlyChecklistUrl;

    /** 整改通知单文件URL */
    private String rectificationNoticeUrl;

    /** 整改结果评定通知书文件URL */
    private String rectificationResultUrl;

    /** 年度履行报告文件URL */
    private String annualReportUrl;

    /** 删除标志（0存在 2删除） */
    private String delFlag;
    private List<SysFileInfo> fileList;
    private List<Long> fileIds;

    public void setContractId(Long contractId)
    {
        this.contractId = contractId;
    }

    public Long getContractId()
    {
        return contractId;
    }

    public void setContractNo(String contractNo)
    {
        this.contractNo = contractNo;
    }

    public String getContractNo()
    {
        return contractNo;
    }

    public Long getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }

    public void setContractName(String contractName)
    {
        this.contractName = contractName;
    }

    public String getContractName()
    {
        return contractName;
    }

    public void setContractVersion(String contractVersion)
    {
        this.contractVersion = contractVersion;
    }

    public String getContractVersion()
    {
        return contractVersion;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setEffectiveDate(Date effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }

    public Date getEffectiveDate()
    {
        return effectiveDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setContractAmount(BigDecimal contractAmount)
    {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getContractAmount()
    {
        return contractAmount;
    }

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    public String getContractContent() {
        return contractContent;
    }

    public void setContractContent(String contractContent) {
        this.contractContent = contractContent;
    }

    public String getImportantClauses() {
        return importantClauses;
    }

    public void setImportantClauses(String importantClauses) {
        this.importantClauses = importantClauses;
    }

    public String getManagerName()
    {
        return managerName;
    }

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }

    public String getManagerPhone()
    {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone)
    {
        this.managerPhone = managerPhone;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public String getContractType()
    {
        return contractType;
    }

    public void setContractType(String contractType)
    {
        this.contractType = contractType;
    }

    public String getContractStage()
    {
        return contractStage;
    }

    public void setContractStage(String contractStage)
    {
        this.contractStage = contractStage;
    }

    public Date getApplyTime()
    {
        return applyTime;
    }

    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
    }

    public Long getApplyUserId()
    {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId)
    {
        this.applyUserId = applyUserId;
    }

    public Date getAcceptTime()
    {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime)
    {
        this.acceptTime = acceptTime;
    }

    public Long getAcceptUserId()
    {
        return acceptUserId;
    }

    public void setAcceptUserId(Long acceptUserId)
    {
        this.acceptUserId = acceptUserId;
    }

    public Date getCompleteTime()
    {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime)
    {
        this.completeTime = completeTime;
    }

    public String getPerformanceStatus()
    {
        return performanceStatus;
    }

    public void setPerformanceStatus(String performanceStatus)
    {
        this.performanceStatus = performanceStatus;
    }

    public String getEntryChecklistUrl()
    {
        return entryChecklistUrl;
    }

    public void setEntryChecklistUrl(String entryChecklistUrl)
    {
        this.entryChecklistUrl = entryChecklistUrl;
    }

    public String getPropertyEntryChecklistUrl()
    {
        return propertyEntryChecklistUrl;
    }

    public void setPropertyEntryChecklistUrl(String propertyEntryChecklistUrl)
    {
        this.propertyEntryChecklistUrl = propertyEntryChecklistUrl;
    }

    public String getMonthlyChecklistUrl()
    {
        return monthlyChecklistUrl;
    }

    public void setMonthlyChecklistUrl(String monthlyChecklistUrl)
    {
        this.monthlyChecklistUrl = monthlyChecklistUrl;
    }

    public String getPropertyMonthlyChecklistUrl()
    {
        return propertyMonthlyChecklistUrl;
    }

    public void setPropertyMonthlyChecklistUrl(String propertyMonthlyChecklistUrl)
    {
        this.propertyMonthlyChecklistUrl = propertyMonthlyChecklistUrl;
    }

    public String getRectificationNoticeUrl()
    {
        return rectificationNoticeUrl;
    }

    public void setRectificationNoticeUrl(String rectificationNoticeUrl)
    {
        this.rectificationNoticeUrl = rectificationNoticeUrl;
    }

    public String getRectificationResultUrl()
    {
        return rectificationResultUrl;
    }

    public void setRectificationResultUrl(String rectificationResultUrl)
    {
        this.rectificationResultUrl = rectificationResultUrl;
    }

    public String getAnnualReportUrl()
    {
        return annualReportUrl;
    }

    public void setAnnualReportUrl(String annualReportUrl)
    {
        this.annualReportUrl = annualReportUrl;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public List<SysFileInfo> getFileList()
    {
        return fileList;
    }

    public void setFileList(List<SysFileInfo> fileList)
    {
        this.fileList = fileList;
    }

    public List<Long> getFileIds()
    {
        return fileIds;
    }

    public void setFileIds(List<Long> fileIds)
    {
        this.fileIds = fileIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("contractId", getContractId())
            .append("contractNo", getContractNo())
            .append("communityId", getCommunityId())
            .append("contractName", getContractName())
            .append("contractVersion", getContractVersion())
            .append("fileUrl", getFileUrl())
            .append("effectiveDate", getEffectiveDate())
            .append("expiryDate", getExpiryDate())
            .append("contractAmount", getContractAmount())
            .append("partyA", getPartyA())
            .append("partyB", getPartyB())
            .append("contractContent", getContractContent())
            .append("importantClauses", getImportantClauses())
            .append("managerName", getManagerName())
            .append("managerPhone", getManagerPhone())
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
