package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysContractSupervisionMapper;
import com.ruoyi.system.domain.SysContractSupervision;
import com.ruoyi.system.service.ISysContractSupervisionService;

/**
 * 合同履约监督Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-06
 */
@Service
public class SysContractSupervisionServiceImpl implements ISysContractSupervisionService
{
    @Autowired
    private SysContractSupervisionMapper contractSupervisionMapper;

    /**
     * 查询合同履约监督
     *
     * @param supervisionId 合同履约监督主键
     * @return 合同履约监督
     */
    @Override
    public SysContractSupervision selectContractSupervisionById(Long supervisionId)
    {
        return contractSupervisionMapper.selectContractSupervisionById(supervisionId);
    }

    /**
     * 查询合同履约监督列表
     *
     * @param sysContractSupervision 合同履约监督
     * @return 合同履约监督
     */
    @Override
    public List<SysContractSupervision> selectContractSupervisionList(SysContractSupervision sysContractSupervision)
    {
        return contractSupervisionMapper.selectContractSupervisionList(sysContractSupervision);
    }

    /**
     * 新增合同履约监督
     *
     * @param sysContractSupervision 合同履约监督
     * @return 结果
     */
    @Override
    public int insertContractSupervision(SysContractSupervision sysContractSupervision)
    {
        return contractSupervisionMapper.insertContractSupervision(sysContractSupervision);
    }

    /**
     * 修改合同履约监督
     *
     * @param sysContractSupervision 合同履约监督
     * @return 结果
     */
    @Override
    public int updateContractSupervision(SysContractSupervision sysContractSupervision)
    {
        return contractSupervisionMapper.updateContractSupervision(sysContractSupervision);
    }

    /**
     * 批量删除合同履约监督
     *
     * @param supervisionIds 需要删除的合同履约监督主键
     * @return 结果
     */
    @Override
    public int deleteContractSupervisionByIds(Long[] supervisionIds)
    {
        return contractSupervisionMapper.deleteContractSupervisionByIds(supervisionIds);
    }

    /**
     * 删除合同履约监督信息
     *
     * @param supervisionId 合同履约监督主键
     * @return 结果
     */
    @Override
    public int deleteContractSupervisionById(Long supervisionId)
    {
        return contractSupervisionMapper.deleteContractSupervisionById(supervisionId);
    }

    /**
     * 公示考核结果
     *
     * @param supervisionId 合同履约监督主键
     * @return 结果
     */
    @Override
    public int publishResult(Long supervisionId)
    {
        SysContractSupervision supervision = new SysContractSupervision();
        supervision.setSupervisionId(supervisionId);
        supervision.setIsPublished("1");
        supervision.setPublishTime(new Date());
        return contractSupervisionMapper.updateContractSupervision(supervision);
    }
}
