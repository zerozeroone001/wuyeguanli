package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.SysMeetingLogMapper;
import com.ruoyi.system.domain.SysMeetingLog;
import com.ruoyi.system.service.ISysMeetingLogService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 业主大会日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-26
 */
@Service
public class SysMeetingLogServiceImpl implements ISysMeetingLogService 
{
    @Autowired
    private SysMeetingLogMapper sysMeetingLogMapper;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询业主大会日志
     * 
     * @param logId 业主大会日志主键
     * @return 业主大会日志
     */
    @Override
    public SysMeetingLog selectSysMeetingLogByLogId(Long logId)
    {
        return sysMeetingLogMapper.selectSysMeetingLogByLogId(logId);
    }

    /**
     * 查询业主大会日志列表
     * 
     * @param sysMeetingLog 业主大会日志
     * @return 业主大会日志
     */
    @Override
    public List<SysMeetingLog> selectSysMeetingLogList(SysMeetingLog sysMeetingLog)
    {
        return sysMeetingLogMapper.selectSysMeetingLogList(sysMeetingLog);
    }

    /**
     * 新增业主大会日志
     * 
     * @param sysMeetingLog 业主大会日志
     * @return 结果
     */
    @Override
    public int insertSysMeetingLog(SysMeetingLog sysMeetingLog)
    {
        if (sysMeetingLog.getCreateTime() == null) {
            sysMeetingLog.setCreateTime(new Date());
        }
        return sysMeetingLogMapper.insertSysMeetingLog(sysMeetingLog);
    }

    /**
     * 修改业主大会日志
     * 
     * @param sysMeetingLog 业主大会日志
     * @return 结果
     */
    @Override
    public int updateSysMeetingLog(SysMeetingLog sysMeetingLog)
    {
        return sysMeetingLogMapper.updateSysMeetingLog(sysMeetingLog);
    }

    /**
     * 批量删除业主大会日志
     * 
     * @param logIds 需要删除的业主大会日志主键
     * @return 结果
     */
    @Override
    public int deleteSysMeetingLogByLogIds(Long[] logIds)
    {
        return sysMeetingLogMapper.deleteSysMeetingLogByLogIds(logIds);
    }

    /**
     * 删除业主大会日志信息
     * 
     * @param logId 业主大会日志主键
     * @return 结果
     */
    @Override
    public int deleteSysMeetingLogByLogId(Long logId)
    {
        return sysMeetingLogMapper.deleteSysMeetingLogByLogId(logId);
    }

    /**
     * 查询用户在特定会议的最新日志
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     * @return 业主大会日志
     */
    @Override
    public SysMeetingLog selectLatestLogByUserAndMeeting(Long userId, Long meetingId)
    {
        return sysMeetingLogMapper.selectLatestLogByUserAndMeeting(userId, meetingId);
    }

    /**
     * 查询指定会议的所有日志
     * 
     * @param meetingId 会议ID
     * @return 业主大会日志集合
     */
    @Override
    public List<SysMeetingLog> selectLogsByMeetingId(Long meetingId)
    {
        return sysMeetingLogMapper.selectLogsByMeetingId(meetingId);
    }

    /**
     * 记录用户访问业主大会详情日志
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     * @param hasVoted 是否已投票
     */
    @Override
    public void logMeetingAccess(Long userId, Long meetingId, boolean hasVoted)
    {
        // 查询用户信息
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return;
        }

        // 创建日志记录
        SysMeetingLog log = new SysMeetingLog();
        log.setMeetingId(meetingId);
        log.setUserId(userId);
        log.setUserName(user.getUserName());
        log.setPhone(user.getPhonenumber());
        
        // 根据是否已投票设置日志类型
        if (hasVoted) {
            log.setLogType(2); // 线上-已参会已投票
            log.setLogDesc("用户访问业主大会详情页面，已投票");
        } else {
            log.setLogType(1); // 线上-已参会未投票
            log.setLogDesc("用户访问业主大会详情页面，未投票");
        }
        
        insertSysMeetingLog(log);
    }

    /**
     * 记录用户投票日志
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     */
    @Override
    public void logMeetingVote(Long userId, Long meetingId)
    {
        // 查询用户信息
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return;
        }

        // 创建日志记录
        SysMeetingLog log = new SysMeetingLog();
        log.setMeetingId(meetingId);
        log.setUserId(userId);
        log.setUserName(user.getUserName());
        log.setPhone(user.getPhonenumber());
        log.setLogType(2); // 线上-已参会已投票
        log.setLogDesc("用户提交投票");
        
        insertSysMeetingLog(log);
    }

    /**
     * 记录线下拜访日志
     * 
     * @param userId 用户ID
     * @param meetingId 会议ID
     * @param logType 日志类型(3-7)
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param remark 备注
     */
    @Override
    public void logOfflineVisit(Long userId, Long meetingId, Integer logType, Long operatorId, String operatorName, String remark)
    {
        // 验证日志类型范围
        if (logType < 3 || logType > 7) {
            throw new IllegalArgumentException("线下拜访日志类型必须在3-7之间");
        }

        // 查询用户信息
        SysUser user = sysUserService.selectUserById(userId);
        if (user == null) {
            return;
        }

        // 创建日志记录
        SysMeetingLog log = new SysMeetingLog();
        log.setMeetingId(meetingId);
        log.setUserId(userId);
        log.setUserName(user.getUserName());
        log.setPhone(user.getPhonenumber());
        log.setLogType(logType);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setRemark(remark);
        
        // 根据日志类型设置描述
        switch (logType) {
            case 3:
                log.setLogDesc("线下拜访-已送无人");
                break;
            case 4:
                log.setLogDesc("线下拜访-已送未收");
                break;
            case 5:
                log.setLogDesc("线下拜访-已收未投");
                break;
            case 6:
                log.setLogDesc("线下拜访-已投待唱");
                break;
            case 7:
                log.setLogDesc("线下拜访-已唱");
                break;
        }
        
        insertSysMeetingLog(log);
    }
}
