package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysCommitteeMeeting;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业主委员会会议管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Mapper
public interface SysCommitteeMeetingMapper 
{
    /**
     * 查询业主委员会会议管理
     * 
     * @param meetingId 业主委员会会议管理主键
     * @return 业主委员会会议管理
     */
    public SysCommitteeMeeting selectSysCommitteeMeetingByMeetingId(Long meetingId);

    /**
     * 查询业主委员会会议管理列表
     * 
     * @param sysCommitteeMeeting 业主委员会会议管理
     * @return 业主委员会会议管理集合
     */
    public List<SysCommitteeMeeting> selectSysCommitteeMeetingList(SysCommitteeMeeting sysCommitteeMeeting);

    /**
     * 新增业主委员会会议管理
     * 
     * @param sysCommitteeMeeting 业主委员会会议管理
     * @return 结果
     */
    public int insertSysCommitteeMeeting(SysCommitteeMeeting sysCommitteeMeeting);

    /**
     * 修改业主委员会会议管理
     * 
     * @param sysCommitteeMeeting 业主委员会会议管理
     * @return 结果
     */
    public int updateSysCommitteeMeeting(SysCommitteeMeeting sysCommitteeMeeting);

    /**
     * 删除业主委员会会议管理
     * 
     * @param meetingId 业主委员会会议管理主键
     * @return 结果
     */
    public int deleteSysCommitteeMeetingByMeetingId(Long meetingId);

    /**
     * 批量删除业主委员会会议管理
     * 
     * @param meetingIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCommitteeMeetingByMeetingIds(Long[] meetingIds);
}
