
package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SysMeetingTopic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long topicId;

    private Long meetingId;

    private String topicTitle;

    private String topicContent;

    private Integer agreeCount;

    private Integer opposeCount;

    private Integer abstainCount;

    private String congduo;

    private Integer congduoCount;

    private List<SysMeetingTopicFile> files;

    private String filesRaw;

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
    public void setAgreeCount(Integer agreeCount)
    {
        this.agreeCount = agreeCount;
    }

    public Integer getAgreeCount()
    {
        return agreeCount;
    }
    public void setOpposeCount(Integer opposeCount)
    {
        this.opposeCount = opposeCount;
    }

    public Integer getOpposeCount()
    {
        return opposeCount;
    }
    public void setAbstainCount(Integer abstainCount)
    {
        this.abstainCount = abstainCount;
    }

    public Integer getAbstainCount()
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

    public void setCongduoCount(Integer congduoCount)
    {
        this.congduoCount = congduoCount;
    }

    public Integer getCongduoCount()
    {
        return congduoCount;
    }

    public String getFilesRaw() {
        return filesRaw;
    }

    public void setFilesRaw(String filesRaw) {
        this.filesRaw = filesRaw;
    }

    public List<SysMeetingTopicFile> getFiles() {
        if (this.files == null && StringUtils.isNotEmpty(this.filesRaw)) {
            this.files = new ArrayList<>();
            List<String> fileUrlList = Arrays.asList(this.filesRaw.split(","));
            for (String fileUrl : fileUrlList) {
                if (StringUtils.isNotEmpty(fileUrl)) {
                    SysMeetingTopicFile topicFile = new SysMeetingTopicFile();
                    topicFile.setFileUrl(fileUrl.trim());
                    // Extract file name from URL
                    String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
                    topicFile.setFileName(fileName);
                    this.files.add(topicFile);
                }
            }
        }
        return this.files;
    }

    public void setFiles(List<SysMeetingTopicFile> files) {
        this.files = files;
    }
}
