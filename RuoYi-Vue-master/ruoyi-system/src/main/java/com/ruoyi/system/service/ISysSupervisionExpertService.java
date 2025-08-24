
package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysSupervisionExpert;

/**
 * 指导监督专家Service接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface ISysSupervisionExpertService 
{
    /**
     * 查询指导监督专家列表
     * 
     * @param sysSupervisionExpert 指导监督专家
     * @return 指导监督专家集合
     */
    public List<SysSupervisionExpert> selectSupervisionExpertList(SysSupervisionExpert sysSupervisionExpert);
}
