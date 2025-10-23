package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysFormSubmission;
import com.ruoyi.system.mapper.SysFormSubmissionMapper;
import com.ruoyi.system.service.ISysFormSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表单提交记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-10-23
 */
@Service
public class SysFormSubmissionServiceImpl implements ISysFormSubmissionService
{
    @Autowired
    private SysFormSubmissionMapper sysFormSubmissionMapper;

    @Override
    public SysFormSubmission selectSysFormSubmissionBySubmissionId(Long submissionId)
    {
        return sysFormSubmissionMapper.selectSysFormSubmissionBySubmissionId(submissionId);
    }

    @Override
    public SysFormSubmission selectByPollIdAndUserId(Long pollId, Long userId)
    {
        return sysFormSubmissionMapper.selectByPollIdAndUserId(pollId, userId);
    }

    @Override
    public List<SysFormSubmission> selectSysFormSubmissionList(SysFormSubmission sysFormSubmission)
    {
        return sysFormSubmissionMapper.selectSysFormSubmissionList(sysFormSubmission);
    }

    @Override
    public int insertSysFormSubmission(SysFormSubmission sysFormSubmission)
    {
        sysFormSubmission.setCreateTime(DateUtils.getNowDate());
        sysFormSubmission.setSubmitTime(DateUtils.getNowDate());
        return sysFormSubmissionMapper.insertSysFormSubmission(sysFormSubmission);
    }

    @Override
    public int updateSysFormSubmission(SysFormSubmission sysFormSubmission)
    {
        sysFormSubmission.setUpdateTime(DateUtils.getNowDate());
        sysFormSubmission.setSubmitTime(DateUtils.getNowDate());
        return sysFormSubmissionMapper.updateSysFormSubmission(sysFormSubmission);
    }

    @Override
    public int deleteSysFormSubmissionBySubmissionIds(Long[] submissionIds)
    {
        return sysFormSubmissionMapper.deleteSysFormSubmissionBySubmissionIds(submissionIds);
    }

    @Override
    public int deleteSysFormSubmissionBySubmissionId(Long submissionId)
    {
        return sysFormSubmissionMapper.deleteSysFormSubmissionBySubmissionId(submissionId);
    }

    @Override
    public SysFormSubmission saveOrUpdateSubmission(SysFormSubmission submission)
    {
        SysFormSubmission existing = sysFormSubmissionMapper.selectByPollIdAndUserId(submission.getPollId(), submission.getUserId());
        if (existing == null)
        {
            insertSysFormSubmission(submission);
            return submission;
        }
        submission.setSubmissionId(existing.getSubmissionId());
        submission.setCreateTime(existing.getCreateTime());
        updateSysFormSubmission(submission);
        return submission;
    }
}

