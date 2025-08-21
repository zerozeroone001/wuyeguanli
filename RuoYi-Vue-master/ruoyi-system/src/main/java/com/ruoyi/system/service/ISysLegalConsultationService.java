package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysLegalConsultation;

/**
 * 法律咨询Service接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface ISysLegalConsultationService 
{
    /**
     * 查询法律咨询
     * 
     * @param consultationId 法律咨询主键
     * @return 法律咨询
     */
    public SysLegalConsultation selectSysLegalConsultationByConsultationId(Long consultationId);

    /**
     * 查询法律咨询列表
     * 
     * @param sysLegalConsultation 法律咨询
     * @return 法律咨询集合
     */
    public List<SysLegalConsultation> selectSysLegalConsultationList(SysLegalConsultation sysLegalConsultation);

    /**
     * 新增法律咨询
     * 
     * @param sysLegalConsultation 法律咨询
     * @return 结果
     */
    public int insertSysLegalConsultation(SysLegalConsultation sysLegalConsultation);

    /**
     * 修改法律咨询
     * 
     * @param sysLegalConsultation 法律咨询
     * @return 结果
     */
    public int updateSysLegalConsultation(SysLegalConsultation sysLegalConsultation);

    /**
     * 批量删除法律咨询
     * 
     * @param consultationIds 需要删除的法律咨询主键集合
     * @return 结果
     */
    public int deleteSysLegalConsultationByConsultationIds(Long[] consultationIds);

    /**
     * 删除法律咨询信息
     * 
     * @param consultationId 法律咨询主键
     * @return 结果
     */
    public int deleteSysLegalConsultationByConsultationId(Long consultationId);
}
