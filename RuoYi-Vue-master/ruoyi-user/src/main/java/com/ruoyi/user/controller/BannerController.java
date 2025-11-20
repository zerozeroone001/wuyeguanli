package com.ruoyi.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysBanner;
import com.ruoyi.system.service.ISysBannerService;

/**
 * 轮播图Controller（小程序端）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/banner")
public class BannerController extends BaseController
{
    @Autowired
    private ISysBannerService sysBannerService;

    /**
     * 查询轮播图列表（小程序端）
     * 只返回状态为正常的轮播图，按排序字段排序
     */
    @GetMapping("/list")
    public AjaxResult list()
    {
        SysBanner query = new SysBanner();
        query.setStatus("0"); // 只查询正常状态的轮播图
        List<SysBanner> list = sysBannerService.selectSysBannerList(query);
        return success(list);
    }
}
