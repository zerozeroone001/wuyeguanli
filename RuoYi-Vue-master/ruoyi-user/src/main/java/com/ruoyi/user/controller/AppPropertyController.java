
package com.ruoyi.user.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.EstateCommunity;
import com.ruoyi.system.domain.EstateProperty;
import com.ruoyi.system.domain.EstateUserProperty;
import com.ruoyi.system.service.IEstateCommunityService;
import com.ruoyi.system.service.IEstatePropertyService;
import com.ruoyi.system.service.IEstateUserPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端App房产相关接口
 * 
 * @author gemini
 */
@RestController
@RequestMapping("/app")
public class AppPropertyController extends BaseController {

    @Autowired
    private IEstateCommunityService estateCommunityService;

    @Autowired
    private IEstatePropertyService estatePropertyService;

    @Autowired
    private IEstateUserPropertyService estateUserPropertyService;

    /**
     * 查询小区信息列表 (对用户开放)
     */
    @GetMapping("/community/list")
    public TableDataInfo listCommunities(EstateCommunity estateCommunity) {
        startPage();
        List<EstateCommunity> list = estateCommunityService.selectEstateCommunityList(estateCommunity);
        return getDataTable(list);
    }

    /**
     * 查询房产信息列表 (对用户开放)
     */
    @GetMapping("/property/list")
    public TableDataInfo listProperties(EstateProperty estateProperty) {
        startPage();
        List<EstateProperty> list = estatePropertyService.selectEstatePropertyList(estateProperty);
        return getDataTable(list);
    }
    
    /**
     * 获取房产详细信息 (对用户开放)
     */
    @GetMapping(value = "/property/{propertyId}")
    public AjaxResult getPropertyInfo(@PathVariable("propertyId") Long propertyId)
    {
        return success(estatePropertyService.selectEstatePropertyByPropertyId(propertyId));
    }

    /**
     * 查询当前用户的房产关系列表 (对用户开放)
     */
    @GetMapping("/user-property/list")
    public TableDataInfo listUserProperties(EstateUserProperty estateUserProperty) {
        // 强制查询当前登录用户的
        estateUserProperty.setUserId(SecurityUtils.getUserId());
        startPage();
        List<EstateUserProperty> list = estateUserPropertyService.selectEstateUserPropertyList(estateUserProperty);
        return getDataTable(list);
    }

    /**
     * 新增用户与房产关系 (提交审核)
     */
    @PostMapping("/user-property/add")
    public AjaxResult addUserProperty(@RequestBody EstateUserProperty estateUserProperty) {
        // 根据propertyId查询完整的房产信息
        EstateProperty property = estatePropertyService.selectEstatePropertyByPropertyId(estateUserProperty.getPropertyId());
        if (property == null) {
            return AjaxResult.error("提交失败，选择的房产不存在");
        }

        Long currentUserId = SecurityUtils.getUserId();

        // 检查当前用户是否已经提交过该房产的认证申请
        EstateUserProperty userQuery = new EstateUserProperty();
        userQuery.setUserId(currentUserId);
        userQuery.setPropertyId(estateUserProperty.getPropertyId());
        List<EstateUserProperty> userExistingList = estateUserPropertyService.selectEstateUserPropertyList(userQuery);

        if (userExistingList != null && !userExistingList.isEmpty()) {
            return AjaxResult.error("您已提交过该房产的认证申请，请勿重复提交");
        }

        // 检查该房产是否已被其他用户绑定（审核通过的记录）
        EstateUserProperty propertyQuery = new EstateUserProperty();
        propertyQuery.setPropertyId(estateUserProperty.getPropertyId());
        propertyQuery.setStatus("1"); // 1-审核通过
        List<EstateUserProperty> propertyExistingList = estateUserPropertyService.selectEstateUserPropertyList(propertyQuery);

        if (propertyExistingList != null && !propertyExistingList.isEmpty()) {
            // 获取房产地址信息用于提示
            String roomInfo = property.getBuildingName() + property.getUnitName() + property.getRoomNumber();
            return AjaxResult.error("该房产(" + roomInfo + ")已被其他用户绑定，如有疑问请联系物业管理处");
        }

        // 设置当前用户ID、小区ID和默认状态
        estateUserProperty.setUserId(currentUserId);
        estateUserProperty.setCommunityId(property.getCommunityId()); // 从查询到的房产信息中获取communityId
        estateUserProperty.setStatus("0"); // 0-待审核
        return toAjax(estateUserPropertyService.insertEstateUserProperty(estateUserProperty));
    }
}

