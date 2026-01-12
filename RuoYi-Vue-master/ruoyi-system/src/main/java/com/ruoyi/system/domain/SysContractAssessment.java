package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 合同考核活动对象 sys_contract_assessment
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public class SysContractAssessment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long assessmentId;

    /** 关联合同ID */
    @Excel(name = "合同ID")
    private Long contractId;

    /** 小区ID */
    private Long communityId;

    /** 考核年度（如：2026） */
    @Excel(name = "考核年度")
    private String assessmentYear;

    /** 考核名称 */
    @Excel(name = "考核名称")
    private String assessmentName;

    /** 考核说明 */
    private String assessmentDesc;

    /** 考核开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 考核结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 满意度平均分（1-5分） */
    @Excel(name = "满意度平均分")
    private BigDecimal satisfactionAvgScore;

    /** 完成义务平均分（1-5分） */
    @Excel(name = "完成义务平均分")
    private BigDecimal obligationAvgScore;

    /** 参与人数 */
    @Excel(name = "参与人数")
    private Integer totalParticipants;

    /** 考核报告文件URL */
    private String fileUrl;

    /** 状态：0未开始 1进行中 2已结束 */
    @Excel(name = "状态", readConverterExp = "0=未开始,1=进行中,2=已结束")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getAssessmentYear() {
        return assessmentYear;
    }

    public void setAssessmentYear(String assessmentYear) {
        this.assessmentYear = assessmentYear;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getAssessmentDesc() {
        return assessmentDesc;
    }

    public void setAssessmentDesc(String assessmentDesc) {
        this.assessmentDesc = assessmentDesc;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getSatisfactionAvgScore() {
        return satisfactionAvgScore;
    }

    public void setSatisfactionAvgScore(BigDecimal satisfactionAvgScore) {
        this.satisfactionAvgScore = satisfactionAvgScore;
    }

    public BigDecimal getObligationAvgScore() {
        return obligationAvgScore;
    }

    public void setObligationAvgScore(BigDecimal obligationAvgScore) {
        this.obligationAvgScore = obligationAvgScore;
    }

    public Integer getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(Integer totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("assessmentId", getAssessmentId())
            .append("contractId", getContractId())
            .append("communityId", getCommunityId())
            .append("assessmentYear", getAssessmentYear())
            .append("assessmentName", getAssessmentName())
            .append("assessmentDesc", getAssessmentDesc())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("satisfactionAvgScore", getSatisfactionAvgScore())
            .append("obligationAvgScore", getObligationAvgScore())
            .append("totalParticipants", getTotalParticipants())
            .append("fileUrl", getFileUrl())
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
