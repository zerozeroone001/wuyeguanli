package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.OcrVoteData;

/**
 * OCR投票数据处理服务接口
 * 
 * @author ruoyi
 */
public interface IOcrVoteProcessorService {
    
    /**
     * 处理OCR识别响应，解析投票数据
     * 
     * @param ocrResponse OCR接口返回的JSON字符串
     * @return 解析后的投票数据
     */
    OcrVoteData processOcrResponse(String ocrResponse);
}