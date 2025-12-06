package com.ruoyi.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投票记录视图对象
 */
public class VoteRecordVO {

    /** 用户ID */
    private Long userId;

    /** 业主ID */
    private Long ownerId;

    /** 用户名 */
    private String userName;

    /** 真实姓名 */
    private String realName;

    /** 房号 */
    private String roomNumber;

    /** 建筑面积 */
    private BigDecimal area;

    /** 手机号 */
    private String phonenumber;

    /** 票权状态 (0正常 1冻结) */
    private String voteRightStatus;

    /** 投票状态 (0未投票 1已投票) */
    private String voterStatus;

    /** 认证状态 (0未认证 1待审核 2已认证 3认证失败) */
    private Integer authStatus;

    /** 实际票权 (建筑面积) */
    private BigDecimal actualVoteRight;

    /** 参与时间 */
    private Date participationTime;

    /** 投票选项 (0同意 1反对 2弃权) */
    private Integer voteOption;

    /** 投票方式 (0小程序投票 1纸质投票 2语音投票) */
    private Integer voteType;

    /** 投票编号 */
    private String voteNo;

    /** 委托状态 */
    private String proxyStatus;

    /** 是否业委会成员 */
    private String isCommitteeMember;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getVoteRightStatus() {
        return voteRightStatus;
    }

    public void setVoteRightStatus(String voteRightStatus) {
        this.voteRightStatus = voteRightStatus;
    }

    public String getVoterStatus() {
        return voterStatus;
    }

    public void setVoterStatus(String voterStatus) {
        this.voterStatus = voterStatus;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public BigDecimal getActualVoteRight() {
        return actualVoteRight;
    }

    public void setActualVoteRight(BigDecimal actualVoteRight) {
        this.actualVoteRight = actualVoteRight;
    }

    public Date getParticipationTime() {
        return participationTime;
    }

    public void setParticipationTime(Date participationTime) {
        this.participationTime = participationTime;
    }

    public Integer getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(Integer voteOption) {
        this.voteOption = voteOption;
    }

    public Integer getVoteType() {
        return voteType;
    }

    public void setVoteType(Integer voteType) {
        this.voteType = voteType;
    }

    public String getVoteNo() {
        return voteNo;
    }

    public void setVoteNo(String voteNo) {
        this.voteNo = voteNo;
    }

    public String getProxyStatus() {
        return proxyStatus;
    }

    public void setProxyStatus(String proxyStatus) {
        this.proxyStatus = proxyStatus;
    }

    public String getIsCommitteeMember() {
        return isCommitteeMember;
    }

    public void setIsCommitteeMember(String isCommitteeMember) {
        this.isCommitteeMember = isCommitteeMember;
    }
}
