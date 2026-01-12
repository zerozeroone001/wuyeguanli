package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 承接查验记录对象 sys_inspection_record
 *
 * @author ruoyi
 */
public class SysInspectionRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 关联合同ID */
    private Long contractId;

    /** 合同名称（用于显示，非数据库字段） */
    private String contractName;

    /** 数据状态：0-草稿 1-已提交 */
    private String status;

    // ============= 文件上传字段 =============

    /** 1. 承接查验报告及法律意见书封面及目录 */
    private String coverTocUrl;

    /** 2. 前言 */
    private String prefaceUrl;

    /** 3. 物业服务合同解除终止查验报告 */
    private String terminationReportUrl;

    /** 4. 物业服务人进场前承接查验清单 */
    private String entryChecklistUrl;

    /** 5. 物业服务合同履行告知清单 */
    private String performanceNoticeUrl;

    /** 6. 物业服务整改通知单 */
    private String rectificationNoticeUrl;

    /** 7. 物业服务整改结果告知单 */
    private String rectificationResultUrl;

    /** 8. 物业服务履行结果评定通知书 */
    private String assessmentNoticeUrl;

    /** 9. 物业服务合同履行年度报告 */
    private String annualReportUrl;

    /** 10. 物业服务合同解除终止查验报告（第二份） */
    private String terminationReportUrl2;

    /** 11. 查验结论分析 */
    private String conclusionAnalysisUrl;

    /** 12. 物业服务项目承接查验法律意见书 */
    private String legalOpinionUrl;

    // ============= Getters and Setters =============

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverTocUrl() {
        return coverTocUrl;
    }

    public void setCoverTocUrl(String coverTocUrl) {
        this.coverTocUrl = coverTocUrl;
    }

    public String getPrefaceUrl() {
        return prefaceUrl;
    }

    public void setPrefaceUrl(String prefaceUrl) {
        this.prefaceUrl = prefaceUrl;
    }

    public String getTerminationReportUrl() {
        return terminationReportUrl;
    }

    public void setTerminationReportUrl(String terminationReportUrl) {
        this.terminationReportUrl = terminationReportUrl;
    }

    public String getEntryChecklistUrl() {
        return entryChecklistUrl;
    }

    public void setEntryChecklistUrl(String entryChecklistUrl) {
        this.entryChecklistUrl = entryChecklistUrl;
    }

    public String getPerformanceNoticeUrl() {
        return performanceNoticeUrl;
    }

    public void setPerformanceNoticeUrl(String performanceNoticeUrl) {
        this.performanceNoticeUrl = performanceNoticeUrl;
    }

    public String getRectificationNoticeUrl() {
        return rectificationNoticeUrl;
    }

    public void setRectificationNoticeUrl(String rectificationNoticeUrl) {
        this.rectificationNoticeUrl = rectificationNoticeUrl;
    }

    public String getRectificationResultUrl() {
        return rectificationResultUrl;
    }

    public void setRectificationResultUrl(String rectificationResultUrl) {
        this.rectificationResultUrl = rectificationResultUrl;
    }

    public String getAssessmentNoticeUrl() {
        return assessmentNoticeUrl;
    }

    public void setAssessmentNoticeUrl(String assessmentNoticeUrl) {
        this.assessmentNoticeUrl = assessmentNoticeUrl;
    }

    public String getAnnualReportUrl() {
        return annualReportUrl;
    }

    public void setAnnualReportUrl(String annualReportUrl) {
        this.annualReportUrl = annualReportUrl;
    }

    public String getTerminationReportUrl2() {
        return terminationReportUrl2;
    }

    public void setTerminationReportUrl2(String terminationReportUrl2) {
        this.terminationReportUrl2 = terminationReportUrl2;
    }

    public String getConclusionAnalysisUrl() {
        return conclusionAnalysisUrl;
    }

    public void setConclusionAnalysisUrl(String conclusionAnalysisUrl) {
        this.conclusionAnalysisUrl = conclusionAnalysisUrl;
    }

    public String getLegalOpinionUrl() {
        return legalOpinionUrl;
    }

    public void setLegalOpinionUrl(String legalOpinionUrl) {
        this.legalOpinionUrl = legalOpinionUrl;
    }
}
