package com.ruoyi.user.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SysPropertyComplaint;
import com.ruoyi.system.service.ISysPropertyComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C端投诉接口
 */
@RestController
@RequestMapping("/userapi/complaint")
public class UserComplaintController extends BaseController {

    @Autowired
    private ISysPropertyComplaintService complaintService;

    /**
     * 新增投诉
     */
    @PostMapping
    public AjaxResult add(@RequestBody SysPropertyComplaint complaint) {
//        complaint.setUserId(ShiroUtils.getUserId());
        complaint.setStatus("0"); // 待处理
        complaintService.insertSysPropertyComplaint(complaint);
        return AjaxResult.success(complaint);
    }
}
