package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysInspectionRecord;

public interface ISysInspectionRecordService {
    public SysInspectionRecord selectSysInspectionRecordByRecordId(Long recordId);
    public List<SysInspectionRecord> selectSysInspectionRecordList(SysInspectionRecord sysInspectionRecord);
    public int insertSysInspectionRecord(SysInspectionRecord sysInspectionRecord);
    public int updateSysInspectionRecord(SysInspectionRecord sysInspectionRecord);
    public int deleteSysInspectionRecordByRecordIds(Long[] recordIds);
}
