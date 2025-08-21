package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysGeneralMeeting;

/**
 * 业主大会会议管理Service接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface ISysGeneralMeetingService 
{
    /**
     * 查询业主大会会议管理
     * 
     * @param meetingId 业主大会会议管理主键
     * @return 业主大会会议管理
     */
    public SysGeneralMeeting selectSysGeneralMeetingByMeetingId(Long meetingId);

    /**
     * 查询业主大会会议管理列表
     * 
     * @param sysGeneralMeeting 业主大会会议管理
     * @return 业主大会会议管理集合
     */
    public List<SysGeneralMeeting> selectSysGeneralMeetingList(SysGeneralMeeting sysGeneralMeeting);

    /**
     * 新增业主大会会议管理
     * 
     * @param sysGeneralMeeting 业主大会会议管理
     * @return 结果
     */
    public int insertSysGeneralMeeting(SysGeneralMeeting sysGeneralMeeting);

    /**
     * 修改业主大会会议管理
     * 
     * @param sysGeneralMeeting 业主大会会议管理
     * @return 结果
     */
    public int updateSysGeneralMeeting(SysGeneralMeeting sysGeneralMeeting);

    /**
     * 批量删除业主大会会议管理
     * 
     * @param meetingIds 需要删除的业主大会会议管理主键集合
     * @return 结果
     */
    public int deleteSysGeneralMeetingByMeetingIds(Long[] meetingIds);

    /**
     * 删除业主大会会议管理信息
     * 
     * @param meetingId 业主大会会议管理主键
     * @return 结果
     */
    public int deleteSysGeneralMeetingByMeetingId(Long meetingId);
}
