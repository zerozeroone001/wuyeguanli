package com.ruoyi.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户投票详情视图对象
 */
public class UserVoteDetailVO {

    /** 议题标题 */
    private String topicTitle;

    /** 用户姓名 */
    private String userName;

    /** 投票选项 (0同意 1反对 2弃权) */
    private Integer voteOption;

    /** 投票时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date voteTime;

    /** 投票方式 (0小程序投票 1纸质投票 2语音投票) */
    private Integer voteType;

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(Integer voteOption) {
        this.voteOption = voteOption;
    }

    public Date getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(Date voteTime) {
        this.voteTime = voteTime;
    }

    public Integer getVoteType() {
        return voteType;
    }

    public void setVoteType(Integer voteType) {
        this.voteType = voteType;
    }
}
