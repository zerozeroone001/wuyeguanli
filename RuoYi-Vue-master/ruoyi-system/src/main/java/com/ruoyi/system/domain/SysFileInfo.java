package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文件信息对象 sys_file_info
 * 
 * @author ruoyi
 * @date 2025-09-01
 */
public class SysFileInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件ID */
    private Long fileId;

    /** 文件原始名称 */
    @Excel(name = "文件原始名称")
    private String fileOriginName;

    /** 文件存储名称 */
    @Excel(name = "文件存储名称")
    private String fileName;

    /** 文件存储路径 */
    private String filePath;

    /** 文件访问地址 */
    @Excel(name = "文件访问地址")
    private String fileUrl;

    /** 文件MIME类型 */
    @Excel(name = "文件MIME类型")
    private String fileType;

    /** 文件大小(byte) */
    @Excel(name = "文件大小(byte)")
    private Long fileSize;

    /** 存储类型 (local, oss) */
    @Excel(name = "存储类型")
    private String storageType;

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public Long getFileId()
    {
        return fileId;
    }
    public void setFileOriginName(String fileOriginName)
    {
        this.fileOriginName = fileOriginName;
    }

    public String getFileOriginName()
    {
        return fileOriginName;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFilePath()
    {
        return filePath;
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
    public void setStorageType(String storageType)
    {
        this.storageType = storageType;
    }

    public String getStorageType()
    {
        return storageType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fileId", getFileId())
            .append("fileOriginName", getFileOriginName())
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
            .append("fileUrl", getFileUrl())
            .append("fileType", getFileType())
            .append("fileSize", getFileSize())
            .append("storageType", getStorageType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
