package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class SysInspectionRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long recordId;
    private Long planId;
    private String location;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inspectionDate;
    private String items; // JSON String
    private String results; // JSON String
    private String conclusion;
    private String rectificationNotice;
    private String status;
    private String attachments;
    private String reportContent;

    // Getters and Setters
    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Date getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(Date inspectionDate) { this.inspectionDate = inspectionDate; }
    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }
    public String getResults() { return results; }
    public void setResults(String results) { this.results = results; }
    public String getConclusion() { return conclusion; }
    public void setConclusion(String conclusion) { this.conclusion = conclusion; }
    public String getRectificationNotice() { return rectificationNotice; }
    public void setRectificationNotice(String rectificationNotice) { this.rectificationNotice = rectificationNotice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }
    public String getReportContent() { return reportContent; }
    public void setReportContent(String reportContent) { this.reportContent = reportContent; }
}
