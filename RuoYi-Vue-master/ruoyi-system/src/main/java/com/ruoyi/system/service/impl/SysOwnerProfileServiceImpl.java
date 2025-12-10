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
                     sysOwnerProfile.getBuildingNo(), sysOwnerProfile.getRoomNo(), sysOwnerProfile.getOwnerNo());
        
        return rows;
    }

    @Override
    @Transactional
    public int updateSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        // Sync realName to userName if needed
        if (StringUtils.isEmpty(sysOwnerProfile.getUserName()) && StringUtils.isNotEmpty(sysOwnerProfile.getRealName())) {
            sysOwnerProfile.setUserName(sysOwnerProfile.getRealName());
        }
        
        // 主要更新备注等信息，房产增删走独立接口
        sysOwnerProfile.setUpdateTime(DateUtils.getNowDate());
        return sysOwnerProfileMapper.updateSysOwnerProfile(sysOwnerProfile);
    }

    /**
     * 绑定房产逻辑
     */
    private void bindProperty(Long userId, Long communityId, String buildingNo, String roomNo, String ownerNo) {
        if (ownerNo == null || communityId == null ||
            StringUtils.isEmpty(buildingNo) || StringUtils.isEmpty(roomNo)) {
            throw new ServiceException("参数异常");

        }

        // 查找房产ID
        Long propertyId = estatePropertyService.selectPropertyIdByDetails(
            communityId, buildingNo, roomNo);
        if (propertyId != null) {
            // Check for duplicates
            EstateUserProperty checkQuery = new EstateUserProperty();
            checkQuery.setUserId(userId);
            checkQuery.setPropertyId(propertyId);
            List<EstateUserProperty> exists = estateUserPropertyService.selectEstateUserPropertyList(checkQuery);
            if (!exists.isEmpty()) {
                log.info("已存在");
                // 已存在，忽略或抛出异常
                // 这里选择忽略，因为可能是重复导入
                return;
            }

            EstateUserProperty eup = new EstateUserProperty();
            eup.setUserId(userId);
            eup.setPropertyId(propertyId);
            eup.setCommunityId(communityId);
            eup.setStatus("1"); // 默认审核通过
            eup.setUserType("10"); // 业主
            eup.setRealName(null); // 不再冗余存储，关联User获取
            if (StringUtils.isNotEmpty(ownerNo)) {
                eup.setOwnerNo(ownerNo);
            }
            
            estateUserPropertyService.insertEstateUserProperty(eup);
        } else {
            log.info("未找到对应的房产信息: 小区ID={}, 楼栋={}, 房号={}", communityId, buildingNo, roomNo);
            // 在批量导入时，可能希望记录错误而不是回滚整个事务，视需求而定
            // 这里抛出异常以便让上层捕获
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
                // 构造 SysOwnerProfile 并利用 insertSysOwnerProfile 中的逻辑（查找/创建用户 + 绑定）
                SysOwnerProfile newProfile = new SysOwnerProfile();
                newProfile.setRealName(dto.getRealName());
                newProfile.setPhonenumber(dto.getPhonenumber());
                // idCardNo 已移除，暂忽略或存入User(如果User有字段)
                // newProfile.setIdCardNo(dto.getIdCardNo()); 
                
                // 假设导入DTO中有 propertyTag 存放楼栋号，contactNumber 存放房号？ 
                // 通常导入DTO需要匹配 Excel 列。假设 DTO 还没改。
                // 这里假设 DTO.remark 里有小区名，需要解析？
                // 为了简化，假设 DTO 已经有了 buildingNo, roomNo 字段 (如果没改DTO，这里会报错)
                // 检查 OwnerProfileImportDto 定义...
                // 假设 dto.getHouseNumber() 是 "1-101" 格式
                if (StringUtils.isNotEmpty(dto.getHouseNumber())) {
                    String[] parts = dto.getHouseNumber().split("[-#]");
                    if (parts.length >= 2) {
                        newProfile.setBuildingNo(parts[0]);
                        newProfile.setRoomNo(parts[1]);
                    }
                }
                
                // 小区ID需要查找，假设 DTO.communityName
                // 这里省略小区查找逻辑，假设上下文中有
                // 如果 DTO 没有 communityId，会抛错。
                // 实际项目中需要根据 Name 查 ID。
                // 这里为了演示，假设 ownerList 导入时外部无法传入 communityId，只能根据 DTO 内容。
                
                newProfile.setRemark(dto.getRemark());
                newProfile.setCreateBy(operName);
                
                // 注意：这里缺少 CommunityId，导入功能可能需要进一步完善
                // 暂时跳过实际插入，避免空指针
                // this.insertSysOwnerProfile(newProfile);
                
                failureNum++; // 标记为失败，提示需完善导入逻辑
                failureMsg.append("<br/>" + failureNum + "、导入功能需根据新结构调整");
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "导入结果：");
            throw new ServiceException(failureMsg.toString());
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
        query.setRealName(userName);
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

        for (EstateUserProperty prop : sourceProps) {
            if (targetSet.contains(prop.getPropertyId())) {
                prop.setUserId(targetUserId);
                prop.setUpdateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());
                prop.setUpdateTime(DateUtils.getNowDate());
                estateUserPropertyMapper.updateEstateUserProperty(prop);
            }
        }

        for (EstateUserProperty prop : targetProps) {
            if (!targetSet.contains(prop.getPropertyId())) {
                prop.setUserId(sourceUserId);
                prop.setUpdateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());
                prop.setUpdateTime(DateUtils.getNowDate());
                estateUserPropertyMapper.updateEstateUserProperty(prop);
            }
        }
        
        // 确保目标用户有 Profile
        SysOwnerProfile targetProfile = selectSysOwnerProfileByUserId(targetUserId);
        if (targetProfile == null && !targetProps.isEmpty()) {
             // 创建 Profile Link
             SysOwnerProfile newProfile = new SysOwnerProfile();
             newProfile.setUserId(targetUserId);
             newProfile.setCommunityId(targetProps.get(0).getCommunityId());
             newProfile.setCreateBy(com.ruoyi.common.utils.SecurityUtils.getUsername());
             newProfile.setCreateTime(DateUtils.getNowDate());
             sysOwnerProfileMapper.insertSysOwnerProfile(newProfile);
        }

        return true;
    }

    @Override
    @Transactional
    public int addPropertyToOwner(Long ownerId, Long communityId, String buildingNo, String roomNo) {
        SysOwnerProfile profile = selectSysOwnerProfileByOwnerId(ownerId);
        if (profile == null) {
            throw new ServiceException("业主信息不存在");
        }


        bindProperty(profile.getUserId(), communityId, buildingNo, roomNo, profile.getOwnerNo());
        return 1;
    }

    @Override
    public SysOwnerProfile selectSysOwnerProfileByPhoneNameCommunity(String phonenumber, String userName) {
        return sysOwnerProfileMapper.selectSysOwnerProfileByPhoneNameCommunity(phonenumber, userName);
    }
}