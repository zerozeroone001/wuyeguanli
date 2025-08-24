package com.ruoyi.web.controller.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ISysPropertyComplaintService;
import com.ruoyi.system.service.ISysPropertyMeetingService;
import com.ruoyi.system.service.ISysPropertyFundFlowService;

/**
 * 仪表盘控制器
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/system/dashboard")
public class SysDashboardController extends BaseController
{
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysPropertyComplaintService complaintService;
    
    @Autowired
    private ISysPropertyMeetingService meetingService;
    
    @Autowired
    private ISysPropertyFundFlowService fundFlowService;

    /**
     * 获取仪表盘统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:view')")
    @GetMapping("/statistics")
    public AjaxResult getStatistics()
    {
        Map<String, Object> data = new HashMap<>();
        
        // 业主统计
        Map<String, Object> ownerStats = new HashMap<>();
        Long totalOwners = userService.countOwners();
        Long monthNewOwners = userService.countMonthNewOwners();
        ownerStats.put("total", totalOwners);
        ownerStats.put("monthNew", monthNewOwners);
        ownerStats.put("trend", monthNewOwners > 0 ? "up" : "stable");
        data.put("ownerStats", ownerStats);
        
        // 投票统计
        Map<String, Object> voteStats = new HashMap<>();
        Long ongoingVotes = meetingService.countOngoingMeetings();
        Double participationRate = meetingService.getAverageParticipationRate();
        voteStats.put("ongoing", ongoingVotes);
        voteStats.put("participationRate", participationRate);
        voteStats.put("trend", participationRate > 80 ? "up" : "down");
        data.put("voteStats", voteStats);
        
        // 投诉统计
        Map<String, Object> complaintStats = new HashMap<>();
        Long pendingComplaints = complaintService.countPendingComplaints();
        Double complaintGrowth = complaintService.getComplaintGrowthRate();
        complaintStats.put("pending", pendingComplaints);
        complaintStats.put("growth", complaintGrowth);
        complaintStats.put("trend", complaintGrowth > 0 ? "up" : "down");
        data.put("complaintStats", complaintStats);
        
        // 资金统计
        Map<String, Object> fundStats = new HashMap<>();
        BigDecimal totalFunds = fundFlowService.getTotalFunds();
        Double fundGrowth = fundFlowService.getFundGrowthRate();
        fundStats.put("total", totalFunds);
        fundStats.put("growth", fundGrowth);
        fundStats.put("trend", fundGrowth > 0 ? "up" : "down");
        data.put("fundStats", fundStats);
        
        return success(data);
    }

    /**
     * 获取待办事项列表
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:view')")
    @GetMapping("/todoList")
    public AjaxResult getTodoList()
    {
        List<Map<String, Object>> todoList = new ArrayList<>();
        
        // 紧急投诉待处理
        Long urgentComplaints = complaintService.countUrgentComplaints();
        if (urgentComplaints > 0) {
            Map<String, Object> todo = new HashMap<>();
            todo.put("id", 1);
            todo.put("type", "complaint");
            todo.put("title", "紧急投诉处理");
            todo.put("count", urgentComplaints);
            todo.put("priority", "high");
            todo.put("url", "/system/complaint");
            todo.put("icon", "el-icon-warning");
            todoList.add(todo);
        }
        
        // 资金审批待处理
        Long pendingApprovals = fundFlowService.countPendingApprovals();
        if (pendingApprovals > 0) {
            Map<String, Object> todo = new HashMap<>();
            todo.put("id", 2);
            todo.put("type", "fund");
            todo.put("title", "资金审批");
            todo.put("count", pendingApprovals);
            todo.put("priority", "medium");
            todo.put("url", "/system/fund");
            todo.put("icon", "el-icon-money");
            todoList.add(todo);
        }
        
        // 会议安排
        Long upcomingMeetings = meetingService.countUpcomingMeetings();
        if (upcomingMeetings > 0) {
            Map<String, Object> todo = new HashMap<>();
            todo.put("id", 3);
            todo.put("type", "meeting");
            todo.put("title", "会议安排");
            todo.put("count", upcomingMeetings);
            todo.put("priority", "normal");
            todo.put("url", "/system/meeting");
            todo.put("icon", "el-icon-date");
            todoList.add(todo);
        }
        
        return success(todoList);
    }

    /**
     * 获取图表数据
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:view')")
    @GetMapping("/chartData")
    public AjaxResult getChartData()
    {
        Map<String, Object> data = new HashMap<>();
        
        // 投票参与率趋势 (最近7天)
        List<Map<String, Object>> voteParticipation = meetingService.getVoteParticipationTrend();
        data.put("voteParticipation", voteParticipation);
        
        // 投诉处理趋势 (最近30天)
        List<Map<String, Object>> complaintTrend = complaintService.getComplaintTrend();
        data.put("complaintTrend", complaintTrend);
        
        // 资金收支分析 (本月)
        Map<String, Object> fundAnalysis = fundFlowService.getMonthFundAnalysis();
        data.put("fundAnalysis", fundAnalysis);
        
        // 会议活动统计 (最近3个月)
        List<Map<String, Object>> meetingActivity = meetingService.getMeetingActivityStats();
        data.put("meetingActivity", meetingActivity);
        
        return success(data);
    }

    /**
     * 获取最近活动
     */
    @PreAuthorize("@ss.hasPermi('system:dashboard:view')")
    @GetMapping("/recentActivity")
    public AjaxResult getRecentActivity()
    {
        List<Map<String, Object>> activities = new ArrayList<>();
        
        // 获取最近的投诉、投票、资金流水等活动
        List<Map<String, Object>> recentComplaints = complaintService.getRecentComplaints(5);
        List<Map<String, Object>> recentVotes = meetingService.getRecentVotes(5);
        List<Map<String, Object>> recentFunds = fundFlowService.getRecentFunds(5);
        
        activities.addAll(recentComplaints);
        activities.addAll(recentVotes);
        activities.addAll(recentFunds);
        
        // 按时间排序
        activities.sort((a, b) -> {
            String timeA = (String) a.get("time");
            String timeB = (String) b.get("time");
            if (timeA == null || timeB == null) {
                return 0;
            }
            return timeB.compareTo(timeA);
        });
        
        // 只返回最新的10条
        if (activities.size() > 10) {
            activities = activities.subList(0, 10);
        }
        
        return success(activities);
    }
}