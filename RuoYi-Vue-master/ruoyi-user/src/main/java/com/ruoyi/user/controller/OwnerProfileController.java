package com.ruoyi.user.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.service.ISysOwnerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 业主信息扩展Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/ownerProfile")
public class OwnerProfileController extends BaseController
{
    @Autowired
    private ISysOwnerProfileService sysOwnerProfileService;



    /**
     * (小程序端) 提交认证申请
     */
    @Log(title = "业主认证申请", businessType = BusinessType.INSERT)
    @PostMapping("/submitAuth")
    public AjaxResult submitAuth(@RequestBody SysOwnerProfile sysOwnerProfile)
    {
        Long userId = SecurityUtils.getUserId();
        SysOwnerProfile existingProfile = sysOwnerProfileService.selectSysOwnerProfileByUserId(userId);
        if (existingProfile != null && ("1".equals(existingProfile.getAuthStatus()) || "2".equals(existingProfile.getAuthStatus()))) {
            return AjaxResult.error("您已提交认证申请或已认证，请勿重复提交");
        }

        sysOwnerProfile.setUserId(userId);
        sysOwnerProfile.setAuthStatus(1); // 1-待审核
        if (existingProfile != null) {
            // 如果之前有记录（如认证失败），则更新
            // 需要把之前的ownerId设置进去，否则更新会失败
            sysOwnerProfile.setOwnerId(existingProfile.getOwnerId());
            return toAjax(sysOwnerProfileService.updateSysOwnerProfile(sysOwnerProfile));
        } else {
            // 否则，插入新纪录
            return toAjax(sysOwnerProfileService.insertSysOwnerProfile(sysOwnerProfile));
        }
    }

    /**
     * (小程序端) 获取我的认证信息
     */
    @GetMapping("/myProfile")
    public AjaxResult getMyProfile()
    {
        Long userId = SecurityUtils.getUserId();
        SysOwnerProfile profile = sysOwnerProfileService.selectSysOwnerProfileByUserId(userId);
        if (profile == null) {
            // 为了前端处理方便，如果不存在记录，返回一个包含默认状态的对象
            SysOwnerProfile defaultProfile = new SysOwnerProfile();
            defaultProfile.setAuthStatus(0); // 0-未认证
            return AjaxResult.success(defaultProfile);
        }
        return AjaxResult.success(profile);
    }

    /**
     * 查询业主信息扩展列表
     */
    @PreAuthorize("@ss.hasPermi('system:profile:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOwnerProfile sysOwnerProfile)
    {
        startPage();
        List<SysOwnerProfile> list = sysOwnerProfileService.selectSysOwnerProfileList(sysOwnerProfile);
        return getDataTable(list);
    }

    /**
     * 导出业主信息扩展列表
     */
    @PreAuthorize("@ss.hasPermi('system:profile:export')")
    @Log(title = "业主信息扩展", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOwnerProfile sysOwnerProfile)
    {
        List<SysOwnerProfile> list = sysOwnerProfileService.selectSysOwnerProfileList(sysOwnerProfile);
        ExcelUtil<SysOwnerProfile> util = new ExcelUtil<SysOwnerProfile>(SysOwnerProfile.class);
        util.exportExcel(response, list, "业主信息扩展数据");
    }

    /**
     * 获取业主信息扩展详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:profile:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(sysOwnerProfileService.selectSysOwnerProfileByUserId(userId));
    }

    /**
     * 新增业主信息扩展
     */
    @PreAuthorize("@ss.hasPermi('system:profile:add')")
    @Log(title = "业主信息扩展", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysOwnerProfile sysOwnerProfile)
    {
        return toAjax(sysOwnerProfileService.insertSysOwnerProfile(sysOwnerProfile));
    }

    /**
     * 修改业主信息扩展
     */
    @PreAuthorize("@ss.hasPermi('system:profile:edit')")
    @Log(title = "业主信息扩展", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysOwnerProfile sysOwnerProfile)
    {
        return toAjax(sysOwnerProfileService.updateSysOwnerProfile(sysOwnerProfile));
    }

    /**
     * 删除业主信息扩展
     */
    @PreAuthorize("@ss.hasPermi('system:profile:remove')")
    @Log(title = "业主信息扩展", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(sysOwnerProfileService.deleteSysOwnerProfileByUserIds(userIds));
    }
}
