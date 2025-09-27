package com.ruoyi.system.domain.vo;

/**
 * 投票文件信息
 * 
 * @author ruoyi
 */
public class VoteFileInfo {
    
    /** 原始文件名 */
    private String originalName;
    
    /** 文件访问URL */
    private String fileUrl;
    
    /** 文件大小（字节） */
    private Long fileSize;
    
    /** 上传状态 */
    private String status;
    
    /** 处理消息 */
    private String message;
    
    /** 错误信息 */
    private String errorMessage;
    
    public VoteFileInfo() {
    }
    
    public VoteFileInfo(String originalName, String fileUrl) {
        this.originalName = originalName;
        this.fileUrl = fileUrl;
        this.status = "success";
        this.message = "上传成功";
    }
    
    public VoteFileInfo(String originalName, String fileUrl, String errorMessage) {
        this.originalName = originalName;
        this.fileUrl = fileUrl;
        this.errorMessage = errorMessage;
        this.status = "failed";
        this.message = errorMessage;
    }
    
    // Getters and Setters
    public String getOriginalName() {
        return originalName;
    }
    
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
    
    public String getFileUrl() {
        return fileUrl;
    }
    
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * 是否上传成功
     */
    public boolean isSuccess() {
        return "success".equals(status);
    }
    
    /**
     * 是否上传失败
     */
    public boolean isFailed() {
        return "failed".equals(status);
    }
    
    /**
     * 是否验证有效
     */
    public boolean isValid() {
        return "valid".equals(status);
    }
    
    /**
     * 格式化文件大小
     */
    public String getFormattedFileSize() {
        if (fileSize == null || fileSize <= 0) {
            return "0 B";
        }
        
        String[] units = {"B", "KB", "MB", "GB"};
        double size = fileSize.doubleValue();
        int unitIndex = 0;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", size, units[unitIndex]);
    }
}