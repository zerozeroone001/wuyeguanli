package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CommunityUtils;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysPropertyComplaintMapper;
import com.ruoyi.system.domain.SysPropertyComplaint;
import com.ruoyi.system.service.ISysPropertyComplaintService;

/**
 * 投诉管理Service业务层处理
 *
 * <p>针对多小区场景，增加小区范围校验和过滤逻辑，确保普通账号仅能访问自身绑定的小区数据。</p>
 */
@Service
public class SysPropertyComplaintServiceImpl implements ISysPropertyComplaintService
{
    @Autowired
    private SysPropertyComplaintMapper sysPropertyComplaintMapper;

    /**
     * 查询投诉管理
     */
    @Override
    public SysPropertyComplaint selectSysPropertyComplaintByComplaintId(Long complaintId)
    {
        SysPropertyComplaint complaint = sysPropertyComplaintMapper.selectSysPropertyComplaintByComplaintId(complaintId);
        if (complaint != null)
        {
            CommunityUtils.checkCommunityPermission(complaint.getCommunityId());
        }
        return complaint;
    }

    /**
     * 查询投诉管理列表
     */
    @Override
    public List<SysPropertyComplaint> selectSysPropertyComplaintList(SysPropertyComplaint sysPropertyComplaint)
    {
        return sysPropertyComplaintMapper.selectSysPropertyComplaintList(sysPropertyComplaint);
    }

    /**
     * 新增投诉管理
     */
    @Override
    @Transactional
    public int insertSysPropertyComplaint(SysPropertyComplaint sysPropertyComplaint)
    {
        enforceCommunityScope(sysPropertyComplaint.getCommunityId());
        sysPropertyComplaint.setCreateTime(DateUtils.getNowDate());
        return sysPropertyComplaintMapper.insertSysPropertyComplaint(sysPropertyComplaint);
    }

    /**
     * 修改投诉管理
     */
    @Override
    @Transactional
    public int updateSysPropertyComplaint(SysPropertyComplaint sysPropertyComplaint)
    {
        enforceCommunityScope(sysPropertyComplaint.getCommunityId());
        sysPropertyComplaint.setUpdateTime(DateUtils.getNowDate());
        return sysPropertyComplaintMapper.updateSysPropertyComplaint(sysPropertyComplaint);
    }

    /**
     * 批量删除投诉管理
     */
    @Override
    @Transactional
    public int deleteSysPropertyComplaintByComplaintIds(Long[] complaintIds)
    {
        for (Long complaintId : complaintIds)
        {
            SysPropertyComplaint complaint = sysPropertyComplaintMapper.selectSysPropertyComplaintByComplaintId(complaintId);
            if (complaint != null)
            {
                CommunityUtils.checkCommunityPermission(complaint.getCommunityId());
            }
        }
        return sysPropertyComplaintMapper.deleteSysPropertyComplaintByComplaintIds(complaintIds);
    }

    /**
     * 删除投诉管理信息
     */
    @Override
    @Transactional
    public int deleteSysPropertyComplaintByComplaintId(Long complaintId)
    {
        SysPropertyComplaint complaint = sysPropertyComplaintMapper.selectSysPropertyComplaintByComplaintId(complaintId);
        if (complaint != null)
        {
            CommunityUtils.checkCommunityPermission(complaint.getCommunityId());
        }
        return sysPropertyComplaintMapper.deleteSysPropertyComplaintByComplaintId(complaintId);
    }

    @Override
    public Map<String, Object> getComplaintStats()
    {
        return sysPropertyComplaintMapper.getComplaintStats(resolveCommunityIdForStatistics());
    }

    @Override
    public Long countPendingComplaints()
    {
        return sysPropertyComplaintMapper.countPendingComplaints(resolveCommunityIdForStatistics());
    }

    @Override
    public Long countUrgentComplaints()
    {
        return sysPropertyComplaintMapper.countUrgentComplaints(resolveCommunityIdForStatistics());
    }

    @Override
    public Double getComplaintGrowthRate()
    {
        return sysPropertyComplaintMapper.getComplaintGrowthRate(resolveCommunityIdForStatistics());
    }

    @Override
    public List<Map<String, Object>> getComplaintTrend()
    {
        return sysPropertyComplaintMapper.getComplaintTrend(resolveCommunityIdForStatistics());
    }

    @Override
    public List<Map<String, Object>> getRecentComplaints(int limit)
    {
        return sysPropertyComplaintMapper.getRecentComplaints(resolveCommunityIdForStatistics(), limit);
    }

    @Override
    public List<Map<String, Object>> getComplaintTypeStats()
    {
        return sysPropertyComplaintMapper.getComplaintTypeStats(resolveCommunityIdForStatistics());
    }

    @Override
    public List<Map<String, Object>> getComplaintStatusStats()
    {
        return sysPropertyComplaintMapper.getComplaintStatusStats(resolveCommunityIdForStatistics());
    }

    private void enforceCommunityScope(Long communityId)
    {
        if (communityId == null)
        {
            throw new ServiceException("投诉记录必须绑定所属小区");
        }
        CommunityUtils.checkCommunityPermission(communityId);
    }

    private Long resolveCommunityIdForStatistics()
    {
        if (CommunityUtils.isCurrentUserAdmin())
        {
            return CommunityUtils.getCurrentCommunityId();
        }
        return CommunityUtils.requireCurrentCommunityId("当前账号未绑定任何小区");
    }
}
