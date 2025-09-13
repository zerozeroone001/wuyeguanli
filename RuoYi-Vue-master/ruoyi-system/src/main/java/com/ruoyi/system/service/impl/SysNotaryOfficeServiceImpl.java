package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysNotaryOfficeMapper;
import com.ruoyi.system.domain.SysNotaryOffice;
import com.ruoyi.system.service.ISysNotaryOfficeService;

/**
 * 公证处信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-11
 */
@Service
public class SysNotaryOfficeServiceImpl implements ISysNotaryOfficeService 
{
    @Autowired
    private SysNotaryOfficeMapper sysNotaryOfficeMapper;

    /**
     * 查询公证处信息
     * 
     * @param officeId 公证处信息主键
     * @return 公证处信息
     */
    @Override
    public SysNotaryOffice selectSysNotaryOfficeByOfficeId(Long officeId)
    {
        return sysNotaryOfficeMapper.selectSysNotaryOfficeByOfficeId(officeId);
    }

    /**
     * 查询公证处信息列表
     * 
     * @param sysNotaryOffice 公证处信息
     * @return 公证处信息
     */
    @Override
    public List<SysNotaryOffice> selectSysNotaryOfficeList(SysNotaryOffice sysNotaryOffice)
    {
        return sysNotaryOfficeMapper.selectSysNotaryOfficeList(sysNotaryOffice);
    }

    /**
     * 新增公证处信息
     * 
     * @param sysNotaryOffice 公证处信息
     * @return 结果
     */
    @Override
    public int insertSysNotaryOffice(SysNotaryOffice sysNotaryOffice)
    {
        sysNotaryOffice.setCreateTime(DateUtils.getNowDate());
        return sysNotaryOfficeMapper.insertSysNotaryOffice(sysNotaryOffice);
    }

    /**
     * 修改公证处信息
     * 
     * @param sysNotaryOffice 公证处信息
     * @return 结果
     */
    @Override
    public int updateSysNotaryOffice(SysNotaryOffice sysNotaryOffice)
    {
        sysNotaryOffice.setUpdateTime(DateUtils.getNowDate());
        return sysNotaryOfficeMapper.updateSysNotaryOffice(sysNotaryOffice);
    }

    /**
     * 批量删除公证处信息
     * 
     * @param officeIds 需要删除的公证处信息主键
     * @return 结果
     */
    @Override
    public int deleteSysNotaryOfficeByOfficeIds(Long[] officeIds)
    {
        return sysNotaryOfficeMapper.deleteSysNotaryOfficeByOfficeIds(officeIds);
    }

    /**
     * 删除公证处信息信息
     * 
     * @param officeId 公证处信息主键
     * @return 结果
     */
    @Override
    public int deleteSysNotaryOfficeByOfficeId(Long officeId)
    {
        return sysNotaryOfficeMapper.deleteSysNotaryOfficeByOfficeId(officeId);
    }
}
