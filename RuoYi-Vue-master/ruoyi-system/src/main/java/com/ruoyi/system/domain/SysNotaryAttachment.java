package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公证附件对象 sys_notary_attachment
 * 
 * @author ruoyi
 * @date 2025-09-11
 */
public class SysNotaryAttachment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 附件ID */
    private Long attachmentId;

    /** 关联公证ID */
    @Excel(name = "关联公证ID")
    private Long notaryId;

    /** 文件原始名称 */
    @Excel(name = "文件原始名称")
    private String fileName;

    /** 文件存储URL */
    @Excel(name = "文件存储URL")
    private String fileUrl;

    /** 文件材料类型 (id_card, application, supporting_docs, photos) */
    @Excel(name = "文件材料类型 (id_card, application, supporting_docs, photos)")
    private String fileType;

    /** 文件大小（字节） */
    @Excel(name = "文件大小（字节）")
    private Long fileSize;

    /** 文件哈希值 */
    @Excel(name = "文件哈希值")
    private String fileHash;

    /** 上传时间 */
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date uploadTime;

    /** 上传人 */
    @Excel(name = "上传人")
    private String uploadBy;

    /** 附件状态 (pending, verified, rejected) */
    @Excel(name = "附件状态 (pending, verified, rejected)")
    private String status;

    public void setAttachmentId(Long attachmentId) 
    {
        this.attachmentId = attachmentId;
    }

    public Long getAttachmentId() 
    {
        return attachmentId;
    }

    public void setNotaryId(Long notaryId) 
    {
        this.notaryId = notaryId;
    }

    public Long getNotaryId() 
    {
        return notaryId;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }

    public void setFileHash(String fileHash) 
    {
        this.fileHash = fileHash;
    }

    public String getFileHash() 
    {
        return fileHash;
    }

    public void setUploadTime(java.util.Date uploadTime) 
    {
        this.uploadTime = uploadTime;
    }

    public java.util.Date getUploadTime() 
    {
        return uploadTime;
    }

    public void setUploadBy(String uploadBy) 
    {
        this.uploadBy = uploadBy;
    }

    public String getUploadBy() 
    {
        return uploadBy;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("attachmentId", getAttachmentId())
            .append("notaryId", getNotaryId())
            .append("fileName", getFileName())
            .append("fileUrl", getFileUrl())
            .append("fileType", getFileType())
            .append("fileSize", getFileSize())
            .append("fileHash", getFileHash())
            .append("uploadTime", getUploadTime())
            .append("uploadBy", getUploadBy())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
