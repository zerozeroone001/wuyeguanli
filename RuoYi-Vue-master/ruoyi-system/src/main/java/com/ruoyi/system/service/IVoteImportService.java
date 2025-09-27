package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.service.impl.VoteImportServiceImpl.VoteImportResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 投票导入服务接口
 * 
 * @author ruoyi
 */
public interface IVoteImportService {
    
    /**
     * 从图片批量导入投票
     * 
     * @param meetingId 会议ID
     * @param imageFiles 投票图片文件列表
     * @return 导入结果列表
     */
    List<VoteImportResult> importVotesFromImages(Long meetingId, List<MultipartFile> imageFiles);
    
    /**
     * 处理单个投票图片
     * 
     * @param meetingId 会议ID
     * @param imageFile 投票图片文件
     * @return 处理结果
     */
    VoteImportResult processSingleVoteImage(Long meetingId, MultipartFile imageFile);
    
    /**
     * 批量插入投票记录
     * 
     * @param meetingId 会议ID
     * @param votes 投票记录列表
     * @return 插入结果列表
     */
    List<VoteImportResult> batchInsertVotes(Long meetingId, List<SysMeetingVote> votes);
}