package com.ruoyi.common.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储服务策略接口
 * 
 * @author Gemini
 */
public interface IStorageService {

    /**
     * 文件上传
     *
     * @param file 上传的文件
     * @return 文件名（相对路径或文件key）
     * @throws Exception
     */
    String upload(MultipartFile file) throws Exception;

}
