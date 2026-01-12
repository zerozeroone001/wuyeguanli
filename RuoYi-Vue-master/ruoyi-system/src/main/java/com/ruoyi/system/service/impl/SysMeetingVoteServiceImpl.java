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
            throw new IllegalArgumentException("投票信息不完整,voteOption、topicId、userId不能为空");
        }
        
        // 1. Find if a vote already exists
        SysMeetingVote existingVote = meetingVoteMapper.findVote(vote.getUserId(), vote.getTopicId());

        boolean isNewVote = (existingVote == null);
        boolean isChangeVote = (existingVote != null || existingVote.getVoteOption() !=null || (existingVote.getVoteOption()!=null &&!existingVote.getVoteOption().equals(vote.getVoteOption())));

        if (!isNewVote && !isChangeVote) {
            // Vote is identical, no stats update needed, just return current topic state
            return meetingTopicMapper.selectMeetingTopicById(vote.getTopicId());
        }

        // 2. 保护后台导入的数据,不允许修改
        if (existingVote != null && existingVote.getVoteType() != null && existingVote.getVoteType() == 1) {
            throw new IllegalArgumentException("该投票记录为后台导入数据,不允许修改");
        }

        // 3. Decrement old vote count if it exists and choice is different
        if (isChangeVote && existingVote.getVoteOption() != null ) {
            meetingTopicMapper.decrementVoteCount(existingVote.getTopicId(), existingVote.getVoteOption().toString());
        }

        // 4. Insert or Update the vote record
        if (isNewVote) {
            // 为新投票生成编号,优先使用该用户在同一会议下已有的编号
            if (vote.getVoteNo() == null || vote.getVoteNo().isEmpty()) {
                String voteNo = getOrGenerateVoteNo(vote.getUserId(), vote.getMeetingId());
                vote.setVoteNo(voteNo);
            }
            meetingVoteMapper.insertVote(vote);
        } else {
            existingVote.setVoteOption(vote.getVoteOption());
            existingVote.setVoteType(vote.getVoteType());
            existingVote.setFlieUrl(vote.getFlieUrl());
            meetingVoteMapper.updateVote(existingVote);
        }

        // 5. Increment new vote count
        meetingTopicMapper.incrementVoteCount(vote.getTopicId(), vote.getVoteOption().toString());

        // 6. Return the updated topic with new counts
        return meetingTopicMapper.selectMeetingTopicById(vote.getTopicId());
    }

    /**
     * 获取或生成投票编号
     * 优先使用该用户在同一会议下已有的投票编号,保持一致性
     * 如果没有已有编号,则生成新的16位随机编号
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     * @return 投票编号
     */
    private String getOrGenerateVoteNo(Long userId, Long meetingId) {
        // 查询该用户在该会议下是否已有投票记录
        SysMeetingVote query = new SysMeetingVote();
        query.setUserId(userId);
        query.setMeetingId(meetingId);
        List<SysMeetingVote> existingVotes = meetingVoteMapper.selectSysMeetingVoteList(query);
        
        // 如果已有投票记录且有编号,使用已有编号
        if (existingVotes != null && !existingVotes.isEmpty()) {
            for (SysMeetingVote existingVote : existingVotes) {
                if (existingVote.getVoteNo() != null && !existingVote.getVoteNo().isEmpty()) {
                    return existingVote.getVoteNo();
                }
            }
        }
        
        // 生成新的16位随机编号,与后台导入逻辑一致
        return org.apache.commons.lang3.RandomStringUtils.random(16, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
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

    @Override
    public List<com.ruoyi.system.domain.vo.VoteListExportVO> selectVoteListForExport(Long meetingId, Long communityId) {
        return meetingVoteMapper.selectVoteListForExport(meetingId, communityId);
    }
}