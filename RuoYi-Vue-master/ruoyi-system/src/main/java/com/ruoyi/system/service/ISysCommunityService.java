
package com.ruoyi.system.service;

import java.util.Map;

/**
 * 小区信息Service接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface ISysCommunityService 
{
    /**
     * 查询小区信息
     * 
     * @return 小区信息
     */
    public Map<String, Object> selectCommunityInfo();
}
