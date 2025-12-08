package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.EstateUserProperty;
import com.ruoyi.system.service.IEstateUserPropertyService;

/**
 * �û��뷿�ݹ�������
 */
@RestController
@RequestMapping("/system/estateUserProperty")
public class EstateUserPropertyController extends BaseController
{
    @Autowired
    private IEstateUserPropertyService estateUserPropertyService;

    /**
     * ��ѯ�б�
     */
    @PreAuthorize("@ss.hasPermi('system:property:list')")
    @GetMapping("/list")
    public TableDataInfo list(EstateUserProperty estateUserProperty)
    {
        startPage();
        List<EstateUserProperty> list = estateUserPropertyService.selectEstateUserPropertyList(estateUserProperty);
        return getDataTable(list);
    }

    /**
     * apply
     */
    @PreAuthorize("@ss.hasPermi('system:property:list')")
    @GetMapping("/applyList")
    public TableDataInfo applyList(EstateUserProperty estateUserProperty)
    {
        startPage();
        List<EstateUserProperty> list = estateUserPropertyService.selectEstateUserPropertyList(estateUserProperty);
        return getDataTable(list);
    }

    /**
     * ����
     */
    @PreAuthorize("@ss.hasPermi('system:property:export')")
    @Log(title = "�û����ݹ���", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EstateUserProperty estateUserProperty)
    {
        List<EstateUserProperty> list = estateUserPropertyService.selectEstateUserPropertyList(estateUserProperty);
        ExcelUtil<EstateUserProperty> util = new ExcelUtil<>(EstateUserProperty.class);
        util.exportExcel(response, list, "�û����ݹ�������");
    }

    /**
     * ��ȡ����
     */
    @PreAuthorize("@ss.hasPermi('system:property:query')")
    @GetMapping(value = "/{associationId}")
    public AjaxResult getInfo(@PathVariable Long associationId)
    {
        return success(estateUserPropertyService.selectEstateUserPropertyByAssociationId(associationId));
    }

    /**
     * ����
     */
    @PreAuthorize("@ss.hasPermi('system:property:add')")
    @Log(title = "�û����ݹ���", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EstateUserProperty estateUserProperty)
    {
        return toAjax(estateUserPropertyService.insertEstateUserProperty(estateUserProperty));
    }

    /**
     * �޸�
     */
    @PreAuthorize("@ss.hasPermi('system:property:edit')")
    @Log(title = "�û����ݹ���", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EstateUserProperty estateUserProperty)
    {
        return toAjax(estateUserPropertyService.updateEstateUserProperty(estateUserProperty));
    }

    /**
     * ���
     */
    @PreAuthorize("@ss.hasPermi('system:owner:audit')")
    @Log(title = "�û����ݹ������", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody EstateUserProperty estateUserProperty)
    {
        return toAjax(estateUserPropertyService.auditEstateUserProperty(estateUserProperty));
    }

    /**
     * ɾ��
     */
    @PreAuthorize("@ss.hasPermi('system:property:remove')")
    @Log(title = "�û����ݹ���", businessType = BusinessType.DELETE)
    @DeleteMapping("/{associationIds}")
    public AjaxResult remove(@PathVariable Long[] associationIds)
    {
        return toAjax(estateUserPropertyService.deleteEstateUserPropertyByAssociationIds(associationIds));
    }
}
