package com.ruoyi.system.service.impl;

import java.util.List;
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

    /**
     * 查询资金流水
     * 
     * @param flowId 资金流水主键
     * @return 资金流水
     */
    @Override
    public SysPropertyFundFlow selectSysPropertyFundFlowByFlowId(Long flowId)
    {
        return sysPropertyFundFlowMapper.selectSysPropertyFundFlowByFlowId(flowId);
    }

    /**
     * 查询资金流水列表
     * 
     * @param sysPropertyFundFlow 资金流水
     * @return 资金流水
     */
    @Override
    public List<SysPropertyFundFlow> selectSysPropertyFundFlowList(SysPropertyFundFlow sysPropertyFundFlow)
    {
        return sysPropertyFundFlowMapper.selectSysPropertyFundFlowList(sysPropertyFundFlow);
    }

    /**
     * 新增资金流水
     * 
     * @param sysPropertyFundFlow 资金流水
     * @return 结果
     */
    @Override
    public int insertSysPropertyFundFlow(SysPropertyFundFlow sysPropertyFundFlow)
    {
        sysPropertyFundFlow.setCreateTime(DateUtils.getNowDate());
        return sysPropertyFundFlowMapper.insertSysPropertyFundFlow(sysPropertyFundFlow);
    }

    /**
     * 修改资金流水
     * 
     * @param sysPropertyFundFlow 资金流水
     * @return 结果
     */
    @Override
    public int updateSysPropertyFundFlow(SysPropertyFundFlow sysPropertyFundFlow)
    {
        sysPropertyFundFlow.setUpdateTime(DateUtils.getNowDate());
        return sysPropertyFundFlowMapper.updateSysPropertyFundFlow(sysPropertyFundFlow);
    }

    /**
     * 批量删除资金流水
     * 
     * @param flowIds 需要删除的资金流水主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyFundFlowByFlowIds(Long[] flowIds)
    {
        return sysPropertyFundFlowMapper.deleteSysPropertyFundFlowByFlowIds(flowIds);
    }

    /**
     * 删除资金流水信息
     * 
     * @param flowId 资金流水主键
     * @return 结果
     */
    @Override
    public int deleteSysPropertyFundFlowByFlowId(Long flowId)
    {
        return sysPropertyFundFlowMapper.deleteSysPropertyFundFlowByFlowId(flowId);
    }
}
