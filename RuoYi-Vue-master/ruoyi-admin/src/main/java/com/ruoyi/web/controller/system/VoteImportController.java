package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.system.service.IVoteImportService;
import com.ruoyi.system.service.impl.VoteImportServiceImpl.VoteImportResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 投票导入Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/vote")
public class VoteImportController extends BaseController {
    
    private static final Logger log = LoggerFactory.getLogger(VoteImportController.class);
    
    @Autowired
    private IVoteImportService voteImportService;
    
    @Autowired
    private ServerConfig serverConfig;

    @Value("${ruoyi.uploadMode}")
    private String uploadMode;

    @Value("${aliyun.oss.domain:}")
    private String ossDomain;

    @Value("${aliyun.oss.bucketName:}")
    private String ossBucketName;

    @Value("${aliyun.oss.endpoint:}")
    private String ossEndpoint;
    
    /**
     * 批量导入投票
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:import')")
    @Log(title = "投票导入", businessType = BusinessType.IMPORT)
    @PostMapping("/import/{meetingId}")
    public AjaxResult importVotes(@PathVariable Long meetingId, 
                                 @RequestParam("files") List<MultipartFile> files) {
        try {
            // 验证参数
            if (meetingId == null) {
                return AjaxResult.error("会议ID不能为空");
            }
            
            if (files == null || files.isEmpty()) {
                return AjaxResult.error("请选择要上传的投票图片文件");
            }
            
            // 验证文件数量限制
            if (files.size() > 50) {
                return AjaxResult.error("单次最多只能上传50个文件");
            }
            
            // 验证文件总大小限制（100MB）
            long totalSize = files.stream().mapToLong(MultipartFile::getSize).sum();
            if (totalSize > 100 * 1024 * 1024) {
                return AjaxResult.error("文件总大小不能超过100MB");
            }
            
            log.info("开始批量导入投票，会议ID: {}，文件数量: {}", meetingId, files.size());
            
            // 执行导入
            List<VoteImportResult> results = voteImportService.importVotesFromImages(meetingId, files);
            
            // 处理文件URL（确保本地存储模式下的URL是完整的）
            processFileUrls(results);
            
            // 统计结果
            long successCount = results.stream().filter(VoteImportResult::isSuccess).count();
            long failedCount = results.size() - successCount;
            long totalVoteRecords = results.stream()
                    .mapToLong(r -> r.getVoteRecords().size())
                    .sum();
            
            AjaxResult ajax = AjaxResult.success();
            ajax.put("results", results);
            ajax.put("summary", createSummary(successCount, failedCount, totalVoteRecords));
            ajax.put("message", String.format("导入完成：成功处理 %d 个文件，失败 %d 个文件，共导入 %d 条投票记录", 
                successCount, failedCount, totalVoteRecords));
            
            return ajax;
            
        } catch (Exception e) {
            log.error("批量导入投票失败", e);
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }
    
    /**
     * 导入单个投票文件
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:import')")
    @Log(title = "单个投票导入", businessType = BusinessType.IMPORT)
    @PostMapping("/import/single/{meetingId}")
    public AjaxResult importSingleVote(@PathVariable Long meetingId, 
                                      @RequestParam("file") MultipartFile file) {
        try {
            // 验证参数
            if (meetingId == null) {
                return AjaxResult.error("会议ID不能为空");
            }
            
            if (file == null || file.isEmpty()) {
                return AjaxResult.error("请选择要上传的投票图片文件");
            }
            
            // 验证文件大小限制（10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                return AjaxResult.error("文件大小不能超过10MB");
            }
            
            log.info("开始导入单个投票，会议ID: {}，文件: {}", meetingId, file.getOriginalFilename());
            
            // 执行导入
            VoteImportResult result = voteImportService.processSingleVoteImage(meetingId, file);
            
            if (result.isSuccess()) {
                return AjaxResult.success("导入成功", result);
            } else {
                return AjaxResult.error(result.getMessage(), result);
            }
            
        } catch (Exception e) {
            log.error("导入单个投票失败", e);
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }
    
    /**
     * 验证投票文件
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:import')")
    @PostMapping("/validate")
    public AjaxResult validateVoteFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            if (files == null || files.isEmpty()) {
                return AjaxResult.error("请选择要验证的文件");
            }
            
            // 验证文件
            int validCount = 0;
            int invalidCount = 0;
            long totalSize = 0;
            
            for (MultipartFile file : files) {
                totalSize += file.getSize();
                if (isValidVoteFile(file)) {
                    validCount++;
                } else {
                    invalidCount++;
                }
            }
            
            AjaxResult ajax = AjaxResult.success();
            ajax.put("totalCount", files.size());
            ajax.put("validCount", validCount);
            ajax.put("invalidCount", invalidCount);
            ajax.put("totalSize", totalSize);
            ajax.put("formattedSize", formatFileSize(totalSize));
            
            return ajax;
            
        } catch (Exception e) {
            log.error("验证投票文件失败", e);
            return AjaxResult.error("验证失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取导入进度（用于长时间处理的情况）
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:import')")
    @GetMapping("/progress/{taskId}")
    public AjaxResult getImportProgress(@PathVariable String taskId) {
        // TODO: 实现异步导入进度查询
        // 这里可以使用Redis或内存缓存来存储导入进度
        AjaxResult ajax = AjaxResult.success();
        ajax.put("taskId", taskId);
        ajax.put("progress", 100); // 百分比
        ajax.put("status", "completed"); // processing, completed, failed
        ajax.put("message", "导入完成");
        return ajax;
    }
    
    /**
     * 创建导入结果摘要
     */
    private ImportSummary createSummary(long successCount, long failedCount, long totalVoteRecords) {
        ImportSummary summary = new ImportSummary();
        summary.setTotalFiles(successCount + failedCount);
        summary.setSuccessFiles(successCount);
        summary.setFailedFiles(failedCount);
        summary.setTotalVoteRecords(totalVoteRecords);
        summary.setSuccessRate(summary.getTotalFiles() > 0 ? 
            (double) successCount / summary.getTotalFiles() * 100 : 0);
        return summary;
    }
    
    /**
     * 验证是否为有效的投票文件
     */
    private boolean isValidVoteFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }
        
        // 检查文件大小（不超过10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            return false;
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return false;
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        return extension.matches("jpg|jpeg|png|bmp|gif");
    }
    
    /**
     * 处理文件URL，确保本地存储模式下返回完整URL
     */
    private void processFileUrls(List<VoteImportResult> results) {
        if ("local".equals(uploadMode)) {
            for (VoteImportResult result : results) {
                String fileUrl = result.getFileUrl();
                if (fileUrl != null && fileUrl.startsWith("/profile/")) {
                    result.setFileUrl(serverConfig.getUrl() + fileUrl);
                }
            }
        }
    }
    
    /**
     * 构建文件访问URL
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
            // 本地存储模式
            return serverConfig.getUrl() + fileName;
        }
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(long size) {
        if (size <= 0) {
            return "0 B";
        }
        
        String[] units = {"B", "KB", "MB", "GB"};
        double fileSize = size;
        int unitIndex = 0;
        
        while (fileSize >= 1024 && unitIndex < units.length - 1) {
            fileSize /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", fileSize, units[unitIndex]);
    }
    
    /**
     * 导入摘要信息
     */
    public static class ImportSummary {
        private long totalFiles;
        private long successFiles;
        private long failedFiles;
        private long totalVoteRecords;
        private double successRate;
        
        // Getters and Setters
        public long getTotalFiles() { return totalFiles; }
        public void setTotalFiles(long totalFiles) { this.totalFiles = totalFiles; }
        
        public long getSuccessFiles() { return successFiles; }
        public void setSuccessFiles(long successFiles) { this.successFiles = successFiles; }
        
        public long getFailedFiles() { return failedFiles; }
        public void setFailedFiles(long failedFiles) { this.failedFiles = failedFiles; }
        
        public long getTotalVoteRecords() { return totalVoteRecords; }
        public void setTotalVoteRecords(long totalVoteRecords) { this.totalVoteRecords = totalVoteRecords; }
        
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
    }
}