package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysNotaryApplication;
import com.ruoyi.system.domain.SysNotaryProcessLog;

import java.util.List;
import java.util.Map;

/**
 * 公证业务流程服务接口
 * 
 * @author ruoyi
 * @date 2025-01-27
 */
public interface ISysNotaryProcessService 
{
    /**
     * 提交公证申请
     * 
     * @param application 公证申请
     * @return 结果
     */
    int submitApplication(SysNotaryApplication application);

    /**
     * 审核公证申请
     * 
     * @param notaryId 公证ID
     * @param reviewResult 审核结果（pass通过 reject拒绝）
     * @param reviewComment 审核意见
     * @param reviewerId 审核人ID
     * @return 结果
     */
    int reviewApplication(Long notaryId, String reviewResult, String reviewComment, Long reviewerId);

    /**
     * 开始办理公证
     * 
     * @param notaryId 公证ID
     * @param notaryPersonId 公证员ID
     * @return 结果
     */
    int startProcessing(Long notaryId, Long notaryPersonId);

    /**
     * 完成公证
     * 
     * @param notaryId 公证ID
     * @param certificateUrl 证书URL
     * @param certificateNo 证书编号
     * @return 结果
     */
    int completeNotary(Long notaryId, String certificateUrl, String certificateNo);

    /**
     * 获取公证流程记录
     * 
     * @param notaryId 公证ID
     * @return 流程记录列表
     */
    List<SysNotaryProcessLog> getProcessLogs(Long notaryId);

    /**
     * 添加流程记录
     * 
     * @param log 流程记录
     * @return 结果
     */
    int addProcessLog(SysNotaryProcessLog log);

    /**
     * 计算公证费用
     * 
     * @param type 公证类型
     * @param isUrgent 是否加急
     * @return 费用信息
     */
    Map<String, Object> calculateFee(String type, boolean isUrgent);

    /**
     * 更新公证状态
     * 
     * @param notaryId 公证ID
     * @param status 新状态
     * @param operatorId 操作人ID
     * @param comment 备注
     * @return 结果
     */
    int updateStatus(Long notaryId, String status, Long operatorId, String comment);

    /**
     * 获取公证统计信息
     * 
     * @param userId 用户ID
     * @return 统计信息
     */
    Map<String, Object> getNotaryStats(Long userId);

    /**
     * 验证公证申请材料
     * 
     * @param notaryId 公证ID
     * @return 验证结果
     */
    Map<String, Object> validateApplication(Long notaryId);

    /**
     * 发送通知消息
     * 
     * @param notaryId 公证ID
     * @param messageType 消息类型
     * @param content 消息内容
     * @return 结果
     */
    int sendNotification(Long notaryId, String messageType, String content);
}
