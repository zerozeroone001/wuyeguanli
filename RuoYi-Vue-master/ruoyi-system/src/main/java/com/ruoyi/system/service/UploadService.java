package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 通用文件上传服务接口
 *
 * 提供单文件与多文件上传能力，并返回标准化的上传结果。
 */
public interface UploadService {

    /**
     * 上传单个文件
     *
     * @param file 前端提交的文件
     * @return 上传结果，包含访问地址及名称信息
     * @throws Exception 上传过程中发生的异常
     */
    AjaxResult uploadFile(MultipartFile file) throws Exception;

    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @return 上传结果，包含批量上传后的地址及名称信息
     * @throws Exception 上传过程中发生的异常
     */
    AjaxResult uploadFiles(List<MultipartFile> files) throws Exception;
}
