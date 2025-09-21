package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysOpinionConsultationMapper;
import com.ruoyi.system.domain.SysOpinionConsultation;
import com.ruoyi.system.service.ISysOpinionConsultationService;

/**
 * 意见征询Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
@Service
public class SysOpinionConsultationServiceImpl implements ISysOpinionConsultationService 
{
    @Autowired
    private SysOpinionConsultationMapper sysOpinionConsultationMapper;

    /**
     * 查询意见征询
     * 
     * @param consultationId 意见征询主键
     * @return 意见征询
     */
    @Override
    public SysOpinionConsultation selectSysOpinionConsultationByConsultationId(Long consultationId)
    {
        return sysOpinionConsultationMapper.selectSysOpinionConsultationByConsultationId(consultationId);
    }

    /**
     * 查询意见征询列表
     * 
     * @param sysOpinionConsultation 意见征询
     * @return 意见征询
     */
    @Override
    public List<SysOpinionConsultation> selectSysOpinionConsultationList(SysOpinionConsultation sysOpinionConsultation)
    {
        return sysOpinionConsultationMapper.selectSysOpinionConsultationList(sysOpinionConsultation);
    }

    /**
     * 新增意见征询
     * 
     * @param sysOpinionConsultation 意见征询
     * @return 结果
     */
    @Override
    public int insertSysOpinionConsultation(SysOpinionConsultation sysOpinionConsultation)
    {
        sysOpinionConsultation.setCreateTime(DateUtils.getNowDate());
        return sysOpinionConsultationMapper.insertSysOpinionConsultation(sysOpinionConsultation);
    }

    /**
     * 修改意见征询
     * 
     * @param sysOpinionConsultation 意见征询
     * @return 结果
     */
    @Override
    public int updateSysOpinionConsultation(SysOpinionConsultation sysOpinionConsultation)
    {
        sysOpinionConsultation.setUpdateTime(DateUtils.getNowDate());
        return sysOpinionConsultationMapper.updateSysOpinionConsultation(sysOpinionConsultation);
    }

    /**
     * 批量删除意见征询
     * 
     * @param consultationIds 需要删除的意见征询主键
     * @return 结果
     */
    @Override
    public int deleteSysOpinionConsultationByConsultationIds(Long[] consultationIds)
    {
        return sysOpinionConsultationMapper.deleteSysOpinionConsultationByConsultationIds(consultationIds);
    }

    /**
     * 删除意见征询信息
     * 
     * @param consultationId 意见征询主键
     * @return 结果
     */
    @Override
    public int deleteSysOpinionConsultationByConsultationId(Long consultationId)
    {
        return sysOpinionConsultationMapper.deleteSysOpinionConsultationByConsultationId(consultationId);
    }
}
