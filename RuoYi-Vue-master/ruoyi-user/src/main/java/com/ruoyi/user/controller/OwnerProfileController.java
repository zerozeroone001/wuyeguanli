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
import com.ruoyi.system.domain.SysEstateUserApply;
import com.ruoyi.system.service.ISysEstateUserApplyService;
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

    @Autowired
    private ISysEstateUserApplyService sysEstateUserApplyService;

    /**
     * (小程序端) 提交认证申请
     */
    @Log(title = "业主认证申请", businessType = BusinessType.INSERT)
    @PostMapping("/submitAuth")
    public AjaxResult submitAuth(@RequestBody SysEstateUserApply sysEstateUserApply)
    {
        Long userId = SecurityUtils.getUserId();
        
        if (sysEstateUserApply.getPropertyId() == null) {
            return error("参数不完整：缺少房产ID");
        }

        sysEstateUserApply.setUserId(userId);

        // 检查是否重复申请
        if (sysEstateUserApplyService.checkPendingOrApprovedApply(userId, sysEstateUserApply.getPropertyId())) {
            return AjaxResult.error("您已提交过该房产的认证申请或已认证通过，请勿重复提交");
        }

        sysEstateUserApply.setStatus("0"); // 0-待审核
        return toAjax(sysEstateUserApplyService.insertSysEstateUserApply(sysEstateUserApply));
    }

    /**
     * (小程序端) 获取我的认证信息
     */
    @GetMapping("/myProfile")
    public AjaxResult getMyProfile()
    {
        Long userId = SecurityUtils.getUserId();
        SysOwnerProfile profile = sysOwnerProfileService.selectSysOwnerProfileByUserId(userId);

        return AjaxResult.success(profile);
    }

    /**
     * 查询业主信息扩展列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysOwnerProfile sysOwnerProfile)
    {
        startPage();
        List<SysOwnerProfile> list = sysOwnerProfileService.selectSysOwnerProfileList(sysOwnerProfile);
        return getDataTable(list);
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
     * 修改业主信息扩展
     */
    @PreAuthorize("@ss.hasPermi('system:profile:edit')")
    @Log(title = "业主信息扩展", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysOwnerProfile sysOwnerProfile)
    {
        return toAjax(sysOwnerProfileService.updateSysOwnerProfile(sysOwnerProfile));
    }

}
