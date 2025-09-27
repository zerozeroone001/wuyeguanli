package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.domain.vo.OcrVoteData;
import com.ruoyi.system.domain.vo.OwnerInfo;
import com.ruoyi.system.domain.vo.VoteFileInfo;
import com.ruoyi.system.domain.vo.VoteItem;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 投票导入服务实现类
 * 
 * @author ruoyi
 */
@Service
public class VoteImportServiceImpl implements IVoteImportService {
    
    private static final Logger log = LoggerFactory.getLogger(VoteImportServiceImpl.class);
    
    @Autowired
    private IVoteFileUploadService voteFileUploadService;
    
    @Autowired
    private IOcrVoteProcessorService ocrVoteProcessorService;
    
    @Autowired
    private IVoteUserQueryService voteUserQueryService;
    
    @Autowired
    private IVoteTopicMatchService voteTopicMatchService;
    
    @Autowired
    private ISysMeetingVoteService sysMeetingVoteService;
    
    @Override
    @Transactional
    public List<VoteImportResult> importVotesFromImages(Long meetingId, List<MultipartFile> imageFiles) {
        List<VoteImportResult> results = new ArrayList<>();
        
        if (meetingId == null || imageFiles == null || imageFiles.isEmpty()) {
            return results;
        }
        
        log.info("开始批量导入投票，会议ID: {}，文件数量: {}", meetingId, imageFiles.size());
        
        for (MultipartFile imageFile : imageFiles) {
            VoteImportResult result = processSingleVoteImage(meetingId, imageFile);
            results.add(result);
        }
        
        // 统计导入结果
        long successCount = results.stream().mapToLong(r -> r.getVoteRecords().size()).sum();
        long failedCount = results.stream().filter(r -> !r.isSuccess()).count();
        
        log.info("投票导入完成，成功: {} 条投票记录，失败: {} 个文件", successCount, failedCount);
        
        return results;
    }
    
    @Override
    @Transactional
    public VoteImportResult processSingleVoteImage(Long meetingId, MultipartFile imageFile) {
        VoteImportResult result = new VoteImportResult();
        result.setFileName(imageFile.getOriginalFilename());
        result.setFileSize(imageFile.getSize());
        
        try {
            // 1. 上传文件
            String fileUrl = voteFileUploadService.uploadVoteImage(imageFile);
            result.setFileUrl(fileUrl);
            
            // 2. 调用OCR接口（这里模拟OCR调用，实际需要调用腾讯云OCR）
            String ocrResponse = callTencentOcr(imageFile);
            
            // 3. 解析OCR数据
            OcrVoteData ocrData = ocrVoteProcessorService.processOcrResponse(ocrResponse);
            if (ocrData.getStatus() == OcrVoteData.ProcessStatus.FAILED) {
                result.setSuccess(false);
                result.setMessage("OCR识别失败：" + String.join(", ", ocrData.getWarnings()));
                return result;
            }
            
            // 4. 查找用户
            Long userId = findUserId(ocrData.getOwnerInfo());
            if (userId == null) {
                result.setSuccess(false);
                result.setMessage("未找到匹配的用户：" + ocrData.getOwnerInfo().getOwnerName() + 
                    " - " + ocrData.getOwnerInfo().getPhoneNumber());
                return result;
            }
            
            // 5. 批量处理投票记录
            List<SysMeetingVote> voteRecords = processVoteItems(
                meetingId, userId, ocrData, fileUrl);
            
            if (voteRecords.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("没有找到匹配的议题");
                return result;
            }
            
            // 6. 保存投票记录
            int savedCount = saveVoteRecords(voteRecords);
            
            result.setSuccess(true);
            result.setMessage("成功导入 " + savedCount + " 条投票记录");
            result.setVoteRecords(voteRecords);
            result.setOwnerInfo(ocrData.getOwnerInfo());
            
            return result;
            
        } catch (Exception e) {
            log.error("处理投票图片失败：{}", imageFile.getOriginalFilename(), e);
            result.setSuccess(false);
            result.setMessage("处理失败：" + e.getMessage());
            return result;
        }
    }
    
    @Override
    public List<VoteImportResult> batchInsertVotes(Long meetingId, List<SysMeetingVote> votes) {
        List<VoteImportResult> results = new ArrayList<>();
        
        if (meetingId == null || votes == null || votes.isEmpty()) {
            return results;
        }
        
        // 按文件URL分组处理
        Map<String, List<SysMeetingVote>> votesByFile = new HashMap<>();
        for (SysMeetingVote vote : votes) {
            String fileUrl = vote.getFileUrl();
            votesByFile.computeIfAbsent(fileUrl, k -> new ArrayList<>()).add(vote);
        }
        
        for (Map.Entry<String, List<SysMeetingVote>> entry : votesByFile.entrySet()) {
            VoteImportResult result = new VoteImportResult();
            result.setFileUrl(entry.getKey());
            
            try {
                int savedCount = saveVoteRecords(entry.getValue());
                result.setSuccess(true);
                result.setMessage("成功保存 " + savedCount + " 条投票记录");
                result.setVoteRecords(entry.getValue());
            } catch (Exception e) {
                log.error("批量插入投票记录失败", e);
                result.setSuccess(false);
                result.setMessage("保存失败：" + e.getMessage());
            }
            
            results.add(result);
        }
        
        return results;
    }
    
    /**
     * 调用腾讯云OCR接口（模拟实现）
     */
    private String callTencentOcr(MultipartFile imageFile) {
        // TODO: 实际调用腾讯云OCR表格识别v3接口
        // 这里返回模拟数据，实际开发时需要替换为真实的OCR调用
        return "{\n" +
            "  \"TableDetections\": [\n" +
            "    {},\n" +
            "    {},\n" +
            "    {\n" +
            "      \"Cells\": [\n" +
            "        {\"Text\": \"业主姓名\", \"RowTl\": 100, \"RowBr\": 150, \"ColTl\": 50, \"ColBr\": 200},\n" +
            "        {\"Text\": \"测试甲\", \"RowTl\": 100, \"RowBr\": 150, \"ColTl\": 200, \"ColBr\": 350},\n" +
            "        {\"Text\": \"专有部分建筑面积(平方米)\", \"RowTl\": 150, \"RowBr\": 200, \"ColTl\": 50, \"ColBr\": 200},\n" +
            "        {\"Text\": \"108\", \"RowTl\": 150, \"RowBr\": 200, \"ColTl\": 200, \"ColBr\": 350},\n" +
            "        {\"Text\": \"房号\", \"RowTl\": 200, \"RowBr\": 250, \"ColTl\": 50, \"ColBr\": 200},\n" +
            "        {\"Text\": \"3栋2单元1101室\", \"RowTl\": 200, \"RowBr\": 250, \"ColTl\": 200, \"ColBr\": 350},\n" +
            "        {\"Text\": \"电话\", \"RowTl\": 250, \"RowBr\": 300, \"ColTl\": 50, \"ColBr\": 200},\n" +
            "        {\"Text\": \"18888888888\", \"RowTl\": 250, \"RowBr\": 300, \"ColTl\": 200, \"ColBr\": 350}\n" +
            "      ]\n" +
            "    },\n" +
            "    {},\n" +
            "    {},\n" +
            "    {\n" +
            "      \"Cells\": [\n" +
            "        {\"Text\": \"表决事项\", \"RowTl\": 400, \"RowBr\": 450, \"ColTl\": 50, \"ColBr\": 200},\n" +
            "        {\"Text\": \"投票结果\", \"RowTl\": 400, \"RowBr\": 450, \"ColTl\": 200, \"ColBr\": 350},\n" +
            "        {\"Text\": \"选聘物业服务企业\", \"RowTl\": 450, \"RowBr\": 500, \"ColTl\": 50, \"ColBr\": 200},\n" +
            "        {\"Text\": \"√\", \"RowTl\": 450, \"RowBr\": 500, \"ColTl\": 200, \"ColBr\": 350},\n" +
            "        {\"Text\": \"使用公共资金维修设施\", \"RowTl\": 500, \"RowBr\": 550, \"ColTl\": 50, \"ColBr\": 200},\n" +
            "        {\"Text\": \"×\", \"RowTl\": 500, \"RowBr\": 550, \"ColTl\": 200, \"ColBr\": 350}\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    }
    
    /**
     * 查找用户ID
     */
    private Long findUserId(OwnerInfo ownerInfo) {
        if (ownerInfo == null) {
            return null;
        }
        
        // 优先通过电话号码查找
        if (StringUtils.isNotEmpty(ownerInfo.getPhoneNumber())) {
            Long userId = voteUserQueryService.findUserIdByPhone(ownerInfo.getPhoneNumber());
            if (userId != null) {
                return userId;
            }
        }
        
        // 通过代理人电话查找
        if (StringUtils.isNotEmpty(ownerInfo.getAgentPhone())) {
            Long userId = voteUserQueryService.findUserIdByPhone(ownerInfo.getAgentPhone());
            if (userId != null) {
                return userId;
            }
        }
        
        // 通过姓名和房号查找
        if (StringUtils.isNotEmpty(ownerInfo.getOwnerName()) && 
            StringUtils.isNotEmpty(ownerInfo.getRoomNumber())) {
            return voteUserQueryService.findUserIdByNameAndRoom(
                ownerInfo.getOwnerName(), ownerInfo.getRoomNumber());
        }
        
        return null;
    }
    
    /**
     * 处理投票项目
     */
    private List<SysMeetingVote> processVoteItems(Long meetingId, Long userId, 
            OcrVoteData ocrData, String fileUrl) {
        List<SysMeetingVote> voteRecords = new ArrayList<>();
        
        if (ocrData.getVoteItems() == null || ocrData.getVoteItems().isEmpty()) {
            return voteRecords;
        }
        
        for (VoteItem voteItem : ocrData.getVoteItems()) {
            // 匹配议题
            Long topicId = voteTopicMatchService.matchTopicId(meetingId, voteItem.getTopicTitle());
            if (topicId == null) {
                log.warn("未找到匹配的议题：{}", voteItem.getTopicTitle());
                continue;
            }
            
            // 检查是否已投票
            SysMeetingVote existingVote = sysMeetingVoteService.findVote(userId, topicId);
            
            // 创建投票记录
            SysMeetingVote vote = new SysMeetingVote();
            vote.setMeetingId(meetingId);
            vote.setTopicId(topicId);
            vote.setUserId(userId);
            vote.setUserName(ocrData.getOwnerInfo().getOwnerName());
            vote.setChoice(String.valueOf(voteItem.getVoteOption().getCode()));
            vote.setVoteType(1); // 纸质投票导入
            vote.setFileUrl(fileUrl);
            
            if (existingVote != null) {
                // 如果已投票，更新投票记录
                vote.setVoteId(existingVote.getVoteId());
            }
            
            voteRecords.add(vote);
        }
        
        return voteRecords;
    }
    
    /**
     * 保存投票记录
     */
    private int saveVoteRecords(List<SysMeetingVote> voteRecords) {
        int savedCount = 0;
        
        for (SysMeetingVote vote : voteRecords) {
            try {
                if (vote.getVoteId() != null) {
                    // 更新已存在的投票
                    sysMeetingVoteService.updateSysMeetingVote(vote);
                } else {
                    // 插入新投票
                    sysMeetingVoteService.insertSysMeetingVote(vote);
                }
                savedCount++;
            } catch (Exception e) {
                log.error("保存投票记录失败：用户ID {}, 议题ID {}", 
                    vote.getUserId(), vote.getTopicId(), e);
            }
        }
        
        return savedCount;
    }
    
    /**
     * 投票导入结果类
     */
    public static class VoteImportResult {
        private String fileName;
        private Long fileSize;
        private String fileUrl;
        private boolean success;
        private String message;
        private OwnerInfo ownerInfo;
        private List<SysMeetingVote> voteRecords = new ArrayList<>();
        
        // Getters and Setters
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        
        public Long getFileSize() { return fileSize; }
        public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
        
        public String getFileUrl() { return fileUrl; }
        public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public OwnerInfo getOwnerInfo() { return ownerInfo; }
        public void setOwnerInfo(OwnerInfo ownerInfo) { this.ownerInfo = ownerInfo; }
        
        public List<SysMeetingVote> getVoteRecords() { return voteRecords; }
        public void setVoteRecords(List<SysMeetingVote> voteRecords) { this.voteRecords = voteRecords; }
    }
}