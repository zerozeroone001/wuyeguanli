package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysRegulationCategory;

/**
 * 制度分类Service接口
 * 
 * @author ruoyi
 * @date 2025-09-13
 */
public interface ISysRegulationCategoryService 
{
    /**
     * 查询制度分类
     * 
     * @param categoryId 制度分类主键
     * @return 制度分类
     */
    public SysRegulationCategory selectSysRegulationCategoryByCategoryId(Long categoryId);

    /**
     * 查询制度分类列表
     * 
     * @param sysRegulationCategory 制度分类
     * @return 制度分类集合
     */
    public List<SysRegulationCategory> selectSysRegulationCategoryList(SysRegulationCategory sysRegulationCategory);

    /**
     * 新增制度分类
     * 
     * @param sysRegulationCategory 制度分类
     * @return 结果
     */
    public int insertSysRegulationCategory(SysRegulationCategory sysRegulationCategory);

    /**
     * 修改制度分类
     * 
     * @param sysRegulationCategory 制度分类
     * @return 结果
     */
    public int updateSysRegulationCategory(SysRegulationCategory sysRegulationCategory);

    /**
     * 批量删除制度分类
     * 
     * @param categoryIds 需要删除的制度分类主键集合
     * @return 结果
     */
    public int deleteSysRegulationCategoryByCategoryIds(Long[] categoryIds);

    /**
     * 删除制度分类信息
     * 
     * @param categoryId 制度分类主键
     * @return 结果
     */
    public int deleteSysRegulationCategoryByCategoryId(Long categoryId);
}