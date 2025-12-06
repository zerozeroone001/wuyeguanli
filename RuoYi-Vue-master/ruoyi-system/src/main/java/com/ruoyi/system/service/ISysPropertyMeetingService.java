package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.SysPropertyMeeting;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会议管理Service接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Mapper
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
     * 获取会议标记
     *
     * @return 结果
     */
    public List<Map<String, Object>> getMeetingMarks();


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

    /**
     * 发送会议通知（业主委员会会议）
     * 
     * @param meetingId 业主委员会会议管理主键
     */
    public void sendCommitteeMeetingNotification(Long meetingId);

    /**
     * 发送会议通知（业主大会会议）
     * 
     * @param meetingId 业主大会会议管理主键
     */
    public void sendGeneralMeetingNotification(Long meetingId);

    /**
     * 导出表决票
     *
     * @param response 响应
     * @param meetingId 会议ID
     * @param type 导出类型（blank=空白票，filled=填充票）
     */
    public void exportBallot(javax.servlet.http.HttpServletResponse response, Long meetingId, String type) throws java.io.IOException;

    /**
     * 导出投票结果
     *
     * @param response 响应
     * @param meetingId 会议ID
     */
    public void exportVotingResults(javax.servlet.http.HttpServletResponse response, Long meetingId) throws java.io.IOException;

    /**
     * 导出投票明细公开表
     *
     * @param response 响应
     * @param meetingId 会议ID
     */
    public void exportVotingDetailsPublic(javax.servlet.http.HttpServletResponse response, Long meetingId) throws java.io.IOException;

    /**
     * 导出会议文件
     *
     * @param response 响应
     * @param meetingId 会议ID
     */
    public void exportMeetingDocuments(javax.servlet.http.HttpServletResponse response, Long meetingId) throws java.io.IOException;

    /**
     * 获取楼栋投票统计
     *
     * @param meetingId 会议ID
     * @return 统计结果
     */
    public List<Map<String, Object>> getBuildingVoteStats(Long meetingId);

    /**
     * 复制会议
     *
     * @param meetingId 会议ID
     * @return 结果
     */
    public int copyMeeting(Long meetingId);
}
