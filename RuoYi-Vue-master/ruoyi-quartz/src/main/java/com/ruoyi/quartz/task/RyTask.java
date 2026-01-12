package com.ruoyi.quartz.task;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.service.ISysPropertyMeetingService;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    @Autowired
    private ISysPropertyMeetingService sysPropertyMeetingService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
        if ("meeting".equals(params))
        {
            SysPropertyMeeting sysPropertyMeeting = new SysPropertyMeeting();
            List<SysPropertyMeeting> list = sysPropertyMeetingService.selectSysPropertyMeetingList(sysPropertyMeeting);
            if (list != null && list.size() > 0)
            {
                Date now = new Date();
                for (SysPropertyMeeting meetingVo : list)
                {
                    String status = "0";
                    // 存在投票时间
                    if (meetingVo.getVoteStartTime() != null && meetingVo.getVoteEndTime() != null)
                    {
                        if (now.getTime() < meetingVo.getVoteStartTime().getTime())
                        {
                            status = "0";
                        }
                        else if (now.getTime() > meetingVo.getVoteEndTime().getTime())
                        {
                            status = "2";
                        }
                        else
                        {
                            status = "1";
                        }
                    }
                    // 仅存在会议时间
                    else if (meetingVo.getMeetingTime() != null)
                    {
                        if (now.getTime() < meetingVo.getMeetingTime().getTime())
                        {
                            status = "0";
                        }
                        else
                        {
                            status = "1";
                        }
                    }
                    
                    if (meetingVo.getMeetingStatus() == null || !meetingVo.getMeetingStatus().equals(status))
                    {
                        meetingVo.setMeetingStatus(status);
                        sysPropertyMeetingService.updateMeetingStatus(meetingVo);
                    }
                }
            }
        }
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }
}
