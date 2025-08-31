package com.ruoyi.common.storage;

import com.aliyun.oss.OSS;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.Seq;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 阿里云OSS存储服务
 * 
 * @author Gemini
 */
@Service("ossStorageService")
@ConditionalOnProperty(prefix = "ruoyi", name = "uploadMode", havingValue = "oss")
public class OssStorageServiceImpl implements IStorageService {

    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file) throws Exception {
        // 生成文件名
        String fileName = extractFilename(file);

        // 上传文件
        try (InputStream inputStream = file.getInputStream()) {
            ossClient.putObject(bucketName, fileName, inputStream);
        }

        // 返回文件名（key）
        return fileName;
    }

    /**
     * 编码文件名
     */
    private String extractFilename(MultipartFile file) {
        return StringUtils.format("{}/{}_{}.{}", DateUtils.datePath(),
                FilenameUtils.getBaseName(file.getOriginalFilename()), Seq.getId(Seq.uploadSeqType), getExtension(file));
    }

    /**
     * 获取文件名的后缀
     */
    private String getExtension(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }
}
