package com.ruoyi.system.domain;

/**
 * 合同与文件关联对象 sys_contract_file
 * 
 * @author ruoyi
 */
public class SysContractFile
{
    /** 合同ID */
    private Long contractId;

    /** 文件ID */
    private Long fileId;

    public SysContractFile(Long contractId, Long fileId) {
        this.contractId = contractId;
        this.fileId = fileId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
