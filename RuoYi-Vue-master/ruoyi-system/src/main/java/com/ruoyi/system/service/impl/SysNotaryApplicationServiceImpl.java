package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysNotaryApplicationMapper;
import com.ruoyi.system.mapper.SysNotaryTypeConfigMapper;
import com.ruoyi.system.mapper.SysNotaryProcessLogMapper;
import com.ruoyi.system.mapper.SysNotaryAttachmentMapper;
import com.ruoyi.system.domain.SysNotaryApplication;
import com.ruoyi.system.domain.SysNotaryTypeConfig;
import com.ruoyi.system.domain.SysNotaryProcessLog;
import com.ruoyi.system.domain.SysNotaryAttachment;
import com.ruoyi.system.service.ISysNotaryApplicationService;

/**
 * 公证服务申请Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysNotaryApplicationServiceImpl implements ISysNotaryApplicationService 
{
    @Autowired
    private SysNotaryApplicationMapper sysNotaryApplicationMapper;
    
    @Autowired
    private SysNotaryTypeConfigMapper sysNotaryTypeConfigMapper;
    
    @Autowired
    private SysNotaryProcessLogMapper sysNotaryProcessLogMapper;
    
    @Autowired
    private SysNotaryAttachmentMapper sysNotaryAttachmentMapper;

    /**
     * 查询公证服务申请
     * 
     * @param applicationId 公证服务申请主键
     * @return 公证服务申请
     */
    @Override
    public SysNotaryApplication selectSysNotaryApplicationByApplicationId(Long applicationId)
    {
        return sysNotaryApplicationMapper.selectSysNotaryApplicationByApplicationId(applicationId);
    }

    /**
     * 查询公证服务申请列表
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 公证服务申请
     */
    @Override
    public List<SysNotaryApplication> selectSysNotaryApplicationList(SysNotaryApplication sysNotaryApplication)
    {
        return sysNotaryApplicationMapper.selectSysNotaryApplicationList(sysNotaryApplication);
    }

    /**
     * 新增公证服务申请
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 结果
     */
    @Override
    public int insertSysNotaryApplication(SysNotaryApplication sysNotaryApplication)
    {
        sysNotaryApplication.setCreateTime(DateUtils.getNowDate());
        return sysNotaryApplicationMapper.insertSysNotaryApplication(sysNotaryApplication);
    }

    /**
     * 修改公证服务申请
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 结果
     */
    @Override
    public int updateSysNotaryApplication(SysNotaryApplication sysNotaryApplication)
    {
        sysNotaryApplication.setUpdateTime(DateUtils.getNowDate());
        return sysNotaryApplicationMapper.updateSysNotaryApplication(sysNotaryApplication);
    }

    /**
     * 批量删除公证服务申请
     * 
     * @param applicationIds 需要删除的公证服务申请主键
     * @return 结果
     */
    @Override
    public int deleteSysNotaryApplicationByApplicationIds(Long[] applicationIds)
    {
        return sysNotaryApplicationMapper.deleteSysNotaryApplicationByApplicationIds(applicationIds);
    }

    /**
     * 删除公证服务申请信息
     * 
     * @param applicationId 公证服务申请主键
     * @return 结果
     */
    @Override
    public int deleteSysNotaryApplicationByApplicationId(Long applicationId)
    {
        return sysNotaryApplicationMapper.deleteSysNotaryApplicationByApplicationId(applicationId);
    }

    @Override
    public java.util.Map<String, Object> getNotaryStatsByUserId(Long userId) {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        long totalNotary = 0;
        long pendingNotary = 0;
        long completedNotary = 0;

        List<java.util.Map<String, Object>> counts = sysNotaryApplicationMapper.countByStatus(userId);

        for (java.util.Map<String, Object> count : counts) {
            String status = (String) count.get("status");
            long num = (long) count.get("count");
            totalNotary += num;
            if ("completed".equals(status)) {
                completedNotary = num;
            } else if ("reviewing".equals(status) || "processing".equals(status)) {
                pendingNotary += num;
            }
        }

        stats.put("totalNotary", totalNotary);
        stats.put("pendingNotary", pendingNotary);
        stats.put("completedNotary", completedNotary);

        if (totalNotary > 0) {
            double successRate = (double) completedNotary / totalNotary * 100;
            stats.put("successRate", Math.round(successRate));
        } else {
            stats.put("successRate", 0);
        }

        return stats;
    }

    /**
     * 获取公证类型配置列表
     */
    @Override
    public List<SysNotaryTypeConfig> getNotaryTypeConfigs() {
        SysNotaryTypeConfig config = new SysNotaryTypeConfig();
        config.setStatus("0"); // 只查询正常状态的配置
        return sysNotaryTypeConfigMapper.selectSysNotaryTypeConfigList(config);
    }

    /**
     * 计算公证费用
     */
    @Override
    public Map<String, Object> calculateFee(String type, Boolean urgent) {
        Map<String, Object> result = new HashMap<>();
        
        // 查询类型配置
        SysNotaryTypeConfig config = new SysNotaryTypeConfig();
        config.setTypeCode(type);
        config.setStatus("0");
        List<SysNotaryTypeConfig> configs = sysNotaryTypeConfigMapper.selectSysNotaryTypeConfigList(config);
        
        if (configs.isEmpty()) {
            result.put("error", "未找到该类型的公证配置");
            return result;
        }
        
        SysNotaryTypeConfig typeConfig = configs.get(0);
        BigDecimal baseFee = typeConfig.getBaseFee();
        BigDecimal urgentFee = urgent ? typeConfig.getUrgentFee() : BigDecimal.ZERO;
        BigDecimal totalFee = baseFee.add(urgentFee);
        
        result.put("baseFee", baseFee);
        result.put("urgentFee", urgentFee);
        result.put("totalFee", totalFee);
        result.put("typeName", typeConfig.getTypeName());
        result.put("processingDays", urgent ? typeConfig.getUrgentDays() : typeConfig.getProcessingDays());
        
        return result;
    }

    /**
     * 获取公证流程日志
     */
    @Override
    public List<SysNotaryProcessLog> getProcessLogs(Long notaryId) {
        SysNotaryProcessLog log = new SysNotaryProcessLog();
        log.setNotaryId(notaryId);
        return sysNotaryProcessLogMapper.selectSysNotaryProcessLogList(log);
    }

    /**
     * 上传公证材料
     */
    @Override
    @Transactional
    public AjaxResult uploadAttachment(Long notaryId, Map<String, Object> params) {
        try {
            String fileName = (String) params.get("fileName");
            String fileUrl = (String) params.get("fileUrl");
            String fileType = (String) params.get("fileType");
            Long fileSize = params.get("fileSize") != null ? Long.valueOf(params.get("fileSize").toString()) : null;
            
            if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(fileUrl) || StringUtils.isEmpty(fileType)) {
                return AjaxResult.error("参数不完整");
            }
            
            // 检查公证申请是否存在
            SysNotaryApplication application = sysNotaryApplicationMapper.selectSysNotaryApplicationByApplicationId(notaryId);
            if (application == null) {
                return AjaxResult.error("公证申请不存在");
            }
            
            // 创建附件记录
            SysNotaryAttachment attachment = new SysNotaryAttachment();
            attachment.setNotaryId(notaryId);
            attachment.setFileName(fileName);
            attachment.setFileUrl(fileUrl);
            attachment.setFileType(fileType);
            attachment.setFileSize(fileSize);
            attachment.setUploadTime(DateUtils.getNowDate());
            attachment.setUploadBy(SecurityUtils.getUsername());
            attachment.setStatus("pending");
            attachment.setCreateTime(DateUtils.getNowDate());
            attachment.setCreateBy(SecurityUtils.getUsername());
            
            int result = sysNotaryAttachmentMapper.insertSysNotaryAttachment(attachment);
            
            if (result > 0) {
                return AjaxResult.success("上传成功", attachment);
            } else {
                return AjaxResult.error("上传失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 删除公证材料
     */
    @Override
    @Transactional
    public int deleteAttachment(Long attachmentId) {
        return sysNotaryAttachmentMapper.deleteSysNotaryAttachmentByAttachmentId(attachmentId);
    }

    /**
     * 提交支付
     */
    @Override
    @Transactional
    public AjaxResult submitPayment(Long notaryId, Map<String, Object> params) {
        try {
            String paymentMethod = (String) params.get("paymentMethod");
            String transactionId = (String) params.get("transactionId");
            
            if (StringUtils.isEmpty(paymentMethod)) {
                return AjaxResult.error("支付方式不能为空");
            }
            
            // 检查公证申请状态
            SysNotaryApplication application = sysNotaryApplicationMapper.selectSysNotaryApplicationByApplicationId(notaryId);
            if (application == null) {
                return AjaxResult.error("公证申请不存在");
            }
            
            if (!"draft".equals(application.getStatus())) {
                return AjaxResult.error("只有草稿状态的申请才能提交支付");
            }
            
            // 更新支付信息
            application.setPaymentMethod(paymentMethod);
            application.setTransactionId(transactionId);
            application.setPaymentStatus("0"); // 未支付
            application.setPaymentTime(DateUtils.getNowDate());
            application.setStatus("reviewing"); // 改为审核中
            application.setUpdateTime(DateUtils.getNowDate());
            application.setUpdateBy(SecurityUtils.getUsername());
            
            int result = sysNotaryApplicationMapper.updateSysNotaryApplication(application);
            
            if (result > 0) {
                // 记录流程日志
                addProcessLog(notaryId, "payment_submitted", "用户提交支付申请", "0");
                
                return AjaxResult.success("支付申请提交成功");
            } else {
                return AjaxResult.error("提交失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 获取支付状态
     */
    @Override
    public Map<String, Object> getPaymentStatus(Long notaryId) {
        Map<String, Object> result = new HashMap<>();
        
        SysNotaryApplication application = sysNotaryApplicationMapper.selectSysNotaryApplicationByApplicationId(notaryId);
        if (application == null) {
            result.put("error", "公证申请不存在");
            return result;
        }
        
        result.put("paymentStatus", application.getPaymentStatus());
        result.put("paymentMethod", application.getPaymentMethod());
        result.put("paymentTime", application.getPaymentTime());
        result.put("transactionId", application.getTransactionId());
        result.put("feeAmount", application.getFeeAmount());
        
        return result;
    }

    /**
     * 审核公证申请
     */
    @Override
    @Transactional
    public int reviewApplication(Long notaryId, String reviewResult, String reviewComment) {
        try {
            SysNotaryApplication application = sysNotaryApplicationMapper.selectSysNotaryApplicationByApplicationId(notaryId);
            if (application == null) {
                return 0;
            }
            
            application.setReviewerId(SecurityUtils.getUserId());
            application.setReviewTime(DateUtils.getNowDate());
            application.setReviewComment(reviewComment);
            application.setUpdateTime(DateUtils.getNowDate());
            application.setUpdateBy(SecurityUtils.getUsername());
            
            if ("approved".equals(reviewResult)) {
                application.setStatus("processing");
                application.setProgress(10);
            } else if ("rejected".equals(reviewResult)) {
                application.setStatus("rejected");
                application.setRejectedReason(reviewComment);
            }
            
            int result = sysNotaryApplicationMapper.updateSysNotaryApplication(application);
            
            if (result > 0) {
                // 记录流程日志
                String logContent = "approved".equals(reviewResult) ? "审核通过，开始办理" : "审核未通过：" + reviewComment;
                String logStatus = "approved".equals(reviewResult) ? "1" : "2";
                addProcessLog(notaryId, "reviewed", logContent, logStatus);
            }
            
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 分配公证员
     */
    @Override
    @Transactional
    public int assignNotary(Long notaryId, Long notaryPersonId) {
        try {
            SysNotaryApplication application = sysNotaryApplicationMapper.selectSysNotaryApplicationByApplicationId(notaryId);
            if (application == null) {
                return 0;
            }
            
            application.setNotaryPersonId(notaryPersonId);
            application.setUpdateTime(DateUtils.getNowDate());
            application.setUpdateBy(SecurityUtils.getUsername());
            
            int result = sysNotaryApplicationMapper.updateSysNotaryApplication(application);
            
            if (result > 0) {
                addProcessLog(notaryId, "assigned", "已分配公证员，ID：" + notaryPersonId, "1");
            }
            
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 更新公证进度
     */
    @Override
    @Transactional
    public int updateProgress(Long notaryId, Integer progress, String comment) {
        try {
            SysNotaryApplication application = sysNotaryApplicationMapper.selectSysNotaryApplicationByApplicationId(notaryId);
            if (application == null) {
                return 0;
            }
            
            application.setProgress(progress);
            application.setUpdateTime(DateUtils.getNowDate());
            application.setUpdateBy(SecurityUtils.getUsername());
            
            int result = sysNotaryApplicationMapper.updateSysNotaryApplication(application);
            
            if (result > 0) {
                addProcessLog(notaryId, "progress_updated", "进度更新：" + progress + "% - " + comment, "1");
            }
            
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 完成公证
     */
    @Override
    @Transactional
    public int completeNotary(Long notaryId, String certificateNo, String certificateUrl, String comment) {
        try {
            SysNotaryApplication application = sysNotaryApplicationMapper.selectSysNotaryApplicationByApplicationId(notaryId);
            if (application == null) {
                return 0;
            }
            
            application.setStatus("completed");
            application.setProgress(100);
            application.setCertificateNo(certificateNo);
            application.setCertificateUrl(certificateUrl);
            application.setCompletedTime(DateUtils.getNowDate());
            application.setUpdateTime(DateUtils.getNowDate());
            application.setUpdateBy(SecurityUtils.getUsername());
            
            int result = sysNotaryApplicationMapper.updateSysNotaryApplication(application);
            
            if (result > 0) {
                addProcessLog(notaryId, "completed", "公证完成，证书编号：" + certificateNo + " - " + comment, "1");
            }
            
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取管理端统计数据
     */
    @Override
    public Map<String, Object> getNotaryManagementStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计各状态数量
        List<Map<String, Object>> statusCounts = sysNotaryApplicationMapper.countAllByStatus();
        
        long totalCount = 0;
        long draftCount = 0;
        long reviewingCount = 0;
        long processingCount = 0;
        long completedCount = 0;
        long rejectedCount = 0;
        
        for (Map<String, Object> count : statusCounts) {
            String status = (String) count.get("status");
            long num = ((Number) count.get("count")).longValue();
            totalCount += num;
            
            switch (status) {
                case "draft":
                    draftCount = num;
                    break;
                case "reviewing":
                    reviewingCount = num;
                    break;
                case "processing":
                    processingCount = num;
                    break;
                case "completed":
                    completedCount = num;
                    break;
                case "rejected":
                    rejectedCount = num;
                    break;
            }
        }
        
        stats.put("totalCount", totalCount);
        stats.put("draftCount", draftCount);
        stats.put("reviewingCount", reviewingCount);
        stats.put("processingCount", processingCount);
        stats.put("completedCount", completedCount);
        stats.put("rejectedCount", rejectedCount);
        
        // 计算成功率
        if (totalCount > 0) {
            double successRate = (double) completedCount / totalCount * 100;
            stats.put("successRate", Math.round(successRate * 100.0) / 100.0);
        } else {
            stats.put("successRate", 0.0);
        }
        
        // 本月新增
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date monthStart = calendar.getTime();
        
        SysNotaryApplication query = new SysNotaryApplication();
        query.setCreateTime(monthStart);
        List<SysNotaryApplication> monthApplications = sysNotaryApplicationMapper.selectSysNotaryApplicationList(query);
        stats.put("monthlyCount", monthApplications.size());
        
        return stats;
    }

    /**
     * 批量操作公证申请
     */
    @Override
    @Transactional
    public int batchOperation(Long[] notaryIds, String operation, String comment) {
        int successCount = 0;
        
        for (Long notaryId : notaryIds) {
            try {
                switch (operation) {
                    case "approve":
                        successCount += reviewApplication(notaryId, "approved", comment);
                        break;
                    case "reject":
                        successCount += reviewApplication(notaryId, "rejected", comment);
                        break;
                    case "delete":
                        successCount += deleteSysNotaryApplicationByApplicationId(notaryId);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                // 继续处理其他记录
                continue;
            }
        }
        
        return successCount;
    }

    /**
     * 更新公证类型配置
     */
    @Override
    @Transactional
    public int updateTypeConfig(Map<String, Object> config) {
        try {
            Long configId = Long.valueOf(config.get("configId").toString());
            SysNotaryTypeConfig typeConfig = sysNotaryTypeConfigMapper.selectSysNotaryTypeConfigByConfigId(configId);
            
            if (typeConfig == null) {
                return 0;
            }
            
            // 更新配置信息
            if (config.get("baseFee") != null) {
                typeConfig.setBaseFee(new BigDecimal(config.get("baseFee").toString()));
            }
            if (config.get("urgentFee") != null) {
                typeConfig.setUrgentFee(new BigDecimal(config.get("urgentFee").toString()));
            }
            if (config.get("processingDays") != null) {
                typeConfig.setProcessingDays(Integer.valueOf(config.get("processingDays").toString()));
            }
            if (config.get("urgentDays") != null) {
                typeConfig.setUrgentDays(Integer.valueOf(config.get("urgentDays").toString()));
            }
            if (config.get("status") != null) {
                typeConfig.setStatus(config.get("status").toString());
            }
            
            typeConfig.setUpdateTime(DateUtils.getNowDate());
            typeConfig.setUpdateBy(SecurityUtils.getUsername());
            
            return sysNotaryTypeConfigMapper.updateSysNotaryTypeConfig(typeConfig);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 添加流程日志
     */
    private void addProcessLog(Long notaryId, String action, String content, String status) {
        try {
            SysNotaryProcessLog log = new SysNotaryProcessLog();
            log.setNotaryId(notaryId);
            log.setAction(action);
            log.setContent(content);
            log.setStatus(status);
            log.setOperatorId(SecurityUtils.getUserId());
            log.setOperatorName(SecurityUtils.getUsername());
            log.setOperateTime(DateUtils.getNowDate());
            log.setCreateTime(DateUtils.getNowDate());
            log.setCreateBy(SecurityUtils.getUsername());
            
            sysNotaryProcessLogMapper.insertSysNotaryProcessLog(log);
        } catch (Exception e) {
            // 日志记录失败不影响主流程
        }
    }
}
