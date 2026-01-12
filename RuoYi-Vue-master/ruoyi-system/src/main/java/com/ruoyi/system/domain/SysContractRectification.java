package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 整改记录对象 sys_contract_rectification
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public class SysContractRectification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long rectificationId;

    /** 关联合同ID */
    @Excel(name = "合同ID")
    private Long contractId;

    /** 小区ID */
    private Long communityId;

    /** 来源类型：monthly_checklist(月履行)/assessment(考核) */
    @Excel(name = "来源类型")
    private String sourceType;

    /** 来源ID（月履行清单ID或考核ID） */
    private Long sourceId;

    /** 整改通知内容 */
    private String noticeContent;

    /** 整改通知文件URL */
    private String noticeFileUrl;

    /** 通知时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date noticeTime;

    /** 通知人ID（业委会） */
    private Long noticeUserId;

    /** 整改结果内容 */
    private String resultContent;

    /** 整改结果文件URL */
    private String resultFileUrl;

    /** 整改完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resultTime;

    /** 提交整改结果人ID（物业） */
    private Long resultUserId;

    /** 状态：pending待整改/submitted已提交/accepted已验收/rejected驳回 */
    @Excel(name = "状态")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public Long getRectificationId() {
        return rectificationId;
    }

    public void setRectificationId(Long rectificationId) {
        this.rectificationId = rectificationId;
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

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeFileUrl() {
        return noticeFileUrl;
    }

    public void setNoticeFileUrl(String noticeFileUrl) {
        this.noticeFileUrl = noticeFileUrl;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public Long getNoticeUserId() {
        return noticeUserId;
    }

    public void setNoticeUserId(Long noticeUserId) {
        this.noticeUserId = noticeUserId;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public String getResultFileUrl() {
        return resultFileUrl;
    }

    public void setResultFileUrl(String resultFileUrl) {
        this.resultFileUrl = resultFileUrl;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public Long getResultUserId() {
        return resultUserId;
    }

    public void setResultUserId(Long resultUserId) {
        this.resultUserId = resultUserId;
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
            .append("rectificationId", getRectificationId())
            .append("contractId", getContractId())
            .append("communityId", getCommunityId())
            .append("sourceType", getSourceType())
            .append("sourceId", getSourceId())
            .append("noticeContent", getNoticeContent())
            .append("noticeFileUrl", getNoticeFileUrl())
            .append("noticeTime", getNoticeTime())
            .append("noticeUserId", getNoticeUserId())
            .append("resultContent", getResultContent())
            .append("resultFileUrl", getResultFileUrl())
            .append("resultTime", getResultTime())
            .append("resultUserId", getResultUserId())
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
