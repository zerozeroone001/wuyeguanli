package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.SysPropertyMeeting;

/**
 * 会议管理Service接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface ISysPropertyMeetingService 
{
    /**
     * 查询会议管理
     * 
     * @param meetingId 会议管理主键
     * @return 会议管理
     */
    public SysPropertyMeeting selectSysPropertyMeetingByMeetingId(Long meetingId);

    /**
     * 查询会议管理列表
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 会议管理集合
     */
    public List<SysPropertyMeeting> selectSysPropertyMeetingList(SysPropertyMeeting sysPropertyMeeting);

    /**
     * 新增会议管理
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 结果
     */
    public int insertSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting);

    /**
     * 修改会议管理
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 结果
     */
    public int updateSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting);

    /**
     * 批量删除会议管理
     * 
     * @param meetingIds 需要删除的会议管理主键集合
     * @return 结果
     */
    public int deleteSysPropertyMeetingByMeetingIds(Long[] meetingIds);

    /**
     * 删除会议管理信息
     * 
     * @param meetingId 会议管理主键
     * @return 结果
     */
    public int deleteSysPropertyMeetingByMeetingId(Long meetingId);

    /**
     * 统计进行中的会议数量
     * 
     * @return 进行中的会议数量
     */
    public Long countOngoingMeetings();

    /**
     * 获取平均参与率
     * 
     * @return 平均参与率
     */
    public Double getAverageParticipationRate();

    /**
     * 统计即将开始的会议数量
     * 
     * @return 即将开始的会议数量
     */
    public Long countUpcomingMeetings();

    /**
     * 获取投票参与率趋势
     * 
     * @return 投票参与率趋势数据
     */
    public List<Map<String, Object>> getVoteParticipationTrend();

    /**
     * 获取会议活动统计
     * 
     * @return 会议活动统计数据
     */
    public List<Map<String, Object>> getMeetingActivityStats();

    /**
     * 获取最近投票记录
     * 
     * @param limit 限制数量
     * @return 最近投票记录
     */
    public List<Map<String, Object>> getRecentVotes(int limit);
}
