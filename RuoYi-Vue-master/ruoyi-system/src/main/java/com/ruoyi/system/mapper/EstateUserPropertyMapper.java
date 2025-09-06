package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.EstateUserProperty;

/**
 * 用户与房产关系Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
public interface EstateUserPropertyMapper 
{
    /**
     * 查询用户与房产关系
     * 
     * @param associationId 用户与房产关系主键
     * @return 用户与房产关系
     */
    public EstateUserProperty selectEstateUserPropertyByAssociationId(Long associationId);

    /**
     * 查询用户与房产关系列表
     * 
     * @param estateUserProperty 用户与房产关系
     * @return 用户与房产关系集合
     */
    public List<EstateUserProperty> selectEstateUserPropertyList(EstateUserProperty estateUserProperty);

    /**
     * 新增用户与房产关系
     * 
     * @param estateUserProperty 用户与房产关系
     * @return 结果
     */
    public int insertEstateUserProperty(EstateUserProperty estateUserProperty);

    /**
     * 修改用户与房产关系
     * 
     * @param estateUserProperty 用户与房产关系
     * @return 结果
     */
    public int updateEstateUserProperty(EstateUserProperty estateUserProperty);

    /**
     * 删除用户与房产关系
     * 
     * @param associationId 用户与房产关系主键
     * @return 结果
     */
    public int deleteEstateUserPropertyByAssociationId(Long associationId);

    /**
     * 批量删除用户与房产关系
     * 
     * @param associationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEstateUserPropertyByAssociationIds(Long[] associationIds);
}
