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
import com.ruoyi.system.domain.SysMeetingLog;
import com.ruoyi.system.service.ISysMeetingLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 业主大会日志Controller
 * 
 * @author ruoyi
 * @date 2025-12-26
 */
@RestController
@RequestMapping("/system/meetingLog")
public class SysMeetingLogController extends BaseController
{
    @Autowired
    private ISysMeetingLogService sysMeetingLogService;

    /**
     * 查询业主大会日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:meetingLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysMeetingLog sysMeetingLog)
    {
        startPage();
        List<SysMeetingLog> list = sysMeetingLogService.selectSysMeetingLogList(sysMeetingLog);
        return getDataTable(list);
    }

    /**
     * 导出业主大会日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:meetingLog:export')")
    @Log(title = "业主大会日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysMeetingLog sysMeetingLog)
    {
        List<SysMeetingLog> list = sysMeetingLogService.selectSysMeetingLogList(sysMeetingLog);
        ExcelUtil<SysMeetingLog> util = new ExcelUtil<SysMeetingLog>(SysMeetingLog.class);
        util.exportExcel(response, list, "业主大会日志数据");
    }

    /**
     * 获取业主大会日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:meetingLog:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return success(sysMeetingLogService.selectSysMeetingLogByLogId(logId));
    }

    /**
     * 查询指定会议的所有日志
     */
    @PreAuthorize("@ss.hasPermi('system:meetingLog:query')")
    @GetMapping(value = "/meeting/{meetingId}")
    public AjaxResult getLogsByMeetingId(@PathVariable("meetingId") Long meetingId)
    {
        List<SysMeetingLog> logs = sysMeetingLogService.selectLogsByMeetingId(meetingId);
        return success(logs);
    }

    /**
     * 新增业主大会日志
     */
    @PreAuthorize("@ss.hasPermi('system:meetingLog:add')")
    @Log(title = "业主大会日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysMeetingLog sysMeetingLog)
    {
        return toAjax(sysMeetingLogService.insertSysMeetingLog(sysMeetingLog));
    }

    /**
     * 修改业主大会日志
     */
    @PreAuthorize("@ss.hasPermi('system:meetingLog:edit')")
    @Log(title = "业主大会日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMeetingLog sysMeetingLog)
    {
        return toAjax(sysMeetingLogService.updateSysMeetingLog(sysMeetingLog));
    }

    /**
     * 删除业主大会日志
     */
    @PreAuthorize("@ss.hasPermi('system:meetingLog:remove')")
    @Log(title = "业主大会日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(sysMeetingLogService.deleteSysMeetingLogByLogIds(logIds));
    }
}
