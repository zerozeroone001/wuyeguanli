package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysMeetingNotice;

/**
 * 会议通知记录Service接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public interface ISysMeetingNoticeService 
{
    /**
     * 查询会议通知记录
     * 
     * @param noticeId 会议通知记录主键
     * @return 会议通知记录
     */
    public SysMeetingNotice selectSysMeetingNoticeByNoticeId(Long noticeId);

    /**
     * 查询会议通知记录列表
     * 
     * @param sysMeetingNotice 会议通知记录
     * @return 会议通知记录集合
     */
    public List<SysMeetingNotice> selectSysMeetingNoticeList(SysMeetingNotice sysMeetingNotice);

    /**
     * 新增会议通知记录
     * 
     * @param sysMeetingNotice 会议通知记录
     * @return 结果
     */
    public int insertSysMeetingNotice(SysMeetingNotice sysMeetingNotice);

    /**
     * 修改会议通知记录
     * 
     * @param sysMeetingNotice 会议通知记录
     * @return 结果
     */
    public int updateSysMeetingNotice(SysMeetingNotice sysMeetingNotice);

    /**
     * 批量删除会议通知记录
     * 
     * @param noticeIds 需要删除的会议通知记录主键集合
     * @return 结果
     */
    public int deleteSysMeetingNoticeByNoticeIds(Long[] noticeIds);

    /**
     * 删除会议通知记录信息
     * 
     * @param noticeId 会议通知记录主键
     * @return 结果
     */
    public int deleteSysMeetingNoticeByNoticeId(Long noticeId);
}
