package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.EstateProperty;

/**
 * 房产信息Service接口
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
public interface IEstatePropertyService 
{
    /**
     * 查询房产信息
     * 
     * @param propertyId 房产信息主键
     * @return 房产信息
     */
    public EstateProperty selectEstatePropertyByPropertyId(Long propertyId);

    /**
     * 查询房产信息列表
     * 
     * @param estateProperty 房产信息
     * @return 房产信息集合
     */
    public List<EstateProperty> selectEstatePropertyList(EstateProperty estateProperty);

    /**
     * 新增房产信息
     * 
     * @param estateProperty 房产信息
     * @return 结果
     */
    public int insertEstateProperty(EstateProperty estateProperty);

    /**
     * 修改房产信息
     * 
     * @param estateProperty 房产信息
     * @return 结果
     */
    public int updateEstateProperty(EstateProperty estateProperty);

    /**
     * 批量删除房产信息
     * 
     * @param propertyIds 需要删除的房产信息主键集合
     * @return 结果
     */
    public int deleteEstatePropertyByPropertyIds(Long[] propertyIds);

    /**
     * 删除房产信息信息
     * 
     * @param propertyId 房产信息主键
     * @return 结果
     */
    public int deleteEstatePropertyByPropertyId(Long propertyId);
}
