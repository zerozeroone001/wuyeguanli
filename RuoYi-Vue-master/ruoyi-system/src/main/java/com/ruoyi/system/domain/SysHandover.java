
package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SysHandover extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String content;

    private String status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
}
