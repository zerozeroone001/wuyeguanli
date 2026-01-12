package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 合同履约监督对象 sys_contract_supervision
 *
 * @author ruoyi
 * @date 2026-01-06
 */
public class SysContractSupervision extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 监督ID */
    private Long supervisionId;

    /** 关联合同ID */
    @Excel(name = "合同ID")
    private Long contractId;

    /** 小区ID */
    private Long communityId;

    /** 监督标题 */
    @Excel(name = "监督标题")
    private String title;

    /** 富文本内容 */
    private String content;

    /** 视频文件URL列表(JSON格式) */
    private String videoUrls;

    /** 考核细则文件URL列表(JSON格式) */
    private String rulesUrls;

    /** 考核通知文件URL列表(JSON格式) */
    private String noticeUrls;

    /** 考核表文件URL列表(JSON格式) */
    private String tableUrls;

    /** 考核结果文件URL列表(JSON格式) */
    private String resultUrls;

    /** 是否公示(0否 1是) */
    @Excel(name = "是否公示", readConverterExp = "0=否,1=是")
    private String isPublished;

    /** 公示时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /** 状态(0正常 1停用) */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志(0存在 2删除) */
    private String delFlag;

    /** 合同名称(用于列表显示) */
    private String contractName;

    public Long getSupervisionId() {
        return supervisionId;
    }

    public void setSupervisionId(Long supervisionId) {
        this.supervisionId = supervisionId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(String videoUrls) {
        this.videoUrls = videoUrls;
    }

    public String getRulesUrls() {
        return rulesUrls;
    }

    public void setRulesUrls(String rulesUrls) {
        this.rulesUrls = rulesUrls;
    }

    public String getNoticeUrls() {
        return noticeUrls;
    }

    public void setNoticeUrls(String noticeUrls) {
        this.noticeUrls = noticeUrls;
    }

    public String getTableUrls() {
        return tableUrls;
    }

    public void setTableUrls(String tableUrls) {
        this.tableUrls = tableUrls;
    }

    public String getResultUrls() {
        return resultUrls;
    }

    public void setResultUrls(String resultUrls) {
        this.resultUrls = resultUrls;
    }

    public String getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(String isPublished) {
        this.isPublished = isPublished;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("supervisionId", getSupervisionId())
            .append("contractId", getContractId())
            .append("communityId", getCommunityId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("videoUrls", getVideoUrls())
            .append("rulesUrls", getRulesUrls())
            .append("noticeUrls", getNoticeUrls())
            .append("tableUrls", getTableUrls())
            .append("resultUrls", getResultUrls())
            .append("isPublished", getIsPublished())
            .append("publishTime", getPublishTime())
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
