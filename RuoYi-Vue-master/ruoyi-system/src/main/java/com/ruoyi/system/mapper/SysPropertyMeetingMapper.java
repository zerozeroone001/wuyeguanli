package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SysPropertyMeeting;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会议管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Mapper
public interface SysPropertyMeetingMapper 
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
     * 删除会议管理信息
     * 
     * @param meetingId 会议管理主键
     * @return 结果
     */
    public int deleteSysPropertyMeetingByMeetingId(Long meetingId);

    /**
     * 获取会议标记
     *
     * @return 结果
     */
    public List<Map<String, Object>> getMeetingMarks();


    /**
     * 批量删除会议管理
     * 
     * @param meetingIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysPropertyMeetingByMeetingIds(Long[] meetingIds);
}
