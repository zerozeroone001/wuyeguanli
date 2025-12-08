package com.ruoyi.user.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysVisitor;
import com.ruoyi.system.service.ISysVisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C端访客登记接口
 */
@RestController
@RequestMapping("/visitor")
public class UserVisitorController extends BaseController {

    @Autowired
    private ISysVisitorService visitorService;

    /**
     * 新增访客登记
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysVisitor visitor) {
        visitor.setStatus("0"); // 待审核
        return toAjax(visitorService.insertSysVisitor(visitor));
    }
}
