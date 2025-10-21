package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.storage.IStorageService;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
//import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.system.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用文件上传服务实现
 *
 * 统一封装上传逻辑，控制层仅负责参数和结果转换。
 */
@Service
public class UploadServiceImpl implements UploadService {

    private static final String FILE_DELIMITER = ",";


    @Autowired
    private IStorageService storageService;

    @Value("${ruoyi.uploadMode}")
    private String uploadMode;

    @Value("${aliyun.oss.domain:}")
    private String ossDomain;

    @Value("${aliyun.oss.bucketName:}")
    private String ossBucketName;

    @Value("${aliyun.oss.endpoint:}")
    private String ossEndpoint;

    @Override
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return AjaxResult.error("上传文件不能为空");
        }

        try {
            String fileName = storageService.upload(file);
            String url = buildUrl(fileName);

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @Override
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception {
        if (files == null || files.isEmpty()) {
            return AjaxResult.error("上传文件列表不能为空");
        }

        try {
            List<String> urls = new ArrayList<>();
            List<String> fileNames = new ArrayList<>();
            List<String> newFileNames = new ArrayList<>();
            List<String> originalFilenames = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    return AjaxResult.error("上传文件不能为空");
                }
                String fileName = storageService.upload(file);
                String url = buildUrl(fileName);

                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }

            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMITER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMITER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMITER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMITER));
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    private String buildUrl(String fileName) {
        if ("oss".equals(uploadMode)) {
            if (StringUtils.isNotEmpty(ossDomain)) {
                return "https://" + ossDomain + "/" + fileName;
            } else {
                return "https://" + ossBucketName + "." + ossEndpoint + "/" + fileName;
            }
        }
        return fileName;
    }
}
