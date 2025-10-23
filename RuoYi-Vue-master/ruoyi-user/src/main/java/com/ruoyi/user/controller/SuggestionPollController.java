package com.ruoyi.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.SysFormSubmission;
import com.ruoyi.system.domain.SysFormTemplate;
import com.ruoyi.system.domain.SysSuggestionPoll;
import com.ruoyi.system.service.ISysFormSubmissionService;
import com.ruoyi.system.service.ISysFormTemplateService;
import com.ruoyi.system.service.ISysSuggestionPollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户端意见征询/问卷接口
 *
 * 提供问卷列表、详情、提交等能力。
 *
 * @author ruoyi
 * @date 2025-10-23
 */
@RestController
@RequestMapping("/poll")
public class SuggestionPollController extends BaseController
{
    @Autowired
    private ISysSuggestionPollService suggestionPollService;

    @Autowired
    private ISysFormTemplateService formTemplateService;

    @Autowired
    private ISysFormSubmissionService formSubmissionService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取当前可用的问卷列表
     */
    @GetMapping("/list")
    public AjaxResult list()
    {
        SysSuggestionPoll query = new SysSuggestionPoll();
        query.setStatus("1"); // 进行中
        List<SysSuggestionPoll> polls = suggestionPollService.selectSysSuggestionPollList(query);
        Date now = new Date();

        List<Map<String, Object>> result = polls.stream()
            .filter(poll -> poll != null)
            .filter(poll -> {
                Date start = poll.getStartTime();
                Date end = poll.getEndTime();
                boolean afterStart = (start == null) || !now.before(start);
                boolean beforeEnd = (end == null) || !now.after(end);
                return afterStart && beforeEnd;
            })
            .map(poll -> {
                Map<String, Object> item = new HashMap<>();
                item.put("pollId", poll.getPollId());
                item.put("title", poll.getTitle());
                item.put("description", poll.getDescription());
                item.put("status", poll.getStatus());
                item.put("startTime", poll.getStartTime());
                item.put("endTime", poll.getEndTime());
                item.put("formId", poll.getFormId());
                item.put("formName", poll.getFormName());
                return item;
            })
            .collect(Collectors.toList());

        return AjaxResult.success(result);
    }

    /**
     * 获取问卷详情与表单结构
     */
    @GetMapping("/{pollId}")
    public AjaxResult detail(@PathVariable("pollId") Long pollId)
    {
        SysSuggestionPoll poll = suggestionPollService.selectSysSuggestionPollByPollId(pollId);
        if (poll == null)
        {
            return AjaxResult.error("问卷不存在");
        }
        if (poll.getFormId() == null)
        {
            return AjaxResult.error("问卷尚未绑定表单模板");
        }

        SysFormTemplate formTemplate = formTemplateService.selectSysFormTemplateByFormId(poll.getFormId());
        if (formTemplate == null || StringUtils.equals("1", formTemplate.getStatus()))
        {
            return AjaxResult.error("问卷表单模板不可用");
        }

        Map<String, Object> data = new HashMap<>(4);
        data.put("poll", poll);
        data.put("form", formTemplate);

        Long userId = getUserId();
        if (userId != null)
        {
            SysFormSubmission submission = formSubmissionService.selectByPollIdAndUserId(pollId, userId);
            if (submission != null)
            {
                data.put("submission", submission);
            }
        }

        return AjaxResult.success(data);
    }

    /**
     * 提交问卷答案
     */
    @PostMapping("/{pollId}/submit")
    public AjaxResult submit(@PathVariable("pollId") Long pollId,
                             @RequestBody Map<String, Object> payload,
                             HttpServletRequest request)
    {
        Long userId = getUserId();
        if (userId == null)
        {
            return AjaxResult.error("用户未登录");
        }

        SysSuggestionPoll poll = suggestionPollService.selectSysSuggestionPollByPollId(pollId);
        if (poll == null)
        {
            return AjaxResult.error("问卷不存在");
        }
        if (poll.getFormId() == null)
        {
            return AjaxResult.error("问卷尚未绑定表单模板");
        }

        Date now = new Date();
        if (poll.getStartTime() != null && now.before(poll.getStartTime()))
        {
            return AjaxResult.error("问卷尚未开始");
        }
        if (poll.getEndTime() != null && now.after(poll.getEndTime()))
        {
            return AjaxResult.error("问卷已结束");
        }

        Object answersObj = payload.get("answers");
        if (answersObj == null)
        {
            return AjaxResult.error("缺少问卷答案");
        }

        Object clientTypeObj = payload.get("clientType");
        String clientType = clientTypeObj == null ? "mobile" : clientTypeObj.toString();
        String remark = (String) payload.get("remark");

        String answersJson;
        try
        {
            answersJson = objectMapper.writeValueAsString(answersObj);
        }
        catch (JsonProcessingException e)
        {
            return AjaxResult.error("问卷答案格式错误");
        }

        SysFormSubmission submission = new SysFormSubmission();
        submission.setPollId(pollId);
        submission.setFormId(poll.getFormId());
        submission.setUserId(userId);
        submission.setClientType(clientType);
        submission.setSubmitIp(IpUtils.getIpAddr(request));
        submission.setStatus("0");
        submission.setRemark(remark);
        submission.setAnswersData(answersJson);

        formSubmissionService.saveOrUpdateSubmission(submission);

        return AjaxResult.success("提交成功");
    }
}
