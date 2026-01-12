package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysContractAssessment;

/**
 * 合同考核活动Service接口
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public interface ISysContractAssessmentService
{
    public SysContractAssessment selectSysContractAssessmentByAssessmentId(Long assessmentId);

    public List<SysContractAssessment> selectSysContractAssessmentList(SysContractAssessment sysContractAssessment);

    public List<SysContractAssessment> selectByContractId(Long contractId);

    public int insertSysContractAssessment(SysContractAssessment sysContractAssessment);

    public int updateSysContractAssessment(SysContractAssessment sysContractAssessment);

    public int deleteSysContractAssessmentByAssessmentIds(Long[] assessmentIds);

    public int deleteSysContractAssessmentByAssessmentId(Long assessmentId);

    /**
     * 更新考核活动的统计数据（平均分、参与人数）
     */
    public void updateAssessmentStatistics(Long assessmentId);
}
