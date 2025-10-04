package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.domain.vo.OcrVoteData;
import com.ruoyi.system.domain.vo.OwnerInfo;
import com.ruoyi.system.domain.vo.VoteItem;
import com.ruoyi.system.service.*;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.RecognizeTableAccurateOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.RecognizeTableAccurateOCRResponse;
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

    @Autowired
    private ISysConfigService configService;
    
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
            String ocrResponse = callTencentOcr(fileUrl);
            log.error("===================================");
            log.error(ocrResponse);
            log.error("===================================");
            
            // 3. 解析OCR数据
            OcrVoteData ocrData = ocrVoteProcessorService.processOcrResponse(ocrResponse);
            if (ocrData.getStatus() == OcrVoteData.ProcessStatus.FAILED) {
                result.setSuccess(false);
                result.setMessage("OCR识别失败：" + String.join(", ", ocrData.getWarnings()));
                return result;
            }
            System.out.println(ocrData.toString());
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
            String fileUrl = vote.getFlieUrl();
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
    private String callTencentOcr(String imageFile) {
        // TODO: 实际调用腾讯云OCR表格识别v3接口
        try{

            //密钥从数据库中获取
            String secretId = configService.selectConfigByKey("tencentCloudSecretId");
            String secretKey = configService.selectConfigByKey("tencentCloudSecretKey");

            // 密钥信息从环境变量读取，需要提前在环境变量中设置 TENCENTCLOUD_SECRET_ID 和 TENCENTCLOUD_SECRET_KEY
            // 使用环境变量方式可以避免密钥硬编码在代码中，提高安全性
            // 生产环境建议使用更安全的密钥管理方案，如密钥管理系统(KMS)、容器密钥注入等
            // 请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(secretId,secretKey);
            // 使用临时密钥示例
            // Credential cred = new Credential("SecretId", "SecretKey", "Token");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            OcrClient client = new OcrClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            RecognizeTableAccurateOCRRequest req = new RecognizeTableAccurateOCRRequest();
            req.setImageUrl(imageFile);

            // 返回的resp是一个RecognizeTableAccurateOCRResponse的实例，与请求对象对应
            RecognizeTableAccurateOCRResponse resp = client.RecognizeTableAccurateOCR(req);

            // 输出json格式的字符串回包
            return AbstractModel.toJsonString(resp);
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

        // 这里返回模拟数据，实际开发时需要替换为真实的OCR调用
        return "";
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
//        if (StringUtils.isNotEmpty(ownerInfo.getAgentPhone())) {
//            Long userId = voteUserQueryService.findUserIdByPhone(ownerInfo.getAgentPhone());
//            if (userId != null) {
//                return userId;
//            }
//        }
//
//        // 通过姓名和房号查找
//        if (StringUtils.isNotEmpty(ownerInfo.getOwnerName()) &&
//            StringUtils.isNotEmpty(ownerInfo.getRoomNumber())) {
//            return voteUserQueryService.findUserIdByNameAndRoom(
//                ownerInfo.getOwnerName(), ownerInfo.getRoomNumber());
//        }
        
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
            vote.setCreateTime(new Date());
            
            // 安全处理voteOption，避免NullPointerException
            if (voteItem.getVoteOption() != null) {
                vote.setVoteOption(voteItem.getVoteOption().getCode());
            } else {
                log.warn("投票选项为空，议题：{}，用户：{}", voteItem.getTopicTitle(), userId);
                vote.setVoteOption(2); // 默认设为弃权
            }
            
            vote.setVoteType(1); // 纸质投票导入
            vote.setFlieUrl(fileUrl);
            
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