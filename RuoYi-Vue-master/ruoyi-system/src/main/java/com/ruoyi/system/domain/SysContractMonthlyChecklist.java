package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 月履行清单对象 sys_contract_monthly_checklist
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public class SysContractMonthlyChecklist extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long checklistId;

    /** 关联合同ID */
    @Excel(name = "合同ID")
    private Long contractId;

    /** 小区ID */
    private Long communityId;

    /** 年月（如：2026-01） */
    @Excel(name = "年月")
    private String yearMonth;

    /** 清单名称 */
    @Excel(name = "清单名称")
    private String checklistName;

    /** 清单内容（JSON格式） */
    private String checklistContent;

    /** 清单文件URL */
    private String fileUrl;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    /** 上传人ID */
    private Long uploadUserId;

    /** 物业上传的履行结果文件URL */
    private String propertyFileUrl;

    /** 物业上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date propertyUploadTime;

    /** 物业上传人ID */
    private Long propertyUploadUserId;

    /** 履行状态：pending待填写/incomplete未完全履行/complete完全履行 */
    @Excel(name = "履行状态")
    private String performanceStatus;

    /** 状态：0草稿 1已发布 2物业已下载 3物业已上传 */
    @Excel(name = "状态", readConverterExp = "0=草稿,1=已发布,2=物业已下载,3=物业已上传")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
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

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public String getChecklistContent() {
        return checklistContent;
    }

    public void setChecklistContent(String checklistContent) {
        this.checklistContent = checklistContent;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(Long uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public String getPropertyFileUrl() {
        return propertyFileUrl;
    }

    public void setPropertyFileUrl(String propertyFileUrl) {
        this.propertyFileUrl = propertyFileUrl;
    }

    public Date getPropertyUploadTime() {
        return propertyUploadTime;
    }

    public void setPropertyUploadTime(Date propertyUploadTime) {
        this.propertyUploadTime = propertyUploadTime;
    }

    public Long getPropertyUploadUserId() {
        return propertyUploadUserId;
    }

    public void setPropertyUploadUserId(Long propertyUploadUserId) {
        this.propertyUploadUserId = propertyUploadUserId;
    }

    public String getPerformanceStatus() {
        return performanceStatus;
    }

    public void setPerformanceStatus(String performanceStatus) {
        this.performanceStatus = performanceStatus;
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
            .append("checklistId", getChecklistId())
            .append("contractId", getContractId())
            .append("communityId", getCommunityId())
            .append("yearMonth", getYearMonth())
            .append("checklistName", getChecklistName())
            .append("checklistContent", getChecklistContent())
            .append("fileUrl", getFileUrl())
            .append("uploadTime", getUploadTime())
            .append("uploadUserId", getUploadUserId())
            .append("propertyFileUrl", getPropertyFileUrl())
            .append("propertyUploadTime", getPropertyUploadTime())
            .append("propertyUploadUserId", getPropertyUploadUserId())
            .append("performanceStatus", getPerformanceStatus())
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
