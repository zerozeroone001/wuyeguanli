package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysFormTemplate;
import com.ruoyi.system.mapper.SysFormTemplateMapper;
import com.ruoyi.system.service.ISysFormTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义表单模板Service业务层处理
 *
 * @author ruoyi
 * @date 2025-10-23
 */
@Service
public class SysFormTemplateServiceImpl implements ISysFormTemplateService
{
    @Autowired
    private SysFormTemplateMapper sysFormTemplateMapper;

    @Override
    public SysFormTemplate selectSysFormTemplateByFormId(Long formId)
    {
        return sysFormTemplateMapper.selectSysFormTemplateByFormId(formId);
    }

    @Override
    public List<SysFormTemplate> selectSysFormTemplateList(SysFormTemplate sysFormTemplate)
    {
        return sysFormTemplateMapper.selectSysFormTemplateList(sysFormTemplate);
    }

    @Override
    public List<SysFormTemplate> selectSysFormTemplateOptions(SysFormTemplate sysFormTemplate)
    {
        return sysFormTemplateMapper.selectSysFormTemplateOptions(sysFormTemplate);
    }

    @Override
    public int insertSysFormTemplate(SysFormTemplate sysFormTemplate)
    {
        sysFormTemplate.setCreateTime(DateUtils.getNowDate());
        return sysFormTemplateMapper.insertSysFormTemplate(sysFormTemplate);
    }

    @Override
    public int updateSysFormTemplate(SysFormTemplate sysFormTemplate)
    {
        sysFormTemplate.setUpdateTime(DateUtils.getNowDate());
        return sysFormTemplateMapper.updateSysFormTemplate(sysFormTemplate);
    }

    @Override
    public int deleteSysFormTemplateByFormIds(Long[] formIds)
    {
        return sysFormTemplateMapper.deleteSysFormTemplateByFormIds(formIds);
    }

    @Override
    public int deleteSysFormTemplateByFormId(Long formId)
    {
        return sysFormTemplateMapper.deleteSysFormTemplateByFormId(formId);
    }
}

