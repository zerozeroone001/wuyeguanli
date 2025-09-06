package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.EstateCommunity;

/**
 * 小区信息Service接口
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
public interface IEstateCommunityService 
{
    /**
     * 查询小区信息
     * 
     * @param communityId 小区信息主键
     * @return 小区信息
     */
    public EstateCommunity selectEstateCommunityByCommunityId(Long communityId);

    /**
     * 查询小区信息列表
     * 
     * @param estateCommunity 小区信息
     * @return 小区信息集合
     */
    public List<EstateCommunity> selectEstateCommunityList(EstateCommunity estateCommunity);

    /**
     * 新增小区信息
     * 
     * @param estateCommunity 小区信息
     * @return 结果
     */
    public int insertEstateCommunity(EstateCommunity estateCommunity);

    /**
     * 修改小区信息
     * 
     * @param estateCommunity 小区信息
     * @return 结果
     */
    public int updateEstateCommunity(EstateCommunity estateCommunity);

    /**
     * 批量删除小区信息
     * 
     * @param communityIds 需要删除的小区信息主键集合
     * @return 结果
     */
    public int deleteEstateCommunityByCommunityIds(Long[] communityIds);

    /**
     * 删除小区信息信息
     * 
     * @param communityId 小区信息主键
     * @return 结果
     */
    public int deleteEstateCommunityByCommunityId(Long communityId);
}
