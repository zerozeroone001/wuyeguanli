package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysPropertyFundFlowMapper;
import com.ruoyi.system.domain.SysPropertyFundFlow;
import com.ruoyi.system.service.ISysPropertyFundFlowService;

/**
 * 资金流水Service业务层处理
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
        return sysPropertyFundFlowMapper.selectSysPropertyFundFlowByFlowId(flowId);
    }

    @Override
    public List<SysPropertyFundFlow> selectSysPropertyFundFlowList(SysPropertyFundFlow sysPropertyFundFlow)
    {
        return sysPropertyFundFlowMapper.selectSysPropertyFundFlowList(sysPropertyFundFlow);
    }

    @Override
    public int insertSysPropertyFundFlow(SysPropertyFundFlow sysPropertyFundFlow)
    {
        sysPropertyFundFlow.setCreateTime(DateUtils.getNowDate());
        return sysPropertyFundFlowMapper.insertSysPropertyFundFlow(sysPropertyFundFlow);
    }

    @Override
    public int updateSysPropertyFundFlow(SysPropertyFundFlow sysPropertyFundFlow)
    {
        sysPropertyFundFlow.setUpdateTime(DateUtils.getNowDate());
        return sysPropertyFundFlowMapper.updateSysPropertyFundFlow(sysPropertyFundFlow);
    }

    @Override
    public int deleteSysPropertyFundFlowByFlowIds(Long[] flowIds)
    {
        return sysPropertyFundFlowMapper.deleteSysPropertyFundFlowByFlowIds(flowIds);
    }

    @Override
    public int deleteSysPropertyFundFlowByFlowId(Long flowId)
    {
        return sysPropertyFundFlowMapper.deleteSysPropertyFundFlowByFlowId(flowId);
    }

    @Override
    public Map<String, Object> getFundOverview() {
        return sysPropertyFundFlowMapper.getFundOverview();
    }

    @Override
    public Map<String, Object> getMonthlyStats() {
        return sysPropertyFundFlowMapper.getMonthlyStats();
    }
}