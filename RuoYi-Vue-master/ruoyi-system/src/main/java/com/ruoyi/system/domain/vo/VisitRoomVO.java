package com.ruoyi.system.domain.vo;

/**
 * 房屋拜访信息视图对象
 * 
 * @author ruoyi
 * @date 2025-12-28
 */
public class VisitRoomVO
{
    /** 房产ID */
    private Long propertyId;

    /** 房号 */
    private String roomNumber;

    /** 用户ID */
    private Long userId;

    /** 业主姓名 */
    private String ownerName;

    /** 联系电话 */
    private String phone;

    /** 是否已投票 */
    private Boolean hasVoted;

    /** 短信通知状态: 0-未通知, 1-已通知 */
    private Integer smsNotifyStatus;

    /** 电话通知状态: 0-未通知, 1-已通知 */
    private Integer phoneNotifyStatus;

    /** 线下拜访状态: 0-未送, 3-已送无人, 4-已送未收, 5-已收未投, 6-已投待唱, 7-已唱 */
    private Integer offlineStatus;

    /** 日志记录ID */
    private Long logId;

    public Long getPropertyId() 
    {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) 
    {
        this.propertyId = propertyId;
    }

    public String getRoomNumber() 
    {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) 
    {
        this.roomNumber = roomNumber;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public String getOwnerName() 
    {
        return ownerName;
    }

    public void setOwnerName(String ownerName) 
    {
        this.ownerName = ownerName;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public Boolean getHasVoted() 
    {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) 
    {
        this.hasVoted = hasVoted;
    }

    public Integer getSmsNotifyStatus() 
    {
        return smsNotifyStatus;
    }

    public void setSmsNotifyStatus(Integer smsNotifyStatus) 
    {
        this.smsNotifyStatus = smsNotifyStatus;
    }

    public Integer getPhoneNotifyStatus() 
    {
        return phoneNotifyStatus;
    }

    public void setPhoneNotifyStatus(Integer phoneNotifyStatus) 
    {
        this.phoneNotifyStatus = phoneNotifyStatus;
    }

    public Integer getOfflineStatus() 
    {
        return offlineStatus;
    }

    public void setOfflineStatus(Integer offlineStatus) 
    {
        this.offlineStatus = offlineStatus;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }
}
