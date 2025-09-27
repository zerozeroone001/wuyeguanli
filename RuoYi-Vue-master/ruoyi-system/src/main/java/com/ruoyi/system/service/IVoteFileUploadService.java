package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.VoteFileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 投票文件上传服务接口
 * 
 * @author ruoyi
 */
public interface IVoteFileUploadService {
    
    /**
     * 上传单个投票图片文件
     * 
     * @param file 图片文件
     * @return 文件访问URL
     * @throws IOException 上传异常
     */
    String uploadVoteImage(MultipartFile file) throws IOException;
    
    /**
     * 批量上传投票图片文件
     * 
     * @param files 图片文件列表
     * @return 上传结果列表
     */
    List<VoteFileInfo> batchUploadVoteImages(List<MultipartFile> files);
    
    /**
     * 验证是否为有效的图片文件
     * 
     * @param file 文件
     * @return 是否有效
     */
    boolean isValidImageFile(MultipartFile file);
    
    /**
     * 获取文件访问URL
     * 
     * @param fileName 文件名
     * @return 访问URL
     */
    String getAccessUrl(String fileName);
    
    /**
     * 验证文件列表
     * 
     * @param files 文件列表
     * @return 验证结果列表
     */
    List<VoteFileInfo> validateFiles(List<MultipartFile> files);
    
    /**
     * 获取文件总大小
     * 
     * @param files 文件列表
     * @return 总大小（字节）
     */
    long getTotalFileSize(List<MultipartFile> files);
    
    /**
     * 获取有效文件数量
     * 
     * @param files 文件列表
     * @return 有效文件数量
     */
    int getValidFileCount(List<MultipartFile> files);
}