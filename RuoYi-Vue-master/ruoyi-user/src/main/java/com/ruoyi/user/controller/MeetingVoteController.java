package com.ruoyi.user.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.service.ISysMeetingVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 业主大会投票Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/user/vote")
public class MeetingVoteController extends BaseController
{
    @Autowired
    private ISysMeetingVoteService sysMeetingVoteService;

    /**
     * 查询业主大会投票列表
     */
    @PreAuthorize("@ss.hasPermi('system:vote:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysMeetingVote sysMeetingVote)
    {
        startPage();
        List<SysMeetingVote> list = sysMeetingVoteService.selectSysMeetingVoteList(sysMeetingVote);
        return getDataTable(list);
    }

    /**
     * 导出业主大会投票列表
     */
    @PreAuthorize("@ss.hasPermi('system:vote:export')")
    @Log(title = "业主大会投票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysMeetingVote sysMeetingVote)
    {
        List<SysMeetingVote> list = sysMeetingVoteService.selectSysMeetingVoteList(sysMeetingVote);
        ExcelUtil<SysMeetingVote> util = new ExcelUtil<SysMeetingVote>(SysMeetingVote.class);
        util.exportExcel(response, list, "业主大会投票数据");
    }

    /**
     * 获取业主大会投票详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:vote:query')")
    @GetMapping(value = "/{voteId}")
    public AjaxResult getInfo(@PathVariable("voteId") Long voteId)
    {
        return success(sysMeetingVoteService.selectSysMeetingVoteByVoteId(voteId));
    }

    /**
     * 新增业主大会投票
     */
    @PreAuthorize("@ss.hasPermi('system:vote:add')")
    @Log(title = "业主大会投票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysMeetingVote sysMeetingVote)
    {
        return toAjax(sysMeetingVoteService.insertSysMeetingVote(sysMeetingVote));
    }

    /**
     * 修改业主大会投票
     */
    @PreAuthorize("@ss.hasPermi('system:vote:edit')")
    @Log(title = "业主大会投票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMeetingVote sysMeetingVote)
    {
        return toAjax(sysMeetingVoteService.updateSysMeetingVote(sysMeetingVote));
    }

    /**
     * 删除业主大会投票
     */
    @PreAuthorize("@ss.hasPermi('system:vote:remove')")
    @Log(title = "业主大会投票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{voteIds}")
    public AjaxResult remove(@PathVariable Long[] voteIds)
    {
        return toAjax(sysMeetingVoteService.deleteSysMeetingVoteByVoteIds(voteIds));
    }
}
