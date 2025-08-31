package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysFileInfoMapper;
import com.ruoyi.system.domain.SysFileInfo;
import com.ruoyi.system.service.ISysFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-01
 */
@Service
public class SysFileInfoServiceImpl implements ISysFileInfoService 
{
    @Autowired
    private SysFileInfoMapper sysFileInfoMapper;

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return 文件信息
     * @throws Exception
     */
    @Override
    public SysFileInfo uploadFile(MultipartFile file) throws Exception
    {
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        
        SysFileInfo fileInfo = new SysFileInfo();
        fileInfo.setFileOriginName(file.getOriginalFilename());
        fileInfo.setFileName(fileName);
        fileInfo.setFilePath(filePath + "/" + fileName); // 存储完整路径或相对路径，取决于您的设计
        fileInfo.setFileUrl("/profile/upload/" + fileName); // Web访问URL
        fileInfo.setFileType(file.getContentType());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setStorageType("local"); // or from config
        fileInfo.setCreateTime(DateUtils.getNowDate());
        // 这里可以设置创建者, 例如: fileInfo.setCreateBy(SecurityUtils.getUsername());

        this.insertSysFileInfo(fileInfo);
        return fileInfo;
    }

    /**
     * 查询文件信息
     * 
     * @param fileId 文件信息主键
     * @return 文件信息
     */
    @Override
    public SysFileInfo selectSysFileInfoByFileId(Long fileId)
    {
        return sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
    }

    /**
     * 根据合同ID查询相关文档列表
     *
     * @param contractId 合同ID
     * @return 文件信息集合
     */
    @Override
    public List<SysFileInfo> selectSysFileInfoListByContractId(Long contractId)
    {
        return sysFileInfoMapper.selectSysFileInfoListByContractId(contractId);
    }

    /**
     * 查询文件信息列表
     * 
     * @param sysFileInfo 文件信息
     * @return 文件信息
     */
    @Override
    public List<SysFileInfo> selectSysFileInfoList(SysFileInfo sysFileInfo)
    {
        return sysFileInfoMapper.selectSysFileInfoList(sysFileInfo);
    }

    /**
     * 新增文件信息
     * 
     * @param sysFileInfo 文件信息
     * @return 结果
     */
    @Override
    public int insertSysFileInfo(SysFileInfo sysFileInfo)
    {
        sysFileInfo.setCreateTime(DateUtils.getNowDate());
        return sysFileInfoMapper.insertSysFileInfo(sysFileInfo);
    }

    /**
     * 修改文件信息
     * 
     * @param sysFileInfo 文件信息
     * @return 结果
     */
    @Override
    public int updateSysFileInfo(SysFileInfo sysFileInfo)
    {
        sysFileInfo.setUpdateTime(DateUtils.getNowDate());
        return sysFileInfoMapper.updateSysFileInfo(sysFileInfo);
    }

    /**
     * 批量删除文件信息
     * 
     * @param fileIds 需要删除的文件信息主键
     * @return 结果
     */
    @Override
    public int deleteSysFileInfoByFileIds(Long[] fileIds)
    {
        return sysFileInfoMapper.deleteSysFileInfoByFileIds(fileIds);
    }

    /**
     * 删除文件信息信息
     * 
     * @param fileId 文件信息主键
     * @return 结果
     */
    @Override
    public int deleteSysFileInfoByFileId(Long fileId)
    {
        return sysFileInfoMapper.deleteSysFileInfoByFileId(fileId);
    }
}
