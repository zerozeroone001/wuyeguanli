package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 业主投票列表导出VO
 */
public class VoteListExportVO {

    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
    private Integer rowNum;

    @Excel(name = "房号")
    private String roomNumber;

    @Excel(name = "业主姓名")
    private String userName;

    @Excel(name = "手机号")
    private String phonenumber;

    @Excel(name = "建筑面积(㎡)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal area;

    @Excel(name = "认证状态", readConverterExp = "0=未认证,1=待审核,2=已认证,3=认证失败")
    private Integer authStatus;

    @Excel(name = "投票状态")
    private String voterStatus;

    @Excel(name = "投票时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date participationTime;

    @Excel(name = "投票方式", readConverterExp = "0=小程序,1=纸质,2=语音")
    private Integer voteType;

    // 动态议题列(在Service中动态添加)
    private String topicVotes;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public String getVoterStatus() {
        return voterStatus;
    }

    public void setVoterStatus(String voterStatus) {
        this.voterStatus = voterStatus;
    }

    public Date getParticipationTime() {
        return participationTime;
    }

    public void setParticipationTime(Date participationTime) {
        this.participationTime = participationTime;
    }

    public Integer getVoteType() {
        return voteType;
    }

    public void setVoteType(Integer voteType) {
        this.voteType = voteType;
    }

    public String getTopicVotes() {
        return topicVotes;
    }

    public void setTopicVotes(String topicVotes) {
        this.topicVotes = topicVotes;
    }
}
