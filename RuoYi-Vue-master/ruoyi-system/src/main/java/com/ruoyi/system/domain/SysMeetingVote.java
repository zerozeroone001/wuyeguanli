package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SysMeetingVote extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long voteId;
    private Long meetingId;
    private Long topicId;
    private Long userId;
    private String userName;
    
    /** 投票选项（0同意 1反对 2弃权） */
    private Integer voteOption;
    
    /** 投票方式（0小程序投票 1纸质投票 2语音投票） */
    private Integer voteType;
    
    /** 文件地址/录音地址 */
    private String flieUrl;

    /** 投票编号 */
    private String voteNo;

    /** 会议状态（0未开始 1进行中 2已结束）*/
    private String meetingStatus;

    /** 是否投票（0否 1是）*/
    private Integer isVote;

    // Getters and Setters
    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(Integer voteOption) {
        this.voteOption = voteOption;
    }

    public String getFlieUrl() {
        return flieUrl;
    }

    public void setFlieUrl(String flieUrl) {
        this.flieUrl = flieUrl;
    }

    public String getVoteNo() {
        return voteNo;
    }

    public void setVoteNo(String voteNo) {
        this.voteNo = voteNo;
    }

    public Integer getVoteType() {
        return voteType;
    }

    public void setVoteType(Integer voteType) {
        this.voteType = voteType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(String meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    public Integer getIsVote() {
        return isVote;
    }

    public void setIsVote(Integer isVote) {
        this.isVote = isVote;
    }
}
