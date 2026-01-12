package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysContractSupervision;

/**
 * 合同履约监督Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-06
 */
public interface SysContractSupervisionMapper
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
     * 删除合同履约监督
     *
     * @param supervisionId 合同履约监督主键
     * @return 结果
     */
    public int deleteContractSupervisionById(Long supervisionId);

    /**
     * 批量删除合同履约监督
     *
     * @param supervisionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteContractSupervisionByIds(Long[] supervisionIds);
}
