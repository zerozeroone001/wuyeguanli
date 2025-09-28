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
        return votes.stream()
                .filter(vote -> vote.getVoteOption() != null) // 过滤掉voteOption为null的记录
                .collect(Collectors.toMap(
                    SysMeetingVote::getTopicId, 
                    vote -> vote.getVoteOption().toString()
                ));
    }

    @Override
    public List<SysMeetingVote> selectSysMeetingVoteListByTopicId(Long topicId) {
        return meetingVoteMapper.selectVotesByTopicId(topicId);
    }

    @Override
    @Transactional
    public SysMeetingTopic submitVote(SysMeetingVote vote) {
        // 参数验证
        if (vote == null || vote.getVoteOption() == null || vote.getTopicId() == null || vote.getUserId() == null) {
            throw new IllegalArgumentException("投票信息不完整，voteOption、topicId、userId不能为空");
        }
        
        // 1. Find if a vote already exists
        SysMeetingVote existingVote = meetingVoteMapper.findVote(vote.getUserId(), vote.getTopicId());

        // 2. Decrement old vote count if it exists and choice is different
        if (existingVote != null && existingVote.getVoteOption() != null && !existingVote.getVoteOption().equals(vote.getVoteOption())) {
            meetingTopicMapper.decrementVoteCount(existingVote.getTopicId(), existingVote.getVoteOption().toString());
        }

        // 3. Insert or Update the vote record
        if (existingVote == null) {
            meetingVoteMapper.insertVote(vote);
        } else {
            existingVote.setVoteOption(vote.getVoteOption());
            existingVote.setVoteType(vote.getVoteType());
            existingVote.setFlieUrl(vote.getFlieUrl());
            meetingVoteMapper.updateVote(existingVote);
        }

        // 4. Increment new vote count
        meetingTopicMapper.incrementVoteCount(vote.getTopicId(), vote.getVoteOption().toString());

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