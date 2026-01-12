package com.ruoyi.web.controller.system;

import java.util.List;
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
import com.ruoyi.system.domain.SysContractAssessmentScore;
import com.ruoyi.system.service.ISysContractAssessmentScoreService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 业主考核评分Controller
 *
 * @author ruoyi
 * @date 2026-01-05
 */
@RestController
@RequestMapping("/system/contract/assessmentScore")
public class SysContractAssessmentScoreController extends BaseController
{
    @Autowired
    private ISysContractAssessmentScoreService sysContractAssessmentScoreService;

    @PreAuthorize("@ss.hasPermi('system:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysContractAssessmentScore sysContractAssessmentScore)
    {
        sysContractAssessmentScore.setCommunityId(resolveCommunityId(sysContractAssessmentScore.getCommunityId()));
        startPage();
        List<SysContractAssessmentScore> list = sysContractAssessmentScoreService.selectSysContractAssessmentScoreList(sysContractAssessmentScore);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/{scoreId}")
    public AjaxResult getInfo(@PathVariable("scoreId") Long scoreId)
    {
        return success(sysContractAssessmentScoreService.selectSysContractAssessmentScoreByScoreId(scoreId));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:query')")
    @GetMapping(value = "/assessment/{assessmentId}")
    public AjaxResult getByAssessmentId(@PathVariable("assessmentId") Long assessmentId)
    {
        return success(sysContractAssessmentScoreService.selectByAssessmentId(assessmentId));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:add')")
    @Log(title = "业主考核评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysContractAssessmentScore sysContractAssessmentScore)
    {
        sysContractAssessmentScore.setCommunityId(requireResolvedCommunityId(sysContractAssessmentScore.getCommunityId(), "请选择小区后再操作"));
        return toAjax(sysContractAssessmentScoreService.insertSysContractAssessmentScore(sysContractAssessmentScore));
    }

    /**
     * 业主提交评分
     */
    @PreAuthorize("@ss.hasPermi('system:contract:add')")
    @Log(title = "业主考核评分", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public AjaxResult submitScore(@RequestBody SysContractAssessmentScore sysContractAssessmentScore)
    {
        sysContractAssessmentScore.setCommunityId(requireResolvedCommunityId(sysContractAssessmentScore.getCommunityId(), "请选择小区后再操作"));
        return toAjax(sysContractAssessmentScoreService.submitScore(sysContractAssessmentScore));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:edit')")
    @Log(title = "业主考核评分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysContractAssessmentScore sysContractAssessmentScore)
    {
        return toAjax(sysContractAssessmentScoreService.updateSysContractAssessmentScore(sysContractAssessmentScore));
    }

    @PreAuthorize("@ss.hasPermi('system:contract:remove')")
    @Log(title = "业主考核评分", businessType = BusinessType.DELETE)
    @DeleteMapping("/{scoreIds}")
    public AjaxResult remove(@PathVariable Long[] scoreIds)
    {
        return toAjax(sysContractAssessmentScoreService.deleteSysContractAssessmentScoreByScoreIds(scoreIds));
    }
}
