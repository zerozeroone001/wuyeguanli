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
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.domain.dto.OwnerProfileImportDto;
import com.ruoyi.system.service.ISysOwnerProfileService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 业主信息扩展Controller
 * 
 * @author ruoyi
 * @date 2025-09-15
 */
@RestController
@RequestMapping("/system/owner")
public class SysOwnerProfileController extends BaseController
{
    @Autowired
    private ISysOwnerProfileService sysOwnerProfileService;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询业主信息扩展列表
     */
    @PreAuthorize("@ss.hasPermi('system:owner:list')")
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
    @PreAuthorize("@ss.hasPermi('system:owner:export')")
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
    @PreAuthorize("@ss.hasPermi('system:owner:query')")
    @GetMapping(value = "/{ownerId}")
    public AjaxResult getInfo(@PathVariable("ownerId") Long ownerId)
    {
        return success(sysOwnerProfileService.selectSysOwnerProfileByOwnerId(ownerId));
    }

    /**
     * 新增业主信息扩展
     */
    @PreAuthorize("@ss.hasPermi('system:owner:add')")
    @Log(title = "业主信息扩展", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysOwnerProfile sysOwnerProfile)
    {
        return toAjax(sysOwnerProfileService.insertSysOwnerProfile(sysOwnerProfile));
    }

    /**
     * 修改业主信息扩展
     */
    @PreAuthorize("@ss.hasPermi('system:owner:edit')")
    @Log(title = "业主信息扩展", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysOwnerProfile sysOwnerProfile)
    {
        return toAjax(sysOwnerProfileService.updateSysOwnerProfile(sysOwnerProfile));
    }

    /**
     * 删除业主信息扩展
     */
    @PreAuthorize("@ss.hasPermi('system:owner:remove')")
    @Log(title = "业主信息扩展", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ownerIds}")
    public AjaxResult remove(@PathVariable Long[] ownerIds)
    {
        return toAjax(sysOwnerProfileService.deleteSysOwnerProfileByOwnerIds(ownerIds));
    }

    @Log(title = "业主信息", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:owner:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<OwnerProfileImportDto> util = new ExcelUtil<OwnerProfileImportDto>(OwnerProfileImportDto.class);
        List<OwnerProfileImportDto> ownerList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = sysOwnerProfileService.importOwner(ownerList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<OwnerProfileImportDto> util = new ExcelUtil<OwnerProfileImportDto>(OwnerProfileImportDto.class);
        util.importTemplateExcel(response, "业主数据");
    }

    /**
     * 审核业主认证
     */
    @PreAuthorize("@ss.hasPermi('system:owner:audit')")
    @Log(title = "业主认证审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody SysOwnerProfile sysOwnerProfile)
    {
        int result = sysOwnerProfileService.auditOwnerProfile(sysOwnerProfile);
        if (result > 0) {
            // 审核成功后，清除用户端缓存（通过清除Redis中的用户信息缓存）
            sysOwnerProfileService.clearUserCache(sysOwnerProfile.getUserId());
        }
        return toAjax(result);
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:owner:edit')")
    @Log(title = "业主状态修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 身份修改
     */
    @PreAuthorize("@ss.hasPermi('system:owner:edit')")
    @Log(title = "业主身份修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeIdentity")
    public AjaxResult changeIdentity(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 查询可绑定的用户列表（user_type为10且未绑定业主信息的用户）
     */
    @PreAuthorize("@ss.hasPermi('system:owner:add')")
    @GetMapping("/unboundUsers")
    public TableDataInfo getUnboundUsers(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnboundUsers(user);
        return getDataTable(list);
    }

    /**
     * 房产合并与拆分
     */
    @PreAuthorize("@ss.hasPermi('system:owner:edit')")
    @Log(title = "房产合并与拆分", businessType = BusinessType.UPDATE)
    @PostMapping("/transfer")
    public AjaxResult transferProperties(@RequestBody java.util.Map<String, Object> params)
    {
        Long sourceUserId = Long.valueOf(params.get("sourceUserId").toString());
        Long targetUserId = Long.valueOf(params.get("targetUserId").toString());
        List<Long> targetPropertyIds = new java.util.ArrayList<>();
        
        if (params.get("targetPropertyIds") instanceof List) {
            List<?> list = (List<?>) params.get("targetPropertyIds");
            for (Object obj : list) {
                targetPropertyIds.add(Long.valueOf(obj.toString()));
            }
        }
    
        return toAjax(sysOwnerProfileService.transferOwnerProperties(sourceUserId, targetUserId, targetPropertyIds));
    }
}
