package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysPropertyMeetingTopic;
import com.ruoyi.system.domain.vo.VoteItem;
import com.ruoyi.system.service.ISysPropertyMeetingTopicService;
import com.ruoyi.system.service.IVoteTopicMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 投票议题匹配服务实现类
 * 
 * @author ruoyi
 */
@Service
public class VoteTopicMatchServiceImpl implements IVoteTopicMatchService {
    
    private static final Logger log = LoggerFactory.getLogger(VoteTopicMatchServiceImpl.class);
    
    @Autowired
    private ISysPropertyMeetingTopicService meetingTopicService;
    
    @Override
    public Long matchTopicId(Long meetingId, String topicTitle) {
        if (meetingId == null || StringUtils.isEmpty(topicTitle)) {
            log.warn("会议ID或议题标题为空，无法匹配议题");
            return null;
        }
        
        // 获取该会议的所有议题
        List<SysPropertyMeetingTopic> meetingTopics = getMeetingTopics(meetingId);
        if (meetingTopics == null || meetingTopics.isEmpty()) {
            log.warn("会议ID {} 没有找到任何议题", meetingId);
            return null;
        }
        
        // 清理和标准化议题标题
        String cleanTitle = cleanTopicTitle(topicTitle);
        
        // 1. 精确匹配
        Long exactMatchId = findExactMatch(meetingTopics, cleanTitle);
        if (exactMatchId != null) {
            log.info("精确匹配成功：{} -> 议题ID: {}", topicTitle, exactMatchId);
            return exactMatchId;
        }

        
        log.warn("未找到匹配的议题：{}", topicTitle);
        return null;
    }
    
    @Override
    public Map<String, Long> batchMatchTopics(Long meetingId, List<VoteItem> voteItems) {
        Map<String, Long> matchResults = new HashMap<>();
        
        if (meetingId == null || voteItems == null || voteItems.isEmpty()) {
            return matchResults;
        }
        
        // 获取该会议的所有议题（只查询一次，提高效率）
        List<SysPropertyMeetingTopic> meetingTopics = getMeetingTopics(meetingId);
        if (meetingTopics == null || meetingTopics.isEmpty()) {
            log.warn("会议ID {} 没有找到任何议题", meetingId);
            return matchResults;
        }
        
        // 批量匹配
        for (VoteItem voteItem : voteItems) {
            String topicTitle = voteItem.getTopicTitle();
            if (StringUtils.isNotEmpty(topicTitle)) {
                Long topicId = matchTopicFromList(meetingTopics, topicTitle);
                matchResults.put(topicTitle, topicId);
            }
        }
        
        return matchResults;
    }
    
    @Override
    public Double calculateMatchConfidence(String ocrTitle, String dbTitle) {
        if (StringUtils.isEmpty(ocrTitle) || StringUtils.isEmpty(dbTitle)) {
            return 0.0;
        }
        
        String cleanOcrTitle = cleanTopicTitle(ocrTitle);
        String cleanDbTitle = cleanTopicTitle(dbTitle);
        
        // 精确匹配
        if (cleanOcrTitle.equals(cleanDbTitle)) {
            return 1.0;
        }
        
        // 包含关系
        if (cleanDbTitle.contains(cleanOcrTitle) || cleanOcrTitle.contains(cleanDbTitle)) {
            return 0.8;
        }
        
        // 关键词匹配
        double keywordScore = calculateKeywordSimilarity(cleanOcrTitle, cleanDbTitle);
        if (keywordScore > 0.5) {
            return keywordScore;
        }
        
        // 编辑距离相似度
        double editDistanceScore = calculateEditDistanceSimilarity(cleanOcrTitle, cleanDbTitle);
        
        return editDistanceScore;
    }
    
    /**
     * 获取会议的所有议题
     */
    private List<SysPropertyMeetingTopic> getMeetingTopics(Long meetingId) {
        try {
            return meetingTopicService.selectSysPropertyMeetingTopicListByMeetingId(meetingId);
        } catch (Exception e) {
            log.error("查询会议议题失败，会议ID: {}", meetingId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 从议题列表中匹配指定标题
     */
    private Long matchTopicFromList(List<SysPropertyMeetingTopic> meetingTopics, String topicTitle) {
        String cleanTitle = cleanTopicTitle(topicTitle);
        
        // 精确匹配
        Long exactMatchId = findExactMatch(meetingTopics, cleanTitle);
        if (exactMatchId != null) {
            return exactMatchId;
        }
        
        // 关键词匹配
        Long keywordMatchId = findKeywordMatch(meetingTopics, cleanTitle);
        if (keywordMatchId != null) {
            return keywordMatchId;
        }
        
        // 模糊匹配
        return findFuzzyMatch(meetingTopics, cleanTitle);
    }
    
    /**
     * 精确匹配
     */
    private Long findExactMatch(List<SysPropertyMeetingTopic> topics, String cleanTitle) {
        for (SysPropertyMeetingTopic topic : topics) {
            String cleanDbTitle = cleanTopicTitle(topic.getTopicTitle());
            if (cleanTitle.equals(cleanDbTitle)) {
                return topic.getTopicId();
            }
        }
        return null;
    }
    
    /**
     * 关键词匹配
     */
    private Long findKeywordMatch(List<SysPropertyMeetingTopic> topics, String cleanTitle) {
        // 提取关键词
        Set<String> keywords = extractKeywords(cleanTitle);
        if (keywords.isEmpty()) {
            return null;
        }
        
        Long bestMatchId = null;
        double bestScore = 0.0;
        
        for (SysPropertyMeetingTopic topic : topics) {
            String cleanDbTitle = cleanTopicTitle(topic.getTopicTitle());
            Set<String> dbKeywords = extractKeywords(cleanDbTitle);
            
            double score = calculateKeywordOverlap(keywords, dbKeywords);
            if (score > bestScore && score >= 0.6) { // 至少60%的关键词匹配
                bestScore = score;
                bestMatchId = topic.getTopicId();
            }
        }
        
        return bestMatchId;
    }
    
    /**
     * 模糊匹配（基于编辑距离）
     */
    private Long findFuzzyMatch(List<SysPropertyMeetingTopic> topics, String cleanTitle) {
        Long bestMatchId = null;
        double bestScore = 0.0;
        
        for (SysPropertyMeetingTopic topic : topics) {
            String cleanDbTitle = cleanTopicTitle(topic.getTopicTitle());
            double score = calculateEditDistanceSimilarity(cleanTitle, cleanDbTitle);
            
            if (score > bestScore && score >= 0.7) { // 至少70%的相似度
                bestScore = score;
                bestMatchId = topic.getTopicId();
            }
        }
        
        return bestMatchId;
    }
    
    /**
     * 清理和标准化议题标题
     */
    private String cleanTopicTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            return "";
        }
        
        return title.trim()
                .replaceAll("\\s+", "")  // 移除所有空白字符
                .replaceAll("[《》\"\"''()（）\\[\\]【】]", "") // 移除标点符号
                .toLowerCase();
    }
    
    /**
     * 提取关键词
     */
    private Set<String> extractKeywords(String title) {
        Set<String> keywords = new HashSet<>();
        
        // 常见的关键词模式
        String[] patterns = {
            "物业服务", "管理规约", "资金", "维修", "停车费", "收费标准", 
            "选聘", "使用", "调整", "小区", "公共", "设施", "企业"
        };
        
        for (String pattern : patterns) {
            if (title.contains(pattern)) {
                keywords.add(pattern);
            }
        }
        
        // 如果没有匹配到预定义关键词，则按字符分割
        if (keywords.isEmpty() && title.length() >= 2) {
            for (int i = 0; i <= title.length() - 2; i++) {
                keywords.add(title.substring(i, i + 2));
            }
        }
        
        return keywords;
    }
    
    /**
     * 计算关键词重叠度
     */
    private double calculateKeywordOverlap(Set<String> keywords1, Set<String> keywords2) {
        if (keywords1.isEmpty() || keywords2.isEmpty()) {
            return 0.0;
        }
        
        Set<String> intersection = new HashSet<>(keywords1);
        intersection.retainAll(keywords2);
        
        Set<String> union = new HashSet<>(keywords1);
        union.addAll(keywords2);
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * 计算关键词相似度
     */
    private double calculateKeywordSimilarity(String title1, String title2) {
        Set<String> keywords1 = extractKeywords(title1);
        Set<String> keywords2 = extractKeywords(title2);
        
        return calculateKeywordOverlap(keywords1, keywords2);
    }
    
    /**
     * 计算编辑距离相似度
     */
    private double calculateEditDistanceSimilarity(String s1, String s2) {
        if (StringUtils.isEmpty(s1) || StringUtils.isEmpty(s2)) {
            return 0.0;
        }
        
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) {
            return 1.0;
        }
        
        int editDistance = levenshteinDistance(s1, s2);
        return 1.0 - (double) editDistance / maxLen;
    }
    
    /**
     * 计算编辑距离
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
}