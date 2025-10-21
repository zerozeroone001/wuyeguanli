package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysNotaryApplication;

/**
 * 公证服务申请Service接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface ISysNotaryApplicationService 
{
    /**
     * 查询公证服务申请
     * 
     * @param applicationId 公证服务申请主键
     * @return 公证服务申请
     */
    public SysNotaryApplication selectSysNotaryApplicationByApplicationId(Long applicationId);

    /**
     * 查询公证服务申请列表
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 公证服务申请集合
     */
    public List<SysNotaryApplication> selectSysNotaryApplicationList(SysNotaryApplication sysNotaryApplication);

    /**
     * 新增公证服务申请
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 结果
     */
    public int insertSysNotaryApplication(SysNotaryApplication sysNotaryApplication);

    /**
     * 修改公证服务申请
     * 
     * @param sysNotaryApplication 公证服务申请
     * @return 结果
     */
    public int updateSysNotaryApplication(SysNotaryApplication sysNotaryApplication);

    /**
     * 批量删除公证服务申请
     * 
     * @param applicationIds 需要删除的公证服务申请主键集合
     * @return 结果
     */
    public int deleteSysNotaryApplicationByApplicationIds(Long[] applicationIds);

    /**
     * 删除公证服务申请信息
     * 
     * @param applicationId 公证服务申请主键
     * @return 结果
     */
    public int deleteSysNotaryApplicationByApplicationId(Long applicationId);

    /**
     * 根据用户ID获取公证统计信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    public java.util.Map<String, Object> getNotaryStatsByUserId(Long userId);

    /**
     * 获取公证类型配置列表
     *
     * @return 配置列表
     */
    public List<com.ruoyi.system.domain.SysNotaryTypeConfig> getNotaryTypeConfigs();

    /**
     * 计算公证费用
     *
     * @param type 公证类型
     * @param urgent 是否加急
     * @return 费用信息
     */
    public java.util.Map<String, Object> calculateFee(String type, Boolean urgent);

    /**
     * 获取公证流程日志
     *
     * @param notaryId 公证ID
     * @return 流程日志列表
     */
    public List<com.ruoyi.system.domain.SysNotaryProcessLog> getProcessLogs(Long notaryId);

    /**
     * 上传公证材料
     *
     * @param notaryId 公证ID
     * @param params 上传参数
     * @return 结果
     */
    public AjaxResult uploadAttachment(Long notaryId, java.util.Map<String, Object> params);

    /**
     * 删除公证材料
     *
     * @param attachmentId 材料ID
     * @return 结果
     */
    public int deleteAttachment(Long attachmentId);

    /**
     * 提交支付
     *
     * @param notaryId 公证ID
     * @param params 支付参数
     * @return 结果
     */
    public AjaxResult submitPayment(Long notaryId, java.util.Map<String, Object> params);

    /**
     * 获取支付状态
     *
     * @param notaryId 公证ID
     * @return 支付状态
     */
    public java.util.Map<String, Object> getPaymentStatus(Long notaryId);

    /**
     * 审核公证申请
     *
     * @param notaryId 公证ID
     * @param reviewResult 审核结果
     * @param reviewComment 审核意见
     * @return 结果
     */
    public int reviewApplication(Long notaryId, String reviewResult, String reviewComment);

    /**
     * 分配公证员
     *
     * @param notaryId 公证ID
     * @param notaryPersonId 公证员ID
     * @return 结果
     */
    public int assignNotary(Long notaryId, Long notaryPersonId);

    /**
     * 更新公证进度
     *
     * @param notaryId 公证ID
     * @param progress 进度百分比
     * @param comment 备注
     * @return 结果
     */
    public int updateProgress(Long notaryId, Integer progress, String comment);

    /**
     * 完成公证
     *
     * @param notaryId 公证ID
     * @param certificateNo 证书编号
     * @param certificateUrl 证书URL
     * @param comment 备注
     * @return 结果
     */
    public int completeNotary(Long notaryId, String certificateNo, String certificateUrl, String comment);

    /**
     * 获取管理端统计数据
     *
     * @return 统计数据
     */
    public java.util.Map<String, Object> getNotaryManagementStats();

    /**
     * 批量操作公证申请
     *
     * @param notaryIds 公证ID数组
     * @param operation 操作类型
     * @param comment 备注
     * @return 结果
     */
    public int batchOperation(Long[] notaryIds, String operation, String comment);

    /**
     * 更新公证类型配置
     *
     * @param config 配置信息
     * @return 结果
     */
    public int updateTypeConfig(java.util.Map<String, Object> config);
}
