package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.EstateCommunityMapper;
import com.ruoyi.system.domain.EstateCommunity;
import com.ruoyi.system.service.IEstateCommunityService;

/**
 * 小区信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
@Service
public class EstateCommunityServiceImpl implements IEstateCommunityService 
{
    @Autowired
    private EstateCommunityMapper estateCommunityMapper;

    /**
     * 查询小区信息
     * 
     * @param communityId 小区信息主键
     * @return 小区信息
     */
    @Override
    public EstateCommunity selectEstateCommunityByCommunityId(Long communityId)
    {
        return estateCommunityMapper.selectEstateCommunityByCommunityId(communityId);
    }

    /**
     * 查询小区信息列表
     * 
     * @param estateCommunity 小区信息
     * @return 小区信息
     */
    @Override
    public List<EstateCommunity> selectEstateCommunityList(EstateCommunity estateCommunity)
    {
        return estateCommunityMapper.selectEstateCommunityList(estateCommunity);
    }

    /**
     * 新增小区信息
     * 
     * @param estateCommunity 小区信息
     * @return 结果
     */
    @Override
    public int insertEstateCommunity(EstateCommunity estateCommunity)
    {
        estateCommunity.setCreateTime(DateUtils.getNowDate());
        return estateCommunityMapper.insertEstateCommunity(estateCommunity);
    }

    /**
     * 修改小区信息
     * 
     * @param estateCommunity 小区信息
     * @return 结果
     */
    @Override
    public int updateEstateCommunity(EstateCommunity estateCommunity)
    {
        estateCommunity.setUpdateTime(DateUtils.getNowDate());
        return estateCommunityMapper.updateEstateCommunity(estateCommunity);
    }

    /**
     * 批量删除小区信息
     * 
     * @param communityIds 需要删除的小区信息主键
     * @return 结果
     */
    @Override
    public int deleteEstateCommunityByCommunityIds(Long[] communityIds)
    {
        return estateCommunityMapper.deleteEstateCommunityByCommunityIds(communityIds);
    }

    /**
     * 删除小区信息信息
     * 
     * @param communityId 小区信息主键
     * @return 结果
     */
    @Override
    public int deleteEstateCommunityByCommunityId(Long communityId)
    {
        return estateCommunityMapper.deleteEstateCommunityByCommunityId(communityId);
    }
}
