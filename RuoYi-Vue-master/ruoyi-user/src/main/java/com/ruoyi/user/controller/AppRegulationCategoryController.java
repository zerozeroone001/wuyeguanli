package com.ruoyi.user.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysRegulationCategory;
import com.ruoyi.system.service.ISysRegulationCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * C端-制度分类接口
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/regulationCategory")
public class AppRegulationCategoryController extends BaseController
{
    @Autowired
    private ISysRegulationCategoryService regulationCategoryService;

    /**
     * 查询制度分类列表（公开接口）
     */
    @GetMapping("/list")
    public AjaxResult list(SysRegulationCategory sysRegulationCategory)
    {
        List<SysRegulationCategory> list = regulationCategoryService.selectSysRegulationCategoryList(sysRegulationCategory);
        return AjaxResult.success(list);
    }
}
