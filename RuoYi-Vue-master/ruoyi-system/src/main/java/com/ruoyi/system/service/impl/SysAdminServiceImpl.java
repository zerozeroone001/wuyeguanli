package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysAdminMapper;
import com.ruoyi.system.domain.SysAdmin;
import com.ruoyi.system.service.ISysAdminService;

/**
 * 管理员Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-27
 */
@Service
public class SysAdminServiceImpl implements ISysAdminService 
{
    @Autowired
    private SysAdminMapper sysAdminMapper;

    /**
     * 查询管理员
     * 
     * @param adminId 管理员主键
     * @return 管理员
     */
    @Override
    public SysAdmin selectSysAdminByAdminId(Long adminId)
    {
        return sysAdminMapper.selectSysAdminByAdminId(adminId);
    }

    /**
     * 查询管理员列表
     * 
     * @param sysAdmin 管理员
     * @return 管理员
     */
    @Override
    public List<SysAdmin> selectSysAdminList(SysAdmin sysAdmin)
    {
        return sysAdminMapper.selectSysAdminList(sysAdmin);
    }

    /**
     * 新增管理员
     * 
     * @param sysAdmin 管理员
     * @return 结果
     */
    @Override
    public int insertSysAdmin(SysAdmin sysAdmin)
    {
        sysAdmin.setCreateTime(DateUtils.getNowDate());
        return sysAdminMapper.insertSysAdmin(sysAdmin);
    }

    /**
     * 修改管理员
     * 
     * @param sysAdmin 管理员
     * @return 结果
     */
    @Override
    public int updateSysAdmin(SysAdmin sysAdmin)
    {
        sysAdmin.setUpdateTime(DateUtils.getNowDate());
        return sysAdminMapper.updateSysAdmin(sysAdmin);
    }

    /**
     * 批量删除管理员
     * 
     * @param adminIds 需要删除的管理员主键
     * @return 结果
     */
    @Override
    public int deleteSysAdminByAdminIds(Long[] adminIds)
    {
        return sysAdminMapper.deleteSysAdminByAdminIds(adminIds);
    }

    /**
     * 删除管理员信息
     * 
     * @param adminId 管理员主键
     * @return 结果
     */
    @Override
    public int deleteSysAdminByAdminId(Long adminId)
    {
        return sysAdminMapper.deleteSysAdminByAdminId(adminId);
    }
}
