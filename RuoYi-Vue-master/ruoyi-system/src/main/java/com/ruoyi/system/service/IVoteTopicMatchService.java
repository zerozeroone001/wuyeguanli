package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.VoteItem;

import java.util.List;
import java.util.Map;

/**
 * 投票议题匹配服务接口
 * 
 * @author ruoyi
 */
public interface IVoteTopicMatchService {
    
    /**
     * 匹配议题ID
     * 
     * @param meetingId 会议ID
     * @param topicTitle OCR识别的议题标题
     * @return 匹配的议题ID，未匹配返回null
     */
    Long matchTopicId(Long meetingId, String topicTitle);
    
    /**
     * 批量匹配议题
     * 
     * @param meetingId 会议ID
     * @param voteItems 投票项目列表
     * @return 匹配结果映射 (议题标题 -> 议题ID)
     */
    Map<String, Long> batchMatchTopics(Long meetingId, List<VoteItem> voteItems);
    
    /**
     * 计算匹配置信度
     * 
     * @param ocrTitle OCR识别的标题
     * @param dbTitle 数据库中的标题
     * @return 匹配置信度 (0.0-1.0)
     */
    Double calculateMatchConfidence(String ocrTitle, String dbTitle);
}