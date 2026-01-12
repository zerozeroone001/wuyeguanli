package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysOwnerTagMapper;
import com.ruoyi.system.domain.SysOwnerTag;
import com.ruoyi.system.service.ISysOwnerTagService;

/**
 * 业主标签Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@Service
public class SysOwnerTagServiceImpl implements ISysOwnerTagService 
{
    @Autowired
    private SysOwnerTagMapper sysOwnerTagMapper;

    /**
     * 查询业主标签
     * 
     * @param tagId 业主标签主键
     * @return 业主标签
     */
    @Override
    public SysOwnerTag selectSysOwnerTagByTagId(Long tagId)
    {
        return sysOwnerTagMapper.selectSysOwnerTagByTagId(tagId);
    }

    /**
     * 查询业主标签列表
     * 
     * @param sysOwnerTag 业主标签
     * @return 业主标签
     */
    @Override
    public List<SysOwnerTag> selectSysOwnerTagList(SysOwnerTag sysOwnerTag)
    {
        return sysOwnerTagMapper.selectSysOwnerTagList(sysOwnerTag);
    }

    /**
     * 新增业主标签
     * 
     * @param sysOwnerTag 业主标签
     * @return 结果
     */
    @Override
    public int insertSysOwnerTag(SysOwnerTag sysOwnerTag)
    {
        sysOwnerTag.setCreateTime(DateUtils.getNowDate());
        return sysOwnerTagMapper.insertSysOwnerTag(sysOwnerTag);
    }

    /**
     * 修改业主标签
     * 
     * @param sysOwnerTag 业主标签
     * @return 结果
     */
    @Override
    public int updateSysOwnerTag(SysOwnerTag sysOwnerTag)
    {
        sysOwnerTag.setUpdateTime(DateUtils.getNowDate());
        return sysOwnerTagMapper.updateSysOwnerTag(sysOwnerTag);
    }

    /**
     * 批量删除业主标签
     * 
     * @param tagIds 需要删除的业主标签主键
     * @return 结果
     */
    @Override
    public int deleteSysOwnerTagByTagIds(Long[] tagIds)
    {
        return sysOwnerTagMapper.deleteSysOwnerTagByTagIds(tagIds);
    }

    /**
     * 删除业主标签信息
     * 
     * @param tagId 业主标签主键
     * @return 结果
     */
    @Override
    public int deleteSysOwnerTagByTagId(Long tagId)
    {
        return sysOwnerTagMapper.deleteSysOwnerTagByTagId(tagId);
    }
}
