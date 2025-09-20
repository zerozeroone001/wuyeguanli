package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysInspectionRecord;
import com.ruoyi.system.service.ISysInspectionRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/system/inspectionRecord")
public class SysInspectionRecordController extends BaseController {
    @Autowired
    private ISysInspectionRecordService inspectionRecordService;

    @PreAuthorize("@ss.hasPermi('system:inspectionRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysInspectionRecord sysInspectionRecord) {
        startPage();
        List<SysInspectionRecord> list = inspectionRecordService.selectSysInspectionRecordList(sysInspectionRecord);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:inspectionRecord:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId) {
        return success(inspectionRecordService.selectSysInspectionRecordByRecordId(recordId));
    }

    @PreAuthorize("@ss.hasPermi('system:inspectionRecord:add')")
    @Log(title = "检测记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysInspectionRecord sysInspectionRecord) {
        return toAjax(inspectionRecordService.insertSysInspectionRecord(sysInspectionRecord));
    }

    @PreAuthorize("@ss.hasPermi('system:inspectionRecord:edit')")
    @Log(title = "检测记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysInspectionRecord sysInspectionRecord) {
        return toAjax(inspectionRecordService.updateSysInspectionRecord(sysInspectionRecord));
    }

    @PreAuthorize("@ss.hasPermi('system:inspectionRecord:remove')")
    @Log(title = "检测记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds) {
        return toAjax(inspectionRecordService.deleteSysInspectionRecordByRecordIds(recordIds));
    }
}
