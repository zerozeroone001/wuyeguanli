package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysBanner;

/**
 * 轮播图管理Service接口
 *
 * @author ruoyi
 */
public interface ISysBannerService
{
    /**
     * 查询轮播图
     *
     * @param bannerId 轮播图主键
     * @return 轮播图
     */
    public SysBanner selectSysBannerByBannerId(Long bannerId);

    /**
     * 查询轮播图列表
     *
     * @param sysBanner 轮播图
     * @return 轮播图集合
     */
    public List<SysBanner> selectSysBannerList(SysBanner sysBanner);

    /**
     * 新增轮播图
     *
     * @param sysBanner 轮播图
     * @return 结果
     */
    public int insertSysBanner(SysBanner sysBanner);

    /**
     * 修改轮播图
     *
     * @param sysBanner 轮播图
     * @return 结果
     */
    public int updateSysBanner(SysBanner sysBanner);

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 需要删除的轮播图主键集合
     * @return 结果
     */
    public int deleteSysBannerByBannerIds(Long[] bannerIds);

    /**
     * 删除轮播图信息
     *
     * @param bannerId 轮播图主键
     * @return 结果
     */
    public int deleteSysBannerByBannerId(Long bannerId);
}
