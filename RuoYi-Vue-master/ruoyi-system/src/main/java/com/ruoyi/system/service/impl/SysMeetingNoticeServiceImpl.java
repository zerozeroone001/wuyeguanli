package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMeetingNoticeMapper;
import com.ruoyi.system.domain.SysMeetingNotice;
import com.ruoyi.system.service.ISysMeetingNoticeService;

/**
 * 会议通知记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Service
public class SysMeetingNoticeServiceImpl implements ISysMeetingNoticeService 
{
    @Autowired
    private SysMeetingNoticeMapper sysMeetingNoticeMapper;

    /**
     * 查询会议通知记录
     * 
     * @param noticeId 会议通知记录主键
     * @return 会议通知记录
     */
    @Override
    public SysMeetingNotice selectSysMeetingNoticeByNoticeId(Long noticeId)
    {
        return sysMeetingNoticeMapper.selectSysMeetingNoticeByNoticeId(noticeId);
    }

    /**
     * 查询会议通知记录列表
     * 
     * @param sysMeetingNotice 会议通知记录
     * @return 会议通知记录
     */
    @Override
    public List<SysMeetingNotice> selectSysMeetingNoticeList(SysMeetingNotice sysMeetingNotice)
    {
        return sysMeetingNoticeMapper.selectSysMeetingNoticeList(sysMeetingNotice);
    }

    /**
     * 新增会议通知记录
     * 
     * @param sysMeetingNotice 会议通知记录
     * @return 结果
     */
    @Override
    public int insertSysMeetingNotice(SysMeetingNotice sysMeetingNotice)
    {
        sysMeetingNotice.setCreateTime(DateUtils.getNowDate());
        return sysMeetingNoticeMapper.insertSysMeetingNotice(sysMeetingNotice);
    }

    /**
     * 修改会议通知记录
     * 
     * @param sysMeetingNotice 会议通知记录
     * @return 结果
     */
    @Override
    public int updateSysMeetingNotice(SysMeetingNotice sysMeetingNotice)
    {
        sysMeetingNotice.setUpdateTime(DateUtils.getNowDate());
        return sysMeetingNoticeMapper.updateSysMeetingNotice(sysMeetingNotice);
    }

    /**
     * 批量删除会议通知记录
     * 
     * @param noticeIds 需要删除的会议通知记录主键
     * @return 结果
     */
    @Override
    public int deleteSysMeetingNoticeByNoticeIds(Long[] noticeIds)
    {
        return sysMeetingNoticeMapper.deleteSysMeetingNoticeByNoticeIds(noticeIds);
    }

    /**
     * 删除会议通知记录信息
     * 
     * @param noticeId 会议通知记录主键
     * @return 结果
     */
    @Override
    public int deleteSysMeetingNoticeByNoticeId(Long noticeId)
    {
        return sysMeetingNoticeMapper.deleteSysMeetingNoticeByNoticeId(noticeId);
    }
}
