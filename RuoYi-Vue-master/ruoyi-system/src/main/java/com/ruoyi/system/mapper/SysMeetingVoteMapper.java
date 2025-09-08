package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysMeetingVote;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}