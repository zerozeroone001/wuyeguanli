package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysMeetingTopic;
import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.mapper.SysMeetingTopicMapper;
import com.ruoyi.system.mapper.SysMeetingVoteMapper;
import com.ruoyi.system.service.ISysMeetingVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysMeetingVoteServiceImpl implements ISysMeetingVoteService {

    @Autowired
    private SysMeetingVoteMapper meetingVoteMapper;

    @Autowired
    private SysMeetingTopicMapper meetingTopicMapper;

    @Override
    public Map<Long, String> selectUserVotesInMeeting(Long userId, Long meetingId) {
        List<SysMeetingVote> votes = meetingVoteMapper.selectUserVotesInMeeting(userId, meetingId);
        return votes.stream().collect(Collectors.toMap(SysMeetingVote::getTopicId, SysMeetingVote::getChoice));
    }

    @Override
    public List<SysMeetingVote> selectSysMeetingVoteListByTopicId(Long topicId) {
        return meetingVoteMapper.selectVotesByTopicId(topicId);
    }

    @Override
    @Transactional
    public SysMeetingTopic submitVote(SysMeetingVote vote) {
        // 1. Find if a vote already exists
        SysMeetingVote existingVote = meetingVoteMapper.findVote(vote.getUserId(), vote.getTopicId());

        // 2. Decrement old vote count if it exists and choice is different
        if (existingVote != null && !existingVote.getChoice().equals(vote.getChoice())) {
            meetingTopicMapper.decrementVoteCount(existingVote.getTopicId(), existingVote.getChoice());
        }

        // 3. Insert or Update the vote record
        if (existingVote == null) {
            meetingVoteMapper.insertVote(vote);
        } else {
            existingVote.setChoice(vote.getChoice());
            meetingVoteMapper.updateVote(existingVote);
        }

        // 4. Increment new vote count
        meetingTopicMapper.incrementVoteCount(vote.getTopicId(), vote.getChoice());

        // 5. Return the updated topic with new counts
        return meetingTopicMapper.selectMeetingTopicById(vote.getTopicId());
    }

    @Override
    public SysMeetingVote selectSysMeetingVoteByVoteId(Long voteId) {
        return meetingVoteMapper.selectSysMeetingVoteByVoteId(voteId);
    }

    @Override
    public List<SysMeetingVote> selectSysMeetingVoteList(SysMeetingVote sysMeetingVote) {
        return meetingVoteMapper.selectSysMeetingVoteList(sysMeetingVote);
    }

    @Override
    public int insertSysMeetingVote(SysMeetingVote sysMeetingVote) {
        return meetingVoteMapper.insertSysMeetingVote(sysMeetingVote);
    }

    @Override
    public int updateSysMeetingVote(SysMeetingVote sysMeetingVote) {
        return meetingVoteMapper.updateSysMeetingVote(sysMeetingVote);
    }

    @Override
    public int deleteSysMeetingVoteByVoteIds(Long[] voteIds) {
        return meetingVoteMapper.deleteSysMeetingVoteByVoteIds(voteIds);
    }

    @Override
    public SysMeetingVote findVote(Long userId, Long topicId) {
        return meetingVoteMapper.findVote(userId, topicId);
    }
}