package com.ruoyi.user.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SysNotaryApplication;
import com.ruoyi.system.domain.SysNotaryOffice;
import com.ruoyi.system.service.ISysNotaryApplicationService;
import com.ruoyi.system.service.ISysNotaryOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * App端-公证服务接口 (ruoyi-user 模块)
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/app/notary")
public class NotaryApplicationController extends BaseController {

    @Autowired
    private ISysNotaryApplicationService notaryApplicationService;

    @Autowired
    private ISysNotaryOfficeService notaryOfficeService;

    /**
     * 查询我的公证列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysNotaryApplication sysNotaryApplication) {
        // 仅查询当前登录用户的申请
        sysNotaryApplication.setUserId(getUserId());
        startPage();
        List<SysNotaryApplication> list = notaryApplicationService.selectSysNotaryApplicationList(sysNotaryApplication);
        return getDataTable(list);
    }

    /**
     * 获取公证详情
     */
    @GetMapping("/{notaryId}")
    public AjaxResult getNotaryDetail(@PathVariable("notaryId") Long notaryId) {
        SysNotaryApplication notary = notaryApplicationService.selectSysNotaryApplicationByApplicationId(notaryId);
        // 安全校验，确保用户只能查看自己的申请
        if (notary == null || !notary.getUserId().equals(getUserId())) {
            return AjaxResult.error("无权访问");
        }
        return AjaxResult.success(notary);
    }

    /**
     * 新增公证申请
     */
    @PostMapping
    public AjaxResult addNotary(@RequestBody SysNotaryApplication sysNotaryApplication) {
        sysNotaryApplication.setUserId(getUserId());
        sysNotaryApplication.setCreateBy(getUsername());
        sysNotaryApplication.setApplyTime(new Date());
        return toAjax(notaryApplicationService.insertSysNotaryApplication(sysNotaryApplication));
    }

    /**
     * 修改公证申请
     */
    @PutMapping
    public AjaxResult updateNotary(@RequestBody SysNotaryApplication sysNotaryApplication) {
        // 安全校验
        SysNotaryApplication existingApplication = notaryApplicationService.selectSysNotaryApplicationByApplicationId(sysNotaryApplication.getNotaryId());
        if (existingApplication == null || !existingApplication.getUserId().equals(getUserId())) {
            return AjaxResult.error("无权修改");
        }
        sysNotaryApplication.setUpdateBy(getUsername());
        return toAjax(notaryApplicationService.updateSysNotaryApplication(sysNotaryApplication));
    }

    /**
     * 删除公证申请
     */
    @DeleteMapping("/{notaryId}")
    public AjaxResult deleteNotary(@PathVariable("notaryId") Long notaryId) {
         // 安全校验
        SysNotaryApplication existingApplication = notaryApplicationService.selectSysNotaryApplicationByApplicationId(notaryId);
        if (existingApplication == null || !existingApplication.getUserId().equals(getUserId())) {
            return AjaxResult.error("无权删除");
        }
        return toAjax(notaryApplicationService.deleteSysNotaryApplicationByApplicationId(notaryId));
    }

    /**
     * 获取首页统计数据
     */
    @GetMapping("/stats")
    public AjaxResult getNotaryStats() {
        Long userId = getUserId();
        Map<String, Object> stats = notaryApplicationService.getNotaryStatsByUserId(userId);
        return AjaxResult.success(stats);
    }

    /**
     * 获取公证处列表
     */
    @GetMapping("/office/list")
    public AjaxResult getNotaryOfficeList() {
        SysNotaryOffice filter = new SysNotaryOffice();
        filter.setStatus("0"); // 只查询状态正常的公证处
        List<SysNotaryOffice> list = notaryOfficeService.selectSysNotaryOfficeList(filter);
        return AjaxResult.success(list);
    }
}