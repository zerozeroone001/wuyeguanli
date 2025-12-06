package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysOwnerChangeMapper;
import com.ruoyi.system.domain.SysOwnerChange;
import com.ruoyi.system.service.ISysOwnerChangeService;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.service.ISysOwnerProfileService;
import com.ruoyi.common.utils.StringUtils;

/**
 * 业主变更申请Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-03
 */
@Service
public class SysOwnerChangeServiceImpl implements ISysOwnerChangeService 
{
    @Autowired
    private SysOwnerChangeMapper sysOwnerChangeMapper;

    @Autowired
    private ISysOwnerProfileService sysOwnerProfileService;

    @Autowired
    private com.ruoyi.system.service.IEstatePropertyService estatePropertyService;

    /**
     * 查询业主变更申请
     * 
     * @param changeId 业主变更申请主键
     * @return 业主变更申请
     */
    @Override
    public SysOwnerChange selectSysOwnerChangeByChangeId(Long changeId)
    {
        return sysOwnerChangeMapper.selectSysOwnerChangeByChangeId(changeId);
    }

    /**
     * 查询业主变更申请列表
     * 
     * @param sysOwnerChange 业主变更申请
     * @return 业主变更申请
     */
    @Override
    public List<SysOwnerChange> selectSysOwnerChangeList(SysOwnerChange sysOwnerChange)
    {
        return sysOwnerChangeMapper.selectSysOwnerChangeList(sysOwnerChange);
    }

    /**
     * 新增业主变更申请
     * 
     * @param sysOwnerChange 业主变更申请
     * @return 结果
     */
    @Override
    public int insertSysOwnerChange(SysOwnerChange sysOwnerChange)
    {
        sysOwnerChange.setCreateTime(DateUtils.getNowDate());
        return sysOwnerChangeMapper.insertSysOwnerChange(sysOwnerChange);
    }

    /**
     * 修改业主变更申请
     * 
     * @param sysOwnerChange 业主变更申请
     * @return 结果
     */
    @Override
    public int updateSysOwnerChange(SysOwnerChange sysOwnerChange)
    {
        sysOwnerChange.setUpdateTime(DateUtils.getNowDate());
        return sysOwnerChangeMapper.updateSysOwnerChange(sysOwnerChange);
    }

    /**
     * 批量删除业主变更申请
     * 
     * @param changeIds 需要删除的业主变更申请主键
     * @return 结果
     */
    @Override
    public int deleteSysOwnerChangeByChangeIds(Long[] changeIds)
    {
        return sysOwnerChangeMapper.deleteSysOwnerChangeByChangeIds(changeIds);
    }

    /**
     * 删除业主变更申请信息
     * 
     * @param changeId 业主变更申请主键
     * @return 结果
     */
    @Override
    public int deleteSysOwnerChangeByChangeId(Long changeId)
    {
        return sysOwnerChangeMapper.deleteSysOwnerChangeByChangeId(changeId);
    }

    /**
     * 审核业主变更申请
     */
    @Override
    @Transactional
    public int auditSysOwnerChange(SysOwnerChange sysOwnerChange) {
        // 1. 更新申请状态
        sysOwnerChange.setUpdateTime(DateUtils.getNowDate());
        // sysOwnerChange.setUpdateBy(SecurityUtils.getUsername()); // Controller层已设置
        int rows = sysOwnerChangeMapper.updateSysOwnerChange(sysOwnerChange);

        // 2. 如果审核通过，执行变更逻辑
        if ("1".equals(sysOwnerChange.getStatus())) {
            // 获取完整的变更申请信息
            SysOwnerChange fullRequest = sysOwnerChangeMapper.selectSysOwnerChangeByChangeId(sysOwnerChange.getChangeId());
            if (fullRequest == null) {
                throw new RuntimeException("变更申请不存在");
            }

            // 这里简化逻辑：创建一个新的业主档案，或者更新现有档案
            // 由于"业主变更"通常意味着产权转移，我们需要：
            // A. 创建新业主档案 (如果不存在)
            // B. 更新房产关联关系 (sys_estate_user_property or property owner_id)
            
            // 在本系统中，房产归属似乎主要通过 sys_owner_profile 的 buildingNo/roomNo 或者是 sys_estate_user_property 关联
            // 假设是更新 sys_owner_profile
            
            // 查找该房产当前的业主档案
            // 由于 property_id 对应 sys_estate_property，我们需要找到对应的 sys_owner_profile
            // 但 sys_owner_profile 存储的是 building_no 和 room_no 文本
            
            // 1. 获取房产详情
            com.ruoyi.system.domain.EstateProperty property = estatePropertyService.selectEstatePropertyByPropertyId(fullRequest.getPropertyId());
            if (property == null) {
                 throw new RuntimeException("房产信息不存在");
            }

            // 2. 查找旧业主档案并标记为历史/删除，或者解除绑定
            // 这里假设一个房产只能有一个业主，或者我们只是添加新业主
            // 为了简单起见，我们直接插入一条新的业主记录
            
            SysOwnerProfile newProfile = new SysOwnerProfile();
            newProfile.setCommunityId(fullRequest.getCommunityId());
            newProfile.setRealName(fullRequest.getNewOwnerName());
            newProfile.setPhonenumber(fullRequest.getNewOwnerPhone());
            newProfile.setIdCardNo(fullRequest.getNewOwnerIdCard());
            newProfile.setBuildingNo(property.getBuildingName());
            newProfile.setRoomNo(property.getRoomNumber());
            // newProfile.setUnitNo(property.getUnitName()); // 如果有单元号
            newProfile.setCreateBy(sysOwnerChange.getUpdateBy());
            newProfile.setCreateTime(DateUtils.getNowDate());
            newProfile.setRemark("通过业主变更申请自动创建");
            
            sysOwnerProfileService.insertSysOwnerProfile(newProfile);
        }

        return rows;
    }
}
