package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SysPropertyComplaint;
import org.apache.ibatis.annotations.Mapper;

/**
 * 投诉管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Mapper
public interface SysPropertyComplaintMapper 
{
    /**
     * 查询投诉管理
     * 
     * @param complaintId 投诉管理主键
     * @return 投诉管理
     */
    public SysPropertyComplaint selectSysPropertyComplaintByComplaintId(Long complaintId);

    /**
     * 查询投诉管理列表
     * 
     * @param sysPropertyComplaint 投诉管理
     * @return 投诉管理集合
     */
    public List<SysPropertyComplaint> selectSysPropertyComplaintList(SysPropertyComplaint sysPropertyComplaint);

    /**
     * 新增投诉管理
     * 
     * @param sysPropertyComplaint 投诉管理
     * @return 结果
     */
    public int insertSysPropertyComplaint(SysPropertyComplaint sysPropertyComplaint);

    /**
     * 修改投诉管理
     * 
     * @param sysPropertyComplaint 投诉管理
     * @return 结果
     */
    public int updateSysPropertyComplaint(SysPropertyComplaint sysPropertyComplaint);

    /**
     * 删除投诉管理信息
     * 
     * @param complaintId 投诉管理主键
     * @return 结果
     */
    public int deleteSysPropertyComplaintByComplaintId(Long complaintId);

    /**
     * 获取投诉统计信息
     *
     * @return 结果
     */
    public Map<String, Object> getComplaintStats();


    /**
     * 批量删除投诉管理
     * 
     * @param complaintIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysPropertyComplaintByComplaintIds(Long[] complaintIds);
}
