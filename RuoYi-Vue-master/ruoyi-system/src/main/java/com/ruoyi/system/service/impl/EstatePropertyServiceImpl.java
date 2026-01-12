package com.ruoyi.system.service.impl;


import java.util.List;
import java.util.ArrayList;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.EstatePropertyMapper;
import com.ruoyi.system.mapper.EstateCommunityMapper;
import com.ruoyi.system.domain.EstateProperty;
import com.ruoyi.system.domain.EstateCommunity;
import com.ruoyi.system.domain.dto.EstatePropertyImportDto;
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

    @Autowired
    private EstateCommunityMapper estateCommunityMapper;

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
     * 根据小区ID、楼栋名称、单元号和房号查询房产ID
     *
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param unitName 单元号
     * @param roomNumber 房号
     * @return 房产ID，如果不存在则返回null
     */
    @Override
    public Long selectPropertyIdByDetails(Long communityId, String buildingName, String unitName, String roomNumber) {
        return estatePropertyMapper.selectPropertyIdByDetails(communityId, buildingName, unitName, roomNumber);
    }

    /**
     * 批量导入房产信息
     *
     * @param propertyList 房产信息列表
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 导入结果信息
     */
    @Override
    public String importProperty(List<EstatePropertyImportDto> propertyList, boolean updateSupport, String operName)
    {
        if (StringUtils.isNull(propertyList) || propertyList.size() == 0)
        {
            throw new ServiceException("导入房产数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        
        for (EstatePropertyImportDto dto : propertyList)
        {
            try
            {
                // 验证必填字段
                if (StringUtils.isEmpty(dto.getCommunityName()))
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、小区名称不能为空");
                    continue;
                }
                if (StringUtils.isEmpty(dto.getBuildingName()))
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、楼栋号不能为空");
                    continue;
                }
                if (StringUtils.isEmpty(dto.getRoomNumber()))
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、房号不能为空");
                    continue;
                }

                // 根据小区名称查询小区ID
                EstateCommunity queryCommunity = new EstateCommunity();
                queryCommunity.setCommunityName(dto.getCommunityName());
                List<EstateCommunity> communityList = estateCommunityMapper.selectEstateCommunityList(queryCommunity);
                
                if (communityList == null || communityList.isEmpty())
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、小区 ").append(dto.getCommunityName()).append(" 不存在");
                    continue;
                }
                
                Long communityId = communityList.get(0).getCommunityId();

                // 检查房产是否已存在（包含单元号判断）
                Long existPropertyId = selectPropertyIdByDetails(communityId, dto.getBuildingName(), dto.getUnitName(), dto.getRoomNumber());
                
                if (StringUtils.isNotNull(existPropertyId))
                {
                    if (updateSupport)
                    {
                        // 更新已存在的房产信息
                        EstateProperty property = new EstateProperty();
                        property.setPropertyId(existPropertyId);
                        property.setCommunityId(communityId);
                        property.setBuildingName(dto.getBuildingName());
                        property.setUnitName(dto.getUnitName());
                        property.setFloor(dto.getFloor());
                        property.setRoomNumber(dto.getRoomNumber());
                        property.setArea(dto.getArea());
                        property.setRemark(dto.getRemark());
                        property.setStatus("0");
                        property.setUpdateBy(operName);
                        updateEstateProperty(property);
                        successNum++;
                        successMsg.append("<br/>").append(successNum).append("、房产 ").append(dto.getRoomNumber()).append(" 更新成功");
                    }
                    else
                    {
                        failureNum++;
                        failureMsg.append("<br/>").append(failureNum).append("、房产 ").append(dto.getRoomNumber()).append(" 已存在");
                    }
                }
                else
                {
                    // 新增房产信息
                    EstateProperty property = new EstateProperty();
                    property.setCommunityId(communityId);
                    property.setBuildingName(dto.getBuildingName());
                    property.setUnitName(dto.getUnitName());
                    property.setFloor(dto.getFloor());
                    property.setRoomNumber(dto.getRoomNumber());
                    property.setArea(dto.getArea());
                    property.setRemark(dto.getRemark());
                    property.setStatus("0");
                    property.setCreateBy(operName);
                    insertEstateProperty(property);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、房产 ").append(dto.getRoomNumber()).append(" 导入成功");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、房产 " + dto.getRoomNumber() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
            }
        }
        
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 查询房产树形结构
     * 返回楼栋→单元→房号的三级树形结构,排除已绑定的房产
     *
     * @param communityId 小区ID
     * @param excludeOwnerNo 排除的业主编号(可选,编辑时传入当前业主编号)
     * @return 树形结构节点列表
     */
    @Override
    public List<com.ruoyi.system.domain.dto.PropertyTreeNode> selectPropertyTree(Long communityId, String excludeOwnerNo)
    {
        // 查询可用房产列表
        List<EstateProperty> properties = estatePropertyMapper.selectAvailableProperties(communityId, excludeOwnerNo);
        
        // 构建树形结构: 楼栋 -> 单元 -> 房号
        List<com.ruoyi.system.domain.dto.PropertyTreeNode> buildingNodes = new ArrayList<>();
        java.util.Map<String, com.ruoyi.system.domain.dto.PropertyTreeNode> buildingMap = new java.util.LinkedHashMap<>();
        
        for (EstateProperty property : properties)
        {
            String buildingName = property.getBuildingName();
            String unitName = property.getUnitName() != null ? property.getUnitName() : "无单元";
            String roomNumber = property.getRoomNumber();
            boolean isBound = "1".equals(property.getStatus()); // status=1表示已绑定
            
            // 获取或创建楼栋节点
            com.ruoyi.system.domain.dto.PropertyTreeNode buildingNode = buildingMap.get(buildingName);
            if (buildingNode == null)
            {
                buildingNode = new com.ruoyi.system.domain.dto.PropertyTreeNode(buildingName, buildingName, 1);
                buildingMap.put(buildingName, buildingNode);
                buildingNodes.add(buildingNode);
            }
            
            // 获取或创建单元节点
            com.ruoyi.system.domain.dto.PropertyTreeNode unitNode = null;
            for (com.ruoyi.system.domain.dto.PropertyTreeNode child : buildingNode.getChildren())
            {
                if (child.getValue().equals(unitName))
                {
                    unitNode = child;
                    break;
                }
            }
            if (unitNode == null)
            {
                unitNode = new com.ruoyi.system.domain.dto.PropertyTreeNode(unitName, unitName, 2);
                buildingNode.addChild(unitNode);
            }
            
            // 创建房号节点(叶子节点,不应有children)
            com.ruoyi.system.domain.dto.PropertyTreeNode roomNode = new com.ruoyi.system.domain.dto.PropertyTreeNode(roomNumber, roomNumber, 3);
            roomNode.setPropertyId(property.getPropertyId());
            roomNode.setDisabled(isBound); // 已绑定的房产设置为禁用
            roomNode.setChildren(null); // 叶子节点不应有children属性
            unitNode.addChild(roomNode);
        }
        
        return buildingNodes;
    }
}
