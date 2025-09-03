package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysOpinionConsultation;

/**
 * 意见征询Service接口
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
public interface ISysOpinionConsultationService 
{
    /**
     * 查询意见征询
     * 
     * @param consultationId 意见征询主键
     * @return 意见征询
     */
    public SysOpinionConsultation selectSysOpinionConsultationByConsultationId(Long consultationId);

    /**
     * 查询意见征询列表
     * 
     * @param sysOpinionConsultation 意见征询
     * @return 意见征询集合
     */
    public List<SysOpinionConsultation> selectSysOpinionConsultationList(SysOpinionConsultation sysOpinionConsultation);

    /**
     * 新增意见征询
     * 
     * @param sysOpinionConsultation 意见征询
     * @return 结果
     */
    public int insertSysOpinionConsultation(SysOpinionConsultation sysOpinionConsultation);

    /**
     * 修改意见征询
     * 
     * @param sysOpinionConsultation 意见征询
     * @return 结果
     */
    public int updateSysOpinionConsultation(SysOpinionConsultation sysOpinionConsultation);

    /**
     * 批量删除意见征询
     * 
     * @param consultationIds 需要删除的意见征询主键集合
     * @return 结果
     */
    public int deleteSysOpinionConsultationByConsultationIds(Long[] consultationIds);

    /**
     * 删除意见征询信息
     * 
     * @param consultationId 意见征询主键
     * @return 结果
     */
    public int deleteSysOpinionConsultationByConsultationId(Long consultationId);
}
