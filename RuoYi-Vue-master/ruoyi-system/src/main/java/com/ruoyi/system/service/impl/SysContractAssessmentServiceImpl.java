package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysContractAssessmentScore;
import com.ruoyi.system.mapper.SysContractAssessmentScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysContractAssessmentMapper;
import com.ruoyi.system.domain.SysContractAssessment;
import com.ruoyi.system.service.ISysContractAssessmentService;

/**
 * 合同考核活动Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@Service
public class SysContractAssessmentServiceImpl implements ISysContractAssessmentService
{
    @Autowired
    private SysContractAssessmentMapper sysContractAssessmentMapper;

    @Autowired
    private SysContractAssessmentScoreMapper sysContractAssessmentScoreMapper;

    @Override
    public SysContractAssessment selectSysContractAssessmentByAssessmentId(Long assessmentId)
    {
        return sysContractAssessmentMapper.selectSysContractAssessmentByAssessmentId(assessmentId);
    }

    @Override
    public List<SysContractAssessment> selectSysContractAssessmentList(SysContractAssessment sysContractAssessment)
    {
        return sysContractAssessmentMapper.selectSysContractAssessmentList(sysContractAssessment);
    }

    @Override
    public List<SysContractAssessment> selectByContractId(Long contractId)
    {
        return sysContractAssessmentMapper.selectByContractId(contractId);
    }

    @Override
    public int insertSysContractAssessment(SysContractAssessment sysContractAssessment)
    {
        sysContractAssessment.setCreateTime(DateUtils.getNowDate());
        sysContractAssessment.setTotalParticipants(0);
        return sysContractAssessmentMapper.insertSysContractAssessment(sysContractAssessment);
    }

    @Override
    public int updateSysContractAssessment(SysContractAssessment sysContractAssessment)
    {
        sysContractAssessment.setUpdateTime(DateUtils.getNowDate());
        return sysContractAssessmentMapper.updateSysContractAssessment(sysContractAssessment);
    }

    @Override
    public int deleteSysContractAssessmentByAssessmentIds(Long[] assessmentIds)
    {
        return sysContractAssessmentMapper.deleteSysContractAssessmentByAssessmentIds(assessmentIds);
    }

    @Override
    public int deleteSysContractAssessmentByAssessmentId(Long assessmentId)
    {
        return sysContractAssessmentMapper.deleteSysContractAssessmentByAssessmentId(assessmentId);
    }

    @Override
    public void updateAssessmentStatistics(Long assessmentId)
    {
        List<SysContractAssessmentScore> scores = sysContractAssessmentScoreMapper.selectByAssessmentId(assessmentId);
        if (scores == null || scores.isEmpty())
        {
            return;
        }

        int totalParticipants = scores.size();
        BigDecimal satisfactionSum = BigDecimal.ZERO;
        BigDecimal obligationSum = BigDecimal.ZERO;

        for (SysContractAssessmentScore score : scores)
        {
            if (score.getSatisfactionScore() != null)
            {
                satisfactionSum = satisfactionSum.add(BigDecimal.valueOf(score.getSatisfactionScore()));
            }
            if (score.getObligationScore() != null)
            {
                obligationSum = obligationSum.add(BigDecimal.valueOf(score.getObligationScore()));
            }
        }

        SysContractAssessment assessment = new SysContractAssessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setTotalParticipants(totalParticipants);
        assessment.setSatisfactionAvgScore(satisfactionSum.divide(BigDecimal.valueOf(totalParticipants), 2, RoundingMode.HALF_UP));
        assessment.setObligationAvgScore(obligationSum.divide(BigDecimal.valueOf(totalParticipants), 2, RoundingMode.HALF_UP));
        assessment.setUpdateTime(DateUtils.getNowDate());

        sysContractAssessmentMapper.updateSysContractAssessment(assessment);
    }
}
