package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysOwnerProfileMapper;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.service.ISysOwnerProfileService;

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
    private ISysUserService userService;

    @Override
    public SysOwnerProfile selectSysOwnerProfileByUserId(Long userId)
    {
        return sysOwnerProfileMapper.selectSysOwnerProfileByUserId(userId);
    }

    @Override
    public List<SysOwnerProfile> selectSysOwnerProfileList(SysOwnerProfile sysOwnerProfile)
    {
        return sysOwnerProfileMapper.selectSysOwnerProfileList(sysOwnerProfile);
    }

    @Override
    @Transactional
    public int insertSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
//        SysUser sysUser = new SysUser();
//        sysUser.setNickName(sysOwnerProfile.getRealName());
//        sysUser.setUserName(sysOwnerProfile.getPhonenumber());
//        sysUser.setPhonenumber(sysOwnerProfile.getPhonenumber());
//        sysUser.setPassword(SecurityUtils.encryptPassword("000000"));
//
//        if (UserConstants.NOT_UNIQUE)
//        {
//            throw new ServiceException("新增用户'" + sysUser.getUserName() + "'失败，登录账号已存在");
//        }
//        if (StringUtils.isNotEmpty(sysOwnerProfile.getPhonenumber()) && UserConstants.NOT_UNIQUE)
//        {
//            throw new ServiceException("新增用户'" + sysUser.getUserName() + "'失败，手机号码已存在");
//        }
//
//        userService.insertUser(sysUser);
//
//        sysOwnerProfile.setUserId(sysUser.getUserId());
        sysOwnerProfile.setCreateTime(DateUtils.getNowDate());
        return sysOwnerProfileMapper.insertSysOwnerProfile(sysOwnerProfile);
    }

    @Override
    @Transactional
    public int updateSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
//        SysUser sysUser = userService.selectUserById(sysOwnerProfile.getUserId());
//        if (sysUser != null) {
//            sysUser.setNickName(sysOwnerProfile.getRealName());
//            sysUser.setPhonenumber(sysOwnerProfile.getPhonenumber());
//
//            if (StringUtils.isNotEmpty(sysOwnerProfile.getPhonenumber()) && UserConstants.NOT_UNIQUE)
//            {
//                throw new ServiceException("修改用户'" + sysUser.getUserName() + "'失败，手机号码已存在");
//            }
//            userService.updateUser(sysUser);
//        }

        sysOwnerProfile.setUpdateTime(DateUtils.getNowDate());
        return sysOwnerProfileMapper.updateSysOwnerProfile(sysOwnerProfile);
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByUserIds(Long[] userIds)
    {
//        for (Long userId : userIds) {
//            userService.deleteUserById(userId);
//        }
        return sysOwnerProfileMapper.deleteSysOwnerProfileByUserIds(userIds);
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByUserId(Long userId)
    {
//        userService.deleteUserById(userId);
        return sysOwnerProfileMapper.deleteSysOwnerProfileByUserId(userId);
    }

    @Override
    public String importOwner(List<SysOwnerProfile> ownerList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(ownerList) || ownerList.size() == 0)
        {
            throw new ServiceException("导入业主数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysOwnerProfile owner : ownerList)
        {
            try
            {
                // Verify whether this user exists by phone number (which is the username)
                SysUser user = userService.selectUserByUserName(owner.getPhonenumber());
                if (StringUtils.isNull(user))
                {
                    owner.setCreateBy(operName);
                    this.insertSysOwnerProfile(owner);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + owner.getPhonenumber() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    owner.setUserId(user.getUserId());
                    owner.setUpdateBy(operName);
                    this.updateSysOwnerProfile(owner);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + owner.getPhonenumber() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + owner.getPhonenumber() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + owner.getPhonenumber() + " 导入失败：";
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
}