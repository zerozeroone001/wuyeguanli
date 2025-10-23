package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysFormSubmission;

import java.util.List;

/**
 * 表单提交记录Service接口
 *
 * @author ruoyi
 * @date 2025-10-23
 */
public interface ISysFormSubmissionService
{
    /**
     * 查询提交记录
     *
     * @param submissionId 主键
     * @return 记录
     */
    SysFormSubmission selectSysFormSubmissionBySubmissionId(Long submissionId);

    /**
     * 根据问卷和用户查询提交记录
     *
     * @param pollId 问卷ID
     * @param userId 用户ID
     * @return 记录
     */
    SysFormSubmission selectByPollIdAndUserId(Long pollId, Long userId);

    /**
     * 查询提交记录列表
     *
     * @param sysFormSubmission 查询条件
     * @return 列表
     */
    List<SysFormSubmission> selectSysFormSubmissionList(SysFormSubmission sysFormSubmission);

    /**
     * 新增提交记录
     *
     * @param sysFormSubmission 记录
     * @return 结果
     */
    int insertSysFormSubmission(SysFormSubmission sysFormSubmission);

    /**
     * 修改提交记录
     *
     * @param sysFormSubmission 记录
     * @return 结果
     */
    int updateSysFormSubmission(SysFormSubmission sysFormSubmission);

    /**
     * 批量删除提交记录
     *
     * @param submissionIds 主键集合
     * @return 结果
     */
    int deleteSysFormSubmissionBySubmissionIds(Long[] submissionIds);

    /**
     * 删除提交记录信息
     *
     * @param submissionId 主键
     * @return 结果
     */
    int deleteSysFormSubmissionBySubmissionId(Long submissionId);

    /**
     * 保存或更新提交记录（按问卷+用户唯一）
     *
     * @param submission 记录
     * @return 最新记录
     */
    SysFormSubmission saveOrUpdateSubmission(SysFormSubmission submission);
}

