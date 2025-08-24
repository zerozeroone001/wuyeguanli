
package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysSupervisionFaqMapper;
import com.ruoyi.system.domain.SysSupervisionFaq;
import com.ruoyi.system.service.ISysSupervisionFaqService;

/**
 * 指导监督FAQService业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
@Service
public class SysSupervisionFaqServiceImpl implements ISysSupervisionFaqService 
{
    @Autowired
    private SysSupervisionFaqMapper supervisionFaqMapper;

    /**
     * 查询指导监督FAQ列表
     * 
     * @param sysSupervisionFaq 指导监督FAQ
     * @return 指导监督FAQ
     */
    @Override
    public List<SysSupervisionFaq> selectSupervisionFaqList(SysSupervisionFaq sysSupervisionFaq)
    {
        return supervisionFaqMapper.selectSupervisionFaqList(sysSupervisionFaq);
    }
}
