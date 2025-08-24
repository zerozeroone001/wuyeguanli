
package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SysSupervisionFaq extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String question;

    private String answerPreview;

    private String category;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getQuestion()
    {
        return question;
    }
    public void setAnswerPreview(String answerPreview)
    {
        this.answerPreview = answerPreview;
    }

    public String getAnswerPreview()
    {
        return answerPreview;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }
}
