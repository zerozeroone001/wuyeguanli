package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysContractAssessmentScore;
import org.apache.ibatis.annotations.Param;

/**
 * 业主考核评分Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public interface SysContractAssessmentScoreMapper
{
    public SysContractAssessmentScore selectSysContractAssessmentScoreByScoreId(Long scoreId);

    public List<SysContractAssessmentScore> selectSysContractAssessmentScoreList(SysContractAssessmentScore sysContractAssessmentScore);

    public List<SysContractAssessmentScore> selectByAssessmentId(Long assessmentId);

    public SysContractAssessmentScore selectByAssessmentIdAndUserId(@Param("assessmentId") Long assessmentId, @Param("userId") Long userId);

    public int insertSysContractAssessmentScore(SysContractAssessmentScore sysContractAssessmentScore);

    public int updateSysContractAssessmentScore(SysContractAssessmentScore sysContractAssessmentScore);

    public int deleteSysContractAssessmentScoreByScoreId(Long scoreId);

    public int deleteSysContractAssessmentScoreByScoreIds(Long[] scoreIds);

    public int countByAssessmentId(Long assessmentId);
}
