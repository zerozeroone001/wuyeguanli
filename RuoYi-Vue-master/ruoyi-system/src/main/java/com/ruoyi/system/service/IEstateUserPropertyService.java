package com.ruoyi.system.service;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.ruoyi.system.domain.EstateUserProperty;

/**
 * �û��뷿�ݹ��� Service �ӿ�
 *
 * @author ruoyi
 * @date 2025-09-05
 */
public interface IEstateUserPropertyService
{
    /**
     * ����������ѯ
     *
     * @param associationId ����ID
     * @return �û����ݹ���
     */
    EstateUserProperty selectEstateUserPropertyByAssociationId(Long associationId);

    /**
     * ��ѯ�û����ݹ����б�
     *
     * @param estateUserProperty ��ѯ����
     * @return ��������
     */
    List<EstateUserProperty> selectEstateUserPropertyList(EstateUserProperty estateUserProperty);

    /**
     * �����û����ݹ���
     *
     * @param estateUserProperty ����
     * @return Ӱ������
     */
    int insertEstateUserProperty(EstateUserProperty estateUserProperty);

    /**
     * �޸��û����ݹ���
     *
     * @param estateUserProperty ����
     * @return Ӱ������
     */
    int updateEstateUserProperty(EstateUserProperty estateUserProperty);

    /**
     * ����ɾ���û����ݹ���
     *
     * @param associationIds ��������
     * @return Ӱ������
     */
    int deleteEstateUserPropertyByAssociationIds(Long[] associationIds);

    /**
     * ɾ�������û����ݹ���
     *
     * @param associationId ����
     * @return Ӱ������
     */
    int deleteEstateUserPropertyByAssociationId(Long associationId);

    /**
     * ����û����ݹ���
     *
     * @param estateUserProperty ��˲���
     * @return Ӱ������
     */
    int auditEstateUserProperty(EstateUserProperty estateUserProperty);
    /**
     * 根据用户ID和房产ID删除房产关系
     *
     * @param userId 用户ID
     * @param propertyId 房产ID
     * @return 结果
     */
    int deleteEstateUserPropertyByUserIdAndPropertyId(@Param("userId") Long userId, @Param("propertyId") Long propertyId);

    /**
     * 根据用户ID和communityId删除房产关系
     * @param userId 用户ID
     * @param communityId 小区ID
     * @return 结果
     */
    int deleteEstateUserPropertyByUserIdAndCommunityId(@Param("userId") Long userId, @Param("communityId") Long communityId);
}
