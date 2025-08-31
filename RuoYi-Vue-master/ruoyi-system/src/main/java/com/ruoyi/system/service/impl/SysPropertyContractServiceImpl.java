package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysContractFile;
import com.ruoyi.system.domain.SysFileInfo;
import com.ruoyi.system.mapper.SysContractFileMapper;
import com.ruoyi.system.service.ISysFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysPropertyContractMapper;
import com.ruoyi.system.domain.SysPropertyContract;
import com.ruoyi.system.service.ISysPropertyContractService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 物业服务合同Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysPropertyContractServiceImpl implements ISysPropertyContractService 
{
    @Autowired
    private SysPropertyContractMapper sysPropertyContractMapper;

    @Autowired
    private ISysFileInfoService sysFileInfoService;

    @Autowired
    private SysContractFileMapper sysContractFileMapper;

    /**
     * 查询物业服务合同
     * 
     * @param contractId 物业服务合同主键
     * @return 物业服务合同
     */
    @Override
    public SysPropertyContract selectSysPropertyContractByContractId(Long contractId)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract != null)
        {
            List<SysFileInfo> fileList = sysFileInfoService.selectSysFileInfoListByContractId(contractId);
            contract.setFileList(fileList);
        }
        return contract;
    }

    /**
     * 查询物业服务合同列表
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 物业服务合同
     */
    @Override
    public List<SysPropertyContract> selectSysPropertyContractList(SysPropertyContract sysPropertyContract)
    {
        return sysPropertyContractMapper.selectSysPropertyContractList(sysPropertyContract);
    }

    /**
     * 新增物业服务合同
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSysPropertyContract(SysPropertyContract sysPropertyContract)
    {
        sysPropertyContract.setCreateTime(DateUtils.getNowDate());
        // 1. 插入主表
        int rows = sysPropertyContractMapper.insertSysPropertyContract(sysPropertyContract);
        // 2. 插入关联文件
        insertContractFiles(sysPropertyContract);
        return rows;
    }

    /**
     * 修改物业服务合同
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSysPropertyContract(SysPropertyContract sysPropertyContract)
    {
        sysPropertyContract.setUpdateTime(DateUtils.getNowDate());
        // 1. 删除旧的文件关联
        sysContractFileMapper.deleteByContractId(sysPropertyContract.getContractId());
        // 2. 插入新的文件关联
        insertContractFiles(sysPropertyContract);
        // 3. 更新主表
        return sysPropertyContractMapper.updateSysPropertyContract(sysPropertyContract);
    }

    /**
     * 批量删除物业服务合同
     * 
     * @param contractIds 需要删除的物业服务合同主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysPropertyContractByContractIds(Long[] contractIds)
    {
        for (Long contractId : contractIds) {
            sysContractFileMapper.deleteByContractId(contractId);
        }
        return sysPropertyContractMapper.deleteSysPropertyContractByContractIds(contractIds);
    }

    /**
     * 删除物业服务合同信息
     * 
     * @param contractId 物业服务合同主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysPropertyContractByContractId(Long contractId)
    {
        sysContractFileMapper.deleteByContractId(contractId);
        return sysPropertyContractMapper.deleteSysPropertyContractByContractId(contractId);
    }

    /**
     * 新增合同与文件关联信息
     *
     * @param contract 合同对象
     */
    public void insertContractFiles(SysPropertyContract contract)
    {
        List<Long> fileIds = contract.getFileIds();
        if (fileIds != null && !fileIds.isEmpty())
        {
            List<SysContractFile> list = new ArrayList<>();
            for (Long fileId : fileIds)
            {
                list.add(new SysContractFile(contract.getContractId(), fileId));
            }
            if (list.size() > 0)
            {
                sysContractFileMapper.batchInsert(list);
            }
        }
    }
}