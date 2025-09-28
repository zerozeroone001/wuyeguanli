package com.ruoyi.user.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ComplaintNoUtils;
//import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SysPropertyComplaint;
import com.ruoyi.system.service.ISysPropertyComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        
        // 自动生成投诉编号
        if (StringUtils.isEmpty(complaint.getComplaintNo())) {
            complaint.setComplaintNo(ComplaintNoUtils.generateComplaintNo());
        }
        
        // 设置默认状态为待处理
        if (StringUtils.isEmpty(complaint.getStatus())) {
            complaint.setStatus("0"); // 待处理
        }
        
        complaintService.insertSysPropertyComplaint(complaint);
        return AjaxResult.success(complaint);
    }

    /**
     * 获取我的投诉列表
     */
    @GetMapping("/my")
    public TableDataInfo listMyComplaints(SysPropertyComplaint complaint) {
        startPage();
        // 这里应该根据当前用户ID过滤，暂时查询所有
        // complaint.setUserId(ShiroUtils.getUserId());
        List<SysPropertyComplaint> list = complaintService.selectSysPropertyComplaintList(complaint);
        return getDataTable(list);
    }

    /**
     * 获取投诉详情
     */
    @GetMapping("/{complaintId}")
    public AjaxResult getComplaintDetail(@PathVariable Long complaintId) {
        SysPropertyComplaint complaint = complaintService.selectSysPropertyComplaintByComplaintId(complaintId);
        return AjaxResult.success(complaint);
    }

    /**
     * 获取投诉处理记录
     */
    @GetMapping("/handle/{complaintId}")
    public AjaxResult getComplaintHandles(@PathVariable Long complaintId) {
        // 这里应该调用获取处理记录的方法
        // List<SysPropertyComplaintHandle> handles = complaintService.selectComplaintHandles(complaintId);
        return AjaxResult.success("处理记录功能待实现");
    }

    /**
     * 评价投诉处理
     */
    @PostMapping("/evaluate")
    public AjaxResult evaluateComplaint(@RequestBody SysPropertyComplaint complaint) {
        // 这里应该实现评价功能
        // complaintService.updateComplaintEvaluation(complaint);
        return AjaxResult.success("评价功能待实现");
    }
}
