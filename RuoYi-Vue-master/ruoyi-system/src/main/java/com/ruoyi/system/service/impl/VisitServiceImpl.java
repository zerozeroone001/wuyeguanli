package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysAdmin;
import com.ruoyi.system.domain.SysMeetingLog;
import com.ruoyi.system.domain.SysMeetingNotice;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.domain.vo.BuildingVO;
import com.ruoyi.system.domain.vo.VisitRoomVO;
import com.ruoyi.system.mapper.EstatePropertyMapper;
import com.ruoyi.system.mapper.SysAdminMapper;
import com.ruoyi.system.mapper.SysMeetingLogMapper;
import com.ruoyi.system.mapper.SysMeetingNoticeMapper;
import com.ruoyi.system.service.ISysPropertyMeetingService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 上门拜访Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
@Service
public class VisitServiceImpl implements IVisitService 
{
    @Autowired
    private SysAdminMapper sysAdminMapper;

    @Autowired
    private EstatePropertyMapper estatePropertyMapper;

    @Autowired
    private SysMeetingNoticeMapper sysMeetingNoticeMapper;

    @Autowired
    private SysMeetingLogMapper sysMeetingLogMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysPropertyMeetingService sysPropertyMeetingService;

    /**
     * 检查当前用户是否为管理员
     * 
     * @param userId 用户ID
     * @return 管理员信息
     */
    @Override
    public Map<String, Object> checkIsAdmin(Long userId) 
    {
        Map<String, Object> result = new HashMap<>();
        
        // 查询sys_admin表
        SysAdmin query = new SysAdmin();
        query.setUserId(userId);
        List<SysAdmin> adminList = sysAdminMapper.selectSysAdminList(query);
        
        if (adminList != null && !adminList.isEmpty()) 
        {
            SysAdmin admin = adminList.get(0);
            result.put("isAdmin", true);
            result.put("communityId", admin.getCommunityId());
            result.put("communityName", admin.getCommunityName());
            result.put("adminId", admin.getAdminId());
            result.put("adminName", admin.getAdminName());
        } 
        else 
        {
            result.put("isAdmin", false);
        }
        
        return result;
    }

    /**
     * 获取小区楼栋列表
     * 
     * @param communityId 小区ID
     * @return 楼栋列表
     */
    @Override
    public List<BuildingVO> getBuildingList(Long communityId) 
    {
        List<Map<String, Object>> buildingInfoList = estatePropertyMapper.selectBuildingInfoByCommunityId(communityId);
        
        return buildingInfoList.stream().map(info -> {
            BuildingVO vo = new BuildingVO();
            vo.setBuildingName((String) info.get("buildingName"));
            vo.setHasUnit(((Number) info.get("hasUnit")).intValue() == 1);
            vo.setRoomCount(((Number) info.get("roomCount")).intValue());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取楼栋的单元列表
     * 
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @return 单元列表
     */
    @Override
    public List<String> getUnitList(Long communityId, String buildingName) 
    {
        return estatePropertyMapper.selectUnitsByBuilding(communityId, buildingName);
    }

    /**
     * 获取房屋列表及业主信息
     * 
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param unitName 单元名称(可选)
     * @param meetingId 会议ID
     * @return 房屋列表
     */
    @Override
    public List<VisitRoomVO> getRoomList(Long communityId, String buildingName, String unitName, Long meetingId) 
    {
        List<Map<String, Object>> roomDetailsList = estatePropertyMapper.selectRoomDetailsForVisit(communityId, buildingName, unitName, meetingId);
        
        return roomDetailsList.stream().map(detail -> {
            VisitRoomVO vo = new VisitRoomVO();
            vo.setPropertyId(((Number) detail.get("propertyId")).longValue());
            vo.setRoomNumber((String) detail.get("roomNumber"));
            
            // 用户信息可能为空(房屋未绑定业主)
            if (detail.get("userId") != null) 
            {
                vo.setUserId(((Number) detail.get("userId")).longValue());
                vo.setOwnerName((String) detail.get("ownerName"));
                vo.setPhone((String) detail.get("phone"));
                vo.setHasVoted(((Number) detail.get("hasVoted")).intValue() == 1);
                
                // 查询短信和电话通知记录
                SysMeetingNotice smsQuery = new SysMeetingNotice();
                smsQuery.setMeetingId(meetingId);
                smsQuery.setUserId(vo.getUserId());
                smsQuery.setVoteType(0); // 短信通知
                List<SysMeetingNotice> smsNotices = sysMeetingNoticeMapper.selectSysMeetingNoticeList(smsQuery);
                vo.setSmsNotifyStatus(smsNotices != null && !smsNotices.isEmpty() ? 1 : 0);
                
                SysMeetingNotice phoneQuery = new SysMeetingNotice();
                phoneQuery.setMeetingId(meetingId);
                phoneQuery.setUserId(vo.getUserId());
                phoneQuery.setVoteType(1); // 电话通知
                List<SysMeetingNotice> phoneNotices = sysMeetingNoticeMapper.selectSysMeetingNoticeList(phoneQuery);
                vo.setPhoneNotifyStatus(phoneNotices != null && !phoneNotices.isEmpty() ? 1 : 0);
                
                // 线下拜访状态从 SQL 查询结果中获取
                if (detail.get("offlineStatus") != null) 
                {
                    vo.setOfflineStatus(((Number) detail.get("offlineStatus")).intValue());
                    vo.setLogId(((Number) detail.get("logId")).longValue());
                } 
                else 
                {
                    vo.setOfflineStatus(0); // 默认未送
                }
            } 
            else 
            {
                vo.setOwnerName("未绑定");
                vo.setPhone("");
                vo.setHasVoted(false);
                vo.setSmsNotifyStatus(0);
                vo.setPhoneNotifyStatus(0);
                vo.setOfflineStatus(0);
            }
            
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 更新线下通知状态
     * 
     * @param meetingId 会议ID
     * @param userId 用户ID
     * @param offlineStatus 线下状态(0-未送, 3-已送无人, 4-已送未收, 5-已收未投, 6-已投待唱, 7-已唱)
     * @param visitorId 拜访人ID
     * @param visitorName 拜访人姓名
     * @return 结果
     */
    @Override
    public int updateOfflineStatus(Long meetingId, Long userId, Integer offlineStatus, Long visitorId, String visitorName) 
    {
        // 如果状态为0(未送),则不需要记录日志
        if (offlineStatus == null || offlineStatus == 0) 
        {
            return 1; // 返回成功,但不插入记录
        }
        
        // 查询用户信息
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) 
        {
            return 0; // 用户不存在,返回失败
        }
        
        // 查询会议信息
        SysPropertyMeeting meeting = sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(meetingId);
        
        // 创建新的日志记录
        SysMeetingLog log = new SysMeetingLog();
        log.setMeetingId(meetingId);
        log.setUserId(userId);
        log.setUserName(user.getUserName());
        log.setPhone(user.getPhonenumber());
        log.setLogType(offlineStatus); // 3,4,5,6,7
        log.setOperatorId(visitorId);
        log.setOperatorName(visitorName);
        log.setCreateTime(DateUtils.getNowDate());
        
        // 设置会议标题
        if (meeting != null) 
        {
            log.setMeetingTitle(meeting.getMeetingTitle());
        }
        
        // 根据状态类型设置日志描述和类型名称
        String logDesc = "";
        String logTypeName = "";
        switch (offlineStatus) 
        {
            case 3:
                logDesc = "线下拜访-已送无人";
                logTypeName = "线下拜访-已送无人";
                break;
            case 4:
                logDesc = "线下拜访-已送未收";
                logTypeName = "线下拜访-已送未收";
                break;
            case 5:
                logDesc = "线下拜访-已收未投";
                logTypeName = "线下拜访-已收未投";
                break;
            case 6:
                logDesc = "线下拜访-已投待唱";
                logTypeName = "线下拜访-已投待唱";
                break;
            case 7:
                logDesc = "线下拜访-已唱";
                logTypeName = "线下拜访-已唱";
                break;
            default:
                logDesc = "线下拜访";
                logTypeName = "线下拜访";
        }
        log.setLogDesc(logDesc);
        log.setLogTypeName(logTypeName);
        
        return sysMeetingLogMapper.insertSysMeetingLog(log);
    }
}
