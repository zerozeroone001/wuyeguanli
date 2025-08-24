
package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysSupervisionFaq;

/**
 * 指导监督FAQService接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface ISysSupervisionFaqService 
{
    /**
     * 查询指导监督FAQ列表
     * 
     * @param sysSupervisionFaq 指导监督FAQ
     * @return 指导监督FAQ集合
     */
    public List<SysSupervisionFaq> selectSupervisionFaqList(SysSupervisionFaq sysSupervisionFaq);
}
