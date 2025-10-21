package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysNotaryApplication;
import com.ruoyi.system.domain.SysNotaryProcessLog;
import com.ruoyi.system.domain.SysNotaryTypeConfig;
import com.ruoyi.system.mapper.SysNotaryApplicationMapper;
import com.ruoyi.system.mapper.SysNotaryProcessLogMapper;
import com.ruoyi.system.mapper.SysNotaryTypeConfigMapper;
import com.ruoyi.system.service.ISysNotaryProcessService;
import com.ruoyi.system.service.ISysNotaryApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 公证业务流程服务实现类
 * 
 * @author ruoyi
 * @date 2025-01-27
 */
@Service
public class SysNotaryProcessServiceImpl implements ISysNotaryProcessService 
{
    @Autowired
    private SysNotaryApplicationMapper notaryApplicationMapper;
    
    @Autowired
    private SysNotaryProcessLogMapper processLogMapper;
    
    @Autowired
    private SysNotaryTypeConfigMapper typeConfigMapper;
    
    @Autowired
    private ISysNotaryApplicationService notaryApplicationService;

    /**
     * 提交公证申请
     */
    @Override
    @Transactional
    public int submitApplication(SysNotaryApplication application) {
        // 生成公证编号
        if (StringUtils.isEmpty(application.getNotaryNo())) {
            application.setNotaryNo(generateNotaryNo());
        }
        
        // 设置初始状态
        application.setStatus("draft");
        application.setProgress(0);
        application.setCreateTime(DateUtils.getNowDate());
        application.setApplyTime(DateUtils.getNowDate());
        
        // 计算费用
        boolean isUrgent = application.getUrgent() != null && application.getUrgent() == 1;
        Map<String, Object> feeInfo = calculateFee(application.getType(), isUrgent);
        application.setFeeAmount((BigDecimal) feeInfo.get("totalFee"));
        if (isUrgent) {
            application.setUrgentFee((BigDecimal) feeInfo.get("urgentFee"));
        }
        
        // 计算预计完成时间
        Integer processingDays = (Integer) feeInfo.get("processingDays");
        application.setExpectedTime(DateUtils.addDays(DateUtils.getNowDate(), processingDays));
        
        // 保存申请
        int result = notaryApplicationService.insertSysNotaryApplication(application);
        
        // 添加流程记录
        if (result > 0) {
            addProcessLog(application.getNotaryId(), "submit", "提交申请", "draft", 
                application.getCreateBy(), "用户提交公证申请");
        }
        
        return result;
    }

    /**
     * 审核公证申请
     */
    @Override
    @Transactional
    public int reviewApplication(Long notaryId, String reviewResult, String reviewComment, Long reviewerId) {
        SysNotaryApplication application = notaryApplicationService.selectSysNotaryApplicationByApplicationId(notaryId);
        if (application == null) {
            return 0;
        }
        
        String newStatus = "pass".equals(reviewResult) ? "reviewing" : "rejected";
        String stepName = "pass".equals(reviewResult) ? "审核通过" : "审核拒绝";
        
        // 更新申请状态
        application.setStatus(newStatus);
        application.setReviewerId(reviewerId);
        application.setReviewTime(DateUtils.getNowDate());
        application.setReviewComment(reviewComment);
        application.setUpdateTime(DateUtils.getNowDate());
        
        if ("rejected".equals(newStatus)) {
            application.setRejectedReason(reviewComment);
        }
        
        int result = notaryApplicationService.updateSysNotaryApplication(application);
        
        // 添加流程记录
        if (result > 0) {
            addProcessLog(notaryId, "review", stepName, newStatus, 
                String.valueOf(reviewerId), reviewComment);
        }
        
        return result;
    }

    /**
     * 开始办理公证
     */
    @Override
    @Transactional
    public int startProcessing(Long notaryId, Long notaryPersonId) {
        SysNotaryApplication application = notaryApplicationService.selectSysNotaryApplicationByApplicationId(notaryId);
        if (application == null) {
            return 0;
        }
        
        // 更新申请状态
        application.setStatus("processing");
        application.setProgress(20);
        application.setNotaryPersonId(notaryPersonId);
        application.setUpdateTime(DateUtils.getNowDate());
        
        int result = notaryApplicationService.updateSysNotaryApplication(application);
        
        // 添加流程记录
        if (result > 0) {
            addProcessLog(notaryId, "process", "开始办理", "processing", 
                String.valueOf(notaryPersonId), "公证员开始办理公证");
        }
        
        return result;
    }

    /**
     * 完成公证
     */
    @Override
    @Transactional
    public int completeNotary(Long notaryId, String certificateUrl, String certificateNo) {
        SysNotaryApplication application = notaryApplicationService.selectSysNotaryApplicationByApplicationId(notaryId);
        if (application == null) {
            return 0;
        }
        
        // 更新申请状态
        application.setStatus("completed");
        application.setProgress(100);
        application.setCompletedTime(DateUtils.getNowDate());
        application.setCertificateUrl(certificateUrl);
        application.setCertificateNo(certificateNo);
        application.setUpdateTime(DateUtils.getNowDate());
        
        // 设置证书有效期（默认1年）
        application.setValidityPeriod(365);
        application.setExpiryDate(DateUtils.addDays(DateUtils.getNowDate(), 365));
        
        int result = notaryApplicationService.updateSysNotaryApplication(application);
        
        // 添加流程记录
        if (result > 0) {
            addProcessLog(notaryId, "complete", "公证完成", "completed", 
                String.valueOf(application.getNotaryPersonId()), "公证办理完成，证书已生成");
        }
        
        return result;
    }

    /**
     * 获取公证流程记录
     */
    @Override
    public List<SysNotaryProcessLog> getProcessLogs(Long notaryId) {
        SysNotaryProcessLog log = new SysNotaryProcessLog();
        log.setNotaryId(notaryId);
        return processLogMapper.selectSysNotaryProcessLogList(log);
    }

    /**
     * 添加流程记录
     */
    @Override
    public int addProcessLog(SysNotaryProcessLog log) {
        log.setOperationTime(DateUtils.getNowDate());
        return processLogMapper.insertSysNotaryProcessLog(log);
    }

    /**
     * 计算公证费用
     */
    @Override
    public Map<String, Object> calculateFee(String type, boolean isUrgent) {
        Map<String, Object> result = new HashMap<>();
        
        // 查询公证类型配置
        SysNotaryTypeConfig queryConfig = new SysNotaryTypeConfig();
        queryConfig.setTypeCode(type);
        List<SysNotaryTypeConfig> configs = typeConfigMapper.selectSysNotaryTypeConfigList(queryConfig);
        SysNotaryTypeConfig config = configs.isEmpty() ? null : configs.get(0);
        if (config == null) {
            result.put("baseFee", BigDecimal.ZERO);
            result.put("urgentFee", BigDecimal.ZERO);
            result.put("totalFee", BigDecimal.ZERO);
            result.put("processingDays", 7);
            return result;
        }
        
        BigDecimal baseFee = config.getBaseFee() != null ? config.getBaseFee() : BigDecimal.ZERO;
        BigDecimal urgentFee = isUrgent ? (config.getUrgentFee() != null ? config.getUrgentFee() : BigDecimal.ZERO) : BigDecimal.ZERO;
        BigDecimal totalFee = baseFee.add(urgentFee);
        
        Integer processingDays = isUrgent ? config.getUrgentDays() : config.getProcessingDays();
        if (processingDays == null) {
            processingDays = isUrgent ? 3 : 7;
        }
        
        result.put("baseFee", baseFee);
        result.put("urgentFee", urgentFee);
        result.put("totalFee", totalFee);
        result.put("processingDays", processingDays);
        
        return result;
    }

    /**
     * 更新公证状态
     */
    @Override
    @Transactional
    public int updateStatus(Long notaryId, String status, Long operatorId, String comment) {
        SysNotaryApplication application = notaryApplicationService.selectSysNotaryApplicationByApplicationId(notaryId);
        if (application == null) {
            return 0;
        }
        
        String oldStatus = application.getStatus();
        application.setStatus(status);
        application.setUpdateTime(DateUtils.getNowDate());
        
        int result = notaryApplicationService.updateSysNotaryApplication(application);
        
        // 添加流程记录
        if (result > 0) {
            addProcessLog(notaryId, "status_change", "状态变更", status, 
                String.valueOf(operatorId), String.format("状态从 %s 变更为 %s", oldStatus, status));
        }
        
        return result;
    }

    /**
     * 获取公证统计信息
     */
    @Override
    public Map<String, Object> getNotaryStats(Long userId) {
        List<Map<String, Object>> statsList = notaryApplicationMapper.countByStatus(userId);
        
        // 统计各状态数量
        Map<String, Object> stats = new HashMap<>();
        for (Map<String, Object> stat : statsList) {
            String status = (String) stat.get("status");
            Long count = ((Number) stat.get("count")).longValue();
            stats.put(status, count.intValue());
        }
        
        int totalNotary = stats.values().stream().mapToInt(v -> (Integer) v).sum();
        int completedNotary = (Integer) stats.getOrDefault("completed", 0);
        int pendingNotary = (Integer) stats.getOrDefault("processing", 0) + 
                           (Integer) stats.getOrDefault("reviewing", 0);
        
        int successRate = totalNotary > 0 ? (completedNotary * 100 / totalNotary) : 0;
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalNotary", totalNotary);
        result.put("completedNotary", completedNotary);
        result.put("pendingNotary", pendingNotary);
        result.put("successRate", successRate);
        
        return result;
    }

    /**
     * 验证公证申请材料
     */
    @Override
    public Map<String, Object> validateApplication(Long notaryId) {
        Map<String, Object> result = new HashMap<>();
        
        SysNotaryApplication application = notaryApplicationService.selectSysNotaryApplicationByApplicationId(notaryId);
        if (application == null) {
            result.put("valid", false);
            result.put("message", "公证申请不存在");
            return result;
        }
        
        // 基本字段验证
        boolean valid = true;
        List<String> errors = new ArrayList<>();
        
        if (StringUtils.isEmpty(application.getTitle())) {
            valid = false;
            errors.add("公证标题不能为空");
        }
        
        if (StringUtils.isEmpty(application.getType())) {
            valid = false;
            errors.add("公证类型不能为空");
        }
        
        if (StringUtils.isEmpty(application.getContactName())) {
            valid = false;
            errors.add("联系人姓名不能为空");
        }
        
        if (StringUtils.isEmpty(application.getContactPhone())) {
            valid = false;
            errors.add("联系电话不能为空");
        }
        
        if (StringUtils.isEmpty(application.getIdCard())) {
            valid = false;
            errors.add("身份证号不能为空");
        }
        
        result.put("valid", valid);
        result.put("errors", errors);
        
        return result;
    }

    /**
     * 发送通知消息
     */
    @Override
    public int sendNotification(Long notaryId, String messageType, String content) {
        // TODO: 实现消息推送功能
        // 可以通过短信、微信、邮件等方式发送通知
        return 1;
    }

    /**
     * 添加流程记录（内部方法）
     */
    private void addProcessLog(Long notaryId, String processStep, String stepName, 
                              String status, String operatorName, String comment) {
        SysNotaryProcessLog log = new SysNotaryProcessLog();
        log.setNotaryId(notaryId);
        log.setProcessStep(processStep);
        log.setStepName(stepName);
        log.setStatus(status);
        log.setOperatorName(operatorName);
        log.setComment(comment);
        log.setOperationTime(DateUtils.getNowDate());
        log.setCreateTime(DateUtils.getNowDate());
        
        processLogMapper.insertSysNotaryProcessLog(log);
    }

    /**
     * 生成公证编号
     */
    private String generateNotaryNo() {
        String dateStr = DateUtils.dateTimeNow("yyyyMMdd");
        String randomStr = String.valueOf(System.currentTimeMillis()).substring(8);
        return "NOTARY" + dateStr + randomStr;
    }
}
