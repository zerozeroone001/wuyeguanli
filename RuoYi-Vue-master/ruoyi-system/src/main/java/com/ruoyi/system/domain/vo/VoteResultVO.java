package com.ruoyi.system.domain.vo;

import java.math.BigDecimal;

/**
 * 表决结果统计视图对象
 */
public class VoteResultVO {

    /** 议题ID */
    private Long topicId;

    /** 议题标题 */
    private String topicTitle;

    /** 同意人数 */
    private Integer agreePeople;

    /** 同意面积 */
    private BigDecimal agreeArea;

    /** 反对人数 */
    private Integer opposePeople;

    /** 反对面积 */
    private BigDecimal opposeArea;

    /** 弃权人数 */
    private Integer abstainPeople;

    /** 弃权面积 */
    private BigDecimal abstainArea;

    /** 未投票人数 */
    private Integer notVotedPeople;

    /** 未投票面积 */
    private BigDecimal notVotedArea;

    /** 总人数 */
    private Integer totalPeople;

    /** 总面积 */
    private BigDecimal totalArea;

    /** 实际参与人数 */
    private Integer actualPeople;

    /** 实际参与面积 */
    private BigDecimal actualArea;

    /** 人数参与率(%) */
    private BigDecimal peopleParticipationRate;

    /** 面积参与率(%) */
    private BigDecimal areaParticipationRate;

    /** 同意人数占比(%) */
    private BigDecimal agreePeopleRate;

    /** 同意面积占比(%) */
    private BigDecimal agreeAreaRate;

    /** 是否通过 */
    private Boolean isPassed;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public Integer getAgreePeople() {
        return agreePeople;
    }

    public void setAgreePeople(Integer agreePeople) {
        this.agreePeople = agreePeople;
    }

    public BigDecimal getAgreeArea() {
        return agreeArea;
    }

    public void setAgreeArea(BigDecimal agreeArea) {
        this.agreeArea = agreeArea;
    }

    public Integer getOpposePeople() {
        return opposePeople;
    }

    public void setOpposePeople(Integer opposePeople) {
        this.opposePeople = opposePeople;
    }

    public BigDecimal getOpposeArea() {
        return opposeArea;
    }

    public void setOpposeArea(BigDecimal opposeArea) {
        this.opposeArea = opposeArea;
    }

    public Integer getAbstainPeople() {
        return abstainPeople;
    }

    public void setAbstainPeople(Integer abstainPeople) {
        this.abstainPeople = abstainPeople;
    }

    public BigDecimal getAbstainArea() {
        return abstainArea;
    }

    public void setAbstainArea(BigDecimal abstainArea) {
        this.abstainArea = abstainArea;
    }

    public Integer getNotVotedPeople() {
        return notVotedPeople;
    }

    public void setNotVotedPeople(Integer notVotedPeople) {
        this.notVotedPeople = notVotedPeople;
    }

    public BigDecimal getNotVotedArea() {
        return notVotedArea;
    }

    public void setNotVotedArea(BigDecimal notVotedArea) {
        this.notVotedArea = notVotedArea;
    }

    public Integer getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(Integer totalPeople) {
        this.totalPeople = totalPeople;
    }

    public BigDecimal getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    public Integer getActualPeople() {
        return actualPeople;
    }

    public void setActualPeople(Integer actualPeople) {
        this.actualPeople = actualPeople;
    }

    public BigDecimal getActualArea() {
        return actualArea;
    }

    public void setActualArea(BigDecimal actualArea) {
        this.actualArea = actualArea;
    }

    public BigDecimal getPeopleParticipationRate() {
        return peopleParticipationRate;
    }

    public void setPeopleParticipationRate(BigDecimal peopleParticipationRate) {
        this.peopleParticipationRate = peopleParticipationRate;
    }

    public BigDecimal getAreaParticipationRate() {
        return areaParticipationRate;
    }

    public void setAreaParticipationRate(BigDecimal areaParticipationRate) {
        this.areaParticipationRate = areaParticipationRate;
    }

    public BigDecimal getAgreePeopleRate() {
        return agreePeopleRate;
    }

    public void setAgreePeopleRate(BigDecimal agreePeopleRate) {
        this.agreePeopleRate = agreePeopleRate;
    }

    public BigDecimal getAgreeAreaRate() {
        return agreeAreaRate;
    }

    public void setAgreeAreaRate(BigDecimal agreeAreaRate) {
        this.agreeAreaRate = agreeAreaRate;
    }

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean isPassed) {
        this.isPassed = isPassed;
    }
}
