package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysNotaryApplicationMapper;
import com.ruoyi.system.domain.SysNotaryApplication;
import com.ruoyi.system.service.ISysNotaryApplicationService;

/**
 * 公证服务申请Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysNotaryApplicationServiceImpl implements ISysNotaryApplicationService 
{
    @Autowired
    private SysNotaryApplicationMapper sysNotaryApplicationMapper;

    /**
     * 查询公证服务申请
     * 
     * @param applicationId 公证服务申请主键
     * @return 公证服务申请
     */
    @Override
    public SysNotaryApplication selectSysNotaryApplicationByApplicationId(Long applicationId)
    {
        return sysNotaryApplicationMapper.selectSysNotaryApplicationByApplicationId(applicationId);
    }

    /**
     * 查询公证服务申请列表
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 公证服务申请
     */
    @Override
    public List<SysNotaryApplication> selectSysNotaryApplicationList(SysNotaryApplication sysNotaryApplication)
    {
        return sysNotaryApplicationMapper.selectSysNotaryApplicationList(sysNotaryApplication);
    }

    /**
     * 新增公证服务申请
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 结果
     */
    @Override
    public int insertSysNotaryApplication(SysNotaryApplication sysNotaryApplication)
    {
        sysNotaryApplication.setCreateTime(DateUtils.getNowDate());
        return sysNotaryApplicationMapper.insertSysNotaryApplication(sysNotaryApplication);
    }

    /**
     * 修改公证服务申请
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 结果
     */
    @Override
    public int updateSysNotaryApplication(SysNotaryApplication sysNotaryApplication)
    {
        sysNotaryApplication.setUpdateTime(DateUtils.getNowDate());
        return sysNotaryApplicationMapper.updateSysNotaryApplication(sysNotaryApplication);
    }

    /**
     * 批量删除公证服务申请
     * 
     * @param applicationIds 需要删除的公证服务申请主键
     * @return 结果
     */
    @Override
    public int deleteSysNotaryApplicationByApplicationIds(Long[] applicationIds)
    {
        return sysNotaryApplicationMapper.deleteSysNotaryApplicationByApplicationIds(applicationIds);
    }

    /**
     * 删除公证服务申请信息
     * 
     * @param applicationId 公证服务申请主键
     * @return 结果
     */
    @Override
    public int deleteSysNotaryApplicationByApplicationId(Long applicationId)
    {
        return sysNotaryApplicationMapper.deleteSysNotaryApplicationByApplicationId(applicationId);
    }
}
