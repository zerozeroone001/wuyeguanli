package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysSuggestionPoll;

/**
 * 意见征询主Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface SysSuggestionPollMapper 
{
    /**
     * 查询意见征询主
     * 
     * @param pollId 意见征询主主键
     * @return 意见征询主
     */
    public SysSuggestionPoll selectSysSuggestionPollByPollId(Long pollId);

    /**
     * 查询意见征询主列表
     * 
     * @param sysSuggestionPoll 意见征询主
     * @return 意见征询主集合
     */
    public List<SysSuggestionPoll> selectSysSuggestionPollList(SysSuggestionPoll sysSuggestionPoll);

    /**
     * 新增意见征询主
     * 
     * @param sysSuggestionPoll 意见征询主
     * @return 结果
     */
    public int insertSysSuggestionPoll(SysSuggestionPoll sysSuggestionPoll);

    /**
     * 修改意见征询主
     * 
     * @param sysSuggestionPoll 意见征询主
     * @return 结果
     */
    public int updateSysSuggestionPoll(SysSuggestionPoll sysSuggestionPoll);

    /**
     * 删除意见征询主
     * 
     * @param pollId 意见征询主主键
     * @return 结果
     */
    public int deleteSysSuggestionPollByPollId(Long pollId);

    /**
     * 批量删除意见征询主
     * 
     * @param pollIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysSuggestionPollByPollIds(Long[] pollIds);
}
