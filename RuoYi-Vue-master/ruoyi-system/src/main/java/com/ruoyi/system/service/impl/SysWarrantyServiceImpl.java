package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysWarrantyMapper;
import com.ruoyi.system.domain.SysWarranty;
import com.ruoyi.system.service.ISysWarrantyService;
import com.ruoyi.common.core.text.Convert;

/**
 * 设备保修Service业务层处理
 *
 * @author ruoyi
 * @date 2025-08-25
 */
@Service
public class SysWarrantyServiceImpl implements ISysWarrantyService {
    @Autowired
    private SysWarrantyMapper sysWarrantyMapper;

    /**
     * 查询设备保修
     *
     * @param warrantyId 设备保修ID
     * @return 设备保修
     */
    @Override
    public SysWarranty selectSysWarrantyById(Long warrantyId) {
        return sysWarrantyMapper.selectSysWarrantyById(warrantyId);
    }

    /**
     * 查询设备保修列表
     *
     * @param sysWarranty 设备保修
     * @return 设备保修
     */
    @Override
    public List<SysWarranty> selectSysWarrantyList(SysWarranty sysWarranty) {
        return sysWarrantyMapper.selectSysWarrantyList(sysWarranty);
    }

    /**
     * 新增设备保修
     *
     * @param sysWarranty 设备保修
     * @return 结果
     */
    @Override
    public int insertSysWarranty(SysWarranty sysWarranty) {
        sysWarranty.setCreateTime(DateUtils.getNowDate());
        return sysWarrantyMapper.insertSysWarranty(sysWarranty);
    }

    /**
     * 修改设备保修
     *
     * @param sysWarranty 设备保修
     * @return 结果
     */
    @Override
    public int updateSysWarranty(SysWarranty sysWarranty) {
        sysWarranty.setUpdateTime(DateUtils.getNowDate());
        return sysWarrantyMapper.updateSysWarranty(sysWarranty);
    }

    /**
     * 删除设备保修对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysWarrantyByIds(String ids) {
        return sysWarrantyMapper.deleteSysWarrantyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除设备保修信息
     *
     * @param warrantyId 设备保修ID
     * @return 结果
     */
    @Override
    public int deleteSysWarrantyById(Long warrantyId) {
        return sysWarrantyMapper.deleteSysWarrantyById(warrantyId);
    }
}
