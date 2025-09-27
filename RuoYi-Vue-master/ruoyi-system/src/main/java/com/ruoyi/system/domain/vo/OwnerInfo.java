package com.ruoyi.system.domain.vo;

/**
 * 业主基础信息
 * 
 * @author ruoyi
 */
public class OwnerInfo {
    
    /** 业主姓名 */
    private String ownerName;
    
    /** 建筑面积 */
    private Double buildingArea;
    
    /** 房号 */
    private String roomNumber;
    
    /** 电话号码 */
    private String phoneNumber;
    
    /** 代理人姓名 */
    private String agentName;
    
    /** 代理人电话 */
    private String agentPhone;
    
    public OwnerInfo() {
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private OwnerInfo ownerInfo = new OwnerInfo();
        
        public Builder ownerName(String ownerName) {
            ownerInfo.ownerName = ownerName;
            return this;
        }
        
        public Builder buildingArea(Double buildingArea) {
            ownerInfo.buildingArea = buildingArea;
            return this;
        }
        
        public Builder roomNumber(String roomNumber) {
            ownerInfo.roomNumber = roomNumber;
            return this;
        }
        
        public Builder phoneNumber(String phoneNumber) {
            ownerInfo.phoneNumber = phoneNumber;
            return this;
        }
        
        public Builder agentName(String agentName) {
            ownerInfo.agentName = agentName;
            return this;
        }
        
        public Builder agentPhone(String agentPhone) {
            ownerInfo.agentPhone = agentPhone;
            return this;
        }
        
        public OwnerInfo build() {
            return ownerInfo;
        }
    }
    
    // Getters and Setters
    public String getOwnerName() {
        return ownerName;
    }
    
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    public Double getBuildingArea() {
        return buildingArea;
    }
    
    public void setBuildingArea(Double buildingArea) {
        this.buildingArea = buildingArea;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAgentName() {
        return agentName;
    }
    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    
    public String getAgentPhone() {
        return agentPhone;
    }
    
    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }
}