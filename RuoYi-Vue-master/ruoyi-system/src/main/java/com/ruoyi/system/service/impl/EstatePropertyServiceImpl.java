package com.ruoyi.system.service.impl;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.EstatePropertyMapper;
import com.ruoyi.system.domain.EstateProperty;
import com.ruoyi.system.service.IEstatePropertyService;

/**
 * 房产信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
@Service
public class EstatePropertyServiceImpl implements IEstatePropertyService 
{
    @Autowired
    private EstatePropertyMapper estatePropertyMapper;

    /**
     * 查询房产信息
     * 
     * @param propertyId 房产信息主键
     * @return 房产信息
     */
    @Override
    public EstateProperty selectEstatePropertyByPropertyId(Long propertyId)
    {
        return estatePropertyMapper.selectEstatePropertyByPropertyId(propertyId);
    }

    /**
     * 查询房产信息列表
     * 
     * @param estateProperty 房产信息
     * @return 房产信息
     */
    @Override
    public List<EstateProperty> selectEstatePropertyList(EstateProperty estateProperty)
    {
        return estatePropertyMapper.selectEstatePropertyList(estateProperty);
    }

    /**
     * 新增房产信息
     * 
     * @param estateProperty 房产信息
     * @return 结果
     */
    @Override
    public int insertEstateProperty(EstateProperty estateProperty)
    {
        estateProperty.setCreateTime(DateUtils.getNowDate());
        return estatePropertyMapper.insertEstateProperty(estateProperty);
    }

    /**
     * 修改房产信息
     * 
     * @param estateProperty 房产信息
     * @return 结果
     */
    @Override
    public int updateEstateProperty(EstateProperty estateProperty)
    {
        estateProperty.setUpdateTime(DateUtils.getNowDate());
        return estatePropertyMapper.updateEstateProperty(estateProperty);
    }

    /**
     * 批量删除房产信息
     * 
     * @param propertyIds 需要删除的房产信息主键
     * @return 结果
     */
    @Override
    public int deleteEstatePropertyByPropertyIds(Long[] propertyIds)
    {
        return estatePropertyMapper.deleteEstatePropertyByPropertyIds(propertyIds);
    }

    /**
     * 删除房产信息信息
     * 
     * @param propertyId 房产信息主键
     * @return 结果
     */
    @Override
    public int deleteEstatePropertyByPropertyId(Long propertyId)
    {
        return estatePropertyMapper.deleteEstatePropertyByPropertyId(propertyId);
    }

    /**
     * 查询小区下的楼栋列表
     *
     * @param communityId 小区ID
     * @return 楼栋名称集合
     */
    @Override
    public List<String> selectBuildingNamesByCommunityId(Long communityId)
    {
        return estatePropertyMapper.selectBuildingNamesByCommunityId(communityId);
    }

    /**
     * 查询楼栋下的房号列表
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @return 房号集合
     */
    @Override
    public List<String> selectRoomNumbersByBuildingName(Long communityId, String buildingName)
    {
        return selectRoomNumbersByBuildingName(communityId, buildingName, null);
    }

    /**
     * 查询楼栋下的房号列表
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param ownerId 排除的业主ID
     * @return 房号集合
     */
    @Override
    public List<String> selectRoomNumbersByBuildingName(Long communityId, String buildingName, Long ownerId)
    {
        return estatePropertyMapper.selectRoomNumbersByBuildingName(communityId, buildingName, ownerId);
    }

    /**
     * 根据小区ID、楼栋名称和房号查询房产ID
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param roomNumber 房号
     * @return 房产ID，如果不存在则返回null
     */
    @Override
    public Long selectPropertyIdByDetails(Long communityId, String buildingName, String roomNumber) {
        return estatePropertyMapper.selectPropertyIdByDetails(communityId, buildingName, roomNumber);
    }
}
