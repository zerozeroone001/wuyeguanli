package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMeetingVoteMapper;
import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.service.ISysMeetingVoteService;

/**
 * 业主大会投票Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysMeetingVoteServiceImpl implements ISysMeetingVoteService 
{
    @Autowired
    private SysMeetingVoteMapper sysMeetingVoteMapper;

    /**
     * 查询业主大会投票
     * 
     * @param voteId 业主大会投票主键
     * @return 业主大会投票
     */
    @Override
    public SysMeetingVote selectSysMeetingVoteByVoteId(Long voteId)
    {
        return sysMeetingVoteMapper.selectSysMeetingVoteByVoteId(voteId);
    }

    /**
     * 查询业主大会投票列表
     * 
     * @param sysMeetingVote 业主大会投票
     * @return 业主大会投票
     */
    @Override
    public List<SysMeetingVote> selectSysMeetingVoteList(SysMeetingVote sysMeetingVote)
    {
        return sysMeetingVoteMapper.selectSysMeetingVoteList(sysMeetingVote);
    }

    /**
     * 新增业主大会投票
     * 
     * @param sysMeetingVote 业主大会投票
     * @return 结果
     */
    @Override
    public int insertSysMeetingVote(SysMeetingVote sysMeetingVote)
    {
        sysMeetingVote.setCreateTime(DateUtils.getNowDate());
        return sysMeetingVoteMapper.insertSysMeetingVote(sysMeetingVote);
    }

    /**
     * 修改业主大会投票
     * 
     * @param sysMeetingVote 业主大会投票
     * @return 结果
     */
    @Override
    public int updateSysMeetingVote(SysMeetingVote sysMeetingVote)
    {
        sysMeetingVote.setUpdateTime(DateUtils.getNowDate());
        return sysMeetingVoteMapper.updateSysMeetingVote(sysMeetingVote);
    }

    /**
     * 批量删除业主大会投票
     * 
     * @param voteIds 需要删除的业主大会投票主键
     * @return 结果
     */
    @Override
    public int deleteSysMeetingVoteByVoteIds(Long[] voteIds)
    {
        return sysMeetingVoteMapper.deleteSysMeetingVoteByVoteIds(voteIds);
    }

    /**
     * 删除业主大会投票信息
     * 
     * @param voteId 业主大会投票主键
     * @return 结果
     */
    @Override
    public int deleteSysMeetingVoteByVoteId(Long voteId)
    {
        return sysMeetingVoteMapper.deleteSysMeetingVoteByVoteId(voteId);
    }
}
