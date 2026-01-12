package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysContractAssessmentScore;

/**
 * 业主考核评分Service接口
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public interface ISysContractAssessmentScoreService
{
    public SysContractAssessmentScore selectSysContractAssessmentScoreByScoreId(Long scoreId);

    public List<SysContractAssessmentScore> selectSysContractAssessmentScoreList(SysContractAssessmentScore sysContractAssessmentScore);

    public List<SysContractAssessmentScore> selectByAssessmentId(Long assessmentId);

    public SysContractAssessmentScore selectByAssessmentIdAndUserId(Long assessmentId, Long userId);

    public int insertSysContractAssessmentScore(SysContractAssessmentScore sysContractAssessmentScore);

    public int updateSysContractAssessmentScore(SysContractAssessmentScore sysContractAssessmentScore);

    public int deleteSysContractAssessmentScoreByScoreIds(Long[] scoreIds);

    public int deleteSysContractAssessmentScoreByScoreId(Long scoreId);

    /**
     * 业主提交评分
     */
    public int submitScore(SysContractAssessmentScore score);
}
