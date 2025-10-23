package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysFormTemplate;

import java.util.List;

/**
 * 自定义表单模板Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-23
 */
public interface SysFormTemplateMapper
{
    /**
     * 查询自定义表单模板
     *
     * @param formId 主键
     * @return 模板
     */
    SysFormTemplate selectSysFormTemplateByFormId(Long formId);

    /**
     * 查询模板列表
     *
     * @param sysFormTemplate 查询条件
     * @return 模板集合
     */
    List<SysFormTemplate> selectSysFormTemplateList(SysFormTemplate sysFormTemplate);

    /**
     * 查询轻量模板列表（仅返回基础字段，避免传输大字段）
     *
     * @param sysFormTemplate 查询条件
     * @return 模板集合
     */
    List<SysFormTemplate> selectSysFormTemplateOptions(SysFormTemplate sysFormTemplate);

    /**
     * 新增模板
     *
     * @param sysFormTemplate 模板
     * @return 结果
     */
    int insertSysFormTemplate(SysFormTemplate sysFormTemplate);

    /**
     * 修改模板
     *
     * @param sysFormTemplate 模板
     * @return 结果
     */
    int updateSysFormTemplate(SysFormTemplate sysFormTemplate);

    /**
     * 删除模板
     *
     * @param formId 主键
     * @return 结果
     */
    int deleteSysFormTemplateByFormId(Long formId);

    /**
     * 批量删除模板
     *
     * @param formIds 需要删除的主键集合
     * @return 结果
     */
    int deleteSysFormTemplateByFormIds(Long[] formIds);
}

