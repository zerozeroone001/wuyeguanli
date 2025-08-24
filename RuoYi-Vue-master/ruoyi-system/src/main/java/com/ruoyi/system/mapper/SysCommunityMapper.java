
package com.ruoyi.system.mapper;

import java.util.Map;

/**
 * 小区信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface SysCommunityMapper 
{
    /**
     * 查询小区信息
     * 
     * @return 小区信息
     */
    public Map<String, Object> selectCommunityInfo();
}
