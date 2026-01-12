package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysOwnerTag;

/**
 * 业主标签Service接口
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public interface ISysOwnerTagService 
{
    /**
     * 查询业主标签
     * 
     * @param tagId 业主标签主键
     * @return 业主标签
     */
    public SysOwnerTag selectSysOwnerTagByTagId(Long tagId);

    /**
     * 查询业主标签列表
     * 
     * @param sysOwnerTag 业主标签
     * @return 业主标签集合
     */
    public List<SysOwnerTag> selectSysOwnerTagList(SysOwnerTag sysOwnerTag);

    /**
     * 新增业主标签
     * 
     * @param sysOwnerTag 业主标签
     * @return 结果
     */
    public int insertSysOwnerTag(SysOwnerTag sysOwnerTag);

    /**
     * 修改业主标签
     * 
     * @param sysOwnerTag 业主标签
     * @return 结果
     */
    public int updateSysOwnerTag(SysOwnerTag sysOwnerTag);

    /**
     * 批量删除业主标签
     * 
     * @param tagIds 需要删除的业主标签主键集合
     * @return 结果
     */
    public int deleteSysOwnerTagByTagIds(Long[] tagIds);

    /**
     * 删除业主标签信息
     * 
     * @param tagId 业主标签主键
     * @return 结果
     */
    public int deleteSysOwnerTagByTagId(Long tagId);
}
