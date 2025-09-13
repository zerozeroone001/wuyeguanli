package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysNotaryAttachmentMapper;
import com.ruoyi.system.domain.SysNotaryAttachment;
import com.ruoyi.system.service.ISysNotaryAttachmentService;

/**
 * 公证附件Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-11
 */
@Service
public class SysNotaryAttachmentServiceImpl implements ISysNotaryAttachmentService 
{
    @Autowired
    private SysNotaryAttachmentMapper sysNotaryAttachmentMapper;

    /**
     * 查询公证附件
     * 
     * @param attachmentId 公证附件主键
     * @return 公证附件
     */
    @Override
    public SysNotaryAttachment selectSysNotaryAttachmentByAttachmentId(Long attachmentId)
    {
        return sysNotaryAttachmentMapper.selectSysNotaryAttachmentByAttachmentId(attachmentId);
    }

    /**
     * 查询公证附件列表
     * 
     * @param sysNotaryAttachment 公证附件
     * @return 公证附件
     */
    @Override
    public List<SysNotaryAttachment> selectSysNotaryAttachmentList(SysNotaryAttachment sysNotaryAttachment)
    {
        return sysNotaryAttachmentMapper.selectSysNotaryAttachmentList(sysNotaryAttachment);
    }

    /**
     * 新增公证附件
     * 
     * @param sysNotaryAttachment 公证附件
     * @return 结果
     */
    @Override
    public int insertSysNotaryAttachment(SysNotaryAttachment sysNotaryAttachment)
    {
        sysNotaryAttachment.setCreateTime(DateUtils.getNowDate());
        return sysNotaryAttachmentMapper.insertSysNotaryAttachment(sysNotaryAttachment);
    }

    /**
     * 修改公证附件
     * 
     * @param sysNotaryAttachment 公证附件
     * @return 结果
     */
    @Override
    public int updateSysNotaryAttachment(SysNotaryAttachment sysNotaryAttachment)
    {
        return sysNotaryAttachmentMapper.updateSysNotaryAttachment(sysNotaryAttachment);
    }

    /**
     * 批量删除公证附件
     * 
     * @param attachmentIds 需要删除的公证附件主键
     * @return 结果
     */
    @Override
    public int deleteSysNotaryAttachmentByAttachmentIds(Long[] attachmentIds)
    {
        return sysNotaryAttachmentMapper.deleteSysNotaryAttachmentByAttachmentIds(attachmentIds);
    }

    /**
     * 删除公证附件信息
     * 
     * @param attachmentId 公证附件主键
     * @return 结果
     */
    @Override
    public int deleteSysNotaryAttachmentByAttachmentId(Long attachmentId)
    {
        return sysNotaryAttachmentMapper.deleteSysNotaryAttachmentByAttachmentId(attachmentId);
    }
}
