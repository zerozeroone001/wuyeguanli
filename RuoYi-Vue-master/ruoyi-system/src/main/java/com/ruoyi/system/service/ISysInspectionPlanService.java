package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysInspectionPlan;

public interface ISysInspectionPlanService {
    public SysInspectionPlan selectSysInspectionPlanByPlanId(Long planId);
    public List<SysInspectionPlan> selectSysInspectionPlanList(SysInspectionPlan sysInspectionPlan);
    public int insertSysInspectionPlan(SysInspectionPlan sysInspectionPlan);
    public int updateSysInspectionPlan(SysInspectionPlan sysInspectionPlan);
    public int deleteSysInspectionPlanByPlanIds(Long[] planIds);
}
