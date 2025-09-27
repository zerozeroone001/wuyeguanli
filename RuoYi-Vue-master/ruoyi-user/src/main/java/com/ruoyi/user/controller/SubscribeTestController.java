package com.ruoyi.user.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 订阅消息测试控制器
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/test")
public class SubscribeTestController extends BaseController {
    
    @Autowired
    private WechatService wechatService;
    
    /**
     * 测试发送会议通知
     */
    @PostMapping("/meeting")
    public AjaxResult testMeetingNotification(@RequestBody Map<String, String> params) {
        try {
            String openId = "wxid_b14hcp3gc79712";
            String meetingTitle = params.getOrDefault("meetingTitle", "测试会议");
            String meetingTime = params.getOrDefault("meetingTime", "2024年01月15日 19:00");
            String meetingLocation = params.getOrDefault("meetingLocation", "小区会议室");
            
            if (openId == null || openId.trim().isEmpty()) {
                return AjaxResult.error("openId不能为空");
            }
            
            boolean result = wechatService.sendMeetingNotification(openId, meetingTitle, meetingTime, meetingLocation);
            
            if (result) {
                return AjaxResult.success("会议通知发送成功");
            } else {
                return AjaxResult.error("会议通知发送失败");
            }
        } catch (Exception e) {
            logger.error("测试发送会议通知失败", e);
            return AjaxResult.error("发送失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试发送投诉处理通知
     */
    @PostMapping("/complaint")
    public AjaxResult testComplaintNotification(@RequestBody Map<String, String> params) {
        try {
            String openId = params.get("openId");
            String complaintTitle = params.getOrDefault("complaintTitle", "测试投诉");
            String status = params.getOrDefault("status", "处理中");
            String remark = params.getOrDefault("remark", "已安排工作人员处理");
            
            if (openId == null || openId.trim().isEmpty()) {
                return AjaxResult.error("openId不能为空");
            }
            
            boolean result = wechatService.sendComplaintNotification(openId, complaintTitle, status, remark);
            
            if (result) {
                return AjaxResult.success("投诉通知发送成功");
            } else {
                return AjaxResult.error("投诉通知发送失败");
            }
        } catch (Exception e) {
            logger.error("测试发送投诉通知失败", e);
            return AjaxResult.error("发送失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试发送缴费提醒
     */
    @PostMapping("/payment")
    public AjaxResult testPaymentReminder(@RequestBody Map<String, String> params) {
        try {
            String openId = params.get("openId");
            String feeType = params.getOrDefault("feeType", "物业费");
            String amount = params.getOrDefault("amount", "1200元");
            String dueDate = params.getOrDefault("dueDate", "2024年01月31日");
            
            if (openId == null || openId.trim().isEmpty()) {
                return AjaxResult.error("openId不能为空");
            }
            
            boolean result = wechatService.sendPaymentReminder(openId, feeType, amount, dueDate);
            
            if (result) {
                return AjaxResult.success("缴费提醒发送成功");
            } else {
                return AjaxResult.error("缴费提醒发送失败");
            }
        } catch (Exception e) {
            logger.error("测试发送缴费提醒失败", e);
            return AjaxResult.error("发送失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试发送维修进度通知
     */
    @PostMapping("/repair")
    public AjaxResult testRepairNotification(@RequestBody Map<String, String> params) {
        try {
            String openId = params.get("openId");
            String repairTitle = params.getOrDefault("repairTitle", "电梯维修");
            String status = params.getOrDefault("status", "已完成");
            String remark = params.getOrDefault("remark", "电梯已恢复正常运行");
            
            if (openId == null || openId.trim().isEmpty()) {
                return AjaxResult.error("openId不能为空");
            }
            
            boolean result = wechatService.sendRepairNotification(openId, repairTitle, status, remark);
            
            if (result) {
                return AjaxResult.success("维修通知发送成功");
            } else {
                return AjaxResult.error("维修通知发送失败");
            }
        } catch (Exception e) {
            logger.error("测试发送维修通知失败", e);
            return AjaxResult.error("发送失败：" + e.getMessage());
        }
    }
}