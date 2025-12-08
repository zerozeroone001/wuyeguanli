package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysOwnerProfile;

/**
 * 业主信息扩展Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-15
 */
public interface SysOwnerProfileMapper 
{
    /**
     * 查询业主信息扩展
     * 
     * @param ownerId 业主信息扩展主键
     * @return 业主信息扩展
     */
    public SysOwnerProfile selectSysOwnerProfileByOwnerId(Long ownerId);

    /**
     * 根据身份证号查询业主信息
     * 
     * @param idCardNo 身份证号
     * @return 业主信息扩展
     */
    public SysOwnerProfile selectSysOwnerProfileByIdCardNo(String idCardNo);

    /**
     * 根据用户ID查询业主信息
     * 
     * @param userId 用户ID
     * @return 业主信息扩展
     */
    public SysOwnerProfile selectSysOwnerProfileByUserId(Long userId);

    /**
     * 根据用户ID和小区ID查询业主信息
     * 
     * @param userId 用户ID
     * @param communityId 小区ID
     * @return 业主信息扩展
     */
    public SysOwnerProfile selectSysOwnerProfileByUserIdAndCommunityId(@org.apache.ibatis.annotations.Param("userId") Long userId, @org.apache.ibatis.annotations.Param("communityId") Long communityId);

    /**
     * 检查业主编号是否唯一
     * 
     * @param ownerNo 业主编号
     * @return 结果
     */
    public int checkOwnerNoUnique(String ownerNo);

    /**
     * 查询业主信息扩展列表
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 业主信息扩展集合
     */
    public List<SysOwnerProfile> selectSysOwnerProfileList(SysOwnerProfile sysOwnerProfile);

    /**
     * 查询业主信息扩展列表（未分组，用于自动匹配）
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 业主信息扩展集合
     */
    public List<SysOwnerProfile> selectSysOwnerProfileRawList(SysOwnerProfile sysOwnerProfile);

    /**
     * 新增业主信息扩展
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 结果
     */
    public int insertSysOwnerProfile(SysOwnerProfile sysOwnerProfile);

    /**
     * 修改业主信息扩展
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 结果
     */
    public int updateSysOwnerProfile(SysOwnerProfile sysOwnerProfile);

    /**
     * 批量删除业主信息扩展
     * 
     * @param ownerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysOwnerProfileByOwnerIds(Long[] ownerIds);

    /**
     * 批量删除业主信息扩展
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysOwnerProfileByUserIds(Long[] userIds);

    /**
     * 删除业主信息扩展信息
     * 
     * @param ownerId 业主信息扩展主键
     * @return 结果
     */
    public int deleteSysOwnerProfileByOwnerId(Long ownerId);

    /**
     * 根据用户联系电话、用户姓名、小区ID查询单条业主信息
     * @param phonenumber 用户联系电话
     * @param userName 用户姓名 (实际对应u.nick_name)
     * @return 业主信息扩展
     */
    public SysOwnerProfile selectSysOwnerProfileByPhoneNameCommunity(
            @org.apache.ibatis.annotations.Param("phonenumber") String phonenumber,
            @org.apache.ibatis.annotations.Param("userName") String userName);
}
