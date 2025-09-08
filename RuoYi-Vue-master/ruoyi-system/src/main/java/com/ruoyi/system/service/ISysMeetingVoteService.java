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
}