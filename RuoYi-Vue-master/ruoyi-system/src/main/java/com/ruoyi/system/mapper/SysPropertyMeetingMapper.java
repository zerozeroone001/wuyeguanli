package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.domain.vo.MeetingVoteReportVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

/**
 * 浼氳绠＄悊Mapper鎺ュ彛
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Mapper
public interface SysPropertyMeetingMapper 
{
    /**
     * 鏌ヨ浼氳绠＄悊
     * 
     * @param meetingId 浼氳绠＄悊涓婚敭
     * @return 浼氳绠＄悊
     */
    public SysPropertyMeeting selectSysPropertyMeetingByMeetingId(Long meetingId);

    /**
     * 鏌ヨ浼氳绠＄悊鍒楄〃
     * 
     * @param sysPropertyMeeting 浼氳绠＄悊
     * @return 浼氳绠＄悊闆嗗悎
     */
    public List<SysPropertyMeeting> selectSysPropertyMeetingList(SysPropertyMeeting sysPropertyMeeting);

    public List<SysPropertyMeeting> userSelectSysPropertyMeetingList(SysPropertyMeeting sysPropertyMeeting);

    /**
     * 鏂板浼氳绠＄悊
     * 
     * @param sysPropertyMeeting 浼氳绠＄悊
     * @return 缁撴灉
     */
    public int insertSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting);

    /**
     * 淇敼浼氳绠＄悊
     * 
     * @param sysPropertyMeeting 浼氳绠＄悊
     * @return 缁撴灉
     */
    public int updateSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting);

    /**
     * 鍒犻櫎浼氳绠＄悊淇℃伅
     * 
     * @param meetingId 浼氳绠＄悊涓婚敭
     * @return 缁撴灉
     */
    public int deleteSysPropertyMeetingByMeetingId(Long meetingId);

    /**
     * 鑾峰彇浼氳鏍囪
     *
     * @return 缁撴灉
     */
    public List<Map<String, Object>> getMeetingMarks(@Param("communityId") Long communityId);


    /**
     * 鎵归噺鍒犻櫎浼氳绠＄悊
     * 
     * @param meetingIds 闇€瑕佸垹闄ょ殑鏁版嵁涓婚敭闆嗗悎
     * @return 缁撴灉
     */
    public int deleteSysPropertyMeetingByMeetingIds(Long[] meetingIds);

    /**
     * 获取会议投票统计报告数据(用于PDF导出)
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @return 报告数据
     */
    MeetingVoteReportVO selectMeetingVoteReportData(@Param("meetingId") Long meetingId,
                                                     @Param("communityId") Long communityId);

    /**
     * 获取楼栋投票统计
     *
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @return 统计结果
     */
    List<Map<String, Object>> getBuildingVoteStats(@Param("meetingId") Long meetingId, @Param("communityId") Long communityId);

    /**
     * 统计根据条件进行的会议数
     */
    Long countOngoingMeetings();

    /**
     * 获取平均参与率
     */
    Double getAverageParticipationRate();

    /**
     * 获取投票参与趋势
     */
    List<Map<String, Object>> getVoteParticipationTrend();

    /**
     * 获取会议活动统计
     */
    List<Map<String, Object>> getMeetingActivityStats();

    /**
     * 获取最近的投票
     */
    List<Map<String, Object>> getRecentVotes(@Param("limit") int limit);

    /**
     * 统计即将开始的会议
     */
    Long countUpcomingMeetings();
}

