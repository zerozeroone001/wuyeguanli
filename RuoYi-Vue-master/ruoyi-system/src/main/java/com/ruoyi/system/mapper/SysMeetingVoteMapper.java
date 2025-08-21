package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysMeetingVote;

/**
 * 业主大会投票Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface SysMeetingVoteMapper 
{
    /**
     * 查询业主大会投票
     * 
     * @param voteId 业主大会投票主键
     * @return 业主大会投票
     */
    public SysMeetingVote selectSysMeetingVoteByVoteId(Long voteId);

    /**
     * 查询业主大会投票列表
     * 
     * @param sysMeetingVote 业主大会投票
     * @return 业主大会投票集合
     */
    public List<SysMeetingVote> selectSysMeetingVoteList(SysMeetingVote sysMeetingVote);

    /**
     * 新增业主大会投票
     * 
     * @param sysMeetingVote 业主大会投票
     * @return 结果
     */
    public int insertSysMeetingVote(SysMeetingVote sysMeetingVote);

    /**
     * 修改业主大会投票
     * 
     * @param sysMeetingVote 业主大会投票
     * @return 结果
     */
    public int updateSysMeetingVote(SysMeetingVote sysMeetingVote);

    /**
     * 删除业主大会投票
     * 
     * @param voteId 业主大会投票主键
     * @return 结果
     */
    public int deleteSysMeetingVoteByVoteId(Long voteId);

    /**
     * 批量删除业主大会投票
     * 
     * @param voteIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysMeetingVoteByVoteIds(Long[] voteIds);
}
