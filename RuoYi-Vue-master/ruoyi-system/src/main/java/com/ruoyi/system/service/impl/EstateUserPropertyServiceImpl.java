package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import com.ruoyi.system.domain.EstateUserProperty;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.mapper.EstateUserPropertyMapper;
import com.ruoyi.system.service.IEstateUserPropertyService;
import com.ruoyi.system.service.ISysOwnerProfileService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * �û��뷿�ݹ���ҵ��ʵ��
 *
 * @author ruoyi
 * @date 2025-09-05
 */
@Service
public class EstateUserPropertyServiceImpl implements IEstateUserPropertyService
{
    @Autowired
    private EstateUserPropertyMapper estateUserPropertyMapper;

    @Autowired
    private ISysOwnerProfileService ownerProfileService;

    @Autowired
    private ISysUserService userService;

    @Override
    public EstateUserProperty selectEstateUserPropertyByAssociationId(Long associationId)
    {
        return estateUserPropertyMapper.selectEstateUserPropertyByAssociationId(associationId);
    }

    @Override
    public List<EstateUserProperty> selectEstateUserPropertyList(EstateUserProperty estateUserProperty)
    {
        return estateUserPropertyMapper.selectEstateUserPropertyList(estateUserProperty);
    }

    @Override
    public int insertEstateUserProperty(EstateUserProperty estateUserProperty)
    {
        estateUserProperty.setCreateTime(DateUtils.getNowDate());
        estateUserProperty.setCreateBy(SecurityUtils.getUsername());

        try {
            return estateUserPropertyMapper.insertEstateUserProperty(estateUserProperty);
        } catch (Exception e) {
            // 捕获数据库唯一索引约束异常
            if (e.getMessage() != null &&
                (e.getMessage().contains("Duplicate entry") ||
                 e.getMessage().contains("uniq_user_property"))) {
                throw new ServiceException("您已提交过该房产的认证申请，请勿重复提交");
            }
            // 其他异常继续抛出
            throw e;
        }
    }

    @Override
    public int updateEstateUserProperty(EstateUserProperty estateUserProperty)
    {
        estateUserProperty.setUpdateTime(DateUtils.getNowDate());
        estateUserProperty.setUpdateBy(SecurityUtils.getUsername());
        return estateUserPropertyMapper.updateEstateUserProperty(estateUserProperty);
    }

    @Override
    public int deleteEstateUserPropertyByAssociationIds(Long[] associationIds)
    {
        return estateUserPropertyMapper.deleteEstateUserPropertyByAssociationIds(associationIds);
    }

    @Override
    public int deleteEstateUserPropertyByAssociationId(Long associationId)
    {
        return estateUserPropertyMapper.deleteEstateUserPropertyByAssociationId(associationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int auditEstateUserProperty(EstateUserProperty estateUserProperty)
    {
        if (estateUserProperty == null || estateUserProperty.getAssociationId() == null)
        {
            throw new ServiceException("����ID����Ϊ��");
        }
        if (StringUtils.isEmpty(estateUserProperty.getStatus()))
        {
            throw new ServiceException("���״̬����Ϊ��");
        }

        EstateUserProperty current = estateUserPropertyMapper.selectEstateUserPropertyByAssociationId(estateUserProperty.getAssociationId());
        if (current == null)
        {
            throw new ServiceException("�������벻���ڻ��ѱ�ɾ��");
        }

        boolean approved = "1".equals(estateUserProperty.getStatus());

        if (approved)
        {
            SysUser user = userService.selectUserById(current.getUserId());
            if (user != null && (user.getIsOwner() == 0 || user.getIsOwner() != 1))
            {
                user.setIsOwner(1);
                userService.updateUser(user);
            }

            SysOwnerProfile ownerProfile = ownerProfileService.selectSysOwnerProfileByUserId(current.getUserId());
            if (ownerProfile == null)
            {
                ownerProfile = new SysOwnerProfile();
                ownerProfile.setUserId(current.getUserId());
                ownerProfile.setCommunityId(current.getCommunityId());
                ownerProfile.setBuildingNo(current.getBuildingName());
                ownerProfile.setUnitNo(current.getUnitName());
                ownerProfile.setRoomNo(current.getRoomNumber());
                ownerProfile.setRealName(StringUtils.defaultIfBlank(current.getRealName(), user.getNickName()));
                ownerProfile.setPhonenumber(user.getPhonenumber());
                ownerProfile.setAuthStatus(2);
                ownerProfile.setIsOwner(1);
                ownerProfile.setStatus("0");
                ownerProfile.setRemark("审核通过自动创建");
                ownerProfile.setCreateBy(SecurityUtils.getUsername());
                ownerProfile.setCreateTime(DateUtils.getNowDate());
                ownerProfileService.insertSysOwnerProfile(ownerProfile);
            }
        }

        EstateUserProperty update = new EstateUserProperty();
        update.setAssociationId(estateUserProperty.getAssociationId());
        update.setStatus(estateUserProperty.getStatus());
        update.setRemark(estateUserProperty.getRemark());
        update.setUpdateTime(DateUtils.getNowDate());
        int rows = estateUserPropertyMapper.updateEstateUserProperty(update);

        if (approved && current.getUserId() != null)
        {
            ownerProfileService.clearUserCache(current.getUserId());
        }
        return rows;
    }

    @Override
    public int deleteEstateUserPropertyByUserIdAndPropertyId(Long userId, Long propertyId) {
        return 0;
    }

    @Override
    public int deleteEstateUserPropertyByUserIdAndCommunityId(Long userId, Long communityId) {
        return 0;
    }
}


