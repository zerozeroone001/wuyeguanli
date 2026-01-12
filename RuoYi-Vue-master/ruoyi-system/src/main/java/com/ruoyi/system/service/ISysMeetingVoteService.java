package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysMeetingTopic;
import com.ruoyi.system.domain.SysMeetingVote;

import java.util.List;
import java.util.Map;

public interface ISysMeetingVoteService {

    SysMeetingVote selectSysMeetingVoteByVoteId(Long voteId);

    List<SysMeetingVote> selectSysMeetingVoteList(SysMeetingVote sysMeetingVote);

    int insertSysMeetingVote(SysMeetingVote sysMeetingVote);

    int updateSysMeetingVote(SysMeetingVote sysMeetingVote);

    int deleteSysMeetingVoteByVoteIds(Long[] voteIds);

    Map<Long, String> selectUserVotesInMeeting(Long userId, Long meetingId);

    List<SysMeetingVote> selectSysMeetingVoteListByTopicId(Long topicId);

    SysMeetingTopic submitVote(SysMeetingVote vote);

    /**
     * 查找用户对指定议题的投票记录
     * 
     * @param userId 用户ID
     * @param topicId 议题ID
     * @return 投票记录，未找到返回null
     */
    SysMeetingVote findVote(Long userId, Long topicId);

    /**
     * 查询投票列表用于导出
     *
     * @param meetingId 会议ID
     * @param communityId 小区ID
     * @return 投票导出列表
     */
    List<com.ruoyi.system.domain.vo.VoteListExportVO> selectVoteListForExport(Long meetingId, Long communityId);
}