package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysAdmin;

/**
 * 管理员Service接口
 * 
 * @author ruoyi
 * @date 2025-12-27
 */
public interface ISysAdminService 
{
    /**
     * 查询管理员
     * 
     * @param adminId 管理员主键
     * @return 管理员
     */
    public SysAdmin selectSysAdminByAdminId(Long adminId);

    /**
     * 查询管理员列表
     * 
     * @param sysAdmin 管理员
     * @return 管理员集合
     */
    public List<SysAdmin> selectSysAdminList(SysAdmin sysAdmin);

    /**
     * 新增管理员
     * 
     * @param sysAdmin 管理员
     * @return 结果
     */
    public int insertSysAdmin(SysAdmin sysAdmin);

    /**
     * 修改管理员
     * 
     * @param sysAdmin 管理员
     * @return 结果
     */
    public int updateSysAdmin(SysAdmin sysAdmin);

    /**
     * 批量删除管理员
     * 
     * @param adminIds 需要删除的管理员主键集合
     * @return 结果
     */
    public int deleteSysAdminByAdminIds(Long[] adminIds);

    /**
     * 删除管理员信息
     * 
     * @param adminId 管理员主键
     * @return 结果
     */
    public int deleteSysAdminByAdminId(Long adminId);
}
