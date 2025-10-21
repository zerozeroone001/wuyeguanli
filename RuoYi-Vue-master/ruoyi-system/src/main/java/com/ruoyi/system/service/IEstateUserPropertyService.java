package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.EstateUserProperty;

/**
 * 用户与房屋关联 Service 接口
 *
 * @author ruoyi
 * @date 2025-09-05
 */
public interface IEstateUserPropertyService
{
    /**
     * 根据主键查询
     *
     * @param associationId 关联ID
     * @return 用户房屋关联
     */
    EstateUserProperty selectEstateUserPropertyByAssociationId(Long associationId);

    /**
     * 查询用户房屋关联列表
     *
     * @param estateUserProperty 查询条件
     * @return 关联集合
     */
    List<EstateUserProperty> selectEstateUserPropertyList(EstateUserProperty estateUserProperty);

    /**
     * 新增用户房屋关联
     *
     * @param estateUserProperty 数据
     * @return 影响行数
     */
    int insertEstateUserProperty(EstateUserProperty estateUserProperty);

    /**
     * 修改用户房屋关联
     *
     * @param estateUserProperty 数据
     * @return 影响行数
     */
    int updateEstateUserProperty(EstateUserProperty estateUserProperty);

    /**
     * 批量删除用户房屋关联
     *
     * @param associationIds 主键集合
     * @return 影响行数
     */
    int deleteEstateUserPropertyByAssociationIds(Long[] associationIds);

    /**
     * 删除单条用户房屋关联
     *
     * @param associationId 主键
     * @return 影响行数
     */
    int deleteEstateUserPropertyByAssociationId(Long associationId);

    /**
     * 审核用户房屋关联
     *
     * @param estateUserProperty 审核参数
     * @return 影响行数
     */
    int auditEstateUserProperty(EstateUserProperty estateUserProperty);
}
