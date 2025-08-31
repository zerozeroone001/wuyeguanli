package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysPropertyMeetingTopic;
import com.ruoyi.system.service.ISysPropertyMeetingTopicService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 会议议题Controller
 * 
 * @author ruoyi
 * @date 2025-08-31
 */
@RestController
@RequestMapping("/system/topic")
public class SysPropertyMeetingTopicController extends BaseController
{
    @Autowired
    private ISysPropertyMeetingTopicService sysPropertyMeetingTopicService;

    /**
     * 查询会议议题列表
     */
    @PreAuthorize("@ss.hasPermi('system:topic:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyMeetingTopic sysPropertyMeetingTopic)
    {
        startPage();
        List<SysPropertyMeetingTopic> list = sysPropertyMeetingTopicService.selectSysPropertyMeetingTopicList(sysPropertyMeetingTopic);
        return getDataTable(list);
    }

    /**
     * 导出会议议题列表
     */
    @PreAuthorize("@ss.hasPermi('system:topic:export')")
    @Log(title = "会议议题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyMeetingTopic sysPropertyMeetingTopic)
    {
        List<SysPropertyMeetingTopic> list = sysPropertyMeetingTopicService.selectSysPropertyMeetingTopicList(sysPropertyMeetingTopic);
        ExcelUtil<SysPropertyMeetingTopic> util = new ExcelUtil<SysPropertyMeetingTopic>(SysPropertyMeetingTopic.class);
        util.exportExcel(response, list, "会议议题数据");
    }

    /**
     * 获取会议议题详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:topic:query')")
    @GetMapping(value = "/{topicId}")
    public AjaxResult getInfo(@PathVariable("topicId") Long topicId)
    {
        return success(sysPropertyMeetingTopicService.selectSysPropertyMeetingTopicByTopicId(topicId));
    }

    /**
     * 新增会议议题
     */
    @PreAuthorize("@ss.hasPermi('system:topic:add')")
    @Log(title = "会议议题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPropertyMeetingTopic sysPropertyMeetingTopic)
    {
        return toAjax(sysPropertyMeetingTopicService.insertSysPropertyMeetingTopic(sysPropertyMeetingTopic));
    }

    /**
     * 修改会议议题
     */
    @PreAuthorize("@ss.hasPermi('system:topic:edit')")
    @Log(title = "会议议题", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPropertyMeetingTopic sysPropertyMeetingTopic)
    {
        return toAjax(sysPropertyMeetingTopicService.updateSysPropertyMeetingTopic(sysPropertyMeetingTopic));
    }

    /**
     * 删除会议议题
     */
    @PreAuthorize("@ss.hasPermi('system:topic:remove')")
    @Log(title = "会议议题", businessType = BusinessType.DELETE)
	@DeleteMapping("/{topicIds}")
    public AjaxResult remove(@PathVariable Long[] topicIds)
    {
        return toAjax(sysPropertyMeetingTopicService.deleteSysPropertyMeetingTopicByTopicIds(topicIds));
    }
}
