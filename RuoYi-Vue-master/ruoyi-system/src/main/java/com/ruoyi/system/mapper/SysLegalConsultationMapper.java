package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysLegalConsultation;

/**
 * 法律咨询Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface SysLegalConsultationMapper 
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
     * 删除法律咨询
     * 
     * @param consultationId 法律咨询主键
     * @return 结果
     */
    public int deleteSysLegalConsultationByConsultationId(Long consultationId);

    /**
     * 批量删除法律咨询
     * 
     * @param consultationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysLegalConsultationByConsultationIds(Long[] consultationIds);
}
