package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.EstateProperty;
import com.ruoyi.system.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysEstateUserApplyMapper;
import com.ruoyi.system.domain.SysEstateUserApply;
import com.ruoyi.system.service.ISysEstateUserApplyService;
import com.ruoyi.system.service.IEstatePropertyService;
import com.ruoyi.system.service.IEstateUserPropertyService;
import com.ruoyi.system.service.ISysOwnerProfileService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.domain.EstateUserProperty;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 房产认证申请Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-07
 */
@Service
public class SysEstateUserApplyServiceImpl implements ISysEstateUserApplyService 
{
    @Autowired
    private SysEstateUserApplyMapper sysEstateUserApplyMapper;

    @Autowired
    private IEstatePropertyService estatePropertyService;

    @Autowired
    private IEstateUserPropertyService estateUserPropertyService;

    @Autowired
    private ISysOwnerProfileService ownerProfileService;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询房产认证申请
     * 
     * @param applyId 房产认证申请主键
     * @return 房产认证申请
     */
    @Override
    public SysEstateUserApply selectSysEstateUserApplyByApplyId(Long applyId)
    {
        return sysEstateUserApplyMapper.selectSysEstateUserApplyByApplyId(applyId);
    }

    /**
     * 查询房产认证申请列表
     * 
     * @param sysEstateUserApply 房产认证申请
     * @return 房产认证申请
     */
    @Override
    public List<SysEstateUserApply> selectSysEstateUserApplyList(SysEstateUserApply sysEstateUserApply)
    {
        return sysEstateUserApplyMapper.selectSysEstateUserApplyList(sysEstateUserApply);
    }

    /**
     * 新增房产认证申请
     * 
     * @param sysEstateUserApply 房产认证申请
     * @return 结果
     */
    @Override
    public int insertSysEstateUserApply(SysEstateUserApply sysEstateUserApply)
    {
        sysEstateUserApply.setCreateTime(DateUtils.getNowDate());
        sysEstateUserApply.setCreateBy(SecurityUtils.getUsername());
        return sysEstateUserApplyMapper.insertSysEstateUserApply(sysEstateUserApply);
    }

    /**
     * 修改房产认证申请
     * 
     * @param sysEstateUserApply 房产认证申请
     * @return 结果
     */
    @Override
    public int updateSysEstateUserApply(SysEstateUserApply sysEstateUserApply)
    {
        sysEstateUserApply.setUpdateTime(DateUtils.getNowDate());
        sysEstateUserApply.setUpdateBy(SecurityUtils.getUsername());
        return sysEstateUserApplyMapper.updateSysEstateUserApply(sysEstateUserApply);
    }

    /**
     * 批量删除房产认证申请
     * 
     * @param applyIds 需要删除的房产认证申请主键
     * @return 结果
     */
    @Override
    public int deleteSysEstateUserApplyByApplyIds(Long[] applyIds)
    {
        return sysEstateUserApplyMapper.deleteSysEstateUserApplyByApplyIds(applyIds);
    }

    /**
     * 删除房产认证申请信息
     * 
     * @param applyId 房产认证申请主键
     * @return 结果
     */
    @Override
    public int deleteSysEstateUserApplyByApplyId(Long applyId)
    {
        return sysEstateUserApplyMapper.deleteSysEstateUserApplyByApplyId(applyId);
    }

    @Override
    @Transactional
    public int auditApply(Long applyId, String status, String remark) {
        SysEstateUserApply apply = selectSysEstateUserApplyByApplyId(applyId);
        if (apply == null) {
            throw new ServiceException("申请不存在");
        }
        
        apply.setStatus(status);
        // Removed auditRemark field, using remark? Or if status update uses auditRemark, I need to check entity.
        // Entity has 'remark' (inherited) but 'auditRemark' was removed.
        // So I should append to remark or just ignore if no field.
        if (remark != null) {
            apply.setRemark(remark);
        }
        apply.setUpdateBy(SecurityUtils.getUsername());
        apply.setUpdateTime(DateUtils.getNowDate());
        
        if ("1".equals(status)) { // 通过
            // 1. 获取房产ID (直接从申请中获取)
            Long propertyId = apply.getPropertyId();
            
            if (propertyId == null) {
                // 如果没有propertyId, 可能需要兼容旧逻辑? 但字段已变更, 假设propertyId必填
                throw new ServiceException("申请数据异常: 缺少房产ID");
            }

            String ownerNo = Utils.createOwnerNo();

            // 2. 绑定房产 (EstateUserProperty)
            EstateProperty estateProperty = estatePropertyService.selectEstatePropertyByPropertyId(propertyId);

            //获取用户
            SysUser user = userService.selectUserById(apply.getUserId());

            // 3. 创建/更新业主档案 (SysOwnerProfile)
            SysOwnerProfile ownerProfile = ownerProfileService.selectSysOwnerProfileByUserId(apply.getUserId());
            if (ownerProfile == null) {
                ownerProfile = new SysOwnerProfile();
                ownerProfile.setOwnerNo(ownerNo);
                ownerProfile.setUserId(apply.getUserId());
                ownerProfile.setCommunityId(apply.getCommunityId());
                ownerProfile.setUserName(user.getUserName());
                ownerProfile.setPhonenumber(user.getPhonenumber());
                ownerProfile.setCreateBy(SecurityUtils.getUsername());
                ownerProfile.setCreateTime(DateUtils.getNowDate());

                ownerProfile.setCommunityId(estateProperty.getCommunityId());
                ownerProfile.setBuildingNo(estateProperty.getBuildingName());
                ownerProfile.setRoomNo(estateProperty.getRoomNumber());
                ownerProfileService.insertSysOwnerProfile(ownerProfile);
            }else{
                EstateUserProperty estateUserProperty = new EstateUserProperty();
                estateUserProperty.setOwnerNo(ownerProfile.getOwnerNo());
                estateUserProperty.setPropertyId(propertyId);
                estateUserProperty.setCommunityId(ownerProfile.getCommunityId());
                estateUserProperty.setUserId(user.getUserId());
                estateUserProperty.setRoomNumber(estateProperty.getRoomNumber());
                estateUserProperty.setUserType("00");
                estateUserProperty.setStatus("1");
                estateUserPropertyService.insertEstateUserProperty(estateUserProperty);
            }

            // 4. 更新用户状态
            if (user != null && user.getIsOwner() != 1) {
                user.setIsOwner(1);
                userService.updateUser(user);
            }
        }
        
        return sysEstateUserApplyMapper.updateSysEstateUserApply(apply);
    }

    @Override
    public boolean checkPendingOrApprovedApply(Long userId, Long propertyId) {
        SysEstateUserApply query = new SysEstateUserApply();
        query.setUserId(userId);
        query.setPropertyId(propertyId);
        int count = sysEstateUserApplyMapper.countPendingOrApprovedApply(query);
        return count > 0;
    }

}
