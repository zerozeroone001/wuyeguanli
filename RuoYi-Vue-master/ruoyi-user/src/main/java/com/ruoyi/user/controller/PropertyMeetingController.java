package com.ruoyi.user.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysMeetingTopic;
import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.domain.SysOpinionConsultation;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会议管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/meeting")
public class PropertyMeetingController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(PropertyMeetingController.class);

    @Autowired
    private ISysPropertyMeetingService sysPropertyMeetingService;

    @Autowired
    private ISysMeetingVoteService meetingVoteService;

    @Autowired
    private ISysMeetingTopicService meetingTopicService;

    @Autowired
    private ISysOpinionConsultationService opinionConsultationService;

    /**
     * 查询会议管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyMeeting sysPropertyMeeting)
    {
        startPage();
        List<SysPropertyMeeting> list = sysPropertyMeetingService.selectSysPropertyMeetingList(sysPropertyMeeting);
        return getDataTable(list);
    }

    /**
     * 导出会议管理列表
     */
    @Log(title = "会议管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyMeeting sysPropertyMeeting)
    {
        List<SysPropertyMeeting> list = sysPropertyMeetingService.selectSysPropertyMeetingList(sysPropertyMeeting);
        ExcelUtil<SysPropertyMeeting> util = new ExcelUtil<SysPropertyMeeting>(SysPropertyMeeting.class);
        util.exportExcel(response, list, "会议管理数据");
    }

    /**
     * 获取会议管理详细信息
     */
    @GetMapping(value = "/{meetingId}")
    public AjaxResult getInfo(@PathVariable("meetingId") Long meetingId)
    {
        return success(sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(meetingId));
    }

    /**
     * 新增会议管理
     */
    @Log(title = "会议管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPropertyMeeting sysPropertyMeeting)
    {
        return toAjax(sysPropertyMeetingService.insertSysPropertyMeeting(sysPropertyMeeting));
    }

    /**
     * 修改会议管理
     */
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPropertyMeeting sysPropertyMeeting)
    {
        return toAjax(sysPropertyMeetingService.updateSysPropertyMeeting(sysPropertyMeeting));
    }

    /**
     * 删除会议管理
     */
    @Log(title = "会议管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{meetingIds}")
    public AjaxResult remove(@PathVariable Long[] meetingIds)
    {
        return toAjax(sysPropertyMeetingService.deleteSysPropertyMeetingByMeetingIds(meetingIds));
    }

    @GetMapping("/marks")
    public AjaxResult getMeetingMarks() {
        return AjaxResult.success(sysPropertyMeetingService.getMeetingMarks());
    }

    @GetMapping("/{meetingId}/topics")
    public AjaxResult getMeetingTopics(@PathVariable("meetingId") Long meetingId) {
        return AjaxResult.success(meetingTopicService.selectMeetingTopicList(meetingId));
    }

    /**
     * 获取用户在指定会议中的投票记录
     * @param meetingId 会议ID
     * @return 用户投票记录
     */
    @GetMapping("/{meetingId}/my-votes")
    public AjaxResult getMyVotes(@PathVariable("meetingId") Long meetingId) {
//        try {
            Long userId = getUserId();
            if (userId == null) {
                return AjaxResult.error("用户未登录");
            }
            
            if (meetingId == null) {
                return AjaxResult.error("会议ID不能为空");
            }

            // 验证会议是否存在
            SysPropertyMeeting meeting = sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(meetingId);
            if (meeting == null) {
                return AjaxResult.error("会议不存在");
            }

            Map<Long, String> voteData = meetingVoteService.selectUserVotesInMeeting(userId, meetingId);
            return AjaxResult.success(voteData);
//        } catch (Exception e) {
//            log.error("获取用户投票记录失败", e);
//            return AjaxResult.error("获取投票记录失败：" + e.getMessage());
//        }
    }

    /**
     * 提交投票
     * @param vote 投票信息
     * @return 投票结果
     */
    @PostMapping("/vote")
    public AjaxResult submitVote(@RequestBody SysMeetingVote vote) {
        try {
            // 1. 参数验证
            if (vote == null) {
                return AjaxResult.error("投票信息不能为空");
            }
            if (vote.getMeetingId() == null) {
                return AjaxResult.error("会议ID不能为空");
            }
            if (vote.getTopicId() == null) {
                return AjaxResult.error("议题ID不能为空");
            }
            if (vote.getVoteOption() == null) {
                return AjaxResult.error("投票选项不能为空");
            }
            if (vote.getVoteOption() < 0 || vote.getVoteOption() > 2) {
                return AjaxResult.error("投票选项无效，只能是0(同意)、1(反对)、2(弃权)");
            }

            // 2. 获取当前用户信息
            Long userId = getUserId();
            String userName = getUsername();
            if (userId == null) {
                return AjaxResult.error("用户未登录");
            }

            // 3. 验证会议是否存在且处于投票状态
            SysPropertyMeeting meeting = sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(vote.getMeetingId());
            if (meeting == null) {
                return AjaxResult.error("会议不存在");
            }
            
            // 检查会议状态
            if (!"1".equals(meeting.getMeetingStatus())) {
                return AjaxResult.error("会议未开始投票或已结束");
            }

            // 检查投票时间
            java.util.Date now = new java.util.Date();
            if (meeting.getVoteStartTime() != null && now.before(meeting.getVoteStartTime())) {
                return AjaxResult.error("投票尚未开始");
            }
            if (meeting.getVoteEndTime() != null && now.after(meeting.getVoteEndTime())) {
                return AjaxResult.error("投票已截止");
            }

            // 4. 验证议题是否属于该会议
            SysMeetingTopic topic = meetingTopicService.selectMeetingTopicById(vote.getTopicId());
            if (topic == null) {
                return AjaxResult.error("议题不存在");
            }
            if (!vote.getMeetingId().equals(topic.getMeetingId())) {
                return AjaxResult.error("议题不属于该会议");
            }

            // 5. 设置投票信息
            vote.setUserId(userId);
            vote.setUserName(userName);
            if (vote.getVoteType() == null) {
                vote.setVoteType(0); // 默认为小程序投票
            }

            // 6. 提交投票
            SysMeetingTopic updatedTopic = meetingVoteService.submitVote(vote);

            // 7. 构建返回数据
            AjaxResult result = AjaxResult.success("投票成功");
            result.put("topic", updatedTopic);
            result.put("voteOption", vote.getVoteOption());
            result.put("voteTime", new java.util.Date());
            
            return result;

        } catch (IllegalArgumentException e) {
            return AjaxResult.error("参数错误：" + e.getMessage());
        } catch (Exception e) {
            log.error("投票失败", e);
            return AjaxResult.error("投票失败：" + e.getMessage());
        }
    }

    @PostMapping("/opinion")
    public AjaxResult submitOpinion(@RequestBody Map<String, Object> payload) {
        Long topicId = Long.valueOf(payload.get("topicId").toString());
        String opinion = payload.get("opinion").toString();

        SysMeetingTopic topic = meetingTopicService.selectMeetingTopicById(topicId);
        if (topic == null) {
            return AjaxResult.error("议题不存在");
        }

        SysOpinionConsultation consultation = new SysOpinionConsultation();
        consultation.setUserId(getUserId());
        consultation.setTitle(topic.getTopicTitle());
        consultation.setContent(opinion);
        consultation.setSourceType("MEETING_TOPIC");
        consultation.setSourceId(topicId);
        consultation.setCreateBy(getUsername());
        consultation.setUpdateBy(getUsername());

        return toAjax(opinionConsultationService.insertSysOpinionConsultation(consultation));
    }

    /**
     * 获取会议投票统计信息
     * @param meetingId 会议ID
     * @return 投票统计信息
     */
    @GetMapping("/{meetingId}/vote-statistics")
    public AjaxResult getVoteStatistics(@PathVariable("meetingId") Long meetingId) {
        try {
            if (meetingId == null) {
                return AjaxResult.error("会议ID不能为空");
            }

            // 验证会议是否存在
            SysPropertyMeeting meeting = sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(meetingId);
            if (meeting == null) {
                return AjaxResult.error("会议不存在");
            }

            // 获取会议议题列表
            List<SysMeetingTopic> topics = meetingTopicService.selectMeetingTopicList(meetingId);
            
            AjaxResult result = AjaxResult.success();
            result.put("meeting", meeting);
            result.put("topics", topics);
            result.put("totalTopics", topics.size());
            
            // 计算总投票数
            int totalVotes = topics.stream()
                .mapToInt(topic -> (topic.getAgreeCount() != null ? topic.getAgreeCount() : 0) + 
                                 (topic.getOpposeCount() != null ? topic.getOpposeCount() : 0) + 
                                 (topic.getAbstainCount() != null ? topic.getAbstainCount() : 0))
                .sum();
            result.put("totalVotes", totalVotes);
            
            return result;
        } catch (Exception e) {
            log.error("获取投票统计信息失败", e);
            return AjaxResult.error("获取统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 检查用户是否可以投票
     * @param meetingId 会议ID
     * @return 投票权限检查结果
     */
    @GetMapping("/{meetingId}/vote-permission")
    public AjaxResult checkVotePermission(@PathVariable("meetingId") Long meetingId) {
        try {
            Long userId = getUserId();
            if (userId == null) {
                return AjaxResult.error("用户未登录");
            }
            
            if (meetingId == null) {
                return AjaxResult.error("会议ID不能为空");
            }

            // 验证会议是否存在
            SysPropertyMeeting meeting = sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(meetingId);
            if (meeting == null) {
                return AjaxResult.error("会议不存在");
            }

            AjaxResult result = AjaxResult.success();
            result.put("canVote", true);
            result.put("message", "可以投票");

            // 检查会议状态
            if (!"1".equals(meeting.getMeetingStatus())) {
                result.put("canVote", false);
                result.put("message", "会议未开始投票或已结束");
                return result;
            }

            // 检查投票时间
            java.util.Date now = new java.util.Date();
            if (meeting.getVoteStartTime() != null && now.before(meeting.getVoteStartTime())) {
                result.put("canVote", false);
                result.put("message", "投票尚未开始");
                result.put("voteStartTime", meeting.getVoteStartTime());
                return result;
            }
            if (meeting.getVoteEndTime() != null && now.after(meeting.getVoteEndTime())) {
                result.put("canVote", false);
                result.put("message", "投票已截止");
                result.put("voteEndTime", meeting.getVoteEndTime());
                return result;
            }

            // 添加时间信息
            result.put("voteStartTime", meeting.getVoteStartTime());
            result.put("voteEndTime", meeting.getVoteEndTime());
            result.put("currentTime", now);
            
            return result;
        } catch (Exception e) {
            log.error("检查投票权限失败", e);
            return AjaxResult.error("权限检查失败：" + e.getMessage());
        }
    }

    /**
     * 获取我的投票记录列表
     * @param query 查询参数
     * @return 投票记录列表
     */
    @GetMapping("/vote/my")
    public TableDataInfo getMyVoteRecords(SysMeetingVote query) {
        try {
            Long userId = null;
            try {
                userId = getUserId();
            } catch (Exception e) {
                log.warn("获取用户ID失败，可能用户未登录: {}", e.getMessage());
                return getDataTable(new ArrayList<>());
            }
            
            if (userId == null) {
                log.warn("用户ID为空");
                return getDataTable(new ArrayList<>());
            }
            
            log.info("获取投票记录，用户ID: {}", userId);
            
            // 设置用户ID过滤条件
            query.setUserId(userId);
            
            startPage();
            List<SysMeetingVote> list = meetingVoteService.selectSysMeetingVoteList(query);
            log.info("查询到投票记录数量: {}", list.size());
            
            // 创建包含扩展信息的投票记录列表
            List<Map<String, Object>> resultList = new ArrayList<>();
            
            for (SysMeetingVote vote : list) {
                Map<String, Object> voteMap = new HashMap<>();
                
                // 基础投票信息
                voteMap.put("voteId", vote.getVoteId());
                voteMap.put("meetingId", vote.getMeetingId());
                voteMap.put("topicId", vote.getTopicId());
                voteMap.put("userId", vote.getUserId());
                voteMap.put("userName", vote.getUserName());
                voteMap.put("voteOption", vote.getVoteOption());
                voteMap.put("voteType", vote.getVoteType());
                voteMap.put("createTime", vote.getCreateTime());
                voteMap.put("updateTime", vote.getUpdateTime());
                
                // 补充会议信息
                if (vote.getMeetingId() != null) {
                    SysPropertyMeeting meeting = sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(vote.getMeetingId());
                    if (meeting != null) {
                        voteMap.put("meetingTitle", meeting.getMeetingTitle());
                        voteMap.put("meetingStatus", meeting.getMeetingStatus());
                        voteMap.put("voteStartTime", meeting.getVoteStartTime());
                        voteMap.put("voteEndTime", meeting.getVoteEndTime());
                    }
                }
                
                // 补充议题信息
                if (vote.getTopicId() != null) {
                    SysMeetingTopic topic = meetingTopicService.selectMeetingTopicById(vote.getTopicId());
                    if (topic != null) {
                        voteMap.put("topicTitle", topic.getTopicTitle());
                        voteMap.put("topicDescription", topic.getTopicContent());
                        int voteCount = (topic.getAgreeCount() != null ? topic.getAgreeCount() : 0) + 
                                      (topic.getOpposeCount() != null ? topic.getOpposeCount() : 0) + 
                                      (topic.getAbstainCount() != null ? topic.getAbstainCount() : 0);
                        voteMap.put("voteCount", voteCount);
                    }
                }
                
                // 设置投票选项文本
                String myChoice = "未知";
                if (vote.getVoteOption() != null) {
                    switch (vote.getVoteOption()) {
                        case 0:
                            myChoice = "同意";
                            break;
                        case 1:
                            myChoice = "反对";
                            break;
                        case 2:
                            myChoice = "弃权";
                            break;
                    }
                }
                voteMap.put("myChoice", myChoice);
                
                // 设置会议状态文本
                String status = "unknown";
                String meetingStatus = (String) voteMap.get("meetingStatus");
                if (meetingStatus != null) {
                    switch (meetingStatus) {
                        case "0":
                            status = "pending";
                            break;
                        case "1":
                            status = "ongoing";
                            break;
                        case "2":
                            status = "finished";
                            break;
                    }
                }
                voteMap.put("status", status);
                
                resultList.add(voteMap);
            }
            
            // 返回包含扩展信息的分页结果
            TableDataInfo dataTable = getDataTable(list);
            dataTable.setRows(resultList);
            log.info("返回投票记录数据，原始记录数: {}, 处理后记录数: {}", list.size(), resultList.size());
            return dataTable;
        } catch (Exception e) {
            log.error("获取我的投票记录失败", e);
            return getDataTable(new ArrayList<>());
        }
    }


    /**
     * 撤销投票（在允许的时间内）
     * @param voteId 投票ID
     * @return 撤销结果
     */
    @DeleteMapping("/vote/{voteId}")
    public AjaxResult withdrawVote(@PathVariable("voteId") Long voteId) {
        try {
            Long userId = getUserId();
            if (userId == null) {
                return AjaxResult.error("用户未登录");
            }
            
            if (voteId == null) {
                return AjaxResult.error("投票ID不能为空");
            }

            // 获取投票记录
            SysMeetingVote vote = meetingVoteService.selectSysMeetingVoteByVoteId(voteId);
            if (vote == null) {
                return AjaxResult.error("投票记录不存在");
            }

            // 验证是否是当前用户的投票
            if (!userId.equals(vote.getUserId())) {
                return AjaxResult.error("只能撤销自己的投票");
            }

            // 验证会议是否还在投票期内
            SysPropertyMeeting meeting = sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(vote.getMeetingId());
            if (meeting == null) {
                return AjaxResult.error("会议不存在");
            }

            java.util.Date now = new java.util.Date();
            if (meeting.getVoteEndTime() != null && now.after(meeting.getVoteEndTime())) {
                return AjaxResult.error("投票已截止，无法撤销");
            }

            // 删除投票记录（这里可能需要根据业务需求调整）
            int result = meetingVoteService.deleteSysMeetingVoteByVoteIds(new Long[]{voteId});
            if (result > 0) {
                return AjaxResult.success("投票撤销成功");
            } else {
                return AjaxResult.error("投票撤销失败");
            }
        } catch (Exception e) {
            log.error("撤销投票失败", e);
            return AjaxResult.error("撤销投票失败：" + e.getMessage());
        }
    }
}
