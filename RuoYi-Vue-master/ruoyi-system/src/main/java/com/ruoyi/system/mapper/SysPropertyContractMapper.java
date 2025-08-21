package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysPropertyContract;

/**
 * 物业服务合同Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface SysPropertyContractMapper 
{
    /**
     * 查询物业服务合同
     * 
     * @param contractId 物业服务合同主键
     * @return 物业服务合同
     */
    public SysPropertyContract selectSysPropertyContractByContractId(Long contractId);

    /**
     * 查询物业服务合同列表
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 物业服务合同集合
     */
    public List<SysPropertyContract> selectSysPropertyContractList(SysPropertyContract sysPropertyContract);

    /**
     * 新增物业服务合同
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 结果
     */
    public int insertSysPropertyContract(SysPropertyContract sysPropertyContract);

    /**
     * 修改物业服务合同
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 结果
     */
    public int updateSysPropertyContract(SysPropertyContract sysPropertyContract);

    /**
     * 删除物业服务合同
     * 
     * @param contractId 物业服务合同主键
     * @return 结果
     */
    public int deleteSysPropertyContractByContractId(Long contractId);

    /**
     * 批量删除物业服务合同
     * 
     * @param contractIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysPropertyContractByContractIds(Long[] contractIds);
}
