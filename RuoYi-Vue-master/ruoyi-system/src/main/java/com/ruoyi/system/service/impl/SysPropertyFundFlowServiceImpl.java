package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CommunityUtils;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysPropertyFundFlowMapper;
import com.ruoyi.system.domain.SysPropertyFundFlow;
import com.ruoyi.system.service.ISysPropertyFundFlowService;

/**
 * 资金流水Service业务层处理
 *
 * <p>在原有逻辑基础上补充小区范围校验，确保除超级管理员外的账号只能访问自身小区的数据。</p>
 *
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysPropertyFundFlowServiceImpl implements ISysPropertyFundFlowService
{
    @Autowired
    private SysPropertyFundFlowMapper sysPropertyFundFlowMapper;

    @Override
    public SysPropertyFundFlow selectSysPropertyFundFlowByFlowId(Long flowId)
    {
        SysPropertyFundFlow flow = sysPropertyFundFlowMapper.selectSysPropertyFundFlowByFlowId(flowId);
        if (flow != null)
        {
            CommunityUtils.checkCommunityPermission(flow.getCommunityId());
        }
        return flow;
    }

    @Override
    public List<SysPropertyFundFlow> selectSysPropertyFundFlowList(SysPropertyFundFlow sysPropertyFundFlow)
    {
        return sysPropertyFundFlowMapper.selectSysPropertyFundFlowList(sysPropertyFundFlow);
    }

    @Override
    public int insertSysPropertyFundFlow(SysPropertyFundFlow sysPropertyFundFlow)
    {
        enforceCommunityScope(sysPropertyFundFlow.getCommunityId());
        // 自动生成流水编号 flowNo（前端无需手动提供）
        if (sysPropertyFundFlow.getFlowNo() == null || sysPropertyFundFlow.getFlowNo().isEmpty())
        {
            String timestamp = DateUtils.dateTimeNow("yyyyMMddHHmmss");
            int rand = (int) (Math.random() * 9000) + 1000;
            sysPropertyFundFlow.setFlowNo("FL" + timestamp + rand);
        }
        sysPropertyFundFlow.setCreateTime(DateUtils.getNowDate());
        return sysPropertyFundFlowMapper.insertSysPropertyFundFlow(sysPropertyFundFlow);
    }

    @Override
    public int updateSysPropertyFundFlow(SysPropertyFundFlow sysPropertyFundFlow)
    {
        enforceCommunityScope(sysPropertyFundFlow.getCommunityId());
        sysPropertyFundFlow.setUpdateTime(DateUtils.getNowDate());
        return sysPropertyFundFlowMapper.updateSysPropertyFundFlow(sysPropertyFundFlow);
    }

    @Override
    public int deleteSysPropertyFundFlowByFlowIds(Long[] flowIds)
    {
        for (Long flowId : flowIds)
        {
            SysPropertyFundFlow flow = sysPropertyFundFlowMapper.selectSysPropertyFundFlowByFlowId(flowId);
            if (flow != null)
            {
                CommunityUtils.checkCommunityPermission(flow.getCommunityId());
            }
        }
        return sysPropertyFundFlowMapper.deleteSysPropertyFundFlowByFlowIds(flowIds);
    }

    @Override
    public int deleteSysPropertyFundFlowByFlowId(Long flowId)
    {
        SysPropertyFundFlow flow = sysPropertyFundFlowMapper.selectSysPropertyFundFlowByFlowId(flowId);
        if (flow != null)
        {
            CommunityUtils.checkCommunityPermission(flow.getCommunityId());
        }
        return sysPropertyFundFlowMapper.deleteSysPropertyFundFlowByFlowId(flowId);
    }

    @Override
    public BigDecimal getTotalFunds()
    {
        return null;
    }

    @Override
    public Double getFundGrowthRate()
    {
        return 0.0;
    }

    @Override
    public Long countPendingApprovals()
    {
        return 0L;
    }

    @Override
    public Map<String, Object> getMonthFundAnalysis()
    {
        return Collections.emptyMap();
    }

    @Override
    public List<Map<String, Object>> getRecentFunds(int limit)
    {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getFundOverview()
    {
        return sysPropertyFundFlowMapper.getFundOverview(resolveCommunityIdForStatistics());
    }

    @Override
    public Map<String, Object> getMonthlyStats()
    {
        return sysPropertyFundFlowMapper.getMonthlyStats(resolveCommunityIdForStatistics());
    }

    /**
     * 校验小区ID的有效性，并确认当前账号具备访问权限。
     */
    private void enforceCommunityScope(Long communityId)
    {
        if (communityId == null)
        {
            throw new ServiceException("资金流水必须绑定所属小区");
        }
        CommunityUtils.checkCommunityPermission(communityId);
    }

    /**
     * 统计类场景统一解析小区ID：超级管理员允许不选，普通用户必须绑定。
     */
    private Long resolveCommunityIdForStatistics()
    {
        if (CommunityUtils.isCurrentUserAdmin())
        {
            return CommunityUtils.getCurrentCommunityId();
        }
        return CommunityUtils.requireCurrentCommunityId("当前账号未绑定任何小区");
    }
}
