package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysPropertyMeetingMapper;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.service.ISysPropertyMeetingService;

/**
 * 会议管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysPropertyMeetingServiceImpl implements ISysPropertyMeetingService 
{
    @Autowired
    private SysPropertyMeetingMapper sysPropertyMeetingMapper;

    /**
     * 查询会议管理
     * 
     * @param meetingId 会议管理主键
     * @return 会议管理
     */
    @Override
    public SysPropertyMeeting selectSysPropertyMeetingByMeetingId(Long meetingId)
    {
        return sysPropertyMeetingMapper.selectSysPropertyMeetingByMeetingId(meetingId);
    }

    /**
     * 查询会议管理列表
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 会议管理
     */
    @Override
    public List<SysPropertyMeeting> selectSysPropertyMeetingList(SysPropertyMeeting sysPropertyMeeting)
    {
        return sysPropertyMeetingMapper.selectSysPropertyMeetingList(sysPropertyMeeting);
    }

    /**
     * 新增会议管理
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 结果
     */
    @Override
    public int insertSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting)
    {
        sysPropertyMeeting.setCreateTime(DateUtils.getNowDate());
        return sysPropertyMeetingMapper.insertSysPropertyMeeting(sysPropertyMeeting);
    }

    /**
     * 修改会议管理
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 结果
     */
    @Override
    public int updateSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting)
    {
        sysPropertyMeeting.setUpdateTime(DateUtils.getNowDate());
        return sysPropertyMeetingMapper.updateSysPropertyMeeting(sysPropertyMeeting);
    }

    /**
     * 批量删除会议管理
     * 
     * @param meetingIds 需要删除的会议管理主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyMeetingByMeetingIds(Long[] meetingIds)
    {
        return sysPropertyMeetingMapper.deleteSysPropertyMeetingByMeetingIds(meetingIds);
    }

    /**
     * 删除会议管理信息
     * 
     * @param meetingId 会议管理主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyMeetingByMeetingId(Long meetingId)
    {
        return sysPropertyMeetingMapper.deleteSysPropertyMeetingByMeetingId(meetingId);
    }
}
