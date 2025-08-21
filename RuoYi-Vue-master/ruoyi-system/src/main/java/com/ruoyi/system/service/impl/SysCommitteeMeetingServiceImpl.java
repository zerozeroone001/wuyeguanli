package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysCommitteeMeetingMapper;
import com.ruoyi.system.domain.SysCommitteeMeeting;
import com.ruoyi.system.service.ISysCommitteeMeetingService;

/**
 * 业主委员会会议管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysCommitteeMeetingServiceImpl implements ISysCommitteeMeetingService 
{
    @Autowired
    private SysCommitteeMeetingMapper sysCommitteeMeetingMapper;

    /**
     * 查询业主委员会会议管理
     * 
     * @param meetingId 业主委员会会议管理主键
     * @return 业主委员会会议管理
     */
    @Override
    public SysCommitteeMeeting selectSysCommitteeMeetingByMeetingId(Long meetingId)
    {
        return sysCommitteeMeetingMapper.selectSysCommitteeMeetingByMeetingId(meetingId);
    }

    /**
     * 查询业主委员会会议管理列表
     * 
     * @param sysCommitteeMeeting 业主委员会会议管理
     * @return 业主委员会会议管理
     */
    @Override
    public List<SysCommitteeMeeting> selectSysCommitteeMeetingList(SysCommitteeMeeting sysCommitteeMeeting)
    {
        return sysCommitteeMeetingMapper.selectSysCommitteeMeetingList(sysCommitteeMeeting);
    }

    /**
     * 新增业主委员会会议管理
     * 
     * @param sysCommitteeMeeting 业主委员会会议管理
     * @return 结果
     */
    @Override
    public int insertSysCommitteeMeeting(SysCommitteeMeeting sysCommitteeMeeting)
    {
        sysCommitteeMeeting.setCreateTime(DateUtils.getNowDate());
        return sysCommitteeMeetingMapper.insertSysCommitteeMeeting(sysCommitteeMeeting);
    }

    /**
     * 修改业主委员会会议管理
     * 
     * @param sysCommitteeMeeting 业主委员会会议管理
     * @return 结果
     */
    @Override
    public int updateSysCommitteeMeeting(SysCommitteeMeeting sysCommitteeMeeting)
    {
        sysCommitteeMeeting.setUpdateTime(DateUtils.getNowDate());
        return sysCommitteeMeetingMapper.updateSysCommitteeMeeting(sysCommitteeMeeting);
    }

    /**
     * 批量删除业主委员会会议管理
     * 
     * @param meetingIds 需要删除的业主委员会会议管理主键
     * @return 结果
     */
    @Override
    public int deleteSysCommitteeMeetingByMeetingIds(Long[] meetingIds)
    {
        return sysCommitteeMeetingMapper.deleteSysCommitteeMeetingByMeetingIds(meetingIds);
    }

    /**
     * 删除业主委员会会议管理信息
     * 
     * @param meetingId 业主委员会会议管理主键
     * @return 结果
     */
    @Override
    public int deleteSysCommitteeMeetingByMeetingId(Long meetingId)
    {
        return sysCommitteeMeetingMapper.deleteSysCommitteeMeetingByMeetingId(meetingId);
    }
}
