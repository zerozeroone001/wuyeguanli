package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysMeetingLog;
import org.apache.ibatis.annotations.Param;

/**
 * 业主大会日志Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-26
 */
public interface SysMeetingLogMapper 
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
     * 删除业主大会日志
     * 
     * @param logId 业主大会日志主键
     * @return 结果
     */
    public int deleteSysMeetingLogByLogId(Long logId);

    /**
     * 批量删除业主大会日志
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysMeetingLogByLogIds(Long[] logIds);

    /**
     * 查询用户在特定会议的最新日志
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     * @return 业主大会日志
     */
    public SysMeetingLog selectLatestLogByUserAndMeeting(@Param("userId") Long userId, @Param("meetingId") Long meetingId);

    /**
     * 查询指定会议的所有日志
     * 
     * @param meetingId 会议ID
     * @return 业主大会日志集合
     */
    public List<SysMeetingLog> selectLogsByMeetingId(Long meetingId);
}
