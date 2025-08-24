
package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysSupervisionExpertMapper;
import com.ruoyi.system.domain.SysSupervisionExpert;
import com.ruoyi.system.service.ISysSupervisionExpertService;

/**
 * 指导监督专家Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
@Service
public class SysSupervisionExpertServiceImpl implements ISysSupervisionExpertService 
{
    @Autowired
    private SysSupervisionExpertMapper supervisionExpertMapper;

    /**
     * 查询指导监督专家列表
     * 
     * @param sysSupervisionExpert 指导监督专家
     * @return 指导监督专家
     */
    @Override
    public List<SysSupervisionExpert> selectSupervisionExpertList(SysSupervisionExpert sysSupervisionExpert)
    {
        return supervisionExpertMapper.selectSupervisionExpertList(sysSupervisionExpert);
    }
}
