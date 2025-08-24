
package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysHandoverMapper;
import com.ruoyi.system.domain.SysHandover;
import com.ruoyi.system.service.ISysHandoverService;

/**
 * 承接查验Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
@Service
public class SysHandoverServiceImpl implements ISysHandoverService 
{
    @Autowired
    private SysHandoverMapper handoverMapper;

    /**
     * 查询承接查验列表
     * 
     * @param sysHandover 承接查验
     * @return 承接查验
     */
    @Override
    public List<SysHandover> selectHandoverList(SysHandover sysHandover)
    {
        return handoverMapper.selectHandoverList(sysHandover);
    }
}
