
package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysSupervisionMapper;
import com.ruoyi.system.domain.SysSupervision;
import com.ruoyi.system.service.ISysSupervisionService;

/**
 * 指导监督Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
@Service
public class SysSupervisionServiceImpl implements ISysSupervisionService 
{
    @Autowired
    private SysSupervisionMapper supervisionMapper;

    /**
     * 查询指导监督列表
     * 
     * @param sysSupervision 指导监督
     * @return 指导监督
     */
    @Override
    public List<SysSupervision> selectSupervisionList(SysSupervision sysSupervision)
    {
        return supervisionMapper.selectSupervisionList(sysSupervision);
    }
}
