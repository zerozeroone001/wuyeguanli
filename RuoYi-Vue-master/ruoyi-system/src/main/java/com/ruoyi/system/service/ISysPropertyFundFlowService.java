package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysPropertyFundFlow;

/**
 * 资金流水Service接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface ISysPropertyFundFlowService 
{
    /**
     * 查询资金流水
     * 
     * @param flowId 资金流水主键
     * @return 资金流水
     */
    public SysPropertyFundFlow selectSysPropertyFundFlowByFlowId(Long flowId);

    /**
     * 查询资金流水列表
     * 
     * @param sysPropertyFundFlow 资金流水
     * @return 资金流水集合
     */
    public List<SysPropertyFundFlow> selectSysPropertyFundFlowList(SysPropertyFundFlow sysPropertyFundFlow);

    /**
     * 新增资金流水
     * 
     * @param sysPropertyFundFlow 资金流水
     * @return 结果
     */
    public int insertSysPropertyFundFlow(SysPropertyFundFlow sysPropertyFundFlow);

    /**
     * 修改资金流水
     * 
     * @param sysPropertyFundFlow 资金流水
     * @return 结果
     */
    public int updateSysPropertyFundFlow(SysPropertyFundFlow sysPropertyFundFlow);

    /**
     * 批量删除资金流水
     * 
     * @param flowIds 需要删除的资金流水主键集合
     * @return 结果
     */
    public int deleteSysPropertyFundFlowByFlowIds(Long[] flowIds);

    /**
     * 删除资金流水信息
     * 
     * @param flowId 资金流水主键
     * @return 结果
     */
    public int deleteSysPropertyFundFlowByFlowId(Long flowId);
}
