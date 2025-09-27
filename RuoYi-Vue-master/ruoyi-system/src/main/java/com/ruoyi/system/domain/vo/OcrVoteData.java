package com.ruoyi.system.domain.vo;

import java.util.List;

/**
 * OCR投票数据结果
 * 
 * @author ruoyi
 */
public class OcrVoteData {
    
    /** 业主基础信息 */
    private OwnerInfo ownerInfo;
    
    /** 投票项目列表 */
    private List<VoteItem> voteItems;
    
    /** 识别警告信息 */
    private List<String> warnings;
    
    /** 处理状态 */
    private ProcessStatus status;
    
    public OcrVoteData() {
    }
    
    public OcrVoteData(OwnerInfo ownerInfo, List<VoteItem> voteItems) {
        this.ownerInfo = ownerInfo;
        this.voteItems = voteItems;
        this.status = ProcessStatus.SUCCESS;
    }
    
    // Getters and Setters
    public OwnerInfo getOwnerInfo() {
        return ownerInfo;
    }
    
    public void setOwnerInfo(OwnerInfo ownerInfo) {
        this.ownerInfo = ownerInfo;
    }
    
    public List<VoteItem> getVoteItems() {
        return voteItems;
    }
    
    public void setVoteItems(List<VoteItem> voteItems) {
        this.voteItems = voteItems;
    }
    
    public List<String> getWarnings() {
        return warnings;
    }
    
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
    
    public ProcessStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProcessStatus status) {
        this.status = status;
    }
    
    /**
     * 处理状态枚举
     */
    public enum ProcessStatus {
        SUCCESS("成功"),
        FAILED("失败"),
        PARTIAL("部分成功");
        
        private String description;
        
        ProcessStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}