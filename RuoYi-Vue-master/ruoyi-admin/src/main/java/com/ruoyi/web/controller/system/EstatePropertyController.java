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
import com.ruoyi.system.domain.EstateProperty;
import com.ruoyi.system.service.IEstatePropertyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 房产信息Controller
 * 
 * @author ruoyi
 * @date 2025-09-05
 */
@RestController
@RequestMapping("/system/property")
public class EstatePropertyController extends BaseController
{
    @Autowired
    private IEstatePropertyService estatePropertyService;

    /**
     * 查询房产信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:property:list')")
    @GetMapping("/list")
    public TableDataInfo list(EstateProperty estateProperty)
    {
        startPage();
        List<EstateProperty> list = estatePropertyService.selectEstatePropertyList(estateProperty);
        return getDataTable(list);
    }

    /**
     * 导出房产信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:property:export')")
    @Log(title = "房产信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EstateProperty estateProperty)
    {
        List<EstateProperty> list = estatePropertyService.selectEstatePropertyList(estateProperty);
        ExcelUtil<EstateProperty> util = new ExcelUtil<EstateProperty>(EstateProperty.class);
        util.exportExcel(response, list, "房产信息数据");
    }

    /**
     * 获取房产信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:property:query')")
    @GetMapping(value = "/{propertyId}")
    public AjaxResult getInfo(@PathVariable("propertyId") Long propertyId)
    {
        return success(estatePropertyService.selectEstatePropertyByPropertyId(propertyId));
    }

    /**
     * 新增房产信息
     */
    @PreAuthorize("@ss.hasPermi('system:property:add')")
    @Log(title = "房产信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EstateProperty estateProperty)
    {
        return toAjax(estatePropertyService.insertEstateProperty(estateProperty));
    }

    /**
     * 修改房产信息
     */
    @PreAuthorize("@ss.hasPermi('system:property:edit')")
    @Log(title = "房产信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EstateProperty estateProperty)
    {
        return toAjax(estatePropertyService.updateEstateProperty(estateProperty));
    }

    /**
     * 删除房产信息
     */
    @PreAuthorize("@ss.hasPermi('system:property:remove')")
    @Log(title = "房产信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{propertyIds}")
    public AjaxResult remove(@PathVariable Long[] propertyIds)
    {
        return toAjax(estatePropertyService.deleteEstatePropertyByPropertyIds(propertyIds));
    }

    /**
     * 查询小区下的楼栋列表
     */
    @GetMapping("/buildings")
    public AjaxResult getBuildings(Long communityId)
    {
        return success(estatePropertyService.selectBuildingNamesByCommunityId(communityId));
    }

    /**
     * 查询楼栋下的房号列表
     */
    @GetMapping("/rooms")
    public AjaxResult getRooms(Long communityId, String buildingName, Long ownerId)
    {
        return success(estatePropertyService.selectRoomNumbersByBuildingName(communityId, buildingName, ownerId));
    }
}
