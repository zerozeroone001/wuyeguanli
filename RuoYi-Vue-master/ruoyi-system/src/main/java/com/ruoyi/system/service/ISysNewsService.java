
package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysNews;

/**
 * 新闻Service接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface ISysNewsService 
{
    /**
     * 查询新闻列表
     * 
     * @param sysNews 新闻
     * @return 新闻集合
     */
    public List<SysNews> selectNewsList(SysNews sysNews);
}
