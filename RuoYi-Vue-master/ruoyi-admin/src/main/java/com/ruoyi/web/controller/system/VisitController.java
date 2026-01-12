package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.vo.BuildingVO;
import com.ruoyi.system.domain.vo.VisitRoomVO;
import com.ruoyi.system.service.IVisitService;

/**
 * 上门拜访Controller
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
@RestController
@RequestMapping("/system/visit")
public class VisitController extends BaseController
{
    @Autowired
    private IVisitService visitService;

    /**
     * 检查当前用户是否为管理员
     */
    @PreAuthorize("@ss.hasPermi('system:visit:check')")
    @GetMapping("/checkAdmin")
    public AjaxResult checkAdmin()
    {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> result = visitService.checkIsAdmin(userId);
        return success(result);
    }

    /**
     * 获取小区楼栋列表
     */
    @PreAuthorize("@ss.hasPermi('system:visit:list')")
    @GetMapping("/buildings")
    public AjaxResult getBuildingList(@RequestParam Long communityId)
    {
        List<BuildingVO> list = visitService.getBuildingList(communityId);
        return success(list);
    }

    /**
     * 获取楼栋的单元列表
     */
    @PreAuthorize("@ss.hasPermi('system:visit:list')")
    @GetMapping("/units")
    public AjaxResult getUnitList(@RequestParam Long communityId, @RequestParam String buildingName)
    {
        List<String> list = visitService.getUnitList(communityId, buildingName);
        return success(list);
    }

    /**
     * 获取房屋列表及业主信息
     */
    @PreAuthorize("@ss.hasPermi('system:visit:list')")
    @GetMapping("/rooms")
    public AjaxResult getRoomList(
        @RequestParam Long communityId,
        @RequestParam String buildingName,
        @RequestParam(required = false) String unitName,
        @RequestParam Long meetingId
    )
    {
        List<VisitRoomVO> list = visitService.getRoomList(communityId, buildingName, unitName, meetingId);
        return success(list);
    }

    /**
     * 更新线下通知状态
     */
    @PreAuthorize("@ss.hasPermi('system:visit:edit')")
    @Log(title = "上门拜访", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus")
    public AjaxResult updateOfflineStatus(@RequestBody Map<String, Object> params)
    {
        Long meetingId = Long.valueOf(params.get("meetingId").toString());
        Long userId = Long.valueOf(params.get("userId").toString());
        Integer offlineStatus = Integer.valueOf(params.get("offlineStatus").toString());
        Long visitorId = Long.valueOf(params.get("visitorId").toString());
        String visitorName = params.get("visitorName").toString();
        
        int result = visitService.updateOfflineStatus(meetingId, userId, offlineStatus, visitorId, visitorName);
        return toAjax(result);
    }
}
