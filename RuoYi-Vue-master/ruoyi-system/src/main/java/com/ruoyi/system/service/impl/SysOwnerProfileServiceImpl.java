package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysOwnerProfileMapper;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.service.ISysOwnerProfileService;

/**
 * 业主信息扩展Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysOwnerProfileServiceImpl implements ISysOwnerProfileService 
{
    @Autowired
    private SysOwnerProfileMapper sysOwnerProfileMapper;

    /**
     * 查询业主信息扩展
     * 
     * @param userId 业主信息扩展主键
     * @return 业主信息扩展
     */
    @Override
    public SysOwnerProfile selectSysOwnerProfileByUserId(Long userId)
    {
        return sysOwnerProfileMapper.selectSysOwnerProfileByUserId(userId);
    }

    /**
     * 查询业主信息扩展列表
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 业主信息扩展
     */
    @Override
    public List<SysOwnerProfile> selectSysOwnerProfileList(SysOwnerProfile sysOwnerProfile)
    {
        return sysOwnerProfileMapper.selectSysOwnerProfileList(sysOwnerProfile);
    }

    /**
     * 新增业主信息扩展
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 结果
     */
    @Override
    public int insertSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        sysOwnerProfile.setCreateTime(DateUtils.getNowDate());
        return sysOwnerProfileMapper.insertSysOwnerProfile(sysOwnerProfile);
    }

    /**
     * 修改业主信息扩展
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 结果
     */
    @Override
    public int updateSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        sysOwnerProfile.setUpdateTime(DateUtils.getNowDate());
        return sysOwnerProfileMapper.updateSysOwnerProfile(sysOwnerProfile);
    }

    /**
     * 批量删除业主信息扩展
     * 
     * @param userIds 需要删除的业主信息扩展主键
     * @return 结果
     */
    @Override
    public int deleteSysOwnerProfileByUserIds(Long[] userIds)
    {
        return sysOwnerProfileMapper.deleteSysOwnerProfileByUserIds(userIds);
    }

    /**
     * 删除业主信息扩展信息
     * 
     * @param userId 业主信息扩展主键
     * @return 结果
     */
    @Override
    public int deleteSysOwnerProfileByUserId(Long userId)
    {
        return sysOwnerProfileMapper.deleteSysOwnerProfileByUserId(userId);
    }
}
