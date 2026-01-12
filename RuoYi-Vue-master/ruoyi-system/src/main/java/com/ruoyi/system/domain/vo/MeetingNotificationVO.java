package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import java.util.Date;

/**
 * 会议通知记录VO
 */
public class MeetingNotificationVO {

    @Excel(name = "用户姓名")
    private String userName;

    @Excel(name = "联系电话")
    private String phonenumber;

    @Excel(name = "详细地址")
    private String address;

    @Excel(name = "是否投票")
    private String hasVoted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "短信通知时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date smsTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "电话通知时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date phoneTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "纸质票发放时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date paperTime;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(String hasVoted) {
        this.hasVoted = hasVoted;
    }

    public Date getSmsTime() {
        return smsTime;
    }

    public void setSmsTime(Date smsTime) {
        this.smsTime = smsTime;
    }

    public Date getPhoneTime() {
        return phoneTime;
    }

    public void setPhoneTime(Date phoneTime) {
        this.phoneTime = phoneTime;
    }

    public Date getPaperTime() {
        return paperTime;
    }

    public void setPaperTime(Date paperTime) {
        this.paperTime = paperTime;
    }
}
