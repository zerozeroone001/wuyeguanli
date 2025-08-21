package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysGeneralMeetingMapper;
import com.ruoyi.system.domain.SysGeneralMeeting;
import com.ruoyi.system.service.ISysGeneralMeetingService;

/**
 * 业主大会会议管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysGeneralMeetingServiceImpl implements ISysGeneralMeetingService 
{
    @Autowired
    private SysGeneralMeetingMapper sysGeneralMeetingMapper;

    /**
     * 查询业主大会会议管理
     * 
     * @param meetingId 业主大会会议管理主键
     * @return 业主大会会议管理
     */
    @Override
    public SysGeneralMeeting selectSysGeneralMeetingByMeetingId(Long meetingId)
    {
        return sysGeneralMeetingMapper.selectSysGeneralMeetingByMeetingId(meetingId);
    }

    /**
     * 查询业主大会会议管理列表
     * 
     * @param sysGeneralMeeting 业主大会会议管理
     * @return 业主大会会议管理
     */
    @Override
    public List<SysGeneralMeeting> selectSysGeneralMeetingList(SysGeneralMeeting sysGeneralMeeting)
    {
        return sysGeneralMeetingMapper.selectSysGeneralMeetingList(sysGeneralMeeting);
    }

    /**
     * 新增业主大会会议管理
     * 
     * @param sysGeneralMeeting 业主大会会议管理
     * @return 结果
     */
    @Override
    public int insertSysGeneralMeeting(SysGeneralMeeting sysGeneralMeeting)
    {
        sysGeneralMeeting.setCreateTime(DateUtils.getNowDate());
        return sysGeneralMeetingMapper.insertSysGeneralMeeting(sysGeneralMeeting);
    }

    /**
     * 修改业主大会会议管理
     * 
     * @param sysGeneralMeeting 业主大会会议管理
     * @return 结果
     */
    @Override
    public int updateSysGeneralMeeting(SysGeneralMeeting sysGeneralMeeting)
    {
        sysGeneralMeeting.setUpdateTime(DateUtils.getNowDate());
        return sysGeneralMeetingMapper.updateSysGeneralMeeting(sysGeneralMeeting);
    }

    /**
     * 批量删除业主大会会议管理
     * 
     * @param meetingIds 需要删除的业主大会会议管理主键
     * @return 结果
     */
    @Override
    public int deleteSysGeneralMeetingByMeetingIds(Long[] meetingIds)
    {
        return sysGeneralMeetingMapper.deleteSysGeneralMeetingByMeetingIds(meetingIds);
    }

    /**
     * 删除业主大会会议管理信息
     * 
     * @param meetingId 业主大会会议管理主键
     * @return 结果
     */
    @Override
    public int deleteSysGeneralMeetingByMeetingId(Long meetingId)
    {
        return sysGeneralMeetingMapper.deleteSysGeneralMeetingByMeetingId(meetingId);
    }
}
