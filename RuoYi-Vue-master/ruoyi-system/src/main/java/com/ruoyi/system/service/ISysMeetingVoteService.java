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
}