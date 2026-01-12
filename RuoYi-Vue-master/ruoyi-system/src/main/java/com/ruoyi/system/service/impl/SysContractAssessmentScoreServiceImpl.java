package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysContractAssessmentScoreMapper;
import com.ruoyi.system.domain.SysContractAssessmentScore;
import com.ruoyi.system.service.ISysContractAssessmentScoreService;
import com.ruoyi.system.service.ISysContractAssessmentService;

/**
 * 业主考核评分Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@Service
public class SysContractAssessmentScoreServiceImpl implements ISysContractAssessmentScoreService
{
    @Autowired
    private SysContractAssessmentScoreMapper sysContractAssessmentScoreMapper;

    @Autowired
    private ISysContractAssessmentService sysContractAssessmentService;

    @Override
    public SysContractAssessmentScore selectSysContractAssessmentScoreByScoreId(Long scoreId)
    {
        return sysContractAssessmentScoreMapper.selectSysContractAssessmentScoreByScoreId(scoreId);
    }

    @Override
    public List<SysContractAssessmentScore> selectSysContractAssessmentScoreList(SysContractAssessmentScore sysContractAssessmentScore)
    {
        return sysContractAssessmentScoreMapper.selectSysContractAssessmentScoreList(sysContractAssessmentScore);
    }

    @Override
    public List<SysContractAssessmentScore> selectByAssessmentId(Long assessmentId)
    {
        return sysContractAssessmentScoreMapper.selectByAssessmentId(assessmentId);
    }

    @Override
    public SysContractAssessmentScore selectByAssessmentIdAndUserId(Long assessmentId, Long userId)
    {
        return sysContractAssessmentScoreMapper.selectByAssessmentIdAndUserId(assessmentId, userId);
    }

    @Override
    public int insertSysContractAssessmentScore(SysContractAssessmentScore sysContractAssessmentScore)
    {
        sysContractAssessmentScore.setCreateTime(DateUtils.getNowDate());
        return sysContractAssessmentScoreMapper.insertSysContractAssessmentScore(sysContractAssessmentScore);
    }

    @Override
    public int updateSysContractAssessmentScore(SysContractAssessmentScore sysContractAssessmentScore)
    {
        sysContractAssessmentScore.setUpdateTime(DateUtils.getNowDate());
        return sysContractAssessmentScoreMapper.updateSysContractAssessmentScore(sysContractAssessmentScore);
    }

    @Override
    public int deleteSysContractAssessmentScoreByScoreIds(Long[] scoreIds)
    {
        return sysContractAssessmentScoreMapper.deleteSysContractAssessmentScoreByScoreIds(scoreIds);
    }

    @Override
    public int deleteSysContractAssessmentScoreByScoreId(Long scoreId)
    {
        return sysContractAssessmentScoreMapper.deleteSysContractAssessmentScoreByScoreId(scoreId);
    }

    @Override
    @Transactional
    public int submitScore(SysContractAssessmentScore score)
    {
        // 检查是否已评分
        SysContractAssessmentScore existingScore = sysContractAssessmentScoreMapper.selectByAssessmentIdAndUserId(
            score.getAssessmentId(), score.getUserId());
        
        if (existingScore != null)
        {
            throw new ServiceException("您已对此考核活动进行过评分，不能重复提交");
        }

        // 验证评分范围
        if (score.getSatisfactionScore() != null && (score.getSatisfactionScore() < 1 || score.getSatisfactionScore() > 5))
        {
            throw new ServiceException("满意度评分必须在1-5分之间");
        }
        if (score.getObligationScore() != null && (score.getObligationScore() < 1 || score.getObligationScore() > 5))
        {
            throw new ServiceException("完成义务评分必须在1-5分之间");
        }

        score.setSubmitTime(DateUtils.getNowDate());
        score.setCreateTime(DateUtils.getNowDate());
        int result = sysContractAssessmentScoreMapper.insertSysContractAssessmentScore(score);

        // 更新考核活动统计数据
        sysContractAssessmentService.updateAssessmentStatistics(score.getAssessmentId());

        return result;
    }
}
