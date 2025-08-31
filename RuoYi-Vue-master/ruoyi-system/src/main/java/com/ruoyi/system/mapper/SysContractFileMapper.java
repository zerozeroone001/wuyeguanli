package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysContractFile;
import java.util.List;

/**
 * 合同与文件关联Mapper接口
 * 
 * @author ruoyi
 */
public interface SysContractFileMapper 
{
    /**
     * 批量新增合同与文件关联
     * 
     * @param contractFileList 合同与文件关联列表
     * @return 结果
     */
    public int batchInsert(List<SysContractFile> contractFileList);

    /**
     * 根据合同ID删除关联
     * 
     * @param contractId 合同ID
     * @return 结果
     */
    public int deleteByContractId(Long contractId);
}
