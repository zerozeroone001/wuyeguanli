
package com.ruoyi.system.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysCommunityMapper;
import com.ruoyi.system.service.ISysCommunityService;

/**
 * 小区信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
@Service
public class SysCommunityServiceImpl implements ISysCommunityService 
{
    @Autowired
    private SysCommunityMapper communityMapper;

    /**
     * 查询小区信息
     * 
     * @return 小区信息
     */
    @Override
    public Map<String, Object> selectCommunityInfo()
    {
        return communityMapper.selectCommunityInfo();
    }
}
