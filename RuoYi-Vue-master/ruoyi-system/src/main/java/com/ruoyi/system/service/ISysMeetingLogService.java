package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysMeetingLog;

/**
 * 业主大会日志Service接口
 * 
 * @author ruoyi
 * @date 2025-12-26
 */
public interface ISysMeetingLogService 
{
    /**
     * 查询业主大会日志
     * 
     * @param logId 业主大会日志主键
     * @return 业主大会日志
     */
    public SysMeetingLog selectSysMeetingLogByLogId(Long logId);

    /**
     * 查询业主大会日志列表
     * 
     * @param sysMeetingLog 业主大会日志
     * @return 业主大会日志集合
     */
    public List<SysMeetingLog> selectSysMeetingLogList(SysMeetingLog sysMeetingLog);

    /**
     * 新增业主大会日志
     * 
     * @param sysMeetingLog 业主大会日志
     * @return 结果
     */
    public int insertSysMeetingLog(SysMeetingLog sysMeetingLog);

    /**
     * 修改业主大会日志
     * 
     * @param sysMeetingLog 业主大会日志
     * @return 结果
     */
    public int updateSysMeetingLog(SysMeetingLog sysMeetingLog);

    /**
     * 批量删除业主大会日志
     * 
     * @param logIds 需要删除的业主大会日志主键集合
     * @return 结果
     */
    public int deleteSysMeetingLogByLogIds(Long[] logIds);

    /**
     * 删除业主大会日志信息
     * 
     * @param logId 业主大会日志主键
     * @return 结果
     */
    public int deleteSysMeetingLogByLogId(Long logId);

    /**
     * 查询用户在特定会议的最新日志
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     * @return 业主大会日志
     */
    public SysMeetingLog selectLatestLogByUserAndMeeting(Long userId, Long meetingId);

    /**
     * 查询指定会议的所有日志
     * 
     * @param meetingId 会议ID
     * @return 业主大会日志集合
     */
    public List<SysMeetingLog> selectLogsByMeetingId(Long meetingId);

    /**
     * 记录用户访问业主大会详情日志
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     * @param hasVoted 是否已投票
     */
    public void logMeetingAccess(Long userId, Long meetingId, boolean hasVoted);

    /**
     * 记录用户投票日志
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     */
    public void logMeetingVote(Long userId, Long meetingId);

    /**
     * 记录线下拜访日志
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     * @param logType 日志类型(3-7)
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param remark 备注
     */
    public void logOfflineVisit(Long userId, Long meetingId, Integer logType, Long operatorId, String operatorName, String remark);
}
