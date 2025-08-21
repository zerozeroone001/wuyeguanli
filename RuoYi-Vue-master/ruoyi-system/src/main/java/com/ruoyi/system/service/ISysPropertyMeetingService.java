package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysPropertyMeeting;

/**
 * 会议管理Service接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface ISysPropertyMeetingService 
{
    /**
     * 查询会议管理
     * 
     * @param meetingId 会议管理主键
     * @return 会议管理
     */
    public SysPropertyMeeting selectSysPropertyMeetingByMeetingId(Long meetingId);

    /**
     * 查询会议管理列表
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 会议管理集合
     */
    public List<SysPropertyMeeting> selectSysPropertyMeetingList(SysPropertyMeeting sysPropertyMeeting);

    /**
     * 新增会议管理
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 结果
     */
    public int insertSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting);

    /**
     * 修改会议管理
     * 
     * @param sysPropertyMeeting 会议管理
     * @return 结果
     */
    public int updateSysPropertyMeeting(SysPropertyMeeting sysPropertyMeeting);

    /**
     * 批量删除会议管理
     * 
     * @param meetingIds 需要删除的会议管理主键集合
     * @return 结果
     */
    public int deleteSysPropertyMeetingByMeetingIds(Long[] meetingIds);

    /**
     * 删除会议管理信息
     * 
     * @param meetingId 会议管理主键
     * @return 结果
     */
    public int deleteSysPropertyMeetingByMeetingId(Long meetingId);
}
