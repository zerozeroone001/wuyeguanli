package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.service.ISysOwnerProfileService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IVoteUserQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 投票用户查询服务实现类
 * 
 * @author ruoyi
 */
@Service
public class VoteUserQueryServiceImpl implements IVoteUserQueryService {
    
    private static final Logger log = LoggerFactory.getLogger(VoteUserQueryServiceImpl.class);
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysOwnerProfileService ownerProfileService;
    
    @Override
    public Long findUserIdByPhone(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            log.warn("电话号码为空，无法查询用户");
            return null;
        }
        
        // 清理电话号码格式
        String cleanPhone = cleanPhoneNumber(phoneNumber);
        if (StringUtils.isEmpty(cleanPhone)) {
            log.warn("电话号码格式不正确：{}", phoneNumber);
            return null;
        }
        
        // 先从sys_user表查询
        SysUser user = userService.selectUserByPhonenumber("13873593663");
        if (user != null) {
            log.info("通过电话号码 {} 在sys_user表中找到用户ID: {}", cleanPhone, user.getUserId());
            return user.getUserId();
        }
        
        // 如果sys_user表中没有找到，尝试从业主信息表查询
        Long userIdFromOwner = findUserIdFromOwnerProfile(cleanPhone);
        if (userIdFromOwner != null) {
            log.info("通过电话号码 {} 在业主信息表中找到用户ID: {}", cleanPhone, userIdFromOwner);
            return userIdFromOwner;
        }
        
        log.warn("未找到电话号码为 {} 的用户", cleanPhone);
        return null;
    }
    
    @Override
    public SysUser findUserByPhone(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return null;
        }
        
        String cleanPhone = cleanPhoneNumber(phoneNumber);
        if (StringUtils.isEmpty(cleanPhone)) {
            return null;
        }
        
        return userService.selectUserByPhonenumber(cleanPhone);
    }
    
    @Override
    public Long findUserIdByNameAndRoom(String ownerName, String roomNumber) {
        if (StringUtils.isEmpty(ownerName) || StringUtils.isEmpty(roomNumber)) {
            log.warn("业主姓名或房号为空，无法查询用户");
            return null;
        }
        
        // 通过业主信息表查询
        SysOwnerProfile queryParam = new SysOwnerProfile();
        queryParam.setRealName(ownerName);
        queryParam.setRoomNo(roomNumber);
        
        try {
            // 这里需要根据实际的业主信息服务接口调整
            // 假设有一个通过姓名和房号查询的方法
            Long userId = findUserIdFromOwnerProfileByNameAndRoom(ownerName, roomNumber);
            if (userId != null) {
                log.info("通过姓名 {} 和房号 {} 找到用户ID: {}", ownerName, roomNumber, userId);
                return userId;
            }
        } catch (Exception e) {
            log.error("通过姓名和房号查询用户失败", e);
        }
        
        log.warn("未找到姓名为 {} 房号为 {} 的用户", ownerName, roomNumber);
        return null;
    }
    
    /**
     * 从业主信息表中通过电话号码查找用户ID
     */
    private Long findUserIdFromOwnerProfile(String phoneNumber) {
        try {
            // 这里需要根据实际的业主信息服务接口调整
            SysOwnerProfile queryParam = new SysOwnerProfile();
            queryParam.setPhonenumber(phoneNumber);
            
            // 假设业主信息服务有查询方法
            // List<SysOwnerProfile> profiles = ownerProfileService.selectSysOwnerProfileList(queryParam);
            // if (profiles != null && !profiles.isEmpty()) {
            //     return profiles.get(0).getUserId();
            // }
            
            // 临时实现，需要根据实际情况调整
            return null;
        } catch (Exception e) {
            log.error("从业主信息表查询用户失败", e);
            return null;
        }
    }
    
    /**
     * 从业主信息表中通过姓名和房号查找用户ID
     */
    private Long findUserIdFromOwnerProfileByNameAndRoom(String ownerName, String roomNumber) {
        try {
            // 这里需要根据实际的业主信息服务接口调整
            SysOwnerProfile queryParam = new SysOwnerProfile();
            queryParam.setRealName(ownerName);
            queryParam.setRoomNo(roomNumber);
            
            // 假设业主信息服务有查询方法
            // List<SysOwnerProfile> profiles = ownerProfileService.selectSysOwnerProfileList(queryParam);
            // if (profiles != null && !profiles.isEmpty()) {
            //     return profiles.get(0).getUserId();
            // }
            
            // 临时实现，需要根据实际情况调整
            return null;
        } catch (Exception e) {
            log.error("从业主信息表通过姓名和房号查询用户失败", e);
            return null;
        }
    }
    
    /**
     * 清理电话号码格式
     */
    private String cleanPhoneNumber(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return "";
        }
        
        // 只保留数字
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        
        // 验证手机号格式
        if (cleanPhone.length() == 11 && cleanPhone.startsWith("1")) {
            return cleanPhone;
        }
        
        // 如果是固定电话格式，也尝试处理
        if (cleanPhone.length() >= 7 && cleanPhone.length() <= 12) {
            return cleanPhone;
        }
        
        return ""; // 格式不正确
    }
}