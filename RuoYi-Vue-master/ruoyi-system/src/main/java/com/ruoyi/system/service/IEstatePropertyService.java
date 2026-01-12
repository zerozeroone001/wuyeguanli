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
     * @return 房号集合
     */
    public List<String> selectRoomNumbersByBuildingName(Long communityId, String buildingName);

    /**
     * 查询楼栋下的房号列表
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param ownerId 排除的业主ID
     * @return 房号集合
     */
    public List<String> selectRoomNumbersByBuildingName(Long communityId, String buildingName, Long ownerId);

    /**
     * 根据小区ID、楼栋名称、单元号和房号查询房产ID
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param unitName 单元号
     * @param roomNumber 房号
     * @return 房产ID，如果不存在则返回null
     */
    Long selectPropertyIdByDetails(Long communityId, String buildingName, String unitName, String roomNumber);

    /**
     * 批量导入房产信息
     *
     * @param propertyList 房产信息列表
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 导入结果信息
     */
    String importProperty(List<com.ruoyi.system.domain.dto.EstatePropertyImportDto> propertyList, boolean updateSupport, String operName);

    /**
     * 查询房产树形结构
     * 返回楼栋→单元→房号的三级树形结构,排除已绑定的房产
     *
     * @param communityId 小区ID
     * @param excludeOwnerNo 排除的业主编号(可选,编辑时传入当前业主编号)
     * @return 树形结构节点列表
     */
    List<com.ruoyi.system.domain.dto.PropertyTreeNode> selectPropertyTree(Long communityId, String excludeOwnerNo);
}
