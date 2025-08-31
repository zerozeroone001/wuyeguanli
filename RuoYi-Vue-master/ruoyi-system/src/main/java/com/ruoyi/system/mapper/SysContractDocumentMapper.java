package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysContractDocument;

/**
 * 合同相关文档Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-01
 */
public interface SysContractDocumentMapper 
{
    /**
     * 查询合同相关文档
     * 
     * @param docId 合同相关文档主键
     * @return 合同相关文档
     */
    public SysContractDocument selectSysContractDocumentByDocId(Long docId);

    /**
     * 根据合同ID查询相关文档列表
     *
     * @param contractId 合同ID
     * @return 合同相关文档集合
     */
    public List<SysContractDocument> selectSysContractDocumentListByContractId(Long contractId);

    /**
     * 查询合同相关文档列表
     * 
     * @param sysContractDocument 合同相关文档
     * @return 合同相关文档集合
     */
    public List<SysContractDocument> selectSysContractDocumentList(SysContractDocument sysContractDocument);

    /**
     * 新增合同相关文档
     * 
     * @param sysContractDocument 合同相关文档
     * @return 结果
     */
    public int insertSysContractDocument(SysContractDocument sysContractDocument);

    /**
     * 修改合同相关文档
     * 
     * @param sysContractDocument 合同相关文档
     * @return 结果
     */
    public int updateSysContractDocument(SysContractDocument sysContractDocument);

    /**
     * 删除合同相关文档
     * 
     * @param docId 合同相关文档主键
     * @return 结果
     */
    public int deleteSysContractDocumentByDocId(Long docId);

    /**
     * 批量删除合同相关文档
     * 
     * @param docIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysContractDocumentByDocIds(Long[] docIds);
}
