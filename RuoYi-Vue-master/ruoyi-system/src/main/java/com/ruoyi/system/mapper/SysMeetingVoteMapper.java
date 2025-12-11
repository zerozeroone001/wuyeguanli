package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.domain.vo.VoteListExportVO;
import com.ruoyi.system.domain.vo.VoteRecordVO;
import com.ruoyi.system.domain.vo.VoteResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ruoyi.system.domain.vo.UserVoteDetailVO;

public interface SysMeetingVoteMapper {

    SysMeetingVote findVote(@Param("userId") Long userId, @Param("topicId") Long topicId);

    int insertVote(SysMeetingVote vote);

    int updateVote(SysMeetingVote vote);

    List<SysMeetingVote> selectUserVotesInMeeting(@Param("userId") Long userId, @Param("meetingId") Long meetingId);

    List<SysMeetingVote> selectVotesByTopicId(Long topicId);

    SysMeetingVote selectSysMeetingVoteByVoteId(Long voteId);

    List<SysMeetingVote> selectSysMeetingVoteList(SysMeetingVote sysMeetingVote);

    int insertSysMeetingVote(SysMeetingVote sysMeetingVote);

    int updateSysMeetingVote(SysMeetingVote sysMeetingVote);

    int deleteSysMeetingVoteByVoteIds(Long[] voteIds);

    /**
     * 查询会议投票记录列表
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @param userName 用户名/房号/手机号
     * @param voteStatus 投票状态
     * @return 投票记录列表
     */
    List<VoteRecordVO> selectVoteRecordsList(@Param("meetingId") Long meetingId,
                                             @Param("communityId") Long communityId,
                                             @Param("userName") String userName,
                                             @Param("voteStatus") String voteStatus);

    /**
     * 查询会议的表决结果统计(按议题)
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @return 表决结果列表
     */
    List<VoteResultVO> selectVoteResultsByMeeting(@Param("meetingId") Long meetingId,
                                                   @Param("communityId") Long communityId);

    /**
     * 查询投票列表用于Excel导出
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @return 投票列表导出数据
     */
    List<VoteListExportVO> selectVoteListForExport(@Param("meetingId") Long meetingId,
                                                    @Param("communityId") Long communityId);

    /**
     * 查询用户在某次会议中的详细投票记录
     * @param meetingId 会议ID
     * @param userId 用户ID
     * @return 用户投票详情列表
     */
    List<UserVoteDetailVO> selectUserVoteDetails(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

    /**
     * 批量新增会议投票
     * 
     * @param sysMeetingVoteList 会议投票列表
     * @return 结果
     */
    public int batchInsertSysMeetingVote(List<SysMeetingVote> sysMeetingVoteList);
}
