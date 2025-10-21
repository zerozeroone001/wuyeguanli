package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysNotaryProcessLog;

/**
 * 公证流程记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-27
 */
public interface SysNotaryProcessLogMapper 
{
    /**
     * 查询公证流程记录
     * 
     * @param logId 公证流程记录主键
     * @return 公证流程记录
     */
    public SysNotaryProcessLog selectSysNotaryProcessLogByLogId(Long logId);

    /**
     * 查询公证流程记录列表
     * 
     * @param sysNotaryProcessLog 公证流程记录
     * @return 公证流程记录集合
     */
    public List<SysNotaryProcessLog> selectSysNotaryProcessLogList(SysNotaryProcessLog sysNotaryProcessLog);

    /**
     * 新增公证流程记录
     * 
     * @param sysNotaryProcessLog 公证流程记录
     * @return 结果
     */
    public int insertSysNotaryProcessLog(SysNotaryProcessLog sysNotaryProcessLog);

    /**
     * 修改公证流程记录
     * 
     * @param sysNotaryProcessLog 公证流程记录
     * @return 结果
     */
    public int updateSysNotaryProcessLog(SysNotaryProcessLog sysNotaryProcessLog);

    /**
     * 删除公证流程记录
     * 
     * @param logId 公证流程记录主键
     * @return 结果
     */
    public int deleteSysNotaryProcessLogByLogId(Long logId);

    /**
     * 批量删除公证流程记录
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNotaryProcessLogByLogIds(Long[] logIds);
}
