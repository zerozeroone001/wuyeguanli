package com.ruoyi.web.controller.userapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysEstateUserApplyService;
import com.ruoyi.system.domain.SysEstateUserApply;

/**
 * 业主房产认证相关API，供用户端调用
 */
@RestController
@RequestMapping("/userapi/ownerProfile")
public class SysOwnerProfileApiController extends BaseController {

    @Autowired
    private ISysEstateUserApplyService sysEstateUserApplyService;

    /**
     * 检查是否存在待审核或已通过的房产申请记录
     * @param propertyId 房产ID
     * @param userId 用户ID
     * @return 存在则返回true，否则false
     */
    @GetMapping("/checkApplyStatus")
    public AjaxResult checkApplyStatus(
            @RequestParam("propertyId") Long propertyId,
            @RequestParam("userId") Long userId) {
        
        boolean exists = sysEstateUserApplyService.checkPendingOrApprovedApply(userId, propertyId);
        return success(exists);
    }

    /**
     * 提交房产认证申请
     */
    @PostMapping("/submitAuth")
    public AjaxResult submitAuth(@RequestBody SysEstateUserApply sysEstateUserApply)
    {
        if (sysEstateUserApply.getUserId() == null || sysEstateUserApply.getPropertyId() == null) {
            return error("参数不完整");
        }

        // 1. Check if exists (Double check)
        if (sysEstateUserApplyService.checkPendingOrApprovedApply(sysEstateUserApply.getUserId(), sysEstateUserApply.getPropertyId())) {
            return error("您已提交过该房产的认证申请或已认证通过，请勿重复提交");
        }
        
        // 2. Set default status to 0 (Pending)
        sysEstateUserApply.setStatus("0");
        
        return toAjax(sysEstateUserApplyService.insertSysEstateUserApply(sysEstateUserApply));
    }
}
