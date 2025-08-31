package com.ruoyi.web.controller.system;

import com.alibaba.druid.util.StringUtils;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.service.ISysOwnerProfileService;
import com.ruoyi.system.service.ISysUserService;
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
@RequestMapping("/system/profile")
public class SysOwnerProfileController extends BaseController
{
    @Autowired
    private ISysOwnerProfileService sysOwnerProfileService;

    @Autowired
    private ISysUserService userService;

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
     * 审核认证申请
     */
    @PreAuthorize("@ss.hasPermi('system:profile:audit')")
    @Log(title = "业主认证审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody SysOwnerProfile profile)
    {
        // 简单验证，确保状态和备注不为空
        if (StringUtils.isEmpty(profile.getAuthStatus()) || profile.getUserId() == null)
        {
            return error("审核失败，缺少必要参数");
        }
        // 审核通过时，将用户信息同步到主表
        if ("2".equals(profile.getAuthStatus())) {
            SysOwnerProfile ownerProfile = sysOwnerProfileService.selectSysOwnerProfileByUserId(profile.getUserId());
            SysUser user = new SysUser();
            user.setUserId(ownerProfile.getUserId());
            user.setNickName(ownerProfile.getRealName()); // 可将昵称同步为真实姓名
            // user.setPhonenumber(ownerProfile.getPhonenumber()); // 如果需要同步手机号
            userService.updateUserProfile(user);
        }
        return toAjax(sysOwnerProfileService.updateSysOwnerProfile(profile));
    }


    /**
     * 导出业主信息扩展列表
     */
//    @PreAuthorize("@ss.hasPermi('system:profile:export')")
//    @Log(title = "业主信息扩展", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SysOwnerProfile sysOwnerProfile)
//    {
//        List<SysOwnerProfile> list = sysOwnerProfileService.selectSysOwnerProfileList(sysOwnerProfile);
//        ExcelUtil<SysOwnerProfile> util = new ExcelUtil<SysOwnerProfile>(SysOwnerProfile.class);
//        util.exportExcel(response, list, "业主信息扩展数据");
//    }

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
