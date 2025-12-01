package com.ruoyi.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 业主大会投票统计报告VO (用于PDF导出)
 */
public class MeetingVoteReportVO {

    /** 会议信息 */
    private Long meetingId;
    private String meetingTitle;
    private String communityName;
    private Date meetingTime;
    private String meetingLocation;
    private Date voteStartTime;
    private Date voteEndTime;

    /** 小区统计 */
    private Integer totalOwners;
    private BigDecimal totalArea;
    private Integer participatedOwners;
    private BigDecimal participatedArea;
    private BigDecimal ownerParticipationRate;
    private BigDecimal areaParticipationRate;

    /** 议题统计列表 */
    private List<VoteResultVO> topicResults;

    /** 导出时间 */
    private Date exportTime;

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Date getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Date meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public Date getVoteStartTime() {
        return voteStartTime;
    }

    public void setVoteStartTime(Date voteStartTime) {
        this.voteStartTime = voteStartTime;
    }

    public Date getVoteEndTime() {
        return voteEndTime;
    }

    public void setVoteEndTime(Date voteEndTime) {
        this.voteEndTime = voteEndTime;
    }

    public Integer getTotalOwners() {
        return totalOwners;
    }

    public void setTotalOwners(Integer totalOwners) {
        this.totalOwners = totalOwners;
    }

    public BigDecimal getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    public Integer getParticipatedOwners() {
        return participatedOwners;
    }

    public void setParticipatedOwners(Integer participatedOwners) {
        this.participatedOwners = participatedOwners;
    }

    public BigDecimal getParticipatedArea() {
        return participatedArea;
    }

    public void setParticipatedArea(BigDecimal participatedArea) {
        this.participatedArea = participatedArea;
    }

    public BigDecimal getOwnerParticipationRate() {
        return ownerParticipationRate;
    }

    public void setOwnerParticipationRate(BigDecimal ownerParticipationRate) {
        this.ownerParticipationRate = ownerParticipationRate;
    }

    public BigDecimal getAreaParticipationRate() {
        return areaParticipationRate;
    }

    public void setAreaParticipationRate(BigDecimal areaParticipationRate) {
        this.areaParticipationRate = areaParticipationRate;
    }

    public List<VoteResultVO> getTopicResults() {
        return topicResults;
    }

    public void setTopicResults(List<VoteResultVO> topicResults) {
        this.topicResults = topicResults;
    }

    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }
}
