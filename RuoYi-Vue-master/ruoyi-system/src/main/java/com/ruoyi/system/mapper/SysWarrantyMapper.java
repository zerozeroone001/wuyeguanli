package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysWarranty;

/**
 * 设备保修Mapper接口
 *
 * @author ruoyi
 * @date 2025-08-25
 */
public interface SysWarrantyMapper {
    /**
     * 查询设备保修
     *
     * @param warrantyId 设备保修ID
     * @return 设备保修
     */
    public SysWarranty selectSysWarrantyById(Long warrantyId);

    /**
     * 查询设备保修列表
     *
     * @param sysWarranty 设备保修
     * @return 设备保修集合
     */
    public List<SysWarranty> selectSysWarrantyList(SysWarranty sysWarranty);

    /**
     * 新增设备保修
     *
     * @param sysWarranty 设备保修
     * @return 结果
     */
    public int insertSysWarranty(SysWarranty sysWarranty);

    /**
     * 修改设备保修
     *
     * @param sysWarranty 设备保修
     * @return 结果
     */
    public int updateSysWarranty(SysWarranty sysWarranty);

    /**
     * 删除设备保修
     *
     * @param warrantyId 设备保修ID
     * @return 结果
     */
    public int deleteSysWarrantyById(Long warrantyId);

    /**
     * 批量删除设备保修
     *
     * @param warrantyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysWarrantyByIds(String[] warrantyIds);
}
