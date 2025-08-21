package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysSuggestionPollMapper;
import com.ruoyi.system.domain.SysSuggestionPoll;
import com.ruoyi.system.service.ISysSuggestionPollService;

/**
 * 意见征询主Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysSuggestionPollServiceImpl implements ISysSuggestionPollService 
{
    @Autowired
    private SysSuggestionPollMapper sysSuggestionPollMapper;

    /**
     * 查询意见征询主
     * 
     * @param pollId 意见征询主主键
     * @return 意见征询主
     */
    @Override
    public SysSuggestionPoll selectSysSuggestionPollByPollId(Long pollId)
    {
        return sysSuggestionPollMapper.selectSysSuggestionPollByPollId(pollId);
    }

    /**
     * 查询意见征询主列表
     * 
     * @param sysSuggestionPoll 意见征询主
     * @return 意见征询主
     */
    @Override
    public List<SysSuggestionPoll> selectSysSuggestionPollList(SysSuggestionPoll sysSuggestionPoll)
    {
        return sysSuggestionPollMapper.selectSysSuggestionPollList(sysSuggestionPoll);
    }

    /**
     * 新增意见征询主
     * 
     * @param sysSuggestionPoll 意见征询主
     * @return 结果
     */
    @Override
    public int insertSysSuggestionPoll(SysSuggestionPoll sysSuggestionPoll)
    {
        sysSuggestionPoll.setCreateTime(DateUtils.getNowDate());
        return sysSuggestionPollMapper.insertSysSuggestionPoll(sysSuggestionPoll);
    }

    /**
     * 修改意见征询主
     * 
     * @param sysSuggestionPoll 意见征询主
     * @return 结果
     */
    @Override
    public int updateSysSuggestionPoll(SysSuggestionPoll sysSuggestionPoll)
    {
        sysSuggestionPoll.setUpdateTime(DateUtils.getNowDate());
        return sysSuggestionPollMapper.updateSysSuggestionPoll(sysSuggestionPoll);
    }

    /**
     * 批量删除意见征询主
     * 
     * @param pollIds 需要删除的意见征询主主键
     * @return 结果
     */
    @Override
    public int deleteSysSuggestionPollByPollIds(Long[] pollIds)
    {
        return sysSuggestionPollMapper.deleteSysSuggestionPollByPollIds(pollIds);
    }

    /**
     * 删除意见征询主信息
     * 
     * @param pollId 意见征询主主键
     * @return 结果
     */
    @Override
    public int deleteSysSuggestionPollByPollId(Long pollId)
    {
        return sysSuggestionPollMapper.deleteSysSuggestionPollByPollId(pollId);
    }
}
