package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysNotaryOffice;

/**
 * 公证处信息Service接口
 * 
 * @author ruoyi
 * @date 2025-09-11
 */
public interface ISysNotaryOfficeService 
{
    /**
     * 查询公证处信息
     * 
     * @param officeId 公证处信息主键
     * @return 公证处信息
     */
    public SysNotaryOffice selectSysNotaryOfficeByOfficeId(Long officeId);

    /**
     * 查询公证处信息列表
     * 
     * @param sysNotaryOffice 公证处信息
     * @return 公证处信息集合
     */
    public List<SysNotaryOffice> selectSysNotaryOfficeList(SysNotaryOffice sysNotaryOffice);

    /**
     * 新增公证处信息
     * 
     * @param sysNotaryOffice 公证处信息
     * @return 结果
     */
    public int insertSysNotaryOffice(SysNotaryOffice sysNotaryOffice);

    /**
     * 修改公证处信息
     * 
     * @param sysNotaryOffice 公证处信息
     * @return 结果
     */
    public int updateSysNotaryOffice(SysNotaryOffice sysNotaryOffice);

    /**
     * 批量删除公证处信息
     * 
     * @param officeIds 需要删除的公证处信息主键集合
     * @return 结果
     */
    public int deleteSysNotaryOfficeByOfficeIds(Long[] officeIds);

    /**
     * 删除公证处信息信息
     * 
     * @param officeId 公证处信息主键
     * @return 结果
     */
    public int deleteSysNotaryOfficeByOfficeId(Long officeId);
}
