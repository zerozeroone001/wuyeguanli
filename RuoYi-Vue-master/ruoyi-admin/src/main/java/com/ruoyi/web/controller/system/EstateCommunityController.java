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
import com.ruoyi.system.domain.EstateCommunity;
import com.ruoyi.system.service.IEstateCommunityService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 小区信息Controller
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
@RestController
@RequestMapping("/system/community")
public class EstateCommunityController extends BaseController
{
    @Autowired
    private IEstateCommunityService estateCommunityService;

    /**
     * 查询小区信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:community:list')")
    @GetMapping("/list")
    public TableDataInfo list(EstateCommunity estateCommunity)
    {
        startPage();
        List<EstateCommunity> list = estateCommunityService.selectEstateCommunityList(estateCommunity);
        return getDataTable(list);
    }

    /**
     * 导出小区信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:community:export')")
    @Log(title = "小区信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EstateCommunity estateCommunity)
    {
        List<EstateCommunity> list = estateCommunityService.selectEstateCommunityList(estateCommunity);
        ExcelUtil<EstateCommunity> util = new ExcelUtil<EstateCommunity>(EstateCommunity.class);
        util.exportExcel(response, list, "小区信息数据");
    }

    /**
     * 获取小区信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:community:query')")
    @GetMapping(value = "/{communityId}")
    public AjaxResult getInfo(@PathVariable("communityId") Long communityId)
    {
        return success(estateCommunityService.selectEstateCommunityByCommunityId(communityId));
    }

    /**
     * 新增小区信息
     */
    @PreAuthorize("@ss.hasPermi('system:community:add')")
    @Log(title = "小区信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EstateCommunity estateCommunity)
    {
        return toAjax(estateCommunityService.insertEstateCommunity(estateCommunity));
    }

    /**
     * 修改小区信息
     */
    @PreAuthorize("@ss.hasPermi('system:community:edit')")
    @Log(title = "小区信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EstateCommunity estateCommunity)
    {
        return toAjax(estateCommunityService.updateEstateCommunity(estateCommunity));
    }

    /**
     * 删除小区信息
     */
    @PreAuthorize("@ss.hasPermi('system:community:remove')")
    @Log(title = "小区信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{communityIds}")
    public AjaxResult remove(@PathVariable Long[] communityIds)
    {
        return toAjax(estateCommunityService.deleteEstateCommunityByCommunityIds(communityIds));
    }
}
