package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.EstateProperty;

/**
 * 房产信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
public interface EstatePropertyMapper 
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
     * 删除房产信息
     * 
     * @param propertyId 房产信息主键
     * @return 结果
     */
    public int deleteEstatePropertyByPropertyId(Long propertyId);

    /**
     * 批量删除房产信息
     * 
     * @param propertyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEstatePropertyByPropertyIds(Long[] propertyIds);

    /**
     * 查询小区下的楼栋列表
     *
     * @param communityId 小区ID
     * @return 楼栋名称集合
     */
    public List<String> selectBuildingNamesByCommunityId(Long communityId);

    /**
     * 查询楼栋下的房号列表
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param ownerId 排除的业主ID（即当前编辑的业主ID，该业主占用的房号不应被过滤）
     * @return 房号集合
     */
    public List<String> selectRoomNumbersByBuildingName(@Param("communityId") Long communityId, @Param("buildingName") String buildingName, @Param("ownerId") Long ownerId);

    /**
     * 根据小区ID、楼栋名称和房号查询房产ID
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param roomNumber 房号
     * @return 房产ID，如果不存在则返回null
     */
    Long selectPropertyIdByDetails(@Param("communityId") Long communityId, @Param("buildingName") String buildingName, @Param("roomNumber") String roomNumber);
}
