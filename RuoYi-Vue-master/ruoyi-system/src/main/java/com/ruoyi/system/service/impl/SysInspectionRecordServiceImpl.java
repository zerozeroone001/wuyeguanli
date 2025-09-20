package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysInspectionRecordMapper;
import com.ruoyi.system.domain.SysInspectionRecord;
import com.ruoyi.system.service.ISysInspectionRecordService;

@Service
public class SysInspectionRecordServiceImpl implements ISysInspectionRecordService {
    @Autowired
    private SysInspectionRecordMapper sysInspectionRecordMapper;

    @Override
    public SysInspectionRecord selectSysInspectionRecordByRecordId(Long recordId) {
        return sysInspectionRecordMapper.selectSysInspectionRecordByRecordId(recordId);
    }

    @Override
    public List<SysInspectionRecord> selectSysInspectionRecordList(SysInspectionRecord sysInspectionRecord) {
        return sysInspectionRecordMapper.selectSysInspectionRecordList(sysInspectionRecord);
    }

    @Override
    public int insertSysInspectionRecord(SysInspectionRecord sysInspectionRecord) {
        sysInspectionRecord.setCreateTime(DateUtils.getNowDate());
        return sysInspectionRecordMapper.insertSysInspectionRecord(sysInspectionRecord);
    }

    @Override
    public int updateSysInspectionRecord(SysInspectionRecord sysInspectionRecord) {
        sysInspectionRecord.setUpdateTime(DateUtils.getNowDate());
        return sysInspectionRecordMapper.updateSysInspectionRecord(sysInspectionRecord);
    }

    @Override
    public int deleteSysInspectionRecordByRecordIds(Long[] recordIds) {
        return sysInspectionRecordMapper.deleteSysInspectionRecordByRecordIds(recordIds);
    }
}
