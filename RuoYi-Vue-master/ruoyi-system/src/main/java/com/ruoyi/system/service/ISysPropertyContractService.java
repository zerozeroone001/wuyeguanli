package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysPropertyContract;

/**
 * 物业服务合同Service接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface ISysPropertyContractService 
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
     * 批量删除物业服务合同
     * 
     * @param contractIds 需要删除的物业服务合同主键集合
     * @return 结果
     */
    public int deleteSysPropertyContractByContractIds(Long[] contractIds);

    /**
     * 删除物业服务合同信息
     * 
     * @param contractId 物业服务合同主键
     * @return 结果
     */
    public int deleteSysPropertyContractByContractId(Long contractId);

    /**
     * 更新合同阶段
     * 
     * @param contractId 合同ID
     * @param newStage 新阶段
     * @return 结果
     */
    public int updateContractStage(Long contractId, String newStage);

    /**
     * 审核合同（待审核 -> 待制作查验清单）
     * 
     * @param contractId 合同ID
     * @return 结果
     */
    public int reviewContract(Long contractId);

    /**
     * 上传查验清单
     * 
     * @param contractId 合同ID
     * @param fileUrl 文件URL
     * @return 结果
     */
    public int uploadEntryChecklist(Long contractId, String fileUrl);

    /**
     * 上传物业查验清单
     * 
     * @param contractId 合同ID
     * @param fileUrl 文件URL
     * @return 结果
     */
    public int uploadPropertyEntryChecklist(Long contractId, String fileUrl);

    /**
     * 上传合同月履行清单
     * 
     * @param contractId 合同ID
     * @param fileUrl 文件URL
     * @return 结果
     */
    public int uploadMonthlyChecklist(Long contractId, String fileUrl);

    /**
     * 上传物业月履行清单
     * 
     * @param contractId 合同ID
     * @param fileUrl 文件URL
     * @return 结果
     */
    public int uploadPropertyMonthlyChecklist(Long contractId, String fileUrl);


    /**
     * 月履行清单审核
     * 
     * @param contractId 合同ID
     * @param result 审核结果：complete(完全履行) / incomplete(未完全履行)
     * @return 结果
     */
    public int reviewMonthlyChecklist(Long contractId, String result);

    /**
     * 上传整改通知单
     * 
     * @param contractId 合同ID
     * @param fileUrl 文件URL
     * @return 结果
     */
    public int uploadRectificationNotice(Long contractId, String fileUrl);

    /**
     * 上传整改结果评定通知书
     * 
     * @param contractId 合同ID
     * @param fileUrl 文件URL
     * @return 结果
     */
    public int uploadRectificationResult(Long contractId, String fileUrl);

    /**
     * 上传年度履行报告
     * 
     * @param contractId 合同ID
     * @param fileUrl 文件URL
     * @return 结果
     */
    public int uploadAnnualReport(Long contractId, String fileUrl);
}
