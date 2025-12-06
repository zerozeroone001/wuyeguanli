package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.dto.OwnerProfileImportDto;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysOwnerProfileMapper;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.service.ISysOwnerProfileService;

import com.ruoyi.system.domain.EstateUserProperty;
import com.ruoyi.system.mapper.EstateUserPropertyMapper;
import com.ruoyi.system.service.IEstatePropertyService;
import com.ruoyi.system.service.IEstateUserPropertyService;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

/**
 * 业主信息扩展Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-15
 */
@Service
public class SysOwnerProfileServiceImpl implements ISysOwnerProfileService 
{
    private static final Logger log = LoggerFactory.getLogger(SysOwnerProfileServiceImpl.class);

    @Autowired
    private SysOwnerProfileMapper sysOwnerProfileMapper;

    @Autowired
    private EstateUserPropertyMapper estateUserPropertyMapper;

    @Autowired
    private IEstatePropertyService estatePropertyService;

    @Autowired
    private IEstateUserPropertyService estateUserPropertyService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public SysOwnerProfile selectSysOwnerProfileByOwnerId(Long ownerId)
    {
        return sysOwnerProfileMapper.selectSysOwnerProfileByOwnerId(ownerId);
    }

    @Override
    public SysOwnerProfile selectSysOwnerProfileByUserId(Long userId)
    {
        return sysOwnerProfileMapper.selectSysOwnerProfileByUserId(userId);
    }

    @Override
    public List<SysOwnerProfile> selectSysOwnerProfileList(SysOwnerProfile sysOwnerProfile)
    {
        List<SysOwnerProfile> list = sysOwnerProfileMapper.selectSysOwnerProfileList(sysOwnerProfile);
        for (SysOwnerProfile p : list) {
            if (p.getPropertyCount() != null && p.getPropertyCount() > 1) {
                p.setPropertyTag("多套房");
            } else {
                p.setPropertyTag("单套房");
            }
        }
        return list;
    }

    @Override
    @Transactional
    public int insertSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        sysOwnerProfile.setCreateTime(DateUtils.getNowDate());
        int rows = sysOwnerProfileMapper.insertSysOwnerProfile(sysOwnerProfile);
        bindProperty(sysOwnerProfile);
        return rows;
    }

    @Override
    @Transactional
    public int updateSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        sysOwnerProfile.setUpdateTime(DateUtils.getNowDate());
        int rows = sysOwnerProfileMapper.updateSysOwnerProfile(sysOwnerProfile);
        bindProperty(sysOwnerProfile);
        return rows;
    }

    /**
     * 尝试将业主信息中的房产绑定到用户
     */
    private void bindProperty(SysOwnerProfile profile) {
        if (profile.getUserId() == null || profile.getCommunityId() == null || 
            StringUtils.isEmpty(profile.getBuildingNo()) || StringUtils.isEmpty(profile.getRoomNo())) {
            return;
        }

        try {
            // 查找房产ID
            Long propertyId = estatePropertyService.selectPropertyIdByDetails(
                profile.getCommunityId(), profile.getBuildingNo(), profile.getRoomNo());

            if (propertyId != null) {
                EstateUserProperty eup = new EstateUserProperty();
                eup.setUserId(profile.getUserId());
                eup.setPropertyId(propertyId);
                eup.setCommunityId(profile.getCommunityId());
                eup.setStatus("1"); // 默认审核通过
                eup.setUserType("10"); // 业主
                
                // 尝试插入，忽略重复异常（由Service内部处理或捕获）
                try {
                    estateUserPropertyService.insertEstateUserProperty(eup);
                } catch (Exception e) {
                    // 忽略重复绑定错误
                    log.info("自动绑定房产跳过: 用户 {} 已绑定房产 {}", profile.getUserId(), propertyId);
                }
            }
        } catch (Exception e) {
            log.error("自动绑定房产失败", e);
        }
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByOwnerIds(Long[] ownerIds)
    {
        return sysOwnerProfileMapper.deleteSysOwnerProfileByOwnerIds(ownerIds);
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByUserIds(Long[] userIds)
    {
        return sysOwnerProfileMapper.deleteSysOwnerProfileByUserIds(userIds);
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByOwnerId(Long ownerId)
    {
        return sysOwnerProfileMapper.deleteSysOwnerProfileByOwnerId(ownerId);
    }

    @Override
    public String importOwner(List<OwnerProfileImportDto> ownerList, boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(ownerList) || ownerList.size() == 0)
        {
            throw new ServiceException("导入业主数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (OwnerProfileImportDto dto : ownerList)
        {
            try
            {
                // 根据用户最新要求，不进行重复校验，直接插入
                SysOwnerProfile newProfile = new SysOwnerProfile();

                // 拼接备注字段
                StringBuilder remarkBuilder = new StringBuilder();
                if (StringUtils.isNotEmpty(dto.getCommunityName())) {
                    remarkBuilder.append("小区：").append(dto.getCommunityName()).append(" ");
                }
                if (StringUtils.isNotEmpty(dto.getHouseNumber())) {
                    remarkBuilder.append("门牌号：").append(dto.getHouseNumber()).append(" ");
                }
                if (StringUtils.isNotEmpty(dto.getRemark())) {
                    remarkBuilder.append("原备注：").append(dto.getRemark());
                }

                newProfile.setRealName(dto.getRealName());
                newProfile.setPhonenumber(dto.getPhonenumber());
                newProfile.setIdCardNo(dto.getIdCardNo());
                newProfile.setRemark(remarkBuilder.toString());
                newProfile.setCreateBy(operName);
                
                this.insertSysOwnerProfile(newProfile);
                successNum++;
                successMsg.append("<br/>" + successNum + "、身份证号 " + dto.getIdCardNo() + " 的数据导入成功");
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、身份证号 " + dto.getIdCardNo() + " 的数据导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "导入结果不完整！ " + successNum + " 条数据成功，" + failureNum + " 条数据失败，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条。");
        }
        return successMsg.toString();
    }

    @Override
    public int auditOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        // 设置审核时间
        sysOwnerProfile.setUpdateTime(DateUtils.getNowDate());
        // 设置审核人（从SecurityUtils获取当前登录用户）
        sysOwnerProfile.setUpdateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());

        //判断审核状态，审核通过修改用户信息
        if ("1".equals(sysOwnerProfile.getStatus())) {
            SysUser user = userService.selectUserById(sysOwnerProfile.getUserId());
            if (user != null) {
                user.setIsOwner(1);

                userService.updateUser(user);
            }
        }

        // 执行审核更新
        return sysOwnerProfileMapper.updateSysOwnerProfile(sysOwnerProfile);
    }

    @Override
    public void clearUserCache(Long userId)
    {
        try {
            // 清除用户信息缓存
            String userInfoKey = "login_user:" + userId;
            redisCache.deleteObject(userInfoKey);
            
            // 清除用户权限缓存
            String permissionKey = "login_permissions:" + userId;
            redisCache.deleteObject(permissionKey);
            
            log.info("已清除用户ID为 {} 的缓存信息", userId);
        } catch (Exception e) {
            log.error("清除用户缓存失败，用户ID: {}", userId, e);
        }
    }

    @Override
    @Transactional
    public boolean transferOwnerProperties(Long sourceUserId, Long targetUserId, List<Long> targetPropertyIds) {
        if (sourceUserId == null || targetUserId == null) {
            throw new ServiceException("源用户和目标用户不能为空");
        }

        // 1. 获取源用户和目标用户当前拥有的所有房产关联记录
        EstateUserProperty querySource = new EstateUserProperty();
        querySource.setUserId(sourceUserId);
        List<EstateUserProperty> sourceProps = estateUserPropertyMapper.selectEstateUserPropertyList(querySource);

        EstateUserProperty queryTarget = new EstateUserProperty();
        queryTarget.setUserId(targetUserId);
        List<EstateUserProperty> targetProps = estateUserPropertyMapper.selectEstateUserPropertyList(queryTarget);

        // 2. 构建目标房产ID集合，方便快速查找
        Set<Long> targetSet = new HashSet<>();
        if (targetPropertyIds != null) {
            targetSet.addAll(targetPropertyIds);
        }

        // 3. 处理源用户的房产：如果存在于目标集合中，则转移给目标用户
        for (EstateUserProperty prop : sourceProps) {
            if (targetSet.contains(prop.getPropertyId())) {
                prop.setUserId(targetUserId);
                prop.setUpdateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());
                prop.setUpdateTime(DateUtils.getNowDate());
                estateUserPropertyMapper.updateEstateUserProperty(prop);
            }
        }

        // 4. 处理目标用户的房产：如果不存在于目标集合中（说明被移回左边了），则转移给源用户
        for (EstateUserProperty prop : targetProps) {
            if (!targetSet.contains(prop.getPropertyId())) {
                prop.setUserId(sourceUserId);
                prop.setUpdateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());
                prop.setUpdateTime(DateUtils.getNowDate());
                estateUserPropertyMapper.updateEstateUserProperty(prop);
            }
        }

        return true;
    }
}
