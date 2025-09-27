package com.ruoyi.system.service.impl;

import com.ruoyi.common.storage.IStorageService;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.system.domain.vo.VoteFileInfo;
import com.ruoyi.system.service.IVoteFileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 投票文件上传服务实现类
 * 使用统一的存储服务，支持本地和阿里云OSS
 * 
 * @author ruoyi
 */
@Service
public class VoteFileUploadServiceImpl implements IVoteFileUploadService {
    
    private static final Logger log = LoggerFactory.getLogger(VoteFileUploadServiceImpl.class);

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
    
    // 支持的图片格式
    private static final String[] ALLOWED_EXTENSIONS = MimeTypeUtils.IMAGE_EXTENSION;
    
    @Override
    public String uploadVoteImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        
        // 验证文件类型
        if (!isValidImageFile(file)) {
            throw new IllegalArgumentException("只支持JPG、PNG、JPEG、BMP、GIF格式的图片文件");
        }
        
        // 验证文件大小（限制为10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }
        
        try {
            String originalFilename = file.getOriginalFilename();
            
            // 使用统一存储服务上传文件
            String fileName = storageService.upload(file);
            
            // 构建访问URL
            String accessUrl = buildUrl(fileName);
            
            log.info("投票图片上传成功：{} -> {}", originalFilename, accessUrl);
            return accessUrl;
            
        } catch (Exception e) {
            log.error("投票图片上传失败：{}", file.getOriginalFilename(), e);
            throw new IOException("文件上传失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<VoteFileInfo> batchUploadVoteImages(List<MultipartFile> files) {
        List<VoteFileInfo> fileInfoList = new ArrayList<>();
        
        if (files == null || files.isEmpty()) {
            return fileInfoList;
        }
        
        for (MultipartFile file : files) {
            VoteFileInfo fileInfo = new VoteFileInfo();
            fileInfo.setOriginalName(file.getOriginalFilename());
            fileInfo.setFileSize(file.getSize());
            
            try {
                String fileUrl = uploadVoteImage(file);
                fileInfo.setFileUrl(fileUrl);
                fileInfo.setStatus("success");
                fileInfo.setMessage("上传成功");
            } catch (Exception e) {
                fileInfo.setStatus("failed");
                fileInfo.setMessage("上传失败：" + e.getMessage());
                log.error("批量上传文件失败：{}", file.getOriginalFilename(), e);
            }
            
            fileInfoList.add(fileInfo);
        }
        
        return fileInfoList;
    }
    
    @Override
    public boolean isValidImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return false;
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String getAccessUrl(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        
        return buildUrl(fileName);
    }

    /**
     * 构建文件访问URL
     * @param fileName 文件名
     * @return 访问URL
     */
    private String buildUrl(String fileName) {
        if ("oss".equals(uploadMode)) {
            // 阿里云OSS模式
            if (StringUtils.isNotEmpty(ossDomain)) {
                return "https://" + ossDomain + "/" + fileName;
            } else {
                return "https://" + ossBucketName + "." + ossEndpoint + "/" + fileName;
            }
        } else {
            // 本地存储模式，返回相对路径，由前端或Controller处理完整URL
            return "/profile/" + fileName;
        }
    }
    
    @Override
    public List<VoteFileInfo> validateFiles(List<MultipartFile> files) {
        List<VoteFileInfo> validationResults = new ArrayList<>();
        
        if (files == null || files.isEmpty()) {
            return validationResults;
        }
        
        for (MultipartFile file : files) {
            VoteFileInfo fileInfo = new VoteFileInfo();
            fileInfo.setOriginalName(file.getOriginalFilename());
            fileInfo.setFileSize(file.getSize());
            
            // 验证文件
            if (!isValidImageFile(file)) {
                fileInfo.setStatus("invalid");
                fileInfo.setMessage("不支持的文件格式，只支持图片文件");
            } else if (file.getSize() > 10 * 1024 * 1024) {
                fileInfo.setStatus("invalid");
                fileInfo.setMessage("文件大小不能超过10MB");
            } else if (file.isEmpty()) {
                fileInfo.setStatus("invalid");
                fileInfo.setMessage("文件内容为空");
            } else {
                fileInfo.setStatus("valid");
                fileInfo.setMessage("文件验证通过");
            }
            
            validationResults.add(fileInfo);
        }
        
        return validationResults;
    }
    
    @Override
    public long getTotalFileSize(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return 0;
        }
        
        return files.stream()
                .mapToLong(MultipartFile::getSize)
                .sum();
    }
    
    @Override
    public int getValidFileCount(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return 0;
        }
        
        return (int) files.stream()
                .filter(this::isValidImageFile)
                .count();
    }
}