package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物业制度管理对象 sys_property_regulation
 * 
 * @author ruoyi
 * @date 2025-09-13
 */
public class SysPropertyRegulation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 制度ID */
    private Long regulationId;

    /** 制度名称 */
    @Excel(name = "制度名称")
    private String regulationName;

    /** 制度分类ID */
    @Excel(name = "制度分类ID")
    private Long categoryId;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 制度类型 */
    @Excel(name = "制度类型")
    private String regulationType;

    /** 制度摘要 */
    private String summary;

    /** 发布部门 */
    @Excel(name = "发布部门")
    private String publishDept;

    /** 是否重要(0-否, 1-是) */
    @Excel(name = "是否重要", readConverterExp = "0=否,1=是")
    private Integer isImportant;

    /** 结构化内容(章节、条款) */
    private String chapters;

    /** 修订历史 */
    private String revisionHistory;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    /** 生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生效日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date effectiveDate;

    /** 版本号 */
    @Excel(name = "版本号")
    private String version;

    /** 状态(draft-草稿, active-生效中, expired-已失效) */
    @Excel(name = "状态")
    private String status;

    /** 浏览次数 */
    @Excel(name = "浏览次数")
    private Long viewCount;

    /** 下载次数 */
    @Excel(name = "下载次数")
    private Long downloadCount;

    /** 收藏次数 */
    @Excel(name = "收藏次数")
    private Long favoriteCount;

    /** 文件大小(bytes) */
    @Excel(name = "文件大小(bytes)")
    private Long fileSize;

    public Long getRegulationId() {
        return regulationId;
    }

    public void setRegulationId(Long regulationId) {
        this.regulationId = regulationId;
    }

    public String getRegulationName() {
        return regulationName;
    }

    public void setRegulationName(String regulationName) {
        this.regulationName = regulationName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getRegulationType() {
        return regulationType;
    }

    public void setRegulationType(String regulationType) {
        this.regulationType = regulationType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublishDept() {
        return publishDept;
    }

    public void setPublishDept(String publishDept) {
        this.publishDept = publishDept;
    }

    public Integer getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(Integer isImportant) {
        this.isImportant = isImportant;
    }

    public String getChapters() {
        return chapters;
    }

    public void setChapters(String chapters) {
        this.chapters = chapters;
    }

    public String getRevisionHistory() {
        return revisionHistory;
    }

    public void setRevisionHistory(String revisionHistory) {
        this.revisionHistory = revisionHistory;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("regulationId", getRegulationId())
            .append("regulationName", getRegulationName())
            .append("categoryId", getCategoryId())
            .append("communityId", getCommunityId())
            .append("regulationType", getRegulationType())
            .append("summary", getSummary())
            .append("publishDept", getPublishDept())
            .append("isImportant", getIsImportant())
            .append("chapters", getChapters())
            .append("revisionHistory", getRevisionHistory())
            .append("filePath", getFilePath())
            .append("effectiveDate", getEffectiveDate())
            .append("version", getVersion())
            .append("status", getStatus())
            .append("viewCount", getViewCount())
            .append("downloadCount", getDownloadCount())
            .append("favoriteCount", getFavoriteCount())
            .append("fileSize", getFileSize())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
