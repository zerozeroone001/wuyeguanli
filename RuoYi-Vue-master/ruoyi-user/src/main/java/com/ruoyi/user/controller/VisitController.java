package com.ruoyi.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
 * 上门拜访Controller (用户端)
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
    @GetMapping("/buildings")
    public AjaxResult getBuildingList(@RequestParam Long communityId)
    {
        List<BuildingVO> list = visitService.getBuildingList(communityId);
        return success(list);
    }

    /**
     * 获取楼栋的单元列表
     */
    @GetMapping("/units")
    public AjaxResult getUnitList(@RequestParam Long communityId, @RequestParam String buildingName)
    {
        List<String> list = visitService.getUnitList(communityId, buildingName);
        return success(list);
    }

    /**
     * 获取房屋列表及业主信息
     */
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
    @Log(title = "上门拜访", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus")
    public AjaxResult updateOfflineStatus(@RequestBody Map<String, Object> params)
    {
        try {
            // 验证必填参数
            if (params.get("meetingId") == null || params.get("meetingId").toString().isEmpty()) {
                return error("会议ID不能为空");
            }
            if (params.get("userId") == null || params.get("userId").toString().isEmpty()) {
                return error("用户ID不能为空");
            }
            if (params.get("offlineStatus") == null || params.get("offlineStatus").toString().isEmpty()) {
                return error("状态不能为空");
            }
            
            Long meetingId = Long.valueOf(params.get("meetingId").toString());
            Long userId = Long.valueOf(params.get("userId").toString());
            Integer offlineStatus = Integer.valueOf(params.get("offlineStatus").toString());
            
            // 处理可选参数 visitorId 和 visitorName
            Long visitorId = null;
            String visitorName = "";
            
            if (params.get("visitorId") != null && !params.get("visitorId").toString().isEmpty()) {
                visitorId = Long.valueOf(params.get("visitorId").toString());
            }
            
            if (params.get("visitorName") != null && !params.get("visitorName").toString().isEmpty()) {
                visitorName = params.get("visitorName").toString();
            }
            
            int result = visitService.updateOfflineStatus(meetingId, userId, offlineStatus, visitorId, visitorName);
            return toAjax(result);
        } catch (NumberFormatException e) {
            return error("参数格式错误：" + e.getMessage());
        } catch (Exception e) {
            return error("更新失败：" + e.getMessage());
        }
    }
}
