package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
    private com.ruoyi.system.mapper.SysOwnerChangeMapper sysOwnerChangeMapper;

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

        // Generate Unique OwnerNo
        if (StringUtils.isEmpty(estateUserProperty.getOwnerNo())) {
            String ownerNo = null;
            for (int i = 0; i < 5; i++) {
                // Generate 18 chars: 14 chars date + 4 chars random number
                String candidate = DateUtils.dateTimeNow() + RandomStringUtils.randomNumeric(4);
                if (estateUserPropertyMapper.checkOwnerNoUnique(candidate) == 0) {
                    ownerNo = candidate;
                    break;
                }
            }
            if (ownerNo == null) {
                 throw new ServiceException("生成业主编号失败，请重试");
            }
            estateUserProperty.setOwnerNo(ownerNo);
        }

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
        // 1. 收集受影响的用户ID
        Set<Long> userIds = new HashSet<>();
        for (Long id : associationIds) {
            EstateUserProperty p = estateUserPropertyMapper.selectEstateUserPropertyByAssociationId(id);
            if (p != null && p.getUserId() != null) {
                userIds.add(p.getUserId());
            }
        }

        int rows = estateUserPropertyMapper.deleteEstateUserPropertyByAssociationIds(associationIds);

        // 2. 检查并撤销业主身份
        checkAndRevokeOwnerStatus(userIds);

        return rows;
    }

    @Override
    public int deleteEstateUserPropertyByAssociationId(Long associationId)
    {
        // 1. 收集用户ID
        Set<Long> userIds = new HashSet<>();
        EstateUserProperty p = estateUserPropertyMapper.selectEstateUserPropertyByAssociationId(associationId);
        if (p != null && p.getUserId() != null) {
            userIds.add(p.getUserId());
        }

        int rows = estateUserPropertyMapper.deleteEstateUserPropertyByAssociationId(associationId);

        // 2. 检查并撤销业主身份
        checkAndRevokeOwnerStatus(userIds);

        return rows;
    }

    /**
     * 检查用户是否还有房产，如果没有则撤销业主身份并删除变更记录
     */
    private void checkAndRevokeOwnerStatus(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return;

        for (Long userId : userIds) {
            EstateUserProperty query = new EstateUserProperty();
            query.setUserId(userId);
            // 查询剩余房产
            List<EstateUserProperty> list = estateUserPropertyMapper.selectEstateUserPropertyList(query);
            
            if (list == null || list.isEmpty()) {
                // 1. 更新用户表 is_owner = 0
                SysUser user = userService.selectUserById(userId);
                if (user != null) {
                    user.setIsOwner(0);
                    userService.updateUser(user);
                }
                
                // 2. 删除 sys_owner_change 表中的数据
                sysOwnerChangeMapper.deleteSysOwnerChangeByUserId(userId);
            }
        }
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

            // 检查房产是否已绑定
            EstateUserProperty query = new EstateUserProperty();
            query.setUserId(current.getUserId());
            query.setPropertyId(current.getPropertyId());
            query.setStatus("1");
            List<EstateUserProperty> exists = estateUserPropertyMapper.selectEstateUserPropertyList(query);
            for (EstateUserProperty p : exists) {
                if (!p.getAssociationId().equals(current.getAssociationId())) {
                     throw new ServiceException("该用户已绑定此房产，无需重复添加");
                }
            }

            SysOwnerProfile ownerProfile = ownerProfileService.selectSysOwnerProfileByUserIdAndCommunityId(current.getUserId(), current.getCommunityId());
            if (ownerProfile == null)
            {
                ownerProfile = new SysOwnerProfile();
                ownerProfile.setUserId(current.getUserId());
                ownerProfile.setCommunityId(current.getCommunityId());
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


