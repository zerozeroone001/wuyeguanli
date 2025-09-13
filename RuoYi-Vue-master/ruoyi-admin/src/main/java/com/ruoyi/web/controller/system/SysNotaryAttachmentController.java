package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNotaryAttachment;
import com.ruoyi.system.service.ISysNotaryAttachmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 公证附件Controller
 * 
 * @author ruoyi
 * @date 2025-09-11
 */
@RestController
@RequestMapping("/system/attachment")
public class SysNotaryAttachmentController extends BaseController
{
    @Autowired
    private ISysNotaryAttachmentService sysNotaryAttachmentService;

    /**
     * 查询公证附件列表
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotaryAttachment sysNotaryAttachment)
    {
        startPage();
        List<SysNotaryAttachment> list = sysNotaryAttachmentService.selectSysNotaryAttachmentList(sysNotaryAttachment);
        return getDataTable(list);
    }

    /**
     * 导出公证附件列表
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:export')")
    @Log(title = "公证附件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNotaryAttachment sysNotaryAttachment)
    {
        List<SysNotaryAttachment> list = sysNotaryAttachmentService.selectSysNotaryAttachmentList(sysNotaryAttachment);
        ExcelUtil<SysNotaryAttachment> util = new ExcelUtil<SysNotaryAttachment>(SysNotaryAttachment.class);
        util.exportExcel(response, list, "公证附件数据");
    }

    /**
     * 获取公证附件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:query')")
    @GetMapping(value = "/{attachmentId}")
    public AjaxResult getInfo(@PathVariable("attachmentId") Long attachmentId)
    {
        return success(sysNotaryAttachmentService.selectSysNotaryAttachmentByAttachmentId(attachmentId));
    }

    /**
     * 新增公证附件
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:add')")
    @Log(title = "公证附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysNotaryAttachment sysNotaryAttachment)
    {
        return toAjax(sysNotaryAttachmentService.insertSysNotaryAttachment(sysNotaryAttachment));
    }

    /**
     * 修改公证附件
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:edit')")
    @Log(title = "公证附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysNotaryAttachment sysNotaryAttachment)
    {
        return toAjax(sysNotaryAttachmentService.updateSysNotaryAttachment(sysNotaryAttachment));
    }

    /**
     * 删除公证附件
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:remove')")
    @Log(title = "公证附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{attachmentIds}")
    public AjaxResult remove(@PathVariable Long[] attachmentIds)
    {
        return toAjax(sysNotaryAttachmentService.deleteSysNotaryAttachmentByAttachmentIds(attachmentIds));
    }
}
