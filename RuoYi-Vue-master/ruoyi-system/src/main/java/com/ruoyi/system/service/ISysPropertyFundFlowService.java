package com.ruoyi.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

    /**
     * 获取资金总额
     * 
     * @return 资金总额
     */
    public BigDecimal getTotalFunds();

    /**
     * 获取资金增长率
     * 
     * @return 资金增长率
     */
    public Double getFundGrowthRate();

    /**
     * 统计待审批申请数量
     * 
     * @return 待审批申请数量
     */
    public Long countPendingApprovals();

    /**
     * 获取本月资金分析
     * 
     * @return 本月资金分析数据
     */
    public Map<String, Object> getMonthFundAnalysis();

    /**
     * 获取最近资金流水记录
     * 
     * @param limit 限制数量
     * @return 最近资金流水记录
     */
    public List<Map<String, Object>> getRecentFunds(int limit);
}
