package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.BuildingVO;
import com.ruoyi.system.domain.vo.VisitRoomVO;
import java.util.List;
import java.util.Map;

/**
 * 上门拜访Service接口
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
public interface IVisitService 
{
    /**
     * 检查当前用户是否为管理员
     * 
     * @param userId 用户ID
     * @return 管理员信息(包含isAdmin, communityId, communityName)
     */
    public Map<String, Object> checkIsAdmin(Long userId);

    /**
     * 获取小区楼栋列表
     * 
     * @param communityId 小区ID
     * @return 楼栋列表
     */
    public List<BuildingVO> getBuildingList(Long communityId);

    /**
     * 获取楼栋的单元列表
     * 
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @return 单元列表
     */
    public List<String> getUnitList(Long communityId, String buildingName);

    /**
     * 获取房屋列表及业主信息
     * 
     * @param communityId 小区ID
     * @param buildingName 楼栋名称
     * @param unitName 单元名称(可选)
     * @param meetingId 会议ID
     * @return 房屋列表
     */
    public List<VisitRoomVO> getRoomList(Long communityId, String buildingName, String unitName, Long meetingId);

    /**
     * 更新线下通知状态
     * 
     * @param meetingId 会议ID
     * @param userId 用户ID
     * @param offlineStatus 线下状态
     * @param visitorId 拜访人ID
     * @param visitorName 拜访人姓名
     * @return 结果
     */
    public int updateOfflineStatus(Long meetingId, Long userId, Integer offlineStatus, Long visitorId, String visitorName);
}
