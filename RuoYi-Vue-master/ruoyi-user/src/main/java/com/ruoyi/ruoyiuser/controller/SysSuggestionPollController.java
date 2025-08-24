package com.ruoyi.ruoyiuser.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysSuggestionPoll;
import com.ruoyi.system.service.ISysSuggestionPollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 意见征询主Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/poll")
public class SysSuggestionPollController extends BaseController
{
    @Autowired
    private ISysSuggestionPollService sysSuggestionPollService;

    /**
     * 查询意见征询主列表
     */
    @PreAuthorize("@ss.hasPermi('system:poll:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSuggestionPoll sysSuggestionPoll)
    {
        startPage();
        List<SysSuggestionPoll> list = sysSuggestionPollService.selectSysSuggestionPollList(sysSuggestionPoll);
        return getDataTable(list);
    }

    /**
     * 导出意见征询主列表
     */
    @PreAuthorize("@ss.hasPermi('system:poll:export')")
    @Log(title = "意见征询主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysSuggestionPoll sysSuggestionPoll)
    {
        List<SysSuggestionPoll> list = sysSuggestionPollService.selectSysSuggestionPollList(sysSuggestionPoll);
        ExcelUtil<SysSuggestionPoll> util = new ExcelUtil<SysSuggestionPoll>(SysSuggestionPoll.class);
        util.exportExcel(response, list, "意见征询主数据");
    }

    /**
     * 获取意见征询主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:poll:query')")
    @GetMapping(value = "/{pollId}")
    public AjaxResult getInfo(@PathVariable("pollId") Long pollId)
    {
        return success(sysSuggestionPollService.selectSysSuggestionPollByPollId(pollId));
    }

    /**
     * 新增意见征询主
     */
    @PreAuthorize("@ss.hasPermi('system:poll:add')")
    @Log(title = "意见征询主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSuggestionPoll sysSuggestionPoll)
    {
        return toAjax(sysSuggestionPollService.insertSysSuggestionPoll(sysSuggestionPoll));
    }

    /**
     * 修改意见征询主
     */
    @PreAuthorize("@ss.hasPermi('system:poll:edit')")
    @Log(title = "意见征询主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSuggestionPoll sysSuggestionPoll)
    {
        return toAjax(sysSuggestionPollService.updateSysSuggestionPoll(sysSuggestionPoll));
    }

    /**
     * 删除意见征询主
     */
    @PreAuthorize("@ss.hasPermi('system:poll:remove')")
    @Log(title = "意见征询主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{pollIds}")
    public AjaxResult remove(@PathVariable Long[] pollIds)
    {
        return toAjax(sysSuggestionPollService.deleteSysSuggestionPollByPollIds(pollIds));
    }
}
