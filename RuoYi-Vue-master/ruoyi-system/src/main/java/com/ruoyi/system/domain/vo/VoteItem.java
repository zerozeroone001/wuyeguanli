package com.ruoyi.system.domain.vo;

/**
 * 投票项目
 * 
 * @author ruoyi
 */
public class VoteItem {
    
    /** 议题标题 */
    private String topicTitle;
    
    /** 原始投票符号 */
    private String originalSymbol;
    
    /** 识别的投票选项 */
    private VoteOption voteOption;
    
    /** 识别置信度 */
    private Double confidence;
    
    public VoteItem() {
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private VoteItem voteItem = new VoteItem();
        
        public Builder topicTitle(String topicTitle) {
            voteItem.topicTitle = topicTitle;
            return this;
        }
        
        public Builder originalSymbol(String originalSymbol) {
            voteItem.originalSymbol = originalSymbol;
            return this;
        }
        
        public Builder voteOption(VoteOption voteOption) {
            voteItem.voteOption = voteOption;
            return this;
        }
        
        public Builder confidence(Double confidence) {
            voteItem.confidence = confidence;
            return this;
        }
        
        public VoteItem build() {
            return voteItem;
        }
    }
    
    // Getters and Setters
    public String getTopicTitle() {
        return topicTitle;
    }
    
    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }
    
    public String getOriginalSymbol() {
        return originalSymbol;
    }
    
    public void setOriginalSymbol(String originalSymbol) {
        this.originalSymbol = originalSymbol;
    }
    
    public VoteOption getVoteOption() {
        return voteOption;
    }
    
    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    /**
     * 投票选项枚举
     */
    public enum VoteOption {
        AGREE(0, "同意"),
        OPPOSE(1, "反对"),
        ABSTAIN(2, "弃权");
        
        private int code;
        private String description;
        
        VoteOption(int code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getDescription() {
            return description;
        }
    }
}