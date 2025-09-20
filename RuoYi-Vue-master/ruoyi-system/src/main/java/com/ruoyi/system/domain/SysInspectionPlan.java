package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class SysInspectionPlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long planId;
    private String planName;
    private String inspectionType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String inspector;
    private String status;
    private String supervisionTarget;
    private String description;

    // Getters and Setters
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getInspectionType() { return inspectionType; }
    public void setInspectionType(String inspectionType) { this.inspectionType = inspectionType; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getInspector() { return inspector; }
    public void setInspector(String inspector) { this.inspector = inspector; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSupervisionTarget() { return supervisionTarget; }
    public void setSupervisionTarget(String supervisionTarget) { this.supervisionTarget = supervisionTarget; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
