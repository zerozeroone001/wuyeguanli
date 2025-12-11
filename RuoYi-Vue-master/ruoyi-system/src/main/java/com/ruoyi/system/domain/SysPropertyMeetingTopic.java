package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.List;

/**
 * 会议议题对象 sys_property_meeting_topic
 *
 * @author ruoyi
 * @date 2025-08-31
 */
public class SysPropertyMeetingTopic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 议题ID */
    private Long topicId;

    /** 所属会议ID */
    @Excel(name = "所属会议ID")
    private Long meetingId;

    /** 议题标题 */
    @Excel(name = "议题标题")
    private String topicTitle;

    /** 议题内容 */
    @Excel(name = "议题内容")
    private String topicContent;

    /** 附件URL列表 */
    @Excel(name = "附件URL列表")
    private String files;

    /** 头像/Logo */
    @Excel(name = "头像/Logo")
    private String avatar;

    /** 候选人ID */
    @Excel(name = "候选人ID")
    private Long candidateId;

    /** 同意票数 */
    @Excel(name = "同意票数")
    private Long agreeCount;

    /** 反对票数 */
    @Excel(name = "反对票数")
    private Long opposeCount;

    /** 弃权票数 */
    @Excel(name = "弃权票数")
    private Long abstainCount;

    /** 是否开启从多选项 */
    @Excel(name = "是否开启从多选项")
    private String congduo;

    /** 从多票数 */
    @Excel(name = "从多票数")
    private Long congduoCount;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /** 投票列表 */
    private List<SysMeetingVote> voteList;

    /** 意见反馈列表 */
    private List<SysMeetingFeedback> feedbackList;

    public void setTopicId(Long topicId)
    {
        this.topicId = topicId;
    }

    public Long getTopicId()
    {
        return topicId;
    }

    public void setMeetingId(Long meetingId)
    {
        this.meetingId = meetingId;
    }

    public Long getMeetingId()
    {
        return meetingId;
    }

    public void setTopicTitle(String topicTitle)
    {
        this.topicTitle = topicTitle;
    }

    public String getTopicTitle()
    {
        return topicTitle;
    }

    public void setTopicContent(String topicContent)
    {
        this.topicContent = topicContent;
    }

    public String getTopicContent()
    {
        return topicContent;
    }

    public void setFiles(String files)
    {
        this.files = files;
    }

    public String getFiles()
    {
        return files;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setCandidateId(Long candidateId)
    {
        this.candidateId = candidateId;
    }

    public Long getCandidateId()
    {
        return candidateId;
    }

    public void setAgreeCount(Long agreeCount)
    {
        this.agreeCount = agreeCount;
    }

    public Long getAgreeCount()
    {
        return agreeCount;
    }

    public void setOpposeCount(Long opposeCount)
    {
        this.opposeCount = opposeCount;
    }

    public Long getOpposeCount()
    {
        return opposeCount;
    }

    public void setAbstainCount(Long abstainCount)
    {
        this.abstainCount = abstainCount;
    }

    public Long getAbstainCount()
    {
        return abstainCount;
    }

    public void setCongduo(String congduo)
    {
        this.congduo = congduo;
    }

    public String getCongduo()
    {
        return congduo;
    }

    public void setCongduoCount(Long congduoCount)
    {
        this.congduoCount = congduoCount;
    }

    public Long getCongduoCount()
    {
        return congduoCount;
    }

    public void setOrderNum(Long orderNum)
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum()
    {
        return orderNum;
    }

    public List<SysMeetingVote> getVoteList() {
        return voteList;
    }

    public void setVoteList(List<SysMeetingVote> voteList) {
        this.voteList = voteList;
    }

    public List<SysMeetingFeedback> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<SysMeetingFeedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("topicId", getTopicId())
            .append("meetingId", getMeetingId())
            .append("topicTitle", getTopicTitle())
            .append("topicContent", getTopicContent())
            .append("files", getFiles())
            .append("avatar", getAvatar())
            .append("candidateId", getCandidateId())
            .append("agreeCount", getAgreeCount())
            .append("opposeCount", getOpposeCount())
            .append("abstainCount", getAbstainCount())
            .append("orderNum", getOrderNum())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("voteList", getVoteList())
            .append("feedbackList", getFeedbackList())
            .toString();
    }
}