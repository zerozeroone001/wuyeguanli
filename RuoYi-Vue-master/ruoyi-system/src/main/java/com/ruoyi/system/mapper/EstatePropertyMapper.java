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
     * 根据小区ID、楼栋名称、单元号和房号查询房产ID
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param unitName 单元号
     * @param roomNumber 房号
     * @return 房产ID，如果不存在则返回null
     */
    Long selectPropertyIdByDetails(@Param("communityId") Long communityId, @Param("buildingName") String buildingName, @Param("unitName") String unitName, @Param("roomNumber") String roomNumber);

    /**
     * 查询可用房产列表(用于构建树形结构)
     * 排除已绑定的房产,或者排除指定业主编号已绑定的房产
     *
     * @param communityId 小区ID
     * @param excludeOwnerNo 排除的业主编号(可选,编辑时传入当前业主编号)
     * @return 房产信息集合
     */
    List<EstateProperty> selectAvailableProperties(@Param("communityId") Long communityId, @Param("excludeOwnerNo") String excludeOwnerNo);

    /**
     * 查询小区的楼栋信息(包含是否有单元的判断)
     *
     * @param communityId 小区ID
     * @return 楼栋信息列表(buildingName, hasUnit, roomCount)
     */
    List<java.util.Map<String, Object>> selectBuildingInfoByCommunityId(Long communityId);

    /**
     * 查询楼栋的单元列表
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @return 单元名称列表
     */
    List<String> selectUnitsByBuilding(@Param("communityId") Long communityId, @Param("buildingName") String buildingName);

    /**
     * 查询房屋详细信息(用于上门拜访)
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param unitName 单元名称(可选)
     * @param meetingId 会议ID
     * @return 房屋详细信息列表
     */
    List<java.util.Map<String, Object>> selectRoomDetailsForVisit(@Param("communityId") Long communityId, @Param("buildingName") String buildingName, @Param("unitName") String unitName, @Param("meetingId") Long meetingId);
}
