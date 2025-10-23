package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysFormSubmission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表单提交记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-23
 */
public interface SysFormSubmissionMapper
{
    /**
     * 查询提交记录
     *
     * @param submissionId 主键
     * @return 提交记录
     */
    SysFormSubmission selectSysFormSubmissionBySubmissionId(Long submissionId);

    /**
     * 根据问卷和用户查询提交记录
     *
     * @param pollId 问卷ID
     * @param userId 用户ID
     * @return 提交记录
     */
    SysFormSubmission selectByPollIdAndUserId(@Param("pollId") Long pollId, @Param("userId") Long userId);

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
     * 删除提交记录
     *
     * @param submissionId 主键
     * @return 结果
     */
    int deleteSysFormSubmissionBySubmissionId(Long submissionId);

    /**
     * 批量删除提交记录
     *
     * @param submissionIds 主键集合
     * @return 结果
     */
    int deleteSysFormSubmissionBySubmissionIds(Long[] submissionIds);
}

