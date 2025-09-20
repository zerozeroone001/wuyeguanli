package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysInspectionPlanMapper;
import com.ruoyi.system.domain.SysInspectionPlan;
import com.ruoyi.system.service.ISysInspectionPlanService;

@Service
public class SysInspectionPlanServiceImpl implements ISysInspectionPlanService {
    @Autowired
    private SysInspectionPlanMapper sysInspectionPlanMapper;

    @Override
    public SysInspectionPlan selectSysInspectionPlanByPlanId(Long planId) {
        return sysInspectionPlanMapper.selectSysInspectionPlanByPlanId(planId);
    }

    @Override
    public List<SysInspectionPlan> selectSysInspectionPlanList(SysInspectionPlan sysInspectionPlan) {
        return sysInspectionPlanMapper.selectSysInspectionPlanList(sysInspectionPlan);
    }

    @Override
    public int insertSysInspectionPlan(SysInspectionPlan sysInspectionPlan) {
        sysInspectionPlan.setCreateTime(DateUtils.getNowDate());
        return sysInspectionPlanMapper.insertSysInspectionPlan(sysInspectionPlan);
    }

    @Override
    public int updateSysInspectionPlan(SysInspectionPlan sysInspectionPlan) {
        sysInspectionPlan.setUpdateTime(DateUtils.getNowDate());
        return sysInspectionPlanMapper.updateSysInspectionPlan(sysInspectionPlan);
    }

    @Override
    public int deleteSysInspectionPlanByPlanIds(Long[] planIds) {
        return sysInspectionPlanMapper.deleteSysInspectionPlanByPlanIds(planIds);
    }
}
