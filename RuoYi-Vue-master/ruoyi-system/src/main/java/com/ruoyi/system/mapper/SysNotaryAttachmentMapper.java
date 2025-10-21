package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysNotaryAttachment;

/**
 * 公证附件Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-27
 */
public interface SysNotaryAttachmentMapper 
{
    /**
     * 查询公证附件
     * 
     * @param attachmentId 公证附件主键
     * @return 公证附件
     */
    public SysNotaryAttachment selectSysNotaryAttachmentByAttachmentId(Long attachmentId);

    /**
     * 查询公证附件列表
     * 
     * @param sysNotaryAttachment 公证附件
     * @return 公证附件集合
     */
    public List<SysNotaryAttachment> selectSysNotaryAttachmentList(SysNotaryAttachment sysNotaryAttachment);

    /**
     * 新增公证附件
     * 
     * @param sysNotaryAttachment 公证附件
     * @return 结果
     */
    public int insertSysNotaryAttachment(SysNotaryAttachment sysNotaryAttachment);

    /**
     * 修改公证附件
     * 
     * @param sysNotaryAttachment 公证附件
     * @return 结果
     */
    public int updateSysNotaryAttachment(SysNotaryAttachment sysNotaryAttachment);

    /**
     * 删除公证附件
     * 
     * @param attachmentId 公证附件主键
     * @return 结果
     */
    public int deleteSysNotaryAttachmentByAttachmentId(Long attachmentId);

    /**
     * 批量删除公证附件
     * 
     * @param attachmentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNotaryAttachmentByAttachmentIds(Long[] attachmentIds);
}