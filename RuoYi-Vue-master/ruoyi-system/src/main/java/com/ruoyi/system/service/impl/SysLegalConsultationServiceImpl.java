package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysLegalConsultationMapper;
import com.ruoyi.system.domain.SysLegalConsultation;
import com.ruoyi.system.service.ISysLegalConsultationService;

/**
 * 法律咨询Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysLegalConsultationServiceImpl implements ISysLegalConsultationService 
{
    @Autowired
    private SysLegalConsultationMapper sysLegalConsultationMapper;

    /**
     * 查询法律咨询
     * 
     * @param consultationId 法律咨询主键
     * @return 法律咨询
     */
    @Override
    public SysLegalConsultation selectSysLegalConsultationByConsultationId(Long consultationId)
    {
        return sysLegalConsultationMapper.selectSysLegalConsultationByConsultationId(consultationId);
    }

    /**
     * 查询法律咨询列表
     * 
     * @param sysLegalConsultation 法律咨询
     * @return 法律咨询
     */
    @Override
    public List<SysLegalConsultation> selectSysLegalConsultationList(SysLegalConsultation sysLegalConsultation)
    {
        return sysLegalConsultationMapper.selectSysLegalConsultationList(sysLegalConsultation);
    }

    /**
     * 新增法律咨询
     * 
     * @param sysLegalConsultation 法律咨询
     * @return 结果
     */
    @Override
    public int insertSysLegalConsultation(SysLegalConsultation sysLegalConsultation)
    {
        sysLegalConsultation.setCreateTime(DateUtils.getNowDate());
        return sysLegalConsultationMapper.insertSysLegalConsultation(sysLegalConsultation);
    }

    /**
     * 修改法律咨询
     * 
     * @param sysLegalConsultation 法律咨询
     * @return 结果
     */
    @Override
    public int updateSysLegalConsultation(SysLegalConsultation sysLegalConsultation)
    {
        sysLegalConsultation.setUpdateTime(DateUtils.getNowDate());
        return sysLegalConsultationMapper.updateSysLegalConsultation(sysLegalConsultation);
    }

    /**
     * 批量删除法律咨询
     * 
     * @param consultationIds 需要删除的法律咨询主键
     * @return 结果
     */
    @Override
    public int deleteSysLegalConsultationByConsultationIds(Long[] consultationIds)
    {
        return sysLegalConsultationMapper.deleteSysLegalConsultationByConsultationIds(consultationIds);
    }

    /**
     * 删除法律咨询信息
     * 
     * @param consultationId 法律咨询主键
     * @return 结果
     */
    @Override
    public int deleteSysLegalConsultationByConsultationId(Long consultationId)
    {
        return sysLegalConsultationMapper.deleteSysLegalConsultationByConsultationId(consultationId);
    }
}
