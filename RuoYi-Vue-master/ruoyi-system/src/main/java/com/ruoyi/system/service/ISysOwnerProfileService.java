package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.domain.dto.OwnerProfileImportDto;

/**
 * 业主信息扩展Service接口
 * 
 * @author ruoyi
 * @date 2025-09-15
 */
public interface ISysOwnerProfileService 
{
    /**
     * 查询业主信息扩展
     * 
     * @param ownerId 业主信息扩展主键
     * @return 业主信息扩展
     */
    public SysOwnerProfile selectSysOwnerProfileByOwnerId(Long ownerId);

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
    public SysOwnerProfile selectSysOwnerProfileByUserIdAndCommunityId(Long userId, Long communityId);

    /**
     * 查询业主信息扩展列表
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 业主信息扩展集合
     */
    public List<SysOwnerProfile> selectSysOwnerProfileList(SysOwnerProfile sysOwnerProfile);

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
     * 导入业主数据
     * 
     * @param ownerList 业主数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importOwner(List<OwnerProfileImportDto> ownerList, boolean isUpdateSupport, String operName);

    /**
     * 审核业主认证
     * 
     * @param sysOwnerProfile 业主信息扩展
     * @return 结果
     */
    public int auditOwnerProfile(SysOwnerProfile sysOwnerProfile);

    /**
     * 自动绑定用户到业主档案
     * 
     * @param userId 用户ID
     * @param phonenumber 手机号
     * @param userName 真实姓名
     * @return 绑定的业主记录数量
     */
    public int bindUserToOwner(Long userId, String phonenumber, String userName);

    /**
     * 清除用户缓存
     */
    public void clearUserCache(Long userId);

    /**
     * 房产合并与拆分（转移房产）
     *
     * @param sourceUserId 源用户ID
     * @param targetUserId 目标用户ID
     * @param targetPropertyIds 目标用户最终应拥有的房产ID列表（涉及到的房产）
     * @return 结果
     */
    public boolean transferOwnerProperties(Long sourceUserId, Long targetUserId, List<Long> targetPropertyIds);

    /**
     * 为已有业主添加新房产
     *
     * @param ownerId 业主ID
     * @param communityId 小区ID
     * @param buildingNo 楼栋号
     * @param roomNo 房号
     * @return 结果
     */
    public int addPropertyToOwner(Long ownerId, Long communityId, String buildingNo,String UtilNo, String roomNo);

    /**
     * 根据用户联系电话、用户姓名、小区ID查询单条业主信息
     * @param phonenumber 用户联系电话
     * @param userName 用户姓名
     * @return 业主信息扩展
     */
    public SysOwnerProfile selectSysOwnerProfileByPhoneNameCommunity(String phonenumber, String userName);
}