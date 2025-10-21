package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysPropertyFundFlow;

/**
 * 资金流水Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface SysPropertyFundFlowMapper 
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
     * 删除资金流水
     * 
     * @param flowId 资金流水主键
     * @return 结果
     */
    public int deleteSysPropertyFundFlowByFlowId(Long flowId);

    /**
     * 批量删除资金流水
     * 
     * @param flowIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysPropertyFundFlowByFlowIds(Long[] flowIds);

    /**
     * 获取资金概览
     *
     * @return 资金概览数据
     */
    public java.util.Map<String, Object> getFundOverview(Long communityId);

    /**
     * 获取月度收支统计
     *
     * @return 月度收支统计数据
     */
    public java.util.Map<String, Object> getMonthlyStats(Long communityId);
}
