package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 合同相关文档对象 sys_contract_document
 * 
 * @author ruoyi
 * @date 2025-09-01
 */
public class SysContractDocument extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文档ID */
    private Long docId;

    /** 合同ID */
    @Excel(name = "合同ID")
    private Long contractId;

    /** 文档名称 */
    @Excel(name = "文档名称")
    private String docName;

    /** 文档URL */
    @Excel(name = "文档URL")
    private String docUrl;

    /** 文档类型 (e.g., pdf, docx) */
    @Excel(name = "文档类型")
    private String docType;

    /** 文档大小 (in bytes) */
    @Excel(name = "文档大小")
    private Long docSize;

    /** 上传时间 */
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    public void setDocId(Long docId)
    {
        this.docId = docId;
    }

    public Long getDocId()
    {
        return docId;
    }
    public void setContractId(Long contractId)
    {
        this.contractId = contractId;
    }

    public Long getContractId()
    {
        return contractId;
    }
    public void setDocName(String docName)
    {
        this.docName = docName;
    }

    public String getDocName()
    {
        return docName;
    }
    public void setDocUrl(String docUrl)
    {
        this.docUrl = docUrl;
    }

    public String getDocUrl()
    {
        return docUrl;
    }
    public void setDocType(String docType)
    {
        this.docType = docType;
    }

    public String getDocType()
    {
        return docType;
    }
    public void setDocSize(Long docSize)
    {
        this.docSize = docSize;
    }

    public Long getDocSize()
    {
        return docSize;
    }
    public void setUploadTime(Date uploadTime)
    {
        this.uploadTime = uploadTime;
    }

    public Date getUploadTime()
    {
        return uploadTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("docId", getDocId())
            .append("contractId", getContractId())
            .append("docName", getDocName())
            .append("docUrl", getDocUrl())
            .append("docType", getDocType())
            .append("docSize", getDocSize())
            .append("uploadTime", getUploadTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
