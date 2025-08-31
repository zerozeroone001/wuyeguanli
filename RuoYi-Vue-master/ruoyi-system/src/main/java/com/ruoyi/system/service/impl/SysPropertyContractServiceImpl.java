package com.ruoyi.system.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysPropertyContractMapper;
import com.ruoyi.system.domain.SysPropertyContract;
import com.ruoyi.system.service.ISysPropertyContractService;

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

    /**
     * 查询物业服务合同
     * 
     * @param contractId 物业服务合同主键
     * @return 物业服务合同
     */
    @Override
    public SysPropertyContract selectSysPropertyContractByContractId(Long contractId)
    {
        return sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
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
    public int insertSysPropertyContract(SysPropertyContract sysPropertyContract)
    {
        // 生成合同编号
        String contractNo = "HT" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ThreadLocalRandom.current().nextInt(100000, 1000000);
        sysPropertyContract.setContractNo(contractNo);
        sysPropertyContract.setCreateTime(DateUtils.getNowDate());
        return sysPropertyContractMapper.insertSysPropertyContract(sysPropertyContract);
    }

    /**
     * 修改物业服务合同
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 结果
     */
    @Override
    public int updateSysPropertyContract(SysPropertyContract sysPropertyContract)
    {
        sysPropertyContract.setUpdateTime(DateUtils.getNowDate());
        return sysPropertyContractMapper.updateSysPropertyContract(sysPropertyContract);
    }

    /**
     * 批量删除物业服务合同
     * 
     * @param contractIds 需要删除的物业服务合同主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyContractByContractIds(Long[] contractIds)
    {
        return sysPropertyContractMapper.deleteSysPropertyContractByContractIds(contractIds);
    }

    /**
     * 删除物业服务合同信息
     * 
     * @param contractId 物业服务合同主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyContractByContractId(Long contractId)
    {
        return sysPropertyContractMapper.deleteSysPropertyContractByContractId(contractId);
    }
}
