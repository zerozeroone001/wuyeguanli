package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会议管理对象 sys_property_meeting
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class SysPropertyMeeting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会议ID */
    private Long meetingId;

    /** 会议标题 */
    @Excel(name = "会议标题")
    private String meetingTitle;

    /** 子标题 */
    @Excel(name = "子标题")
    private String subTitle;

    /** 小区ID */
    @Excel(name = "小区ID")
    private Long communityId;

    /** 小区名称 */
    private String communityName;

    /** 会议类型 */
    @Excel(name = "会议类型")
    private String meetingType;

    /** 会议标签(1:业主大会,2:招标会议,3:选举会议) */
    @Excel(name = "会议标签", readConverterExp = "1=业主大会,2=招标会议,3=选举会议")
    private Integer meetingTag;

    /** 总选项数(几选几中的总数) */
    @Excel(name = "总选项数")
    private Integer selectionTotal;

    /** 可选数量(几选几中的可选数) */
    @Excel(name = "可选数量")
    private Integer selectionCount;

    /** 会议内容 */
    @Excel(name = "会议内容")
    private String meetingContent;

    /** 会议时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会议时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date meetingTime;

    /** 会议地点 */
    @Excel(name = "会议地点")
    private String meetingLocation;

    /** 会议状态(0-筹备中,1-进行中,2-已结束) */
    @Excel(name = "会议状态(0-筹备中,1-进行中,2-已结束)")
    private String meetingStatus;

    /** 投票开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投票开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voteStartTime;

    /** 投票结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投票结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voteEndTime;

    /** 应参与人数 */
    @Excel(name = "应参与人数")
    private Long totalVoters;

    /** 实际参与人数 */
    @Excel(name = "实际参与人数")
    private Long actualVoters;
    
    /** 投票权总面积 */
    private BigDecimal totalVotingArea;

    /** 已投票面积 */
    private BigDecimal participatedArea;

    /** 投票面积占比 */
    private String votingAreaPercentage;

    /** 是否展示参与人数 */
    @Excel(name = "是否展示参与人数")
    private String showParticipantCount;

    /** 缩略图 */
    @Excel(name = "缩略图")
    private String coverImage;

    /** 是否开启从多选项 */
    @Excel(name = "是否开启从多选项")
    private String congduo;

    /** 选中的房产ID列表(JSON格式) */
    @Excel(name = "选中的房产ID列表")
    private String selectedProperties;

    /** 通过标准(normal:普通,high:高规格) */
    @Excel(name = "通过标准")
    private String passStandard;

    /** 普通标准-参与投票率 */
    @Excel(name = "普通标准-参与投票率")
    private BigDecimal normalParticipationRate;

    /** 普通标准-投票同意率 */
    @Excel(name = "普通标准-投票同意率")
    private BigDecimal normalApprovalRate;

    /** 高规格标准-参与投票率 */
    @Excel(name = "高规格标准-参与投票率")
    private BigDecimal highParticipationRate;

    /** 高规格标准-投票同意率 */
    @Excel(name = "高规格标准-投票同意率")
    private BigDecimal highApprovalRate;


    /** 投票状态 (0:未投票 1:已投票) */
    private String voterStatus;

    /** 删除标志(0代表存在 2代表删除) */
    private String delFlag;

    /** 议题列表 */
    private List<SysPropertyMeetingTopic> topics;

    public void setMeetingId(Long meetingId) 
    {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() 
    {
        return meetingId;
    }

    public void setMeetingTitle(String meetingTitle) 
    {
        this.meetingTitle = meetingTitle;
    }

    public String getMeetingTitle() 
    {
        return meetingTitle;
    }

    public void setSubTitle(String subTitle) 
    {
        this.subTitle = subTitle;
    }

    public String getSubTitle() 
    {
        return subTitle;
    }

    public Long getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(Long communityId)
    {
        this.communityId = communityId;
    }

    public String getCommunityName()
    {
        return communityName;
    }

    public void setCommunityName(String communityName)
    {
        this.communityName = communityName;
    }

    public void setMeetingType(String meetingType) 
    {
        this.meetingType = meetingType;
    }

    public String getMeetingType() 
    {
        return meetingType;
    }

    public void setMeetingTag(Integer meetingTag)
    {
        this.meetingTag = meetingTag;
    }

    public Integer getMeetingTag()
    {
        return meetingTag;
    }

    public void setSelectionTotal(Integer selectionTotal)
    {
        this.selectionTotal = selectionTotal;
    }

    public Integer getSelectionTotal()
    {
        return selectionTotal;
    }

    public void setSelectionCount(Integer selectionCount)
    {
        this.selectionCount = selectionCount;
    }

    public Integer getSelectionCount()
    {
        return selectionCount;
    }

    public void setMeetingContent(String meetingContent) 
    {
        this.meetingContent = meetingContent;
    }

    public String getMeetingContent() 
    {
        return meetingContent;
    }

    public void setMeetingTime(Date meetingTime) 
    {
        this.meetingTime = meetingTime;
    }

    public Date getMeetingTime() 
    {
        return meetingTime;
    }

    public void setMeetingLocation(String meetingLocation) 
    {
        this.meetingLocation = meetingLocation;
    }

    public String getMeetingLocation() 
    {
        return meetingLocation;
    }

    public void setMeetingStatus(String meetingStatus) 
    {
        this.meetingStatus = meetingStatus;
    }

    public String getMeetingStatus() 
    {
        return meetingStatus;
    }

    public void setVoteStartTime(Date voteStartTime) 
    {
        this.voteStartTime = voteStartTime;
    }

    public Date getVoteStartTime() 
    {
        return voteStartTime;
    }

    public void setVoteEndTime(Date voteEndTime) 
    {
        this.voteEndTime = voteEndTime;
    }

    public Date getVoteEndTime() 
    {
        return voteEndTime;
    }

    public void setTotalVoters(Long totalVoters) 
    {
        this.totalVoters = totalVoters;
    }

    public Long getTotalVoters() 
    {
        return totalVoters;
    }

    public void setActualVoters(Long actualVoters) 
    {
        this.actualVoters = actualVoters;
    }

    public Long getActualVoters()
    {
        return actualVoters;
    }
    
    public BigDecimal getTotalVotingArea()
    {
        return totalVotingArea;
    }
    
    public void setTotalVotingArea(BigDecimal totalVotingArea)
    {
        this.totalVotingArea = totalVotingArea;
    }
    
    public BigDecimal getParticipatedArea()
    {
        return participatedArea;
    }
    
    public void setParticipatedArea(BigDecimal participatedArea)
    {
        this.participatedArea = participatedArea;
    }
    
    public String getVotingAreaPercentage()
    {
        return votingAreaPercentage;
    }
    
    public void setVotingAreaPercentage(String votingAreaPercentage)
    {
        this.votingAreaPercentage = votingAreaPercentage;
    }

    public void setShowParticipantCount(String showParticipantCount)
    {
        this.showParticipantCount = showParticipantCount;
    }

    public String getShowParticipantCount()
    {
        return showParticipantCount;
    }

    public void setCoverImage(String coverImage)
    {
        this.coverImage = coverImage;
    }

    public String getCoverImage()
    {
        return coverImage;
    }

    public void setCongduo(String congduo)
    {
        this.congduo = congduo;
    }

    public String getCongduo()
    {
        return congduo;
    }

    public void setSelectedProperties(String selectedProperties)
    {
        this.selectedProperties = selectedProperties;
    }

    public String getSelectedProperties()
    {
        return selectedProperties;
    }

    public void setPassStandard(String passStandard)
    {
        this.passStandard = passStandard;
    }

    public String getPassStandard()
    {
        return passStandard;
    }

    public void setNormalParticipationRate(BigDecimal normalParticipationRate)
    {
        this.normalParticipationRate = normalParticipationRate;
    }

    public BigDecimal getNormalParticipationRate()
    {
        return normalParticipationRate;
    }

    public void setNormalApprovalRate(BigDecimal normalApprovalRate)
    {
        this.normalApprovalRate = normalApprovalRate;
    }

    public BigDecimal getNormalApprovalRate()
    {
        return normalApprovalRate;
    }

    public void setHighParticipationRate(BigDecimal highParticipationRate)
    {
        this.highParticipationRate = highParticipationRate;
    }

    public BigDecimal getHighParticipationRate()
    {
        return highParticipationRate;
    }

    public void setHighApprovalRate(BigDecimal highApprovalRate)
    {
        this.highApprovalRate = highApprovalRate;
    }

    public BigDecimal getHighApprovalRate()
    {
        return highApprovalRate;
    }


    public String getVoterStatus() 
    {
        return voterStatus;
    }

    public void setVoterStatus(String voterStatus) 
    {
        this.voterStatus = voterStatus;
    }

    public List<SysPropertyMeetingTopic> getTopics()
    {
        return topics;
    }

    public void setTopics(List<SysPropertyMeetingTopic> topics)
    {
        this.topics = topics;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("meetingId", getMeetingId())
            .append("meetingTitle", getMeetingTitle())
            .append("subTitle", getSubTitle())
            .append("communityId", getCommunityId())
            .append("communityName", getCommunityName())
            .append("meetingType", getMeetingType())
            .append("meetingTag", getMeetingTag())
            .append("selectionTotal", getSelectionTotal())
            .append("selectionCount", getSelectionCount())
            .append("meetingContent", getMeetingContent())
            .append("meetingTime", getMeetingTime())
            .append("meetingLocation", getMeetingLocation())
            .append("meetingStatus", getMeetingStatus())
            .append("voteStartTime", getVoteStartTime())
            .append("voteEndTime", getVoteEndTime())
            .append("totalVoters", getTotalVoters())
            .append("actualVoters", getActualVoters())
            .append("totalVotingArea", getTotalVotingArea())
            .append("participatedArea", getParticipatedArea())
            .append("votingAreaPercentage", getVotingAreaPercentage())
            .append("showParticipantCount", getShowParticipantCount())
            .append("coverImage", getCoverImage())
            .append("congduo", getCongduo())
            .append("selectedProperties", getSelectedProperties())
            .append("passStandard", getPassStandard())
            .append("normalParticipationRate", getNormalParticipationRate())
            .append("normalApprovalRate", getNormalApprovalRate())
            .append("highParticipationRate", getHighParticipationRate())
            .append("highApprovalRate", getHighApprovalRate())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("topics", getTopics())
            .toString();
    }

}
