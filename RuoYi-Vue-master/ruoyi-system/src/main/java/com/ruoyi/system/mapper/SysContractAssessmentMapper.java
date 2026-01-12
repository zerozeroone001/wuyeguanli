package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysContractAssessment;

/**
 * 合同考核活动Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public interface SysContractAssessmentMapper
{
    public SysContractAssessment selectSysContractAssessmentByAssessmentId(Long assessmentId);

    public List<SysContractAssessment> selectSysContractAssessmentList(SysContractAssessment sysContractAssessment);

    public List<SysContractAssessment> selectByContractId(Long contractId);

    public int insertSysContractAssessment(SysContractAssessment sysContractAssessment);

    public int updateSysContractAssessment(SysContractAssessment sysContractAssessment);

    public int deleteSysContractAssessmentByAssessmentId(Long assessmentId);

    public int deleteSysContractAssessmentByAssessmentIds(Long[] assessmentIds);
}
