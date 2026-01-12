package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.utils.Utils;
import org.apache.commons.lang3.RandomStringUtils;
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
    private com.ruoyi.system.mapper.SysOwnerChangeMapper sysOwnerChangeMapper;

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

    @Autowired
    private com.ruoyi.system.service.IEstateCommunityService estateCommunityService;

    @Autowired
    private com.ruoyi.system.service.ISysOwnerTagService sysOwnerTagService;

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
    public SysOwnerProfile selectSysOwnerProfileByUserIdAndCommunityId(Long userId, Long communityId)
    {
        return sysOwnerProfileMapper.selectSysOwnerProfileByUserIdAndCommunityId(userId, communityId);
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
        int rows = 0;
        // 1. 确保用户存在
        // 如果传入了phonenumber，尝试查找或创建用户
        if (sysOwnerProfile.getUserId() == null && StringUtils.isNotEmpty(sysOwnerProfile.getPhonenumber())) {
            SysUser user = userService.selectUserByUserName(sysOwnerProfile.getPhonenumber());

        }

        // 2. 检查是否已存在档案 (Check Unique by UserId + CommunityId)
        SysOwnerProfile existing = sysOwnerProfileMapper.selectSysOwnerProfileByUserIdAndCommunityId(
                sysOwnerProfile.getUserId(), sysOwnerProfile.getCommunityId());
        String ownerNo = Utils.createOwnerNo();
        if (existing == null) {
            sysOwnerProfile.setOwnerNo(ownerNo);
            // 3. 插入档案
            sysOwnerProfile.setCreateTime(DateUtils.getNowDate());
            rows = sysOwnerProfileMapper.insertSysOwnerProfile(sysOwnerProfile);
        }

        // 4. 绑定房产
        bindProperty(sysOwnerProfile.getUserId(), sysOwnerProfile.getCommunityId(),
                     sysOwnerProfile.getBuildingNo(),sysOwnerProfile.getUnitNo(), sysOwnerProfile.getRoomNo(), sysOwnerProfile.getOwnerNo());
        
        return rows;
    }

    @Override
    @Transactional
    public int updateSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        // 主要更新备注等信息，房产增删走独立接口
        sysOwnerProfile.setUpdateTime(DateUtils.getNowDate());
        return sysOwnerProfileMapper.updateSysOwnerProfile(sysOwnerProfile);
    }

    /**
     * 绑定房产逻辑
     */
    /**
     * 安全地绑定房产 - 房产不存在时不抛出异常
     * @return SUCCESS-成功, PROPERTY_NOT_FOUND-房产不存在, NO_USER-无关联用户, 其他-错误信息
     */
    private String bindPropertySafely(Long userId, Long communityId, String buildingNo, String unitName, String roomNo, String ownerNo) {
        // 如果没有关联用户,不能绑定房产
        if (userId == null) {
            log.info("业主档案无关联用户,跳过房产绑定: ownerNo={}", ownerNo);
            return "NO_USER";
        }
        
        if (ownerNo == null || communityId == null ||
            StringUtils.isEmpty(buildingNo) || StringUtils.isEmpty(roomNo)) {
            return "参数异常";
        }

        // 查找房产ID
        Long propertyId = estatePropertyService.selectPropertyIdByDetails(
            communityId, buildingNo, unitName, roomNo);
        
        if (propertyId == null) {
            log.warn("未找到对应的房产信息: 小区ID={}, 楼栋={}, 单元={}, 房号={}", communityId, buildingNo, unitName, roomNo);
            return "PROPERTY_NOT_FOUND";
        }
        
        // Check for duplicates
        EstateUserProperty checkQuery = new EstateUserProperty();
        checkQuery.setUserId(userId);
        checkQuery.setPropertyId(propertyId);
        List<EstateUserProperty> exists = estateUserPropertyService.selectEstateUserPropertyList(checkQuery);
        if (!exists.isEmpty()) {
            log.info("房产已绑定: userId={}, propertyId={}", userId, propertyId);
            return "SUCCESS"; // 已存在视为成功
        }

        EstateUserProperty eup = new EstateUserProperty();
        eup.setUserId(userId);
        eup.setPropertyId(propertyId);
        eup.setCommunityId(communityId);
        eup.setStatus("1"); // 默认审核通过
        eup.setUserType("10"); // 业主
        if (StringUtils.isNotEmpty(ownerNo)) {
            eup.setOwnerNo(ownerNo);
        }
        
        try {
            estateUserPropertyService.insertEstateUserProperty(eup);
            return "SUCCESS";
        } catch (Exception e) {
            log.error("绑定房产失败", e);
            return e.getMessage();
        }
    }
    
    /**
     * 原有的绑定房产方法 - 保留用于其他地方调用
     */
    private void bindProperty(Long userId, Long communityId, String buildingNo, String unitName, String roomNo, String ownerNo) {
        String result = bindPropertySafely(userId, communityId, buildingNo, unitName, roomNo, ownerNo);
        if (!"SUCCESS".equals(result) && !"PROPERTY_NOT_FOUND".equals(result)) {
            throw new ServiceException(result);
        }
        if ("PROPERTY_NOT_FOUND".equals(result)) {
            throw new ServiceException("未找到房产: " + buildingNo + " " + roomNo);
        }
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByOwnerIds(Long[] ownerIds)
    {
        // 1. 收集受影响的用户ID
        Set<Long> userIds = new HashSet<>();
        for (Long id : ownerIds) {
            SysOwnerProfile profile = sysOwnerProfileMapper.selectSysOwnerProfileByOwnerId(id);
            if (profile != null && profile.getUserId() != null) {
                userIds.add(profile.getUserId());
            }
        }

        // 2. 执行删除
        int rows = sysOwnerProfileMapper.deleteSysOwnerProfileByOwnerIds(ownerIds);

        // 3. 检查并撤销业主身份
        checkAndRevokeOwnerStatus(userIds);

        return rows;
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByUserIds(Long[] userIds)
    {
        int rows = sysOwnerProfileMapper.deleteSysOwnerProfileByUserIds(userIds);
        
        Set<Long> userIdSet = new HashSet<>();
        for (Long id : userIds) userIdSet.add(id);
        checkAndRevokeOwnerStatus(userIdSet);
        
        return rows;
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByOwnerId(Long ownerId)
    {
        Set<Long> userIds = new HashSet<>();
        SysOwnerProfile profile = sysOwnerProfileMapper.selectSysOwnerProfileByOwnerId(ownerId);
        if (profile != null && profile.getUserId() != null) {
            userIds.add(profile.getUserId());
        }

        int rows = sysOwnerProfileMapper.deleteSysOwnerProfileByOwnerId(ownerId);
        checkAndRevokeOwnerStatus(userIds);
        return rows;
    }

    private void checkAndRevokeOwnerStatus(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return;

        for (Long userId : userIds) {
            SysOwnerProfile query = new SysOwnerProfile();
            query.setUserId(userId);
            List<SysOwnerProfile> list = sysOwnerProfileMapper.selectSysOwnerProfileList(query);
            
            if (list == null || list.isEmpty()) {
                SysUser user = userService.selectUserById(userId);
                if (user != null) {
                    user.setIsOwner(0);
                    userService.updateUser(user);
                }
                sysOwnerChangeMapper.deleteSysOwnerChangeByUserId(userId);
            }
        }
    }

    @Override
    @Transactional
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
                // 1. 验证必填字段
                if (StringUtils.isEmpty(dto.getUserName()))
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、姓名不能为空");
                    continue;
                }
                if (StringUtils.isEmpty(dto.getPhonenumber()))
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、电话不能为空：" + dto.getUserName());
                    continue;
                }
                if (StringUtils.isEmpty(dto.getCommunityName()))
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、小区名称不能为空：" + dto.getUserName());
                    continue;
                }
                if (StringUtils.isEmpty(dto.getBuildingNo()) || StringUtils.isEmpty(dto.getRoomNo()))
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、楼栋和房号不能为空：" + dto.getUserName());
                    continue;
                }

                // 2. 根据小区名称查找小区ID
                com.ruoyi.system.domain.EstateCommunity communityQuery = new com.ruoyi.system.domain.EstateCommunity();
                communityQuery.setCommunityName(dto.getCommunityName());
                List<com.ruoyi.system.domain.EstateCommunity> communityList = estateCommunityService.selectEstateCommunityList(communityQuery);
                if (communityList == null || communityList.isEmpty())
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、未找到小区：" + dto.getCommunityName() + "(" + dto.getUserName() + ")");
                    continue;
                }
                Long communityId = communityList.get(0).getCommunityId();

                // 3. 根据标签名称查找标签ID(可选)
                Long tagId = null;
                if (StringUtils.isNotEmpty(dto.getTagName()))
                {
                    com.ruoyi.system.domain.SysOwnerTag tagQuery = new com.ruoyi.system.domain.SysOwnerTag();
                    tagQuery.setTagName(dto.getTagName());
                    List<com.ruoyi.system.domain.SysOwnerTag> tagList = sysOwnerTagService.selectSysOwnerTagList(tagQuery);
                    if (tagList != null && !tagList.isEmpty())
                    {
                        tagId = tagList.get(0).getTagId();
                    }
                }

                // 4. 查找已存在的用户(不自动创建)
                SysUser existUser = null;
                Long userId = null;
                
                // 通过电话查询用户
                SysUser userByPhone = userService.selectUserByUserName(dto.getPhonenumber());
                
                if (userByPhone != null) {
                    // 找到用户,验证姓名是否匹配
                    if (dto.getUserName().equals(userByPhone.getNickName())) {
                        existUser = userByPhone;
                        userId = existUser.getUserId();
                    } else {
                        // 电话匹配但姓名不匹配,仍然使用该用户
                        existUser = userByPhone;
                        userId = existUser.getUserId();
                        log.warn("导入数据姓名不匹配: 电话={}, 导入姓名={}, 系统姓名={}", 
                                dto.getPhonenumber(), dto.getUserName(), userByPhone.getNickName());
                    }
                    
                    // 如果需要更新用户信息
                    if (isUpdateSupport)
                    {
                        existUser.setNickName(dto.getUserName());
                        existUser.setIsOwner(1);
                        existUser.setUpdateBy(operName);
                        userService.updateUser(existUser);
                    }
                } else {
                    // 用户不存在,不创建用户,userId 保持为 null
                    log.info("未找到用户: 电话={}, 姓名={}, 将创建无关联用户的业主档案", 
                            dto.getPhonenumber(), dto.getUserName());
                }


                // 5. 检查业主档案是否存在
                SysOwnerProfile existProfile = null;
                
                if (userId != null) {
                    // 如果有用户ID,通过用户ID和小区ID查询
                    existProfile = sysOwnerProfileMapper.selectSysOwnerProfileByUserIdAndCommunityId(userId, communityId);
                } else {
                    // 如果没有用户ID,通过姓名、电话和小区ID查询
                    existProfile = sysOwnerProfileMapper.selectSysOwnerProfileByPhoneNameCommunity(dto.getPhonenumber(), dto.getUserName());
                    // 进一步检查小区是否匹配
                    if (existProfile != null && !communityId.equals(existProfile.getCommunityId())) {
                        existProfile = null; // 小区不匹配,视为不存在
                    }
                }
                
                String ownerNo = null;
                if (existProfile == null)
                {
                    // 创建新业主档案
                    ownerNo = Utils.createOwnerNo();
                    SysOwnerProfile newProfile = new SysOwnerProfile();
                    newProfile.setOwnerNo(ownerNo);
                    newProfile.setUserId(userId); // 可能为null
                    newProfile.setCommunityId(communityId);
                    newProfile.setUserName(dto.getUserName());
                    newProfile.setPhonenumber(dto.getPhonenumber());
                    newProfile.setTagId(tagId);
                    newProfile.setIsOwner(1);
                    newProfile.setRemark(dto.getRemark());
                    newProfile.setCreateBy(operName);
                    newProfile.setCreateTime(DateUtils.getNowDate());
                    sysOwnerProfileMapper.insertSysOwnerProfile(newProfile);
                }
                else
                {
                    ownerNo = existProfile.getOwnerNo();
                    // 如果需要更新
                    if (isUpdateSupport && tagId != null)
                    {
                        existProfile.setTagId(tagId);
                        existProfile.setUpdateBy(operName);
                        existProfile.setUpdateTime(DateUtils.getNowDate());
                        sysOwnerProfileMapper.updateSysOwnerProfile(existProfile);
                    }
                }


                // 6. 绑定房产(如果房产存在且有关联用户)
                String bindResult = bindPropertySafely(userId, communityId, dto.getBuildingNo(), dto.getUnitNo(), dto.getRoomNo(), ownerNo);
                if ("SUCCESS".equals(bindResult))
                {
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、" + dto.getUserName() + " 导入成功");
                }
                else if ("NO_USER".equals(bindResult))
                {
                    // 无关联用户,仅创建档案
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、" + dto.getUserName() + " 导入成功(无关联用户,仅创建业主档案)");
                }
                else if ("PROPERTY_NOT_FOUND".equals(bindResult))
                {
                    // 房产不存在,但档案已创建
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、" + dto.getUserName() + " 导入成功(未找到对应房产,仅创建业主档案)");
                }
                else
                {
                    // 其他错误
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、" + dto.getUserName() + " 房产绑定失败：" + bindResult);
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、" + (dto.getUserName() != null ? dto.getUserName() : "未知") + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
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

    @Override
    public int auditOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        // 审核逻辑已移至 SysEstateUserApply，此处保留仅更新状态
        sysOwnerProfile.setUpdateTime(DateUtils.getNowDate());
        sysOwnerProfile.setUpdateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());
        return sysOwnerProfileMapper.updateSysOwnerProfile(sysOwnerProfile);
    }

    @Override
    public void clearUserCache(Long userId)
    {
        try {
            String userInfoKey = "login_user:" + userId;
            redisCache.deleteObject(userInfoKey);
            String permissionKey = "login_permissions:" + userId;
            redisCache.deleteObject(permissionKey);
        } catch (Exception e) {
            log.error("清除用户缓存失败", e);
        }
    }

    @Override
    @Transactional
    public int bindUserToOwner(Long userId, String phonenumber, String userName) {
        if (StringUtils.isEmpty(phonenumber) || StringUtils.isEmpty(userName)) {
            return 0;
        }

        SysOwnerProfile query = new SysOwnerProfile();
        query.setUserName(userName);
        query.setPhonenumber(phonenumber);
        
        // Use selectSysOwnerProfileRawList to find potential matches by name and phone
        List<SysOwnerProfile> list = sysOwnerProfileMapper.selectSysOwnerProfileRawList(query);
        
        int count = 0;
        for (SysOwnerProfile profile : list) {
            // Only bind if not already bound (or maybe update anyway? let's update)
            // If already bound to another user? Ideally check. But requirement says bind.
            // Assuming unbound or re-binding to same user.
            
            // 1. Update SysOwnerProfile
            profile.setUserId(userId);
            sysOwnerProfileMapper.updateSysOwnerProfile(profile);
            
            // 2. Update related EstateUserProperty records
            if (StringUtils.isNotEmpty(profile.getOwnerNo())) {
                EstateUserProperty propQuery = new EstateUserProperty();
                propQuery.setOwnerNo(profile.getOwnerNo());
                List<EstateUserProperty> props = estateUserPropertyMapper.selectEstateUserPropertyList(propQuery);
                for (EstateUserProperty prop : props) {
                    prop.setUserId(userId);
                    estateUserPropertyMapper.updateEstateUserProperty(prop);
                }
            }
            count++;
        }
        
        return count;
    }

    @Override
    @Transactional
    public boolean transferOwnerProperties(Long sourceUserId, Long targetUserId, List<Long> targetPropertyIds) {
        if (sourceUserId == null || targetUserId == null) {
            throw new ServiceException("源用户和目标用户不能为空");
        }

        log.info("开始房产转移: sourceUserId={}, targetUserId={}, targetPropertyIds={}", 
                sourceUserId, targetUserId, targetPropertyIds);

        // 1. 查询双方用户的房产
        EstateUserProperty querySource = new EstateUserProperty();
        querySource.setUserId(sourceUserId);
        List<EstateUserProperty> sourceProps = estateUserPropertyMapper.selectEstateUserPropertyList(querySource);

        EstateUserProperty queryTarget = new EstateUserProperty();
        queryTarget.setUserId(targetUserId);
        List<EstateUserProperty> targetProps = estateUserPropertyMapper.selectEstateUserPropertyList(queryTarget);

        Set<Long> targetSet = new HashSet<>();
        if (targetPropertyIds != null) {
            targetSet.addAll(targetPropertyIds);
        }

        log.info("源用户房产数量: {}, 目标用户房产数量: {}, 要转移到目标的房产ID: {}", 
                sourceProps.size(), targetProps.size(), targetSet);

        // 2. 收集需要处理的房产信息
        // 从源用户转移到目标用户的房产
        List<EstateUserProperty> sourceToTarget = new java.util.ArrayList<>();
        // 从目标用户转移到源用户的房产
        List<EstateUserProperty> targetToSource = new java.util.ArrayList<>();
        
        for (EstateUserProperty prop : sourceProps) {
            if (targetSet.contains(prop.getPropertyId())) {
                sourceToTarget.add(prop);
            }
        }
        
        for (EstateUserProperty prop : targetProps) {
            if (!targetSet.contains(prop.getPropertyId())) {
                targetToSource.add(prop);
            }
        }
        
        log.info("需要转移: 源->目标 {} 个, 目标->源 {} 个", 
                sourceToTarget.size(), targetToSource.size());

        String currentUser = com.ruoyi.common.utils.SecurityUtils.getUsername();
        java.util.Date now = DateUtils.getNowDate();

        // 变更房产信息
        for (EstateUserProperty prop : sourceToTarget) {
            //根据房产id查询 用户房产表
            EstateUserProperty queryByProperty = new EstateUserProperty();
            queryByProperty.setPropertyId(prop.getPropertyId());
            queryByProperty.setUserId(sourceUserId);
            List<EstateUserProperty> propertyList = estateUserPropertyMapper.selectEstateUserPropertyList(queryByProperty);
            
            if (propertyList.isEmpty()) {
                log.warn("未找到房产关联记录: propertyId={}, userId={}", prop.getPropertyId(), sourceUserId);
                continue;
            }
            
            EstateUserProperty currentProperty = propertyList.get(0);
            
            //查询目标用户的业主信息,如果不存在 则创建
            SysOwnerProfile targetProfile = sysOwnerProfileMapper.selectSysOwnerProfileByUserIdAndCommunityId(
                    targetUserId, currentProperty.getCommunityId());
            
            String targetOwnerNo;
            if (targetProfile == null) {
                // 目标用户在该小区没有业主档案,创建新档案
                targetOwnerNo = Utils.createOwnerNo();
                SysOwnerProfile newProfile = new SysOwnerProfile();
                newProfile.setOwnerNo(targetOwnerNo);
                newProfile.setUserId(targetUserId);
                newProfile.setCommunityId(currentProperty.getCommunityId());
                
                // 获取目标用户信息
                SysUser targetUser = userService.selectUserById(targetUserId);
                if (targetUser != null) {
                    newProfile.setUserName(targetUser.getNickName());
                    newProfile.setPhonenumber(targetUser.getPhonenumber());
                }
                
                newProfile.setIsOwner(1);
                newProfile.setCreateBy(currentUser);
                newProfile.setCreateTime(now);
                sysOwnerProfileMapper.insertSysOwnerProfile(newProfile);
                
                log.info("为目标用户创建业主档案: userId={}, communityId={}, ownerNo={}", 
                        targetUserId, currentProperty.getCommunityId(), targetOwnerNo);
            } else {
                targetOwnerNo = targetProfile.getOwnerNo();
            }
            
            //将当前循环的房产用户id与业主编号改成目标业主信息
            // 先删除原有关联,避免唯一约束冲突
            estateUserPropertyMapper.deleteEstateUserPropertyByAssociationId(currentProperty.getAssociationId());
            
            // 创建新的关联记录
            EstateUserProperty newProperty = new EstateUserProperty();
            newProperty.setUserId(targetUserId);
            newProperty.setPropertyId(currentProperty.getPropertyId());
            newProperty.setCommunityId(currentProperty.getCommunityId());
            newProperty.setOwnerNo(targetOwnerNo);
            newProperty.setUserType("10"); // 业主
            newProperty.setStatus("1"); // 已通过
            newProperty.setCreateBy(currentUser);
            newProperty.setCreateTime(now);
            
            estateUserPropertyMapper.insertEstateUserProperty(newProperty);
            
            log.info("房产转移成功: propertyId={}, 从用户 {} 转移到用户 {}, 新业主编号={}", 
                    currentProperty.getPropertyId(), sourceUserId, targetUserId, targetOwnerNo);
        }


        // 检查源用户转移后是否还有房产
        EstateUserProperty querySourceAfter = new EstateUserProperty();
        querySourceAfter.setUserId(sourceUserId);
        List<EstateUserProperty> sourcePropsAfter = estateUserPropertyMapper.selectEstateUserPropertyList(querySourceAfter);
        
        log.info("转移后源用户房产数量: {}", sourcePropsAfter.size());
        
        // 如果源用户没有房产了,撤销业主身份
        if (sourcePropsAfter.isEmpty()) {
            log.info("源用户已无房产,撤销业主身份");
            SysUser sourceUser = userService.selectUserById(sourceUserId);
            if (sourceUser != null && sourceUser.getIsOwner() == 1) {
                sourceUser.setIsOwner(0);
                sourceUser.setUpdateBy(currentUser);
                userService.updateUser(sourceUser);
            }
        }

        log.info("房产转移完成");
        return true;
    }

    @Override
    @Transactional
    public int addPropertyToOwner(Long ownerId, Long communityId, String buildingNo, String UtilNo, String roomNo) {
        SysOwnerProfile profile = selectSysOwnerProfileByOwnerId(ownerId);
        if (profile == null) {
            throw new ServiceException("业主信息不存在");
        }


        bindProperty(profile.getUserId(), communityId, buildingNo,UtilNo, roomNo, profile.getOwnerNo());
        return 1;
    }

    @Override
    public SysOwnerProfile selectSysOwnerProfileByPhoneNameCommunity(String phonenumber, String userName) {
        return sysOwnerProfileMapper.selectSysOwnerProfileByPhoneNameCommunity(phonenumber, userName);
    }
}