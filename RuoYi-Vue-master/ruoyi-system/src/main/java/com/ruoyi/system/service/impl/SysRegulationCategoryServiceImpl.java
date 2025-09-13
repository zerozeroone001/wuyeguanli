package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysRegulationCategoryMapper;
import com.ruoyi.system.domain.SysRegulationCategory;
import com.ruoyi.system.service.ISysRegulationCategoryService;

/**
 * 制度分类Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-13
 */
@Service
public class SysRegulationCategoryServiceImpl implements ISysRegulationCategoryService 
{
    @Autowired
    private SysRegulationCategoryMapper sysRegulationCategoryMapper;

    /**
     * 查询制度分类
     * 
     * @param categoryId 制度分类主键
     * @return 制度分类
     */
    @Override
    public SysRegulationCategory selectSysRegulationCategoryByCategoryId(Long categoryId)
    {
        return sysRegulationCategoryMapper.selectSysRegulationCategoryByCategoryId(categoryId);
    }

    /**
     * 查询制度分类列表
     * 
     * @param sysRegulationCategory 制度分类
     * @return 制度分类
     */
    @Override
    public List<SysRegulationCategory> selectSysRegulationCategoryList(SysRegulationCategory sysRegulationCategory)
    {
        return sysRegulationCategoryMapper.selectSysRegulationCategoryList(sysRegulationCategory);
    }

    /**
     * 新增制度分类
     * 
     * @param sysRegulationCategory 制度分类
     * @return 结果
     */
    @Override
    public int insertSysRegulationCategory(SysRegulationCategory sysRegulationCategory)
    {
        sysRegulationCategory.setCreateTime(DateUtils.getNowDate());
        return sysRegulationCategoryMapper.insertSysRegulationCategory(sysRegulationCategory);
    }

    /**
     * 修改制度分类
     * 
     * @param sysRegulationCategory 制度分类
     * @return 结果
     */
    @Override
    public int updateSysRegulationCategory(SysRegulationCategory sysRegulationCategory)
    {
        sysRegulationCategory.setUpdateTime(DateUtils.getNowDate());
        return sysRegulationCategoryMapper.updateSysRegulationCategory(sysRegulationCategory);
    }

    /**
     * 批量删除制度分类
     * 
     * @param categoryIds 需要删除的制度分类主键
     * @return 结果
     */
    @Override
    public int deleteSysRegulationCategoryByCategoryIds(Long[] categoryIds)
    {
        return sysRegulationCategoryMapper.deleteSysRegulationCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除制度分类信息
     * 
     * @param categoryId 制度分类主键
     * @return 结果
     */
    @Override
    public int deleteSysRegulationCategoryByCategoryId(Long categoryId)
    {
        return sysRegulationCategoryMapper.deleteSysRegulationCategoryByCategoryId(categoryId);
    }
}
