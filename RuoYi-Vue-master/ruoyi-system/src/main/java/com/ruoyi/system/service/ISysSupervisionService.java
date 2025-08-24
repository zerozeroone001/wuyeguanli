
package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysSupervision;

/**
 * 指导监督Service接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface ISysSupervisionService 
{
    /**
     * 查询指导监督列表
     * 
     * @param sysSupervision 指导监督
     * @return 指导监督集合
     */
    public List<SysSupervision> selectSupervisionList(SysSupervision sysSupervision);
}
