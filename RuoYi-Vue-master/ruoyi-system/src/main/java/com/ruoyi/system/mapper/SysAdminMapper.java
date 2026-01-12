package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysAdmin;

/**
 * 管理员Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-27
 */
public interface SysAdminMapper 
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
     * 删除管理员
     * 
     * @param adminId 管理员主键
     * @return 结果
     */
    public int deleteSysAdminByAdminId(Long adminId);

    /**
     * 批量删除管理员
     * 
     * @param adminIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAdminByAdminIds(Long[] adminIds);
}
