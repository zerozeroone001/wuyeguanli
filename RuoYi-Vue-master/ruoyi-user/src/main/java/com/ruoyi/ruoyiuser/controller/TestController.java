package com.ruoyi.ruoyiuser.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * 用于验证Service注入是否正常
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ISysUserService userService;

    /**
     * 测试Service注入
     */
    @GetMapping("/service")
    public AjaxResult testService() {
        try {
            // 尝试调用service方法
            if (userService != null) {
                return AjaxResult.success("Service注入成功！UserService: " + userService.getClass().getName());
            } else {
                return AjaxResult.error("Service注入失败：userService为null");
            }
        } catch (Exception e) {
            return AjaxResult.error("Service调用异常: " + e.getMessage());
        }
    }

    /**
     * 简单的健康检查
     */
    @GetMapping("/health")
    public AjaxResult health() {
        return AjaxResult.success("用户端应用运行正常");
    }
}