package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysInspectionRecord;

public interface SysInspectionRecordMapper {
    public SysInspectionRecord selectSysInspectionRecordByRecordId(Long recordId);
    public List<SysInspectionRecord> selectSysInspectionRecordList(SysInspectionRecord sysInspectionRecord);
    public int insertSysInspectionRecord(SysInspectionRecord sysInspectionRecord);
    public int updateSysInspectionRecord(SysInspectionRecord sysInspectionRecord);
    public int deleteSysInspectionRecordByRecordIds(Long[] recordIds);
}
