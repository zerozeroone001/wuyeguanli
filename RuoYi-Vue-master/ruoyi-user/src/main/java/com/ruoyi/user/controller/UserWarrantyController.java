package com.ruoyi.user.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysWarranty;
import com.ruoyi.system.service.ISysWarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C端设备保修接口
 */
@RestController
@RequestMapping("/warranty")
public class UserWarrantyController extends BaseController {

    @Autowired
    private ISysWarrantyService warrantyService;

    /**
     * 新增设备保修
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysWarranty warranty) {
        warranty.setStatus("0"); // 待处理
        return toAjax(warrantyService.insertSysWarranty(warranty));
    }
}
