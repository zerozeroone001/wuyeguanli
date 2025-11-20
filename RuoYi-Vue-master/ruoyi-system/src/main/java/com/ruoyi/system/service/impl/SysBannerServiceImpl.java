package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysBannerMapper;
import com.ruoyi.system.domain.SysBanner;
import com.ruoyi.system.service.ISysBannerService;

/**
 * 轮播图管理Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysBannerServiceImpl implements ISysBannerService
{
    @Autowired
    private SysBannerMapper sysBannerMapper;

    /**
     * 查询轮播图
     *
     * @param bannerId 轮播图主键
     * @return 轮播图
     */
    @Override
    public SysBanner selectSysBannerByBannerId(Long bannerId)
    {
        return sysBannerMapper.selectSysBannerByBannerId(bannerId);
    }

    /**
     * 查询轮播图列表
     *
     * @param sysBanner 轮播图
     * @return 轮播图
     */
    @Override
    public List<SysBanner> selectSysBannerList(SysBanner sysBanner)
    {
        return sysBannerMapper.selectSysBannerList(sysBanner);
    }

    /**
     * 新增轮播图
     *
     * @param sysBanner 轮播图
     * @return 结果
     */
    @Override
    public int insertSysBanner(SysBanner sysBanner)
    {
        sysBanner.setCreateTime(DateUtils.getNowDate());
        return sysBannerMapper.insertSysBanner(sysBanner);
    }

    /**
     * 修改轮播图
     *
     * @param sysBanner 轮播图
     * @return 结果
     */
    @Override
    public int updateSysBanner(SysBanner sysBanner)
    {
        sysBanner.setUpdateTime(DateUtils.getNowDate());
        return sysBannerMapper.updateSysBanner(sysBanner);
    }

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 需要删除的轮播图主键
     * @return 结果
     */
    @Override
    public int deleteSysBannerByBannerIds(Long[] bannerIds)
    {
        return sysBannerMapper.deleteSysBannerByBannerIds(bannerIds);
    }

    /**
     * 删除轮播图信息
     *
     * @param bannerId 轮播图主键
     * @return 结果
     */
    @Override
    public int deleteSysBannerByBannerId(Long bannerId)
    {
        return sysBannerMapper.deleteSysBannerByBannerId(bannerId);
    }
}
