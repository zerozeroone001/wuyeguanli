package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysContractRectification;

/**
 * 整改记录Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-05
 */
public interface SysContractRectificationMapper
{
    public SysContractRectification selectSysContractRectificationByRectificationId(Long rectificationId);

    public List<SysContractRectification> selectSysContractRectificationList(SysContractRectification sysContractRectification);

    public List<SysContractRectification> selectByContractId(Long contractId);

    public int insertSysContractRectification(SysContractRectification sysContractRectification);

    public int updateSysContractRectification(SysContractRectification sysContractRectification);

    public int deleteSysContractRectificationByRectificationId(Long rectificationId);

    public int deleteSysContractRectificationByRectificationIds(Long[] rectificationIds);
}
