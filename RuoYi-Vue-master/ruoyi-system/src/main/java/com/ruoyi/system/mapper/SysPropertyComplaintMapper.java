package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SysPropertyComplaint;

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
    public Map<String, Object> getComplaintStats(@Param("communityId") Long communityId);


    /**
     * 批量删除投诉管理
     * 
     * @param complaintIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysPropertyComplaintByComplaintIds(Long[] complaintIds);

    /**
     * 统计待处理投诉数量
     * 
     * @return 待处理投诉数量
     */
    public Long countPendingComplaints(@Param("communityId") Long communityId);

    /**
     * 统计紧急投诉数量
     * 
     * @return 紧急投诉数量
     */
    public Long countUrgentComplaints(@Param("communityId") Long communityId);

    /**
     * 获取投诉增长率
     * 
     * @return 投诉增长率
     */
    public Double getComplaintGrowthRate(@Param("communityId") Long communityId);

    /**
     * 获取投诉趋势数据
     * 
     * @return 投诉趋势数据
     */
    public List<Map<String, Object>> getComplaintTrend(@Param("communityId") Long communityId);

    /**
     * 获取最近投诉记录
     * 
     * @param limit 限制数量
     * @return 最近投诉记录
     */
    public List<Map<String, Object>> getRecentComplaints(@Param("communityId") Long communityId, @Param("limit") int limit);

    /**
     * 按类型统计投诉
     * 
     * @return 按类型统计结果
     */
    public List<Map<String, Object>> getComplaintTypeStats(@Param("communityId") Long communityId);

    /**
     * 按状态统计投诉
     * 
     * @return 按状态统计结果
     */
    public List<Map<String, Object>> getComplaintStatusStats(@Param("communityId") Long communityId);
}
