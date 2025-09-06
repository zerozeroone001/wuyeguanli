package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.EstateUserPropertyMapper;
import com.ruoyi.system.domain.EstateUserProperty;
import com.ruoyi.system.service.IEstateUserPropertyService;

/**
 * 用户与房产关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
@Service
public class EstateUserPropertyServiceImpl implements IEstateUserPropertyService 
{
    @Autowired
    private EstateUserPropertyMapper estateUserPropertyMapper;

    /**
     * 查询用户与房产关系
     * 
     * @param associationId 用户与房产关系主键
     * @return 用户与房产关系
     */
    @Override
    public EstateUserProperty selectEstateUserPropertyByAssociationId(Long associationId)
    {
        return estateUserPropertyMapper.selectEstateUserPropertyByAssociationId(associationId);
    }

    /**
     * 查询用户与房产关系列表
     * 
     * @param estateUserProperty 用户与房产关系
     * @return 用户与房产关系
     */
    @Override
    public List<EstateUserProperty> selectEstateUserPropertyList(EstateUserProperty estateUserProperty)
    {
        return estateUserPropertyMapper.selectEstateUserPropertyList(estateUserProperty);
    }

    /**
     * 新增用户与房产关系
     * 
     * @param estateUserProperty 用户与房产关系
     * @return 结果
     */
    @Override
    public int insertEstateUserProperty(EstateUserProperty estateUserProperty)
    {
        estateUserProperty.setCreateTime(DateUtils.getNowDate());
        return estateUserPropertyMapper.insertEstateUserProperty(estateUserProperty);
    }

    /**
     * 修改用户与房产关系
     * 
     * @param estateUserProperty 用户与房产关系
     * @return 结果
     */
    @Override
    public int updateEstateUserProperty(EstateUserProperty estateUserProperty)
    {
        estateUserProperty.setUpdateTime(DateUtils.getNowDate());
        return estateUserPropertyMapper.updateEstateUserProperty(estateUserProperty);
    }

    /**
     * 批量删除用户与房产关系
     * 
     * @param associationIds 需要删除的用户与房产关系主键
     * @return 结果
     */
    @Override
    public int deleteEstateUserPropertyByAssociationIds(Long[] associationIds)
    {
        return estateUserPropertyMapper.deleteEstateUserPropertyByAssociationIds(associationIds);
    }

    /**
     * 删除用户与房产关系信息
     * 
     * @param associationId 用户与房产关系主键
     * @return 结果
     */
    @Override
    public int deleteEstateUserPropertyByAssociationId(Long associationId)
    {
        return estateUserPropertyMapper.deleteEstateUserPropertyByAssociationId(associationId);
    }
}
