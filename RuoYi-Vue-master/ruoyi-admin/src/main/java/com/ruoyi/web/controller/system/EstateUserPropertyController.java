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
import com.ruoyi.system.domain.EstateUserProperty;
import com.ruoyi.system.service.IEstateUserPropertyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户与房产关系Controller
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
@RestController
@RequestMapping("/system/property")
public class EstateUserPropertyController extends BaseController
{
    @Autowired
    private IEstateUserPropertyService estateUserPropertyService;

    /**
     * 查询用户与房产关系列表
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
     * 导出用户与房产关系列表
     */
    @PreAuthorize("@ss.hasPermi('system:property:export')")
    @Log(title = "用户与房产关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EstateUserProperty estateUserProperty)
    {
        List<EstateUserProperty> list = estateUserPropertyService.selectEstateUserPropertyList(estateUserProperty);
        ExcelUtil<EstateUserProperty> util = new ExcelUtil<EstateUserProperty>(EstateUserProperty.class);
        util.exportExcel(response, list, "用户与房产关系数据");
    }

    /**
     * 获取用户与房产关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:property:query')")
    @GetMapping(value = "/{associationId}")
    public AjaxResult getInfo(@PathVariable("associationId") Long associationId)
    {
        return success(estateUserPropertyService.selectEstateUserPropertyByAssociationId(associationId));
    }

    /**
     * 新增用户与房产关系
     */
    @PreAuthorize("@ss.hasPermi('system:property:add')")
    @Log(title = "用户与房产关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EstateUserProperty estateUserProperty)
    {
        return toAjax(estateUserPropertyService.insertEstateUserProperty(estateUserProperty));
    }

    /**
     * 修改用户与房产关系
     */
    @PreAuthorize("@ss.hasPermi('system:property:edit')")
    @Log(title = "用户与房产关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EstateUserProperty estateUserProperty)
    {
        return toAjax(estateUserPropertyService.updateEstateUserProperty(estateUserProperty));
    }

    /**
     * 删除用户与房产关系
     */
    @PreAuthorize("@ss.hasPermi('system:property:remove')")
    @Log(title = "用户与房产关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{associationIds}")
    public AjaxResult remove(@PathVariable Long[] associationIds)
    {
        return toAjax(estateUserPropertyService.deleteEstateUserPropertyByAssociationIds(associationIds));
    }
}
