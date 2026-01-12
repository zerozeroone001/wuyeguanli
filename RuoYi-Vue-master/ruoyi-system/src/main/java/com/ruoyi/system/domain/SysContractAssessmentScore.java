package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业主考核评分对象 sys_contract_assessment_score
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public class SysContractAssessmentScore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long scoreId;

    /** 考核活动ID */
    private Long assessmentId;

    /** 关联合同ID */
    private Long contractId;

    /** 小区ID */
    private Long communityId;

    /** 业主用户ID */
    @Excel(name = "业主ID")
    private Long userId;

    /** 满意度评分（1-5分） */
    @Excel(name = "满意度评分")
    private Integer satisfactionScore;

    /** 完成义务评分（1-5分） */
    @Excel(name = "完成义务评分")
    private Integer obligationScore;

    /** 满意度评价备注 */
    private String satisfactionComment;

    /** 完成义务评价备注 */
    private String obligationComment;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public Long getScoreId() {
        return scoreId;
    }

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSatisfactionScore() {
        return satisfactionScore;
    }

    public void setSatisfactionScore(Integer satisfactionScore) {
        this.satisfactionScore = satisfactionScore;
    }

    public Integer getObligationScore() {
        return obligationScore;
    }

    public void setObligationScore(Integer obligationScore) {
        this.obligationScore = obligationScore;
    }

    public String getSatisfactionComment() {
        return satisfactionComment;
    }

    public void setSatisfactionComment(String satisfactionComment) {
        this.satisfactionComment = satisfactionComment;
    }

    public String getObligationComment() {
        return obligationComment;
    }

    public void setObligationComment(String obligationComment) {
        this.obligationComment = obligationComment;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
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
            .append("scoreId", getScoreId())
            .append("assessmentId", getAssessmentId())
            .append("contractId", getContractId())
            .append("communityId", getCommunityId())
            .append("userId", getUserId())
            .append("satisfactionScore", getSatisfactionScore())
            .append("obligationScore", getObligationScore())
            .append("satisfactionComment", getSatisfactionComment())
            .append("obligationComment", getObligationComment())
            .append("submitTime", getSubmitTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
