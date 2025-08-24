
package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysNewsMapper;
import com.ruoyi.system.domain.SysNews;
import com.ruoyi.system.service.ISysNewsService;

/**
 * 新闻Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
@Service
public class SysNewsServiceImpl implements ISysNewsService 
{
    @Autowired
    private SysNewsMapper newsMapper;

    /**
     * 查询新闻列表
     * 
     * @param sysNews 新闻
     * @return 新闻
     */
    @Override
    public List<SysNews> selectNewsList(SysNews sysNews)
    {
        return newsMapper.selectNewsList(sysNews);
    }
}
