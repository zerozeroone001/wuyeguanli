package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysContractSupervision;

/**
 * 合同履约监督Service接口
 *
 * @author ruoyi
 * @date 2026-01-06
 */
public interface ISysContractSupervisionService
{
    /**
     * 查询合同履约监督
     *
     * @param supervisionId 合同履约监督主键
     * @return 合同履约监督
     */
    public SysContractSupervision selectContractSupervisionById(Long supervisionId);

    /**
     * 查询合同履约监督列表
     *
     * @param sysContractSupervision 合同履约监督
     * @return 合同履约监督集合
     */
    public List<SysContractSupervision> selectContractSupervisionList(SysContractSupervision sysContractSupervision);

    /**
     * 新增合同履约监督
     *
     * @param sysContractSupervision 合同履约监督
     * @return 结果
     */
    public int insertContractSupervision(SysContractSupervision sysContractSupervision);

    /**
     * 修改合同履约监督
     *
     * @param sysContractSupervision 合同履约监督
     * @return 结果
     */
    public int updateContractSupervision(SysContractSupervision sysContractSupervision);

    /**
     * 批量删除合同履约监督
     *
     * @param supervisionIds 需要删除的合同履约监督主键集合
     * @return 结果
     */
    public int deleteContractSupervisionByIds(Long[] supervisionIds);

    /**
     * 删除合同履约监督信息
     *
     * @param supervisionId 合同履约监督主键
     * @return 结果
     */
    public int deleteContractSupervisionById(Long supervisionId);

    /**
     * 公示考核结果
     *
     * @param supervisionId 合同履约监督主键
     * @return 结果
     */
    public int publishResult(Long supervisionId);
}
